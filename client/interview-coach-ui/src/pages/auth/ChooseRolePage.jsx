
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { FaUserGraduate } from "react-icons/fa";
import { MdOutlineWork } from "react-icons/md";
import { setRole } from "../../services/AuthService";

function ChooseRolePage() {

    const navigate = useNavigate();
    const { id: userId } = useParams();

    const [loading, setLoading] = useState(false);
    const [success, setSuccess] = useState(false);
    const [error, setError] = useState("");

    const handleRoleSelection = async (role) => {

        try {

            setLoading(true);
            setError("");

            await setRole(userId, role);

            setSuccess(true);

            setTimeout(() => {

                navigate("/auth/login");

            }, 2000);

        } catch (err) {

            setError(
                err?.response?.data?.message ||
                "Failed to save role. Please try again."
            );

        } finally {

            setLoading(false);

        }

    };

    if (loading) {

        return (

            <div
                className="
                    min-h-screen
                    flex
                    items-center
                    justify-center
                    bg-slate-100
                "
            >

                <div
                    className="
                        bg-white
                        p-8
                        rounded-2xl
                        shadow-lg
                        text-center
                    "
                >

                    <h2
                        className="
                            text-2xl
                            font-semibold
                            text-blue-600
                        "
                    >
                        Saving your role...
                    </h2>

                    <p
                        className="
                            text-slate-500
                            mt-2
                        "
                    >
                        Please wait
                    </p>

                </div>

            </div>

        );

    }

    if (success) {

        return (

            <div
                className="
                    min-h-screen
                    flex
                    items-center
                    justify-center
                    bg-slate-100
                "
            >

                <div
                    className="
                        bg-white
                        p-8
                        rounded-2xl
                        shadow-lg
                        text-center
                    "
                >

                    <h2
                        className="
                            text-3xl
                            font-bold
                            text-green-600
                        "
                    >
                        ✓ Role Selected Successfully
                    </h2>

                    <p
                        className="
                            text-slate-500
                            mt-3
                        "
                    >
                        Redirecting to login...
                    </p>

                </div>

            </div>

        );

    }

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
                    rounded-3xl
                    shadow-xl
                    p-10
                    max-w-4xl
                    w-full
                "
            >

                <div className="text-center mb-10">

                    <h1
                        className="
                            text-4xl
                            font-bold
                            text-slate-800
                        "
                    >
                        Choose Your Role
                    </h1>

                    <p
                        className="
                            text-slate-500
                            mt-3
                        "
                    >
                        Select how you want to use Interview Coach
                    </p>

                </div>

                {
                    error && (

                        <div
                            className="
                                mb-6
                                rounded-xl
                                bg-red-50
                                border
                                border-red-200
                                p-4
                                text-red-700
                            "
                        >
                            ✗ {error}
                        </div>

                    )
                }

                <div
                    className="
                        grid
                        md:grid-cols-2
                        gap-8
                    "
                >

                    <div
                        onClick={() =>
                            handleRoleSelection("CANDIDATE")
                        }
                        className="
                            cursor-pointer
                            border
                            border-slate-200
                            rounded-2xl
                            p-8
                            hover:border-blue-500
                            hover:shadow-lg
                            transition
                        "
                    >

                        <div className="flex justify-center mb-5">

                            <FaUserGraduate
                                size={60}
                                className="text-blue-600"
                            />

                        </div>

                        <h2
                            className="
                                text-2xl
                                font-semibold
                                text-center
                                mb-4
                            "
                        >
                            Candidate
                        </h2>

                        <ul
                            className="
                                text-slate-600
                                space-y-2
                            "
                        >
                            <li>• Build your profile</li>
                            <li>• Book mock interviews</li>
                            <li>• Receive feedback</li>
                            <li>• Improve interview skills</li>
                        </ul>

                    </div>

                    <div
                        onClick={() =>
                            handleRoleSelection("INTERVIEWER")
                        }
                        className="
                            cursor-pointer
                            border
                            border-slate-200
                            rounded-2xl
                            p-8
                            hover:border-green-500
                            hover:shadow-lg
                            transition
                        "
                    >

                        <div className="flex justify-center mb-5">

                            <MdOutlineWork
                                size={60}
                                className="text-green-600"
                            />

                        </div>

                        <h2
                            className="
                                text-2xl
                                font-semibold
                                text-center
                                mb-4
                            "
                        >
                            Interviewer
                        </h2>

                        <ul
                            className="
                                text-slate-600
                                space-y-2
                            "
                        >
                            <li>• Create interview slots</li>
                            <li>• Conduct mock interviews</li>
                            <li>• Provide feedback</li>
                            <li>• Help candidates grow</li>
                        </ul>

                    </div>

                </div>

            </div>

        </div>

    );

}

export default ChooseRolePage;
