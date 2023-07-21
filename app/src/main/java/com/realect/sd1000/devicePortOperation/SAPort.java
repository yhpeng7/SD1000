package com.realect.sd1000.devicePortOperation;

import java.io.File;
import java.io.IOException;

import com.realect.sd1000.parameterStatus.*;

import android_serialport_api.SerialPort;

public class SAPort extends Thread{
	byte[] buffer = new byte[512];
    String Port4SA="/dev/ttyUSB1";//暂时借用读卡器的串口（ttySAC1）调试  本来该用 ttyUSB1
    public SerialPort OpenSerialPortForSA()
    {
	SerialPort sPort = null;
	try
	{
	    sPort = new SerialPort(new File(Port4SA), 115200, 0);
	}
	catch (SecurityException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return sPort;
    }
    public SerialPort OpenSerialPortForSAOpenBySD100()
    {
	SerialPort sPort = null;
	try
	{
	    sPort = new SerialPort(new File(Port4SA), MidData.LisBrt, 0);
	}
	catch (SecurityException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return sPort;
    }
    public void SendToSAPortClose()
    {
	//sPort.close();
	try
	{
		if(MidData.m_OutputStream_SA!=null)
			MidData.m_OutputStream_SA.close();
	    MidData.m_OutputStream_SA=null;
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    public void SendToSAPort(byte[] point, int len)
    {
	byte[] data = new byte[len];
	for (int i = 0; i < len; i++)
	{
	    data[i] = point[i];
	}
	try
	{
	    MidData.m_OutputStream_SA.write(data);
	    System.out.println(new String(data,0,len)+"__________SendToSAPort---");
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }
}
