import React, {useState} from 'react';
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import InputLabel from "@mui/material/InputLabel";
import FormControl, { useFormControl } from "@mui/material/FormControl";
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import SearchIcon from '@mui/icons-material/Search';
import IconButton from '@mui/material/IconButton';
import {useNavigate} from "react-router-dom"
import axios from "axios";
import authAxios from "../utility/authAxios";

import { createTheme, colors, ThemeProvider } from '@mui/material';

const theme2 = createTheme({
   palette: {
       primary: {
         main: colors.grey[50],
       },
       secondary: {
         main: colors.orange[400]
         }
       }
});
const whiteColor = colors.grey[50]


const SearchBar = ({onSearch}) => {
    const [inputValue, setInputValue] = useState('')
    const [selectedOption, setSelectedOption] = useState('All');
    const [searchTerm, setSearchTerm] = useState('');
    const navigate = useNavigate();

    const handleOptionChange = (event) => {
        setSelectedOption(event.target.value);
    };

    const handleSearchTermChange = (event) => {
        setSearchTerm(event.target.value);
        if (onSearch) {
            onSearch(event.target.value)
        }
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

        navigate('/searchresults', {state: {query: url}});
    };

    return (
        <div>
            <ThemeProvider theme={theme2}>
            <TextField sx={{ m: 1, color:'white'}}
                       size="small"
                       id="searchfield"
                       label="Search"
                       type="text"
                       value={searchTerm}
                       onChange={handleSearchTermChange}
                       placeholder="Search"

            />
            <FormControl sx={{ m: 1, minWidth: 150 }} size="small">
                <InputLabel id="searchtype">Search by...</InputLabel>
                <Select
                    labelId="searchtype-label"
                    id="search-type"
                    //value={selectedOption}
                    //onChange={handleOptionChange}
                    label="Search By"
                >
                    <MenuItem value="All">All Recipes</MenuItem>
                    <MenuItem value="By Ingredient">Ingredients</MenuItem>
                    <MenuItem value="By Time to Prepare">Time To Prepare</MenuItem>
                </Select>
            </FormControl>
{/*             <Button sx={{m: 1, color:'white'}} variant="contained" onClick={handleSearch} startIcon={<SearchIcon/>}>Search</Button> */}
                <IconButton sx={{m:1, color:'white'}} variant="contained" onClick={handleSearch}><SearchIcon fontSize="inherit"/></IconButton>
        </ThemeProvider>
        </div>
    );
};

export default SearchBar;