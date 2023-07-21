package com.realect.sd1000.Calculate;

import java.text.DecimalFormat;

import com.realect.sd1000.parameterStatus.*;

public class CalculateFormula {
	 public static DecimalFormat fnum = new DecimalFormat("##0");
	 public static DecimalFormat fnum1 = new DecimalFormat("##0.0");
	 public static String FitDataForExport(String rs)
	    {
		String rts = "";
		if (rs.equals(""))
		    return "";
		String[] strS = rs.split(";");
		int num = strS.length;

		for (int i = 0; i < num; i++)
		{
		    // String[] str_s=strS[i-1].split(",");
		    String[] str_r = strS[i].split(",");
		    double dt = Double.parseDouble(str_r[1]);// -Double.parseDouble(str_s[1]);

		   /* if (MidData.IsCurveFit == 1)
		    {
			dt = (double) CurveFitting("0", (float) dt,
				MidData.isDebugging, MidData.XueChenTestRealTime);
			if (dt > 160)
			    dt = 160;
			if (MidData.IsAutoTemResive.equals("1"))
			{
			    dt = (double) TemRevise((float) dt);
			    if (dt < 1)
				dt = 1;
			}
			else
			{
			    if (dt < 0)
				dt = 0;
			}
		    }*/

		    if (i == num)
		    {
			rts += str_r[0] + "," + Double.toString(dt);
		    }
		    else
		    {
			rts += str_r[0] + "," + Double.toString(dt) + ";";
		    }
		}

		return rts;
	    }
	public static float TemReviseByGivenTemp(float h,double tem)
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
	public byte[] StringTo4Bytes(int res)
    {
	byte[] target = new byte[4];
	String str = Integer.toHexString(res);
	if (str.getBytes().length == 4)
	{
	    target[0] = str.getBytes()[0];
	    target[1] = str.getBytes()[1];
	    target[2] = str.getBytes()[2];
	    target[3] = str.getBytes()[3];
	}
	else if (str.getBytes().length == 3)
	{
	    target[0] = 0x30;
	    target[1] = str.getBytes()[0];
	    target[2] = str.getBytes()[1];
	    target[3] = str.getBytes()[2];
	}
	else if (str.getBytes().length == 2)
	{
	    target[0] = 0x30;
	    target[1] = 0x30;
	    target[2] = str.getBytes()[0];
	    target[3] = str.getBytes()[1];
	}
	else if (str.getBytes().length == 1)
	{
	    target[0] = 0x30;
	    target[1] = 0x30;
	    target[2] = 0x30;
	    target[3] = str.getBytes()[0];
	}
	else
	{
	    target[0] = 0x30;
	    target[1] = 0x30;
	    target[2] = 0x30;
	    target[3] = 0x30;
	}
	return target;
    }

    public byte[] StringTo2Bytes(int res)
    {
	byte[] target = new byte[2];
	String str = Integer.toHexString(res);

	if (str.getBytes().length == 2)
	{
	    target[0] = str.getBytes()[0];
	    target[1] = str.getBytes()[1];
	}
	else if (str.getBytes().length == 1)
	{
	    target[0] = 0x30;
	    target[1] = str.getBytes()[0];
	}
	else
	{
	    target[0] = 0x30;
	    target[1] = 0x30;
	}
	return target;
    }

    public byte[] IntTo3Bytes(int res)
    {
	byte[] target = new byte[3];
	String str = Integer.toHexString(res);
	if (str.getBytes().length == 3)
	{
	    target[0] = str.getBytes()[0];
	    target[1] = str.getBytes()[1];
	    target[2] = str.getBytes()[2];
	}
	else if (str.getBytes().length == 2)
	{
	    target[0] = 0x30;
	    target[1] = str.getBytes()[0];
	    target[2] = str.getBytes()[1];
	}
	else if (str.getBytes().length == 1)
	{
	    target[0] = 0x30;
	    target[1] = 0x30;
	    target[2] = str.getBytes()[0];
	}
	else
	{
	    target[0] = 0x30;
	    target[1] = 0x30;
	    target[2] = 0x30;
	}
	return target;
    }

