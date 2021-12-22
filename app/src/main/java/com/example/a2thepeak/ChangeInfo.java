package com.example.a2thepeak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ChangeInfo extends AppCompatActivity {
    TextView textViewName;
    EditText editTextPhone, editTextEmail, editTextPassword;
    Button btnUpdate;
    ImageView btnBack, imageViewProfile;

    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        textViewName = findViewById(R.id.textViewName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnBack = findViewById(R.id.btnBack);
        btnUpdate = findViewById(R.id.btnUpdate);
        imageViewProfile = findViewById(R.id.cardimageViewPiece);

        TTPDatabase db = new TTPDatabase(this);
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userInfo = sessionManager.getUserDataFromSession();

        image_uri = Uri.parse(db.getProfileUri(userInfo.get(SessionManager.KEY_USERNAME)));
        imageViewProfile.setImageURI(image_uri);

        textViewName.setText("" + userInfo.get(SessionManager.KEY_USERNAME) + "'s");
        if(!userInfo.get(SessionManager.KEY_PHONE).equals("")) editTextPhone.setHint(userInfo.get(SessionManager.KEY_PHONE));
        else editTextPhone.setHint("012 3456789");
        if(!userInfo.get(SessionManager.KEY_EMAIL).equals("")) editTextEmail.setHint(userInfo.get(SessionManager.KEY_EMAIL));
        else editTextEmail.setHint("example@sample.com");

        setClickable(db, userInfo, sessionManager);
    }

    private void setClickable(TTPDatabase db, HashMap<String, String> userInfo, SessionManager sessionManager) {
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("id", "1");
            startActivity(intent);
        });
        btnUpdate.setOnClickListener(v -> updateProfile(db, userInfo, sessionManager));
    }

    private void updateProfile(TTPDatabase db, HashMap<String, String> userInfo, SessionManager sessionManager) {
        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        if(!phone.equals("") || !password.equals("") || !email.equals("")) {
            if((!phone.equals("") || !email.equals("")) && password.equals(""))
                Toast.makeText(getApplicationContext(), "Please fill in password to make changes.", Toast.LENGTH_SHORT).show();
            if(!password.equals("")) {
                if(email.equals("") && phone.equals(""))
                    Toast.makeText(getApplicationContext(), "Nothing to update.", Toast.LENGTH_SHORT).show();
                boolean isUpdatable = db.verifyProfile(userInfo.get(SessionManager.KEY_USERNAME), password);
                if(isUpdatable) {
                    boolean isUpdated = false;
                    if(!phone.equals("")) {
                        if(phone.equals(userInfo.get(SessionManager.KEY_PHONE)))
                            Toast.makeText(getApplicationContext(), "Old and New Phone Number are same.", Toast.LENGTH_SHORT).show();
                        else {
                                isUpdatable = db.updateProfile(userInfo.get(SessionManager.KEY_USERNAME), "", phone, "");
                                sessionManager.setKeyPhone(phone);
                            }
                    }
                    if(!email.equals("")) {
                        if(email.equals(userInfo.get(SessionManager.KEY_EMAIL)))
                            Toast.makeText(getApplicationContext(), "Old and New E-mail are same.", Toast.LENGTH_SHORT).show();
                        else {
                            isUpdatable = db.updateProfile(userInfo.get(SessionManager.KEY_USERNAME), "", "", email);
                            sessionManager.setKeyEmail(email);
                        }
                    }
                    if(isUpdatable) {
                        Toast.makeText(getApplicationContext(), "Profile Updated.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("id", "1");
                        startActivity(intent);
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Incorrect Password.", Toast.LENGTH_SHORT).show();
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Please fill in at least 1 field.", Toast.LENGTH_SHORT).show();
    }
}