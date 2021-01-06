package spiral.bit.dev.qrscanner.Activities;

import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import spiral.bit.dev.qrscanner.Adapters.TabAccessorAdapter;
import spiral.bit.dev.qrscanner.Other.BaseActivity;
import spiral.bit.dev.qrscanner.R;

public class QrHistoryActivity extends BaseActivity {

    //MADE BY SPIRAL BIT DEVELOPMENT

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAccessorAdapter tabAccessorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_history);

        tabLayout = findViewById(R.id.main_tab);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        tabLayout.getTabAt(0).setText(R.string.scanner);
        tabLayout.getTabAt(1).setText(R.string.generation);
        tabAccessorAdapter = new TabAccessorAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.main_tabs_pager);
        viewPager.setAdapter(tabAccessorAdapter);
    }
}