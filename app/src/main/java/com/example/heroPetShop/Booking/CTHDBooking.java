package com.example.heroPetShop.Booking;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CTHDBooking implements Serializable {
    private String idcthdbooking;
    private String iduser;
    private List<String> serviceIds; // Chuyển từ String thành List<String>
    private String tenDichVu;
    private double giaDichVu;
    private String tenKhachHang;
    private String tenThuCung;
    private String loaiThuCung;
    private double canNang;
    private Date thoiGianDatLich;
    private String trangThai;
    private String sdtNguoiDung;  // Thêm trường số điện thoại người dùng
    private String idBooking;

    private String lyDoHuy;

    // Constructor, Getter, Setter
    public CTHDBooking() {
    }

    public String getLyDoHuy() {
        return lyDoHuy;
    }

    public void setLyDoHuy(String lyDoHuy) {
        this.lyDoHuy = lyDoHuy;
    }

    public String getSdtNguoiDung() {
        return sdtNguoiDung;
    }

    public void setSdtNguoiDung(String sdtNguoiDung) {
        this.sdtNguoiDung = sdtNguoiDung;
    }

    public String getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getIdcthdbooking() {
        return idcthdbooking;
    }

    public void setIdcthdbooking(String idcthdbooking) {
        this.idcthdbooking = idcthdbooking;
    }

    public List<String> getServiceIds() {
        return serviceIds;
    }

    public void setServiceIds(List<String> serviceId) {
        this.serviceIds = serviceId;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
    }

    public double getGiaDichVu() {
        return giaDichVu;
    }

    public void setGiaDichVu(double giaDichVu) {
        this.giaDichVu = giaDichVu;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getTenThuCung() {
        return tenThuCung;
    }

    public void setTenThuCung(String tenThuCung) {
        this.tenThuCung = tenThuCung;
    }

    public String getLoaiThuCung() {
        return loaiThuCung;
    }

    public void setLoaiThuCung(String loaiThuCung) {
        this.loaiThuCung = loaiThuCung;
    }

    public double getCanNang() {
        return canNang;
    }

    public void setCanNang(double canNang) {
        this.canNang = canNang;
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
