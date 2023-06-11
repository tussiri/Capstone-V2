import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import Button from "@mui/material/Button";
import axios from 'axios';
import SearchResults from "./SearchResults";
import LoadingScreen from "./LoadingPage";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";
import LoadingPage from "./LoadingPage";
import FoodCard from "../Components/FoodCard";


function AllRecipes() {

    const [recipes, setRecipes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        axios.get(`http://localhost:8080/recipes?page=${page - 1}&size=10`)
            .then((res) => {
                if (response.status === 200) {
                    setRecipes(response.data.data.content);
                    setTotalPages(response.data.data.totalPages);
                    setLoading(false);
                }
            })
            .catch((error) => {
                console.log("There was an error fetching all recipes: ", error);
                setLoading(false);
            })
    }, [page])

    if (loading) {
        return <LoadingPage/>
    }

    return (
        <>
            <div>
                <h1>All Recipes</h1>
                {recipes.map((recipe) => (
                    <FoodCard
                        key={recipe.id}
                        recipe={recipe}
                        userId={null}
                        onClick={() => handleCardClick(recipe.id)}
                    />
                ))}
            </div>

        </>
    )
}

export default AllRecipes