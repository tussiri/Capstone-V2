import React from "react";
import "../SignUp.css";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
//import PersonRoundedIcon from "@mui/icons-material/PersonRounded";

function SignUp() {
  return (
    <div className="App">
      <h1
        style={{
          position: "relative",
          top: "50px",
        }}
      >
        Sign Up
      </h1>
      <Box
        component="form"
        sx={{
          "& .MuiTextField-root": { m: 1, width: "52ch" },
        }}
        noValidate
        autoComplete="off"
      >
        <div style={{ position: "relative", top: "50px" }}>
          <TextField
            id="outlined-basic"
            sx={{ color: "action.active", mr: 1, my: 0.5 }}
            label="Username(Email)*"
            variant="outlined"
          />
        </div>
      </Box>
      <Box
        component="form"
        sx={{
          "& .MuiTextField-root": { m: 1, width: "25ch" },
        }}
        noValidate
        autoComplete="off"
      >
        <div style={{ position: "relative", top: "50px" }}>
          <TextField
            id="outlined-basic1"
            label="First Name*"
            variant="outlined"
          />
          <TextField
            id="outlined-basic2"
            label="Last Name*"
            variant="outlined"
          />
        </div>
      </Box>
      <Box
        component="form"
        sx={{
          "& .MuiTextField-root": { m: 1, width: "52ch" },
        }}
        noValidate
        autoComplete="off"
      >
        <div style={{ position: "relative", top: "50px" }}>
          <TextField
            id="outlined-basic"
            label="Enter Password*"
            variant="outlined"
          />
        </div>
      </Box>
      <Box
        component="form"
        sx={{
          "& .MuiTextField-root": { m: 1, width: "52ch" },
        }}
        noValidate
        autoComplete="off"
      >
        <div style={{ position: "relative", top: "50px" }}>
          <TextField
            id="outlined-basic"
            label="Confirm Password*"
            variant="outlined"
          />
        </div>
      </Box>
      <div style={{ position: "relative", top: "80px" }}>
        <Button variant="contained" color="success">
          Continue
        </Button>
      </div>
    </div>
  );
}

export default SignUp;
