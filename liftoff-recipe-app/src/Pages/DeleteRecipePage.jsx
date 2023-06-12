import {useContext, useEffect, useState} from "react";
import {useNavigate, useParams} from 'react-router-dom';
import axios from "axios"
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";

import {UserContext} from "../stores/UserStore";
import authAxios from "../utility/authAxios";
import NavBar from "../Components/NavBar";


function DeleteRecipe() {
    const navigate = useNavigate();
    const {recipeId} = useParams();
    const {user, parseJwt} = useContext(UserContext);
    const [recipeName, setRecipeName] = useState("");

    useEffect(() => {
        const fetchRecipe = async () => {
            const response = await authAxios.get(`http://localhost:8080/recipes/${recipeId}`);
            setRecipeName(response.data.name);
        };
        fetchRecipe();
    }, [recipeId])
    const deleteRecipe = async (e: any) => {
        const token = localStorage.getItem('token');
        const decodedToken = parseJwt(token);
        const userId = decodedToken ? decodedToken.userId : null;
        // const userId=localStorage.getItem('userId');

        try {
            console.log("UserId Passed: ", userId)
            console.log("Token: ", token)
            console.log("RecipeId: ", recipeId);
            await authAxios.delete(`http://localhost:8080/recipes/delete/${recipeId}`, {
                headers: {
                    'userId': userId
                }
            })
            alert("Delete Successful")
            navigate('/dashboard')
        } catch (err) {
            alert("Delete Error! Check the console for more information")
            console.log('Error while attempting to delete recipe: ', err)
        }
    };

    const handleDelete = (e) => {
        e.preventDefault();
        if (window.confirm(`You are about to delete the recipe ${recipeName}. This action cannot be reversed. If you would like to proceed, please click OK.`)) {
            deleteRecipe();
        } else {
            navigate('/dashboard');
        }
    }

    return (
        <div>
            <Box>
                <h1>Delete Recipe</h1>
                <Box
                    sx={{
                        minHeight: 330,
                        display: "flex",
                        flexDirection: "column",
                        justifyContent: "space-between",
                        color: 'white'
                    }}
                    component="form"
                    onSubmit={handleDelete}
                >
                    {/*<TextField id="id" label="Recipe id" variant="outlined" value={recipeId}*/}
                    {/*           onChange={(e) => setRecipeId(e.target.value)}/>*/}
                    <Button type="submit" variant="contained">
                        Delete
                    </Button>
                </Box>
            </Box>
        </div>
    );
}

export default DeleteRecipe;