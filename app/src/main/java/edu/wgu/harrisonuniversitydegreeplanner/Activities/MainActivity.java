package edu.wgu.harrisonuniversitydegreeplanner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class MainActivity extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");
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

    public void onViewAllTermsClicked(View view) {
        Intent intent = new Intent(this, TermList.class);
        startActivity(intent);
    }

    public void onResetDBClicked(View view) {
        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        DBHelper.deleteTables(db);
        DBHelper.createTables(db);
        Toast.makeText(MainActivity.this, "The database has been cleared.", Toast.LENGTH_SHORT).show();
    }

    public void onSearchAllCoursesClicked(View view) {
        Intent intent = new Intent(this, CourseSearch.class);
        startActivity(intent);
    }

    public void onGenerateReportClicked(View view) {
        Intent intent = new Intent(this, GenerateReportMenu.class);
        startActivity(intent);
    }

    public void onPopulateSampleDataClicked(View view) {
        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        DBHelper.deleteTables(db);
        DBHelper.createTables(db);
        DBHelper.populateSampleData(db);
        Toast.makeText(MainActivity.this, "The database has been reset and populated with sample data.", Toast.LENGTH_SHORT).show();
    }
}