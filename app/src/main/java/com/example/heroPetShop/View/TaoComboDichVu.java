package com.example.heroPetShop.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.Booking.BookingActivity1;
import com.example.heroPetShop.DichVu.ComBoServiceAdapter;
import com.example.heroPetShop.DichVu.DetailServiceActivity;
import com.example.heroPetShop.DichVu.Service;
import com.example.heroPetShop.MainActivity;
import com.example.heroPetShop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TaoComboDichVu extends AppCompatActivity {

    private RecyclerView rcv_dv;
    private ComBoServiceAdapter serviceAdapter;
    private List<Service> serviceList = new ArrayList<>();
    private FirebaseFirestore dbdv = FirebaseFirestore.getInstance();


    private FirebaseAuth mAuth;
    private String userId;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    private String accumulatedNames = "";
    private int totalPrice = 0;

    TextView tTenDv;
    TextView tGiaDv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taocombo_dichvu);

        setupDichVu();

        tGiaDv = findViewById(R.id.tvTotalPrice);
        tTenDv = findViewById(R.id.tvSelectedServices);
        findViewById(R.id.img_back_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TaoComboDichVu.this, MainActivity.class);
                startActivity(intent);
            }

        });
        // Tìm Button và xử lý sự kiện click
        Button btnSendToBooking = findViewById(R.id.btnSendToBooking);
        btnSendToBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy giá trị từ TextView khi người dùng nhấn nút
                String ten = tTenDv.getText().toString();  // Lấy tên dịch vụ đã chọn
                double gia = Double.parseDouble(tGiaDv.getText().toString());  // Lấy tổng giá dịch vụ

                if (ten.isEmpty() || gia <= 0 || Double.isNaN(gia) || Double.isInfinite(gia)) {
                    // Xử lý khi tên dịch vụ hoặc giá không hợp lệ
                    Toast.makeText(TaoComboDichVu.this, "Vui lòng chọn ít nhất một dịch vụ khi tạo combo của chúng tôi", Toast.LENGTH_SHORT).show();
                } else {


                    mAuth = FirebaseAuth.getInstance();
                    db = FirebaseFirestore.getInstance();
                    currentUser = mAuth.getCurrentUser();
                    if (currentUser != null) {
                        userId = currentUser.getUid(); // Lấy ID người dùng
                    } else {
                        Toast.makeText(TaoComboDichVu.this, "Bạn cần đăng nhập", Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(TaoComboDichVu.this, "Bạn cần cập nhập thông tin cá nhân trước khi đặt lịch", Toast.LENGTH_SHORT).show();

                                    }else {
                                        Intent intent = new Intent(TaoComboDichVu.this, BookingActivity1.class);

                                        // Đưa dữ liệu vào Intent
                                        intent.putExtra("ten", ten);
                                        intent.putExtra("gia", gia);

                                        // Mở BookingActivity1
                                        startActivity(intent);
                                    }


                                } else {
                                    Log.d("Firestore", "Không tìm thấy thông tin Profile của userId: " + userId);
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firestore", "Lỗi khi truy vấn thông tin", e);
                            });



                }
                }
        });
    }

    private void setupDichVu() {
        rcv_dv = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(TaoComboDichVu.this, LinearLayoutManager.HORIZONTAL, false);
        rcv_dv.setLayoutManager(layoutManager);

        serviceAdapter = new ComBoServiceAdapter(serviceList, TaoComboDichVu.this);
        rcv_dv.setAdapter(serviceAdapter);

        loadServicesFromFirestore();
    }

    private void loadServicesFromFirestore() {
        CollectionReference servicesRef = dbdv.collection("services");
        servicesRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Service service = document.toObject(Service.class);
                        serviceList.add(service);
                    }
                    serviceAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi tải dữ liệu
                });
    }

    // Phương thức để cập nhật TextView khi dịch vụ được chọn
    public void updateUI() {
        tTenDv.setText(accumulatedNames);  // Cập nhật tên dịch vụ đã chọn
        tGiaDv.setText(String.valueOf(totalPrice));  // Cập nhật tổng giá dịch vụ
    }

    // Hàm này sẽ được gọi khi có sự thay đổi trong lựa chọn dịch vụ
    public void onServiceSelected(String serviceName, int servicePrice) {
        accumulatedNames += serviceName + " ";  // Thêm tên dịch vụ vào chuỗi
        totalPrice += servicePrice;  // Cộng giá dịch vụ vào tổng

        // Cập nhật lại giao diện người dùng
        updateUI();
    }
}
