package com.example.sunnah;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sunnah.Api.ApiClient;
import com.example.sunnah.Api.ApiInterface;
import com.example.sunnah.Model.Sunnah.ResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SunnahUbahActivity extends AppCompatActivity {
    private int xId;
    private String xName, xUsername, xLevel, xPassword, xFavorit;
    private EditText etLevel, etPassword, etFavorit;
    private TextView tvName, tvUsername;
    private Button btnFavorit, btnUnFavorit;
    private String yName, yUsername, yLevel,yPassword, yFavorit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunnah_ubah);

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xName = terima.getStringExtra("xName");
        xUsername = terima.getStringExtra("xUsername");
        xLevel = terima.getStringExtra("xLevel");
        xPassword = terima.getStringExtra("xPassword");
        xFavorit = terima.getStringExtra("xFavorit");

        tvName = findViewById(R.id.tv_name);
        tvUsername = findViewById(R.id.tv_username);

        etLevel = findViewById(R.id.et_level);
        etPassword = findViewById(R.id.et_password);
        etFavorit = findViewById(R.id.et_favorit);
        btnFavorit = findViewById(R.id.btn_favorit);
        btnUnFavorit = findViewById(R.id.btn_unfavorit);

        tvName.setText(xName);
        tvUsername.setText(xUsername);

        etLevel.setText(xLevel);
        etLevel.setVisibility(View.GONE);
        etPassword.setText(xPassword);
        etPassword.setVisibility(View.GONE);

        if (xFavorit.equals("1")) {
            etFavorit.setText("0");
            btnFavorit.setVisibility(View.GONE);
        } else {
            etFavorit.setText("1");
            btnUnFavorit.setVisibility(View.GONE);
        }

        if (xFavorit.equals("99")) {
            btnFavorit.setVisibility(View.GONE);
            btnUnFavorit.setVisibility(View.GONE);
        }

        etFavorit.setVisibility(View.GONE);

        btnFavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yName = tvName.getText().toString();
                yUsername = tvUsername.getText().toString();
                yLevel = etLevel.getText().toString();
                yPassword = etPassword.getText().toString();
                yFavorit = etFavorit.getText().toString();

                updateData();
            }
        });

        btnUnFavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yName = tvName.getText().toString();
                yUsername = tvUsername.getText().toString();
                yLevel = etLevel.getText().toString();
                yPassword = etPassword.getText().toString();
                yFavorit = etFavorit.getText().toString();

                updateData();
            }
        });
    }

    private void updateData(){
        ApiInterface ardData = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseModel> ubahData = ardData.ardUpdateData(xId, yName, yUsername, yLevel,yPassword,yFavorit);

        ubahData.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                int kode = response.body().getKode();
                String pesan = response.body().getPesan();

                Toast.makeText(SunnahUbahActivity.this, "Kode : "+kode+" | Pesan : "+pesan, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(SunnahUbahActivity.this, "Gagal Menghubungi Server | "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}