import React, {useState} from "react";
<<<<<<< HEAD
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
            localStorage.setItem('userId', response.data.data.id)
            console.log("Stored token:", localStorage.getItem('token'));
            console.log("Server response:", response.data)

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
=======

// form containing username and password fields to be sent to backend


const Login=()=>{
	//set state for email and password fields
	const [email,setEmail]=useState("");
	const [password,setPassword]=useState("");
	// event handler verifies the user enters info in both fields and throws an error if not
	const handleSubmit =(event)=>{
		event.preventDefault();
		if (email&&password){
			console.log(email,password);
		} else {
				alert("Please fill out both fields.");
			}
		}
	return(
		<div>
			<form onSubmit={handleSubmit}> 
				<div> 
					<label htmlForm="email">Email</label>
					<input type="text" name="email" id="email"/> 
				</div> 
				<div> 
					<label htmlForm="password">Password</label>
					<input type="text" name="password" id="password"/> 
				</div>  
				<button type="submit">Login</button>
			</form>
		</div>
	)
>>>>>>> main
}

export default Login;