    public static byte[] intToByteArray(int i)
    {
	byte[] result = new byte[4];
	// 由高位到低位
	result[0] = (byte) ((i >> 24) & 0xFF);
	result[1] = (byte) ((i >> 16) & 0xFF);
	result[2] = (byte) ((i >> 8) & 0xFF);
	result[3] = (byte) (i & 0xFF);
	return result;
    }
    public static double AnverageTem(double[] tem)
    {
	double sum = 0;
	for (int i = 0; i < 5; i++)
	{
	    sum += tem[i];
	}
	return sum / 5;//加温度修正
    }
    public static int GetByteOfBitLength(byte bb)
    {
	int byteofbitlength = 0;
	if (bb > '0' && bb < '2')
	    byteofbitlength = 1;
	else if (bb >= '2' && bb < '4')
	    byteofbitlength = 2;
	else if (bb >= '4' && bb < '8')
	    byteofbitlength = 3;
	else if ((bb >= '8' && (bb <= '9')) || (bb >= 'A' && bb <= 'F')
		|| (bb >= 'a' && bb <= 'f'))
	    byteofbitlength = 4;

	return byteofbitlength;
    }
    public static float GetHeightByByte(byte[] heightByte, float revise)
    {
	String heightStr = new String(heightByte);
	int v = Integer.parseInt(heightStr, 16) * 10 + 5;
	float height = (float) ((double) v / 1200) + revise;// ((((double)v)*0.0008)+9.7078);
							    // //((double)v*0.001);
	/*
	 * height=CurveFitting(MidData.TestType,height,MidData.Instance
	 * ().isDebugging,MidData.XueChenTestRealTime);
	 * if(MidData.IsAutoTemResive.equals("1")) { height=TemRevise(height); }
	 */
	return height;
    }

