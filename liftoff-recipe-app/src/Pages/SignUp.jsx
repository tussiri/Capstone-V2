import React from "react";
import "../SignUp.css";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Logo from "../Assets/logo-removebg-preview 1.png";
import FormControl from "@mui/material/FormControl";
import OutlinedInput from "@mui/material/OutlinedInput";
import FilledInput from "@mui/material/FilledInput";
import InputLabel from "@mui/material/InputLabel";
import IconButton from "@mui/material/IconButton";
import InputAdornment from "@mui/material/InputAdornment";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";

function SignUp() {
  const [showPassword, setShowPassword] = React.useState(false);
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    firstName: "",
    lastName: "",
  });

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  const handleMouseDownPassword = (event) => {
    event.preventDefault();
  };
  return (
    <>
      <img src={Logo} alt="Logo" maxHeight="50" maxWidth="50"></img>
      <h1>Mealify</h1>
      <div className="App">
        <h1
          style={{
            position: "relative",
            top: "20px",
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
          <div style={{ position: "relative", top: "50px", left: "-5px" }}>
            <TextField
              id="filled-basic"
              sx={{ color: "action.active", mr: 1, my: 0.5 }}
              label="Username(Email)*"
              variant="filled"
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
          <div style={{ position: "relative", top: "50px", left: "-5px" }}>
            <TextField
              id="filled-basic"
              sx={{ color: "action.active", mr: 1, my: 0.5 }}
              label="First Name*"
              variant="filled"
            />
            <TextField
              id="filled-basic"
              sx={{ color: "action.active", mr: 1, my: 0.5 }}
              label="Last Name*"
              variant="filled"
            />
          </div>
        </Box>
        <Box sx={{ display: "flex", flexWrap: "wrap" }}>
          <div style={{ position: "relative", top: "50px", left: "460px" }}>
            <FormControl sx={{ m: 1, width: "52ch" }} variant="filled">
              <InputLabel htmlFor="filled-adornment-password">
                Enter Password*
              </InputLabel>
              <FilledInput
                id="filled-adornment-password"
                type={showPassword ? "text" : "password"}
                endAdornment={
                  <InputAdornment position="end">
                    <IconButton
                      aria-label="toggle password visibility"
                      onClick={handleClickShowPassword}
                      onMouseDown={handleMouseDownPassword}
                      edge="end"
                    >
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                }
                label="Password"
              />
            </FormControl>
          </div>
        </Box>
        <Box sx={{ display: "flex", flexWrap: "wrap" }}>
          <div style={{ position: "relative", top: "50px", left: "460px" }}>
            <FormControl sx={{ m: 1, width: "52ch" }} variant="filled">
              <InputLabel htmlFor="filled-adornment-password">
                Confirm Password*
              </InputLabel>
              <FilledInput
                id="filled-adornment-password"
                type={showPassword ? "text" : "password"}
                endAdornment={
                  <InputAdornment position="end">
                    <IconButton
                      aria-label="toggle password visibility"
                      onClick={handleClickShowPassword}
                      onMouseDown={handleMouseDownPassword}
                      edge="end"
                    >
                      {showPassword ? <VisibilityOff /> : <Visibility />}
                    </IconButton>
                  </InputAdornment>
                }
                label="Password"
              />
            </FormControl>
          </div>
        </Box>
        <div style={{ position: "relative", top: "80px" }}>
          <Button variant="contained" color="success">
            Continue
          </Button>
        </div>
      </div>
    </>
  );
}

export default SignUp;
