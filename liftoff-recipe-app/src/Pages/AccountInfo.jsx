// import React, {useContext, useEffect, useState} from 'react';
// import {UserContext} from "../stores/UserStore";
//
// function AccountInfo({user, onEdit}) {
//     const {name, email, phone, location} = user;
//     return (
//         <div>
//             <h1>Account Information </h1>
//             <p>Name: {firstName} {lastName}</p>
//             <p>Email: {email}</p>
//             <p>Phone: {phone}</p>
//             <p>Location: {location}</p>
//             <button onClick={onEdit}>Edit</button>
//         </div>
//     );
// }
//
// function AccountEdit({user, onSave, onCancel}) {
//     const [firstName, setFirstName] = useState(user.firstName);
//     const [lastName, setLastName] = useState(user.lastName);
//     const [email, setEmail] = useState(user.email);
//     const [phone, setPhone] = useState(user.phone);
//     const [location, setLocation] = useState(user.location);
//
//     const handleSave = () => {
//         onSave({name, email, phone, location});
//     };
//
//     const handleCancel = () => {
//         onCancel();
//     };
//
//     return (
//         <div>
//             <h1>Edit Account Information</h1>
//             <label>Name:</label>
//             <input type="text" value={name} onChange={(e) => setName(e.target.value)}/>
//             <label>Email:</label>
//             <input type="email" value={email} onChange={(e) => setEmail(e.target.value)}/>
//             <label>Phone:</label>
//             <input type="tel" value={phone} onChange={(e) => setPhone(e.target.value)}/>
//             <label>Location:</label>
//             <input type="text" value={location} onChange={(e) => setLocation(e.target.value)}/>
//             <button onClick={handleSave}>Save</button>
//             <button onClick={handleCancel}>Cancel</button>
//         </div>
//     );
// }
//
// function AccountPage() {
//     const {user, parseJwt} = useContext(UserContext);
//     const [editing, setEditing] = useState(false);
//     const [currentUser, setCurrentUser] = useState(null);
//
//
//     useEffect(() => {
//         setCurrentUser(user);
//     }, [user]);
//
//     const handleEdit = () => {
//         setEditing(true);
//     };
//
//     const handleSave = (data) => {
//         setUser(data);
//         setEditing(false);
//     };
//
//     const handleCancel = () => {
//         setEditing(false);
//     };
//
//     const handleDelete = () => {
//         // handle deleting the user's account
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