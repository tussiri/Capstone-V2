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
            if (user) {
                console.log(user)
                try {
                    const response = await authAxios.get(`http://localhost:8080/recipes/user/${userId}`);
                    setRecipes(response.data.data.content);
                    console.log(response.data.data.content)
                    // setIsLoading(false);
                } catch (error) {
                    console.error(error);
                }
            } else {
                try {
                    const response = await axios.get(
                        "http://localhost:8080/recipes?page=0&size=8"
                    );
                    // const data = response.data.data;
                    console.log(response.data.data);
                    setRecipes(response.data.data.content);
                } catch (error) {
                    console.error(error);
                }
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
                <span>&nbsp;&nbsp;&nbsp;</span>
                <span>&nbsp;&nbsp;&nbsp;</span>
            {/* New Recipe button only visible to logged-in users */}
            {user && (
                <>
                    <span>&nbsp;&nbsp;&nbsp;</span>
                    <Link to="newrecipe">
                        <Button variant="contained">New Recipe</Button>
                    </Link>
                </>
            )}

            <span>&nbsp;&nbsp;&nbsp;</span>
            <span>&nbsp;&nbsp;&nbsp;</span>
{/*             <Link to="login"> */}
{/*                 <Button variant="contained">Log In</Button> */}
{/*             </Link> */}
{/*             <span>&nbsp;&nbsp;&nbsp;</span> */}
{/*             <Link to="signup"> */}
{/*                 <Button variant="contained">Sign Up</Button> */}
{/*             </Link> */}
            <p></p>

            <div className='container'>
                {user && <Sidebar user={user} className="sidebar"/>}
                <div className="app-main">
                    {user ? (
                        <>
                            <Button variant="contained" onClick={logout}>Log Out</Button>
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

                            <div className="recipe-section">
                                {!user && recipes.map((recipe) => (
                                    <FoodCard
                                        key={recipe.id}
                                        recipe={recipe}
                                        onClick={() => handleCardClick(recipe.id)}
                                    />
                                ))}
                            </div>

                            {
                                isSearching && hasSearched ? (
                                    <LoadingScreen/>
                                ) : (
                                    <SearchResults query={searchQuery} onSearchComplete={handleSearchComplete}/>
                                )
                            }
                        </>
                    )}
                </div>
            </div>
        </>
    );
}

export default HomePage;