import axios from 'axios'

const authAxios = axios.create(
    {baseURL: `${process.env.REACT_APP_BACKEND_URL}`}
);

authAxios.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    // console.log("Interceptor Token: ", token)

    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    // console.log("Interceptor token: ", config.headers)
    return config
}, (error) => {
    return Promise.reject(error);
})


authAxios.interceptors.response.use((response) => {
    return response;
}, (error) => {
    if (error.response && error.response.status === 401) {
        console.error("Token is invalid or expired. Redirecting to login.");
    }
    return Promise.reject(error);
});

export default authAxios;
