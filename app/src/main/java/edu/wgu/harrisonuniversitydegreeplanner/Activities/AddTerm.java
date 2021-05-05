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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Database.TermDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Term;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class AddTerm extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "AddTerm";

    static boolean emptyInput;
    static boolean incorrectDateFormat;
    static String emptyInputErrorMessage;
    static String incorrectDateFormatErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
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
    public void onAddTermClicked(View view) {
        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        EditText termTitleTxt = (EditText) findViewById(R.id.termTitleTxt);
        EditText startDateTxt = (EditText) findViewById(R.id.startDateTxt);
        EditText endDateTxt = (EditText) findViewById(R.id.endDateTxt);

        long termId = -1;
        String termTitle = termTitleTxt.getText().toString().trim();
        String startDate = startDateTxt.getText().toString().trim();
        String endDate = endDateTxt.getText().toString().trim();

        boolean isValidInput = isValidInput(termTitle, startDate, endDate);

        if(!isValidInput) {
            createErrorDialogue();
            return;
        }

        Term newTerm = new Term(termId, termTitle, startDate, endDate);
        TermDAOImpl.insertTerm(newTerm, db);

        Intent intent = new Intent(this, TermList.class);
        startActivity(intent);
    }

    public void createErrorDialogue() {
        if(emptyInput) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(AddTerm.this);
            builder1.setTitle("Error");
            builder1.setMessage(emptyInputErrorMessage);
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
        }

        if(incorrectDateFormat) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(AddTerm.this);
            builder1.setTitle("Error");
            builder1.setMessage(incorrectDateFormatErrorMessage);
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
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean isValidInput(String termTitle, String startDate, String endDate) {
        emptyInput = false;
        incorrectDateFormat = false;

        emptyInputErrorMessage = "The following required fields are empty:";
        if(termTitle.isEmpty()) {
            emptyInputErrorMessage = emptyInputErrorMessage + "\nTerm Title";
            emptyInput = true;
        }
        if(startDate.isEmpty()) {
            emptyInputErrorMessage = emptyInputErrorMessage + "\nStart Date";
            emptyInput = true;
        }
        if(endDate.isEmpty()) {
            emptyInputErrorMessage = emptyInputErrorMessage + "\nEnd Date";
            emptyInput = true;
        }

        if(emptyInput)
            return false;

        incorrectDateFormatErrorMessage = "Dates must be in the format \"YYYY-MM-DD\"\n";

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;

        try {
            LocalDate date = LocalDate.parse(startDate, formatter);
        } catch (DateTimeParseException e) {
            incorrectDateFormatErrorMessage = incorrectDateFormatErrorMessage + startDate + " is not a valid date.\n";
            incorrectDateFormat = true;
        }

        try {
            LocalDate date = LocalDate.parse(endDate, formatter);
        } catch (DateTimeParseException e) {
            incorrectDateFormatErrorMessage = incorrectDateFormatErrorMessage + endDate + " is not a valid date.";
            incorrectDateFormat = true;
        }

        return !incorrectDateFormat && !emptyInput;
    }
}