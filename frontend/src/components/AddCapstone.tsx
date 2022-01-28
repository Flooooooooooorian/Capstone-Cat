import {useState} from "react";
import {Button, Card, Typography} from "@mui/material";
import styled from "styled-components";
import {CapstoneRequest} from "../models/Capstone";

export interface AddCapstoneProps {
  addCapstone: (capstone: CapstoneRequest) => void
}

export default function AddCapstone({addCapstone}: AddCapstoneProps) {

  const [githubApiUrl, setGithubApiUrl] = useState("")
  const [studentName, setStudentName] = useState("")

  const handleSubmit = () => {
    addCapstone({studentName, githubApiUrl})
    setGithubApiUrl("")
    setStudentName("")
  }

  return (
    <StyledCard>
      <Typography>Add Capstone</Typography>
      <input type={"text"} placeholder={"Name"} value={studentName}
             onChange={(event => setStudentName(event.target.value))}/>
      <input type={"text"} placeholder={"Github Api Url"} value={githubApiUrl}
             onChange={(event => setGithubApiUrl(event.target.value))}/>
      <Button variant={"outlined"} onClick={handleSubmit}>Add</Button>
    </StyledCard>
  )
}

const StyledCard = styled(Card)`
    display: flex
    flex-direction: row
`
