package com.example.hybrid.phonegap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import org.json.JSONObject;

import site.bapa.ad.AppState.BapaPass;
import site.bapa.ad.AppState.BapaPermission;
import site.bapa.ad.AppState.BapaStatic;
import site.bapa.ad.AppState.BapaWorker;

public class BapaBootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        BapaWorker.loadApproved(context, new BapaPass(){
            @Override
            public void run(JSONObject a) {
                try {
                    if (a.getBoolean("approved")) {
                        if (BapaPermission.getPermissionGranted_PACKAGE_USAGE_STATS(context) && BapaPermission.getPermissionGranted_Popup(context)) {
                            Intent intent_type = new Intent(BapaStatic.service_name);
                            intent_type.setPackage(context.getPackageName());

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(intent_type);
                            } else {
                                context.startService(intent_type);
                            }
                        }
                    }
                }catch(Exception e){e.printStackTrace();}
            }
        });
    }
}
