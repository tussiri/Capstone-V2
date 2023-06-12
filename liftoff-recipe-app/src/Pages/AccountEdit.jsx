import React, {useContext, useState} from "react";
import axios from "axios";
import authAxios from "../utility/authAxios";
import {UserContext} from "../stores/UserStore";

function AccountEdit() {
    const {user, logout} = useContext(UserContext)
    const [firstName, setFirstName] = useState(user.firstName);
    const [lastName, setLastName] = useState(user.lastName);
    const [email, setEmail] = useState(user.email);
    const [password, setPassword] = useState("");
    const [newPassword, setNewPassword] = useState("");

    const handleSave = async () => {
        try {
            const updatedUser = {
                firstName: firstName,
                lastName: lastName,
                email: email,
                password: password === "" ? undefined : password,
                // dateOfBirth:'',
            };
            console.log("Updated user: ", updatedUser)

            const response = await authAxios.put(`http://localhost:8080/users/${user.id}`, updatedUser);
            console.log("Server response: ", response)
            setPassword("");

        } catch (error) {
            console.log("There was an error updating the user's information: ", error)
        }
    };

    const handleChangePassword = async () => {
        try {
            const updatedUser = {
                password: newPassword
            };

            const response = await authAxios.put(`http://localhost:8080/users/${user.id}`, updatedUser);
            console.log("Server response for password: ", response)
            setNewPassword("");
            setPassword("");
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div>
            <h1>Edit Account Information</h1>
            <label>First Name:</label>
            <input
                type="text"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
            />
            <label>Last Name:</label>
            <input
                type="text"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
            />
            <button onClick={handleSave}>Save</button>
            <h2>Change Password</h2>
            <label>Old Password:</label>
            <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <label>New Password:</label>
            <input
                type="password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
            />
            <button onClick={handleChangePassword}>Change Password</button>
            <button onClick={logout}>Logout</button>
        </div>
    );
}

export default AccountEdit;