import { useState } from "react";
import { getRole } from "../../services/AuthService";



function InterviewCard({ interview, handleCompleteInterview, handleSubmitFeedback, handleSubmitAssessment, handleCancelInterview }) {

    const [showCompleteModal, setShowCompleteModal] = useState(false);
    const role = getRole()
    const [showFeedbackModal, setShowFeedbackModal] = useState(false);
    const [showAssessmentModal, setShowAssessmentModal] = useState(false);
    const [rating, setRating] = useState(5);
    const [review, setReview] = useState("");
    const [comments, setComments] = useState("");

    const isScheduled = (interview.interviewStatus === "SCHEDULED");

    const submitFeedback =
        async () => {

            try {

                await handleSubmitFeedback(
                    interview.interviewId,
                    rating,
                    review
                );

                setShowFeedbackModal(
                    false
                );

            } catch (error) {

                console.error(
                    error
                );

            }

        };

    const submitAssessment =
        async () => {

            try {

                await handleSubmitAssessment(
                    interview.interviewId,
                    rating,
                    comments
                );

                setShowAssessmentModal(
                    false
                );

            } catch (error) {

                console.error(
                    error
                );

            }

        };

    const handleComplete =
        async () => {

            try {

                await handleCompleteInterview(
                    interview.interviewId
                );

                setShowCompleteModal(
                    false
                );

            } catch (error) {

                console.error(
                    error
                );

            }

        };

    const getStatusClasses = () => {

        switch (
        interview.interviewStatus
        ) {

            case "SCHEDULED":

                return `
                    bg-blue-100
                    text-blue-700
                `;

            case "COMPLETED":

                return `
                    bg-green-100
                    text-green-700
                `;

            case "CANCELLED":

                return `
                    bg-red-100
                    text-red-700
                `;

            default:

                return `
                    bg-slate-100
                    text-slate-700
                `;

        }

    };

    return (
        <>

            <div
                className="
                bg-white
                rounded-3xl
                border
                border-slate-200
                shadow-lg
                p-6
                hover:shadow-xl
                transition
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

                        <h3
                            className="
                            text-xl
                            font-bold
                            text-slate-800
                        "
                        >
                            Mock Interview
                        </h3>

                        <p
                            className="
                            text-slate-500
                            mt-1
                        "
                        >
                            Interview ID:
                            {" "}
                            {
                                interview.interviewId
                            }
                        </p>

                    </div>

                    <span
                        className={`
                        px-4
                        py-2
                        rounded-full
                        text-sm
                        font-semibold
                        ${getStatusClasses()}
                    `}
                    >
                        {
                            interview.interviewStatus
                        }
                    </span>

                </div>

                <div
                    className="
                    grid
                    grid-cols-1
                    md:grid-cols-2
                    gap-4
                    mb-5
                "
                >

                    <InfoItem
                        label="Candidate"
                        value={
                            interview.candidateEmail
                        }
                    />

                    <InfoItem
                        label="Interviewer"
                        value={
                            interview.interviewerEmail
                        }
                    />

                    <InfoItem
                        label="Date"
                        value={
                            interview.interviewDate
                        }
                    />

                    <InfoItem
                        label="Time"
                        value={
                            `${interview.interviewStartTime}
                         - 
                         ${interview.interviewEndTime}`
                        }
                    />

                </div>

                {
                    isScheduled && (

                        <div
                            className="
                flex
                flex-wrap
                gap-3
            "
                        >

                            <a
                                href={interview.meetingLink}
                                target="_blank"
                                rel="noreferrer"
                                className="
                    inline-block
                    bg-blue-600
                    text-white
                    px-5
                    py-3
                    rounded-xl
                    font-medium
                    hover:bg-blue-700
                    transition
                "
                            >
                                Join Interview
                            </a>

                            {
                                role === "INTERVIEWER" && !interview.assessmentSubmitted && (
                                    <button
                                        onClick={() =>
                                            setShowCompleteModal(true)
                                        }
                                        className="
                            px-5
                            py-3
                            rounded-xl
                            bg-green-600
                            text-white
                            font-medium
                            hover:bg-green-700
                            transition
                        "
                                    >
                                        Mark Completed
                                    </button>
                                )
                            }

                            <button
                                onClick={() => handleCancelInterview(interview.interviewId)}
                                className="
                    px-5
                    py-3
                    rounded-xl
                    bg-red-600
                    text-white
                    font-medium
                    hover:bg-red-700
                    transition
                "
                            >
                                Cancel Interview
                            </button>

                        </div>

                    )
                }
                {
                    interview.interviewStatus === "COMPLETED" && (

                        <div className="mt-6">

                            <div className="bg-green-50 border border-green-200 rounded-2xl p-4 mb-5">

                                <h3 className="text-lg font-semibold text-green-700">
                                    Interview Completed
                                </h3>

                                <p className="text-green-600 text-sm mt-1">
                                    Review the submitted feedback and assessment below.
                                </p>

                            </div>

                            <div className="grid grid-cols-1 lg:grid-cols-2 gap-5">

                                {/* Candidate Feedback */}

                                <div className="bg-slate-50 rounded-2xl border border-slate-200 p-5">

                                    <h4 className="font-semibold text-slate-800 mb-4">
                                        Feedback For Interviewer
                                    </h4>

                                    {

                                        role === "CANDIDATE" && !interview.feedbackSubmitted ?

                                            (

                                                <button
                                                    onClick={() => setShowFeedbackModal(true)}
                                                    className="w-full py-3 rounded-xl bg-blue-600 hover:bg-blue-700 text-white font-medium"
                                                >
                                                    Provide Feedback
                                                </button>

                                            )

                                            :

                                            (

                                                <>
                                                    <div className="mb-3">

                                                        <span className="text-yellow-500 text-xl">

                                                            {"★".repeat(interview.feedback.interviewerRating)}

                                                            {"☆".repeat(
                                                                10 - interview.feedback.interviewerRating
                                                            )}

                                                        </span>

                                                    </div>

                                                    <p className="text-slate-700 whitespace-pre-wrap">

                                                        {interview.feedback.interviewerFeedback}

                                                    </p>

                                                    <div className="mt-5 text-green-600 font-medium">

                                                        ✓ Submitted

                                                    </div>

                                                </>

                                            )

                                    }

                                </div>

                                {/* Interviewer Assessment */}

                                <div className="bg-slate-50 rounded-2xl border border-slate-200 p-5">

                                    <h4 className="font-semibold text-slate-800 mb-4">
                                        Candidate Assessment
                                    </h4>

                                    {

                                        role === "INTERVIEWER" && !interview.feedbackSubmitted ?

                                            (

                                                <button
                                                    onClick={() => setShowAssessmentModal(true)}
                                                    className="w-full py-3 rounded-xl bg-purple-600 hover:bg-purple-700 text-white font-medium"
                                                >
                                                    Provide Assessment
                                                </button>

                                            )

                                            :

                                            (

                                                <>
                                                    <div className="mb-3">

                                                        <span className="text-yellow-500 text-xl">

                                                            {"★".repeat(interview.feedback.candidateRating)}

                                                            {"☆".repeat(
                                                                10 - interview.feedback.candidateRating
                                                            )}

                                                        </span>

                                                    </div>

                                                    <p className="text-slate-700 whitespace-pre-wrap">

                                                        {interview.feedback.candidateAssessment}

                                                    </p>

                                                    <div className="mt-5 text-green-600 font-medium">

                                                        ✓ Submitted

                                                    </div>

                                                </>

                                            )

                                    }

                                </div>

                            </div>

                        </div>

                    )
                }
                {
                    interview.interviewStatus ===
                    "CANCELLED" && (

                        <div
                            className="
                            px-4
                            py-3
                            rounded-xl
                            bg-red-50
                            text-red-700
                            font-medium
                        "
                        >
                            Interview Cancelled
                        </div>

                    )
                }

            </div>
            {
                showCompleteModal && (

                    <div
                        className="
                fixed
                inset-0
                bg-black/50
                flex
                items-center
                justify-center
                z-50
            "
                    >

                        <div
                            className="
                    bg-white
                    rounded-3xl
                    p-8
                    w-full
                    max-w-md
                "
                        >

                            <h2
                                className="
                        text-2xl
                        font-bold
                        mb-6
                    "
                            >
                                Complete Interview
                            </h2>

                            <div
                                className="
                        space-y-4
                    "
                            >

                                <div>

                                    <p
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Candidate
                                    </p>

                                    <p
                                        className="
                                font-medium
                            "
                                    >
                                        {interview.candidateEmail}
                                    </p>

                                </div>

                                <div>

                                    <p
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Interviewer
                                    </p>

                                    <p
                                        className="
                                font-medium
                            "
                                    >
                                        {interview.interviewerEmail}
                                    </p>

                                </div>

                                <div>

                                    <p
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Date
                                    </p>

                                    <p
                                        className="
                                font-medium
                            "
                                    >
                                        {interview.interviewDate}
                                    </p>

                                </div>

                                <div>

                                    <p
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Time
                                    </p>

                                    <p
                                        className="
                                font-medium
                            "
                                    >
                                        {interview.interviewStartTime}
                                        {" - "}
                                        {interview.interviewEndTime}
                                    </p>

                                </div>

                                <div
                                    className="
                            bg-green-50
                            border
                            border-green-200
                            rounded-xl
                            p-4
                        "
                                >

                                    <p
                                        className="
                                text-green-800
                            "
                                    >
                                        Once marked completed, this interview will move to the feedback stage.
                                    </p>

                                </div>

                            </div>

                            <div
                                className="
                        flex
                        justify-end
                        gap-3
                        mt-8
                    "
                            >

                                <button
                                    onClick={() =>
                                        setShowCompleteModal(
                                            false
                                        )
                                    }
                                    className="
                            px-4
                            py-2
                            border
                            rounded-xl
                        "
                                >
                                    Cancel
                                </button>

                                <button
                                    onClick={
                                        handleComplete
                                    }
                                    className="
                            px-4
                            py-2
                            bg-green-600
                            text-white
                            rounded-xl
                        "
                                >
                                    Mark Completed
                                </button>

                            </div>

                        </div>

                    </div>

                )
            }

            {
                showFeedbackModal && (

                    <div
                        className="
                fixed
                inset-0
                bg-black/50
                flex
                items-center
                justify-center
                z-50
            "
                    >

                        <div
                            className="
                    bg-white
                    rounded-3xl
                    p-8
                    w-full
                    max-w-lg
                "
                        >

                            <h2
                                className="
                        text-2xl
                        font-bold
                        mb-6
                    "
                            >
                                Feedback For Interviewer
                            </h2>

                            <div
                                className="
                        space-y-4
                    "
                            >

                                <div>

                                    <label>
                                        Rating (1-10)
                                    </label>

                                    <input
                                        type="number"
                                        min="1"
                                        max="10"
                                        value={rating}
                                        onChange={
                                            event =>
                                                setRating(
                                                    event.target.value
                                                )
                                        }
                                        className="
                                w-full
                                border
                                rounded-xl
                                px-4
                                py-3
                            "
                                    />

                                </div>

                                <div>

                                    <label>
                                        Review
                                    </label>

                                    <textarea
                                        rows="4"
                                        value={review}
                                        onChange={
                                            event =>
                                                setReview(
                                                    event.target.value
                                                )
                                        }
                                        className="
                                w-full
                                border
                                rounded-xl
                                px-4
                                py-3
                            "
                                    />

                                </div>

                            </div>

                            <div
                                className="
                        flex
                        justify-end
                        gap-3
                        mt-8
                    "
                            >

                                <button
                                    onClick={() =>
                                        setShowFeedbackModal(
                                            false
                                        )
                                    }
                                >
                                    Cancel
                                </button>

                                <button
                                    onClick={submitFeedback}
                                    className="
                            bg-blue-600
                            text-white
                            px-4
                            py-2
                            rounded-xl
                        "
                                >
                                    Submit Feedback
                                </button>

                            </div>

                        </div>

                    </div>

                )
            }
            {
                showAssessmentModal && (

                    <div
                        className="
                fixed
                inset-0
                bg-black/50
                flex
                items-center
                justify-center
                z-50
            "
                    >

                        <div
                            className="
                    bg-white
                    rounded-3xl
                    p-8
                    w-full
                    max-w-lg
                "
                        >

                            <h2
                                className="
                        text-2xl
                        font-bold
                        mb-6
                    "
                            >
                                Candidate Assessment
                            </h2>

                            <div
                                className="
                        space-y-4
                    "
                            >

                                <div>

                                    <label>
                                        Rating (1-10)
                                    </label>

                                    <input
                                        type="number"
                                        min="1"
                                        max="10"
                                        value={rating}
                                        onChange={
                                            event =>
                                                setRating(
                                                    event.target.value
                                                )
                                        }
                                        className="
                                w-full
                                border
                                rounded-xl
                                px-4
                                py-3
                            "
                                    />

                                </div>

                                <div>

                                    <label>
                                        Comments
                                    </label>

                                    <textarea
                                        rows="4"
                                        value={comments}
                                        onChange={
                                            event =>
                                                setComments(
                                                    event.target.value
                                                )
                                        }
                                        className="
                                w-full
                                border
                                rounded-xl
                                px-4
                                py-3
                            "
                                    />

                                </div>

                            </div>

                            <div
                                className="
                        flex
                        justify-end
                        gap-3
                        mt-8
                    "
                            >

                                <button
                                    onClick={() =>
                                        setShowAssessmentModal(
                                            false
                                        )
                                    }
                                >
                                    Cancel
                                </button>

                                <button
                                    onClick={submitAssessment}
                                    className="
                            bg-purple-600
                            text-white
                            px-4
                            py-2
                            rounded-xl
                        "
                                >
                                    Submit Assessment
                                </button>

                            </div>

                        </div>

                    </div>

                )
            }
        </>

    );

}

function InfoItem({
    label,
    value
}) {

    return (

        <div
            className="
                bg-slate-50
                border
                border-slate-200
                rounded-2xl
                p-4
            "
        >

            <p
                className="
                    text-xs
                    uppercase
                    tracking-wide
                    text-slate-500
                    mb-2
                "
            >
                {label}
            </p>

            <p
                className="
                    font-semibold
                    text-slate-800
                    break-all
                "
            >
                {value}
            </p>

        </div>

    );

}

export default InterviewCard;