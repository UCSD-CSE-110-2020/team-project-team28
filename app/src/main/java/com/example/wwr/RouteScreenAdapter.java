package com.example.wwr;

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
  //  Context context;

    public RouteScreenAdapter(ArrayList<Route> routeList) {
        this.routeList = routeList;
   //     this.context = context;
    }

    public static class RouteScreenViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView routeName;
        public TextView mText2;
        public TextView mText3;
        public TextView mText4;


//        RelativeLayout detailPage;  //

        public RouteScreenViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.favorite);
            routeName = view.findViewById(R.id.route_name);
            mText2 = view.findViewById(R.id.text2);
            mText3 = view.findViewById(R.id.text3);
            mText4 = view.findViewById(R.id.text4);

//            detailPage = view.findViewById(R.id.route_information_page); //
        }
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
        final Route currentRoute = routeList.get(position); // final

        holder.image.setImageResource(currentRoute.getImage());
        holder.routeName.setText(currentRoute.getName());
        holder.mText2.setText(currentRoute.getStartLocation());
        holder.mText3.setText(Integer.toString(currentRoute.getSteps()));
        holder.mText4.setText(Double.toString(currentRoute.getMiles()));

//
//        holder.detailPage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), RouteDetail.class);
//                intent.putExtra("route_name", currentRoute.getName());
//                intent.putExtra("start_location", currentRoute.getStartLocation());
//                intent.putExtra("steps", Integer.toString(currentRoute.getSteps()));
//                startAc
//
//            }
//
//        });
    }




    @Override
    public int getItemCount() {
        return routeList.size();
    }
}
