package com.example.heroPetShop.View.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.Booking.CTHDBooking;
import com.example.heroPetShop.Booking.BookingAdapter_admin;
import com.example.heroPetShop.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity_admin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookingAdapter_admin bookingAdapter;
    private List<CTHDBooking> bookingList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);  // Đảm bảo layout này tồn tại

        findViewById(R.id.img_back_users).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingActivity_admin.this, AdminHomeActivity.class);
                startActivity(intent);
            }

        });
        db = FirebaseFirestore.getInstance(); // Khởi tạo Firestore

        // Cài đặt RecyclerView
        recyclerView = findViewById(R.id.recyclerViewBookingsAdm);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter_admin(this, bookingList);
        recyclerView.setAdapter(bookingAdapter);

        // Lấy danh sách đặt lịch từ Firestore
        getBookingsFromFirestore();

        // Lắng nghe sự kiện long-click trong RecyclerView
        bookingAdapter.setOnItemClickListener(new BookingAdapter_admin.OnItemClickListener() {
            @Override
            public void onItemLongClick(CTHDBooking booking) {
                // Hiển thị dialog khi long-click vào item
                BookingAdapter_admin.showOptionsDialog(BookingActivity_admin.this, booking);
                // Cập nhật lại danh sách bookings ngay lập tức
                getBookingsFromFirestore(); // Tải lại dữ liệu sau khi thay đổi
            }
        });
    }

    private void getBookingsFromFirestore() {
        db.collection("CTHDBooking")
                .orderBy("thoiGianDatLich", Query.Direction.ASCENDING) // Sắp xếp theo thời gian từ nhỏ nhất lên lớn nhất
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            bookingList.clear();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                CTHDBooking booking = document.toObject(CTHDBooking.class);
                                bookingList.add(booking);
                            }
                            bookingAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                        }
                    } else {
                        Toast.makeText(BookingActivity_admin.this, "Error getting bookings.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
