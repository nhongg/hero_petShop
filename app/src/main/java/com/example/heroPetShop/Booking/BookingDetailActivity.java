package com.example.heroPetShop.Booking;



import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.Adapter.CTHDAdapter;
import com.example.heroPetShop.Booking.CTHDBooking;
import com.example.heroPetShop.DichVu.Service;
import com.example.heroPetShop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BookingDetailActivity extends AppCompatActivity {
    private List<CTBooking> serviceList = new ArrayList<>();
    private FirebaseFirestore db;
    private RecyclerView recyclerViewServices;
    private CTHDAdapter cthdAdapter;

    private ImageButton btn_back;
    private String idcthdbooking;
    private TextView txtHuy, txtGiaDichVu, txtTenKhachHang, txtTenThuCung, txtLoaiThuCung, txtCanNang, txtThoiGianDatLich, txtTrangThai, txtSdtNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail); // Layout chi tiết, bạn cần tạo layout này

        db = FirebaseFirestore.getInstance(); // Khởi tạo Firestore

        // Ánh xạ các TextView
        txtGiaDichVu = findViewById(R.id.txtGiaDichVu);
        txtTenKhachHang = findViewById(R.id.txtTenKhachHang);
        txtTenThuCung = findViewById(R.id.txtTenThuCung);
        txtLoaiThuCung = findViewById(R.id.txtLoaiThuCung);
        txtCanNang = findViewById(R.id.txtCanNang);
        txtThoiGianDatLich = findViewById(R.id.txtThoiGianDatLich);
        txtTrangThai = findViewById(R.id.txtTrangThai);
        txtSdtNguoiDung = findViewById(R.id.txtSdtNguoiDung);
        txtHuy = findViewById(R.id.txtHuy);
        btn_back = findViewById(R.id.btn_back);
        recyclerViewServices = findViewById(R.id.rvDichVu);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        TextView txtHuy = findViewById(R.id.txtHuy);
        TextView lblLyDoHuy = findViewById(R.id.lblLyDoHuy); // TextView tiêu đề "Lý do hủy"
        // Nhận dữ liệu từ Intent
        CTHDBooking booking = (CTHDBooking) getIntent().getSerializableExtra("bookingDetail");

        if (booking != null) {
            // Kiểm tra lý do hủy
            if (booking.getLyDoHuy() != null && !booking.getLyDoHuy().isEmpty()) {
                txtHuy.setText(booking.getLyDoHuy());
                txtHuy.setVisibility(View.VISIBLE); // Hiển thị lý do hủy
                lblLyDoHuy.setVisibility(View.VISIBLE); // Hiển thị tiêu đề "Lý do hủy"
            } else {
                txtHuy.setVisibility(View.GONE); // Ẩn lý do hủy
                lblLyDoHuy.setVisibility(View.GONE); // Ẩn tiêu đề "Lý do hủy"
            }
        }

        int soNguyen = (int)booking.getGiaDichVu();
        int canNang = (int)booking.getCanNang();

        idcthdbooking = booking.getIdcthdbooking();
        // Hiển thị thông tin khác từ booking
        txtGiaDichVu.setText(String.valueOf(soNguyen));
        txtTenKhachHang.setText(booking.getTenKhachHang());
        txtTenThuCung.setText(booking.getTenThuCung());
        txtLoaiThuCung.setText(booking.getLoaiThuCung());
        txtCanNang.setText(String.valueOf(canNang));
        txtThoiGianDatLich.setText(formatDate(booking.getThoiGianDatLich().getTime()));
        txtTrangThai.setText(booking.getTrangThai());
        txtSdtNguoiDung.setText(booking.getSdtNguoiDung());
        txtHuy.setText(String.valueOf(booking.getLyDoHuy()));

        // Cài đặt LayoutManager
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));
        cthdAdapter = new CTHDAdapter(getApplicationContext(),serviceList);
        recyclerViewServices.setAdapter(cthdAdapter);

        // Hiển thị thông tin các dịch vụ sử dụng
        getServices();
    }
    // Định dạng thời gian
    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(timestamp);
    }

    public void getServices() {
        // Lọc booking theo userId
        db.collection("CTBooking")
                .whereEqualTo("idcthdbooking", idcthdbooking) // Lọc theo idcthdbooking
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                CTBooking service = document.toObject(CTBooking.class);
                                serviceList.add(service);
                            }
                            cthdAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }
    }


