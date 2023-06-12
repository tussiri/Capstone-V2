// import React, {useContext, useEffect, useState} from 'react';
// import {UserContext} from "../stores/UserStore";
// import axios from "axios";
// import AccountEdit from "./AccountEdit";
// import {useNavigate} from "react-router-dom";
//
// function AccountInfo({onEdit, user}) {
// const navigate=useNavigate()
//     const handleUpload = async (e) => {
//         const file = e.target.files[0];
//         // upload file logic here
//     };
//
//     return (
//         <NavBar>
//         <div>
//             <h2>{user.firstName}</h2>
//             <p>{user.email}</p>
//             <img src={user.avatar} alt={user.name}/> {/* I assume user object has a avatar url property */}
//             <input type='file' accept='image/*' onChange={handleUpload}/>
//             <button onClick={onEdit}>Edit</button>
//         </div>
//     );
// }
//
//
// function AccountPage() {
//     const {user, setUser, parseJwt} = useContext(UserContext);
//     const [editing, setEditing] = useState(false);
//     const [currentUser, setCurrentUser] = useState(null);
//     const navigate = useNavigate();
//
//     useEffect(() => {
//         setCurrentUser(user);
//     }, [user]);
//
//     const handleEdit = () => {
//         setEditing(true);
//     };
//
//     const handleSave = async (data) => {
//         try {
//             const response = await axios.put(`/users/${user.id}/account`, data);
//             setUser(response.data);
//             setEditing(false);
//         } catch (error) {
//             console.error("Failed to update user:", error);
//         }
//     };
//
//     const handleCancel = () => {
//         setEditing(false);
//     };
//
//     const handleDelete = async () => {
//         if(window.confirm('Are you sure you want to delete your account? This action cannot be undone.')){
//             try {
//                 await axios.delete(`/users/${user.id}`);
//                 setUser(null);
//                 localStorage.removeItem("token");
//                 navigate('/');
//             } catch (error) {
//                 console.error("Failed to delete user:", error);
//             }
//         }
//     };
//
//     return (
//         <div>
//             {editing ? (
//                 <AccountEdit user={currentUser} onSave={handleSave} onCancel={handleCancel}/>
//             ) : (
//                 <AccountInfo user={currentUser} onEdit={handleEdit}/>
//             )}
//             <button onClick={handleDelete}>Delete Account</button>
//         </div>
//     );
// }
//
// export default AccountPage;

import React, {useContext, useEffect, useState} from 'react';
import {UserContext} from "../stores/UserStore";
import axios from "axios";
import AccountEdit from "./AccountEdit";
import {useNavigate} from "react-router-dom";
import authAxios from "../utility/authAxios";

function AccountInfo({onEdit, user}) {
    const navigate=useNavigate()
    const handleUpload = async (e) => {
        const file = e.target.files[0];
        // upload file logic here
    };

    return (
        user &&
        <div>
            <h2>{user.firstName}</h2>
            <p>{user.email}</p>
            <img src={user.avatar} alt={user.name}/> {/* I assume user object has a avatar url property */}
            <input type='file' accept='image/*' onChange={handleUpload}/>
            <button onClick={onEdit}>Edit</button>
        </div>
    );
}

function AccountPage() {
    const {user, setUser, parseJwt} = useContext(UserContext);
    const [editing, setEditing] = useState(false);
    const [currentUser, setCurrentUser] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        setCurrentUser(user);
    }, [user]);

    const handleEdit = () => {
        setEditing(true);
    };

    const handleSave = async (data) => {
        console.log(user);
        console.log(user.id);
        try {
            const response = await authAxios.put(`/users/${user.id}`, data);
            console.log("Response: ", response.data)
            setUser(response.data);
            setEditing(false);
        } catch (error) {
            console.error("Failed to update user:", error);
        }
    };

    const handleCancel = () => {
        setEditing(false);
    };

    const handleDelete = async () => {
        if(window.confirm('Are you sure you want to delete your account? This action cannot be undone.')){
            try {
                await axios.delete(`/users/${user.id}`);
                setUser(null);
                localStorage.removeItem("token");
                navigate('/');
            } catch (error) {
                console.error("Failed to delete user:", error);
            }
        }
    };

    return (
        <div>
            {editing ? (
                <AccountEdit user={currentUser} onSave={handleSave} onCancel={handleCancel}/>
            ) : (
                currentUser && <AccountInfo user={currentUser} onEdit={handleEdit}/>
            )}
            <button onClick={handleDelete}>Delete Account</button>
        </div>
    );
}

export default AccountPage;