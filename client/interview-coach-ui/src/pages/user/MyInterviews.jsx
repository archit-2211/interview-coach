import { useEffect, useMemo, useState } from "react";

import InterviewCard from "../../components/interview/InterviewCard"

import { getMyInterviews, completeInterview, cancelInterview } from "../../services/InterviewManagementService"
import {  submitFeedbackAssessment } from "../../services/FeedbackService";

function MyInterviews() {

    const [interviews, setInterviews] =
        useState([]);

    const [selectedFilter, setSelectedFilter] =
        useState("ALL");

    const [loading, setLoading] =
        useState(true);

    const [error, setError] =
        useState("");
    
        const handleCancelInterview = async (interviewId) => {
            try{

                await cancelInterview(interviewId) ; 
                await loadInterviews()

            }
            catch(error) {
                console.error(error)
            }
        }

    const handleSubmitFeedback =
    async (
        interviewId,
        rating,
        review
    ) => {

        try {

            await submitFeedbackAssessment(
                interviewId,
                rating,
                review
            );

            await loadInterviews();

        } catch (error) {

            console.error(
                error
            );

        }

    };

    const handleSubmitAssessment =
    async (
        interviewId,
        rating,
        comments
    ) => {

        try {

            await submitFeedbackAssessment(
                interviewId,
                rating,
                comments
            );

            await loadInterviews();

        } catch (error) {

            console.error(
                error
            );

        }

    };

    const loadInterviews =
        async () => {

            try {

                setLoading(true);

                const response =
                    await getMyInterviews();

                setInterviews(
                    response.interviews
                );

            } catch (error) {

                console.error(error);

                setError(
                    "Failed to load interviews"
                );

            } finally {

                setLoading(false);

            }

        };

    useEffect(() => {

        loadInterviews();

    }, []);

    const handleCompleteInterview = async (interviewId) => {
        try {
        await completeInterview(interviewId) 
        await loadInterviews()
        }
        catch(error) {
            console.error(error)
        }


    }




    const stats = useMemo(() => {

        return {

            total:
                interviews.length,

            scheduled:
                interviews.filter(
                    interview =>
                        interview.interviewStatus ===
                        "SCHEDULED"
                ).length,

            completed:
                interviews.filter(
                    interview =>
                        interview.interviewStatus ===
                        "COMPLETED"
                ).length,

            cancelled:
                interviews.filter(
                    interview =>
                        interview.interviewStatus ===
                        "CANCELLED"
                ).length

        };

    }, [interviews]);

    const filteredInterviews =
        useMemo(() => {

            if (
                selectedFilter ===
                "ALL"
            ) {

                return interviews;

            }

            return interviews.filter(
                interview =>
                    interview.interviewStatus ===
                    selectedFilter
            );

        }, [
            interviews,
            selectedFilter
        ]);

    if (loading) {

        return (

            <div
                className="
                    min-h-screen
                    flex
                    items-center
                    justify-center
                    text-slate-500
                "
            >
                Loading Interviews...
            </div>

        );

    }

    if (error) {

        return (

            <div
                className="
                    min-h-screen
                    flex
                    items-center
                    justify-center
                    text-red-500
                "
            >
                {error}
            </div>

        );

    }

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
                        My Interviews
                    </h1>

                    <p
                        className="
                            text-slate-500
                            mt-2
                        "
                    >
                        View and manage all your interviews.
                    </p>

                </div>

                <div
                    className="
                        grid
                        grid-cols-1
                        md:grid-cols-4
                        gap-4
                        mb-8
                    "
                >

                    <StatCard
                        title="Total"
                        value={stats.total}
                    />

                    <StatCard
                        title="Scheduled"
                        value={stats.scheduled}
                    />

                    <StatCard
                        title="Completed"
                        value={stats.completed}
                    />

                    <StatCard
                        title="Cancelled"
                        value={stats.cancelled}
                    />

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
                            "SCHEDULED",
                            "COMPLETED",
                            "CANCELLED"
                        ].map(
                            (filter) => (

                                <button
                                    key={filter}
                                    onClick={() =>
                                        setSelectedFilter(
                                            filter
                                        )
                                    }
                                    className={`
                                        px-5
                                        py-2.5
                                        rounded-xl
                                        font-medium
                                        transition
                                        ${
                                            selectedFilter === filter
                                                ? "bg-blue-600 text-white"
                                                : "bg-white border border-slate-200 text-slate-700 hover:bg-slate-50"
                                        }
                                    `}
                                >
                                    {filter}
                                </button>

                            )
                        )
                    }

                </div>

                {
                    filteredInterviews.length === 0
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
                                    📅
                                </div>

                                <p
                                    className="
                                        text-slate-600
                                        font-medium
                                    "
                                >
                                    No interviews found
                                </p>

                            </div>

                        )
                        : (

                            <div
                                className="
                                    grid
                                    gap-5
                                "
                            >

                                {
                                    filteredInterviews.map(
                                        interview => (

                                            <InterviewCard
                                                key={
                                                    interview.interviewId
                                                }
                                                interview={
                                                    interview
                                                }
                                                handleCompleteInterview={handleCompleteInterview}
                                                handleSubmitAssessment={handleSubmitAssessment}
                                                handleSubmitFeedback={handleSubmitFeedback}
                                                handleCancelInterview={handleCancelInterview}
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

function StatCard({
    title,
    value
}) {

    return (

        <div
            className="
                bg-white
                rounded-2xl
                border
                border-slate-200
                shadow-sm
                p-5
            "
        >

            <p
                className="
                    text-sm
                    text-slate-500
                "
            >
                {title}
            </p>

            <h2
                className="
                    text-3xl
                    font-bold
                    text-slate-800
                    mt-2
                "
            >
                {value}
            </h2>

        </div>

    );

}

export default MyInterviews;