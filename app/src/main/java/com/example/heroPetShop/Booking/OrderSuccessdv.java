package com.example.heroPetShop.Booking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.heroPetShop.MainActivity;
import com.example.heroPetShop.R;
public class OrderSuccessdv extends AppCompatActivity {
    private TextView tvIDHoadon, tvDateHoadon, tvSanphamHoadon,
            tvNameHoadon, tvDiachiHoadon, tvSDTHoadon, tvPhuongthucHoadon, tvGhichuHoadon, tvTongtienHoadon;
    private Button btnHoanthanhHoadon, btnXuatPDFHoadon;

    private String idhoadon, ngaydat, hoten, diachi, sdt, phuongthuc, ghichu, sanpham, tienthanhtoan;
    private Bitmap bmp,scalebmp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_dv);

        InitWidget();
        Init();
        Event();
    }

    private void Event() {

        btnHoanthanhHoadon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderSuccessdv.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }



    private void Init() {

        Intent intent = getIntent();
        idhoadon = intent.getStringExtra("idhoadon");
        ngaydat = intent.getStringExtra("ngaydat");
        hoten = intent.getStringExtra("hoten");
        sdt = intent.getStringExtra("sdt");
        phuongthuc = intent.getStringExtra("phuongthuc");
        sanpham = intent.getStringExtra("tendv");
        tienthanhtoan = intent.getStringExtra("tongtien");


//        intent.putExtra("idhoadon", idBooking);
//        intent.putExtra("hoten", tenKhachHang);
//        intent.putExtra("ngaydat", thoiGianDatLichCt);
//        intent.putExtra("sdt", sdt);
//        intent.putExtra("tendv", tenDichVu);
//        intent.putExtra("tongtien", finalGiaDichVu);

        tvIDHoadon.setText(idhoadon);
        tvDateHoadon.setText(ngaydat);
        tvNameHoadon.setText(hoten);
        tvSDTHoadon.setText(sdt);
        tvPhuongthucHoadon.setText(phuongthuc);
        tvSanphamHoadon.setText(sanpham);
        tvTongtienHoadon.setText(tienthanhtoan);


        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);


        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pizzahead);
        scalebmp = Bitmap.createScaledBitmap(bmp, 1200, 518, false);

    }

    private void InitWidget() {
        tvIDHoadon = findViewById(R.id.tv_id_hoadondv);
        tvDateHoadon = findViewById(R.id.tv_date_hoadondv);
        tvNameHoadon = findViewById(R.id.tv_name_hoadondv);
        tvSDTHoadon = findViewById(R.id.tv_sdt_hoadondv);
        tvPhuongthucHoadon = findViewById(R.id.tv_phuongthuc_hoadondv);
        tvSanphamHoadon = findViewById(R.id.tv_sanpham_hoadondv);
        tvTongtienHoadon = findViewById(R.id.tv_tongtien_hoadondv);
        btnHoanthanhHoadon = findViewById(R.id.btn_hoanthanh_hoadondv);
    }
}