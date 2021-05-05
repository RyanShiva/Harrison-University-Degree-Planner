package edu.wgu.harrisonuniversitydegreeplanner.Model;

public class CourseNote {
    private long courseNoteId;
    private long courseId;
    private String courseNoteText;

    public CourseNote(long courseNoteId, long courseId, String courseNoteText) {
        this.courseNoteId = courseNoteId;
        this.courseId = courseId;
        this.courseNoteText = courseNoteText;
    }

    public long getCourseNoteId() {
        return courseNoteId;
    }

    public void setCourseNoteId(long courseNoteId) {
        this.courseNoteId = courseNoteId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getCourseNoteText() {
        return courseNoteText;
    }

    public void setCourseNoteText(String courseNoteText) {
        this.courseNoteText = courseNoteText;
    }
}
