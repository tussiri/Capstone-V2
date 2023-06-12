import React, {createContext, useEffect, useState} from 'react'
import axios from "axios";
import {useNavigate} from "react-router-dom";

export const UserContext = createContext(null);

export function UserProvider({children}) {
    const [user, setUser] = useState(null);
    const navigate = useNavigate()

    useEffect(()=>{
        const token = localStorage.getItem('token');
        if(token){
            const user=parseJwt(token);
            setUser(user);
        }
    }, [])
    function parseJwt(token){
        try{
            const decoded=JSON.parse(atob(token.split('.')[1]));
            return{
                userId:decoded.userId,
                username:decoded.sub,
            };
        }catch(error){
            console.error("Error parsing JWT token: ", error)
            return null;
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

            // const user = response.data.data;
            const user = parseJwt(token)
            setUser(user);

            // const user = parseJwt(token);
            // setUser(user);
            console.log("Stored token:", localStorage.getItem('token'));
            console.log("Server response:", response.data)
            navigate('/')
            return Promise.resolve();
        } catch (error) {
            console.error("Error:", error);
            return Promise.reject(error);
        }
    }

    function logout() {
        localStorage.removeItem("token");
        setUser(null)
        navigate('/')
    }

    return (<UserContext.Provider value={{user, setUser, login, logout, parseJwt}}>
            {children}
        </UserContext.Provider>
    );
}