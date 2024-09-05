package com.example.sunnah.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunnah.Api.ApiClient;
import com.example.sunnah.Api.ApiInterface;
import com.example.sunnah.Model.Sunnah.DataModel;
import com.example.sunnah.Model.Sunnah.ResponseModel;
import com.example.sunnah.R;
import com.example.sunnah.ReminderActivity;
import com.example.sunnah.SunnahUbahActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterDataReminder extends RecyclerView.Adapter<AdapterDataReminder.HolderData> {
    private Context ctx;
    private List<DataModel> listData;
    private List<DataModel> listUser;
    private int idUser;

    public AdapterDataReminder(Context ctx, List<DataModel> listData) {
        this.ctx = ctx;
        this.listData = listData;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_sunnah, parent, false);
        HolderData holder = new HolderData(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        DataModel dm = listData.get(position);

        holder.tvId.setText(String.valueOf(dm.getId()));
        holder.tvName.setText(dm.getJudul());
        holder.tvUsername.setText(dm.getDesk());
        holder.tvPassword.setText(dm.getSub());
        holder.tvLevel.setText(dm.getKategori());
        holder.tvFavorit.setText(dm.getFavorit());
        holder.tvGambar.setText(dm.getGambar());
        holder.tvAudio.setText(dm.getAudio());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView tvId, tvName, tvUsername, tvLevel, tvPassword, tvFavorit, tvGambar, tvAudio;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            tvId = itemView.findViewById(R.id.tv_id);
            tvName = itemView.findViewById(R.id.tv_name);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvLevel = itemView.findViewById(R.id.tv_level);
            tvPassword = itemView.findViewById(R.id.tv_password);
            tvFavorit = itemView.findViewById(R.id.tv_favorit);
            tvGambar = itemView.findViewById(R.id.gambar);
            tvAudio = itemView.findViewById(R.id.audio);


            tvLevel.setVisibility(View.GONE);
            tvUsername.setVisibility(View.GONE);
            tvPassword.setVisibility(View.GONE);
            tvFavorit.setVisibility(View.GONE);
            tvGambar.setVisibility(View.GONE);
            tvAudio.setVisibility(View.GONE);

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
            Call<ResponseModel> ambilData = ardData.ardGetData(idUser);

            ambilData.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    int kode = response.body().getKode();
                    String pesan = response.body().getPesan();
                    listUser = response.body().getData();

                    int varIdUser = listUser.get(0).getId();
                    String varName = listUser.get(0).getJudul();
                    String varUsername = listUser.get(0).getDesk();
                    String varLevel = listUser.get(0).getKategori();
                    String varPassword = listUser.get(0).getSub();
                    String varFavorit = listUser.get(0).getFavorit();
                    String varHour = listUser.get(0).getHour();
                    String varMinute = listUser.get(0).getMinute();
                    String varGambar = listUser.get(0).getGambar();
                    String varAudio = listUser.get(0).getAudio();

                    Intent kirim = new Intent(ctx, ReminderActivity.class);
                    kirim.putExtra("xId", varIdUser);
                    kirim.putExtra("xName", varName);
                    kirim.putExtra("xUsername", varUsername);
                    kirim.putExtra("xLevel", varLevel);
                    kirim.putExtra("xPassword", varPassword);
                    kirim.putExtra("xFavorit", varFavorit);
                    kirim.putExtra("xHour", varHour);
                    kirim.putExtra("xMinute", varMinute);
                    kirim.putExtra("xGambar", varGambar);
                    kirim.putExtra("xAudio", varAudio);
                    ctx.startActivity(kirim);
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(ctx, "Gagal Menghubungi Server : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}