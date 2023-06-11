import React, {useEffect, useState} from 'react';
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import axios from "axios"
import authAxios from "../utility/authAxios";
import {useNavigate, useParams} from "react-router-dom";
import NavBar from "../Components/NavBar";

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
            <Box>
                <h1>Update Recipe</h1>
                <Box
                    sx={{
                        minHeight: 330,
                        display: "flex",
                        flexDirection: "column",
                        justifyContent: "space-between",
                    }}
                    component="form"
                    onSubmit={updateRecipe}
                >
                    {/*<TextField name="id" label="Recipe id" variant="outlined" onChange={handleChange}/>*/}
                    <TextField name="name" label="Recipe Name" variant="outlined" onChange={handleChange}/>
                    <TextField name="description" label="description" variant="outlined" onChange={handleChange}/>
                    <TextField name="category" label="category" variant="outlined" onChange={handleChange}/>
                    <TextField name="time" label="Time" variant="outlined" onChange={handleChange}/>
                    <TextField name="ingredients" label="Ingredients" variant="outlined" onChange={handleChange}/>
                    <TextField name="directions" label="Directions" variant="outlined" onChange={handleChange}/>
                    <Button type="submit" variant="contained">
                        Update
                    </Button>
                </Box>
            </Box>
        </div>
    );
}

export default UpdateRecipe;