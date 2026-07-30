import { useEffect, useState } from "react";

import InterviewRequestCard from "../../components/interview/InterviewRequestCard";

import {getInterviewRequests, createInterview, rejectInterviewRequest, cancelInterviewRequest} from "../../services/InterviewManagementService"

function InterviewRequests() {

    const [interviewRequests,setInterviewRequests] = useState([]);

    const [filter,setFilter] = useState("ALL");

    const loadInterviewRequests =
        async () => {

            try {

                const response =
                    await getInterviewRequests();

                setInterviewRequests(
                    response.interviewRequests
                );

            } catch (error) {

                console.error(error);

            }

        };

    useEffect(() => {

        loadInterviewRequests();

    }, []);

    const handleCreateInterview = async (requestId, meetingLink) => {
        
        const response = await createInterview(requestId, meetingLink)


    }

    const handleRejectInterviewRequest = async (requestId, rejectReason) => {
    
        // make the api call 
        await rejectInterviewRequest(requestId, rejectReason)
        await loadInterviewRequests()
    }

    const handleCancelInterviewRequest = async (requestId) => {
        await cancelInterviewRequest(requestId)
        await loadInterviewRequests()

        
        }
    
    const filteredRequests =
        filter === "ALL"
            ? interviewRequests
            : interviewRequests.filter(
                request =>
                    request.status === filter
            );

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
                    max-w-6xl
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
                        Interview Requests
                    </h1>

                    <p
                        className="
                            text-slate-500
                            mt-2
                        "
                    >
                        Manage your interview requests.
                    </p>

                </div>

                <div
                    className="
                        flex
                        flex-wrap
                        gap-3
                        mb-8
                    "
                >

                    {
                        [
                            "ALL",
                            "PENDING",
                            "ACCEPTED",
                            "REJECTED",
                            "CANCELLED"
                        ].map(
                            status => (

                                <button
                                    key={status}
                                    onClick={() =>
                                        setFilter(
                                            status
                                        )
                                    }
                                    className={`
                                        px-4
                                        py-2
                                        rounded-full
                                        border
                                        font-medium
                                        transition
                                        ${
                                            filter === status
                                                ? "bg-blue-600 text-white border-blue-600"
                                                : "bg-white text-slate-700 border-slate-300"
                                        }
                                    `}
                                >
                                    {status}
                                </button>

                            )
                        )
                    }

                </div>

                {
                    filteredRequests.length === 0
                        ? (

                            <div
                                className="
                                    bg-white
                                    rounded-3xl
                                    border
                                    border-slate-200
                                    p-12
                                    text-center
                                    text-slate-500
                                "
                            >
                                No Interview Requests Found
                            </div>

                        )
                        : (

                            <div
                                className="
                                    space-y-4
                                "
                            >

                                {
                                    filteredRequests.map(
                                        request => (

                                            <InterviewRequestCard
                                                key={
                                                    request.requestId
                                                }
                                                request={
                                                    request
                                                }
                                                handleCreateInterview = {
                                                    handleCreateInterview
                                                }

                                                handleRejectInterviewRequest = {
                                                    handleRejectInterviewRequest
                                                }
                                                handleCancelInterviewRequest = {
                                                    handleCancelInterviewRequest
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

export default InterviewRequests;

