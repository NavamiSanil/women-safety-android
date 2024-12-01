package com.navami.kavach;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.snackbar.Snackbar;

public class SirenActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;  // To track the play state
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_siren);

        // Initialize the DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayout);

        // Initialize the MediaPlayer with the siren sound
        mediaPlayer = MediaPlayer.create(this, R.raw.siren);

        // Find the ImageView for the siren
        ImageView sirenImageView = findViewById(R.id.sirenImage);
        // Set the siren image
        sirenImageView.setImageResource(R.drawable.siren_icon);

        // Menu Icon Click Listener
        findViewById(R.id.menuIcon).setOnClickListener(view -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                // Stop the sound if it's playing when the menu is opened
                if (isPlaying) {
                    mediaPlayer.pause();
                    mediaPlayer.seekTo(0);
                    isPlaying = false;
                }
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        // Home Button Click Listener to navigate to MainActivity
        findViewById(R.id.menuIcon).setOnClickListener(v -> {
            // Create an Intent to navigate to MainActivity
            Intent intent = new Intent(SirenActivity.this, MainActivity.class);
            startActivity(intent);  // Start MainActivity
            finish(); // Optional: To ensure the user cannot navigate back to the SirenActivity
        });

        // Siren Image Click Listener
        sirenImageView.setOnClickListener(v -> {
            if (isPlaying) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
                isPlaying = false;
            } else {
                mediaPlayer.start();
                isPlaying = true;
            }
        });

        // Dashboard Option Click Listener
        findViewById(R.id.dashboardOption).setOnClickListener(v -> {
            // Create an Intent to navigate to MainActivity
            Intent intent = new Intent(SirenActivity.this, MainActivity.class);
            startActivity(intent);  // Start MainActivity

            Snackbar.make(findViewById(android.R.id.content), "Dashboard Selected", Snackbar.LENGTH_SHORT).show();

            // Close the drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        // Change Number Option Click Listener
        findViewById(R.id.changeNumberOption).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterNumberActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources to prevent memory leaks
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
