import {Routes, Route} from "react-router-dom";
import "./App.css";
import { createTheme, colors, ThemeProvider } from '@mui/material';
import HomePage from "./Pages/HomePage";
import SignUp from "./Pages/SignUp";
import NewRecipePage from "./Pages/NewRecipePage";
import Login from "./Pages/Login"
import Dashboard from "./Pages/Dashboard";
import RecipePage from "./Components/RecipePage";
import ReviewPage from "./Components/ReviewPage";
import {UserProvider} from "./stores/UserStore";
import SearchResults from "./Pages/SearchResults";
import UpdateRecipe from "./Pages/UpdateRecipe";
import DeleteRecipe from "./Pages/DeleteRecipePage";
import RandomRecipes from "./Pages/RandomRecipes";
import NavBar from "./Components/NavBar";
import BottomBar from "./Components/BottomBar"
import AccountInfo from "./Pages/AccountInfo";
import AllRecipes from "./Pages/AllRecipes";


const theme = createTheme({
    palette: {
        primary: {
          main: colors.orange[400],
        },
        secondary: {
          main: colors.grey[50]
        }
    },
    components:{
        select: {
            '&:before': {
                borderColor: 'white',
            },
            '&:after': {
                borderColor: 'white',
            },
            '&:not(.Mui-disabled):hover::before': {
                borderColor: 'white',
            },
        },
        root: {
            color: 'white',
        },
    }
});

function App() {
    return (
        <ThemeProvider theme={theme}>
            <div className="App" style={{paddingBottom:'60px', paddingTop:'60px'}}>
                <UserProvider>
                    <NavBar/>
                    <BottomBar/>
                    <Routes>
                        <Route path="/" element={<HomePage/>}/>
                        <Route path="/login" element={<Login/>}/>
                        <Route path="/dashboard" element={<Dashboard/>}/>
                        <Route path="/dashboard/newrecipe" element={<NewRecipePage/>}/>
                        <Route path="/signup" element={<SignUp/>}/>
                        <Route path="/newrecipe" element={<NewRecipePage/>}/>
                        <Route path="/recipes/update/:recipeId" element={<UpdateRecipe/>}/>
                        <Route path="/recipes/delete/:recipeId" element={<DeleteRecipe/>}/>
                        <Route path="/recipes/:recipeId" element={<RecipePage/>}/>
                        <Route path="/recipes/:recipeId/review" element={<ReviewPage/>}/>
                        <Route path="/review/:recipeId" element={<ReviewPage/>}/>
                        <Route path="/review/recipes/:recipeId/reviews" element={<ReviewPage/>}/>
                        <Route path="/searchresults" element={<SearchResults/>}/>
                        <Route path="/users/:id/recipes/:recipeId" element={<RecipePage/>}/>
                        <Route path="/recipes/random" element={<RandomRecipes/>}/>
                        <Route path="/account" element={<AccountInfo/>}/>
                        <Route path="/homepage" element={<HomePage/>}/>
                        <Route path="/allrecipes" element={<AllRecipes/>}/>
                        <Route path="/randomrecipe" element={<RandomRecipes/>}/>
                        <Route path="/users/:id/recipes/:recipeId" component={RecipePage}/>

                </Routes>
            </UserProvider>
        </div>
    </ThemeProvider>

    );
}

export default App;
//        <Route path="/create" element={ <CreatePage/> } />
//        <Route path="/profile" element={ <UserProfile/> } />