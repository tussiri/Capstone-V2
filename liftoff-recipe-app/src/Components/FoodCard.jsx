import React from "react"
import Card from '@mui/material/Card'
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import CardActionArea from '@mui/material/CardActionArea';
import stockImage from '../Assets/logo-removebg-preview 1.png';

function FoodCard({recipe, onClick}) {
    return (
        <Card onClick={onClick} className='recipe-card'>
            <CardActionArea>
                <CardMedia
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
                    <Typography variant="body2" color="text.secondary">
                        Directions: {recipe.directions}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        Rating: {recipe.rating}
                    </Typography>
                </CardContent>
            </CardActionArea>
        </Card>
    );
}

export default FoodCard;