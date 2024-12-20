package com.example.heroPetShop.Booking;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookingActivity1 extends AppCompatActivity {
    private EditText edtTenThuCung, edtCanNang, edtLoaiThuCung;
    private TextView txtChonNgay, txtChonGio;
    private Button btnDatLich;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;
    private String selectedDate, selectedTime;
    private String tenDichVu; // Dịch vụ từ DetailServiceActivity
    private double giaDichvu;
    private  String id;
    private String userId; // ID người dùng
    Date thoiGianDatLichCt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        edtTenThuCung = findViewById(R.id.edtTenThuCung);
        edtCanNang = findViewById(R.id.edtCanNang);
        TextView weightWarningText = findViewById(R.id.weightWarningText);

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
            Toast.makeText(BookingActivity1.this, "Bạn cần đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nhận dữ liệu từ DetailServiceActivity (Tên dịch vụ)
        tenDichVu = getIntent().getStringExtra("ten");

        giaDichvu= getIntent().getDoubleExtra("gia",0.0);
        id = getIntent().getStringExtra("id");
        //String serviceId = getIntent().getStringExtra("serviceId");
        String[] serviceIdArray = id.split(",\\s*");
        List<String> serviceIdList = new ArrayList<>(Arrays.asList(serviceIdArray));



        // Lấy thời gian hiện tại
        Calendar currentCalendar = Calendar.getInstance();
        long currentTimeMillis = currentCalendar.getTimeInMillis();

// Chọn ngày
        txtChonNgay.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            selectedYear = calendar.get(Calendar.YEAR);
            selectedMonth = calendar.get(Calendar.MONTH);
            selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    BookingActivity1.this, (view, year, month, dayOfMonth) -> {
                selectedYear = year;
                selectedMonth = month;
                selectedDay = dayOfMonth;
                selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;

                // Kiểm tra nếu ngày chọn là quá khứ
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
                long selectedTimeMillis = selectedCalendar.getTimeInMillis();

                if (selectedTimeMillis < currentTimeMillis) {
                    // Nếu thời gian chọn là quá khứ, yêu cầu chọn lại
                    Toast.makeText(BookingActivity1.this, "Thời gian không hợp lệ. Vui lòng chọn thời gian trong tương lai.", Toast.LENGTH_SHORT).show();
                    txtChonNgay.setText("");  // Xóa ngày đã chọn
                } else {
                    txtChonNgay.setText("Ngày đặt: " + selectedDate);
                }
            }, selectedYear, selectedMonth, selectedDay);

            datePickerDialog.show();
        });

// Chọn giờ
        txtChonGio.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
            selectedMinute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    BookingActivity1.this, (view, hourOfDay, minute) -> {
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
                    Toast.makeText(BookingActivity1.this, "Thời gian không hợp lệ. Vui lòng chọn thời gian trong tương lai.", Toast.LENGTH_SHORT).show();
                    txtChonGio.setText("");  // Xóa giờ đã chọn
                    return;
                }

                // Kiểm tra giờ trong khoảng 8h sáng đến 8h tối
                if (selectedHour < 8 || selectedHour > 20 || (selectedHour == 20 && selectedMinute > 0)) {
                    // Nếu giờ ngoài khoảng hợp lệ (08:00 - 20:00)
                    Toast.makeText(BookingActivity1.this, "Giờ đặt phải trong khoảng từ 8h sáng đến 8h tối.", Toast.LENGTH_SHORT).show();
                    txtChonGio.setText("");  // Xóa giờ đã chọn
                } else {
                    txtChonGio.setText("Giờ đặt: " + selectedTime);
                }
            }, selectedHour, selectedMinute, true);

            timePickerDialog.show();
        });





        btnDatLich.setOnClickListener(v -> {
            String tenThuCung = edtTenThuCung.getText().toString();
            String canNangStr = edtCanNang.getText().toString();
            String loaiThuCung = edtLoaiThuCung.getText().toString();

            // Kiểm tra dữ liệu
            if (tenThuCung.isEmpty() || canNangStr.isEmpty() || loaiThuCung.isEmpty() ||
                    selectedDate == null || selectedTime == null) {
                Toast.makeText(BookingActivity1.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double canNang = Double.parseDouble(canNangStr);
            if(canNang>15){
                giaDichvu += 50000;
                Toast.makeText(this, "can nặng lớn hơn 15", Toast.LENGTH_SHORT).show();

            }
            // Truy vấn thông tin từ Firestore
            double finalGiaDichVu = giaDichvu;


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
                                    Toast.makeText(BookingActivity1.this, "Đặt lịch thành công", Toast.LENGTH_SHORT).show();

                                    // Lấy thông tin dịch vụ từ Firestore
                                         // Truy vấn thông tin từ Firestore
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
                                                                cthdBooking.setServiceIds(serviceIdList);
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
                                                                                       // Toast.makeText(BookingActivity1.this, "Lưu thông tin vào CTHDBooking thành công", Toast.LENGTH_SHORT).show();
                                                                                        finish();  // Quay lại màn hình trước
                                                                                    })
                                                                                    .addOnFailureListener(e -> {
                                                                                     //   Toast.makeText(BookingActivity1.this, "Cập nhật ID CTHDBooking thất bại", Toast.LENGTH_SHORT).show();
                                                                                    });
                                                                        })
                                                                        .addOnFailureListener(e -> {
                                                                          //  Toast.makeText(BookingActivity1.this, "Lưu thông tin vào CTHDBooking thất bại", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(BookingActivity1.this, "Cập nhật ID thất bại", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(BookingActivity1.this, "Đặt lịch thất bại", Toast.LENGTH_SHORT).show();
                    });
        });






    }
}
