package com.example.sd1000application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver
{

    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent)
    {
    //Toast.makeText(context, intent.getAction()+"------startAction------BootBroadcastReceiver-----收到广播消息--------",Toast.LENGTH_LONG).show();
    	
    	
	if (intent.getAction().equals(ACTION))
	{
	    Intent startIntent = new Intent(context, MainActivity.class);
	    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    context.startActivity(startIntent);
	}
    }
}
