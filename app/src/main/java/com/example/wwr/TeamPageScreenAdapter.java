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
        // Current Team
        final Route currentRoute = routeList.get(position); // final
        holder.teamMemberInitial.setImageResource(currentRoute.getImage());
        holder.memberName.setText(currentRoute.getUserName());
        holder.memberEmail.setText(currentRoute.getUserEmail());
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

}