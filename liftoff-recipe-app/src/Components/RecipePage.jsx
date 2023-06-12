import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate, useParams} from 'react-router-dom'
import axios from "axios";
import Button from "@mui/material/Button";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";
import LoadingScreen from "../Pages/LoadingPage";
import stockImage from '../Assets/MealifyNoImage.png'
import NavBar from "./NavBar";

import '../Styles/RecipePage.css'

function RecipePage({match}) {
    const [recipe, setRecipe] = useState(null);
    const [reviews, setReviews] = useState([]);
    const {recipeId} = useParams();
    const [isLoading, setIsLoading] = useState(false);
    // const userId = localStorage.getItem('userId');
    const {user} = useContext(UserContext);
    // const loggedUserId = user?.id;
    const loggedUserId = localStorage.getItem('userId');
    const isUserRecipeOwner = user && recipe && recipe.userId.toString() === loggedUserId;
    const navigate = useNavigate()


    useEffect(() => {
        // const token = localStorage.getItem('token');
        // const userId = localStorage.getItem('userId')
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
    }, [recipeId]);

    const handleToggleFavorite = () => {
        if (!isUserRecipeOwner) {
            authAxios
                .post(`http://localhost:8080/recipes/${recipeId}/favorite`)
                .then((response) => {
                    console.log("Favorited recipe: ", response.data)
                    setRecipe(response.data.data);
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

    const renderIngredients = () => {
        const ingredientsList = recipe.ingredients.map((ingredient) => (
            <li key={ingredient}>{ingredient}</li>
        ));
        return <ul>{ingredientsList}</ul>;
    };

    const renderDirections = () => {
        const directionsList = recipe.directions.split('.').filter((direction) => direction.trim() !== '');
        const directionsItems = directionsList.map((direction, index) => (
            <li key={index}>{direction.trim()}</li>
        ));
        return <ol>{directionsItems}</ol>;
    };

    return (
        <div>
            <h2 className="recipe-name">{recipe.name}</h2>
            <img src={recipe.picture ? recipe.picture : stockImage} alt={recipe.name}
                 style={{maxWidth: '100%', height: 'auto'}}/>
            <p className="recipe-description">{recipe.description}</p>
            <p className="recipe-category">Category: {recipe.category}</p>
            <p className="recipe-time">Preparation time: {recipe.time} minutes</p>
            <p className="recipe-ingredients">Ingredients: {renderIngredients()}</p>

            {/*{recipe.ingredients.join(", ")}</p>*/}
            <p className="recipe-directions">Directions: {renderDirections()}</p>

            {/*{recipe.directions}</p>*/}
            <p className="recipe-allergens">Allergens: {recipe.allergens.join(", ")}</p>

            {isUserRecipeOwner && (
                <div>
                    <Button variant="contained" onClick={() => navigate(`/recipes/update/${recipe.id}`)}>Update
                        Recipe</Button>
                    <Button variant="contained" onClick={() => navigate(`/recipes/delete/${recipeId}`)}>Delete
                        Recipe</Button>
                </div>
            )}

            <p>
                Liked: {recipe.favorite ? "Yes" : "No"}{" "}
                <Button variant="contained" onClick={handleToggleFavorite}>
                    {recipe.favorite ? "Unlike" : "Like"}
                </Button>
            </p>

            <h3>Reviews:</h3>
            {reviews.length > 0 ? (
                <>
                    <div key={reviews[0].id}>
                        <p>Rating: {reviews[0].rating}</p>
                        <p>{reviews[0].comment}</p>
                    </div>
                    {!isUserRecipeOwner && user ? (
                        <>
                            <Button variant="contained"
                                    onClick={() => navigate(`/review/recipes/${recipe.id}/reviews`)}>
                                View all reviews
                            </Button>
                            <Button variant="contained" onClick={() => navigate(`/recipes/${recipe.id}/review`)}>Leave
                                your review</Button>
                        </>
                    ) : user ? (
                        <Button variant="contained"
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
                        <Button variant="contained" onClick={() => navigate(`/recipes/${recipe.id}/review`)}>
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