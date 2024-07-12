package com.example.sunnah;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.sunnah.Adapter.AdapterDataSunnah;
import com.example.sunnah.Api.ApiClient;
import com.example.sunnah.Api.ApiInterface;
import com.example.sunnah.Model.Sunnah.DataModel;
import com.example.sunnah.Model.Sunnah.ResponseModel;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button btnPengertian, btnSunnah, btnFavorite, btnReminder, btnAplikasi;
    private RecyclerView rvData;
    private AdapterDataSunnah adData;
    private LinearLayoutManager lmData;
    private List<DataModel> listData = new ArrayList<>();
    private List<DataModel> filteredData = new ArrayList<>();
    private SwipeRefreshLayout srlData;
    private ProgressBar pbData;
    private boolean isSearchActive = false;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnPengertian = findViewById(R.id.btn_pengertian);
        btnSunnah = findViewById(R.id.btn_sunnah);
        btnFavorite = findViewById(R.id.btn_favorite);
        btnReminder = findViewById(R.id.btn_reminder);
        btnAplikasi = findViewById(R.id.btn_aplikasi);



        btnPengertian.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PengertianActivity.class);
            startActivity(intent);
        });

        btnSunnah.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, KategoriDataActivity.class);
            startActivity(intent);
        });

        btnFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SunnahDataActivity.class);
            intent.putExtra("extraData", "1");
            startActivity(intent);
        });

        btnAplikasi.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TentangAplikasiActivity.class);
            startActivity(intent);
        });

        btnReminder.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ReminderDataActivity.class);
            intent.putExtra("extraData", "99");
            startActivity(intent);
        });


        rvData = findViewById(R.id.rv_data);
        srlData = findViewById(R.id.srl_data);
        pbData = findViewById(R.id.pb_data);

        lmData = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvData.setLayoutManager(lmData);

        srlData.setOnRefreshListener(() -> {
            srlData.setRefreshing(true);
            if (isSearchActive) {
                filterData(searchView.getQuery().toString());
            } else {
                retrieveData();
            }
            srlData.setRefreshing(false);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                isSearchActive = true;
                filterData(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(() -> {
            isSearchActive = false;
            filteredData.clear();
            adData.notifyDataSetChanged();
            return false;
        });

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isSearchActive) {
            retrieveData();
        }
    }

    public void retrieveData() {
        pbData.setVisibility(View.VISIBLE);

        ApiInterface ardData = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> tampilData = ardData.ardRetrieveData();

        tampilData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body() != null) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();

                    listData = response.body().getData();
                    filteredData.clear();
                    adData = new AdapterDataSunnah(MainActivity.this, filteredData);
                    rvData.setAdapter(adData);
                    adData.notifyDataSetChanged();
                }

                pbData.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                pbData.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void filterData(String text) {
        filteredData.clear();
        if (text.isEmpty()) {
            adData.notifyDataSetChanged();
            return;
        }
        text = text.toLowerCase();
        for (DataModel item : listData) {
            if (item.getJudul().toLowerCase().contains(text)) {
                filteredData.add(item);
            }
        }
        adData.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
