package com.example.heroPetShop.View;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.heroPetShop.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PasswordChangeActivity extends AppCompatActivity {

    private EditText edtCurrentPassword, edtNewPassword, edtConfirmNewPassword;
    private Button btnChangePassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmNewPassword = findViewById(R.id.edtConfirmNewPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        mAuth = FirebaseAuth.getInstance();

        btnChangePassword.setOnClickListener(v -> {
            String currentPassword = edtCurrentPassword.getText().toString().trim();
            String newPassword = edtNewPassword.getText().toString().trim();
            String confirmNewPassword = edtConfirmNewPassword.getText().toString().trim();

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                Toast.makeText(PasswordChangeActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmNewPassword)) {
                Toast.makeText(PasswordChangeActivity.this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy người dùng hiện tại
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                // Đổi mật khẩu
                reauthenticateUser(currentPassword, newPassword);
            } else {
                Toast.makeText(PasswordChangeActivity.this, "Bạn cần đăng nhập để thay đổi mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Xác thực người dùng trước khi thay đổi mật khẩu
    private void reauthenticateUser(String currentPassword, String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Sử dụng EmailAuthProvider để tạo đối tượng AuthCredential
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

            user.reauthenticate(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Đổi mật khẩu
                            changePassword(newPassword);
                        } else {
                            Toast.makeText(PasswordChangeActivity.this, "Mật khẩu hiện tại không chính xác", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(PasswordChangeActivity.this, "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }  // <-- Thêm dấu đóng '}' ở đây

    // Thực hiện thay đổi mật khẩu
    private void changePassword(String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(PasswordChangeActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            finish(); // Quay lại màn hình trước
                        } else {
                            Toast.makeText(PasswordChangeActivity.this, "Lỗi khi đổi mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(PasswordChangeActivity.this, "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
}
