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
                favoriteRecipes:[],
            };
        }catch(error){
            console.error("Error parsing JWT token: ", error)
            return null;
        }
    }

    async function register() {
        try {
            const token = localStorage.getItem('token');
            const id = localStorage.getItem('userId');
            if (token && id) {
                const user = parseJwt(token)
                setUser(user);
                console.log("Stored token: ", localStorage.getItem('token'));
                navigate('/');
                return Promise.resolve();
            } else {
                console.error("Error: No token found");
                return Promise.reject("No token found");
            }
        } catch (error) {
            console.error("Error:", error);
            return Promise.reject(error);
        }
    }

    function isTokenExpired() {
        const expiry = localStorage.getItem("tokenExpiry");
        if (!expiry) return true;
        return new Date().getTime() > new Date(expiry).getTime();
    }



    async function login(email, password) {
        navigate('/login')
        try {
            const response = await axios.post('${process.env.REACT_APP_BACKEND_URL}/auth/login', {
                email,
                password
            });
            const {token, id, expiry} = response.data.data;
            localStorage.setItem('token', token);
            localStorage.setItem('userId', id);
            localStorage.setItem("tokenExpiry", expiry);


            // const user = response.data.data;
            const user = parseJwt(token)
            setUser(user);

            const expiryDate = new Date();
            expiryDate.setSeconds(expiryDate.getSeconds() + expiry);
            localStorage.setItem("tokenExpiry", expiryDate);

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

    return (<UserContext.Provider value={{user, setUser, login, logout, parseJwt, register}}>
            {children}
        </UserContext.Provider>
    );
}