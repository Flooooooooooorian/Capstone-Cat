import {Typography} from "@mui/material";
import { useNavigate } from "react-router-dom";


export default function CourseListItem({course, style}) {

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
