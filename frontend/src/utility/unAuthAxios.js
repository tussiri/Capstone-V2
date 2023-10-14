import axios from "axios";

const unAuthAxios = axios.create({
    baseUrl:`${process.env.REACT_APP_BACKEND_URL}/recipes/search?`
})
export default unAuthAxios