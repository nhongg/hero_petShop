package com.example.heroPetShop.fragment.bill;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.heroPetShop.Models.Schedule;
import com.example.heroPetShop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.UUID;

public class ScheduleFragment extends Fragment {

    private EditText edtTitle, edtDescription;
    private TextView tvDate, tvTime;
    private Button btnSelectDate, btnSelectTime, btnSubmit;

    private FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        // Ánh xạ view
        edtTitle = view.findViewById(R.id.edt_title);
        edtDescription = view.findViewById(R.id.edt_description);
        tvDate = view.findViewById(R.id.tv_date);
        tvTime = view.findViewById(R.id.tv_time);
        btnSelectDate = view.findViewById(R.id.btn_select_date);
        btnSelectTime = view.findViewById(R.id.btn_select_time);
        btnSubmit = view.findViewById(R.id.btn_submit);

        // Kết nối Firestore
        firestore = FirebaseFirestore.getInstance();

        // Sự kiện chọn ngày
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        // Sự kiện chọn giờ
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        // Sự kiện gửi dữ liệu
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSchedule();
            }
        });

        return view;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                tvDate.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                tvTime.setText(time);
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

    private void saveSchedule() {
        String title = edtTitle.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String date = tvDate.getText().toString();
        String time = tvTime.getText().toString();

        if (title.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = UUID.randomUUID().toString();
        Schedule schedule = new Schedule(id, title, description, date, time);

        firestore.collection("Schedules").document(id)
                .set(schedule)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Đặt lịch thất bại, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
