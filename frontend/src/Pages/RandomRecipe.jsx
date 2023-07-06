import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import axios from 'axios';
import SearchResults from "./SearchResults";
import LoadingScreen from "./LoadingPage";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";


function RandomRecipe(){

return(
<>

        <div>
        <body>
        <h1>Display a random recipe here</h1>
        </body>
        </div>

</>
)}
export default RandomRecipe