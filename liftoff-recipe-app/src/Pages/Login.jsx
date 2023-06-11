import React, {useState, useContext, useEffect} from "react";
import axios from "axios"
import {Link, useNavigate} from 'react-router-dom'
import {UserContext} from '../stores/UserStore'
import Button from "@mui/material/Button";
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import Logo from "../Assets/MealifyLogoNoBG100x100.png";


function Login() {
    const {user, login} = useContext(UserContext);
    const navigate = useNavigate()

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    useEffect(() => {
        if (localStorage.getItem('token') && user) {
            navigate('/dashboard');
        }
    }, [user, navigate]);

    const handleLogin = (e) => {
        e.preventDefault();
        login(email, password)
            .then(() => navigate("/"))
            .catch((error) => console.log("Error:", error));
    };


    return (
        <div>
            <Container component="main" maxWidth="xs">
                <CssBaseline/>
                <Box
                    sx={{
                        marginTop: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <img src={Logo}/>
                    <Typography component="h1" variant="h5">
                        Sign in
                    </Typography>
                    <Box component="form" onSubmit={handleLogin} noValidate sx={{mt: 1}}>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="email"
                            label="Email Address"
                            name="email"
                            autoComplete="email"
                            autoFocus
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="password"
                            label="Password"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                        />
                        {/*                     <FormControlLabel */}
                        {/*                       control={<Checkbox value="remember" color="primary" />} */}
                        {/*                       label="Remember me" */}
                        {/*                     /> */}
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{mt: 3, mb: 2, color: 'white'}}
                        >
                            Sign In
                        </Button>
                        <Grid container justifyContent="center">
                            {/*                       <Grid item xs> */}
                            {/*                         <Link href="#" variant="body2"> */}
                            {/*                           Forgot password? */}
                            {/*                         </Link> */}
                            {/*                       </Grid> */}
                            <Grid item>
                                <Link to='/signup' variant="body2">
                                    <Button>Don't have an account? Sign Up</Button>
                                </Link>
                            </Grid>
                        </Grid>
                    </Box>
                </Box>
            </Container>
        </div>
    );
}

export default Login;