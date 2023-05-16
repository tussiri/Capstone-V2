import "./App.css";
import HomePage from "./Pages/HomePage";
import SignUp from "./Pages/SignUp";
import NewRecipePage from "./Pages/NewRecipePage";

function App() {
  return (
    <div className="App">
       <HomePage/>
      <Login/>
      <NewRecipePage />
      <AccountInfo/>
    </div>
  );
}

export default App;
