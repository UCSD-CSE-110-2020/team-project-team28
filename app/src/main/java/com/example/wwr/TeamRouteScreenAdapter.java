package com.example.wwr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TeamRouteScreenAdapter extends RecyclerView.Adapter<TeamRouteScreenAdapter.TeamRouteScreenViewHolder> {
    private ArrayList<Route> routeList;
    Context context;


    public TeamRouteScreenAdapter(ArrayList<Route> routeList, Context context) {
        this.routeList = routeList;
        this.context = context;

    }

    public static class TeamRouteScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public ImageView image;
        public TextView routeName;
        public TextView teamMemberName;
        public TextView startingPoint;
        public TextView totalTime;
        public TextView totalSteps;
        public TextView totalDistance;

        ArrayList<Route> routeList;
        Context context;

        public TeamRouteScreenViewHolder(View view, Context context, ArrayList<Route> routeList) {
            super(view);
            this.routeList = routeList;
            this.context = context;

            view.setOnClickListener(this);
            image = view.findViewById(R.id.team_favorite);
            routeName = view.findViewById(R.id.team_route_name);
            teamMemberName = view.findViewById(R.id.team_member_name);
            startingPoint = view.findViewById(R.id.team_startingPoint);
            totalTime = view.findViewById(R.id.team_totalTime);
            totalSteps = view.findViewById(R.id.team_totalSteps);
            totalDistance = view.findViewById(R.id.team_totalDistance);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Route currentRoute = routeList.get(position);
            Intent intent = new Intent(this.context, RouteDetail.class);

            intent.putExtra("routeName", "Route Name: " + currentRoute.getName());
            intent.putExtra("startLocation", "Start Location: " + currentRoute.getStartLocation());
            intent.putExtra("timeTaken", "Seconds Taken: " + (currentRoute.getTotalSeconds()));
            intent.putExtra("steps", "Steps: " + (currentRoute.getSteps()));
            intent.putExtra("distance", "Distance: " + (currentRoute.getTotalMiles()));
            intent.putExtra("features", "Features: \n" + currentRoute.getFlatOrHilly()
                    + "\n" + currentRoute.getLoopOrOut() + "\n" + currentRoute.getStreetOrTrail() +
                    "\n" + currentRoute.getSurface() + "\n" + currentRoute.getDifficulty());
            intent.putExtra("note", "Notes: " + currentRoute.getNote());
          //  intent.putExtra("teamRoute", true);

            SharedPreferences sp = context.getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("currPos", String.valueOf(position));

            this.context.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public TeamRouteScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.team_route_list, parent, false);
        TeamRouteScreenViewHolder viewHolder = new TeamRouteScreenViewHolder(view, context, routeList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamRouteScreenViewHolder holder, int position) {
        final Route currentRoute = routeList.get(position); // final

        holder.image.setImageResource(currentRoute.getImage());
        holder.routeName.setText(currentRoute.getName());
        holder.teamMemberName.setText(currentRoute.getUserName());
        holder.startingPoint.setText("Starting Location: " + currentRoute.getStartLocation());
        holder.totalTime.setText("Total Time: " + currentRoute.getTotalSeconds() + "s");
        holder.totalSteps.setText("Total Steps: " + currentRoute.getSteps() + " steps");
        holder.totalDistance.setText("Total Distance: " + currentRoute.getTotalMiles() + " miles");
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

}