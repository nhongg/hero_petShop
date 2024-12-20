package com.example.heroPetShop.Booking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingAdapter_admin extends RecyclerView.Adapter<BookingAdapter_admin.BookingViewHolder> {

    private List<CTHDBooking> bookingList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemLongClick(CTHDBooking booking);
    }

    public BookingAdapter_admin(Context context, List<CTHDBooking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    // Gán listener cho adapter
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public BookingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookingViewHolder holder, int position) {
        CTHDBooking booking = bookingList.get(position);

        holder.txtTenDichVu.setText(booking.getTenDichVu());
        holder.txtTenThuCung.setText(booking.getTenThuCung());
        holder.txtThoiGian.setText(formatDate(booking.getThoiGianDatLich().getTime()));
        holder.txtTrangThai.setText(booking.getTrangThai());

        // Long-click event listener
        holder.itemView.setOnLongClickListener(v -> {
            if (listener != null) {
                listener.onItemLongClick(booking);  // Trigger the long-click action
            }
            return true;
        });

        // Click item to show detail
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingDetailActivity.class);
            intent.putExtra("bookingDetail", booking);  // Truyền đối tượng booking sang Activity chi tiết
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    // Định dạng thời gian
    private String formatDate(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(timestamp);
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenDichVu, txtTenThuCung, txtThoiGian,txtTrangThai;

        public BookingViewHolder(View itemView) {
            super(itemView);
            txtTenDichVu = itemView.findViewById(R.id.txtTenDichVu);
            txtTenThuCung = itemView.findViewById(R.id.txtTenThuCung);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGian);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);
        }
    }

    // Hiển thị Dialog với các tùy chọn
    public static void showOptionsDialog(Context context, CTHDBooking booking) {
        // Kiểm tra trạng thái của booking trước khi hiển thị các tùy chọn
        if ("Đã hủy".equals(booking.getTrangThai())) {
            Toast.makeText(context, "Booking đã bị hủy, không thể thay đổi trạng thái nữa.", Toast.LENGTH_SHORT).show();
            return; // Không hiển thị dialog nếu trạng thái là "Đã hủy"
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn hành động")
                .setItems(new CharSequence[]{"Hủy đặt lịch","Xác nhận đơn","Đang xử lý","Hoàn thành"}, (dialog, which) -> {
                    switch (which) {
                        case 0:  // Hủy đặt lịch
                            cancelBooking(context, booking);
                            break;
                        case 1:  // Xác nhận đơn
                            confirmOrder(context, booking);
                            break;
                        case 2:// Đang xử lý
                            suLydonHang(context, booking);
                            break;
                        case 3:  // Hoàn thành đơn hàng
                            hoanthanhdonHang(context, booking);
                            break;
                    }
                })
                .create()
                .show();
    }

    // Hủy đặt lịch
    private static void cancelBooking(Context context, CTHDBooking booking) {
        if (booking.getIdcthdbooking() == null || booking.getIdcthdbooking().isEmpty()) {
            Toast.makeText(context, "Không thể xác định lịch để hủy"+booking.getIdcthdbooking(), Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CTHDBooking").document(booking.getIdcthdbooking())
                .update("trangThai", "Đã hủy")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đã hủy đặt lịch", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Không thể hủy đặt lịch", Toast.LENGTH_SHORT).show();
                });

    }



    // Mở Activity để sửa thông tin
    private static void editBooking(Context context, Booking booking) {
        // Intent intent = new Intent(context, EditBookingActivity.class);
//        intent.putExtra("bookingId", booking.getIdBooking());
//        context.startActivity(intent);
    }

    // Xác nhận đơn hàng
    private static void confirmOrder(Context context, CTHDBooking booking) {
        if (booking.getIdcthdbooking() == null || booking.getIdcthdbooking().isEmpty()) {
            Toast.makeText(context, "Không thể xác định đơn hàng để xác nhận", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CTHDBooking").document(booking.getIdcthdbooking())
                .update("trangThai", "Đã xác nhận")  // Cập nhật trạng thái đơn hàng thành "Đã xác nhận"
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đơn hàng đã được xác nhận", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Không thể xác nhận đơn hàng", Toast.LENGTH_SHORT).show();
                });
    }


    // xử lý đơn hàng
    private static void suLydonHang(Context context, CTHDBooking booking) {
        if (booking.getIdcthdbooking() == null || booking.getIdcthdbooking().isEmpty()) {
            Toast.makeText(context, "Không thể xử lý đơn hàng để xác nhận", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CTHDBooking").document(booking.getIdcthdbooking())
                .update("trangThai", "Đang xử lý")  // Cập nhật trạng thái đơn hàng thành "Đã xác nhận"
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đơn hàng đã được xác nhận", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Không thể xác nhận đơn hàng", Toast.LENGTH_SHORT).show();
                });
    }

    // hoàn thành đơn hàng
    private static void hoanthanhdonHang(Context context, CTHDBooking booking) {
        if (booking.getIdcthdbooking() == null || booking.getIdcthdbooking().isEmpty()) {
            Toast.makeText(context, "Không thể xử lý đơn hàng để hoàn thành", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Cập nhật trạng thái đơn hàng thành "Hoàn thành"
        db.collection("CTHDBooking").document(booking.getIdcthdbooking())
                .update("trangThai", "Hoàn thành")
                .addOnSuccessListener(aVoid -> {
                    // Thêm thông báo vào bảng Notifications
                    Map<String, Object> notification = new HashMap<>();
                    notification.put("message", "Booking " + booking.getIdcthdbooking() + " Đã hoàn thành.");
                    notification.put("createdAt", FieldValue.serverTimestamp());

                    db.collection("Notifications")
                            .add(notification)
                            .addOnSuccessListener(docRef -> {
                               // Toast.makeText(context, "Đơn hàng đã được hoàn thành ", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                               // Toast.makeText(context, "Cập nhật trạng thái thành công nhưng không thể lưu thông báo.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Không thể hoàn thành đơn hàng", Toast.LENGTH_SHORT).show();
                });
    }

}
