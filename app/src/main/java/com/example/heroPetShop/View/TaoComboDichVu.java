package com.example.heroPetShop.View;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.DichVu.ComBoServiceAdapter;
import com.example.heroPetShop.DichVu.Service;
import com.example.heroPetShop.DichVu.ServiceAdapter_KH;
import com.example.heroPetShop.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taocombo_dichvu);


        setupDichVu();
    }



    private void setupDichVu(){
        rcv_dv = findViewById(R.id.recyclerView);
//        rcv_dv.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(TaoComboDichVu.this, LinearLayoutManager.HORIZONTAL, false);
        rcv_dv.setLayoutManager(layoutManager);
        serviceAdapter = new ComBoServiceAdapter(serviceList, TaoComboDichVu.this);
        rcv_dv.setAdapter(serviceAdapter);
        loadServicesFromFirestore();
    }
    // Hàm lấy danh sách dịch vụ từ Firestore
    private void loadServicesFromFirestore() {
        CollectionReference servicesRef = dbdv.collection("services");
        servicesRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Service service = document.toObject(Service.class);
                        serviceList.add(service);
                    }
                    serviceAdapter.notifyDataSetChanged(); // Cập nhật adapter khi dữ liệu đã được tải
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi khi lấy dữ liệu từ Firestore
                });
    }
}
