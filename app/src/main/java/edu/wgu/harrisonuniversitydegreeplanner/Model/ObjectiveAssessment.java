package edu.wgu.harrisonuniversitydegreeplanner.Model;

public class ObjectiveAssessment extends Assessment {
    private double assessmentScore;

    public ObjectiveAssessment(long assessmentId, long courseId, String assessmentTitle, String assessmentStartDate, String assessmentEndDate, double assessmentScore) {
        super(assessmentId, courseId, assessmentTitle, assessmentStartDate, assessmentEndDate);
        this.assessmentScore = assessmentScore;
    }

    public double getAssessmentScore() {
        return assessmentScore;
    }

    public void setAssessmentScore(double score) {
        this.assessmentScore = score;
    }
}
