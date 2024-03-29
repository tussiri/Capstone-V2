import React, {useEffect, useState} from 'react';
import axios from 'axios';
import FoodCard from '../Components/FoodCard';
import Grid from '@mui/material/Grid';
import {useNavigate, useParams} from "react-router-dom";
import NavBar from "../Components/NavBar";
import Box from '@mui/material/Box';
import LoadingWave from "./LoadingWave";

const RandomRecipes = () => {
    const [recipes, setRecipes] = useState([]);
    const {recipeId} = useParams();
    const navigate = useNavigate();
    const[loading, setLoading]=useState(false);


    useEffect(() => {
        setLoading(true);
        const fetchRandomRecipes = async () => {

            try {
                const response = await axios.get(`http://localhost:8080/recipes/random`);
                console.log("Response: ", response.data.data);
                setRecipes([response.data.data]);
                setTimeout(() => {
                    setLoading(false);
                }, 1000);
            } catch (error) {
                if (error.response) {
                    console.log(error.response.data);
                    console.log(error.response.status);
                    console.log(error.response.headers);
                } else if (error.request) {
                    console.log(error.request);
                } else {
                    // Something happened in setting up the request that triggered an Error
                    console.log('Error', error.message);
                }
                console.log(error.config);
                setLoading(false)
            }
        }

        fetchRandomRecipes();
    }, []);

    const handleCardClick = async (recipeId) => {
        try {
            const recipeResponse = await axios.get(`http://localhost:8080/recipes/${recipeId}`);
            navigate(`/recipes/${recipeId}`);
        } catch (error) {
            console.error(error);
        }
    };

    if (loading) {
        return <LoadingWave/>
    }


    return (
        <div>
            <Box sx={{maxWidth: '100%', display: 'flex', flexDirection: 'column'}} justifyContent='center'
                 alignItems="center">
                <h2 style={{ margin: '20px 0' }}>Try this random recipe!</h2>
                <Box sx={{
                    maxWidth: '50%',
                    display: 'flex',
                    flexDirection: 'column',
                    flexWrap: 'wrap',
                    alignContent: 'start'
                }}
                     justifyContent='center'
                     alignItems="center">
                    {recipes.map((recipe, index) => (
                        <Box>
                            <FoodCard recipe={recipe}
                                      key={recipe.id}
                                      onClick={() => handleCardClick(recipe.id)}/>
                        </Box>
                    ))}
                </Box>
            </Box>
        </div>
    )
}

export default RandomRecipes;