package com.example.heroPetShop.DichVu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.R;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    private List<Service> serviceList;
    private Context context;

    public ServiceAdapter(List<Service> serviceList, Context context) {
        this.serviceList = serviceList;
        this.context = context;
    }

    @Override
    public ServiceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
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
        private TextView txtServiceDescription;
        private TextView txtServicePrice;
        private TextView txtServiceDuration;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            txtServiceDescription = itemView.findViewById(R.id.txtServiceDescription);
            txtServicePrice = itemView.findViewById(R.id.txtServicePrice);
            txtServiceDuration = itemView.findViewById(R.id.txtServiceDuration);

            // Sự kiện nhấn vào item để sửa dịch vụ
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Service service = serviceList.get(position);
                    // Mở dialog sửa dịch vụ
                    if (context instanceof ServiceManagementActivity) {
                        ((ServiceManagementActivity) context).showEditServiceDialog(service);
                    }
                }
            });
        }

        public void bind(Service service) {
            txtServiceName.setText(service.getTenDichVu());
            txtServiceDescription.setText(service.getMoTa());
            txtServicePrice.setText("Giá: " + service.getGia() + " VND");
            txtServiceDuration.setText("Thời gian: " + service.getThoiGian() + " phút");
        }
    }
}
