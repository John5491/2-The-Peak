package com.example.a2thepeak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    Button  btnSearch;
    ImageView btnBack;
    EditText editTextSearch;
    LinearLayout layoutSearch;
    TextView textViewNoResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        editTextSearch = findViewById(R.id.editTextSearch);
        layoutSearch = findViewById(R.id.layoutSearch);
        textViewNoResult = findViewById(R.id.textViewNoResult);

        textViewNoResult.setVisibility(View.GONE);

        editTextSearch.setText(getIntent().getStringExtra("Keyword"));
        searchDB();
        setClickable();
    }

    private void setClickable() {
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("id", "0");
            startActivity(intent);
        });
        btnSearch.setOnClickListener(v -> {
            String keyword = editTextSearch.getText().toString();
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("Keyword", keyword);
            startActivity(intent);
        });
    }

    private void searchDB() {
        TTPDatabase db = new TTPDatabase(this);
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userInfo = sessionManager.getUserDataFromSession();

        List<Hike> searchResult = db.searchHike(getIntent().getStringExtra("Keyword"));
        Log.d("t", "" + searchResult.size());
        if(!(searchResult.size() > 0)) {
            textViewNoResult.setVisibility(View.VISIBLE);
        }
        else {
            for(int i = 0; i < searchResult.size(); i++) {
                View cardView = getLayoutInflater().inflate(R.layout.cardpiece, null, false);
                ImageView imageViewPicture = cardView.findViewById(R.id.imageViewPiece);
                TextView textViewTitle = cardView.findViewById(R.id.textViewPieceTitle);
                TextView textViewRegion = cardView.findViewById(R.id.textViewPieceRegion);
                TextView textViewDifficulty = cardView.findViewById(R.id.textViewPieceDifficulty);
                RatingBar ratingBar = cardView.findViewById(R.id.pieceRatingBar);
                TextView textViewReviewCount = cardView.findViewById(R.id.textViewReviewCount);
                TextView textViewDistance = cardView.findViewById(R.id.textViewDistance);
                TextView textViewTime = cardView.findViewById(R.id.textViewEstimateTime);
                ImageButton btnSave = cardView.findViewById(R.id.btnSave);

                Hike hikeTemp = searchResult.get(i);

                if(hikeTemp.get_id() == 1) imageViewPicture.setImageResource(R.drawable.broga);
                if(hikeTemp.get_id() == 2) imageViewPicture.setImageResource(R.drawable.gasing);
                if(hikeTemp.get_id() == 3) imageViewPicture.setImageResource(R.drawable.saga);
                textViewTitle.setText(hikeTemp.getTitle());
                textViewRegion.setText(hikeTemp.getRegion());
                if(hikeTemp.getDifficulty() == 1) {
                    textViewDifficulty.setText("easy");
                    textViewDifficulty.setBackgroundResource(R.drawable.difficulty_bg1);
                }
                if(hikeTemp.getDifficulty() == 2) {
                    textViewDifficulty.setText("moderate");
                    textViewDifficulty.setBackgroundResource(R.drawable.difficulty_bg2);
                }
                if(hikeTemp.getDifficulty() == 3) {
                    textViewDifficulty.setText("hard");
                    textViewDifficulty.setBackgroundResource(R.drawable.difficulty_bg3);
                }
                ratingBar.setRating((float) hikeTemp.getRating());
                textViewReviewCount.setText("(" + hikeTemp.getRatingCount() + ")");
                textViewDistance.setText("Length: " + hikeTemp.getLength() + "km");
                textViewTime.setText("Est. " + hikeTemp.getTime());
                final boolean[] saved = {db.getFavourite(userInfo.get(SessionManager.KEY_USERNAME), hikeTemp.get_id())};
                if(saved[0]) btnSave.setImageResource(R.drawable.ic_saved);
                else btnSave.setImageResource(R.drawable.ic_save);
                btnSave.setOnClickListener(v -> {
                    db.updateFavourite(userInfo.get(SessionManager.KEY_USERNAME), hikeTemp.get_id(), !saved[0]);
                    saved[0] = !saved[0];
                    if(hikeTemp.get_id() == 1) sessionManager.setKeyHike1(saved[0] ? 1 : 0);
                    if(hikeTemp.get_id() == 2) sessionManager.setKeyHike2(saved[0] ? 1 : 0);
                    if(hikeTemp.get_id() == 3) sessionManager.setKeyHike3(saved[0] ? 1 : 0);
                    if(saved[0]) btnSave.setImageResource(R.drawable.ic_saved);
                    else btnSave.setImageResource(R.drawable.ic_save);
                });

                layoutSearch.addView(cardView);
            }
        }
    }
}