    public float GetHeightByByte(byte[] heightByte)
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
    public static float TemRevise(float h)
    {
	float rh = h;
	/*
	 * if (AnverageTem(MidData.Temperature) != 18) { rh +=
	 * (AnverageTem(MidData.Temperature) - 18); }
	 */
	double tem = AnverageTem(MidData.Temperature);
	//
	/*
	 * y=kx+b k=f(t)=4E-05t^3+0.003t^2-0.102t+2.069 b=H(t) { t<=20 b=0 t>20
	 * b=0.019t^2+0.528t-2.988 }
	 */

	double K = 0.00065 * Math.pow(tem, 2) - 0.04927 * tem + 1.70045;
	double b = 0;
	if (tem > 20)
	    b = -0.01896 * Math.pow(tem, 2) + 0.5286 * tem - 2.988;
	rh = (float) (K * h + b);
	if (rh < 1.0)
	    rh = (float) 1.0;
	// rh=(float) (7.64+(8*(h-7.64))/(tem-10));
	return rh;
    }
    public static String YaJiToShow(String yj){
    	String valueyj="";
    	if (!yj.equals("") && !yj.equals("未检测"))
 	    {    		
 	    	if(MidData.m_resultInt==1)
 			{
 				//xuechenvalues = fnum.format(dataXC);
 	    		if((Double.parseDouble(yj)*100)%100<45)
 	    		{
 	    			valueyj = fnum.format((int)Double.parseDouble(yj));
 	    		}
 	    		else
 	    		{
 	    			valueyj = fnum.format((int)Double.parseDouble(yj)+1);
 	    		} 	    		
 			}
 			else
 			{
 				valueyj = fnum1.format(Double.parseDouble(yj));
 			}
 	    	//return valueyj;
 	    }
 	    else if (yj.equals("未检测"))
 	    {
 	    	valueyj=MidData.IsChinese==1?"未检测":"Untest";
 	    }
		return valueyj;    	 
    	
    }
    public static String GetHematTestResult(float height,String xcSrc)
    {
    	double firstH=0;
    	if (!xcSrc.equals("") && !xcSrc.equals("未检测"))
	    {
    		
    		String[] xcData=xcSrc.split(";");
    		if(xcData.length<10)
    		{
    			firstH=57;
    		}
    		else
    		{
	    		int testTime=xcData.length;
	    		String Firststr=xcData[0];
	    		String[] XCDataStr=Firststr.split(",");
	    		if(XCDataStr.length<2)
	    		{
	    			firstH=57;
	    		}
	    		else
	    		{
	    			firstH=Double.parseDouble(Firststr.split(",")[1]);
	    		}	    		
    		}
	    }
    	else
    	{
    		firstH=57;    		
    	}
		double dataYaJi = (height / (firstH-MidData.mSetParameters.AnticoagulantsHeight));		
		dataYaJi = dataYaJi * 100;
		dataYaJi = dataYaJi+MidData.mSetParameters.HematResultRevise;
		if (dataYaJi > 100)
		    dataYaJi = 100;
		else if(dataYaJi<20)
			dataYaJi=20;
		return YaJiToShow(Double.toString(dataYaJi));    	
    }
    public static int StringToIntForUpLoad(String val)
    {
    	if (val.equals("")||val==null||val.equals("未检测")||val.equals("Untest"))
	    {
	    	return 0;
	    }
    	int xx=0;
    	if((Double.parseDouble(val)*10)%10<5)
		{
    		xx=(int) Double.parseDouble(val);
	    	
		}
    	else
    	{
    		xx=(int) Double.parseDouble(val)+1;
	    	
    	}
    	return xx;
    	
    }
    public static String GetERSTestResult(String xcSrc,String isTestType,int isDebug,String testtimetype,double temD)
    {
    	String value="";
    	if (!xcSrc.equals("") && !xcSrc.equals("未检测"))
	    {
    		String[] xcData=xcSrc.split(";");
    		int testTime=xcData.length;
    		String Firststr=xcData[0];
    		String Laststr=xcData[testTime-1];
    		if(testtimetype.equals("0"))//60分钟测试
    		{
    			Laststr=xcData[testTime/2-1];
    		}
    		String firstH=Firststr.split(",")[1];
    		String LastH=Laststr.split(",")[1];
    		
			String xc="0";
	    		
			double dataXC = Double.parseDouble(firstH)-Double.parseDouble(LastH);// 根据实际需要转换为可用结果
			if (dataXC < 0.25)
			{
			    if (MidData.mSetParameters.IsCurveFit != 1)
			    {
				dataXC = 0;
			    }
			}
			if (MidData.mSetParameters.IsCurveFit == 1)
			{
				//System.out.println(selID[i]+"-----"+dataXC+"---SelHHHHPP");
			    dataXC = CalculateFormula.CurveFitting(isTestType, (float) dataXC,
				    MidData.isDebugging, testtimetype);
			    //System.out.println(selID[i]+"-----"+dataXC+"---SelHHHHEE");
			    /*if (dataXC > 160)
				dataXC = 160;*/
			    if (MidData.mSetParameters.IsAutoTemResive==1)
			    {				
				dataXC = CalculateFormula.TemReviseByGivenTemp(
					(float) dataXC, temD);
				
			    }
			   
			}
			if(isDebug==1)
				dataXC+=MidData.mSetParameters.ERSTestResultRevise;
			
			if(MidData.m_resultInt==1)
			{
				//dataXC=Math.round(dataXC)
				if((dataXC*100)%100<45)
				{
					dataXC=(int)dataXC;
				}
				else
				{
					dataXC=(int)dataXC+1;
				}
			}
			
			if(dataXC<1)
			{
				dataXC=1.0;
			}
			else if(dataXC>160)
			{
				dataXC=160.0;
			}
		
			if(MidData.m_resultInt==1)
			{
				value = fnum.format(dataXC);
			}
			else
			{
				value = fnum1.format(dataXC);
			}	
	    }
	    else 
	    {
	    	value= MidData.IsChinese==1?"未检测":"Untest";
	    }
    	return value;
    }
    public static String ResourceResultToShow(String xc,String isTestType,int isDebug,
    	    String testtimetype,double temD){
    	String value="";
    	if (!xc.equals("") && !xc.equals("未检测"))
	    {
		// xc
    		
		double dataXC = Double.parseDouble(xc);// 根据实际需要转换为可用结果
		if (dataXC < 0.25)
		{
		    if (MidData.mSetParameters.IsCurveFit != 1)
		    {
			dataXC = 0;
		    }
		}
		if (MidData.mSetParameters.IsCurveFit == 1)
		{
			//System.out.println(selID[i]+"-----"+dataXC+"---SelHHHHPP");
		    dataXC = CalculateFormula.CurveFitting(isTestType, (float) dataXC,
			    MidData.isDebugging, testtimetype);
		    //System.out.println(selID[i]+"-----"+dataXC+"---SelHHHHEE");
		    /*if (dataXC > 160)
			dataXC = 160;*/
		    if (MidData.mSetParameters.IsAutoTemResive==1)
		    {
			// dataXC = MidData.sOp.TemRevise((float) dataXC);
		    	
			dataXC = CalculateFormula.TemReviseByGivenTemp(
				(float) dataXC, temD);
			//System.out.println(selID[i]+"-----"+dataXC+"---SelTTTTT");
			/*if (dataXC < 1.0)
			    dataXC = 1.0;*/
		    }
		    else
		    {
			/*if (dataXC < 0)
			    dataXC = 0;*/
		    }
		    /*if (dataXC < 1.0)
			    dataXC = 1.0;*/
		}
		//dataXC=255;
		dataXC+=MidData.mSetParameters.ERSTestResultRevise;
		//System.out.println(dataXC+"---xc---"+num);
		//dataXC=5.5;
		if(MidData.m_resultInt==1)
		{
			//dataXC=Math.round(dataXC)
			if((dataXC*100)%100<45)
			{
				dataXC=(int)dataXC;
			}
			else
			{
				dataXC=(int)dataXC+1;
			}
		}
		//else
		//{
			//reult=MidData.mTestParameters[num].hematResult;
		//}
		if(dataXC<1)
		{
			dataXC=1.0;
		}
		else if(dataXC>160)
		{
			dataXC=160.0;
		}
	
		if(MidData.m_resultInt==1)
		{
			value = fnum.format(dataXC);
		}
		else
		{
			value = fnum1.format(dataXC);
		}	
		 //return value;
	    }
	    else if (xc.equals("未检测"))
	    {
	    	value= MidData.IsChinese==1?"未检测":"Untest";
	    }
    	return value;
    }
    public static float CurveFitting(String isTestType, float h, int isDebug,
    	    String testtimetype)
        {
    	double rh = 0;
    	//if (!isDebug && isTestType.equals("0"))
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

    public static float CurveFiting60(float h)
        {
    	double rh =0.3219 + 1.7295 * h + 0.0527 * h * h - 0.0003 * h * h * h;
    	/*for (int i = 6; i >= 0; i--)
    	{
    	    //
    	    rh += MidData.CurveXP[i] * Math.pow(h, i);
    	}*/
    	// if(rh<0)rh=0;
    	// if(rh>159)rh=159;
    	return (float) rh;
        }

    public static float CurveFiting30(float h)
        {

    	double rh = 0.3219 + 1.7295 * h + 0.0527 * h * h - 0.0003 * h * h * h;
    	// if(rh<0)rh=0;
    	// if(rh>159)rh=159;
    	return (float) rh;

    	/*
    	 * double rh = 0; if (h < 0.25) h = 0; if (h >= 0 && h <= 12) rh = 2.5 *
    	 * h; else if (h >= 12 && h <= 15) rh = 4 * h - 17.33; else if (h >= 15
    	 * && h <= 18) rh = 2.86 * h - 1.43; else if (h >= 18 && h <= 23) rh = 4
    	 * * h - 22; else if (h >= 23 && h <= 26) rh = 3.33 * h - 6.67; else if
    	 * (h >= 26 && h <= 30) rh = 5 * h - 50; else if (h >= 30 && h <= 33) rh
    	 * = 3.33 * h; else if (h >= 33 && h <= 40) rh = 5 * h - 55; else if (h
    	 * >= 41 && h <= 50) rh = 160; else rh = 160; return (float) rh;
    	 */
        }

      
}
