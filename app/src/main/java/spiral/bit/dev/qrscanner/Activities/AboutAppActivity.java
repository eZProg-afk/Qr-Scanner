package spiral.bit.dev.qrscanner.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import spiral.bit.dev.qrscanner.Other.BaseActivity;
import spiral.bit.dev.qrscanner.R;

import static spiral.bit.dev.qrscanner.Other.Helper.deleteCache;

public class AboutAppActivity extends BaseActivity {

    //MADE BY SPIRAL BIT DEVELOPMENT

    private AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        setUpBottomMenu();

        adView = findViewById(R.id.adView);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7780453585001831/8487367717");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
            }
            @Override
            public void onAdOpened() {
            }
            @Override
            public void onAdClicked() {
            }
            @Override
            public void onAdLeftApplication() {
            }
            @Override
            public void onAdClosed() {
            }
        });

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }
            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
            }
            @Override
            public void onAdOpened() {
            }
            @Override
            public void onAdClicked() {
            }
            @Override
            public void onAdLeftApplication() {
            }
            @Override
            public void onAdClosed() {
                Toast.makeText(AboutAppActivity.this, getString(R.string.thanks_toast), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void supportAuthor(View view) {
        if (mInterstitialAd.isLoaded()) mInterstitialAd.show();
    }

    public void estimateApp(View view) {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void clearCache(View view) {
        deleteCache(getApplicationContext());
        Toast.makeText(AboutAppActivity.this, getString(R.string.cache_deleted_toast), Toast.LENGTH_SHORT).show();
    }

    public void exitApp(View view) {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}