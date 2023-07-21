package com.example.sd1000application;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.realect.sd1000.Calculate.CalculateFormula;
import com.realect.sd1000.devicePortOperation.TestDataConvert;
import com.realect.sd1000.parameterStatus.MidData;

import KyleSocket_api.KyleSocketClass;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager.OnCancelListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CurveActivity extends Activity{
	private EditText editText_SampleNum;
	private TextView textView_Result;
	private TextView textView_Process;
	private TextView textView_Height;
	private TextView textView_Address;
	private TextView textView_Status;
	private ImageView CurveArea;
	//private DecimalFormat fnum10 = new DecimalFormat("##0000000000");
	DecimalFormat fnum = new DecimalFormat("##0");
	DecimalFormat fnum1 = new DecimalFormat("##0.0");
	float drawGridWidth = 140;//canvas.getWidth();//480;// getWidth() - 30;//缂備焦锚閸╂锟介挊澶婎唺
	float drawGridHeight = 180;//canvas.getHeight();//450;// getHeight() - 40;//缂備焦锚閸╂顨炲Ο鍝勵唺

	float beginX = 40;
	float beginY = 20;
	
	float xSpace = (drawGridWidth)  / 4;
	float ySpace = (drawGridHeight) / 4;
	
	
	private Bitmap baseBitmap;
	private static Canvas canvas;
	private Paint backPaint;
	private Paint linePaint;
	private Paint chartPaint;

	private Paint textPaint;
	private Button button_Print;
	private Button button_UpLoad;
	private Button button_Delete;
	private Button button_Back;
	private Button button_Submit_SampleNum;
	private Button button_GetScanningSampleNum;
	private Button button_Submit_SampleNumReTest;
	private Button button_Submit_SampleNumZhiKong;
	private TextView textViewNumLab;
	Intent intent;
	Bundle b;
	int num=0;
	private static final int MSG_SUCCESS = 0;//获取图片成功的标识
	private static final int MSG_FAILURE = 1;//获取图片失败的标识
	private Handler mHandler = new Handler() {
			public void handleMessage (Message msg) {//此方法在ui线程运行
				switch(msg.what) {
				case MSG_SUCCESS:
					//Toast.makeText(getApplication(), "11111111111111111111111成功！", Toast.LENGTH_LONG).show();
					button_UpLoad.setEnabled(true);
					break;
				case MSG_FAILURE:
					//Toast.makeText(getApplication(), "111111111111111111111111失败！", Toast.LENGTH_LONG).show();
					button_UpLoad.setEnabled(true);
					break;
				}
			}
		};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_curve);
		//getWindow().setTitle(MidData.IsChinese?"状态":"State");
		LoadXML();
		InitialChart();
		LoadStatus();		
		LoadChart();
		LoadSet();
	}
	private void LoadSet(){
		if(MidData.mSetParameters.SetNumWay==0)
			button_GetScanningSampleNum.setVisibility(View.INVISIBLE);
		else 
			button_GetScanningSampleNum.setVisibility(View.VISIBLE);
		button_Print.setText(MidData.IsChinese==1?"打印":"Print");
		button_UpLoad.setText(MidData.IsChinese==1?"上传":"Upload");
		button_Delete.setText(MidData.IsChinese==1?"删除":"Delete");
		button_Back.setText(MidData.IsChinese==1?"返回":"Return");	
		button_GetScanningSampleNum.setText(MidData.IsChinese==1?"开始扫码":"Scanning");
		textViewNumLab.setText(MidData.IsChinese==1?"标本号:":"ID:");
		button_Submit_SampleNumReTest.setText(MidData.IsChinese==1?"重测":"Retest");
		button_Submit_SampleNumZhiKong.setText(MidData.IsChinese==1?"质控编号":"Lot No.");

	}
	private void LoadChart(){
		float Multiple = (float) 1.0;
		if(!MidData.IsESRSelect[num])
			return;
		if (MidData.mSetParameters.ERSTestTime==0)
		{
		    Multiple = (float) (((double) (120 ) / (double) MidData.mSetParameters.RunPeriodSet));
		}
		else
		{
		    Multiple = (float) (((double) (60) / (double) MidData.mSetParameters.RunPeriodSet));
		}
		Multiple = (float) (((double) (60) / (double) MidData.mSetParameters.RunPeriodSet));
		// System.out.println(Float.toString(Multiple)+"-----------Multiple");
		int lenOfData = (int) (MidData.mTestParameters[num].aisleTestTime / Multiple) - 1;
		if (lenOfData <= 0)
		{
		    //InitialChart();
		    return;
		}
		String str="";		
		for (int i = 0; i < MidData.mTestParameters[num].aisleTestTime; i++)
		{
		    //xData[i] =i*10;
		    //yData[i] =i*10;
		    str+=Double.toString(i)+","+Double.toString(MidData.mTestParameters[num].aisleHeght[i])+";";
		}
		//System.out.println(str+"-----------ResPoint");
		//lenOfData=10;
		if (MidData.mSetParameters.ERSTestTime==0)
		{
		    Multiple /=2;
		}
		
		double[] xData = new double[lenOfData];
		double[] yData = new double[lenOfData];
		for (int i = 0; i < lenOfData; i++)
		{
			if(MidData.mTestParameters[num].aisleHeght[(int) ((i + 1) * Multiple)]<=0.0)
				MidData.mTestParameters[num].aisleHeght[(int) ((i + 1) * Multiple)]=MidData.mTestParameters[num].aisleHeght[(int) ((i + 1) * Multiple)-1];
		    xData[i] = (i);
		    yData[i] = MidData.mTestParameters[num].aisleHeght[0]
			    - MidData.mTestParameters[num].aisleHeght[(int) ((i + 1) * Multiple)];// Change
		    // By
		    // K
		    // 2014年8月11日11:32:20
		    if (yData[i] < 0.25)
			yData[i] = 0;

		    // System.out.println(String.valueOf(yData[i])+"  __yydata"+"----"+i);
		    // System.out.println(String.valueOf(Multiple)+" LenMin="+String.valueOf(lenOfData)+" "+String.valueOf(i)+" "+String.valueOf(num)+" :"+
		    // String.valueOf(MidData.dataOfTest[(int) ((i + 1)
		    // *Multiple)][num])+"     ---    "+String.valueOf(yData[i])+"  __ydata");
		}
		str="";		
		for (int i = 0; i < lenOfData; i++)
		{
		    //xData[i] =i*10;
		    //yData[i] =i*10;
		    str+=Double.toString(xData[i])+","+Double.toString(yData[i])+";";
		}
		//System.out.println(str+"-----------PrePoint");
		if (MidData.mSetParameters.IsCurveFit == 1)
		{
		    for (int i = 0; i < lenOfData; i++)
		    {
			if (yData[i] < 0.25)
			    yData[i] = 0;
			yData[i] = (double) CalculateFormula.CurveFitting("0",
				(float) yData[i], MidData.isDebugging,
				Integer.toString(MidData.mSetParameters.ERSTestTime));
			/*if (yData[i] > 160)
			    yData[i] = 160;*/
			if (MidData.mSetParameters.IsAutoTemResive==1)
			{
				double temp=CalculateFormula.AnverageTem(MidData.Temperature);
				if(MidData.mTestParameters[num].aisleTestFinished)
					temp=MidData.mTestParameters[num].averageTem;
			    yData[i] = (double) CalculateFormula.TemReviseByGivenTemp((float) yData[i],temp);
			    if (yData[i] < 1)
				yData[i] = 1;
			}
			else
			{
			    if (yData[i] < 0)
				yData[i] = 0;
			}
			if (yData[i] < 1)
				yData[i] = 1;
			else if (yData[i] > 160)
			    yData[i] = 160;
		    }
		    
		}
		
		str="";		
		for (int i = 0; i < lenOfData; i++)
		{
		    //xData[i] =i*10;
		    //yData[i] =i*10;
		    str+=Double.toString(xData[i])+","+Double.toString(yData[i])+";";
		}
		//System.out.println(str+"-----------LastPoint");
		str="";		
		for (int i = 0; i < lenOfData; i++)
		{
		    //xData[i] =i*10;
		    //yData[i] =i*10;
		    str+=Double.toString(xData[i])+","+Double.toString(yData[i])+";";
		}
		//System.out.println(str+"-----------chartPoint");
		for (int i = 0; i < lenOfData; i++)
		{
		    xData[i] = xData[i]*(xSpace/15)+beginX;
		    yData[i] =200-yData[i]*(ySpace/40);
		}
		Path path=new Path();
		
		path.moveTo((float)xData[0], (float)yData[0]);
		//str=Double.toString(xData[0])+","+Double.toString(yData[0])+";";
		for (int i = 0; i < lenOfData; i++)
		{
			path.lineTo((float)xData[i], (float)yData[i]);
			//str+=Double.toString(xData[i]+beginX)+","+Double.toString(yData[i]+beginY)+";";
		}
		canvas.drawPath(path, chartPaint);
		//System.out.println(str+"-----------chartPoint");
		CurveArea.setImageBitmap(baseBitmap);
		
	}
	private void LoadStatus(){
		
		intent=getIntent();
		b=intent.getExtras();
		String add=b.getString("位置");
		textView_Address.setText(MidData.IsChinese==1?"位置："+add:"Position:"+add);
		num=b.getInt("通道号");
		if(MidData.mTestParameters[num].aisleNum==null||MidData.mTestParameters[num].aisleNum.equals(""))
		{
			editText_SampleNum.setText("");
		}
		else
		{
			editText_SampleNum.setText(MidData.mTestParameters[num].aisleNum);
		}
		if(MidData.mTestParameters[num].aisleStatus==-1)
		{
			textView_Result.setText(MidData.IsChinese==1?"结果：   ":"Test Result:");
			textView_Status.setText(MidData.IsChinese==1?"状态：通道故障":"Status:Channel fault");
			textView_Height.setText(MidData.IsChinese==1?"高度：         ":"Height: ");
			textView_Process.setText(MidData.IsChinese==1?"进度：0/0":"Progress:0/0");
		}
		
		else
		{
			if(MidData.isDebugging==2)
			{
				if(MidData.mTestParameters[num].testStatus==0)
				{
					textView_Result.setText(MidData.IsChinese==1?"结果：   ":"Test Result:");
					textView_Status.setText(MidData.IsChinese==1?"状态：无样本":"Status:No sample");
					textView_Height.setText(MidData.IsChinese==1?"高度：":"Height: ");
					//textView_Process.setText("进度：   "+"0/0");
				}
				else if(MidData.mTestParameters[num].testStatus==1)
				{
					textView_Result.setText(MidData.IsChinese==1?"结果：":"Test Result:");
					textView_Status.setText(MidData.IsChinese==1?"状态：发现样本":"Status:Found samples");
					textView_Height.setText(MidData.IsChinese==1?"高度：":"Height: ");
					//textView_Process.setText("进度：   "+Integer.toString(MidData.mTestParameters[num].aisleTestTime)+"/"+sumTime);
				}
				else if(MidData.mTestParameters[num].testStatus==2)
				{
					textView_Result.setText(MidData.IsChinese==1?"结果：":"Test Result:");
					textView_Status.setText(MidData.IsChinese==1?"状态：正在检测":"Status:Testing");
					if(MidData.mTestParameters[num].aisleTestTime>0)
					  textView_Height.setText(MidData.IsChinese==1?"高度："+fnum1.format(MidData.mTestParameters[num].height_RunAllTime):"Height:"+fnum1.format(MidData.mTestParameters[num].height_RunAllTime));
					else
					   textView_Height.setText(MidData.IsChinese==1?"高度：":"Height: ");
					//textView_Process.setText("进度：   "+Integer.toString(MidData.mTestParameters[num].aisleTestTime)+"/"+sumTime);
				}
				else if(MidData.mTestParameters[num].testStatus==3)
				{
						textView_Status.setText(MidData.IsChinese==1?"状态：检测完成":"Status:Test complete");
						textView_Height.setText(MidData.IsChinese==1?"高度："+fnum1.format(MidData.mTestParameters[num].aisleHeght[MidData.mTestParameters[num].aisleTestTime-1]):"Height:"+fnum1.format(MidData.mTestParameters[num].aisleHeght[MidData.mTestParameters[num].aisleTestTime-1]));
						textView_Result.setText(MidData.IsChinese==1?"结果：":"Test Result:");					
				}
				textView_Process.setText(MidData.IsChinese==1?"进度：正在拷机":"Progress:Torture testing");
			}
			else
			{
				new DecimalFormat("##0.0");
			    String passtime = Integer
				    .toString((int) ((((double) MidData.mTestParameters[num].aisleTestTime * (double) MidData.mSetParameters.RunPeriodSet)) / 60.0));
			    if (((int) ((((double) 10 * (double) MidData.mTestParameters[num].aisleTestTime * (double) MidData.mSetParameters.RunPeriodSet)) / 60.0)) % 10 >= 5)
			    {
				passtime = Integer
					.toString((int) ((((double) MidData.mTestParameters[num].aisleTestTime * (double) MidData.mSetParameters.RunPeriodSet)) / 60.0 + 1));
			    }
			    // ProcessTextView.setText(Integer.toString((int)((((double)MidData.timesOfTest[t]*(double)MidData.PulsePeriodSet))/60.0))+"/"+Integer.toString((int)((((double)MidData.DirvceRunTestTime*(double)MidData.PulsePeriodSet))/60.0)));//
			    String sumTime;
			    String result;
			    double sum_yushu = ((MidData.mTestParameters[num].aisleTestTimeCount) * 10) % 10;
			    if (sum_yushu >= 5)
				sumTime = Integer
					.toString((int) (MidData.mTestParameters[num].aisleTestTimeCount) + 1);
			    else
				sumTime = Integer
					.toString((int) (MidData.mTestParameters[num].aisleTestTimeCount));
			    if(MidData.IsESRSelect[num])
			    {
			    	if(Double.parseDouble(MidData.mTestParameters[num].ERSResult)>160)
			    	{
			    		result="160";
			    	}
			    	else if(Double.parseDouble(MidData.mTestParameters[num].ERSResult)<1)
			    	{
			    		result="1";
			    	}
			    	else
			    	{
			    		/*if(MidData.m_resultInt==1)
			    		{
			    			if((Double.parseDouble(MidData.mTestParameters[num].ERSResult)*10)%10<5)
			    			{
			    				result=Integer.toString((int)Double.parseDouble(MidData.mTestParameters[num].ERSResult));
			    			}
			    			else
			    			{
			    				result=Integer.toString((int)(Double.parseDouble(MidData.mTestParameters[num].ERSResult)+1));
			    			}
			    		}
			    		else
			    		{
			    			result=MidData.mTestParameters[num].ERSResult;
			    		}*/
			    		result=MidData.mTestParameters[num].ERSResult;
			    		//reult=MidData.mTestParameters[num].ERSResult;
			    	}
				    
			    }
			    else
			    {
			    	System.out.println(MidData.mTestParameters[num].hematResult+"----yj---res---");
			    	/*if(MidData.m_resultInt==1)
		    		{
		    			if((int)(Double.parseDouble(MidData.mTestParameters[num].hematResult)*10)%10<5)
		    			{
		    				result=fnum.format(Double.parseDouble(MidData.mTestParameters[num].hematResult));
		    			}
		    			else
		    			{
		    				result=fnum.format((Double.parseDouble(MidData.mTestParameters[num].hematResult)+1));
		    			}
		    		}
		    		else
		    		{
		    			result=MidData.mTestParameters[num].hematResult;
		    		}*/
			    	result=MidData.mTestParameters[num].hematResult;
			    }
			    
			if(MidData.mTestParameters[num].testStatus==0)
			{
				textView_Result.setText(MidData.IsChinese==1?"结果：":"Test Result:");
				textView_Status.setText(MidData.IsChinese==1?"状态：无样本":"Status:No sample");
				textView_Height.setText(MidData.IsChinese==1?"高度：":"Height: ");
				textView_Process.setText(MidData.IsChinese==1?"进度：0/0":"Progress:0/0");
			}
			else if(MidData.mTestParameters[num].testStatus==1)
			{
				textView_Result.setText(MidData.IsChinese==1?"结果：":"Test Result:");
				textView_Status.setText(MidData.IsChinese==1?"状态：发现样本":"Status:Found samples");
				textView_Height.setText(MidData.IsChinese==1?"高度：":"Height: ");
				textView_Process.setText(MidData.IsChinese==1?"进度："+Integer.toString(MidData.mTestParameters[num].aisleTestTime)+"/"+sumTime:"Progress:"+Integer.toString(MidData.mTestParameters[num].aisleTestTime)+"/"+sumTime);
			}
			else if(MidData.mTestParameters[num].testStatus==2)
			{
				
				textView_Result.setText(MidData.IsChinese==1?"结果：":"Test Result:");
				textView_Status.setText(MidData.IsChinese==1?"状态：正在检测":"Status:Testing");
				if(MidData.mTestParameters[num].aisleTestTime>0)
				  textView_Height.setText(MidData.IsChinese==1?"高度："+fnum1.format(MidData.mTestParameters[num].aisleHeght[MidData.mTestParameters[num].aisleTestTime-1]):"Height:"+fnum1.format(MidData.mTestParameters[num].aisleHeght[MidData.mTestParameters[num].aisleTestTime-1]));
				else
				   textView_Height.setText(MidData.IsChinese==1?"高度：":"Height: ");
				textView_Process.setText(MidData.IsChinese==1?"进度："+Integer.toString(MidData.mTestParameters[num].aisleTestTime)+"/"+sumTime:"Progress:"+Integer.toString(MidData.mTestParameters[num].aisleTestTime)+"/"+sumTime);
			}
			else if(MidData.mTestParameters[num].testStatus==3)
			{
					if(!MidData.IsESRSelect[num]&&MidData.mTestParameters[num].hematExcetion==1)
					{
						textView_Status.setText(MidData.IsChinese==1?"状态：检测异常":"Status:Measurement abnormal");
						textView_Height.setText(MidData.IsChinese==1?"高度："+fnum1.format(MidData.mTestParameters[num].aisleHeght[MidData.mTestParameters[num].aisleTestTime-1]):"Height:"+fnum1.format(MidData.mTestParameters[num].aisleHeght[MidData.mTestParameters[num].aisleTestTime-1]));
						textView_Process.setText(MidData.IsChinese==1?"进度：":"Progress:");
						textView_Result.setText(MidData.IsChinese==1?"结果：":"Test Result:");
					}
					else
					{
						textView_Status.setText(MidData.IsChinese==1?"状态：检测完成":"Status:Test complete");
						textView_Height.setText(MidData.IsChinese==1?"高度："+fnum1.format(MidData.mTestParameters[num].aisleHeght[MidData.mTestParameters[num].aisleTestTime-1]):"Height:"+fnum1.format(MidData.mTestParameters[num].aisleHeght[MidData.mTestParameters[num].aisleTestTime-1]));
						textView_Process.setText(MidData.IsChinese==1?"进度："+Integer.toString(MidData.mTestParameters[num].aisleTestTime)+"/"+sumTime:"Progress:"+Integer.toString(MidData.mTestParameters[num].aisleTestTime)+"/"+sumTime);
						if((MidData.mTestParameters[num].aisleTestTime<Integer.parseInt(sumTime))&&(result.equals("0.0")||result.equals("0.00")||result.equals("0")||(result.equals("1")&&!MidData.mTestParameters[num].aisleTestFinished)))
							textView_Result.setText(MidData.IsChinese==1?"结果：":"Test Result:");
						else
						{
							if(MidData.mSetParameters.SetNumWay==0)							
								textView_Result.setText(MidData.IsChinese==1?"结果："+result:"Test Result:"+result);
							else
								if(MidData.IsESRSelect[num])
									textView_Result.setText(MidData.IsChinese==1?"结果："+result:"Test Result:"+result);
								else
									textView_Result.setText(MidData.IsChinese==1?"结果：请扫码...":"Test Result:Please scan barcode...");
						}
						
					}
					
				
			}
			else if(MidData.mTestParameters[num].testStatus==4)
			{
				textView_Result.setText(MidData.IsChinese==1?"结果：":"Test Result:");
				textView_Status.setText(MidData.IsChinese==1?"状态：标本超限":"Status:Sample overrun");
				textView_Height.setText(MidData.IsChinese==1?"高度：":"Height:");
				textView_Process.setText(MidData.IsChinese==1?"进度：":"Progress:");
			}
			else if(MidData.mTestParameters[num].testStatus==5)
			{
				textView_Result.setText(MidData.IsChinese==1?"结果：":"Test Result:");
				textView_Status.setText(MidData.IsChinese==1?"状态：标本不足":"Status:Sample shortage ");
				textView_Height.setText(MidData.IsChinese==1?"高度：":"Height:");
				textView_Process.setText(MidData.IsChinese==1?"进度：":"Progress:");
			}
			if(MidData.mTestParameters[num].aisleNumShow!=null)
			editText_SampleNum.setText(MidData.mTestParameters[num].aisleNumShow);
			}
			 
		}
	}
	class button_PrintClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.mTestParameters[num].aisleTestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"检测未完成，无法打印":"Measuring, please print later", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.mTestParameters[num].aisleNum==null||MidData.mTestParameters[num].aisleNum.equals(""))
		    {
		    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"未编号，请为样本扫码编号":"no number,please scanning num", Toast.LENGTH_LONG).show();
		    	return;
		    }
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在准备打印，请稍候...":"Ready to print, please wait", Toast.LENGTH_LONG).show();
		    
			    new PrintfDataByk("lock", 1).start();
		}
		
	}
	public class PrintfDataByk extends Thread
    {

	private String lock;
	private int id;

	public PrintfDataByk(String lk, int id)
	{
	    // TODO Auto-generated constructor stub
	    this.lock = lk;
	    this.id = id;
	}

	@Override
	public void run()
	{
	    super.run();
	    synchronized (lock)
	    {
	    	//System.out.println(MidData.mTestParameters[num].saveDatabaseID+"-----ID---"+num);
	    	//if(MidData.mTestParameters[num].saveDatabaseID!=0)
	    	//{
	    		PrintData();//cursor	    		
	    	//}   
		
	    }

	}

    }
	class ShowTipThread extends Thread{
    	String str;
    	void ShowTipThread(String str_)
    	{
    		this.str=str_;
    	}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			runOnUiThread(new Runnable()
			{
			    public void run()
			    {
			    	Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();					 
			    }
			});
		}
    	
    }
	private void PrintData()//Cursor cursor
    {
		Time t=new Time();
		t.setToNow();
	    //double day = cursor.getDouble(cursor.getColumnIndex("date"));
	    int year = t.year;//(int) (day / (Math.pow(10, 4)));
	    int month = t.month+1;//(int) ((day - year * (Math.pow(10, 4))) / (Math.pow(10,
		    //2)));
	    int date = t.monthDay;//(int) (day - year * (Math.pow(10, 4)) - month
		    //* (Math.pow(10, 2)));
	    MidData.isPrintDateStr = Integer.toString(year) + "-"
		    + Integer.toString(month) + "-" + Integer.toString(date);
	    
	    MidData.isPrintNumStr = MidData.mTestParameters[num].aisleNum;//cursor.getString(cursor
		    //.getColumnIndex("xuhao"));
	    String resourcevalues = MidData.mTestParameters[num].aislePastTestData;//cursor.getString(cursor
		    //.getColumnIndex("resourcedata"));
	    if(!MidData.IsESRSelect[num])
	    {
	    	//System.out.println(MidData.mTestParameters[num].aisleNum+"----curve--");
	    	if(MidData.sqlOp.XueChenTestFinished(MidData.mTestParameters[num].aisleNum))
	    	{
	    		
	    		//Cursor cursor = null;
	    		String rs[] = MidData.sqlOp.selectSingleHistoryByNumByK(MidData.mTestParameters[num].aisleNum);
				resourcevalues = rs[0];
			    if(rs[0]==null||rs[0].equals(""))
			    {
			    	ShowTipThread mShowTipThread=new ShowTipThread();
			    	mShowTipThread.ShowTipThread(MidData.IsChinese==1?"获取原始数据失败，请稍后重新打印":"Get Data faile,please reprint later!");
			    	mShowTipThread.start();
			    	//Toast.makeText(getApplicationContext(), "获取原始数据失败，请稍后重新打印", Toast.LENGTH_LONG).show();
			    	return;
			    }
				MidData.isPrintXueChenValueStr = rs[1];
			    // 根据实际需要转换为可用结果
				MidData.isPrintXueChenValueStr=CalculateFormula.GetERSTestResult(resourcevalues, "0", MidData.isDebugging, Integer.toString(MidData.mSetParameters.ERSTestTime), Double.parseDouble(rs[2]));//CalculateFormula.ResourceResultToShow(MidData.isPrintXueChenValueStr, "0", MidData.isDebugging, Integer.toString(MidData.mSetParameters.ERSTestTime), Double.parseDouble(rs[2]));
			    
	    	}
	    	else
	    	{	    		
	    		MidData.isPrintXueChenValueStr="未检测";
	    		//System.out.println(MidData.isPrintXueChenValueStr+"------XC-----vvv");
	    	}
	    }
	    else
	    {
	    	MidData.isPrintXueChenValueStr = MidData.mTestParameters[num].ERSResult;
	    }
	    
	    if (MidData.isPrintXueChenValueStr.equals("未检测")||MidData.isPrintXueChenValueStr.equals("Untest")||MidData.isPrintXueChenValueStr.equals("")||MidData.isPrintXueChenValueStr.equals("0.0")||MidData.isPrintXueChenValueStr==null)
	    {
		MidData.isPrintXueChenValueStr = "未检测";
	    }
	   
	   if(MidData.mTestParameters[num].hematResult==null||MidData.mTestParameters[num].hematResult.equals("0.0")||MidData.mTestParameters[num].hematResult.equals("0.00"))
	    	MidData.isPrintYaJiValueStr ="未检测";
	    else
	    	MidData.isPrintYaJiValueStr = MidData.mTestParameters[num].hematResult;//ursor.getString(cursor
		    
	    String xcType = Integer.toString(MidData.mSetParameters.ERSTestTime);
	    String plusPeriod = Integer.toString(MidData.mSetParameters.RunPeriodSet);
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
	    		MidData.sOpForPrint.PrintChartByk(resourcevalues, mul, xcType,MidData.mTestParameters[num].averageTem);
	    }

	    MidData.sOpForPrint.LoadEndThePrint();
	    MidData.sOpForPrint.LoadEndThePrint();
	    if (MidData.mSetParameters.PrintSet==1)
	    {
			try
			{
			    Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
	    }

    }
	public long GetSecond(Time val)
    {
    	DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Time nn=new Time();
    	nn.setToNow();
    	Date d1,d2;
    	long ttSS=0;
		try {
			String D1S=Integer.toString(val.year)+"-"+Integer.toString(val.monthDay)+"-"+Integer.toString(val.monthDay)+" "+Integer.toString(val.hour)+":"+Integer.toString(val.minute)+":"+Integer.toString(val.second);
			String D2S=Integer.toString(nn.year)+"-"+Integer.toString(nn.monthDay)+"-"+Integer.toString(nn.monthDay)+" "+Integer.toString(nn.hour)+":"+Integer.toString(nn.minute)+":"+Integer.toString(nn.second);
			d1 = df.parse(D1S);
			d2 = df.parse(D2S);
	    	long diff=d2.getTime()-d1.getTime();
	    	ttSS=diff/1000;
	    	
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return ttSS;
    }
	class button_UpLoadClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.mTestParameters[num].aisleTestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"检测未完成，无法上传":"Detecting unfinished, cannot upload", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.mTestParameters[num].aisleNum==null||MidData.mTestParameters[num].aisleNum.equals(""))
		    {
		    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"未编号，请为样本扫码编号":"no number,please scanning num", Toast.LENGTH_LONG).show();
		    	return;
		    }
			button_UpLoad.setEnabled(false);
		    if (MidData.m_UpToSA)
		    {
			OpenSerialPortForUpLoadDataToSABySD100();
			
			if (MidData.m_OutputStream_SA != null)
			{			   
			    new UpData2PcInSD100Byk().start();
			}
			else
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"连接SA失败，请确认连接":"Connect SA failed, please check connection", Toast.LENGTH_LONG).show();
				button_UpLoad.setEnabled(true);
				return;
			}
		    }
		    else if (MidData.m_UpToLIS_net||MidData.m_UpToLIS_ser)
		    {
		    	if(MidData.m_UpToLIS_net)
		    	{		    	
		    		if (MidData.fd <0||MidData.LisNetConnetTime==null||GetSecond(MidData.LisNetConnetTime)>3600)
		    			OpenSerialPortForUpLoadDataToLIS();
				    if (MidData.fd>0)
					{
					    new UpData2PcByk().start();
					}
					else
					{
						Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"连接LIS失败，请确认网络连接":"Connect LIS failed, please check network connection", Toast.LENGTH_LONG).show();
						button_UpLoad.setEnabled(true);
						return;
					}
		    	}
		    	else if(MidData.m_UpToLIS_ser)
		    	{		    		
		    		OpenSerialPortForUpLoadDataToSABySD100();
		    		if (MidData.m_OutputStream_SA != null)
					{
					    new UpData2PcByk().start();
					}
					else
					{
						Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"连接失败，请确认连接":"Connect LIS failed, please check network connection", Toast.LENGTH_LONG).show();
						button_UpLoad.setEnabled(true);
						return;
					}
		    	}
		    	
		    }
		}
		
	}
	public class UpData2PcByk extends Thread
    {

	public UpData2PcByk()
	{
	}

	@Override
	public void run()
	{
	    super.run();
		 
		//Cursor cursor=null;
	    //new UpData2PcInSD100Byk("lock", i).start();
	    //cursor = MidData.sqlOp.selectResourceDataByID(cursor,Long
		//	.toString(MidData.mTestParameters[num].saveDatabaseID));
	    UpLaodData2PC();//cursor
		if(MidData.LisUploadLine)
		{
			UploadLine2PC(MidData.mTestParameters[num].aisleNum);
		}
		 Looper.prepare();
		// KyleSocketClass.DeInitPort(MidData.fd);
		 //MidData.fd=-1;
		 Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"成功发送数据到LIS":"Send data to LIS successfully", Toast.LENGTH_LONG).show();
		 mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
		 Looper.loop();
		 
		 //MidData.sOpForSA.SendToSAPortClose();
	}

    }
	private void UploadLine2PC(String Num)
    {
    	int testID=Integer.parseInt(Num);
    	MidData.testDataConvert=new TestDataConvert();
    	String str=MidData.testDataConvert.ConvertLineForLisByNum(testID);
    	if(str.equals(""))return;
    	str=">QX,"+Num+","+str;
    	byte[] Data=new byte[str.length()];
    	byte[] Buff=new byte[str.length()+6];
    	Data=str.getBytes();
    	int Length=0;
    	for(int i=0;i<Data.length;i++)
    	{
    		Buff[Length++]=Data[i];
    	}
    	
    	int Crc = MidData.sOp.GetCrc(Data, Length);    	
    	Buff[Length++] = (byte) MidData.sOp.Hex2Ascii((Crc & 0xF000) >> 12);
    	Buff[Length++] = (byte) MidData.sOp.Hex2Ascii((Crc & 0x0F00) >> 8);
    	Buff[Length++] = (byte) MidData.sOp.Hex2Ascii((Crc & 0x00F0) >> 4);
    	Buff[Length++] = (byte) MidData.sOp.Hex2Ascii(Crc & 0x000F);
    	Buff[Length++] = 0x0D;
    	Buff[Length++] = 0x0A;
    	int times=0;
	    int tp=-1;
    	if(MidData.m_UpToLIS_net)
	    {
		    while(times<3)
		    {
		    	if(tp<0)
		    		tp= KyleSocketClass.SendData(Buff, Length, MidData.fd);
		    	else
		    		break;
		    	times++;
		    }
	    }
	    else if(MidData.m_UpToLIS_ser)
	    {
	    	
	    	try {
				MidData.m_OutputStream_SA.write(Buff, 0, Length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
    }
	private void UpLaodData2PC()//Cursor cursor
    {
	//while (cursor.moveToNext())
	//{
		 String XC ="";
		 String YJ="";
		 if(!MidData.IsESRSelect[num]){
			 if(MidData.sqlOp.XueChenTestFinished(MidData.mTestParameters[num].aisleNum))
		    	{
		    		
		    		//Cursor cursor = null;
		    		String rs[] = MidData.sqlOp.selectSingleHistoryByNumByK(MidData.mTestParameters[num].aisleNum);
					//resourcevalues = rs[0];
				    if(rs[0]==null||rs[0].equals(""))
				    {
				    	ShowTipThread mShowTipThread=new ShowTipThread();
				    	mShowTipThread.ShowTipThread(MidData.IsChinese==1?"获取原始数据失败，请稍后重新打印。":"Failed to get data,please print again later.");
				    	mShowTipThread.start();
				    	//Toast.makeText(getApplicationContext(), "获取原始数据失败，请稍后重新打印", Toast.LENGTH_LONG).show();
				    	return;
				    }
					XC=CalculateFormula.GetERSTestResult(MidData.mTestParameters[num].aislePastTestData, "0", MidData.isDebugging, Integer.toString(MidData.mSetParameters.ERSTestTime), Double.parseDouble(rs[2]));//.ResourceResultToShow(rs[1], "0", MidData.isDebugging, Integer.toString(MidData.mSetParameters.ERSTestTime), Double.parseDouble(rs[2]));
					YJ = MidData.mTestParameters[num].hematResult;
		    	}
		    	else
		    	{	    		
		    		XC="NULL";
		    		YJ=MidData.mTestParameters[num].hematResult;
		    	}
		 }
		 else
		 {
			 XC = MidData.mTestParameters[num].ERSResult;
			 YJ = "NULL";
		 }
		byte[] Data2 = null;
	    byte[] Data3 = null;
	    //String XC = MidData.mTestParameters[num].ERSResult;//cursor.getString(cursor.getColumnIndex("xuechendata"));
	    if (XC.equals("未检测")||XC.equals("Untest")||XC==null||XC.equals(""))
	    {
		XC = "NULL";
	    }	    
	    Data2 = XC.getBytes();
	   // String YJ = MidData.mTestParameters[num].hematResult;//cursor.getString(cursor.getColumnIndex("yajidata"));
	    if (YJ.equals("未检测")||YJ.equals("Untest")||YJ==null||YJ.equals(""))
	    {
		YJ = "NULL";
	    }	    
	    Data3 = YJ.getBytes();
	    byte[] Data1 = MidData.mTestParameters[num].aisleNum.getBytes();//cursor.getString(cursor.getColumnIndex("xuhao"))
		    //.getBytes();
	    byte[] data5 = new byte[200];

	    int Length = 0;
	    data5[Length++] = '>';
	    for (int i = 0; i < Data1.length; i++)
		data5[Length++] = Data1[i];
	    data5[Length++] = ',';
	    for (int i = 0; i < Data2.length; i++)
		data5[Length++] = Data2[i];
	    data5[Length++] = ',';
	    for (int i = 0; i < Data3.length; i++)
		data5[Length++] = Data3[i];
	    data5[Length++] = ',';
	    int Crc = MidData.sOp.GetCrc(data5, Length);
	    data5[Length++] = (byte) MidData.sOp
		    .Hex2Ascii((Crc & 0xF000) >> 12);
	    data5[Length++] = (byte) MidData.sOp.Hex2Ascii((Crc & 0x0F00) >> 8);
	    data5[Length++] = (byte) MidData.sOp.Hex2Ascii((Crc & 0x00F0) >> 4);
	    data5[Length++] = (byte) MidData.sOp.Hex2Ascii(Crc & 0x000F);
	    data5[Length++] = 0x0D;
	    data5[Length++] = 0x0A;
	    int times=0;
	    int tp=-1;
	    if(MidData.m_UpToLIS_net)
	    {
	    while(times<3)
	    {
	    	if(tp<0)
	    		tp= KyleSocketClass.SendData(data5, Length, MidData.fd);
	    	else
	    		break;
	    	times++;
	    }
	    }
	    else if(MidData.m_UpToLIS_ser)
	    {
	    	byte[] data6=new byte[Length];
	    	
	    	try {
				MidData.m_OutputStream_SA.write(data5,0,Length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	   
	   // MidData.sOpForSA.SendToSAPort(data5, Length);
	//}
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
	    
	 public class UpData2PcInSD100Byk extends Thread
	    {

		public UpData2PcInSD100Byk()
		{
		}

		@Override
		public void run()
		{
		    super.run();
		    byte[] Ptr = new byte[1];
		    ClearSAPort();
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
			if(Ptr[0]==0x71)break;
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
		if(Ptr[0]!=0x71)
			    {
			    Looper.prepare();
				 Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"没有收到握手信号":"No handshake signal received", Toast.LENGTH_LONG).show();
				 mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
				 Looper.loop();
				 MidData.sOpForSA.SendToSAPortClose();
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
				if(Ptr[0]==0x73)break;
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
			 if(Ptr[0]!=0x73)
			     {
			     Looper.prepare();
				 Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"没有收到应答信号":"No response signal received", Toast.LENGTH_LONG).show();
				 mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
				 Looper.loop();
				 MidData.sOpForSA.SendToSAPortClose();
			     return;
			     }
		
					
			 		UpLaodData2PCBySD100();//cursor
					
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
			     Looper.prepare();
				 Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"没有收到结束应答信号":"No finish response singnal received", Toast.LENGTH_LONG).show();
				 mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
				 Looper.loop();
				 MidData.sOpForSA.SendToSAPortClose();
			     return;
			     }
			 Looper.prepare();
			 Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"发送数据成功":"Send data successfully", Toast.LENGTH_LONG).show();
			 mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
			 Looper.loop();
			 MidData.sOpForSA.SendToSAPortClose();


		}

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
		long xx;
		long yy;
		
		String YJ;
		//while (cursor.moveToNext())
		//{
		byte[] data = new byte[9]; 
		  xx= Long.parseLong(test(MidData.mTestParameters[num].aisleNum));//cursor.getString(cursor.getColumnIndex("xuhao"))

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
		  
			 
			  
			  if(!MidData.IsESRSelect[num])
			    {
			    	if(MidData.sqlOp.XueChenTestFinished(MidData.mTestParameters[num].aisleNum))
			    	{
			    		
			    		String rs[] = MidData.sqlOp.selectSingleHistoryByNumByK(MidData.mTestParameters[num].aisleNum);
			    		 if(rs[0]==null||rs[0].equals(""))
						    {
						    	ShowTipThread mShowTipThread=new ShowTipThread();
						    	mShowTipThread.ShowTipThread(MidData.IsChinese==1?"获取原始数据失败，请稍后重新打印。":"Failed to get data,please print again later.");
						    	mShowTipThread.start();
						    	return;
						    }
						String XC = rs[1];//cursor.getString(cursor.getColumnIndex("xuechendata"));
					    
						XC=CalculateFormula.GetERSTestResult(MidData.mTestParameters[num].aislePastTestData, "0", MidData.isDebugging,Integer.toString(MidData.mSetParameters.ERSTestTime) , Double.parseDouble(rs[2]));//.ResourceResultToShow(XC, "0", MidData.isDebugging,Integer.toString(MidData.mSetParameters.ERSTestTime) , Double.parseDouble(rs[2]));
						if (XC==null||XC.equals("未检测")||XC.equals("Untest")||XC.equals(""))
					    {
						xx=0;
					    }
					    else
					    {						
					    	//xx=Integer.parseInt(XC) ;
					    	xx=CalculateFormula.StringToIntForUpLoad(XC);
					    }				    
			    	}
			    	else
			    	{	    		
			    		xx=0;
			    	}
			    	
			    	
			    	YJ=MidData.mTestParameters[num].hematResult;
			    }
			    else
			    {
			    	xx=CalculateFormula.StringToIntForUpLoad(MidData.mTestParameters[num].ERSResult);
			    	//xx=Integer.parseInt(MidData.mTestParameters[num].ERSResult);
			    	YJ="";
			    }
			  	//xx=220;
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
				  
				  
				  
				  long xxYI;
				  long yyYJ;
				    if (YJ.equals("")||YJ==null||YJ.equals("未检测")||YJ.equals("Untest"))
				    {
				    	xxYI=0;
				    }
				    else
				    {
				    	xxYI= (int) Double.parseDouble(YJ);
				    	if((Double.parseDouble(YJ)*100)%100<45)
				    		xxYI= (int) Double.parseDouble(YJ);// 根据实际需要转换为可用结果
				    	else
				    		xxYI= (int) Double.parseDouble(YJ)+1;
				    }
				    
				    yyYJ=xxYI/100;
					  if(yyYJ>255)yyYJ=255;
					  data[6]=(byte)yyYJ;
					  xxYI=xxYI-yyYJ*100;
				  
					  yyYJ=xxYI/10;
					  if(yyYJ>255)yyYJ=255;
					  data[7]=(byte)yyYJ;
					  xxYI=xxYI-yyYJ*10;
					  data[8]=(byte)xxYI;
					  MidData.sOpForSA.SendToSAPort(data, data.length);
			
	    }
	 public void OpenSerialPortForUpLoadDataToLIS()
	    {
		 if(MidData.fd>0)
	    	{
	    		
	    		KyleSocketClass.DeInitPort(MidData.fd);
	    		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		MidData.fd = KyleSocketClass.InitPort(MidData.IpStr, MidData.Port);// MidData.sOp.OpenSerialPort();
		MidData.LisNetConnetTime=new Time();
		MidData.LisNetConnetTime.setToNow();
		
	    }
	public void OpenSerialPortForUpLoadDataToSA()
    {
	MidData.portForSA = MidData.sOpForSA.OpenSerialPortForSA();// MidData.sOp.OpenSerialPort();
	if (MidData.portForSA == null)
	{
	    Toast.makeText(CurveActivity.this, MidData.IsChinese==1?"连接SA失败，无法上传数据到SA系统":"SA connection fails, can not upload data to the SA system",
		    Toast.LENGTH_SHORT).show();
	    return;
	}
	MidData.m_OutputStream_SA = MidData.portForSA.getOutputStream();
	MidData.m_InputStream_SA = MidData.portForSA.getInputStream();
    }
	public void OpenSerialPortForUpLoadDataToSABySD100()
    {
	// sPortForSA = MidData.sOpForSA.OpenSerialPortForSAOpenBySD100();
	MidData.portForSA = MidData.sOpForSA.OpenSerialPortForSAOpenBySD100();// MidData.sOp.OpenSerialPort();
	if (MidData.portForSA == null)
	{
	    Toast.makeText(CurveActivity.this,
	    		MidData.IsChinese==1?"连接SA失败，无法上传数据到SA系统(按SD100)":"SA connection fails, can not upload data to the SA system(SD100)", Toast.LENGTH_SHORT).show();
	    return;
	}
	MidData.m_OutputStream_SA = MidData.portForSA.getOutputStream();
	MidData.m_InputStream_SA = MidData.portForSA.getInputStream();
    }
	class button_DeleteClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.mTestParameters[num].aisleTestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"检测未完成，无法删除":"Measuring unfinished, cannot be deleted", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.mTestParameters[num].aisleNum==null||MidData.mTestParameters[num].aisleNum.equals(""))
		    {
		    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"未编号，请为样本扫码编号":"no number,please scanning num", Toast.LENGTH_LONG).show();
		    	return;
		    }
		    final AlertDialog isExit = new AlertDialog.Builder(arg0.getContext())
			    .create();
		    // 对话框标题
		    isExit.setTitle(MidData.IsChinese==1?"提示":"Notice");
		    // 对话框消息
		    isExit.setMessage(MidData.IsChinese==1?"数据删除后不能恢复，确认删除?":"Data can not be recovered after deleted, please confirm");
		    // 实例化对话框上的按钮点击事件监听
		    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
		    {
			public void onClick(DialogInterface dialog, int which)
			{
			    switch (which)
			    {
			    case AlertDialog.BUTTON1:// "确认"按钮退出程序
				// ///////////////////////////////////////////
				
				String[] idsStr = {Long.toString(MidData.mTestParameters[num].saveDatabaseID)};
				MidData.exSqlOk=false;
				MidData.sqlOp.DeleteById("DS1000ResourceTable", idsStr,1);					
				// ///////////////////////////
				if(MidData.exSqlOk)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"删除成功":"Deleted successfully", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"删除失败":"Delete failed", Toast.LENGTH_LONG).show();
				}
				break;
			    case AlertDialog.BUTTON2:// "取消"第二个按钮取消对话框
				break;
			    default:
				break;
			    }
			}
		    };
		    // 注册监听
		    isExit.setButton(MidData.IsChinese==1?"确定":"Comfirm", listener);
		    isExit.setButton2(MidData.IsChinese==1?"取消":"Cancel", listener);
		    // 显示对话框
		    isExit.show();
		}
		
	}
	class button_BackClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
	 public long GetTimeAfter2Period(){
		 Calendar cal=Calendar.getInstance();
		 int twoR=MidData.mSetParameters.RunPeriodSet*2;
		 cal.add(Calendar.SECOND, twoR);
	     return cal.getTimeInMillis();
	    }
	 class buttonSampleNumQualityControlClickListener implements OnClickListener
	 {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"自检未完成":"Self-test is not completed", Toast.LENGTH_LONG).show();
				return;
			}
			if (MidData.SelAddress == 101)
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请选择通道":"Please select the channel",
				Toast.LENGTH_LONG).show();
			return;
		    }
		    if(MidData.mTestParameters[num].aisleStatus==-1)
		    {
		    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"通道故障":"The channel error",
						Toast.LENGTH_LONG).show();
					return;
		    }
		    int nums=MidData.sqlOp.GetQualityNum();
		    if(MidData.sqlOp.UpdateQualityNum(nums))
			{
		    	MidData.mTestParameters[num].aisleSampleNumTime=GetTimeAfter2Period();		    
				MidData.mTestParameters[num].ReDetect = true;
				MidData.mTestParameters[num].aisleNum=Integer.toString(nums);
				MidData.mTestParameters[num].aisleNumShow = MidData.mTestParameters[num].aisleNum;
				new AlertDialog.Builder(CurveActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(
			    		 MidData.IsChinese==1?"本质控编号为"+Integer.toString(nums)+"，设置成功！":"The Quality control num is"+Integer.toString(nums)+", setting Success")
			    .setPositiveButton(
			    		MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{
						CurveActivity.this.finish();
					}
				    }).show();	
			}		
		}		 
	 }
	 class buttonSampleNumReTestClickListener implements OnClickListener
	 {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"自检未完成":"Self-test is not completed", Toast.LENGTH_LONG).show();
				return;
			}
			int l=editText_SampleNum.getText().toString().trim().length();
			String fmStr="##";
			for(int i=0;i<l;i++)
				fmStr+="0";
			
			String numtext=editText_SampleNum.getText().toString().trim();
		    if (numtext.equals(""))
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请输入有效编号":"Please enter a valid ID",
				Toast.LENGTH_LONG).show();
			return;
		    }
		    if (MidData.SelAddress == 101)
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请选择通道":"Please select the channel",
				Toast.LENGTH_LONG).show();
			return;
		    }
		    if(MidData.mTestParameters[num].aisleStatus==-1)
		    {
		    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"通道故障":"The channel error",
						Toast.LENGTH_LONG).show();
					return;
		    }
		    boolean f=false;
		    for(int i=0;i<100;i++)
		    {
		    	if(!MidData.mTestParameters[i].aisleNum.equals("")&&MidData.mTestParameters[i].aisleNum.equals(numtext))
		    	{
		    		f=true;
		    		break;
		    	}
		    }
		    if(f)
		    {
		    	new AlertDialog.Builder(CurveActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(
			    		MidData.IsChinese==1?"编号："+ numtext+"正在检测，请对本样本重新编号":"number"+numtext+"Measuring, please renumber")
			    .setPositiveButton(
			    		MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{
											
					}
				    }).setCancelable(false)
			    .show();
    			return;
		    }
		    if (MidData.sqlOp.XueChenTestFinished(editText_SampleNum
					.getText().toString().trim())||MidData.sqlOp.YaJiTestFinished(editText_SampleNum.getText()
							.toString().trim()))
		    {
		    	MidData.mTestParameters[num].RealReDetect=true;		    	
		    }
		    else
		    {
		    	MidData.mTestParameters[num].IsNewSample = false;
		    }
		    MidData.mTestParameters[num].aisleSampleNumTime=GetTimeAfter2Period();		    
			MidData.mTestParameters[num].ReDetect = true;
			MidData.mTestParameters[num].aisleNum=numtext;
			MidData.mTestParameters[num].aisleNumShow = MidData.mTestParameters[num].aisleNum;
			
			//CurveActivity.this.finish();
			new AlertDialog.Builder(CurveActivity.this)
		    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
		    .setMessage(
		    		 MidData.IsChinese==1?"设置成功！":"Setting Success")
		    .setPositiveButton(
		    		MidData.IsChinese==1?"确定":"Comfirm",
			    new DialogInterface.OnClickListener()
			    {
				public void onClick(
					DialogInterface dialog,
					int whichButton)
				{
					CurveActivity.this.finish();
				}
			    }).show();			
		}		 
	 }
	class button_Submit_SampleNumClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"自检未完成":"Self-test is not completed", Toast.LENGTH_LONG).show();
				return;
			}
			int l=editText_SampleNum.getText().toString().trim().length();
			String fmStr="##";
			for(int i=0;i<l;i++)
				fmStr+="0";
			
			String numtext=editText_SampleNum.getText().toString().trim();
		    if (numtext.equals(""))
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请输入有效编号":"Please enter a valid ID",
				Toast.LENGTH_LONG).show();
			return;
		    }
		    if (MidData.SelAddress == 101)
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请选择通道":"Please select the channel",
				Toast.LENGTH_LONG).show();
			return;
		    }
		    			
		    boolean f=false;
		    for(int i=0;i<100;i++)
		    {
		    	if(!MidData.mTestParameters[i].aisleNum.equals("")&&MidData.mTestParameters[i].aisleNum.equals(numtext))
		    	{
		    		f=true;
		    		break;
		    	}
		    }
		    if(f)
		    {
		    	new AlertDialog.Builder(CurveActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(
			    		MidData.IsChinese==1?"编号："+ numtext+"正在检测，请对本样本重新编号":"number"+numtext+"Measuring, please renumber")
			    .setPositiveButton(
			    		MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{
						//MidData.mTestParameters[num].IsNewSample = false;
						//MidData.mTestParameters[num].ReDetect = true;
					//System.out.println(num+"---------num");
						//MidData.mTestParameters[num].aisleNumShow =""; 
						//MidData.mTestParameters[num].aisleNum="";	
						//editText_SampleNum.setText("");
						
					}
				    }).setCancelable(false)
			    .show();
    			return;
		    }
		    
		    
		    /*MidData.mTestParameters[num].aisleNumShow =""; 
			MidData.mTestParameters[num].aisleNum="";*/	
		    if (numtext.equals("样本重检"))
		    {
			new AlertDialog.Builder(arg0.getContext())
				.setTitle(MidData.IsChinese==1?"消息提示":"Notice")
				.setCancelable(false)
				.setMessage(MidData.IsChinese==1?"您确定要重检本样本？":"Retest the sample?")
				.setPositiveButton(MidData.IsChinese==1?"确定":"Comfirm",
					new DialogInterface.OnClickListener()
					{
					    public void onClick(
						    DialogInterface dialoginterface,
						    int i)
					    {

						MidData.mTestParameters[num].IsNewSample = false;
						MidData.mTestParameters[num].ReDetect = true;
						MidData.mTestParameters[num].aisleNumShow = MidData.mTestParameters[num].aisleNum;
						editText_SampleNum.setText(MidData.mTestParameters[num].aisleNumShow);
						MidData.mTestParameters[num].aisleSampleNumTime=GetTimeAfter2Period();
					    
						Toast.makeText(getApplicationContext(),
								MidData.IsChinese==1?"插入试管，开始重检":"Into the cell and began retest", Toast.LENGTH_LONG)
							.show();
						return;

					    }
					}).setNegativeButton(MidData.IsChinese==1?"取消":"Cancel", null).show();
			return;
		    }

		    /*
		     * if(SampleNumEditText.getText().toString().trim().equals("")) {
		     * Toast.makeText(getApplicationContext(), "请输入有效编号",
		     * Toast.LENGTH_LONG).show(); return; }
		     */

		    if (MidData.mSetParameters.TestType==0||(MidData.mTestParameters[num].aisleStatus == 1&&MidData.IsESRSelect[num]&&MidData.mTestParameters[num].testStatus!=0))
		    {
			if (MidData.sqlOp.XueChenTestFinished(editText_SampleNum
				.getText().toString().trim()))
			{
				final String str=numtext;
				new AlertDialog.Builder(CurveActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(
			    		MidData.IsChinese==1?"本编号已完成血沉检测，您确定需要重检？":"ESR already measured with this number, measure again?")
			    .setPositiveButton(
			    		MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{
						MidData.mTestParameters[num].aisleSampleNumTime=GetTimeAfter2Period();
					    MidData.mTestParameters[num].IsNewSample = false;
						MidData.mTestParameters[num].ReDetect = true;
						MidData.mTestParameters[num].aisleNum=str;
						MidData.mTestParameters[num].aisleNumShow = MidData.mTestParameters[num].aisleNum;
						 CurveActivity.this.finish();
					}
				    }).setNegativeButton(MidData.IsChinese==1?"取消":"Cancel", null)
			    .show();			    
			    return;
			}
			else
			{			    
				//MidData.fnum10Hemat=new DecimalFormat(fmStr);
				//MidData.fnum10LenHemat=l;
				MidData.fnum10ESR=new DecimalFormat(fmStr);
				MidData.fnum10LenESR=l;
				long ailseNum=Long.parseLong(numtext);
			    MidData.mTestParameters[num].aisleNumShow =numtext;			   			   
			    MidData.mTestParameters[num].aisleNum=numtext;			    
			    editText_SampleNum.setText(numtext);
			    MidData.mTestParameters[num].aisleSampleNumTime=GetTimeAfter2Period();
			     //MidData.mSetParameters.lastTargetNumHemat=ailseNum;
			    // 要按照输入的数字序号递加只需要直接在确认有效输入的编号后 将这个编号赋值给知道编号就可以了
			    MidData.mSetParameters.lastTargetNumERS = ailseNum+1;
			    new AlertDialog.Builder(CurveActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(
			    		 MidData.IsChinese==1?"设置成功！":"Setting Success")
			    .setPositiveButton(
			    		MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{
						CurveActivity.this.finish();
					}
				    }).show();
			}
		    }
		    else
		    {
			if (MidData.sqlOp.YaJiTestFinished(editText_SampleNum.getText()
				.toString().trim()))
			{
				final String str=numtext;
				new AlertDialog.Builder(CurveActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(
			    		MidData.IsChinese==1?"本编号已完成压积检测，您确定需要重检？":"HCT already measured with this number, measure again?")
			    .setPositiveButton(
			    		MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{
						MidData.mTestParameters[num].IsNewSample = false;
						MidData.mTestParameters[num].ReDetect = true;
						MidData.mTestParameters[num].aisleNum=str;
						MidData.mTestParameters[num].aisleNumShow = MidData.mTestParameters[num].aisleNum;
						MidData.mTestParameters[num].aisleSampleNumTime=GetTimeAfter2Period();
					    CurveActivity.this.finish();
					}
				    }).setNegativeButton(MidData.IsChinese==1?"取消":"Cancel", null)
			    .show();
			    
			    return;
			}
			else
			{
				MidData.fnum10Hemat=new DecimalFormat(fmStr);
				MidData.fnum10LenHemat=l;
				//MidData.fnum10ESR=new DecimalFormat(fmStr);
				//MidData.fnum10LenESR=l;
				
				long ailseNum=Long.parseLong(numtext);
			    MidData.mTestParameters[num].aisleNumShow = numtext;
			    MidData.mTestParameters[num].aisleNum=numtext;			    
			    editText_SampleNum.setText(numtext);
			    MidData.mTestParameters[num].aisleSampleNumTime=GetTimeAfter2Period();
			    //MidData.mSetParameters.lastTargetNumERS=ailseNum+1;
			    
			    // 要按照输入的数字序号递加只需要直接在确认有效输入的编号后 将这个编号赋值给知道编号就可以了
			    MidData.mSetParameters.lastTargetNumHemat = ailseNum+1;
			    
			    
			    new AlertDialog.Builder(CurveActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(
			    		 MidData.IsChinese==1?"设置成功！":"Setting Success")
			    .setPositiveButton(
			    		MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{
						CurveActivity.this.finish();
					}
				    }).show();
			    //Toast.makeText(getApplicationContext(), MidData.IsChinese?"设置成功！":"Setting Success",
					//    Toast.LENGTH_LONG).show();
			}
		    }
			
		}		
	}
	private void LoadXML(){
		button_Submit_SampleNumZhiKong=(Button)findViewById(R.id.button_Submit_SampleNumZhiKong);
		button_Submit_SampleNumZhiKong.setOnClickListener(new buttonSampleNumQualityControlClickListener());
		button_Submit_SampleNumZhiKong.setOnTouchListener(new ButtonTouchListener());
		
		
		button_Submit_SampleNumReTest=(Button)findViewById(R.id.button_Submit_SampleNumReTest);
		button_Submit_SampleNumReTest.setOnClickListener(new buttonSampleNumReTestClickListener());
		button_Submit_SampleNumReTest.setOnTouchListener(new ButtonTouchListener());
		
		textViewNumLab=(TextView)findViewById(R.id.textViewNumLab);
		
		
		button_Submit_SampleNum=(Button)findViewById(R.id.button_Submit_SampleNum);
		button_Submit_SampleNum.setOnClickListener(new button_Submit_SampleNumClickListener());
		button_Submit_SampleNum.setOnTouchListener(new button_Submit_SampleNumTouchListener());
		button_GetScanningSampleNum=(Button)findViewById(R.id.button_GetScanningSampleNums);
		button_GetScanningSampleNum.setOnClickListener(new button_GetScanningSampleNumClickListener());
		
		button_Back=(Button)findViewById(R.id.button_Back);
		button_Back.setOnClickListener(new button_BackClickListener());
		button_Back.setOnTouchListener(new ButtonTouchListener());
		
		button_Delete=(Button)findViewById(R.id.button_Delete);
		button_Delete.setOnClickListener(new button_DeleteClickListener());
		button_Delete.setOnTouchListener(new ButtonTouchListener());
		
		button_UpLoad=(Button)findViewById(R.id.button_UpLoad);
		button_UpLoad.setOnClickListener(new button_UpLoadClickListener());
		button_UpLoad.setOnTouchListener(new ButtonTouchListener());
		
		button_Print=(Button)findViewById(R.id.button_Print);
		button_Print.setOnClickListener(new button_PrintClickListener());
		button_Print.setOnTouchListener(new ButtonTouchListener());
		
		editText_SampleNum=(EditText)findViewById(R.id.editText_SampleNum);
		editText_SampleNum.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		
		textView_Result=(TextView)findViewById(R.id.textView_Result);
		textView_Process=(TextView)findViewById(R.id.textView_Process);
		textView_Height=(TextView)findViewById(R.id.textView_Height);
		textView_Status=(TextView)findViewById(R.id.textView_Status);
		
		textView_Address=(TextView)findViewById(R.id.textView_Address);
		CurveArea=(ImageView)findViewById(R.id.CurveArea);
	}
	class button_GetScanningSampleNumClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			int run = 0;
			for (int k = 0; k < 100; k++)
		    {
			if (MidData.TestFinished)
			{
			    if (MidData.mTestParameters[k].aisleStatus == 1)
			    {
				boolean f2 = MidData.mTestParameters[k].testStatus==1||MidData.mTestParameters[k].testStatus==2 ? true
					: false;

				boolean f3 = !MidData.mTestParameters[k].aisleTestFinished ? true
					: false;
				if (f2 && f3)
				{
					//System.out.println(MidData.mTestParameters[k].testStatus+"---"+MidData.mTestParameters[k].aisleTestFinished+"----"+k);
				    run = 1;
				    break;
				}
			    }
			}
			else
			{
			    run = 2;
			    break;
			}
		    }
		    if (run == 1)
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在测试,请在检测完后扫码！":"Measuring, please scan barcode after finshed",
				Toast.LENGTH_LONG).show();
			return;
		    }
		    else if (run == 2)
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"自检未完成，请在自检完成后开始扫码！":"Please scan barcode when selfcheck finished",
				Toast.LENGTH_LONG).show();
			return;
		    }
			
			Intent intent=new Intent();
			intent.setClass(CurveActivity.this, ScannerActivity.class);
			startActivity(intent);
			finish();
		}
		
	}
	class button_Submit_SampleNumTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			Button bt=(Button)arg0;
			
			if(arg1.getAction()==MotionEvent.ACTION_DOWN)
			{
				bt.setBackgroundResource(R.drawable.samplenumsubmitbuttondown);
			}
			else if(arg1.getAction()==MotionEvent.ACTION_UP)
			{
				bt.setBackgroundResource(R.drawable.samplenumsubmitbutton);
			}
			return false;
		}
		
	}
	class ButtonTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			Button bt=(Button)arg0;
			if(arg1.getAction()==MotionEvent.ACTION_DOWN)
			{
				bt.setBackgroundResource(R.drawable.buttonbackcancel);
			}
			else if(arg1.getAction()==MotionEvent.ACTION_UP)
			{
				bt.setBackgroundResource(R.drawable.buttonbacksel);
			}
			return false;
		}
		
	}
	private void InitialChart(){
		//创建一张空白图片
		baseBitmap=Bitmap.createBitmap((int)drawGridWidth+80,(int)drawGridHeight+50,Bitmap.Config.ARGB_8888);
		//创建一张画布
		canvas=new Canvas(baseBitmap);
		//画布背景灰色
		canvas.drawColor(Color.GRAY);
		
		//初始化划线画笔
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		linePaint.setTextSize(12);
		linePaint.setTextAlign(Align.RIGHT);
		linePaint.setStyle(Style.STROKE);
		//初始化划线画笔
		chartPaint = new Paint();
		chartPaint.setAntiAlias(true);
		chartPaint.setColor(Color.parseColor("#0000cc"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		chartPaint.setTextSize(12);
		chartPaint.setTextAlign(Align.RIGHT);
		chartPaint.setStyle(Style.STROKE);
		//初始化文本画笔
		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		textPaint.setTextSize(20);
		textPaint.setTextAlign(Align.CENTER);
			//将灰色背景画上
		canvas.drawBitmap(baseBitmap, new Matrix(), backPaint);
		CurveArea.setImageBitmap(baseBitmap);
		//画背景坐标	
		canvas.drawLine(beginX, 10, beginX, drawGridHeight+beginY,
				linePaint);
		for(int i=0;i<5;i++)
		{
			float axisY_h=ySpace*i;
			//  横线
			canvas.drawLine(beginX, axisY_h+beginY, drawGridWidth+beginX+10, axisY_h+beginY,
					linePaint);
			//canvas.drawText(Integer.toString(160-i*40), 20, 20+i*ySpace, textPaint);
		}
		canvas.drawText("h", 25, 25, textPaint);
		//标定坐标
		for(int i=0;i<4;i++)
		{
			//canvas.drawCircle(beginX+(i+1)*xSpace, beginY+drawGridHeight, 2, textPaint);
			//canvas.drawText(Integer.toString(15*(i+1)), beginX+(i+1)*xSpace, beginY+drawGridHeight+20, textPaint);
			
		}
		canvas.drawText("t", beginX+(3+1)*xSpace, beginY+drawGridHeight+20, textPaint);		
				//textLabPaint.setTextSize(35);
				//canvas.drawText("X", 20+5*xSpace, 50, textLabPaint);
	}
}
