import React, {useState} from "react";
import axios from "axios";
import "../SignUp.css";
import Box from "@mui/material/Box";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";
import Logo from "../Assets/MealifyLogoNoBG100x100.png";
import FormControl from "@mui/material/FormControl";
import OutlinedInput from "@mui/material/OutlinedInput";
import FilledInput from "@mui/material/FilledInput";
import InputLabel from "@mui/material/InputLabel";
import IconButton from "@mui/material/IconButton";
import InputAdornment from "@mui/material/InputAdornment";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import {Link, useNavigate} from "react-router-dom";
import CssBaseline from '@mui/material/CssBaseline';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';

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
    const [{firstName}, setErrors] = useState({});
    //const [errors, setErrors] = useState({});
    const [selectedDate, setSelectedDate] = React.useState(new Date('2014-08-18T21:11:54'));

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

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/auth/register", formData);
            const {token} = response.data;

            console.log("Registration successful:", response.data);

            localStorage.setItem("token", token);
            navigate("/");
        } catch (error) {
            if (error.response && error.response.data) {
                setErrors(error.response.data.errors);
            } else {
                console.error("Registration failed:", error);
            }
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
                Sign up
              </Typography>
              <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
                <Grid container spacing={2}>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      autoComplete="given-name"
                      name="firstName"
                      required
                      fullWidth
                      id="firstName"
                      label="First Name"
                      autoFocus
                      value={formData.firstName}
                      onChange={handleChange}
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      required
                      fullWidth
                      id="lastName"
                      label="Last Name"
                      name="lastName"
                      autoComplete="family-name"
                      value={formData.lastName}
                      onChange={handleChange}
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      required
                      fullWidth
                      id="email"
                      label="Email Address"
                      name="email"
                      autoComplete="email"
                      value={formData.email}
                      onChange={handleChange}
                    />
                  </Grid>

                  <Grid item xs={12}>
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                        <FormControl fullWidth components={['DatePicker']}>
                          <DatePicker
                          fullWidth
                          label="Date of Birth"
                          required
                          id="birthday"
                          name="birthday"
                          />
                        </FormControl>
                      </LocalizationProvider>
                  </Grid>
                  <Grid item xs={12}>
                    <FormControl fullWidth>
                    <InputLabel htmlFor="passwordField">Password</InputLabel>
                    <OutlinedInput
                      required
                      fullWidth
                      name="password"
                      label="Password"
                      type={showPassword ? 'text' : 'password'}
                      id="password"
                      autoComplete="new-password"
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
                  <Grid item xs={12}>
                    <FormControl fullWidth>
                    <InputLabel htmlFor="confirmPasswordField">Confirm Password</InputLabel>
                    <OutlinedInput
                      required
                      name="confirmPassword"
                      label="Confirm Password"
                      type={showPassword ? 'text' : 'password'}
                      id="confirmPassword"
                      autoComplete="new-password"
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
                  Sign Up
                </Button>
                <Grid container justifyContent="center">
                  <Grid item>
                    <Link to='/login' variant="body2">
                      <Button>Already have an account? Sign in</Button>
                    </Link>
                  </Grid>
                </Grid>
              </Box>
            </Box>
          </Container>
</div>
);

}

export default SignUp;