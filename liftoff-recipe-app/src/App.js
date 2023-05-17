import { Routes, Route } from "react-router-dom";
import "./App.css";
import HomePage from "./Pages/HomePage";
import SignUp from "./Pages/SignUp";
import NewRecipePage from "./Pages/NewRecipePage";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={ <HomePage/> } />
        <Route path="/signup" element={ <SignUp/> } />
        <Route path="/newrecipe" element={ <NewRecipePage/> } />
      </Routes>
    </div>
  );
}

export default App;
//        <Route path="/login" element={ <Login/> } />
//        <Route path="/create" element={ <CreatePage/> } />
//        <Route path="/profile" element={ <UserProfile/> } />