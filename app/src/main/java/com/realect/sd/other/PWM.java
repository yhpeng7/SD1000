package com.realect.sd.other;

import com.friendlyarm.AndroidSDK.HardwareControler;

public class PWM extends Thread
{
    int tm = 1000;

    public void SetPWMTime(int tt)
    {
	tm = tt;
    }

    @Override
    public void run()
    {
	super.run();
//	try
//	{
//	    sleep(tm);
//	}
//	catch (InterruptedException e1)
//	{
//	    e1.printStackTrace();
//	}
	HardwareControler mHardwareControler = new HardwareControler();
	mHardwareControler.PWMPlay(2500);
	try
	{
	    sleep(200);
	}
	catch (InterruptedException e)
	{
	    e.printStackTrace();
	}
	mHardwareControler.PWMStop();
    }
}
