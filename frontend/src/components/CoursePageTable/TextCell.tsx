import {Typography, useTheme} from "@mui/material";
import React from "react";

export interface StudentNameCellProps {
    text: string
}

export default function TextCell({text}: StudentNameCellProps) {
    const theme = useTheme()

    return (
        <Typography color={theme.palette.text.primary}>
            {text}
        </Typography>
    )
}
