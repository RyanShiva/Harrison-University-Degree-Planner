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
import edu.wgu.harrisonuniversitydegreeplanner.Database.CourseDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Course;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class CourseList extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "CourseList";

    ArrayList<Course> coursesInTerm = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        TextView courseListTitleText = findViewById(R.id.courseListTitleText);
        courseListTitleText.setText(TermDetail.termTitle + " Courses");

        listView = findViewById(R.id.courseListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), CourseDetail.class);
                String courseId = String.valueOf(coursesInTerm.get(position).getCourseId());
                intent.putExtra("courseId", courseId);
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
        ArrayList<Course> allCourses = CourseDAOImpl.getAllCourses(db);
        coursesInTerm.clear();

        int courseTitlesSize = 0;

        for(Course course : allCourses) {
            if(course.getTermId() == TermDetail.termId) {
                ++ courseTitlesSize;
                coursesInTerm.add(course);
            }
        }

        String[] courseTitles = new String[courseTitlesSize];

        if(courseTitlesSize != 0) {
            for (int i = 0; i < courseTitlesSize; ++i) {
                courseTitles[i] = coursesInTerm.get(i).getCourseTitle();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseTitles);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_add_course:
                Intent intent2 = new Intent(this, AddCourse.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onViewCourseDetailClicked(View view) {
        Intent intent = new Intent(this, CourseDetail.class);
        startActivity(intent);
    }
}