import React, {useState, useEffect} from "react";
import {useLocation} from "react-router-dom";
import {useNavigate} from "react-router-dom";

import unAuthAxios from '../utility/unAuthAxios'
import authAxios from "../utility/authAxios";
import FoodCard from "../Components/FoodCard";
import LoadingWave from "./LoadingWave";

import Box from '@mui/material/Box';
import Grid from "@mui/material/Grid";

import NoResultsFound from "../Pages/NoResultsFound";

function SearchResults() {
    const location = useLocation();
    const query = location.state?.query || '';
    const [searchResults, setSearchResults] = useState([]);
    const navigate = useNavigate();
    const [isLoading, setIsLoading] = useState(false);

    useEffect(() => {
        if (query) {
            setIsLoading(true);
            unAuthAxios.get(query)
                .then(response => {
                    console.log('Search query response:', response.data.data);
                    if (response.data.data && response.data.data.content) {
                        setSearchResults(response.data.data.content);
                    }
                })
                .catch(error => {
                    console.error(error);
                    navigate('/searchresults');
                })
                .finally(() => {
                    setIsLoading(false);
                });
        }
    }, [query, navigate]);


    if (isLoading) {
        return <LoadingWave/>
    }

    if (searchResults.length > 0) {
        return (
            <div>
                <Box sx={{ maxWidth: '100%', display: 'flex', flexDirection: 'column', justifyContent:'center', alignItems: 'center' }}>
                    <h2>Search results:</h2>
                    <Grid container spacing={3} justifyContent="center">
                        {searchResults.map((recipe) => (
                            <Grid item xs={12} sm={6} md={3} lg={2}>
                                <FoodCard
                                    recipe={recipe}
                                    key={recipe.id}
                                    onClick={() => navigate(`/recipes/${recipe.id}`)}
                                />
                            </Grid>
                        ))}
                    </Grid>
                </Box>
            </div>
        );
    }

    return(
        <div>
            <NoResultsFound/>
        </div>
    );
};

export default SearchResults;