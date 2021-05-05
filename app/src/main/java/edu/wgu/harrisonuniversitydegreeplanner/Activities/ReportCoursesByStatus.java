package edu.wgu.harrisonuniversitydegreeplanner.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Database.CourseDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Database.TermDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Course;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Term;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class ReportCoursesByStatus extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "ReportAssessmentsByType";

    TextView timeGeneratedTxt;

    ListView termTitleListView;
    ListView numPlanToTakeListView;
    ListView numInProgressListView;
    ListView numCompletedListView;
    ListView numDroppedListView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_courses_by_status);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        timeGeneratedTxt = findViewById(R.id.timeGeneratedTxt);
        timeGeneratedTxt.setText("Date and Time Generated: " + LocalDateTime.now().toString().replace("T", ", "));

        termTitleListView = findViewById(R.id.termTitleListView);
        numPlanToTakeListView = findViewById(R.id.numPlanToTakeListView);
        numInProgressListView = findViewById(R.id.numInProgressListView);
        numCompletedListView = findViewById(R.id.numCompletedListView);
        numDroppedListView = findViewById(R.id.numDroppedListView);

        ArrayList<Term> allTerms = TermDAOImpl.getAllTerms(db);
        ArrayList<Course> allCourses = CourseDAOImpl.getAllCourses(db);

        final int NUM_TERMS = allTerms.size();

        String[] termTitles = new String[NUM_TERMS];
        String[] numPlanToTake = new String[NUM_TERMS];
        String[] numInProgress = new String[NUM_TERMS];
        String[] numCompleted = new String[NUM_TERMS];
        String[] numDropped = new String[NUM_TERMS];

        if(NUM_TERMS != 0) {
            for (int i = 0; i < NUM_TERMS; ++i) {
                termTitles[i] = allTerms.get(i).getTermTitle();
            }
        }

        int termCounter = 0;
        for(Term term : allTerms) {
            int numPlanToTakeInTerm = 0;
            int numInProgressInTerm = 0;
            int numCompletedInTerm = 0;
            int numDroppedInTerm = 0;

            for(Course course : allCourses) {
                if(term.getTermId() == course.getTermId()) {
                    if(course.getCourseStatus().equals(Course.Status.PLAN_TO_TAKE))
                        ++ numPlanToTakeInTerm;
                    else if(course.getCourseStatus().equals(Course.Status.IN_PROGRESS))
                        ++ numInProgressInTerm;
                    else if(course.getCourseStatus().equals(Course.Status.COMPLETED))
                        ++ numCompletedInTerm;
                    else if(course.getCourseStatus().equals(Course.Status.DROPPED))
                        ++ numDroppedInTerm;
                }
            }

            numPlanToTake[termCounter] = String.valueOf(numPlanToTakeInTerm);
            numInProgress[termCounter] = String.valueOf(numInProgressInTerm);
            numCompleted[termCounter] = String.valueOf(numCompletedInTerm);
            numDropped[termCounter] = String.valueOf(numDroppedInTerm);
            ++ termCounter;
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, termTitles);
        termTitleListView.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numPlanToTake);
        numPlanToTakeListView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numInProgress);
        numInProgressListView.setAdapter(adapter3);
        adapter3.notifyDataSetChanged();

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numCompleted);
        numCompletedListView.setAdapter(adapter4);
        adapter4.notifyDataSetChanged();

        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numDropped);
        numDroppedListView.setAdapter(adapter5);
        adapter5.notifyDataSetChanged();
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
}