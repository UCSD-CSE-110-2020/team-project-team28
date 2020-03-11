package com.example.wwr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class TeamRouteScreenAdapter extends RecyclerView.Adapter<TeamRouteScreenAdapter.TeamRouteScreenViewHolder> {
    private ArrayList<Route> routeList;
    Hashtable<String, String> teamRoutes;
    ArrayList<Route> myRouteList;

    Context context;


    public TeamRouteScreenAdapter(ArrayList<Route> routeList,
                                  Context context,
                                  ArrayList<Route> myRouteList) {
        this.routeList = routeList;
        this.context = context;
        this.teamRoutes = teamRoutes;
        this.myRouteList = myRouteList;

    }



    public static class TeamRouteScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public ImageView image;
        public TextView routeName;
        public TextView teamMemberName;
        public TextView startingPoint;
        public TextView totalTime;
        public TextView totalSteps;
        public TextView totalDistance;
        public CheckedTextView checkedRoute;

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
            checkedRoute = view.findViewById(R.id.team_checkedRoute);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Route currentRoute = routeList.get(position);
            Intent intent = new Intent(this.context, TeamRouteDetail.class);

            // Fix later for team information
            intent.putExtra("routeName", "Route Name: " + currentRoute.getName());
            intent.putExtra("startLocation", "Start Location: " + currentRoute.getStartLocation());
            intent.putExtra("timeTaken", "Seconds Taken: " + (currentRoute.getTotalSeconds()));
            intent.putExtra("steps", "Steps: " + (currentRoute.getSteps()));
            intent.putExtra("distance", "Distance: " + (currentRoute.getTotalMiles()));
            intent.putExtra("features", "Features: \n" + currentRoute.getFlatOrHilly()
                    + "\n" + currentRoute.getLoopOrOut() + "\n" + currentRoute.getStreetOrTrail() +
                    "\n" + currentRoute.getSurface() + "\n" + currentRoute.getDifficulty());
            intent.putExtra("note", "Notes: " + currentRoute.getNote());

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

        /*
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        TextDrawable drawable = TextDrawable.builder().buildRect(currentRoute.getUserName().charAt(0) + "", color);

        holder.image.setImageDrawable(drawable);
         */
        holder.image.setImageResource(0);
        holder.routeName.setText(currentRoute.getName());
        holder.teamMemberName.setText(currentRoute.getUserName());
        holder.startingPoint.setText("Starting Location: " + currentRoute.getStartLocation());
        holder.totalTime.setText("Total Time: " + currentRoute.getTotalSeconds() + "s");
        holder.totalSteps.setText("Total Steps: " + currentRoute.getSteps() + " steps");
        holder.totalDistance.setText("Total Distance: " + currentRoute.getTotalMiles() + " miles");
        holder.checkedRoute.setCheckMarkDrawable(R.drawable.check);
        holder.checkedRoute.setVisibility(View.INVISIBLE);

        for (Route route : myRouteList) {
            if (route.getName().equals(currentRoute.getName()) && route.hasWalked()) {
                holder.checkedRoute.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

}