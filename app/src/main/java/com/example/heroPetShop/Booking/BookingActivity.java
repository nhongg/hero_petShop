package com.example.heroPetShop.Booking;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heroPetShop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookingActivity extends AppCompatActivity {
    private EditText edtTenThuCung, edtCanNang, edtLoaiThuCung;
    private TextView txtChonNgay, txtChonGio;
    private Button btnDatLich;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private String selectedDate, selectedTime;
    private String tenDichVu; // Dịch vụ từ DetailServiceActivity
    private String userId; // ID người dùng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        edtTenThuCung = findViewById(R.id.edtTenThuCung);
        edtCanNang = findViewById(R.id.edtCanNang);
        edtLoaiThuCung = findViewById(R.id.edtLoaiThuCung);
        txtChonNgay = findViewById(R.id.txtChonNgay);
        txtChonGio = findViewById(R.id.txtChonGio);
        btnDatLich = findViewById(R.id.btnDatLich);

        // Khởi tạo Firebase Auth và Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Lấy thông tin người dùng hiện tại từ Firebase
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid(); // Lấy ID người dùng
        } else {
            Toast.makeText(BookingActivity.this, "Bạn cần đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nhận dữ liệu từ DetailServiceActivity (Tên dịch vụ)
        tenDichVu = getIntent().getStringExtra("tenDichVu");

        // Chọn ngày
        txtChonNgay.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            selectedYear = calendar.get(Calendar.YEAR);
            selectedMonth = calendar.get(Calendar.MONTH);
            selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BookingActivity.this, (view, year, month, dayOfMonth) -> {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;
                selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                txtChonNgay.setText("Ngày đặt: " + selectedDate);
            }, selectedYear, selectedMonth, selectedDay);

            datePickerDialog.show();
        });

        // Chọn giờ
        txtChonGio.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
            selectedMinute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    BookingActivity.this, (view, hourOfDay, minute) -> {
                selectedHour = hourOfDay;
                selectedMinute = minute;
                selectedTime = selectedHour + ":" + (selectedMinute < 10 ? "0" + selectedMinute : selectedMinute);
                txtChonGio.setText("Giờ đặt: " + selectedTime);
            }, selectedHour, selectedMinute, true);

            timePickerDialog.show();
        });

        // Xử lý nút "Đặt Lịch"
        btnDatLich.setOnClickListener(v -> {
            String tenThuCung = edtTenThuCung.getText().toString();
            String canNangStr = edtCanNang.getText().toString();
            String loaiThuCung = edtLoaiThuCung.getText().toString();

            // Kiểm tra dữ liệu
            if (tenThuCung.isEmpty() || canNangStr.isEmpty() || loaiThuCung.isEmpty() ||
                    selectedDate == null || selectedTime == null) {
                Toast.makeText(BookingActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double canNang = Double.parseDouble(canNangStr);

            // Tạo đối tượng Booking mới
            Booking booking = new Booking();
            booking.setTenDichVu("Dịch vụ "+tenDichVu); // Dịch vụ đã nhận từ DetailServiceActivity
            booking.setTenThuCung(tenThuCung);
            booking.setCanNang(canNang);
            booking.setLoaiThuCung(loaiThuCung);
            booking.setTrangThai("Chưa xác nhận");
            // Tạo thời gian đặt lịch
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date thoiGianDatLich = sdf.parse(selectedDate + " " + selectedTime);
                booking.setThoiGianDatLich(thoiGianDatLich);
            } catch (Exception e) {
                e.printStackTrace();
            }

            booking.setTrangThai("Chưa xác nhận");
            booking.setIdUser(userId); // ID người dùng

            // Lưu vào Firestore
            db.collection("bookings")
                    .add(booking)
                    .addOnSuccessListener(documentReference -> {
                        // Gán ID từ Firestore vào booking
                        String idBooking = documentReference.getId();
                        db.collection("bookings").document(idBooking)
                                .update("idBooking", idBooking) // Thêm idBooking vào tài liệu
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(BookingActivity.this, "Đặt lịch thành công", Toast.LENGTH_SHORT).show();
                                    finish();  // Quay lại màn hình trước
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(BookingActivity.this, "Cập nhật ID thất bại", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(BookingActivity.this, "Đặt lịch thất bại", Toast.LENGTH_SHORT).show();
                    });
        });

    }
}
