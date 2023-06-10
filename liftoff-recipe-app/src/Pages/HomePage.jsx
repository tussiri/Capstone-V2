import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import Button from "@mui/material/Button";
import axios from 'axios';

import FoodCard from "../Components/FoodCard"
import ChickenPic from "../Assets/Rectangle 65.png"
import PastaPic from "../Assets/Rectangle 66.png"
import TacoPic from "../Assets/Rectangle 67.png"
import ChocoPic from "../Assets/Rectangle 68.png"
import TortaPic from "../Assets/Rectangle 69.png"
import TirraPic from "../Assets/Rectangle 70.png"
import NavBar from "../Components/NavBar"
import SearchBar from "../Components/SearchBar"
import SearchResults from "./SearchResults";
import LoadingScreen from "./LoadingPage";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";
import Sidebar from "../Components/SideBar";


function HomePage() {
    const [isSearching, setIsSearching] = useState(false);
    const [hasSearched, setHasSearched] = useState(false);
    const [searchQuery, setSearchQuery] = useState('')
    const [isLoading, setIsLoading] = useState(true);
    const {user, logout} = useContext(UserContext);
    const [recipes, setRecipes] = useState([]);
    const [likedRecipes, setLikedRecipes] = useState([]);
    const userId = localStorage.getItem("userId")
    // const [userRecipes, setUserRecipes] = useState([]);


    const navigate = useNavigate();

    const handleSearch = (query) => {
        setIsSearching(true);
        setSearchQuery(query);
    }

    const handleSearchComplete = () => {
        setIsSearching(false);
        setHasSearched(true)
    }

    const handleCardClick = (recipeId) => {
        navigate(`/recipes/${recipeId}`);
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/")
    }

    useEffect(() => {
        setIsLoading(true)
        const fetchRecipes = async () => {
            try {
                const generalResponse = await axios.get("http://localhost:8080/recipes?page=0&size=8");
                setRecipes(generalResponse.data.data.content);

                if (user) {
                    const userResponse = await authAxios.get(`http://localhost:8080/recipes/user/${userId}`);
                    setLikedRecipes(userResponse.data.data.content); // set another state for user's recipes
                }
            } catch (error) {
                console.error(error);
            }
            setIsLoading(false);
        };

        fetchRecipes();
    }, [user]);

    useEffect(() => {
        if (isSearching && hasSearched) {
            setIsLoading(true);
        }
    }, [isSearching, hasSearched]);


    if (isLoading) {
        return <LoadingScreen/>
    }

    return (
        <>
            <NavBar/>
            <SearchBar onSearch={handleSearch}/>
            <div className="container">
                <Sidebar user={user} className="sidebar"/>
                <div className="app-main">
                    <h2>All Recipes</h2>
                    {recipes.map((recipe) => (
                        <FoodCard
                            key={recipe.id}
                            recipe={recipe}
                            onClick={() => handleCardClick(recipe.id)}
                        />
                    ))}
                </div>
                {isSearching && hasSearched ? (
                    <LoadingScreen/>
                ) : (
                    <SearchResults query={searchQuery} onSearchComplete={handleSearchComplete}/>
                )}
            </div>
        </>
    )
        ;
}

export default HomePage;