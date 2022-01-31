import React, {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import AddCapstone from "../components/AddCapstone";
import {
  Button,
  Card,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow
} from "@mui/material";
import {CapstoneRequest} from "../models/Capstone";


function CapstoneInputTable({capstones}: { capstones: CapstoneRequest[] }) {
  return (
    <TableContainer component={Paper}>
      <Table size="small">
        <TableHead>
          <TableRow>
            <TableCell>Name</TableCell>
            <TableCell>GitHub URL</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {
            capstones.map((capstone, index) => (
              <TableRow key={index}>
                <TableCell>{capstone.studentName}</TableCell>
                <TableCell>{capstone.githubApiUrl}</TableCell>
              </TableRow>
            ))
          }
        </TableBody>
      </Table>
    </TableContainer>
  )
}

export default function AddCoursePage() {
  const [name, setName] = useState("")
  const [capstones, setCapstones] = useState<CapstoneRequest[]>([])
  const [showError, setShowError] = useState<boolean>(false)

  const navigate = useNavigate()

  const handleSubmit: React.FormEventHandler<HTMLFormElement> = (event) => {
    event.preventDefault()

    if (name && capstones && capstones.length) {

      axios.post("/api/course", {name: name, capstones: capstones})
        .then(() => {
          navigate("/")
        })
        .catch(console.error)
    } else {
      setShowError(true)
    }
  }

  const addCapstone = (capstone: CapstoneRequest) => {
    setCapstones((currents) => [...currents, capstone])
  }


  return (
    <>
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

      <Card>
        <form onSubmit={handleSubmit}>
          <input placeholder={"Name"} type={"text"} value={name}
                 onChange={(event) => setName(event.target.value)}/>
          <AddCapstone addCapstone={addCapstone}/>
          {!!capstones.length && <CapstoneInputTable capstones={capstones}/>}
          <Button variant={"outlined"} type={"submit"}>Save</Button>
        </form>
      </Card>
    </>
  )

}
