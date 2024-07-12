package com.example.sunnah;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PengertianActivity extends AppCompatActivity {

    private Button buttonMenurutUmum;
    private Button buttonMenurutBahasa;
    private Button buttonMenurutUlama;
    private TextView textViewPengertian;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengertian);

        buttonMenurutUmum = findViewById(R.id.button_menurut_umum);
        buttonMenurutBahasa = findViewById(R.id.button_menurut_bahasa);
        buttonMenurutUlama = findViewById(R.id.button_menurut_ulama);
        textViewPengertian = findViewById(R.id.textViewPengertian);

        buttonMenurutUmum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewPengertian.setText("Menurut umum, Sunnah adalah segala sesuatu yang bersumber dari Nabi Muhammad SAW, baik itu dalam bentuk perkataan, perbuatan, ataupun persetujuan (taqrir). Ini mencakup segala aspek kehidupan Nabi yang dijadikan contoh oleh umat Islam, selain dari perintah yang wajib dilakukan (fardhu) atau larangan yang harus dijauhi (haram). Sunnah mencakup praktik-praktik ibadah, etika, dan kehidupan sehari-hari yang dianjurkan untuk diikuti oleh umat Islam guna mencapai kehidupan yang lebih baik dan mendapatkan keridhaan Allah SWT.");
                hideButtons();
            }
        });

        buttonMenurutBahasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewPengertian.setText("Menurut bahasa, Sunnah berarti 'jalan' atau 'cara'. Ini merujuk pada kebiasaan atau praktik yang dijalani. Sunnah dalam konteks ini menunjukkan jalan atau cara hidup yang diikuti dan dicontohkan oleh Nabi Muhammad SAW. Secara linguistik, istilah ini bisa merujuk pada praktik-praktik yang baik atau buruk, tetapi dalam konteks agama Islam, Sunnah selalu merujuk pada contoh yang baik dan positif dari Nabi.");
                hideButtons();
            }
        });

        buttonMenurutUlama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewPengertian.setText("Menurut ulama, Sunnah adalah segala sesuatu yang bersumber dari Nabi Muhammad SAW, baik itu ucapan, perbuatan, ketetapan, atau sifat yang menjadi teladan bagi umat Islam. Dalam ilmu hadits, Sunnah mencakup apa saja yang diriwayatkan dari Nabi, baik berupa kata-kata, perbuatan, atau persetujuan beliau terhadap tindakan orang lain. Ulama membagi Sunnah ke dalam beberapa kategori, seperti Sunnah Qauliyah (ucapan Nabi), Sunnah Fi'liyah (perbuatan Nabi), dan Sunnah Taqririyah (persetujuan Nabi terhadap perbuatan sahabat). Sunnah digunakan sebagai sumber hukum kedua setelah Al-Quran dalam menetapkan hukum syariah dan tata cara beribadah.");
                hideButtons();
            }
        });
    }

    private void hideButtons() {
        buttonMenurutUmum.setVisibility(View.GONE);
        buttonMenurutBahasa.setVisibility(View.GONE);
        buttonMenurutUlama.setVisibility(View.GONE);
    }
}
