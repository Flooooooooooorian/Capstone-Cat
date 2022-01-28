import {useCallback, useState} from "react";
import axios from "axios";

export default function useCourse() {
    const [course, setCourse] = useState({capstones: []})

    const loadCapstones = useCallback((courseId) => {
        axios.get(`/api/course/${courseId}`)
            .then(response => response.data)
            .then(setCourse)
            .catch(console.error)
    }, [])

    const refreshCapstonesById = (courseId, capstoneId) => {
        return axios.get(`/api/course/${courseId}/capstones/${capstoneId}/refresh`)
            .then(response => response.data)
            .then((data) => {
                setCourse((currentState) => {
                    const newCapstones = currentState.capstones.map((capstone) => capstone.id === capstoneId ? data : capstone)
                    return {...currentState, capstones: newCapstones}
                })
            })
            .catch(console.error)
    }

    return {course, loadCapstones, refreshCapstonesById}

}
