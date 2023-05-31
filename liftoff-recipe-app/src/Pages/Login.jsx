import React, {useState} from "react";
import axios from "axios"
import {useNavigate} from 'react-router-dom'



function Login () {

    let navigate = useNavigate()

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const handleLogin = () => {
        axios.post('http://localhost:8080/auth/login', {
            email,
            password
        }).then(response => {
            localStorage.setItem('token', response.data.data.token);
            console.log("Stored token:", localStorage.getItem('token'));
            console.log("Server response:", response.data.data)

            navigate("/dashboard");
        }).catch(error => {
            console.log("Error:", error);
        });
    };

    return (
        <div>
            <input type="email" value={email} onChange={e => setEmail(e.target.value)}/>
            <input type="password" value={password} onChange={e => setPassword(e.target.value)}/>
            <button onClick={handleLogin}>Login</button>
        </div>
    );
}

export default Login;