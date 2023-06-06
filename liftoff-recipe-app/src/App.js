import {Routes, Route} from "react-router-dom";
import "./App.css";
import HomePage from "./Pages/HomePage";
import SignUp from "./Pages/SignUp";
import NewRecipePage from "./Pages/NewRecipePage";
import UpdateRecipePage from "./Pages/UpdateRecipePage";
import Login from "./Pages/Login"
import AccountInfo from "./Pages/AccountInfo"
import Dashboard from "./Pages/Dashboard";
import RecipePage from "./Components/RecipePage";
import ReviewPage from "./Components/ReviewPage";
import {UserProvider} from "./stores/UserStore";
import SearchResults from "./Pages/SearchResults";

function App() {
    return (
        <div className="App">
            <UserProvider>
                <Routes>
                    <Route path="/" element={<HomePage/>}/>
                    <Route path="/login" element={<Login/>}/>
                    {/*<Route path = "/account" element={<AccountInfo/>}/>*/}
                    <Route path="/dashboard" element={<Dashboard/>}/>
                    <Route path="/signup" element={<SignUp/>}/>
                    <Route path="/newrecipe" element={<NewRecipePage/>}/>
                    <Route path="/updaterecipe" element={<UpdateRecipePage/>}/>
                    <Route path="/recipes/:recipeId" element={<RecipePage/>}/>
                    <Route path="/recipes/:recipeId/review" element={<ReviewPage/>}/>
                    <Route path="/review/:recipeId" component={ReviewPage}/>
                    <Route path = "/searchresults" component={<SearchResults/>}/>
                    {/*<Route path="/users/:userId/recipes/:recipeId" component={RecipePage} />*/}

                    <Route path="/users/:id/recipes/:recipeId" component={RecipePage}/>

                </Routes>
            </UserProvider>
        </div>
    );
}

export default App;
//        <Route path="/create" element={ <CreatePage/> } />
//        <Route path="/profile" element={ <UserProfile/> } />