
import { useState } from "react";

import InterviewerCard from "../../components/candidate/InterviewerCard";

import {getInterviewersBySkills} from "../../services/InterviewManagementService";

function SearchInterviewers() {

    const [skills, setSkills] = useState([]);

    const [newSkill, setNewSkill] = useState("");

    const [interviewers, setInterviewers] = useState([]);

    const [loading, setLoading] = useState(false);

    const [error, setError] = useState("");

    const handleAddSkill =
        (event) => {

            if (
                event.key === "Enter" &&
                newSkill.trim()
            ) {

                event.preventDefault();

                const skill =
                    newSkill.trim();

                const alreadyExists =
                    skills.some(
                        currentSkill =>
                            currentSkill.toLowerCase() ===
                            skill.toLowerCase()
                    );

                if (alreadyExists) {

                    setNewSkill("");

                    return;

                }

                setSkills([
                    ...skills,
                    skill
                ]);

                setNewSkill("");

            }

        };

    const handleDeleteSkill =
        (skillToDelete) => {

            setSkills(

                skills.filter(
                    skill =>
                        skill !==
                        skillToDelete
                )

            );

        };

    const handleSearch =
        async () => {

            try {

                setLoading(true);

                setError("");

                const response =
                    await getInterviewersBySkills(
                        skills
                    );

                setInterviewers(
                    response.interviewers
                );

            } catch (error) {

                console.error(error);

                setError(
                    "Failed to fetch interviewers"
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
                py-10
                px-4
            "
        >

            <div
                className="
                    max-w-7xl
                    mx-auto
                "
            >

                <div
                    className="
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
                        Search Interviewers
                    </h1>

                    <p
                        className="
                            text-slate-500
                            mt-2
                        "
                    >
                        Find experienced interviewers
                        based on their skills.
                    </p>

                </div>

                <div
                    className="
                        bg-white
                        rounded-3xl
                        shadow-xl
                        border
                        border-slate-200
                        p-8
                        mb-8
                    "
                >

                    <div
                        className="
                            flex
                            justify-between
                            items-center
                            mb-6
                        "
                    >

                        <h2
                            className="
                                text-2xl
                                font-bold
                                text-slate-800
                            "
                        >
                            Search Skills
                        </h2>

                        <span
                            className="
                                px-4
                                py-2
                                rounded-full
                                bg-blue-100
                                text-blue-700
                                text-sm
                                font-semibold
                            "
                        >
                            {skills.length} Skills
                        </span>

                    </div>

                    <input
                        type="text"
                        value={newSkill}
                        placeholder="Type skill and press Enter"
                        onChange={(event) =>
                            setNewSkill(
                                event.target.value
                            )
                        }
                        onKeyDown={
                            handleAddSkill
                        }
                        className="
                            w-full
                            border
                            border-slate-300
                            rounded-xl
                            px-4
                            py-3
                            mb-6
                            focus:outline-none
                            focus:ring-2
                            focus:ring-blue-500
                        "
                    />

                    {
                        skills.length > 0 && (

                            <div
                                className="
                                    flex
                                    flex-wrap
                                    gap-3
                                    mb-6
                                "
                            >

                                {
                                    skills.map(
                                        skill => (

                                            <div
                                                key={skill}
                                                className="
                                                    flex
                                                    items-center
                                                    gap-2
                                                    px-4
                                                    py-2
                                                    rounded-full
                                                    bg-blue-50
                                                    border
                                                    border-blue-200
                                                    text-blue-700
                                                    font-medium
                                                "
                                            >

                                                <span>
                                                    {skill}
                                                </span>

                                                <button
                                                    onClick={() =>
                                                        handleDeleteSkill(
                                                            skill
                                                        )
                                                    }
                                                    className="
                                                        text-red-500
                                                        font-bold
                                                    "
                                                >
                                                    ×
                                                </button>

                                            </div>

                                        )
                                    )
                                }

                            </div>

                        )
                    }

                    <button
                        onClick={
                            handleSearch
                        }
                        disabled={
                            loading ||
                            skills.length === 0
                        }
                        className="
                            bg-blue-600
                            text-white
                            px-6
                            py-3
                            rounded-xl
                            font-medium
                            hover:bg-blue-700
                            transition
                            disabled:bg-slate-400
                            disabled:cursor-not-allowed
                        "
                    >
                        {
                            loading
                                ? "Searching..."
                                : "Search Interviewers"
                        }
                    </button>

                </div>

                {
                    error && (

                        <div
                            className="
                                text-red-500
                                font-medium
                                mb-6
                            "
                        >
                            {error}
                        </div>

                    )
                }

                {
                    interviewers.length === 0 &&
                    !loading
                        ? (

                            <div
                                className="
                                    bg-white
                                    rounded-3xl
                                    border
                                    border-dashed
                                    border-slate-300
                                    py-16
                                    text-center
                                "
                            >

                                <div
                                    className="
                                        text-5xl
                                        mb-4
                                    "
                                >
                                    🔍
                                </div>

                                <p
                                    className="
                                        text-slate-600
                                        font-medium
                                    "
                                >
                                    Search for interviewers
                                    using one or more skills.
                                </p>

                            </div>

                        )
                        : (

                            <div
                                className="
                                    grid
                                    grid-cols-1
                                    lg:grid-cols-2
                                    gap-6
                                "
                            >

                                {
                                    interviewers.map(
                                        interviewer => (

                                            <InterviewerCard
                                                key={
                                                    interviewer.email
                                                }
                                                interviewer={
                                                    interviewer
                                                }
                                            />

                                        )
                                    )
                                }

                            </div>

                        )
                }

            </div>

        </div>

    );

}

export default SearchInterviewers;

