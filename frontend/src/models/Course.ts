import Capstone from "./Capstone";

export default interface Course {
  id?: string
  name: string
  capstones: Capstone[]
}