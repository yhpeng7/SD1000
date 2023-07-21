package com.realect.sd1000.sqlite;

import com.realect.sd1000.parameterStatus.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;


import android.os.Environment;
import android.text.format.Time;

public class LoadInitialData
{
    public void InitialZiKu()
    {
	/*
	 * MidData.ziku_0.FNT_GB12={};
	 * MidData.ziku_1.FNT_GB12={};
	 * MidData.ziku_2.FNT_GB12={};
	 * MidData.ziku_3.FNT_GB12={};
	 * MidData.ziku_4.FNT_GB12={};
	 * MidData.ziku_5.FNT_GB12={};
	 * MidData.ziku_6.FNT_GB12={};
	 * MidData.ziku_7.FNT_GB12={};
	 * MidData.ziku_8.FNT_GB12={};
	 * MidData.ziku_9.FNT_GB12={};
	 * 
	 * MidData.ziku_Xue.FNT_GB12={0x00,0x20,0x00,0x20,0x3F,0xE0,0x20
	 * ,0x20, 0x3F,0xE0,0x60,0x20,0xA0,0x20,0x3F,0xE0,
	 * 0x20,0x20,0x7F,0xE0,0x20,0x20,0x00,0x20};
	 * MidData.ziku_Chen.FNT_GB12={};
	 * MidData.ziku_Ya.FNT_GB12={};
	 * MidData.ziku_Ji.FNT_GB12={};
	 */
    }

