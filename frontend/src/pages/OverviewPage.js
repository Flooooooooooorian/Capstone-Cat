import React from "react";
import { DataGrid } from '@mui/x-data-grid';

export default function OverviewPage() {
    const rows = [
        { id: 1, studentName: 'Hello', capstoneLink: 'https://google.com/' , lastCommitOn: '2021-11-01T12:32:16Z'},
        { id: 2, studentName: 'DataGridPro', capstoneLink: 'https://github.com/' , lastCommitOn: '2021-11-01T12:32:16Z'},
        { id: 3, studentName: 'MUI', capstoneLink: 'https://google.com/' , lastCommitOn: '2021-11-01T12:32:16Z'},
    ];

    const renderCapstoneRepoCell = (props) => {
        console.log(props)
        return (
            <p onClick={(event) => {
                console.log(event)
                window.open(event.target.textContent, '_blank')}}>{props.formattedValue}</p>
        )
    }

    const columns = [
        { field: 'studentName', headerName: 'Student', width: 150 },
        { field: 'capstoneLink', headerName: 'Capstone', width: 150 , renderCell: renderCapstoneRepoCell },
        { field: 'lastCommitOn', headerName: 'Last Commit', width: 170 },
    ];



    return (
        <div style={{ height: 300, width: '100%' }}>
            <DataGrid rows={rows} columns={columns} />
        </div>
    )
}