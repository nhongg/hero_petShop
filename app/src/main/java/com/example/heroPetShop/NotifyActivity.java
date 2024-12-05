package com.example.heroPetShop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotifyActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Toolbar toolbar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView txtdiachi, txtsdt, txtnoidung;
    private Button btnOpenLink; // Khai báo button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify); // Chỉnh lại tên layout cho Activity

        // Ánh xạ các thành phần giao diện
        toolbar = findViewById(R.id.toolbar);
        txtdiachi = findViewById(R.id.txtdiachi);
        txtsdt = findViewById(R.id.txtsdt);
        txtnoidung = findViewById(R.id.txtnoidung);
        btnOpenLink = findViewById(R.id.btnMap); // Ánh xạ button

        // Thiết lập toolbar
        setSupportActionBar(toolbar);

        // Lấy dữ liệu từ Firestore và cập nhật giao diện
        db.collection("ThongTinCuaHang").document("FQaw4inRmOCfsdotmL5W")
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        txtdiachi.setText("Địa chỉ : " + documentSnapshot.getString("diachi"));
                        txtsdt.setText("Liên hệ : " + documentSnapshot.getString("sdt"));
                        txtnoidung.setText("Nội Dung : " + documentSnapshot.getString("noidung"));
                    }
                });

        // Xử lý sự kiện nhấn nút để mở link
        btnOpenLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mở link khi người dùng nhấn nút
                String url = "https://maps.app.goo.gl/8yvPjzSGSozzV1bC8";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });




//        // Google Maps
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(this);
//        }
//
//        Log.d("state", "onCreate");
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        db.collection("ThongTinCuaHang").document("FQaw4inRmOCfsdotmL5W").get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//                        txtdiachi.setText("Địa chỉ : " + documentSnapshot.getString("diachi"));
//                        txtsdt.setText("Liên hệ : " + documentSnapshot.getString("sdt"));
//                        txtnoidung.setText("Nội Dung : " + documentSnapshot.getString("noidung"));
//                        // Đọc vị trí Google Map từ Firestore và thiết lập vị trí trên bản đồ
//                        // Gợi ý: lấy vị trí và zoom vào vị trí đó trên bản đồ
//                    }
//                });
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        Log.d("state", "onSave");
//    }
}

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}
