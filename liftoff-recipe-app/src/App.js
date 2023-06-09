import {Routes, Route} from "react-router-dom";
import "./App.css";
// import "./Styles/global.css"
import { createTheme, colors, ThemeProvider } from '@mui/material';
import HomePage from "./Pages/HomePage";
import SignUp from "./Pages/SignUp";
import NewRecipePage from "./Pages/NewRecipePage";
import UpdateRecipePage from "./Pages/UpdateRecipePage";
import DeleteRecipePage from "./Pages/DeleteRecipePage";
import Login from "./Pages/Login"
import AccountInfo from "./Pages/AccountInfo"
import Dashboard from "./Pages/Dashboard";
import RecipePage from "./Components/RecipePage";
import ReviewPage from "./Components/ReviewPage";
import {UserProvider} from "./stores/UserStore";
import SearchResults from "./Pages/SearchResults";
import AllRecipes from "./Pages/AllRecipes";
import RandomRecipe from "./Pages/RandomRecipe";
import NavBar from "./Components/NavBar";


const theme = createTheme({
    palette: {
        primary: {
          main: colors.orange[400],
        },
        secondary: {
          main: colors.deepOrange[900],
        }
    }
})

function App() {
    return (
    <ThemeProvider theme={theme}>
        <div className="App">
            <UserProvider>
            <NavBar/>
                <Routes>
                    <Route path="/" element={<HomePage/>}/>
                    <Route path="/homepage" element={<HomePage/>}/>
                    <Route path="/login" element={<Login/>}/>
                    {/*<Route path = "/account" element={<AccountInfo/>}/>*/}
                    <Route path="/dashboard" element={<Dashboard/>}/>
                    <Route path="/dashboard/newrecipe" element={<NewRecipePage/>}/>
                    <Route path="/signup" element={<SignUp/>}/>
                    <Route path="/newrecipe" element={<NewRecipePage/>}/>
                    <Route path="/updaterecipe" element={<UpdateRecipePage/>}/>
                    <Route path="/deleterecipe" element={ <DeleteRecipePage/> } />
                    <Route path="/recipes/:recipeId" element={<RecipePage/>}/>
                    <Route path="/recipes/:recipeId/review" element={<ReviewPage/>}/>
                    <Route path="/review/:recipeId" component={ReviewPage}/>
                    <Route path = "/searchresults" element={<SearchResults/>}/>
                    <Route path="/allrecipes" element={<AllRecipes/>}/>
                    <Route path="/randomrecipe" element={<RandomRecipe/>}/>
                    {/*<Route path="/users/:userId/recipes/:recipeId" component={RecipePage} />*/}

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