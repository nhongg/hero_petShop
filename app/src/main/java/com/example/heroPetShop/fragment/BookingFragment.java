package com.example.heroPetShop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.Booking.CTHDBooking;
import com.example.heroPetShop.Booking.BookingAdapter;
import com.example.heroPetShop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookingAdapter bookingAdapter;
    private List<CTHDBooking> bookingList;
    private FirebaseFirestore db;
    private String userId;  // Biến lưu trữ id người dùng

    // Constructor mặc định
    public BookingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance(); // Khởi tạo Firestore

        // Lấy userId từ Firebase Authentication
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();  // Lấy uid của người dùng đang đăng nhập
        } else {
            // Nếu không có người dùng đăng nhập, có thể đưa ra thông báo hoặc điều hướng về trang đăng nhập
            // ví dụ: redirectToLogin();
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking_list, container, false);

        // Cài đặt RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(getContext(), bookingList);
        recyclerView.setAdapter(bookingAdapter);

        // Lấy danh sách đặt lịch từ Firestore
        getBookingsFromFirestore();
        Toast.makeText(getContext(), ""+userId, Toast.LENGTH_SHORT).show();
        bookingAdapter.setOnItemClickListener(new BookingAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(CTHDBooking booking) {
                // Hiển thị dialog khi long-click vào item
                BookingAdapter.showOptionsDialog(getContext(), booking);
                bookingAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void getBookingsFromFirestore() {
        // Lọc booking theo userId
        db.collection("CTHDBooking")
           .whereEqualTo("iduser", userId) // Lọc theo userId
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
                    }
                });
    }
}
