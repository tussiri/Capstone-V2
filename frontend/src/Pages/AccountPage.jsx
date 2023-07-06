// import React, {useEffect, useState} from 'react';
// import axios from 'axios';
// import {useNavigate} from 'react-router-dom';
// import Button from '@mui/material/Button';
// import AccountInfo from './AccountInfo';
// import authAxios from '../utility/authAxios';
// import AccountEdit from "./AccountEdit";
//
//
// function AccountPage() {
//     // const { user, setUser } = useContext(UserContext);
//     const [user, setUser] = useState(null)
//     // const [editing, setEditing] = useState(false);
//     // const [deleted, setDeleted] = useState(false);
//     const navigate = useNavigate();
//     const userId = localStorage.getItem('userId');
//
//     useEffect(() => {
//         const fetchUser = async () => {
//             try {
//                 const response = await authAxios.get(`http://localhost:8080/users/${userId}`);
//                 setUser(response.data.data);
//             } catch (error) {
//                 console.error('Failed to fetch user:', error);
//             }
//         };
//
//         if (userId) {
//             fetchUser();
//         }
//     }, [userId]);
//
//     useEffect(() => {
//         if (deleted) {
//             setUser(null);
//             localStorage.removeItem('token');
//             navigate('/');
//         }
//     }, [deleted, navigate]);
//
//     const handleDelete = async () => {
//         if (window.confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
//             const deleteRecipes = window.confirm('Do you also want to delete all your recipes?');
//
//             try {
//                 await authAxios.delete(`http://localhost:8080/users/${userId}?deleteRecipes=${deleteRecipes}`);
//                 setUser(null);
//                 localStorage.removeItem('token');
//                 navigate('/');
//             } catch (error) {
//                 console.error('Failed to delete user:', error);
//             }
//         }
//     };
//
//     return (
//         <div>
//             {user ? (
//                 <>
//                     <AccountInfo user={user} />
//                     <Button variant="contained" onClick={handleDelete}>
//                         Delete Account
//                     </Button>
//                 </>
//             ) : (
//                 <p>Loading user information...</p>
//             )}
//         </div>
//     );
// }
//
// export default AccountPage;