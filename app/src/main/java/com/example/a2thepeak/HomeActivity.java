package com.example.a2thepeak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    SpaceNavigationView navigationView;
    RelativeLayout layoutFeaturePiece, layoutHomeView;
    RecyclerView recyclerViewAllHikes;
    RecyclerView.Adapter adapter;

    Uri image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        navigationView = findViewById(R.id.space);
        layoutHomeView = findViewById(R.id.layoutHomeView);

        int id = Integer.parseInt(getIntent().getStringExtra("id"));

        navigationView.initWithSaveInstanceState(savedInstanceState);
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_love));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_settings));

        TTPDatabase db = new TTPDatabase(this);
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userInfo = sessionManager.getUserDataFromSession();

        setupBotNav(db, userInfo, sessionManager);
        navigationView.changeCurrentItem(id);
    }

    private void setupBotNav(TTPDatabase db, HashMap<String, String> userInfo, SessionManager sessionManager) {
        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                setupHomeFragment();
                setupRecycler(db);
                setupFeaturePiece(db, userInfo, sessionManager);
                navigationView.setCentreButtonSelectable(true);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                if(itemIndex == 0) setupFavouriteFragment(userInfo, db, sessionManager);
                if(itemIndex == 1) setupSettingsFragment(sessionManager, userInfo, db);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                if(itemIndex == 0) setupFavouriteFragment(userInfo, db, sessionManager);
                if(itemIndex == 1) setupSettingsFragment(sessionManager, userInfo, db);
            }
        });
    }

    private void setupHomeFragment() {
        layoutHomeView.removeAllViews();
        View view = getLayoutInflater().inflate(R.layout.homefragment, null, false);
        EditText editTextSearch = view.findViewById(R.id.editTextSearch);
        Button btnSearch = view.findViewById(R.id.btnSearch);
        layoutFeaturePiece = view.findViewById(R.id.layoutFeature);
        recyclerViewAllHikes = view.findViewById(R.id.recyclerViewAllHikes);

        btnSearch.setOnClickListener(v -> {
            String keyword = editTextSearch.getText().toString();
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("Keyword", keyword);
            startActivity(intent);
        });
        layoutHomeView.addView(view);
    }

    private void setupFavouriteFragment(HashMap<String, String> userInfo, TTPDatabase db, SessionManager sessionManager) {
        layoutHomeView.removeAllViews();
        View view = getLayoutInflater().inflate(R.layout.favouritefragment, null, false);
        LinearLayout layoutFavourite = view.findViewById(R.id.layoutFavourite);
        TextView textViewNoFavourite = view.findViewById(R.id.textViewNoFavourite);
        textViewNoFavourite.setVisibility(View.GONE);
        
        ArrayList<Hike> favouriteHikes = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            if(db.getFavourite(userInfo.get(SessionManager.KEY_USERNAME), i+1)) {
                Hike temp = db.getHikeInfo(i+1);
                favouriteHikes.add(temp);
            }
        }
        if(favouriteHikes.size() == 0) {
            textViewNoFavourite.setVisibility(View.VISIBLE);
        }
        for(int i = 0; i < favouriteHikes.size(); i++) {
            View cardView = getLayoutInflater().inflate(R.layout.cardpiece, null, false);
            CardView card = cardView.findViewById(R.id.cardView);
            ImageView imageViewPicture = cardView.findViewById(R.id.imageViewPiece);
            TextView textViewTitle = cardView.findViewById(R.id.textViewPieceTitle);
            TextView textViewRegion = cardView.findViewById(R.id.textViewPieceRegion);
            TextView textViewDifficulty = cardView.findViewById(R.id.textViewPieceDifficulty);
            RatingBar ratingBar = cardView.findViewById(R.id.pieceRatingBar);
            TextView textViewReviewCount = cardView.findViewById(R.id.textViewReviewCount);
            TextView textViewDistance = cardView.findViewById(R.id.textViewDistance);
            TextView textViewTime = cardView.findViewById(R.id.textViewEstimateTime);
            ImageButton btnSave = cardView.findViewById(R.id.btnSave);
            
            Hike hikeTemp = favouriteHikes.get(i);

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
            ratingBar.setIsIndicator(true);
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

            card.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), HikePageActivity.class);
                intent.putExtra("id", String.valueOf(hikeTemp.get_id()));
                startActivity(intent);
            });

            layoutFavourite.addView(cardView);
        }

        layoutHomeView.addView(view);
    }

    private void setupSettingsFragment(SessionManager sessionManager, HashMap<String, String> userInfo, TTPDatabase db) {
        layoutHomeView.removeAllViews();
        View view = getLayoutInflater().inflate(R.layout.settingsfragment, null, false);
        ImageView imageViewProfilePicture = view.findViewById(R.id.cardimageViewPiece);
        TextView textViewName = view.findViewById(R.id.textViewName);
        LinearLayout layoutChangeProfilePicture = view.findViewById(R.id.layoutChangeProfilePicture);
        LinearLayout layoutChangeProfileInfo = view.findViewById(R.id.layoutChangeProfilInfo);
        LinearLayout layoutChangeProfilePassword = view.findViewById(R.id.layoutChangeProfilePassword);
        LinearLayout layoutSignout = view.findViewById(R.id.layoutSignOut);
        textViewName.setText("" + userInfo.get(SessionManager.KEY_USERNAME) + "'s");

        image_uri = Uri.parse(db.getProfileUri(userInfo.get(SessionManager.KEY_USERNAME)));
        imageViewProfilePicture.setImageURI(image_uri);

        layoutChangeProfilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChangeProfilePicture.class);
            startActivity(intent);
        });

        layoutChangeProfileInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChangeInfo.class);
            startActivity(intent);
        });

        layoutChangeProfilePassword.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChangePassword.class);
            startActivity(intent);
        });

        layoutSignout.setOnClickListener(v -> {
            sessionManager.logoutUserFromSession();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });
        layoutHomeView.addView(view);
    }

    private void setupRecycler(TTPDatabase db) {
        recyclerViewAllHikes.setHasFixedSize(true);
        recyclerViewAllHikes.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<Hike> allHikes = new ArrayList<>();
        allHikes.add(db.getHikeInfo(1));
        allHikes.add(db.getHikeInfo(2));
        allHikes.add(db.getHikeInfo(3));

        adapter = new SlideAdapter(allHikes, getApplicationContext());
        recyclerViewAllHikes.setAdapter(adapter);
    }

    private void setupFeaturePiece(TTPDatabase db, HashMap<String, String> userInfo, SessionManager sessionManager) {
        layoutFeaturePiece.removeAllViews();
        Hike hike = db.getHikeInfo(1);

        View slidePiece = getLayoutInflater().inflate(R.layout.cardpiece, null, false);
        CardView cardView = slidePiece.findViewById(R.id.cardView);
        ImageView imageViewPicture = slidePiece.findViewById(R.id.imageViewPiece);
        TextView textViewTitle = slidePiece.findViewById(R.id.textViewPieceTitle);
        TextView textViewRegion = slidePiece.findViewById(R.id.textViewPieceRegion);
        TextView textViewDifficulty = slidePiece.findViewById(R.id.textViewPieceDifficulty);
        RatingBar ratingBar = slidePiece.findViewById(R.id.pieceRatingBar);
        TextView textViewReviewCount = slidePiece.findViewById(R.id.textViewReviewCount);
        TextView textViewDistance = slidePiece.findViewById(R.id.textViewDistance);
        TextView textViewTime = slidePiece.findViewById(R.id.textViewEstimateTime);
        ImageButton btnSave = slidePiece.findViewById(R.id.btnSave);

        textViewTitle.setText(hike.getTitle());
        textViewRegion.setText(hike.getRegion());
        if(hike.getDifficulty() == 1) {
            textViewDifficulty.setText("easy");
            textViewDifficulty.setBackgroundResource(R.drawable.difficulty_bg1);
        }
        if(hike.getDifficulty() == 2) {
            textViewDifficulty.setText("moderate");
            textViewDifficulty.setBackgroundResource(R.drawable.difficulty_bg2);
        }
        if(hike.getDifficulty() == 3) {
            textViewDifficulty.setText("hard");
            textViewDifficulty.setBackgroundResource(R.drawable.difficulty_bg3);
        }
        ratingBar.setRating((float) hike.getRating());
        ratingBar.setIsIndicator(true);
        textViewReviewCount.setText("(" + hike.getRatingCount() + ")");
        textViewDistance.setText("Length: " + hike.getLength() + "km");
        textViewTime.setText("Est. " + hike.getTime());
        imageViewPicture.setImageResource(R.drawable.broga);
        final boolean[] saved = {db.getFavourite(userInfo.get(SessionManager.KEY_USERNAME), 1)};
        if(saved[0]) btnSave.setImageResource(R.drawable.ic_saved);
        else btnSave.setImageResource(R.drawable.ic_save);
        btnSave.setOnClickListener(v -> {
            db.updateFavourite(userInfo.get(SessionManager.KEY_USERNAME), 1, !saved[0]);
            saved[0] = !saved[0];
            sessionManager.setKeyHike1(saved[0] ? 1 : 0);
            if(saved[0]) btnSave.setImageResource(R.drawable.ic_saved);
            else btnSave.setImageResource(R.drawable.ic_save);
        });

        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HikePageActivity.class);
            intent.putExtra("id", "1");
            startActivity(intent);
        });

        layoutFeaturePiece.addView(slidePiece);
    }
}