    public boolean LoadAddressSampleNum()
    {
    boolean flag=false;
	Time tn = new Time();
	tn.setToNow();
	long t = (int) ((int) (tn.year % 100) * Math.pow(10, 4) + (tn.month + 1)
		* Math.pow(10, 2) + tn.monthDay);
	String[] ds ={ "lasttargetnum", "lasttargetnumyj","initialnum"};
	MidData.exSqlOk=false;
	ds = MidData.sqlOp.select("SystemSetTable", ds,3);
	if(!MidData.exSqlOk)
		return false;
	
	long lastNumXC = Long.parseLong(ds[0]);
	long lastNumXCd = lastNumXC / 10000;
	long lastNumYJ = Long.parseLong(ds[1]);
	long lastNumYJd = lastNumYJ / 10000;
	MidData.exSqlOk=false;
	boolean flag_xc=MidData.sqlOp.TodayTestedXueChen();
	if(!MidData.exSqlOk)
		return false;
	MidData.exSqlOk=false;
	boolean flag_yj=MidData.sqlOp.TodayTestedYaJi();
	if(!MidData.exSqlOk)
		return false;
	if(!flag_xc&&!flag_yj)
	{
		MidData.mSetParameters.lastTargetNumHemat =  Long.parseLong(ds[2]);//(t * 10000)+1;
		MidData.mSetParameters.lastTargetNumERS =  Long.parseLong(ds[2]);//(t * 10000)+1;
		    	
    	MidData.fnum10LenESR=ds[2].length();
    	String fmStrESR="##";
		for(int i=0;i<MidData.fnum10LenESR;i++)
			fmStrESR+="0";
		MidData.fnum10ESR=new DecimalFormat(fmStrESR);
		MidData.fnum10LenHemat=10;
		MidData.fnum10Hemat=new DecimalFormat(fmStrESR);
	}
	else if(!flag_xc&&flag_yj)
	{
		MidData.mSetParameters.lastTargetNumERS = Long.parseLong(ds[2]);
    	
    	MidData.fnum10LenESR=ds[2].length();
    	
    	String fmStr="##";
		for(int i=0;i<MidData.fnum10LenESR;i++)
			fmStr+="0";
		MidData.fnum10ESR=new DecimalFormat(fmStr);
		
		MidData.mSetParameters.lastTargetNumHemat = Long.parseLong(ds[1])>Long.parseLong(ds[2])?Long.parseLong(ds[1]):Long.parseLong(ds[2]);
		
		MidData.fnum10LenHemat=Long.toString(MidData.mSetParameters.lastTargetNumHemat).length();
		fmStr="##";
		for(int i=0;i<MidData.fnum10LenHemat;i++)
			fmStr+="0";		
		MidData.fnum10Hemat=new DecimalFormat(fmStr);
		
	}
	else if(flag_xc&&!flag_yj)
	{		
		MidData.mSetParameters.lastTargetNumHemat = Long.parseLong(ds[2]);
		MidData.fnum10LenHemat=Long.toString(MidData.mSetParameters.lastTargetNumHemat).length();
		String fmStr="##";
		for(int i=0;i<MidData.fnum10LenHemat;i++)
			fmStr+="0";		
		MidData.fnum10Hemat=new DecimalFormat(fmStr);
		
		
		MidData.mSetParameters.lastTargetNumERS = Long.parseLong(ds[0])>Long.parseLong(ds[2])?Long.parseLong(ds[0]):Long.parseLong(ds[2]);
		MidData.fnum10LenESR=Long.toString(MidData.mSetParameters.lastTargetNumERS).length();
    	
		fmStr="##";
		for(int i=0;i<MidData.fnum10LenESR;i++)
			fmStr+="0";		
		MidData.fnum10ESR=new DecimalFormat(fmStr);
		
		
	}
	else if(flag_xc&&flag_yj)
	{
		if(lastNumXC>lastNumYJ)
		{
			MidData.mSetParameters.lastTargetNumERS = Long.parseLong(ds[0])>Long.parseLong(ds[2])?Long.parseLong(ds[0]):Long.parseLong(ds[2]);
			MidData.mSetParameters.lastTargetNumHemat = Long.parseLong(ds[1])>Long.parseLong(ds[2])?Long.parseLong(ds[1]):Long.parseLong(ds[2]);
			if(MidData.mSetParameters.TestType==1)
			{	
				MidData.fnum10LenESR=Long.parseLong(ds[0])>Long.parseLong(ds[2])?ds[0].length():ds[2].length();
		    	String fmStr="##";
				for(int i=0;i<MidData.fnum10LenESR;i++)
					fmStr+="0";
				MidData.fnum10ESR=new DecimalFormat(fmStr);
		    	MidData.fnum10LenHemat=Long.parseLong(ds[1])>Long.parseLong(ds[2])?ds[1].length():ds[2].length();
		    	fmStr="##";
				for(int i=0;i<MidData.fnum10LenHemat;i++)
					fmStr+="0";
				MidData.fnum10Hemat=new DecimalFormat(fmStr);
			} 
			else
			{
				MidData.fnum10LenESR=Long.parseLong(ds[0])>Long.parseLong(ds[2])?ds[0].length():ds[2].length();
		    	String fmStr="##";
				for(int i=0;i<MidData.fnum10LenESR;i++)
					fmStr+="0";
				MidData.fnum10ESR=new DecimalFormat(fmStr);
				MidData.fnum10LenHemat=Long.parseLong(ds[1])>Long.parseLong(ds[2])?ds[1].length():ds[2].length();
		    	fmStr="##";
				for(int i=0;i<MidData.fnum10LenHemat;i++)
					fmStr+="0";
				MidData.fnum10Hemat=new DecimalFormat(fmStr);
			}
		}
		else
		{
			MidData.mSetParameters.lastTargetNumERS = Long.parseLong(ds[0])>Long.parseLong(ds[2])?Long.parseLong(ds[0]):Long.parseLong(ds[2]);
			MidData.mSetParameters.lastTargetNumHemat = Long.parseLong(ds[1])>Long.parseLong(ds[2])?Long.parseLong(ds[1]):Long.parseLong(ds[2]);;
			MidData.fnum10LenESR=Long.parseLong(ds[0])>Long.parseLong(ds[2])?ds[0].length():ds[2].length();
			MidData.fnum10LenHemat=Long.parseLong(ds[1])>Long.parseLong(ds[2])?ds[1].length():ds[2].length();
	    	
			String fmStr="##";
			for(int i=0;i<MidData.fnum10LenESR;i++)
				fmStr+="0";
			MidData.fnum10ESR=new DecimalFormat(fmStr);
			
			fmStr="##";
			for(int i=0;i<MidData.fnum10LenHemat;i++)
				fmStr+="0";
			MidData.fnum10Hemat=new DecimalFormat(fmStr);
		}		
	}	
	return true;
	/*if (lastNumd == t)
	{
	    MidData.mSetParameters.lastTargetNumERS = Long.parseLong(ds[0]);
	    if(MidData.mSetParameters.TestType==0)
		{	    	
	    	MidData.fnum10Len=ds[0].length();
	    	String fmStr="##";
			for(int i=0;i<MidData.fnum10Len;i++)
				fmStr+="0";
			MidData.fnum10=new DecimalFormat(fmStr);
		}  
	    
	    lastNum = Long.parseLong(ds[1]);
		lastNumd = lastNum / 10000;
		if (lastNumd != t)
		{
			if(MidData.mSetParameters.lastNumSetWay==2)
			{
				MidData.mSetParameters.lastTargetNumHemat= Long.parseLong(ds[0]);
				if(MidData.mSetParameters.TestType==1)
				{	    	
			    	MidData.fnum10Len=ds1[0].length();
			    	String fmStr="##";
					for(int i=0;i<MidData.fnum10Len;i++)
						fmStr+="0";
					MidData.fnum10=new DecimalFormat(fmStr);
				}
			}
			else
			{
				MidData.mSetParameters.lastTargetNumHemat =  (t * 10000);
				String str = Long.toString(MidData.mSetParameters.lastTargetNumHemat);
				if(MidData.mSetParameters.TestType==1)
				{	    	
			    	MidData.fnum10Len=10;
			    	String fmStr="##";
					for(int i=0;i<MidData.fnum10Len;i++)
						fmStr+="0";
					MidData.fnum10=new DecimalFormat(fmStr);
				}
			}			
			//MidData.sqlOp.update(1, "SystemSetTable","lasttargetnumyj", str);
		}
		else	
		{
			MidData.mSetParameters.lastTargetNumHemat= Long.parseLong(ds[0]);
			if(MidData.mSetParameters.TestType==1)
			{	    	
		    	MidData.fnum10Len=ds[0].length();
		    	String fmStr="##";
				for(int i=0;i<MidData.fnum10Len;i++)
					fmStr+="0";
				MidData.fnum10=new DecimalFormat(fmStr);
			}
		}
			 
		
		
	}
	else
	{
		
	    MidData.mSetParameters.lastTargetNumERS = (int) (t * 10000);
	    if(MidData.mSetParameters.TestType==0)
		{	    	
	    	MidData.fnum10Len=10;
	    	String fmStr="##";
			for(int i=0;i<MidData.fnum10Len;i++)
				fmStr+="0";
			MidData.fnum10=new DecimalFormat(fmStr);
		}
	    ds[0] ="lasttargetnumyj";
	    ds = MidData.sqlOp.select("SystemSetTable", ds, 1);
	    lastNum = Long.parseLong(ds[0]);
		lastNumd = lastNum / 10000;
		if (lastNumd != t)
		{
			MidData.mSetParameters.lastTargetNumHemat=MidData.mSetParameters.lastTargetNumERS;
		}
		else
		{
			MidData.mSetParameters.lastTargetNumHemat= Long.parseLong(ds[0]);
		    if(MidData.mSetParameters.TestType==1)
			{	    	
		    	MidData.fnum10Len=ds[0].length();
		    	String fmStr="##";
				for(int i=0;i<MidData.fnum10Len;i++)
					fmStr+="0";
				MidData.fnum10=new DecimalFormat(fmStr);
			}
		}    
	    
	    String str = Long.toString(MidData.mSetParameters.lastTargetNumERS);
	    //MidData.sqlOp.update(1, "SystemSetTable","lasttargetnum", str);
	    //MidData.sqlOp.update(1, "SystemSetTable","lasttargetnumyj", str);

	}*/
    }
   
