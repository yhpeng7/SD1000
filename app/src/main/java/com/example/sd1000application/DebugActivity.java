package com.example.sd1000application;

import java.io.IOException;
import java.text.DecimalFormat;

import com.realect.sd.other.*;

import com.realect.sd1000.devicePortOperation.ICCreditCardPort;
import com.realect.sd1000.parameterStatus.MidData;
import com.sankos.*;
import com.sankos.utils.DrawDebugView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DebugActivity extends Activity{
	private DecimalFormat fnum1 = new DecimalFormat("##0.0");
	private int lastDo=0; //上一次动作  1扫描全部   2获取补偿
	float drawGridWidth = 460;//canvas.getWidth();//480;// getWidth() - 30;//缂備焦锚閸╂锟介挊澶婎唺
	float drawGridHeight = 440;//canvas.getHeight();//450;// getHeight() - 40;//缂備焦锚閸╂顨炲Ο鍝勵唺

	float beginX = 0;
	float beginY = 0;
	
	float xSpace = (drawGridWidth)  / 11;
	float ySpace = (drawGridHeight) / 11;
	
	private boolean ICCardOK=false;
	DrawDebugView mDrawDebugView;
	private ImageView showarea;
	private Bitmap baseBitmap;
	private static Canvas canvas;
	private Paint backPaint;
	private Paint linePaint;
	private Paint oldAreaPaint;
	private Paint oldRedPaint;
	private Paint newAreaPaint;
	private Paint textPaint;
	private Paint textLabPaint;
	private Paint labbackPaint;
	
	private static int lastSel=999;
	private Button KaoJiButton;
	private Button ExitDebugButton=null;
	private Button RunPeriodButton=null;
	private Button UpButton=null;
	private Button PauseButton=null;
	private Button DownButton=null;
	private Button PrintButton=null;
	private Button CardButton=null;
	private Button PWMButton=null;
	private Button HeightFitButton=null;
	private Button OtherSetButton=null;
	private Button BackButton=null;
	private Button ScanAllButton=null;
	private Button GetRecompenseButton=null;
	private Button LanguageButton=null;
	private Button SAPortButton=null;
	private Button ConsumerSetButton=null;
	private Button InitialConsumeButton=null;
	private boolean isOnDebugActivity=false;
	private ICCreditCardPort mSerialPortForICCreditCard;
	Intent t;
	int RecompenseTime=0;
	//Intent t;
	//Bundle b;
	private void setButtonAble(boolean fg){
		UpButton.setEnabled(fg);
		PauseButton.setEnabled(fg);
		DownButton.setEnabled(fg);
		PrintButton.setEnabled(fg);
		CardButton.setEnabled(fg);
		PWMButton.setEnabled(fg);
		HeightFitButton.setEnabled(fg);
		OtherSetButton.setEnabled(fg);
		BackButton.setEnabled(fg);
		ScanAllButton.setEnabled(fg);
		GetRecompenseButton.setEnabled(fg);
		
		UpButton.setEnabled(fg);
		KaoJiButton.setEnabled(fg);
		ExitDebugButton.setEnabled(fg);
		RunPeriodButton.setEnabled(fg);
		}
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setFinishOnTouchOutside(false);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_debug);
		mSerialPortForICCreditCard = new ICCreditCardPort();
		//t=getIntent();
		LoadXML();
		InitialChartArea();
		startDebug();
		t=getIntent();
		isOnDebugActivity = true;
		
		//UpdateRecompense();
		Thread updateStatus = new Thread(new Runnable()
		{
		    @Override
		    public void run()
		    {
			// TODO Auto-generated method stub
			while (MidData.m_isInterrupted_LowerMachine && isOnDebugActivity)
			{
			   
			    // System.out.println("-------------UpdateActivtyOn------------");
			    if (MidData.m_isUpdateDebug || MidData.m_isUpdateRecompense)
			    {
				runOnUiThread(new Runnable()
				{
				    public void run()
				    {
					updateStatusShow();
				    }
				});
			    }
			    // MidData.Instance().isUpdateDebug=false;
			    // MidData.Instance().isUpdateRecompense=false;
			    try
			    {
				Thread.sleep(5000);
			    }
			    catch (InterruptedException e)
			    {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			}
		    }
		});
		updateStatus.start();
	}

	private void updateStatusShow()
	    {
		if (MidData.m_isUpdateDebug)
		{
		    UpdateDebug();
		    lastDo=1;
		    MidData.m_isUpdateDebug = false;
		    //ScanAllButton.setClickable(true);
		    setButtonAble(true);
		    MidData.isScallingAllFinish=true;
		}
		else  if (MidData.m_isUpdateRecompense)
	    {
		UpdateRecompense();
		lastDo=2;
		MidData.m_isUpdateRecompense = false;
		RecompenseTime++;
		if(RecompenseTime>=2)
			MidData.isGetRecomFinish=true;
		//GetRecompenseButton.setClickable(true);
		//setButtonAble(true);
	    }
	}
	private void UpdateDebug()
	{
		//MidData.fgCan=4;
		//mDrawDebugView.draw(MidData.m_canvas);
		int len=0;
		for(int i=0;i<11;i++)
		{
			for(int j=0;j<11;j++)
			{
				if((i<5&&j<5)||(i>5&&j<5)||(i<5&&j>5)||(i>5&&j>5))
				{
					//canvas.drawText(datatoString(i), 15+(i+1)*xSpace, 70+5*ySpace, textPaint);
					int num=i*11+j;
					float[] rect=GetRectLast(num);
					int address=len++;
					float tp=Float.parseFloat(MidData.m_debugStatus[address]);
					if(tp<0.8)
						canvas.drawRect(rect[0], rect[1], rect[2], rect[3], num==lastSel?newAreaPaint:oldRedPaint);
					else	
						canvas.drawRect(rect[0], rect[1], rect[2], rect[3], num==lastSel?newAreaPaint:oldAreaPaint);
					
					canvas.drawText(MidData.m_debugStatus[address], j*xSpace+20, i*ySpace+25, textPaint);
					/*int ad=i*11+j;
					int p=ad/11;
					int m=ad/6;
					int h;
					if(ad<=54)
					{
						h=ad-m+p;			
					}
					else
					{
						h=ad-m+p-11;	
					}
					canvas.drawText(Integer.toString(h), j*xSpace+10, i*ySpace+25, textPaint);*/
				}				
			}
		}
		showarea.setImageBitmap(baseBitmap);
	}
	private void UpdateRecompense()
	{
		int len=0;
		for(int i=0;i<11;i++)
		{
			for(int j=0;j<11;j++)
			{
				if((i<5&&j<5)||(i>5&&j<5)||(i<5&&j>5)||(i>5&&j>5))
				{
					//canvas.drawText(datatoString(i), 15+(i+1)*xSpace, 70+5*ySpace, textPaint);
					
					int num=i*11+j;
					float[] rect=GetRectLast(num);
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], num==lastSel?newAreaPaint:oldAreaPaint);
					//int areaNum=GetAddressNum(lastSel);
					String str=fnum1.format(MidData.mTestParameters[len++].aisleRecompenseHeight);
					//System.out.println(str+"--------REc----"+len);


					canvas.drawText(str, j*xSpace+20, i*ySpace+25, textPaint);					
				}				
			}
		}
		showarea.setImageBitmap(baseBitmap);
		//MidData.fgCan=3;
		//mDrawDebugView.draw(MidData.m_canvas);
	}
	 private void startDebug()
	    {
		 byte order1 = 'A';
			byte[] data = new byte[10];
			data[0] = 'E';
			data[1] = 0x31;
			MidData.DriRunTime = 3;
		MidData.sOp.SendDataToSerial(order1, data, 2);
		
		byte order = 'A';
		byte[] buff = new byte[2];
		buff[0] = 'M';
		if(MidData.mSetParameters.TestType==0)
			buff[1] = 0x33;
		else
			buff[1] = 0x30;
		//MidData.DriRunTime = 3;
		MidData.sOp.SendDataToSerial(order, buff, 2);
	    }
	class LanguageButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent t=new Intent();
			t.setClass(DebugActivity.this, LanguageActivity.class);
			startActivity(t);
		}
		
	}
	
	private void LoadXML()
	{
		InitialConsumeButton=(Button)findViewById(R.id.InitialConsumeButton);
		InitialConsumeButton.setOnClickListener(new ConsumerInitialButtonClickListener());
		InitialConsumeButton.setOnTouchListener(new ButtonTouchListener());
		InitialConsumeButton.setText(MidData.IsChinese==1?"耗材初始化":"Recharge init");
		
		ConsumerSetButton=(Button)findViewById(R.id.ConsumerSetButton);
		ConsumerSetButton.setOnClickListener(new ConsumerSetButtonClickListener());
		ConsumerSetButton.setOnTouchListener(new ButtonTouchListener());
		ConsumerSetButton.setText(MidData.IsChinese==1?"耗材设置":"Recharge set");
		
		SAPortButton=(Button)findViewById(R.id.SAPortButton);
		SAPortButton.setOnClickListener(new SAPortButtonClickListener());
		SAPortButton.setOnTouchListener(new ButtonTouchListener());
		SAPortButton.setText(MidData.IsChinese==1?"SA端口测试":"SA Port Test");
		
		LanguageButton=(Button)findViewById(R.id.LanguageButton);
		LanguageButton.setOnClickListener(new LanguageButtonClickListener());
		LanguageButton.setOnTouchListener(new ButtonTouchListener());
		LanguageButton.setText(MidData.IsChinese==1?"语言设置":"Language");
		
		GetRecompenseButton=(Button)findViewById(R.id.GetRecompenseButton);
		GetRecompenseButton.setOnClickListener(new GetRecompenseButtonClickListener());
		GetRecompenseButton.setOnTouchListener(new ButtonTouchListener());
		GetRecompenseButton.setText(MidData.IsChinese==1?"获取补偿":"Adjust");
		
		ScanAllButton=(Button)findViewById(R.id.ScanAllButton);
		ScanAllButton.setOnClickListener(new ScanAllButtonClickListener());
		ScanAllButton.setOnTouchListener(new ButtonTouchListener());
		ScanAllButton.setText(MidData.IsChinese==1?"扫描全部":"Scan All");
		
		BackButton=(Button)findViewById(R.id.BackButton);
		BackButton.setOnClickListener(new BackButtonClickListener());
		BackButton.setOnTouchListener(new ButtonTouchListener());
		BackButton.setText(MidData.IsChinese==1?"返回":"Return");
		
		OtherSetButton=(Button)findViewById(R.id.OtherSetButton);
		OtherSetButton.setOnClickListener(new OtherSetButtonClickListener());
		OtherSetButton.setOnTouchListener(new ButtonTouchListener());
		OtherSetButton.setText(MidData.IsChinese==1?"其他设置":"Other");
		
		HeightFitButton=(Button)findViewById(R.id.HeightFitButton);
		HeightFitButton.setOnClickListener(new HeightFitButtonClickListener());
		HeightFitButton.setOnTouchListener(new ButtonTouchListener());
		HeightFitButton.setText(MidData.IsChinese==1?"高度拟合":"Highly fitting");
		
		PWMButton=(Button)findViewById(R.id.PWMButton);
		PWMButton.setOnClickListener(new PWMButtonClickListener());
		PWMButton.setOnTouchListener(new ButtonTouchListener());
		PWMButton.setText(MidData.IsChinese==1?"蜂鸣器":"Buzzer");
		
		CardButton=(Button)findViewById(R.id.CardButton);
		CardButton.setOnClickListener(new CardButtonClickListener());
		CardButton.setOnTouchListener(new ButtonTouchListener());
		CardButton.setText(MidData.IsChinese==1?"读卡器":"Reader");
		
		PrintButton=(Button)findViewById(R.id.PrintButton);
		PrintButton.setOnClickListener(new PrintButtonClickListener());
		PrintButton.setOnTouchListener(new ButtonTouchListener());
		PrintButton.setText(MidData.IsChinese==1?"打印机":"Print");
		
		DownButton=(Button)findViewById(R.id.DownButton);
		DownButton.setOnClickListener(new DownButtonClickListener());
		DownButton.setOnTouchListener(new ButtonTouchListener());
		DownButton.setText(MidData.IsChinese==1?"下":"Down");
		
		PauseButton=(Button)findViewById(R.id.PauseButton);
		PauseButton.setOnClickListener(new PauseButtonClickListener());
		PauseButton.setOnTouchListener(new ButtonTouchListener());
		PauseButton.setText(MidData.IsChinese==1?"停止":"Stop");
		
		UpButton=(Button)findViewById(R.id.UpButton);
		UpButton.setOnClickListener(new UpButtonClickListener());
		UpButton.setOnTouchListener(new ButtonTouchListener());
		UpButton.setText(MidData.IsChinese==1?"上":"Up");
		
		RunPeriodButton=(Button)findViewById(R.id.RunPeriodButton);
		RunPeriodButton.setOnClickListener(new RunPeriodButtonClickListener());
		RunPeriodButton.setOnTouchListener(new ButtonTouchListener());
		RunPeriodButton.setText(MidData.IsChinese==1?"运行周期":"Run cycle");
		
		ExitDebugButton=(Button)findViewById(R.id.ExitDebugButton);
		ExitDebugButton.setOnClickListener(new ExitDebugButtonClickListener());
		ExitDebugButton.setOnTouchListener(new ButtonTouchListener());
		ExitDebugButton.setText(MidData.IsChinese==1?"退出调试":"Quit debug");
		
		KaoJiButton=(Button)findViewById(R.id.KaoJiButton);
		KaoJiButton.setOnClickListener(new KaoJiButtonOnClickListener());
		KaoJiButton.setOnTouchListener(new ButtonTouchListener());
		KaoJiButton.setText(MidData.IsChinese==1?"拷机":"Torture Test");
		
		//mDrawDebugView=new DrawDebugView(this);
		showarea=(ImageView)findViewById(R.id.DebugArea);
		showarea.setOnTouchListener(new ShowAreaTouchListener());
		int w=showarea.getWidth();
		int h=showarea.getHeight();
		int a=0;
	}
	class SAPortButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent t=new Intent();
			t.setClass(DebugActivity.this, SAPortTestActivity.class);
			startActivity(t);
			
		}
		
	}
	class ConsumerInitialButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent t=new Intent();
			t.setClass(DebugActivity.this, ConfirmConsumeInitial.class);
			startActivity(t);			
		}
		
	}
	class ConsumerSetButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent t=new Intent();
			t.setClass(DebugActivity.this, ConsumeControlActivity.class);
			startActivity(t);			
		}
		
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
	class GetRecompenseButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, set after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.IsReviseRunPeriod)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在调整运行周期，请稍后设置":"Adjusting running cycle, please set later", Toast.LENGTH_LONG).show();
				return;
			}
			byte order = 'A';
		    byte[] data = new byte[10];
		    data[0] = 'g';
		    MidData.sOp.SendDataToSerial(order, data, 1);
		    //setButtonAble(false);
		    Intent intent = new Intent();
		    intent.setClass(DebugActivity.this,
			    MessageShowActivity.class);
		    MidData.isGetRecomFinish=false;
		    Bundle bundle = new Bundle();
		    bundle.putInt("type", 2);
		   
		    intent.putExtras(bundle);
		    startActivityForResult(intent, 0);
		    
		}
		
	}
	class ScanAllButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, set after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.IsReviseRunPeriod)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在调整运行周期，请稍后设置":"Adjusting running cycle, please set later", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.isDebugging==0)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"当前不在调试模式，无法设置":"Not in debug mode currently, can not be set", Toast.LENGTH_LONG).show();
				return;
			}
			setButtonAble(false);
			byte order = 'A';
		    byte[] data = new byte[10];
		    data[0] = 'D';
		    MidData.sOp.SendDataToSerial(order, data, 1);
			/* byte order = 'A';
			 byte[] data = new byte[10];
			 data[0] = 'E';
			 data[1] = 0x30;
			 MidData.sOp.SendDataToSerial(order, data, 2);
			byte[] buff = new byte[2];
			buff[0] = 'M';
			buff[1] = 0x33;
			MidData.DriRunTime = 3;
			MidData.sOp.SendDataToSerial(order, buff, 2);*/
			//ScanAllButton.setClickable(false);
		    MidData.isScallingAllFinish=false;
		    Intent intent=new Intent();
		    intent.setClass(DebugActivity.this,
				    MessageShowActivity.class);
		    Bundle bundle = new Bundle();
		    bundle.putInt("type", 1);
		   
		    intent.putExtras(bundle);
		    startActivityForResult(intent, 0);
		}
		
	}
	class BackButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			setButtonAble(false);
			byte order = 'A';
		    byte[] data = new byte[10];
		    data[0] = 'E';
		    data[1] = 0x30;
		    MidData.sOp.SendDataToSerial(order, data, 2);
		    try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		    t.putExtra("val", Integer.toString(MidData.isDebugging));
			DebugActivity.this.setResult(RESULT_OK, t);
		    
			
			setButtonAble(true);
			DebugActivity.this.finish();
		    //finish();	
		}
		
	}
	private class exitDebugThread extends Thread
    {

	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    byte order = 'A';
	    byte[] data = new byte[10];
	    data[0] = 'E';
	    data[1] = 0x30;
	    MidData.sOp.SendDataToSerial(order, data, 2);
	    
	}

    }
	class OtherSetButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, set after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.isDebugging==0)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"当前不在调试模式，无法设置":"Not in debug mode currently, can not be set", Toast.LENGTH_LONG).show();
				return;
			}
			Intent otherset=new Intent();
			otherset.setClass(DebugActivity.this, OtherSetActivity.class);
			startActivity(otherset);
		}
		
	}
	class HeightFitButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent heightfit=new Intent();
			heightfit.setClass(DebugActivity.this, HeightReviseActivity.class);
			startActivity(heightfit);
		}
		
	}
	class PWMButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			PWM mPWMThread = new PWM();
		    mPWMThread.start();
		}
		
	}
	class CardButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			 Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在检测，请稍候！":"Testing, please wait",
					    Toast.LENGTH_LONG).show();
				    if (!MidData.m_isInterrupted_ICCreditCard)
				    {
					if (!OpenSerialPortForICCreditCard())
					{
					    Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"连接读卡器失败":"Failed to connect readers",
						    Toast.LENGTH_LONG).show();
					    return;
					}
				    }
				    //setButtonAble(false);
				    CardSelfTestThread mCardSelfTestThread = new CardSelfTestThread();
				    mCardSelfTestThread.start();
				    
				    //setButtonAble(true);
		}
		
	}
	 class CardSelfTestThread extends Thread
	    {

		@Override
		public void run()
		{
		    // TODO Auto-generated method stub
		    super.run();
		    for(int i=0;i<128;i++)
				MidData.ICCardSerialPortData[i]=0;
		    
		    
		    SendForSelfTest();
		    
		    String str = "";
		    ReadForGetSelfResult mReadForGetSelfResult=new ReadForGetSelfResult();
		    mReadForGetSelfResult.start();
		    int times=0;
		    while(!ICCardOK&&times<5)
		    {
		    try
		    {
			sleep(1000);
		    }
		    catch (InterruptedException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    times++;
		    }
		    if (ICCardOK)
		    {
			str = MidData.IsChinese==1?"读卡器运行正常！":"Reader operating normally";
		    }
		    else
		    {
			str = MidData.IsChinese==1?"读卡器通信失败！":"Reader communication failure";
		    }
		    
		    final String strshow = str;
		    runOnUiThread(new Runnable()
		    {

			@Override
			public void run()
			{
			    // TODO Auto-generated method stub
			    Toast.makeText(getApplicationContext(), strshow,
				    Toast.LENGTH_LONG).show();
			}
		    });
		    ICCardOK=false;
		}

	    }
	  class ReadForGetSelfResult extends Thread
	    {
		  
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			byte[] Ptr = new byte[128];
			/*try
			{
			    MidData.m_InputStream_ICCreditCard.read(Ptr);
			}
			catch (IOException e)
			{
			    // TODO Auto-generated catch block
			    System.out
				    .println("````````````ReciveError______SerialIC```````````````");
			    e.printStackTrace();
			}*/
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=0;i<128;i++)
				Ptr[i]=MidData.ICCardSerialPortData[i];
			//System.out.println(new String(Ptr) + "________GetSerialNumber");
			if (Ptr[Ptr[0]] == mSerialPortForICCreditCard
				.GetCheckValue(Ptr, Ptr[0]))
			{
			    for(int i=0;i<Ptr.length&&i<5;i++)
			    {
			    if (Ptr[i+0] == 0x4A && Ptr[i+1] == 0x4D&& Ptr[i+2] == 0x59)
			    {
			    	ICCardOK=true;
			    }		    	
				
			    }
			}
		}
	    }
	 public void SendForSelfTest()
	    {// 发送获取序列号的请求
		byte[] buf = new byte[32];
		buf[0] = 0x02;
		buf[1] = 0x10;
		buf[2] = mSerialPortForICCreditCard.GetCheckValue(buf, 2);
		mSerialPortForICCreditCard.DEV_WriteComm(buf, 3);
	    }
	public Boolean OpenSerialPortForICCreditCard()
    {
	MidData.sOpForICCreditCard = new ICCreditCardPort();
	
	MidData.portForICCreditCard = MidData.sOpForICCreditCard
			.OpenSerialPortForICCreditCard();// MidData.Instance().sOp.OpenSerialPort();
	if (MidData.portForICCreditCard == null)
	{
	    Toast.makeText(DebugActivity.this, MidData.IsChinese==1?"连接失败，请检查读卡器连接！":"Reader connection fails, check the connection",
		    Toast.LENGTH_SHORT).show();
	    return false;
	}
	MidData.m_OutputStream_ICCreditCard = MidData.portForICCreditCard.getOutputStream();
	MidData.m_InputStream_ICCreditCard = MidData.portForICCreditCard
		.getInputStream();
	if (MidData.m_InputStream_ICCreditCard != null)
	{
	    MidData.m_isInterrupted_ICCreditCard = true;
	    // MidData.Instance().sOpForICCreditCard.ReciveDataFromSerial();//开启串口接受数据的线程
	    return true;
	}
	else
	{
	    Toast.makeText(DebugActivity.this, MidData.IsChinese==1?"连接失败，请检查读卡器连接":"Reader connection fails, check the connection",
		    Toast.LENGTH_SHORT).show();
	    return false;
	}

    }

	class PrintButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Time t = new Time();
		    t.setToNow();
		    int year = t.year;
		    int month = t.month + 1;

		    int date = t.monthDay;
		    int hour = t.hour;
		    int min = t.minute;
		    String m = month < 10 ? "0" + Integer.toString(month) : Integer
			    .toString(month);
		    String d = date < 10 ? "0" + Integer.toString(date) : Integer
			    .toString(date);
		    String h = hour < 10 ? "0" + Integer.toString(hour) : Integer
			    .toString(hour);
		    String mm = hour < 10 ? "0" + Integer.toString(min) : Integer
			    .toString(min);
		    MidData.isPrintDateStr = Integer.toString(year) + "-" + m + "-" + d;
		
		    MidData.isPrintTimeStr = h + ":" + mm;

		    // String
		    // resourcevalues="";//cursor.getString(cursor.getColumnIndex("resourcedata"));
		    // MidData.Instance().isPrintXueChenValueStr="0";
		    // MidData.Instance().isPrintYaJiValueStr="0";
		    /*
		     * for(int i=0;i<30;i++) {
		     * resourcevalues+=Integer.toString(i+1)+","+"57"+";"; }
		     * resourcevalues+=Integer.toString(30)+","+"57"; //String
		     * xcType="1"; //String
		     * plusPeriod=cursor.getString(cursor.getColumnIndex("plusperiod"));
		     * float mul=(float) 1.0;
		     */
		    // mul=(float)60/(float)Integer.parseInt(plusPeriod);

		    LoadExplanation();
		    LoadEndThePrint();
		    PWM mPWMThread = new PWM();
		    mPWMThread.start();
		    //setButtonAble(true);
		}
		
	}
	 private void LoadEndThePrint()
    {
	byte[] end =
	{ 0x0D };
	MidData.sOpForPrint.SendToPrintPort(end);
	MidData.sOpForPrint.SendToPrintPort(end);
    }
	private void LoadExplanation()
    {
	int row = 0;
	int row_row = 0;
	LoadFont loadfont = new LoadFont();
	for (int i = 0; i < 4; i++)
	{
	    if (i % 2 == 0)
	    {
		byte[] point =
		{ 0x1B, 0x4B, 3, 0, 0, 0, 0, 0x0D };
		MidData.sOpForPrint.SendToPrintPort(point);
		// MidData.Instance().sOp.SendToPrintPort(point);
	    }
	    row = i / 2;
	    row_row = (i - row * 2) % 2;
	    int num = loadfont.LoadTestFontByRow(row, row_row);
	    if (num > 0)
	    {
		int ad = 0;
		byte[] point = new byte[num + 5];
		point[ad++] = 0x1B;
		point[ad++] = 0x4B;
		point[ad++] = (byte) (num);
		point[ad++] = 0;
		for (int j = 0; j < num; j++)
		{
		    point[ad++] = (byte) (MidData.AddressBitmap[j]);
		}
		point[ad++] = 0x0D;
		MidData.sOpForPrint.SendToPrintPort(point);
	    }
	    else
	    {
		byte[] point =
		{ 0x1B, 0x4B, 3, 0, 0, 0, 0, 0x0D };
		MidData.sOpForPrint.SendToPrintPort(point);
	    }
	}
    }
	class DownButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, set after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.IsReviseRunPeriod)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在调整运行周期，请稍后设置":"Adjusting running cycle, please set later", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.isDebugging==0)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"当前不在调试模式，无法设置":"Not in debug mode currently, can not be set", Toast.LENGTH_LONG).show();
				return;
			}
			setButtonAble(false);
		    byte order = 'A';
		    byte[] data = new byte[10];
		    data[0] = 'C';
		    data[1] = 0x32;
		    MidData.sOp.SendDataToSerial(order, data, 2);
		    setButtonAble(true);
		}
		
	}
	class PauseButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, set after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.IsReviseRunPeriod)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在调整运行周期，请稍后设置":"Adjusting running cycle, please set later", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.isDebugging==0)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"当前不在调试模式，无法设置":"Not in debug mode currently, can not be set", Toast.LENGTH_LONG).show();
				return;
			}
			setButtonAble(false);
			byte order = 'A';
		    byte[] data = new byte[10];
		    data[0] = 'C';
		    data[1] = 0x30;
		    MidData.sOp.SendDataToSerial(order, data, 2);
		    setButtonAble(true);
		}		
	}
	class UpButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, set after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.IsReviseRunPeriod)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在调整运行周期，请稍后设置":"Adjusting running cycle, please set later", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.isDebugging==0)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"当前不在调试模式，无法设置":"Not in debug mode currently, can not be set", Toast.LENGTH_LONG).show();
				return;
			}
			setButtonAble(false);
		    byte order = 'A';
		    byte[] data = new byte[10];
		    data[0] = 'C';
		    data[1] = 0x31;
		    MidData.sOp.SendDataToSerial(order, data, 2);
		    setButtonAble(true);
		}
		
	}
	class RunPeriodButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, set after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			Intent runperiod=new Intent();
			runperiod.setClass(DebugActivity.this, RunPeriodActivity.class);
			startActivity(runperiod);
		}
		
	}
	class ExitDebugButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, set after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.IsReviseRunPeriod)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在调整运行周期，请稍后设置":"Adjusting running cycle, please set later", Toast.LENGTH_LONG).show();
				return;
			}
			setButtonAble(false);
			byte order1 = 'A';
		    byte[] data = new byte[10];
		    data[0] = 'E';
		    data[1] = 0x30;
		    MidData.sOp.SendDataToSerial(order1, data, 2);
			
		    try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte order = 'A';
			byte[] buff = new byte[2];
			buff[0] = 'M';
			if (MidData.mSetParameters.TestType==0)
			{
			    if (MidData.mSetParameters.ERSTestTime==0)
			    {
				buff[1] = 0x32;
				MidData.DriRunTime = 2;
			    }
			    else
			    {
				buff[1] = 0x31;
				MidData.DriRunTime = 1;
			    }
			}
			else
			{
			    buff[1] = 0x30;
			    MidData.DriRunTime = 0;
			}
			if (!MidData.sOp.SendDataToSerial(order, buff, 2))
			{
			    Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置失败，请重新设置！":"Set failed, please re-set",
				    Toast.LENGTH_LONG).show();
			    //return;
			}
			else
			{
			    MidData.isDebugging = 0;
			    if (MidData.mSetParameters.ERSTestTime==0)
			    {
				MidData.ERSRealTestTime = (double) (((double) 60 * (double) 60) / MidData.mSetParameters.RunPeriodSet);
			    }
			    else
			    {
				MidData.ERSRealTestTime = (double) (((double) 30 * (double) 60) / MidData.mSetParameters.RunPeriodSet);
			    }
			    MidData.XueChenTestRealTime=Double.toString(MidData.ERSRealTestTime);
			    
			    Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"退出调试模式！":"Out of debug mode",
				    Toast.LENGTH_LONG).show();
			    t.putExtra("val", Integer.toString(MidData.isDebugging));
				DebugActivity.this.setResult(RESULT_OK, t);
			    
				
				DebugActivity.this.finish();
			    //t.putExtra("val", Integer.toString(0));
    			//DebugActivity.this.setResult(RESULT_OK, t);
    			//DebugActivity.this.finish();	
			    
			    //finish();
			}
			setButtonAble(true);
		}
		
	}
	class KaoJiButtonOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, set after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.IsReviseRunPeriod)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在调整运行周期，请稍后设置":"Adjusting running cycle, please wait", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.isDebugging==0)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"当前不在调试模式，无法设置":"Not in debug mode currently, can not be set", Toast.LENGTH_LONG).show();
				return;
			}
			//if(!MidData.m_upDown.equals("上"))
			//{
			setButtonAble(false);
				byte order = 'A';
			    byte[] buff = new byte[2];
			    buff[0] = 'M';
			    buff[1] = 0x34;
			    MidData.DriRunTime = 4;
			    if (!MidData.sOp.SendDataToSerial(order, buff, 2))
			    {
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置失败，请重新设置！":"Set failed, please re-set",
					Toast.LENGTH_LONG).show();
				//return;
			    }
			    else
			    {
				// MidData.DirvceRunTestTime=9999;
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"进入拷机模式！":"Enter the Autorun mode",
					Toast.LENGTH_LONG).show();
				MidData.isDebugging = 2;
			    }
			    setButtonAble(true);
			//}
			//else
			//{
			//	Toast.makeText(getApplicationContext(), "上升过程不能发送指令", Toast.LENGTH_LONG).show();
			//}
		}
		
	}
	class ShowAreaTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			float x=arg1.getX();
			float y=arg1.getY();
			int num=GetNumByXY(x, y);
			if(lastSel<=121)
			{
				MidData.LastArea=GetRectLast(lastSel);
				canvas.drawRect(MidData.LastArea[0], MidData.LastArea[1], MidData.LastArea[2], MidData.LastArea[3], oldAreaPaint);
				int areaNum=GetAddressNum(lastSel);
				if(lastDo==1)
				{
					float tp=Float.parseFloat(MidData.m_debugStatus[areaNum]);
					if(tp<0.8)
						canvas.drawRect(MidData.LastArea[0], MidData.LastArea[1], MidData.LastArea[2], MidData.LastArea[3], oldRedPaint);
					
					canvas.drawText(MidData.m_debugStatus[areaNum], MidData.LastArea[0]+20, MidData.LastArea[1]+25, textPaint);
				}
				else if(lastDo==2)
				{
					canvas.drawText(fnum1.format(MidData.mTestParameters[areaNum].aisleRecompenseHeight),MidData.LastArea[0]+20, MidData.LastArea[1]+25, textPaint);
				}
				showarea.setImageBitmap(baseBitmap);
			}
			if(num%11!=5&&(num<55||num>65))
			{
				lastSel=num;
				MidData.fgCan=2;
				MidData.NewArea=GetRectNew(num);
				
				canvas.drawRect(MidData.NewArea[0], MidData.NewArea[1], MidData.NewArea[2], MidData.NewArea[3], newAreaPaint);
				
				int newNum=GetAddressNum(num);
				if(lastDo==1)
				{
					canvas.drawText(MidData.m_debugStatus[newNum], MidData.NewArea[0]+20, MidData.NewArea[1]+25, textPaint);
				}
				else if(lastDo==2)
				{
					canvas.drawText(fnum1.format(MidData.mTestParameters[newNum].aisleRecompenseHeight),MidData.NewArea[0]+20, MidData.NewArea[1]+25, textPaint);
				}
				showarea.setImageBitmap(baseBitmap);
				
			}
			//Toast.makeText(getApplicationContext(), x+":"+y+","+num, Toast.LENGTH_LONG).show();
			return false;
		}
		
	}
	private int GetAddressNum(int ad){
		
		int p=ad/11;
		int m=ad/6;
		
		if(ad<=54)
		{
			return ad-m+p;			
		}
		else
		{
			return ad-m+p-11;	
		}
	}
	private float[] GetRectLast(int num)
	{		
		float[] rect=new float[4];
		int x=num%11;
		int y=num/11;
		rect[0]=(float) (x*xSpace+1);
		rect[1]=(float) (y*ySpace+1);
		rect[2]=(float) ((x+1)*xSpace-1);
		rect[3]=(float) ((y+1)*ySpace-1);
		return rect;
	}
	private float[] GetRectNew(int num)
	{		
		float[] rect=new float[4];
		int x=num%11;
		int y=num/11;
		rect[0]=(float) (x*xSpace+2);
		rect[1]=(float) (y*ySpace+2);
		rect[2]=(float) ((x+1)*xSpace-2);
		rect[3]=(float) ((y+1)*ySpace-2);
		return rect;
	}
	private int GetNumByXY(float x,float y)
	{
		
		int add_X=(int) (x/xSpace);
		int add_Y=(int) ((y)/ySpace);
		return add_Y*11+add_X;
	}
	private void InitialChartArea()
    {
	//创建一张空白图片
	baseBitmap=Bitmap.createBitmap((int)drawGridWidth,(int)drawGridHeight,Bitmap.Config.ARGB_8888);
	//创建一张画布
	canvas=new Canvas(baseBitmap);
	//画布背景灰色
	canvas.drawColor(Color.WHITE);
	
	//初始化划线画笔
	linePaint = new Paint();
	linePaint.setAntiAlias(true);
	linePaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
	linePaint.setTextSize(12);
	linePaint.setTextAlign(Align.RIGHT);
	linePaint.setStyle(Style.STROKE);
	
	//初始化标签文本画笔
	textLabPaint = new Paint();
	textLabPaint.setAntiAlias(true);
	textLabPaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
	textLabPaint.setTextSize(25);
	textLabPaint.setTextAlign(Align.CENTER);
	//初始化文本画笔
	textPaint = new Paint();
	textPaint.setAntiAlias(true);
	textPaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
	textPaint.setTextSize(12);
	textPaint.setTextAlign(Align.CENTER);
	//初始化新选中画笔
	newAreaPaint = new Paint();
	newAreaPaint.setAntiAlias(true);
	newAreaPaint.setColor(Color.BLUE);// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
	newAreaPaint.setTextSize(25);
	//初始化上次选中画笔
	oldAreaPaint = new Paint();
	//textPaint.setAntiAlias(true);
	oldAreaPaint.setColor(Color.WHITE);// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
	oldAreaPaint.setTextSize(25);	
	//初始化超范围画笔
	oldRedPaint = new Paint();
	//textPaint.setAntiAlias(true);
	oldRedPaint.setColor(Color.RED);// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
	oldRedPaint.setTextSize(25);	
	//初始化标签背景画笔
	labbackPaint = new Paint();
	labbackPaint.setStyle(Style.FILL);
	labbackPaint.setARGB(200, 168, 214, 235);// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
	
	//将灰色背景画上
	canvas.drawBitmap(baseBitmap, new Matrix(), backPaint);
	showarea.setImageBitmap(baseBitmap);
	
	//标定坐标
	for(int i=0;i<10;i++)
	{
		
		if(i<5)
		{
			int num=5+11*i;
			float[] rect=GetRectLast(num);
			
			canvas.drawRect(rect[0], rect[1], rect[2], rect[3], labbackPaint);
			canvas.drawText(Integer.toString(i), 230, 30+i*ySpace, textLabPaint);
		}
			
		else if(i>=5)
		{
			int num=5+11*(i+1);
			float[] rect=GetRectLast(num);
			
			canvas.drawRect(rect[0], rect[1], rect[2], rect[3], labbackPaint);
			canvas.drawText(Integer.toString(i), 230, 30+(i+1)*ySpace, textLabPaint);
		}
			
	}
	for(int i=0;i<10;i++)
	{
		
		if(i<5)
		{
			int num=55+i;
			float[] rect=GetRectLast(num);
			
			canvas.drawRect(rect[0], rect[1], rect[2], rect[3], labbackPaint);
			canvas.drawText(datatoString(i), 20+i*xSpace, 30+5*ySpace, textLabPaint);
		}
			
		else if(i>=5)
		{
			
			int num=56+i;
			float[] rect=GetRectLast(num);
			
			canvas.drawRect(rect[0], rect[1], rect[2], rect[3], labbackPaint);
			canvas.drawText(datatoString(i), 20+(i+1)*xSpace, 30+5*ySpace, textLabPaint);
		}
		
			
	}
	//画背景方格	
		for(int i=0;i<13;i++)
		{
			float axisY_h=ySpace*i;
			
			float axisX_v=xSpace*i;
			//  横线
			canvas.drawLine(0, axisY_h, drawGridWidth, axisY_h,
					linePaint);
			//竖线
			
			canvas.drawLine(axisX_v, beginY, axisX_v, drawGridHeight+beginY,
					linePaint);
		}
	
	float[] rect=GetRectLast(60);
	
	canvas.drawRect(rect[0], rect[1], rect[2], rect[3], labbackPaint);
	Path path=new Path();
	path.moveTo(rect[0], rect[1]);
	path.lineTo(rect[2], rect[3]);
	Path path1=new Path();
	path1.moveTo(rect[2], rect[1]);
	path1.lineTo(rect[0], rect[3]);
	canvas.drawPath(path, linePaint);
	canvas.drawPath(path, linePaint);
	canvas.drawPath(path, linePaint);
	canvas.drawPath(path1, linePaint);
	canvas.drawPath(path1, linePaint);
	canvas.drawPath(path1, linePaint);
	textLabPaint.setTextSize(35);	
	int m = 0;
    }
	private String datatoString(int t)
	{
		switch (t) {
		case 0:
			return "A";
		case 1:
			return "B";
		case 2:
			return "C";
		case 3:
			return "D";
		case 4:
			return "E";
		case 5:
			return "F";
		case 6:
			return "G";
		case 7:
			return "H";
		case 8:
			return "I";
		case 9:
			return "J";
		default:
			return null;
		}
	}
	public boolean dispatchKeyEvent(KeyEvent event)
    {
	// TODO Auto-generated method stub
	if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
	{
	    return true;
	}
	else
	    return super.dispatchKeyEvent(event);
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			Bundle bundle = data.getExtras();
			
			String type = bundle.getString("type");
			String value = bundle.getString("value");
			if (type.equals("1")) {
				if(value.equals("1"))
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"成功扫描全部":"Scan successfully", Toast.LENGTH_LONG).show();
				
				else
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"扫描全部失败":"Scan failed", Toast.LENGTH_LONG).show();
				setButtonAble(true);
			} else if (type.equals("2")) {
				if(value.equals("1"))
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"成功获取补偿":"Get compensation success", Toast.LENGTH_LONG).show();
				else
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"获取补偿失败":"Get compensation failed", Toast.LENGTH_LONG).show();
			} else if (type.equals("3")) {
				
			} 
			break;
		default:
			break;
		}
	}	
}
