import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import Button from "@mui/material/Button";
import axios from 'axios';
import NavBar from "../Components/NavBar"
import SearchBar from "../Components/SearchBar"
import SearchResults from "./SearchResults";
import LoadingScreen from "./LoadingPage";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";
import Sidebar from "../Components/SideBar";
//
// //create component to view edit and delete user info and account
// // this component displays user information
//
// function AccountInfo({user, onEdit}) {
//     const {name, email, phone, location}= user;
//     return (
//         <NavBar>
//         <div>
//             <h1>Account Information </h1>
//             <p>Name:{name}</p>
//             <p>Email:{email}</p>
//             <p>Phone:{phone}</p>
//             <p>Location:{location}</p>
//             <button onClick={onEdit}>Edit</button>
//
//         </div>
//     );
//
// }
// //allows user to edit account information
// function AccountEdit({user, onSave,onCancel}){
//     const[name, setName]= useState(user.name);
//     const[email, setEmail]= useState(user.email);
//     const[phone, setPhone]= useState(user.phone);
//     const[location, setLocation]= useState(user.locationv  );
//
//     const handleSave =() => {
//         onSave({ name, email,phone});
//     };
//     const handleCancel = () => {
//         onCancel();
//     };
//
//     return (
//         <div>
//             <h1>Edit Account Information</h1>
//             <label>Name:</label>
//             <input type="text" name="{name}"onChange={(e)=> setName(e.target.value)}/>
//             <label>Email:</label>
//             <input type="email" email="{email}"onChange={(e)=> setEmail(e.target.value)}/>
//             <label>Phone:</label>
//             <input type="tel" phone="{phone}"onChange={(e)=> setPhone(e.target.value)}/>
//             <label>Name:</label>
//             <input type="text" name="{location}"onChange={(e)=> setLocation(e.target.value)}/>
//             //
//             <button onClick={handleSave}>Save</button>
//             <button onClick={handleCancel}>Cancel</button>
//         </div>
//
//     );
// //
//     function AccountPage() {
//         const [user, setUser]= useState({
//             name: "First Last",
//             email: "User@example.com",
//             phone: "123",
//             location:"Earth",
//         });
//         const [editing, setEditing]=useState(false);
//         const handleEdit =() => {
//             setEditing(true);
//         };
//
//         const handleSave = (data) => {
//             setUser(data);
//             setEditing(false);
//         };
//
//         const handleCancel = () => {
//             setEditing(false);
//         };
//
//         const handleDelete = () => {
//             // handle deleting the user's account
//         };
//
//         return (
//             <div>
//                 {editing ? (
//                     <AccountEdit user={user} onSave={handleSave} onCancel={handleCancel} />
//                 ) : (
//                     <AccountInfo user={user} onEdit={handleEdit} />
//                 )}
//                 <button onClick={handleDelete}>Delete Account</button>
//             </div>
//         );
//     }
//
// }
//
// export default AccountInfo