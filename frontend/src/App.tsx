import { useEffect, useState } from 'react'

import { Card, CardContent, CardHeader, CardTitle } from './components/ui/card'
import { ColumnDef } from '@tanstack/react-table'
import FileTable from '@/components/file-table'

import { ValueNoneIcon, CheckCircledIcon, CrossCircledIcon, Half2Icon } from '@radix-ui/react-icons'

/*enum FileStatus {
  NOT_STARTED,
  STARTED,
  FAILED
}*/

type File = {
  fileName: string,
  path: string,
  extracted: boolean,
  status: string
}

const columns: ColumnDef<File>[] = [
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
  }
]

function App() {
  const [data, setData] = useState([])

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
    </>
  )
}

export default App
