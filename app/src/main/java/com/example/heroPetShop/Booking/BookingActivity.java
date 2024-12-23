package com.example.heroPetShop.Booking;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.heroPetShop.R;
import com.example.heroPetShop.ultil.OtpSender;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingActivity extends AppCompatActivity {
    private EditText edtTenThuCung, edtCanNang, edtLoaiThuCung;
    private TextView txtChonNgay, txtChonGio;
    private Button btnDatLich;
    private ImageButton btnback;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private String selectedDate, selectedTime,selectedTime1;
    private String tenDichVu; // Dịch vụ từ DetailServiceActivity
    private String userId; // ID người dùng
    Date thoiGianDatLichCt;
    String tenNguoiDung;
    String sdtNguoiDung;

    private RadioButton rdoXacNhanOTP;
    private Button btnca1, btnca2,btnca3,btnca4,btnca5,btnca6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        edtTenThuCung = findViewById(R.id.edtTenThuCung);
        edtCanNang = findViewById(R.id.edtCanNang);
        btnca1= findViewById(R.id.btnCa1);
        btnca2= findViewById(R.id.btnCa2);
        btnca3= findViewById(R.id.btnCa3);
        btnca4= findViewById(R.id.btnCa4);
        btnca5= findViewById(R.id.btnCa5);
        btnca6= findViewById(R.id.btnCa6);
        rdoXacNhanOTP = findViewById(R.id.rdoXacNhanOTP);

        TextView weightWarningText = findViewById(R.id.weightWarningText);



        btnca1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTime = "08:00";
                selectedTime1 = "8:00:00 - 10:00:00";

                txtChonGio.setText("Giờ đặt: " + selectedTime1);
             //   Toast.makeText(BookingActivity.this, ""+selectedTime, Toast.LENGTH_SHORT).show();

            }
        });
        btnca2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTime = "10:00";
                selectedTime1 = "10:00:00 - 12:00:00";

                txtChonGio.setText("Giờ đặt: " + selectedTime1);
               // Toast.makeText(BookingActivity.this, ""+selectedTime, Toast.LENGTH_SHORT).show();

            }
        }); btnca3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTime = "12:00";
                selectedTime1 = "12:00:00 - 14:00:00";

                txtChonGio.setText("Giờ đặt: " + selectedTime1);
               // Toast.makeText(BookingActivity.this, ""+selectedTime, Toast.LENGTH_SHORT).show();

            }
        });
        btnca4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTime = "14:00";
                selectedTime1 = "14:00:00 - 16:00:00";

                txtChonGio.setText("Giờ đặt: " + selectedTime1);
             //   Toast.makeText(BookingActivity.this, ""+selectedTime, Toast.LENGTH_SHORT).show();

            }
        });
        btnca5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTime = "16:00";

                selectedTime1 = "16:00:00 - 18:00:00";

                txtChonGio.setText("Giờ đặt: " + selectedTime1);
                //Toast.makeText(BookingActivity.this, ""+selectedTime, Toast.LENGTH_SHORT).show();

            }
        });
        btnca6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedTime = "18:00";
                selectedTime1 = "18:00:00 - 20:00:00";

                txtChonGio.setText("Giờ đặt: " + selectedTime1);
                //Toast.makeText(BookingActivity.this, ""+selectedTime, Toast.LENGTH_SHORT).show();

            }
        });


        edtCanNang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Không làm gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    try {
                        double weight = Double.parseDouble(charSequence.toString());

                        // Kiểm tra cân nặng và hiển thị cảnh báo nếu cân nặng quá lớn
                        if (weight > 15) {
                            weightWarningText.setText("Cân nặng quá lớn! Phí dịch vụ của bạn sẽ thêm 50.000");
                            weightWarningText.setVisibility(View.VISIBLE);  // Hiển thị cảnh báo
                        } else {
                            weightWarningText.setVisibility(View.GONE);  // Ẩn cảnh báo khi không có vấn đề
                        }
                    } catch (NumberFormatException e) {
                        weightWarningText.setVisibility(View.GONE);  // Ẩn cảnh báo nếu người dùng nhập không phải số
                    }
                } else {
                    weightWarningText.setVisibility(View.GONE);  // Ẩn cảnh báo nếu chưa nhập gì
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Không làm gì ở đây
            }
        });


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
        String serviceId = getIntent().getStringExtra("serviceId");

        String[] serviceIdArray = serviceId.split(",\\s*");
        List<String> serviceIdList = new ArrayList<>(Arrays.asList(serviceIdArray));
        // Lấy thời gian hiện tại
        Calendar currentCalendar = Calendar.getInstance();
        long currentTimeMillis = currentCalendar.getTimeInMillis();

