import React, {useContext, useEffect, useState} from "react";
import axios from "axios";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";
import Container from '@mui/material/Container';
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import FormControl from "@mui/material/FormControl";
import OutlinedInput from "@mui/material/OutlinedInput";
import FilledInput from "@mui/material/FilledInput";
import InputLabel from "@mui/material/InputLabel";
import IconButton from "@mui/material/IconButton";
import InputAdornment from "@mui/material/InputAdornment";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import FormControlLabel from '@mui/material/FormControlLabel';
import Typography from '@mui/material/Typography';
import Grid from '@mui/material/Grid';
import Logo from "../Assets/MealifyLogoNoBG100x100.png";
import {Link, useNavigate} from "react-router-dom";
import CssBaseline from '@mui/material/CssBaseline';

function AccountEdit() {
    const {user, logout} = useContext(UserContext)
    const userId = localStorage.getItem('userId');

    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        email: "",
        oldPassword: "",
        password: "",
        confirmPassword: "",
    });

    const [showPassword, setShowPassword] = useState(false);
    const handleClickShowPassword = () => setShowPassword((show) => !show);
    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };

    useEffect(() => {
        if (user) {
            setFormData({
                ...formData,
                firstName: user.firstName || "",
                lastName: user.lastName || "",
                email: user.email || "",
                oldPassword: "",
                password: "",
                confirmPassword: "",
            })
        }
    }, [user]);

    const handleChange = (event) => {
        const {name, value} = event.target;
        setFormData((prevFormData) => ({
            ...prevFormData,
            [name]: value,
        }));
    };

    const handleSave = async (event) => {
        event.preventDefault();
        if (formData.password && (formData.password.length < 8 || formData.password.length > 50)) {
            alert("Password must be between 8 and 50 characters!");
            return;
        }
        try {
            let updatedUser = {};

            if (formData.firstName) updatedUser.firstName = formData.firstName;
            if (formData.lastName) updatedUser.lastName = formData.lastName;
            if (formData.email) updatedUser.email = formData.email;
            if (formData.password) updatedUser.password = formData.password;

            console.log("Updated user: ", updatedUser)

            const response = await authAxios.put(`http://localhost:8080/users/${userId}`, updatedUser);
            console.log("Server response: ", response)
            setFormData(prevFormData => ({
                ...prevFormData,
                password: "",
            }));
        } catch (error) {
            console.log("There was an error updating the user's information: ", error)
        }
    };

    const handleChangePassword = async () => {
        if (formData.password !== formData.confirmPassword) {
            alert("Passwords do not match!");
            return;
        }
        if (formData.password && (formData.password.length < 8 || formData.password.length > 50)) {
            alert("New password must be between 8 and 50 characters!");
            return;
        }
        try {
            const updatedUser = {
                firstName: formData.firstName,
                lastName: formData.lastName,
                email: formData.email,
                password: formData.password
            };

            const response = await authAxios.put(`http://localhost:8080/users/${userId}`, updatedUser);
            console.log("Server response for password: ", response)
            setFormData(prevFormData => ({
                ...prevFormData,
                password: "",
                confirmPassword: "",
            }));
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div>
            <Container component="main" maxWidth="xs">
                <CssBaseline/>
                <Box
                    sx={{
                        marginTop: 4,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <img src={Logo}/>
                    <Typography component="h1" variant="h5">
                        Edit Account Info
                    </Typography>
                    <Box
                        component="form"
                        noValidate
                        onSubmit={handleSave}
                        //                     onSubmit={handleSubmit}
                        sx={{mt: 3}}>
                        <Grid container spacing={2}>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    autoComplete="given-name"
                                    name="firstName"
                                    fullWidth
                                    id="firstName"
                                    label="First Name"
                                    value={formData.firstName}
                                    onChange={handleChange}
                                />
                            </Grid>
                            <Grid item xs={12} sm={6}>
                                <TextField
                                    fullWidth
                                    id="lastName"
                                    label="Last Name"
                                    name="lastName"
                                    autoComplete="family-name"
                                    value={formData.lastName}
                                    onChange={handleChange}
                                />
                            </Grid>
                        </Grid>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{mt: 2, mb: 5, color: 'white'}}
                        >
                            Change Name
                        </Button>
                        <Grid container spacing={2}>
                            <Grid item xs={12}>
                                <FormControl fullWidth>
                                    <InputLabel htmlFor="passwordField">Old Password</InputLabel>
                                    <OutlinedInput
                                        fullWidth
                                        name="oldPassword"
                                        label="Old Password"
                                        type={showPassword ? 'text' : 'password'}
                                        id="oldPassword"
                                        onChange={handleChange}
                                        endAdornment={
                                            <InputAdornment position="end">
                                                <IconButton
                                                    aria-label="toggle password visibility"
                                                    onClick={handleClickShowPassword}
                                                    onMouseDown={handleMouseDownPassword}
                                                    edge="end">
                                                    {showPassword ? <VisibilityOff/> : <Visibility/>}
                                                </IconButton>
                                            </InputAdornment>
                                        }
                                    />
                                </FormControl>
                            </Grid>
                            <Grid item xs={12}>
                                <FormControl fullWidth>
                                    <InputLabel htmlFor="passwordField"> New Password</InputLabel>
                                    <OutlinedInput
                                        fullWidth
                                        name="password"
                                        label="New Password"
                                        type={showPassword ? 'text' : 'password'}
                                        id="newPassword"
                                        onChange={handleChange}
                                        endAdornment={
                                            <InputAdornment position="end">
                                                <IconButton
                                                    aria-label="toggle password visibility"
                                                    onClick={handleClickShowPassword}
                                                    onMouseDown={handleMouseDownPassword}
                                                    edge="end">
                                                    {showPassword ? <VisibilityOff/> : <Visibility/>}
                                                </IconButton>
                                            </InputAdornment>
                                        }
                                    />
                                </FormControl>
                            </Grid>
                            <Grid item xs={12}>
                                <FormControl fullWidth>
                                    <InputLabel htmlFor="confirmPasswordField">Confirm New Password</InputLabel>
                                    <OutlinedInput
                                        name="confirmPassword"
                                        label="Confirm New Password"
                                        type={showPassword ? 'text' : 'password'}
                                        id="confirmPassword"
                                        value={formData.confirmPassword}
                                        onChange={handleChange}
                                        endAdornment={
                                            <InputAdornment position="end">
                                                <IconButton
                                                    aria-label="toggle password visibility"
                                                    onClick={handleClickShowPassword}
                                                    onMouseDown={handleMouseDownPassword}
                                                    edge="end">
                                                    {showPassword ? <VisibilityOff/> : <Visibility/>}
                                                </IconButton>
                                            </InputAdornment>
                                        }
                                    />
                                </FormControl>
                            </Grid>
                        </Grid>
                        <Button
                            onClick={handleChangePassword}
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{mt: 3, mb: 2, color: 'white'}}
                        >
                            Change Password
                        </Button>
                        <Grid container justifyContent="center">
                            <Grid item>
                                <Link to='/account' variant="body2">
                                    <Button>Back to Account Info</Button>
                                </Link>
                            </Grid>
                        </Grid>
                    </Box>
                </Box>
            </Container>
        </div>
    );
}

export default AccountEdit;
