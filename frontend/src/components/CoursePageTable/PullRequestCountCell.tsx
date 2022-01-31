import {Typography, useTheme} from "@mui/material";
import React from "react";

export interface PullRequestCountCellProps {
    openPullRequests: number
    allPullRequests: number
}

export default function PullRequestCountCell({openPullRequests, allPullRequests}: PullRequestCountCellProps) {
    const theme = useTheme()
    return (
        <Typography color={theme.palette.text.primary}>
            {openPullRequests}({allPullRequests})
        </Typography>
    )
}
