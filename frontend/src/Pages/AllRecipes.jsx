import React, {useContext, useEffect, useState} from "react";
import {Link, useLocation, useNavigate, useParams} from "react-router-dom";
import Button from "@mui/material/Button";
import axios from 'axios';
import SearchResults from "./SearchResults";
import LoadingWave from "./LoadingWave";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";
import FoodCard from "../Components/FoodCard";
import Box from '@mui/material/Box';
import Grid from "@mui/material/Grid";


function AllRecipes() {

    const [recipes, setRecipes] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigate = useNavigate();
    const { search } = useLocation();

    const searchParams = new URLSearchParams(search);
    const page = searchParams.get("page") || 1;

    const [totalPages, setTotalPages] = useState(0);


    useEffect(() => {
        setIsLoading(true);
        authAxios.get(`http://localhost:8080/recipes?page=${page - 1}&size=12`)
            .then((response) => {
                console.log("API response:", response.data);
                if (response.status === 200) {
                    setRecipes(response.data.data.content);
                    setTotalPages(response.data.data.totalPages);
                    console.log("recipes: ", response.data.data.content)
                    console.log("total number of pages: ", totalPages)
                }
            })
            .catch((error) => {
                console.log("There was an error fetching all recipes: ", error);
            }).finally(() => {
            setIsLoading(false);
        })
    }, [page])

    const handleNextPage = () => {
        const searchParams = new URLSearchParams(search);
        const currentPage = parseInt(searchParams.get("page")) || 1;
        const nextPage = currentPage + 1;
        setIsLoading(true)
        searchParams.set("page", nextPage);
        navigate(`?${searchParams.toString()}`);
    };
    const handlePreviousPage = () => {
        setIsLoading(true)
        const searchParams = new URLSearchParams(search);
        let currentPage = parseInt(searchParams.get("page")) || 1;
        currentPage = Math.max(1, currentPage - 1);
        searchParams.set("page", currentPage);
        navigate(`?${searchParams.toString()}`);
    };

    const handleCardClick = async (recipeId) => {
        try {
            const recipeResponse = await axios.get(`http://localhost:8080/recipes/${recipeId}`);
            navigate(`/recipes/${recipeId}`);
        } catch (error) {
            console.error(error);
        }
    };

    if (isLoading) {
        return <LoadingWave/>
    }

    return (
        <div>
            <Box sx={{ maxWidth: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent:'center' }}>
                <h2>All Recipes</h2>
                <Grid container spacing={3} justifyContent="center">
                    {recipes.map((recipe) => (
                        <Grid item xs={12} sm={6} md={3} lg={2}>
                            <FoodCard
                                key={recipe.id}
                                recipe={recipe}
                                userId={null}
                                onClick={() => handleCardClick(recipe.id)}
                            />
                        </Grid>
                    ))}
                </Grid>
                <Box sx={{ m: 5, display: 'flex', gap: '2rem' }}>
                    {page > 1 && (
                        <Button sx={{ color: 'white' }} variant="contained" onClick={handlePreviousPage}>Previous Page</Button>
                    )}
                    {page < totalPages && (
                        <Button sx={{ color: 'white' }} variant="contained" onClick={handleNextPage}>Next Page</Button>
                    )}
                </Box>
            </Box>
        </div>
    )
}

export default AllRecipes