// Chọn ngày
//        txtChonNgay.setOnClickListener(v -> {
//            Calendar calendar = Calendar.getInstance();
//            selectedYear = calendar.get(Calendar.YEAR);
//            selectedMonth = calendar.get(Calendar.MONTH);
//            selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog datePickerDialog = new DatePickerDialog(
//                    BookingActivity.this, (view, year, month, dayOfMonth) -> {
//                selectedYear = year;
//                selectedMonth = month;
//                selectedDay = dayOfMonth;
//                selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
//
//                // Kiểm tra nếu ngày chọn là quá khứ
//                Calendar selectedCalendar = Calendar.getInstance();
//                selectedCalendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
//                long selectedTimeMillis = selectedCalendar.getTimeInMillis();
//
//                if (selectedTimeMillis < currentTimeMillis) {
//                    // Nếu thời gian chọn là quá khứ, yêu cầu chọn lại
//                    Toast.makeText(BookingActivity.this, "Thời gian không hợp lệ. Vui lòng chọn thời gian trong tương lai.", Toast.LENGTH_SHORT).show();
//                    txtChonNgay.setText("");  // Xóa ngày đã chọn
//                } else {
//                    txtChonNgay.setText("Ngày đặt: " + selectedDate);
//                }
//            }, selectedYear, selectedMonth, selectedDay);
//
//            datePickerDialog.show();
//        });
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

                // Kiểm tra nếu ngày chọn là quá khứ
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay, 0, 0, 0); // Chỉ lấy ngày, không có giờ phút
                long selectedTimeMillis = selectedCalendar.getTimeInMillis();
                //  long currentTimeMillis = System.currentTimeMillis();

                if (selectedTimeMillis < currentTimeMillis) {
                    // Nếu thời gian chọn là quá khứ, yêu cầu chọn lại
                    Toast.makeText(BookingActivity.this, "Thời gian không hợp lệ. Vui lòng chọn thời gian trong tương lai.", Toast.LENGTH_SHORT).show();
                    txtChonNgay.setText("");  // Xóa ngày đã chọn
                } else {
                    txtChonNgay.setText("Ngày đặt: " + selectedDate);

                    // Chuyển ngày chọn thành Timestamp cho thời gian 8h sáng
                    Calendar startCalendar1 = Calendar.getInstance();
                    startCalendar1.set(selectedYear, selectedMonth, selectedDay, 7, 59, 59); // 8h sáng
                    Timestamp startTimestamp1 = new Timestamp(startCalendar1.getTime());

                    // Chuyển ngày chọn thành Timestamp cho thời gian 10h sáng
                    Calendar endCalendar1 = Calendar.getInstance();
                    endCalendar1.set(selectedYear, selectedMonth, selectedDay, 10, 0, 0); // 10h sáng
                    Timestamp endTimestamp1 = new Timestamp(endCalendar1.getTime());

                    // Chuyển ngày chọn thành Timestamp cho thời gian 10h sáng
                    Calendar startCalendar2 = Calendar.getInstance();
                    startCalendar2.set(selectedYear, selectedMonth, selectedDay, 9, 59, 59); // 10h sáng
                    Timestamp startTimestamp2 = new Timestamp(startCalendar2.getTime());

                    // Chuyển ngày chọn thành Timestamp cho thời gian 12h trưa
                    Calendar endCalendar2 = Calendar.getInstance();
                    endCalendar2.set(selectedYear, selectedMonth, selectedDay, 12, 0, 0); // 12h trưa
                    Timestamp endTimestamp2 = new Timestamp(endCalendar2.getTime());

                    // Chuyển ngày chọn thành Timestamp cho thời gian 12h sáng
                    Calendar startCalendar3 = Calendar.getInstance();
                    startCalendar2.set(selectedYear, selectedMonth, selectedDay, 11, 59, 59); // 10h sáng
                    Timestamp startTimestamp3 = new Timestamp(startCalendar2.getTime());

                    // Chuyển ngày chọn thành Timestamp cho thời gian 14h
                    Calendar endCalendar3 = Calendar.getInstance();
                    endCalendar2.set(selectedYear, selectedMonth, selectedDay, 14, 0, 0); // 12h trưa
                    Timestamp endTimestamp3 = new Timestamp(endCalendar2.getTime());


                    // Chuyển ngày chọn thành Timestamp cho thời gian 14h
                    Calendar startCalendar4 = Calendar.getInstance();
                    startCalendar2.set(selectedYear, selectedMonth, selectedDay, 13, 59, 59); // 10h sáng
                    Timestamp startTimestamp4 = new Timestamp(startCalendar2.getTime());

                    // Chuyển ngày chọn thành Timestamp cho thời gian 16h
                    Calendar endCalendar4 = Calendar.getInstance();
                    endCalendar2.set(selectedYear, selectedMonth, selectedDay, 16, 0, 0); // 12h trưa
                    Timestamp endTimestamp4 = new Timestamp(endCalendar2.getTime());


                    // Chuyển ngày chọn thành Timestamp cho thời gian 16h
                    Calendar startCalendar5 = Calendar.getInstance();
                    startCalendar2.set(selectedYear, selectedMonth, selectedDay, 15, 59, 59); // 10h sáng
                    Timestamp startTimestamp5 = new Timestamp(startCalendar2.getTime());

                    // Chuyển ngày chọn thành Timestamp cho thời gian 18h
                    Calendar endCalendar5 = Calendar.getInstance();
                    endCalendar2.set(selectedYear, selectedMonth, selectedDay, 18, 0, 0); // 12h trưa
                    Timestamp endTimestamp5 = new Timestamp(endCalendar2.getTime());


                    // Chuyển ngày chọn thành Timestamp cho thời gian 18h sáng
                    Calendar startCalendar6 = Calendar.getInstance();
                    startCalendar2.set(selectedYear, selectedMonth, selectedDay, 17, 59, 59); // 10h sáng
                    Timestamp startTimestamp6 = new Timestamp(startCalendar2.getTime());

                    // Chuyển ngày chọn thành Timestamp cho thời gian 12h trưa
                    Calendar endCalendar6 = Calendar.getInstance();
                    endCalendar2.set(selectedYear, selectedMonth, selectedDay, 20, 0, 0); // 12h trưa
                    Timestamp endTimestamp6 = new Timestamp(endCalendar2.getTime());

                    // Thực hiện truy vấn Firestore cho khoảng thời gian từ 8h-10h
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("CTHDBooking")
                            .whereGreaterThanOrEqualTo("thoiGianDatLich", startTimestamp1)
                            .whereLessThanOrEqualTo("thoiGianDatLich", endTimestamp1)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    int numberOfInvoices1 = task.getResult().size();
                                   // Toast.makeText(BookingActivity.this, "Số hóa đơn trong khoảng thời gian từ 8-10h: " + numberOfInvoices1, Toast.LENGTH_SHORT).show();
                                    if (numberOfInvoices1 > 3) {
                                        btnca1.setBackgroundColor(ContextCompat.getColor(BookingActivity.this, R.color.Red)); // Thay đổi màu sang đỏ
                                        btnca1.setEnabled(false);
                                    } else {
                                        btnca1.setBackgroundColor(ContextCompat.getColor(BookingActivity.this, R.color.bg_color_url)); // Màu mặc định
                                        btnca1.setEnabled(true);
                                    }



                                } else {
                                   // Toast.makeText(BookingActivity.this, "Lỗi khi truy vấn (8-10h): " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    // Thực hiện truy vấn Firestore cho khoảng thời gian từ 10h-12h
                    db.collection("CTHDBooking")
                            .whereGreaterThanOrEqualTo("thoiGianDatLich", startTimestamp2)
                            .whereLessThanOrEqualTo("thoiGianDatLich", endTimestamp2)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    int numberOfInvoices2 = task.getResult().size();
                                   // Toast.makeText(BookingActivity.this, "Số hóa đơn trong khoảng thời gian từ 10-12h: " + numberOfInvoices2, Toast.LENGTH_SHORT).show();

                                    if (numberOfInvoices2 > 3) {
                                        btnca2.setBackgroundColor(ContextCompat.getColor(BookingActivity.this, R.color.Red)); // Thay đổi màu sang đỏ
                                        btnca2.setEnabled(false);
                                    } else {
                                        btnca2.setBackgroundColor(ContextCompat.getColor(BookingActivity.this, R.color.bg_color_url)); // Màu mặc định
                                        btnca2.setEnabled(true);
                                    }

                                } else {
                                  //  Toast.makeText(BookingActivity.this, "Lỗi khi truy vấn (10-12h): " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    //ca3
                    db.collection("CTHDBooking")
                            .whereGreaterThanOrEqualTo("thoiGianDatLich", startTimestamp3)
                            .whereLessThanOrEqualTo("thoiGianDatLich", endTimestamp3)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    int numberOfInvoices3 = task.getResult().size();
                                   // Toast.makeText(BookingActivity.this, "Số hóa đơn trong khoảng thời gian từ 10-12h: " + numberOfInvoices3, Toast.LENGTH_SHORT).show();

                                    if (numberOfInvoices3 > 3) {
                                        btnca3.setBackgroundColor(ContextCompat.getColor(BookingActivity.this, R.color.Red)); // Thay đổi màu sang đỏ
                                        btnca3.setEnabled(false);
                                    } else {
                                        btnca3.setBackgroundColor(ContextCompat.getColor(BookingActivity.this, R.color.bg_color_url)); // Màu mặc định
                                        btnca3.setEnabled(true);
                                    }

                                } else {
                                   // Toast.makeText(BookingActivity.this, "Lỗi khi truy vấn (10-12h): " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    //ca 4
                    db.collection("CTHDBooking")
                            .whereGreaterThanOrEqualTo("thoiGianDatLich", startTimestamp4)
                            .whereLessThanOrEqualTo("thoiGianDatLich", endTimestamp4)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    int numberOfInvoices2 = task.getResult().size();
                                   // Toast.makeText(BookingActivity.this, "Số hóa đơn trong khoảng thời gian từ 10-12h: " + numberOfInvoices2, Toast.LENGTH_SHORT).show();

                                    if (numberOfInvoices2 > 3) {
                                        btnca4.setBackgroundColor(ContextCompat.getColor(BookingActivity.this, R.color.Red)); // Thay đổi màu sang đỏ
                                        btnca4.setEnabled(false);
                                    } else {
                                        btnca4.setBackgroundColor(ContextCompat.getColor(BookingActivity.this, R.color.bg_color_url)); // Màu mặc định
                                        btnca4.setEnabled(true);
                                    }

                                } else {
                                   // Toast.makeText(BookingActivity.this, "Lỗi khi truy vấn (10-12h): " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    //ca 5
                    db.collection("CTHDBooking")
                            .whereGreaterThanOrEqualTo("thoiGianDatLich", startTimestamp5)
                            .whereLessThanOrEqualTo("thoiGianDatLich", endTimestamp5)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    int numberOfInvoices2 = task.getResult().size();
                                    //Toast.makeText(BookingActivity.this, "Số hóa đơn trong khoảng thời gian từ 10-12h: " + numberOfInvoices2, Toast.LENGTH_SHORT).show();

                                    if (numberOfInvoices2 > 3) {
                                        btnca5.setBackgroundColor(ContextCompat.getColor(BookingActivity.this, R.color.Red)); // Thay đổi màu sang đỏ
                                        btnca5.setEnabled(false);
                                    } else {
                                        btnca5.setBackgroundColor(ContextCompat.getColor(BookingActivity.this, R.color.bg_color_url)); // Màu mặc định
                                        btnca5.setEnabled(true);
                                    }

                                } else {
                                  //  Toast.makeText(BookingActivity.this, "Lỗi khi truy vấn (10-12h): " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            });

                    //ca 6
                    db.collection("CTHDBooking")
                            .whereGreaterThanOrEqualTo("thoiGianDatLich", startTimestamp6)
                            .whereLessThanOrEqualTo("thoiGianDatLich", endTimestamp6)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    int numberOfInvoices2 = task.getResult().size();
                                    //Toast.makeText(BookingActivity.this, "Số hóa đơn trong khoảng thời gian từ 10-12h: " + numberOfInvoices2, Toast.LENGTH_SHORT).show();

                                    if (numberOfInvoices2 > 3) {
                                        btnca6.setBackgroundColor(ContextCompat.getColor(BookingActivity.this, R.color.Red)); // Thay đổi màu sang đỏ
                                        btnca6.setEnabled(false);
                                    } else {
                                        btnca6.setBackgroundColor(ContextCompat.getColor(BookingActivity.this, R.color.bg_color_url)); // Màu mặc định
                                        btnca6.setEnabled(true);
                                    }

                                } else {
                                  //  Toast.makeText(BookingActivity.this, "Lỗi khi truy vấn (10-12h): " + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }, selectedYear, selectedMonth, selectedDay);

            datePickerDialog.show();
        });


