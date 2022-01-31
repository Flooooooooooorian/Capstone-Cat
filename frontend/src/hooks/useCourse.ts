import {useCallback, useState} from "react";
import axios from "axios";
import Course from "../models/Course";

export interface useCourseResult {
  course: Course
  loadCapstones: (courseId: NonNullable<Course['id']>) => void
  refreshCapstonesById: (courseId: string, capstoneId: string) => Promise<void>
}

export default function useCourse(): useCourseResult {
  const [course, setCourse] = useState<Course>({name: "", capstones: []})

  const loadCapstones: (courseId: string) => void = useCallback((courseId: string) => {
    axios.get(`/api/course/${courseId}`)
      .then(response => response.data)
      .then(setCourse)
      .catch(console.error)
  }, [])

  const refreshCapstonesById = (courseId: string, capstoneId: string) => {
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
