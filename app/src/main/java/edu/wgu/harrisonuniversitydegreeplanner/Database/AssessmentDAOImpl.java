package edu.wgu.harrisonuniversitydegreeplanner.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Model.Assessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.ObjectiveAssessment;
import edu.wgu.harrisonuniversitydegreeplanner.Model.PerformanceAssessment;

public class AssessmentDAOImpl {

    private static final String TAG = "AssessmentDAOImpl";

    public static long insertAssessment(Assessment newAssessment, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        if (newAssessment instanceof ObjectiveAssessment) {
            values.put("objectiveAssessmentCourseId", newAssessment.getCourseId());
            values.put("objectiveAssessmentTitle", newAssessment.getAssessmentTitle());
            values.put("objectiveAssessmentStartDate", newAssessment.getAssessmentStartDate());
            values.put("objectiveAssessmentEndDate", newAssessment.getAssessmentEndDate());
            values.put("objectiveAssessmentScore", ((ObjectiveAssessment) newAssessment).getAssessmentScore());

            return db.insert(DBHelper.TABLE_OBJECTIVE_ASSESSMENTS, null, values);
        }
        else {
            values.put("performanceAssessmentCourseId", newAssessment.getCourseId());
            values.put("performanceAssessmentTitle", newAssessment.getAssessmentTitle());
            values.put("performanceAssessmentStartDate", newAssessment.getAssessmentStartDate());
            values.put("performanceAssessmentEndDate", newAssessment.getAssessmentEndDate());
            values.put("performanceAssessmentOutcome", ((PerformanceAssessment) newAssessment).getAssessmentOutcome().toString());

            return db.insert(DBHelper.TABLE_PERFORMANCE_ASSESSMENTS, null, values);
        }
    }

    public static ArrayList<Assessment> getAllAssessments(SQLiteDatabase db) {
        ArrayList<Assessment> allAssessments = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_OBJECTIVE_ASSESSMENTS, null);

        if (cursor.moveToFirst()) {
            do {
                long assessmentId = cursor.getInt(0);
                long courseId = cursor.getInt(1);
                String assessmentTitle = cursor.getString(2);
                String assessmentStartDate = cursor.getString(3);
                String assessmentEndDate = cursor.getString(4);
                double assessmentScore = cursor.getDouble(5);
                Log.d(TAG, "Objective Assessment : " + assessmentId + ", " + courseId + ", " + assessmentTitle + ", " + assessmentStartDate+ ", " + assessmentEndDate + ", " + assessmentScore);
                allAssessments.add(new ObjectiveAssessment(assessmentId, courseId, assessmentTitle, assessmentStartDate, assessmentEndDate, assessmentScore));
            } while (cursor.moveToNext());
        }

        cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_PERFORMANCE_ASSESSMENTS, null);

        if (cursor.moveToFirst()) {
            do {
                long assessmentId = cursor.getInt(0);
                long courseId = cursor.getInt(1);
                String assessmentTitle = cursor.getString(2);
                String assessmentStartDate = cursor.getString(3);
                String assessmentEndDate = cursor.getString(4);
                PerformanceAssessment.AssessmentOutcome assessmentOutcome =  PerformanceAssessment.AssessmentOutcome.valueOf(cursor.getString(5));
                Log.d(TAG, "Performance Assessment : " + assessmentId + ", " + courseId + ", " + assessmentTitle + ", " + assessmentStartDate+ ", " + assessmentEndDate + ", " + assessmentOutcome);
                allAssessments.add(new PerformanceAssessment(assessmentId, courseId, assessmentTitle, assessmentStartDate, assessmentEndDate, assessmentOutcome));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return allAssessments;
    }

    public static int updateAssessment(Assessment updatedAssessment, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        if (updatedAssessment instanceof ObjectiveAssessment) {
            values.put("objectiveAssessmentCourseId", updatedAssessment.getCourseId());
            values.put("objectiveAssessmentTitle", updatedAssessment.getAssessmentTitle());
            values.put("objectiveAssessmentStartDate", updatedAssessment.getAssessmentStartDate());
            values.put("objectiveAssessmentEndDate", updatedAssessment.getAssessmentEndDate());
            values.put("objectiveAssessmentScore", ((ObjectiveAssessment) updatedAssessment).getAssessmentScore());

            String[] whereArgs = {Long.toString(updatedAssessment.getAssessmentId())};

            return db.update(DBHelper.TABLE_OBJECTIVE_ASSESSMENTS, values, "_id = ?", whereArgs);
        }
        else {
            values.put("performanceAssessmentCourseId", updatedAssessment.getCourseId());
            values.put("performanceAssessmentTitle", updatedAssessment.getAssessmentTitle());
            values.put("performanceAssessmentStartDate", updatedAssessment.getAssessmentStartDate());
            values.put("performanceAssessmentEndDate", updatedAssessment.getAssessmentEndDate());
            values.put("performanceAssessmentOutcome", ((PerformanceAssessment) updatedAssessment).getAssessmentOutcome().toString());

            String[] whereArgs = {Long.toString(updatedAssessment.getAssessmentId())};

            return db.update(DBHelper.TABLE_PERFORMANCE_ASSESSMENTS, values, "_id = ?", whereArgs);
        }
    }

    public static int deleteAssessment(long assessmentId, boolean isObjectiveAssessment, SQLiteDatabase db) {
        String[] whereArgs = {Long.toString(assessmentId)};

        if(isObjectiveAssessment) {
            return db.delete(DBHelper.TABLE_OBJECTIVE_ASSESSMENTS, "_id = ?", whereArgs);
        }
        else {
            return db.delete(DBHelper.TABLE_PERFORMANCE_ASSESSMENTS, "_id = ?", whereArgs);
        }
    }
}