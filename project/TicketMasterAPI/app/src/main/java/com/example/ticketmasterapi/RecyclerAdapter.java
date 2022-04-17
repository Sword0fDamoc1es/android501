package com.example.ticketmasterapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    List<String> names;
    List<String> descriptions;
    List<String> imageURLS;
    Context context;

    public RecyclerAdapter(Context ct, List<String> n,List<String> d,List<String> u){
        context = ct;
        names = n;
        descriptions = d;
        imageURLS = u;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String title = names.get(position);
        String description = descriptions.get(position);
        String url = imageURLS.get(position);
        holder.tvTitle.setText(title);
        holder.tvDescription.setText(description);

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, EventInfoActivity.class);
                i.putExtra("name", title);
                i.putExtra("description", description);
                i.putExtra("url", url);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle, tvDescription;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
