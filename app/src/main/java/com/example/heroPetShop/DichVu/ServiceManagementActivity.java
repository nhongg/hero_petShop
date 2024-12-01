package com.example.heroPetShop.DichVu;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.MainActivity;
import com.example.heroPetShop.R;
import com.example.heroPetShop.View.Admin.AdminHomeActivity;
import com.example.heroPetShop.sildeshow.ManageImagesActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ServiceManagementActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ServiceAdapter adapter;
    private List<Service> serviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        // Khởi tạo Firestore và RecyclerView
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewdv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        serviceList = new ArrayList<>();
        adapter = new ServiceAdapter(serviceList, this);
        recyclerView.setAdapter(adapter);

        // Lấy danh sách dịch vụ từ Firestore
        fetchServices();
        findViewById(R.id.img_back_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ServiceManagementActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });



        // Sự kiện thêm dịch vụ
        findViewById(R.id.btnAddService).setOnClickListener(v -> showAddServiceDialog());
    }

    private void fetchServices() {
        db.collection("services")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        serviceList.clear();
                        for (DocumentSnapshot document : task.getResult()) {
                            Service service = document.toObject(Service.class);
                            serviceList.add(service);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ServiceManagementActivity.this, "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAddServiceDialog() {
        // Tạo dialog để thêm dịch vụ
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm Dịch Vụ");

        // Inflate layout cho dialog
        View view = getLayoutInflater().inflate(R.layout.dialog_add_service, null);
        EditText edtServiceName = view.findViewById(R.id.edtServiceName);
        EditText edtServiceDescription = view.findViewById(R.id.edtServiceDescription);
        EditText edtServicePrice = view.findViewById(R.id.edtServicePrice);
        EditText edtServiceDuration = view.findViewById(R.id.edtServiceDuration);

        builder.setView(view);

        // Thêm sự kiện cho nút "Lưu"
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String serviceName = edtServiceName.getText().toString().trim();
            String serviceDescription = edtServiceDescription.getText().toString().trim();
            String servicePriceString = edtServicePrice.getText().toString().trim();
            String serviceDurationString = edtServiceDuration.getText().toString().trim();

            if (serviceName.isEmpty() || serviceDescription.isEmpty() || servicePriceString.isEmpty() || serviceDurationString.isEmpty()) {
                Toast.makeText(ServiceManagementActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                double servicePrice = Double.parseDouble(servicePriceString);
                long serviceDuration = Long.parseLong(serviceDurationString);

                // Tạo đối tượng Service mới
                String idDichVu = String.valueOf(System.currentTimeMillis()); // ID dịch vụ tự động
                Service newService = new Service(idDichVu, serviceName, serviceDescription, servicePrice, serviceDuration, true);

                // Thêm dịch vụ vào Firestore
                addServiceToFirestore(newService);
            }
        });

        // Thêm sự kiện cho nút "Hủy"
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void addServiceToFirestore(Service newService) {
        db.collection("services")
                .document(newService.getIdDichVu())
                .set(newService)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        serviceList.add(newService);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(ServiceManagementActivity.this, "Dịch vụ đã được thêm", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ServiceManagementActivity.this, "Lỗi khi thêm dịch vụ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Sửa dịch vụ
    public void showEditServiceDialog(Service service) {
        // Tạo dialog để sửa dịch vụ
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sửa Dịch Vụ");

        // Inflate layout cho dialog
        View view = getLayoutInflater().inflate(R.layout.dialog_add_service, null);
        EditText edtServiceName = view.findViewById(R.id.edtServiceName);
        EditText edtServiceDescription = view.findViewById(R.id.edtServiceDescription);
        EditText edtServicePrice = view.findViewById(R.id.edtServicePrice);
        EditText edtServiceDuration = view.findViewById(R.id.edtServiceDuration);

        // Điền dữ liệu cũ vào các trường nhập liệu
        edtServiceName.setText(service.getTenDichVu());
        edtServiceDescription.setText(service.getMoTa());
        edtServicePrice.setText(String.valueOf(service.getGia()));
        edtServiceDuration.setText(String.valueOf(service.getThoiGian()));

        builder.setView(view);

        // Thêm sự kiện cho nút "Lưu"
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String serviceName = edtServiceName.getText().toString().trim();
            String serviceDescription = edtServiceDescription.getText().toString().trim();
            String servicePriceString = edtServicePrice.getText().toString().trim();
            String serviceDurationString = edtServiceDuration.getText().toString().trim();

            if (serviceName.isEmpty() || serviceDescription.isEmpty() || servicePriceString.isEmpty() || serviceDurationString.isEmpty()) {
                Toast.makeText(ServiceManagementActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                double servicePrice = Double.parseDouble(servicePriceString);
                long serviceDuration = Long.parseLong(serviceDurationString);

                // Cập nhật thông tin dịch vụ
                service.setTenDichVu(serviceName);
                service.setMoTa(serviceDescription);
                service.setGia(servicePrice);
                service.setThoiGian(serviceDuration);

                // Cập nhật dịch vụ vào Firestore
                updateServiceInFirestore(service);
            }
        });

        // Thêm sự kiện cho nút "Hủy"
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void updateServiceInFirestore(Service service) {
        db.collection("services")
                .document(service.getIdDichVu())
                .set(service)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Cập nhật lại danh sách dịch vụ
                        int index = serviceList.indexOf(service);
                        if (index != -1) {
                            serviceList.set(index, service);
                            adapter.notifyItemChanged(index);
                        }
                        Toast.makeText(ServiceManagementActivity.this, "Dịch vụ đã được cập nhật", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ServiceManagementActivity.this, "Lỗi khi cập nhật dịch vụ", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
