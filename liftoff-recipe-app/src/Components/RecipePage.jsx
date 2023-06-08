import React, {useContext, useEffect, useState} from "react";
import axios from "axios";
import {Link, useNavigate, useParams} from 'react-router-dom'
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";
import LoadingScreen from "../Pages/LoadingPage";
import stockImage from '../Assets/MealifyNoImage.png'

function RecipePage({match}) {
    const [recipe, setRecipe] = useState(null);
    const [reviews, setReviews] = useState([]);
    const {recipeId} = useParams();
    const [isLoading, setIsLoading] = useState(false);
    // const userId = localStorage.getItem('userId');
    const {user} = useContext(UserContext);
    const loggedUserId=localStorage.getItem('userId');
    const isUserRecipeOwner = user && recipe && recipe.userId.toString() === loggedUserId;
    const navigate = useNavigate()


    useEffect(() => {
        // const token = localStorage.getItem('token');
        // const userId = localStorage.getItem('userId')
        console.log(recipeId);
        console.log(loggedUserId);
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

    const handleUpdate=()=>{
        navigate(`http:localhost:8080/recipes/update/${recipeId}`)
        console.log(recipeId)

    }
    const handleDelete = () => {
        authAxios
            .delete(`http://localhost:8080/recipes/delete/${recipeId}`)
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
            <h2>{recipe.name}</h2>
            <img src={recipe.picture ? recipe.picture : stockImage} alt={recipe.name}/>
            <p>{recipe.description}</p>
            <p>Category: {recipe.category}</p>
            <p>Preparation time: {recipe.time} minutes</p>
            <p>Ingredients: {recipe.ingredients.join(", ")}</p>
            <p>Directions: {recipe.directions}</p>
            <p>Allergens: {recipe.allergens.join(", ")}</p>

            {isUserRecipeOwner && (
                <div>
                    <Link to={`/recipes/update/${recipeId}`}>Update Recipe</Link>
                    <Link to={`/recipes/delete/${recipe.id}`}>Delete Recipe</Link>
                </div>
            )}

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
                ) : (
                    <p>You must be <Link to="/login">logged in </Link> to leave a review.</p>
                )
            )}
            {user && <Link to={"/recipes/${recipe.id}/review"}>Leave your review</Link>}
        </div>
    );
}

export default RecipePage;