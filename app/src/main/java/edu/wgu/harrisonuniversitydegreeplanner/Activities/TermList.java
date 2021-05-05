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

import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Database.TermDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Term;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class TermList extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "TermList";

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.termListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), TermDetail.class);
                ArrayList<Term> allTerms = TermDAOImpl.getAllTerms(db);
                String termId = String.valueOf(allTerms.get(position).getTermId());
                intent.putExtra("termId", termId);
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
        ArrayList<Term> allTerms = TermDAOImpl.getAllTerms(db);

        final int NUM_TERMS = allTerms.size();

        String[] termTitles = new String[NUM_TERMS];
        if(NUM_TERMS != 0) {
            for (int i = 0; i < NUM_TERMS; ++i) {
                termTitles[i] = allTerms.get(i).getTermTitle();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, termTitles);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.term_list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_add_term:
                Intent intent2 = new Intent(this, AddTerm.class);
                startActivity(intent2);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}