import React, {useContext, useEffect, useState} from "react";
import axios from "axios";
import {Link, useNavigate} from "react-router-dom";
import FoodCard from "../Components/FoodCard";
import "../Styles/Dashboard.css";
import authAxios from "../utility/authAxios";
import SearchBar from "../Components/SearchBar";
import Sidebar from "../Components/SideBar";
import NavBar from "../Components/NavBar";
import Button from "@mui/material/Button";
import LoadingScreen from "./LoadingPage";
import SearchResults from "./SearchResults";
import {UserContext} from "../stores/UserStore";
import Box from '@mui/material/Box';

function Dashboard() {
    const [recipes, setRecipes] = useState([]);
    const [allRecipes, setAllRecipes] = useState([]);
    const navigate = useNavigate();
    // const [user, setUser] = useState(null);
    const userId = localStorage.getItem("userId")
    const [isSearching, setIsSearching] = useState(false);
    const [hasSearched, setHasSearched] = useState(false);
    const [searchQuery, setSearchQuery] = useState('')
    const [isLoading, setIsLoading] = useState(true);
    const {user, logout, login} = useContext(UserContext);
    const [likedRecipes, setLikedRecipes] = useState([]);


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

    useEffect(() => {
        const fetchAllRecipes = async () => {
            try {
                console.log("Fetching all recipes...");
                const response = await authAxios.get('http://localhost:8080/recipes');
                const data = response.data.data;
                console.log("Fetched all recipes", data);
                setAllRecipes(data.content);
            } catch (error) {
                console.error("Error fetching all recipes: ", error);
            }
        };
        fetchAllRecipes();
    }, [])


    const handleCardClick = (recipeId) => {
        navigate(`/recipes/${recipeId}`);
    };
    return (
        <>
            {/* New Recipe button only visible to logged-in users */}
            {user && (
                <>
                    <Link to="newrecipe">
                        <Button sx={{color: 'white', mt: 2}} variant="contained">Add New Recipe</Button>
                    </Link>
                </>
            )}

            <div>

                <div>
                    {user ? (
                          <Box sx={{ maxWidth: '100%', display: 'flex', flexDirection: 'column'}} justifyContent='center' alignItems="center">
                            <h2>Your Recipes</h2>
                            <Box sx={{
                                maxWidth:'100%',
                                display: 'flex',
                                flexDirection: 'row',
                                flexWrap: 'wrap',
                                alignContent: 'start'
                                }}
                                justifyContent='center'
                                alignItems="center">
                            {recipes && recipes.length > 0 && recipes.map((recipe) => (
                                <Box sx={{ maxWidth:'23%' }}>
                                <FoodCard
                                    key={recipe.id}
                                    recipe={recipe}
                                    onClick={() => handleCardClick(recipe.id)}
                                />
                                </Box>
                            ))}
                            </Box>
                            <Box sx={{
                                maxWidth:'100%',
                                display: 'flex',
                                flexDirection: 'row',
                                flexWrap: 'wrap',
                                }}
                                justifyContent='center'
                                alignItems="center">
                            <h2>Liked Recipes</h2>
                            {likedRecipes.map((recipe) => (
                                <Box sx={{ maxWidth:'23%' }}>
                                <FoodCard
                                    key={recipe.id}
                                    recipe={recipe}
                                    onClick={() => handleCardClick(recipe.id)}
                                />
                                </Box>
                            ))}
                            </Box>
                        </Box>
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
                    )
                    }
                </div>
            </div>
        </>
    )
        ;
}

export default Dashboard;