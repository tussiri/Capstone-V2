import React, { useState } from "react";
import axios from "axios";
import authAxios from "../utility/authAxios";

function AccountEdit({ user }) {
    const [firstName, setFirstName] = useState(user.firstName);
    const [lastName, setLastName] = useState(user.lastName);
    const [email, setEmail] = useState(user.email);
    const [password, setPassword] = useState("");
    const [dateOfBirth, setDateOfBirth] = useState(user.dateOfBirth);

    const handleSave = async () => {
        try {
            const updatedUser = {
                firstName:'',
                lastName:'',
                email:'',
                password:'',
                dateOfBirth:'',
            };

            await authAxios.put(`http://localhost:8080/users/${user.id}`, updatedUser);

        } catch (error) {
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
            <label>Email:</label>
            <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <label>Password:</label>
            <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <label>Date of Birth:</label>
            <input
                type="date"
                value={dateOfBirth}
                onChange={(e) => setDateOfBirth(e.target.value)}
            />
            <button onClick={handleSave}>Save</button>
        </div>
    );
}

export default AccountEdit;