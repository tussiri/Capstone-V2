import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate, useParams} from 'react-router-dom'
import axios from "axios";
import Button from "@mui/material/Button";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";
import LoadingScreen from "../Pages/LoadingPage";
import stockImage from '../Assets/MealifyNoImage.png'

import Box from '@mui/material/Box';

import '../Styles/RecipePage.css'

function RecipePage({match}) {
    const [recipe, setRecipe] = useState(null);
    const [reviews, setReviews] = useState([]);
    const {recipeId} = useParams();
    const [isLoading, setIsLoading] = useState(false);
    const {user, setUser} = useContext(UserContext);
    const loggedUserId = localStorage.getItem('userId');
    const isUserRecipeOwner = user && recipe && recipe.userId.toString() === loggedUserId;
    const navigate = useNavigate()


    useEffect(() => {
        console.log(recipeId);
        console.log(loggedUserId);
        setIsLoading(true)
        axios
            .get(`http://localhost:8080/recipes/${recipeId}`)
            .then((response) => {
                setRecipe(response.data.data);
                console.log(response.data.data)
            })

            .catch((error) => console.error(error));

        authAxios
            .get(`http://localhost:8080/review/recipe/${recipeId}`)
            .then((response) => {
                setReviews(response.data.slice(0, 5));
            })
            .catch((error) => console.error(error))
            .finally(() => {
                setIsLoading(false);
            })
    }, [recipeId, loggedUserId]);

    const handleToggleFavorite = () => {
        if (!isUserRecipeOwner && user) {
            const isRecipeFavorited = user.favoriteRecipes.includes(recipeId);

            const url = isRecipeFavorited
                ? `http://localhost:8080/users/${loggedUserId}/favorites/${recipeId}`
                : `http://localhost:8080/users/${loggedUserId}/favorites/${recipeId}`;

            const method = isRecipeFavorited ? 'DELETE' : 'POST';

            authAxios
                .request({
                    url,
                    method,
                })
                .then(() => {
                    const updatedUser = { ...user };
                    if (isRecipeFavorited) {
                        updatedUser.favoriteRecipes = updatedUser.favoriteRecipes.filter(
                            (id) => id !== recipeId
                        );
                    } else {
                        updatedUser.favoriteRecipes.push(recipeId);
                    }
                    setUser(updatedUser);
                    setRecipe((prevRecipe) => ({
                        ...prevRecipe,
                        favorite: !isRecipeFavorited,
                    }));
                })
                .catch((error) => {
                    console.error(error);
                });
        }
    };



    const handleUpdate = () => {
        navigate(`/recipes/update/${recipeId}`)
        console.log(recipeId)

    }
    const handleDelete = () => {
        authAxios
            .delete(`/recipes/delete/${recipeId}`)
            .then(() => {
                navigate('/dashboard');
            })
            .catch((error) => {
                console.error(error);
            });
    };


    if (!recipe) {
        return <LoadingScreen/>;
    }

    return (
        <div>
          <Box sx={{ maxWidth: '100%', display: 'flex', flexDirection: 'column'}} justifyContent='center' alignItems="center">
            <h2>{recipe.name}</h2>
            <Box sx={{ maxWidth: '250px', display: 'flex'}} justifyContent='center' alignItems="center">
                <img src={recipe.picture ? recipe.picture : stockImage} alt={recipe.name}/>
            </Box>
            <Box sx={{ maxWidth: '50%', display: 'flex', flexDirection: 'column', justify: 'center' }} justifyContent='center'>
                <p>{recipe.description}</p>
                <p>Category: {recipe.category}</p>
                <p>Preparation time: {recipe.time} minutes</p>
                <p>Ingredients:</p>
                <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px' }}>
                    {recipe.ingredients.map((ingredient, index) => (
                        <span key={index}>{ingredient.name}</span>
                    ))}
                </div>
                <p>Directions: {recipe.directions}</p>
                <p>Allergens: {recipe.allergens.join(", ")}</p>
            </Box>
          </Box>

            {isUserRecipeOwner && (
                <div>
                    <Button sx={{color: 'white', m:1}} variant="contained" onClick={() => navigate(`/recipes/update/${recipe.id}`)}>Update
                        Recipe</Button>
                    <Button sx={{color: 'white', m:1}} variant="contained" onClick={() => navigate(`/recipes/delete/${recipeId}`)}>Delete
                        Recipe</Button>
                </div>
            )}

            {/*<p>*/}
            {/*    Liked: {recipe.favorite ? "Yes" : "No"}{" "}*/}
            {/*    {!isUserRecipeOwner &&*/}
            {/*        <Button variant="contained" onClick={handleToggleFavorite}>*/}
            {/*            {recipe.favorite ? "Unlike" : "Like"}*/}
            {/*        </Button>*/}
            {/*    }*/}
            {/*</p>*/}

            <h3>Reviews:</h3>
            {reviews.length > 0 ? (
                <>
                    <div key={reviews[0].id}>
                        <p>Rating: {reviews[0].rating}</p>
                        <p>{reviews[0].comment}</p>
                    </div>
                    {!isUserRecipeOwner && user ? (
                        <>
                            <Button sx={{color: 'white', m:1}} variant="contained"
                                    onClick={() => navigate(`/review/recipes/${recipe.id}/reviews`)}>
                                View all reviews
                            </Button>
                            <Button sx={{color: 'white'}} variant="contained" onClick={() => navigate(`/recipes/${recipe.id}/review`)}>Leave
                                your review</Button>
                        </>
                    ) : user ? (
                        <Button sx={{color: 'white', m:1}} variant="contained"
                                onClick={() => navigate(`/review/recipes/${recipe.id}/reviews`)}>
                            View all reviews
                        </Button>
                    ) : (
                        <p>You must be <Link to="/login">logged in </Link> to leave a review.</p>
                    )}
                </>
            ) : (
                user ? (
                    <p>
                        <Button sx={{color: 'white', m:1}} variant="contained" onClick={() => navigate(`/recipes/${recipe.id}/review`)}>
                            {isUserRecipeOwner ? "View all reviews" : "Be the first to review"}
                        </Button>
                    </p>
                ) : (
                    <p>You must be <Link to="/login">logged in </Link> to leave a review.</p>
                )
            )}
        </div>
    );
}

export default RecipePage;