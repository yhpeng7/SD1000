package com.realect.sd1000.devicePortOperation;

import java.io.File;
import java.io.IOException;



import com.example.sd1000application.ConsumeActivity;
//import com.example.sd1000application.ConsumeActivity.ReadSDCardTestThread;
import com.realect.sd1000.parameterStatus.*;

import KyleSocket_api.KyleSocketClass;
import android.R.bool;
import android.text.format.Time;
import android_serialport_api.SerialPort;

public class ICCreditCardPort {
	byte[] buffer = new byte[512];
	public static boolean Test0K=false;
    public SerialPort OpenSerialPortForICCreditCard()
    {
		SerialPort sPort = null;
		
		try
		{
		    sPort = new SerialPort(new File("/dev/ttySAC1"), 19200, 0);//115200
		}
		catch (SecurityException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		catch (IOException e)
		{
		    // TODO Auto-generated catch block'
		    e.printStackTrace();
		}
		return sPort;
    }
    public void SetReciveStatus(boolean flag)
    {
    	Test0K=flag;    	
    }
    public boolean GetReciveStatus()
    {
    	return Test0K;    	
    }
    public void ReciveDataFromSerial()
    {
    	ICCardReadThread mReadThread = new ICCardReadThread();

	if (MidData.m_InputStream_ICCreditCard != null)
	{
	     mReadThread.start();
	}
    }
    public String SendForSet()
    {// 发送获取序列号的请求
		byte[] buf = new byte[32];
		buf[0] = 0x03;
		buf[1] = 0x11;
		buf[2] = 0x01;
		buf[3] = GetCheckValue(buf, 3);
		SendToPort(buf, 4);
		System.out.println("_-----SDTEST__SEND------");
		return null;
    }
   
    public boolean TestICCard() throws IOException
    {
    	
    	//KyleSocketClass.InitPort(MidData.IpStr, 9999);
    	SendForSet();
		try
		{
		    Thread.sleep(5000);
		}
		catch (InterruptedException e1)
		{
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		Test0K=false;
		ICCardReadThread mReadThread1 = new ICCardReadThread();
		mReadThread1.start();
		
		
		try
		{
		    Thread.sleep(10000);
		}
		catch (InterruptedException e1)
		{
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}
		return Test0K;
		//if(!Test0K)
		//{			
			//KyleSocketClass.InitPort(MidData.IpStr, 9999);
		//}
    	
    }
    volatile boolean IsThRun=false;
    volatile long LastTick=0;
    public class ICCardReadThread extends Thread
    {
		@SuppressWarnings("deprecation")
		@Override
		
		public void run()
		{		    
		    super.run();
		   
		    if(IsThRun)
		    	{
		    	System.out.println("Thread is runing!");
		    	return;
		    	}
		    IsThRun=true;
		    boolean rec = false;
		    byte[] Ptr = new byte[1];
		    int len=0;
		    while (MidData.m_isInterrupted_ICCreditCard)
			{
				//System.out.println("__________ReadThreadIC_RUNNING________Start");
		    	rec = false;
		    	len=0;
		    	while(!rec)
		    	{
		    		try
					{
						int recL=MidData.m_InputStream_ICCreditCard.read(Ptr);
						
						if(recL==0)
							continue;
						if(recL>0)
						{
							 Time t = new Time();
							    t.setToNow();
							    long Xnew=t.toMillis(false);
							    if(Xnew-LastTick>500)
							    {
							    	len=0;
							    }
								LastTick=Xnew;
						}

						MidData.ICCardSerialPortData[len++]=Ptr[0];
						System.out.println(BytesToStr(Ptr,1)+"__________ReadThreadIC_Recived----");
						
						if(len>64)
						{
							rec=true;
						}
						if((len-1)==MidData.ICCardSerialPortData[0])
						{
							System.out.println(BytesToStr(MidData.ICCardSerialPortData,len)+"__________ReadThreadIC_RUNNING________Recived----"+len);
							byte tr=GetCheckValue(MidData.ICCardSerialPortData, len-1);
							if (MidData.ICCardSerialPortData[len-1] == tr)
						    {			
						    	Test0K=true;
						    	rec=true;
						    	//MidData.ICCardSerialPortData = Ptr;			
						    	System.out.println("--------ICOK------");
						    }							
						}
						else if((len-1)>MidData.ICCardSerialPortData[0]||(len-1)==31)
						{
							rec=true;
						}
						try {
							sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					catch (IOException e)
					{
					    // TODO Auto-generated catch block
					   // System.out.println("````````````ReciveError______SerialIC```````````````");
					    e.printStackTrace();
					}
		    	}
		    	try {
					sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				    	
			}
		    IsThRun=false;
		}
    }
    public byte GetCheckValue(byte[] buf, int Length)
    {
	byte Re = 0;
	for (int i = 0; i < Length; i++)
	    Re ^= buf[i];
	return Re;
    }

    public Boolean DEV_WriteComm(byte[] Buf, int len)
    {
	//System.out.println("-----SendDataToSerial------");
	int time = 0;
	boolean send = false;
	MidData.isRecivedIC = false;
	while (true)
	{
	    if (!MidData.isRecivedIC)
	    {
		SendToPort(Buf, len);
		time++;
	    }
	    else
	    {
		send = true;
		break;
	    }
	    try
	    {
		Thread.sleep(1000);
	    }
	    catch (Exception e)
	    {
		// TODO: handle exception
	    }
	    if (time >= 1)
	    {
		break;
	    }
	}
	return send;
    }
    public static String BytesToStr(byte[] target,int length)
    {
     String buf = "";
     String hex="";
     for (int i = 0, j = length; i < j; i++) {
    	 hex = Integer.toHexString(target[i] & 0xFF); 
         if (hex.length() == 1) { 
           hex = '0' + hex; 
         }
         buf+=hex+" ";
     }
     return buf;
    }
    public void SendToPort(byte[] point, int len)
    {
	byte[] data = new byte[len];
	for (int i = 0; i < len; i++)
	{
	    data[i] = point[i];
	}
	
	try
	{
	    MidData.m_OutputStream_ICCreditCard.write(data);
	    
	    
	    System.out.println(BytesToStr(data,len)+"_________m_OutputStreamForICCreditCard");
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
}
