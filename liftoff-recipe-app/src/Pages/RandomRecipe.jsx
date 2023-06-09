import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import Button from "@mui/material/Button";
import axios from 'axios';
import NavBar from "../Components/NavBar"
import SearchBar from "../Components/SearchBar"
import SearchResults from "./SearchResults";
import LoadingScreen from "./LoadingPage";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";
import Sidebar from "../Components/SideBar";

function RandomRecipe(){

return(
<>

        <NavBar/>
        <div>
        <body>
        <h1>Display a random recipe here</h1>
        </body>
        </div>

</>
)}
export default RandomRecipe