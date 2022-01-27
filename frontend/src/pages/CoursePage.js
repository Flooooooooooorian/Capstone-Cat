import React, {useEffect, useState} from "react";
import {DataGrid} from '@mui/x-data-grid';
import useCourse from "../useCourse";
import {Typography} from "@mui/material";
import styled from "styled-components";
import CachedIcon from '@mui/icons-material/Cached';
import {LoadingButton} from "@mui/lab";
import {useParams} from "react-router-dom";

export default function CoursePage() {

    const {courseId} = useParams()

    const [loading, setLoading] = useState({})
    const {course, loadCapstones, refreshCapstonesById} = useCourse()

    console.log(course)

    useEffect(() => {
        loadCapstones(courseId)
    }, [])

    const handleLoadCapstoneBy = (id) => {
        setLoading((currentState) => {
            return {...currentState, [id]: true}
        })
        refreshCapstonesById(courseId, id)
            .then(() => {
                setLoading((currentState) => {
                    return {...currentState, [id]: false}
                })
            })
    }

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

    const renderRefreshButton = (props) => {
        return (
            <LoadingButton loading={loading[props.row.id]} variant="outlined" onClick={() => {handleLoadCapstoneBy(props.row.id)}}>
                <CachedIcon />
            </LoadingButton>

        )
    }

    const renderRefreshHeader = () => {
        const handleOnClick = () => {
            course.capstones.forEach((capstone) => {
                handleLoadCapstoneBy(capstone.id)
            })
        }

        return (
            <LoadingButton variant="outlined" onClick={handleOnClick}>
                <CachedIcon />
            </LoadingButton>
        )
    }

    const columns = [
        {field: 'studentName', headerName: 'Student', width: 150},
        {field: 'url', headerName: 'Capstone', width: 150, renderCell: renderCapstoneRepoCell},
        {field: 'updatedDefaultAt', headerName: 'Last Main Commit', width: 170},
        {field: 'updatedAt', headerName: 'Last Commit', width: 170},
        {field: 'mainCommits', headerName: 'Commits', width: 140, renderCell: renderCommits},
        {field: 'openPulls', headerName: "PR's", width: 140, renderCell: renderPulls},
        {field: 'coverageBadgeUrl', headerName: 'Coverage', width: 150, renderCell: renderBadge},
        {field: 'qualityBadgeUrl', headerName: 'Quality Gate', width: 150, renderCell: renderBadge},
        {field: '', sortable: false, disableColumnMenu: true, renderHeader: renderRefreshHeader, headerName: "", width:100, renderCell: renderRefreshButton}
    ];


    return (
        <div style={{height: 800, width: '100%'}}>
            <Typography variant={"h6"}>{course?.name}</Typography>
            <DataGrid rows={course?.capstones} columns={columns}/>
        </div>
    )
}

const RepoLink = styled(Typography)`
  text-decoration: blue underline;
`