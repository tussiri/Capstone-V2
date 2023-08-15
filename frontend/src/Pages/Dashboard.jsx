import React, {useContext, useEffect, useState} from "react";
import axios from "axios";
import {Link, useNavigate, useParams} from "react-router-dom";
import FoodCard from "../Components/FoodCard";
import "../Styles/Dashboard.css";
import authAxios from "../utility/authAxios";
// import SearchBar from "../Components/SearchBar";
// import Sidebar from "../Components/SideBar";
// import NavBar from "../Components/NavBar";
// import Button from "@mui/material/Button";
import LoadingScreen from "./LoadingPage";
import SearchResults from "./SearchResults";
import {UserContext} from "../stores/UserStore";
import Box from '@mui/material/Box';

function Dashboard() {
    const [recipes, setRecipes] = useState([]);
    const navigate = useNavigate();
    const userId = localStorage.getItem("userId")
    const [isSearching, setIsSearching] = useState(false);
    const [hasSearched, setHasSearched] = useState(false);
    const [searchQuery, setSearchQuery] = useState('')
    const {user, logout, login} = useContext(UserContext);
    const [likedRecipes, setLikedRecipes] = useState([]);
    const {recipeId} = useParams();
    const [isFavorite, setIsFavorite] = useState();
    const [forceUpdate, setForceUpdate] = useState(0);


    const handleSearch = (query) => {
        setIsSearching(true);
        setSearchQuery(query);
    }

    const handleSearchComplete = () => {
        setIsSearching(false);
        setHasSearched(true)
    }

    useEffect(() => {
        const fetchRecipes = async () => {
            try {
                if (userId) {
                    console.log("Fetching recipes...");
                    const response = await authAxios.get(`http://localhost:8080/recipes/user/${userId}`);
                    const data = response.data.data;
                    console.log("Fetched recipes:", data);
                    setRecipes(data.content);
                } else {
                    console.log("User ID not found. Skipping recipe fetching.");
                }
            } catch (error) {
                console.error("Error fetching recipes:", error);
            }
        };
        fetchRecipes();
    }, [userId]);

    const onFavorite = async (recipeId) => {
        const userId = localStorage.getItem('userId');
        try {
            console.log("Before favorite API request");
            const response = await authAxios.post(`http://localhost:8080/users/${userId}/favorites/${recipeId}`);
            console.log("Heart clicked: ", response.data.data);
            setIsFavorite(true);
        } catch (error) {
            console.error(error);
        }
    };

    const onUnfavorite = async (recipeId) => {
        try {
            console.log("Before unfavorite API request");
            await authAxios.delete(`http://localhost:8080/users/${userId}/favorites/${recipeId}`);
            console.log("Unfavorited recipe with ID:", recipeId);

            const response = await authAxios.get(`http://localhost:8080/users/${userId}/favorites`);
            console.log("Fetched liked recipes after unfavoriting: ", response.data.data);
            setLikedRecipes(response.data.data);

            setIsFavorite(false);
        } catch (error) {
            console.error("Error unfavoriting recipe:", error);
        }
    };


    useEffect(() => {
        const fetchLikedRecipes = async () => {
            if (userId) {
                const response = await authAxios.get(`http://localhost:8080/users/${userId}/favorites`);
                console.log("Fetched liked recipes: ", response.data.data);
                setLikedRecipes(response.data.data);
            } else {
                console.log("User ID not found. Skipping liked Recipes fetching.");
            }
        };
        fetchLikedRecipes();
        console.log('useEffect run');
    }, [userId, isFavorite]);

    useEffect(() => {
        console.log("Updated liked recipes:", likedRecipes);
    }, [likedRecipes]);

    const handleCardClick = (recipe) => {
        navigate(`/recipes/${recipe.id}`);
    };

    return (
        <>
            <Box sx={{ maxWidth: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                {user ? (
                    <>
                        <h2>Your Recipes</h2>
                        <Box sx={{ display: 'flex', flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'center' }}>
                            {recipes && recipes.length > 0 && recipes.map((recipeId) => (
                                <Box sx={{ maxWidth: '23%', margin: '1%' }}>
                                    <FoodCard
                                        key={recipeId}
                                        recipe={recipeId}
                                        onClick={() => handleCardClick(recipeId)}
                                    />
                                </Box>
                            ))}
                        </Box>
                        <h2>Liked Recipes</h2>
                        <Box sx={{ maxWidth: '100%', display: 'flex', flexDirection: 'row', alignItems: 'center' }}>
                            {likedRecipes && likedRecipes.length > 0 && likedRecipes.map((recipeId) => (
                                <Box sx={{ maxWidth: '100%', margin: '1%' }}>
                                    <FoodCard
                                        key={recipeId}
                                        recipe={recipeId}
                                        onClick={() => handleCardClick(recipeId)}
                                        onFavorite={() => onFavorite(recipeId)}
                                        onUnfavorite={() => onUnfavorite(recipeId)}
                                    />
                                </Box>
                            ))}
                        </Box>
                    </>
                ) : (
                    <>
                        {
                            isSearching && hasSearched ? (
                                <LoadingScreen/>
                            ) : (
                                <SearchResults query={searchQuery} onSearchComplete={handleSearchComplete}/>
                            )
                        }
                    </>
                )}
            </Box>
        </>
    );

}

export default Dashboard;