package com.jorjoto.mahabharat.adapter;

import android.app.Activity;
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
        CategoryModel current = data.get(position);

        holder.loutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
