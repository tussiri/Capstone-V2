import axios from 'axios'

const authAxios = axios.create(
    {baseURL: 'http://localhost:8080'}
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

authAxios.interceptors.response.use(
    (response)=>{
        return response;
    },
    (error)=>{
        if(error.response && error.response.status===401){
            localStorage.removeItem("token");
            window.location.href="/";
        }
        return Promise.reject(error);
    }
)


export default authAxios;