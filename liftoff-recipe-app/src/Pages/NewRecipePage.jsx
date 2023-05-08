import React from "react";
import "../SignUp.css";
import Logo from "../Assets/logo-removebg-preview 1.png";
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import MenuItem from "@mui/material/MenuItem";
import Button from "@mui/material/Button";
import FavoriteTwoToneIcon from "@mui/icons-material/FavoriteTwoTone";

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

  const [flag, setFlag] = React.useState(true);

  const handleClick = () => {
    setFlag(!flag);
  };

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
      <FavoriteTwoToneIcon
        onClick={handleClick}
        style={flag ? { color: "black" } : { color: "crimson" }}
        fontSize="large"
      />
      <h3
        style={{
          position: "relative",
          top: "-30px",
          left: "-250px",
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
          left: "-5px",
        }}
      />

      <h3
        style={{
          position: "relative",
          top: "-30px",
          left: "-235px",
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
          left: "-150px",
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
          left: "-225px",
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
          left: "-5px",
        }}
      />

      <div
        style={{
          position: "relative",
          top: "-20px",
          left: "-5px",
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

      <h3
        style={{
          position: "relative",
          top: "-30px",
          left: "-225px",
        }}
      >
        Ingredients
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
          left: "-5px",
        }}
      />

      <h3
        style={{
          position: "relative",
          top: "-30px",
          left: "-225px",
        }}
      >
        Allergens
      </h3>

      <TextField
        id="outlined-basic"
        variant="outlined"
        size="small"
        style={{
          width: "54ch",
          position: "relative",
          top: "-40px",
          left: "-5px",
        }}
      />

      <h3
        style={{
          position: "relative",
          top: "-30px",
          left: "-225px",
        }}
      >
        Directions
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
          left: "-5px",
        }}
      />

      <div style={{ position: "relative", top: "20px" }}>
        <Button variant="contained" color="success">
          Submit
        </Button>
      </div>
    </>
  );
}

export default NewRecipePage;
