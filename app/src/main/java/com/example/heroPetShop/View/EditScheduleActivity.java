package com.example.heroPetShop.View;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heroPetShop.Models.Schedule;
import com.example.heroPetShop.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditScheduleActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etDate, etTime;
    private Button btnSave;

    private FirebaseFirestore firestore;
    private String scheduleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_schedule);

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        btnSave = findViewById(R.id.btn_save);

        firestore = FirebaseFirestore.getInstance();
        scheduleId = getIntent().getStringExtra("scheduleId");

        loadScheduleDetails();

        btnSave.setOnClickListener(v -> saveSchedule());
    }

    private void loadScheduleDetails() {
        firestore.collection("Schedules").document(scheduleId).get().addOnSuccessListener(documentSnapshot -> {
            Schedule schedule = documentSnapshot.toObject(Schedule.class);
            if (schedule != null) {
                etTitle.setText(schedule.getTitle());
                etDescription.setText(schedule.getDescription());
                etDate.setText(schedule.getDate());
                etTime.setText(schedule.getTime());
            }
        });
    }

    private void saveSchedule() {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        String date = etDate.getText().toString();
        String time = etTime.getText().toString();

        firestore.collection("Schedules").document(scheduleId)
                .update("title", title, "description", description, "date", date, "time", time)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi khi cập nhật!", Toast.LENGTH_SHORT).show());
    }
}
