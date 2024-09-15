package com.example.sunnah;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {
    EditText editTextHour, editTextMinute;
    Button btnSetNotification;
    private final String CHANNEL_ID = "ReminderChannel";
    private final int NOTIFICATION_ID = 001;

    ImageView HGambar;

    private int xId;
    private String xName, xUsername, xLevel, xPassword,xHour, xMinute, xAudio, xGambar;
    private EditText etLevel, etPassword, etFavorit;
    private TextView etName, etUsername;
    private float textSize = 20f;
    private MediaPlayer mediaPlayer;
    Intent terima;

    private LinearLayout playButtonsContainer; // Container untuk tombol play




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        HGambar = findViewById(R.id.tv_gambar);

        Button zoomInButton = findViewById(R.id.button_zoom_in);
        Button zoomOutButton = findViewById(R.id.button_zoom_out);

        createNotificationChannel();

        editTextHour = findViewById(R.id.editTextHour);
        editTextMinute = findViewById(R.id.editTextMinute);
        btnSetNotification = findViewById(R.id.buttonSetNotification);



        btnSetNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hourString = editTextHour.getText().toString();
                String minuteString = editTextMinute.getText().toString();

                if (!hourString.isEmpty() && !minuteString.isEmpty()) {
                    int hour = Integer.parseInt(hourString);
                    int minute = Integer.parseInt(minuteString);

                    if (isInputValid(hour, minute)) {
                        saveNotificationTime(hour, minute);

                        Toast.makeText(ReminderActivity.this, "Reminder Berhasil Ditambahkan " + hour + ":" + minute, Toast.LENGTH_SHORT).show();

                        scheduleNotification(hour, minute);
                    } else {
                        Toast.makeText(ReminderActivity.this, "Invalid time input", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ReminderActivity.this, "Please enter hour and minute", Toast.LENGTH_SHORT).show();
                }
            }
        });

        terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xName = terima.getStringExtra("xName");
        xUsername = terima.getStringExtra("xUsername");
        xLevel = terima.getStringExtra("xLevel");
        xPassword = terima.getStringExtra("xPassword");
        xHour = terima.getStringExtra("xHour");
        xMinute = terima.getStringExtra("xMinute");
        xAudio = terima.getStringExtra("xAudio");
        xGambar = terima.getStringExtra("xGambar");

        Glide.with(ReminderActivity.this)
                .load(Config.IMAGES_URL + terima.getStringExtra("xGambar"))
                .apply(new RequestOptions().override(550, 550))
                .into(HGambar);

        etName = findViewById(R.id.tv_name);
        etUsername = findViewById(R.id.tv_username);
        etLevel = findViewById(R.id.et_level);
        etPassword = findViewById(R.id.et_password);
        etFavorit = findViewById(R.id.et_favorit);
        editTextHour.setText(xHour);
        editTextMinute.setText(xMinute);


        etName.setText(xName);
        etUsername.setText(xUsername);
        etLevel.setText(xLevel);
        etLevel.setVisibility(View.GONE);
        etPassword.setText(xPassword);
        etPassword.setVisibility(View.GONE);
        editTextHour.setVisibility(View.GONE);
        editTextMinute.setVisibility(View.GONE);

        etFavorit.setVisibility(View.GONE);

        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize += 2f; // Increase text size by 2sp
                etName.setTextSize(textSize);
                etUsername.setTextSize(textSize);
                updateTextViews(); // Update all TextViews in playButtonsContainer
            }
        });

        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textSize > 8f) { // Minimum text size
                    textSize -= 2f; // Decrease text size by 2sp
                    etName.setTextSize(textSize);
                    etUsername.setTextSize(textSize);
                    updateTextViews(); // Update all TextViews in playButtonsContainer
                }
            }
        });



        // Inisialisasi MediaPlayer dan container untuk tombol
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
            etUsername.setVisibility(View.VISIBLE);  // Tampilkan TextView asli
        } else {
            etUsername.setVisibility(View.GONE); // Sembunyikan TextView asli
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
            mediaPlayer.reset();
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



    private void saveNotificationTime(int hour, int minute) {
        SharedPreferences.Editor editor = getSharedPreferences("NotificationPrefs", MODE_PRIVATE).edit();
        editor.putInt("hour", hour);
        editor.putInt("minute", minute);
        editor.apply();
    }

    private boolean isInputValid(int hour, int minute) {
        if (hour < 0 || hour > 23) {
            return false;
        }
        if (minute < 0 || minute > 59) {
            return false;
        }
        return true;
    }

    private void scheduleNotification(int hour, int minute) {
        final long delayMillis = calculateDelay(hour, minute);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                displayNotification();
            }
        }, delayMillis);
    }

    private long calculateDelay(int hourOfDay, int minute) {
        Calendar calendarNow = Calendar.getInstance();
        int currentHour = calendarNow.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendarNow.get(Calendar.MINUTE);

        long currentMillis = calendarNow.getTimeInMillis();
        long selectedMillis;

        // Calculate the milliseconds until the selected time
        Calendar calendarSelected = Calendar.getInstance();
        calendarSelected.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendarSelected.set(Calendar.MINUTE, minute);
        calendarSelected.set(Calendar.SECOND, 0);
        calendarSelected.set(Calendar.MILLISECOND, 0);

        selectedMillis = calendarSelected.getTimeInMillis();

        if (selectedMillis <= currentMillis) {
            // Selected time is before current time, add one day to selected time
            calendarSelected.add(Calendar.DATE, 1);
            selectedMillis = calendarSelected.getTimeInMillis();
        }

        return selectedMillis - currentMillis;
    }

//    private void displayNotification() {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_add)
//                .setContentTitle("Reminder: " + xName )
//                .setContentText(xUsername)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }

    private void displayNotification() {
        // Membuat Intent yang akan membuka ReminderActivity
        Intent intent = new Intent(this, ReminderActivity.class);

        // Tambahkan data yang ingin diteruskan ke ReminderActivity
        intent.putExtra("xId", xId);
        intent.putExtra("xName", xName);
        intent.putExtra("xUsername", xUsername);
        intent.putExtra("xLevel", xLevel);
        intent.putExtra("xPassword", xPassword);
        intent.putExtra("xHour", xHour);
        intent.putExtra("xMinute", xMinute);
        intent.putExtra("xAudio", xAudio);
        intent.putExtra("xGambar", xGambar);

        // Membuat PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Membuat notifikasi dengan pending intent
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle("Reminder: " + xName)
                .setContentText(xUsername)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)  // Set pending intent
                .setAutoCancel(true);  // Notifikasi akan hilang setelah diklik

        // Menampilkan notifikasi
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }



    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Personal Notifications";
            String description = "Include all the personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }


}
