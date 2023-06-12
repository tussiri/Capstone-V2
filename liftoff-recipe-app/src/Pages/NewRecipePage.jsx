import React, {useState, useContext} from 'react'
import {Button, TextField, Box} from '@mui/material';
import {UserContext} from "../stores/UserStore";
import authAxios from "../utility/authAxios";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";

import Logo from "../Assets/MealifyLogoNoBG100x100.png";
import Typography from "@mui/material/Typography";
import Grid from "@mui/material/Grid";
import {useNavigate} from "react-router-dom";





const NewRecipePage = () => {


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
    const [recipe, setRecipe] = useState({
            // id: '',
            name: '',
            category: '',
            description: '',
            time: '',
            servings: '',
            ingredients: [],
            directions: '',
        })


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
    const handleChange = (e) => {
            const {name, value} = e.target;
            if (name === 'ingredients') {
                if (value.trim() === '') {
                    setRecipe({
                        ...recipe,
                        [name]: [],
                    });
                } else {
                    setRecipe({
                        ...recipe,
                        [name]: value.split(',').map(item => item.trim()),
                    });
                }
            } else {
                setRecipe({
                    ...recipe,
                    [name]: value,
                });
            }
        }

    return (
        <div>
        <Box sx={{mt: 3}}>
            <img src={Logo}/>
            <Typography component="h1" variant="h5">New Recipe</Typography>
               <Box
                  sx={{
                     maxWidth:'100%',
                     marginTop: 1,
                     display: 'flex',
                     flexDirection: 'column',
                     alignItems: 'center',
                  }}
               >
               <Box component="form" noValidate sx={{ mt: 3, maxWidth:'60%'}}>
                  <Grid container spacing={2}>
                      <Grid item xs={12}>
                         <TextField
                            name="name"
                            label="Recipe Name"
                            required
                            id="recipeName"
                            fullWidth
                            onChange={handleChange}
                         />
                      </Grid>

                      <Grid item xs={12}>
                         <TextField
                            name="description"
                            label="Description"
                            required
                            id="description"
                            fullWidth
                            onChange={handleChange}
                         />
                      </Grid>

                      <Grid item xs={12}>
                         <TextField
                            name="category"
                            label="Category"
                            required
                            id="category"
                            fullWidth
                            onChange={handleChange}
                         />
                      </Grid>
                      <Grid item xs={12}>
                         <TextField
                            name="time"
                            label="Time to Cook"
                            required
                            id="time"
                            fullWidth
                            onChange={handleChange}
                         />
                      </Grid>

                      <Grid item xs={12}>
                         <TextField
                            name="ingredients"
                            label="Ingredients List"
                            required
                            id="ingredients"
                            onChange={handleChange}
                            fullWidth
                            multiline
                            rows={3}
                         />
                      </Grid>

                      <Grid item xs={12}>
                         <TextField
                            name="directions"
                            label="Directions"
                            required
                            id="directions"
                            onChange={handleChange}
                            fullWidth
                            multiline
                            rows={4}
                         />
                      </Grid>
                  </Grid>
                  <Button
                     type="submit"
                     variant="contained"
                     sx={{ mt: 3, mb: 2, color: 'white' }}
                  >
                  Submit Recipe
                  </Button>
               </Box>
            </Box>
        </Box>
        </div>
    );
}

export default NewRecipePage;