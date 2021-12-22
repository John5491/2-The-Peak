package com.example.a2thepeak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.HashMap;
import java.util.List;

public class HikePageActivity extends AppCompatActivity{
    RelativeLayout layoutHeader;
    LinearLayout layoutReview;
    RatingBar ratingBarHeader, ratingBarStarsAtReview, ratingBarReview;
    ImageButton btnSaved;
    CardView btnMaps, btnReviews, btnImages;
    ImageView imageViewMaps, imageViewPicture1, imageViewPicture2, imageViewPicture3, btnBack;
    Button btnViewMap, btnSubmitReview;
    TextView textViewTitle, textViewDifficulty, textViewRatingCount, textViewRegion, textViewDescription, textViewLength, textViewElevationGain, textViewRouteType, textViewRatingInNumber, textViewImagesAnchor,
                textViewReviewAnchor;
    EditText editTextReview;
    ScrollView scrollView;
    VideoView videoView;
    
    Hike currentHike;
    int pageID;
    boolean saved;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_page);
        
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewDifficulty = findViewById(R.id.textViewDifficulty);
        textViewRatingCount = findViewById(R.id.textViewReviewCount);
        textViewRegion = findViewById(R.id.textViewRegion);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewLength = findViewById(R.id.textViewLength);
        textViewElevationGain = findViewById(R.id.textViewElevationGain);
        textViewRouteType = findViewById(R.id.textViewRouteType);
        textViewRatingInNumber = findViewById(R.id.textViewRatingInNumber);
        textViewImagesAnchor = findViewById(R.id.textViewImagesAnchor);
        textViewReviewAnchor = findViewById(R.id.textViewReviewsAnchor);
        editTextReview = findViewById(R.id.editTextReview);
        layoutHeader = findViewById(R.id.layoutHeader);
        layoutReview = findViewById(R.id.layoutReviewRows);
        ratingBarHeader = findViewById(R.id.ratingBarStars);
        ratingBarStarsAtReview = findViewById(R.id.ratingBarStarsAtReview);
        ratingBarReview = findViewById(R.id.ratingBarReview);
        btnSaved = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        btnMaps = findViewById(R.id.btnMaps);
        btnReviews = findViewById(R.id.btnReviews);
        btnImages = findViewById(R.id.btnImages);
        imageViewMaps = findViewById(R.id.imageViewMap);
        imageViewPicture1 = findViewById(R.id.imageViewPicture1);
        imageViewPicture2 = findViewById(R.id.imageViewPicture2);
        imageViewPicture3 = findViewById(R.id.imageViewPicture3);
        btnViewMap = findViewById(R.id.btnViewMap);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        scrollView = findViewById(R.id.scrollView1);
        videoView = findViewById(R.id.videoView);
        
        TTPDatabase db = new TTPDatabase(this);
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> userInfo = sessionManager.getUserDataFromSession();
        
        pageID = Integer.parseInt(getIntent().getStringExtra("id"));
        currentHike = db.getHikeInfo(pageID);
        
        initialiseElements(db, userInfo);
        setClickable(db, userInfo, sessionManager);

        Handler handler = new Handler();
        handler.postDelayed(() -> scrollView.scrollTo(0, layoutHeader.getTop()), 100);
    }
    
    private void initialiseElements(TTPDatabase db, HashMap<String, String> userInfo){
        Uri uri = null;
        if(pageID == 1) {
            layoutHeader.setBackgroundResource(R.drawable.broga);
            imageViewMaps.setImageResource(R.drawable.brogamap);
            imageViewPicture1.setImageResource(R.drawable.brogapic1);
            imageViewPicture2.setImageResource(R.drawable.brogapic2);
            imageViewPicture3.setImageResource(R.drawable.brogapic3);
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.broga4);
        }
        if(pageID == 2) {
            layoutHeader.setBackgroundResource(R.drawable.gasing);
            imageViewMaps.setImageResource(R.drawable.gasingmap);
            imageViewPicture1.setImageResource(R.drawable.gasing1);
            imageViewPicture2.setImageResource(R.drawable.gasing2);
            imageViewPicture3.setImageResource(R.drawable.gasing3);
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.gasing4);

        }
        if(pageID == 3) {
            layoutHeader.setBackgroundResource(R.drawable.saga);
            imageViewMaps.setImageResource(R.drawable.sagamap);
            imageViewPicture1.setImageResource(R.drawable.saga1);
            imageViewPicture2.setImageResource(R.drawable.saga2);
            imageViewPicture3.setImageResource(R.drawable.saga3);
            uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.saga4);

        }
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        textViewTitle.setText(currentHike.getTitle());
        if(currentHike.getDifficulty() == 1) {
            textViewDifficulty.setText("easy");
            textViewDifficulty.setBackgroundResource(R.drawable.difficulty_bg1);
        }
        if(currentHike.getDifficulty() == 2) {
            textViewDifficulty.setText("moderate");
            textViewDifficulty.setBackgroundResource(R.drawable.difficulty_bg2);
        }
        if(currentHike.getDifficulty() == 3) {
            textViewDifficulty.setText("hard");
            textViewDifficulty.setBackgroundResource(R.drawable.difficulty_bg3);
        }
        textViewRatingCount.setText("(" + currentHike.getRatingCount() + ")");
        textViewRegion.setText(currentHike.getRegion());
        textViewDescription.setText(currentHike.getDescription());
        textViewLength.setText(currentHike.getLength() + " km");
        textViewElevationGain.setText(currentHike.getElevationGain() + " m");
        textViewRouteType.setText(currentHike.getRouteType());
        textViewRatingInNumber.setText(String.valueOf(currentHike.getRating()));
        ratingBarHeader.setRating((float) currentHike.getRating());
        ratingBarStarsAtReview.setRating((float) currentHike.getRating());

        saved = db.getFavourite(userInfo.get(SessionManager.KEY_USERNAME), currentHike.get_id());
        if(saved) btnSaved.setImageResource(R.drawable.ic_saved);
        else btnSaved.setImageResource(R.drawable.ic_save);

        setupReviewSection(db);
    }

    private void setClickable(TTPDatabase db, HashMap<String, String> userInfo, SessionManager sessionManager) {
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("id", "0");
            startActivity(intent);
        });
        btnSaved.setOnClickListener(v -> {
            db.updateFavourite(userInfo.get(SessionManager.KEY_USERNAME), currentHike.get_id(), !saved);
            saved = !saved;
            if(currentHike.get_id() == 1) sessionManager.setKeyHike1(saved ? 1 : 0);
            if(currentHike.get_id() == 2) sessionManager.setKeyHike2(saved ? 1 : 0);
            if(currentHike.get_id() == 3) sessionManager.setKeyHike3(saved ? 1 : 0);
            if(saved) btnSaved.setImageResource(R.drawable.ic_saved);
            else btnSaved.setImageResource(R.drawable.ic_save);
        });
        btnMaps.setOnClickListener(v -> scrollView.scrollTo(0, textViewDescription.getBottom()));
        btnImages.setOnClickListener(v -> scrollView.scrollTo(0, textViewImagesAnchor.getTop()));
        btnReviews.setOnClickListener(v -> scrollView.scrollTo(0, textViewReviewAnchor.getTop()));
        btnViewMap.setOnClickListener(v -> launchMap());
        imageViewPicture1.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OpenImageActivity.class);
            if(pageID == 1) intent.putExtra("uri", "android.resource://com.example.a2thepeak/drawable/brogapic1");
            if(pageID == 2) intent.putExtra("uri", "android.resource://com.example.a2thepeak/drawable/gasing1");
            if(pageID == 3) intent.putExtra("uri", "android.resource://com.example.a2thepeak/drawable/saga1");
            intent.putExtra("id", String.valueOf(pageID));
            startActivity(intent);
        });
        imageViewPicture2.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OpenImageActivity.class);
            if(pageID == 1) intent.putExtra("uri", "android.resource://com.example.a2thepeak/drawable/brogapic2");
            if(pageID == 2) intent.putExtra("uri", "android.resource://com.example.a2thepeak/drawable/gasing2");
            if(pageID == 3) intent.putExtra("uri", "android.resource://com.example.a2thepeak/drawable/saga2");
            intent.putExtra("id", String.valueOf(pageID));
            startActivity(intent);
        });
        imageViewPicture3.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), OpenImageActivity.class);
            if(pageID == 1) intent.putExtra("uri", "android.resource://com.example.a2thepeak/drawable/brogapic3");
            if(pageID == 2) intent.putExtra("uri", "android.resource://com.example.a2thepeak/drawable/gasing3");
            if(pageID == 3) intent.putExtra("uri", "android.resource://com.example.a2thepeak/drawable/saga3");
            intent.putExtra("id", String.valueOf(pageID));
            startActivity(intent);
        });
        btnSubmitReview.setOnClickListener(v -> createReview(db, userInfo));
    }

    private void launchMap() {
        String destination = null;
        if(pageID == 1) destination = "https://www.google.com/maps/place/Broga+Hill+1st+Hilltop/@2.9464154,101.8985678,17z/data=!3m1!4b1!4m5!3m4!1s0x31cdd1ebc65c8109:0xf2eb629d7f3cfb2e!8m2!3d2.9464154!4d101.9007565";
        if(pageID == 2) destination = "https://www.google.com/maps/place/Bukit+Gasing,+Petaling+Jaya,+Selangor/@3.0990084,101.6491482,15z/data=!3m1!4b1!4m5!3m4!1s0x31cc4bcc67d48e97:0x62f1843b7214c354!8m2!3d3.100449!4d101.6540669";
        if(pageID == 3) destination = "https://www.google.com/maps/place/Bukit+Saga+Waterfall/@3.1044347,101.7795965,17z/data=!3m1!4b1!4m5!3m4!1s0x31cc341bc3aebefb:0x487508b4b36d158!8m2!3d3.1044293!4d101.7817852";
        try{
            Uri uri = Uri.parse(destination);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void createReview(TTPDatabase db, HashMap<String, String> userInfo) {
        String description = editTextReview.getText().toString();
        String username = userInfo.get(SessionManager.KEY_USERNAME);
        double rating = (double) ratingBarReview.getRating();
        if(rating == 0.0) rating = 1.0;

        boolean isCreated = db.createReview(username, description, rating, pageID);
        if(isCreated) {
            db.updateHikeRating(pageID);
            Toast.makeText(getApplicationContext(), "Review Submitted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), HikePageActivity.class);
            intent.putExtra("id", String.valueOf(pageID));
            startActivity(intent);
        }
        else Toast.makeText(getApplicationContext(), "Review Submission Failed", Toast.LENGTH_SHORT).show();
    }

    private void setupReviewSection(TTPDatabase db) {
        layoutReview.removeAllViews();

        List<Rating> allRating = db.getAllRating(pageID);
        for(int i = 0; i < allRating.size(); i++) {
            View view = getLayoutInflater().inflate(R.layout.reviewrow, null, false);
            ImageView imageViewPic = view.findViewById(R.id.imageViewReviewPic);
            TextView textViewName = view.findViewById(R.id.textViewName);
            TextView textViewReviewDescription = view.findViewById(R.id.textViewReviewDescription);
            RatingBar ratingBar = view.findViewById(R.id.ratingBarReview);
            ImageButton imageButtonDelete = view.findViewById(R.id.imageButtonDelete);

            Uri image_uri = Uri.parse(db.getProfileUri(allRating.get(i).getUsername()));
            imageViewPic.setImageURI(image_uri);
            textViewName.setText(allRating.get(i).getUsername());
            textViewReviewDescription.setText(allRating.get(i).getDescription());
            ratingBar.setRating((float) allRating.get(i).getRating());
            ratingBar.setIsIndicator(true);

            int finalI = i;
            imageButtonDelete.setOnClickListener(v -> {
                db.deleteReview(allRating.get(finalI).getId());
                db.updateHikeRating(pageID);
                Toast.makeText(getApplicationContext(), "Review Deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), HikePageActivity.class);
                intent.putExtra("id", String.valueOf(pageID));
                startActivity(intent);
            });

            layoutReview.addView(view);
        }
    }
}