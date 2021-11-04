import {useEffect, useState} from "react";
import axios from "axios";

export default function useCapstones() {
    const [capstones, setCapstones] = useState([])

    useEffect(() => {
        loadCapstones()
    }, [])

    const loadCapstones = () => {
        axios.get("/api/capstones")
            .then(response => response.data)
            .then(setCapstones)
            .catch(console.error)
    }

    return {capstones}

}