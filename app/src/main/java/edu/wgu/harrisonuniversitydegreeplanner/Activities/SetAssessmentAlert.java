package edu.wgu.harrisonuniversitydegreeplanner.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import edu.wgu.harrisonuniversitydegreeplanner.Database.AssessmentDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Model.Assessment;
import edu.wgu.harrisonuniversitydegreeplanner.MyAlarm;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class SetAssessmentAlert extends AppCompatActivity {
    // request code 300XX = Assessment Start Date Alarm
    // request code 400XX = Assessment End Date Alarm

    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "SetAssessmentAlert";

    public static ArrayList<Integer> startAlarmAssessmentIds = new ArrayList<>();
    public static ArrayList<Integer> endAlarmAssessmentIds = new ArrayList<>();

    String assessmentStartDate = "", assessmentEndDate = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_assessment_alert);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Degree Planner");

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        TextView setAlertTitle = (TextView) findViewById(R.id.setAlertTitle);
        setAlertTitle.setText("Set " + AssessmentDetail.assessmentTitle + " Alert");

        Switch startSwitch = (Switch) findViewById(R.id.startSwitch);
        Switch endSwitch = (Switch) findViewById(R.id.endSwitch);

        for(int alarmRequestCode : startAlarmAssessmentIds) {
            if(alarmRequestCode == AssessmentDetail.assessmentId + 30000) {
                startSwitch.setChecked(true);
            }
        }

        for(int alarmRequestCode : endAlarmAssessmentIds) {
            if(alarmRequestCode == AssessmentDetail.assessmentId + 40000) {
                endSwitch.setChecked(true);
            }
        }

        ArrayList<Assessment> allAssessments = AssessmentDAOImpl.getAllAssessments(db);
        for(Assessment assessment : allAssessments) {
            if(assessment.getAssessmentId() == AssessmentDetail.assessmentId) {
                assessmentStartDate = assessment.getAssessmentStartDate();
                assessmentEndDate = assessment.getAssessmentEndDate();
            }
        }

        LocalDate startLocalDate = LocalDate.parse(assessmentStartDate);
        if(!startLocalDate.isAfter(LocalDate.now()))
            startSwitch.setClickable(false);
        LocalDate endLocalDate = LocalDate.parse(assessmentEndDate);
        if(!endLocalDate.isAfter(LocalDate.now()))
            endSwitch.setClickable(false);

        startSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                String[] startDateArray = assessmentStartDate.split("-");
                String startYearString = startDateArray[0];
                String startMonthString = startDateArray[1];
                String startDayString = startDateArray[2];
                int startYear = 0, startMonth = 0, startDay = 0;
                try {
                    startYear = Integer.parseInt(startYearString);
                    startMonth = Integer.parseInt(startMonthString) - 1;
                    startDay = Integer.parseInt(startDayString);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }

                int requestCode = (int) AssessmentDetail.assessmentId + 30000;
                if(isChecked) {
                    Calendar calendar = Calendar.getInstance();

                    //calendar.setTimeInMillis(System.currentTimeMillis());
                    //calendar.add(Calendar.SECOND, 7);
                    calendar.set(startYear, startMonth, startDay, 8, 0, 0);

                    setAlarm(calendar.getTimeInMillis(), requestCode);
                }
                else {
                    cancelAlarm(requestCode);
                }
            }

            private void setAlarm(long timeInMillis, int requestCode) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(SetAssessmentAlert.this, MyAlarm.class);

                intent.putExtra("requestCode", String.valueOf(requestCode));
                intent.putExtra("title", AssessmentDetail.assessmentTitle);
                intent.putExtra("startOrEnd", "start");
                intent.putExtra("courseOrAssessment", "assessment");

                PendingIntent pendingIntent = PendingIntent.getBroadcast(SetAssessmentAlert.this, requestCode, intent, 0);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
                startAlarmAssessmentIds.add((Integer) requestCode);
                Toast.makeText(getApplicationContext(), "Start date alarm set", Toast.LENGTH_SHORT).show();
            }

            private void cancelAlarm(int requestCode) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(SetAssessmentAlert.this, MyAlarm.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(SetAssessmentAlert.this, requestCode, intent, 0);

                if (pendingIntent != null && alarmManager != null) {
                    alarmManager.cancel(pendingIntent);
                    startAlarmAssessmentIds.remove((Integer) requestCode);
                    Toast.makeText(getApplicationContext(), "Start date alarm cancelled", Toast.LENGTH_SHORT).show();
                }
            }

        });

        endSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String[] endDateArray = assessmentEndDate.split("-");
                String endYearString = endDateArray[0];
                String endMonthString = endDateArray[1];
                String endDayString = endDateArray[2];
                int endYear = 0, endMonth = 0, endDay = 0;
                try {
                    endYear = Integer.parseInt(endYearString);
                    endMonth = Integer.parseInt(endMonthString) - 1;
                    endDay = Integer.parseInt(endDayString);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }

                int requestCode = (int) AssessmentDetail.assessmentId + 40000;
                if(isChecked) {
                    Calendar calendar = Calendar.getInstance();

                    //calendar.setTimeInMillis(System.currentTimeMillis());
                    //calendar.add(Calendar.SECOND, 7);
                    calendar.set(endYear, endMonth, endDay, 8, 0, 0);

                    setAlarm(calendar.getTimeInMillis(), requestCode);
                }
                else {
                    cancelAlarm(requestCode);
                }
            }

            private void setAlarm(long timeInMillis, int requestCode) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(SetAssessmentAlert.this, MyAlarm.class);

                intent.putExtra("requestCode", String.valueOf(requestCode));
                intent.putExtra("title", AssessmentDetail.assessmentTitle);
                intent.putExtra("startOrEnd", "end");
                intent.putExtra("courseOrAssessment", "assessment");

                PendingIntent pendingIntent = PendingIntent.getBroadcast(SetAssessmentAlert.this, requestCode, intent, 0);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
                endAlarmAssessmentIds.add((Integer) requestCode);
                Toast.makeText(getApplicationContext(), "End date alarm set", Toast.LENGTH_SHORT).show();
            }

            private void cancelAlarm(int requestCode) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(SetAssessmentAlert.this, MyAlarm.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(SetAssessmentAlert.this, requestCode, intent, 0);

                if (pendingIntent != null && alarmManager != null) {
                    alarmManager.cancel(pendingIntent);
                    endAlarmAssessmentIds.remove((Integer) requestCode);
                    Toast.makeText(getApplicationContext(), "End date alarm cancelled", Toast.LENGTH_SHORT).show();
                }
            }

        });
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