import React from "react";
import CourseListItem from "../components/CourseListItem";
import {Button, Card, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";
import styled from "styled-components";
import Course from "../models/Course";


export interface HomePageProps {
    courses: Course[]
}

export default function HomePage({courses}: HomePageProps) {

  const navigation = useNavigate()

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

