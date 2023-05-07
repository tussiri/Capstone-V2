import React from "react";
import "../SignUp.css";
import Logo from "../Assets/logo-removebg-preview 1.png";
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import MenuItem from "@mui/material/MenuItem";

function NewRecipePage() {
  const categories = [
    {
      value: "Vgn",
      label: "Vegan",
    },
    {
      value: "Veg",
      label: "Vegetarian",
    },
    {
      value: "Non-Veg",
      label: "Non-Vegetarian",
    },
    {
      value: "App",
      label: "Appetizers",
    },
    {
      value: "Sou",
      label: "Soups",
    },
    {
      value: "Sal",
      label: "Salads",
    },
    {
      value: "DST",
      label: "Dessert",
    },
  ];
  return (
    <>
      <h1
        style={{
          position: "relative",
          top: "20px",
        }}
      >
        New Recipe
      </h1>

      <img
        src={Logo}
        alt="Logo"
        maxHeight="30"
        maxWidth="30"
        style={{ position: "relative", top: "-60px", left: "650px" }}
      ></img>

      <h3
        style={{
          position: "relative",
          top: "-30px",
          left: "-400px",
        }}
      >
        Name
      </h3>

      <TextField
        id="outlined-basic"
        variant="outlined"
        size="small"
        style={{
          width: "54ch",
          position: "relative",
          top: "-40px",
          left: "-155px",
        }}
      />

      <h3
        style={{
          position: "relative",
          top: "-30px",
          left: "-385px",
        }}
      >
        Category
      </h3>

      <TextField
        id="outlined-select-currency"
        select
        size="small"
        style={{
          width: "25ch",
          position: "relative",
          top: "-40px",
          left: "-300px",
        }}
      >
        {categories.map((option) => (
          <MenuItem key={option.value} value={option.value}>
            {option.label}
          </MenuItem>
        ))}
      </TextField>

      <h3
        style={{
          position: "relative",
          top: "-30px",
          left: "-375px",
        }}
      >
        Description
      </h3>

      <TextField
        id="outlined-multiline-flexible"
        size="small"
        multiline
        maxRows={5}
        style={{
          width: "54ch",
          position: "relative",
          top: "-40px",
          left: "-155px",
        }}
      />

      <div
        style={{
          position: "relative",
          top: "-10px",
          left: "-155px",
        }}
      >
        <TextField
          id="filled-basic"
          label="Prep-time"
          variant="filled"
          size="small"
          style={{
            width: "16ch",
            padding: "1%",
          }}
        />
        <TextField
          id="filled-basic"
          label="Cook-time"
          variant="filled"
          size="small"
          style={{
            width: "16ch",
            padding: "1%",
          }}
        />
        <TextField
          id="filled-basic"
          label="Servings"
          variant="filled"
          size="small"
          style={{
            width: "16ch",
            padding: "1%",
          }}
        />
      </div>
    </>
  );
}

export default NewRecipePage;
