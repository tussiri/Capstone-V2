import React from "react"

function FoodCard({recipe, onClick}){
    return(
        <div onClick={onClick}>
            <img src={recipe.picture} alt={recipe.name}/>
            <h2>{recipe.name}</h2>
            <p>{recipe.description}</p>
            <p>Category: {recipe.category}</p>
            <p>Preparation Time: {recipe.time} minutes</p>
            <p>Ingredients: {recipe.ingredients ? recipe.ingredients.join(", ") : ""}</p>
            <p>Directions: {recipe.directions}</p>
            <p>Rating: {recipe.rating}</p>
        </div>
    );
}

export default FoodCard;