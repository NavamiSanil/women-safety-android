package com.navami.kavach;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SafetyTipsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_tips);

        // Button for video tutorials
        Button videoButton = findViewById(R.id.videoButton);
        videoButton.setOnClickListener(v -> {
            // Open a YouTube playlist or video tutorials link
            String url = "https://youtube.com/playlist?list=PLHfTPxnG4fWq1Wa1vAt8NXnsr9pGmGvQ3&si=3Ym5TBZROx_5c-BX";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });

        // Home Button Click Listener to navigate to MainActivity
        findViewById(R.id.menuSafety).setOnClickListener(v -> {
            // Create an Intent to navigate to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);  // Start MainActivity
            finish(); // Optional: To ensure the user cannot navigate back to the SirenActivity
        });


        // Button for ambulance contact
        Button ambulanceButton = findViewById(R.id.ambulanceButton);
        ambulanceButton.setOnClickListener(this::dialAmbulance);

        // Button for police contact
        Button policeButton = findViewById(R.id.PoliceButton);
        policeButton.setOnClickListener(this::dialPolice);

        // Button for women protection contact
        Button womenProtectionButton = findViewById(R.id.WomenProtection);
        womenProtectionButton.setOnClickListener(this::dialWomanHelpline);
    }

    // Method to open dialer with ambulance number
    public void dialAmbulance(View view) {
        String phoneNumber = "108"; // Ambulance number
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    // Method to open dialer with police number
    public void dialPolice(View view) {
        String phoneNumber = "100"; // Police helpline
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    // Method to open dialer with women protection helpline number
    public void dialWomanHelpline(View view) {
        String phoneNumber = "1091"; // Women protection helpline
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
