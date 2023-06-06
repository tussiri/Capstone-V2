import React, {useState} from "react";
import axios from "axios";
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

import {useNavigate} from "react-router-dom";

function SignUp() {
    let navigate = useNavigate();

    const [showPassword, setShowPassword] = useState(false);
    const [formData, setFormData] = useState({
        email: "",
        firstName: "",
        lastName: "",
        password: "",
        confirmPassword: "",
        dateOfBirth: "",
    });
    const [errors, setErrors] = useState({});

    const handleClickShowPassword = () => setShowPassword((show) => !show);

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    const handleChange = (event) => {
        const {name, value} = event.target;
        setFormData((prevFormData) => ({
            ...prevFormData,
            [name]: value,
        }));
    };

    const formatDate = (dateString) => {
        // Convert the date string to ISO-8601 format
        const [month, day, year] = dateString.split("-");
        return `${year}-${month}-${day}`;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/auth/register", formData);
            const {token} = response.data;

            console.log("Registration successful:", response.data);

            localStorage.setItem("token", token);
            navigate("/");
        }catch (error) {
        if (error.response && error.response.data) {
            setErrors(error.response.data.errors);
        } else {
            console.error("Registration failed:", error);
        }
    }
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
                    "& .MuiTextField-root": {m: 1, width: "52ch"},
                }}
                noValidate
                autoComplete="off"
            >
                <div style={{position: "relative", top: "50px", left: "-5px"}}>
                    <TextField
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        // error={errors.email}
                        // helperText={errors.email}
                        sx={{color: "action.active", mr: 1, my: 0.5}}
                        label="Username(Email)*"
                        variant="filled"
                    />
                </div>
            </Box>
            <Box
                component="form"
                sx={{
                    "& .MuiTextField-root": {m: 1, width: "25ch"},
                }}
                noValidate
                autoComplete="off"
            >
                <div style={{position: "relative", top: "50px", left: "-5px"}}>
                    <TextField
                        id="firstName"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        // error={errors.firstName}
                        // helperText={errors.firstName}
                        sx={{color: "action.active", mr: 1, my: 0.5}}
                        label="First Name*"
                        variant="filled"
                    />
                    <TextField
                        id="lastName"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleChange}
                        error={errors.lastName}
                        helperText={errors.lastName}
                        sx={{color: "action.active", mr: 1, my: 0.5}}
                        label="Last Name*"
                        variant="filled"
                    />
                    <TextField
                        id="dateOfBirth"
                        type="date"
                        name="dateOfBirth"
                        value={formData.dateOfBirth}
                        onChange={handleChange}
                        // error={errors.dateOfBirth}
                        // helperText={errors.dateOfBirth}
                        sx={{color: "action.active", mr: 1, my: 0.5}}
                        label="Date of Birth*"
                        variant="filled"
                    />
                </div>
            </Box>
            <Box sx={{display: "flex", flexWrap: "wrap"}}>
                <div style={{position: "relative", top: "50px", left: "460px"}}>
                    <FormControl sx={{m: 1, width: "52ch"}} variant="filled">
                        <InputLabel htmlFor="password">Enter Password*</InputLabel>
                        <FilledInput
                            id="password"
                            name="password"
                            type={showPassword ? "text" : "password"}
                            value={formData.password}
                            onChange={handleChange}
                            // error={errors.password}
                            // helperText={errors.password}
                            endAdornment={
                                <InputAdornment position="end">
                                    <IconButton
                                        aria-label="toggle password visibility"
                                        onClick={handleClickShowPassword}
                                        onMouseDown={handleMouseDownPassword}
                                        edge="end"
                                    >
                                        {showPassword ? <VisibilityOff/> : <Visibility/>}
                                    </IconButton>
                                </InputAdornment>
                            }
                            label="Password"
                        />
                    </FormControl>
                </div>
            </Box>
            <Box sx={{display: "flex", flexWrap: "wrap"}}>
                <div style={{position: "relative", top: "50px", left: "460px"}}>
                    <FormControl sx={{m: 1, width: "52ch"}} variant="filled">
                        <InputLabel htmlFor="confirmPassword">Confirm Password*</InputLabel>
                        <FilledInput
                            id="confirmPassword"
                            name="confirmPassword"
                            type={showPassword ? "text" : "password"}
                            value={formData.confirmPassword}
                            onChange={handleChange}
                            // error={errors.confirmPassword}
                            // helperText={errors.confirmPassword}
                            endAdornment={
                                <InputAdornment position="end">
                                    <IconButton
                                        aria-label="toggle password visibility"
                                        onClick={handleClickShowPassword}
                                        onMouseDown={handleMouseDownPassword}
                                        edge="end"
                                    >
                                        {showPassword ? <VisibilityOff/> : <Visibility/>}
                                    </IconButton>
                                </InputAdornment>
                            }
                            label="Confirm Password"
                        />
                    </FormControl>
                </div>
            </Box>
            <div style={{position: "relative", top: "80px"}}>
                <Button variant="contained" color="success" onClick={handleSubmit}>
                    Continue
                </Button>
            </div>
        </div>
    </>
);
}

export default SignUp;