import CoursePage from "./pages/CoursePage";
import {Route, Routes} from "react-router-dom";
import HomePage from "./pages/HomePage";
import Header from "./components/Header";
import AddCoursePage from "./pages/AddCoursePage";
import {createTheme, CssBaseline, ThemeProvider} from "@mui/material";
import useCourses from "./hooks/useCourses";

const theme = createTheme({
  palette: {
    mode: 'light',
  },
})

function App() {
    const {courses, refreshCapstonesById, addCourse} = useCourses()


  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Header/>
      <Routes>
        <Route path="/">
          <Route index element={<HomePage courses={courses}/>}/>
          <Route path={"course/create"} element={<AddCoursePage addCourse={addCourse}/>}/>
          <Route path="course/:courseId" element={<CoursePage courses={courses} refreshCapstoneById={refreshCapstonesById}/>}> </Route>
        </Route>

      </Routes>
    </ThemeProvider>
  );
}

export default App;
