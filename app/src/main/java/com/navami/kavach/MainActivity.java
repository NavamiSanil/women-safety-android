package com.navami.kavach;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;  // To track the siren play state
    private DrawerLayout drawerLayout;

    @Override
    protected void onResume() {
        super.onResume();

        // Check shared preferences for stored ENUM value
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String ENUM = sharedPreferences.getString("ENUM", "NONE");

        if (ENUM != null && ENUM.equalsIgnoreCase("NONE")) {
            // Redirect to RegisterNumberActivity if ENUM is not set
            startActivity(new Intent(this, RegisterNumberActivity.class));
        } else {
            TextView textView = findViewById(R.id.textNum);
            textView.setText(String.format("SOS Will Be Sent To\n%s", ENUM));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayout);

        // Setup notification channel (for Android O and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "MYID", "CHANNELFOREGROUND", NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

        // Sidebar menu click handling
        setupSidebarMenu();

        // Siren Mini ImageView setup
        setupSirenMini();

        // Setup permissions
        requestNecessaryPermissions();
    }

    /**
     * Setup the sidebar menu options and their click listeners.
     */
    private void setupSidebarMenu() {
        // Menu icon click listener to open/close the sidebar
        findViewById(R.id.menuIcon).setOnClickListener(view -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        // Dashboard option
        findViewById(R.id.dashboardOption).setOnClickListener(v -> {
            Snackbar.make(findViewById(android.R.id.content), "Dashboard Selected", Snackbar.LENGTH_SHORT).show();
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        // Change number option
        findViewById(R.id.changeNumberOption).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterNumberActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        findViewById(R.id.safetyTips).setOnClickListener(v -> {
            Intent intent = new Intent(this, SafetyTipsActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        // Siren option
        findViewById(R.id.siren).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SirenActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
        });
    }

    /**
     * Setup the sirenMini ImageView functionality.
     */
    private void setupSirenMini() {
        // Reference the sirenMini ImageView
        ImageView sirenImageViewMini = findViewById(R.id.sirenMini);

        // Set siren icon
        sirenImageViewMini.setImageResource(R.drawable.siren_icon);

        // Initialize MediaPlayer with siren sound
        mediaPlayer = MediaPlayer.create(this, R.raw.siren);

        // Toggle siren sound on click
        sirenImageViewMini.setOnClickListener(v -> {
            if (isPlaying) {
                // Stop the siren
                mediaPlayer.pause();
                mediaPlayer.seekTo(0);  // Reset the sound
                isPlaying = false;
            } else {
                // Start the siren
                mediaPlayer.start();
                isPlaying = true;
            }
        });
    }

    /**
     * Request necessary permissions at runtime.
     */
    private void requestNecessaryPermissions() {
        multiplePermissions.launch(new String[]{
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        });
    }

    /**
     * Handle stop service functionality.
     */
    public void stopService(View view) {
        Intent notificationIntent = new Intent(this, ServiceMine.class);
        notificationIntent.setAction("STOP");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(notificationIntent);
            // Show the Snack bar indefinitely with an action to dismiss it
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Service Stopped!", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("DISMISS", v -> snackbar.dismiss()); // Dismiss when clicked
            snackbar.show();
        }
    }

    public void startServiceV(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Intent notificationIntent = new Intent(this, ServiceMine.class);
            notificationIntent.setAction("START");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getApplicationContext().startForegroundService(notificationIntent);
                // Show the Snackbar indefinitely with an action to dismiss it
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Service Started!", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("DISMISS", v -> snackbar.dismiss()); // Dismiss when clicked
                snackbar.show();
            }
        } else {
            requestNecessaryPermissions();
        }
    }


    /**
     * Release MediaPlayer resources on activity destroy.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    // Permissions result callback
    private final ActivityResultLauncher<String[]> multiplePermissions = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            new ActivityResultCallback<Map<String, Boolean>>() {
                @Override
                public void onActivityResult(Map<String, Boolean> result) {
                    for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                        if (!entry.getValue()) {
                            Snackbar snackbar = Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Permission Must Be Granted!",
                                    Snackbar.LENGTH_INDEFINITE
                            );
                            snackbar.setAction("Grant Permission", v -> {
                                multiplePermissions.launch(new String[]{entry.getKey()});
                                snackbar.dismiss();
                            });
                            snackbar.show();
                        }
                    }
                }
            }
    );
}
