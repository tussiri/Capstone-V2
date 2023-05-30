import React, {useState} from "react";

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
}

export default Login;