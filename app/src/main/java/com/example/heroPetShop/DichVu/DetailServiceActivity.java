package com.example.heroPetShop.DichVu;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.heroPetShop.Booking.BookingActivity;
import com.example.heroPetShop.MainActivity;
import com.example.heroPetShop.R;
import com.example.heroPetShop.View.Admin.AdminHomeActivity;
import com.example.heroPetShop.sildeshow.ManageImagesActivity;

public class DetailServiceActivity extends AppCompatActivity {
    private TextView txtServiceName, txtDescription, txtPrice, txtTime, txtStatus;
    private Button btnDatLich;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_service);

        txtServiceName = findViewById(R.id.txtServiceName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        txtTime = findViewById(R.id.txtTime);
        btnDatLich =findViewById(R.id.btnDatLich);
        findViewById(R.id.img_back_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailServiceActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });

        // Nhận dữ liệu từ Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            txtServiceName.setText(bundle.getString("tenDichVu"));
            txtDescription.setText(bundle.getString("moTa"));
            txtPrice.setText(String.valueOf(bundle.getDouble("gia")) + " VNĐ");
            txtTime.setText(bundle.getLong("thoiGian") + " phút");

        }

        // Xử lý click nút Đặt Lịch
        btnDatLich.setOnClickListener(v -> {
            Intent intent = new Intent(DetailServiceActivity.this, BookingActivity.class);
            intent.putExtra("tenDichVu", txtServiceName.getText().toString());
            startActivity(intent);
        });
    }
}
