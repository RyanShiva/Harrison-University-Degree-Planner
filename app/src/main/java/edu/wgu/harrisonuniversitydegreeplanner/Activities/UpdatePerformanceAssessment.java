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

import edu.wgu.harrisonuniversitydegreeplanner.Database.AssessmentDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Assessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.PerformanceAssessment;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class UpdatePerformanceAssessment extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "UpdatePerfAssessment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_performance_assessment);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        ArrayList<Assessment> allAssessments = AssessmentDAOImpl.getAllAssessments(db);
        for(Assessment assessment : allAssessments) {
            if (assessment.getAssessmentId() == AssessmentDetail.assessmentId && assessment instanceof PerformanceAssessment) {
                EditText assessmentTitleTxt = (EditText) findViewById(R.id.assessmentTitleTxt);
                EditText startDateTxt = (EditText) findViewById(R.id.startDateTxt);
                EditText endDateTxt = (EditText) findViewById(R.id.endDateTxt);
                RadioGroup outcomeRadioGroup = findViewById(R.id.outcomeRadioGroup);

                assessmentTitleTxt.setText(assessment.getAssessmentTitle());
                startDateTxt.setText(assessment.getAssessmentStartDate());
                endDateTxt.setText(assessment.getAssessmentEndDate());
                outcomeRadioGroup.check(R.id.notAttemptedRB);
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
    public void onUpdateAssessmentClicked(View view) {
        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        EditText assessmentTitleTxt = (EditText) findViewById(R.id.assessmentTitleTxt);
        EditText startDateTxt = (EditText) findViewById(R.id.startDateTxt);
        EditText endDateTxt = (EditText) findViewById(R.id.endDateTxt);
        RadioGroup outcomeRadioGroup = findViewById(R.id.outcomeRadioGroup);

        long assessmentId = AssessmentDetail.assessmentId;
        String assessmentTitle = assessmentTitleTxt.getText().toString().trim();
        String startDate = startDateTxt.getText().toString().trim();
        String endDate = endDateTxt.getText().toString().trim();

        if(!isValidInput(assessmentTitle, startDate, endDate))
            return;

        if (outcomeRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select an Outcome Radio Button.", Toast.LENGTH_SHORT).show();
            return;
        }
        String assessmentOutcomeString = ((RadioButton) outcomeRadioGroup.findViewById(outcomeRadioGroup.getCheckedRadioButtonId())).getText().toString();

        PerformanceAssessment.AssessmentOutcome assessmentOutcome = PerformanceAssessment.AssessmentOutcome.NOT_ATTEMPTED;
        if(assessmentOutcomeString.equals("Passed"))
            assessmentOutcome = PerformanceAssessment.AssessmentOutcome.PASS;
        else if(assessmentOutcomeString.equals("Failed"))
            assessmentOutcome = PerformanceAssessment.AssessmentOutcome.FAIL;

        Assessment newAssessment = new PerformanceAssessment(assessmentId, CourseDetail.courseId, assessmentTitle, startDate, endDate, assessmentOutcome);
        AssessmentDAOImpl.updateAssessment(newAssessment, db);

        Intent intent = new Intent(this, AssessmentDetail.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean isValidInput(String assessmentTitle, String startDate, String endDate) {
        boolean emptyInput = false;

        String errorMessage = "The following required fields are empty:";
        if(assessmentTitle.isEmpty()) {
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

            return false;
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