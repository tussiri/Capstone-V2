import React, {useEffect, useState} from 'react';
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import axios from "axios"
import authAxios from "../utility/authAxios";
import {useNavigate, useParams} from "react-router-dom";
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';

function UpdateRecipe() {
    const navigate = useNavigate()
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
                const response = await authAxios.get(`http://localhost:8080/recipes/${recipeId}`)
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
    const updateRecipe = async (e) => {
        e.preventDefault();
        console.log(recipeId)

        try {
            await authAxios.put(`http://localhost:8080/recipes/update/${recipeId}`, recipe)
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
                       Submit Update
                       </Button>
                    </Box>
                </Box>
            </Box>
        </div>
    );
}

export default UpdateRecipe;