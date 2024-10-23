package com.example.dynamicdemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!this.isTaskRoot()) {
            Intent mainIntent = getIntent();
            String action = mainIntent.getAction();
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                finish();
                return;
            }
        }

        setContentView(R.layout.activity_launcher);

        jumpToLauncher();
    }

    private void jumpToLauncher() {
        Intent intent = new Intent(this, LauncherActivity.class);

        SharedPreferences sp = getSharedPreferences("save", MODE_PRIVATE);
        String changeLogoTime = sp.getString("change", "");
        String resetLogoTime = sp.getString("reset", "");

        Date nowDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String currentTime = sdf.format(nowDate);

        if (!TextUtils.isEmpty(changeLogoTime) && !TextUtils.isEmpty(resetLogoTime)) {
            PackageManager pm = getPackageManager();
            if (currentTime.compareTo(changeLogoTime) >= 0 && currentTime.compareTo(resetLogoTime) <= 0) {
                if (PackageManager.COMPONENT_ENABLED_STATE_DISABLED != pm.getComponentEnabledSetting(new ComponentName(getPackageName(), SplashActivity.class.getName()))) {
                    pm.setComponentEnabledSetting(new ComponentName(SplashActivity.this, SplashActivity.class.getName()),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    pm.setComponentEnabledSetting(new ComponentName(SplashActivity.this, NewSplashActivity.class.getName()),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                }
            } else if (currentTime.compareTo(resetLogoTime) >= 0) {
                if (PackageManager.COMPONENT_ENABLED_STATE_DISABLED != pm.getComponentEnabledSetting(new ComponentName(getPackageName(), NewSplashActivity.class.getName()))) {
                    pm.setComponentEnabledSetting(new ComponentName(SplashActivity.this, NewSplashActivity.class.getName()),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    pm.setComponentEnabledSetting(new ComponentName(SplashActivity.this, SplashActivity.class.getName()),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                }
            }
        }
        startActivity(intent);
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

}
