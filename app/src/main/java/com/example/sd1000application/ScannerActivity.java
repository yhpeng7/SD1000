package com.example.sd1000application;

import java.util.Calendar;

import jxl.biff.ContinueRecord;

import com.realect.sd1000.parameterStatus.IsSaveToDatabaseData;
import com.realect.sd1000.parameterStatus.MidData;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ScannerActivity extends Activity{
	private EditText editText_ScanningAddress;
	private EditText editText_ScanningNum;
	private EditText editText_ScanningStatus;
	private Button button_ScanningFinish;
	private Button button_submit;
	private boolean isOnScanningActivity;
	int XshowTime=0;
	private TextView textViewTitle;
	private TextView textViewScanningAddressLab;
	private TextView textViewScanningNumLab;
	private TextView textViewScanningStatusLab;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_scanningnum);
		LoadXML();
		isOnScanningActivity=true;
		 
		Thread updateStatus = new Thread(new Runnable()
		{
		    @Override
		    public void run()
		    {
			// TODO Auto-generated method stub
			while (isOnScanningActivity)
			{

			    if (XshowTime > 49 && !MidData.isScannerUpdated)
			    {
				XshowTime = 0;
				runOnUiThread(new Runnable()
				{
				    public void run()
				    {			
				    	if(IsSaveToDatabaseData.Add<0||IsSaveToDatabaseData.Add>99)
				    		return;
				    	editText_ScanningAddress.setText(GetAddress(IsSaveToDatabaseData.Add));
				    	editText_ScanningNum.setText(MidData.mTestParameters[IsSaveToDatabaseData.Add].aisleNum);
				    	if(MidData.mTestParameters[IsSaveToDatabaseData.Add].aisleNumShow.equals("样本重检"))
				    	{
				    		editText_ScanningStatus.setTextColor(Color.RED);
				    		editText_ScanningStatus.setText(MidData.IsChinese==1?"此编号样本已完成测试，是否需要覆盖，请确认":"Sample number existed, please confirm to cover or not");
				    		button_submit.setVisibility(View.VISIBLE);
				    	}
				    	else if(MidData.mTestParameters[IsSaveToDatabaseData.Add].aisleNumShow.equals("本编号无效（完成压积测试），请重新编号"))
				    	{
				    		editText_ScanningStatus.setTextColor(Color.RED);
				    		editText_ScanningStatus.setText(MidData.IsChinese==1?"本编号无效（完成压积测试），请重新编号":"Invalid number(HCT finished), please renumber");
				    		button_submit.setVisibility(View.INVISIBLE);
				    		MidData.mTestParameters[IsSaveToDatabaseData.Add].aisleNum="";
					    	MidData.mTestParameters[IsSaveToDatabaseData.Add].aisleNumShow="";
				    	}
				    	else if(MidData.mTestParameters[IsSaveToDatabaseData.Add].savedToDatabase)
				    	{
				    		editText_ScanningStatus.setTextColor(Color.BLACK);
				    		editText_ScanningStatus.setText(MidData.IsChinese==1?"保存成功":"Save successful");
				    		button_submit.setVisibility(View.INVISIBLE);
				    		MidData.mTestParameters[IsSaveToDatabaseData.Add].aisleNum="";
					    	MidData.mTestParameters[IsSaveToDatabaseData.Add].aisleNumShow="";
				    	}
				    	else
				    	{
				    		editText_ScanningStatus.setTextColor(Color.BLACK);
				    		editText_ScanningStatus.setText(MidData.IsChinese==1?"保存失败":"Save failed");
				    		button_submit.setVisibility(View.INVISIBLE);
				    	}					    		
				    	
				    	MidData.isScannerUpdated=true;
				    }
				});
			    }
			    try
			    {
				Thread.sleep(20);
			    }
			    catch (InterruptedException e)
			    {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			    XshowTime++;
			}
		    }
		});
		updateStatus.start();
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
	private void LoadXML(){
		textViewScanningAddressLab=(TextView)findViewById(R.id.textViewScanningAddressLab);
		textViewScanningAddressLab.setText(MidData.IsChinese==1?"位置:":"Address:");
		textViewScanningNumLab=(TextView)findViewById(R.id.textViewScanningNumLab);
		textViewScanningNumLab.setText(MidData.IsChinese==1?"编号:":"ID:");
		textViewScanningStatusLab=(TextView)findViewById(R.id.textViewScanningStatusLab);
		textViewScanningStatusLab.setText(MidData.IsChinese==1?"状态:":"State:");
		textViewTitle=(TextView)findViewById(R.id.textViewTitle);
		textViewTitle.setText(MidData.IsChinese==1?"扫码":"Scanning");
		button_submit=(Button)findViewById(R.id.button_submit);
		button_submit.setOnClickListener(new button_submitClickListener());
		button_submit.setText(MidData.IsChinese==1?"确定":"Comfirm");
		button_ScanningFinish=(Button)findViewById(R.id.button_ScanningFinish);
		button_ScanningFinish.setOnClickListener(new button_ScanningFinishClickListener());
		button_ScanningFinish.setText(MidData.IsChinese==1?"扫码完成":"Scanning finish");
		editText_ScanningAddress=(EditText)findViewById(R.id.editText_ScanningAddress);
		editText_ScanningNum=(EditText)findViewById(R.id.editText_ScanningNum);
		editText_ScanningStatus=(EditText)findViewById(R.id.editText_ScanningStatus);
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
	class button_submitClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			MidData.mTestParameters[MidData.SelAddress].aisleSampleNumTime=GetTimeAfter2Period();
			//System.out.println(MidData.mTestParameters[MidData.SelAddress].aisleNum+"----nnnnnnnnnmmmmmmmm");
			if(IsSaveToDatabaseData.TestType==0)
			{
				MidData.sqlOp
			    .DeleteDataByNum(MidData.mTestParameters[MidData.SelAddress].aisleNum);
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
		    MidData.mTestParameters[MidData.SelAddress].aisleNum="";
		    MidData.mTestParameters[MidData.SelAddress].aisleNumShow="";
		    Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"保存成功":"Save successful", Toast.LENGTH_LONG).show();
		    editText_ScanningNum.setText("");
		    editText_ScanningStatus.setTextColor(Color.BLACK);
    		editText_ScanningStatus.setText("");
    		button_submit.setVisibility(View.INVISIBLE);
    		
			}
			else
			{
				MidData.sqlOp
				.UpdateYaJiByNo(
					MidData.mTestParameters[MidData.SelAddress].aisleNum,
					IsSaveToDatabaseData.hematResult,
					IsSaveToDatabaseData.aislePastTestData,
					"1",
					GetAddress(MidData.SelAddress));
				MidData.mTestParameters[MidData.SelAddress].aisleNum="";
			    MidData.mTestParameters[MidData.SelAddress].aisleNumShow="";
			    Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"保存成功":"Save successful", Toast.LENGTH_LONG).show();
			    editText_ScanningNum.setText("");
			    editText_ScanningStatus.setTextColor(Color.BLACK);
	    		editText_ScanningStatus.setText("");
	    		button_submit.setVisibility(View.INVISIBLE);
			}
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
	public String GetTimeStr(){
    	Time t=new Time();
    	t.setToNow();
    	String y = Integer.toString(t.year);
    	String m = t.month < 9 ? "0" + Integer.toString(t.month + 1) : Integer
    		.toString(t.month + 1);
    	String d = t.monthDay < 10 ? "0" + Integer.toString(t.monthDay)
    		: Integer.toString(t.monthDay);
    	String h = t.hour < 10 ? "0" + Integer.toString(t.hour) : Integer
    		.toString(t.hour);
    	String min = t.minute < 10 ? "0" + Integer.toString(t.minute) : Integer
    		.toString(t.minute);
    	String sec = t.second < 10 ? "0" + Integer.toString(t.second) : Integer
    		.toString(t.second);
    	return y + m + d + h + min + sec;
    }
	class button_ScanningFinishClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			isOnScanningActivity=false;
			finish();
		}
		
	}
}
