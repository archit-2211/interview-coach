
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function AuthSuccess() {
    const navigate = useNavigate();

    useEffect(() => {
        const token = new URLSearchParams(
            window.location.hash.substring(1)
        ).get("token");

        if (token) {
            localStorage.setItem("accessToken", token);

            // Remove token from URL
            window.history.replaceState({}, "", "/");

            navigate("/");
        } else {
            navigate("/login");
        }
    }, [navigate]);

    return <div>Logging you in...</div>;
}

export default AuthSuccess;
