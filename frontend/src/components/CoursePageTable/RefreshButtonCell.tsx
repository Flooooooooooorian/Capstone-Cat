import {LoadingButton} from "@mui/lab";
import CachedIcon from "@mui/icons-material/Cached";
import React from "react";

export interface RefreshButtonCellProps {
    id: string
    refreshCapstoneById: (id: string) => void
    loading: boolean
}

export default function RefreshButtonCell({id, loading, refreshCapstoneById}: RefreshButtonCellProps) {
    return (
        <LoadingButton loading={loading} variant="outlined" onClick={() => {
            refreshCapstoneById(id)
        }}>
            <CachedIcon color={'primary'}/>
        </LoadingButton>
    )
}
