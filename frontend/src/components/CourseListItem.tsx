import {Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import {CSSProperties} from "styled-components";
import Course from "../models/Course";

export interface CourseListItemProps {
  course: Course
  style?: CSSProperties
}

export default function CourseListItem({course, style}: CourseListItemProps) {

  const navigate = useNavigate()

  const handleOnClick = () => {
    navigate(`/course/${course.id}`)
  }

  return (
    <div style={style} onClick={handleOnClick}>
      <Typography>{course.name}</Typography>
    </div>
  )
}
