package com.example.heroPetShop.DichVu;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.heroPetShop.Booking.BookingActivity;
import com.example.heroPetShop.MainActivity;
import com.example.heroPetShop.R;
import com.example.heroPetShop.View.Admin.AdminHomeActivity;
import com.example.heroPetShop.sildeshow.ManageImagesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailServiceActivity extends AppCompatActivity {
    private TextView txtServiceName, txtDescription, txtPrice, txtTime, txtStatus;
    private Button btnDatLich;
    private FirebaseAuth mAuth;
    private String userId;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    ImageView image;
    String serviceId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_service);

        txtServiceName = findViewById(R.id.txtServiceName);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        txtTime = findViewById(R.id.txtTime);
        btnDatLich =findViewById(R.id.btnDatLich);
        image = findViewById(R.id.imagedichvu);
        findViewById(R.id.img_back_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailServiceActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });


        Intent intent1 = getIntent();
        serviceId = intent1.getStringExtra("serviceId");

        // Nhận dữ liệu từ Intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            txtServiceName.setText(bundle.getString("tenDichVu"));
            txtDescription.setText(bundle.getString("moTa"));
            txtPrice.setText(String.valueOf(bundle.getDouble("gia")) + " VNĐ");
            txtTime.setText(bundle.getLong("thoiGian") + " phút");

        }

        if ("1733168593657".equals(serviceId)) {
            // Gán hình ảnh đặc biệt cho dịch vụ có serviceId = "abc"
            image.setImageResource(R.drawable.image_tam_rua);  // Gán ảnh đặc biệt
        } else  if ("1733305192175".equals(serviceId)){
            // Gán hình ảnh mặc định
            image.setImageResource(R.drawable.image_tia_long);
        }
        else  if ("1733305098918".equals(serviceId)){
            // Gán hình ảnh mặc định
            image.setImageResource(R.drawable.image_dieu_tri);
        }
        else {
            image.setImageResource(R.drawable.image_tia_long);
        }





        // Xử lý click nút Đặt Lịch
        btnDatLich.setOnClickListener(v -> {
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                userId = currentUser.getUid(); // Lấy ID người dùng
            } else {
                Toast.makeText(DetailServiceActivity.this, "Bạn cần đăng nhập", Toast.LENGTH_SHORT).show();
                return;
            }

            db.collection("User").document(userId).collection("Profile").get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            // Lấy tài liệu đầu tiên trong collection "Profile"
                            String sdt = querySnapshot.getDocuments().get(0).getString("sdt");


                            // Lấy tên khách hàng từ FirebaseUser
                            String tenKhachHang = currentUser.getDisplayName();

                            if(sdt.isEmpty()||tenKhachHang.isEmpty()){

                                Toast.makeText(this, "Bạn cần cập nhập thông tin cá nhân trước khi đặt lịch", Toast.LENGTH_SHORT).show();

                            }else {
                                Intent intent = new Intent(DetailServiceActivity.this, BookingActivity.class);
                                intent.putExtra("tenDichVu", txtServiceName.getText().toString());
                                String serviceId = getIntent().getStringExtra("serviceId");
                                intent.putExtra("serviceId", serviceId); // Gửi ID dịch vụ
                                startActivity(intent);
                            }


                        } else {
                            Log.d("Firestore", "Không tìm thấy thông tin Profile của userId: " + userId);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Lỗi khi truy vấn thông tin", e);
                    });


        });
    }
}
