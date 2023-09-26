import React from 'react';
import Box from '@mui/material/Box';
import Button from '@mui/material/Button';
import {useNavigate} from 'react-router-dom';
import {ReactComponent as NoResultsSVG} from '../Assets/NoResults.svg';

function NoResultsFound() {
    const navigate = useNavigate();

    return (
        <Box
            sx={{
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'center',
                alignItems: 'center',
                height: '80vh',
                textAlign: 'center',
                gap: '2rem'
            }}
        >
            <NoResultsSVG style={{ width: '200px', height: '200px' }}/>
            <h2>No Results Found</h2>
            <p>Sorry, we couldn't find any recipes matching your search criteria. Try adjusting your search or browse our popular recipes.</p>
            <Button variant="contained" color="primary" onClick={() => navigate('/recipes/random')}>
                Browse Popular Recipes
            </Button>
        </Box>
    );
}

export default NoResultsFound;
