package com.realect.sd1000.devicePortOperation;

import java.io.File;
import java.io.IOException;

import com.realect.sd.other.LoadFont;
import com.realect.sd1000.Calculate.CalculateFormula;
import com.realect.sd1000.parameterStatus.*;

import android_serialport_api.SerialPort;

public class PrintPort extends Thread{
	public SerialPort OpenSerialPortForPrint()
    {
		SerialPort sPort = null;
		try
		{	
		    sPort = new SerialPort(new File("/dev/ttySAC2"), 9600, 0);
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

    
    static int XMax = 150;
    static int YMax = 144;// 必须是8的整数倍
    static int YMaxOffSet = 20;
    static int XMaxOffset = 30;
    byte PrintfBuf[] = new byte[XMax * YMax / 8];// 一行用240个点
    float[] sortResult = new float[512];

    byte NorVa[] =
    { -128, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x01 };

    public void PrintLine(int X0, int Y0, int X1, int Y1)
    {

	if (Math.abs(X0 - X1) > Math.abs(Y0 - Y1))// 以X算
	{
	    double k = ((double) (Y0 - Y1)) / ((double) (X0 - X1));
	    double b = Y0 - k * X0;
	    for (int i = Math.min(X0, X1); i < Math.max(X1, X0); i++)
	    {
		PrintDot(i, (int) (k * i + b));
	    }
	}
	else
	{
	    double k = ((double) (X0 - X1)) / ((double) (Y0 - Y1));
	    double b = X0 - Y0 * k;
	    for (int i = Math.min(Y0, Y1); i < Math.max(Y0, Y1); i++)
	    {
		PrintDot((int) (i * k + b), i);
	    }
	}
    }

    public void PrintDot(int X, int Y)// 从 0 开始
    {
	if (X >= XMax)
	    return;
	if (Y >= YMax)
	    return;
	if (X < 0)
	    return;
	if (Y < 0)
	    return;

	int YUp = Y / 8;
	PrintfBuf[YUp * XMax + X] = (byte) (NorVa[Y % 8] | PrintfBuf[YUp * XMax
		+ X]);
    }
    public short nAsciiDotE[]={
    		0x00,0xFF,0xFF,0x08,0x10,  // -h-
    		0x1F,0x0F,0x00,0x00,0xC0,
    		0xC0,0x00,0x00,0xC0,0xC0,
    		0x00,
    		0x10,0x10,0x7F,0xFF,0x10,  // -t-
    		0x10,0x00,0x00,0x00,0x00,
    		0xC0,0xE0,0x60,0xC0,0x80,
    		0x00
    };
    public short nAsciiDot[] =
    {	    
	    0x7C,0x8A,0x92,0xA2,0x7C, // -0-

		0x00,0x42,0xFE,0x02,0x00, // -1-

		0x46,0x8A,0x92,0x92,0x62, // -2-

		0x84,0x82,0x92,0xB2,0xCC, // -3-

		0x18,0x28,0x48,0xFE,0x08, // -4-

		0xE4,0xA2,0xA2,0xA2,0x9C, // -5-

		0x3C,0x52,0x92,0x92,0x8C, // -6-

		0x80,0x8E,0x90,0xA0,0xC0, // -7-

		0x6C,0x92,0x92,0x92,0x6C, // -8-

		0x62,0x92,0x92,0x94,0x78, // -9-
		
		0x20,0xFC,0x22,0x22,0x24, // -t-
		
		0xFE,0x10,0x20,0x20,0x1E, // -h-
	    };
    public void PrintSZM(int Num, int Xs, int Ys)
    {
		short Va;
		int i = 0;
		if (Num > 9)
		    return;
		for (; i < 10; i++)
		{
		    Va = nAsciiDotE[Num * 10 + i];
		    for (int j = 0; j < 16; j++)
		    {
			if ((Va & (0x80 >> j)) > 0)
			{
			    PrintDot(Xs + i, Ys + j);
			}
		    }	
		}
    }
    public void PrintSNum(int Num, int Xs, int Ys)
    {
		short Va;
		int i = 0;
		if (Num > 11)
		    return;
		for (; i < 5; i++)
		{
		    Va = nAsciiDot[Num * 5 + i];
		    for (int j = 0; j < 8; j++)
		    {
			if ((Va & (0x80 >> j)) > 0)
			{
			    PrintDot(Xs + i, Ys + j);
			}
		    }	
		}
    }
    
    public void PrintChartByk(String resourcevalues, float mul,
	    String testtimetype,double temp)
    {
	
	// 原则上 mul 这个值不会小于1
	byte PrintfBufTemp[] = new byte[XMax + 5];
	//System.out.println(resourcevalues+"-----"+testtimetype+"--"+temp);
	String[] strS = resourcevalues.split(";");
	int len = strS.length;//这时基本上是没有有效数据或者没有血沉数据
	if(len<4)return ;//
	float[] yData = new float[len];
	for (int i = 0; i < len; i++)
	{
	    String[] str_r = strS[i].split(",");
	    yData[i] = (float) Double.parseDouble(str_r[1]);
	}
	
	for (int i = 0; i < PrintfBuf.length; i++)
	{
	    PrintfBuf[i]=0;
	}
	
	int XLen = (int) ((float) len / mul);// 这个值 应该只有两个值 30或者60
		    
	sortResult[0] = yData[0];
	sortResult[1] = sortResult[0]-yData[(int) mul];
	float hmul=mul;
	if(testtimetype.equals("0"))
    	hmul =  mul/ 2;
   
	for (int i = 2; i < XLen; i++)
	{
	    sortResult[i] = sortResult[0]-(yData[(int) ( i * hmul)]);
	    
	    if (MidData.mSetParameters.IsCurveFit == 1)
	    {
	    	//System.out.println(sortResult[i]+"----Fit0---"+i);
	    sortResult[i]  = CurveFitting("0",(float)sortResult[i] , false,testtimetype);
	    //System.out.println(sortResult[i]+"----Fit1---"+i);
		
		if (MidData.mSetParameters.IsAutoTemResive==1)
		{
		    sortResult[i] = CalculateFormula.TemReviseByGivenTemp(
					(float) sortResult[i], temp);//((float)sortResult[i], Double.parseDouble(xxxx) );
		    if (sortResult[i]  < 1)
			sortResult[i]  = 1;
		}
		else
		{
		    if (sortResult[i] < 0)
			sortResult[i]  = 0;
		}
		if (sortResult[i]  < 1)
			sortResult[i]  = 1;
		else if (sortResult[i] > 160)
		    sortResult[i] = 160;		
	    }
	    //System.out.println(sortResult[i]+"----Fit2---"+i);
	    sortResult[i]=(float) (sortResult[i]*0.75);
	    
	     PrintLine(XMaxOffset +(i-1)*2,YMax - 1 - YMaxOffSet -(int)sortResult[i-1],XMaxOffset +i*2,YMax - 1 - YMaxOffSet -(int)sortResult[i]);

	}

	PrintfBufTemp[0] = 0x1B;
	PrintfBufTemp[1] = 0x4B;
	PrintfBufTemp[2] = (byte) ((XMax % 256) - 256);// 240
	PrintfBufTemp[3] = (byte) (XMax / 256);
	PrintfBufTemp[XMax + 4] = 0x0D;


	PrintLine(XMaxOffset, 0, XMaxOffset, YMax - 1 - YMaxOffSet);// Y
	PrintLine(XMax - 1, YMax - 1 - YMaxOffSet, XMaxOffset, YMax - 1
		- YMaxOffSet);// X

	//PrintSNum(0, XMaxOffset - 10, YMax - 1 - YMaxOffSet);
	
	PrintDot(XMaxOffset + 1, YMax - 1 - YMaxOffSet - 30);
	//PrintSNum(4, XMaxOffset - 16, YMax - 1 - YMaxOffSet - 30);
	//PrintSNum(0, XMaxOffset - 10, YMax - 1 - YMaxOffSet - 30);
	PrintDot(XMaxOffset + 1, YMax - 1 - YMaxOffSet - 60);
	//PrintSNum(8, XMaxOffset - 16, YMax - 1 - YMaxOffSet - 60);
	//PrintSNum(0, XMaxOffset - 10, YMax - 1 - YMaxOffSet - 60);
	PrintDot(XMaxOffset + 1, YMax - 1 - YMaxOffSet - 90);
	//PrintSNum(1, XMaxOffset - 22, YMax - 1 - YMaxOffSet - 90);
	//PrintSNum(2, XMaxOffset - 16, YMax - 1 - YMaxOffSet - 90);
	//PrintSNum(0, XMaxOffset - 10, YMax - 1 - YMaxOffSet - 90);
	PrintDot(XMaxOffset + 1, YMax - 1 - YMaxOffSet - 120);
	//PrintSNum(1, XMaxOffset - 22, YMax - 1 - YMaxOffSet - 120);
	//PrintSNum(6, XMaxOffset - 16, YMax - 1 - YMaxOffSet - 120);
	//PrintSNum(0, XMaxOffset - 10, YMax - 1 - YMaxOffSet - 120);
	PrintSNum(11, XMaxOffset - 10, YMax - 1 - YMaxOffSet - 120);

	PrintDot(XMaxOffset + 15 * 2, YMax - 1 - YMaxOffSet - 1);
	//PrintSNum(1,XMaxOffset + 15 * 2-12, YMax - YMaxOffSet+3);
	//PrintSNum(5,XMaxOffset + 15 * 2-6, YMax - YMaxOffSet+3);
	
	PrintDot(XMaxOffset + 30 * 2, YMax - 1 - YMaxOffSet - 1);
	//PrintSNum(3,XMaxOffset + 30 * 2-12, YMax - YMaxOffSet+3);
	//PrintSNum(0,XMaxOffset + 30 * 2-6, YMax - YMaxOffSet+3);
	PrintDot(XMaxOffset + 45 * 2, YMax - 1 - YMaxOffSet - 1);
	//PrintSNum(4,XMaxOffset + 45 * 2-12, YMax - YMaxOffSet+3);
	//PrintSNum(5,XMaxOffset + 45 * 2-6, YMax - YMaxOffSet+3);
	PrintDot(XMaxOffset + 60 * 2, YMax - 1 - YMaxOffSet - 1);
	//PrintSNum(6,XMaxOffset + 60 * 2-12, YMax - YMaxOffSet+3);
	//PrintSNum(0,XMaxOffset + 60 * 2-6, YMax - YMaxOffSet+3);
	PrintSNum(10,XMaxOffset + 60 * 2-6, YMax - YMaxOffSet+3);
	//PrintSZM(1,XMaxOffset + 60 * 2-6, YMax - YMaxOffSet+3);
	for (int i = 0; i < YMax / 8; i++)
	{
	    for (int j = 0; j < XMax; j++)
	    {
	    	PrintfBufTemp[j + 4] = PrintfBuf[j + i * XMax];
	    }
	    SendToPrintPortWithNoCopy(PrintfBufTemp);
	    try
	    {
	    	sleep(200);
	    }
	    catch (InterruptedException e)
		{
		    e.printStackTrace();
		}
	}
    }
    public double AnverageTem(double[] tem)
    {
		double sum = 0;
		for (int i = 0; i < 5; i++)
		{
		    sum += tem[i];
		}
		return sum / 5;
    }
    public float TemRevise(float h)
    {
		float rh = h;
		
		double tem = AnverageTem(MidData.Temperature);
		
		double K = 0.00065 * Math.pow(tem, 2) - 0.04927 * tem + 1.70045;
		double b = 0;
		if (tem > 20)
		    b = -0.01896 * Math.pow(tem, 2) + 0.5286 * tem - 2.988;
		rh = (float) (K * h + b);
		if (rh < 1.0)
		    rh = (float) 1.0;
		return rh;
    }
    public float TemReviseByGivenTemp(float h,double tem)
    {
		float rh = h;
		double K = 0.00065 * Math.pow(tem, 2) - 0.04927 * tem + 1.70045;
		double b = 0;
		if (tem > 20)
		    b = -0.01896 * Math.pow(tem, 2) + 0.5286 * tem - 2.988;
		rh = (float) (K * h + b);
		if (rh < 1.0)
		    rh = (float) 1.0;
		return rh;
    }
    public float CurveFitting(String isTestType, float h, Boolean isDebug, String testtimetype)
    {
	double rh = 0;
	if (isTestType.equals("0"))
	{
	    if (testtimetype.equals("0"))
	    {
	    	rh = CurveFiting60(h);
	    }
	    else
	    {
	    	rh = CurveFiting30(h);
	    }
	}
	else
	    rh = h;
	return (float) rh;
    }

    private float CurveFiting60(float h)
    {
		/*double rh = 0;
		for (int i = 6; i >= 0; i--)
		{
		    rh += MidData.CurveXP[i] * Math.pow(h, i);
		}
		
		return (float) rh;*/
    	
    	double rh = 0.3219 + 1.7295 * h + 0.0527 * h * h - 0.0003 * h * h * h;
    	
		return (float) rh;	
    }

    private float CurveFiting30(float h)
    {
		double rh = 0.3219 + 1.7295 * h + 0.0527 * h * h - 0.0003 * h * h * h;
	
		return (float) rh;	
    }

    public void LoadEndThePrint()
    {
	byte[] end =
	{ 0x0D };
	SendToPrintPort(end);
	SendToPrintPort(end);
    }
    public void LoadExplanation()
    {
	int row = 0;
	int row_row = 0;
	LoadFont loadfont = new LoadFont();
	byte[] point2 =
	{ 0x1B, 0x31, 0, 0x0D };
	SendToPrintPort(point2);
	for (int i = 0; i < 8; i++)
	{
	    row = i / 2;
	    row_row = (i - row * 2) % 2;
	    int num = loadfont.LoadFontByRow(row, row_row);
	    if (num > 0)
	    {
		int ad = 0;
		byte[] point = new byte[num + 5];
		point[ad++] = 0x1B;
		point[ad++] = 0x4B;
		point[ad++] = (byte) (num);
		point[ad++] = 0;
		for (int j = 0; j < num; j++)
		{
		    point[ad++] = (byte) (MidData.AddressBitmap[j]);
		}
		point[ad++] = 0x0D;
		SendToPrintPort(point);
	    }

	}
	SendToPrintPort(point2);
	SendToPrintPort(point2);
	SendToPrintPort(point2);
    }
    
    
    public void SendToPrintPort(byte[] point)
    {
	if (MidData.m_OutputStream_Print == null)
	    return;
	try
	{
	   // System.out.println(new String(point) + "____________printExplain");
	    MidData.m_OutputStream_Print.write(point);
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void SendToPrintPortWithNoCopy(byte[] pointBuf)
    {
	try
	{
	    MidData.m_OutputStream_Print.write(pointBuf);
	}
	catch (IOException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
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
}
