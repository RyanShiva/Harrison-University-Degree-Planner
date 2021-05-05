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

import edu.wgu.harrisonuniversitydegreeplanner.Database.AssessmentDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.CourseDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Database.TermDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Assessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Course;
import edu.wgu.harrisonuniversitydegreeplanner.Model.ObjectiveAssessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Term;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class ReportAssessmentsByType extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "ReportAssessmentsByType";

    TextView timeGeneratedTxt;

    ListView termTitleListView;
    ListView numObjectiveListView;
    ListView numPerformanceListView;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_assessments_by_type);
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
        numObjectiveListView = findViewById(R.id.numObjectiveListView);
        numPerformanceListView = findViewById(R.id.numPerformanceListView);

        ArrayList<Term> allTerms = TermDAOImpl.getAllTerms(db);
        ArrayList<Course> allCourses = CourseDAOImpl.getAllCourses(db);
        ArrayList<Assessment> allAssessments = AssessmentDAOImpl.getAllAssessments(db);

        final int NUM_TERMS = allTerms.size();

        String[] termTitles = new String[NUM_TERMS];
        String[] numObjective = new String[NUM_TERMS];
        String[] numPerformance = new String[NUM_TERMS];

        if(NUM_TERMS != 0) {
            for (int i = 0; i < NUM_TERMS; ++i) {
                termTitles[i] = allTerms.get(i).getTermTitle();
            }
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, termTitles);
        termTitleListView.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

        int termCounter = 0;
        for(Term term : allTerms) {
            ArrayList<Long> courseIdsInTerm = new ArrayList<>();
            int objAssessmentsInTerm = 0;
            int perfAssessmentsInTerm = 0;

            for(Course course : allCourses) {
                if(term.getTermId() == course.getTermId()) {
                    courseIdsInTerm.add(course.getCourseId());
                }
            }

            for(Assessment assessment : allAssessments) {
                for(Long courseId : courseIdsInTerm) {
                    if(courseId == assessment.getCourseId()) {
                        if(assessment instanceof ObjectiveAssessment)
                            ++ objAssessmentsInTerm;
                        else
                            ++ perfAssessmentsInTerm;

                    }
                }
            }

            numObjective[termCounter] = String.valueOf(objAssessmentsInTerm);
            numPerformance[termCounter] = String.valueOf(perfAssessmentsInTerm);
            ++ termCounter;
        }

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numObjective);
        numObjectiveListView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, numPerformance);
        numPerformanceListView.setAdapter(adapter3);
        adapter3.notifyDataSetChanged();
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