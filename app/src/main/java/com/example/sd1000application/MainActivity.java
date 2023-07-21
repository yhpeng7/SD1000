package com.example.sd1000application;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Locale;

import com.realect.sd1000.sqlite.*;

import com.realect.sd1000.Calculate.CalculateFormula;
import com.realect.sd1000.devicePortOperation.ICCardTest;
import com.realect.sd1000.devicePortOperation.ICCreditCardPort;
import com.realect.sd1000.devicePortOperation.LowerMachinePort;
import com.realect.sd1000.devicePortOperation.LowerMachinePort.LoadSelfTestStatusThread;
import com.realect.sd1000.devicePortOperation.PrintPort;
import com.realect.sd1000.devicePortOperation.PrintPortTest;
import com.realect.sd1000.devicePortOperation.SAPort;
import com.realect.sd1000.devicePortOperation.ScanningPort;
import com.realect.sd1000.parameterStatus.MidData;
import com.realect.sd1000.parameterStatus.TestParameters;

import KyleSocket_api.KyleSocketClass;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android_serialport_api.SerialPort;

public class MainActivity extends Activity {
private Button button_Test=null;
private Button button_set=null;
private Button button_ReTest=null;
private Button button_Data=null;
private TextView StatustextView=null;
private TextView textViewDevNum=null;
private TextView textViewDriceLab=null;

private TextView textViewLab=null;
//private TextView textViewDevNumLab=null;
private int XshowTime = 0;
DecimalFormat fnum1 = new DecimalFormat("##0.0");
	@Override
	protected void onCreate(Bundle savedInstanceState) {

    	/*Locale locale = Locale.SIMPLIFIED_CHINESE;
    	Locale.setDefault(locale);*/
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		CreateDB();
		Initial();
		boolean ld=LoadInitial();
		MidData.ErrorStrShow=MidData.IsChinese==1?"��������":"Running well";
		loadXML();		
		if(!ld)
		{
			new AlertDialog.Builder(MainActivity.this).setTitle(MidData.IsChinese==1?"��Ϣ��ʾ":"Notice")
			.setMessage(MidData.IsChinese==1?"���س�ʼ������ʧ�ܣ�����������":"Initialize error, please reboot").setPositiveButton(MidData.IsChinese==1?"ȷ��":"Comfirm", null)
			.setCancelable(false).show();
			StatustextView.setTextColor(Color.RED);
			StatustextView.setText(MidData.IsChinese==1?"���س�ʼ��"+"\n"+"����ʧ�ܣ�"+"\n"+"������������":"Initialize "+"\n"+"error, please��"+"\n"+"reboot��");
			return;
		}
		//System.out.println(MidData.isSql+"---------sql");
		MidData.isOnMain=true;
		if (MidData.m_InputStream_LowerMachine == null)
		    OpenSerialPort();// �򿪴���
		SendStartOrder mSendStartOrder=new SendStartOrder();		
		mSendStartOrder.start();
		if(MidData.mSetParameters.ERSCount<500||MidData.mSetParameters.hematCount<500)
			new AlertDialog.Builder(MainActivity.this).setTitle(MidData.IsChinese==1?"��Ϣ��ʾ":"Notice")
		    .setMessage(MidData.IsChinese==1?"�Թ��������㣬�뼰ʱ��ֵ��":"Insufficient tubes , please recharge").setPositiveButton(MidData.IsChinese==1?"ȷ��":"Comfirm", null)
		    .setCancelable(false).show();		
		MidData.Product_ID = "SD";		
		try
		{
		    File f = new File("/system/Systemconfig");
		    if (f.exists())
		    {
			try
			{
			    File sdCardDir = Environment.getRootDirectory();// Environment.getExternalStorageDirectory();
			    // ��ȡָ���ļ���Ӧ��������
			    FileInputStream fis = new FileInputStream(
				    sdCardDir.getCanonicalPath() + "/Systemconfig");
			    // ��ָ����������װ��BufferedReader
			    BufferedReader br = new BufferedReader(
				    new InputStreamReader(fis));
			    // StringBuilder sb=new StringBuilder("");
			    String line = null;
			    while ((line = br.readLine()) != null)
			    {
					if (line.startsWith("SN="))
					{
					    MidData.Product_ID = line.substring(
						    line.indexOf('=') + 1,
						    line.lastIndexOf('E'));
					    break;
					}
			    }
			    br.close();
			    fis.close();
			}
			catch (Exception e)
			{
			    e.printStackTrace();
			}
			if (MidData.Product_ID == "SD")
			{
			    new AlertDialog.Builder(MainActivity.this).setTitle(MidData.IsChinese==1?"��Ϣ��ʾ":"Notice")
				    .setMessage(MidData.IsChinese==1?"�ؼ�����ȱʧ":"Key data missing").setPositiveButton(MidData.IsChinese==1?"ȷ��":"Comfirm", null)
				    .setCancelable(false).show();			    
			}
		    }
		    else
		    {
			/*new AlertDialog.Builder(MainActivity.this).setTitle(MidData.IsChinese?"��Ϣ��ʾ":"Notice")
				.setMessage(MidData.IsChinese?"�ؼ��ļ�ȱʧ":"key file missing").setPositiveButton(MidData.IsChinese?"ȷ��":"Comfirm", null)
				.setCancelable(false).show();*/
		    }
		}		
		catch (Exception e)
		{
		    // TODO: handle exception
		}
		textViewDevNum.setText(MidData.IsChinese==1?"������ţ�"+MidData.Product_ID:"SN��"+MidData.Product_ID);
		Thread updateStatus = new Thread(new Runnable()
		{
		    @Override
		    public void run()
		    {
			// TODO Auto-generated method stub
			while (MidData.m_isInterrupted_LowerMachine)
			{
			    if (XshowTime > 49 && MidData.isOnMain)
			    {
				XshowTime = 0;
				runOnUiThread(new Runnable()
				{
				    public void run()
				    {					
					    updateStatusShow();					
				    }
				});
			    }
			    try
			    {
				Thread.sleep(20);
			    }
			    catch (InterruptedException e)
			    {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			    XshowTime++;
			}
		    }
		});
		updateStatus.start();
		
	}
	public void setTempOk(){
		MidData.startTemTestOK=true;
	}
	public void updateStatusShow(){
		String str;
		
		if(MidData.TestFinished)
		{
			if(MidData.TemperatureErrorShow.equals(""))
				str=MidData.IsChinese==1?MidData.StatusStrShow+"\n"+"�������¶ȣ�"+fnum1.format(CalculateFormula.AnverageTem(MidData.Temperature))+"��"+"\n"+MidData.ErrorStrShow:MidData.StatusStrShow+"\n"+"Test area temperature��"+fnum1.format(CalculateFormula.AnverageTem(MidData.Temperature))+"��"+"\n"+MidData.ErrorStrShow;
			else
				str=MidData.StatusStrShow+"\n"+MidData.TemperatureErrorShow+"\n"+MidData.ErrorStrShow;
			if(!MidData.startTemTestFinish)
			{
				MidData.startTemTestFinish=true;
				if(!MidData.startTemTestOK)
				{					
					final boolean tempOK=false;
					new AlertDialog.Builder(MainActivity.this)
				    .setTitle(MidData.IsChinese==1?"��Ϣ��ʾ":"Notice")
				    .setCancelable(false)
				    .setMessage(MidData.IsChinese==1?"�����¶ȳ�����Ч�Ĳ��Է�Χ�����ܵ��²��Խ����׼ȷ���Ƿ�Ҫ��������?":"Result maybe inaccurate due to high room temperature,contiunue to test?")
				    .setPositiveButton(MidData.IsChinese==1?"��":"YES",
					    new DialogInterface.OnClickListener()
					    {
				    	
						public void onClick(
							DialogInterface dialoginterface, int i)
						{
							MidData.startTemTestOK=true;
							Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�������Խ�����µ����⣬�û����и���":"User is responsible for consequences", Toast.LENGTH_LONG).show();
						    
						}
					    }).setNegativeButton(MidData.IsChinese==1?"��":"No", new DialogInterface.OnClickListener()
					    {

						public void onClick(
							DialogInterface dialoginterface, int i)
						{
							Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�뽫��������10-30��Ļ����£���������������ʹ��":"Please keep analyzer at 10-30��  environment��restart to use", Toast.LENGTH_LONG).show();
							
						}
					    }).show();
				}
			}
			
			if(!MidData.startTemTestFinish1)
			{
				MidData.startTemTestFinish1=true;
				if(!MidData.startTemTestOK1)
				{
					final boolean tempOK1=false;
					new AlertDialog.Builder(MainActivity.this)
				    .setTitle(MidData.IsChinese==1?"��Ϣ��ʾ":"Notice")
				    .setCancelable(false)
				    .setMessage(MidData.IsChinese==1?"�������¶��쳣�����ܵ��²��Խ����׼ȷ���Ƿ�Ҫ��������?":"Temp error with test channel��result  may be affected,continue testing?")
				    .setPositiveButton(MidData.IsChinese==1?"��":"YES",
					    new DialogInterface.OnClickListener()
					    {
				    	
						public void onClick(
							DialogInterface dialoginterface, int i)
						{
							MidData.startTemTestOK1=true;
							Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�������Խ�����µ����⣬�û����и���":"User is responsible for consequences.", Toast.LENGTH_LONG).show();
						    
						}
					    }).setNegativeButton(MidData.IsChinese==1?"��":"No", new DialogInterface.OnClickListener()
					    {

						public void onClick(
							DialogInterface dialoginterface, int i)
						{
							Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�����¶ȴ���������������������ʹ��":"Check temp sensor��restart analyzer.", Toast.LENGTH_LONG).show();
						}
					    }).show();
				}
			
			}
		}
		else 
			str=MidData.IsChinese==1?"\n"+MidData.StatusStrShow+"\n"+"�뽫���Կ��ڵ������Թ�ȡ��":"\n"+MidData.StatusStrShow+"\n"+"Please remove all tubes";
		StatustextView.setText(str);
	}
	  private void Initial(){
    	for(int i=0;i<100;i++)
    	{
    		TestParameters mm=new TestParameters();
    		mm.testStatus=0;
    		mm.aisleNumShow="";
    		mm.showEndOfNotSumTime=false;
    		mm.aisleNum="";
    		mm.ERSResult="0.00";
    		mm.hematResult="0.00";
    		mm.IsNewSample=true;
    		mm.ReDetect=false;
    		mm.RealReDetect=false;
    		mm.areaTemp="";
    		mm.aislePastTestData="";
    		MidData.mTestParameters[i]=mm;    		
    	}    	
    }
    class SendStartOrder extends Thread
    {	
	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    MidData.m_isRecived = false;
	    byte buff = 0x41;
		byte[] data = new byte[10];
		data[0] = 'B';
		data[1] = Integer.toHexString(MidData.DriRunTime).getBytes()[0];
		CalculateFormula mtc = new CalculateFormula();

		byte[] pulusetimeset = mtc.StringTo2Bytes(MidData.mSetParameters.RunPeriodSet);
		data[2] = pulusetimeset[0];
		data[3] = pulusetimeset[1];
	    while (true)
	    {
		if (!MidData.m_isRecived)
		{
		    MidData.sOp.SendData(buff, data, 4,MidData.m_OutputStream_LowerMachine);
		}
		else
		{
		    MidData.m_isRecived = false;
		    break;
		}
		try
		{
		    Thread.sleep(500);
		}
		catch (Exception e)
		{
		    // TODO: handle exception
		}
	    }
	}
    }
	public void OpenSerialPort()
    {// �򿪴���
		
		MidData.sOpForPrint = new PrintPort();
		MidData.sOpForScanner = new ScanningPort();
		MidData.sOpForSA = new SAPort();
	OpenSerialPortForDri();

	OpenSerialPortForPrint();
	OpenSerialPortForICCreditCard();
	OpenSerialPortForScanner();
	// OpenSerialPortForUpLoadDataToSA();//�����ϴ��˿� ��Ҫʱ�ٴ�

    }
	 private boolean LoadInitial()
	    {
		LoadInitialData loadinitial = new LoadInitialData();
		if(loadinitial.LoadAddressSampleNum())
		{
			if(loadinitial.LoadSystemSet())
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
		
	    }
	 	public static boolean reboot=false;
	 	public void OpenSerialPortForICCreditCard()
	 	{
		MidData.sOpForICCreditCard = new ICCreditCardPort();
		MidData.portForICCreditCard = MidData.sOpForICCreditCard
			.OpenSerialPortForICCreditCard();
		if (MidData.portForICCreditCard == null)
		{
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"���Ӷ�����ʧ�ܣ�������������ӣ�":"Reader connection fails, check the connection",
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		
		MidData.m_OutputStream_ICCreditCard = MidData.portForICCreditCard.getOutputStream();
		MidData.m_InputStream_ICCreditCard = MidData.portForICCreditCard.getInputStream();
		
		if (MidData.m_InputStream_ICCreditCard != null)
		{			
			
		    MidData.m_isInterrupted_ICCreditCard = true;
		    MidData.sOpForICCreditCard.ReciveDataFromSerial();// �������ڽ������ݵ��߳�
		    
		}
		else
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"���Ӷ�����ʧ�ܣ��������������":"Reader connection fails, check the connection",
			    Toast.LENGTH_SHORT).show();
    }

	    public void OpenSerialPortForDri()
	    {
	    SerialPort sPortForCom = null;
	    MidData.sOp = new LowerMachinePort();
		sPortForCom = MidData.sOp.OpenSerialPort();
		MidData.port = sPortForCom;// MidData.sOp.OpenSerialPort();
		if (MidData.port == null)
		{
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"COM����ʧ�ܣ��޷���ײ�Ӳ���豸ͨ�ţ�":"COM connection fails, can not communicate with the underlying hardware",
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		MidData.m_OutputStream_LowerMachine = sPortForCom.getOutputStream();
		MidData.m_InputStream_LowerMachine = sPortForCom.getInputStream();
		if (MidData.m_InputStream_LowerMachine != null)
		{
		    MidData.m_isInterrupted_LowerMachine = true;
		    MidData.sOp.ReciveDataFromSerial();// �������ڽ������ݵ��߳�
		}
		else
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"COM����ʧ�ܣ��޷���ײ�Ӳ���豸ͨ��":"COM connection fails, can not communicate with the underlying hardware",
			    Toast.LENGTH_SHORT).show();
	    }

	    public void OpenSerialPortForPrint()
	    {
	    SerialPort sPortForPrint = null;
		sPortForPrint = MidData.sOpForPrint.OpenSerialPortForPrint();
		MidData.portForPrint = sPortForPrint;// MidData.sOp.OpenSerialPort();
		if (MidData.portForPrint == null)
		{
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"��ӡ������ʧ�ܣ��޷����д�ӡ":"Printer connection fails, can not print",
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		MidData.m_OutputStream_Print = sPortForPrint.getOutputStream();
		MidData.m_InputStream_Print = sPortForPrint.getInputStream();
		if (MidData.m_InputStream_Print != null)
		{
		    MidData.m_isInterrupted_Print = true;
		    // MidData.sOp.ReciveDataFromSerial();//�������ڽ������ݵ��߳�
		}
		else
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"��ӡ������ʧ�ܣ��޷����д�ӡ":"Printer connection fails, can not print",
			    Toast.LENGTH_SHORT).show();
	    }

	    public void OpenSerialPortForScanner()
	    {
	    SerialPort sPortForScanner = null;
		sPortForScanner = MidData.sOpForScanner.OpenSerialPortForScanner();
		MidData.portForScanner = sPortForScanner;// MidData.sOp.OpenSerialPort();
		if (MidData.portForScanner == null)
		{
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"ɨ��ǹ����ʧ�ܣ��޷�����ɨ��":"Scanner connection fails, can not scan",
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		MidData.m_OutputStream_Scanner = sPortForScanner.getOutputStream();
		MidData.m_InputStream_Scanner = sPortForScanner.getInputStream();
		if (MidData.m_InputStream_Scanner != null)
		{
		    MidData.m_isInterrupted_Scanner = true;
		    MidData.sOpForScanner.ReciveDataFromSerial();// �������ڽ������ݵ��߳�
		}
		else
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"ɨ��ǹ����ʧ�ܣ��޷�����ɨ��":"Scanner connection fails, can not scan",
			    Toast.LENGTH_SHORT).show();
	    }

	private void CreateDB()
    {
	MidData.sqlOp = new SQLite(getBaseContext(), "SD1000DayaBase.db", null,1);
	SQLiteDatabase db = null;
	db = MidData.sqlOp.getWritableDatabase();
	MidData.sqlOp.onCreateDataBase(db);
	db.close();
    }
    private void loadXML()
    {    	    	
    	textViewLab=(TextView)findViewById(R.id.textViewLab);
    	textViewLab.setWidth(550);
    	textViewLab.setBackgroundResource(MidData.IsChinese==1?R.drawable.logoc:R.drawable.logoe);
    	
    	
    	textViewDriceLab=(TextView)findViewById(R.id.textViewDriceLab);
    	textViewDriceLab.setText(MidData.IsChinese==1?"SD-1000��̬Ѫ��/ѹ��������":"SD-1000 Automated ESR Analyer");
    	
    	button_Test=(Button)findViewById(R.id.button_Test);
    	button_Test.setOnClickListener(new button_TestClickListener());
    	button_Test.setOnTouchListener(new ButtonTouchListener());
    	button_Test.setText(MidData.IsChinese==1?"����":"TEST");
    	    	
    	button_Data=(Button)findViewById(R.id.button_Data);
    	button_Data.setOnClickListener(new DataButtonOnClickListener());
    	button_Data.setOnTouchListener(new ButtonTouchListener());
    	button_Data.setText(MidData.IsChinese==1?"����":"DATA");
    	
    	button_ReTest=(Button)findViewById(R.id.button_ReTest);
    	button_ReTest.setOnClickListener(new ReTestButtonOnClickListener());
    	button_ReTest.setOnTouchListener(new ButtonTouchListener());
    	button_ReTest.setText(MidData.IsChinese==1?"����\n�Լ�":"Self\nTest");
    	
    	textViewDevNum=(TextView)findViewById(R.id.textViewDevNum);
    	StatustextView=(TextView)findViewById(R.id.StatustextView);
    	
    	button_set=(Button)findViewById(R.id.button_set);
    	button_set.setOnClickListener(new SetButtonOnClick());
    	button_set.setOnTouchListener(new ButtonTouchListener());
    	button_set.setText(MidData.IsChinese==1?"����":"SETUP");
    } 
    class ButtonTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			Button bt=(Button)arg0;
			if(arg1.getAction()==MotionEvent.ACTION_DOWN)
			{
				bt.setBackgroundResource(R.drawable.mainbuttonbackhui);
			}
			else if(arg1.getAction()==MotionEvent.ACTION_UP)
			{
				bt.setBackgroundResource(R.drawable.mainbuttonback);
			}
			return false;
		}    	
    }
    
    class button_TestClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent test=new Intent();
			test.setClass(MainActivity.this, TestUpActivity.class);
			startActivity(test);
			
		}
    	
    }
    
    class DataButtonOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent set=new Intent();
			set.setClass(MainActivity.this, DataActivity.class);//
			startActivity(set);
		}
    	
    }
    class ReTestButtonOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(MidData.IsReviseRunPeriod)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���ڵ����������ڣ����Ժ�����":"Adjusting running cycle, please wait", Toast.LENGTH_LONG).show();
				return;
			}
			int run = 0;
		    for (int k = 0; k < 100; k++)
		    {
			if (MidData.TestFinished)
			{
			    if (MidData.mTestParameters[k].aisleStatus == 1)
			    {
					boolean f2 = MidData.mTestParameters[k].testStatus==1||MidData.mTestParameters[k].testStatus==2 ? true
						: false;
		
					boolean f3 = !MidData.mTestParameters[k].aisleTestFinished ? true
						: false;
					if (f2 && f3)
					{
					    run = 1;
					    break;
					}
			    }
			}
			else
			{
			    run = 2;
			    break;
			}
		    }
		    if (run == 1)
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���ڲ���,���ڼ��������ã�":"Being tested, please set up after test",
				Toast.LENGTH_LONG).show();
			return;
		    }
		    else if (run == 2)
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�Լ�δ��ɣ������Լ���ɺ����ã�":"Self-test is not completed, set after the completion of the self-test",
				Toast.LENGTH_LONG).show();
			return;
		    }

		    for (int ii = 0; ii < 100; ii++)
		    {
			MidData.mTestParameters[ii].aisleTestFinished = false;
			// MidData.passageIsOn[ii] = false;
			// MidData.passageFlags[ii] = 1;
			MidData.mTestParameters[ii].aisleStatus = 0;
			MidData.mTestParameters[ii].aisleTestTime = 0;
			// MidData.passageFlags[ii] = 1;
			MidData.mTestParameters[ii].aislePastTestData = "";
			MidData.mTestParameters[ii].aisleHeght[0] = 0;
			MidData.mTestParameters[ii].aisleNum="";
			MidData.mTestParameters[ii].aisleNumShow="";	
			MidData.mTestParameters[ii].aisleSampleNumTime=0;
			MidData.mTestParameters[ii].aisleDataSaved=false;
		    }
		    
		    MidData.TestFinished = false;
		    MidData.ErrorStrShow = MidData.IsChinese==1?"��������":"Running well";
		    LoadSelfTestStatusThread mLoadSelfTestStatusThread = new LowerMachinePort().new LoadSelfTestStatusThread();
		    mLoadSelfTestStatusThread.start();
		    SendStartOrder mSendStartOrder=new SendStartOrder();
			mSendStartOrder.start();
		}
    	
    }
    class SetButtonOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent set=new Intent();
			set.setClass(MainActivity.this, SetActivity.class);
			startActivity(set);
		}
		
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	boolean exit = false;
	Time t=new Time();
	long timesec=0;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event)
    {
    	System.out.println("-----Key-----"+timesec);
    	// TODO Auto-generated method stub
    	if (MidData.isDebugging==0)
    	{
    		return true;
    	}
    	if (exit == false)
    	{
    	    exit = true;
    	}
    	else
    	{
    	    exit = false;
    	}
    	t.setToNow();
    	if(event.getAction()==KeyEvent.ACTION_DOWN)
    	{    		
    		timesec=t.toMillis(true);
    	}
    	else if(event.getAction()==KeyEvent.ACTION_UP)
    	{
    		if(timesec!=0&&t.toMillis(true)-timesec>3000)
    		{
    			return true;
    		}
    	}
    		
    	if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_HOME)
    		&& exit)
    	{
    	    if (MidData.isDebugging!=0)
    	    {

    		final AlertDialog isExit = new AlertDialog.Builder(this)
    			.create();
    		// �Ի������
    		isExit.setTitle(MidData.IsChinese==1?"ϵͳ��ʾ":"Notice");
    		// �Ի�����Ϣ
    		isExit.setCancelable(false);
    		isExit.setMessage(MidData.IsChinese==1?"��ȷ��Ҫ�˳�ϵͳ��?":"Exit the system?");
    		// ʵ�����Ի����ϵİ�ť����¼�����
    		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    		{
    		    public void onClick(DialogInterface dialog, int which)
    		    {
    			switch (which)
    			{
    			case AlertDialog.BUTTON1:// "ȷ��"��ť�˳�����
    			    // closeall=true;
    			    // MidData.UserName="δ֪";
    			    MidData.m_isInterrupted_LowerMachine = false;
    			    MidData.m_isInterrupted_ICCreditCard = false;
    			    MidData.m_isInterrupted_Print = false;
    			   // MidData.m_isInterruptedForSA = false;
    			    MidData.m_isInterrupted_Scanner = false;
    			    MidData.isDebugging = 0;
    			    finish();
    			    android.os.Process.killProcess(android.os.Process.myPid());
    			    break;
    			case AlertDialog.BUTTON2:// "ȡ��"�ڶ�����ťȡ���Ի���
    			    // isExit.cancel();
    			    break;
    			default:
    			    break;
    			}
    		    }
    		};
    		// ע�����
    		isExit.setButton(MidData.IsChinese==1?"ȷ��":"Comfirm", listener);
    		isExit.setButton2(MidData.IsChinese==1?"ȡ��":"Cancel", listener);
    		// ��ʾ�Ի���
    		isExit.show();
    		return true;
    	    }
    	    else
    	    {
    		return true;
    	    }
    		//return super.dispatchKeyEvent(event);
    	}
    	else
    	{
    	    return super.dispatchKeyEvent(event);
    	}
        
    }

}
