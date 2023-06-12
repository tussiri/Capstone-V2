import React, {useContext, useEffect, useState} from "react";
import axios from "axios";
import {Link, useNavigate, useParams} from "react-router-dom";
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

function Dashboard() {
    const [recipes, setRecipes] = useState([]);
    const [allRecipes, setAllRecipes] = useState([]);
    const navigate = useNavigate();
    const userId = localStorage.getItem("userId")
    const [isSearching, setIsSearching] = useState(false);
    const [hasSearched, setHasSearched] = useState(false);
    const [searchQuery, setSearchQuery] = useState('')
    const [isLoading, setIsLoading] = useState(true);
    const {user, logout, login} = useContext(UserContext);
    const [likedRecipes, setLikedRecipes] = useState([]);
    const {recipeId} = useParams();



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
    //
    // useEffect(() => {
    //     const fetchAllRecipes = async () => {
    //         try {
    //             console.log("Fetching all recipes...");
    //             const response = await authAxios.get('http://localhost:8080/recipes');
    //             const data = response.data.data;
    //             console.log("Fetched all recipes", data);
    //             setAllRecipes(data.content);
    //         } catch (error) {
    //             console.error("Error fetching all recipes: ", error);
    //         }
    //     };
    //     fetchAllRecipes();
    // }, [])

    useEffect(() => {
        const fetchLikedRecipes = async () => {
            try {
                if (userId) {
                    console.log("Fetching liked recipes...");
                    const response = await authAxios.get(
                        `http://localhost:8080/recipes/user/${userId}/favorite`
                    );
                    const data = response.data.result;
                    console.log("Fetched liked recipes:", data);
                    setLikedRecipes(data);
                } else {
                    console.log("User ID not found. Skipping liked recipes fetching.");
                }
            } catch (error) {
                console.error("Error fetching liked recipes:", error);
            }
        };
        fetchLikedRecipes();
    }, [userId]);


    useEffect(() => {
        const favoriteRecipe = async () => {
            authAxios.post(`/recipes/${recipeId}/favorite`, {}, {
                headers: {
                    "userId": userId
                }
            })
                .then(response => {
                    setRecipes(recipes.map(recipe =>
                        recipe.id === recipeId ? {...recipe, isFavorite: true} : recipe
                    ));
                    console.log(response.data.message);
                })
                .catch(error => {
                    console.error(error);
                });
        }
    })


    const handleCardClick = (recipeId) => {
        navigate(`/recipes/${recipeId}`);
    };
    return (
        <>
            <SearchBar onSearch={handleSearch}/>
            <div className={'container'}>
                {user && <Sidebar user={user} className="sidebar"/>}
                <div className={"app-main"}>
                    {user ? (
                        <>
                            {/*<Button variant="contained" onClick={logout}>Log Out</Button>*/}
                            <h2>Your Recipes</h2>
                            {recipes && recipes.length > 0 && recipes.map((recipe) => (
                                <FoodCard
                                    key={recipe.id}
                                    recipe={recipe}
                                    onClick={() => handleCardClick(recipe.id)}
                                />
                            ))}
                            <h2>Liked Recipes</h2>
                            {likedRecipes.map((recipe) => (
                                <FoodCard
                                    key={recipe.id}
                                    recipe={recipe}
                                    onClick={() => handleCardClick(recipe.id)}
                                />
                            ))}
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
                    )
                    }
                </div>
            </div>
        </>
    )
        ;
}

export default Dashboard;