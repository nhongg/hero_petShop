package com.example.heroPetShop.DichVu;



public class Service {
    private String idDichVu;      // ID dịch vụ (mã định danh)
    private String tenDichVu;     // Tên dịch vụ (vd: Tắm rửa, Cắt móng)
    private String moTa;          // Mô tả chi tiết về dịch vụ
    private double gia;           // Giá của dịch vụ
    private long thoiGian;        // Thời gian thực hiện dịch vụ (phút)
    private boolean hoatDong;     // Trạng thái hoạt động (true = đang mở, false = ngừng cung cấp)

    // Constructor rỗng (bắt buộc cho Firestore)
    public Service() {}

    // Constructor đầy đủ
    public Service(String idDichVu, String tenDichVu, String moTa, double gia, long thoiGian, boolean hoatDong) {
        this.idDichVu = idDichVu;
        this.tenDichVu = tenDichVu;
        this.moTa = moTa;
        this.gia = gia;
        this.thoiGian = thoiGian;
        this.hoatDong = hoatDong;
    }

    // Getters và Setters
    public String getIdDichVu() {
        return idDichVu;
    }

    public void setIdDichVu(String idDichVu) {
        this.idDichVu = idDichVu;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public long getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(long thoiGian) {
        this.thoiGian = thoiGian;
    }

    public boolean isHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(boolean hoatDong) {
        this.hoatDong = hoatDong;
    }
}

