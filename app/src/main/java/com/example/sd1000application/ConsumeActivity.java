package com.example.sd1000application;


import java.io.IOException;

//import com.example.sd1000application.ConsumeActivity.ReadSDCardTestThread;
import com.realect.sd1000.devicePortOperation.ICCreditCardPort;
import com.realect.sd1000.devicePortOperation.ICCreditCardPort.ICCardReadThread;
import com.realect.sd1000.parameterStatus.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.realect.sd.other.*;
public class ConsumeActivity extends Activity{
	private static boolean isReadSdCardOK = false;
	private static boolean isReadingSDCard=false;
	private static boolean isReadSDCardFinish=false;
	private static boolean isCreditSdCardOK = false;
	Time t = new Time();
	private ICCreditCardPort mSerialPortForICCreditCard=new ICCreditCardPort();
	private Button button_ReadICCard;
	private Button button_Import;
	private TextView textView_HematNum;
	private TextView textView_ERSNum;
	private TextView textView_CardNum;
	private TextView textView_Count;
	private TextView textView_EffTime;
	private TextView textView_BatchNum;
	private TextView StatustextView;
	private TextView textView1;
	private TextView textViewLab;
	private Button button_Back;
	private ProgressBar CreditCardProgressBar;
	private static int dwSn;// 卡的序列号
	private int[] SDNum = new int[4];
	private static String GenerateKey;// 密钥
	private static String CardNum;
	private static String BatchNumber;
	private static String MarginOfCard;
	private TextView textView_LastImportTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_consume);
		//getWindow().setTitle(MidData.IsChinese?"耗材":"consumable");
		LoadXML();
		LoadLastSet();
	}
	private void LoadLastSet(){
		textViewLab.setText(MidData.IsChinese==1?"名称            数量":"Name          Qty");
		textView_ERSNum.setText(MidData.IsChinese==1?"血沉试管        "+MidData.mSetParameters.ERSCount:"ESR tubes :"+MidData.mSetParameters.ERSCount);
		textView_HematNum.setText(MidData.IsChinese==1?"压积试管        "+MidData.mSetParameters.hematCount:"HCT tubes :"+MidData.mSetParameters.hematCount);
		textView_LastImportTime.setText(MidData.IsChinese==1?"上次充值时间 \n"+MidData.mSetParameters.LastCreditCard_Time:"Recharge time\n"+MidData.mSetParameters.LastCreditCard_Time);
		button_ReadICCard.setText(MidData.IsChinese==1?"读卡":"Read");
		button_Import.setText(MidData.IsChinese==1?"充值":"Recharge");
		button_Back.setText(MidData.IsChinese==1?"返回":"Return");
		StatustextView.setText(MidData.IsChinese==1?"IC卡信息":"IC card infor");
		textView1.setText(MidData.IsChinese==1?"剩余耗材":"Remain consum");
		textView_BatchNum.setText(MidData.IsChinese==1?"批号:":"Lot:");
		textView_EffTime.setText(MidData.IsChinese==1?"有效期:":"Expiry date:");
		textView_CardNum.setText(MidData.IsChinese==1?"卡号:":"Card NO:");
		textView_Count.setText(MidData.IsChinese==1?"数量:":"Qty:");
	}
	class button_BackListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}		
	}
	private void LoadXML(){
		textViewLab=(TextView)findViewById(R.id.textViewLab);
		textView1=(TextView)findViewById(R.id.textViewsoftwarelab);
		StatustextView=(TextView)findViewById(R.id.StatustextView);
		textView_LastImportTime=(TextView)findViewById(R.id.textView_LastImportTime);
		
		textView_BatchNum=(TextView)findViewById(R.id.textView_BatchNum);
		CreditCardProgressBar=(ProgressBar)findViewById(R.id.CreditCardProgressBar);
		button_Back=(Button)findViewById(R.id.button_Back);
		button_Back.setOnClickListener(new button_BackListener());
		button_Back.setOnTouchListener(new ButtonTouchListener());
		
		textView_EffTime=(TextView)findViewById(R.id.textView_EffTime);
		
		textView_Count=(TextView)findViewById(R.id.textView_Count);
		
		textView_CardNum=(TextView)findViewById(R.id.textView_CardNum);
		
		textView_ERSNum=(TextView)findViewById(R.id.textView_ERSNum);
		
		textView_HematNum=(TextView)findViewById(R.id.textView_HematNum);
		
		button_Import=(Button)findViewById(R.id.button_Import);
		button_Import.setOnClickListener(new button_ImportListener());
		button_Import.setOnTouchListener(new ButtonTouchListener());
		
		button_ReadICCard=(Button)findViewById(R.id.button_ReadICCard);
		button_ReadICCard.setOnClickListener(new button_ReadICCardListener());
		button_ReadICCard.setOnTouchListener(new ButtonTouchListener());
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
	class button_ImportListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			new AlertDialog.Builder(arg0.getContext())
		    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
		    .setMessage(MidData.IsChinese==1?"您确定要将本IC卡的余额充入设备？":"Recharge the IC card into the machine?")
		    .setPositiveButton(MidData.IsChinese==1?"确定":"Comfirm",
			    new DialogInterface.OnClickListener()
			    {

				public void onClick(
					DialogInterface dialoginterface, int i)
				{
				    Toast.makeText(getApplicationContext(),
				    		MidData.IsChinese==1?"正在充值，IC卡不要离开感应区,否则无法完成充值！":"Recharging, please leave IC card in the sensor area",
					    Toast.LENGTH_LONG).show();
				    ToCreditCardThread mCreditCardThread = new ToCreditCardThread();
				    mCreditCardThread.start();

				}
			    }).setNegativeButton(MidData.IsChinese==1?"取消":"Cancel", null).show();
		}
		
	}
	public class CreditCardThread extends Thread
    {

	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    while (!isCreditSdCardOK)
		{
		    mSerialPortForICCreditCard.SetReciveStatus(false);
			SendForGetSN();
			try
			{
			    sleep(100);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			//ReadThread mReadThread = new ICCreditCardPort().new ReadThread();
			//mReadThread.start();
			/*try
			{
			    sleep(50);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}*/
			if(!mSerialPortForICCreditCard.GetReciveStatus())
			{
				continue;				
			}
			dwSn = GetSN();
			if (dwSn == 0)
			{
			    //mReadThread.interrupt();
			    continue;
			}
			mSerialPortForICCreditCard.SetReciveStatus(false);
			SendForGetSerialInfo(1, 0);
			try
			{
			    sleep(500);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			//ReadThread mReadThread1 = new ICCreditCardPort().new ReadThread();
			//mReadThread1.start();
			try
			{
			    sleep(50);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			if(!mSerialPortForICCreditCard.GetReciveStatus())
			{
				continue;				
			}
			final String newCard = ReadBlock();
	
			if (!newCard.equals(CardNum) || newCard.equals(""))
			{
			    //mReadThread1.interrupt();
			    continue;
			}
			mSerialPortForICCreditCard.SetReciveStatus(false);
			SendForWriteCard(3, 1);
			try
			{
			    sleep(50);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			//ReadThread mReadThread2 = new ICCreditCardPort().new ReadThread();
			//mReadThread2.start();
			try
			{
			    sleep(50);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			if(!mSerialPortForICCreditCard.GetReciveStatus())
			{
				continue;				
			}
			if (ReadWriteResult())
			{
			    MidData.mSetParameters.ERSCount += Integer
				    .parseInt(MarginOfCard);
			    MidData.mSetParameters.hematCount += Integer
				    .parseInt(MarginOfCard);
			    int[] va={MidData.mSetParameters.ERSCount,MidData.mSetParameters.hematCount};
			    if (MidData.sqlOp.UpdateConsumInfo(va))
			    {
				runOnUiThread(new Runnable()
				{
				    public void run()
				    {
				    	textView_ERSNum.setText(MidData.IsChinese==1?"血沉试管    "+MidData.mSetParameters.ERSCount:"ESR tubes:"+MidData.mSetParameters.ERSCount);
				    	textView_HematNum.setText(MidData.IsChinese==1?"压积试管    "+MidData.mSetParameters.hematCount:"HCT tubes:"+MidData.mSetParameters.hematCount);
				    	textView_Count.setText(MidData.IsChinese==1?"数量：    0":"Quantity:0");
				    	MarginOfCard = "0";
					isCreditSdCardOK = true;
					// Toast.makeText(getApplicationContext(),
					// "充值完成",Toast.LENGTH_LONG).show();
				    }
				});
			    }
			    else
			    {
				//System.out.println("111111111111111111111111111111111");
				// Toast.makeText(getApplicationContext(),
				// "充值失败",Toast.LENGTH_LONG).show();
			    }
			}
			else
			{
			   // System.out.println("2222222222222222222222222");
			    // Toast.makeText(getApplicationContext(),
			    // "充值失败",Toast.LENGTH_LONG).show();
			    //mReadThread2.interrupt();
			    continue;
			}
		}

	}

    }
	 public boolean ReadWriteResult()
	    {
		/*
		 * byte[] Ptr=new byte[32]; try {
		 * MidData.Instance().m_InputStreamForICCreditCard.read(Ptr); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * System.out.println
		 * ("````````````ReciveError______ReadWriteResult```````````````");
		 * e.printStackTrace(); } System.out.println(new
		 * String(Ptr)+"________ReadWriteResult");
		 */
		if (MidData.ICCardSerialPortData[MidData.ICCardSerialPortData[0]] == mSerialPortForICCreditCard
			.GetCheckValue(MidData.ICCardSerialPortData,
				MidData.ICCardSerialPortData[0]))
		{
		    if (MidData.ICCardSerialPortData[1] == 0x22)
		    {
			return true;
		    }
		    else
		    {
			return false;
		    }
		}
		else
		    return false;
	    }
	public void SendForWriteCard(int iSec, int iBlock)
    {
	String nGKey = GetGenerateKey(iSec, dwSn);
	byte[] cmd = new byte[64];
	// 0a 21 01 [块号1B] [密钥6B] chk
	cmd[0] = 0x1a;
	cmd[1] = 0x22;
	cmd[2] = 0x00;
	cmd[3] = (byte) (iSec * 4 + iBlock);

	// GenerateKey(((CComboBox*)(GetDlgItem(IDC_COMBO2)))->GetCurSel(),m_dwSn,(char*)Key);
	// memcpy(cmd+4,Key,6);
	for (int i = 0; i < 6; i++)
	{
	    String s = nGKey.substring(i, i + 1);
	    cmd[4 + i] = (byte) (s.getBytes()[0]);
	}
	for (int i = 0; i < 16; i++)
	{
	    cmd[10 + i] = 0x30;
	}
	// cmd[25]=0x39;
	cmd[26] = mSerialPortForICCreditCard.GetCheckValue(cmd, 26);
	mSerialPortForICCreditCard.SendToPort(cmd, 27);
    }

	 class ToCreditCardThread extends Thread
	    {

		@Override
		public void run()
		{
		    // TODO Auto-generated method stub
		    super.run();
		    isCreditSdCardOK = false;
		    CreditCardThread mCreditCardThread = new CreditCardThread();
		    mCreditCardThread.start();
		    int time = 0;
		    while (time <= 20)
		    {
			try
			{
			    sleep(500);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			if (!isCreditSdCardOK)
			{
			    if (time < 20)
			    {
				CreditCardProgressBar.setProgress(5 * time);
				CreditCardProgressBar
					.setSecondaryProgress(5 * time + 5);
			    }
			    else
			    {
				mCreditCardThread.interrupt();
				isCreditSdCardOK = true;
				runOnUiThread(new Runnable()
				{
				    public void run()
				    {
					Toast.makeText(getBaseContext(), MidData.IsChinese==1?"充值失败":"Failure recharge",
						Toast.LENGTH_LONG).show();
				    }
				});
				CreditCardProgressBar.setProgress(100);
				CreditCardProgressBar.setSecondaryProgress(100);
			    }
			}
			else
			{
			    Time t = new Time();
			    t.setToNow();
			    String year = Integer.toString(t.year);
			    String month = (t.month + 1) < 10 ? "0"
				    + Integer.toString(t.month + 1) : Integer
				    .toString(t.month + 1);
			    String day = (t.monthDay + 1) < 10 ? "0"
				    + Integer.toString(t.monthDay + 1) : Integer
				    .toString(t.monthDay + 1);
			    String hour = (t.hour) < 10 ? "0"
				    + Integer.toString(t.hour) : Integer
				    .toString(t.hour);
			    String min = (t.minute) < 10 ? "0"
				    + Integer.toString(t.minute) : Integer
				    .toString(t.minute);
			    MidData.LastCreditCard_Time = year + "-" + month
				    + "-" + day + "  " + hour + ":" + min;
			    MidData.sqlOp.update(1, "ConsumableInforTable",
				    "lastcreditcardtime",
				    MidData.LastCreditCard_Time);
			    CreditCardProgressBar.setProgress(100);
			    CreditCardProgressBar.setSecondaryProgress(100);
			    PWM mPWMThread = new PWM();
			    mPWMThread.start();
			    runOnUiThread(new Runnable()
			    {
				public void run()
				{
					textView_LastImportTime.setText(MidData.IsChinese==1?"上次充值时间\n"+MidData.LastCreditCard_Time:"Last recharge\n"+MidData.LastCreditCard_Time);
				    
				    Toast.makeText(getBaseContext(), MidData.IsChinese==1?"充值成功":"Successful recharge",
					    Toast.LENGTH_LONG).show();
				}
			    });
			    break;
			}

			time++;
		    }
		    /*
		     * final String str=strs; runOnUiThread(new Runnable() { public void
		     * run() { Toast.makeText(getBaseContext(),str,
		     * Toast.LENGTH_LONG).show(); } });
		     */

		}

	    }

	class button_ReadICCardListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			runOnUiThread(new Runnable()
		    {
			public void run()
			{
				textView_CardNum.setText(MidData.IsChinese==1?"卡号：    ":"Card NO:");
				textView_BatchNum.setText(MidData.IsChinese==1?"批号：    ":"Lot:  ");
			    
				textView_Count.setText(MidData.IsChinese==1?"数量：    ":"Qty:");
			    //isReadSdCardOK = true;
			}
		    });
			if(isReadingSDCard)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在读卡，请稍后！":"IC card sence is reading card!", Toast.LENGTH_LONG).show();
				return;
			}
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请将IC卡放置在感应区，否则将无法获得卡片信息！":"Please leave IC card in the sensing area",
				    Toast.LENGTH_LONG).show();
		    if (!MidData.m_isInterrupted_ICCreditCard)
		    	OpenSerialPortForICCreditCard();
		    isReadSdCardOK = false;
		    isReadingSDCard=true;
		    //isReadSDCardFinish=false;
		    //button_ReadICCard.setClickable(false);
		    WaittingReadCardThread mWaittingReadCardThread = new WaittingReadCardThread();
		    mWaittingReadCardThread.start();
		}
		
	}
	public String SendForSet()
    {// 发送获取序列号的请求
	byte[] buf = new byte[32];
	buf[0] = 0x03;
	buf[1] = 0x11;
	buf[2] = 0x01;
	buf[3] = mSerialPortForICCreditCard.GetCheckValue(buf, 3);
	mSerialPortForICCreditCard.SendToPort(buf, 4);
	return null;
    }
	public String ReadBlock()
    {
	if (MidData.ICCardSerialPortData[0] != 0&&MidData.ICCardSerialPortData[0]<32
		&& MidData.ICCardSerialPortData[MidData.ICCardSerialPortData[0]] == mSerialPortForICCreditCard
			.GetCheckValue(MidData.ICCardSerialPortData,
				MidData.ICCardSerialPortData[0]))
	{
	    byte[] buff = new byte[MidData.ICCardSerialPortData[0] - 2];
	    for (int i = 2; i < MidData.ICCardSerialPortData[0]; i++)
	    {
		/*
		 * if(Ptr[i]<0) { buff[i-2]=256+Ptr[i]; }
		 */
		// else
		// {
		buff[i - 2] = MidData.ICCardSerialPortData[i];
		// }

	    }
	    for (int i = 0; i < 32; i++)
	    {
		MidData.ICCardSerialPortData[i] = 0;
	    }
	    /*
	     * int mask=0xff; int temp=0; int n=0; for(int i=Ptr[0]-3;i>=0;i--)
	     * { n<<=8; temp=buff[i]&mask; n|=temp; }
	     */
	    return new String(buff).trim();
	}
	else
	{
	    return "";
	}
    }
	public String SendForGetSN()
    {// 发送获取序列号的请求
	byte[] buf = new byte[32];
	buf[0] = 0x03;
	buf[1] = 0x20;
	buf[2] = 0x00;
	buf[3] = mSerialPortForICCreditCard.GetCheckValue(buf, 3);
	mSerialPortForICCreditCard.SendToPort(buf, 4);
	return null;
    }
	public int GetSN()
    {
	/*
	 * byte[] Ptr=new byte[32]; try {
	 * MidData.Instance().m_InputStreamForICCreditCard.read(Ptr); } catch
	 * (IOException e) { // TODO Auto-generated catch block
	 * System.out.println
	 * ("````````````ReciveError______SerialIC```````````````");
	 * e.printStackTrace(); } System.out.println(new
	 * String(Ptr)+"________GetSN");
	 */
	if (MidData.ICCardSerialPortData[0] != 0
		&& MidData.ICCardSerialPortData[MidData.ICCardSerialPortData[0]] == mSerialPortForICCreditCard
			.GetCheckValue(MidData.ICCardSerialPortData,
				MidData.ICCardSerialPortData[0]))
	{
	    for (int i = 0; i < 4; i++)
	    {
		if (MidData.ICCardSerialPortData[i + 2] < 0)
		{
		    SDNum[i] = 256 + MidData.ICCardSerialPortData[i + 2];
		}
		else
		{
		    SDNum[i] = MidData.ICCardSerialPortData[i + 2];
		}
	    }
	    int mask = 0xff;
	    int temp = 0;
	    int n = 0;
	    for (int i = 3; i >= 0; i--)
	    {
		n <<= 8;
		temp = SDNum[i] & mask;
		n |= temp;
	    }
	    // dwSn=n;
	    for (int i = 0; i < 32; i++)
	    {
		MidData.ICCardSerialPortData[i] = 0;
	    }
	    return n;
	}
	else
	    return 0;

    }
	 public String GetGenerateKey(int iSec, int dwSn)
	    {
		int[] nNum =
		{ 0, 0, 0, 0 };
		// nNum[3] = dwSn/(unsigned long)pow(10,8)%10;//7 0
		// nNum[2] = dwSn/(unsigned long)pow(10,5)%10;//0 0
		// nNum[1] = dwSn/(unsigned long)pow(10,3)%10;//0 9
		// nNum[0] = dwSn/(unsigned long)pow(10,2)%10;//6 8
		nNum[3] = (dwSn / 100000000) % 10;
		nNum[2] = (dwSn / 100000) % 10;
		nNum[1] = (dwSn / 1000) % 10;
		nNum[0] = (dwSn / 100) % 10;

		int nPart1 = iSec * 1000 + iSec + nNum[0] * 100000 + nNum[2] * 10000
			+ nNum[3] * 100 + nNum[1] * 10;// 600700
		int nKey = nPart1 | dwSn;

		String nKeyStr = Integer.toString(nKey);
		int nLen = nKeyStr.length();
		String nGenerateKey;
		if (nLen >= 6)
		{
		    nGenerateKey = nKeyStr.substring(nLen - 6, nLen);
		}
		else
		{
		    String lastK = "";
		    for (int i = 0; i < 6 - nLen; i++)
			lastK += "a";
		    nGenerateKey = lastK + nKeyStr;
		}
		GenerateKey = nGenerateKey;
		return GenerateKey;
	    }
	public void SendForGetSerialInfo(int iSec, int iBlock)
    {
	String nGKey = GetGenerateKey(iSec, dwSn);
	byte[] cmd = new byte[64];
	// 0a 21 01 [块号1B] [密钥6B] chk
	cmd[0] = 0x0a;
	cmd[1] = 0x21;
	cmd[2] = 0x00;
	cmd[3] = (byte) (iSec * 4 + iBlock);

	// GenerateKey(((CComboBox*)(GetDlgItem(IDC_COMBO2)))->GetCurSel(),m_dwSn,(char*)Key);
	// memcpy(cmd+4,Key,6);
	for (int i = 0; i < 6; i++)
	{
	    String s = nGKey.substring(i, i + 1);
	    cmd[4 + i] = (byte) (s.getBytes()[0]);
	}
	cmd[10] = mSerialPortForICCreditCard.GetCheckValue(cmd, 10);
	mSerialPortForICCreditCard.SendToPort(cmd, 11);
    }
	
	class ReadSdCardThread extends Thread
	    {

		@SuppressWarnings("deprecation")
		@Override
		public void run()
		{
		    // TODO Auto-generated method stub
		    super.run();

		    t.setToNow();
		    String ts = Integer.toString(t.hour) + "_"
			    + Integer.toString(t.minute) + "_"
			    + Integer.toString(t.minute) + "_"
			    + Integer.toString(t.second);
		    //System.out.println(ts + "__________ts");
		    while (!isReadSdCardOK)
		    {
		    	if(!isReadingSDCard)
		    		break;
		    	mSerialPortForICCreditCard.SetReciveStatus(false);
			SendForSet();
			
			try
			{
			    sleep(1000);
			}
			catch (InterruptedException e1)
			{
			    // TODO Auto-generated catch block
			    e1.printStackTrace();
			}
			
			if(!mSerialPortForICCreditCard.GetReciveStatus())
			{
				continue;
			}
			ReadBlock();
			if(!isReadingSDCard)
	    		break;
			mSerialPortForICCreditCard.SetReciveStatus(false);
			SendForGetSN();
			try
			{
			    sleep(1000);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			if(!mSerialPortForICCreditCard.GetReciveStatus())
			{
				continue;
			}			
			dwSn = GetSN();
			
			if (dwSn == 0)
			{
			    //mReadThread.interrupt();
			    continue;
			}

			/*
			 * if(str.equals("")) { mReadThread1.interrupt();
			 * 
			 * continue; }
			 */
			if(!isReadingSDCard)
	    		break;
			mSerialPortForICCreditCard.SetReciveStatus(false);
			SendForGetSN();
			try
			{
			    sleep(1000);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			
			if(!mSerialPortForICCreditCard.GetReciveStatus())
			{
				continue;
			}
			dwSn = GetSN();
			//System.out.println(Integer.toString(dwSn)
			//	+ "_____________________dwSn2");

			if (dwSn == 0)
			{
			    //mReadThread2.interrupt();
			    continue;
			}
			if(!isReadingSDCard)
	    		break;
			mSerialPortForICCreditCard.SetReciveStatus(false);
			SendForGetSerialInfo(1, 0);

			try
			{
			    sleep(1000);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			//ReadThread mReadThread3 = new ICCreditCardPort().new ReadThread();
			//mReadThread3.start();
			
			if(!mSerialPortForICCreditCard.GetReciveStatus())
			{
				continue;
			}
			CardNum = ReadBlock();
			//System.out.println(CardNum + "_____________________CardNum");

			if (CardNum.equals(""))
			{
			    //mReadThread3.interrupt();

			    continue;
			}
			if(!isReadingSDCard)break;
	    		
			mSerialPortForICCreditCard.SetReciveStatus(false);
			SendForGetSN();
			// CreditCardProgressBar.setProgress(35);
			// CreditCardProgressBar.setSecondaryProgress(40);
			try
			{
			    sleep(1000);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			
			// CreditCardProgressBar.setProgress(40);
			// CreditCardProgressBar.setSecondaryProgress(45);
			if(!mSerialPortForICCreditCard.GetReciveStatus())
			{
				continue;
			}
			dwSn = GetSN();
			//System.out.println(Integer.toString(dwSn)
			//	+ "_____________________dwSn3");

			if (dwSn == 0)
			{
			    //mReadThread4.interrupt();
			    /*
			     * runOnUiThread(new Runnable() { public void run() {
			     * Toast.makeText(getApplicationContext(),
			     * "读取卡片序列号失败4，在读卡过程中请将卡片置于感应区，请重新读卡！",
			     * Toast.LENGTH_LONG).show(); } });
			     */
			    continue;
			}
			if(!isReadingSDCard)break;
			mSerialPortForICCreditCard.SetReciveStatus(false);
			SendForGetSerialInfo(3, 2);// 发送获取批号的请求
			try
			{
			    sleep(1000);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			
			if(!mSerialPortForICCreditCard.GetReciveStatus())
			{
				continue;
			}
			BatchNumber = ReadBlock();// 获取批号

//			System.out.println(BatchNumber
//				+ "_____________________BatchNumber");
			if (BatchNumber.equals(""))
			{
			    //mReadThread5.interrupt();

			    continue;
			}
			if(!isReadingSDCard)break;
			mSerialPortForICCreditCard.SetReciveStatus(false);
			SendForGetSN();
			try
			{
			    sleep(1000);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			if(!isReadingSDCard)break;
			if(!mSerialPortForICCreditCard.GetReciveStatus())
			{
				continue;
			}
			dwSn = GetSN();

			if (dwSn == 0)
			{
			    //mReadThread6.interrupt();
			    runOnUiThread(new Runnable()
			    {
				public void run()
				{
				    Toast.makeText(getApplicationContext(),
					    MidData.IsChinese==1?"读取卡片序列号失败5，在读卡过程中请将卡片置于感应区，请重新读卡！":"Failed to read the card ID, place IC card in the sensing area, please rereader",
					    Toast.LENGTH_LONG).show();
				}
			    });
			    continue;
			}
			if(!isReadingSDCard)break;
			mSerialPortForICCreditCard.SetReciveStatus(false);
			SendForGetSerialInfo(3, 1);// 发送获取卡余量的请求
			try
			{
			    sleep(1000);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
			
			if(!mSerialPortForICCreditCard.GetReciveStatus())
			{
				continue;
			}
			final String numt = ReadBlock();// 获取卡余量
//			System.out.println(numt + "_____________________numt");

			t.setToNow();
			String te = Integer.toString(t.hour) + "_"
				+ Integer.toString(t.minute) + "_"
				+ Integer.toString(t.minute) + "_"
				+ Integer.toString(t.second);
//			System.out.println(te + "__________te");
			if (numt.equals(""))
			{
			    //mReadThread7.interrupt();

			    continue;
			}
			else
			{
			    MarginOfCard = Integer.toString(Integer.parseInt(numt));
			    runOnUiThread(new Runnable()
			    {
				public void run()
				{
					textView_CardNum.setText(MidData.IsChinese==1?"卡号：    "+CardNum:"Card NO:"+CardNum);
					textView_BatchNum.setText(MidData.IsChinese==1?"批号：    "+BatchNumber:"Lot:  "+BatchNumber);
				    
					textView_Count.setText(MidData.IsChinese==1?"数量：    "+MarginOfCard:"Qty:"+MarginOfCard);
				    isReadSdCardOK = true;
				}
			    });
			}
		    }

		    CreditCardProgressBar.setProgress(100);
		}

	    }
	class WaittingReadCardThread extends Thread
    {

	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    int times = 0;
	    /*runOnUiThread(new Runnable()
		{
		    public void run()
		    {
		    	button_ReadICCard.setClickable(false);
		    }
		});*/
	    
	    ReadSdCardThread mReadSdCardThread = new ReadSdCardThread();
	    mReadSdCardThread.start();
	    while (times < 11)
	    {
		try
		{
		    sleep(2000);
		}
		catch (InterruptedException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

		if (!isReadSdCardOK)
		{
		    if (times < 10)
		    {
			CreditCardProgressBar.setProgress(10 * times);
			CreditCardProgressBar
				.setSecondaryProgress(10 * times + 5);
		    }
		    else
		    {
			mReadSdCardThread.interrupt();
			isReadSdCardOK = true;
			runOnUiThread(new Runnable()
			{
			    public void run()
			    {
				Toast.makeText(getBaseContext(),
					MidData.IsChinese==1?"读卡失败，请将卡片置于感应区域，重新开始读卡":"Fails read IC card , place IC card in the sensing area, restart read",
					Toast.LENGTH_LONG).show();
			    }
			});
			CreditCardProgressBar.setProgress(100);
			CreditCardProgressBar.setSecondaryProgress(100);
		    }
		}
		else
		{
		    CreditCardProgressBar.setProgress(100);
		    CreditCardProgressBar.setSecondaryProgress(100);
		    PWM mPWMThread = new PWM();
		    mPWMThread.start();
		    runOnUiThread(new Runnable()
		    {
			public void run()
			{
			    Toast.makeText(getBaseContext(), MidData.IsChinese==1?"读卡完成":"Completed read",
				    Toast.LENGTH_LONG).show();
			}
		    });
		    break;
		}
		times++;
	    }
	    isReadingSDCard=false;
	    /*runOnUiThread(new Runnable()
		{
		    public void run()
		    {
		    	button_ReadICCard.setClickable(true);
		    }
		});*/
	}

    }

	public void OpenSerialPortForICCreditCard()
    {
	MidData.sOpForICCreditCard = new ICCreditCardPort();
	
	MidData.portForICCreditCard = MidData.sOpForICCreditCard
			.OpenSerialPortForICCreditCard();// MidData.Instance().sOp.OpenSerialPort();
	if (MidData.portForICCreditCard == null)
	{
	    Toast.makeText(ConsumeActivity.this, MidData.IsChinese==1?"连接读卡器失败，请检查读卡器连接！":"Failed to connect reader，please check the connection",
		    Toast.LENGTH_SHORT).show();
	    return;
	}
	MidData.m_OutputStream_ICCreditCard = MidData.portForICCreditCard
		.getOutputStream();
	MidData.m_InputStream_ICCreditCard = MidData.portForICCreditCard
		.getInputStream();
	if (MidData.m_InputStream_ICCreditCard != null)
	{
	    MidData.m_isInterrupted_ICCreditCard = true;
	    // MidData.Instance().sOpForICCreditCard.ReciveDataFromSerial();//开启串口接受数据的线程
	}
	else
	    Toast.makeText(ConsumeActivity.this,
	    		MidData.IsChinese==1?"连接读卡器失败，请检查读卡器连接！":"Failed to connect reader，please check the connection", Toast.LENGTH_SHORT).show();
    }
	
}
