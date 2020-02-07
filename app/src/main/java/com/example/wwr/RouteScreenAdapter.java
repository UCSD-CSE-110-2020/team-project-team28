package com.example.wwr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RouteScreenAdapter extends RecyclerView.Adapter<RouteScreenAdapter.RouteScreenViewHolder> {
    private ArrayList<Route> routeList;

    public static class RouteScreenViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView routeName;
        public TextView mText2;
        public TextView mText3;
        public TextView mText4;


        public RouteScreenViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.favorite);
            routeName = view.findViewById(R.id.route_name);
            mText2 = view.findViewById(R.id.text2);
            mText3 = view.findViewById(R.id.text3);
            mText4 = view.findViewById(R.id.text4);
        }
    }

    public RouteScreenAdapter(ArrayList<Route> routeList) {
        this.routeList = routeList;
    }

    @NonNull
    @Override
    public RouteScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.route_list, parent, false);
        RouteScreenViewHolder viewHolder = new RouteScreenViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RouteScreenViewHolder holder, int position) {
        Route currentRoute = routeList.get(position);

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
