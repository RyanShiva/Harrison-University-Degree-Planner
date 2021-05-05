package edu.wgu.harrisonuniversitydegreeplanner.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import edu.wgu.harrisonuniversitydegreeplanner.Database.DBHelper;
import edu.wgu.harrisonuniversitydegreeplanner.Database.UserDAOImpl;
import edu.wgu.harrisonuniversitydegreeplanner.Model.User;
import edu.wgu.harrisonuniversitydegreeplanner.R;

public class Login extends AppCompatActivity {
    DBHelper myHelper = new DBHelper(this);
    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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

    public void onLoginClicked(View view) {
        //Create Database
        SQLiteDatabase db = myHelper.getWritableDatabase();

        EditText usernameTxt = (EditText) findViewById(R.id.usernameTxt);
        EditText passwordTxt = (EditText) findViewById(R.id.passwordTxt);

        String username = usernameTxt.getText().toString().trim();
        String password = passwordTxt.getText().toString().trim();

        ArrayList<User> allUsers = UserDAOImpl.getAllUsers(db);
        User thisUser = new User();

        boolean validUsername = false;
        for(User user : allUsers) {
            if(username.equals(user.getUsername())) {
                validUsername = true;
                thisUser = user;
                break;
            }
        }

        if(!validUsername) {
            Toast.makeText(this, "Invalid Username.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(thisUser.getPassword()))
            Toast.makeText(this, "Invalid Password.", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}