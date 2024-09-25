package com.example.sunnah;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sunnah.Api.ApiClient;
import com.example.sunnah.Api.ApiInterface;
import com.example.sunnah.Model.Sunnah.ResponseModel;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SunnahUbahActivity extends AppCompatActivity {
    private int xId;
    private MediaPlayer mediaPlayer;
    ImageView HGambar;
    private String xName, xUsername, xLevel, xPassword, xFavorit, xAudio;
    private EditText etLevel, etPassword, etFavorit;
    private TextView tvName, tvUsername, tvAudio;
    private Button btnFavorit, btnUnFavorit;
    private String yName, yUsername, yLevel,yPassword, yFavorit;
    private float textSize = 16f;
    private LinearLayout playButtonsContainer; // Container untuk tombol play

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunnah_ubah);

        Button zoomInButton = findViewById(R.id.button_zoom_in);
        Button zoomOutButton = findViewById(R.id.button_zoom_out);

        HGambar = (ImageView) findViewById(R.id.tv_gambar);

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xName = terima.getStringExtra("xName");
        xUsername = terima.getStringExtra("xUsername");
        xLevel = terima.getStringExtra("xLevel");
        xPassword = terima.getStringExtra("xPassword");
        xFavorit = terima.getStringExtra("xFavorit");

        Glide.with(SunnahUbahActivity.this)
                .load(Config.IMAGES_URL + terima.getStringExtra("xGambar"))
                .apply(new RequestOptions().override(550, 550))
                .into(HGambar);

        xAudio = terima.getStringExtra("xAudio");

        tvName = findViewById(R.id.tv_name);
        tvUsername = findViewById(R.id.tv_username);
//        tvAudio = findViewById(R.id.audio_label);


        etLevel = findViewById(R.id.et_level);
        etPassword = findViewById(R.id.et_password);
        etFavorit = findViewById(R.id.et_favorit);
        btnFavorit = findViewById(R.id.btn_favorit);
        btnUnFavorit = findViewById(R.id.btn_unfavorit);

        tvName.setText(xName);
        tvUsername.setText(xUsername);
//        tvAudio.setText(xAudio);

        etLevel.setText(xLevel);
        etLevel.setVisibility(View.GONE);
        etPassword.setText(xPassword);
        etPassword.setVisibility(View.GONE);
