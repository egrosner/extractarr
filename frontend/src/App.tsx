import { useEffect, useState } from 'react'

import { Card, CardContent, CardHeader, CardTitle } from './components/ui/card'
import { ColumnDef } from '@tanstack/react-table'
import FileTable from '@/components/file-table'

import { ValueNoneIcon, CheckCircledIcon, CrossCircledIcon, Half2Icon, ReloadIcon } from '@radix-ui/react-icons'
import { Button } from './components/ui/button'
import { useToast } from './components/ui/use-toast'
import { Toaster } from './components/ui/toaster'

/*enum FileStatus {
  NOT_STARTED,
  STARTED,
  FAILED
}*/

type File = {
  id: number,
  fileName: string,
  path: string,
  extracted: boolean,
  status: string
}

function App() {
  const [data, setData] = useState([])

  const { toast } = useToast()

  const retryFile = async (id: Number) => {
    const result = await fetch(`/api/rarfiles/${id}/retry`)
    const resultJson = await result.json()
    if(resultJson) {
      toast({
        description: 'Retry Success!'
      })
    }
    else {
      toast({
        description: 'Retry Failed..'
      })
    }
  }
  
  const columns: ColumnDef<File>[] = [
    {
      accessorKey: "id",
      header: "Id",
    },
    {
      accessorKey: "fileName",
      header: "File Name",
    },
    {
      accessorKey: "path",
      header: "Path",
    },
    {
      accessorKey: "status",
      header: "Status",
      cell: ({ row }) => {
        const status: string = row.getValue("status")
  
        let icon = <ValueNoneIcon />
        switch(status) {
          case "None":
            icon = <ValueNoneIcon />
            break
          case "Success":
            icon = <CheckCircledIcon />
            break
          case "Failure":
            icon = <CrossCircledIcon />
            break
          case "Started":
            icon = <Half2Icon />
            break
        }
        return (
          <>
          <div className='flex items-center'>
            <div className='mr-2'>
              {icon}
            </div>
            {status}
            </div>
          </>
        )
      }
    },
    {
      header: 'Retry',
      cell: ({ row,  }) => {
        const id: Number = row.getValue("id")
        return (
          <>
            <Button variant="outline" size="icon" onClick={() => retryFile(id)}>
              <ReloadIcon />
            </Button>
          </>
        )
      }
    }
  ]

  useEffect(() => {
    const fetchData = async () => {
      const liveData = await fetch('/api/rarfiles')
      const liveDataJson = await liveData.json()
      setData(liveDataJson)
    }
    fetchData()
  }, [])

  return (
    <>
      <Card className='container mx-auto max-w-screen-xl my-16'>
        <CardHeader>
          <CardTitle>Extractarr</CardTitle>
        </CardHeader>
        <CardContent>
          <Card>
            <CardHeader>
              <CardTitle>Processed Files</CardTitle>
            </CardHeader>
            <CardContent>
              <FileTable columns={columns} data={data} />
            </CardContent>
          </Card>
        </CardContent>
      </Card>
      <Toaster />
    </>
  )
}

export default App
