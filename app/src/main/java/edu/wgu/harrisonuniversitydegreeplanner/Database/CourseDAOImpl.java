package edu.wgu.harrisonuniversitydegreeplanner.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Model.Course;

public class CourseDAOImpl {
    private static final String TAG = "CourseDAOImpl";

    public static long insertCourse(Course newCourse, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put("courseTermId", newCourse.getTermId());
        values.put("courseTitle", newCourse.getCourseTitle());
        values.put("courseStartDate", newCourse.getCourseStartDate());
        values.put("courseEndDate", newCourse.getCourseEndDate());
        values.put("courseStatus", newCourse.getCourseStatus().toString());
        values.put("courseInstructor", newCourse.getCourseInstructor());
        values.put("courseInstructorPhone", newCourse.getCourseInstructorPhone());
        values.put("courseInstructorEmail", newCourse.getCourseInstructorEmail());

        return db.insert(DBHelper.TABLE_COURSES, null, values);
    }

    public static ArrayList<Course> getAllCourses(SQLiteDatabase db) {
        ArrayList<Course> allCourses = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_COURSES, null);

        if (cursor.moveToFirst()) {
            do {
                long courseId = cursor.getInt(0);
                long termId = cursor.getInt(1);
                String courseTitle = cursor.getString(2);
                String courseStartDate = cursor.getString(3);
                String courseEndDate = cursor.getString(4);
                Course.Status courseStatus = Course.Status.valueOf(cursor.getString(5));
                String courseInstructor = cursor.getString(6);
                String courseInstructorPhone = cursor.getString(7);
                String courseInstructorEmail = cursor.getString(8);

                Log.d(TAG, "Course = " + courseId + ", " + termId + ", " + courseTitle + ", " + courseStartDate + ", " + courseEndDate + ", " + courseStatus + ", " + courseInstructor + ", " + courseInstructorPhone + ", " + courseInstructorEmail);
                allCourses.add(new Course(courseId, termId, courseTitle, courseStartDate, courseEndDate, courseStatus, courseInstructor, courseInstructorPhone, courseInstructorEmail));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allCourses;
    }

    public static int updateCourse(Course updatedCourse, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put("courseTermId", updatedCourse.getTermId());
        values.put("courseTitle", updatedCourse.getCourseTitle());
        values.put("courseStartDate", updatedCourse.getCourseStartDate());
        values.put("courseEndDate", updatedCourse.getCourseEndDate());
        values.put("courseStatus", updatedCourse.getCourseStatus().toString());
        values.put("courseInstructor", updatedCourse.getCourseInstructor());
        values.put("courseInstructorPhone", updatedCourse.getCourseInstructorPhone());
        values.put("courseInstructorEmail", updatedCourse.getCourseInstructorEmail());

        String[] whereArgs = {Long.toString(updatedCourse.getCourseId())};

        return db.update(DBHelper.TABLE_COURSES, values, "_id = ?", whereArgs);
    }

    public static int deleteCourse(long courseId, SQLiteDatabase db) {
        String[] whereArgs = {Long.toString(courseId)};

        return db.delete(DBHelper.TABLE_COURSES, "_id = ?", whereArgs);
    }
}
