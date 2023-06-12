import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate, useParams} from "react-router-dom";
import Button from "@mui/material/Button";
import axios from 'axios';
import SearchResults from "./SearchResults";
import LoadingScreen from "./LoadingPage";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";
import LoadingPage from "./LoadingPage";
import FoodCard from "../Components/FoodCard";
import Box from '@mui/material/Box';


function AllRecipes() {

    const [recipes, setRecipes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const navigate=useNavigate();
    const {recipeId} = useParams();

    useEffect(() => {
        axios.get(`http://localhost:8080/recipes?page=${page}&size=10`)
            .then((response) => {
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

    const handleNextPage = () => {
            if (page < totalPages) {
                setPage(prevPage => prevPage + 1);
            }
        };

    const handleCardClick = async (recipeId) => {
       try {
          const recipeResponse = await axios.get(`http://localhost:8080/recipes/${recipeId}`);
          navigate(`/recipes/${recipeId}`);
       } catch (error) {
          console.error(error);
       }
    };

    if (loading) {
        return <LoadingPage/>
    }

    return (
    <div>
                <Box sx={{ maxWidth: '100%', display: 'flex', flexDirection: 'column'}} justifyContent='center' alignItems="center">
                <h2>All Recipes</h2>
                    <Box sx={{
                        maxWidth:'100%',
                        display: 'flex',
                        flexDirection: 'row',
                        flexWrap: 'wrap',
                        alignContent: 'start'
                        }} justifyContent='center' alignItems="center">
                    {recipes.map((recipe) => (
                       <Box sx={{ maxWidth:'23%' }}>
                       <FoodCard
                          key={recipe.id}
                          recipe={recipe}
                          userId={null}
                          onClick={() => handleCardClick(recipe.id)}
                       />
                       </Box>
                    ))}

                    </Box >
                    <Box sx={{ maxWidth:'50%', m:5 }}>
                    {page < totalPages && (
                                            <Button sx={{ color: 'white'}} variant="contained" fullWidth onClick={handleNextPage}>Next Page</Button>
                                        )}
                    </Box>
                </Box>
            </div>
    )
}


export default AllRecipes