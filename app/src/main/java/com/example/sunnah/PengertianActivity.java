package com.example.sunnah;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PengertianActivity extends AppCompatActivity {

    TextView buttonMenurutUmum,buttonMenurutBahasa,buttonMenurutUlama;
    private float textSize = 16f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengertian);

        buttonMenurutUmum = findViewById(R.id.button_menurut_umum);
        buttonMenurutBahasa = findViewById(R.id.button_menurut_bahasa);
        buttonMenurutUlama = findViewById(R.id.button_menurut_ulama);

        Button zoomInButton = findViewById(R.id.button_zoom_in);
        Button zoomOutButton = findViewById(R.id.button_zoom_out);

        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textSize += 2f; // Increase text size by 2sp
                buttonMenurutUmum.setTextSize(textSize);
                buttonMenurutBahasa.setTextSize(textSize);
                buttonMenurutUlama.setTextSize(textSize);
            }
        });

        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textSize > 8f) { // Minimum text size
                    textSize -= 2f; // Decrease text size by 2sp
                    buttonMenurutUmum.setTextSize(textSize);
                    buttonMenurutBahasa.setTextSize(textSize);
                    buttonMenurutUlama.setTextSize(textSize);
                }
            }
        });
    }
}

