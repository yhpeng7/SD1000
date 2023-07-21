package com.example.sd1000application;

import java.io.IOException;


import com.example.sd1000application.DataActivity.UpData2PcInSD100Byk;
import com.example.sd1000application.DebugActivity.ButtonTouchListener;
import com.example.sd1000application.R.string;
import com.realect.sd1000.parameterStatus.MidData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class SAPortTestActivity extends Activity{
	private TableLayout tl=null;
	boolean isSending=true;
	boolean isWait=true;
	Button buttonOn;
	Button buttonOff;
	Button buttonClean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saporttest);
		tl = (TableLayout)findViewById(R.id.historytable);
		tl.setOrientation(LinearLayout.VERTICAL);
		buttonOn=(Button)findViewById(R.id.buttonOn);		
		buttonOn.setOnClickListener(new buttonOnClickListener());
		buttonOn.setOnTouchListener(new ButtonTouchListener());
		buttonOn.setText(MidData.IsChinese==1?"开始":"Start");
		buttonOff=(Button)findViewById(R.id.buttonOff);
		buttonOff.setOnClickListener(new buttonOffClickListener());
		buttonOff.setOnTouchListener(new ButtonTouchListener());
		buttonOff.setText(MidData.IsChinese==1?"停止":"Stop");
		buttonClean=(Button)findViewById(R.id.buttonClean);
		buttonClean.setOnClickListener(new buttonCleanClickLitener());
		buttonClean.setOnTouchListener(new ButtonTouchListener());
		buttonClean.setText(MidData.IsChinese==1?"清除":"Clean");
		
		ReadThread rth=new ReadThread();
		rth.start();
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		isWait=false;
		isSending=false;
		super.finish();
	}

	class ButtonTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			Button bt=(Button)arg0;
			if(arg1.getAction()==MotionEvent.ACTION_DOWN)
			{
				bt.setBackgroundResource(R.drawable.buttonbackcancel);
			}
			else if(arg1.getAction()==MotionEvent.ACTION_UP)
			{
				bt.setBackgroundResource(R.drawable.buttonbacksel);
			}
			return false;
		}
		
	}
	 public void OpenSerialPortForUpLoadDataToSABySD100()
	    {
		// sPortForSA = MidData.sOpForSA.OpenSerialPortForSAOpenBySD100();
		MidData.portForSA = MidData.sOpForSA.OpenSerialPortForSAOpenBySD100();// MidData.sOp.OpenSerialPort();
		if (MidData.portForSA == null)
		{
		    Toast.makeText(SAPortTestActivity.this,
		    		MidData.IsChinese==1?"连接SA失败，无法上传数据到SA系统(按SD100)":"Connect SA failed, please check connection(SD100)", Toast.LENGTH_SHORT).show();
		    return;
		}
		MidData.m_OutputStream_SA = MidData.portForSA.getOutputStream();
		MidData.m_InputStream_SA = MidData.portForSA.getInputStream();
	    }
	 class buttonCleanClickLitener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			tl.removeAllViews();
			
		}
		 
	 }
	 class buttonOffClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			isSending=false;
		}
		 
		 
	 }
	class buttonOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			OpenSerialPortForUpLoadDataToSABySD100();
			
			if (MidData.m_OutputStream_SA != null)
			{			   
				SendRequestThread sth=new SendRequestThread();
				sth.start();
			}
			else
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"连接SA失败，请确认连接":"Connect SA failed, please check connection", Toast.LENGTH_LONG).show();
				return;
			}
	}
	private class SendRequestThread extends Thread
    {// 每隔1秒发送一个页面请求

	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    while (isSending)
	    {
	    	
	    	byte[] data={'A','B','C','D','E','F','G','H'};
	    	MidData.sOpForSA.SendToSAPort(data, data.length);			
			try
			{
			    Thread.sleep(1000);
			}
			catch (Exception e)
			{
			    // TODO: handle exception
			}
		    }
		}
    }
	}
	
	private void addRow(String str)
	{
		TableRow tbrow=new TableRow(this);
		TextView recive=new TextView(this);
		recive.setTextSize(20);
		//errornum.setGravity(1);
		recive.setText(""+str);				
		recive.setWidth(300);			
		tbrow.addView(recive);
		tl.addView(tbrow);			
	}
	private class ReadThread extends Thread
    {
	@SuppressWarnings("deprecation")
	@Override
	public void run()
	{
	    int Addr = 0;
	    byte[] Ptr = new byte[512];
	    int len=0;
	    super.run();
	    while (isWait)
	    {
			try
			{
				if(MidData.m_InputStream_SA!=null)
				{
					len=MidData.m_InputStream_SA.read(Ptr);
					if(len>0)
					{
						final String Serialdata_St1r = new String(Ptr, 0,len);
						runOnUiThread(new Runnable()
						{
							//final String ss=Serialdata_St1r;
						    public void run()
						    {
						    	
						    	addRow(Serialdata_St1r);
						    }
						});
					}				
				}			
			}
			catch (IOException e)
			{
			    e.printStackTrace();
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}
    }
}
