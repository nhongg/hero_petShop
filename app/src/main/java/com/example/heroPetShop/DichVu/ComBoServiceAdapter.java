package com.example.heroPetShop.DichVu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.R;

import java.util.List;

public class ComBoServiceAdapter extends RecyclerView.Adapter<ComBoServiceAdapter.ServiceViewHolder> {
    private List<Service> serviceList;
    private Context context;
    private String accumulatedNames = "";  // Biến lưu trữ tên các dịch vụ đã chọn
    private int totalPrice = 0;  // Biến lưu trữ tổng giá các dịch vụ đã chọn

    public ComBoServiceAdapter(List<Service> serviceList, Context context) {
        this.serviceList = serviceList;
        this.context = context;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_khachhang, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        private TextView txtServiceName;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            txtServiceName = itemView.findViewById(R.id.tDichVu);

            // Xử lý click item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Service clickedService = serviceList.get(position);

                    // Cộng dồn tên và giá dịch vụ khi nhấn vào item
                    accumulatedNames += clickedService.getTenDichVu() + " + ";  // Cộng tên
                    totalPrice += clickedService.getGia();  // Cộng giá

                    // Hiển thị thông tin đã cộng dồn
                    Toast.makeText(context, "Tên dịch vụ: " + accumulatedNames + "\nTổng giá: " + totalPrice, Toast.LENGTH_SHORT).show();

                    // Cập nhật lại giao diện với thông tin mới
                    // Nếu cần có một TextView để hiển thị thông tin tổng, bạn có thể gọi một phương thức update UI ở đây.
                }
            });
        }

        public void bind(Service service) {
            txtServiceName.setText(service.getTenDichVu());
        }
    }
}
