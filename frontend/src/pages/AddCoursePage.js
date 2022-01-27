import {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";
import AddCapstone from "../components/AddCapstone";
import {Button, Card, Typography} from "@mui/material";


export default function AddCoursePage() {
    const [name, setName] = useState("")
    const [capstones, setCapstones] = useState([])

    const navigate = useNavigate()

    const handleSubmit = (event) => {
        event.preventDefault()

        axios.post("/api/course", {name: name, capstones: capstones})
            .then(() => {
                navigate("/")
            })
            .catch(console.error)
    }

    const addCapstone = (capstone) => {
        setCapstones((currents) => [...currents, capstone])
    }


    return (
        <Card>
            <form onSubmit={handleSubmit}>
                <input placeholder={"Name"} type={"text"} value={name}
                       onChange={(event) => setName(event.target.value)}/>
                <Typography>Capstones</Typography>
                {capstones.map((capstone, index) => {
                    return (
                        <Typography key={index}>{capstone.name}</Typography>
                    )
                })}
                <AddCapstone addCapstone={addCapstone}/>
                <Button variant={"outlined"} type={"submit"}>Save</Button>
            </form>
        </Card>
    )

}
