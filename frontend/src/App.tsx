import CoursePage from "./pages/CoursePage";
import {Route, Routes} from "react-router-dom";
import HomePage from "./pages/HomePage";
import Header from "./components/Header";
import AddCoursePage from "./pages/AddCoursePage";
import {createTheme, CssBaseline, ThemeProvider} from "@mui/material";

const theme = createTheme({
  palette: {
    mode: 'light',
  },
})

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Header/>
      <Routes>
        <Route path="/">
          <Route index element={<HomePage/>}/>
          <Route path={"course/create"} element={<AddCoursePage/>}/>
          <Route path="course/:courseId" element={<CoursePage/>}> </Route>
        </Route>

      </Routes>
    </ThemeProvider>
  );
}

export default App;
