import {useCallback, useEffect, useState} from "react";
import axios from "axios";
import Course from "../models/Course";
import {CapstoneRequest} from "../models/Capstone";

export interface useCoursesResult {
    courses: Course[]
    loadCapstones: (courseId: NonNullable<Course['id']>) => void
    refreshCapstonesById: (courseId: string, capstoneId: string) => Promise<void>
    addCourse: (courseName: string, capstones: CapstoneRequest[]) => Promise<void>
}

export default function useCourses(): useCoursesResult {
    const [courses, setCourses] = useState<Course[]>([])


    useEffect(
        () => {
            axios.get("/api/course")
                .then(response => response.data)
                .then(setCourses)
                .catch(console.error)
        }, []
    )

    const loadCapstones: (courseId: string) => void = useCallback((courseId: string) => {
        axios.get(`/api/course/${courseId}`)
            .then(response => response.data)
            .then((data) => {
                setCourses((currentCourses) => {
                    return currentCourses.map((course) => course.id === courseId ? {
                        ...course,
                        capstones: data
                    } : course)
                })
            })
            .catch(console.error)
    }, [])

    const refreshCapstoneById = (courseId: string, capstoneId: string) => {
        return axios.get(`/api/course/${courseId}/capstones/${capstoneId}/refresh`)
            .then(response => response.data)
            .then((data) => {
                setCourses((currentState) => {
                    return currentState.map((course => {
                        if (course.id === courseId) {
                            return {
                                ...course,
                                capstones: course.capstones.map((capstone) => capstone.id === capstoneId ? data : capstone)
                            }
                        } else {
                            return course
                        }
                    }))
                })
            })
            .catch(console.error)
    }

    const addCourse = (courseName: string, capstones: CapstoneRequest[]) => {
        return axios.post("/api/course", {name: courseName, capstones: capstones})
            .then((response) => response.data)
            .then(data => setCourses(currentState => [...currentState, data]))
            .catch(console.error)
    }

    return {courses, loadCapstones, refreshCapstonesById: refreshCapstoneById, addCourse}

}
