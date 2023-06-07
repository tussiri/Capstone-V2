import React, {useContext} from 'react';
import {styled} from '@mui/system';
import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography';
import {UserContext} from "../stores/UserStore";

const useStyles = styled(() => ({
    root: {
        width: '20%',
        height: '100vh',
        position: 'fixed',
        backgroundColor: '#f5f5f5',
        padding: '20px',
        boxSizing: 'border-box',
    },
    avatar: {
        width: '100px',
        height: '100px',
        margin: 'auto',
    },
    username: {
        textAlign: 'center',
        marginTop: '10px',
    },
    email: {
        textAlign: 'center',
        marginTop: '10px',
        color: '#888',
    },
}));

const Sidebar = () => {
    const classes = useStyles();
    const {user} = useContext(UserContext)

    if (!user) {
        return null
    }

    return (
        <Box className={classes.root}>
            <Avatar className={classes.avatar}>{user.firstName.charAt(0)}</Avatar>
            <Typography variant="h5" className={classes.email}>{user.firstName} {user.lastName}</Typography>
            <Typography className={classes.email}>{user.email}</Typography>
            {/* Other profile information */}
        </Box>
    );
};

export default Sidebar;