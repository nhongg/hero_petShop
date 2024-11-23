package com.example.heroPetShop.sildeshow;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heroPetShop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageManageAdapter extends RecyclerView.Adapter<ImageManageAdapter.ImageViewHolder> {
    private Context context;
    private List<Image> imageList;
    private OnImageActionListener listener;

    public interface OnImageActionListener {
        void onEdit(Image image);
        void onDelete(Image image);
    }

    public ImageManageAdapter(Context context, List<Image> imageList, OnImageActionListener listener) {
        this.context = context;
        this.imageList = imageList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image image = imageList.get(position);
        holder.urlTextView.setText(image.getUrl());
        Picasso.get().load(image.getUrl()).into(holder.imageView);

        holder.editButton.setOnClickListener(v -> listener.onEdit(image));
        holder.deleteButton.setOnClickListener(v -> listener.onDelete(image));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView urlTextView;
        ImageView imageView;
        View editButton;
        View deleteButton;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            urlTextView = itemView.findViewById(R.id.urlTextView);
            imageView = itemView.findViewById(R.id.imageView);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
