package com.example.heroPetShop.sildeshow;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.heroPetShop.R;
import com.example.heroPetShop.View.Admin.AdminHomeActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageImagesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageManageAdapter adapter;
    private List<Image> imageList;
    private FirebaseFirestore db;


    private ImageView imgBackUsers;
    private ViewPager2 viewPager2;
    private ImageSliderAdapter adapter2;
    private List<String> imageUrls;

    private Handler sliderHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_images);

        recyclerView = findViewById(R.id.recyclerView);
        imageList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        adapter = new ImageManageAdapter(this, imageList, new ImageManageAdapter.OnImageActionListener() {
            @Override
            public void onEdit(Image image) {
                editImage(image);
            }

            @Override
            public void onDelete(Image image) {
                deleteImage(image);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Button Add Image
        findViewById(R.id.btnAddImage).setOnClickListener(v -> addImage());

        loadImages();


        setupSlideshow();

        imgBackUsers = findViewById(R.id.img_back_banner);
        findViewById(R.id.img_back_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageImagesActivity.this, AdminHomeActivity.class);
                startActivity(intent);
            }

        });


    }

    private void loadImages() {
        db.collection("images")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        imageList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String id = document.getId();
                            String url = document.getString("url");
                            imageList.add(new Image(id, url));
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void editImage(Image image) {
        // Hiển thị Dialog để sửa URL
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Image");

        final EditText input = new EditText(this);
        input.setText(image.getUrl()); // Hiển thị URL hiện tại
        builder.setView(input);

        builder.setPositiveButton("Update", (dialog, which) -> {
            String newUrl = input.getText().toString().trim();
            if (!TextUtils.isEmpty(newUrl)) {
                // Cập nhật Firestore
                db.collection("images").document(image.getId())
                        .update("url", newUrl)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Image updated!", Toast.LENGTH_SHORT).show();
                            loadImages(); // Tải lại danh sách ảnh
                            loadImagesFromFirestore();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to update image.", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Image URL cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void deleteImage(Image image) {
        db.collection("images").document(image.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Image deleted!", Toast.LENGTH_SHORT).show();
                    loadImages();
                    loadImagesFromFirestore();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error deleting image.", Toast.LENGTH_SHORT).show();
                });
    }

    private void addImage() {
        // Hiển thị Dialog để nhập URL
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Image");

        final EditText input = new EditText(this);
        input.setHint("Enter image URL");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String imageUrl = input.getText().toString().trim();
            if (!TextUtils.isEmpty(imageUrl)) {
                // Lưu vào Firestore
                db.collection("images")
                        .add(new Image(null, imageUrl))
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(this, "Image added!", Toast.LENGTH_SHORT).show();
                            loadImages(); // Tải lại danh sách ảnh
                            loadImagesFromFirestore();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed to add image.", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Image URL cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }





    private void setupSlideshow() {
        viewPager2 = findViewById(R.id.viewPager);
        imageUrls = new ArrayList<>();
        adapter2 = new ImageSliderAdapter(this, imageUrls);
        viewPager2.setAdapter(adapter2);

        db = FirebaseFirestore.getInstance();
        sliderHandler = new Handler();

        loadImagesFromFirestore();
        startAutoSlide();
    }

    private void loadImagesFromFirestore() {
        db.collection("images") // Tên collection trong Firestore
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String url = document.getString("url");
                            if (url != null && !url.isEmpty()) {
                                imageUrls.add(url); // Thêm URL vào danh sách
                            }
                        }
                        adapter2.notifyDataSetChanged(); // Cập nhật adapter
                    } else {
                        Log.e("Firestore", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void startAutoSlide() {
        Runnable slideRunnable = new Runnable() {
            @Override
            public void run() {
                if (imageUrls.size() > 0) { // Kiểm tra danh sách không rỗng
                    int currentItem = viewPager2.getCurrentItem();
                    int nextItem = (currentItem + 1) % imageUrls.size(); // Lặp lại từ đầu khi hết ảnh
                    viewPager2.setCurrentItem(nextItem, true); // Chuyển đến ảnh tiếp theo
                }
                sliderHandler.postDelayed(this, 3000); // Chuyển ảnh sau 3 giây
            }
        };
        sliderHandler.postDelayed(slideRunnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sliderHandler != null) {
            sliderHandler.removeCallbacksAndMessages(null); // Ngừng tự động chuyển slide
        }
    }
}


