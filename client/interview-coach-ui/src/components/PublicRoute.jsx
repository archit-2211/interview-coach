import { Navigate, Outlet } from "react-router-dom";
import { isLoggedIn } from "../services/AuthService";

function PublicRoute() {
    return isLoggedIn()
        ? <Navigate to="/profile/me" replace />
        : <Outlet />;
}

export default PublicRoute;