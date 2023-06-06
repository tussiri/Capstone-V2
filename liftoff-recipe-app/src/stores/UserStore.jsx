import React, {createContext, useState} from 'react'

export const UserContext = createContext(null);

export function UserProvider({children}) {
    const [user, setUser] = useState(null);

    function login(userData) {

        setUser(userData);
    }

    function logout() {
        setUser(null)
    }

    return (<UserContext.Provider value={{user, login, logout}}>
            {children}
        </UserContext.Provider>
    )
        ;
}