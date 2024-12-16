package com.example.heroPetShop.View.Admin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.Models.Product;
import com.example.heroPetShop.R;
import com.example.heroPetShop.my_interface.IClickCTHD;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;



import java.text.NumberFormat;
import java.util.ArrayList;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Product> mlist;
    private IClickCTHD iClickCTHD;

    public AdminProductAdapter(Context context, ArrayList<Product> mlist, IClickCTHD iClickCTHD) {
        this.context = context;
        this.mlist = mlist;
        this.iClickCTHD = iClickCTHD;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_admin_product,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        Product product = mlist.get(position);
        holder.tvTen.setText(product.getTensp());
        holder.tvMota.setText(product.getMota());
        holder.tvSoluong.setText(product.getSoluong()+"");
        holder.tvDongia.setText(NumberFormat.getInstance().format(product.getGiatien()));
        Picasso.get().load(product.getHinhanh()).into(holder.img);


        if (product.getSoluong() == 0) {
            // Thay đổi trạng thái type của sản phẩm thành 2 (hết hàng)
            product.setType(9);  // Giả sử `setType` là phương thức để cập nhật type trong Product

            // Cập nhật Firestore (hoặc cơ sở dữ liệu của bạn) để lưu trạng thái type mới
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference productRef = db.collection("SanPham").document(product.getId());
            productRef.update("type", 9)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Trạng thái sản phẩm đã được cập nhật thành 2.");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Cập nhật trạng thái sản phẩm thất bại: " + e.getMessage());
                    });

            // Thay đổi màu nền của item thành đỏ để thông báo hết hàng
            holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.Red));  // Màu đỏ cảnh báo
        } else {
            // Đặt lại trạng thái type của sản phẩm nếu số lượng > 0 (hoặc không cần thay đổi)
            product.setType(1);  // Giả sử trạng thái ban đầu của sản phẩm là 1 (có sẵn trong kho)

            // Cập nhật Firestore nếu muốn lưu lại trạng thái này
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference productRef = db.collection("SanPham").document(product.getId());
            productRef.update("type", 1)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Trạng thái sản phẩm đã được cập nhật thành 1.");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Cập nhật trạng thái sản phẩm thất bại: " + e.getMessage());
                    });

            // Đặt lại màu nền nếu số lượng > 0 (màu bình thường)
            holder.constraintLayout.setBackgroundColor(context.getResources().getColor(R.color.white));  // Hoặc màu nền mặc định
        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickCTHD.onClickCTHD(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTen, tvMota, tvSoluong, tvDongia;
        private ImageView img;
        private ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tvTen = itemView.findViewById(R.id.tv_ten_admin);
            tvMota = itemView.findViewById(R.id.tv_mota_admin);
            tvSoluong = itemView.findViewById(R.id.tv_number_admin);
            tvDongia = itemView.findViewById(R.id.tv_dongia_admin);
            img = itemView.findViewById(R.id.img_admin);
            constraintLayout = itemView.findViewById(R.id.constraint_item_admin_product);
        }
    }
}
