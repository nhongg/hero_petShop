package com.example.heroPetShop.Booking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.R;
import com.example.heroPetShop.ultil.NotificationHelper;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private List<CTHDBooking> bookingList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemLongClick(CTHDBooking booking);
    }

    public BookingAdapter(Context context, List<CTHDBooking> bookingList) {
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

        holder.txtID.setText("Mã : "+booking.getIdBooking());
        holder.txtTenDichVu.setText("Dịch vụ sử dụng : "+booking.getTenDichVu());
        holder.txtTenThuCung.setText("Thú cưng : "+booking.getTenThuCung() + " - " + booking.getLoaiThuCung());
        holder.txtThoiGian.setText(formatDate(booking.getThoiGianDatLich().getTime()));
        holder.txtTrangThai.setText(booking.getTrangThai());


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BookingDetailActivity.class);
            intent.putExtra("bookingDetail", booking);  // Truyền booking đến Activity chi tiết
            context.startActivity(intent);
        });


        // Kiểm tra trạng thái của booking, nếu là "Hoàn thành" thì không cho phép long-click
        if ("Chưa xác nhận".equals(booking.getTrangThai()) || "Đã xác nhận".equals(booking.getTrangThai())) {
            holder.itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onItemLongClick(booking);  // Trigger the long-click action
                }
                return true;
            });
        } else {
            holder.itemView.setOnLongClickListener(null); // Không cho phép long-click khi trạng thái là "Hoàn thành"
        }
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
        TextView txtTenDichVu, txtTenThuCung, txtThoiGian,txtTrangThai,txtID;
        LinearLayout itemln;

        public BookingViewHolder(View itemView) {
            super(itemView);
            txtTenDichVu = itemView.findViewById(R.id.txtTenDichVu);
            txtTenThuCung = itemView.findViewById(R.id.txtTenThuCung);
            txtThoiGian = itemView.findViewById(R.id.txtThoiGian);
            txtTrangThai = itemView.findViewById(R.id.txtTrangThai);
            txtID = itemView.findViewById(R.id.txtID);
        }
    }

    // Hiển thị Dialog với các tùy chọn "Hủy" và "Sửa"
    public static void showOptionsDialog(Context context, CTHDBooking booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn hành động")
                .setItems(new CharSequence[]{"Hủy đặt lịch"}, (dialog, which) -> {
                    switch (which) {
                        case 0:  // Hủy đặt lịch
                            cancelBooking(context, booking);

                            break;

                    }
                })
                .create()
                .show();
    }

    // Hủy đặt lịch
    private static void cancelBooking(Context context, CTHDBooking booking) {
        if (booking.getIdcthdbooking() == null || booking.getIdcthdbooking().isEmpty()) {
            Toast.makeText(context, "Không thể xác định lịch để hủy", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị dialog để nhập lý do hủy
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Nhập lý do hủy");

        // Thêm EditText vào dialog
        final EditText input = new EditText(context);
        input.setHint("Lý do hủy...");
        builder.setView(input);

        builder.setPositiveButton("Xác nhận", (dialog, which) -> {
            String reason = input.getText().toString().trim();
            if (reason.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập lý do hủy", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cập nhật Firestore với lý do hủy và trạng thái
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("CTHDBooking").document(booking.getIdcthdbooking())
                    .update("trangThai", "Đã hủy", "lyDoHuy", reason)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Đã hủy đặt lịch với lý do: " + reason, Toast.LENGTH_SHORT).show();
                        NotificationHelper.showNotification(context, "Lịch đặt của bạn đã được huỷ", "Bạn vừa xác nhận huỷ lịch đặt "+booking.getIdBooking()+" với lý do : "+ reason);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Không thể hủy đặt lịch", Toast.LENGTH_SHORT).show();
                    });
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    // Mở Activity để sửa thông tin
    private static void editBooking(Context context, Booking booking) {
        // Intent intent = new Intent(context, EditBookingActivity.class);
//        intent.putExtra("bookingId", booking.getIdBooking());
//        context.startActivity(intent);
    }
}
