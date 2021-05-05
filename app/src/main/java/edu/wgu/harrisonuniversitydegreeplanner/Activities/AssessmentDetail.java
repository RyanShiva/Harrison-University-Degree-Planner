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
import android.widget.TextView;

import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Database.AssessmentDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.CourseDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Assessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Course;
import edu.wgu.harrisonuniversitydegreeplanner.Model.ObjectiveAssessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.PerformanceAssessment;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class AssessmentDetail extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "AssessmentDetail";

    static long assessmentId;
    static String assessmentTitle;
    static boolean isObjectiveAssessment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
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

        if(bundle != null) {
            assessmentId = Integer.parseInt((String) bundle.get("assessmentId"));
            isObjectiveAssessment = (boolean) bundle.get("isObjectiveAssessment");
        }

        if (assessmentId > 0) {
            String courseTitle = "", assessmentStartDate = "", assessmentEndDate = "", assessmentOutcome = "";
            ArrayList<Assessment> allAssessments = AssessmentDAOImpl.getAllAssessments(db);
            ArrayList<Course> allCourses = CourseDAOImpl.getAllCourses(db);
            for(Course course : allCourses) {
                if (course.getCourseId() == CourseDetail.courseId)
                    courseTitle = course.getCourseTitle();
            }

            for (Assessment assessment : allAssessments) {
                if (assessmentId == assessment.getAssessmentId() && assessment instanceof ObjectiveAssessment == isObjectiveAssessment) {
                    assessmentTitle = assessment.getAssessmentTitle();
                    assessmentStartDate = assessment.getAssessmentStartDate();
                    assessmentEndDate = assessment.getAssessmentEndDate();

                    TextView outcomeLabelTxt = findViewById(R.id.outcomeLabelTxt);
                    TextView outcomeTxt = findViewById(R.id.outcomeTxt);

                    TextView assessmentTypeTxt = findViewById(R.id.assessmentTypeTxt);

                    if(assessment instanceof ObjectiveAssessment) {
                        assessmentTypeTxt.setText("Objective Assessment");

                        if(((ObjectiveAssessment) assessment).getAssessmentScore() == -1) {
                            outcomeLabelTxt.setText("");
                            assessmentOutcome = "No assessment score.";
                        }
                        else {
                            outcomeLabelTxt.setText("Score: ");
                            assessmentOutcome = String.valueOf(((ObjectiveAssessment) assessment).getAssessmentScore());
                        }
                        //outcomeLabelTxt.setText("Score: ");
                    }
                    else {
                        assessmentTypeTxt.setText("Performance Assessment");
                        outcomeLabelTxt.setText("Outcome: ");
                        assessmentOutcome = ((PerformanceAssessment) assessment).getAssessmentOutcome().toString();
                    }

                    TextView assessmentTitleTxt = findViewById(R.id.assessmentTitleTxt);
                    TextView courseTitleTxt = findViewById(R.id.courseTitleTxt);
                    TextView startDateTxt = findViewById(R.id.startDateTxt);
                    TextView endDateTxt = findViewById(R.id.endDateTxt);

                    assessmentTitleTxt.setText(assessmentTitle);
                    courseTitleTxt.setText(courseTitle);
                    startDateTxt.setText(assessmentStartDate);
                    endDateTxt.setText(assessmentEndDate);
                    outcomeTxt.setText(assessmentOutcome);

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
        getMenuInflater().inflate(R.menu.assessment_detail_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_update_assessment:
                if(isObjectiveAssessment) {
                    Intent intent2 = new Intent(this, UpdateObjectiveAssessment.class);
                    startActivity(intent2);
                    return true;
                }
                else {
                    Intent intent2 = new Intent(this, UpdatePerformanceAssessment.class);
                    startActivity(intent2);
                    return true;
                }

            case R.id.action_delete_assessment:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Assessment: " + assessmentTitle);
                builder.setMessage("Are you sure you want to delete this assessment?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            SQLiteDatabase db = myHelper.getWritableDatabase();

                            public void onClick(DialogInterface dialog, int id) {
                                AssessmentDAOImpl.deleteAssessment(assessmentId, isObjectiveAssessment, db);
                                Log.d(TAG, "Assessment has been deleted: " + assessmentTitle);
                                Intent intent = new Intent(getApplicationContext(), AssessmentList.class);
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
                Intent intent3 = new Intent(this, SetAssessmentAlert.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}