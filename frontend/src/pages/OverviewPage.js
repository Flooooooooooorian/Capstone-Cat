import React from "react";
import {DataGrid} from '@mui/x-data-grid';
import useCapstones from "../useCapstones";
import {Typography} from "@mui/material";
import styled from "styled-components";

export default function OverviewPage() {
    const {capstones} = useCapstones()

    const renderCapstoneRepoCell = (props) => {
        return (
            <RepoLink onClick={(event) => {
                window.open(event.target.textContent, '_blank')
            }}>{props.formattedValue}</RepoLink>
        )
    }

    const renderBadge = (props) => {
        return (
            <img alt={props.formattedValue} src={props.formattedValue} onClick={(event) => {
                console.log(event)
                window.open(event.target.src, '_blank')
            }}/>
        )
    }

    const renderCommits = (props) => {
        return (
            <Typography>
                {props.row.mainCommits}({props.row.allCommits})
            </Typography>
        )
    }

    const renderPulls = (props) => {
        return (
            <Typography>
                {props.row.openPulls}({props.row.allPulls})
            </Typography>
        )
    }

    const columns = [
        {field: 'studentName', headerName: 'Student', width: 150},
        {field: 'url', headerName: 'Capstone', width: 150, renderCell: renderCapstoneRepoCell},
        {field: 'updatedAt', headerName: 'Last Commit', width: 170},
        {field: 'mainCommits', headerName: 'Commits', width: 140, renderCell: renderCommits},
        {field: 'openPulls', headerName: "PR's", width: 140, renderCell: renderPulls},
        {field: 'coverageBadgeUrl', headerName: 'Coverage', width: 150, renderCell: renderBadge},
        {field: 'qualityBadgeUrl', headerName: 'Quality Gate', width: 150, renderCell: renderBadge},
    ];


    return (
        <div style={{height: 800, width: '100%'}}>
            <DataGrid rows={capstones} columns={columns}/>
        </div>
    )
}

const RepoLink = styled(Typography)`
  text-decoration: blue underline;
`
