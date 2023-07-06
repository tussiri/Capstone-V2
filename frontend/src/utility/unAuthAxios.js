import axios from "axios";

const unAuthAxios = axios.create({
    baseUrl:'http://localhost:8080/recipes/search?'
})
export default unAuthAxios