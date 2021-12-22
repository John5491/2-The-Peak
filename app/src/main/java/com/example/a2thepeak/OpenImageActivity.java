package com.example.a2thepeak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

public class OpenImageActivity extends AppCompatActivity {
    PhotoView photoView;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_image);

        photoView = findViewById(R.id.photoViewCurrent);
        btnBack = findViewById(R.id.btnBack);

        photoView.setImageURI(Uri.parse(getIntent().getStringExtra("uri")));

        setClickable();
    }

    private void setClickable() {
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HikePageActivity.class);
            intent.putExtra("id", String.valueOf(getIntent().getStringExtra("id")));
            startActivity(intent);
        });
    }
}