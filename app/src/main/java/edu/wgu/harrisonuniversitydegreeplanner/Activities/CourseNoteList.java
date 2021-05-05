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
import edu.wgu.harrisonuniversitydegreeplanner.Database.CourseNoteDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Model.CourseNote;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class CourseNoteList extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "CourseNoteList";

    ArrayList<CourseNote> courseNotesInTerm = new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_note_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        TextView courseNoteListTitleText = findViewById(R.id.courseNoteListTitleText);
        courseNoteListTitleText.setText(CourseDetail.courseTitle + " Course Notes");

        listView = findViewById(R.id.courseNoteListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), UpdateCourseNote.class);
                ArrayList<CourseNote> allCourseNotes = CourseNoteDAOImpl.getAllCourseNotes(db);
                String courseNoteId = String.valueOf(courseNotesInTerm.get(position).getCourseNoteId());
                intent.putExtra("courseNoteId", courseNoteId);
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
        ArrayList<CourseNote> allCourseNotes = CourseNoteDAOImpl.getAllCourseNotes(db);
        courseNotesInTerm.clear();

        int courseNoteTitlesSize = 0;

        for(CourseNote courseNote : allCourseNotes) {
            if(courseNote.getCourseId() == CourseDetail.courseId) {
                ++ courseNoteTitlesSize;
                courseNotesInTerm.add(courseNote);
            }
        }

        String[] courseNoteTexts = new String[courseNoteTitlesSize];

        if(courseNoteTitlesSize != 0) {
            for (int i = 0; i < courseNoteTitlesSize; ++i) {
                courseNoteTexts[i] = courseNotesInTerm.get(i).getCourseNoteText();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, courseNoteTexts);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_note_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_add_course_note:
                Intent intent2 = new Intent(this, AddCourseNote.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}