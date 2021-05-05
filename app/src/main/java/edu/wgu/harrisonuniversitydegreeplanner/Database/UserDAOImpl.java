package edu.wgu.harrisonuniversitydegreeplanner.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Model.User;

public class UserDAOImpl {
    private static final String TAG = "UserDAOImpl";

    public static long insertUser(User newUser, SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put("username", newUser.getUsername());
        values.put("password", newUser.getPassword());

        return db.insert(DBHelper.TABLE_USERS, null, values);
    }

    public static ArrayList<User> getAllUsers(SQLiteDatabase db) {
        ArrayList<User> allUsers = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper.TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                long userId = cursor.getInt(0);
                String username = cursor.getString(1);
                String password = cursor.getString(2);
                Log.d(TAG, "User = " + userId + ", " + username + ", " + password);
                allUsers.add(new User(userId, username, password));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return allUsers;
    }
}
