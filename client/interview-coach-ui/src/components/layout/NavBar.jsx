import { NavLink, useNavigate } from "react-router-dom";

import { getRole, logout } from "../../services/AuthService";

function Navbar() {


const role =
    getRole();

const navigate =
    useNavigate();

const handleLogout =
    () => {

        localStorage.removeItem(
            "accessToken"
        );
        logout() ; 

        navigate(
            "/auth/login"
        );

    };

const getNavLinkClass =
    ({ isActive }) =>
        `
            font-medium
            transition
            ${
                isActive
                    ? "text-blue-600"
                    : "text-slate-700 hover:text-blue-600"
            }
        `;

return (

    <nav
        className="
            bg-white
            border-b
            border-slate-200
            sticky
            top-0
            z-50
        "
    >

        <div
            className="
                max-w-7xl
                mx-auto
                px-4
                py-4
                flex
                justify-between
                items-center
            "
        >

            <NavLink
                to="/profile/me"
                className="
                    text-2xl
                    font-bold
                    text-blue-600
                "
            >
                Interview Coach
            </NavLink>

            <div
                className="
                    flex
                    items-center
                    gap-6
                "
            >

                {
                    role === "CANDIDATE" && (
                        <>
                            <NavLink
                                to="/profile/me"
                                className={
                                    getNavLinkClass
                                }
                            >
                                Profile
                            </NavLink>

                            <NavLink
                                to="/search-interviewers"
                                className={
                                    getNavLinkClass
                                }
                            >
                                Interviewers
                            </NavLink>

                            <NavLink
                                to="/interview-requests"
                                className={
                                    getNavLinkClass
                                }
                            >
                                Requests
                            </NavLink>

                            <NavLink
                                to="/interviews/me"
                                className={
                                    getNavLinkClass
                                }
                            >
                                Interviews
                            </NavLink>
                        </>
                    )
                }

                {
                    role === "INTERVIEWER" && (
                        <>
                            <NavLink
                                to="/profile/me"
                                className={
                                    getNavLinkClass
                                }
                            >
                                Profile
                            </NavLink>

                            <NavLink
                                to="/slots/me"
                                className={
                                    getNavLinkClass
                                }
                            >
                                My Slots
                            </NavLink>

                            <NavLink
                                to="/interview-requests"
                                className={
                                    getNavLinkClass
                                }
                            >
                                Requests
                            </NavLink>

                            <NavLink
                                to="/interviews/me"
                                className={
                                    getNavLinkClass
                                }
                            >
                                Interviews
                            </NavLink>
                        </>
                    )
                }

                {
                    role === "ADMIN" && (
                        <>
                            <NavLink
                                to="/admin/users"
                                className={
                                    getNavLinkClass
                                }
                            >
                                Users
                            </NavLink>

                            <NavLink
                                to="/admin/verifications"
                                className={
                                    getNavLinkClass
                                }
                            >
                                Verifications
                            </NavLink>
                        </>
                    )
                }

                <button
                    onClick={
                        handleLogout
                    }
                    className="
                        px-4
                        py-2
                        rounded-xl
                        bg-red-600
                        text-white
                        font-medium
                        hover:bg-red-700
                        transition
                    "
                >
                    Logout
                </button>

            </div>

        </div>

    </nav>

);


}

export default Navbar;
