import React, {useState, useContext } from "react";
import axios from "axios"
import {Link, useNavigate} from 'react-router-dom'
import {UserContext} from '../stores/UserStore'
import Button from "@mui/material/Button";



function Login () {
    const {login} = useContext(UserContext);
    const navigate = useNavigate()

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    // const handleLogin = () => {
    //     axios.post('http://localhost:8080/auth/login', {
    //         email,
    //         password
    //     }).then(response => {
    //         localStorage.setItem('token', response.data.data.token);
    //         localStorage.setItem('userId', response.data.data.id)
    //         console.log("Stored token:", localStorage.getItem('token'));
    //         console.log("Server response:", response.data)
    //
    //         navigate("/");
    //     }).catch(error => {
    //         console.log("Error:", error);
    //     });
    // };

    const handleLogin = () => {
        login(email, password)
            .then(() => navigate("/dashboard"))
            .catch((error) => console.log("Error:", error));
    };


    return (
        <div>
            <input type="email" value={email} onChange={e => setEmail(e.target.value)}/>
            <input type="password" value={password} onChange={e => setPassword(e.target.value)}/>
            <Button variant="contained" onClick={handleLogin}>Login</Button>
            <p>Don't have an account? <Link to="/signup">Register here</Link>.</p>
        </div>
    );
}

export default Login;