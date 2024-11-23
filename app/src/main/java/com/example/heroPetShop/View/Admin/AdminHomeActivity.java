package com.example.heroPetShop.View.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.heroPetShop.R;
import com.example.heroPetShop.View.ChatActivity;
import com.example.heroPetShop.View.SignInActivity;
import com.example.heroPetShop.sildeshow.ManageImagesActivity;

public class AdminHomeActivity extends AppCompatActivity {

    private CardView cHoaDon, cThongKe, cAddProduct, cSignOut, cAddUser, cChat, cslideshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Init();
        Event();

    }

    private void Event() {

        cChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, ChatActivity.class);
                intent.putExtra("message", 1);
                startActivity(intent);
            }
        });

        cAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminUsersActivity.class);
                startActivity(intent);
            }
        });
        cHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminBillMainActivity.class);
                startActivity(intent);
            }
        });

        cThongKe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminChartBillActivity.class);
                startActivity(intent);
            }
        });
        cAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminProductActivity.class);
                startActivity(intent);
            }
        });

        cSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        cslideshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, ManageImagesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Init() {
        cChat = findViewById(R.id.cChat);
        cAddUser = findViewById(R.id.cAddUser);
        cHoaDon = findViewById(R.id.cHoaDon);
        cThongKe = findViewById(R.id.cThongKe);
        cAddProduct = findViewById(R.id.cAddProduct);
        cSignOut = findViewById(R.id.cSignOut);
        cslideshow = findViewById(R.id.cSlideshow);
    }
}