package com.project.water;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadCast extends BroadcastReceiver {  
    @Override  
    public void onReceive(Context context, Intent intent) {  
        /*  
         * ������������*  
         * Intent service=new Intent(context, MyService.class);  
         * context.startService(service);  
         */  
  
        /*  
         * ����������Activity*  
         * Intent activity=new Intent(context,MyActivity.class);  
         * activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );//���Ӵ˾�ᱨ��  
         * context.startActivity(activity);  
         */  
        /* ����������Ӧ�� */  
        Intent appli = context.getPackageManager().getLaunchIntentForPackage("com.project.water");  
        context.startActivity(appli);  
    }
}