import React, {useState} from "react";
import {useNavigate} from "react-router-dom";
import AddCapstone from "../components/AddCapstone";
import {
    Button,
    Card,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    IconButton,
    Typography
} from "@mui/material";
import {CapstoneRequest} from "../models/Capstone";
import styled from "styled-components";
import AddIcon from "@mui/icons-material/Add"

export interface AddCoursePageProps {
    addCourse: (courseName: string, capstones: CapstoneRequest[]) => Promise<void>
}

export default function AddCoursePage({addCourse}: AddCoursePageProps) {
    const [courseName, setCourseName] = useState("")
    const [capstones, setCapstones] = useState<CapstoneRequest[]>([{githubApiUrl: ""}])
    const [showError, setShowError] = useState<boolean>(false)

    const navigate = useNavigate()

    const handleSubmit: React.FormEventHandler<HTMLFormElement> = (event) => {
        event.preventDefault()

        if (courseName && capstones && capstones.length) {
            addCourse(courseName, capstones).then(() => navigate("/"))
        } else {
            setShowError(true)
        }
    }

    const addCapstoneRow = () => {
        setCapstones((currentState) => [...currentState, {githubApiUrl: ""}])
    }

    const onCapstoneChange = (url: string, index: number) => {
        setCapstones((currents) => currents.map((item, i) => i === index ? {githubApiUrl: url} : item))
    }

    const removeCapstone = (index: number) => {
        setCapstones((currentState) => currentState.filter((item, i) => i !== index))
    }

    return (
        <Wrapper>
            <Dialog
                open={showError}
                onClose={() => setShowError(false)}
            >
                <DialogContent>
                    <DialogContentText id="alert-dialog-text">
                        Illegal course setup.
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setShowError(false)} autoFocus>Ok</Button>
                </DialogActions>
            </Dialog>

            <CardStyled>
                <FormStyled onSubmit={handleSubmit}>
                    <Typography>Course Name:</Typography>
                    <input placeholder={"Name"} type={"text"} value={courseName}
                           onChange={(event) => setCourseName(event.target.value)}/>

                    {capstones.map((capstone, index) => <AddCapstone key={index}
                                                                     capstone={capstone}
                                                                     index={index}
                                                                     onChange={onCapstoneChange}
                                                                     remove={removeCapstone}/>)}

                    <AddButtonStyled onClick={addCapstoneRow}>
                        <AddIcon/>
                    </AddButtonStyled>
                    <SaveButtonStyled variant={"outlined"} type={"submit"}>Save</SaveButtonStyled>
                </FormStyled>
            </CardStyled>
        </Wrapper>
    )

}

const CardStyled = styled(Card)`
  width: 50%;
`

const AddButtonStyled = styled(IconButton)`
  margin: 12px;
`

const SaveButtonStyled = styled(Button)`
  justify-self: flex-end;
  margin: 12px;
`

const FormStyled = styled.form`
  margin: 12px;
  display: flex;
  flex-direction: column;
`

const Wrapper = styled.div`
  display: flex;
  justify-content: center;
  margin: 12px;
`
