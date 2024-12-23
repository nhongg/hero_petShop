package com.example.heroPetShop.ultil;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.HashMap;
import java.util.Map;

public class OtpSender {
    private static final String TAG = "OtpSender";
    private FirebaseFunctions mFunctions;

    public OtpSender() {
        mFunctions = FirebaseFunctions.getInstance();
    }

    public void sendOtp(String email, Context context, Callback callback) {
        Log.d(TAG, "Email to send OTP 1234: " + email); // Thêm log để kiểm tra giá trị email

        if (email == null || email.isEmpty()) {
            Log.e(TAG, "Invalid email address");
            callback.onFailure(new Exception("Invalid or missing email address :))"));
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        Log.e(TAG, "Data :" + data);

        mFunctions
                .getHttpsCallable("sendOtpEmail") // Tên hàm Firebase Functions
                .call(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        HttpsCallableResult result = task.getResult();
                        Map<String, Object> response = (Map<String, Object>) result.getData();
                        if (response != null && (boolean) response.get("success")) {
                            Object otpObject = response.get("otp");

                            // Kiểm tra kiểu dữ liệu của OTP và xử lý phù hợp
                            int otp;
                            if (otpObject instanceof Double) {
                                otp = ((Double) otpObject).intValue();
                            } else if (otpObject instanceof Integer) {
                                otp = (Integer) otpObject;
                            } else {
                                callback.onFailure(new Exception("OTP không hợp lệ"));
                                return;
                            }

                            saveOtp(context, otp); // Lưu OTP vào SharedPreferences
                            callback.onSuccess(otp);
                        } else {
                            callback.onFailure(new Exception("Lỗi không xác định"));
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });

    }

    // Lưu OTP vào SharedPreferences
    public void saveOtp(Context context, int otp) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("OTP_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("otp", otp);
        editor.apply(); // Lưu lại thay đổi

        // Thiết lập hủy OTP sau 3 phút (180000 ms)
        new android.os.Handler().postDelayed(() -> {
            clearOtp(context); // Xóa OTP sau 3 phút
        }, 180000);
    }

    // Lấy OTP từ SharedPreferences
    public int getOtp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("OTP_Preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("otp", -1); // -1 là giá trị mặc định nếu OTP chưa được lưu
    }

    // Xác thực OTP
    public boolean verifyOtp(Context context, int enteredOtp) {
        int savedOtp = getOtp(context); // Lấy OTP đã lưu từ SharedPreferences
        return enteredOtp == savedOtp;  // So sánh OTP đã nhập với OTP đã lưu
    }

    // Xóa OTP khỏi SharedPreferences
    public void clearOtp(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("OTP_Preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("otp", -1); // Đặt giá trị OTP về -1
        editor.apply();
    }


    public interface Callback {
        void onSuccess(int otp);
        void onFailure(Exception e);
    }
}
