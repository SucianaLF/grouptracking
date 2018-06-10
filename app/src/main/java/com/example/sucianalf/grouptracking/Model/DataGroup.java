package com.example.sucianalf.grouptracking.Model;

public class DataGroup {
    private String nama_group;
    private String lokasi;
    private String tanggal_dibuat;
    private String dibuat_oleh;

    public String getDibuat_oleh() {
        return dibuat_oleh;
    }

    public void setDibuat_oleh(String dibuat_oleh) {
        this.dibuat_oleh = dibuat_oleh;
    }

    public DataGroup () {

    }

    public DataGroup(String nama_group, String tanggal_dibuat, String dibuat_oleh) {
        this.nama_group = nama_group;
        this.tanggal_dibuat = tanggal_dibuat;
        this.dibuat_oleh = dibuat_oleh;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getTanggal_dibuat() {
        return tanggal_dibuat;
    }

    public void setTanggal_dibuat(String tanggal_dibuat) {
        this.tanggal_dibuat = tanggal_dibuat;
    }

    public String getNama_group() {
        return nama_group;
    }

    public void setNama_group(String nama_group) {
        this.nama_group = nama_group;
    }
}
