package com.example.heroPetShop.DichVu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.R;
import com.example.heroPetShop.View.TaoComboDichVu;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComBoServiceAdapter extends RecyclerView.Adapter<ComBoServiceAdapter.ServiceViewHolder> {
    private List<Service> serviceList;
    private Context context;
    private StringBuilder selectedServiceNames = new StringBuilder();  // Dùng StringBuilder để nối chuỗi hiệu quả
    private double totalPrice = 0;  // Biến lưu trữ tổng tiền dịch vụ đã chọn
    private Set<Integer> selectedPositions = new HashSet<>();  // Set để lưu các vị trí item đã chọn

    public ComBoServiceAdapter(List<Service> serviceList, Context context) {
        this.serviceList = serviceList;
        this.context = context;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_combo_dv, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.bind(service, position);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView txtServiceName;
        private View itemView;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            txtServiceName = itemView.findViewById(R.id.tDichVu);
        }

        public void bind(Service service, int position) {
            txtServiceName.setText(service.getTenDichVu());

            // Kiểm tra xem item đã được chọn hay chưa để thay đổi màu nền
            if (selectedPositions.contains(position)) {
                itemView.setBackgroundColor(Color.parseColor("#FF6600"));  // Màu khi chọn
            } else {
                itemView.setBackgroundColor(Color.parseColor("#66CCFF"));  // Màu khi chưa chọn
            }

            // Xử lý khi người dùng click vào item
            itemView.setOnClickListener(v -> {
                if (selectedPositions.contains(position)) {
                    // Nếu đã chọn, bỏ chọn
                    selectedPositions.remove(position);
                    // Loại bỏ tên dịch vụ khỏi chuỗi đã chọn
                    selectedServiceNames.delete(selectedServiceNames.indexOf(service.getTenDichVu()),
                            selectedServiceNames.indexOf(service.getTenDichVu()) + service.getTenDichVu().length() + 3);  // Xóa " + "
                    totalPrice -= service.getGia();
                } else {
                    // Nếu chưa chọn, chọn nó
                    selectedPositions.add(position);
                    // Thêm tên dịch vụ vào chuỗi đã chọn
                    selectedServiceNames.append(service.getTenDichVu()).append(" + ");
                    totalPrice += service.getGia();
                }

                // Cập nhật lại giao diện người dùng
                updateUI();
            });
        }

        // Cập nhật lại TextView hiển thị tên dịch vụ và tổng tiền
        private void updateUI() {
            // Cập nhật danh sách dịch vụ đã chọn
            TextView tvSelectedServices = ((TaoComboDichVu) context).findViewById(R.id.tvSelectedServices);
            tvSelectedServices.setText( selectedServiceNames.toString());

            // Định dạng và hiển thị tổng tiền
            DecimalFormat df = new DecimalFormat("#.##");
            TextView tvTotalPrice = ((TaoComboDichVu) context).findViewById(R.id.tvTotalPrice);
            tvTotalPrice.setText(df.format(totalPrice));  // Sử dụng DecimalFormat để định dạng đúng
        }
    }
}
