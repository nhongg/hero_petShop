package com.example.heroPetShop.Booking;



import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.Adapter.CTHDAdapter;
import com.example.heroPetShop.Booking.CTHDBooking;
import com.example.heroPetShop.DichVu.Service;
import com.example.heroPetShop.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BookingDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerViewServices;
    private CTHDAdapter cthdAdapter;

    private TextView txtHuy, txtGiaDichVu, txtTenKhachHang, txtTenThuCung, txtLoaiThuCung, txtCanNang, txtThoiGianDatLich, txtTrangThai, txtSdtNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail); // Layout chi tiết, bạn cần tạo layout này

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
        recyclerViewServices = findViewById(R.id.rvDichVu);


        // Cài đặt LayoutManager
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));

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

        // Hiển thị thông tin chi tiết
        if (booking != null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            List<Service> serviceList = new ArrayList<>();
            List<String> serviceIds = booking.getServiceIds();

            for (String serviceId : serviceIds) {
                db.collection("services")
                        .document(serviceId)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                // Lấy dữ liệu từ Firestore
                                String tenDichVu = documentSnapshot.getString("tenDichVu");
                                String moTa = documentSnapshot.getString("moTa");
                                Double gia = documentSnapshot.getDouble("gia");
                                Long thoiGian = documentSnapshot.getLong("thoiGian");
                                Boolean hoatDong = documentSnapshot.getBoolean("hoatDong");
                                String img = documentSnapshot.getString("img");

                                // Khởi tạo đối tượng Service
                                Service service = new Service(serviceId, tenDichVu, moTa, gia != null ? gia : 0.0, thoiGian != null ? thoiGian : 0, hoatDong != null ? hoatDong : false);
                                service.setImg(img);

                                // Thêm vào danh sách
                                serviceList.add(service);

                                // Nếu đã tải xong toàn bộ danh sách
                                if (serviceList.size() == serviceIds.size()) {
                                    // Thiết lập adapter cho RecyclerView
                                    cthdAdapter = new CTHDAdapter(serviceList);
                                    recyclerViewServices.setAdapter(cthdAdapter);
                                }
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("FirebaseError", "Error loading service: " + e.getMessage());
                        });
            }
            int soNguyen = (int)booking.getGiaDichVu();
            int canNang = (int)booking.getCanNang();

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


        }}
    // Định dạng thời gian
    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(timestamp);
    }
    }


