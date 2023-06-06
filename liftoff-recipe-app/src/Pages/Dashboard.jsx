import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import FoodCard from "../Components/FoodCard";
import "../Styles/Dashboard.css"
import authAxios from "../utility/authAxios";

function Dashboard() {
    const [recipes, setRecipes] = useState([]);
    const [recommended, setRecommended] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {

        authAxios
            .get("http://localhost:8080/recipes?page=0&size=8")
            .then((response) => {
                const data = response.data.data;
                console.log(response.data)
                setRecipes(data.content);
            })
            .catch((error) => console.error(error));
    }, []);

    // New useEffect for fetching recommended recipes
    useEffect(() => {
        // Fetch the recommended recipes from Spoonacular API
        axios
            .get("https://api.spoonacular.com/recipes/complexSearch?apiKey=a6f8abac7b0b4d4b9041bef47b8d82c2")
            .then((response) => {
                setRecommended(response.data.results);
            })
            .catch((error) => console.error(error));
    }, []);

    const handleCardClick = (recipeId) => {
        navigate(`/recipes/${recipeId}`);
    };

    return (
        <div className="Dashboard">
            <h2>Your Recipes</h2>
            <div className="recipe-section">
                {recipes.map((recipe) => (
                    <FoodCard
                        key={recipe.id}
                        recipe={recipe}
                        onClick={() => handleCardClick(recipe.id)}
                    />
                ))}
            </div>
            <h2>Recommended Recipes</h2>
            <div className="recipe-section">
                {recommended.map((recipe) => (
                    <FoodCard
                        key={recipe.id}
                        recipe={recipe}
                        onClick={() => handleCardClick(recipe.id)}
                    />
                ))}
            </div>
        </div>
    );
}

export default Dashboard;