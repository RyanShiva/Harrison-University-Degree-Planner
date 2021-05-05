package edu.wgu.harrisonuniversitydegreeplanner.Model;

public class PerformanceAssessment extends Assessment {
    private AssessmentOutcome assessmentOutcome;

    public PerformanceAssessment(long assessmentId, long courseId, String assessmentTitle, String assessmentStartDate, String assessmentEndDate, AssessmentOutcome assessmentOutcome) {
        super(assessmentId, courseId, assessmentTitle, assessmentStartDate, assessmentEndDate);
        this.assessmentOutcome = assessmentOutcome;
    }

    public AssessmentOutcome getAssessmentOutcome() {
        return assessmentOutcome;
    }

    public void setAssessmentOutcome(AssessmentOutcome assessmentOutcome) {
        this.assessmentOutcome = assessmentOutcome;
    }

    public enum AssessmentOutcome {
        PASS, FAIL, NOT_ATTEMPTED
    }
}
