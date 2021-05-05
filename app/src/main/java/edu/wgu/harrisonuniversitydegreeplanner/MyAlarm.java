package edu.wgu.harrisonuniversitydegreeplanner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import edu.wgu.harrisonuniversitydegreeplanner.Activities.SetAssessmentAlert;
import edu.wgu.harrisonuniversitydegreeplanner.Activities.SetCourseAlert;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * READ ME: A Note on Course/Assessment Alerts:
 * Alerts are set to go off on 8:00am of the selected course or assessment start or end date.
 * It is assumed that the end user would have no need to set an alert for a course that has already
 * started on or before the current date. Alerts can only be set for dates after the current date
 * setting of the emulator. If the date set on the emulator is on or after the start or end date
 * of a course or assessment, then the switch to set an alert for that date will be disabled.
 * An alert that has already been set will still go off at 8:00am on the date it was scheduled.
 *
 * Instructions for Testing Alerts for Courses and Assessments:
 * 1. Ensure that the current date setting of the emulator is before the date of the start or end date
 * of the alert to be tested. (If the phone's date/time setting is changed the "Set Alert" screen
 * may need to be reloaded).
 *
 * 2. Click the corresponding alert stitch into the "on" position.
 *
 * 3. Set the time on the emulator to just before 8:00am (e.g. 7:59am).
 * (Important: the time must be set before the date. If the date & time of the emulator
 * is ever set for after 8:00am of the alert date, then the alert will misfire).
 *
 * 4. Set the date on the emulator to the date of the alert.
 *
 * 5. The alert will go off when the clock reaches 8:00am.
 * */

public class MyAlarm extends BroadcastReceiver {

    String channel_id="test";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        String title = "", startOrEnd = "", requestCodeString = "", courseOrAssessment = "";
        int requestCode = 0;

        if (bundle != null) {
            requestCodeString = String.valueOf(bundle.get("requestCode"));
            title = String.valueOf(bundle.get("title"));
            startOrEnd = String.valueOf(bundle.get("startOrEnd"));
            courseOrAssessment = String.valueOf(bundle.get("courseOrAssessment"));

            try {
                requestCode = Integer.parseInt(requestCodeString);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        createNotificationChannel(context,channel_id);

        Notification n= new NotificationCompat.Builder(context, channel_id)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("Today is the " + startOrEnd + " date of " + title + "!").build();

        NotificationManager notificationManager=(NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(requestCode, n);

        Toast.makeText(context.getApplicationContext(), "Today is the " + startOrEnd + " date of " + title + "!", Toast.LENGTH_SHORT).show();

        if(startOrEnd.equals("start")) {
            if(courseOrAssessment.equals("course"))
                SetCourseAlert.startAlarmCourseIds.remove((Integer) requestCode);
            else if(courseOrAssessment.equals("assessment"))
                SetAssessmentAlert.startAlarmAssessmentIds.remove((Integer) requestCode);
        }
        else if(startOrEnd.equals("end")) {
            if(courseOrAssessment.equals("course"))
                SetCourseAlert.endAlarmCourseIds.remove((Integer) requestCode);
            else if(courseOrAssessment.equals("assessment"))
                SetAssessmentAlert.endAlarmAssessmentIds.remove((Integer) requestCode);
        }
    }
    private void createNotificationChannel(Context context, String CHANNEL_ID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel", importance);
            channel.setDescription("Alert Channel");
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}