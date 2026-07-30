import { getRole } from "../../services/AuthService";
import { useState } from "react";

import { useNavigate } from "react-router-dom";

function InterviewRequestCard({
    request, handleCreateInterview, handleRejectInterviewRequest, handleCancelInterviewRequest
}) {



    const role = getRole()
    const [showAcceptModal, setShowAcceptModal] = useState(false);
    const [showRejectModal, setShowRejectModal] = useState(false)
    const [rejectionReason, setRejectionReason] = useState("")
    const [showCancelModal, setShowCancelModal] = useState(false)
    const [meetingLink, setMeetingLink] = useState("");
    const navigate = useNavigate()


    const isPending =
        request.status ===
        "PENDING";

    const handleAccept =
        () => {

            setShowAcceptModal(
                true)

        };
    const rejectButton = () => {
        setShowRejectModal(true)
    }
    const cancelButton = () => {
        setShowCancelModal(true)

    }

    const handleCreate =
        async () => {

            try {

                const response =
                    await handleCreateInterview(
                        request.requestId,
                        meetingLink
                    );

                console.log(
                    response
                );

                setShowAcceptModal(
                    false
                );

                navigate(
                    "/interviews/me"
                );

            } catch (error) {

                console.error(
                    error
                );

            }

        };

    const handleReject = async () => {
        try {
            await handleRejectInterviewRequest(request.requestId, rejectionReason)
            setShowRejectModal(false)
            
            navigate("/interview-requests")



        }
        catch (error) {
            console.error(error)
        }
    }

    const handleCancel = async() => {
        try{
            await handleCancelInterviewRequest(request.requestId)
            console.log("Successfully cancelled the interviewRequest")
            setShowCancelModal(false)
            navigate("/interview-requests")

        }
        catch(error) {
            console.error(error)
        }
        
    }



    return (
        <>

            <div
                className="
                bg-white
                border
                border-slate-200
                rounded-3xl
                shadow-md
                p-6
            "
            >

                <div
                    className="
                    flex
                    justify-between
                    items-start
                    mb-4
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

                            {
                                role ===
                                    "CANDIDATE"
                                    ? request.interviewerEmail
                                    : request.candidateEmail
                            }

                        </h2>

                        <p
                            className="
                            text-slate-500
                            mt-1
                        "
                        >
                            {
                                role ===
                                    "CANDIDATE"
                                    ? "Interviewer"
                                    : "Candidate"
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

                        ${request.status ===
                                "PENDING"
                                ? "bg-yellow-100 text-yellow-700"
                                : ""
                            }

                        ${request.status ===
                                "ACCEPTED"
                                ? "bg-green-100 text-green-700"
                                : ""
                            }

                        ${request.status ===
                                "REJECTED"
                                ? "bg-red-100 text-red-700"
                                : ""
                            }

                        ${request.status ===
                                "CANCELLED"
                                ? "bg-slate-200 text-slate-700"
                                : ""
                            }
                    `}
                    >
                        {request.status}
                    </span>

                </div>

                <div
                    className="
                    space-y-3
                "
                >

                    <div>

                        <p
                            className="
                            text-sm
                            text-slate-500
                        "
                        >
                            Description
                        </p>

                        <p
                            className="
                            text-slate-800
                        "
                        >
                            {
                                request.description
                            }
                        </p>

                    </div>

                    <div>

                        <p
                            className="
                            text-sm
                            text-slate-500
                            mb-2
                        "
                        >
                            Topics
                        </p>

                        <div
                            className="
                            flex
                            flex-wrap
                            gap-2
                        "
                        >

                            {
                                request.topics.map(
                                    topic => (

                                        <span
                                            key={topic}
                                            className="
                                            px-3
                                            py-1
                                            rounded-full
                                            bg-blue-50
                                            border
                                            border-blue-200
                                            text-blue-700
                                            text-sm
                                        "
                                        >
                                            {topic}
                                        </span>

                                    )
                                )
                            }

                        </div>

                    </div>

                    <div
                        className="
                        grid
                        grid-cols-1
                        md:grid-cols-2
                        gap-4
                        pt-2
                    "
                    >

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
                                {
                                    request.slotDate
                                }
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
                                {
                                    request.slotStartTime
                                }
                                {" - "}
                                {
                                    request.slotEndTime
                                }
                            </p>

                        </div>

                    </div>

                </div>

                {
                    isPending && (

                        <div
                            className="
                            mt-6
                            flex
                            justify-end
                            gap-3
                        "
                        >

                            {
                                role ===
                                "INTERVIEWER" && (

                                    <>
                                        <button
                                            onClick={() => { handleAccept() }}
                                            className="
                                            px-4
                                            py-2
                                            rounded-xl
                                            bg-green-600
                                            text-white
                                        "
                                        >
                                            Accept
                                        </button>

                                        <button
                                            className="
                                            px-4
                                            py-2
                                            rounded-xl
                                            bg-red-600
                                            text-white
                                        "
                                            onClick={rejectButton}
                                        >
                                            Reject
                                        </button>
                                    </>

                                )
                            }

                            <button
                                className="
                                px-4
                                py-2
                                rounded-xl
                                border
                                border-slate-300
                            "
                                onClick={cancelButton}
                            >
                                Cancel
                            </button>

                        </div>

                    )
                }

            </div>

            {
                showAcceptModal && (

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
                                Create Interview
                            </h2>

                            <div
                                className="
                        space-y-4
                    "
                            >

                                <div>

                                    <label
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Candidate
                                    </label>

                                    <p>
                                        {
                                            request.candidateEmail
                                        }
                                    </p>

                                </div>

                                <div>

                                    <label
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Interviewer
                                    </label>

                                    <p>
                                        {
                                            request.interviewerEmail
                                        }
                                    </p>

                                </div>

                                <div>

                                    <label
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Date
                                    </label>

                                    <p>
                                        {
                                            request.slotDate
                                        }
                                    </p>

                                </div>

                                <div>

                                    <label
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Time
                                    </label>

                                    <p>

                                        {
                                            request.slotStartTime
                                        }

                                        {" - "}

                                        {
                                            request.slotEndTime
                                        }

                                    </p>

                                </div>

                                <div>

                                    <label
                                        className="
                                block
                                text-sm
                                text-slate-500
                                mb-2
                            "
                                    >
                                        Meeting Link
                                    </label>

                                    <input
                                        type="text"
                                        value={
                                            meetingLink
                                        }
                                        onChange={
                                            (
                                                event
                                            ) =>
                                                setMeetingLink(
                                                    event.target.value
                                                )
                                        }
                                        placeholder="https://meet.google.com/..."
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
                                        setShowAcceptModal(
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
                                        handleCreate
                                    }
                                    className="
                            px-4
                            py-2
                            bg-green-600
                            text-white
                            rounded-xl
                        "
                                >
                                    Create Interview
                                </button>

                            </div>

                        </div>

                    </div>

                )
            }
            {
                showRejectModal && (

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
                                Reject Interview Request
                            </h2>

                            <div
                                className="
                        space-y-4
                    "
                            >

                                <div>

                                    <label
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Candidate
                                    </label>

                                    <p
                                        className="
                                font-medium
                            "
                                    >
                                        {
                                            request.candidateEmail
                                        }
                                    </p>

                                </div>

                                <div>

                                    <label
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Date
                                    </label>

                                    <p
                                        className="
                                font-medium
                            "
                                    >
                                        {
                                            request.slotDate
                                        }
                                    </p>

                                </div>

                                <div>

                                    <label
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Time
                                    </label>

                                    <p
                                        className="
                                font-medium
                            "
                                    >
                                        {
                                            request.slotStartTime
                                        }
                                        {" - "}
                                        {
                                            request.slotEndTime
                                        }
                                    </p>

                                </div>

                                <div>

                                    <label
                                        className="
                                block
                                text-sm
                                text-slate-500
                                mb-2
                            "
                                    >
                                        Reason For Rejection
                                    </label>

                                    <textarea
                                        rows="4"
                                        value={
                                            rejectionReason
                                        }
                                        onChange={
                                            (
                                                event
                                            ) =>
                                                setRejectionReason(
                                                    event.target.value
                                                )
                                        }
                                        placeholder="Provide a reason for rejecting this request..."
                                        className="
                                w-full
                                border
                                rounded-xl
                                px-4
                                py-3
                                resize-none
                            "
                                    />

                                </div>

                                <div>

                                    <p
                                        className="
                                text-sm
                                text-slate-500
                                mb-3
                            "
                                    >
                                        Quick Reasons
                                    </p>

                                    <div
                                        className="
                                flex
                                flex-wrap
                                gap-2
                            "
                                    >

                                        {
                                            [
                                                "Skill Mismatch",
                                                "Time Unavailable",
                                                "Not Enough Experience",
                                                "Other"
                                            ].map(
                                                reason => (

                                                    <button
                                                        key={reason}
                                                        type="button"
                                                        onClick={() =>
                                                            setRejectionReason(
                                                                reason
                                                            )
                                                        }
                                                        className="
                                                px-3
                                                py-2
                                                rounded-full
                                                border
                                                border-slate-300
                                                text-sm
                                                hover:bg-slate-100
                                            "
                                                    >
                                                        {reason}
                                                    </button>

                                                )
                                            )
                                        }

                                    </div>

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
                                        setShowRejectModal(
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
                                        handleReject
                                    }
                                    className="
                            px-4
                            py-2
                            bg-red-600
                            text-white
                            rounded-xl
                            hover:bg-red-700
                        "
                                >
                                    Reject Request
                                </button>

                            </div>

                        </div>

                    </div>

                )
            }

            {
                showCancelModal && (

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
                                Cancel Interview Request
                            </h2>

                            <div
                                className="
                        space-y-4
                    "
                            >

                                <div>

                                    <label
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Candidate
                                    </label>

                                    <p
                                        className="
                                font-medium
                            "
                                    >
                                        {
                                            request.candidateEmail
                                        }
                                    </p>

                                </div>

                                <div>

                                    <label
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Date
                                    </label>

                                    <p
                                        className="
                                font-medium
                            "
                                    >
                                        {
                                            request.slotDate
                                        }
                                    </p>

                                </div>

                                <div>

                                    <label
                                        className="
                                text-sm
                                text-slate-500
                            "
                                    >
                                        Time
                                    </label>

                                    <p
                                        className="
                                font-medium
                            "
                                    >
                                        {
                                            request.slotStartTime
                                        }
                                        {" - "}
                                        {
                                            request.slotEndTime
                                        }
                                    </p>

                                </div>

                                <div
                                    className="
                            bg-yellow-50
                            border
                            border-yellow-200
                            rounded-xl
                            p-4
                        "
                                >

                                    <p
                                        className="
                                text-yellow-800
                            "
                                    >
                                        Are you sure you want to cancel this interview request?
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
                                        setShowCancelModal(
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
                                    Go Back
                                </button>

                                <button
                                    onClick={
                                        handleCancel
                                    }
                                    className="
                            px-4
                            py-2
                            bg-red-600
                            text-white
                            rounded-xl
                            hover:bg-red-700
                        "
                                >
                                    Confirm Cancel
                                </button>

                            </div>

                        </div>

                    </div>

                )
            }
        </>

    );

}

export default InterviewRequestCard;