import {Typography, useTheme} from "@mui/material";
import React from "react";

export interface CommitCountCellProps {
    mainCommits: number
    allCommits: number
}

export default function CommitCountCell({mainCommits, allCommits}: CommitCountCellProps) {
    const theme = useTheme()
    return (
        <Typography color={theme.palette.text.primary}>
            {mainCommits}({allCommits})
        </Typography>
    )
}
