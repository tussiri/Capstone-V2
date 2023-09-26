import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import axios from 'axios';
import '../Styles/HomePage.css';
import heroVideo from '../Assets/MealifyHero.mp4';

import FoodCard from "../Components/FoodCard"
import NavBar from "../Components/NavBar"
import SearchBar from "../Components/SearchBar"
import SearchResults from "./SearchResults";
import LoadingWave from "./LoadingWave";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";
import Sidebar from "../Components/SideBar";
import Grid from "@mui/material/Grid";
import Box from '@mui/material/Box';


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

    function shuffleArray(array) {
        for (let i = array.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [array[i], array[j]] = [array[j], array[i]];
        }
        return array;
    }


    const navigate = useNavigate();

    const handleSearch = (query) => {
        setIsSearching(true);
        setSearchQuery(query);
    }

    const handleSearchComplete = () => {
        setIsSearching(false);
        setHasSearched(true)
    }

    const handleCardClick = async (recipeId) => {
        try {
            const recipeResponse = await axios.get(`${process.env.REACT_APP_BACKEND_URL}/recipes/${recipeId}`);
            navigate(`/recipes/${recipeId}`);
        } catch (error) {
            console.error(error);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/")
    }

    useEffect(() => {
        setIsLoading(true)
        const fetchRecipes = async () => {
            try {
                const generalResponse = await axios.get("${process.env.REACT_APP_BACKEND_URL}/recipes?page=0&size=12");
                const shuffledRecipes = shuffleArray(generalResponse.data.data.content);
                setRecipes(shuffledRecipes.slice(0, 14));
                // setRecipes(generalResponse.data.data.content);

                if (user) {
                    const userResponse = await authAxios.get(`V/recipes/user/${userId}`);
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
        return <LoadingWave/>
    }

    return (
        <>
            <div className="hero-section">
                <div className="hero-background">
                    <video autoPlay loop muted>
                        <source src={heroVideo} type="video/mp4"/>
                    </video>
                </div>
                <div className="hero-content">
                    <h1>Welcome to Mealify!</h1>
                    <p>Discover and share amazing recipes with the world.</p>
                    <div className="hero-buttons">
                        <button className="explore-button" onClick={()=>navigate('/allrecipes')} >Explore Recipes</button>
                        <button className="create-button" onClick={() => {
                            const token = localStorage.getItem("token");
                            if (token) {
                                navigate('/recipes/newrecipe');
                            } else {
                                navigate('/login');
                            }
                        }}>Create Your Own</button>
                    </div>
                </div>
            </div>
            <div>
                <h2 style={{ textAlign: 'center', margin: '20px 0' }}>Welcome to Mealify!</h2>
                <Box style={{ padding: '0 0' }}> {/* Added Box */}
                    <Grid container spacing={1}>
                        {recipes.map((recipe) => (
                            <Grid item key={recipe.id} xs={12} sm={5} md={3} lg={2}>
                                <FoodCard
                                    recipe={recipe}
                                    onClick={() => handleCardClick(recipe.id)}
                                />
                            </Grid>
                        ))}
                    </Grid>
                </Box>
            </div>
        </>
    )
        ;

}

export default HomePage;