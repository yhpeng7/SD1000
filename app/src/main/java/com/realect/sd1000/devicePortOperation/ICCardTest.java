package com.realect.sd1000.devicePortOperation;

import java.io.IOException;

import com.realect.sd1000.parameterStatus.MidData;

public class ICCardTest {
	private ICCreditCardPort mSerialPortForICCreditCard=new ICCreditCardPort();
	private static int dwSn;// 卡的序列号
	private int[] SDNum = new int[4];
	private static String GenerateKey;// 密钥
	private static String CardNum;
	private static String BatchNumber;
	private static String MarginOfCard;
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
	 public void StartTestICCard(){
	    	ReadSDCardTestThread mReadSdCardThread = new ReadSDCardTestThread();
		    mReadSdCardThread.start();
	    }
	public class ReadSDCardTestThread extends Thread
	{


		@SuppressWarnings("deprecation")
		@Override
		public void run()
		{

			super.run();
			int loop=0;
			boolean OK=false;
			while(loop<30)
			{
					loop++;
				    if(OK)
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
					mSerialPortForICCreditCard.SetReciveStatus(false);
					/*SendForGetSN();
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
		
					
					 * if(str.equals("")) { mReadThread1.interrupt();
					 * 
					 * continue; }
					 
					if(OK) break;
					mSerialPortForICCreditCard.SetReciveStatus(false);*/
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
					    continue;
					}
					if(OK) break;
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
					if(OK) break;
			    		
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
					    continue;
					}
					if(OK) break;
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
					if(OK) break;
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
					if(OK) break;
					if(!mSerialPortForICCreditCard.GetReciveStatus())
					{
						continue;
					}
					dwSn = GetSN();
		
					if (dwSn == 0)
					{
					    //mReadThread6.interrupt();
					   
					    continue;
					}
					if(OK) break;
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
		
					
		//			System.out.println(te + "__________te");
					if (numt.equals(""))
					{
					    continue;
					}
					else
					{
						OK=true;
					    
					}
				    
			}
			if(OK)
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
