import React, {useContext, useState} from "react";
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
    const [firstName, setFirstName] = useState(user.firstName);
    const [lastName, setLastName] = useState(user.lastName);
    const [email, setEmail] = useState(user.email);
    const [password, setPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");

    const [showPassword, setShowPassword] = useState(false);
    const handleClickShowPassword = () => setShowPassword((show) => !show);
    const handleMouseDownPassword = (event) => {
            event.preventDefault();
        };

    const [formData, setFormData] = useState({
            firstName: "",
            lastName: "",
            password: "",
            confirmPassword: "",
        });

    const handleChange = (event) => {
        const {name, value} = event.target;
        setFormData((prevFormData) => ({
            ...prevFormData,
            [name]: value,
        }));
    };

    const handleSave = async () => {
        try {
            const updatedUser = {
                firstName: firstName,
                lastName: lastName,
                email: email,
                password: password === "" ? undefined : password,
                // dateOfBirth:'',
            };
            console.log("Updated user: ", updatedUser)

            const response = await authAxios.put(`http://localhost:8080/users/${user.id}`, updatedUser);
            console.log("Server response: ", response)
            setPassword("");

        } catch (error) {
            console.log("There was an error updating the user's information: ", error)
        }
    };

    const handleChangePassword = async () => {
        try {
            const updatedUser = {
                password: newPassword
            };

            const response = await authAxios.put(`http://localhost:8080/users/${user.id}`, updatedUser);
            console.log("Server response for password: ", response)
            setNewPassword("");
            setPassword("");
        } catch (error) {
            console.error(error);
        }
    };

    return (
    <div>
         <Container component="main" maxWidth="xs">
                <CssBaseline />
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
//                     onSubmit={handleSubmit}
                    sx={{ mt: 3 }}>
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
                        sx={{ mt: 2, mb: 5, color: 'white' }}
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
                          name="newPassword"
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
                          name="confirmNewPassword"
                          label="Confirm New Password"
                          type={showPassword ? 'text' : 'password'}
                          id="confirmNewPassword"
                          value={formData.password}
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
                      type="submit"
                      fullWidth
                      variant="contained"
                      sx={{ mt: 3, mb: 2, color: 'white' }}
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

{/*         <div> */}
{/*             <h1>Edit Account Information</h1> */}
{/*             <label>First Name:</label> */}
{/*             <input */}
{/*                 type="text" */}
{/*                 value={firstName} */}
{/*                 onChange={(e) => setFirstName(e.target.value)} */}
{/*             /> */}
{/*             <label>Last Name:</label> */}
{/*             <input */}
{/*                 type="text" */}
{/*                 value={lastName} */}
{/*                 onChange={(e) => setLastName(e.target.value)} */}
{/*             /> */}
{/*             <button onClick={handleSave}>Save</button> */}
{/*             <h2>Change Password</h2> */}
{/*             <label>Old Password:</label> */}
{/*             <input */}
{/*                 type="password" */}
{/*                 value={password} */}
{/*                 onChange={(e) => setPassword(e.target.value)} */}
{/*             /> */}
{/*             <label>New Password:</label> */}
{/*             <input */}
{/*                 type="password" */}
{/*                 value={newPassword} */}
{/*                 onChange={(e) => setNewPassword(e.target.value)} */}
{/*             /> */}
{/*             <button onClick={handleChangePassword}>Change Password</button> */}
{/*             <button onClick={logout}>Logout</button> */}
{/*         </div> */}