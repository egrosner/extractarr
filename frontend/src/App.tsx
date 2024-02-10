import { useEffect, useState } from 'react'

import { Card, CardContent, CardHeader, CardTitle } from './components/ui/card'
import { ColumnDef } from '@tanstack/react-table'
import FileTable from '@/components/file-table'

type File = {
  fileName: string,
  path: string,
  extracted: boolean
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
    accessorKey: "extracted",
    header: "Extracted",
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
              <h1>Processed Files:</h1>
              <FileTable columns={columns} data={data} />
            </CardContent>
          </Card>
        </CardContent>
      </Card>
    </>
  )
}

export default App
