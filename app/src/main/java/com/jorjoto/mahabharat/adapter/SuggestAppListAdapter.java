package com.jorjoto.mahabharat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.jorjoto.mahabharat.R;
import com.jorjoto.mahabharat.model.CategoryModel;
import com.jorjoto.mahabharat.util.Utility;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class SuggestAppListAdapter extends RecyclerView.Adapter<SuggestAppListAdapter.MyViewHolder>{
    List<CategoryModel> data = new ArrayList<CategoryModel>();
    private LayoutInflater inflater;
    private Activity activity;

    public SuggestAppListAdapter(Activity activity, ArrayList<CategoryModel> data) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_suggest_appslist, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final CategoryModel current = data.get(position);
        if (current.getAppPackage() != null && current.getAppPackage().trim().length() > 0 && !Utility.isAppInstalled(activity, current.getAppPackage())) {
            holder.loutMain.setVisibility(View.VISIBLE);

            if (current.getBanner() != null && current.getBanner().trim().length() > 0) {
                holder.loutIcon.setVisibility(View.GONE);
                holder.loutBanner.setVisibility(View.VISIBLE);
                holder.imgBanner.setVisibility(View.VISIBLE);
                holder.probrBanner.setVisibility(View.VISIBLE);
                Picasso.with(activity)
                        .load(current.getBanner())
                        .into(holder.imgBanner, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (holder.probrBanner != null) {
                                    holder.probrBanner.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError() {
                                if (holder.probrBanner != null) {
                                    holder.probrBanner.setVisibility(View.GONE);
                                }
                            }
                        });
            } else {
                holder.loutBanner.setVisibility(View.GONE);
                holder.loutIcon.setVisibility(View.VISIBLE);
                holder.imgIcon.setVisibility(View.VISIBLE);
                holder.probr.setVisibility(View.VISIBLE);
                if (current.getAppIcon() != null && current.getAppIcon().trim().length() > 0) {

                    Picasso.with(activity)
                            .load(current.getAppIcon())
                            .into(holder.imgIcon, new Callback() {
                                @Override
                                public void onSuccess() {
                                    if (holder.probr != null) {
                                        holder.probr.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onError() {
                                    if (holder.probr != null) {
                                        holder.probr.setVisibility(View.GONE);
                                    }
                                }
                            });
                }

                if (current.getAppName() != null && current.getAppName().trim().length() > 0) {
                    holder.txtTitle.setVisibility(View.VISIBLE);
                    holder.txtTitle.setText(current.getAppName());
                } else {
                    holder.txtTitle.setVisibility(View.GONE);
                }
            }
        } else {
            holder.loutMain.setVisibility(View.GONE);
        }
        holder.loutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current.getAppLink() != null && current.getAppLink().trim().length() > 0) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(current.getAppLink()));
                        activity.startActivity(browserIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ProgressBar probr;
        ImageView imgBanner;
        TextView txtTitle;
        LinearLayout loutMain;
        ImageView imgIcon;
        LinearLayout loutIcon, loutBanner;
        private final ProgressBar probrBanner;

        public MyViewHolder(View itemView) {
            super(itemView);
            loutMain = (LinearLayout) itemView.findViewById(R.id.loutMain);
            loutIcon = (LinearLayout) itemView.findViewById(R.id.loutIcon);
            loutBanner = (LinearLayout) itemView.findViewById(R.id.loutBanner);
            imgBanner = (ImageView) itemView.findViewById(R.id.imgBanner);
            imgIcon = (ImageView) itemView.findViewById(R.id.imgIcon);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            probr = (ProgressBar) itemView.findViewById(R.id.probr);
            probrBanner = (ProgressBar) itemView.findViewById(R.id.probrBanner);

        }
    }
}
