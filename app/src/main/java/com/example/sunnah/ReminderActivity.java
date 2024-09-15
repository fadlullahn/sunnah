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
    private float textSize = 16f;
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
            }
        });

        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textSize > 8f) { // Minimum text size
                    textSize -= 2f; // Decrease text size by 2sp
                    etName.setTextSize(textSize);
                    etUsername.setTextSize(textSize);
                }
            }
        });


        playButtonsContainer = findViewById(R.id.play_buttons_container); // Menggunakan ID yang ada
        mediaPlayer = new MediaPlayer();

        // Dapatkan nilai xAudio dari Intent
        Intent intent = getIntent();
        String xAudio = intent.getStringExtra("xAudio");

        if (xAudio != null && !xAudio.isEmpty()) {
            // Menghapus tanda kurung siku dan kutipan, lalu memisahkan file menjadi array
            xAudio = xAudio.replaceAll("[\\[\\]\"]", "");
            String[] audioFiles = xAudio.split(",");

            createPlayButtons(audioFiles);
        }


    }

    private void createPlayButtons(String[] audioFiles) {
        for (int i = 0; i < audioFiles.length; i++) {
            String audioFile = audioFiles[i].trim();
            if (!audioFile.isEmpty()) {
                // Buat tombol baru
                Button playButton = new Button(this);
                playButton.setId(View.generateViewId()); // Menghasilkan ID unik untuk tombol
                playButton.setText("Play");
                playButton.setOnClickListener(v -> playAudio(audioFile));

                // Tambahkan tombol ke container
                playButtonsContainer.addView(playButton);
            }
        }
    }



    private void playAudio(String audioFile) {
        // Stop audio yang sedang diputar
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
