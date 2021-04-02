package com.example.hybrid.phonegap;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


import site.bapa.ad.AppState.BapaListener;
import site.bapa.ad.AppState.BapaPermission;
import site.bapa.ad.AppState.BapaWorker;
public class BapaService extends android.app.Service{
    public BapaWorker bapaWorker = null;
    public void bapaStart(){
        if(bapaWorker != null) bapaWorker.stop();
        bapaWorker = new BapaWorker(this, BuildConfig.DEBUG); // Fixed


        bapaWorker.setAppPackages(null); // Revenue is generated when using all apps
//        ArrayList<String> app_packages = new ArrayList<String>(); // Revenue is generated when using the specified app
//        app_packages.add("com.kakao.talk"); // Epman installed by Google Play that
//        bapaWorker.setAppPackages(app_packages); // Revenue is generated when using the specified app


        // When I move to the next home after using the app for 60 seconds or more, an advertisement occurs.
        // In DEBUG environment, use more than 3 seconds for testing
        bapaWorker.setSecondForPopup(BuildConfig.DEBUG ? 3: 60);
        bapaWorker.setPopupPosition("top"); // top,middle,bottom
        bapaWorker.setCloseButtonPosition("bottom"); // top,bottom

        // bapaWorker.setNotificationBuilder( NotificationCompat.Builder ); // Set notification layout directly

        bapaWorker.setListener(new BapaListener() {
            @Override
            public void onLauncherAppForwarded(String packageName, long apptime) {
                Log.d("onLauncherAppForwarded", packageName+"/"+apptime);
            }

            @Override
            public void onPopupOpened(String packageName, long apptime) {
                Log.d("onPopupOpened", packageName+"/"+apptime);
            }

            @Override
            public void onRewarded(String packageName, long apptime) {
                Log.d("onRewarded", packageName+"/"+apptime);
            }
        });

        bapaWorker.setNotificationIcon(R.mipmap.ic_launcher);
        bapaWorker.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (BapaPermission.getPermissionGranted_PACKAGE_USAGE_STATS(this) && BapaPermission.getPermissionGranted_Popup(this)) {
            bapaStart();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bapaWorker != null) bapaWorker.stop();
        stopSelf();
        bapaWorker.startService();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}