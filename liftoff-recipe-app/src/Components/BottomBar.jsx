import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import { styled, alpha } from '@mui/material/styles';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import InputBase from '@mui/material/InputBase';
import Logo from "../Assets/MealifyLogoIcon(100x100).png";
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import AdbIcon from '@mui/icons-material/Adb';
import BarLogo from "../Assets/MealifyLogoNavBar.png";
import SearchBar from "../Components/SearchBar"
import {UserContext} from "../stores/UserStore";
import axios from 'axios';
import authAxios from "../utility/authAxios";


function BottomBar(){

  const [isSearching, setIsSearching] = useState(false);
  const [hasSearched, setHasSearched] = useState(false);
  const [searchQuery, setSearchQuery] = useState('')
  const {user, logout} = useContext(UserContext);
  const [isLoading, setIsLoading] = useState(true);
  const userId = localStorage.getItem("userId")
  const [recipes, setRecipes] = useState([]);

  const handleSearch = (query) => {
          setIsSearching(true);
          setSearchQuery(query);
  }

  const handleSearchComplete = () => {
          setIsSearching(false);
          setHasSearched(true)
          setSearchQuery('')

  }

  useEffect(() => {
    setIsLoading(true)
      const fetchRecipes = async () => {
        if (user) {
          console.log(user)
            try {
              const response = await authAxios.get(`http://localhost:8080/recipes/user/${userId}`);
                setRecipes(response.data.data.content);
                  console.log(response.data.data.content)
                    // setIsLoading(false);
            } catch (error) {
              console.error(error);
            }
        } else {
          try {
            const response = await axios.get(
              "http://localhost:8080/recipes?page=0&size=8"
            );
              // const data = response.data.data;
            console.log(response.data.data);
            setRecipes(response.data.data.content);
          } catch (error) {
            console.error(error);
          }
        }
         setIsLoading(false);

        };

         fetchRecipes();
        }, [user]);

  useEffect(() => {
    if (isSearching && hasSearched) {
      setIsLoading(true);
    }
  }, [isSearching, hasSearched]);


return(

    <AppBar position='fixed' sx={{ top:'auto', bottom: 0}}>
        <Container>
                <Toolbar sx={{ justifyContent: 'center' }}>
                    <Box>
                        <SearchBar onSearch={handleSearch}/>
                    </Box>
                </Toolbar>
        </Container>
    </AppBar>

)
}

export default BottomBar