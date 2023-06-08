import React, {createContext, useState} from 'react'
import axios from "axios";
import {useNavigate} from "react-router-dom";

export const UserContext = createContext(null);

export function UserProvider({children}) {
    const [user, setUser] = useState(null);
    const navigate = useNavigate()
    function parseJwt(token){
        try{
            return JSON.parse(atob(token.split('.')[1]));
        }catch(e){
            return null
        }
    }

    async function login(email, password) {
        navigate('/login')
        try {
            const response = await axios.post('http://localhost:8080/auth/login', {
                email,
                password
            });
            const {token, id} = response.data.data;
            localStorage.setItem('token', token);
            localStorage.setItem('userId', id);

            const user = response.data.data;
            setUser(user);

            // const user = parseJwt(token);
            // setUser(user);
            console.log("Stored token:", localStorage.getItem('token'));
            console.log("Server response:", response.data)
            return Promise.resolve();
        } catch (error) {
            console.error("Error:", error);
            return Promise.reject(error);
        }
    }

    function logout() {
        setUser(null)
        localStorage.removeItem("token");
        navigate('/')
    }

    return (<UserContext.Provider value={{user, login, logout}}>
            {children}
        </UserContext.Provider>
    );
}