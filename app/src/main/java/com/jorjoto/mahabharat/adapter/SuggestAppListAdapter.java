package com.jorjoto.mahabharat.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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

public class SuggestAppListAdapter extends RecyclerView.Adapter<SuggestAppListAdapter.MyViewHolder> {
    List<CategoryModel> data = new ArrayList<CategoryModel>();
    private LayoutInflater inflater;
    private Activity activity;

    public SuggestAppListAdapter(Activity activity, List<CategoryModel> data) {
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
        final CategoryModel current = data.get(position);
        if (current.getAppPackage() != null && current.getAppPackage().trim().length() > 0 && !Utility.isAppInstalled(activity, current.getAppPackage())) {
            holder.loutMain.setVisibility(View.VISIBLE);
            if (current.getAppName() != null && current.getAppName().trim().length() > 0) {
                holder.txtTitle.setVisibility(View.VISIBLE);
                holder.txtTitle.setText(current.getAppName());
            } else {
                holder.txtTitle.setVisibility(View.GONE);
            }

            if (current.getAppIcon() != null && current.getAppIcon().trim().length() > 0) {
                Picasso.with(activity).load(current.getAppIcon()).into(holder.imgBanner, new Callback() {
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
