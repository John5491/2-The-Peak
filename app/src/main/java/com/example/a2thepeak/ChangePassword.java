package com.example.a2thepeak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ChangePassword extends AppCompatActivity {
    EditText editTextOldPassword, editTextNewPassword, editTextNewRPassword;
    Button btnUpdate;
    ImageView btnBack, imageViewProfile;
    TextView textViewName;

    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        editTextOldPassword = findViewById(R.id.editTextOldPassword);
        editTextNewPassword = findViewById(R.id.editTextNewPassowrd);
        editTextNewRPassword = findViewById(R.id.editTextNewRPassowrd);
        textViewName = findViewById(R.id.textViewName);
        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);
        imageViewProfile = findViewById(R.id.cardimageViewPiece);

        TTPDatabase db = new TTPDatabase(this);
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userInfo = sessionManager.getUserDataFromSession();

        textViewName.setText("" + userInfo.get(SessionManager.KEY_USERNAME) + "'s");
        image_uri = Uri.parse(db.getProfileUri(userInfo.get(SessionManager.KEY_USERNAME)));
        imageViewProfile.setImageURI(image_uri);

        setClickable(userInfo, db);
    }

    private void setClickable(HashMap<String, String> userInfo, TTPDatabase db){
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("id", "1");
            startActivity(intent);
        });
        btnUpdate.setOnClickListener(v -> {
            updatePassword(userInfo, db);
        });
    }

    private void updatePassword(HashMap<String, String> userInfo, TTPDatabase db) {
        String oldPassword = editTextOldPassword.getText().toString();
        String newPassword = editTextNewPassword.getText().toString();
        String newRPassword = editTextNewRPassword.getText().toString();

        if(oldPassword.equals("") || newPassword.equals("") || newRPassword.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
        else {
            if(newPassword.equals(newRPassword)) {
                boolean isOldPasswordCorrect = db.verifyProfile(userInfo.get(SessionManager.KEY_USERNAME), oldPassword);
                if (isOldPasswordCorrect) {
                    boolean isUpdated = db.updatePassword(userInfo.get(SessionManager.KEY_USERNAME), newPassword);
                    if(isUpdated) {
                        Toast.makeText(getApplicationContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("id", "1");
                        startActivity(intent);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "Failed To Change Password", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Incorrect Old Password", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Password and Retype Password does not match", Toast.LENGTH_SHORT).show();
        }
    }
}