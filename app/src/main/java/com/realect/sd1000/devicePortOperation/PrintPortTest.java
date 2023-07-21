package com.realect.sd1000.devicePortOperation;

import java.io.IOException;

import com.realect.sd1000.parameterStatus.MidData;

public class PrintPortTest {
	public void  PrintPortTestStart()
	{
		Thread mReadFromPort=new ReadFromPort();
		mReadFromPort.start();
		SendTestData();
	}
	public void SendTestData()
	{
		byte[] data=new byte[512];
		int len=0;
		data[len++]='>';
		data[len++]='A';
		data[len++]='B';
		for(int i=0;i<256;i++)
		{
			data[len++]=(byte) 128;
		}
		data[len++]=0x0D;
		SendToPrintPort(data,len);
	}
	public void SendToPrintPort(byte[] point, int len)
    {
		byte[] data = new byte[len];
		for (int i = 0; i < len; i++)
		{
		    data[i] = point[i];
		}
		try
		{
		    MidData.m_OutputStream_Print.write(data);
		}
		catch (IOException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
    }
	public class ReadFromPort extends Thread
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			byte[] Ptr=new byte[1];
			byte[] buff=new byte[512];
			int len=0;
			while(MidData.m_isInterrupted_Print)
			{
				
				try
				{
				    MidData.m_InputStream_Print.read(Ptr);
				}
				catch (IOException e)
				{
				    // TODO Auto-generated catch block
				    e.printStackTrace();		
				}
				if(Ptr[0]=='>')
				{
					len=0;
				}
				buff[len++]=Ptr[0];
				if(Ptr[0]==0x0D)
				{
					if(buff[0]=='>'&&buff[1]=='A'&&buff[2]=='B'&&len==260)
					{
						String cmd = "su -c reboot";
						try {
						          Runtime.getRuntime().exec(cmd);
						  } catch (IOException e) {
						         // TODO Auto-generated catch block
						         //new AlertDialog.Builder(this).setTitle("Error").setMessage(e.getMessage()).setPositiveButton("OK", null).show();
						  }
					}
					
				}
			}
		}
		
	}
}
