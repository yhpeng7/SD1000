package com.realect.sd1000.devicePortOperation;

import java.text.DecimalFormat;

import android.graphics.Path;

import com.realect.sd1000.Calculate.CalculateFormula;
import com.realect.sd1000.parameterStatus.MidData;

public class TestDataConvert {
	DecimalFormat fnum = new DecimalFormat("##0.0");
	public String ConvertLineForLisByNum(int Num)
	{
		String[] getStr =
		    { "resourcedata", "xuechendata", "yajidata", "xuechentesttimetype",
			    "plusperiod", "isdebug", "temp" };

		    getStr = MidData.sqlOp.selectHistoryByNumAndTime(getStr, Num, 7);
		    String xuechenvalues = getStr[0];
		    if (xuechenvalues.equals(""))
		    {
			   return "";
		    }
		    String testtime = getStr[3];
		    // String plusperiod=getStr[4];
		    int plusperiod = Integer.parseInt(getStr[4]);
		    boolean debugflag = false;
		    if (getStr[5].equals("1"))
			debugflag = true;
		    float mul = (float) 1.0;
		    if (testtime.equals("0"))
			{			  
		    	mul = (float) 120 / (float) plusperiod;
			}
		    else
		    {
		    	mul = (float) 60 / (float) plusperiod;
		    }
		    mul =1;// (float) 60 / (float) plusperiod;
		    final String[] tempxuechenvalues = xuechenvalues.split(";");
		    // String[] tempyajivalues=yajivalues.split(";");
		    double[] xData = new double[(int) (tempxuechenvalues.length / mul)];
		    double[] yData = new double[(int) (tempxuechenvalues.length / mul)];
		    float hMul=mul;
		    if (testtime.equals("0"))
			{
		    	hMul /=2;
			}
		    for (int i = 0; i < (int) (tempxuechenvalues.length / mul); i++)
		    {
			String[] v = tempxuechenvalues[(int) (i * hMul)].split(",");
			xData[i] = i;
			yData[i] = Double.parseDouble(v[1]);
		    }
		    double[] xDataV = new double[(int) ((tempxuechenvalues.length / mul))];
		    double[] yDataV = new double[(int) ((tempxuechenvalues.length / mul))];
		    int lenOfData=(int) ((tempxuechenvalues.length / mul));
		    for (int i = 0; i < lenOfData; i++)
		    {
			xDataV[i] = xData[i];
			yDataV[i] = yData[0] - yData[i];
			if (yDataV[i] < 0)
			    yDataV[i] = 0;
		    }
		    
		    if (MidData.mSetParameters.IsCurveFit == 1)
		    {
				for (int i = 0; i < lenOfData; i++)
				{
				    yDataV[i] = (double) CalculateFormula.CurveFitting("0",
					    (float) yDataV[i], debugflag?1:0, testtime);				   
				    if (MidData.mSetParameters.IsAutoTemResive==1)
				    {
					yDataV[i] = (double) CalculateFormula.TemReviseByGivenTemp(
						(float) yDataV[i],
						Double.parseDouble(getStr[6]));
					
				    if (yDataV[i] < 1)
					    yDataV[i] = 1;
				    else if (yDataV[i] > 160)
						yDataV[i] = 160;
				}
		    }	    
		    }
		    String result="";
		    for (int i = 0; i < lenOfData; i++)
		    {
		    	result+=fnum.format(yDataV[i])+",";//Double.toString(yDataV[i])+",";
		    }
		    xData=yData=xDataV=yDataV=null;
		    return result;
		}
	public String ConvertLineForLis(int Id)
	{
		String[] getStr =
		    { "resourcedata", "xuechendata", "yajidata", "xuechentesttimetype",
			    "plusperiod", "isdebug", "temp" };

		    getStr = MidData.sqlOp.selectHistoryById(getStr, Id, 7);
		    String xuechenvalues = getStr[0];
		    if (xuechenvalues.equals(""))
		    {
			   return "";
		    }
		    String testtime = getStr[3];
		    // String plusperiod=getStr[4];
		    int plusperiod = Integer.parseInt(getStr[4]);
		    boolean debugflag = false;
		    if (getStr[5].equals("1"))
			debugflag = true;
		    float mul = (float) 1.0;
		    if (testtime.equals("0"))
			{			  
		    	mul = (float) 120 / (float) plusperiod;
			}
		    else
		    {
		    	mul = (float) 60 / (float) plusperiod;
		    }
		    mul =1;// (float) 60 / (float) plusperiod;
		    final String[] tempxuechenvalues = xuechenvalues.split(";");
		    // String[] tempyajivalues=yajivalues.split(";");
		    double[] xData = new double[(int) (tempxuechenvalues.length / mul)];
		    double[] yData = new double[(int) (tempxuechenvalues.length / mul)];
		    float hMul=mul;
		    if (testtime.equals("0"))
			{
		    	hMul /=2;
			}
		    for (int i = 0; i < (int) (tempxuechenvalues.length / mul); i++)
		    {
			String[] v = tempxuechenvalues[(int) (i * hMul)].split(",");
			xData[i] = i;
			yData[i] = Double.parseDouble(v[1]);
		    }
		    double[] xDataV = new double[(int) ((tempxuechenvalues.length / mul))];
		    double[] yDataV = new double[(int) ((tempxuechenvalues.length / mul))];
		    int lenOfData=(int) ((tempxuechenvalues.length / mul));
		    for (int i = 0; i < lenOfData; i++)
		    {
			xDataV[i] = xData[i];
			yDataV[i] = yData[0] - yData[i];
			if (yDataV[i] < 0)
			    yDataV[i] = 0;
		    }
		    
		    if (MidData.mSetParameters.IsCurveFit == 1)
		    {
				for (int i = 0; i < lenOfData; i++)
				{
				    yDataV[i] = (double) CalculateFormula.CurveFitting("0",
					    (float) yDataV[i], debugflag?1:0, testtime);				   
				    if (MidData.mSetParameters.IsAutoTemResive==1)
				    {
					yDataV[i] = (double) CalculateFormula.TemReviseByGivenTemp(
						(float) yDataV[i],
						Double.parseDouble(getStr[6]));
					
				    if (yDataV[i] < 1)
					    yDataV[i] = 1;
				    else if (yDataV[i] > 160)
						yDataV[i] = 160;
				}
		    }	    
		    }
		    String result="";
		    for (int i = 0; i < lenOfData; i++)
		    {
		    	result+=fnum.format(yDataV[i])+",";//Double.toString(yDataV[i])+",";
		    }
		    xData=yData=xDataV=yDataV=null;
		    
		    return result;
		}
}
