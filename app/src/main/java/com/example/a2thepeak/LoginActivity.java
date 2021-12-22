package com.example.a2thepeak;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private Button buttonSignIn, buttonSignUp, buttonForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //bind widget
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonForgotPassword = findViewById(R.id.btnForgotPassword);
        buttonSignIn = findViewById(R.id.btnSignIn);
        buttonSignUp = findViewById(R.id.btnSignUp);

        TTPDatabase db = new TTPDatabase(this);

        //set button intent
        setClickable(db);
    }

    private void setClickable(TTPDatabase db) {
        buttonSignIn.setOnClickListener(v -> signIn(db));
        buttonSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        });
        buttonForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void signIn(TTPDatabase db) {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        if(username.equals("") || password.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
        else {
            boolean userNameExist = db.validateProfile(username);
            if (userNameExist) {
                boolean verify = db.verifyProfile(username, password);
                if(verify) {
                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                    sessionManager.createLoginSession(username, db.getFavourite(username, 1), db.getFavourite(username, 2), db.getFavourite(username, 3),
                            db.getInfo(username, 1), db.getInfo(username, 2));
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("id", "0");
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Username Not Found", Toast.LENGTH_SHORT).show();
        }
    }
}