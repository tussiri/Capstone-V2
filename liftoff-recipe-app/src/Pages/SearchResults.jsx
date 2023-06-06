import React, {useState, useEffect} from "react";
import {useLocation} from "react-router-dom";
import authAxios from "../utility/authAxios";
import FoodCard from "../Components/FoodCard";
import {useNavigate} from "react-router-dom";


function SearchResults() {
    const [results, setResults] = useState([]);
    const location = useLocation();
    const query = location.state?.query || '';
    // const {url} = location.state || {}
    const [searchResults, setSearchResults] = useState([]);
    const navigate=useNavigate();

    useEffect(() => {
        if (query.trim() === "http://localhost:8080/recipes/search?") {
            authAxios.get('http://localhost:8080/recipes/random')
                .then(response => {
                    setSearchResults(response.data)
                })
                .catch(error => {
                    console.error(error);
                    navigate('/login')
                });
        } else {
            authAxios.get(query)
                .then(response => {
                    setSearchResults(response.data.data);
                })
                .catch(error => {
                    console.error(error);
                    navigate('/login')
                });
        }
    }, [query, navigate]);

    return (
        <div>
            <h2>Search Results</h2>
            {searchResults.map(recipe => (
                <FoodCard recipe={recipe} key={recipe.id}></FoodCard>
            ))}
        </div>
    );
};

export default SearchResults;