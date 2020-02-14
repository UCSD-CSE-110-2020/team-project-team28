package com.example.wwr;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;

public class RouteScreenAdapter extends RecyclerView.Adapter<RouteScreenAdapter.RouteScreenViewHolder> {
    private ArrayList<Route> routeList;
    Context context;

    public RouteScreenAdapter(ArrayList<Route> routeList, Context context) {
        this.routeList = routeList;
        this.context = context;
    }

    public static class RouteScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public ImageView image;
        public TextView routeName;
        public TextView mText2;
        public TextView mText3;
        public TextView mText4;

        ArrayList<Route> routeList;
        Context context;


        public RouteScreenViewHolder(View view, Context context, ArrayList<Route> routeList) {
            super(view);
            this.routeList = routeList;
            this.context = context;

            view.setOnClickListener(this);
            image = view.findViewById(R.id.favorite);
            routeName = view.findViewById(R.id.route_name);
            mText2 = view.findViewById(R.id.text2);
            mText3 = view.findViewById(R.id.text3);
            mText4 = view.findViewById(R.id.text4);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Route currentRoute = routeList.get(position);
            Intent intent = new Intent(this.context, RouteDetail.class);
            intent.putExtra("routeName", "Route Name: " + currentRoute.getName());
            intent.putExtra("startLocation", "Start Location: " + currentRoute.getStartLocation());
            intent.putExtra("timeTaken", "Time Taken: " + Integer.toString(currentRoute.getTotalMinutes()));
            intent.putExtra("steps", "Steps: " + Integer.toString(currentRoute.getSteps()));
            intent.putExtra("distance", "Distance: " + Double.toString(currentRoute.getTotalMinutes()));
            intent.putExtra("note", "Notes: " + currentRoute.getNote());

            this.context.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public RouteScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.route_list, parent, false);
        RouteScreenViewHolder viewHolder = new RouteScreenViewHolder(view, context, routeList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RouteScreenViewHolder holder, int position) {
        final Route currentRoute = routeList.get(position); // final

        holder.image.setImageResource(currentRoute.getImage());
        holder.routeName.setText(currentRoute.getName());
        holder.mText2.setText(currentRoute.getStartLocation());
        holder.mText3.setText(Integer.toString(currentRoute.getSteps()));
        holder.mText4.setText(Double.toString(currentRoute.getMiles()));

    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }
}
