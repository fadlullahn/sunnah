package com.example.sunnah;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 2500; // Waktu tampilan splash screen dalam milidetik (di sini 2 detik)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Mengatur tampilan menjadi full screen (opsional)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Handler untuk menangani delay sebelum memunculkan MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Menutup SplashScreenActivity agar tidak kembali lagi ketika tombol back ditekan
            }
        }, SPLASH_DELAY);
    }
}
