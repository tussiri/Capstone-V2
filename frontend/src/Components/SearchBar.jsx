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


const SearchBar = ({onSearch}) => {
    const [inputValue, setInputValue] = useState('')
    const [selectedOption, setSelectedOption] = useState('All');
    const [searchTerm, setSearchTerm] = useState('');
    const navigate = useNavigate();

    const handleOptionChange = (event) => {
        setSelectedOption(event.target.value);
        console.log(`selected option: `, event.target.value)
    };

    const handleSearchTermChange = (event) => {
        setSearchTerm(event.target.value);
        console.log('search term: ', event.target.value)
        if (onSearch) {
            onSearch(event.target.value)
        }
    };

    const handleSearch = () => {

        let url = "http://localhost:8080/recipes/search?";
        console.log(`Selected Option: ${selectedOption}, Search Term: ${searchTerm}`);

        switch (selectedOption) {
            case 'All':
                url += `name=${searchTerm}`
                break;
            case 'By Ingredient':
                const encodedSearchTerm = encodeURIComponent(searchTerm);
                url += `ingredients=${encodedSearchTerm}`;
                break;
            case 'By Time to Prepare':
                url += `time=<${searchTerm}`
                break;
            case 'Category':
                url += `category=${searchTerm}`
                break;
            // case 'By rating':
            //     url += `rating=<${searchTerm}`
            //     break;
            default:
                break;
        }


        console.log('Url: ', url)

        // const token = localStorage.getItem('token')
        // console.log("Retrieved token:", token);


        navigate('/searchresults', {state: {query: url}});
    };

    return (
        <div>
            <ThemeProvider theme={theme2}>
            <TextField sx={{ m: 1,
                            color: 'white',
                            input: {color: 'white'},
                            maxWidth: 150,
                            '& label': {
                               color: 'white'},
                            '& label.Mui-focused': {
                               color: 'white'},
                            '& .MuiInput-underline:after': {
                               borderBottomColor: 'white'},
                            '& .MuiOutlinedInput-root': {
                               '& fieldset': {
                                  borderColor: 'white'},
                               '&:hover fieldset': {
                                  borderColor: 'white',
                                  borderWidth: '0.15rem'},
                               '&.Mui-focused fieldset': {
                                  borderColor: 'white'},
                            }
                         }}
                       autoComplete='off'
                       size="small"
                       id="searchfield"
                       label="Search for..."
                       type="text"
                       value={searchTerm}
                       onChange={handleSearchTermChange}
                       placeholder="Search"
                       required

            />
            <FormControl sx={{ m: 1, minWidth: 150, input: {color: 'white'} }} size="small">
                <InputLabel id="searchtype" sx={{color: 'white'}}>Search by...</InputLabel>
                <Select

                    sx={{color: 'white',
                      borderColor: 'white',
                      '& .MuiOutlinedInput-notchedOutline': {
                         borderColor: 'white'},
                      '& .MuiSvgIcon-root': {
                         color: 'white'},
                      '& .Mui-focused .MuiOutlinedInput-notchedOutline':{
                         borderColor: 'white'},
                      '&:hover .MuiOutlinedInput-notchedOutline': {
                         borderColor: 'white', borderWidth: '0.15rem'},
                         '&:after': {
                                     borderColor: 'white',
                                 },
                      }}
                    labelId="searchtype-label"
                    id="search-type"
                    label="Search By"
                    value={selectedOption}
                    onChange={handleOptionChange}

                >
                    <MenuItem value="All">All Recipes</MenuItem>
                    <MenuItem value="By Ingredient">Ingredients</MenuItem>
                    <MenuItem value="By Time to Prepare">Time To Prepare</MenuItem>
                </Select>
            </FormControl>
                <IconButton
                    sx={{m:1, color:'white'}}
                    variant="contained"
                    onClick={handleSearch}
                >
                    <SearchIcon fontSize="inherit"/></IconButton>
            </ThemeProvider>
        </div>
    );
};

export default SearchBar;