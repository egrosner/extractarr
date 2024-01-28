import { useEffect, useState } from 'react'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from './components/ui/table'
import { ColumnDef, flexRender, getCoreRowModel, useReactTable } from '@tanstack/react-table'
import { Card, CardContent, CardHeader, CardTitle } from './components/ui/card'

type File = {
  fileName: string
}

const columns: ColumnDef<File>[] = [
  {
    accessorKey: "fileName",
    header: "File Name",
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

  console.log(JSON.stringify(data))

  const table = useReactTable({
    columns,
    data,
    getCoreRowModel: getCoreRowModel() 
  })

  return (
    <>
      <Card className='container mx-auto max-w-screen-xl my-16'>
        <CardHeader>
          <CardTitle>Extractarr</CardTitle>
        </CardHeader>
        <CardContent>
          <h1>Processed Files:</h1>
          <Table>
            <TableHeader>
              {table.getHeaderGroups().map((headerGroup) => (
                <TableRow key={headerGroup.id}>
                  {headerGroup.headers.map((header) => {
                    return (
                      <TableHead key={header.id}>
                        {header.isPlaceholder ? null : flexRender(header.column.columnDef.header, header.getContext())}
                      </TableHead>
                    )
                  })}
                </TableRow>
              ))}
            </TableHeader>
            <TableBody>
              {table.getRowModel().rows?.length ? (
                table.getRowModel().rows.map((row) => (
                  <TableRow
                    key={row.id}
                    data-state={row.getIsSelected() && "selected"}
                  >
                    {row.getVisibleCells().map((cell) => (
                      <TableCell key={cell.id}>
                        {flexRender(cell.column.columnDef.cell, cell.getContext())}
                      </TableCell>
                    ))}
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={columns.length} className="h-24 text-center">
                    No results.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </>
  )
}

export default App
