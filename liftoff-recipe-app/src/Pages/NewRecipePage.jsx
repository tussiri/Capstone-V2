import React, {useState, useContext} from 'react'
import {Button, TextField, Box} from '@material-ui/core';
import {makeStyles} from '@material-ui/core/styles';
import {UserContext} from "../stores/UserStore";
import authAxios from "../utility/authAxios";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";

import Logo from "../Assets/MealifyLogoNoBG100x100.png";
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";
import {useNavigate} from "react-router-dom";


const useStyles = makeStyles((theme) => ({
    form: {
        display: 'flex',
        flexDirection: 'column',
        margin: 'auto',
        width: 'fit-content',
    },
    textField: {
        margin: theme.spacing(1),
    },
    submitButton: {
        margin: theme.spacing(3),
    },
}));


const NewRecipePage = () => {
    const classes = useStyles();

    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [category, setCategory] = useState('');
    const [ingredients, setIngredients] = useState('');
    const [directions, setDirections] = useState('');
    const [time, setTime] = useState('');
    const [picture, setPicture] = useState('');
    const [allergens, setAllergens] = useState('');
    const navigate = useNavigate();
    const userId = localStorage.getItem('userId');


    const {user} = useContext(UserContext);

    const handleSubmit = async (e) => {
        e.preventDefault();

        const formattedIngredients = ingredients.split(',').filter(ingredient => ingredient.trim() !== '');  // remove empty strings from array

        try {
            const response = await authAxios.post(`http://localhost:8080/recipes/${userId}`, {
                name: name,
                description: description,
                category: category,
                ingredients: formattedIngredients,
                directions: directions,
                time: time,
                picture: picture,
                // allergens: allergens
            });
                console.log("recipe sent: ", response)

            if (response.status === 201) {
                console.log("Recipe created successfully");
                navigate('/');
            } else {
                console.error("Error creating recipe: ", response);
            }

        } catch (error) {
            console.error("Error creating recipe: ", error);
        }
    }

    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline/>
            <Box sx={{marginTop: 8, display: 'flex', flexDirection: 'column', alignItems: 'center',}}>
                <img src={Logo}/>
                <Typography component="h1" variant="h5">
                    Add Recipe
                </Typography>
                <Box component="form" noValidate autoComplete="off" onSubmit={handleSubmit} sx={{mt: 3}}>
                    <Grid container spacing={2}>
                        <Grid item xs={12} sm={6}>
                            <TextField
                                className={classes.textField}
                                label="Recipe Name"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                            />
                            <TextField
                                className={classes.textField}
                                label="Description"
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                            />
                            <TextField
                                className={classes.textField}
                                label="Category"
                                value={category}
                                onChange={(e) => setCategory(e.target.value)}
                            />
                            <TextField
                                className={classes.textField}
                                label="Ingredients (Comma Separated)"
                                value={ingredients}
                                onChange={(e) => setIngredients(e.target.value)}
                            />
                            <TextField
                                className={classes.textField}
                                label="Directions"
                                value={directions}
                                onChange={(e) => setDirections(e.target.value)}
                            />
                            <TextField
                                className={classes.textField}
                                label="Time"
                                value={time}
                                onChange={(e) => setTime(e.target.value)}
                            />
                            <TextField
                                className={classes.textField}
                                label="Picture URL"
                                value={picture}
                                onChange={(e) => setPicture(e.target.value)}
                            />
                            {/*<TextField*/}
                            {/*    className={classes.textField}*/}
                            {/*    label="Allergens"*/}
                            {/*    value={allergens}*/}
                            {/*    onChange={(e) => setAllergens(e.target.value)}*/}
                            {/*/>*/}
                            <Button type="submit" className={classes.submitButton} variant="contained" color="primary">
                                Add Recipe
                            </Button>
                        </Grid>
                    </Grid>
                </Box>
            </Box>
        </Container>
    );
}

export default NewRecipePage;