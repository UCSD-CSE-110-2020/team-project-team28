package com.example.wwr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class TeamPageScreenAdapter extends RecyclerView.Adapter<TeamPageScreenAdapter.TeamPageScreenViewHolder> {
    private ArrayList<Route> routeList;
    Context context;


    public TeamPageScreenAdapter(ArrayList<Route> routeList, Context context) {
        this.routeList = routeList;
        this.context = context;

    }

    public static class TeamPageScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public ImageView teamMemberInitial;
        public TextView memberName;
        public TextView memberEmail;

        ArrayList<Route> routeList;
        Context context;

        public TeamPageScreenViewHolder(View view, Context context, ArrayList<Route> routeList) {
            super(view);
            this.routeList = routeList;
            this.context = context;

            view.setOnClickListener(this);
            teamMemberInitial = view.findViewById(R.id.team_member_initial);
            memberName = view.findViewById(R.id.team_page_member_name);
            memberEmail = view.findViewById(R.id.team_page_member_email);
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

            SharedPreferences sp = context.getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("currPos", String.valueOf(position));

            this.context.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public TeamPageScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.team_page_list, parent, false);
        TeamPageScreenViewHolder viewHolder = new TeamPageScreenViewHolder(view, context, routeList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamPageScreenViewHolder holder, int position) {
        final Route currentRoute = routeList.get(position); // final

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        TextDrawable drawable = TextDrawable.builder().buildRect(currentRoute.getUserName().charAt(0) + "", color);

        holder.teamMemberInitial.setImageDrawable(drawable);
        holder.memberName.setText(currentRoute.getUserName());
        holder.memberEmail.setText(currentRoute.getUserEmail());
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

}