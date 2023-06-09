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


const pages = ['Home', 'All Recipes', 'Random Recipe', 'Search'];
const settings = ['Account', 'My Recipes', 'Logout'];

function NavBar() {
  const [anchorElNav, setAnchorElNav] = React.useState(null);
  const [anchorElUser, setAnchorElUser] = React.useState(null);
  const [isSearching, setIsSearching] = useState(false);
  const [hasSearched, setHasSearched] = useState(false);
  const [searchQuery, setSearchQuery] = useState('')
  const {user, logout} = useContext(UserContext);
  const [isLoading, setIsLoading] = useState(true);
  const userId = localStorage.getItem("userId")
  const [recipes, setRecipes] = useState([]);

  const navigate = useNavigate();

  const handleSearch = (query) => {
          setIsSearching(true);
          setSearchQuery(query);
  }

  const handleSearchComplete = () => {
          setIsSearching(false);
          setHasSearched(true)
  }

  const handleCardClick = (recipeId) => {
          navigate(`/recipes/${recipeId}`);
  };

  const handleLogout = () => {
          localStorage.removeItem("token");
          navigate("/")
  }

  const handleOpenNavMenu = (event) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

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

  return (
    <AppBar position="static">
      <Container maxWidth="xl">
        <Toolbar disableGutters>
          <img src={BarLogo} sx={{ maxHeight:30 }}/>
          <Typography
            variant="h6"
            noWrap
            component="a"
            href="/"
            sx={{
              mr: 2,
              display: { xs: 'none', md: 'flex' },
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
          </Typography>

          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}
              color="inherit"
            >
              <MenuIcon />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}
              sx={{
                display: { xs: 'block', md: 'none' },
              }}
            >
              {pages.map((page) => (
                <MenuItem key={page} onClick={handleCloseNavMenu}>
                  <Typography textAlign="center">{page}</Typography>
                </MenuItem>
              ))}
            </Menu>
          </Box>
          <Typography
            variant="h5"
            noWrap
            component="a"
            href=""
            sx={{
              mr: 2,
              display: { xs: 'flex', md: 'none' },
              flexGrow: 1,
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.3rem',
              color: 'inherit',
              textDecoration: 'none',
            }}
          >
          </Typography>
          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
          <Link to="/homepage">
            <Button sx={{ color: 'white' }} onClick={handleCloseNavMenu}>Home</Button>
          </Link>
          <Link to="/allrecipes">
            <Button sx={{ color: 'white' }} onClick={handleCloseNavMenu}>All Recipes</Button>
          </Link>
          <Link to="/randomrecipe">
            <Button sx={{ color: 'white' }} onClick={handleCloseNavMenu}>Random Recipe</Button>
          </Link>
{/*           <Link to="/search"> */}
{/*             <Button sx={{ color: 'white' }} onClick={handleCloseNavMenu}>Search</Button> */}
{/*           </Link> */}

{/*             {pages.map((page) => ( */}
{/*               <Button */}
{/*                 key={page} */}
{/*                 onClick={handleCloseNavMenu} */}
{/*                 sx={{ my: 2, color: 'white', display: 'block' }} */}
{/*               > */}
{/*                 {page} */}
{/*               </Button> */}
{/*             ))} */}
{/*             <SearchBar onSearch={handleSearch}/> */}{/* search bar in nav bar option*/}
          </Box>

          <Box sx={{ flexGrow: 0 }}>
            <Tooltip title="Open settings">
              <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                <Avatar alt="Test Account" src="/static/images/avatar/2.jpg" />
              </IconButton>
            </Tooltip>
            <Menu
              sx={{ mt: '45px' }}
              id="menu-appbar"
              anchorEl={anchorElUser}
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={Boolean(anchorElUser)}
              onClose={handleCloseUserMenu}
            >
              {settings.map((setting) => (
                <MenuItem key={setting} onClick={handleCloseUserMenu}>
                  <Typography textAlign="center">{setting}</Typography>
                </MenuItem>
              ))}
            </Menu>
          </Box>
        </Toolbar>
      </Container>
    </AppBar>
  );
}

export default NavBar