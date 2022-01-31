import React from "react";
import styled from "styled-components";
import {Typography, useTheme} from "@mui/material";


export interface RepoLinkCellProps {
    url: string
}

export default function RepoLinkCell({url}: RepoLinkCellProps) {
    const theme = useTheme()

    const handleOnClick = (event: React.MouseEvent<HTMLElement>) => {
        const target = event.target as typeof event.target & { textContent: string }
        window.open(target.textContent, '_blank')
    }

    return (
        <RepoLink color={theme.palette.text.primary} onClick={handleOnClick}>
            {url}
        </RepoLink>
    )
}

const RepoLink = styled(Typography)`
  text-decoration: white underline;
`
