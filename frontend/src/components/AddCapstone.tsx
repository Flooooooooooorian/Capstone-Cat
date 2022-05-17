import {IconButton, Typography} from "@mui/material";
import styled from "styled-components";
import {CapstoneRequest} from "../models/Capstone";
import DeleteIcon from "@mui/icons-material/Delete"

export interface AddCapstoneProps {
    onChange: (url: string, index: number) => void
    remove: (index: number) => void
    index: number
    capstone: CapstoneRequest
}

export default function AddCapstone({capstone, onChange, remove, index}: AddCapstoneProps) {

    return (
        <Wrapper>
            <Typography>{index+1}.</Typography>
            <input type={"text"} placeholder={"Github Repository Url"} value={capstone.githubApiUrl}
                   onChange={(event => onChange(event.target.value, index))}/>
            <IconButton onClick={() => remove(index)}>
                <DeleteIcon/>
            </IconButton>
        </Wrapper>
    )
}

const Wrapper = styled.div`
  margin: 6px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
`
