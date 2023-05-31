import React, { useEffect, useState } from "react";
import axios from "axios";
import {Link, useParams} from 'react-router-dom'

function RecipePage({ match }) {
    const [recipe, setRecipe] = useState(null);
    const {id} = useParams();

    useEffect(() => {
        const token=localStorage.getItem('token');
        const config={
            headers:{
                Authorization:`Bearer ${token}`
            }
        }
        axios
            .get(`http://localhost:8080/recipes/${id}`, config)
            .then((response) => {
                setRecipe(response.data.data);
                console.log(response.data)
            })
            .catch((error) => console.error(error));
    }, [id]);

    if (!recipe) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2>{recipe.name}</h2>
            <img src={recipe.picture} alt = {recipe.name} />
            <p>{recipe.description}</p>
            <p>Category: {recipe.category}</p>
            <p>Preparation time: {recipe.time} minutes</p>
            <p>Ingredients: {recipe.ingredients.join(", ")}</p>
            <p>Directions: {recipe.directions}</p>
            <p>Allergens: {recipe.allergens.join(", ")}</p>
            <p>Rating: {recipe.ratings || <Link to={`/recipes/${recipe.id}/review`}>"Be the first to review this recipe!"</Link>}</p>
        </div>
    );
}

export default RecipePage;