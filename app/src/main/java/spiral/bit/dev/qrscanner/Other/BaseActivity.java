package spiral.bit.dev.qrscanner.Other;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import spiral.bit.dev.qrscanner.Activities.AboutAppActivity;
import spiral.bit.dev.qrscanner.Activities.QrGenerateActivity;
import spiral.bit.dev.qrscanner.Activities.QrHistoryActivity;
import spiral.bit.dev.qrscanner.R;
import spiral.bit.dev.qrscanner.Activities.ScanQrCodeActivity;

public abstract class BaseActivity extends AppCompatActivity {

    //MADE BY SPIRAL BIT DEVELOPMENT

    private static BottomNavigationViewEx bnve;

    public static BottomNavigationViewEx getBottomNav() {
        return bnve;
    }

    public static void hideBottomNav() {
        bnve.setVisibility(View.INVISIBLE);
    }

    public static void unHideBottomNav() {
        bnve.setVisibility(View.VISIBLE);
    }

    public void setUpBottomMenu() {
        bnve = findViewById(R.id.bottom_nav_menu);
        bnve.setTextVisibility(true);
        bnve.setTextSize(12);
        bnve.setItemHeight(200);
        bnve.setIconSize(45f, 45f);
        bnve.setItemRippleColor(ColorStateList.valueOf(Color.YELLOW));
        bnve.enableItemShiftingMode(true);
        bnve.enableShiftingMode(false);
        bnve.enableAnimation(false);

        for (int i = 0; i < bnve.getItemCount(); i++) {
            bnve.setIconTintList(i, null);
        }

        bnve.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.qr_code_scan:
                            Intent toAddIntent = new Intent(getApplicationContext(), ScanQrCodeActivity.class);
                            toAddIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            overridePendingTransition(0, 0);
                            startActivity(toAddIntent);
                        return true;
                    case R.id.qr_code_generate:
                            Intent toProfileIntent = new Intent(getApplicationContext(), QrGenerateActivity.class);
                            toProfileIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            overridePendingTransition(0, 0);
                            startActivity(toProfileIntent);
                        return true;
                    case R.id.qr_code_history:
                            Intent toAddTimeTaskIntent = new Intent(getApplicationContext(), QrHistoryActivity.class);
                            toAddTimeTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            overridePendingTransition(0, 0);
                            startActivity(toAddTimeTaskIntent);
                        return true;
                    case R.id.qr_code_about:
                            Intent toToDoIntent = new Intent(getApplicationContext(), AboutAppActivity.class);
                            toToDoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            overridePendingTransition(0, 0);
                            startActivity(toToDoIntent);
                        return true;
                }
                return false;
            }
        });

    }
}
