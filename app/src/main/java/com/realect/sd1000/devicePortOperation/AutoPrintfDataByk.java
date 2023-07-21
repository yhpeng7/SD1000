package com.realect.sd1000.devicePortOperation;

import com.realect.sd1000.parameterStatus.MidData;

import android.text.format.Time;

public class AutoPrintfDataByk extends Thread
{

    //private String lock;
    private String Num;
    private String ESR;
    private String YJ;
    private String xcType;
    private String plusPeriod;
    private String resourcevalues;
    private double temp;

    public AutoPrintfDataByk(String mNum, String mESR, String mYJ,
	    String mxcType, String mplusPeriod, String mresourcevalues,double mtemp)
    {
	//this.lock = lock;
	Num = mNum;
	ESR = mESR;
	YJ = mYJ;
	xcType = mxcType;
	plusPeriod = mplusPeriod;
	resourcevalues = mresourcevalues;
	temp=mtemp;
    }

    private void AutoPrintfDataCans(String Num, String ESR, String YJ,
	    String xcType, String plusPeriod, String resourcevalues,double temp)
    {

	//System.out.println(temp+"----AutoPrintfDataCans");
	Time t = new Time();
	t.setToNow();
	String date = Double.toString(t.year * Math.pow(10, 4) + (t.month + 1)
		* Math.pow(10, 2) + t.monthDay);

	MidData.isPrintDateStr = Integer.toString(t.year) + "-"
		+ Integer.toString(t.month + 1) + "-"
		+ Integer.toString(t.monthDay);
	MidData.isPrintNumStr = Num;
	MidData.isPrintXueChenValueStr = ESR;
	MidData.isPrintYaJiValueStr = YJ;
	float mul = (float) 1.0;
	if (MidData.mSetParameters.ERSTestTime==0)
	{
		mul = (float) 120 / (float) Integer.parseInt(plusPeriod);
	}
	else
	{
	    mul = (float) 60 / (float) Integer.parseInt(plusPeriod);
	}
	
	mul = (float) 60 / (float) Integer.parseInt(plusPeriod);
	MidData.sOpForPrint.LoadExplanation();
	if (MidData.mSetParameters.PrintSet==1)
	{
	    MidData.sOpForPrint.PrintChartByk(resourcevalues, mul,
		    xcType,temp);
	}

	MidData.sOpForPrint.LoadEndThePrint();
	MidData.sOpForPrint.LoadEndThePrint();

    }

    @Override
    public void run()
    {
	super.run();

	/*synchronized (lock)// while(true)
	{
		try {
			if (MidData.mSetParameters.PrintSet==1)
			  sleep(10000);
			else
				sleep(5000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	}*/
	AutoPrintfDataCans(Num, ESR, YJ, xcType, plusPeriod, resourcevalues,temp);
    }
}