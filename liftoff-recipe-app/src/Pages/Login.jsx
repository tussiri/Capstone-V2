import React, {useState, useContext, useEffect} from "react";
import axios from "axios"
import {Link, useNavigate} from 'react-router-dom'
import {UserContext} from '../stores/UserStore'
import Button from "@mui/material/Button";


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

    const handleLogin = () => {
        login(email, password)
            .then(() => navigate("/"))
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