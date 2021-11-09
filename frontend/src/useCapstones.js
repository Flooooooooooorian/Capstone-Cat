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

    const refreshCapstonesById = (id) => {
        return axios.get("/api/capstones/refreshed/" + id)
            .then(response => response.data)
            .then((data) => {
                setCapstones((state) => {
                    return [...state.filter((capstone) => capstone.id !== id), data]
                })
            })
            .catch(console.error)
    }

    return {capstones, refreshCapstonesById}

}