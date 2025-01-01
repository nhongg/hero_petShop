package com.example.heroPetShop.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Thư viện để load ảnh
import com.example.heroPetShop.DichVu.Service;
import com.example.heroPetShop.R;

import java.util.List;

public class CTHDAdapter extends RecyclerView.Adapter<CTHDAdapter.CTHDViewHolder> {

    private List<Service> services;

    public CTHDAdapter(List<Service> services) {
        this.services = services;
    }

    @NonNull
    @Override
    public CTHDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_dv, parent, false);
        return new CTHDViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CTHDViewHolder holder, int position) {
        Service service = services.get(position);
        Log.d("CTHDAdapter", "Image URL: " + service.getImg());
        holder.txtServiceName.setText(service.getTenDichVu());
        holder.txtServicePrice.setText(String.valueOf(service.getGia()));
        Glide.with(holder.itemView.getContext())
                .load(service.getImg()) // URL ảnh
                .error(R.drawable.error_image) // Hình ảnh hiển thị khi lỗi
                .into(holder.imgService);

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    static class CTHDViewHolder extends RecyclerView.ViewHolder {

        TextView txtServiceName, txtServicePrice;
        ImageView imgService;

        public CTHDViewHolder(@NonNull View itemView) {
            super(itemView);
            txtServiceName = itemView.findViewById(R.id.txtServiceName);
            txtServicePrice = itemView.findViewById(R.id.txtServicePrice);
            imgService = itemView.findViewById(R.id.img_dv);
        }
    }
}
