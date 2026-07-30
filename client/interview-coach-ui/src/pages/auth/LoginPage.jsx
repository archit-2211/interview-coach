import { loginService, redirectGoogle } from "../../services/AuthService"

import { useState } from "react";

import { FcGoogle } from "react-icons/fc";
import { Link, useNavigate } from "react-router-dom";

function Login() {

    const [formData, setFormData] = useState({
        email: "",
        password: ""
    });

    const navigate = useNavigate()

    const handleChange = (event) => {

        setFormData({

            ...formData,

            [event.target.name]:
                event.target.value

        });

    };

    const handleSubmit = async (event) => {
        event.preventDefault()

        const response = await loginService(formData)
        localStorage.setItem("accessToken",response.accessToken)
        navigate("/interviews/me")


    };

     

    return (

        <div
            className="
                min-h-screen
                bg-slate-100
                flex
                items-center
                justify-center
                p-6
            "
        >

            <div
                className="
                    bg-white
                    w-full
                    max-w-md
                    rounded-3xl
                    shadow-xl
                    p-8
                "
            >

                <div
                    className="
                        text-center
                        mb-8
                    "
                >

                    <h1
                        className="
                            text-4xl
                            font-bold
                            text-slate-800
                        "
                    >
                        Interview Coach
                    </h1>

                    <p
                        className="
                            text-slate-500
                            mt-3
                        "
                    >
                        Practice interviews,
                        improve your skills,
                        and land your dream job.
                    </p>

                </div>

                <form
                    onSubmit={
                        handleSubmit
                    }
                    className="
                        space-y-5
                    "
                >

                    <div>

                        <label
                            className="
                                block
                                mb-2
                                font-medium
                                text-slate-700
                            "
                        >
                            Email
                        </label>

                        <input
                            type="email"
                            name="email"
                            value={
                                formData.email
                            }
                            onChange={
                                handleChange
                            }
                            placeholder="Enter your email"
                            className="
                                w-full
                                border
                                border-slate-300
                                rounded-xl
                                px-4
                                py-3
                                focus:outline-none
                                focus:ring-2
                                focus:ring-blue-500
                            "
                        />

                    </div>

                    <div>

                        <label
                            className="
                                block
                                mb-2
                                font-medium
                                text-slate-700
                            "
                        >
                            Password
                        </label>

                        <input
                            type="password"
                            name="password"
                            value={
                                formData.password
                            }
                            onChange={
                                handleChange
                            }
                            placeholder="Enter your password"
                            className="
                                w-full
                                border
                                border-slate-300
                                rounded-xl
                                px-4
                                py-3
                                focus:outline-none
                                focus:ring-2
                                focus:ring-blue-500
                            "
                        />

                    </div>

                    <button
                        type="submit"
                        className="
                            w-full
                            bg-blue-600
                            text-white
                            py-3
                            rounded-xl
                            font-semibold
                            hover:bg-blue-700
                            transition
                        "
                    >
                        Login
                    </button>

                </form>

                <div
                    className="
                        mt-6
                        text-center
                    "
                >

                    <span
                        className="
                            text-slate-600
                        "
                    >
                        Don't have an account?
                    </span>

                    <Link
                        to="/auth/register"
                        className="
                            ml-2
                            text-blue-600
                            font-medium
                            hover:text-blue-800
                        "
                    >
                        Register
                    </Link>

                </div>

                <button
                                    type="button"
                                    onClick = {redirectGoogle}
                                    className="
                                            w-full
                                            border
                                            border-slate-300
                                            py-3
                                            rounded-xl
                                            flex
                                            items-center
                                            justify-center
                                            gap-3
                                            font-medium
                                            hover:bg-slate-50
                                            transition
                                        "
                                >
                
                                    <FcGoogle size={24} />
                
                                    Continue with Google
                
                                </button>

            </div>

        </div>

    );
}

export default Login;