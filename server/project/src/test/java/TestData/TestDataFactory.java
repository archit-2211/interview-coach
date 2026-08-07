package TestData;

import com.interviewcoach.project.enums.InterviewStatus;
import com.interviewcoach.project.models.CandidatesAssessment;
import com.interviewcoach.project.models.Feedback;
import com.interviewcoach.project.models.Interview;
import com.interviewcoach.project.models.InterviewRequest;
import com.interviewcoach.project.models.InterviewersFeedback;
import com.interviewcoach.project.models.Profile;

public class TestDataFactory {

    public static Interview completedInterview() {

        Interview interview = new Interview();

        InterviewRequest request =
                new InterviewRequest();

        Profile candidate = new Profile();

        Profile interviewer = new Profile();

        Feedback feedback = new Feedback();

        feedback.setInterviewersFeedback(
                new InterviewersFeedback());

        feedback.setCandidatesAssessment(
                new CandidatesAssessment());

        request.setCandidateProfile(candidate);

        request.setInterviewerProfile(interviewer);

        interview.setInterviewRequest(request);

        interview.setFeedback(feedback);

        interview.setInterviewStatus(
                InterviewStatus.COMPLETED);

        return interview;
    }

}