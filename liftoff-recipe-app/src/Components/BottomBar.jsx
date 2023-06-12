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