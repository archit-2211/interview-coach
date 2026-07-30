import { Link, useNavigate } from "react-router-dom";

function InterviewerCard({ interviewer }) {

    const navigate = useNavigate() 


    return (

        <div
            className="
                bg-white
                rounded-3xl
                border
                border-slate-200
                shadow-md
                hover:shadow-xl
                transition
                p-6
            "
        >

            <div
                className="
                    flex
                    justify-between
                    items-start
                    mb-5
                "
            >

                <div>

                    <h2
                        className="
                            text-xl
                            font-bold
                            text-slate-800
                        "
                    >
                        {interviewer.fullName}
                    </h2>

                    <p
                        className="
                            text-slate-500
                            mt-1
                        "
                    >
                        {interviewer.email}
                    </p>

                </div>

                <div
                    className="
                        flex
                        items-center
                        gap-2
                        bg-yellow-50
                        border
                        border-yellow-200
                        rounded-xl
                        px-4
                        py-2
                    "
                >

                    <span
                        className="
                            text-yellow-500
                        "
                    >
                        ⭐
                    </span>

                    <span
                        className="
                            font-semibold
                            text-slate-800
                        "
                    >
                        {interviewer.rating}
                    </span>

                </div>

            </div>

            <div
                className="
                    flex
                    flex-wrap
                    gap-2
                    mb-6
                "
            >

                {
                    interviewer.skills.map(
                        skill => (

                            <span
                                key={skill}
                                className="
                                    px-3
                                    py-1.5
                                    rounded-full
                                    bg-blue-50
                                    border
                                    border-blue-200
                                    text-blue-700
                                    text-sm
                                    font-medium
                                "
                            >
                                {skill}
                            </span>

                        )
                    )
                }

            </div>

            <div
                className="
                    flex
                    justify-end
                "
            >

                <Link
                    to={`/interviewers/${interviewer.email}`}
                    className="
                        px-5
                        py-3
                        rounded-xl
                        bg-blue-600
                        text-white
                        font-medium
                        hover:bg-blue-700
                        transition
                    "
                >
                    View Profile
                </Link>

            </div>

        </div>

    );

}

export default InterviewerCard;