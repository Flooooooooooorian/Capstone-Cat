import React, {useEffect, useState} from "react";
import CourseListItem from "../components/CourseListItem";
import axios from "axios";
import {Button, Card, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import styled from "styled-components";

export default function HomePage() {

    const [courses, setCourses] = useState([])
    const navigation = useNavigate()

    useEffect(
        () => {
            axios.get("/api/course")
                .then(response => response.data)
                .then(setCourses)
                .catch(console.error)
        }, []
    )

    return (
        <StyledCard>
            <StyledWrapper>
                <Typography variant={"h5"}>Courses</Typography>
                <Button variant={"outlined"} onClick={() => navigation("/course/create")}>Add</Button>
            </StyledWrapper>
            {
                courses.map((course) => {
                    return (
                        <StyledCourseListItem key={course.id} course={course}/>
                    )
                })
            }
        </StyledCard>
    )


}

const StyledCourseListItem = styled(CourseListItem)`
  margin: 8px;
  padding: 8px;
`

const StyledCard = styled(Card)`
  margin: 16px
`

const StyledWrapper = styled.div`
  margin: 8px;
  display: flex;
  justify-content: space-between;
`

