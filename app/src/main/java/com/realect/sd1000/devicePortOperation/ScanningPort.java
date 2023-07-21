package com.realect.sd1000.devicePortOperation;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;


import com.realect.sd1000.Calculate.CalculateFormula;
import com.realect.sd1000.parameterStatus.*;

import android.text.format.Time;
import android_serialport_api.SerialPort;
import com.realect.sd.other.*;
public class ScanningPort extends Thread{
	byte[] buffer = new byte[512];
	DecimalFormat fnum1 = new DecimalFormat("##0.0");
    public SerialPort OpenSerialPortForScanner()
    {
    	SerialPort sPort = null;
	
	    if (!MidData.IsConnectPort[0])
	    {
			try
			{
			    sPort = new SerialPort(new File("/dev/ttyUSB0"), 9600, 0);
			}
			catch (SecurityException e)
			{
			    e.printStackTrace();
			}
			catch (IOException e)
			{
			    e.printStackTrace();
			}
	    }

	    if (sPort != null)
	    {
	    	MidData.IsConnectPort[0] = true;
	    }
	    return sPort;	
    }
    public void ReciveDataFromSerial()
    {
	ReadThread mReadThread = new ReadThread();
	OperationScannerNumThread mOperationScannerNumThread = new OperationScannerNumThread();

	if (MidData.m_InputStream_Scanner != null)
	{
	    mReadThread.start();
	    mOperationScannerNumThread.start();

	}

    }
    public long GetTimeAfter2Period(){
		 Calendar cal=Calendar.getInstance();
		 int twoR=MidData.mSetParameters.RunPeriodSet*2;
		 cal.add(Calendar.SECOND, twoR);
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
    public class OperationScannerNumThread extends Thread
    {

	@SuppressWarnings("unchecked")
	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    if (MidData.m_InputStream_Scanner == null)
	    {
		return;
	    }
	    while (MidData.m_isInterrupted_Scanner)
	    {
		try
		{
		    sleep(100);
		}
		catch (InterruptedException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		if (!MidData.ScannerNumFinished)
		{
		    MidData.ScannerNumFinished = true;
		    byte[] tempBuf = new byte[MidData.ScannerNum];
		    for (int i = 0; i < MidData.ScannerNum; i++)
		    {
			tempBuf[i] = MidData.m_scannerData[i];
		    }
		    String numStr = new String(tempBuf);
		    numStr = numStr.trim();
		    MidData.ScanningNumNowStr = numStr;

		  
		    boolean conflag=false;
		    if (MidData.SelAddress< 0
			    || MidData.SelAddress > 99)// 没有选中 则自动分配
		    	MidData.SelAddress = 0;
		   if(MidData.mSetParameters.SetNumWay==0)
		    {
		    	for (int i=0; i < 100; i++)//???????????????????????????????????????
				{
					int x = i+MidData.SelAddress;
					x=x%100;
					int address = x;
					//MidData.passageStatus[address] = "通道故障";
					if (MidData.mTestParameters[address].aisleStatus==1&&MidData.mTestParameters[address].testStatus==0&& (MidData.mTestParameters[address].aisleNum == null||MidData.mTestParameters[address].aisleNum.equals("")))
					{
					    MidData.SelAddress = x;
					    break;
					}
				}
		    }
		    else
		    {
		    	for (int i=0; i < 100; i++)//???????????????????????????????????????
				{
					int x = i+MidData.SelAddress;
					x=x%100;
					int address = x;
					//MidData.passageStatus[address] = "通道故障";
					if (MidData.mTestParameters[address].aisleStatus==1)
					{
					    MidData.SelAddress = x;
					    break;
					}
				}
		    }
		    
		    if (MidData.mSetParameters.TestType==0)
		    {
				MidData.AddressSampleNumRefreshed = false;
				if(!MidData.sqlOp.YaJiTestFinished(numStr))
				{
					if (!MidData.sqlOp.XueChenTestFinished(numStr))
					{

						MidData.mTestParameters[MidData.SelAddress].aisleNumShow = numStr;
						for(int i=0;i<100;i++)
						{
							if(MidData.mTestParameters[i].aisleNum.equals(numStr)&&!MidData.mTestParameters[i].aisleTestFinished)
							{
								MidData.mTestParameters[MidData.SelAddress].aisleNumShow = "此样本正在检测";
								MidData.IsNeedUserConfirm=3;
								conflag=true;
								PWM mPWMThread = new PWM();
							    mPWMThread.SetPWMTime(1000);
							    mPWMThread.start();
								break;
							}
						}
						MidData.mTestParameters[MidData.SelAddress].aisleNum = numStr;
						MidData.mTestParameters[MidData.SelAddress].aisleSampleNumTime=GetTimeAfter2Period();
					    
					   if(!conflag&&MidData.mSetParameters.SetNumWay==1)
					    {
						   //System.out.println(IsSaveToDatabaseData.aislePastTestData+"---扫码时存储的高度值---"+MidData.SelAddress);
					    	MidData.sqlOp
									.InsertData(Integer.toString(MidData.SelAddress),
										MidData.mTestParameters[MidData.SelAddress].aisleNum,
										GetAddress(MidData.SelAddress),
										"",
										IsSaveToDatabaseData.aislePastTestData,
										IsSaveToDatabaseData.ERSResult,
										"未检测",
										IsSaveToDatabaseData.Temperature,
										Double.toString(IsSaveToDatabaseData.TestRealCount),
										Integer.toString(IsSaveToDatabaseData.runPeriod),
										IsSaveToDatabaseData.isDebug, "1",
										"0", "",MidData.mTestParameters[MidData.SelAddress].areaTemp);
					    	if(MidData.mTestParameters[MidData.SelAddress].saveDatabaseID>0)
					    	{
					    		MidData.mTestParameters[IsSaveToDatabaseData.Add].savedToDatabase=true;
						    	
						    	//MidData.mTestParameters[MidData.SelAddress].aisleNum="";
						    	//MidData.mTestParameters[MidData.SelAddress].aisleNumShow="";
						    	int x=0;
						    	for (int i=1; i < 100; i++)//???????????????????????????????????????
								{
									x = i;
									int address = (MidData.SelAddress+x)%100;
									//MidData.passageStatus[address] = "通道故障";
									if (MidData.mTestParameters[address].aisleStatus==1&&MidData.mTestParameters[address].aisleTestFinished&&MidData.mTestParameters[address].testStatus==3)
									{
									    MidData.SelAddress = address;
									    break;
									}
								}
					    	}
					    	
					    	
					    }
					
					}
					else
					{
						MidData.mTestParameters[MidData.SelAddress].IsNewSample = false;
					    MidData.mTestParameters[MidData.SelAddress].aisleNumShow = "样本重检";
					    MidData.mTestParameters[MidData.SelAddress].aisleNum = numStr;
					    MidData.IsNeedUserConfirm=1;
					    //MidData.passageIdOfDB[MidData.SelAddress] = MidData.AddressSampleNum[MidData.SelAddress];
					    PWM mPWMThread = new PWM();
					    mPWMThread.SetPWMTime(1000);
					    mPWMThread.start();
					}
				}
				else
				{
					MidData.mTestParameters[MidData.SelAddress].IsNewSample = false;
				    MidData.mTestParameters[MidData.SelAddress].aisleNumShow = "本编号无效（完成压积测试），请重新编号";
				    MidData.mTestParameters[MidData.SelAddress].aisleNum = numStr;
				    MidData.IsNeedUserConfirm=4;
				    //MidData.passageIdOfDB[MidData.SelAddress] = MidData.AddressSampleNum[MidData.SelAddress];
				    PWM mPWMThread = new PWM();
				    mPWMThread.SetPWMTime(1000);
				    mPWMThread.start();
				}
				/*if (!MidData.sqlOp.XueChenTestFinished(numStr))
				{
					if(!MidData.sqlOp.YaJiTestFinished(numStr))
					{
						MidData.mTestParameters[MidData.SelAddress].aisleNumShow = numStr;
						for(int i=0;i<100;i++)
						{
							if(MidData.mTestParameters[i].aisleNum.equals(numStr)&&!MidData.mTestParameters[i].aisleTestFinished)
							{
								MidData.mTestParameters[MidData.SelAddress].aisleNumShow = "此样本正在检测";
								MidData.IsNeedUserConfirm=3;
								conflag=true;
								PWM mPWMThread = new PWM();
							    mPWMThread.SetPWMTime(1000);
							    mPWMThread.start();
								break;
							}
						}
						MidData.mTestParameters[MidData.SelAddress].aisleNum = numStr;
						MidData.mTestParameters[MidData.SelAddress].aisleSampleNumTime=GetTimeAfter2Period();
					    
					   if(!conflag&&MidData.mSetParameters.SetNumWay==1)
					    {
						   System.out.println(IsSaveToDatabaseData.aislePastTestData+"---扫码时存储的高度值---"+MidData.SelAddress);
					    	MidData.mTestParameters[MidData.SelAddress].saveDatabaseID=MidData.sqlOp
									.InsertData(
										MidData.mTestParameters[MidData.SelAddress].aisleNum,
										GetAddress(MidData.SelAddress),
										"",
										IsSaveToDatabaseData.aislePastTestData,
										IsSaveToDatabaseData.ERSResult,
										"未检测",
										IsSaveToDatabaseData.Temperature,
										Double.toString(IsSaveToDatabaseData.TestRealCount),
										Integer.toString(IsSaveToDatabaseData.runPeriod),
										IsSaveToDatabaseData.isDebug, "1",
										"0", "");
					    	if(MidData.mTestParameters[MidData.SelAddress].saveDatabaseID>0)
					    	{
					    		MidData.mTestParameters[IsSaveToDatabaseData.Add].savedToDatabase=true;
						    	
						    	//MidData.mTestParameters[MidData.SelAddress].aisleNum="";
						    	//MidData.mTestParameters[MidData.SelAddress].aisleNumShow="";
						    	int x=0;
						    	for (int i=1; i < 100; i++)//???????????????????????????????????????
								{
									x = i;
									int address = (MidData.SelAddress+x)%100;
									//MidData.passageStatus[address] = "通道故障";
									if (MidData.mTestParameters[address].aisleStatus==1&&MidData.mTestParameters[address].aisleTestFinished&&MidData.mTestParameters[address].testStatus==3)
									{
									    MidData.SelAddress = address;
									    break;
									}
								}
					    	}
					    	
					    	
					    }
					}
					else
					{
						MidData.mTestParameters[MidData.SelAddress].IsNewSample = false;
					    MidData.mTestParameters[MidData.SelAddress].aisleNumShow = "本编号无效（完成压积测试），请重新编号";
					    MidData.mTestParameters[MidData.SelAddress].aisleNum = numStr;
					    MidData.IsNeedUserConfirm=4;
					    //MidData.passageIdOfDB[MidData.SelAddress] = MidData.AddressSampleNum[MidData.SelAddress];
					    PWM mPWMThread = new PWM();
					    mPWMThread.SetPWMTime(1000);
					    mPWMThread.start();
					}
				    //MidData.passageIdOfDB[MidData.SelAddress] = MidData.AddressSampleNum[MidData.SelAddress];
				}
				else
				{
				    MidData.mTestParameters[MidData.SelAddress].IsNewSample = false;
				    MidData.mTestParameters[MidData.SelAddress].aisleNumShow = "样本重检";
				    MidData.mTestParameters[MidData.SelAddress].aisleNum = numStr;
				    MidData.IsNeedUserConfirm=1;
				    //MidData.passageIdOfDB[MidData.SelAddress] = MidData.AddressSampleNum[MidData.SelAddress];
				    PWM mPWMThread = new PWM();
				    mPWMThread.SetPWMTime(1000);
				    mPWMThread.start();
				}*/
		    }
		    else
		    {
			MidData.AddressSampleNumRefreshed = false;
			if (!MidData.sqlOp.YaJiTestFinished(new String(tempBuf)))
			{
				MidData.mTestParameters[MidData.SelAddress].aisleNumShow = numStr;
				for(int i=0;i<100;i++)
				{
					if(MidData.mTestParameters[i].aisleNum.equals(numStr)&&!MidData.mTestParameters[i].aisleTestFinished)
					{
						MidData.mTestParameters[MidData.SelAddress].aisleNumShow = "此样本正在检测";
						MidData.IsNeedUserConfirm=3;
						conflag=true;
						PWM mPWMThread = new PWM();
					    mPWMThread.SetPWMTime(1000);
					    mPWMThread.start();
						break;
					}
				}
				
				MidData.mTestParameters[MidData.SelAddress].aisleNum = numStr;
				MidData.mTestParameters[MidData.SelAddress].aisleSampleNumTime=GetTimeAfter2Period();
			    //MidData.passageIdOfDB[MidData.SelAddress] = MidData.AddressSampleNum[MidData.SelAddress];
			    if(!conflag&&MidData.mSetParameters.SetNumWay==1)
			    {
			    	
				    if (!MidData.sqlOp.XueChenTestFinished(new String(tempBuf)))// 有标本号// 没有记录
				    {
					// 没有测试血沉
					// 此需要插入值 而不是更新
				    	double yajid=IsSaveToDatabaseData.hematTestHeight * 100 / (57-MidData.mSetParameters.AnticoagulantsHeight);
					    if(yajid>100.0)
					    	yajid=100.0;
						String dataYaJiStr = Double.toString(yajid);// 单位是
						IsSaveToDatabaseData.hematResult = fnum1.format(yajid);
						
				    	MidData.sqlOp
								.InsertData(Integer.toString(MidData.SelAddress),
									MidData.mTestParameters[MidData.SelAddress].aisleNum,
									"",
									GetAddress(MidData.SelAddress),
									"",
									"未检测",
									IsSaveToDatabaseData.hematResult,
									IsSaveToDatabaseData.Temperature,
									Double.toString(IsSaveToDatabaseData.TestRealCount),
									Integer.toString(MidData.mSetParameters.RunPeriodSet),
									"0",
									"0",
									"1",
									IsSaveToDatabaseData.aislePastTestData,MidData.mTestParameters[MidData.SelAddress].areaTemp);
				    	//System.out.println(MidData.mTestParameters[MidData.SelAddress].saveDatabaseID+"---insertFlagYJ");
				    	if(MidData.mTestParameters[MidData.SelAddress].saveDatabaseID>0)
				    	{
				    		MidData.mTestParameters[IsSaveToDatabaseData.Add].savedToDatabase=true;
					    	int x=0;
					    	for (int i=1; i < 100; i++)//???????????????????????????????????????
							{
								x = i;
								int address = (MidData.SelAddress+x)%100;
								//MidData.passageStatus[address] = "通道故障";
								if (MidData.mTestParameters[address].aisleStatus==1&&MidData.mTestParameters[address].aisleTestFinished&&MidData.mTestParameters[address].testStatus==3)
								{
								    MidData.SelAddress = address;
								    break;
								}
							}
				    	}
				    	//MidData.mTestParameters[MidData.SelAddress].aisleNum="";
				    	//MidData.mTestParameters[MidData.SelAddress].aisleNumShow="";
				    	
					}
				    
				    else
				    {
				    	String rs2[] = MidData.sqlOp.selectSingleHistoryByNumByK(MidData.mTestParameters[MidData.SelAddress].aisleNum);
					    String rs = rs2[0];
					    //System.out.println(rs+"---"+rs2[1]+"---------rs");
				    	String[] rs_XueChen = rs
					.split(";");
				String[] rs_XueChen_First = rs_XueChen[0]
					.split(",");
				double dataYaJi = (IsSaveToDatabaseData.hematTestHeight)
					/ (Double
						.parseDouble(rs_XueChen_First[1])-MidData.mSetParameters.AnticoagulantsHeight);
				if (dataYaJi > 1)
				    dataYaJi = 1;
				dataYaJi = dataYaJi * 100;

				IsSaveToDatabaseData.hematResult = fnum1.format(dataYaJi);
				    	if(MidData.sqlOp
						.UpdateYaJiByNo(
							MidData.mTestParameters[MidData.SelAddress].aisleNum,
							IsSaveToDatabaseData.hematResult,
							IsSaveToDatabaseData.aislePastTestData,
							"1",
							GetAddress(MidData.SelAddress)))
				    	{
				    		//System.out.println("UPDATAYJ-----------OK");
				    		MidData.mTestParameters[IsSaveToDatabaseData.Add].savedToDatabase=true;
					    	int x=0;
					    	for (int i=1; i < 100; i++)//???????????????????????????????????????
							{
								x = i;
								int address = (MidData.SelAddress+x)%100;
								//MidData.passageStatus[address] = "通道故障";
								if (MidData.mTestParameters[address].aisleStatus==1&&MidData.mTestParameters[address].aisleTestFinished&&MidData.mTestParameters[address].testStatus==3)
								{
								    MidData.SelAddress = address;
								    break;
								}
							}
				    	}
				    	else
				    	{
				    		
				    	}
				    		//System.out.println("UPDATAYJ-----------Fail");
				    	//MidData.mTestParameters[MidData.SelAddress].aisleNum="";
				    	//MidData.mTestParameters[MidData.SelAddress].aisleNumShow="";
				    	
				    }
			    }
			}
			else
			{
			    MidData.mTestParameters[MidData.SelAddress].aisleNumShow = "样本重检";
			    MidData.mTestParameters[MidData.SelAddress].aisleNum = numStr;
			    //MidData.passageIdOfDB[MidData.SelAddress] = MidData.AddressSampleNum[MidData.SelAddress];
			    MidData.IsNeedUserConfirm=1;
			    PWM mPWMThread = new PWM();
			    mPWMThread.SetPWMTime(1000);
			    mPWMThread.start();
			}
		    }
		    MidData.isScannerUpdated=false;
		}
	    }
	}
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
    private class ReadThread extends Thread
    {
	@SuppressWarnings("deprecation")
	@Override
	public void run()
	{
	    int Addr = 0;
	    byte[] Ptr = new byte[1];
	    if (MidData.m_InputStream_Scanner == null)
	    {
		return;
	    }
	    super.run();
	    while (MidData.m_isInterrupted_Scanner)
	    {
		try
		{
			MidData.m_InputStream_Scanner.read(Ptr);
			if (Addr > 511)
			{
			    Addr = 0;
			}
			//if (Ptr[0] <= '9' && Ptr[0] >= '0')
			    if(
					(Ptr[0]<='9'&&Ptr[0]>='0')||
			 (Ptr[0]<='Z'&&Ptr[0]>='A')||
			 (Ptr[0]<='z'&&Ptr[0]>='a')
			 )
			buffer[Addr++] = Ptr[0];

			if (Ptr[0] == 0x0D && Addr > 3)
			{		    
			    for (int i = 0; i < Addr; i++) // 得去掉前面的乱码
			    {
			    	MidData.m_scannerData[i] = buffer[i];
			    }
			    MidData.ScannerNum = Addr ;//- Outi
			    MidData.ScannerNumFinished = false;
			    Addr = 0;
			}
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}

	    }
	}
    }

}
