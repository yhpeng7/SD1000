package com.realect.sd1000.devicePortOperation;

public class PrintDataStructClass {
	private String Num;
    private String ESR;
    private String YJ;
    private String xcType;
    private String plusPeriod;
    private String resourcevalues;
    private double temp;
     public void SetNum(String str)
     {
    	 Num=str;    	 
     }
     public void SetESR(String str)
     {
    	 ESR=str;
     }
     public void SetYJ(String str)
     {
    	 YJ=str;    	 
     }
     public void SetxcType(String str)
     {
    	 xcType=str;
     }
     public void SetplusPeriod(String str)
     {
    	 plusPeriod=str;
     }
     public void Setresourcevalues(String str)
     {
    	 resourcevalues=str;
     }
     public void Settemp(double v)
     {
    	 temp=v;
     }
     
     
     public String GetNum()
     {
    	 return Num;    	 
     }
     public String GetESR()
     {
    	 return ESR;
     }
     public String GetYJ()
     {
    	 return YJ;    	 
     }
     public String GetxcType()
     {
    	 return xcType;
     }
     public String GetplusPeriod()
     {
    	 return plusPeriod;
     }
     public String Getresourcevalues()
     {
    	 return resourcevalues;
     }
     public double Gettemp()
     {
    	 return temp;
     }
}