//        tvAudio.setVisibility(View.GONE);

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

        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize += 2f; // Increase text size by 2sp
                tvName.setTextSize(textSize);
                tvUsername.setTextSize(textSize);
                updateTextViews(); // Update all TextViews in playButtonsContainer
            }
        });

        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textSize > 8f) { // Minimum text size
                    textSize -= 2f; // Decrease text size by 2sp
                    tvName.setTextSize(textSize);
                    tvUsername.setTextSize(textSize);
                    updateTextViews(); // Update all TextViews in playButtonsContainer
                }
            }
        });

        playButtonsContainer = findViewById(R.id.play_buttons_container);
        mediaPlayer = new MediaPlayer();

        // Dapatkan nilai xAudio dan xUsername dari Intent
        Intent intent = getIntent();
        String xAudio = intent.getStringExtra("xAudio");
        String xUsername = intent.getStringExtra("xUsername");

        // Pastikan nilai xAudio dan xUsername tidak null
        if (xAudio != null && !xAudio.isEmpty() && xUsername != null && !xUsername.isEmpty()) {
            // Menghapus tanda kurung siku dan kutipan, lalu memisahkan file menjadi array
            xAudio = xAudio.replaceAll("[\\[\\]\"]", "");
            String[] audioFiles = xAudio.split(",");

            // Log data untuk debugging
            Log.d("xUsername", "xUsername: " + xUsername);
            Log.d("xAudio", "xAudio: " + xAudio);

            // Gantikan nilai "12345" dengan tombol play
            replaceValuesWithButtons(xUsername, audioFiles);
        }


    }
    private void updateTextViews() {
        // Loop melalui semua tampilan dalam playButtonsContainer
        for (int i = 0; i < playButtonsContainer.getChildCount(); i++) {
            View view = playButtonsContainer.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextSize(textSize);
            }
        }
    }



    private void replaceValuesWithButtons(String xUsername, String[] audioFiles) {
        // Pisahkan teks berdasarkan tanda kurung untuk bagian "((()))"
        String[] parts = xUsername.split("\\(\\(\\)\\)");
        int audioIndex = 0;
        boolean has12345 = false;

        // Proses setiap bagian teks
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];

            // Pisahkan teks bagian ini berdasarkan spasi
            String[] words = part.split("\\s+");
            StringBuilder sentenceBuilder = new StringBuilder();

            for (String word : words) {
                if (word.equals("12345") && audioIndex < audioFiles.length) {
                    // Tambahkan kalimat yang sudah dibangun sebelum tombol play
                    if (sentenceBuilder.length() > 0) {
                        TextView textView = new TextView(this);
                        textView.setText(sentenceBuilder.toString().trim());
                        textView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
                        textView.setTextSize(textSize); // Terapkan ukuran teks
                        playButtonsContainer.addView(textView);
                        sentenceBuilder.setLength(0); // Reset untuk kalimat berikutnya
                    }

                    // Tambahkan tombol play di tempat nilai "12345"
                    String audioFile = audioFiles[audioIndex].trim();
                    createPlayButton(audioFile);
                    has12345 = true;
                    audioIndex++; // Pindah ke audio berikutnya
                } else {
                    // Tambahkan kata ke dalam StringBuilder
                    sentenceBuilder.append(word).append(" ");
                }
            }

            // Tambahkan kalimat terakhir jika ada setelah loop selesai
            if (sentenceBuilder.length() > 0) {
                TextView textView = new TextView(this);
                textView.setText(sentenceBuilder.toString().trim());
                textView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
                textView.setTextSize(textSize); // Terapkan ukuran teks
                playButtonsContainer.addView(textView);
            }

            // Tambahkan baris baru untuk bagian teks setelah tanda kurung
            if (i < parts.length - 1) {
                TextView newlineTextView = new TextView(this);
                newlineTextView.setText(" "); // Kosongkan teks, hanya untuk baris baru
                newlineTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                newlineTextView.setPadding(0, 16, 0, 16); // Memberi jarak vertikal
                playButtonsContainer.addView(newlineTextView);
            }
        }

        // Jika tidak ada nilai "12345" ditemukan, maka tampilkan nilai asli di TextView
        if (!has12345) {
            tvUsername.setVisibility(View.VISIBLE);  // Tampilkan TextView asli
        } else {
            tvUsername.setVisibility(View.GONE); // Sembunyikan TextView asli
        }
        updateTextViews();
    }



    private void createPlayButton(String audioFile) {
        if (!audioFile.isEmpty()) {
            // Buat tombol baru
            Button playButton = new Button(this);
            playButton.setId(View.generateViewId()); // Menghasilkan ID unik untuk tombol
            playButton.setText("|>");

            // Atur lebar Button agar match_parent
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, // lebar penuh
                    LinearLayout.LayoutParams.WRAP_CONTENT  // tinggi otomatis sesuai konten
            );
            playButton.setLayoutParams(params);

            playButton.setOnClickListener(v -> playAudio(audioFile));

            // Tambahkan tombol ke container
            playButtonsContainer.addView(playButton);

            // Log tombol yang dibuat
            Log.d("PlayButton", "Menambahkan tombol untuk file audio: " + audioFile);
        }
    }


    private void playAudio(String audioFile) {
        // Hentikan audio yang sedang diputar jika ada
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();  // Reset setelah stop
        } else {
            mediaPlayer.reset();  // Reset jika tidak sedang diputar, untuk memastikan kondisi siap
        }

        // Siapkan MediaPlayer untuk file audio baru
        String audioUrl = Config.AUDIO_URL + audioFile;
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync(); // Persiapkan secara asinkron
            mediaPlayer.setOnPreparedListener(mp -> mp.start()); // Mulai pemutaran saat siap
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error playing audio: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        // Pastikan MediaPlayer dirilis ketika Activity dihancurkan
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        super.onDestroy();
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