package com.example.heroPetShop.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.example.heroPetShop.Adapter.ScheduleAdapter;
import com.example.heroPetShop.Models.Schedule;
import com.example.heroPetShop.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ScheduleListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private ArrayList<Schedule> scheduleList;
    private FirebaseFirestore firestore;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scheduleList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();


        // Set up adapter
        adapter = new ScheduleAdapter(this, scheduleList, new ScheduleAdapter.OnScheduleActionListener() {
            @Override
            public void onEdit(Schedule schedule) {
                Intent intent = new Intent(ScheduleListActivity.this, EditScheduleActivity.class);
                intent.putExtra("scheduleId", schedule.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(Schedule schedule) {
                deleteSchedule(schedule);
            }
        });

        recyclerView.setAdapter(adapter);

        // Load schedules
        loadSchedules();
    }

    private void loadSchedules() {
        // Hiện ProgressBar khi đang tải dữ liệu
        progressBar.setVisibility(ProgressBar.VISIBLE);

        firestore.collection("Schedules").get().addOnCompleteListener(task -> {
            // Ẩn ProgressBar khi tải xong
            progressBar.setVisibility(ProgressBar.GONE);

            if (task.isSuccessful()) {
                scheduleList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Schedule schedule = doc.toObject(Schedule.class);
                    if (schedule != null) {
                        schedule.setId(doc.getId());
                        scheduleList.add(schedule);
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Lỗi khi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteSchedule(Schedule schedule) {
        firestore.collection("Schedules").document(schedule.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Xóa item trong danh sách và thông báo cho adapter
                    int position = scheduleList.indexOf(schedule);
                    scheduleList.remove(schedule);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Đã xóa lịch hẹn", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi xóa lịch hẹn", Toast.LENGTH_SHORT).show());
    }
}
