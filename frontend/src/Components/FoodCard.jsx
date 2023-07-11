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

function FoodCard({recipe, onClick, user}) {
    const [isClicked, setIsClicked] = useState(false);
    const [isFavorite, setIsFavorite] = useState(false);
    // const userId = localStorage.getItem("userId")
    const {recipeId} = useParams();
    const userId = localStorage.getItem("userId")



    const handleClick = () => {
        setIsClicked(!isClicked);
        onClick();
    }

    const handleFavoriteClick = async (event) => {
        try {
            console.log("Heart icon clicked");
            event.stopPropagation();
            if (!isFavorite) {
                await authAxios.post(`http://localhost:8080/favorites/${userId}/${recipe.id}`);
            } else {
                await authAxios.delete(`http://localhost:8080/favorites/${recipe.id}`);
            }
            setIsFavorite(!isFavorite);
        } catch (error) {
            console.error("Error while favoriting:", error.message);
        }
    };

    useEffect(() => {
        const fetchFavoriteStatus = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/favorites/user/${userId}/recipe/${recipeId}`);
                setIsFavorite(response.data.data.content != null);
            } catch (error) {
                console.error(error);
            }
        };

        fetchFavoriteStatus();
    }, [userId, recipeId]);

    return (
        <Card onClick={handleClick} className='food-card'>
            <CardActionArea>
                <CardMedia
                    className='card-media'
                    component="img"
                    height="150"
                    image={recipe.picture ? recipe.picture : stockImage}
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
                        Preparation Time: {recipe.time} minutes
                    </Typography>
                    <IconButton aria-label="add to favorites" onClick={handleFavoriteClick}>
                        <FavoriteIcon color={isFavorite ? "primary" : "action"} />
                    </IconButton>
                </CardContent>
            </CardActionArea>
        </Card>
    );
}

export default FoodCard;