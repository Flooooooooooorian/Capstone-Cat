import React, {useState} from "react";
import {DataGrid, GridColumns} from '@mui/x-data-grid';
import {Typography} from "@mui/material";
import {useParams} from "react-router-dom";
import Course from "../models/Course";
import RefreshHeader from "../components/CoursePageTable/RefreshHeader";
import RepoLinkCell from "../components/CoursePageTable/RepoLinkCell";
import CommitCountCell from "../components/CoursePageTable/CommitCountCell";
import PullRequestCountCell from "../components/CoursePageTable/PullRequestCountCell";
import RefreshButtonCell from "../components/CoursePageTable/RefreshButtonCell";
import BadgeCell from "../components/CoursePageTable/BadgeCell";
import TextCell from "../components/CoursePageTable/TextCell";
import TextHeader from "../components/CoursePageTable/TextHeader";


export interface CoursePageProps {
    courses: Course[]
    refreshCapstoneById: (courseId: string, id: string) => Promise<void>
}

export default function CoursePage({courses, refreshCapstoneById}: CoursePageProps) {

    const {courseId} = useParams()
    const [loading, setLoading] = useState<{ [key: string]: boolean }>({})

    if (!courseId) {
        return <div>Please enter a course ID</div>
    }

    const course = courses.find(c => c.id === courseId)

    if (!course) {
        return <div>Please enter a valid course ID</div>
    }



    const renderRefreshButton = (props: any) => <RefreshButtonCell id={props.row.id}
                                                                   refreshCapstoneById={handleRefreshCapstoneById}
                                                                   loading={loading[props.row.id]}/>

    const renderRefreshHeader = () => <RefreshHeader course={course} refreshCapstoneById={handleRefreshCapstoneById}/>

    const renderCapstoneRepoCell = (props: any) => <RepoLinkCell url={props.formattedValue}/>

    const renderBadge = (props: any) => <BadgeCell url={props.formattedValue}/>

    const renderCommits = (props: any) => <CommitCountCell mainCommits={props.row.mainCommits}
                                                           allCommits={props.row.allCommits}/>

    const renderPulls = (props: any) => <PullRequestCountCell openPullRequests={props.row.openPulls}
                                                              allPullRequests={props.row.allPulls}/>

    const renderTextCell = (props: any) => <TextCell text={props.row.studentName}/>

    const renderTextHeader = (props: any) => <TextHeader text={props.field} />

    const handleRefreshCapstoneById = (id: string) => {
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
        {field: 'studentName', headerName: 'Student', width: 150, renderCell: renderTextCell, renderHeader: renderTextHeader},
        {field: 'url', headerName: 'Capstone', width: 150, renderCell: renderCapstoneRepoCell, renderHeader: renderTextHeader},
        {field: 'updatedDefaultAt', headerName: 'Last Main Commit', width: 170, renderCell: renderTextCell, renderHeader: renderTextHeader},
        {field: 'updatedAt', headerName: 'Last Commit', width: 170, renderCell: renderTextCell, renderHeader: renderTextHeader},
        {field: 'mainCommits', headerName: 'Commits', width: 140, renderCell: renderCommits, renderHeader: renderTextHeader},
        {field: 'openPulls', headerName: "PR's", width: 140, renderCell: renderPulls, renderHeader: renderTextHeader},
        {field: 'coverageBadgeUrl', headerName: 'Coverage', width: 150, renderCell: renderBadge, renderHeader: renderTextHeader},
        {field: 'qualityBadgeUrl', headerName: 'Quality Gate', width: 150, renderCell: renderBadge, renderHeader: renderTextHeader},
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