// Chọn giờ
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

                // Kiểm tra nếu giờ chọn là quá khứ
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
                long selectedTimeMillis = selectedCalendar.getTimeInMillis();
                long currentTimeMillis1 = System.currentTimeMillis();  // Lấy thời gian hiện tại

                // Kiểm tra nếu thời gian chọn là quá khứ
                if (selectedTimeMillis < currentTimeMillis1) {
                    Toast.makeText(BookingActivity.this, "Thời gian không hợp lệ. Vui lòng chọn thời gian trong tương lai.", Toast.LENGTH_SHORT).show();
                    txtChonGio.setText("");  // Xóa giờ đã chọn
                    return;
                }

                // Kiểm tra giờ trong khoảng 8h sáng đến 8h tối
                if (selectedHour < 8 || selectedHour > 20 || (selectedHour == 20 && selectedMinute > 0)) {
                    // Nếu giờ ngoài khoảng hợp lệ (08:00 - 20:00)
                    Toast.makeText(BookingActivity.this, "Giờ đặt phải trong khoảng từ 8h sáng đến 8h tối.", Toast.LENGTH_SHORT).show();
                    txtChonGio.setText("");  // Xóa giờ đã chọn
                } else {
                    txtChonGio.setText("Giờ đặt: " + selectedTime);
                }
            }, selectedHour, selectedMinute, true);

            timePickerDialog.show();
        });

        btnDatLich.setOnClickListener(v -> {

            if(rdoXacNhanOTP.isChecked()){
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

                // Lấy số điện thoại người dùng từ FirebaseUser
                String sdtNguoiDung = currentUser != null ? currentUser.getPhoneNumber() : null;

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
                    thoiGianDatLichCt = thoiGianDatLich;
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

                                        // Lấy thông tin dịch vụ từ Firestore
                                        db.collection("services").document(serviceId).get()
                                                .addOnSuccessListener(serviceDocument -> {
                                                    String tenDichVu = serviceDocument.getString("tenDichVu");


                                                    double giaDichVu = serviceDocument.getDouble("gia");
                                                    if(canNang>15){
                                                        giaDichVu += 50000;
                                                        Toast.makeText(this, "can nặng lớn hơn 15", Toast.LENGTH_SHORT).show();

                                                    }
                                                    // Truy vấn thông tin từ Firestore
                                                    double finalGiaDichVu = giaDichVu;
                                                    db.collection("User").document(userId).collection("Profile").get()
                                                            .addOnSuccessListener(querySnapshot -> {
                                                                if (!querySnapshot.isEmpty()) {
                                                                    // Lấy tài liệu đầu tiên trong collection "Profile"
                                                                    String sdt = querySnapshot.getDocuments().get(0).getString("sdt");

                                                                    // Lấy tên khách hàng từ FirebaseUser
                                                                    String tenKhachHang = currentUser.getDisplayName();

                                                                    // Tạo đối tượng CTHDBooking
                                                                    CTHDBooking cthdBooking = new CTHDBooking();
                                                                    cthdBooking.setIduser(userId);
                                                                    cthdBooking.setServiceIds(serviceIdList );
                                                                    cthdBooking.setIdBooking(idBooking);
                                                                    cthdBooking.setTenDichVu(tenDichVu);
                                                                    cthdBooking.setGiaDichVu(finalGiaDichVu);
                                                                    cthdBooking.setTenKhachHang(tenKhachHang);
                                                                    cthdBooking.setTenThuCung(tenThuCung);
                                                                    cthdBooking.setLoaiThuCung(loaiThuCung);
                                                                    cthdBooking.setCanNang(canNang);
                                                                    cthdBooking.setThoiGianDatLich(thoiGianDatLichCt);
                                                                    cthdBooking.setTrangThai("Chưa xác nhận");
                                                                    cthdBooking.setSdtNguoiDung(sdt); // Gắn số điện thoại vào CTHDBooking

                                                                    // Thêm CTHDBooking vào Firestore
                                                                    db.collection("CTHDBooking")
                                                                            .add(cthdBooking)
                                                                            .addOnSuccessListener(cthdDocRef -> {
                                                                                // Cập nhật ID do Firebase tự tạo vào CTHDBooking
                                                                                String idCthdBooking = cthdDocRef.getId();
                                                                                db.collection("CTHDBooking").document(idCthdBooking)
                                                                                        .update("idcthdbooking", idCthdBooking) // Gán idcthdbooking
                                                                                        .addOnSuccessListener(aVoid1 -> {
                                                                                            //   Toast.makeText(BookingActivity.this, "Đặt lịch thành công", Toast.LENGTH_SHORT).show();
                                                                                            finish();  // Quay lại màn hình trước
                                                                                        })
                                                                                        .addOnFailureListener(e -> {
                                                                                            // Toast.makeText(BookingActivity.this, "Cập nhật ID CTHDBooking thất bại", Toast.LENGTH_SHORT).show();
                                                                                        });
                                                                            })
                                                                            .addOnFailureListener(e -> {
                                                                                //  Toast.makeText(BookingActivity.this, "Lưu thông tin vào CTHDBooking thất bại", Toast.LENGTH_SHORT).show();
                                                                            });
                                                                } else {
                                                                    Log.d("Firestore", "Không tìm thấy thông tin Profile của userId: " + userId);
                                                                }
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Log.e("Firestore", "Lỗi khi truy vấn thông tin", e);
                                                            });

                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(BookingActivity.this, "Lỗi khi lấy thông tin dịch vụ", Toast.LENGTH_SHORT).show();
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(BookingActivity.this, "Cập nhật ID thất bại", Toast.LENGTH_SHORT).show();
                                    });
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(BookingActivity.this, "Đặt lịch thất bại", Toast.LENGTH_SHORT).show();
                        });
            }else{
                openDialog();
            }
        });

        btnback = findViewById(R.id.btnBack);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kết thúc Activity hiện tại và quay lại Activity trước đó
                finish();
            }
        });
    }

    private void openDialog() {
        // Inflate layout cho dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_xacnhan_otp, null);

        // Tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        // Tìm các view trong layout của dialog
        EditText edtOtp = dialogView.findViewById(R.id.edt_otp);
        Button btnGuiOTP = dialogView.findViewById(R.id.btn_guiOTP);
        Button btnXacthuc = dialogView.findViewById(R.id.btn_xacthuc);
        TextView txt_sdt_email_user = dialogView.findViewById(R.id.txt_sdt_email_user);
        ImageView imgCancel = dialogView.findViewById(R.id.img_cancel_dialog);
        TextView txtCountdown = dialogView.findViewById(R.id.txt_countdown); // Thêm TextView để hiển thị đếm ngược

        // Tạo dialog và hiển thị
        AlertDialog dialog = builder.create();
        dialog.show();

        // Lấy email từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("user_email", "Email không khả dụng");

        // Hiển thị email lên TextView
        txt_sdt_email_user.setText(email);
        OtpSender otpSender = new OtpSender();

        // Tạo bộ đếm ngược 30 giây
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Hiển thị thời gian còn lại
                txtCountdown.setText("Bạn có thể gửi lại sau: " + millisUntilFinished / 1000 + " giây");
            }

            @Override
            public void onFinish() {
                // Khi đếm ngược hoàn thành, thay đổi nút "Gửi" thành màu xanh và kích hoạt lại
                btnGuiOTP.setEnabled(true);
                btnGuiOTP.setBackground(getDrawable(R.drawable.background_blue));
                txtCountdown.setText("Có thể gửi lại OTP");

            }
        };

        // Sự kiện khi bấm nút "Gửi"
        btnGuiOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    // Vô hiệu hóa nút gửi và thay đổi màu sắc

                    txtCountdown.setVisibility(View.VISIBLE);
                    btnGuiOTP.setEnabled(false);
                    btnGuiOTP.setBackgroundColor(getResources().getColor(android.R.color.darker_gray)); // Màu xám

                    // Bắt đầu đếm ngược
                    countDownTimer.start();

                    // Gửi OTP qua Firebase Functions
                    otpSender.sendOtp(email, BookingActivity.this, new OtpSender.Callback() {
                        @Override
                        public void onSuccess(int otp) {
                            Toast.makeText(BookingActivity.this, "OTP đã gửi đến email: " + email, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(BookingActivity.this, "Gửi OTP thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(BookingActivity.this, "Email không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnXacthuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = edtOtp.getText().toString().trim();
                if (otp.isEmpty()) {
                    Toast.makeText(BookingActivity.this, "Vui lòng nhập mã OTP!", Toast.LENGTH_SHORT).show();
                } else {
                    // Kiểm tra OTP
                    if (otpSender.verifyOtp(BookingActivity.this, Integer.parseInt(otp))) {
                        Log.d(TAG, "OTP xác nhận thành công");
                        rdoXacNhanOTP.setChecked(true);
                        rdoXacNhanOTP.setText("Đã xác thực danh tính");
                        // Thay đổi màu của nút RadioButton
                        rdoXacNhanOTP.setButtonTintList(ContextCompat.getColorStateList(BookingActivity.this, R.color.purple_700));
                        rdoXacNhanOTP.setTextColor(ContextCompat.getColor(BookingActivity.this, R.color.purple_700));

                        otpSender.clearOtp(BookingActivity.this);
                        dialog.dismiss();
                        Toast.makeText(BookingActivity.this, "OTP xác nhận thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(TAG, "OTP không chính xác");
                        Toast.makeText(BookingActivity.this, "Mã OTP không chính xác. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        // Sự kiện khi bấm nút đóng dialog
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
