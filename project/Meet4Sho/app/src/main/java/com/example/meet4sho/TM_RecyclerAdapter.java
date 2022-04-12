package com.example.meet4sho;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TM_RecyclerAdapter extends RecyclerView.Adapter<TM_RecyclerAdapter.MyViewHolder> {

    List<String> names;
    List<String> descriptions;
    List<String> imageURLS;
    Context context;

    public TM_RecyclerAdapter(Context ct, List<String> n,List<String> d,List<String> u){
        context = ct;
        names = n;
        descriptions = d;
        imageURLS = u;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tm_event_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String title = names.get(position);
        String description = descriptions.get(position);
        String url = imageURLS.get(position);
        holder.tvTitle.setText(title);
        holder.tvDescription.setText(description);
        new TM_EventInfoActivity.DownloadImageTask(holder.ivPreview).execute(url);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, TM_EventInfoActivity.class);
                i.putExtra("name", title);
                i.putExtra("description", description);
                i.putExtra("url", url);
                i.putStringArrayListExtra("names", (ArrayList<String>) names);
                i.putStringArrayListExtra("descriptions", (ArrayList<String>) descriptions);
                i.putStringArrayListExtra("urls", (ArrayList<String>) imageURLS);

                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public void notifyData(List<String> names, List<String> descriptions, List<String> imageURLS) {
        Log.d("notifyData ", names.size() + "");
        this.names = names;
        this.descriptions = descriptions;
        this.imageURLS = imageURLS;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvDescription;
        ImageView ivPreview;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivPreview = itemView.findViewById(R.id.ivPreview);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
