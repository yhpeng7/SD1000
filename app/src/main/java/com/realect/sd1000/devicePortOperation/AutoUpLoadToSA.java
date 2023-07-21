package com.realect.sd1000.devicePortOperation;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import KyleSocket_api.KyleSocketClass;

import android.os.Looper;
import android.widget.Toast;

import com.example.sd1000application.CurveActivity;
import com.realect.sd1000.Calculate.CalculateFormula;
import com.realect.sd1000.parameterStatus.MidData;

public class AutoUpLoadToSA extends Thread{
	private String lock;
	private Queue<String> LoadStringList=new LinkedList<String>(); 
    
    public AutoUpLoadToSA(String lock, Queue<String> Queue)
    {
	this.lock = lock;
	while(true)
	{
		if(Queue.isEmpty())
			break;
		
			int Address=Integer.parseInt(Queue.poll());
			String str=MidData.mTestParameters[Address].aisleNum;
			long xx;
    		long yy;
    		String YJ;
			if(!MidData.IsESRSelect[Address])
		    {
		    	if(MidData.sqlOp.XueChenTestFinished(MidData.mTestParameters[Address].aisleNum))
		    	{
		    		
		    		String rs[] = MidData.sqlOp.selectSingleHistoryByNumByK(MidData.mTestParameters[Address].aisleNum);
		    		 if(rs[0]==null||rs[0].equals(""))
					    {
					    	
					    	return;
					    }
					String XC = rs[1];//cursor.getString(cursor.getColumnIndex("xuechendata"));
				    
					XC=CalculateFormula.GetERSTestResult(rs[0], "0", MidData.isDebugging,Integer.toString(MidData.mSetParameters.ERSTestTime) , Double.parseDouble(rs[2]));//CalculateFormula.ResourceResultToShow(XC, "0", MidData.isDebugging,Integer.toString(MidData.mSetParameters.ERSTestTime) , Double.parseDouble(rs[2]));
					if (XC==null||XC.equals("未检测")||XC.equals("Untest")||XC.equals(""))
				    {
					xx=0;
				    }
				    else
				    {					
					xx=(int) CalculateFormula.StringToIntForUpLoad(XC);//dataXC;
				    }					    
				    
		    	}
		    	else
		    	{	    		
		    		xx=0;
		    		//System.out.println(MidData.isPrintXueChenValueStr+"------XC-----vvv");
		    	}
		    	
		    	
		    	YJ=MidData.mTestParameters[Address].hematResult;
		    }
		    else
		    {
		    	
		    	xx=(int) CalculateFormula.StringToIntForUpLoad(MidData.mTestParameters[Address].ERSResult);
		    	YJ="未检测";
		    }
			str+=","+Long.toString(xx)+","+YJ;
			System.out.println("---AutoUpLoadToSASSASASASA----"+str);
			LoadStringList.add(str);
	}
	
    }
    public void OpenSerialPortForUpLoadDataToSA()
    {
    	OpenSerialPortForUpLoadDataToSABySD100();	
    }
    private String test(String barcodeDesc) {
        Pattern p;
        p = Pattern.compile("\\d{1}");//在这里，编译 成一个正则。
        Matcher m;
        m = p.matcher(barcodeDesc);//获得匹配
        String res = "";
        while(m.find()){
            res=res+ m.group();
        }
        return res;
    }
    private void UpLaodData2PCBySD100()//Cursor cursor
    {
    	while(!LoadStringList.isEmpty())
    	{
    		/*if(LoadStringList.isEmpty())
    			break;*/
    		String[] str=LoadStringList.poll().split(",");
			if(str.length<3)
				continue;
    		String Num=str[0];
    		long xx;
    		long yy;
    		
    		String YJ;
    		
    		byte[] data = new byte[9]; 
		    xx= Long.parseLong(test(Num));//cursor.getString(cursor.getColumnIndex("xuhao"))

		    if(xx>28305)xx=xx%10000;//如果长度不够就只取后4位
		  
			yy=xx/100;
			if(yy>255)yy=255;
			data[0]=(byte)yy;
			xx=xx-yy*100;
		  
			yy=xx/10;
			if(yy>255)yy=255;
			data[1]=(byte)yy;
			xx=xx-yy*10;
			data[2]=(byte)xx;
		  
		  	//xx=220;
		    xx=Long.parseLong(str[1]);
		    YJ=str[2];
		  	if(xx<1)
		  		xx=1;
		  	else if(xx>160)
		  		xx=160;
		    yy=xx/100;
		    if(yy>255)yy=255;
		    data[3]=(byte)yy;
		    xx=xx-yy*100;
	  
		    yy=xx/10;
		    if(yy>255)yy=255;
		    data[4]=(byte)yy;
		    xx=xx-yy*10;
		    data[5]=(byte)xx;
		  
		  
		  
		    long xxYI=CalculateFormula.StringToIntForUpLoad(YJ);;
		    long yyYJ=xxYI/100;
			if(yyYJ>255)yyYJ=255;
			data[6]=(byte)yyYJ;
			xxYI=xxYI-yyYJ*100;
		  
			yyYJ=xxYI/10;
			if(yyYJ>255)yyYJ=255;
			data[7]=(byte)yyYJ;
			xxYI=xxYI-yyYJ*10;
			data[8]=(byte)xxYI;
			MidData.sOpForSA.SendToSAPort(data, data.length); 
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}	
    }
    public void OpenSerialPortForUpLoadDataToSABySD100()
    {
		if (MidData.portForSA == null||MidData.m_OutputStream_SA==null||MidData.m_InputStream_SA==null)
		{
		    MidData.portForSA = MidData.sOpForSA.OpenSerialPortForSAOpenBySD100();// MidData.sOp.OpenSerialPort();
			MidData.m_OutputStream_SA = MidData.portForSA.getOutputStream();
			MidData.m_InputStream_SA = MidData.portForSA.getInputStream();
		}	
    }
    private void ClearSAPort()
    {
    	int ll=0;
		try {
			ll = MidData.m_InputStream_SA.available();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		if(ll>0)
		{
			byte[] dtdt=new byte[ll];
			try {
				MidData.m_InputStream_SA.read(dtdt);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}    	
    }
    
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		synchronized (lock)// while(true)
		{
			/*try {
				sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			//if(MidData.portForSA == null||MidData.m_OutputStream_SA==null||MidData.m_InputStream_SA==null)
			//{
				OpenSerialPortForUpLoadDataToSA();
				ClearSAPort();
			//}
			if(MidData.portForSA != null&&MidData.m_OutputStream_SA!=null&&MidData.m_InputStream_SA!=null)
			{
				byte[] Ptr = new byte[1];
				int tttF=0;
			    for(int i=0;i<20;i++)
			    {
					Ptr[0]=0;
					try
					{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(MidData.m_InputStream_SA.available()>0)
						{
							MidData.m_InputStream_SA.read(Ptr);
							if(Ptr[0]==0x71)
								break;
						}
					}
					catch (IOException e)
					{
					    // TODO Auto-generated catch block
					    e.printStackTrace();
					}				
				tttF++;
			    }
			    System.out.println("------SASASASSASASAS-tttF--------"+tttF);
			    if(Ptr[0]!=0x71)
			    {
			    	System.out.println("------SASASASSASASAS---------0x71");
			    	return;
			    }
			    ClearSAPort();			
				 try
				{
					
				    Ptr[0]=0x72;
				    MidData.m_OutputStream_SA.write(Ptr);
				}
				catch (IOException e)
				{
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
				 int ff=0;
				for(int i=0;i<20;i++)
			    {
					Ptr[0]=0;
					try
					{
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(MidData.m_InputStream_SA.available()>0)
						{
							MidData.m_InputStream_SA.read(Ptr);
							if(Ptr[0]==0x73)
								break;
						}
					}
					catch (IOException e)
					{
					    // TODO Auto-generated catch block
					    e.printStackTrace();
					}
					ff++;
			    }
				System.out.println("------SASASASSASASAS-FFFF--------"+ff);
				 if(Ptr[0]!=0x73)
			     {
					 System.out.println("------SASASASSASASAS---------0x73");
			     return;
			     }
				 System.out.println("-----SASASASASA--------------SSSSSSS");
				 try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			 	 UpLaodData2PCBySD100();//cursor
			 	 System.out.println("-----SASASASASA--------------EEEEEEE");
				
					
				 byte[] PtrEnd = new byte[2];
				 PtrEnd[0]=0x74;
				 PtrEnd[1]=0x74;
				 try
				 {
					  MidData.m_OutputStream_SA.write(PtrEnd);					 
				 }
				 catch (IOException e)
				 {
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				 } 
				for(int i=0;i<10;i++)
			    {
				Ptr[0]=0;
				try
				{
					if(MidData.m_InputStream_SA.available()>0)
						MidData.m_InputStream_SA.read(Ptr);
				}
				catch (IOException e)
				{
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
				if(Ptr[0]==0x75)break;
				try
				{
				    Thread.sleep(1000);
				}
				catch (InterruptedException e)
				{
				    // TODO Auto-generated catch block
				    e.printStackTrace();
				}
			    }
				 if(Ptr[0]!=0x75)
				 {
					 System.out.println("------SASASASSASASAS---------0x75");
				     return;
				 }				 
				 MidData.sOpForSA.SendToSAPortClose();				
			}			
		}		
	}    
}
