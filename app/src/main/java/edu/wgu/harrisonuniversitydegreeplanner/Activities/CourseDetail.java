package edu.wgu.harrisonuniversitydegreeplanner.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Database.AssessmentDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.CourseDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.CourseNoteDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Database.TermDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Assessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Course;
import edu.wgu.harrisonuniversitydegreeplanner.Model.CourseNote;
import edu.wgu.harrisonuniversitydegreeplanner.Model.ObjectiveAssessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Term;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class CourseDetail extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "CourseDetail";

    static long courseId;
    static String courseTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");

        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            courseId = Integer.parseInt((String) bundle.get("courseId"));
        }

        if (courseId > 0) {
            String termTitle = "", courseStartDate = "", courseEndDate = "", courseStatus = "", courseInstructorName = "", courseInstructorPhone = "", courseInstructorEmail = "";
            ArrayList<Course> allCourses = CourseDAOImpl.getAllCourses(db);
            ArrayList<Term> allTerms = TermDAOImpl.getAllTerms(db);
            for(Term term : allTerms) {
                if (term.getTermId() == TermDetail.termId)
                    termTitle = term.getTermTitle();
            }

            for (Course course : allCourses) {
                if (courseId == course.getCourseId()) {
                    courseTitle = course.getCourseTitle();
                    courseStartDate = course.getCourseStartDate();
                    courseEndDate = course.getCourseEndDate();
                    courseStatus = course.getCourseStatus().toString();
                    courseInstructorName = course.getCourseInstructor();
                    courseInstructorPhone = course.getCourseInstructorPhone();
                    courseInstructorEmail = course.getCourseInstructorEmail();

                    TextView termTitleTxt = findViewById(R.id.courseListTitleText);
                    TextView courseTitleTxt = findViewById(R.id.courseTitleTxt);
                    TextView startDateTxt = findViewById(R.id.startDateTxt);
                    TextView endDateTxt = findViewById(R.id.endDateTxt);
                    TextView statusTxt = findViewById(R.id.statusTxt);
                    TextView instructorNameTxt = findViewById(R.id.instructorNameTxt);
                    TextView instructorPhoneTxt = findViewById(R.id.instructorPhoneTxt);
                    TextView instructorEmailTxt = findViewById(R.id.instructorEmailTxt);

                    termTitleTxt.setText(termTitle);
                    courseTitleTxt.setText(courseTitle);
                    startDateTxt.setText(courseStartDate);
                    endDateTxt.setText(courseEndDate);
                    statusTxt.setText(courseStatus);
                    instructorNameTxt.setText(courseInstructorName);
                    instructorPhoneTxt.setText(courseInstructorPhone);
                    instructorEmailTxt.setText(courseInstructorEmail);

                    break;
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Close the database
        myHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_detail_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_update_course:
                Intent intent2 = new Intent(this, UpdateCourse.class);
                startActivity(intent2);
                return true;

            case R.id.action_delete_course:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Course: " + courseTitle);
                builder.setMessage("All assessments and notes associated with this course will also be deleted.\nAre you sure you want to delete this course?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                SQLiteDatabase db = myHelper.getWritableDatabase();

                                ArrayList<Assessment> allAssessments = AssessmentDAOImpl.getAllAssessments(db);

                                for(Assessment assessment : allAssessments) {
                                    if(assessment.getCourseId() == courseId) {
                                        boolean isObjectiveAssessment;
                                        if(assessment instanceof ObjectiveAssessment)
                                            isObjectiveAssessment = true;
                                        else
                                            isObjectiveAssessment = false;
                                        AssessmentDAOImpl.deleteAssessment(assessment.getAssessmentId(), isObjectiveAssessment, db);
                                        Log.d(TAG, "Assessment has been deleted: " + assessment.getAssessmentTitle());
                                    }
                                }

                                ArrayList<CourseNote> allCourseNotes = CourseNoteDAOImpl.getAllCourseNotes(db);

                                for(CourseNote courseNote : allCourseNotes) {
                                    if(courseNote.getCourseId() == courseId) {
                                        CourseNoteDAOImpl.deleteCourseNote(courseNote.getCourseNoteId(), db);
                                        Log.d(TAG, "Course Note has been deleted: " + courseNote.getCourseNoteId());
                                    }
                                }

                                CourseDAOImpl.deleteCourse(courseId, db);
                                Log.d(TAG, "Course has been deleted: " + courseTitle);
                                Intent intent = new Intent(getApplicationContext(), CourseList.class);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });

                builder.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();

            return true;

            case R.id.action_set_alert:
                Intent intent3 = new Intent(this, SetCourseAlert.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onViewAssessmentListClicked(View view) {
        Intent intent = new Intent(this, AssessmentList.class);
        startActivity(intent);
    }

    public void onViewCourseNoteListClicked(View view) {
        Intent intent = new Intent(this, CourseNoteList.class);
        startActivity(intent);
    }
}