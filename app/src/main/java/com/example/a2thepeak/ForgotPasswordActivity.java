package com.example.a2thepeak;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextNewPassword, editTextRNewPassword;
    private Button buttonChangePassword;
    private ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        //bind widget
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextNewPassword = findViewById(R.id.editTextPassword);
        editTextRNewPassword = findViewById(R.id.editTextRPassword);
        buttonChangePassword = findViewById(R.id.btnChangePassword);
        buttonBack = findViewById(R.id.btnBack);

        TTPDatabase db = new TTPDatabase(this);
        //set button intent
        setClickable(db);
    }

    private void setClickable(TTPDatabase db) {
        buttonChangePassword.setOnClickListener(v -> changePassword(db));
        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
    }

    private void changePassword(TTPDatabase db) {
        String username = editTextUsername.getText().toString();
        String password = editTextNewPassword.getText().toString();
        String rpassword = editTextRNewPassword.getText().toString();

        if(username.equals("") || password.equals("") || rpassword.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
        else {
            if(password.equals(rpassword)) {
                boolean userNameExist = db.validateProfile(username);
                if (userNameExist) {
                    boolean isInserted = db.updatePassword(username, password);
                    if(isInserted) {
                        Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Failed To Change Password", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Username Not Exist", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Password and Retype Password does not match", Toast.LENGTH_SHORT).show();
        }
    }
}