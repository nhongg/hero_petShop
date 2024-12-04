package com.example.heroPetShop.Booking;



import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heroPetShop.Booking.CTHDBooking;
import com.example.heroPetShop.R;

import java.text.SimpleDateFormat;

public class BookingDetailActivity extends AppCompatActivity {

    private TextView txtTenDichVu, txtGiaDichVu, txtTenKhachHang, txtTenThuCung, txtLoaiThuCung, txtCanNang, txtThoiGianDatLich, txtTrangThai, txtSdtNguoiDung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail); // Layout chi tiết, bạn cần tạo layout này

        // Ánh xạ các TextView
        txtTenDichVu = findViewById(R.id.txtTenDichVu);
        txtGiaDichVu = findViewById(R.id.txtGiaDichVu);
        txtTenKhachHang = findViewById(R.id.txtTenKhachHang);
        txtTenThuCung = findViewById(R.id.txtTenThuCung);
        txtLoaiThuCung = findViewById(R.id.txtLoaiThuCung);
        txtCanNang = findViewById(R.id.txtCanNang);
        txtThoiGianDatLich = findViewById(R.id.txtThoiGianDatLich);
        txtTrangThai = findViewById(R.id.txtTrangThai);
        txtSdtNguoiDung = findViewById(R.id.txtSdtNguoiDung);

        // Nhận dữ liệu từ Intent
        CTHDBooking booking = (CTHDBooking) getIntent().getSerializableExtra("bookingDetail");

        // Hiển thị thông tin chi tiết
        if (booking != null) {
            txtTenDichVu.setText(booking.getTenDichVu());
            txtGiaDichVu.setText(String.valueOf(booking.getGiaDichVu()));
            txtTenKhachHang.setText(booking.getTenKhachHang());
            txtTenThuCung.setText(booking.getTenThuCung());
            txtLoaiThuCung.setText(booking.getLoaiThuCung());
            txtCanNang.setText(String.valueOf(booking.getCanNang()));
            txtThoiGianDatLich.setText(formatDate(booking.getThoiGianDatLich().getTime()));
            txtTrangThai.setText(booking.getTrangThai());
            txtSdtNguoiDung.setText(booking.getSdtNguoiDung());
        }
    }

    // Định dạng thời gian
    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(timestamp);
    }
}
