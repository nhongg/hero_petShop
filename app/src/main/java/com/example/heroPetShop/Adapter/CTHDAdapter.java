package com.example.heroPetShop.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Thư viện để load ảnh
import com.example.heroPetShop.Booking.CTBooking;
import com.example.heroPetShop.Booking.CTHDBooking;
import com.example.heroPetShop.DichVu.Service;
import com.example.heroPetShop.R;

import java.util.List;

public class CTHDAdapter extends RecyclerView.Adapter<CTHDAdapter.CTHDViewHolder> {

    private List<CTBooking> services;
    private Context context;
    public CTHDAdapter(Context context, List<CTBooking> services) {
        this.context = context;
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
        CTBooking service = services.get(position);
        Log.d("CTAdapter", "Image URL: " + service.getImage());
        int result = (int) (service.getGiaDichVu() );
        holder.txtServiceName.setText(service.getTenDichVu());
        holder.txtServicePrice.setText(String.valueOf(result));
        Glide.with(holder.itemView.getContext())
                .load(service.getImage()) // URL ảnh
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
