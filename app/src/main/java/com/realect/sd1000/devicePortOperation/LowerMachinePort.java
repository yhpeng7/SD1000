package com.realect.sd1000.devicePortOperation;

import com.realect.sd.other.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;




import com.realect.sd1000.sqlite.*;
import com.realect.sd1000.Calculate.CalculateFormula;
import com.realect.sd1000.parameterStatus.IsSaveToDatabaseData;
import com.realect.sd1000.parameterStatus.MidData;
import com.realect.sd1000.parameterStatus.TestParameters;
import com.realect.sd1000.Calculate.*;

import KyleSocket_api.KyleSocketClass;
import android.database.Cursor;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;
import android_serialport_api.SerialPort;

public class LowerMachinePort extends Thread{
	String lock = new String("lock");
	String lockLis = new String("locklis");
	Queue<String> SAUpLoadQueue = new LinkedList<String>(); 
	String lockSA = new String("lockSa");
	byte[] buffer = new byte[512];
	DecimalFormat fnum1 = new DecimalFormat("##0.0");
	DecimalFormat fnum2 = new DecimalFormat("##0.00");
	byte[] dd = { 0x01, 0x02, 0x04, 0x08 };
	byte[] bitd = { 0x0C, 0x03 };
	//private DecimalFormat fnum10 = new DecimalFormat("##0000000000");
	Time t = new Time();
	Queue<PrintDataStructClass> PrintQueue=new LinkedList<PrintDataStructClass>();
	public SerialPort OpenSerialPort()
    {
		SerialPort sPort = null;
		try
		{	
		    sPort = new SerialPort(new File("/dev/ttySAC3"), 115200, 0);
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
    public int GetCrc(char[] pBuffer, int length)
    {
		final int crc_table[] =
		{   0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50A5, 0x60C6, 0x70E7,
			0x8108, 0x9129, 0xA14A, 0xB16B, 0xC18C, 0xD1AD, 0xE1CE, 0xF1EF,
			0x1231, 0x0210, 0x3273, 0x2252, 0x52B5, 0x4294, 0x72F7, 0x62D6,
			0x9339, 0x8318, 0xB37B, 0xA35A, 0xD3BD, 0xC39C, 0xF3FF, 0xE3DE,
			0x2462, 0x3443, 0x0420, 0x1401, 0x64E6, 0x74C7, 0x44A4, 0x5485,
			0xA56A, 0xB54B, 0x8528, 0x9509, 0xE5EE, 0xF5CF, 0xC5AC, 0xD58D,
			0x3653, 0x2672, 0x1611, 0x0630, 0x76D7, 0x66F6, 0x5695, 0x46B4,
			0xB75B, 0xA77A, 0x9719, 0x8738, 0xF7DF, 0xE7FE, 0xD79D, 0xC7BC,
			0x48C4, 0x58E5, 0x6886, 0x78A7, 0x0840, 0x1861, 0x2802, 0x3823,
			0xC9CC, 0xD9ED, 0xE98E, 0xF9AF, 0x8948, 0x9969, 0xA90A, 0xB92B,
			0x5AF5, 0x4AD4, 0x7AB7, 0x6A96, 0x1A71, 0x0A50, 0x3A33, 0x2A12,
			0xDBFD, 0xCBDC, 0xFBBF, 0xEB9E, 0x9B79, 0x8B58, 0xBB3B, 0xAB1A,
			0x6CA6, 0x7C87, 0x4CE4, 0x5CC5, 0x2C22, 0x3C03, 0x0C60, 0x1C41,
			0xEDAE, 0xFD8F, 0xCDEC, 0xDDCD, 0xAD2A, 0xBD0B, 0x8D68, 0x9D49,
			0x7E97, 0x6EB6, 0x5ED5, 0x4EF4, 0x3E13, 0x2E32, 0x1E51, 0x0E70,
			0xFF9F, 0xEFBE, 0xDFDD, 0xCFFC, 0xBF1B, 0xAF3A, 0x9F59, 0x8F78,
			0x9188, 0x81A9, 0xB1CA, 0xA1EB, 0xD10C, 0xC12D, 0xF14E, 0xE16F,
			0x1080, 0x00A1, 0x30C2, 0x20E3, 0x5004, 0x4025, 0x7046, 0x6067,
			0x83B9, 0x9398, 0xA3FB, 0xB3DA, 0xC33D, 0xD31C, 0xE37F, 0xF35E,
			0x02B1, 0x1290, 0x22F3, 0x32D2, 0x4235, 0x5214, 0x6277, 0x7256,
			0xB5EA, 0xA5CB, 0x95A8, 0x8589, 0xF56E, 0xE54F, 0xD52C, 0xC50D,
			0x34E2, 0x24C3, 0x14A0, 0x0481, 0x7466, 0x6447, 0x5424, 0x4405,
			0xA7DB, 0xB7FA, 0x8799, 0x97B8, 0xE75F, 0xF77E, 0xC71D, 0xD73C,
			0x26D3, 0x36F2, 0x0691, 0x16B0, 0x6657, 0x7676, 0x4615, 0x5634,
			0xD94C, 0xC96D, 0xF90E, 0xE92F, 0x99C8, 0x89E9, 0xB98A, 0xA9AB,
			0x5844, 0x4865, 0x7806, 0x6827, 0x18C0, 0x08E1, 0x3882, 0x28A3,
			0xCB7D, 0xDB5C, 0xEB3F, 0xFB1E, 0x8BF9, 0x9BD8, 0xABBB, 0xBB9A,
			0x4A75, 0x5A54, 0x6A37, 0x7A16, 0x0AF1, 0x1AD0, 0x2AB3, 0x3A92,
			0xFD2E, 0xED0F, 0xDD6C, 0xCD4D, 0xBDAA, 0xAD8B, 0x9DE8, 0x8DC9,
			0x7C26, 0x6C07, 0x5C64, 0x4C45, 0x3CA2, 0x2C83, 0x1CE0, 0x0CC1,
			0xEF1F, 0xFF3E, 0xCF5D, 0xDF7C, 0xAF9B, 0xBFBA, 0x8FD9, 0x9FF8,
			0x6E17, 0x7E36, 0x4E55, 0x5E74, 0x2E93, 0x3EB2, 0x0ED1, 0x1EF0, };
		char crc = 0xFFFF;
		char i;
		int len = 0;
		while (length > 0)
		{
		    i = (char) (crc >> 8);
		    crc <<= 8;
		    char bb = pBuffer[len++];
		    int nn = bb;
		    int num = i ^ bb;
		    if (num > 255)
		    {
			// num=255;
			// System.out.println(new String(pBuffer) + "__pBuffer__" + len
			// + "____len____" + i + "___i___" + num + "___num__");
			num = 255;
		    }
		    crc ^= crc_table[num];
		    length--;
		}
		return crc;
    }

    
    public int GetCrc(byte[] pBuffer, int length)
    {
		final int crc_table[] =
		{  
			0x0000, 0x1021, 0x2042, 0x3063, 0x4084, 0x50A5, 0x60C6, 0x70E7,
			0x8108, 0x9129, 0xA14A, 0xB16B, 0xC18C, 0xD1AD, 0xE1CE, 0xF1EF,
			0x1231, 0x0210, 0x3273, 0x2252, 0x52B5, 0x4294, 0x72F7, 0x62D6,
			0x9339, 0x8318, 0xB37B, 0xA35A, 0xD3BD, 0xC39C, 0xF3FF, 0xE3DE,
			0x2462, 0x3443, 0x0420, 0x1401, 0x64E6, 0x74C7, 0x44A4, 0x5485,
			0xA56A, 0xB54B, 0x8528, 0x9509, 0xE5EE, 0xF5CF, 0xC5AC, 0xD58D,
			0x3653, 0x2672, 0x1611, 0x0630, 0x76D7, 0x66F6, 0x5695, 0x46B4,
			0xB75B, 0xA77A, 0x9719, 0x8738, 0xF7DF, 0xE7FE, 0xD79D, 0xC7BC,
			0x48C4, 0x58E5, 0x6886, 0x78A7, 0x0840, 0x1861, 0x2802, 0x3823,
			0xC9CC, 0xD9ED, 0xE98E, 0xF9AF, 0x8948, 0x9969, 0xA90A, 0xB92B,
			0x5AF5, 0x4AD4, 0x7AB7, 0x6A96, 0x1A71, 0x0A50, 0x3A33, 0x2A12,
			0xDBFD, 0xCBDC, 0xFBBF, 0xEB9E, 0x9B79, 0x8B58, 0xBB3B, 0xAB1A,
			0x6CA6, 0x7C87, 0x4CE4, 0x5CC5, 0x2C22, 0x3C03, 0x0C60, 0x1C41,
			0xEDAE, 0xFD8F, 0xCDEC, 0xDDCD, 0xAD2A, 0xBD0B, 0x8D68, 0x9D49,
			0x7E97, 0x6EB6, 0x5ED5, 0x4EF4, 0x3E13, 0x2E32, 0x1E51, 0x0E70,
			0xFF9F, 0xEFBE, 0xDFDD, 0xCFFC, 0xBF1B, 0xAF3A, 0x9F59, 0x8F78,
			0x9188, 0x81A9, 0xB1CA, 0xA1EB, 0xD10C, 0xC12D, 0xF14E, 0xE16F,
			0x1080, 0x00A1, 0x30C2, 0x20E3, 0x5004, 0x4025, 0x7046, 0x6067,
			0x83B9, 0x9398, 0xA3FB, 0xB3DA, 0xC33D, 0xD31C, 0xE37F, 0xF35E,
			0x02B1, 0x1290, 0x22F3, 0x32D2, 0x4235, 0x5214, 0x6277, 0x7256,
			0xB5EA, 0xA5CB, 0x95A8, 0x8589, 0xF56E, 0xE54F, 0xD52C, 0xC50D,
			0x34E2, 0x24C3, 0x14A0, 0x0481, 0x7466, 0x6447, 0x5424, 0x4405,
			0xA7DB, 0xB7FA, 0x8799, 0x97B8, 0xE75F, 0xF77E, 0xC71D, 0xD73C,
			0x26D3, 0x36F2, 0x0691, 0x16B0, 0x6657, 0x7676, 0x4615, 0x5634,
			0xD94C, 0xC96D, 0xF90E, 0xE92F, 0x99C8, 0x89E9, 0xB98A, 0xA9AB,
			0x5844, 0x4865, 0x7806, 0x6827, 0x18C0, 0x08E1, 0x3882, 0x28A3,
			0xCB7D, 0xDB5C, 0xEB3F, 0xFB1E, 0x8BF9, 0x9BD8, 0xABBB, 0xBB9A,
			0x4A75, 0x5A54, 0x6A37, 0x7A16, 0x0AF1, 0x1AD0, 0x2AB3, 0x3A92,
			0xFD2E, 0xED0F, 0xDD6C, 0xCD4D, 0xBDAA, 0xAD8B, 0x9DE8, 0x8DC9,
			0x7C26, 0x6C07, 0x5C64, 0x4C45, 0x3CA2, 0x2C83, 0x1CE0, 0x0CC1,
			0xEF1F, 0xFF3E, 0xCF5D, 0xDF7C, 0xAF9B, 0xBFBA, 0x8FD9, 0x9FF8,
			0x6E17, 0x7E36, 0x4E55, 0x5E74, 0x2E93, 0x3EB2, 0x0ED1, 0x1EF0, };
		char crc = 0xFFFF;
		char i;
		int len = 0;
		while (length > 0)
		{
		    i = (char) (crc >> 8);
		    crc <<= 8;
		    char bb = (char) pBuffer[len++];
		    int nn = bb;
		    int num = i ^ bb;
		    if (num > 255)
		    {
			// num=255;
			// System.out.println(new String(pBuffer) + "__pBuffer__" + len
			// + "____len____" + i + "___i___" + num + "___num__");
			num = 255;
		    }
		    crc ^= crc_table[num];
		    length--;
		}
		return crc;
    }
    public char Hex2Ascii(int i)
    {
		if (i >= 0 && i < 10)
		    return (char) (i + '0');
		else if (i > 9 && i < 16)
		    return (char) (i - 10 + 'A');
		else
		    return 0;
    }

    public char Hex2Ascii(long i)
    {
		if (i >= 0 && i < 10)
		    return (char) (i + '0');
		else if (i > 9 && i < 16)
		    return (char) (i - 10 + 'A');
		else
		    return 0;
    }
    public void ReturnOrder(char c)
    {
	byte buff = 0x41;
	byte[] data = new byte[10];
	data[0] = (byte) c;
	SendData(buff, data, 1, MidData.m_OutputStream_LowerMachine);
    }
    public boolean SendData(byte order, byte[] data, int num,
    	    OutputStream m_OutputStream)
        {
    	if (m_OutputStream == null)
    	{
    	    return false;
    	}

    	byte[] lpOutBuffer = new byte[num + 7];
    	char[] InStr = new char[num + 9];
    	int len = 0;
    	lpOutBuffer[len++] = 0x3E;
    	lpOutBuffer[len++] = order;
    	for (int i = 0; i < num; i++)
    	{
    	    lpOutBuffer[len++] = data[i];
    	}
    	for (int i = 0; i < len + 2; i++)
    	{
    	    InStr[i] = (char) lpOutBuffer[i];
    	}
    	int Crc = GetCrc(InStr, len);
    	lpOutBuffer[len++] = (byte) Hex2Ascii((Crc & 0xF000) >> 12);
    	lpOutBuffer[len++] = (byte) Hex2Ascii((Crc & 0x0F00) >> 8);
    	lpOutBuffer[len++] = (byte) Hex2Ascii((Crc & 0x00F0) >> 4);
    	lpOutBuffer[len++] = (byte) Hex2Ascii(Crc & 0x000F);
    	lpOutBuffer[len++] = 0x0D;

    	try
    	{
    		//System.out.println(new String(lpOutBuffer)+"----"+"SendToSerial");
    	    m_OutputStream.write(lpOutBuffer);
    	    lpOutBuffer=null;
    	    return true;
    	}
    	catch (IOException e)
    	{
    	    e.printStackTrace();
    	    return false;
    	}
    }
    public synchronized Boolean SendDataToSerial(byte buff, byte[] data, int len)
    {
	// System.out.println("-----SendDataToSerial------");
	int time = 0;
	boolean send = false;
	//
	MidData.m_isRecived = false;
	while (true)
	{
	    //
	    if (!MidData.m_isRecived)
	    {
		SendData(buff, data, len, MidData.m_OutputStream_LowerMachine);
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
	    if (time >= 3)
	    {
		break;
	    }
	}
	return send;
    }
    private void Initial(){
    	for(int i=0;i<100;i++)
    	{
    		TestParameters mm=new TestParameters();
    		mm.testStatus=0;
    		MidData.mTestParameters[i]=mm;
    	}
    }
    private class TestTimeCountThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (MidData.m_isInterrupted_LowerMachine)
			{
				if(MidData.LaveTestTime>0)
				{
					MidData.LaveTestTime--;	
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
    
    private class ReadThread extends Thread
	{
		@SuppressWarnings("deprecation")
		@Override
		public void run()
		{
	
		    int Addr = 0;
		    byte[] Ptr = new byte[1];
		    if (MidData.m_InputStream_LowerMachine == null)
		    {
			return;
		    }
		    ExecuteSqlQueue mExecuteSqlQueue=new ExecuteSqlQueue();
		    mExecuteSqlQueue.start();
		    TestTimeCountThread mTestTimeCountThread=new TestTimeCountThread();
		    mTestTimeCountThread.start();
		    //Initial();
		    super.run();
		    while (MidData.m_isInterrupted_LowerMachine)
		    {
			try
			{
			    MidData.m_InputStream_LowerMachine.read(Ptr);
			}
			catch (IOException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
	
			}
	
			if (Ptr[0] == '>')
			{
			    Addr = 0;
			}
			if (Addr > 511)
			{
			    Addr = 0;
			}
			buffer[Addr++] = Ptr[0];
	
			if (Addr == 2)
			{
			    if ((Ptr[0] != 'A' && Ptr[0] != 'B') || buffer[0] != '>')
			    {
				Addr = 0;
			    }// 地址错误
			}
			else if (Ptr[0] == 0x0D && Addr > 3)
			{
			    
			    for (int i = 3; i < Addr - 5; i++)
			    {
			    	if(!(buffer[i]>='0'&&buffer[i]<='9')&&!(buffer[i]>='A'&&buffer[i]<='F')&&buffer[i]!='.')
			    	{
			    		Addr=0;
			    		break;
			    	}
			    }
			    if (Addr < 5)
			    {
					Addr = 0;
					continue;
			    }
			    char[] InStr = new char[Addr - 5];
			    int len = 0;
			    for (int i = 0; i < Addr - 5; i++)
			    {
			    	InStr[len++] = (char) buffer[i];
			    }
			    int Crc = GetCrc(InStr, len);
			    char Crc1 = Hex2Ascii((Crc & 0xF000) >> 12);
			    char Crc2 = Hex2Ascii((Crc & 0x0F00) >> 8);
			    char Crc3 = Hex2Ascii((Crc & 0x00F0) >> 4);
			    char Crc4 = Hex2Ascii(Crc & 0x000F);
			    char crc1R = (char) buffer[len++];
			    char crc2R = (char) buffer[len++];
			    char crc3R = (char) buffer[len++];
			    char crc4R = (char) buffer[len++];
			    InStr=null;
			    if(crc1R == Crc1 && crc2R == Crc2 && crc3R == Crc3 && crc4R == Crc4)
			    {
			    	//System.out.println(new String(buffer)+"---ReciveFromSer--");
					
			    	if (buffer[1] == 'A')
					{
					    if (buffer[2] == 'A')
					    {
							MidData.m_isRecived = true;
							for (int i = 3; i < Addr - 5; i++)
							{
							    MidData.m_systemVision[i - 3] = buffer[i];
							}
					    }
					    else if (buffer[2] == 'R')
					    {
							MidData.m_isRecived = true;
							for (int i = 0; i < Addr; i++)
							{
							    MidData.m_systemError[i] = buffer[i];
							}
					    }
					    else if (buffer[2] == 'F')
					    {
							MidData.m_isRecived = true;
							if (buffer[3] == '3')
							{
							    ReturnOrder('C');
							    for (int i = 0; i < Addr; i++)
							    {
							    	MidData.m_pulusePeriod[i] = buffer[i];
							    }
							    MidData.IsReviseRunPeriod=false;
							    MidData.isRunPeriodFinish=true;
							}
							else
							{
							    for (int i = 0; i < Addr; i++)
							    {
							    	MidData.m_statusSelfTestAisle[i] = buffer[i];
							    }
							}
					    }
					    else if (buffer[2] == 'T')
					    {
							MidData.m_isRecived = true;
							for (int i = 0; i < Addr; i++)
							{
							    MidData.m_temperatureData[i] = buffer[i];
							}
					    }
					    else if (buffer[2] == 'B' || buffer[2] == 'E' || buffer[2] == 'Y' || buffer[2] == 'g')
					    {
					    	MidData.m_isRecived = true;
					    }
					    else if (buffer[2] == 'C')
					    {
							if (buffer[3] == 0x30)
							{
							    MidData.m_isRecived = true;
			
							}
							else if (buffer[3] == 0x31 || buffer[3] == 0x32)
							{
							    MidData.isTop = false;
							    MidData.isBelow = false;
							    MidData.m_isRecived = true;
							}
							else if (buffer[3] == 0x33)
							{
							    MidData.isBelow = false;
							    MidData.isTop = true;
							}
							else if (buffer[3] == 0x34)
							{
							    MidData.isTop = false;
							}
					    }
					    else if (buffer[2] == 'M')
					    {
							byte[] bt = new byte[1];
							bt[0] = buffer[3];
							int tm = Integer.parseInt(new String(bt), 16);
							if (tm == MidData.DriRunTime)
							{
							    MidData.m_isRecived = true;
							}
					    }
					    else if (buffer[2] == 'X')
					    {
					    	//System.out.println(new String(buffer)+"------AX");
							byte udflag = buffer[3];
							if (udflag == 0x30)
							{
							    MidData.m_upDown = "上";
							}
							else if (udflag == 0x31)
							{
							    MidData.m_upDown = "下";
							}
					    }
					    else if (buffer[2] == 'V')
					    {
							byte[] bt = new byte[2];
							bt[0] = buffer[3];
							bt[1] = buffer[4];
							int tm = Integer.parseInt(new String(bt), 16);
							if (tm == MidData.m_pulsePeriodSetLast)
							{
							    MidData.m_isRecived = true;
							}
					    }
					    else if (buffer[2] == 'S')
					    {
							int n = 3;
							byte[] bt = new byte[4];
							for (int k = 0; k < 4; k++)
							    bt[k] = buffer[n++];
							int tm = Integer.parseInt(new String(bt), 16);
							if (MidData.m_voltageReferenceSetLast == tm)
							{
							    MidData.m_isRecived = true;
							}
					    }
					    else if (buffer[2] == 'W')
					    {
						    MidData.m_isRecived = true;				
					    }
					    else if (buffer[2] == 'L')
					    {
						    MidData.LaveTestTime = MidData.mSetParameters.RunPeriodSet;				
					    }
					    else if (buffer[2] == 'O')
					    {
							int n = 3;
							byte[] bt = new byte[4];
							for (int k = 0; k < 4; k++)
							    bt[k] = buffer[n++];
							int tm = Integer.parseInt(new String(bt), 16);
							if (MidData.m_voltageCompareSetLast == tm)
							{
							    MidData.m_isRecived = true;
							}
					    }
					    else if (buffer[2] == 'N')
					    {							
							MidData.m_isRecived = true;
					    }
					    else if (buffer[2] == 'H')
					    {
					    	//System.out.println(new String(buffer)+"---ReciveFromSer--");
							for (int i = 0; i < Addr; i++)
							{
							    MidData.m_heightAisle[i] = buffer[i];
							}
							//System.out.println(new String(MidData.m_heightAisle)+"---ReciveFromSer----H--");
						}
					    else if (buffer[2] == 'G')
					    {
					    	//System.out.println(new String(buffer)+"---ReciveFromSer--");
							for (int i = 0; i < Addr; i++)
							{
							    MidData.m_statusAisle[i] = buffer[i];
							}
						}
		
					    else if (buffer[2] == 'D')
					    {
							for (int i = 0; i < Addr; i++)
							{
							    MidData.m_debug[i] = buffer[i];
							}
							UpdateDebugThread mUpdateDebugThread = new UpdateDebugThread();
							mUpdateDebugThread.start();
					    }
					    else if (buffer[2] == 'h')
					    {
							for (int i = 0; i < Addr; i++)
							{
							    MidData.m_recompenseAisle[i] = buffer[i];
							}
							GetRecompenseThread mGetRecompenseThread = new GetRecompenseThread();
							mGetRecompenseThread.start();
					    }
					}
					else if (buffer[1] == 'B')
					{
					    if (buffer[2] == 'B')
					    {
						for (int i = 0; i < Addr; i++)
						{
						    MidData.m_debug[i] = buffer[i];
						}
						UpdateDebugThread mUpdateDebugThread = new UpdateDebugThread();
						mUpdateDebugThread.start();
					    }
					}
			    }
			    for (int i = 0; i < Addr; i++)
			    {
			    	buffer[i] = '0';
			    }
			    Addr = 0;
			}
		    }
		}
	}
    public void ReciveDataFromSerial()
    {
    	for (int i = 0; i < 100; i++)
    	{
    	    MidData.mTestParameters[i].aisleTestTime = 0;
    	    // MidData.passageIsOn[i] = false;
    	    MidData.mTestParameters[i].testStatus = 0;
    	    //MidData.dataOfTest[0][i] = 0;
    	    //
    	    //MidData.Difference_First_Last[i] = "";
    	    //
    	    MidData.mTestParameters[i].IsNewSample = true;
    	    //
    	    MidData.mTestParameters[i].ReDetect = false;
    	}
    	ReadThread mReadThread = new ReadThread();
    	LoadSelfTestStatusThread mLoadSelfTestStatusThread = new LoadSelfTestStatusThread();
	
	    mReadThread.start();
	    mLoadSelfTestStatusThread.start();	
    }
    
    
    private class UpdatePassageStatusThread extends Thread
    {

	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    try
	    {
		sleep(100);
	    }
	    catch (InterruptedException e1)
	    {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	    boolean testPassageUp = false;
	    boolean testPassageDown = false;
	    int lastdata = 0;
	    while (MidData.m_isInterrupted_LowerMachine && MidData.TestFinished)
	    {
		if (MidData.m_systemError[0] == '>' && MidData.m_systemError[1] == 'A'
			&& MidData.m_systemError[2] == 'R')
		{
		    ReturnOrder('R');
		    byte[] error = new byte[1];
		    error[0] = MidData.m_systemError[3];
		    String bs = new String(error);
		    int intdd = Integer.parseInt(bs, 16);
		    int byteofbitlength = CalculateFormula.GetByteOfBitLength(error[0]);
		    MidData.ErrorStrShow = "";
		    for (int j = 0; j < byteofbitlength; j++)
		    {
			int bitdata = (intdd & dd[j]) >> j;
			if (bitdata == 1)
			{
			    if (MidData.ErrorStrShow.equals(""))
			    {
				MidData.ErrorStrShow = MidData.IsChinese==1?"错误：":"ERROR:"
					+ MidData.ErrorInfor[j];
			    }
			    else
				MidData.ErrorStrShow += ";"
					+ MidData.ErrorInfor[j];
			    MakeBuzzerBeep();
			}
		    }
		    MidData.m_systemError[2] = 0;
		}
		else if (MidData.m_temperatureData[0] == '>'
			&& MidData.m_temperatureData[1] == 'A'
			&& MidData.m_temperatureData[2] == 'T')
		{
		    int len = 3;
		    for (int ti = 0; ti < 5; ti++)
		    {
			byte sign = MidData.m_temperatureData[len++];
			byte[] tem = new byte[3];
			tem[0] = MidData.m_temperatureData[len++];
			tem[1] = MidData.m_temperatureData[len++];
			tem[2] = MidData.m_temperatureData[len++];
			String temStr = new String(tem);
			MidData.Temperature[ti] = sign == '0' ? ((Double
				.parseDouble(Integer.toString(Integer.parseInt(
					temStr, 16))) / 10.0) + MidData.mSetParameters.TemperatureRevise)
				: -((Double
					.parseDouble(Integer.toString(Integer
						.parseInt(temStr, 16))) / 10.0) + MidData.mSetParameters.TemperatureRevise);
		    }
		    MidData.TemperatureErrorShow = "";
		    double averageTem=CalculateFormula.AnverageTem(MidData.Temperature);
		    for (int tt = 0; tt < 5; tt++)
		    {
			if ((MidData.Temperature[tt] - averageTem) > 2)
			{
			    MidData.TemperatureErrorShow += "T"
				    + Integer.toString(tt) + "1";
			}
			else
			{
			    if ((MidData.Temperature[tt] - averageTem) < -2)
			    {
				MidData.TemperatureErrorShow += "T"
					+ Integer.toString(tt) + "0";
			    }
			}
		    }
		    MidData.m_temperatureData[1] = '0';
		    MidData.m_temperatureData[2] = '0';
		}
		else if (MidData.m_statusAisle[0] == '>'
			&& MidData.m_statusAisle[1] == 'A'
			&& MidData.m_statusAisle[2] == 'G')
		{
			//System.out.println(new String(MidData.m_statusAisle)+"----AG---Time----start");
		    int len = 0;
		    int lenSt = 3;
		    String strTime=GetTimeStr();
		    double twoR=MidData.mSetParameters.RunPeriodSet*2;
			long timeNow=GetTimeNow();
			if(MidData.startTemTestOK&&MidData.startTemTestOK1)
			{

			    //System.out.println("通道0状态Q:"+String.valueOf(MidData.mTestParameters[0].testStatus)+" "+MidData.m_statusAisle[3]);
				for (int i = 0; i < 50; i++)
			    {
				byte bt[] = new byte[1];
				bt[0] = MidData.m_statusAisle[lenSt++];
				String str = new String(bt);
				int btV = Integer.parseInt(new String(bt), 16);
				for (int j = 0; j < 2; j++)
				{
				    int add = len;
				    if (MidData.mTestParameters[add].aisleStatus == 1)
				    {
					int bV = (btV & bitd[j]) >> (2 - j * 2);
					if (bV == 3)
					{
					    //if (MidData.mTestParameters[add].testStatus!=4&& MidData.mTestParameters[add].testStatus!=5&&MidData.mTestParameters[add].testStatus!= 6)
						if(MidData.mTestParameters[add].testStatus == 2)
					    {
					    	MidData.mTestParameters[add].testStatus = 3;
					    }
					}
					else if (bV == 2)
					{
					    if (MidData.mTestParameters[add].testStatus!=4 && MidData.mTestParameters[add].testStatus!=5&&MidData.mTestParameters[add].testStatus!=6)
					    {
						
						MidData.mTestParameters[add].aisleTestFinished = false;
						
						if(MidData.IsConsumerReduce&&MidData.isDebugging==0)
						{
							if (MidData.mSetParameters.TestType==0&&MidData.mSetParameters.ERSCount<1)
							{
								MidData.mTestParameters[add].testStatus = 6;
								 len++;
								continue;
							}
							else if(MidData.mSetParameters.hematCount<1)
							{							
								MidData.mTestParameters[add].testStatus = 6;
								//System.out.println("耗材不足:"+String.valueOf(add)+" "+ MidData.mTestParameters[add].testStatus);
								 len++;
								continue;
							}
							
						}
						MidData.mTestParameters[add].testStatus = 2;
						// System.out.println("正在检测:"+String.valueOf(add));
					    }
					}
					else if (bV == 1)
					{
						int m=0; 
						//MidData.mTestParameters[add].testStatus = 10;
						//System.out.println(MidData.mTestParameters[add].testStatus+"--555--"+MidData.mSetParameters.TestType+"----"+add);
					    
						if (MidData.mTestParameters[add].testStatus == 0)// 这是为了保护一次上升过程中有状态切换并且有两种不同的测试标本放入时刘赏上传的Bug
					    {
							MidData.LastFindTime=GetTimeStr();
						MidData.mTestParameters[add].testStatus = 1;
						if(MidData.isDebugging==0)
						{
							if (MidData.mSetParameters.TestType==0)
							{
							    MidData.IsESRSelect[add] = true;
							    MidData.mTestParameters[add].aisleTestTimeCount=MidData.ERSRealTestTime;
							}
							else
							{
							    MidData.IsESRSelect[add] = false;
							    MidData.mTestParameters[add].aisleTestTimeCount=MidData.HematRealTestTime;
							    
							}
						}
						else if(MidData.isDebugging==1)
						{
							if (MidData.mSetParameters.TestType==0)
							{
							    MidData.IsESRSelect[add] = true;
							    MidData.mTestParameters[add].aisleTestTimeCount=((double) 5 * (double) 60) / MidData.mSetParameters.RunPeriodSet;
							}
							else
							{
							    MidData.IsESRSelect[add] = false;
							    MidData.mTestParameters[add].aisleTestTimeCount=2;
							}
							
						}
						else if(MidData.isDebugging==2)
						{
							if (MidData.mSetParameters.TestType==0)
							{
							    MidData.IsESRSelect[add] = true;						    
							}
							else
							{
							    MidData.IsESRSelect[add] = false;
							}
						}
						//System.out.println(String
						//	    .valueOf(add) + "--发现样本--"+MidData.IsESRSelect[add]);
						MidData.mTestParameters[add].aisleTestFinished = false;
						MidData.mTestParameters[add].ERSResult = "0.00";
						MidData.mTestParameters[add].hematResult = "0.00";
						// PWM mPWMThread = new PWM();
						// mPWMThread.SetPWMTime(1000);
						// mPWMThread.start();
						MakeBuzzerBeep();
					    }
					}
					else if (bV == 0)
					{
						
						/*double twoR=MidData.mSetParameters.RunPeriodSet*2;
						double interSec=getSecondBetweenTwoTime(MidData.mTestParameters[add].aisleSampleNumTime,strTime);
						*/if (MidData.mTestParameters[add].testStatus!=0)// 从其他状态切换到无样本
					    {
					    	if(MidData.mSetParameters.SetNumWay==1&&MidData.mTestParameters[add].aisleTestFinished&&MidData.SelAddress==add)
							{
								IsSaveToDatabaseData.Add=add;
								IsSaveToDatabaseData.TestType=MidData.mSetParameters.TestType;
								IsSaveToDatabaseData.ERSResult=MidData.mTestParameters[add].ERSResultRes;
								IsSaveToDatabaseData.hematResult=MidData.mTestParameters[add].hematResult;
								IsSaveToDatabaseData.hematTestHeight=MidData.mTestParameters[add].hematTestHeight;
								//System.out.println(IsSaveToDatabaseData.ERSResult+"----"+MidData.mTestParameters[add].aislePastTestData+"----"+IsSaveToDatabaseData.hematTestHeight+"------测试高度值----"+add);
								IsSaveToDatabaseData.aislePastTestData=MidData.mTestParameters[add].aislePastTestData;
								IsSaveToDatabaseData.isDebug=Integer.toString(MidData.isDebugging);
								IsSaveToDatabaseData.runPeriod=MidData.mSetParameters.RunPeriodSet;
								IsSaveToDatabaseData.Temperature=Double.toString(CalculateFormula.AnverageTem(MidData.Temperature));
								IsSaveToDatabaseData.TestRealCount=MidData.mTestParameters[add].aisleTestTimeCount;						
							}
						// System.out.println(String.valueOf(add) + "无样本");
					    	if(timeNow>MidData.mTestParameters[add].aisleSampleNumTime&&!MidData.mTestParameters[add].ReDetect)
					    	{
					    		MidData.mTestParameters[add].aisleNum = "";
								
					    	}
					    	MidData.mTestParameters[add].areaTemp="";
					    	MidData.mTestParameters[add].aisleTestTime = 0;
							MidData.mTestParameters[add].aisleHeght[0] = 0;
						// System.out.println("kong"
						// + String.valueOf(add));
						
					    }
					    
					    //MidData.mTestParameters[1].testStatus = 55;
						//System.out.println(MidData.mTestParameters[add].aisleSampleNumTime+"---"+timeNow+"--------ffffffff---"+i);
					    if(timeNow>MidData.mTestParameters[add].aisleSampleNumTime&&!MidData.mTestParameters[add].ReDetect)
				    	{
					    	MidData.mTestParameters[add].aisleNum="";
						    MidData.mTestParameters[add].aisleNumShow="";					    
				    	}
					    MidData.mTestParameters[add].aislePastTestData="";
					    MidData.mTestParameters[add].aisleTestTime=0;
					    MidData.mTestParameters[add].aisleDataSaved=false;
					    MidData.mTestParameters[add].testStatus = 0;
					    MidData.mTestParameters[add].warningNoConsume=false;
					    MidData.mTestParameters[add].ERSResult = "0.00";
					    MidData.mTestParameters[add].hematResult = "0.00";
					    MidData.mTestParameters[add].aisleTestFinished = false;
					    //System.out.println(add+"    "+MidData.mTestParameters[add].aisleSampleNumTime+"  "+strTime+"    "+interSec+"    "+twoR+"    "+MidData.mTestParameters[add].aisleNum+"   "+MidData.mTestParameters[add].aisleNumShow);
						
					}
					//if(add<10)
					 //for(int ttttttttttt=0;ttttttttttt<add;ttttttttttt++)
						//System.out.println(MidData.mTestParameters[ttttttttttt].testStatus+"--555--"+"----"+ttttttttttt);
				    }
				    else
				    {
					//MidData.mTestParameters[add].aisleStatus = -1;
				    }
				    len++;
				    //System.out.println(MidData.mTestParameters[add].aisleNum+"----"+"--NUMNUMNUM--"+add);
				}
			    }
				 String ts="";
				    for(int t=0;t<10;t++)
				    	ts+=Integer.toString(MidData.mTestParameters[t].testStatus)+"--"+MidData.IsESRSelect[t]+"---"+t+",";
				    //System.out.println(ts+Integer.toString(MidData.mSetParameters.TestType));
				    //boolean noPassageTest = true;
				    for (int i = 0; i < 100; i++)
				    {
				    	//System.out.println(MidData.mTestParameters[i].aisleTestFinished+"---"+MidData.mTestParameters[i].aisleStatus+"---"+MidData.mTestParameters[i].testStatus+"------Time---"+i);
					if (!MidData.mTestParameters[i].aisleTestFinished
						&& MidData.mTestParameters[i].aisleStatus == 1
						&& (MidData.mTestParameters[i].testStatus==2 || MidData.mTestParameters[i].testStatus==3))
					{
					    byte bt[] = new byte[2];
					    bt[0] = MidData.m_statusAisle[lenSt++];
					    bt[1] = MidData.m_statusAisle[lenSt++];
					    int time = Integer.parseInt(new String(bt), 16);
					    if (time != 0)
					    {
						if (time != MidData.mTestParameters[i].aisleTestTime)
						{
						    MidData.mTestParameters[i].aisleTestTime = time;
						    //System.out.println(time+"=MidData.mTestParameters[i].aisleTestTime---"+i);
						    MidData.mTestParameters[i].aisleDataSaved = false;
						}
					    }
					}
					else if (MidData.mTestParameters[i].aisleStatus == 1
						&& MidData.mTestParameters[i].testStatus==0)
					{
					    // /COM By K lenSt = lenSt + 2;

					    // /Add By K 2014年8月5日14:14:08
					    byte bt[] = new byte[2];
					    bt[0] = MidData.m_statusAisle[lenSt++];
					    bt[1] = MidData.m_statusAisle[lenSt++];
					    int time = Integer.parseInt(new String(bt), 16);
					    if (time != MidData.mTestParameters[i].aisleTestTime)
					    {
						MidData.mTestParameters[i].aisleTestTime = time;
						 MidData.mTestParameters[i].aisleDataSaved = false;
					    }
					    // /Add By K 2014年8月5日14:14:08 -END

					}
					else
					{
					    lenSt = lenSt + 2;
					}
					//System.out.println(MidData.mTestParameters[i].aisleTestTime+"----------TestTime-----"+i);
				    }
				    String temStr1="";
				    for (int ti = 0; ti < 5; ti++)
				    {
					byte sign = MidData.m_statusAisle[lenSt++];
					byte[] tem = new byte[3];
					tem[0] = MidData.m_statusAisle[lenSt++];
					tem[1] = MidData.m_statusAisle[lenSt++];
					tem[2] = MidData.m_statusAisle[lenSt++];
					String temStr = new String(tem);
					//
					//
					//
					MidData.Temperature[ti] = sign == '0' ? ((Double
						.parseDouble(Integer.toString(Integer.parseInt(
							temStr, 16))) / 10.0) + MidData.mSetParameters.TemperatureRevise)
						: -((Double
							.parseDouble(Integer.toString(Integer
								.parseInt(temStr, 16))) / 10.0) + MidData.mSetParameters.TemperatureRevise);
				    
					temStr1+=MidData.Temperature[ti]+",";
				    }
				    //byte[] tttt=Arrays.copyOfRange(MidData.m_statusAisle, 0, lenSt);
				   
				    MidData.TempRes=temStr1+";"+new String(Arrays.copyOfRange(MidData.m_statusAisle, lenSt-20, lenSt));
				    //System.out.println(temStr1+"----Temperature----");
				    MidData.TemperatureErrorShow = "";
				    double averageTem=CalculateFormula.AnverageTem(MidData.Temperature);
				    for (int tt = 0; tt < 5; tt++)
				    {
					if ((MidData.Temperature[tt] - averageTem) > 2)
					{
					    MidData.TemperatureErrorShow += "T"
						    + Integer.toString(tt) + "1";
					}
					else
					{
					    if ((MidData.Temperature[tt] - averageTem) < -2)
					    {
						MidData.TemperatureErrorShow += "T"
							+ Integer.toString(tt) + "0";
					    }
					}
				    }
				    MidData.m_statusAisle[0] = '0';
			}
			else{
				MidData.m_statusAisle[0] = '0';
			}
		}
		try
		{
		    sleep(100);
		}
		catch (InterruptedException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
//	    	try
//			{
//			    sleep(1);
//			}
//			catch (InterruptedException e)
//			{
//			    // TODO Auto-generated catch block
//			    e.printStackTrace();
//			}
	    }
	}
    }
    public String GetTimeStr(){
    	Time t=new Time();
    	t.setToNow();
    	
    	String y = Integer.toString(t.year);
    	String m = t.month < 9 ? "0" + Integer.toString(t.month + 1) : Integer
    		.toString(t.month + 1);
    	String d = t.monthDay < 10 ? "0" + Integer.toString(t.monthDay)
    		: Integer.toString(t.monthDay);
    	String h = t.hour < 10 ? "0" + Integer.toString(t.hour) : Integer
    		.toString(t.hour);
    	String min = t.minute < 10 ? "0" + Integer.toString(t.minute) : Integer
    		.toString(t.minute);
    	String sec = t.second < 10 ? "0" + Integer.toString(t.second) : Integer
    		.toString(t.second);
    	return y + m + d + h + min + sec;
    }
    /*public static double getSecondBetweenTwoTime(String strs, String stre)
    {
	double dtime = Double.parseDouble(strs);
	int years = (int) (dtime / (Math.pow(10, 10)));
	int months = (int) ((dtime - years * (Math.pow(10, 10))) / (Math.pow(
		10, 8)));
	int dates = (int) ((dtime - years * (Math.pow(10, 10)) - months
		* (Math.pow(10, 8))) / (Math.pow(10, 6)));
	int hours = (int) ((dtime - years * (Math.pow(10, 10)) - months
		* (Math.pow(10, 8)) - dates * (Math.pow(10, 6))) / (Math.pow(
		10, 4)));
	int mins = (int) ((int) (dtime - years * (Math.pow(10, 10)) - months
		* (Math.pow(10, 8)) - dates * (Math.pow(10, 6)) - hours
		* (Math.pow(10, 4)))/Math.pow(10, 2));
	int seconds = (int) (dtime - years * (Math.pow(10, 10)) - months
		* (Math.pow(10, 8)) - dates * (Math.pow(10, 6)) - hours
		* (Math.pow(10, 4)) - mins * Math.pow(10, 2));

	// int milseconds=(int) (dtime-years*(Math.pow(10,
	// 12))-months*(Math.pow(10, 10))-dates*(Math.pow(10,
	// 8))-hours*(Math.pow(10, 6))-mins*Math.pow(10, 4)-seconds*Math.pow(10,
	// 2));
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date startDate = null;
	try
	{
	    startDate = format.parse(Integer.toString(years) + "-"
		    + Integer.toString(months) + "-" + Integer.toString(dates));
	}
	catch (ParseException e1)
	{
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	Calendar fromCalendar = Calendar.getInstance();
	fromCalendar.setTime(startDate);
	fromCalendar.set(Calendar.HOUR_OF_DAY, hours);
	fromCalendar.set(Calendar.MINUTE, mins);
	fromCalendar.set(Calendar.SECOND, seconds);
	fromCalendar.set(Calendar.MILLISECOND, 0);
	double dtimee = Double.parseDouble(stre);
	int yeare = (int) (dtimee / (Math.pow(10, 10)));
	int monthe = (int) ((dtimee - yeare * (Math.pow(10, 10))) / (Math.pow(
		10, 8)));
	int datee = (int) ((dtimee - yeare * (Math.pow(10, 10)) - monthe
		* (Math.pow(10, 8))) / (Math.pow(10, 6)));
	int houre = (int) ((dtimee - yeare * (Math.pow(10, 10)) - monthe
		* (Math.pow(10, 8)) - datee * (Math.pow(10, 6))) / (Math.pow(
		10, 4)));
	int mine = (int) ((int) (dtimee - yeare * (Math.pow(10, 10)) - monthe
		* (Math.pow(10, 8)) - datee * (Math.pow(10, 6)) - houre
		* (Math.pow(10, 4)))/(Math.pow(10, 2)));
	int seconde = (int) (dtimee - yeare * (Math.pow(10, 10)) - monthe
		* (Math.pow(10, 8)) - datee * (Math.pow(10, 6)) - houre
		* (Math.pow(10, 4)) - mine * Math.pow(10, 2));
	// int milseconde=(int) (dtimee-yeare*(Math.pow(10,
	// 12))-monthe*(Math.pow(10, 10))-datee*(Math.pow(10,
	// 8))-houre*(Math.pow(10, 6))-mine*Math.pow(10, 4)-seconde*Math.pow(10,
	// 2));

	// System.out.println(Integer.toString(yeare)+Integer.toString(monthe)+Integer.toString(datee)+Integer.toString(houre)+Integer.toString(mine)+"-----------TimeNow");
	java.util.Date endDate = null;
	try
	{
	    endDate = format.parse(Integer.toString(yeare) + "-"
		    + Integer.toString(monthe) + "-" + Integer.toString(datee));
	}
	catch (ParseException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	Calendar toCalendar = Calendar.getInstance();
	toCalendar.setTime(endDate);
	toCalendar.set(Calendar.HOUR_OF_DAY, houre);
	toCalendar.set(Calendar.MINUTE, mine);
	toCalendar.set(Calendar.SECOND, seconde);
	// toCalendar.set(Calendar.MILLISECOND, milseconde);
	double ts = toCalendar.getTime().getTime()
		- fromCalendar.getTime().getTime();
	ts /= (1000);
	//System.out.println(ts+"--------ts");
	return ts;
    }*/
    public long GetTimeNow(){
		 Calendar cal=Calendar.getInstance();
	    	/*Time t=new Time();
	    	t.setToNow();*/
	    	/*String y = Integer.toString(t.year);
	    	String m = t.month < 9 ? "0" + Integer.toString(t.month + 1) : Integer
	    		.toString(t.month + 1);
	    	String d = t.monthDay < 10 ? "0" + Integer.toString(t.monthDay)
	    		: Integer.toString(t.monthDay);
	    	String h = t.hour < 10 ? "0" + Integer.toString(t.hour) : Integer
	    		.toString(t.hour);
	    	String min = t.minute < 10 ? "0" + Integer.toString(t.minute) : Integer
	    		.toString(t.minute);
	    	String sec = t.second < 10 ? "0" + Integer.toString(t.second) : Integer
	    		.toString(t.second);*/
	    	return cal.getTimeInMillis();
	    }
    public void SetFanOpenClose(){
    	byte or='A';
    	byte[] buf=new byte[2];
    	buf[0]='W';
    	if(MidData.mSetParameters.FanOpenClose==1)
    	{
    		buf[1]=0x30;
    	}
    	else
    	{
    		buf[1]=0x31;
    	}
    	SendData(or, buf, 2, MidData.m_OutputStream_LowerMachine);
    }
    public void DeleteHistoryBefore30Days(){
    	
    	Calendar calendar=Calendar.getInstance();
    	calendar.add(Calendar.DATE, -30);
    	int year=calendar.get(calendar.YEAR);
    	int month=calendar.get(calendar.MONTH);
    	int day=calendar.get(calendar.DAY_OF_MONTH);
    	String date=year+(month+1>=10?Integer.toString(month+1):"0"+Integer.toString(month+1))+(day>=10?Integer.toString(day):"0"+Integer.toString(day));
    	System.out.println(date+"---------deleteDate------");
    	MidData.sqlOp.deleteSourceByDate(date);
    }
    public class LoadSelfTestStatusThread extends Thread
    {

	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    try
	    {
		sleep(1000);
	    }
	    catch (InterruptedException e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	    while (MidData.m_isInterrupted_LowerMachine && !MidData.TestFinished)
	    {
		try
		{
		    sleep(300);
		}
		catch (InterruptedException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		if (MidData.m_statusSelfTestAisle[0] == '>'
			&& MidData.m_statusSelfTestAisle[1] == 'A'
			&& MidData.m_statusSelfTestAisle[2] == 'F')
		{
		  
		    ReturnOrder('F');
		    if (MidData.m_statusSelfTestAisle[3] == '0')
		    {
			//
			MidData.StatusStrShow = MidData.IsChinese==1?"正在自检":"Self-testing";
		    }
		    else if (MidData.m_statusSelfTestAisle[3] == '1')
		    {
		    	
			MidData.StatusStrShow = MidData.IsChinese==1?"自检完成":"Self-test completed";
			//ICCardTest icTest=new ICCardTest();
            //icTest.StartTestICCard();
			int len = 4;
			int lenSt = 0;

			for (int i = 0; i < 50; i++)
			{
			    byte bt[] = new byte[1];
			    bt[0] = MidData.m_statusSelfTestAisle[len++];
			    String str = new String(bt);
			    int btV = Integer.parseInt(new String(bt), 16);
			    for (int j = 0; j < 2; j++)
			    {
				int add = lenSt;

				int bV = (btV & bitd[j]) >> (2 - j * 2);
				if (bV == 1)
				{
				    MidData.mTestParameters[add].aisleStatus = 1;
				}
				else
				{
				    MidData.mTestParameters[add].aisleStatus = -1;
				}
				lenSt++;
			    }
			}
			byte[] heightB = new byte[8];
			for (int t = 0; t < 8; t++)
			{
			    heightB[t] = MidData.m_statusSelfTestAisle[len++];
			}
			MidData.MaxRPMOfDrivce = Integer.parseInt(new String(
				heightB), 16);
			for (int ti = 0; ti < 5; ti++)
			{
			    byte sign = MidData.m_statusSelfTestAisle[len++];
			    byte[] tem = new byte[3];
			    tem[0] = MidData.m_statusSelfTestAisle[len++];
			    tem[1] = MidData.m_statusSelfTestAisle[len++];
			    tem[2] = MidData.m_statusSelfTestAisle[len++];
			    String temStr = new String(tem);

			    MidData.Temperature[ti] = sign == '0' ? ((Double
				    .parseDouble(Integer.toString(Integer
					    .parseInt(temStr, 16))) / 10.0) + MidData.mSetParameters.TemperatureRevise)
				    : -((Double.parseDouble(Integer
					    .toString(Integer.parseInt(temStr,
						    16))) / 10.0) + MidData.mSetParameters.TemperatureRevise);

			}
			double averageTem=CalculateFormula.AnverageTem(MidData.Temperature);
			MidData.startTemTestFinish=false;
			if(!MidData.startTemTestFinish)
			{
				
						
				if(averageTem>30||averageTem<10)
				{
					MidData.startTemTestOK=false;
				}
				else
				{
					MidData.startTemTestOK=true;
				}
			}
			
			MidData.TemperatureErrorShow = "";
			MidData.startTemTestFinish1=false;
			MidData.startTemTestOK1=true;
			//averageTem=0;
			for (int tt = 0; tt < 5; tt++)
			{
			    if ((MidData.Temperature[tt] - averageTem) > 2)
			    {
				MidData.startTemTestOK1=false;
				MidData.TemperatureErrorShow += "T"
					+ Integer.toString(tt) + "1";
			    }
			    else if ((MidData.Temperature[tt] - averageTem) < -2)
				{
				    MidData.TemperatureErrorShow += "T"
					    + Integer.toString(tt) + "0";
				    MidData.startTemTestOK1=false;
				} 
			    
			}
			
			//MidData.isReviseRunSpeed = true;
			MidData.TestFinished = true;
			// PWM mPWMThread = new PWM();
			// mPWMThread.start();
			MakeBuzzerBeep();
			DeleteHistoryBefore30Days();
			// mSendForPassageStatusAndHeightThread.start();
			OperateHeightDataAndSaveThread mOperateHeightDataAndSaveThread = new OperateHeightDataAndSaveThread();
			UpdatePassageStatusThread mOperationAndUpdateStatusThread = new UpdatePassageStatusThread();
			mOperationAndUpdateStatusThread.start();
			mOperateHeightDataAndSaveThread.start();
			PrintThread PT=new PrintThread();
			PT.start();
			
			SetFanOpenClose();
		    }

		    MidData.m_statusSelfTestAisle[2] = 'P';
		}
		if (MidData.m_systemError[0] == '>' && MidData.m_systemError[1] == 'A'
			&& MidData.m_systemError[2] == 'R')
		{
		    ReturnOrder('R');
		    byte[] error = new byte[1];
		    //
		    error[0] = MidData.m_systemError[3];
		    String bs = new String(error);
		    int intdd = Integer.parseInt(bs, 16);
		    int byteofbitlength = CalculateFormula.GetByteOfBitLength(error[0]);
		    //
		    MidData.ErrorStrShow = "";
		    for (int j = 0; j < byteofbitlength; j++)
		    {
			int bitdata = (intdd & dd[j]) >> j;
			if (bitdata == 1)
			{
			    //
			    if (MidData.ErrorStrShow.equals(""))
			    {
				//
				//
				MidData.ErrorStrShow = "Error："
					+ MidData.ErrorInfor[j];
			    }
			    else
				MidData.ErrorStrShow += ";"
					+ MidData.ErrorInfor[j];
			}
		    }
		    //
		    MidData.m_systemError[2] = 0;
		}
	    }

	}
    }
    
    boolean IsNeedStopTest;
    short StopChBuf[] = new short[13];

    void SetAChTestStop(int add)
    {
	// System.out.println("容量异常:"+String.valueOf(i));
	// int add=99-i;
	int i = ((99 - add) % 10) * 10 + (99 - add) / 10;
	if (!IsNeedStopTest)
	{
	    for (int j = 0; j < 13; j++)
		StopChBuf[j] = 0;
	}
	IsNeedStopTest = true;
	StopChBuf[i / 8] = (short) (StopChBuf[i / 8] | (0x1 << (i % 8)));
	// System.out.println("Stop"+String.valueOf(i));
    }

    void SendStopMsg2Device()
    {
	// System.out.println("SendStopMsg2Device");
	if (IsNeedStopTest)
	{

	    Byte CMD = 'A';
	    byte[] Buf = new byte[26];
	    Buf[0] = 'Y';
	    for (int i = 0; i < 12; i++)
	    {
		Buf[1 + i * 2] = (byte) Hex2Ascii(StopChBuf[i] & 0xF);
		Buf[2 + i * 2] = (byte) Hex2Ascii((StopChBuf[i] & 0xF0) >> 4);
	    }
	    Buf[25] = (byte) Hex2Ascii(StopChBuf[12] & 0xF);

	    //System.out.println(new String(Buf) + "Stop__Order");
	    if (SendDataToSerial(CMD, Buf, 26))
	    {
		IsNeedStopTest = false;
	    }
	    Buf=null;
	}
    }
    long myyyy=0;
    void SendStopMsg2DeviceTestCmd()
    {
    	Byte CMD = 'A';
	    byte[] Buf = new byte[26];
	    Buf[0] = 'Y';
	    for (int i = 0; i < 12; i++)
	    {
		Buf[1 + i * 2] = (byte) Hex2Ascii(0xF& 0xF);
		Buf[2 + i * 2] = (byte) Hex2Ascii((0xF0 & 0xF0) >> 4);
	    }
	    Buf[25] = (byte) Hex2Ascii(StopChBuf[12] & 0xF);

	    //Log.v("MMM", "SendStop__Order");
	    if (SendDataToSerial(CMD, Buf, 26))
	    {
		//IsNeedStopTest = false;
	    }
	    Buf=null;
	    myyyy++;
	    if(myyyy%1000==0)
	    {
	    System.out.println("SendStop__Order"+Long.toString(myyyy));
	    }
    }
   private void AutoUpLoadToLIS(String Num,String XC,String YJ,String Temp)
    {
    	//System.out.println(Num+"---AutoUpLoadToLIS");
    	new AutoUpLoadToLIS(lockLis,Num,XC,YJ,Temp).start();
    }
    private void AutoUpLoadToSA(Queue<String> SAUpLoadQueue)
    {
    	//System.out.println(Num+"---AutoUpLoadToLIS");
    	new AutoUpLoadToSA(lockSA,SAUpLoadQueue).start();
    }
    
    private void AutoPrintfData(String Num, String ESR, String YJ,
    	    String xcType, String plusPeriod, String resourcevalues,double temp)
    {
    	PrintDataStructClass pDsc= new PrintDataStructClass();
    	pDsc.SetNum(Num);
    	pDsc.SetESR(ESR);
    	pDsc.SetYJ(YJ);
    	pDsc.SetxcType(xcType);
    	pDsc.SetplusPeriod(plusPeriod);
    	pDsc.Setresourcevalues(resourcevalues);
    	pDsc.Settemp(temp);
    	PrintQueue.add(pDsc);
    	//System.out.println(temp+"---AutoPrintfData");
	/*new AutoPrintfDataByk( Num, ESR, YJ, xcType, plusPeriod,
		resourcevalues,temp).start();*/
    }
    private class PrintThread extends Thread
    {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (MidData.m_isInterrupted_LowerMachine)
			{
				if(!PrintQueue.isEmpty())
				{
					PrintDataStructClass pDsc= new PrintDataStructClass();
					pDsc=PrintQueue.poll();
					new AutoPrintfDataByk(pDsc.GetNum(),pDsc.GetESR(), pDsc.GetYJ(), pDsc.GetxcType(), pDsc.GetplusPeriod(),
							pDsc.Getresourcevalues(),pDsc.Gettemp()).start();
				}
				if (MidData.mSetParameters.PrintSet==1)
					try {
						sleep(15000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else
					try {
						sleep(7000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}			
		}    	
    }
    @Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
	}
	private String GetAddress(int arg)
    {
	int row = arg % 10;// column
	int col = arg / 10;// row
	String colS = null;
	switch (row)
	{
	case 0:
	    colS = "A";
	    break;
	case 1:
	    colS = "B";
	    break;
	case 2:
	    colS = "C";
	    break;
	case 3:
	    colS = "D";
	    break;
	case 4:
	    colS = "E";
	    break;
	case 5:
	    colS = "F";
	    break;
	case 6:
	    colS = "G";
	    break;
	case 7:
	    colS = "H";
	    break;
	case 8:
	    colS = "I";
	    break;
	case 9:
	    colS = "J";
	    break;
	default:
	    break;
	}
	return colS + Integer.toString(col);
    }
    /*public void OpenSerialPortForUpLoadDataToLIS()
    {
	MidData.fd = KyleSocketClass.InitPort(MidData.IpStr, MidData.Port);// MidData.sOp.OpenSerialPort();
	
    }*/
   /* public class UpData2PcByF_k extends Thread
    {
    	public String id;
    	
    	void UpData2PcByF_kSetID(String idw){
    		
    	}
    		
    	

	@Override
	public void run()
	{
	    super.run();
		
		 Cursor cursor=null;
		    //new UpData2PcInSD100Byk("lock", i).start();
		 cursor = MidData.sqlOp.selectResourceDataByID(cursor,Integer
				.toString(selID[i]));
		 UpLaodData2PC(cursor);
		 Looper.prepare();
		 KyleSocketClass.DeInitPort(MidData.fd);
		 MidData.fd=-1;
		 //Toast.makeText(getApplicationContext(), MidData.IsChinese?"发送数据到LIS成功":"Send data to LIS successfully", Toast.LENGTH_LONG).show();
		 //mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
		 Looper.loop();
		 //button_UpLoad.setEnabled(true);
		 //MidData.sOpForSA.SendToSAPortClose();


	}

    }*/
   /* private void AutoUpLoadToLis()
    {
    	if(MidData.m_UpToLIS)
	    {
		OpenSerialPortForUpLoadDataToLIS();
		if (MidData.fd >0)
		{
		    new UpData2PcByk().start();
		}
		else
		{
			//Toast.makeText(getApplicationContext(), MidData.IsChinese?"连接LIS失败，请确认网络连接":"Connect LIS failed, please check network connection", Toast.LENGTH_LONG).show();
			//button_UpLoad.setEnabled(true);
			return;
		}
		
    }*/
    
    private class OperateHeightDataAndSaveThread extends Thread
    {
	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    try
	    {
		sleep(100);
	    }
	    catch (InterruptedException e1)
	    {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	    //System.out.println("8888888888888888888");
	    
	    while (MidData.m_isInterrupted_LowerMachine&&MidData.TestFinished)
	    {
	    	
		try
		{
		    sleep(100);
		}
		catch (InterruptedException e)
		{
		    e.printStackTrace();
		}

		if (MidData.m_heightAisle[0] == '>'
			&& MidData.m_heightAisle[1] == 'A'
			&& MidData.m_heightAisle[2] == 'H')
		{
		  /*System.out.println(new String(
			    MidData.m_heightAisle)
			    + "-------AH------Time----start");
			*/
			MidData.m_heightAisle[2]=0;
		    int len = 3;
		    IsNeedStopTest = false;
		    String kkkk="";
		   // SAUpLoadQueue.clear();
		    SAUpLoadQueue.clear();
		    for (int v = 0; v < 100; v++)
		    {
			if (MidData.IsESRSelect[v])// 测试类型，血沉测试；压积测试
			{// 是否表示血沉测试
			    if (MidData.mTestParameters[v].aisleStatus == 1)// 如果自检该通道正常
			    {
		    	if(MidData.mTestParameters[v].testStatus == 6 && !MidData.mTestParameters[v].warningNoConsume)
				{
		    		MidData.mTestParameters[v].warningNoConsume=true;
					SetAChTestStop(v);
				    MakeBuzzerBeep();	
					continue;		
				}
				if (MidData.isDebugging!=2) // 如果 不是拷机
				{
					
				    if (!MidData.mTestParameters[v].aisleDataSaved)
				    {
					MidData.mTestParameters[v].aisleDataSaved = true;
					byte[] heightByte = new byte[4];
					for (int n = 0; n < 4; n++)
					{
					    heightByte[n] = MidData.m_heightAisle[len++];
					}
					float height = CalculateFormula.GetHeightByByte(
						heightByte,
						MidData.mTestParameters[v].aisleRecompenseHeight);
					if (MidData.mTestParameters[v].testStatus==2) // 正在检测
					{
					    if (MidData.mTestParameters[v].aisleTestTime == 1)
					    {
					    	
							MidData.mTestParameters[v].aisleHeght[MidData.mTestParameters[v].aisleTestTime-1] = height;
						    
							MidData.mTestParameters[v].aislePastTestData = "1"
								+ ","
								+ Float.toString(height)+";";
							MidData.mTestParameters[v].areaTemp=MidData.TempRes+"//";
							if (MidData.mSetParameters.SetNumWay==0&&(MidData.mTestParameters[v].aisleNum == null||MidData.mTestParameters[v].aisleNum.equals("")))
							{	
										
								if(Long.toString(MidData.mSetParameters.lastTargetNumERS).length()>MidData.fnum10LenESR)
								{
									MidData.fnum10LenESR=Long.toString(MidData.mSetParameters.lastTargetNumERS).length();
									String fmStr="##";
									for(int p=0;p<MidData.fnum10LenESR;p++)
										fmStr+="0";
									MidData.fnum10ESR=new DecimalFormat(fmStr);
								}
								//t.setToNow();
								long ailseNum=MidData.mSetParameters.lastTargetNumERS++;
								String ailseNumStr=MidData.fnum10ESR.format(ailseNum);
							    MidData.mTestParameters[v].aisleNum = ailseNumStr;
							    MidData.mTestParameters[v].aisleNumShow = ailseNumStr;
							    //System.out.println(MidData.mTestParameters[v].aisleNum+"-----"+v);
							    //kkkk += MidData.mTestParameters[i].aisleNum+"("+i+")"+"----";
							    
							}
							if (height > MidData.MaxHeight)//标本容量超限
							{
								MidData.mTestParameters[v].testStatus = 4;
							    SetAChTestStop(v);
							    MakeBuzzerBeep();
							    MidData.mTestParameters[v].aisleTestFinished=true;
							    //continue;
							}
							if (height < MidData.MinHeight)//标本容量不足
							{
								MidData.mTestParameters[v].testStatus = 4;
							    SetAChTestStop(v);
							    MakeBuzzerBeep();
							    MidData.mTestParameters[v].aisleTestFinished=true;
							    //continue;
							}
							
						//System.out.println(MidData.mTestParameters[i].aisleNum+"--------PNum--"+i);
					    }
					    else
					    {
					    	MidData.mTestParameters[v].aisleHeght[MidData.mTestParameters[v].aisleTestTime-1] = height;
					    	if(height<=0||height>(MidData.MaxHeight+1))
					    	{
					    		height=(float) MidData.mTestParameters[v].aisleHeght[MidData.mTestParameters[v].aisleTestTime-2];
					    		MidData.mTestParameters[v].aisleHeght[MidData.mTestParameters[v].aisleTestTime-1] = height;
					    	}
					    	else if(height<11)
					    	{
					    		//SetAChTestStop(v);
							    //MakeBuzzerBeep();							    
					    	}
					    	MidData.mTestParameters[v].aislePastTestData += Integer
									.toString(MidData.mTestParameters[v].aisleTestTime)
								+ ","
								+ Float.toString(height)+";";
					    	MidData.mTestParameters[v].areaTemp+=MidData.TempRes+"//";
					    	//System.out.println(MidData.mTestParameters[v].areaTemp+"----TTTTTTTTTTTttPPPPPPPP");
						    
					    }
					   // System.out.println(MidData.mTestParameters[i].aislePastTestData+"-----"+MidData.mTestParameters[i].aisleNum+"------RESOU------"+i);
					    
					    //System.out.println(MidData.mTestParameters[i].aislePastTestData+
						//	     "--------MidData.mTestParameters[i].aislePastTestData---"+
						//	     Integer.toString(i));
					} // 正在检测 -END
					else if (MidData.mTestParameters[v].testStatus==3)// 检测完成
					{
					    if (MidData.mTestParameters[v].aisleTestFinished)// 如果已经保存//
									       // 这种情况是测试完成//
									       // 标本没有移除
					    {
						continue;
					    }
					    
					    double dataXC = 0;
					    if (MidData.mTestParameters[v].aislePastTestData
						    .equals("")
						    || MidData.mTestParameters[v].aislePastTestData == null||(MidData.mTestParameters[v].aisleNum.equals("")&&MidData.mSetParameters.SetNumWay==0))
					    {
						continue;
					    }
					   /* if(MidData.mTestParameters[v].aisleTestTime<MidData.mTestParameters[v].aisleTestTimeCount)
					    {
					    	MidData.mTestParameters[v].showEndOfNotSumTime=true;
					    }*/
					    if(height<=0||height>(MidData.MaxHeight+1))
				    	{
				    		height=(float) MidData.mTestParameters[v].aisleHeght[MidData.mTestParameters[v].aisleTestTime-2];
				    		MidData.mTestParameters[v].aisleHeght[MidData.mTestParameters[v].aisleTestTime-1] = height;
				    	}
					    
					    
					    MidData.mTestParameters[v].aislePastTestData +=Integer
							    .toString(MidData.mTestParameters[v].aisleTestTime)
						    + ","
						    + Float.toString(height);
					    
					    //MidData.mTestParameters[v].aislePastTestData="1,56.145836;2,55.987503;3,55.845833;4,55.795837;5,55.645836;6,55.67917;7,55.512497;8,55.345833;9,55.354164;10,55.1875;11,55.020836;12,54.854164;13,54.57083;14,54.1875;15,53.904167;16,53.8375;17,53.470833;18,53.2875;19,53.20417;20,52.92917;21,52.745834;22,52.479164;23,52.295837;24,52.104164;25,51.82917;26,51.5375;27,51.3375;28,51.237503;29,50.94583;30,50.737503;31,50.45417;32,50.345833;33,50.154167;34,49.745834;35,49.545837;36,49.45417;37,49.07083;38,48.870834;39,48.6625;40,48.470833;41,48.2875;42,47.987503;43,47.795837;44,47.495834;45,47.30417;46,47.095833;47,46.895836;48,46.604164;49,46.420837;50,46.2125;51,46.012497;52,45.70417;53,45.504166;54,45.295837;55,45.0875;56,44.80417;57,44.69583;58,44.4125;59,44.20417;60,43.920837;61,43.7125;62,43.529167;63,43.32083;64,43.020836;65,42.8125;66,42.604164;67,42.395836;68,42.095833;69,41.870834;70,41.6625;71,41.45417;72,41.254166;73,40.92917;74,40.8125;75,40.612503;76,40.30417;77,40.104168;78,39.895836;79,39.6875;80,39.487503;81,39.2625;82,38.954166;83,38.829166;84,38.620834;85,38.379166;86,38.1625;87,37.920834;88,37.720833;89,37.495834;90,37.19583;91,37.079166;92,36.754166;93,36.645836;94,36.345833;95,36.120834;96,35.9125;97,35.6875;98,35.479168;99,35.229168;100,35.104168;101,34.862503;102,34.629166;103,34.370834;104,34.245834;105,33.9375;106,33.7125;107,33.57083;108,33.354168;109,33.129166;110,32.8875;111,32.6625;112,32.5375;113,32.295834;114,32.0375;115,31.895834;116,31.645834;117,31.404167;118,31.2625;119,31.020834;120,30.804167;";
					    MidData.mTestParameters[v].averageTem=CalculateFormula.AnverageTem(MidData.Temperature);
					    
					    //MidData.mTestParameters[v].aislePastTestData="1,56.3125;2,56.3125;3,56.0375;4,55.920837;5,55.845833;6,55.729164;7,55.620834;8,55.520836;9,55.3375;10,55.229164;11,54.92917;12,54.545837;13,54.220833;14,53.9125;15,53.612503;16,53.387497;17,53.145836;18,52.904167;19,52.595833;20,52.404167;21,52.095833;22,51.887497;23,51.595833;24,51.404167;25,50.995834;26,50.770836;27,50.545837;28,50.220833;29,50.004166;30,49.762497;31,49.4375;32,49.229164;33,48.920837;34,48.7125;35,48.395836;36,48.17917;37,47.845833;38,47.637497;39,47.295837;40,47.07917;41,46.845833;42,46.612503;43,46.395836;44,46.104164;45,45.779167;46,45.545837;47,45.3375;48,45.029167;49,44.720833;50,44.512497;51,44.295837;52,44.004166;53,43.670837;54,43.470833;55,43.237503;56,42.979164;57,42.745834;58,42.479164;59,42.170837;60,41.979164;";
					    MidData.mTestParameters[v].areaTemp+=MidData.TempRes+"//";
					    
					    String dataXCStr=CalculateFormula.GetERSTestResult(MidData.mTestParameters[v].aislePastTestData, "0", MidData.isDebugging, Integer.toString(MidData.mSetParameters.ERSTestTime), MidData.mTestParameters[v].averageTem);
					    //String dataXCStr =CalculateFormula.ResourceResultToShow(Double.toString(dataXC), "0", MidData.isDebugging, Integer.toString(MidData.mSetParameters.ERSTestTime), MidData.mTestParameters[v].averageTem); //fnum1
						MidData.mTestParameters[v].ERSResult =dataXCStr;
					  
					    String debug = "0";
					    if (MidData.isDebugging==1)
					    {
						debug = "1";
					    }

					    MakeBuzzerBeep();
					    	    
					    
					    //dataXCStr = fnum1.format(dataXCCopy);//存原始值 用时根据拟合的选项再计算出来
					    MidData.mTestParameters[v].ERSResultRes=dataXCStr;//存原始值 用时根据拟合的选项再计算出来后扫码存入数据库
					    if(MidData.mSetParameters.SetNumWay==0)
					    {
					    	
					    	// / 自动打印
						    if (MidData.m_PrintAfterERS&&!MidData.m_PrintAfterHemat)
						    {
							
							    AutoPrintfData(
								    MidData.mTestParameters[v].aisleNum,
								    MidData.mTestParameters[v].ERSResult,//MidData.mTestParameters[v].ERSResult
								    "未检测",
								    Integer.toString(MidData.mSetParameters.ERSTestTime),
								    Integer.toString(MidData.mSetParameters.RunPeriodSet),
								    MidData.mTestParameters[v].aislePastTestData,MidData.mTestParameters[v].averageTem);
							
						    }//&&!MidData.sqlOp.XueChenTestFinished(MidData.mTestParameters[v].aisleNum)&&!MidData.sqlOp.YaJiTestFinished(MidData.mTestParameters[v].aisleNum)
					    	//if (MidData.mTestParameters[v].IsNewSample)
						    //{
						        boolean finishERS=MidData.sqlOp.XueChenTestFinished(MidData.mTestParameters[v].aisleNum);
						        boolean finishHemat=MidData.sqlOp.YaJiTestFinished(MidData.mTestParameters[v].aisleNum);
						        if(!finishERS&&MidData.IsConsumerReduce&&MidData.isDebugging==0)
						        {
						        	MidData.mSetParameters.ERSCount--;
						        	MidData.sqlOp
								      .UpdateData(1,
									    "ConsumableInforTable",
									    new String[]{"consumxuechencount"},
									    new String[]{Long.toString(MidData.mSetParameters.ERSCount)},1);
						        }
					    		if(!finishERS&&!finishHemat)
					    		{
					    			MidData.sqlOp
								.InsertData(Integer.toString(v),
									MidData.mTestParameters[v].aisleNum,
									GetAddress(v),
									"",
									MidData.mTestParameters[v].aislePastTestData,
									dataXCStr,
									"未检测",
									Double.toString(CalculateFormula.AnverageTem(MidData.Temperature)),
									Integer.toString(MidData.mSetParameters.ERSTestTime),
									Integer.toString(MidData.mSetParameters.RunPeriodSet),
									debug, "1",
									"0", "",MidData.mTestParameters[v].areaTemp);//dataXCStr
					    		}
					    		else
					    		{
					    			  MidData.sqlOp
									    .UpdateXueChenByNo(MidData.mTestParameters[v].aisleNum,GetAddress(v),MidData.mTestParameters[v].aislePastTestData,
											    dataXCStr,Double.toString(CalculateFormula.AnverageTem(MidData.Temperature)),
											    Integer.toString(MidData.mSetParameters.ERSTestTime),
											    Integer.toString(MidData.mSetParameters.RunPeriodSet),
											    debug, "1",MidData.mTestParameters[v].areaTemp);					    			
					    		}
							  MidData.sqlOp
						      .UpdateData(1,
							    "SystemSetTable",
							    new String[]{"lasttargetnum","lasttargetnumyj"},
							    new String[]{Long.toString(MidData.mSetParameters.lastTargetNumERS),Long.toString(MidData.mSetParameters.lastTargetNumHemat)},2);
					  
						    
					    	if(MidData.m_UpToLIS_net||MidData.m_UpToLIS_ser)
					    	{
					    		AutoUpLoadToLIS(MidData.mTestParameters[v].aisleNum,MidData.mTestParameters[v].ERSResult,"未检测",fnum1.format(MidData.mTestParameters[v].averageTem));
					    	}
					    	else if(MidData.m_UpToSA)
					    	{
					    		SAUpLoadQueue.add(Integer.toString(v));
					    	}
					    	
					    }
					    MakeBuzzerBeep();
					    MidData.mTestParameters[v].aisleTestFinished = true;
					    MidData.mTestParameters[v].IsNewSample=true;
					    MidData.mTestParameters[v].ReDetect=false;
					    MidData.mTestParameters[v].RealReDetect=false;
					}// //检测完成 -END
				    }
				    else
				    {
					len = len + 4;
				    }
				} // 如果 不是拷机 -END
				else
				{
				    byte[] heightByte = new byte[4];
				    for (int n = 0; n < 4; n++)
				    {
					heightByte[n] = MidData.m_heightAisle[len++];
				    }
				    float height = CalculateFormula.GetHeightByByte(heightByte,
					    MidData.mTestParameters[v].aisleRecompenseHeight);
				    MidData.mTestParameters[v].height_RunAllTime = height;
				}
			    }// 如果自检该通道正常 -END
			    else
			    {
				len = len + 4;
			    }
			    // }
			} // 测试类型，0，血沉测试；1压积测试 -END
			else//压积测试
			{
				if(MidData.mTestParameters[v].testStatus == 6&& !MidData.mTestParameters[v].warningNoConsume)
				{
					MidData.mTestParameters[v].warningNoConsume=true;
					SetAChTestStop(v);
				    MakeBuzzerBeep();	
					continue;										
				}
				if (MidData.mTestParameters[v].aisleStatus == 1
				    && MidData.mTestParameters[v].testStatus==2)
			    {// 正在测试压积？？				
				
					
			    MidData.mTestParameters[v].hematExcetion=2;
				if (MidData.mSetParameters.SetNumWay==0&&(MidData.mTestParameters[v].aisleNum == null||MidData.mTestParameters[v].aisleNum.equals("")))
				{
				    
				    if(Long.toString(MidData.mSetParameters.lastTargetNumHemat).length()>MidData.fnum10LenHemat)
					{
						MidData.fnum10LenHemat=Long.toString(MidData.mSetParameters.lastTargetNumHemat).length();
						String fmStr="##";
						for(int p=0;p<MidData.fnum10LenHemat;p++)
							fmStr+="0";
						MidData.fnum10Hemat=new DecimalFormat(fmStr);
					}
				    long ailseNum= MidData.mSetParameters.lastTargetNumHemat++;
				    String ailseNumStr=MidData.fnum10Hemat.format(ailseNum);
				    MidData.mTestParameters[v].aisleNum = ailseNumStr;
				    MidData.mTestParameters[v].aisleNumShow = ailseNumStr;
				   
				}
				//System.out.println(MidData.mTestParameters[i].aisleTestTime+"--"+MidData.mTestParameters[i].aisleTestFinished+"---"+MidData.mTestParameters[i].aisleDataSaved+"----------压积检测---"+i);
				if (MidData.mTestParameters[v].aisleTestTime == 1
					&& !MidData.mTestParameters[v].aisleTestFinished&&!MidData.mTestParameters[v].aisleDataSaved)
				{
				    //MidData.mTestParameters[i].aisleTestFinished = true;
					MidData.mTestParameters[v].aisleDataSaved=true;
				    byte[] heightByte = new byte[4];
				    for (int n = 0; n < 4; n++)
				    {
					heightByte[n] = MidData.m_heightAisle[len++];
				    }
				    float height = GetHeightByByte(heightByte)
					    + MidData.mTestParameters[v].aisleRecompenseHeight;
				    MidData.mTestParameters[v].aisleHeght[0] = height;
				    MidData.mTestParameters[v].aislePastTestData = "1,"
					    + Float.toString(height);
				    MidData.mTestParameters[v].areaTemp=MidData.TempRes;
				    System.out.println(MidData.mTestParameters[v].aislePastTestData+"------"+v+"-----第一次");
				}
				else
				{
				    len = len + 4;
				}
			    }
			    else if (MidData.mTestParameters[v].aisleStatus == 1
				    && MidData.mTestParameters[v].testStatus==3)
			    {
				//System.out.println(MidData.mTestParameters[i].aisleTestFinished+"---"+MidData.mTestParameters[i].aisleDataSaved+"---454545"+i);
				if (!MidData.mTestParameters[v].aisleTestFinished)
				{
				    if (!MidData.mTestParameters[v].aisleDataSaved)
				    {
					MidData.mTestParameters[v].aisleDataSaved = true;
					byte[] heightByte = new byte[4];
					double dataYaJi;
					for (int n = 0; n < 4; n++)
					{
					    heightByte[n] = MidData.m_heightAisle[len++];
					}
					float height = GetHeightByByte(heightByte)
						+ MidData.mTestParameters[v].aisleRecompenseHeight;
					MidData.mTestParameters[v].aislePastTestData += ";"+ Integer.toString(MidData.mTestParameters[v].aisleTestTime)+ "," + Float.toString(height);
					MidData.mTestParameters[v].areaTemp+=MidData.TempRes+"//";
					System.out.println(MidData.mTestParameters[v].aislePastTestData+"-----MidData.mTestParameters[v].aislePastTestData------"+v+"---第二次");
					System.out.println(MidData.mTestParameters[v].aisleHeght[0]+"---"+height+"----"+v);
					if (Math.abs(height- MidData.mTestParameters[v].aisleHeght[0]) > 2)//1 2015-11-02// 大于0.5//// 需要重新测试
					{
						MidData.mTestParameters[v].hematExcetion=1;
					    MidData.IsNeedReTest = 1;
					    MidData.mTestParameters[v].aisleTestFinished = true;
					    MidData.IsNeedReTestMsg = MidData.IsNeedReTestMsg
						    + GetAddress(v) + ",";
					}
					else// 小于0.5 有效
					{
						MidData.mTestParameters[v].hematExcetion=2;
					    height = (float) ((height + MidData.mTestParameters[v].aisleHeght[0]) / 2);
					    MidData.mTestParameters[v].hematTestHeight=height;
					    //System.out.println(MidData.mTestParameters[i].hematTestHeight+"----kkkhhhhhh----"+i);
					    if(MidData.mSetParameters.SetNumWay==0)
						{
					    	//System.out.println(MidData.isSql+"---------sql1111---"+MidData.mTestParameters[i].aisleNum+"----"+i);
						    if(MidData.mTestParameters[v].aisleNum.equals(""))
						    {
						    	//continue;
						    }
					    	if(!MidData.sqlOp.XueChenTestFinished(MidData.mTestParameters[v].aisleNum))
						    {
						    	double yajid=(height * 100 / (57-MidData.mSetParameters.AnticoagulantsHeight))+MidData.mSetParameters.HematResultRevise;
							    if(yajid>100.0)
							    	yajid=100.0;
							    if(yajid<20.0)
							    	yajid=20.0;
								String dataYaJiStr = Double.toString(yajid);// 单位是
								MidData.mTestParameters[v].hematResult =CalculateFormula.YaJiToShow(dataYaJiStr);// fnum1.format(yajid);
								MakeBuzzerBeep();
								
									if (MidData.m_PrintAfterHemat&&!MidData.m_PrintAfterERS)//
									{						    
										AutoPrintfData(
											MidData.mTestParameters[v].aisleNum,
											"未检测",
											MidData.mTestParameters[v].hematResult,
											Integer.toString(MidData.mSetParameters.ERSTestTime),
											Integer.toString(MidData.mSetParameters.RunPeriodSet),
											"",MidData.mTestParameters[v].averageTem);									    
									}
							    	if(!MidData.sqlOp.YaJiTestFinished(MidData.mTestParameters[v].aisleNum))
							    	{
							    		if(MidData.IsConsumerReduce&&MidData.isDebugging==0)
							    		{
									     MidData.mSetParameters.hematCount--;
									        	MidData.sqlOp
											      .UpdateData(1,
												    "ConsumableInforTable",
												    new String[]{"consumyajicount"},
												    new String[]{Long.toString(MidData.mSetParameters.hematCount)},1);
							    		}  
										// 这意味着 虽然有标本号 但没有测试血沉
										// 此需要插入值 而不是更新
									   
											MidData.sqlOp
												.InsertData(Integer.toString(v),
													MidData.mTestParameters[v].aisleNum,
													"",
													GetAddress(v),
													"",
													"未检测",
													dataYaJiStr,
													Double.toString(CalculateFormula.AnverageTem(MidData.Temperature)),
													Integer.toString(MidData.mSetParameters.ERSTestTime),
													Integer.toString(MidData.mSetParameters.RunPeriodSet),
													"0",
													"0",
													"1",
													MidData.mTestParameters[v].aislePastTestData,MidData.mTestParameters[v].areaTemp);
											 MidData.sqlOp
											    .UpdateData(1,
												    "SystemSetTable",
												    new String[]{"lasttargetnum","lasttargetnumyj"},
												    new String[]{Long.toString(MidData.mSetParameters.lastTargetNumERS),Long.toString(MidData.mSetParameters.lastTargetNumHemat)},2);
										  
										MidData.mTestParameters[v].aisleTestFinished = true;
										
									    
							    	}
							    	else
							    	{
							    		
										boolean fh=MidData.sqlOp
											.UpdateYaJiByNo(
												MidData.mTestParameters[v].aisleNum,
												dataYaJiStr,
												MidData.mTestParameters[v].aislePastTestData,
												"1",
												GetAddress(v));
											MidData.sqlOp
									    .UpdateData(1,
										    "SystemSetTable",
										    new String[]{"lasttargetnum","lasttargetnumyj"},
										    new String[]{Long.toString(MidData.mSetParameters.lastTargetNumERS),Long.toString(MidData.mSetParameters.lastTargetNumHemat)},2);
								  
							    	}
							    	//System.out.println(MidData.m_UpToLISAuto+"----MidData.m_UpToLISAuto111111");
							    	if(MidData.m_UpToLIS_net||MidData.m_UpToLIS_ser)
							    	{
							    		AutoUpLoadToLIS(MidData.mTestParameters[v].aisleNum,"未检测",MidData.mTestParameters[v].hematResult,"0");
							    	}	
							    	else if(MidData.m_UpToSA)
							    	{
							    		SAUpLoadQueue.add(Integer.toString(v));
							    		//AutoUpLoadToSA(MidData.mTestParameters[v].aisleNum,v);
							    	}
						    }
						    else
						    {
						    	String rs2[] = MidData.sqlOp.selectSingleHistoryByNumByK(MidData.mTestParameters[v].aisleNum);
							    String rs = rs2[0];
							    //System.out.println(MidData.isSql+"---------sql2222----"+MidData.mTestParameters[i].aisleNum+"----"+i);
							    MidData.mTestParameters[v].averageTem=0.0;
						    	String[] rs_XueChen = rs
										.split(";");
									String[] rs_XueChen_First = rs_XueChen[0]
										.split(",");					
									if(rs_XueChen_First.length<2)
										dataYaJi = (height / (57-MidData.mSetParameters.AnticoagulantsHeight));
									else
									dataYaJi = (height)/ (Double.parseDouble(rs_XueChen_First[1])-MidData.mSetParameters.AnticoagulantsHeight);
						           /*if (dataYaJi > 1)
									    dataYaJi = 1;*/
									dataYaJi = dataYaJi * 100;
									dataYaJi = dataYaJi+MidData.mSetParameters.HematResultRevise;
									if (dataYaJi > 100)
									    dataYaJi = 100;
									else if(dataYaJi<20)
										dataYaJi=20;
									String XC = rs2[1];//cursor.getString(cursor.getColumnIndex("xuechendata"));
									//String XCRe=XC;
									String Temp=rs2[2];
									int xx=0;
									
									XC=CalculateFormula.GetERSTestResult(rs2[0], "0", MidData.isDebugging, Integer.toString(MidData.mSetParameters.ERSTestTime),  Double.parseDouble(rs2[2]));//.ResourceResultToShow(XC, "0", MidData.isDebugging, Integer.toString(MidData.mSetParameters.ERSTestTime),  Double.parseDouble(rs2[2]));
										if (XC==null||XC.equals("未检测")||XC.equals("Untest")||XC.equals(""))
									    {
											XC="";
									    }									    						    
									
									MidData.mTestParameters[v].hematResult = CalculateFormula.YaJiToShow(Double.toString(dataYaJi));//fnum1.format(dataYaJi)
										//.format(dataYaJi);

									MakeBuzzerBeep();
									if(MidData.mSetParameters.SetNumWay==0)
									{
										//  自动打印
										if (MidData.m_PrintAfterHemat&&MidData.m_PrintAfterERS)//
										{
										    
											AutoPrintfData(
												MidData.mTestParameters[v].aisleNum,
												XC,
												MidData.mTestParameters[v].hematResult,
												Integer.toString(MidData.mSetParameters.ERSTestTime),
												Integer.toString(MidData.mSetParameters.RunPeriodSet),
												rs,Double.parseDouble(rs2[2]));										    
										}
										boolean finishHemat=MidData.sqlOp.YaJiTestFinished(MidData.mTestParameters[v].aisleNum);
								        if(!finishHemat&&MidData.IsConsumerReduce&&MidData.isDebugging==0)
								        {
								        	MidData.mSetParameters.hematCount--;
								        	MidData.sqlOp
										      .UpdateData(1,
											    "ConsumableInforTable",
											    new String[]{"consumyajicount"},
											    new String[]{Long.toString(MidData.mSetParameters.hematCount)},1);
								        }
										
										MidData.sqlOp.UpdateYaJiByNo(
														MidData.mTestParameters[v].aisleNum,
														fnum1.format(dataYaJi),
														MidData.mTestParameters[v].aislePastTestData,
														"1",
														GetAddress(v));
										
										MidData.sqlOp
									    .UpdateData(1,
										    "SystemSetTable",
										    new String[]{"lasttargetnum","lasttargetnumyj"},
										    new String[]{Long.toString(MidData.mSetParameters.lastTargetNumERS),Long.toString(MidData.mSetParameters.lastTargetNumHemat)},2);
								  
									}
									//System.out.println(MidData.m_UpToLISAuto+"----MidData.m_UpToLISAuto22222");
									if(MidData.m_UpToLIS_net||MidData.m_UpToLIS_ser)
							    	{
							    		AutoUpLoadToLIS(MidData.mTestParameters[v].aisleNum,XC,MidData.mTestParameters[v].hematResult,Temp);
							    	}
									else if(MidData.m_UpToSA)
							    	{
										SAUpLoadQueue.add(Integer.toString(v));
							    	}
						    }   
						    
						}
					     MidData.mTestParameters[v].aisleTestFinished = true;
					     MidData.mTestParameters[v].IsNewSample=true;
					     MidData.mTestParameters[v].RealReDetect=false;
						 MidData.mTestParameters[v].ReDetect=false;					   
					}
				    }
				    else
				    {
					len = len + 4;
				    }
				}
				else
				{
				    len = len + 4;
				}
			    }
			    else
			    {
				len = len + 4;
			    }
			    
			}
		    }
		    if(MidData.m_UpToSA&&SAUpLoadQueue.size()>0&&!SAUpLoadQueue.isEmpty())
	    	{
	    		//SAUpLoadQueue.add(Integer.toString(v));
	    		AutoUpLoadToSA(SAUpLoadQueue);
	    		
	    	}
		    //System.out.println(kkkk+"-----SETNUM");
		    SendStopMsg2Device();
		    MidData.m_heightAisle[1] = '0';
		    MidData.m_heightAisle[2] = '0';
		    //System.out.println("-------AH------Time----end");
		}
	    }
	}
    }

   
    long lastTime = 0;

    public void MakeBuzzerBeep()
    {
	Time t = new Time();
	t.setToNow();
	long x = ((t.monthDay * 24 + t.hour) * 60 + t.minute) * 60 + t.second;
	if (x - lastTime > 2)
	{
	    new PWM().start();
	    lastTime = x;
	}
    }
    public class GetRecompenseThread extends Thread
    {
		@Override
		public void run()
		{
		    // TODO Auto-generated method stub
		    super.run();
		    if (MidData.m_recompenseAisle[1] == 'A'&& MidData.m_recompenseAisle[2] == 'h')
			{
				
				int len = 3;
				String[] rowname = new String[100];
				String[] value = new String[100];
				int len_Update = 0;
				for (int i = 0; i < 100; i++)
				{
				    byte[] heightByte = new byte[4];
				    for (int n = 0; n < 4; n++)
				    {
					heightByte[n] = MidData.m_recompenseAisle[len++];
				    }
				    float height = GetHeightByByte(heightByte);
				    if (height > 2)
				    {
					float tp = 57 - height;
		
					if (tp != MidData.mTestParameters[i].aisleRecompenseHeight)
					{
		
					    MidData.mTestParameters[i].aisleRecompenseHeight = tp;
					    rowname[len_Update] = "recompenseheight_"
						    + Integer.toString(i);
					    value[len_Update] = Float.toString(tp);
					    // System.out.println(height + "_____" + tp
					    // + "----------------ReHeight");
					    len_Update++;
					}
				    }
				}
				if (len_Update > 0)
				    MidData.sqlOp.UpdateData(1, "RecompenseHeightTable",
					    rowname, value, len_Update);
				MidData.m_recompenseAisle[1] = '0';
				MidData.m_recompenseAisle[2] = '0';
		
				MidData.m_isUpdateRecompense = true;
			}
		}
    }
    private float GetHeightByByte(byte[] heightByte)
    {
		String heightStr = new String(heightByte);
		int v = Integer.parseInt(heightStr, 16) * 10 + 5;
		float height = (float) ((double) v / 1200);// ((((double)v)*0.0008)+9.7078);
							   // //((double)v*0.001);
		/*
		 * height=CurveFitting(MidData.TestType,height,MidData.Instance
		 * ().isDebugging,MidData.XueChenTestRealTime);
		 * if(MidData.IsAutoTemResive.equals("1")) { height=TemRevise(height); }
		 */
		return height;
    }
    private class ExecuteSqlQueue extends Thread
    {

	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    try {
			sleep(60000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    while (MidData.m_isInterrupted_LowerMachine)
	    {

	    	SendStopMsg2DeviceTestCmd();
		if (!MidData.SqlQueue.isEmpty()
			&& (!MidData.isSql))
		{
		    MidData.isSql = true;
		    String Sql = (String) MidData.SqlQueue.poll();
		    MidData.sqlOp.ExInsertTestResult(Sql);
		    /*if()
		    {
		    	MidData.SqlQueue.remove(Sql);
		    }*/
		    MidData.isSql = false;
		}
//		try
//		{
//		    sleep(200);
//		}
//		catch (InterruptedException e)
//		{
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
	    }
	}
    }
    public class UpdateDebugThread extends Thread
    {
		@Override
		public void run()
		{
		    // TODO Auto-generated method stub
		    super.run();
		    if (MidData.m_debug[1] == 'A'
			    && MidData.m_debug[2] == 'D')
		    {
			int len = 3;
			byte[] bf = new byte[4];
			for (int i = 0; i < 100; i++)
			{
			    bf[0] = MidData.m_debug[len++];
			    bf[1] = MidData.m_debug[len++];
			    bf[2] = MidData.m_debug[len++];
			    bf[3] = MidData.m_debug[len++];
			    double bv = (double) Integer.parseInt(new String(bf), 16)
				    / (double) 1000.0;
			    MidData.m_debugStatus[i] = fnum2.format(bv);
			}
			MidData.m_debug[1] = '0';
			MidData.m_debug[2] = '0';
	
			MidData.m_isUpdateDebug = true;
		    }
		}
    }
}
