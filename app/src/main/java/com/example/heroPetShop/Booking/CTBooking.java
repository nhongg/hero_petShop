package com.example.heroPetShop.Booking;

public class CTBooking {
    private String idcthdbooking;
    private String tenDichVu;

    private String image;
    private double giaDichVu;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CTBooking() {
        // Default constructor required for Firestore
    }

    public String getIdcthdbooking() {
        return idcthdbooking;
    }

    public void setIdcthdbooking(String idcthdbooking) {
        this.idcthdbooking = idcthdbooking;
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
}

