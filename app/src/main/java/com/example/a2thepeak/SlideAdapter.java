package com.example.a2thepeak;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {

    ArrayList<Hike> allHikes;
    Context context;

    public SlideAdapter(ArrayList<Hike> allHikes, Context context) {
        this.allHikes = allHikes;
        this.context = context;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardpiece, parent, false);
        SlideViewHolder slideViewHolder = new SlideViewHolder(view);
        return slideViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        Hike hike = allHikes.get(position);

        if(hike.get_id() == 1) holder.imageViewPicture.setImageResource(R.drawable.broga);
        if(hike.get_id() == 2) holder.imageViewPicture.setImageResource(R.drawable.gasing);
        if(hike.get_id() == 3) holder.imageViewPicture.setImageResource(R.drawable.saga);
        holder.textViewTitle.setText(hike.getTitle());
        holder.textViewRegion.setText(hike.getRegion());
        if(hike.getDifficulty() == 1) {
            holder.textViewDifficulty.setText("easy");
            holder.textViewDifficulty.setBackgroundResource(R.drawable.difficulty_bg1);
        }
        if(hike.getDifficulty() == 2) {
            holder.textViewDifficulty.setText("moderate");
            holder.textViewDifficulty.setBackgroundResource(R.drawable.difficulty_bg2);
        }
        if(hike.getDifficulty() == 3) {
            holder.textViewDifficulty.setText("hard");
            holder.textViewDifficulty.setBackgroundResource(R.drawable.difficulty_bg3);
        }
        holder.ratingBar.setRating((float) hike.getRating());
        holder.ratingBar.setIsIndicator(true);
        holder.textViewReviewCount.setText("(" + hike.getRatingCount() + ")");
        holder.textViewDistance.setText("Length: " + hike.getLength() + "km");
        holder.textViewTime.setText("Est. " + hike.getTime());

        TTPDatabase db = new TTPDatabase(context);
        SessionManager sessionManager = new SessionManager(context);
        HashMap<String, String> userInfo = sessionManager.getUserDataFromSession();

        final boolean[] saved = {db.getFavourite(userInfo.get(SessionManager.KEY_USERNAME), hike.get_id())};
        if(saved[0]) holder.btnSave.setImageResource(R.drawable.ic_saved);
        else holder.btnSave.setImageResource(R.drawable.ic_save);
        holder.btnSave.setOnClickListener(v -> {
            db.updateFavourite(userInfo.get(SessionManager.KEY_USERNAME), hike.get_id(), !saved[0]);
            saved[0] = !saved[0];
            if(hike.get_id() == 1) sessionManager.setKeyHike1(saved[0] ? 1 : 0);
            if(hike.get_id() == 2) sessionManager.setKeyHike2(saved[0] ? 1 : 0);
            if(hike.get_id() == 3) sessionManager.setKeyHike3(saved[0] ? 1 : 0);
            if(saved[0]) holder.btnSave.setImageResource(R.drawable.ic_saved);
            else holder.btnSave.setImageResource(R.drawable.ic_save);
        });

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HikePageActivity.class);
            intent.putExtra("id", String.valueOf(hike.get_id()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return allHikes.size();
    }

    public static class SlideViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPicture;
        TextView textViewTitle, textViewRegion, textViewDifficulty, textViewReviewCount, textViewDistance, textViewTime;
        RatingBar ratingBar;
        ImageButton btnSave;
        CardView cardView;
        
        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            imageViewPicture = itemView.findViewById(R.id.imageViewPiece);
            textViewTitle = itemView.findViewById(R.id.textViewPieceTitle);
            textViewRegion = itemView.findViewById(R.id.textViewPieceRegion);
            textViewDifficulty = itemView.findViewById(R.id.textViewPieceDifficulty);
            ratingBar = itemView.findViewById(R.id.pieceRatingBar);
            textViewReviewCount = itemView.findViewById(R.id.textViewReviewCount);
            textViewDistance = itemView.findViewById(R.id.textViewDistance);
            textViewTime = itemView.findViewById(R.id.textViewEstimateTime);
            btnSave = itemView.findViewById(R.id.btnSave);
        }
    }
}
