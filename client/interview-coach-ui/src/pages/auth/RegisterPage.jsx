import { useState } from "react";
import { registerService, redirectGoogle } from "../../services/AuthService";
import { Link } from 'react-router-dom'
import { FcGoogle } from "react-icons/fc";
function RegisterPage() {

    const [formData, setFormData] = useState({
        name: "",
        phoneNumber: "",
        email: "",
        password: "",
        role: "CANDIDATE"
    });

    const [loading, setLoading] = useState(false);
    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const handleChange = (event) => {

        setFormData({

            ...formData,

            [event.target.name]:
                event.target.value

        });

    };

    const handleSubmit = async (event) => {

        event.preventDefault();

        try {

            setLoading(true);
            setErrorMessage("");
            setSuccessMessage("");

            await registerService(formData);

            setSuccessMessage(
                "Account created successfully! Redirecting to login..."
            );

            setFormData({
                name: "",
                phoneNumber: "",
                email: "",
                password: "",
                role: "CANDIDATE"
            });

            setTimeout(() => {
                window.location.href = "/auth/login";
            }, 2000);

        } catch (error) {

            setErrorMessage(
                error?.response?.data?.message ||
                "Registration failed. Please try again."
            );

        } finally {

            setLoading(false);

        }

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
                    max-w-lg
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
                            text-3xl
                            font-bold
                            text-slate-800
                        "
                    >
                        Create Account
                    </h1>

                    <p
                        className="
                            text-slate-500
                            mt-2
                        "
                    >
                        Join Interview Coach
                    </p>

                </div>
                {
                    successMessage && (
                        <div
                            className="
                mb-5
                rounded-xl
                border
                border-green-200
                bg-green-50
                p-4
            "
                        >
                            <p className="text-green-700 font-medium">
                                {successMessage}
                            </p>
                        </div>
                    )
                }
                {
                    errorMessage && (
                        <div
                            className="
                mb-5
                rounded-xl
                border
                border-red-200
                bg-red-50
                p-4
            "
                        >
                            <p className="text-red-700 font-medium">
                                {errorMessage}
                            </p>
                        </div>
                    )
                }
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
                            Full Name
                        </label>

                        <input
                            type="text"
                            name="name"
                            value={
                                formData.name
                            }
                            onChange={
                                handleChange
                            }
                            placeholder="Enter your name"
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
                            Phone Number
                        </label>

                        <input
                            type="text"
                            name="phoneNumber"
                            value={
                                formData.phoneNumber
                            }
                            onChange={
                                handleChange
                            }
                            placeholder="Enter phone number"
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
                            placeholder="Enter email"
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
                            placeholder="Enter password"
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
                            Role
                        </label>

                        <select
                            name="role"
                            value={
                                formData.role
                            }
                            onChange={
                                handleChange
                            }
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
                        >

                            <option
                                value="CANDIDATE"
                            >
                                Candidate
                            </option>

                            <option
                                value="INTERVIEWER"
                            >
                                Interviewer
                            </option>

                        </select>

                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className="
        w-full
        bg-blue-600
        text-white
        py-3
        rounded-xl
        font-semibold
        hover:bg-blue-700
        transition
        disabled:opacity-50
        disabled:cursor-not-allowed
    "
                    >
                        {
                            loading
                                ? "Creating Account..."
                                : "Register"
                        }
                    </button>

                </form>

                <div
                    className="
        text-center
        mt-4
    "
                >

                    <span
                        className="
            text-slate-600
        "
                    >
                        Already have an account?
                    </span>

                    <Link
                        to="/auth/login"
                        className="
            ml-2
            text-blue-600
            font-medium
            hover:text-blue-800
        "
                    >
                        Login
                    </Link>



                </div>

                <button
                    type="button"
                    onClick={redirectGoogle}
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

export default RegisterPage;