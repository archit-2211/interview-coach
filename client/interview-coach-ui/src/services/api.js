import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8080",
    headers: {
        "Content-Type": "application/json"
    }
});
api.interceptors.request.use(

    (config) => {

        const token =
            localStorage.getItem(
                "accessToken"
            );

        if (token) {

            config.headers.Authorization =
                `Bearer ${token}`;

        }

        return config;

    },

    (error) => {

        return Promise.reject(
            error
        );

    }

);



api.interceptors.response.use(

    (response) => response,

    async (error) => {

        console.log("Interceptor triggered");
        console.log("Status:", error.response?.status);
        console.log(error.response)

        const originalRequest = error.config;

        if (
            error.response?.status === 401 &&
            !originalRequest._retry
        ) {

            console.log("Attempting refresh...");

            originalRequest._retry = true;

            try {

                const refreshResponse = await axios.post(
                    "http://localhost:8080/auth/refresh",
                    {},
                    {
                        withCredentials: true
                    }
                );

                const newToken =
                    refreshResponse.data.accessToken;

                localStorage.setItem(
                    "accessToken",
                    newToken
                );

                originalRequest.headers.Authorization =
                    `Bearer ${newToken}`;

                return api(originalRequest);

            } catch (refreshError) {

                localStorage.clear();

                window.location.href =
                    "/login";

                return Promise.reject(refreshError);

            }

        }

        return Promise.reject(error);

    }

);
export default api;
export {api}