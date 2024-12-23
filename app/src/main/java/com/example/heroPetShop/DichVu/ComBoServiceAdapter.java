package com.example.heroPetShop.DichVu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.R;
import com.example.heroPetShop.View.TaoComboDichVu;
import com.squareup.picasso.Picasso;

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
        private TextView txtServiceName,txtGia;

        private ImageView imgDichvu;
        private View itemView;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            txtServiceName = itemView.findViewById(R.id.tDichVu);
            imgDichvu = itemView.findViewById(R.id.imgDichVu);
            txtGia = itemView.findViewById(R.id.txtGia);


        }

        public void bind(Service service, int position) {
            txtServiceName.setText(service.getTenDichVu());
            txtGia.setText(String.valueOf(service.getGia()));
            Picasso.get().load(service.getImg()).into(imgDichvu);

            double price = service.getGia();
            DecimalFormat df = new DecimalFormat("#,###.##"); // Định dạng số
            String formattedPrice = df.format(price); // Định dạng giá

            // Thêm " VND" vào cuối
            txtGia.setText(formattedPrice + " VND");



            // Cập nhật màu nền dựa trên trạng thái đã chọn
            if (selectedPositions.contains(position)) {
                itemView.setBackgroundColor(Color.parseColor("#F2DC92")); // Màu khi chọn
            } else {
                itemView.setBackgroundColor(Color.parseColor("#C8C8C8")); // Màu mặc định
            }

            itemView.setOnLongClickListener(v -> {
                // Chuyển đến màn hình chi tiết dịch vụ khi người dùng ấn giữ
                Intent intent = new Intent(context, DetailServiceActivity.class);
                intent.putExtra("tenDichVu", service.getTenDichVu());
                intent.putExtra("moTa", service.getMoTa());
                intent.putExtra("gia", service.getGia());
                intent.putExtra("thoiGian", service.getThoiGian());
                intent.putExtra("hoatDong", service.isHoatDong());
                intent.putExtra("img", service.getImg());
                intent.putExtra("serviceId", service.getIdDichVu());

                context.startActivity(intent); // Mở Activity chi tiết

                return true; // Trả về true để xử lý sự kiện long click
            });


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
