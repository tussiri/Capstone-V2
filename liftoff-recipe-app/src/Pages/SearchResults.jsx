import React, {useState, useEffect} from "react";
import {useLocation} from "react-router-dom";
import {useNavigate} from "react-router-dom";

import unAuthAxios from '../utility/unAuthAxios'
import authAxios from "../utility/authAxios";
import FoodCard from "../Components/FoodCard";
import LoadingScreen from "./LoadingPage";


function SearchResults() {
    const location = useLocation();
    const query = location.state?.query || '';
    const [searchResults, setSearchResults] = useState([]);
    const navigate=useNavigate();
    const [isLoading, setIsLoading]=useState(false);

    useEffect(() => {
        if(query) {
        setIsLoading(true);
            unAuthAxios.get(query)
                .then(response => {
                    console.log('Search query response:', response.data.data);
                    if(response.data.data && response.data.data.content) {
                        setSearchResults(response.data.data.content);
                    }
                })
                .catch(error => {
                    console.error(error);
                    navigate('/login')
                })
                .finally(()=>{
                    setIsLoading(false);
                })
            ;
        }
    }, [query, navigate]);

    if (isLoading) {
        return <LoadingScreen />
    }

    if (searchResults.length > 0) {
        return searchResults.map(recipe => (
            <FoodCard recipe={recipe} key={recipe.id} onClick={()=>navigate(`/recipes/${recipe.id}`)} />
        ));
    }

    return null;
};

export default SearchResults;