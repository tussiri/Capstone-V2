import React, {useEffect, useState} from "react"
import Card from '@mui/material/Card'
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import CardActionArea from '@mui/material/CardActionArea';
import stockImage from '../Assets/MealifyNoImage.png';
import '../Styles/FoodCard.css'
import axios from "axios";
import IconButton from '@mui/material/IconButton';
import FavoriteIcon from '@mui/icons-material/Favorite';
import {useParams} from "react-router-dom";
import authAxios from "../utility/authAxios";
import {ThumbDown, ThumbUp} from "@mui/icons-material";

function FoodCard({recipe, onClick, user, token}) {
    console.log('FoodCard render');
    const [isClicked, setIsClicked] = useState(false);
    const [isFavorite, setIsFavorite] = useState(false);
    const recipeDetails = recipe ? recipe : recipe;
    const userId = localStorage.getItem("userId")
    const {recipeId} = useParams();
    // const userId = localStorage.getItem("userId")



    const handleClick = () => {
        setIsClicked(!isClicked);
        onClick();
    }

    const onFavorite = async (event) => {
        event.stopPropagation();
        try {
            console.log("Before favorite API request");
            const response = await authAxios.post(`${process.env.REACT_APP_BACKEND_URL}/users/${userId}/favorites/${recipeDetails.id}`);
            console.log("Heart clicked: ", response.data.data);
            setIsFavorite(true);
        } catch (error) {
            console.error(error);
        }
    };

    const onUnfavorite = async (event) => {
        event.stopPropagation();
        try {
            console.log("Before unfavorite API request");
            const response = await authAxios.delete(`${process.env.REACT_APP_BACKEND_URL}/users/${userId}/favorites/${recipeDetails.id}`);
            console.log(response.data.data);
            setIsFavorite(false);
            console.log('After unlike:', isFavorite);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        console.log('isFavorite changed:', isFavorite);
    }, [isFavorite]);

    useEffect(() => {
        const checkIfFavorited = async () => {
            try {
                const response = await authAxios.get(`${process.env.REACT_APP_BACKEND_URL}/users/${userId}/favorites`);
                const favoritedRecipes = response.data.data;

                const isFavorited = favoritedRecipes.some(favoritedRecipe => favoritedRecipe.id === recipeDetails.id);
                setIsFavorite(isFavorited);
            } catch (error) {
                console.error(error);
            }
        };
        checkIfFavorited();
    }, [userId, recipeDetails.id]);



    return (
        <Card onClick={handleClick} className='food-card'>
            <CardActionArea>
                <CardMedia
                    className='card-media'
                    component="img"
                    height="150"
                    image={recipe.picture ? recipe.picture : stockImage} loading="lazy"
                    alt={recipe.name}
                />
                <CardContent>
                    <Typography gutterBottom variant="h5" component="div">
                        {recipe.name}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        {recipe.description}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        Category: {recipe.category}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        Preparation Timesss: {recipe.time} minutes
                    </Typography>
                    <IconButton aria-label="add to favorites" onClick={onFavorite}>
                        <ThumbUp color={isFavorite ? "primary" : "action"} />
                    </IconButton>
                    <IconButton aria-label="remove from favorites" onClick={onUnfavorite}>
                        <ThumbDown color={isFavorite ? "primary":"action"}/>
                    </IconButton>
                </CardContent>
            </CardActionArea>
        </Card>
    );
}

export default FoodCard;