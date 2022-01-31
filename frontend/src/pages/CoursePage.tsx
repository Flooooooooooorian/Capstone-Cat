import React, {useState} from "react";
import {DataGrid, GridColumns} from '@mui/x-data-grid';
import {Typography} from "@mui/material";
import styled from "styled-components";
import CachedIcon from '@mui/icons-material/Cached';
import {LoadingButton} from "@mui/lab";
import {useParams} from "react-router-dom";
import Course from "../models/Course";


export interface CoursePageProps {
  courses: Course[]
  refreshCapstoneById: (courseId: string, id: string) => Promise<void>
}

export default function CoursePage({courses, refreshCapstoneById}: CoursePageProps ) {

  const {courseId} = useParams()

  const [loading, setLoading] = useState<{ [key: string]: boolean }>({})

  if (!courseId) {
    return <div>Please enter a course ID</div>
  }

  const course = courses.find(c => c.id === courseId)

  if (!course) {
    return <div>Please enter a valid course ID</div>
  }

  const renderRefreshButton = (props: any) => {
    return (
      <LoadingButton loading={loading[props.row.id as string]} variant="outlined" onClick={() => {
        handleLoadCapstoneBy(props.row.id)
      }}>
        <CachedIcon/>
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
        <CachedIcon/>
      </LoadingButton>
    )
  }

  const renderCapstoneRepoCell = (props: any) => {
    return (
      <RepoLink onClick={(event: React.MouseEvent<HTMLElement>) => {
        const target = event.target as typeof event.target & { textContent: string }
        window.open(target.textContent, '_blank')
      }}>{props.formattedValue}</RepoLink>
    )
  }

  const renderBadge = (props: any) => {
    return (
      <img alt={props.formattedValue} src={props.formattedValue} onClick={(event) => {
        const target = event.target as typeof event.target & { src: string }
        window.open(target.src, '_blank')
      }}/>
    )
  }

  const renderCommits = (props: any) => {
    return (
      <Typography>
        {props.row.mainCommits}({props.row.allCommits})
      </Typography>
    )
  }

  const renderPulls = (props: any) => {
    return (
      <Typography>
        {props.row.openPulls}({props.row.allPulls})
      </Typography>
    )
  }

  const handleLoadCapstoneBy = (id: string) => {
    setLoading((currentState) => {
      return {...currentState, [id]: true}
    })
    refreshCapstoneById(courseId, id)
      .then(() => {
        setLoading((currentState) => {
          return {...currentState, [id]: false}
        })
      })
  }

  const columns: GridColumns = [
    {field: 'studentName', headerName: 'Student', width: 150},
    {field: 'url', headerName: 'Capstone', width: 150, renderCell: renderCapstoneRepoCell},
    {field: 'updatedDefaultAt', headerName: 'Last Main Commit', width: 170},
    {field: 'updatedAt', headerName: 'Last Commit', width: 170},
    {field: 'mainCommits', headerName: 'Commits', width: 140, renderCell: renderCommits},
    {field: 'openPulls', headerName: "PR's", width: 140, renderCell: renderPulls},
    {field: 'coverageBadgeUrl', headerName: 'Coverage', width: 150, renderCell: renderBadge},
    {field: 'qualityBadgeUrl', headerName: 'Quality Gate', width: 150, renderCell: renderBadge},
    {
      field: '',
      sortable: false,
      disableColumnMenu: true,
      renderHeader: renderRefreshHeader,
      headerName: "",
      width: 100,
      renderCell: renderRefreshButton
    }
  ];

  return (
    <div style={{display: "flex", flexDirection: "column", width: '100%', height: 800}}>
      <Typography variant={"h6"}>{course?.name}</Typography>
      <DataGrid rows={course?.capstones} columns={columns}/>
    </div>
  )
}

const RepoLink = styled(Typography)`
  text-decoration: blue underline;
`
