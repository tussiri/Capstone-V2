import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import Button from "@mui/material/Button";
import axios from 'axios';
import SearchResults from "./SearchResults";
import LoadingScreen from "./LoadingPage";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";


function AllRecipes(){

return(
<>

        <div>
        <body>
        <h1>Display all recipes here</h1>
        </body>
        </div>

</>
)}
export default AllRecipes