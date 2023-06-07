import React, {useContext, useEffect, useState} from "react";
import axios from "axios";
import {Link, useParams} from 'react-router-dom'
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";
import LoadingScreen from "../Pages/LoadingPage";

function RecipePage({match}) {
    const [recipe, setRecipe] = useState(null);
    const [reviews, setReviews] = useState([]);
    const {recipeId, userId} = useParams();
    const [isLoading, setIsLoading]=useState(false);

    const {user} =useContext(UserContext);

    useEffect(() => {
        // const token = localStorage.getItem('token');
        // const userId = localStorage.getItem('userId')
        console.log(recipeId);
        setIsLoading(true)
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
        return <LoadingScreen/>;
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
                <>
                    <div key={reviews[0].id}>
                        <p>Rating: {reviews[0].rating}</p>
                        <p>{reviews[0].comment}</p>
                    </div>
                    <Link to={`http://localhost:8080/review/recipes/${recipe.id}/reviews`}>
                        View all reviews
                    </Link>
                </>
            ) : (
                user ? (
                        <p><Link to={`/recipes/${recipe.id}/review`}>No reviews yet. Be the first!</Link></p>
                    ):(
                        <p>You must be <Link to="/login">logged in </Link> to leave a review.</p>
                    )
            )}
            {user && <Link to={"/recipes/${recipe.id}/review"}>Leave your review</Link>}
        </div>
    );
}

export default RecipePage;