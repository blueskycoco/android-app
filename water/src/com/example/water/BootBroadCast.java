package com.example.water;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadCast extends BroadcastReceiver {  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        /*  
         * 开机启动服务*  
         * Intent service=new Intent(context, MyService.class);  
         * context.startService(service);  
         */  
  
        /*  
         * 开机启动的Activity*  
         * Intent activity=new Intent(context,MyActivity.class);  
         * activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );//不加此句会报错。  
         * context.startActivity(activity);  
         */  
    	//Log.i("dillon","water recv BOOT_COMPLETED");
        /* 开机启动的应用 */  
       // Intent appli = context.getPackageManager().getLaunchIntentForPackage("com.example.water");  
        //context.startActivity(appli);  
    }
}