import React, { useContext } from 'react';
import { UserContext } from '../stores/UserStore';
import { useNavigate } from 'react-router-dom';
import Button from '@mui/material/Button';
import authAxios from '../utility/authAxios';

function AccountInfo({ onEdit }) {
    const navigate = useNavigate();
    const { user } = useContext(UserContext);
    const userId = localStorage.getItem('userId');

    const handleUpload = async (e) => {
        const file = e.target.files[0];
        // upload file logic here
    };

    const handleDelete = async () => {
        if (window.confirm('Are you sure you want to delete your account? This action cannot be undone.')) {
            const deleteRecipes = window.confirm('Do you also want to delete all your recipes?');

            try {
                await authAxios.delete(`http://localhost:8080/users/${userId}?deleteRecipes=${deleteRecipes}`);

                navigate('/');
            } catch (error) {
                console.error('Failed to delete user:', error);
            }
        }
    };

    return (
        user && (
            <div>
                <h2>{user.firstName}</h2>
                <h2>{user.lastName}</h2>
                <p>{user.email}</p>
                <img src={user.avatar} alt={user.name} />
                <input type="file" accept="image/*" onChange={handleUpload} />
                <Button variant="contained" onClick={onEdit}>
                    Edit
                </Button>
                <Button variant="contained" onClick={handleDelete}>
                    Delete Account
                </Button>
            </div>
        )
    );
}

export default AccountInfo;