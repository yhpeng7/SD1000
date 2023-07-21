package com.realect.sd1000.devicePortOperation;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jxl.biff.formula.FunctionNames;

import KyleSocket_api.KyleSocketClass;
import android.database.Cursor;
import android.os.Looper;
import android.text.format.Time;
import android.widget.Toast;

import com.example.sd1000application.DataActivity;
import com.realect.sd1000.Calculate.CalculateFormula;
import com.realect.sd1000.parameterStatus.MidData;

public class AutoUpLoadToLIS extends Thread{
	private String lock;
    private String Num;
    private String XC;
    private String YJ;
    private String Temp;
    DecimalFormat fnum = new DecimalFormat("##0");
	DecimalFormat fnum1 = new DecimalFormat("##0.0");
    public AutoUpLoadToLIS(String lock, String mNum,String mXC,String mYJ,String mTemp)
        {
    	this.lock = lock;
    	Num = mNum;    	
    	XC=mXC;
    	YJ=mYJ;
    	Temp=mTemp;
    	//System.out.println(Num+"---"+XC+"---"+YJ+"---"+Temp);
        }
    private void UploadLine2PC()
    {
    	int testID=Integer.parseInt(Num);
    	MidData.testDataConvert=new TestDataConvert();
    	String str=MidData.testDataConvert.ConvertLineForLisByNum(testID);
    	if(str.equals(""))return;
    	str=">QX,"+Num+","+str;
    	byte[] Data=new byte[str.length()];
    	byte[] Buff=new byte[str.length()+6];
    	Data=str.getBytes();
    	int Length=0;
    	for(int i=0;i<Data.length;i++)
    	{
    		Buff[Length++]=Data[i];
    	}
    	
    	int Crc = MidData.sOp.GetCrc(Data, Length);    	
    	Buff[Length++] = (byte) MidData.sOp.Hex2Ascii((Crc & 0xF000) >> 12);
    	Buff[Length++] = (byte) MidData.sOp.Hex2Ascii((Crc & 0x0F00) >> 8);
    	Buff[Length++] = (byte) MidData.sOp.Hex2Ascii((Crc & 0x00F0) >> 4);
    	Buff[Length++] = (byte) MidData.sOp.Hex2Ascii(Crc & 0x000F);
    	Buff[Length++] = 0x0D;
    	Buff[Length++] = 0x0A;
    	int times=0;
	    int tp=-1;
    	if(MidData.m_UpToLIS_net)
	    {
		    while(times<3)
		    {
		    	if(tp<0)
		    		tp= KyleSocketClass.SendData(Buff, Length, MidData.fd);
		    	else
		    		break;
		    	times++;
		    }
	    }
	    else if(MidData.m_UpToLIS_ser)
	    {
	    	
	    	try {
				MidData.m_OutputStream_SA.write(Buff, 0, Length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
    }
    private void UpLaodData2PC()
    {

	    byte[] Data2 = null;
	    byte[] Data3 = null;
	    if (XC.equals("未检测")||XC.equals("Untest"))
	    {
		XC = "NULL";
	    }
		else
		{
		}
		Data2 = XC.getBytes();
	   
	    //YJ=CalculateFormula.YaJiToShow(YJ);
	    if (YJ.equals("未检测")||YJ.equals("Untest"))
	    {
		YJ = "NULL";
	    }
	    Data3 = YJ.getBytes();    		
	    
	    byte[] Data1 = Num.getBytes();//cursor.getString(cursor.getColumnIndex("xuhao")).getBytes();
	    byte[] data5 = new byte[200];

	    int Length = 0;
	    data5[Length++] = '>';
	    for (int i = 0; i < Data1.length; i++)
		data5[Length++] = Data1[i];
	    data5[Length++] = ',';
	    for (int i = 0; i < Data2.length; i++)
		data5[Length++] = Data2[i];
	    data5[Length++] = ',';
	    for (int i = 0; i < Data3.length; i++)
		data5[Length++] = Data3[i];
	    data5[Length++] = ',';
	    int Crc = MidData.sOp.GetCrc(data5, Length);
	    data5[Length++] = (byte) MidData.sOp
		    .Hex2Ascii((Crc & 0xF000) >> 12);
	    data5[Length++] = (byte) MidData.sOp.Hex2Ascii((Crc & 0x0F00) >> 8);
	    data5[Length++] = (byte) MidData.sOp.Hex2Ascii((Crc & 0x00F0) >> 4);
	    data5[Length++] = (byte) MidData.sOp.Hex2Ascii(Crc & 0x000F);
	    data5[Length++] = 0x0D;
	    data5[Length++] = 0x0A;
	    int times=0;
	    int tp=-1;
	    //System.out.println(new String(data5)+"----D5---");
	    if(MidData.m_UpToLIS_net)
	    {
		    while(times<3)
		    {
		    	if(tp<0)
		    		tp= KyleSocketClass.SendData(data5, Length, MidData.fd);
		    	else
		    		break;
		    	times++;
		    }
	    }
	    else if(MidData.m_UpToLIS_ser)
	    {
	    	
	    	try {
				MidData.m_OutputStream_SA.write(data5, 0, Length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
    }
    public long GetSecond(Time val)
    {
    	DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Time nn=new Time();
    	nn.setToNow();
    	Date d1,d2;
    	long ttSS=0;
		try {
			String D1S=Integer.toString(val.year)+"-"+Integer.toString(val.monthDay)+"-"+Integer.toString(val.monthDay)+" "+Integer.toString(val.hour)+":"+Integer.toString(val.minute)+":"+Integer.toString(val.second);
			String D2S=Integer.toString(nn.year)+"-"+Integer.toString(nn.monthDay)+"-"+Integer.toString(nn.monthDay)+" "+Integer.toString(nn.hour)+":"+Integer.toString(nn.minute)+":"+Integer.toString(nn.second);
			d1 = df.parse(D1S);
			d2 = df.parse(D2S);
	    	long diff=d2.getTime()-d1.getTime();
	    	ttSS=diff/1000;
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return ttSS;
    }
    public void OpenSerialPortForUpLoadDataToSABySD100()
    {
	// sPortForSA = MidData.sOpForSA.OpenSerialPortForSAOpenBySD100();
	MidData.portForSA = MidData.sOpForSA.OpenSerialPortForSAOpenBySD100();// MidData.sOp.OpenSerialPort();
	if (MidData.portForSA == null)
	{
	    //Toast.makeText(DataActivity.this,
	    //		MidData.IsChinese?"连接SA失败，无法上传数据到SA系统(按SD100)":"Connect SA failed, please check connection(SD100)", Toast.LENGTH_SHORT).show();
	    return;
	}
	MidData.m_OutputStream_SA = MidData.portForSA.getOutputStream();
	MidData.m_InputStream_SA = MidData.portForSA.getInputStream();
    }
    public void OpenSerialPortForUpLoadDataToLIS()
    {
    	if(MidData.fd>0)
    	{
    		
    		KyleSocketClass.DeInitPort(MidData.fd);
    		try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
		MidData.fd = KyleSocketClass.InitPort(MidData.IpStr, MidData.Port);// MidData.sOp.OpenSerialPort();
		MidData.LisNetConnetTime=new Time();
		MidData.LisNetConnetTime.setToNow();
		System.out.println(MidData.IpStr+"--"+MidData.Port+"--"+MidData.fd+"----打开网口");
    }
    @Override
    public void run()
    {
	super.run();

	synchronized (lock)// while(true)
	{
		try {
			sleep(3000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(MidData.m_UpToLIS_net)
		{
			if (MidData.fd <0||MidData.LisNetConnetTime==null||GetSecond(MidData.LisNetConnetTime)>3600)
				OpenSerialPortForUpLoadDataToLIS();
			if (MidData.fd >0)
			{
				//Cursor cursor=null;
			    //new UpData2PcInSD100Byk("lock", i).start();
			    //cursor = MidData.sqlOp.selectResourceDataByID(cursor,Num);
			    //System.out.println(cursor.getCount()+"----selllll");
			    UpLaodData2PC();
			    if(MidData.LisUploadLine)
			    {
			    	UploadLine2PC();
			    }
			    //cursor.close();
			}
		}
		else if(MidData.m_UpToLIS_ser)
		{
			
			OpenSerialPortForUpLoadDataToSABySD100();
			
    		if (MidData.m_OutputStream_SA != null)
			{
    			UpLaodData2PC();
    			if(MidData.LisUploadLine)
			    {
			    	UploadLine2PC();
			    }
			}
		}
	    //Looper.prepare();
	    //KyleSocketClass.DeInitPort(MidData.fd);
	    //MidData.fd=-1;
	 //Toast.makeText(getApplicationContext(), MidData.IsChinese?"发送数据到LIS成功":"Send data to LIS successfully", Toast.LENGTH_LONG).show();
	 //mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
	    //Looper.loop();
	 //button_UpLoad.setEnabled(true);
	}

    }
}
