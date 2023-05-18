import React from "react";
import "../SignUp.css";
import axios from "axios";
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
  const [formData, setFormData] = React.useState({
    email: "",
    password: "",
    firstName: "",
    lastName: "",
    password: "",
    confirmPassword: "",
    dateOfBirth: "",
  });
  // const [errors, setErrors] = React.useState({});

  const handleClickShowPassword = () => setShowPassword((show) => !show);

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(
        "http://localhost:8080/auth/register",
        formData
      );
      console.log("Registration successful:", response.formData.data);
    } catch (error) {
      if (error.response && error.response.data) {
        // setErrors(error.response.data.errors);
      } else {
        console.log("Registration failed", error.response);
      }
    }
  };

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

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
        <form onSubmit={handleSubmit}>
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
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                // error={errors.email}
                // helperText={errors.email}
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
                id="firstName"
                name="firstName"
                value={formData.firstName}
                onChange={handleChange}
                // error={errors.firstName}
                // helperText={errors.firstName}
                sx={{ color: "action.active", mr: 1, my: 0.5 }}
                label="First Name*"
                variant="filled"
              />
              <TextField
                id="lastName"
                name="lastName"
                value={formData.lastName}
                onChange={handleChange}
                // error={errors.lastName}
                // helperText={errors.lastName}
                sx={{ color: "action.active", mr: 1, my: 0.5 }}
                label="Last Name*"
                variant="filled"
              />
              <TextField
                id="dateOfBirth"
                name="dateOfBirth"
                value={formData.dateOfBirth}
                onChange={handleChange}
                // error={errors.dateOfBirth}
                // helperText={errors.dateOfBirth}
                sx={{ color: "action.active", mr: 1, my: 0.5 }}
                label="Date of Birth*"
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
                  id="password"
                  name="password"
                  type={showPassword ? "text" : "password"}
                  value={formData.password}
                  onChange={handleChange}
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
                  id="confirmPassword"
                  name="confirmPassword"
                  type={showPassword ? "text" : "password"}
                  value={formData.confirmPassword}
                  onChange={handleChange}
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
            <Button type="submit" variant="contained" color="success">
              Continue
            </Button>
          </div>
        </form>
      </div>
    </>
  );
}

export default SignUp;
