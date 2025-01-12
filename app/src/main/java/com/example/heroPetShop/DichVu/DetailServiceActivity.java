package com.example.heroPetShop.DichVu;



import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.heroPetShop.Booking.BookingActivity;
import com.example.heroPetShop.Booking.BookingActivity1;
import com.example.heroPetShop.MainActivity;
import com.example.heroPetShop.Presenter.GioHangPresenter;
import com.example.heroPetShop.R;
import com.example.heroPetShop.View.Admin.AdminHomeActivity;
import com.example.heroPetShop.View.CommentActivity;
import com.example.heroPetShop.View.DetailSPActivity;
import com.example.heroPetShop.View.TaoComboDichVu;
import com.example.heroPetShop.sildeshow.ManageImagesActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class DetailServiceActivity extends AppCompatActivity {
    private TextView txtDescription, txtPrice, txtTime, txtStatus;
    private Button btnDatLich,btn_add_to_cart;
    private FirebaseAuth mAuth;

    private Toolbar toolbar;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String userId;

    private String serviceId;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private Service service;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_service);
        txtDescription = findViewById(R.id.txtDescription);
        txtPrice = findViewById(R.id.txtPrice);
        txtTime = findViewById(R.id.txtTime);
        image = findViewById(R.id.imagedichvuDV);

        Intent intent1 = getIntent();
        serviceId = intent1.getStringExtra("serviceId");

        // Nhận dữ liệu từ Intent (Bundle)
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Tạo đối tượng Service từ dữ liệu trong bundle
            service = new Service();
            service.setTenDichVu(bundle.getString("tenDichVu"));
            service.setMoTa(bundle.getString("moTa"));
            service.setGia(bundle.getDouble("gia"));
            service.setThoiGian(bundle.getLong("thoiGian"));
            service.setImg(bundle.getString("img"));

            // Cập nhật UI với thông tin từ service
            txtDescription.setText(service.getMoTa());
            double gia = service.getGia();
            String formattedPrice = new DecimalFormat("#,###").format(gia);
            txtPrice.setText(formattedPrice);
            txtTime.setText(service.getThoiGian() + " phút");


            String imgUrl = service.getImg();
            Log.d("DetailServiceActivity", "URL ảnh: " + imgUrl);  // In ra giá trị URL ảnh
            if (imgUrl != null && !imgUrl.isEmpty()) {
                Picasso.get().load(imgUrl).into(image);
            } else {
                Log.e("DetailServiceActivity", "Ảnh không hợp lệ, URL là null hoặc rỗng");
            }


            // Cập nhật CollapsingToolbarLayout
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(service != null ? service.getTenDichVu() : "Dịch vụ");



//         Xử lý click nút Đặt Lịch
        btnDatLich = findViewById(R.id.btn_datlich2);
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

                            if (sdt.isEmpty() || tenKhachHang.isEmpty()) {
                                Toast.makeText(this, "Bạn cần cập nhật thông tin cá nhân trước khi đặt lịch", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(DetailServiceActivity.this, BookingActivity.class);
                                intent.putExtra("tenDichVu", service.getTenDichVu());
                                intent.putExtra("giaDichVu", service.getGia());
                                intent.putExtra("image", service.getImg());
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
            btn_add_to_cart = findViewById(R.id.btn_add_to_cart);
            btn_add_to_cart.setOnClickListener(v -> {
                Intent intent = new Intent(DetailServiceActivity.this, TaoComboDichVu.class);
                startActivity(intent);
            });
    }}




}
