package com.example.sunnah;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.sunnah.Adapter.AdapterDataKategori;
import com.example.sunnah.Api.ApiClient;
import com.example.sunnah.Api.ApiInterface;
import com.example.sunnah.Model.Kategori.DataModelKategori;
import com.example.sunnah.Model.Kategori.ResponseModelKategori;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KategoriDataActivity extends AppCompatActivity {
    private RecyclerView rvData;
    private RecyclerView.Adapter adData;
    private RecyclerView.LayoutManager lmData;
    private List<DataModelKategori> listDataKategori = new ArrayList<>();
    private SwipeRefreshLayout srlData;
    private ProgressBar pbData;
    private FloatingActionButton fabTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori_data);

        rvData = findViewById(R.id.rv_data);
        srlData = findViewById(R.id.srl_data);
        pbData = findViewById(R.id.pb_data);

        lmData = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);


        srlData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlData.setRefreshing(true);
                retrieveDataKategori();
                srlData.setRefreshing(false);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveDataKategori();
    }


    //   Tampilkan Semua Data
    public void retrieveDataKategori() {
        pbData.setVisibility(View.VISIBLE);

        ApiInterface ardData = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModelKategori> tampilData = ardData.ardRetrieveDataKategori();

        tampilData.enqueue(new Callback<ResponseModelKategori>() {
            @Override
            public void onResponse(Call<ResponseModelKategori> call, Response<ResponseModelKategori> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    listDataKategori = response.body().getDataKategori();

                    // Gunakan Set untuk menyimpan kategori unik
                    Set<String> kategoriSet = new HashSet<>();
                    List<DataModelKategori> filteredList = new ArrayList<>();

                    for (DataModelKategori data : listDataKategori) {
                        if (!kategoriSet.contains(data.getNamaKategori())) {
                            kategoriSet.add(data.getNamaKategori());
                            filteredList.add(data);
                        }
                    }

                    adData = new AdapterDataKategori(KategoriDataActivity.this, filteredList);
                    rvData.setAdapter(adData);
                    adData.notifyDataSetChanged();

                    pbData.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(KategoriDataActivity.this, "Respon tidak valid dari server.", Toast.LENGTH_SHORT).show();
                    pbData.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModelKategori> call, Throwable t) {
                Toast.makeText(KategoriDataActivity.this, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                pbData.setVisibility(View.INVISIBLE);
            }
        });
    }
}