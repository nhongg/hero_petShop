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
    private double totalPrice = 0;
    private Set<Integer> selectedPositions = new HashSet<>(); // Đảm bảo item không bị chọn trùng

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

            // Cập nhật màu nền dựa trên trạng thái đã chọn
            if (selectedPositions.contains(position)) {
                itemView.setBackgroundColor(Color.parseColor("#FF6600")); // Màu khi chọn
            } else {
                itemView.setBackgroundColor(Color.parseColor("#66CCFF")); // Màu mặc định
            }

            // Xử lý sự kiện click
            itemView.setOnClickListener(v -> {
                if (selectedPositions.contains(position)) {
                    // Bỏ chọn
                    selectedPositions.remove(position);
                    totalPrice -= service.getGia();
                } else {
                    // Chọn item
                    selectedPositions.add(position);
                    totalPrice += service.getGia();
                }

                // Cập nhật giao diện
                updateUI();
                notifyItemChanged(position); // Thông báo cập nhật màu nền cho item
            });
        }

        private void updateUI() {
            // Danh sách tên dịch vụ đã chọn
            StringBuilder selectedServiceNames = new StringBuilder();
            StringBuilder selectedServiceIDs = new StringBuilder();

            for (int position : selectedPositions) {
                Service selectedService = serviceList.get(position);
                selectedServiceNames.append(selectedService.getTenDichVu()).append(", ");
                selectedServiceIDs.append(selectedService.getIdDichVu()).append(", ");
            }

            // Xóa dấu phẩy cuối cùng
            if (selectedServiceNames.length() > 0) {
                selectedServiceNames.setLength(selectedServiceNames.length() - 2);
            }
            if (selectedServiceIDs.length() > 0) {
                selectedServiceIDs.setLength(selectedServiceIDs.length() - 2);
            }

            // Cập nhật TextView
            if (context instanceof TaoComboDichVu) {
                TaoComboDichVu activity = (TaoComboDichVu) context;

                TextView tvSelectedServices = activity.findViewById(R.id.tvSelectedServices);
                TextView tvServiceIDs = activity.findViewById(R.id.tviddichvu);
                TextView tvTotalPrice = activity.findViewById(R.id.tvTotalPrice);

                if (tvSelectedServices != null) {
                    tvSelectedServices.setText(selectedServiceNames.toString());
                }
                if (tvServiceIDs != null) {
                    tvServiceIDs.setText(selectedServiceIDs.toString());
                }
                if (tvTotalPrice != null) {
                    DecimalFormat df = new DecimalFormat("#.##");
                    tvTotalPrice.setText(df.format(totalPrice));
                }
            }
        }
    }
}
