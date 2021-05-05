package edu.wgu.harrisonuniversitydegreeplanner.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Database.CourseDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Course;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class UpdateCourse extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "UpdateCourse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        ArrayList<Course> allCourses = CourseDAOImpl.getAllCourses(db);
        for(Course course : allCourses) {
            if (course.getCourseId() == CourseDetail.courseId) {
                EditText courseTitleTxt = (EditText) findViewById(R.id.courseTitleTxt);
                EditText startDateTxt = (EditText) findViewById(R.id.startDateTxt);
                EditText endDateTxt = (EditText) findViewById(R.id.endDateTxt);
                RadioGroup statusRadioGroup = findViewById(R.id.statusRadioGroup);
                EditText instructorNameTxt = (EditText) findViewById(R.id.instructorNameTxt);
                EditText instructorPhoneTxt = (EditText) findViewById(R.id.instructorPhoneTxt);
                EditText instructorEmailTxt = (EditText) findViewById(R.id.instructorEmailTxt);

                courseTitleTxt.setText(course.getCourseTitle());
                startDateTxt.setText(course.getCourseStartDate());
                endDateTxt.setText(course.getCourseEndDate());
                instructorNameTxt.setText(course.getCourseInstructor());
                instructorPhoneTxt.setText(course.getCourseInstructorPhone());
                instructorEmailTxt.setText(course.getCourseInstructorEmail());

                if(course.getCourseStatus().equals(Course.Status.PLAN_TO_TAKE))
                    statusRadioGroup.check(R.id.planToTakeRB);
                else if(course.getCourseStatus().equals(Course.Status.IN_PROGRESS))
                    statusRadioGroup.check(R.id.inProgressRB);
                else if(course.getCourseStatus().equals(Course.Status.COMPLETED))
                    statusRadioGroup.check(R.id.completedRB);
                else if(course.getCourseStatus().equals(Course.Status.DROPPED))
                    statusRadioGroup.check(R.id.droppedRB);
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
        getMenuInflater().inflate(R.menu.home_button_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onUpdateCourseClicked(View view) {
        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        EditText courseTitleTxt = (EditText) findViewById(R.id.courseTitleTxt);
        EditText startDateTxt = (EditText) findViewById(R.id.startDateTxt);
        EditText endDateTxt = (EditText) findViewById(R.id.endDateTxt);
        RadioGroup statusRadioGroup = findViewById(R.id.statusRadioGroup);
        EditText instructorNameTxt = (EditText) findViewById(R.id.instructorNameTxt);
        EditText instructorPhoneTxt = (EditText) findViewById(R.id.instructorPhoneTxt);
        EditText instructorEmailTxt = (EditText) findViewById(R.id.instructorEmailTxt);

        long courseId = CourseDetail.courseId;
        String courseTitle = courseTitleTxt.getText().toString().trim();
        String startDate = startDateTxt.getText().toString().trim();
        String endDate = endDateTxt.getText().toString().trim();
        String instructorName = instructorNameTxt.getText().toString().trim();
        String instructorPhone = instructorPhoneTxt.getText().toString().trim();
        String instructorEmail = instructorEmailTxt.getText().toString().trim();

        if(!isValidInput(courseTitle, startDate, endDate, instructorName, instructorPhone, instructorEmail))
            return;

        if (statusRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a Course Status Radio Button.", Toast.LENGTH_SHORT).show();
            return;
        }
        String courseStatusString = ((RadioButton) statusRadioGroup.findViewById(statusRadioGroup.getCheckedRadioButtonId())).getText().toString();

        Course.Status courseStatus = Course.Status.PLAN_TO_TAKE;
        if(courseStatusString.equals("Plan to Take"))
            courseStatus = Course.Status.PLAN_TO_TAKE;
        else if(courseStatusString.equals("In Progress"))
            courseStatus = Course.Status.IN_PROGRESS;
        else if(courseStatusString.equals("Completed"))
            courseStatus = Course.Status.COMPLETED;
        else if(courseStatusString.equals("Dropped"))
            courseStatus = Course.Status.DROPPED;

        Course updatedCourse = new Course(courseId, TermDetail.termId, courseTitle, startDate, endDate, courseStatus, instructorName, instructorPhone, instructorEmail);
        CourseDAOImpl.updateCourse(updatedCourse, db);

        Intent intent = new Intent(this, CourseDetail.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean isValidInput(String courseTitle, String startDate, String endDate, String instructorName, String instructorPhone, String instructorEmail) {
        boolean emptyInput = false;

        String errorMessage = "The following required fields are empty:";
        if(courseTitle.isEmpty()) {
            errorMessage = errorMessage + "\nCourse Title";
            emptyInput = true;
        }
        if(startDate.isEmpty()) {
            errorMessage = errorMessage + "\nStart Date";
            emptyInput = true;
        }
        if(endDate.isEmpty()) {
            errorMessage = errorMessage + "\nEnd Date";
            emptyInput = true;
        }
        if(instructorName.isEmpty()) {
            errorMessage = errorMessage + "\nInstructor Name";
            emptyInput = true;
        }
        if(instructorPhone.isEmpty()) {
            errorMessage = errorMessage + "\nInstructor Phone";
            emptyInput = true;
        }
        if(instructorEmail.isEmpty()) {
            errorMessage = errorMessage + "\nInstructor Email";
            emptyInput = true;
        }
        if(emptyInput) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Error");
            builder1.setMessage(errorMessage);
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            //dialog.cancel();
                        }
                    });


            AlertDialog alert11 = builder1.create();
            alert11.show();

            return !emptyInput;
        }

        boolean incorrectDateFormat = false;
        errorMessage = "Dates must be in the format \"YYYY-MM-DD\"\n";

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        try {
            LocalDate date = LocalDate.parse(startDate, formatter);
        } catch (DateTimeParseException e) {
            errorMessage = errorMessage + startDate + " is not a valid date.\n";
            incorrectDateFormat = true;
        }

        try {
            LocalDate date = LocalDate.parse(endDate, formatter);
        } catch (DateTimeParseException e) {
            errorMessage = errorMessage + endDate + " is not a valid date.";
            incorrectDateFormat = true;
        }

        if(incorrectDateFormat) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle("Error");
            builder1.setMessage(errorMessage);
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            //dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

            return false;
        }
        return true;
    }
}