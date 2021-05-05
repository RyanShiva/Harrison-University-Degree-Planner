package edu.wgu.harrisonuniversitydegreeplanner.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.wgu.harrisonuniversitydegreeplanner.Model.Assessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Course;
import edu.wgu.harrisonuniversitydegreeplanner.Model.CourseNote;
import edu.wgu.harrisonuniversitydegreeplanner.Model.ObjectiveAssessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.PerformanceAssessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Term;
import edu.wgu.harrisonuniversitydegreeplanner.Model.User;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QCOM.db";
    private static final int DATABASE_VERSION = 1;

    // Constants for database tables and columns
    // Terms table
    public static final String TABLE_TERMS = "terms";
    public static final String TERMS_TABLE_ID = "_id";
    public static final String TERM_TITLE = "termTitle";
    public static final String TERM_START_DATE = "termStartDate";
    public static final String TERM_END_DATE = "termEndDate";
    public static final String TERM_CREATED = "termCreated";

    // Courses table
    public static final String TABLE_COURSES = "courses";
    public static final String COURSES_TABLE_ID = "_id";
    public static final String COURSE_TERM_ID = "courseTermId";
    public static final String COURSE_TITLE = "courseTitle";
    public static final String COURSE_START_DATE = "courseStartDate";
    public static final String COURSE_END_DATE = "courseEndDate";
    public static final String COURSE_STATUS = "courseStatus";
    public static final String COURSE_INSTRUCTOR = "courseInstructor";
    public static final String COURSE_INSTRUCTOR_PHONE = "courseInstructorPhone";
    public static final String COURSE_INSTRUCTOR_EMAIL = "courseInstructorEmail";
    public static final String COURSE_CREATED = "courseCreated";

    // Course Notes table
    public static final String TABLE_COURSE_NOTES = "courseNotes";
    public static final String COURSE_NOTES_TABLE_ID = "_id";
    public static final String COURSE_NOTE_COURSE_ID = "courseNoteCourseId";
    public static final String COURSE_NOTE_TEXT = "courseNoteText";
    public static final String COURSE_NOTE_CREATED = "courseCreated";

    // Objective Assessments table
    public static final String TABLE_OBJECTIVE_ASSESSMENTS = "objectiveAssessments";
    public static final String OBJECTIVE_ASSESSMENTS_TABLE_ID = "_id";
    public static final String OBJECTIVE_ASSESSMENT_COURSE_ID = "objectiveAssessmentCourseId";
    public static final String OBJECTIVE_ASSESSMENT_TITLE = "objectiveAssessmentTitle";
    public static final String OBJECTIVE_ASSESSMENT_START_DATE = "objectiveAssessmentStartDate";
    public static final String OBJECTIVE_ASSESSMENT_END_DATE = "objectiveAssessmentEndDate";
    public static final String OBJECTIVE_ASSESSMENT_SCORE = "objectiveAssessmentScore";
    public static final String OBJECTIVE_ASSESSMENT_CREATED = "objectiveAssessmentCreated";

    // Performance Assessments table
    public static final String TABLE_PERFORMANCE_ASSESSMENTS = "performanceAssessments";
    public static final String PERFORMANCE_ASSESSMENTS_TABLE_ID = "_id";
    public static final String PERFORMANCE_ASSESSMENT_COURSE_ID = "performanceAssessmentCourseId";
    public static final String PERFORMANCE_ASSESSMENT_TITLE = "performanceAssessmentTitle";
    public static final String PERFORMANCE_ASSESSMENT_START_DATE = "performanceAssessmentStartDate";
    public static final String PERFORMANCE_ASSESSMENT_END_DATE = "performanceAssessmentEndDate";
    public static final String PERFORMANCE_ASSESSMENT_OUTCOME = "performanceAssessmentOutcome";
    public static final String PERFORMANCE_ASSESSMENT_CREATED = "performanceAssessmentCreated";

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String USERS_TABLE_ID = "_id";
    public static final String USERS_USERNAME = "username";
    public static final String USERS_PASSWORD = "password";
    public static final String USER_CREATED = "userCreated";

    // Create strings for SQLite commands
    // Terms table
    private static final String TERMS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_TERMS + " (" +
                    TERMS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERM_TITLE + " TEXT, " +
                    TERM_START_DATE + " DATE, " +
                    TERM_END_DATE + " DATE, " +
                    TERM_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";

    // Courses table
    private static final String COURSES_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_COURSES + " (" +
                    COURSES_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_TERM_ID + " INTEGER, " +
                    COURSE_TITLE + " TEXT, " +
                    COURSE_START_DATE + " DATE, " +
                    COURSE_END_DATE + " DATE, " +
                    COURSE_STATUS + " TEXT, " +
                    COURSE_INSTRUCTOR + " TEXT, " +
                    COURSE_INSTRUCTOR_PHONE + " TEXT, " +
                    COURSE_INSTRUCTOR_EMAIL + " TEXT, " +
                    COURSE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COURSE_TERM_ID + ") REFERENCES " + TABLE_TERMS + "(" + TERMS_TABLE_ID + ")" +
                    ")";

    // Course Notes table
    private static final String COURSE_NOTES_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_COURSE_NOTES + " (" +
                    COURSE_NOTES_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSE_NOTE_COURSE_ID + " INTEGER, " +
                    COURSE_NOTE_TEXT + " TEXT, " +
                    COURSE_NOTE_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + COURSE_NOTE_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COURSES_TABLE_ID + ")" +
                    ")";

    // Objective Assessments table
    private static final String OBJECTIVE_ASSESSMENTS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_OBJECTIVE_ASSESSMENTS + " (" +
                    OBJECTIVE_ASSESSMENTS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    OBJECTIVE_ASSESSMENT_COURSE_ID + " INTEGER, " +
                    OBJECTIVE_ASSESSMENT_TITLE + " TEXT, " +
                    OBJECTIVE_ASSESSMENT_START_DATE + " DATE, " +
                    OBJECTIVE_ASSESSMENT_END_DATE + " DATE, " +
                    OBJECTIVE_ASSESSMENT_SCORE + " DOUBLE, " +
                    OBJECTIVE_ASSESSMENT_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + OBJECTIVE_ASSESSMENT_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COURSES_TABLE_ID + ")" +
                    ")";

    // Performance Assessments table
    private static final String PERFORMANCE_ASSESSMENTS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_PERFORMANCE_ASSESSMENTS + " (" +
                    PERFORMANCE_ASSESSMENTS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PERFORMANCE_ASSESSMENT_COURSE_ID + " INTEGER, " +
                    PERFORMANCE_ASSESSMENT_TITLE + " TEXT, " +
                    PERFORMANCE_ASSESSMENT_START_DATE + " DATE, " +
                    PERFORMANCE_ASSESSMENT_END_DATE + " DATE, " +
                    PERFORMANCE_ASSESSMENT_OUTCOME + " TEXT, " +
                    PERFORMANCE_ASSESSMENT_CREATED + " TEXT default CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY(" + PERFORMANCE_ASSESSMENT_COURSE_ID + ") REFERENCES " + TABLE_COURSES + "(" + COURSES_TABLE_ID + ")" +
                    ")";

    // Users table
    private static final String USERS_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
                    USERS_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USERS_USERNAME + " TEXT, " +
                    USERS_PASSWORD + " TEXT, " +
                    USER_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void deleteTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJECTIVE_ASSESSMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERFORMANCE_ASSESSMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
    }

    public static void createTables(SQLiteDatabase db) {
        // Create Tables
        db.execSQL(TERMS_TABLE_CREATE);
        db.execSQL(COURSES_TABLE_CREATE);
        db.execSQL(COURSE_NOTES_TABLE_CREATE);
        db.execSQL(OBJECTIVE_ASSESSMENTS_TABLE_CREATE);
        db.execSQL(PERFORMANCE_ASSESSMENTS_TABLE_CREATE);
        db.execSQL(USERS_TABLE_CREATE);
    }

    public static void populateSampleData(SQLiteDatabase db) {
        // Populate Sample Data
        // Populate Terms
        Term term1 = new Term(-1, "Term 1", "2019-06-01", "2019-11-30");
        Term term2 = new Term(-1, "Term 2", "2019-12-01", "2020-05-31");
        Term term3 = new Term(-1, "Term 3", "2020-06-01", "2020-11-30");
        Term term4 = new Term(-1, "Term 4", "2020-12-01", "2021-05-31");

        TermDAOImpl.insertTerm(term1, db);
        TermDAOImpl.insertTerm(term2, db);
        TermDAOImpl.insertTerm(term3, db);
        TermDAOImpl.insertTerm(term4, db);

        // Populate Courses
        Course course1 = new Course(-1, 1, "Course 1", "2019-06-01", "2019-07-15", Course.Status.COMPLETED, "Alvaro Escobar", "888-888-8887", "example@gmail.com");
        Course course2 = new Course(-1, 1, "Course 2", "2019-07-16", "2019-08-31", Course.Status.COMPLETED, "Malcolm Wabara", "888-888-8886", "example2@gmail.com");
        Course course3 = new Course(-1, 1, "Course 3", "2019-09-01", "2019-10-15", Course.Status.COMPLETED, "Alvaro Escobar", "888-888-8887", "example@gmail.com");
        Course course4 = new Course(-1, 1, "Course 4", "2019-10-16", "2019-11-30", Course.Status.COMPLETED, "Malcolm Wabara", "888-888-8886", "example2@gmail.com");

        Course course5 = new Course(-1, 2, "Course 5", "2019-12-01", "2020-01-15", Course.Status.COMPLETED, "John Jones", "888-888-8885", "example3@hotmail.com");
        Course course6 = new Course(-1, 2, "Course 6", "2020-01-16", "2020-02-28", Course.Status.COMPLETED, "Malcolm Wabara", "888-888-8886", "example2@gmail.com");
        Course course7 = new Course(-1, 2, "Course 7", "2020-03-01", "2020-04-15", Course.Status.DROPPED, "Alvaro Escobar", "888-888-8887", "example@gmail.com");
        Course course8 = new Course(-1, 2, "Course 8", "2020-04-16", "2020-05-31", Course.Status.COMPLETED, "Jay Smith", "888-888-8884", "example4@hotmail.com");

        Course course9 = new Course(-1, 3, "Course 9", "2020-06-01", "2020-07-15", Course.Status.COMPLETED, "Alvaro Escobar", "888-888-8887", "example@gmail.com");
        Course course10 = new Course(-1, 3, "Course 10", "2020-07-16", "2020-08-31", Course.Status.COMPLETED, "Malcolm Wabara", "888-888-8886", "example2@gmail.com");
        Course course11 = new Course(-1, 3, "Course 11", "2020-09-01", "2020-10-15", Course.Status.IN_PROGRESS, "Alvaro Escobar", "888-888-8887", "example@gmail.com");
        Course course12 = new Course(-1, 3, "Course 12", "2020-10-16", "2020-11-30", Course.Status.PLAN_TO_TAKE, "Malcolm Wabara", "888-888-8886", "example2@gmail.com");

        Course course13 = new Course(-1, 4, "Course 13", "2020-12-01", "2021-01-15", Course.Status.PLAN_TO_TAKE, "John Jones", "888-888-8885", "example3@hotmail.com");
        Course course14 = new Course(-1, 4, "Course 14", "2021-01-16", "2021-02-28", Course.Status.PLAN_TO_TAKE, "Malcolm Wabara", "888-888-8886", "example2@gmail.com");
        Course course15 = new Course(-1, 4, "Course 15", "2021-03-01", "2021-04-15", Course.Status.PLAN_TO_TAKE, "Alvaro Escobar", "888-888-8887", "example@gmail.com");
        Course course16 = new Course(-1, 4, "Course 16", "2021-04-16", "2021-05-31", Course.Status.PLAN_TO_TAKE, "Jay Smith", "888-888-8884", "example4@hotmail.com");

        CourseDAOImpl.insertCourse(course1, db);
        CourseDAOImpl.insertCourse(course2, db);
        CourseDAOImpl.insertCourse(course3, db);
        CourseDAOImpl.insertCourse(course4, db);
        CourseDAOImpl.insertCourse(course5, db);
        CourseDAOImpl.insertCourse(course6, db);
        CourseDAOImpl.insertCourse(course7, db);
        CourseDAOImpl.insertCourse(course8, db);
        CourseDAOImpl.insertCourse(course9, db);
        CourseDAOImpl.insertCourse(course10, db);
        CourseDAOImpl.insertCourse(course11, db);
        CourseDAOImpl.insertCourse(course12, db);
        CourseDAOImpl.insertCourse(course13, db);
        CourseDAOImpl.insertCourse(course14, db);
        CourseDAOImpl.insertCourse(course15, db);
        CourseDAOImpl.insertCourse(course16, db);

        // Populate Assessments
        Assessment assessment1 = new ObjectiveAssessment(-1, 1, "Assessment 1", "2019-07-15", "2019-07-15", 78.6);
        Assessment assessment2 = new PerformanceAssessment(-1, 1, "Assessment 2", "2019-07-15", "2019-07-15", PerformanceAssessment.AssessmentOutcome.PASS);
        Assessment assessment3 = new ObjectiveAssessment(-1, 2, "Assessment 3", "2019-08-31", "2019-08-31", 92.3);
        Assessment assessment4 = new PerformanceAssessment(-1, 3, "Assessment 4", "2019-10-15", "2019-10-15", PerformanceAssessment.AssessmentOutcome.PASS);
        Assessment assessment5 = new ObjectiveAssessment(-1, 4, "Assessment 5", "2019-11-30", "2019-11-30", 98.4);

        Assessment assessment6 = new ObjectiveAssessment(-1, 5, "Assessment 6", "2020-01-15", "2020-01-15", 89.8);
        Assessment assessment7 = new PerformanceAssessment(-1, 6, "Assessment 7", "2020-02-28", "2020-02-28", PerformanceAssessment.AssessmentOutcome.FAIL);
        Assessment assessment8 = new ObjectiveAssessment(-1, 6, "Assessment 8", "2020-02-28", "2020-02-28", 97.2);
        Assessment assessment9 = new PerformanceAssessment(-1, 7, "Assessment 9", "2020-04-15", "2020-04-15", PerformanceAssessment.AssessmentOutcome.PASS);
        Assessment assessment10 = new ObjectiveAssessment(-1, 8, "Assessment 10", "2020-05-31", "2020-05-31", 67.3);

        Assessment assessment11 = new ObjectiveAssessment(-1, 9, "Assessment 11", "2020-07-15", "2020-07-15", 95.4);
        Assessment assessment12 = new PerformanceAssessment(-1, 10, "Assessment 12", "2020-08-31", "2020-08-31", PerformanceAssessment.AssessmentOutcome.PASS);
        Assessment assessment13 = new ObjectiveAssessment(-1, 11, "Assessment 13", "2020-10-15", "2020-10-15",-1);
        Assessment assessment14 = new PerformanceAssessment(-1, 11, "Assessment 14", "2020-10-15", "2020-10-15", PerformanceAssessment.AssessmentOutcome.NOT_ATTEMPTED);
        Assessment assessment15 = new ObjectiveAssessment(-1, 12, "Assessment 15", "2020-11-30", "2020-11-30", -1);

        Assessment assessment16 = new ObjectiveAssessment(-1, 13, "Assessment 16", "2021-01-15", "2021-01-15", -1);
        Assessment assessment17 = new PerformanceAssessment(-1, 14, "Assessment 17", "2021-02-28", "2021-02-28", PerformanceAssessment.AssessmentOutcome.NOT_ATTEMPTED);
        Assessment assessment18 = new ObjectiveAssessment(-1, 15, "Assessment 18", "2021-04-15", "2021-04-15", -1);
        Assessment assessment19 = new PerformanceAssessment(-1, 16, "Assessment 19", "2021-05-31", "2021-05-31", PerformanceAssessment.AssessmentOutcome.NOT_ATTEMPTED);
        Assessment assessment20 = new ObjectiveAssessment(-1, 16, "Assessment 20", "2021-05-31", "2021-05-31", -1);

        AssessmentDAOImpl.insertAssessment(assessment1, db);
        AssessmentDAOImpl.insertAssessment(assessment2, db);
        AssessmentDAOImpl.insertAssessment(assessment3, db);
        AssessmentDAOImpl.insertAssessment(assessment4, db);
        AssessmentDAOImpl.insertAssessment(assessment5, db);
        AssessmentDAOImpl.insertAssessment(assessment6, db);
        AssessmentDAOImpl.insertAssessment(assessment7, db);
        AssessmentDAOImpl.insertAssessment(assessment8, db);
        AssessmentDAOImpl.insertAssessment(assessment9, db);
        AssessmentDAOImpl.insertAssessment(assessment10, db);
        AssessmentDAOImpl.insertAssessment(assessment11, db);
        AssessmentDAOImpl.insertAssessment(assessment12, db);
        AssessmentDAOImpl.insertAssessment(assessment13, db);
        AssessmentDAOImpl.insertAssessment(assessment14, db);
        AssessmentDAOImpl.insertAssessment(assessment15, db);
        AssessmentDAOImpl.insertAssessment(assessment16, db);
        AssessmentDAOImpl.insertAssessment(assessment17, db);
        AssessmentDAOImpl.insertAssessment(assessment18, db);
        AssessmentDAOImpl.insertAssessment(assessment19, db);
        AssessmentDAOImpl.insertAssessment(assessment20, db);

        // Populate Course Notes
        CourseNote courseNote1 = new CourseNote(-1, 1, "Course Note #1. This is a sample course note.");
        CourseNote courseNote2 = new CourseNote(-1, 1, "Course Note #2. This is a sample course note.");
        CourseNote courseNote3 = new CourseNote(-1, 1, "Course Note #3. This is a sample course note.");

        CourseNote courseNote4 = new CourseNote(-1, 2, "This course has recently been updated.");
        CourseNote courseNote5 = new CourseNote(-1, 2, "A new learning resource has been added.");

        CourseNoteDAOImpl.insertCourseNote(courseNote1, db);
        CourseNoteDAOImpl.insertCourseNote(courseNote2, db);
        CourseNoteDAOImpl.insertCourseNote(courseNote3, db);
        CourseNoteDAOImpl.insertCourseNote(courseNote4, db);
        CourseNoteDAOImpl.insertCourseNote(courseNote5, db);

        // Populate Users
        User test = new User(-1, "test", "test");
        User admin = new User(-1, "admin", "admin");

        UserDAOImpl.insertUser(test, db);
        UserDAOImpl.insertUser(admin, db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
        populateSampleData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
