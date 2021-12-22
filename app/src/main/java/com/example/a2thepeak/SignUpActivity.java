package com.example.a2thepeak;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextRPassword;
    private Button buttonSignUp;
    private ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //bind widget
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRPassword = findViewById(R.id.editTextRPassword);
        buttonSignUp = findViewById(R.id.btnSignUp);
        buttonBack = findViewById(R.id.btnBack);

        TTPDatabase db = new TTPDatabase(this);

        //set button intent
        setClickable(db);
    }

    private void setClickable(TTPDatabase db) {
        buttonSignUp.setOnClickListener(v -> signUp(db));
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }

    private void signUp(TTPDatabase db) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String rpassword = editTextRPassword.getText().toString();

        if(username.equals("") || password.equals("") || rpassword.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
        else {
            if(password.equals(rpassword)) {
                boolean userNameTaken = db.validateProfile(username);
                if (!userNameTaken) {
                    boolean isInserted = db.createProfile(username, password);
                    if(isInserted) {
                        if(!db.getIsHikeInserted()) db.insertHikeTable();
                        db.createFavouriteProfile(username);
                        Toast.makeText(getApplicationContext(), "Account Created", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Account Creation Failed", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Username Taken", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Password and Retype Password does not match", Toast.LENGTH_SHORT).show();
        }
    }
}