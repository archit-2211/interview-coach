import api from "./api"

export async function submitFeedbackAssessment(
    interviewId,
    rating,
    description
) {

    console.log("in service layer" +
        interviewId,
        rating,
        description
    );
    const requestBody = {description, rating}
    const response = await api.post("/interviews/"+interviewId+"/feedback", requestBody)
    // return response.data

    // return {

    //     feedbackId:
    //         "feedback-123",

    //     interviewId,

    //     rating,

    //     review,

    //     submittedAt:
    //         "2026-07-09T20:00:00"

    // };

}

