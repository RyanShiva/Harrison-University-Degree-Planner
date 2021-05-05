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
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Database.CourseDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Course;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class CourseSearch extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "CourseSearch";

    ArrayList<Course> courseSearchResults = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_search);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.courseSearchList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TermDetail.termId = courseSearchResults.get(position).getTermId();
                Intent intent = new Intent(getApplicationContext(), CourseDetail.class);
                String courseId = String.valueOf(courseSearchResults.get(position).getCourseId());
                intent.putExtra("courseId", courseId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void searchCourses(String query, SQLiteDatabase db) {
        ArrayList<Course> allCourses = CourseDAOImpl.getAllCourses(db);
        courseSearchResults.clear();

        int courseTitlesSize = 0;

        for(Course course : allCourses) {
            if(course.getCourseTitle().contains(query)) {
                ++ courseTitlesSize;
                courseSearchResults.add(course);
            }
        }

        String[] courseTitles = new String[courseTitlesSize];

        if(courseTitlesSize != 0) {
            for (int i = 0; i < courseTitlesSize; ++i) {
                courseTitles[i] = courseSearchResults.get(i).getCourseTitle();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseTitles);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Close the database
        myHelper.close();
    }

    public void onClickSearchCourses(View view) {
        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        EditText searchTxt = findViewById(R.id.searchTxt);
        String query = String.valueOf(searchTxt.getText()).trim();

        searchCourses(query, db);
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