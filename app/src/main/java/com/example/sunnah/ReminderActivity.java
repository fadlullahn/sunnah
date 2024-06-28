package com.example.sunnah;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {
    EditText editTextHour, editTextMinute;
    Button btnSetNotification;
    private final String CHANNEL_ID = "ReminderChannel";
    private final int NOTIFICATION_ID = 001;

    private int xId;
    private String xName, xUsername, xLevel, xPassword,xHour, xMinute;
    private EditText etName, etUsername, etLevel, etPassword, etFavorit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

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

        Intent terima = getIntent();
        xId = terima.getIntExtra("xId", -1);
        xName = terima.getStringExtra("xName");
        xUsername = terima.getStringExtra("xUsername");
        xLevel = terima.getStringExtra("xLevel");
        xPassword = terima.getStringExtra("xPassword");
        xHour = terima.getStringExtra("xHour");
        xMinute = terima.getStringExtra("xMinute");

        etName = findViewById(R.id.et_name);
        etUsername = findViewById(R.id.et_username);
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

    private void displayNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_add)
                .setContentTitle("Reminder: " + xName )
                .setContentText(xUsername)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

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
