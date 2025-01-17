package com.example.wwr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

// An adapter for the view of the route screen so that they are displayed as a recycler view.
public class RouteScreenAdapter extends RecyclerView.Adapter<RouteScreenAdapter.RouteScreenViewHolder> {
    private ArrayList<Route> routeList;
    Context context;

    public RouteScreenAdapter(ArrayList<Route> routeList, Context context) {
        this.routeList = routeList;
        this.context = context;
    }

    public static class RouteScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public ImageView image;
        public TextView memberName;
        public TextView routeName;
        public TextView startingPoint;
        public TextView totalTime;
        public TextView totalSteps;
        public TextView totalDistance;
        public CheckedTextView checkedRoute;

        ArrayList<Route> routeList;
        Context context;


        public RouteScreenViewHolder(View view, Context context, ArrayList<Route> routeList) {

            super(view);
            this.routeList = routeList;
            this.context = context;

            view.setOnClickListener(this);
            image = view.findViewById(R.id.favorite);
            memberName = view.findViewById(R.id.route_screen_member_name);
            routeName = view.findViewById(R.id.route_name);
            startingPoint = view.findViewById(R.id.startingPoint);
            totalTime = view.findViewById(R.id.totalTime);
            totalSteps = view.findViewById(R.id.totalSteps);
            totalDistance = view.findViewById(R.id.totalDistance);
            checkedRoute = view.findViewById(R.id.checkedRouteEe);
        }

        @Override
        // Once we click a route, go to its details page.
        public void onClick(View v) {
            int position = getAdapterPosition();
            Route currentRoute = routeList.get(position);
            Intent intent = new Intent(this.context, RouteDetail.class);

            intent.putExtra("memberName", "Team Member Name: " + currentRoute.getUserName());
            intent.putExtra("routeName", "Route Name: " + currentRoute.getName());
            intent.putExtra("startLocation", "Start Location: " + currentRoute.getStartLocation());
            intent.putExtra("timeTaken", "Seconds Taken: " + (currentRoute.getTotalSeconds()));
            intent.putExtra("steps", "Steps: " + (currentRoute.getSteps()));
            intent.putExtra("distance", "Distance: " + (currentRoute.getTotalMiles()));
            intent.putExtra("features", "Features: \n" + currentRoute.getFlatOrHilly()
                    + "\n" + currentRoute.getLoopOrOut() + "\n" + currentRoute.getStreetOrTrail() +
                    "\n" + currentRoute.getSurface() + "\n" + currentRoute.getDifficulty());
            intent.putExtra("note", "Notes: " + currentRoute.getNote());
            intent.putExtra("position", position);

            SharedPreferences sp = context.getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("currPos", String.valueOf(position));
            editor.apply();

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
        holder.memberName.setText(currentRoute.getUserName());
        holder.routeName.setText(currentRoute.getName());
        holder.startingPoint.setText("Starting Location: " + currentRoute.getStartLocation());
        holder.totalTime.setText("Total Time: " + currentRoute.getTotalSeconds() + "s");
        holder.totalSteps.setText("Total Steps: " + currentRoute.getSteps() + " steps");
        holder.totalDistance.setText("Total Distance: " + currentRoute.getTotalMiles() + " miles");

        // set check mark if route has been walked
        if (currentRoute.hasWalked()) {
            holder.checkedRoute.setCheckMarkDrawable(R.drawable.check);
            holder.checkedRoute.setVisibility(View.VISIBLE);
        } else {
            holder.checkedRoute.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }
}
