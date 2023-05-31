// import React, { useState, useEffect } from 'react';
// // import axios from 'axios';
// //
// // function Dashboard() {
// //     // const [userRecipes, setUserRecipes] = useState([]);
// //     // const [likedRecipes, setLikedRecipes] = useState([]);
// //     // const [recommendedRecipes, setRecommendedRecipes] = useState([]);
// //
// //     useEffect(() => {
// //         const token = localStorage.getItem('token');
// //         const config = {
// //             headers: { Authorization: `Bearer ${token}` }
// //         };
// //
// //         axios.get('http://localhost:8080/user-recipes', config)
// //             .then(response => {
// //                 setUserRecipes(response.data);
// //             });
// //
// //         axios.get('http://localhost:8080/liked-recipes', config)
// //             .then(response => {
// //                 setLikedRecipes(response.data);
// //             });
// //
// //         // You would replace 'your-api-key' with your actual Spoonacular API key
// //         axios.get('https://api.spoonacular.com/recipes/random?number=5&apiKey=a6f8abac7b0b4d4b9041bef47b8d82c2')
// //             .then(response => {
// //                 setRecommendedRecipes(response.data.recipes);
// //             });
// //     }, []);
// //
// //     return (
// //         <div>
// //             <h1>Your Recipes</h1>
// //             {userRecipes.map(recipe => (
// //                 <div key={recipe.id}>
// //                     <h2>{recipe.title}</h2>
// //                     <p>{recipe.description}</p>
// //                     {/* Add other recipe details */}
// //                 </div>
// //             ))}
// //
// //             <h1>Liked Recipes</h1>
// //             {likedRecipes.map(recipe => (
// //                 <div key={recipe.id}>
// //                     <h2>{recipe.title}</h2>
// //                     <p>{recipe.description}</p>
// //                     {/* Add other recipe details */}
// //                 </div>
// //             ))}
// //
// //             <h1>Recommended Recipes</h1>
// //             {recommendedRecipes.map(recipe => (
// //                 <div key={recipe.id}>
// //                     <h2>{recipe.title}</h2>
// //                     <p>{recipe.summary}</p>
// //                     <img src={recipe.image} alt={recipe.title} />
// //                     {/* Add other recipe details */}
// //                 </div>
// //             ))}
// //         </div>
// //     );
// // }
// //
// // export default Dashboard;

import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import FoodCard from "../Components/FoodCard";
import "../Styles/Dashboard.css"

function Dashboard() {
    const [recipes, setRecipes] = useState([]);
    const [recommended, setRecommended] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem("token");
        const config = {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        };
        axios
            .get("http://localhost:8080/recipes?page=0&size=5", config)
            .then((response) => {
                const data = response.data.data;
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

    const handleCardClick = (id) => {
        navigate(`/recipes/${id}`);
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