import { jwtDecode } from "jwt-decode"; 
import api from "./api"


export async function registerService(requestPayload) {

    console.log("Received Register Data")
    console.log(requestPayload)

    const response = await api.post("http://localhost:8080/auth/register", requestPayload)
    console.log(response.data)


}

export async function loginService(requestPayload) {
    console.log("Request Received ")
    console.log(requestPayload)
    const response = await api.post("http://localhost:8080/auth/login", requestPayload, {
        withCredentials : true
    })
    console.log(response.data)

    return response.data
}

export async function setRole(userId, role) {
    await api.post("http://localhost:8080/auth/role/setup", {
        role : role , jwtToken : userId
    })

}


export function isLoggedIn() {
    const token = localStorage.getItem("accessToken");

    if (!token) return false;

    try {
        const decoded = jwtDecode(token);

        return decoded.exp * 1000 > Date.now();
    } catch {
        return false;
    }
}

export function getRole() {
    const token = localStorage.getItem("accessToken");
    
    if (!token) {
        throw new Error("NO TOKEN FOUND");
    }

    try {
        const claims = jwtDecode(token);
        console.log("Decoded claims:", claims);
        
        if (!claims.role) {
            throw new Error("NO ROLE FOUND IN TOKEN");
        }
        
        return claims.role;
    } catch (error) {
        console.error("Failed to decode token:", error);
        throw new Error("INVALID TOKEN");
    }
}



export function redirectGoogle () {
    window.location.href = "http://localhost:8080/login/oauth2/code/google"
}

export async function logout() {
    await api.get("http://localhost:8080/auth/logout", {withCredentials : true})
    console.log("Logout Successful") ; 
}
