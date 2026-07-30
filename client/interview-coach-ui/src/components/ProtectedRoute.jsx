import {Navigate,Outlet} from "react-router-dom";

function ProtectedRoute() {

    const token =
        localStorage.getItem(
            "accessToken"
        );

    return token
        ? <Outlet />
        : (
            <Navigate
                to="/auth/login"
                replace
            />
        );
}

export default ProtectedRoute;