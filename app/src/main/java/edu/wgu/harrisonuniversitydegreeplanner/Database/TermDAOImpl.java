package edu.wgu.harrisonuniversitydegreeplanner.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Model.Term;

public class TermDAOImpl {

    private static final String TAG = "TermDAOImpl";

    public static long insertTerm(Term newTerm, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put("termTitle", newTerm.getTermTitle());
        values.put("termStartDate", newTerm.getTermStartDate());
        values.put("termEndDate", newTerm.getTermEndDate());

        return db.insert(DBHelper.TABLE_TERMS, null, values);
    }

    public static ArrayList<Term> getAllTerms(SQLiteDatabase db) {
        ArrayList<Term> allTerms = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_TERMS, null);

        if (cursor.moveToFirst()) {
            do {
                long termId = cursor.getInt(0);
                String termTitle = cursor.getString(1);
                String termStartDate = cursor.getString(2);
                String termEndDate = cursor.getString(3);
                Log.d(TAG, "Term = " + termId + ", " + termTitle + ", " + termStartDate + ", " + termEndDate);
                allTerms.add(new Term(termId, termTitle, termStartDate, termEndDate));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allTerms;
    }

    public static int updateTerm(Term updatedTerm, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put("termTitle", updatedTerm.getTermTitle());
        values.put("termStartDate", updatedTerm.getTermStartDate());
        values.put("termEndDate", updatedTerm.getTermEndDate());

        String[] whereArgs = {Long.toString(updatedTerm.getTermId())};

        return db.update(DBHelper.TABLE_TERMS, values, "_id = ?", whereArgs);
    }

    public static int deleteTerm(long termId, SQLiteDatabase db) {
        String[] whereArgs = {Long.toString(termId)};

        return db.delete(DBHelper.TABLE_TERMS, "_id = ?", whereArgs);
    }
}
