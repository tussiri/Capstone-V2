import React, {useState} from 'react';
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import InputLabel from "@mui/material/InputLabel";
import FormControl from "@mui/material/FormControl";
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import SearchIcon from '@mui/icons-material/Search';
import IconButton from '@mui/material/IconButton';

import {useNavigate} from "react-router-dom"

import axios from "axios";
import authAxios from "../utility/authAxios";

const SearchBar = () => {
    const [selectedOption, setSelectedOption] = useState('All');
    const [searchTerm, setSearchTerm] = useState('');
    const navigate = useNavigate();

    const handleOptionChange = (event) => {
        setSelectedOption(event.target.value);
    };

    const handleSearchTermChange = (event) => {
        setSearchTerm(event.target.value);
    };

    const handleSearch = () => {
            let url = "http://localhost:8080/recipes/search?";
            switch (selectedOption) {
                case 'All':
                    url += `name=${searchTerm}`
                    break;
                case 'By Ingredient':
                    url += `ingredients=${searchTerm}`
                    break;
                case 'By Time to Prepare':
                    url += `time=<${searchTerm}`
                    break;
                default:
                    break;
            }

            // const token = localStorage.getItem('token')
            // console.log("Retrieved token:", token);

            navigate('/searchresults', { state: { query: url } });
        };

    return (
        <div>
            <TextField sx={{m: 1}} id="searchfield" label="Search for..."
                       type="text"
                       value={searchTerm}
                       onChange={handleSearchTermChange}
                       placeholder="Search"

            />
            <FormControl sx={{m: 1, minWidth: 150}}>
                <InputLabel id="searchtype">Search by...</InputLabel>
                <Select
                    labelId="searchtype-label"
                    id="search-type"
                    //value={selectedOption}
                    onChange={handleOptionChange}
                    label="Search By"
                >
                    <MenuItem value="All">All Recipes</MenuItem>
                    <MenuItem value="By Ingredient">Ingredients</MenuItem>
                    <MenuItem value="By Time to Prepare">Time To Prepare</MenuItem>
                </Select>
            </FormControl>
            <Button sx={{m: 2}} variant="contained" onClick={handleSearch} startIcon={<SearchIcon/>}>Go</Button>

            {/* another button option: <IconButton sx={{m:1}} size="large" variant="contained" onClick={handleSearch}><SearchIcon fontSize="inherit"/></IconButton> */}
        </div>
    );
};

export default SearchBar;