    @SuppressWarnings("static-access")
    public boolean LoadSystemSet()
    {
    
	String[] SystemSet =
	{ "testtype", "isprintchart", "xuechentesttype",
		"resetheighrevise", "xuechenrevise", "yajirevise",
		"isautoprint", "isautouploaddata", "isautotemrevise",
		"xuecheneffectiveheightrangerevise","maxrpmofdrivce", "pulseperiodset", 
		"pulseperiodreal","plydistanceset", "voltagereferenceset", 
		"voltagecompareset","temreviseset","iscurvefit","anticoagulantsheightset","curveX6", "curveX5", "curveX4", "curveX3", 
		"curveX2","curveX1", "curveX0", "productid", 
		"xuechenprint","yajiprint","ipstr","port","setnumway","fanopenclose","language","showint","mintest","initialnum",
		"consumerreduce","lisbrt","lisuploadline" };
	MidData.exSqlOk=false;
	SystemSet = MidData.sqlOp.select("SystemSetTable",
		SystemSet, 40);
	if(!MidData.exSqlOk)
		return false;
	MidData.mSetParameters.TestType = Integer.parseInt(SystemSet[0]);
	MidData.mSetParameters.PrintSet = Integer.parseInt(SystemSet[1]);
	
	MidData.mSetParameters.ERSTestTime = Integer.parseInt(SystemSet[2]);
	MidData.mSetParameters.DataTransferSet=Integer.parseInt(SystemSet[7]);
	
	MidData.m_UpToSA=((Integer.parseInt(SystemSet[7])&0x31)==1)?true:false;
	MidData.m_UpToLIS_net=((Integer.parseInt(SystemSet[7])&0x32)==2)?true:false;
	MidData.m_UpToLIS_ser=((Integer.parseInt(SystemSet[7])&0x34)==4)?true:false;
	//MidData.m_UpToSAAuto=((Integer.parseInt(SystemSet[7])&0x38)==8)?true:false;
	
	MidData.m_PrintAfterERS=((Integer.parseInt(SystemSet[6])&0x31)==1)?true:false;
	MidData.m_PrintAfterHemat=((Integer.parseInt(SystemSet[6])&0x32)==2)?true:false;
	
	MidData.mSetParameters.IsAutoTemResive=Integer.parseInt(SystemSet[8]);
	
	MidData.mSetParameters.ResetHeightRevise=((float)((int)(Float.parseFloat(SystemSet[3])*10)))/10;
	MidData.mSetParameters.ERSTestIniValue=(float)((int)(Float.parseFloat(SystemSet[9])*10))/10;
	MidData.mSetParameters.VoltageReferenceSet=Integer.parseInt(SystemSet[14]);
	MidData.mSetParameters.VolageCompareSet=Integer.parseInt(SystemSet[15]);
	MidData.mSetParameters.ERSTestResultRevise=(float)((int)(Float.parseFloat(SystemSet[4])*10))/10;
	MidData.mSetParameters.HematResultRevise=(float)((int)(Float.parseFloat(SystemSet[5])*10))/10;
	MidData.mSetParameters.TemperatureRevise=(float)((int)(Float.parseFloat(SystemSet[16])*10))/10;
	MidData.mSetParameters.RunPeriodSet=Integer.parseInt(SystemSet[11]);
	//MidData.mSetParameters.HeightFitSet=Integer.parseInt(SystemSet[17]);
	//MidData.mSetParameters.TestType=Integer.parseInt(SystemSet[18]);
	MidData.mSetParameters.AnticoagulantsHeight=Float.parseFloat(SystemSet[18]);
	MidData.mSetParameters.IsCurveFit=Integer.parseInt(SystemSet[17]);
	MidData.MaxRPMOfDrivce = Integer.parseInt(SystemSet[10]);
	MidData.mSetParameters.IsAutoTemResive = Integer.parseInt(SystemSet[8]);
	if(MidData.mSetParameters.ERSTestTime==0)
		MidData.ERSRealTestTime=(60 * 60) / MidData.mSetParameters.RunPeriodSet;
	else
		MidData.ERSRealTestTime=(30 * 60) / MidData.mSetParameters.RunPeriodSet;
	MidData.XueChenTestRealTime=Double.toString(MidData.ERSRealTestTime);
	if (MidData.mSetParameters.TestType==0)
	{
	    if (MidData.mSetParameters.ERSTestTime==0)
	    {
		MidData.DriRunTime = 2;
	    }
	    else
	    {
		MidData.DriRunTime = 1;
	    }
	    MidData.DirveRunTestTime=MidData.ERSRealTestTime;
	}
	else
	{
	    MidData.DriRunTime = 0;	   
	}
	 MidData.HematRealTestTime=2;

	MidData.MinHeight = 57 - MidData.mSetParameters.ERSTestIniValue;
	MidData.MaxHeight = 57 + MidData.mSetParameters.ERSTestIniValue;
	MidData.IpStr=SystemSet[29];
	MidData.Port=Integer.parseInt(SystemSet[30]);
	MidData.mSetParameters.FanOpenClose=Integer.parseInt(SystemSet[32]);
	MidData.IsChinese=Integer.parseInt(SystemSet[33]);//SystemSet[33].equals("1")?true:false;
	MidData.m_resultInt=Integer.parseInt(SystemSet[34]);
	MidData.m_minTest=Double.parseDouble(SystemSet[35]);
	MidData.mSetParameters.InitialNum=Integer.parseInt(SystemSet[36]);
	MidData.IsConsumerReduce=(SystemSet[37].equals("1"))?true:false;
	MidData.LisBrt=Integer.parseInt(SystemSet[38]);
	MidData.LisUploadLine=(SystemSet[39].equals("1"))?true:false;
	MidData.mSetParameters.SetNumWay=0;
	//MidData.mSetParameters.SetNumWay=Integer.parseInt(SystemSet[31]);
	String[] ConsumData =
	{ "consumnamexuechen", "consumnameyaji", "consumxuechencount",
		"consumyajicount", "lastcreditcardtime" };
	MidData.exSqlOk=false;
	ConsumData = MidData.sqlOp.selectConsumData(ConsumData, 5);
	if(!MidData.exSqlOk)
		return false;
	MidData.mSetParameters.ERSCount = Integer.parseInt(ConsumData[2]);
	MidData.mSetParameters.hematCount = Integer.parseInt(ConsumData[3]);
	MidData.mSetParameters.LastCreditCard_Time = ConsumData[4];
	MidData.exSqlOk=false;
	float[] RecompenseHeight = MidData.sqlOp
		.selectRecompenseData();
	if(!MidData.exSqlOk)
		return false;
	for(int i=0;i<100;i++)
	{
		MidData.mTestParameters[i].aisleRecompenseHeight=RecompenseHeight[i];
	}
	return true;
    }
}
