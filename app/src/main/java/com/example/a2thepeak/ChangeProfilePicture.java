package com.example.a2thepeak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ChangeProfilePicture extends AppCompatActivity {

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Button btnLaunchCamera, btnResetDefault;
    TextView textViewName;
    ImageView imageViewProfile, btnBack;

    TTPDatabase db;
    SessionManager sessionManager ;
    HashMap<String, String> userInfo;

    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile_picture);

        btnLaunchCamera = findViewById(R.id.btnLaunchCamera);
        btnResetDefault = findViewById(R.id.btnResetDefault);
        textViewName = findViewById(R.id.textViewName);
        imageViewProfile = findViewById(R.id.cardimageViewPiece);
        btnBack = findViewById(R.id.btnBack);

        db = new TTPDatabase(this);
        sessionManager = new SessionManager(this);
        userInfo = sessionManager.getUserDataFromSession();

        textViewName.setText(userInfo.get(SessionManager.KEY_USERNAME) + "'s");
        image_uri = Uri.parse(db.getProfileUri(userInfo.get(SessionManager.KEY_USERNAME)));
        imageViewProfile.setImageURI(image_uri);

        setClickable();
    }

    private void setClickable() {
        btnLaunchCamera.setOnClickListener(v -> launchCamera());
        btnResetDefault.setOnClickListener(v -> resetPicture());
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("id", "1");
            startActivity(intent);
        });
    }

    private void launchCamera() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                requestPermissions(permission, PERMISSION_CODE);
            }
            else {
                openCamera();
            }
        }
        else {
            openCamera();
        }
    }

    private void openCamera() {
        ContentValues cv = new ContentValues();
        cv.put(MediaStore.Images.Media.TITLE, "New Picture");
        cv.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {
            imageViewProfile.setImageURI(image_uri);
            Toast.makeText(getApplicationContext(), "Profile Picture Updated", Toast.LENGTH_SHORT).show();
            db.setProfileUri(userInfo.get(SessionManager.KEY_USERNAME), image_uri.toString());
        }
    }

    private void resetPicture() {
        imageViewProfile.setImageResource(R.drawable.profile);
        db.setProfileUri(userInfo.get(SessionManager.KEY_USERNAME), "android.resource://com.example.a2thepeak/drawable/profile");
        Toast.makeText(getApplicationContext(), "Profile Picture Reset", Toast.LENGTH_SHORT).show();
    }
}