import {LoadingButton} from "@mui/lab";
import CachedIcon from "@mui/icons-material/Cached";
import React from "react";
import Course from "../../models/Course";

export interface RefreshHeaderProps {
    course: Course
    refreshCapstoneById: (id: string) => void
}

export default function RefreshHeader({course, refreshCapstoneById}: RefreshHeaderProps) {

    const handleOnClick = () => {
        course.capstones.forEach((capstone) => {
            refreshCapstoneById(capstone.id)
        })
    }

    return (
        <LoadingButton variant="outlined" onClick={handleOnClick}>
            <CachedIcon color={'primary'}/>
        </LoadingButton>
    )
}
