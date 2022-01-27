import CoursePage from "./pages/CoursePage";
import {Route, Routes} from "react-router-dom";
import HomePage from "./pages/HomePage";
import Header from "./components/Header";
import AddCoursePage from "./pages/AddCoursePage";

function App() {
    return (
        <>
            <Header/>
            <Routes>
                <Route path="/">
                    <Route index element={<HomePage/>}/>
                    <Route path={"course/create"} element={<AddCoursePage/>}/>
                    <Route path="course/:courseId" element={<CoursePage/>}> </Route>
                </Route>

            </Routes>
        </>
    );
}

export default App;
