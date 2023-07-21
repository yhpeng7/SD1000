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
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		CreateDB();
		Initial();
		boolean ld=LoadInitial();
		MidData.ErrorStrShow=MidData.IsChinese==1?"仪器正常":"Running well";
		loadXML();		
		if(!ld)
		{
			new AlertDialog.Builder(MainActivity.this).setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			.setMessage(MidData.IsChinese==1?"加载初始化数据失败，请重新启动":"Initialize error, please reboot").setPositiveButton(MidData.IsChinese==1?"确定":"Comfirm", null)
			.setCancelable(false).show();
			StatustextView.setTextColor(Color.RED);
			StatustextView.setText(MidData.IsChinese==1?"加载初始化"+"\n"+"数据失败，"+"\n"+"请重新启动！":"Initialize "+"\n"+"error, please，"+"\n"+"reboot！");
			return;
		}
		//System.out.println(MidData.isSql+"---------sql");
		MidData.isOnMain=true;
		if (MidData.m_InputStream_LowerMachine == null)
		    OpenSerialPort();// 打开串口
		SendStartOrder mSendStartOrder=new SendStartOrder();		
		mSendStartOrder.start();
		if(MidData.mSetParameters.ERSCount<500||MidData.mSetParameters.hematCount<500)
			new AlertDialog.Builder(MainActivity.this).setTitle(MidData.IsChinese==1?"消息提示":"Notice")
		    .setMessage(MidData.IsChinese==1?"试管余量不足，请及时充值！":"Insufficient tubes , please recharge").setPositiveButton(MidData.IsChinese==1?"确定":"Comfirm", null)
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
			    // 获取指定文件对应的输入流
			    FileInputStream fis = new FileInputStream(
				    sdCardDir.getCanonicalPath() + "/Systemconfig");
			    // 将指定输入流包装成BufferedReader
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
			    new AlertDialog.Builder(MainActivity.this).setTitle(MidData.IsChinese==1?"消息提示":"Notice")
				    .setMessage(MidData.IsChinese==1?"关键数据缺失":"Key data missing").setPositiveButton(MidData.IsChinese==1?"确定":"Comfirm", null)
				    .setCancelable(false).show();			    
			}
		    }
		    else
		    {
			/*new AlertDialog.Builder(MainActivity.this).setTitle(MidData.IsChinese?"消息提示":"Notice")
				.setMessage(MidData.IsChinese?"关键文件缺失":"key file missing").setPositiveButton(MidData.IsChinese?"确定":"Comfirm", null)
				.setCancelable(false).show();*/
		    }
		}		
		catch (Exception e)
		{
		    // TODO: handle exception
		}
		textViewDevNum.setText(MidData.IsChinese==1?"仪器编号："+MidData.Product_ID:"SN："+MidData.Product_ID);
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
				str=MidData.IsChinese==1?MidData.StatusStrShow+"\n"+"测试区温度："+fnum1.format(CalculateFormula.AnverageTem(MidData.Temperature))+"℃"+"\n"+MidData.ErrorStrShow:MidData.StatusStrShow+"\n"+"Test area temperature："+fnum1.format(CalculateFormula.AnverageTem(MidData.Temperature))+"℃"+"\n"+MidData.ErrorStrShow;
			else
				str=MidData.StatusStrShow+"\n"+MidData.TemperatureErrorShow+"\n"+MidData.ErrorStrShow;
			if(!MidData.startTemTestFinish)
			{
				MidData.startTemTestFinish=true;
				if(!MidData.startTemTestOK)
				{					
					final boolean tempOK=false;
					new AlertDialog.Builder(MainActivity.this)
				    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
				    .setCancelable(false)
				    .setMessage(MidData.IsChinese==1?"环境温度超出有效的测试范围，可能导致测试结果不准确，是否要继续测试?":"Result maybe inaccurate due to high room temperature,contiunue to test?")
				    .setPositiveButton(MidData.IsChinese==1?"是":"YES",
					    new DialogInterface.OnClickListener()
					    {
				    	
						public void onClick(
							DialogInterface dialoginterface, int i)
						{
							MidData.startTemTestOK=true;
							Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"后续测试结果导致的问题，用户自行负责":"User is responsible for consequences", Toast.LENGTH_LONG).show();
						    
						}
					    }).setNegativeButton(MidData.IsChinese==1?"否":"No", new DialogInterface.OnClickListener()
					    {

						public void onClick(
							DialogInterface dialoginterface, int i)
						{
							Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请将仪器置于10-30℃的环境下，重新启动仪器后使用":"Please keep analyzer at 10-30℃  environment，restart to use", Toast.LENGTH_LONG).show();
							
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
				    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
				    .setCancelable(false)
				    .setMessage(MidData.IsChinese==1?"测试区温度异常，可能导致测试结果不准确，是否要继续测试?":"Temp error with test channel，result  may be affected,continue testing?")
				    .setPositiveButton(MidData.IsChinese==1?"是":"YES",
					    new DialogInterface.OnClickListener()
					    {
				    	
						public void onClick(
							DialogInterface dialoginterface, int i)
						{
							MidData.startTemTestOK1=true;
							Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"后续测试结果导致的问题，用户自行负责":"User is responsible for consequences.", Toast.LENGTH_LONG).show();
						    
						}
					    }).setNegativeButton(MidData.IsChinese==1?"否":"No", new DialogInterface.OnClickListener()
					    {

						public void onClick(
							DialogInterface dialoginterface, int i)
						{
							Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请检查温度传感器，重新启动仪器后使用":"Check temp sensor，restart analyzer.", Toast.LENGTH_LONG).show();
						}
					    }).show();
				}
			
			}
		}
		else 
			str=MidData.IsChinese==1?"\n"+MidData.StatusStrShow+"\n"+"请将测试孔内的所有试管取出":"\n"+MidData.StatusStrShow+"\n"+"Please remove all tubes";
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
    {// 打开串口
		
		MidData.sOpForPrint = new PrintPort();
		MidData.sOpForScanner = new ScanningPort();
		MidData.sOpForSA = new SAPort();
	OpenSerialPortForDri();

	OpenSerialPortForPrint();
	OpenSerialPortForICCreditCard();
	OpenSerialPortForScanner();
	// OpenSerialPortForUpLoadDataToSA();//数据上传端口 需要时再打开

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
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"连接读卡器失败，请检查读卡器连接！":"Reader connection fails, check the connection",
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		
		MidData.m_OutputStream_ICCreditCard = MidData.portForICCreditCard.getOutputStream();
		MidData.m_InputStream_ICCreditCard = MidData.portForICCreditCard.getInputStream();
		
		if (MidData.m_InputStream_ICCreditCard != null)
		{			
			
		    MidData.m_isInterrupted_ICCreditCard = true;
		    MidData.sOpForICCreditCard.ReciveDataFromSerial();// 开启串口接受数据的线程
		    
		}
		else
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"连接读卡器失败，请检查读卡器连接":"Reader connection fails, check the connection",
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
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"COM连接失败，无法与底层硬件设备通信！":"COM connection fails, can not communicate with the underlying hardware",
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		MidData.m_OutputStream_LowerMachine = sPortForCom.getOutputStream();
		MidData.m_InputStream_LowerMachine = sPortForCom.getInputStream();
		if (MidData.m_InputStream_LowerMachine != null)
		{
		    MidData.m_isInterrupted_LowerMachine = true;
		    MidData.sOp.ReciveDataFromSerial();// 开启串口接受数据的线程
		}
		else
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"COM连接失败，无法与底层硬件设备通信":"COM connection fails, can not communicate with the underlying hardware",
			    Toast.LENGTH_SHORT).show();
	    }

	    public void OpenSerialPortForPrint()
	    {
	    SerialPort sPortForPrint = null;
		sPortForPrint = MidData.sOpForPrint.OpenSerialPortForPrint();
		MidData.portForPrint = sPortForPrint;// MidData.sOp.OpenSerialPort();
		if (MidData.portForPrint == null)
		{
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"打印机连接失败，无法进行打印":"Printer connection fails, can not print",
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		MidData.m_OutputStream_Print = sPortForPrint.getOutputStream();
		MidData.m_InputStream_Print = sPortForPrint.getInputStream();
		if (MidData.m_InputStream_Print != null)
		{
		    MidData.m_isInterrupted_Print = true;
		    // MidData.sOp.ReciveDataFromSerial();//开启串口接受数据的线程
		}
		else
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"打印机连接失败，无法进行打印":"Printer connection fails, can not print",
			    Toast.LENGTH_SHORT).show();
	    }

	    public void OpenSerialPortForScanner()
	    {
	    SerialPort sPortForScanner = null;
		sPortForScanner = MidData.sOpForScanner.OpenSerialPortForScanner();
		MidData.portForScanner = sPortForScanner;// MidData.sOp.OpenSerialPort();
		if (MidData.portForScanner == null)
		{
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"扫描枪连接失败，无法进行扫描":"Scanner connection fails, can not scan",
			    Toast.LENGTH_SHORT).show();
		    return;
		}
		MidData.m_OutputStream_Scanner = sPortForScanner.getOutputStream();
		MidData.m_InputStream_Scanner = sPortForScanner.getInputStream();
		if (MidData.m_InputStream_Scanner != null)
		{
		    MidData.m_isInterrupted_Scanner = true;
		    MidData.sOpForScanner.ReciveDataFromSerial();// 开启串口接受数据的线程
		}
		else
		    Toast.makeText(MainActivity.this, MidData.IsChinese==1?"扫描枪连接失败，无法进行扫描":"Scanner connection fails, can not scan",
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
    	textViewDriceLab.setText(MidData.IsChinese==1?"SD-1000动态血沉/压积测试仪":"SD-1000 Automated ESR Analyer");
    	
    	button_Test=(Button)findViewById(R.id.button_Test);
    	button_Test.setOnClickListener(new button_TestClickListener());
    	button_Test.setOnTouchListener(new ButtonTouchListener());
    	button_Test.setText(MidData.IsChinese==1?"测试":"TEST");
    	    	
    	button_Data=(Button)findViewById(R.id.button_Data);
    	button_Data.setOnClickListener(new DataButtonOnClickListener());
    	button_Data.setOnTouchListener(new ButtonTouchListener());
    	button_Data.setText(MidData.IsChinese==1?"数据":"DATA");
    	
    	button_ReTest=(Button)findViewById(R.id.button_ReTest);
    	button_ReTest.setOnClickListener(new ReTestButtonOnClickListener());
    	button_ReTest.setOnTouchListener(new ButtonTouchListener());
    	button_ReTest.setText(MidData.IsChinese==1?"重新\n自检":"Self\nTest");
    	
    	textViewDevNum=(TextView)findViewById(R.id.textViewDevNum);
    	StatustextView=(TextView)findViewById(R.id.StatustextView);
    	
    	button_set=(Button)findViewById(R.id.button_set);
    	button_set.setOnClickListener(new SetButtonOnClick());
    	button_set.setOnTouchListener(new ButtonTouchListener());
    	button_set.setText(MidData.IsChinese==1?"设置":"SETUP");
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
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在调整运行周期，请稍后设置":"Adjusting running cycle, please wait", Toast.LENGTH_LONG).show();
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
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在测试,请在检测完后设置！":"Being tested, please set up after test",
				Toast.LENGTH_LONG).show();
			return;
		    }
		    else if (run == 2)
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"自检未完成，请在自检完成后设置！":"Self-test is not completed, set after the completion of the self-test",
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
		    MidData.ErrorStrShow = MidData.IsChinese==1?"仪器正常":"Running well";
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
    		// 对话框标题
    		isExit.setTitle(MidData.IsChinese==1?"系统提示":"Notice");
    		// 对话框消息
    		isExit.setCancelable(false);
    		isExit.setMessage(MidData.IsChinese==1?"您确定要退出系统吗?":"Exit the system?");
    		// 实例化对话框上的按钮点击事件监听
    		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    		{
    		    public void onClick(DialogInterface dialog, int which)
    		    {
    			switch (which)
    			{
    			case AlertDialog.BUTTON1:// "确认"按钮退出程序
    			    // closeall=true;
    			    // MidData.UserName="未知";
    			    MidData.m_isInterrupted_LowerMachine = false;
    			    MidData.m_isInterrupted_ICCreditCard = false;
    			    MidData.m_isInterrupted_Print = false;
    			   // MidData.m_isInterruptedForSA = false;
    			    MidData.m_isInterrupted_Scanner = false;
    			    MidData.isDebugging = 0;
    			    finish();
    			    android.os.Process.killProcess(android.os.Process.myPid());
    			    break;
    			case AlertDialog.BUTTON2:// "取消"第二个按钮取消对话框
    			    // isExit.cancel();
    			    break;
    			default:
    			    break;
    			}
    		    }
    		};
    		// 注册监听
    		isExit.setButton(MidData.IsChinese==1?"确定":"Comfirm", listener);
    		isExit.setButton2(MidData.IsChinese==1?"取消":"Cancel", listener);
    		// 显示对话框
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
