package com.example.heroPetShop.DichVu;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.R;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import java.security.Provider;
import java.util.List;

public class ServiceAdapter_KH extends RecyclerView.Adapter<ServiceAdapter_KH.ServiceViewHolder> {
    private List<Service> serviceList;
    private Context context;

    public ServiceAdapter_KH(List<Service> serviceList, Context context) {
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

        private TextView tvTenDV, tvGiaDV, tvTrongluongDV, tvSoluongDV;
        private ImageView imgDV;
        private LinearLayout layoutDV;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            tvTenDV = itemView.findViewById(R.id.tv_ten_dichvu);
            tvGiaDV = itemView.findViewById(R.id.tv_giatien_dichvu);
            imgDV = itemView.findViewById(R.id.img_dichvu);


            layoutDV = itemView.findViewById(R.id.layout_dichvu);

            // Xử lý click item
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Service clickedService = serviceList.get(position);

                    // Chuyển đến màn hình chi tiết dịch vụ
                    Intent intent = new Intent(context, DetailServiceActivity.class);
                    intent.putExtra("tenDichVu", clickedService.getTenDichVu());
                    intent.putExtra("moTa", clickedService.getMoTa());
                    intent.putExtra("gia", clickedService.getGia());
                    intent.putExtra("thoiGian", clickedService.getThoiGian());
                    intent.putExtra("hoatDong", clickedService.isHoatDong());
                    intent.putExtra("serviceId", clickedService.getIdDichVu());

                    context.startActivity(intent);

                }
            });
        }

        public void bind(Service service) {
            tvTenDV.setText(service.getTenDichVu());
            tvGiaDV.setText(String.valueOf(service.getGia()));
            Picasso.get().load(service.getImg()).into(imgDV);
        }
    }



}
