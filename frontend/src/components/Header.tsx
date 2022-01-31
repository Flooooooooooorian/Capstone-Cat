import {Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";


export default function Header() {
  const navigation = useNavigate()

  return (
    <>
      <Typography variant={"h2"} onClick={() => navigation("/")}>
        Capstone Cat
      </Typography>
    </>
  )
}
