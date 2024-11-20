package com.example.heroPetShop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.Models.Schedule;
import com.example.heroPetShop.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Schedule> scheduleList;

    public interface OnScheduleActionListener {
        void onEdit(Schedule schedule);
        void onDelete(Schedule schedule);
    }

    private OnScheduleActionListener actionListener;

    public ScheduleAdapter(Context context, ArrayList<Schedule> scheduleList, OnScheduleActionListener actionListener) {
        this.context = context;
        this.scheduleList = scheduleList;
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.tvTitle.setText(schedule.getTitle());
        holder.tvDescription.setText(schedule.getDescription());
        holder.tvDate.setText(schedule.getDate());
        holder.tvTime.setText(schedule.getTime());

        holder.itemView.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.schedule_menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.action_edit:
                            if (actionListener != null) actionListener.onEdit(schedule);
                            return true;
                        case R.id.action_delete:
                            if (actionListener != null) actionListener.onDelete(schedule);
                            return true;
                        default:
                            return false;
                    }
                }
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDescription, tvDate, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
