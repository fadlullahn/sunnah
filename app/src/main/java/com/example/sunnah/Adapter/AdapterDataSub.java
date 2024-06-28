package com.example.sunnah.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunnah.Api.ApiClient;
import com.example.sunnah.Api.ApiInterface;
import com.example.sunnah.Model.Kategori.DataModelKategori;
import com.example.sunnah.Model.Kategori.ResponseModelKategori;
import com.example.sunnah.R;
import com.example.sunnah.SubUbahActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterDataSub extends RecyclerView.Adapter<AdapterDataSub.HolderData> {
    private Context ctx;
    private List<DataModelKategori> listData;
    private List<DataModelKategori> listUser;
    private int idUser;

    public AdapterDataSub(Context ctx, List<DataModelKategori> listData) {
        this.ctx = ctx;
        this.listData = listData;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_kategori, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModelKategori dm = listData.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvNamaKategori.setText(dm.getNamaKategori());
        holder.tvSubKategori.setText(dm.getSubKategori());

    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvId, tvNamaKategori, tvSubKategori;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvNamaKategori = itemView.findViewById(R.id.tv_kategori);
            tvNamaKategori.setVisibility(View.GONE);
            tvSubKategori = itemView.findViewById(R.id.tv_subkategori);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    idUser = Integer.parseInt(tvId.getText().toString());
                    getData();
                }
            });
        }


        private void getData() {
            ApiInterface ardData = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseModelKategori> ambilData = ardData.ardGetDataKategori(idUser);

            ambilData.enqueue(new Callback<ResponseModelKategori>() {
                @Override
                public void onResponse(Call<ResponseModelKategori> call, Response<ResponseModelKategori> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listUser = response.body().getDataKategori();

                    int varIdUser = listUser.get(0).getId();
                    String varNamaKategori = listUser.get(0).getNamaKategori();
                    String varSubKategori = listUser.get(0).getSubKategori();

                    // Simpan xNamaKategori di SharedPreferences
                    SharedPreferences sharedPref = ctx.getSharedPreferences("my_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("xNamaKategori", varNamaKategori);
                    editor.apply();

                    Intent kirim = new Intent(ctx, SubUbahActivity.class);
                    kirim.putExtra("xId", varIdUser);
                    kirim.putExtra("xNamaKategori", varNamaKategori);
                    kirim.putExtra("xSubKategori", varSubKategori);
                    ctx.startActivity(kirim);
                }

                @Override
                public void onFailure(Call<ResponseModelKategori> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}