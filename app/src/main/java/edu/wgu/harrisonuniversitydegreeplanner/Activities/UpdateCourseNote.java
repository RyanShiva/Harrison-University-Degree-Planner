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

import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Database.CourseNoteDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Model.CourseNote;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class UpdateCourseNote extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "UpdateCourseNote";

    static long courseNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_course_note);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
            courseNoteId = Integer.parseInt((String) bundle.get("courseNoteId"));

        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        ArrayList<CourseNote> allCourseNotes = CourseNoteDAOImpl.getAllCourseNotes(db);
        for(CourseNote courseNote : allCourseNotes) {
            if (courseNote.getCourseNoteId() == courseNoteId) {
                EditText courseNoteTxt = (EditText) findViewById(R.id.courseNoteTxt);

                courseNoteTxt.setText(courseNote.getCourseNoteText());
                break;
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
        getMenuInflater().inflate(R.menu.update_course_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_delete_course_note:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete Course Note");
                builder.setMessage("Are you sure you want to delete this course note?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            SQLiteDatabase db = myHelper.getWritableDatabase();

                            public void onClick(DialogInterface dialog, int id) {
                                CourseNoteDAOImpl.deleteCourseNote(courseNoteId, db);
                                Log.d(TAG, "Course note has been deleted.");
                                Intent intent = new Intent(getApplicationContext(), CourseNoteList.class);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onUpdateCourseNoteClicked(View view) {
        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        EditText courseNoteTxt = (EditText) findViewById(R.id.courseNoteTxt);

        String noteText = courseNoteTxt.getText().toString().trim();

        if(!isValidInput(noteText))
            return;

        CourseNote updatedCourseNote = new CourseNote(courseNoteId, CourseDetail.courseId, noteText);
        CourseNoteDAOImpl.updateCourseNote(updatedCourseNote, db);

        Intent intent = new Intent(this, CourseNoteList.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean isValidInput(String noteText) {
        boolean emptyInput = false;

        String errorMessage = "The course note text field is empty.";
        if(noteText.isEmpty()) {
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

    public void onShareNoteClicked(View view) {
        SQLiteDatabase db = myHelper.getWritableDatabase();

        ArrayList<CourseNote> allCourseNotes = CourseNoteDAOImpl.getAllCourseNotes(db);

        String courseNoteText = "";

        for(CourseNote courseNote : allCourseNotes) {
            if(courseNote.getCourseNoteId() == courseNoteId) {
                courseNoteText = courseNote.getCourseNoteText();
                break;
            }
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, CourseDetail.courseTitle + ": Course Note");
        intent.putExtra(Intent.EXTRA_TEXT, courseNoteText);
        try {
            startActivity(intent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}