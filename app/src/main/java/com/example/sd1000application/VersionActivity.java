package com.example.sd1000application;

import com.realect.sd1000.parameterStatus.*;
import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VersionActivity extends Activity{
	private String Version="";
	private TextView textView_hardware=null;
	private TextView textViewSoftLab=null;
	private TextView textViewHardLab=null;
	private TextView textViewTitle=null;
	private Button button_Cancel=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_version);
		textViewTitle=(TextView)findViewById(R.id.textViewTitle);
		textViewTitle.setText(MidData.IsChinese==1?"版本":"Version");
		textView_hardware=(TextView)findViewById(R.id.textView_hardware);
		textViewSoftLab=(TextView)findViewById(R.id.textViewsoftwarelab);
		textViewSoftLab.setText(MidData.IsChinese==1?"软件版本:":"Software version:");
		textViewHardLab=(TextView)findViewById(R.id.textView3);
		textViewHardLab.setText(MidData.IsChinese==1?"硬件版本:":"Hardware version:");
				
		button_Cancel=(Button)findViewById(R.id.button_Cancel);
		button_Cancel.setOnClickListener(new CancelListener());
		button_Cancel.setText(MidData.IsChinese==1?"确定":"Comfirm");
		try
		{
		    Version="Beta S"+this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionName+"H:";
		}
		catch (NameNotFoundException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		GetSystemNumThread mGetSystemNumThread=new GetSystemNumThread();
		mGetSystemNumThread.start();
	}
	class CancelListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
	class GetSystemNumThread extends Thread
    {

	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    byte order = 'A';
	    byte[] data = new byte[10];
	    data[0] = 'A';
	    MidData.sOp.SendDataToSerial(order, data, 1);
	    try
	    {
		sleep(1000);
	    }
	    catch (InterruptedException e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    runOnUiThread(new Runnable()
	    {

		@Override
		public void run()
		{
		    // TODO Auto-generated method stub
		    /*Toast.makeText(
			    getApplicationContext(),
				    Version+ new String(MidData.m_systemVision),
			    // new String(MidData.systemNum),
			    Toast.LENGTH_LONG).show();*/
		    textView_hardware.setText(Version+ new String(MidData.m_systemVision));
		}
	    });
	}

    }
}
