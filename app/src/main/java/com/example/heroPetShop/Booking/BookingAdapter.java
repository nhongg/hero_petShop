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
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    // Hiển thị Dialog với các tùy chọn "Hủy"
    public static void showOptionsDialog(Context context, CTHDBooking booking, List<CTHDBooking> bookingList) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn hành động")
                .setItems(new CharSequence[]{"Hủy đặt lịch"}, (dialog, which) -> {
                    switch (which) {
                        case 0:  // Hủy đặt lịch
                            cancelBooking(context, booking,bookingList);

                            break;

                    }
                })
                .create()
                .show();
    }

    private static void cancelBooking(Context context, CTHDBooking booking, List<CTHDBooking> bookingList) {
        if (booking.getIdcthdbooking() == null || booking.getIdcthdbooking().isEmpty()) {
            Toast.makeText(context, "Không thể xác định lịch để hủy", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn lý do hủy");

        // Tạo LinearLayout để chứa EditText và TextView
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10); // Thiết lập padding (px)

        // Tạo TextView để hiển thị dòng "Xem chính sách hủy lịch"
        final TextView txtPolicy = new TextView(context);
        txtPolicy.setText("Xem chính sách hủy lịch");
        txtPolicy.setTextColor(Color.BLUE); // Màu chữ xanh
        txtPolicy.setTextSize(14); // Kích thước chữ
        txtPolicy.setPadding(20, 20, 20, 20);
        txtPolicy.setClickable(true);
        txtPolicy.setOnClickListener(v -> showPolicyDialog(context)); // Gắn sự kiện click
        layout.addView(txtPolicy); // Thêm TextView vào layout

        // Tạo radio button cho các lý do hủy
        final String[] reasons = {"Do có việc bận không đến được", "Đổi ý không sử dụng dịch vụ nữa", "Đặt nhầm (ngày/ca)", "Khác (Nhập lý do)"};

        builder.setSingleChoiceItems(reasons, -1, (dialog, which) -> {
            // Lấy lý do hủy từ radio button được chọn
            int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
            String reason = reasons[selectedPosition];

            if (reason.equals("Khác (Nhập lý do)")) {
                // Hiển thị dialog để người dùng nhập lý do
                showCustomReasonDialog(context, booking);
                loadListBooking(context,bookingList);
                dialog.dismiss();
            } else {
                // Xử lý hủy lịch với lý do đã chọn
                updateBookingStatus(context, booking, reason);
                loadListBooking(context,bookingList);
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.setView(layout);
        // Hiển thị dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    // Hiển thị dialog để người dùng nhập lý do hủy
    private static void showCustomReasonDialog(Context context, CTHDBooking booking) {
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
            } else {
                updateBookingStatus(context, booking, reason);
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    // Cập nhật trạng thái lịch đặt vào Firestore
    private static void updateBookingStatus(Context context, CTHDBooking booking, String reason) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("CTHDBooking").document(booking.getIdcthdbooking())
                .update("trangThai", "Đã hủy", "lyDoHuy", reason)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đã hủy đặt lịch với lý do: " + reason, Toast.LENGTH_SHORT).show();
                    NotificationHelper.showNotification(context, "Lịch đặt của bạn đã được huỷ", "Bạn vừa xác nhận huỷ lịch đặt " + booking.getIdBooking() + " với lý do : " + reason);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Không thể hủy đặt lịch", Toast.LENGTH_SHORT).show();
                });
    }

    // Hàm để load lại danh sách đặt lịch
    public static void loadListBooking(Context context, List<CTHDBooking> bookingList) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Query để lấy danh sách đặt lịch sau khi cập nhật
        db.collection("CTHDBooking")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<CTHDBooking> bookings = new ArrayList<CTHDBooking>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        CTHDBooking booking = document.toObject(CTHDBooking.class);
                        bookings.add(booking);
                    }
                    bookingList.clear();
                    bookingList.addAll(bookings);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Không thể tải danh sách đặt lịch", Toast.LENGTH_SHORT).show();
                });
    }

    // Hiển thị dialog chính sách hủy
    private static void showPolicyDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chính sách hủy lịch")
                .setMessage("Các chính sách hủy đơn hàng của cửa hàng spa thú cưng có thể thay đổi tùy thuộc vào từng cửa hàng cụ thể, nhưng thông thường sẽ bao gồm một số yếu tố sau để đảm bảo quyền lợi cho cả khách hàng và cửa hàng:\n" +
                        "\n" +
                        "Thời gian hủy đơn hàng:\n"+
                        "Cửa hàng thường yêu cầu khách hàng hủy đơn hàng ít nhất 24-48 giờ trước khi lịch hẹn dịch vụ diễn ra để tránh việc lãng phí tài nguyên và thời gian.\n" +
                        "Nếu hủy sau thời gian quy định, cửa hàng có thể yêu cầu khách hàng đóng phí hủy hoặc mất phí một phần dịch vụ.\n" +
                        "\n" +
                        "Phí hủy đơn hàng:\n" +
                        "Tùy vào chính sách của cửa hàng, nếu khách hàng hủy đơn quá sát giờ, cửa hàng có thể thu phí hủy đơn. Phí này có thể từ 10% - 50% giá trị của dịch vụ đã đặt.\n" +
                        "Trong một số trường hợp đặc biệt như đặt dịch vụ spa cho thú cưng vào các dịp lễ hoặc thời điểm cao điểm, cửa hàng có thể áp dụng mức phí hủy cao hơn.\n" +
                        "\n" +
                        "Yêu cầu thay đổi lịch hẹn:\n" +
                        "Cửa hàng cũng có thể yêu cầu khách hàng thông báo về việc thay đổi lịch hẹn trước một khoảng thời gian nhất định, ví dụ như 24 giờ. Thay đổi này có thể không bị tính phí nếu thực hiện đúng thời gian quy định.\n" +

                        "\n" +
                        "Lý do hủy:\n" +
                        "Nếu khách hàng hủy vì lý do khẩn cấp như bệnh tật, cửa hàng có thể linh động và không tính phí, nhưng cần có giấy tờ chứng minh (ví dụ: chứng nhận y tế cho thú cưng hoặc giấy tờ liên quan).\n" +
                        "Nếu khách hàng hủy mà không có lý do chính đáng, cửa hàng có thể thu một khoản phí.\n" +
                        "\n" +
                        "Thời gian trả tiền lại:\n" +
                        "Nếu cửa hàng đã nhận tiền trước khi hủy, việc hoàn tiền có thể sẽ phụ thuộc vào thời gian hủy và chính sách của cửa hàng. Thường sẽ mất một vài ngày làm việc để xử lý hoàn tiền.\n" +
                        "\n" +
                        "Đặt cọc trước:\n" +
                        "Một số cửa hàng yêu cầu đặt cọc trước khi đặt lịch hẹn dịch vụ spa cho thú cưng. Nếu hủy đơn hàng, tiền đặt cọc có thể sẽ không được hoàn lại, hoặc hoàn lại một phần tùy theo chính sách.\n" +
                        "\n" +
                        "Điều kiện đặc biệt:\n" +
                        "Các trường hợp hủy do thời tiết xấu (ví dụ: bão, lũ lụt) hay trường hợp bất khả kháng khác có thể được miễn phí hoặc linh động hơn về phí hủy.\n" +
                        "Cửa hàng cần cung cấp thông tin rõ ràng và minh bạch về chính sách hủy đơn hàng trên website hoặc thông qua các phương tiện liên lạc (email, tin nhắn, gọi điện) để khách hàng hiểu và tuân thủ.\n"+
                        "\n" +
                        "1. Hủy lịch trước 24 giờ không mất phí.\n"
                        + "2. Hủy lịch trong vòng 24 giờ sẽ mất từ 30.000đ - 50.000đ chi phí dịch vụ.\n"
                        + "3. Vui lòng liên hệ bộ phận hỗ trợ nếu có thắc mắc ( 0978357121 ).")
                .setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    // Mở Activity để sửa thông tin
    private static void editBooking(Context context, Booking booking) {
        // Intent intent = new Intent(context, EditBookingActivity.class);
//        intent.putExtra("bookingId", booking.getIdBooking());
//        context.startActivity(intent);
    }
}
