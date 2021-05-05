package edu.wgu.harrisonuniversitydegreeplanner.Activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Database.AssessmentDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Assessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.ObjectiveAssessment;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class AssessmentList extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "AssessmentList";

    ArrayList<Assessment> assessmentsInTerm = new ArrayList<>();

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create SQLiteOpenHelper Object
        myHelper = new DBHelper(this);

        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        setContentView(R.layout.activity_assessment_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        TextView assessmentListTitleText = findViewById(R.id.assessmentListTitleText);
        assessmentListTitleText.setText(CourseDetail.courseTitle + " Assessments");

        listView = findViewById(R.id.assessmentListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AssessmentDetail.class);
                String assessmentId = String.valueOf(assessmentsInTerm.get(position).getAssessmentId());
                boolean isObjectiveAssessment = assessmentsInTerm.get(position) instanceof ObjectiveAssessment;
                intent.putExtra("assessmentId", assessmentId);
                intent.putExtra("isObjectiveAssessment", isObjectiveAssessment);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        populateList(db);
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Close the database
        myHelper.close();
    }

    private void populateList(SQLiteDatabase db) {
        ArrayList<Assessment> allAssessments = AssessmentDAOImpl.getAllAssessments(db);
        assessmentsInTerm.clear();

        int assessmentTitlesSize = 0;

        for(Assessment assessment : allAssessments) {
            if(assessment.getCourseId() == CourseDetail.courseId) {
                ++ assessmentTitlesSize;
                assessmentsInTerm.add(assessment);
            }
        }

        String[] assessmentTitles = new String[assessmentTitlesSize];

        if(assessmentTitlesSize != 0) {
            for (int i = 0; i < assessmentTitlesSize; ++i) {
                assessmentTitles[i] = assessmentsInTerm.get(i).getAssessmentTitle();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, assessmentTitles);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.assessment_list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_add_objective_assessment:
                Intent intent2 = new Intent(this, AddObjectiveAssessment.class);
                startActivity(intent2);
                return true;

            case R.id.action_add_performance_assessment:
                Intent intent3 = new Intent(this, AddPerformanceAssessment.class);
                startActivity(intent3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}