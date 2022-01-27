import {useState} from "react";
import {Button, Card, Typography} from "@mui/material";
import styled from "styled-components";

export default function AddCapstone({addCapstone}) {

    const[githubApiUrl, setgithubApiUrl] = useState("")
    const [name, setName] = useState("")

    const handleSubmit = () => {
        addCapstone({name, githubApiUrl})
        setgithubApiUrl("")
        setName("")
    }

    return(
        <StyledCard>
            <Typography>Add Capstone</Typography>
            <input type={"text"} placeholder={"Name"} value={name} onChange={(event => setName(event.target.value))}/>
            <input type={"text"} placeholder={"Github Api Url"} value={githubApiUrl} onChange={(event =>  setgithubApiUrl(event.target.value))}/>
            <Button variant={"outlined"} onClick={handleSubmit}>Add</Button>
        </StyledCard>
    )
}

const StyledCard = styled(Card)`
    display: flex
    flex-direction: row
`
