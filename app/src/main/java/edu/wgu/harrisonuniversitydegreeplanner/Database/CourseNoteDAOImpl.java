package edu.wgu.harrisonuniversitydegreeplanner.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Model.CourseNote;

public class CourseNoteDAOImpl {

    private static final String TAG = "CourseNoteDAOImpl";

    public static long insertCourseNote(CourseNote newCourseNote, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put("CourseNoteCourseId", newCourseNote.getCourseId());
        values.put("CourseNoteText", newCourseNote.getCourseNoteText());

        return db.insert(DBHelper.TABLE_COURSE_NOTES, null, values);
    }

    public static ArrayList<CourseNote> getAllCourseNotes(SQLiteDatabase db) {
        ArrayList<CourseNote> allCourseNotes = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_COURSE_NOTES, null);

        if (cursor.moveToFirst()) {
            do {
                long courseNoteId = cursor.getInt(0);
                long courseId = cursor.getInt(1);
                String courseNoteText = cursor.getString(2);

                Log.d(TAG, "Course Note = " + courseNoteId + ", " + courseId + ", " + courseNoteText);
                allCourseNotes.add(new CourseNote(courseNoteId, courseId, courseNoteText));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allCourseNotes;
    }

    public static int updateCourseNote(CourseNote updatedCourseNote, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put("CourseNoteCourseId", updatedCourseNote.getCourseId());
        values.put("CourseNoteText", updatedCourseNote.getCourseNoteText());

        String[] whereArgs = {Long.toString(updatedCourseNote.getCourseNoteId())};

        return db.update(DBHelper.TABLE_COURSE_NOTES, values, "_id = ?", whereArgs);
    }

    public static int deleteCourseNote(long courseNoteId, SQLiteDatabase db) {
        String[] whereArgs = {Long.toString(courseNoteId)};

        return db.delete(DBHelper.TABLE_COURSE_NOTES, "_id = ?", whereArgs);
    }
}
