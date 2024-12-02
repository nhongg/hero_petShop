package com.example.heroPetShop.Booking;


import java.util.Date;

public class Booking {
    private String idBooking;        // ID đặt lịch
    private String tenDichVu;        // Tên dịch vụ
    private String idUser;           // ID người dùng
    private String tenThuCung;       // Tên thú cưng
    private double canNang;          // Cân nặng thú cưng
    private String loaiThuCung;      // Loại thú cưng
    private Date thoiGianDatLich;   // Thời gian đặt lịch (timestamp)
    private String trangThai;        // Trạng thái (Chưa xác nhận, Thành công, Hủy)

    // Constructor rỗng (bắt buộc cho Firestore)
    public Booking() {}

    // Constructor đầy đủ
    public Booking(String idBooking, String tenDichVu, String idUser, String tenThuCung,
                   double canNang, String loaiThuCung, Date thoiGianDatLich, String trangThai) {
        this.idBooking = idBooking;
        this.tenDichVu = tenDichVu;
        this.idUser = idUser;
        this.tenThuCung = tenThuCung;
        this.canNang = canNang;
        this.loaiThuCung = loaiThuCung;
        this.thoiGianDatLich = thoiGianDatLich;
        this.trangThai = trangThai;
    }

    // Getters và Setters
    public String getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getTenThuCung() {
        return tenThuCung;
    }

    public void setTenThuCung(String tenThuCung) {
        this.tenThuCung = tenThuCung;
    }

    public double getCanNang() {
        return canNang;
    }

    public void setCanNang(double canNang) {
        this.canNang = canNang;
    }

    public String getLoaiThuCung() {
        return loaiThuCung;
    }

    public void setLoaiThuCung(String loaiThuCung) {
        this.loaiThuCung = loaiThuCung;
    }

    public Date getThoiGianDatLich() {
        return thoiGianDatLich;
    }

    public void setThoiGianDatLich(Date thoiGianDatLich) {
        this.thoiGianDatLich = thoiGianDatLich;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
