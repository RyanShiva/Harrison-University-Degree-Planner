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

import edu.wgu.harrisonuniversitydegreeplanner.Database.CourseDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Database.TermDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Course;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Term;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class TermDetail extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "TermDetail";

    static long termId;
    static String termTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);
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
            termId = Integer.parseInt((String) bundle.get("termId"));
        }

        if (termId > 0) {
            String termStartDate = "", termEndDate = "";
            ArrayList<Term> allTerms = TermDAOImpl.getAllTerms(db);
            for (Term term : allTerms) {
                if (termId == term.getTermId()) {
                    termTitle = term.getTermTitle();
                    termStartDate = term.getTermStartDate();
                    termEndDate = term.getTermEndDate();

                    TextView termTitleTxt = findViewById(R.id.courseListTitleText);
                    TextView startDateTxt = findViewById(R.id.startDateTxt);
                    TextView endDateTxt = findViewById(R.id.endDateTxt);

                    termTitleTxt.setText(termTitle);
                    startDateTxt.setText(termStartDate);
                    endDateTxt.setText(termEndDate);

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
        getMenuInflater().inflate(R.menu.term_detail_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_update_term:
                Intent intent2 = new Intent(this, UpdateTerm.class);
                startActivity(intent2);
                return true;

            case R.id.action_delete_term:

                if(hasCourses(termId)) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setTitle("Failed to Delete " + termTitle);
                    builder1.setMessage("The term cannot be deleted because it has one or more associated courses.");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                SQLiteDatabase db = myHelper.getWritableDatabase();

                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                    return true;
                }

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("Delete Term: " + termTitle);
                builder2.setMessage("Are you sure you want to delete this term?");
                builder2.setCancelable(true);

                builder2.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            SQLiteDatabase db = myHelper.getWritableDatabase();

                            public void onClick(DialogInterface dialog, int id) {
                                TermDAOImpl.deleteTerm(termId, db);
                                Log.d(TAG, "Term has been deleted: " + termTitle);
                                Intent intent = new Intent(getApplicationContext(), TermList.class);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        });

                builder2.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert2 = builder2.create();
                alert2.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onViewCourseListClicked(View view) {
        Intent intent = new Intent(this, CourseList.class);
        startActivity(intent);
    }

    public boolean hasCourses(long termId) {
        SQLiteDatabase db = myHelper.getWritableDatabase();

        ArrayList<Course> allCourses = CourseDAOImpl.getAllCourses(db);

        for(Course course : allCourses) {
            if(course.getTermId() == termId) {
                return true;
            }
        }

        return false;
    }

    public void createAlert(String alertTitle, String alertMessage) {

    }
}