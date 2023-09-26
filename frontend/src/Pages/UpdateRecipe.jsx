import React, {useContext, useEffect, useState} from 'react';
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import authAxios from "../utility/authAxios";
import {useNavigate, useParams} from "react-router-dom";
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import {UserContext} from "../stores/UserStore";

function UpdateRecipe() {
    const navigate = useNavigate()
    const {user, setUser, userId} = useContext(UserContext);

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
    const {recipeId} = useParams();

    useEffect(() => {
        const fetchRecipe = async () => {
            try {
                const response = await authAxios.get(`${process.env.REACT_APP_BACKEND_URL}/recipes/${recipeId}`)
                setRecipe(response.data.data);
            } catch (error) {
                console.log("Error fetching recipes: ", error);
            }
        };
        fetchRecipe();
    }, [recipeId]);
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
                    [name]: parseIngredients(value),
                });
            }
        } else {
            setRecipe({
                ...recipe,
                [name]: value,
            });
        }
    }

    const parseIngredients = (input) => {
        return input.split(',').map(item => {
            const [name, quantity] = item.split('(');
            return {
                name: name.trim(),
                quantity: quantity ? quantity.replace(')', '').trim() : ''
            };
        });
    }

    const updateRecipe = async (e) => {
        e.preventDefault();
        console.log(recipeId)

        try {
            await authAxios.put(`${process.env.REACT_APP_BACKEND_URL}/recipes/update/${recipeId}`, recipe, {
                headers: {
                    'userId': user.userId // Include the userId from the context
                }
            })
                .then(() => {
                    alert("Recipe updated successfully.");
                    navigate(`/recipes/${recipeId}`);
                })
        } catch (error) {
            alert("Error while updating recipe.");
            console.log('error', error);
        }
    };
    return (
        <div>
            <Box sx={{mt: 5}}>
                <Typography component="h1" variant="h5">Update Recipe</Typography>
                <Box
                    sx={{
                        maxWidth:'100%',
                        marginTop: 4,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        }}
                >
                    <Box component="form" noValidate sx={{ mt: 3, maxWidth:'60%'}} onSubmit={updateRecipe}>
                       <Grid container spacing={2}>
                           <Grid item xs={12}>
                              <TextField
                                name="name"
                                label="Recipe Name"
                                required
                                id="recipeName"
                                fullWidth
                                onChange={handleChange}
                                value={recipe.name}
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
                                value={recipe.description}
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
                                value={recipe.category}
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
                                    value={recipe.time}
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
                                value={recipe.ingredients.map(ing => ing.quantity ? `${ing.name} (${ing.quantity})` : ing.name).join(', ')}
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
                                value={recipe.directions}
                              />
                           </Grid>
                       </Grid>
                       <Button
                         type="submit"
                         variant="contained"
                         sx={{ mt: 3, mb: 2, color: 'white' }}
                       >
                       Submit Update
                       </Button>
                    </Box>
                </Box>
            </Box>
        </div>
    );
}

export default UpdateRecipe;