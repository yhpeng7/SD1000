package com.example.sd1000application;

import java.text.DecimalFormat;

import com.realect.sd1000.parameterStatus.*;
import android.app.Activity;
import android.app.DownloadManager.Request;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class TemperatureActivity extends Activity{
	private TextView textView_tem1=null;
	private TextView textView_tem2=null;
	private TextView textView_tem3=null;
	private TextView textView_tem4=null;
	private TextView textView_tem5=null;
	private Button button_Cancel=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_temperture);
		LoadXML();
		LoadTem();
	}
	private void LoadTem(){
		DecimalFormat fnum2 = new DecimalFormat("##0.0");
		
		textView_tem1.setText("T1:  " + fnum2.format(MidData.Temperature[0])+"¡æ");
		textView_tem2.setText("T2:  " + fnum2.format(MidData.Temperature[1])+"¡æ");
		textView_tem3.setText("T3:  " + fnum2.format(MidData.Temperature[2])+"¡æ");
		textView_tem4.setText("T4:  " + fnum2.format(MidData.Temperature[3])+"¡æ");
		textView_tem5.setText("T5:  " + fnum2.format(MidData.Temperature[4])+"¡æ");
	}
	private void LoadXML()
	{
		button_Cancel=(Button)findViewById(R.id.button_Cancel);
		button_Cancel.setText(MidData.IsChinese==1?"È·¶¨":"Cancel");
		button_Cancel.setOnClickListener(new CancelListener());
		textView_tem1=(TextView)findViewById(R.id.textView_tem1);
		textView_tem2=(TextView)findViewById(R.id.textView_tem2);
		textView_tem3=(TextView)findViewById(R.id.textView_tem3);
		textView_tem4=(TextView)findViewById(R.id.textView_tem4);
		textView_tem5=(TextView)findViewById(R.id.textView_tem5);
	}
	class CancelListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
}
