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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import edu.wgu.harrisonuniversitydegreeplanner.Database.AssessmentDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Model.ObjectiveAssessment;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class AddObjectiveAssessment extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "AddObjectiveAssessment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_objective_assessment);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
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
    public void onAddAssessmentClicked(View view) {
        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        EditText assessmentTitleTxt = (EditText) findViewById(R.id.assessmentTitleTxt);
        EditText startDateTxt = (EditText) findViewById(R.id.startDateTxt);
        EditText endDateTxt = (EditText) findViewById(R.id.endDateTxt);
        EditText scoreTxt = (EditText) findViewById(R.id.scoreTxt);

        long assessmentId = -1;
        String assessmentTitle = assessmentTitleTxt.getText().toString().trim();
        String startDate = startDateTxt.getText().toString().trim();
        String endDate = endDateTxt.getText().toString().trim();

        if(!isValidInput(assessmentTitle, startDate, endDate))
            return;

        double score;
        String scoreText = scoreTxt.getText().toString().trim();
        if(!scoreText.isEmpty()) {
            try {
                score = Double.parseDouble(scoreText);
            }
            catch(Exception e) {
                Log.d(TAG, e.getMessage());
                Toast.makeText(this, "Score must be a positive decimal number.", Toast.LENGTH_SHORT).show();
                return;
            }

            if(score < 0) {
                Toast.makeText(this, "Score must be a positive decimal number.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else {
            score = -1;
        }

        ObjectiveAssessment newObjectiveAssessment = new ObjectiveAssessment(assessmentId, CourseDetail.courseId, assessmentTitle, startDate, endDate, score);
        AssessmentDAOImpl.insertAssessment(newObjectiveAssessment, db);

        Intent intent = new Intent(this, AssessmentList.class);
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