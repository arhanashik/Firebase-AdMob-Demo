package com.blackspider.firebaseadmobdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class MainActivity extends AppCompatActivity{
    private static final String APP_ID = "ca-app-pub-5700470285392997~9748226300";
    private static final String TEST_APP_ID = "ca-app-pub-3940256099942544~3347511713";

    private Button btnInterstitialAd, btnRewardAdd;
    private AdView mBannerAd;

    private AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    private RewardedVideoAd mRewardedVideoAd;
    private AdListener mAdListener;
    private RewardedVideoAdListener mRewardedVideoAdListener;

    boolean showInterstitialAdd, showRewardVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, TEST_APP_ID);

        btnInterstitialAd = findViewById(R.id.btn_interstitial_ad);
        btnRewardAdd = findViewById(R.id.btn_reward_ad);
        mBannerAd = findViewById(R.id.bannar_ad_view);

        initAd();

        btnInterstitialAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else {
                    showInterstitialAdd = true;
                    Toast.makeText(MainActivity.this, "Ad still loading!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRewardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRewardedVideoAd.isLoaded()) mRewardedVideoAd.show();
                else {
                    showRewardVideo = true;
                    Toast.makeText(MainActivity.this, "Ad still loading!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    private void initAd(){
        adRequest = new AdRequest.Builder().build();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.test_interstitial_ad_unit_id));
        mInterstitialAd.setAdListener(mAdListener);
        mInterstitialAd.loadAd(adRequest);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(mRewardedVideoAdListener);
        loadRewardedVideoAd();

        mBannerAd.loadAd(adRequest);

        mAdListener = new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if(showInterstitialAdd) mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Toast.makeText(MainActivity.this, "Ad closed", Toast.LENGTH_SHORT).show();
            }
        };

        mRewardedVideoAdListener = new RewardedVideoAdListener() {
            @Override
            public void onRewarded(RewardItem reward) {
                Toast.makeText(MainActivity.this, "onRewarded! currency: " + reward.getType() + "  amount: " +
                        reward.getAmount(), Toast.LENGTH_SHORT).show();
                // Reward the user.
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(MainActivity.this, "onRewardedVideoAdLeftApplication",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(MainActivity.this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
                loadRewardedVideoAd();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int errorCode) {
                Toast.makeText(MainActivity.this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(MainActivity.this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
                if(showRewardVideo) mRewardedVideoAd.show();
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(MainActivity.this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(MainActivity.this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd(getString(R.string.test_reward_ad_unit_id),
                new AdRequest.Builder().build());
    }
}
