import {Typography, useTheme} from "@mui/material";
import React from "react";


export interface TextHeaderProps {
    text: string
}

export default function TextHeader({text}: TextHeaderProps) {
    const theme = useTheme()

    return (
        <Typography color={theme.palette.text.primary}>
            {text}
        </Typography>
    )
}
