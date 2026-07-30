import { Routes, Route, Navigate } from "react-router-dom";

import "./App.css";

import RegisterPage from "./pages/auth/RegisterPage";
import Login from "./pages/auth/LoginPage";

import UserProfile from "./pages/user/UserProfile";
import MyInterviews from "./pages/user/MyInterviews";
import InterviewRequests from "./pages/user/InterviewRequests";

import SearchInterviewers from "./pages/candidate/SearchInterviewers";

import InterviewerProfile from "./pages/interviewer/InterviewerProfile";
import MySlots from "./pages/interviewer/MySlots";

import ProtectedRoute from "./components/ProtectedRoute";
import PublicRoute from "./components/PublicRoute"
import RoleProtectedComponent from "./components/RoleProtectedComponent";

import AppLayout from "./layouts/AppLayout";
import ChooseRolePage from "./pages/auth/ChooseRolePage";
import AuthSuccess from "./components/user/AuthSuccess";
import { isLoggedIn } from "./services/AuthService";

function NotFound() {
    return (
        <div className="min-h-screen flex flex-col items-center justify-center bg-slate-100">
            <h1 className="text-8xl font-bold text-slate-800">
                404
            </h1>

            <p className="text-slate-500 mt-4 text-lg">
                Oops! The page you're looking for doesn't exist.
            </p>

            <button
                onClick={() => (window.location.href = "/profile/me")}
                className="mt-8 px-6 py-3 rounded-xl bg-blue-600 text-white hover:bg-blue-700 transition"
            >
                Go Home
            </button>
        </div>
    );
}

function App() {
    return (
        <Routes>

            {/* Default Route */}
            <Route
                path="/"
                element={
                    isLoggedIn()
                        ? <Navigate to="/profile/me" replace />
                        : <Navigate to="/auth/login" replace />
                }
            />

            {/* Public Routes */}

            <Route
                path="/role/me/:id"
                element={<ChooseRolePage />}
            />

            <Route
                path="/auth/success"
                element={<AuthSuccess />}
            />

            <Route element={<PublicRoute />}>

                <Route path="/auth">

                    <Route
                        index
                        element={<Navigate to="login" replace />}
                    />

                    <Route
                        path="login"
                        element={<Login />}
                    />

                    <Route
                        path="register"
                        element={<RegisterPage />}
                    />

                </Route>

            </Route>

           

            {/* Protected Routes */}

            <Route element={<ProtectedRoute />}>

                <Route element={<AppLayout />}>

                    {/* Common */}

                    <Route
                        path="/profile/me"
                        element={<UserProfile />}
                    />

                    <Route
                        path="/interviews/me"
                        element={<MyInterviews />}
                    />

                    <Route
                        path="/interview-requests"
                        element={<InterviewRequests />}
                    />

                    {/* Candidate */}

                    <Route
                        element={
                            <RoleProtectedComponent
                                allowedRoles={[
                                    "CANDIDATE",
                                ]}
                            />
                        }
                    >

                        <Route
                            path="/search-interviewers"
                            element={<SearchInterviewers />}
                        />

                        <Route
                            path="/interviewers/:email"
                            element={<InterviewerProfile />}
                        />

                    </Route>

                    {/* Interviewer */}

                    <Route
                        element={
                            <RoleProtectedComponent
                                allowedRoles={[
                                    "INTERVIEWER",
                                ]}
                            />
                        }
                    >

                        <Route
                            path="/slots/me"
                            element={<MySlots />}
                        />

                    </Route>

                    {/* Admin */}

                    <Route
                        element={
                            <RoleProtectedComponent
                                allowedRoles={[
                                    "ADMIN",
                                ]}
                            />
                        }
                    >

                        <Route
                            path="/admin/users"
                            element={<div>Admin Users</div>}
                        />

                        <Route
                            path="/admin/verifications"
                            element={<div>Admin Verifications</div>}
                        />

                    </Route>

                    {/* Invalid Protected Route */}

                    <Route
                        path="*"
                        element={<Navigate to="/profile/me" replace />}
                    />

                </Route>

            </Route>

            {/* Global 404 */}

            <Route
                path="*"
                element={<NotFound />}
            />

        </Routes>
    );
}

export default App;