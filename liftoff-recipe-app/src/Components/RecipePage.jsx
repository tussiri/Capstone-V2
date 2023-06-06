import React, {useEffect, useState} from "react";
import axios from "axios";
import {Link, useParams} from 'react-router-dom'
import authAxios from "../utility/authAxios";

function RecipePage({match}) {
    const [recipe, setRecipe] = useState(null);
    const [reviews, setReviews] = useState([]);
    const {recipeId, userId} = useParams();

    useEffect(() => {
        // const token = localStorage.getItem('token');
        // const userId = localStorage.getItem('userId')
        console.log(recipeId);
        authAxios
            .get(`http://localhost:8080/recipes/${recipeId}`)
            .then((response) => {
                setRecipe(response.data.data);
                console.log(response.data.data)
            })
            .catch((error) => console.error(error));

        axios
            .get(`http://localhost:8080/review/recipe/${recipeId}`)
            .then((response) => {
                setReviews(response.data.slice(0, 5));
            })
            .catch((error) => console.error(error));
    }, [recipeId]);

    if (!recipe) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2>{recipe.name}</h2>
            <img src={recipe.picture} alt={recipe.name}/>
            <p>{recipe.description}</p>
            <p>Category: {recipe.category}</p>
            <p>Preparation time: {recipe.time} minutes</p>
            <p>Ingredients: {recipe.ingredients.join(", ")}</p>
            <p>Directions: {recipe.directions}</p>
            <p>Allergens: {recipe.allergens.join(", ")}</p>

            <h3>Reviews:</h3>
            {reviews.length > 0 ? (
                reviews.map((review) => (
                    <div key={review.id}>
                        <p>Rating: {review.rating}</p>
                        <p>{review.comment}</p>
                    </div>
                ))
            ) : (
                <p>No reviews yet. Be the first!</p>
            )}

            <Link
                to={`/recipes/${recipe.id}/review`}>{reviews.length > 0 ? "Leave your review" : "Be the first to review this recipe!"}</Link>
        </div>
    );
}

export default RecipePage;