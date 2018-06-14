package com.jorjoto.mahabharat.adapter;

import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jorjoto.mahabharat.R;
import com.jorjoto.mahabharat.activity.YouTubeVideoActivity;
import com.jorjoto.mahabharat.model.CategoryModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SuggestVideoListAdapter extends RecyclerView.Adapter<SuggestVideoListAdapter.MyViewHolder> {
    List<CategoryModel> data = new ArrayList<CategoryModel>();
    private LayoutInflater inflater;
    private Activity activity;

    public SuggestVideoListAdapter(Activity activity, List<CategoryModel> data) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_suggest_videolist, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CategoryModel current = data.get(position);
        if (current.getVideoTitle() != null && current.getVideoTitle().trim().length() > 0) {
            holder.txtTitle.setVisibility(View.VISIBLE);
            holder.txtTitle.setText(current.getVideoTitle());
        } else {
            holder.txtTitle.setVisibility(View.GONE);
        }


        if (current.getVideoThumb() != null && current.getVideoThumb().trim().length() > 0) {
            Picasso.with(activity).load(current.getVideoThumb()).into(holder.imgBanner, new Callback() {
                public void onSuccess() {
                    if (holder.probr != null) {
                        holder.probr.setVisibility(View.GONE);
                    }
                }

                public void onError() {
                    if (holder.probr != null && holder.imgBanner != null) {
                        holder.probr.setVisibility(View.GONE);
                    }

                }
            });
        } else {
            Picasso.with(activity).load("https://i1.ytimg.com/vi/G0wGs3useV8/0.jpg").into(holder.imgBanner, new Callback() {
                public void onSuccess() {
                    if (holder.probr != null) {
                        holder.probr.setVisibility(View.GONE);
                    }
                }

                public void onError() {
                    if (holder.probr != null && holder.imgBanner != null) {
                        holder.probr.setVisibility(View.GONE);
                    }

                }
            });
        }

        if (data.get(position).getVideoLink().equals(YouTubeVideoActivity.currentVideo)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.loutMain.setBackground(activity.getResources().getDrawable(R.drawable.card_view_red));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.loutMain.setBackground(activity.getResources().getDrawable(R.drawable.card_view));
            }
        }
        holder.loutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!data.get(position).getVideoLink().equals(YouTubeVideoActivity.currentVideo)) {
                    YouTubeVideoActivity.youTubePlayer.cueVideo(data.get(position).getVideoLink());
                    YouTubeVideoActivity.currentVideo = data.get(position).getVideoLink();
                    notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        private final ProgressBar probr;
        private final ImageView imgBanner;
        private final TextView txtTitle;
        private final LinearLayout loutMain;

        public MyViewHolder(View itemView) {
            super(itemView);
            loutMain = (LinearLayout) itemView.findViewById(R.id.loutMain);
            probr = (ProgressBar) itemView.findViewById(R.id.probr);
            imgBanner = (ImageView) itemView.findViewById(R.id.imgBanner);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
        }
    }
}
