package com.example.sunnah.Model.Kategori;

import com.example.sunnah.Model.Sunnah.DataModel;

import java.util.List;

public class ResponseModelKategori {
    private int kode;
    private String pesan;
    private List<DataModelKategori> data;

    public int getKode() {
        return kode;
    }

    public void setKode(int kode) {
        this.kode = kode;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public List<DataModelKategori> getDataKategori() {
        return data;
    }

    public void setDataKategori(List<DataModelKategori> data) {
        this.data = data;
    }
}
