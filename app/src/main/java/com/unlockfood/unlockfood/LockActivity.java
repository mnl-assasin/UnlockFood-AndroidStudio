package com.unlockfood.unlockfood;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.unlockfood.unlockfood.activity.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LockActivity extends BaseActivity {

    String TAG = LockActivity.class.getSimpleName();
    @Bind(R.id.adView)
    AdView adView;
    @Bind(R.id.stvSwipeUnlock)
    ShimmerTextView stvSwipeUnlock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        ButterKnife.bind(this);

        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
//        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.d(TAG, "Ad Closed");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Log.d(TAG, "Ad Failed to load Error code: " + errorCode);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.d(TAG, "Ad left application");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                Log.d(TAG, "Ad opened");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d(TAG, "Ad loaded");
            }
        });

        Shimmer shimmer = new Shimmer();
        shimmer.start(stvSwipeUnlock);

        shimmer.setDuration(2000)
                .setStartDelay(300)
                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR);

//        text();
//
//        final PackageManager pm = getPackageManager();
//
//        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//        List<ResolveInfo> appList = pm.queryIntentActivities(mainIntent, 0);
//        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(pm));
//
//        for (ResolveInfo temp : appList) {
//
//            Log.v("my logs", "package and activity name = "
//                    + temp.activityInfo.packageName + "    "
//                    + temp.activityInfo.name);
//
//
//        }
//
//        stvSwipeUnlock.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                startActivity(new Intent(LockActivity.this, PinActivity.class));
////
////                Intent intent = null;
////                final PackageManager packageManager = getPackageManager();
////                for (final ResolveInfo resolveInfo : packageManager.queryIntentActivities(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), PackageManager.MATCH_DEFAULT_ONLY)) {
////                    if (!getPackageName().equals(resolveInfo.activityInfo.packageName))  //if this activity is not in our activity (in other words, it's another default home screen)
////                    {
////                        intent = packageManager.getLaunchIntentForPackage(resolveInfo.activityInfo.packageName);
////                        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER));
////                        break;
////                    }
////                }
//
////                startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER));
//                startActivity(new Intent(Intent.ACTION_MAIN)
//                        .addCategory(Intent.CATEGORY_HOME)
//                        .setPackage(getPackageManager()
//                                .queryIntentActivities(new Intent(Intent.ACTION_MAIN)
//                                                .addCategory(Intent.CATEGORY_HOME),
//                                        PackageManager.MATCH_DEFAULT_ONLY)
//                                .get(0).activityInfo.packageName));
//
//            }
//        });


    }

}
