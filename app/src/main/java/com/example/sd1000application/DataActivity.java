package com.example.sd1000application;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.realect.sd1000.Calculate.CalculateFormula;
import com.realect.sd1000.devicePortOperation.TestDataConvert;
import com.realect.sd1000.parameterStatus.*;


import KyleSocket_api.KyleSocketClass;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.LinkedList;
import java.util.Queue;
public class DataActivity extends Activity{
	Time t = new Time();
	DecimalFormat fnum = new DecimalFormat("##0");
	DecimalFormat fnum1 = new DecimalFormat("##0.0");
	private static int selCount;
    private static boolean[] printFlag;
    private static int printCount;
    private static int printedCount;
    private String[] SelectedDataStr;
    
    static int showcount = 5;
	static int offset = 0;
	private static final int SHOW_DATEStartPICK = 0;

    private static final int DATEStart_DIALOG_ID = 1;

    private static final int SHOW_DATEEndPICK = 2;

    private static final int DATEEnd_DIALOG_ID = 3;

    private int mSYear;

    private static int mSMonth;

    private static int mSDay;

    private static int mEYear;

    private static int mEMonth;

    private static int mEDay;
    int firstCheckPos = -1;
    int secondCheckPos = -1;
    final Calendar c = Calendar.getInstance();
    private Button button_UpLoad;
    private Button button_Print;
    private Button button_Back;
    private Button button_Export;
    private Button button_Delete;
    private Button button_PrePage;
    private Button button_NextPage;
    private EditText start = null;
    private EditText end = null;
    private TextView textView_PageCount;
    private TextView textView_PageNum;
    private CheckBox checkBox_SelAll;
    private RadioButton radioButton_Selected;
    private RadioButton radioButton_Range;
	private CheckBox[] mCheckBoxSelect=new CheckBox[5];
	private TextView[] mTextViewSampleNum=new TextView[5];
	private TextView[] mTextViewERSResult=new TextView[5];
	private TextView[] mTextViewHematResult=new TextView[5];
	private TextView[] mTextViewAddress=new TextView[5];
	private TableRow[] mTableRow=new TableRow[5];
	private TableLayout seltable;
	private static boolean[] selChecked;
	private static int[] selID;
	private static String[] selNum;
	private static String[] selAdd;
	private static String[] selERS;
	private static String[] selHemat;
	private static String[] selTime;
	private static String[] selTem;
	private static String[] selData;
	private static String[] selERSTestTimeType;
	
	private Button button_Select;
	private TextView TextView_XCLab;
	private TextView TextView_YJLab;
	private TextView textview_numLab;
	private TextView textview_addLab;
	private boolean loadFinish=false;
	boolean isLoad=true;
	private static final int MSG_SUCCESS = 0;//��ȡͼƬ�ɹ��ı�ʶ
	private static final int MSG_FAILURE = 1;//��ȡͼƬʧ�ܵı�ʶ
	 private Handler mHandler = new Handler() {
			public void handleMessage (Message msg) {//�˷�����ui�߳�����
				switch(msg.what) {
				case MSG_SUCCESS:
					//Toast.makeText(getApplication(), "11111111111111111111111�ɹ���", Toast.LENGTH_LONG).show();
					SetEnable(true);
					break;
				case MSG_FAILURE:
					//Toast.makeText(getApplication(), "111111111111111111111111ʧ�ܣ�", Toast.LENGTH_LONG).show();
					button_UpLoad.setEnabled(true);
					break;
				}
			}
		};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_selectdata);
		LoadXML();
		InitialSelTime();
		selCount=0;
		offset=0;
		//LoadTable();
	}

    private void InitialSelTime()
    {
	mSYear = c.get(Calendar.YEAR);
	mSMonth = c.get(Calendar.MONTH);
	mSDay = c.get(Calendar.DAY_OF_MONTH);
	mEYear = c.get(Calendar.YEAR);
	mEMonth = c.get(Calendar.MONTH);
	mEDay = c.get(Calendar.DAY_OF_MONTH);
	updateDateDisplay();
    }
    private void updateDateDisplay()
    {

	start.setText(new StringBuilder()
		.append((mSYear))
		.append("-")
		.append((mSMonth + 1) < 10 ? "0" + (mSMonth + 1)
			: (mSMonth + 1)).append("-")
		.append((mSDay < 10) ? "0" + mSDay : mSDay));

	end.setText(new StringBuilder()
		.append((mEYear))
		.append("-")
		.append((mEMonth + 1) < 10 ? "0" + (mEMonth + 1)
			: (mEMonth + 1)).append("-")
		.append((mEDay < 10) ? "0" + mEDay : mEDay));
    }
    class button_SelectListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Cursor cursor = null;
		    String[] getStr =
		    { "id", "date", "time", "xuhao", "testbit", "resourcedata",
			    "xuechendata", "yajidata", "temp" };
		  
			double starttime = mSYear * Math.pow(10, 4) + (mSMonth + 1)
				* Math.pow(10, 2) + mSDay;
			double endtime = mEYear * Math.pow(10, 4) + (mEMonth + 1)
				* Math.pow(10, 2) + mEDay;
			MidData.exSqlOk=false;
			cursor = MidData.sqlOp.selectHistoryByDate(cursor,getStr, starttime,
				endtime);
			if(!MidData.exSqlOk)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"��ѯʧ�ܣ������²�ѯ��":"Query failed, please retry", Toast.LENGTH_LONG).show();
				return;
			}
		    
		    int nn = cursor.getCount();
		    if (nn == 0)
		    {
		    	checkBox_SelAll.setClickable(false);
		    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"��ǰ����������!":"The current range is no data",
				Toast.LENGTH_LONG).show();
		    	return;
		    }
		    checkBox_SelAll.setClickable(true);
		    /*
		     * if(nn>800) { Toast.makeText(getApplicationContext(),
		     * "�������ϴ������Ժ�", Toast.LENGTH_LONG).show(); }
		     */
		    SetSelectedDataToTable(arg0, cursor, 0);
		    /*if(cursor!=null)
		    	cursor.close();*/
		}
    	
    }
    private void SetSelectedDataToTable(View arg0, Cursor cursor, int selType)
    {
	int countnum = cursor.getCount();	

	offset = 0;
	printCount = 0;
	printedCount = 0;
	
    selCount = countnum;
    selChecked = new boolean[countnum];
    selID = new int[countnum];
    SelectedDataStr = new String[countnum];
    selNum=new String[countnum];
    selAdd=new String[countnum];
    selERS=new String[countnum];
    selTime=new String[countnum];
    selHemat=new String[countnum];
    selTem=new String[countnum];
    selData=new String[countnum];
    
    printFlag = new boolean[countnum];
    int pageCount = countnum % showcount > 0 ? (countnum / showcount + 1)
	    : countnum / showcount;
    textView_PageNum.setText(((offset + showcount) / showcount)+"");
    textView_PageCount.setText(pageCount+"");
    loadFinish=false;
	new LoadSelectedDataThread(cursor).start();
	try
	{
	    Thread.sleep(500);
	}
	catch (Exception e)
	{
	    // TODO: handle exception
	}
	LoadShowData();
    }
    private void LoadShowData()
    {
	for(int i=0;i<5;i++)
	{
		mCheckBoxSelect[i].setChecked(false);
	    mCheckBoxSelect[i].setText("");
	    mTextViewSampleNum[i].setText("");
	    mTextViewERSResult[i].setText("");
	    mTextViewHematResult[i].setText("");	
	}
	for (int i = offset; i < offset + showcount; i++)
	{
	    if (i < selCount)
	    {
	    	if(SelectedDataStr[i]==null||SelectedDataStr[i].equals(""))
	    	{
	    		Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ�ܣ����Ժ���ת":"Loading error, please skip later", Toast.LENGTH_LONG).show();
	    		break;
	    	}
		if (SelectedDataStr[i].length() > 0)// �����Ϊʲô��ʱ������ط���ֵ��Ϊ��
		{
		    String[] str = SelectedDataStr[i].split(",");
		    mCheckBoxSelect[i-offset].setClickable(true);
		   	mCheckBoxSelect[i-offset].setChecked(selChecked[i]);
		    mCheckBoxSelect[i-offset].setText(Integer.toString(i+1));
		    mTextViewSampleNum[i-offset].setText(str[2]);
		    mTextViewERSResult[i-offset].setText(str[4]);
		    mTextViewHematResult[i-offset].setText(str[5]);
		    mTextViewAddress[i-offset].setText(str[7]);
		}
	    }
	    else
	    {
	    	mCheckBoxSelect[i-offset].setClickable(false);
	    	mCheckBoxSelect[i-offset].setChecked(false);
		    mCheckBoxSelect[i-offset].setText("");
		    mTextViewSampleNum[i-offset].setText("");
		    mTextViewERSResult[i-offset].setText("");
		    mTextViewHematResult[i-offset].setText("");	 
		    mTextViewAddress[i-offset].setText("");
	    }
	}
    }
    class LoadSelectedDataThread extends Thread
    {
    	Cursor cursor;

	public LoadSelectedDataThread(Cursor cursor)
	{
	    this.cursor = cursor;
	}

	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    LoadSelectedData(cursor);
	}

    }
    private void LoadSelectedData(Cursor cursor)
    {
	int i = 0;
	while (cursor.moveToNext())
	{

	    int id = cursor.getInt(0);
	    selID[i] = id;
	    double day = cursor.getDouble(1);
	    double time = cursor.getDouble(2);
	    String num = cursor.getString(3);
	    String testxcbit = cursor.getString(4);
	    String testyjbit = cursor.getString(5);
	    String xcResouceData=cursor.getString(6);
	    String xc = cursor.getString(7);
	    String temS = cursor.getString(9);
	    String xcTestTimeType=cursor.getString(10);
	    double temD = Double.parseDouble(temS);
	    String tem = fnum1.format(temD);
	    selTem[i]=tem;
	    selNum[i]=num;
	    selAdd[i]=testxcbit+"/"+testyjbit;
	    
	    selERS[i]=CalculateFormula.GetERSTestResult(xcResouceData, "0", MidData.isDebugging, xcTestTimeType, temD);//CalculateFormula.ResourceResultToShow(xc, "0", MidData.isDebugging, xcTestTimeType, temD);
	    
	   
	    String yj = cursor.getString(cursor.getColumnIndex("yajidata"));
	    selHemat[i]= CalculateFormula.YaJiToShow(yj);//;
	  
	    
	    int year = (int) (day / (Math.pow(10, 4)));
	    int month = (int) ((day - year * (Math.pow(10, 4))) / (Math.pow(10,
		    2)));
	    int date = (int) (day - year * (Math.pow(10, 4)) - month
		    * (Math.pow(10, 2)));
	    int hour = (int) (time / (Math.pow(10, 2)));
	    int min = (int) (time - hour * (Math.pow(10, 2)));
	    String sdate = Integer.toString(year) + "-"
		    + Integer.toString(month) + "-" + Integer.toString(date);
	    String stime;
	    if (min < 10)
	    {
		stime = Integer.toString(hour) + ":" + "0"
			+ Integer.toString(min);
	    }
	    else
	    {
		stime = Integer.toString(hour) + ":" + Integer.toString(min);
	    }
	    selTime[i]=sdate+"["+stime+"]";
	    SelectedDataStr[i] = id + "," + sdate + "[" + stime + "]" + ","
		    + num + "," + testxcbit + "/" + testyjbit + ","
		    + selERS[i] + "," + selHemat[i] + "," + tem+","+selAdd[i];

	    i++;
	}
	if(cursor!=null)
    	cursor.close();
	loadFinish=true;
    }
    class button_PrePageListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			isLoad=true;
			if(offset<showcount)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�ѵ����1ҳ":"Currently is the first page", Toast.LENGTH_LONG).show();
				return;
			}
			
			offset-=showcount;
			textView_PageNum.setText(offset/showcount+1+"");
			LoadShowData();
		}
    	
    }
    class button_NextPageListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			isLoad=true;
			if(offset+showcount>=selCount)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�ѵ������1ҳ":"Currently is the last page", Toast.LENGTH_LONG).show();
				return;
			}
			
			offset+=showcount;
			textView_PageNum.setText(offset/showcount+1+"");
			LoadShowData();
		}
    	
    }
    class radioButton_SelectedListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			radioButton_Selected.setChecked(arg1);
			radioButton_Range.setChecked(!arg1);
		}
    	
    }
    class radioButton_RangeListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			radioButton_Selected.setChecked(!arg1);
			radioButton_Range.setChecked(arg1);
		}
    	
    }
    class checkBox_SelAllListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			
			for(int i=0;i<selCount;i++)
			{
				selChecked[i]=arg1;
			}
			LoadShowData();
			if(!arg1)
			{
				radioButton_Selected.setChecked(true);
			}
		}
    	
    }
    class button_DeleteListener implements OnClickListener{

		@Override
		public void onClick(final View arg0) {
			// TODO Auto-generated method stub
			printCount = 0;
		    for (int i = 0; i < selCount; i++)
		    {
			if (selChecked[i])
			{
			    printCount++;
			}
		    }
		    if(printCount==0)
		    {
		    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"δѡ���κμ�¼":"No records are selected", Toast.LENGTH_LONG).show();
		    	return;
		    }
		    final AlertDialog isExit = new AlertDialog.Builder(arg0.getContext())
			    .create();
		    // �Ի������
		    isExit.setTitle(MidData.IsChinese==1?"��ʾ":"Notice");
		    // �Ի�����Ϣ
		    isExit.setMessage(MidData.IsChinese==1?"����ɾ�����ָܻ���ȷ��ɾ��?":"Data can not be recovered after deleted, please confirm ");
		    // ʵ�����Ի����ϵİ�ť����¼�����
		    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
		    {
			public void onClick(DialogInterface dialog, int which)
			{
			    switch (which)
			    {
			    case AlertDialog.BUTTON1:// "ȷ��"��ť�˳�����
				// ///////////////////////////////////////////
				int selnum = 0;
				for (int i = 0; i < selCount; i++)
				{
				    if (selChecked[i])
				    {
					selnum++;
				    }
				}
				String[] idsStr = new String[selnum];
				int num = 0;
				for (int i = 0; i < selCount; i++)
				{
				    if (selChecked[i])
				    {
					idsStr[num++] = Integer.toString(selID[i]);
				    }
				}
				MidData.exSqlOk=false;
				MidData.sqlOp.DeleteById("DS1000ResourceTable", idsStr,
					selnum);
				if(!MidData.exSqlOk)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"ɾ��ʧ��":"Delete failed", Toast.LENGTH_LONG).show();
					return;
				}
				String[] getStr =
				{ "id", "date", "time", "xuhao", "testbit",
					"resourcedata", "xuechendata", "yajidata",
					"temp" };
				Cursor cursor = null;
				double starttime = mSYear * Math.pow(10, 4) + (mSMonth + 1)
						* Math.pow(10, 2) + mSDay;
				double endtime = mEYear * Math.pow(10, 4) + (mEMonth + 1)
						* Math.pow(10, 2) + mEDay;
				cursor = MidData.sqlOp.selectHistoryByDate(cursor,getStr,
						starttime, endtime);
				

				SetSelectedDataToTable(arg0, cursor, 0);
				if(cursor!=null)
					cursor.close();
				// ///////////////////////////
				break;
			    case AlertDialog.BUTTON2:// "ȡ��"�ڶ�����ťȡ���Ի���
				break;
			    default:
				break;
			    }
			}
		    };
		    // ע�����
		    isExit.setButton(MidData.IsChinese==1?"ȷ��":"Comfirm", listener);
		    isExit.setButton2(MidData.IsChinese==1?"ȡ��":"Cancel", listener);
		    // ��ʾ�Ի���
		    isExit.show();

		}
    	
    }
    class button_ExportListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			boolean saved = true;
		    t.setToNow(); // ȡ��ϵͳʱ��3
		    int selnum = 0;
		    for (int i = 0; i < selCount; i++)
		    {
			if (selChecked[i])
			{
			    selnum++;
			}
		    }
		    if (selnum == 0)
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"δѡ���κ����ݽ��е���":"Please select the data need export",
				Toast.LENGTH_LONG).show();
			return;
		    }
		    String[] idsStr = new String[selnum];
		    int num = 0;
		    for (int i = 0; i < selCount; i++)
		    {
			if (selChecked[i])
			{
			    idsStr[num++] = Integer.toString(selID[i]);
			}
		    }
		    String[] getStr =
		    { "id", "date", "time", "xuhao", "testxcbit", "testyjbit",
			    "resourcedata", "xuechendata", "yajidata", "temp",
			    "resourceyajidata","tempres" };
		    Cursor cursor = null;

		    cursor = MidData.sqlOp.selectResourceDataByIDs(cursor,idsStr);

		    if (cursor.getCount() == 0)
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����Ҫ����������!":"No data need to export ",
				Toast.LENGTH_LONG).show();
			return;
		    }

		    String filename = MidData.IsChinese==1?MidData.Product_ID +"-"+Integer.toString(t.year)+"��"
			    + Integer.toString(t.month + 1)+"��"
			    + Integer.toString(t.monthDay)+"��" + Integer.toString(t.hour)+"ʱ"
			    + Integer.toString(t.minute)+"��" + Integer.toString(t.second)+"��":MidData.Product_ID +"-"+Integer.toString(t.year)+"Year"
					    + Integer.toString(t.month + 1)+"Month"
					    + Integer.toString(t.monthDay)+"Day" + Integer.toString(t.hour)+"h"
					    + Integer.toString(t.minute)+"m" + Integer.toString(t.second)+"s";
		    jxl.write.WritableWorkbook book = null;
		    WritableSheet sheet = null;

		    File fileD = new File("/udisk/SD1000Record/");

		    if (!fileD.exists())
		    {
			fileD.mkdir();
		    }
		    String fn = "udisk/SD1000Record/" + filename + ".xls";
		    try
		    {
			book = Workbook.createWorkbook(new File(fn));
			// ������Ϊ����һҳ���Ĺ�����,����0��ʾ���ǵ�һҳ
			sheet = book.createSheet(MidData.IsChinese==1?"��һҳ":"first page", 0);
		    }
		    catch (IOException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�����ļ�ʧ�ܣ���ȷ��U���Ѿ�����":"Failed to create file, make sure U-disk has been inserted",
				Toast.LENGTH_LONG).show();
			return;
		    }

		    saved = setbableTitle(sheet);

		    int i = 0;
		    // tl.removeAllViews();
		    while (cursor.moveToNext())
		    {
			String[] data = new String[10];
			double day = cursor.getDouble(cursor.getColumnIndex(getStr[1]));
			double time = cursor
				.getDouble(cursor.getColumnIndex(getStr[2]));
			int year = (int) (day / (Math.pow(10, 4)));
			int month = (int) ((day - year * (Math.pow(10, 4))) / (Math
				.pow(10, 2)));
			int date = (int) (day - year * (Math.pow(10, 4)) - month
				* (Math.pow(10, 2)));
			int hour = (int) (time / (Math.pow(10, 2)));
			int min = (int) (time - hour * (Math.pow(10, 2)));

			String stime;
			if (min < 10)
			{
			    stime = Integer.toString(hour) + ":" + "0"
				    + Integer.toString(min);
			}
			else
			{
			    stime = Integer.toString(hour) + ":"
				    + Integer.toString(min);
			}
			data[0] = Integer.toString(year) + "-"
				+ Integer.toString(month) + "-"
				+ Integer.toString(date) + "[" + stime + "]";

			data[1] = cursor.getString(cursor.getColumnIndex(getStr[3]));
			data[2] = cursor.getString(cursor.getColumnIndex(getStr[4]))
				+ "/"
				+ cursor.getString(cursor.getColumnIndex(getStr[5]));

			data[3] = cursor.getString(cursor.getColumnIndex(getStr[7]));
			
			
			String RESStr=CalculateFormula.GetERSTestResult(cursor.getString(cursor.getColumnIndex(getStr[6])), "0", MidData.isDebugging, cursor.getString(cursor.getColumnIndex(getStr[10])), Double.parseDouble(cursor.getString(cursor.getColumnIndex(getStr[9]))));//.ResourceResultToShow(data[3], "0", MidData.isDebugging, cursor.getString(cursor.getColumnIndex(getStr[10])), Double.parseDouble(cursor.getString(cursor.getColumnIndex(getStr[9]))));
			
			data[4] =RESStr;
			String YaJi=CalculateFormula.YaJiToShow(cursor.getString(cursor.getColumnIndex(getStr[8])));
			
			
			data[5] = YaJi;
			data[6] = cursor.getString(cursor.getColumnIndex(getStr[9]));
			data[8] = cursor.getString(cursor.getColumnIndex(getStr[10]));
			data[9] = cursor.getString(cursor.getColumnIndex(getStr[6]));
			data[9] = CalculateFormula.FitDataForExport(data[9]);
			data[7] = cursor.getString(cursor.getColumnIndex(getStr[11]));
			for (int k = 0; k < 10; k++)
			{
			    
				Label label = new Label(k, i + 1, data[k]);

				// ������õĵ�Ԫ����ӵ���������
				try
				{
				    sheet.addCell(label);
				}
				catch (RowsExceededException e)
				{
				    // TODO Auto-generated catch block
				    saved = false;
				    // e.printStackTrace();
				}
				catch (WriteException e)
				{
				    // TODO Auto-generated catch block
				    // e.printStackTrace();
				    saved = false;
				}
			   // }

			}

			i++;
		    }
		    if (saved)
		    {
			try
			{
			    book.write();
			}
			catch (IOException e)
			{
			    // TODO Auto-generated catch block
			    // e.printStackTrace();
			    saved = false;
			}
			try
			{
			    book.close();
			}
			catch (WriteException e)
			{
			    // TODO Auto-generated catch block
			    // e.printStackTrace();
			    saved = false;
			}
			catch (IOException e)
			{
			    // TODO Auto-generated catch block
			    // e.printStackTrace();
			    saved = false;
			}
		    }
		    try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    if (saved == false)
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ�ܣ�":"Failed to save",
				Toast.LENGTH_LONG).show();
		    else
			Toast.makeText(getApplicationContext(),
					MidData.IsChinese==1?"�ļ��ɹ����浽U���ϵġ�SD1000Record���ļ����У�":"Successfully saved to ��SD1000Record�� file folder on U-disk", Toast.LENGTH_LONG)
				.show();
		    if(cursor!=null)
		    	cursor.close();
		}

		
    	
    }
    private boolean setbableTitle(WritableSheet sheet)
    {
	boolean saved = true;
	Label labelT = new Label(0, 0, MidData.IsChinese==1?"����":"Date");

	// ������õĵ�Ԫ����ӵ���������
	try
	{
	    sheet.addCell(labelT);
	}
	catch (RowsExceededException e)
	{
	    // TODO Auto-generated catch block
	    saved = false;
	    // e.printStackTrace();
	}
	catch (WriteException e)
	{
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    saved = false;
	}
	labelT = new Label(1, 0, MidData.IsChinese==1?"�걾��":"ID");

	// ������õĵ�Ԫ����ӵ���������
	try
	{
	    sheet.addCell(labelT);
	}
	catch (RowsExceededException e)
	{
	    // TODO Auto-generated catch block
	    saved = false;
	    // e.printStackTrace();
	}
	catch (WriteException e)
	{
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    saved = false;
	}
	labelT = new Label(2, 0, MidData.IsChinese==1?"����λ":"Position");

	// ������õĵ�Ԫ����ӵ���������
	try
	{
	    sheet.addCell(labelT);
	}
	catch (RowsExceededException e)
	{
	    // TODO Auto-generated catch block
	    saved = false;
	    // e.printStackTrace();
	}
	catch (WriteException e)
	{
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    saved = false;
	}

	labelT = new Label(3, 0, MidData.IsChinese==1?"�߶Ȳ�":"Height gap");

	// ������õĵ�Ԫ����ӵ���������
	try
	{
	    sheet.addCell(labelT);
	}
	catch (RowsExceededException e)
	{
	    // TODO Auto-generated catch block
	    saved = false;
	    // e.printStackTrace();
	}
	
	catch (WriteException e)
	{
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    saved = false;
	}
	labelT = new Label(4, 0, MidData.IsChinese==1?"Ѫ��(mm/h)":"ESR(mm/h)");

	// ������õĵ�Ԫ����ӵ���������
	try
	{
	    sheet.addCell(labelT);
	}
	catch (RowsExceededException e)
	{
	    // TODO Auto-generated catch block
	    saved = false;
	    // e.printStackTrace();
	}
	
	catch (WriteException e)
	{
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    saved = false;
	}
	labelT = new Label(5, 0, MidData.IsChinese==1?"ѹ��(%)":"HCT(%)");

	// ������õĵ�Ԫ����ӵ���������
	try
	{
	    sheet.addCell(labelT);
	}
	catch (RowsExceededException e)
	{
	    // TODO Auto-generated catch block
	    saved = false;
	    // e.printStackTrace();
	}
	catch (WriteException e)
	{
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    saved = false;
	}
	labelT = new Label(6, 0, MidData.IsChinese==1?"�¶�(��)":"Temp(��)");
	// ������õĵ�Ԫ����ӵ���������
	try
	{
	    sheet.addCell(labelT);
	}
	catch (RowsExceededException e)
	{
	    // TODO Auto-generated catch block
	    saved = false;
	    // e.printStackTrace();
	}
	catch (WriteException e)
	{
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    saved = false;
	}
	labelT = new Label(7, 0, MidData.IsChinese==1?"ԭʼ�¶�":"Original tem");
	// ������õĵ�Ԫ����ӵ���������
	try
	{
	    sheet.addCell(labelT);
	}
	catch (RowsExceededException e)
	{
	    // TODO Auto-generated catch block
	    saved = false;
	    // e.printStackTrace();
	}
	catch (WriteException e)
	{
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    saved = false;
	}
	
	labelT = new Label(8, 0, MidData.IsChinese==1?"ѹ��ԭʼ��������":"HCT original test data");
	// ������õĵ�Ԫ����ӵ���������
	try
	{
	    sheet.addCell(labelT);
	}
	catch (RowsExceededException e)
	{
	    // TODO Auto-generated catch block
	    saved = false;
	    // e.printStackTrace();
	}
	catch (WriteException e)
	{
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    saved = false;
	}
	labelT = new Label(9, 0, MidData.IsChinese==1?"Ѫ��ԭʼ��������":"ESR original test data");
	// ������õĵ�Ԫ����ӵ���������
	try
	{
	    sheet.addCell(labelT);
	}
	catch (RowsExceededException e)
	{
	    // TODO Auto-generated catch block
	    saved = false;
	    // e.printStackTrace();
	}
	catch (WriteException e)
	{
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    saved = false;
	}
	
	return saved;
    }
    class button_BackListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
    	
    }
    class button_PrintListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			printCount = 0;
		    for (int i = 0; i < selCount; i++)
		    {
			if (selChecked[i])
			{
			    printCount++;
			}
		    }
		    if(printCount==0)
		    {
		    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"δѡ���κμ�¼":"No records are selected", Toast.LENGTH_LONG).show();
		    	return;
		    }
		    Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����׼����ӡ�����Ժ�...":"Ready to print, please wait...", Toast.LENGTH_LONG).show();
		    for (int i = 0; i < selCount; i++)
		    {
			if (selChecked[i])
			{
			    // isPrinting=true;
			    printFlag[i] = true;
			    Cursor cursor = null;
				cursor = MidData.sqlOp.selectResourceDataByID(cursor,Integer
					.toString(selID[i]));
				PrintQueue.add(cursor);
			    new PrintfDataByk().start();
			}
		    }
		}
    	
    }
    
    Queue<Cursor> PrintQueue=new LinkedList<Cursor>();
    boolean isPrintOn=false;
    public class PrintfDataByk extends Thread
    {
		/*private String lock;
		private int id;
	
		public PrintfDataByk(String lk, int id)
		{
		    // TODO Auto-generated constructor stub
		    this.lock = lk;
		    this.id = id;
		}*/
	
		@Override
		public void run()
		{
		    super.run();
		    if(isPrintOn)return;
		    isPrintOn=true;
		    while(!PrintQueue.isEmpty())
			{
				
				Cursor cursor=PrintQueue.poll();
				PrintData(cursor);
				if (MidData.mSetParameters.PrintSet==1)
			    {
					try
					{
					    Thread.sleep(10000);
					}
					catch (InterruptedException e)
					{
					    // TODO Auto-generated catch block
					    e.printStackTrace();
					}
			    }
				else
				{
					    try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}				
					
				}
				if(cursor!=null)
					cursor.close();
			}    
		    isPrintOn=false;
		}

    }
    private void PrintData(Cursor cursor)
    {
	while (cursor.moveToNext())
	{
		
	    double day = cursor.getDouble(cursor.getColumnIndex("date"));
	    int year = (int) (day / (Math.pow(10, 4)));
	    int month = (int) ((day - year * (Math.pow(10, 4))) / (Math.pow(10,
		    2)));
	    int date = (int) (day - year * (Math.pow(10, 4)) - month
		    * (Math.pow(10, 2)));
	    MidData.isPrintDateStr = Integer.toString(year) + "-"
		    + Integer.toString(month) + "-" + Integer.toString(date);
	    MidData.isPrintNumStr = cursor.getString(cursor
		    .getColumnIndex("xuhao"));
	    System.out.println(MidData.isPrintNumStr+"------��ӡ��ID------");
	    String resourcevalues = cursor.getString(cursor
		    .getColumnIndex("resourcedata"));
	    /*MidData.isPrintXueChenValueStr = cursor.getString(cursor
		    .getColumnIndex("xuechendata"));*/
	    String xc = cursor.getString(7);
	    String xxxx = cursor.getString(cursor
				.getColumnIndex("temp"));
	   
	    String xcType = cursor.getString(cursor
			    .getColumnIndex("xuechentesttimetype"));
	    MidData.isPrintXueChenValueStr=CalculateFormula.GetERSTestResult(resourcevalues, "0" , MidData.isDebugging,xcType , Double.parseDouble(xxxx));////CalculateFormula.ResourceResultToShow(MidData.isPrintXueChenValueStr,"0" , MidData.isDebugging,xcType , Double.parseDouble(xxxx));
	    //System.out.println(MidData.isPrintXueChenValueStr+"---print");
	    MidData.isPrintYaJiValueStr = CalculateFormula.YaJiToShow(cursor.getString(cursor
			    .getColumnIndex("yajidata")));
	   
	    String plusPeriod = cursor.getString(cursor
		    .getColumnIndex("plusperiod"));
	    float mul = (float) 1.0;
	    if(xcType.equals("0"))
	    	mul = (float) 120 / (float) Integer.parseInt(plusPeriod);
	    else
	    	mul = (float) 60 / (float) Integer.parseInt(plusPeriod);
	    mul = (float) 60 / (float) Integer.parseInt(plusPeriod);
	    MidData.sOpForPrint.LoadExplanation();
	    // LoadEndThePrint();
	    if (MidData.mSetParameters.PrintSet==1)
	    {
	    	MidData.sOpForPrint.PrintChartByk(resourcevalues, mul, xcType,Double.parseDouble(xxxx));
	    }

	    MidData.sOpForPrint.LoadEndThePrint();
	    MidData.sOpForPrint.LoadEndThePrint();
	    /*if (MidData.mSetParameters.PrintSet==1)
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
	    }*/

	    // LoadExplanation();
	    // LoadEndThePrint();
	}
    }
    public void OpenSerialPortForUpLoadDataToSABySD100()
    {
	// sPortForSA = MidData.sOpForSA.OpenSerialPortForSAOpenBySD100();
	MidData.portForSA = MidData.sOpForSA.OpenSerialPortForSAOpenBySD100();// MidData.sOp.OpenSerialPort();
	if (MidData.portForSA == null)
	{
	    Toast.makeText(DataActivity.this,
	    		MidData.IsChinese==1?"����SAʧ�ܣ��޷��ϴ����ݵ�SAϵͳ(��SD100)":"Connect SA failed, please check connection(SD100)", Toast.LENGTH_SHORT).show();
	    return;
	}
	MidData.m_OutputStream_SA = MidData.portForSA.getOutputStream();
	MidData.m_InputStream_SA = MidData.portForSA.getInputStream();
    }
    public void OpenSerialPortForUpLoadDataToSA()
    {
	MidData.portForSA = MidData.sOpForSA.OpenSerialPortForSA();// MidData.sOp.OpenSerialPort();
	if (MidData.portForSA == null)
	{
	    Toast.makeText(DataActivity.this, MidData.IsChinese==1?"����SAʧ�ܣ��޷��ϴ����ݵ�SAϵͳ":"Connect SA failed, please check connection",
		    Toast.LENGTH_SHORT).show();
	    return;
	}
	MidData.m_OutputStream_SA = MidData.portForSA.getOutputStream();
	MidData.m_InputStream_SA = MidData.portForSA.getInputStream();
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
    private void SetEnable(boolean fg){
    	button_UpLoad.setEnabled(fg);
		button_Export.setEnabled(fg);
		button_Print.setEnabled(fg);
		button_Export.setEnabled(fg);
		button_Delete.setEnabled(fg);
		button_Back.setEnabled(fg);
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
    class button_UpLoadListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			printCount = 0;
		    for (int i = 0; i < selCount; i++)
		    {
			if (selChecked[i])
			{
			    printCount++;
			}
		    }
		    if(printCount==0)
		    {
		    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"δѡ���κμ�¼":"No records are selected", Toast.LENGTH_LONG).show();
		    	return;
		    }
			
		    SetEnable(false);
		    if (MidData.m_UpToSA)
		    {
			OpenSerialPortForUpLoadDataToSABySD100();
			
			if (MidData.m_OutputStream_SA != null)
			{			   
			    new UpData2PcInSD100Byk().start();
			}
			else
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����SAʧ�ܣ���ȷ������":"Connect SA failed, please check connection", Toast.LENGTH_LONG).show();
				SetEnable(true);
				return;
			}
		    }
		    else if(MidData.m_UpToLIS_net||MidData.m_UpToLIS_ser)
		    {
		    	if(MidData.m_UpToLIS_net)
		    	{
		    		if (MidData.fd <0||MidData.LisNetConnetTime==null||GetSecond(MidData.LisNetConnetTime)>3600)
		    			OpenSerialPortForUpLoadDataToLIS();
					if (MidData.fd >0)
					{
					    new UpData2PcByk().start();
					}
					else
					{
						Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����LISʧ�ܣ���ȷ����������":"Connect LIS failed, please check network connection", Toast.LENGTH_LONG).show();
						SetEnable(true);
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
						Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ�ܣ���ȷ������":"Connect LIS failed, please check network connection", Toast.LENGTH_LONG).show();
						SetEnable(true);
						return;
					}
		    	}
		    }
		}
    	
    }
    private void UploadLine2PC(String Num,String ID)
    {
    	int testID=Integer.parseInt(ID);
    	MidData.testDataConvert=new TestDataConvert();
    	String str=MidData.testDataConvert.ConvertLineForLis(testID);
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
    private void UpLaodData2PC(String Num,String XC,String YJ)
    {

	    byte[] Data2 = null;
	    byte[] Data3 = null;
	    if (XC.equals("δ���"))
	    {
		XC = "NULL";
	    }
	    Data2 = XC.getBytes();
	    if (YJ.equals("δ���"))
	    {
			YJ = "NULL";			
	    }
	    Data3 = YJ.getBytes();	
	    byte[] Data1 = Num.getBytes();
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
	    	try {
				MidData.m_OutputStream_SA.write(data5, 0, Length);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    }
	    Data1=null;
	    Data2=null;
	    Data3=null;
	    data5=null;
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
		for (int i = 0; i < selCount; i++)
		{
			if (selChecked[i])
			{
				/*Cursor cursor=null;
			    //new UpData2PcInSD100Byk("lock", i).start();
			    cursor = MidData.sqlOp.selectResourceDataByID(cursor,Integer
					.toString(selID[i]));*/
				try {
					sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    UpLaodData2PC(selNum[i],selERS[i],selHemat[i]);
			    if(MidData.LisUploadLine)
			    {
			    	UploadLine2PC(selNum[i],Integer.toString(selID[i]));
			    }
			 }
		 }
		 Looper.prepare();
		 //KyleSocketClass.DeInitPort(MidData.fd);
		 //MidData.fd=-1;
		 //Toast.makeText(getApplicationContext(), MidData.IsChinese?"�������ݵ�LIS�ɹ�":"Send data to LIS successfully", Toast.LENGTH_LONG).show();
		 mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
		 Looper.loop();
		 //button_UpLoad.setEnabled(true);
		 //MidData.sOpForSA.SendToSAPortClose();


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
		   
		    ReBackStatus mReBackStatus=new ReBackStatus();
			 mReBackStatus.ReBackStatus(MidData.IsChinese==1?"û���յ������ź�":"No handshake signal received");
			 mReBackStatus.start();
			 
		    //button_UpLoad.setEnabled(true);
			 //Toast.makeText(getApplicationContext(), MidData.IsChinese?"û���յ������ź�":"No handshake signal received", Toast.LENGTH_LONG).show();
			 mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
			 Looper.loop();
			 MidData.sOpForSA.SendToSAPortClose();
		    return;
		    }
	
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
		     ReBackStatus mReBackStatus=new ReBackStatus();
			 mReBackStatus.ReBackStatus(MidData.IsChinese==1?"û���յ�Ӧ���ź�":"No response signal received");
			 mReBackStatus.start();
		     
		     //button_UpLoad.setEnabled(true);
			 //Toast.makeText(getApplicationContext(), MidData.IsChinese?"û���յ�Ӧ���ź�":"No response signal received", Toast.LENGTH_LONG).show();
			 mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
			 Looper.loop();
			 MidData.sOpForSA.SendToSAPortClose();
			 
		     return;
		     }
		 for (int i = 0; i < selCount; i++)
		    {
			if (selChecked[i])
			{
				Cursor cursor=null;
			    //new UpData2PcInSD100Byk("lock", i).start();
			    cursor = MidData.sqlOp.selectResourceDataByID(cursor,Integer
					.toString(selID[i]));
				UpLaodData2PCBySD100(cursor);
				if(cursor!=null)
					cursor.close();
			}
		    }
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
		     ReBackStatus mReBackStatus=new ReBackStatus();
			 mReBackStatus.ReBackStatus(MidData.IsChinese==1?"û���յ�����Ӧ���ź�":"No finish response singnal received");
			 mReBackStatus.start();
		     //button_UpLoad.setEnabled(true);
			 //Toast.makeText(getApplicationContext(), MidData.IsChinese?"û���յ�����Ӧ���ź�":"No finish response singnal received", Toast.LENGTH_LONG).show();
			 mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
			 Looper.loop();
			 MidData.sOpForSA.SendToSAPortClose();
		     return;
		 }
		 Looper.prepare();
		 ReBackStatus mReBackStatus=new ReBackStatus();
		 mReBackStatus.ReBackStatus(MidData.IsChinese==1?"�������ݳɹ�":"Send data successfully");
		 mReBackStatus.start();
		 //button_UpLoad.setEnabled(true);
		 //Toast.makeText(getApplicationContext(), MidData.IsChinese?"�������ݳɹ�":"Send data successfully", Toast.LENGTH_LONG).show();
		 mHandler.obtainMessage(MSG_SUCCESS).sendToTarget();
		 Looper.loop();		 
		 MidData.sOpForSA.SendToSAPortClose();


	}

    }
    class ReBackStatus extends Thread{
    	String str;
    	void ReBackStatus(String str_)
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
			    	button_UpLoad.setEnabled(true);	
			    	Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
					 
			    }
			});
		}
    	
    }
    private String test(String barcodeDesc) {
        Pattern p;
        p = Pattern.compile("\\d{1}");//��������� ��һ������
        Matcher m;
        m = p.matcher(barcodeDesc);//���ƥ��
        String res = "";
        while(m.find()){
            res=res+ m.group();
        }
        return res;
    }
    private void UpLaodData2PCBySD100(Cursor cursor)
    {
	long xx;
	long yy;
	double xx_xc;
	double xx_yj;
	while (cursor.moveToNext())
	{
	    byte[] data = new byte[9]; 
	  xx= Long.parseLong(test(cursor.getString(cursor.getColumnIndex("xuhao"))));

	  if(xx>28305)xx=xx%10000;//������Ȳ�����ֻȡ��4λ
	  
		  yy=xx/100;
		  if(yy>255)yy=255;
		  data[0]=(byte)yy;
		  xx=xx-yy*100;
	  
		  yy=xx/10;
		  if(yy>255)yy=255;
		  data[1]=(byte)yy;
		  xx=xx-yy*10;
		  data[2]=(byte)xx;
		  String XC = CalculateFormula.GetERSTestResult(cursor.getString(cursor.getColumnIndex("resourcedata")), "0", MidData.isDebugging, Integer.toString(MidData.mSetParameters.ERSTestTime), Double.parseDouble(cursor.getString(cursor
					.getColumnIndex("temp"))));//.ResourceResultToShow(cursor.getString(cursor.getColumnIndex("xuechendata")), "0", MidData.isDebugging, Integer.toString(MidData.mSetParameters.ERSTestTime), Double.parseDouble(cursor.getString(cursor
					//.getColumnIndex("temp"))));
		  	if (XC.equals("δ���")||XC.equals("Untest"))
		    {
			xx=0;
		    }
		    else
		    {
		    	int dataXC =CalculateFormula.StringToIntForUpLoad(XC);		    	
		    	xx=dataXC;
		    }
		  
		    yy=xx/100;
			  if(yy>255)yy=255;
			  data[3]=(byte)yy;
			  xx=xx-yy*100;
		  
			  yy=xx/10;
			  if(yy>255)yy=255;
			  data[4]=(byte)yy;
			  xx=xx-yy*10;
			  data[5]=(byte)xx;
			  		  
			  
			  String YJ = CalculateFormula.YaJiToShow(cursor.getString(cursor.getColumnIndex("yajidata")));
			    if (YJ.equals("δ���")||YJ.equals("Untest"))
			    {
				xx=0;
			    }
			    else
			    {
			    	/*if((Double.parseDouble(YJ)*100)%100<45)
			    		xx=(int)Double.parseDouble(YJ);
					else
						xx=(int)Double.parseDouble(YJ)+1;*/
			    	xx=CalculateFormula.StringToIntForUpLoad(YJ);
			    }
			    
			    yy=xx/100;
				  if(yy>255)yy=255;
				  data[6]=(byte)yy;
				  xx=xx-yy*100;
			  
				  yy=xx/10;
				  if(yy>255)yy=255;
				  data[7]=(byte)yy;
				  xx=xx-yy*10;
				  data[8]=(byte)xx;
				  MidData.sOpForSA.SendToSAPort(data, data.length);
			  
	}
    }
    class textView_PageNumTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			if (arg1.getAction() == MotionEvent.ACTION_DOWN)
			{
				int pageCount = selCount % showcount > 0 ? (selCount / showcount + 1)
					    : selCount / showcount;
				Intent intent = new Intent();
			    intent.setClass(DataActivity.this,
			    		ParaSetActivity.class);
			    Bundle bundle = new Bundle();
			    bundle.putString("title", MidData.IsChinese==1?"��תҳ������":"Page turning");
			    bundle.putInt("min", 1);
			    bundle.putInt("max", pageCount);
			    bundle.putInt("page",Integer.parseInt(textView_PageNum.getText().toString().trim()));
			   
			    intent.putExtras(bundle);
			    startActivityForResult(intent, 0);
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
				bt.setBackgroundResource(R.drawable.buttonbackselbthui);
			}
			else if(arg1.getAction()==MotionEvent.ACTION_UP)
			{
				bt.setBackgroundResource(R.drawable.buttonbackselbt);
			}
			return false;
		}
    	
    }
    class PageTurnListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			Button bt=(Button)arg0;
			if(arg1.getAction()==MotionEvent.ACTION_DOWN)
			{
				bt.setBackgroundResource(R.drawable.buttonbackupdownhui);
			}
			else if(arg1.getAction()==MotionEvent.ACTION_UP)
			{
				bt.setBackgroundResource(R.drawable.buttonbackupdown);
			}
			return false;
		}
    	
    }
	private void LoadXML(){
		TextView_XCLab=(TextView)findViewById(R.id.TextView_XCLab);
		TextView_XCLab.setText(MidData.IsChinese==1?"Ѫ��"+"\n"+"(mm/h)":"ESR"+"\n"+"(mm/h)");
		TextView_YJLab=(TextView)findViewById(R.id.TextView_YJLab);
		TextView_YJLab.setText(MidData.IsChinese==1?"ѹ��"+"\n"+"(%)":"HCT"+"\n"+"(%)");
		textview_numLab=(TextView)findViewById(R.id.textview_numLab);
		textview_numLab.setText(MidData.IsChinese==1?"�걾��":"ID");
		textview_addLab=(TextView)findViewById(R.id.textview_addLab);
		textview_addLab.setText(MidData.IsChinese==1?"����λ":"POS");
		mTableRow[0]=(TableRow)findViewById(R.id.tableRow_SEL1);
		
		mTableRow[1]=(TableRow)findViewById(R.id.tableRow_SEL2);
		mTableRow[2]=(TableRow)findViewById(R.id.tableRow_SEL3);
		mTableRow[3]=(TableRow)findViewById(R.id.tableRow_SEL4);
		mTableRow[4]=(TableRow)findViewById(R.id.tableRow_SEL5);
		for(int i=0;i<5;i++)
		{
			final int off=offset;
			//final int count=selCount;
			mTableRow[i].setId(i);
			mTableRow[i].setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					int id=arg0.getId();
					id+=offset;
					if(id>=selCount)
					{
						return false;
					}
					 Intent intent = new Intent();
					    intent.setClass(DataActivity.this,
						    DataTouchShowActivity.class);
					    Bundle bundle = new Bundle();
					    int d=id;
					    bundle.putString("���", selNum[id]);
					    bundle.putString("ʱ��", selTime[id]);
					    bundle.putString("λ��", selAdd[id]);
					    bundle.putString("Ѫ�����", selERS[id]);
					    bundle.putString("ѹ�����", selHemat[id]);
					    bundle.putString("�¶�", selTem[id]);
					    bundle.putInt("ID", selID[id]);
					    intent.putExtras(bundle);
					    startActivityForResult(intent, 0);
					return false;
				}
			});
		}
		button_UpLoad=(Button)findViewById(R.id.button_UpLoad);
		button_UpLoad.setOnClickListener(new button_UpLoadListener());
		button_UpLoad.setOnTouchListener(new ButtonTouchListener());
		button_UpLoad.setText(MidData.IsChinese==1?"�ϴ�":"Upload");
		
		button_Print=(Button)findViewById(R.id.button_Print);
		button_Print.setOnClickListener(new button_PrintListener());
		button_Print.setOnTouchListener(new ButtonTouchListener());
		button_Print.setText(MidData.IsChinese==1?"��ӡ":"Print");
		
		button_Back=(Button)findViewById(R.id.button_Back);
		button_Back.setOnClickListener(new button_BackListener());
		button_Back.setOnTouchListener(new ButtonTouchListener());
		button_Back.setText(MidData.IsChinese==1?"����":"Return");
		
		button_Export=(Button)findViewById(R.id.button_Export);
		button_Export.setOnClickListener(new button_ExportListener());
		button_Export.setOnTouchListener(new ButtonTouchListener());
		button_Export.setText(MidData.IsChinese==1?"����":"Export");
		
		button_Delete=(Button)findViewById(R.id.button_Delete);
		button_Delete.setOnClickListener(new button_DeleteListener());
		button_Delete.setOnTouchListener(new ButtonTouchListener());
		button_Delete.setText(MidData.IsChinese==1?"ɾ��":"Delete");
		
		checkBox_SelAll=(CheckBox)findViewById(R.id.checkBox_SelAll);
		checkBox_SelAll.setOnCheckedChangeListener(new checkBox_SelAllListener());
		checkBox_SelAll.setClickable(false);
		checkBox_SelAll.setText(MidData.IsChinese==1?"ȫ��":"All");
		
		radioButton_Range=(RadioButton)findViewById(R.id.radioButton_Range);
		radioButton_Range.setOnCheckedChangeListener(new radioButton_RangeListener());
		radioButton_Range.setText(MidData.IsChinese==1?"��Χ":"\nMulti select");
		
		radioButton_Selected=(RadioButton)findViewById(R.id.radioButton_Selected);
		radioButton_Selected.setOnCheckedChangeListener(new radioButton_SelectedListener());
		radioButton_Selected.setText(MidData.IsChinese==1?"ѡ��":"\nSingle select");
		
		button_NextPage=(Button)findViewById(R.id.button_NextPage);
		button_NextPage.setOnClickListener(new button_NextPageListener());
		button_NextPage.setOnTouchListener(new PageTurnListener());
		button_NextPage.setText(MidData.IsChinese==1?"��\nҳ":"Page\nDown");
		
		
		button_PrePage=(Button)findViewById(R.id.button_PrePage);
		button_PrePage.setOnClickListener(new button_PrePageListener());
		button_PrePage.setOnTouchListener(new PageTurnListener());
		button_PrePage.setText(MidData.IsChinese==1?"�� \nҳ":"Page\nUp");
		
		
		textView_PageCount=(TextView)findViewById(R.id.textView_PageCount);
		textView_PageNum=(TextView)findViewById(R.id.textView_PageNum);
		textView_PageNum.setOnTouchListener(new textView_PageNumTouchListener());
		
		button_Select=(Button)findViewById(R.id.button_Select);
		button_Select.setOnClickListener(new button_SelectListener());
		button_Select.setOnTouchListener(new ButtonTouchListener());
		button_Select.setText(MidData.IsChinese==1?"��ѯ":"Query");
		
		start = (EditText) findViewById(R.id.DateStartEditView);
		start.setOnTouchListener(new OnTouchListener()
		{

		    @Override
		    public boolean onTouch(View v, MotionEvent event)
		    {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_UP)
			{

			}
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
			   
				Thread task = new Thread(new Runnable()
				{

				    @Override
				    public void run()
				    {
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what =DataActivity.SHOW_DATEStartPICK;
					DataActivity.this.dateSetHandler
						.sendMessage(msg);
				    }
				});
				task.start();
			  

			}

			return false;
		    }
		});
		end = (EditText) findViewById(R.id.DateEndEditView);
		end.setOnTouchListener(new OnTouchListener()
		{

		    @Override
		    public boolean onTouch(View v, MotionEvent event)
		    {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_UP)
			{

			}
			if (event.getAction() == MotionEvent.ACTION_DOWN)
			{
			    
				Thread task = new Thread(new Runnable()
				{

				    @Override
				    public void run()
				    {
					// TODO Auto-generated method stub
					Message msg = new Message();
					msg.what = DataActivity.SHOW_DATEEndPICK;
					DataActivity.this.dateSetHandler
						.sendMessage(msg);
				    }
				});
				task.start();
			   

			}
			return false;
		    }
		});
	   
		mCheckBoxSelect[0]=(CheckBox)findViewById(R.id.checkBox1);
		mCheckBoxSelect[0].setOnClickListener(new SelectChecked());
		mCheckBoxSelect[0].setClickable(false);
		mCheckBoxSelect[1]=(CheckBox)findViewById(R.id.checkBox2);
		mCheckBoxSelect[1].setOnClickListener(new SelectChecked());
		mCheckBoxSelect[1].setClickable(false);
		mCheckBoxSelect[2]=(CheckBox)findViewById(R.id.checkBox3);
		mCheckBoxSelect[2].setOnClickListener(new SelectChecked());
		mCheckBoxSelect[2].setClickable(false);
		mCheckBoxSelect[3]=(CheckBox)findViewById(R.id.checkBox4);
		mCheckBoxSelect[3].setOnClickListener(new SelectChecked());
		mCheckBoxSelect[3].setClickable(false);
		mCheckBoxSelect[4]=(CheckBox)findViewById(R.id.checkBox5);
		mCheckBoxSelect[4].setOnClickListener(new SelectChecked());
		mCheckBoxSelect[4].setClickable(false);
		mTextViewSampleNum[0]=(TextView)findViewById(R.id.TextView_SampleNum1);
		mTextViewSampleNum[1]=(TextView)findViewById(R.id.TextView_SampleNum2);
		mTextViewSampleNum[2]=(TextView)findViewById(R.id.TextView_SampleNum3);
		mTextViewSampleNum[3]=(TextView)findViewById(R.id.TextView_SampleNum4);
		mTextViewSampleNum[4]=(TextView)findViewById(R.id.TextView_SampleNum5);
		mTextViewERSResult[0]=(TextView)findViewById(R.id.TextView_ERSResult1);
		mTextViewERSResult[1]=(TextView)findViewById(R.id.TextView_ERSResult2);
		mTextViewERSResult[2]=(TextView)findViewById(R.id.TextView_ERSResult3);
		mTextViewERSResult[3]=(TextView)findViewById(R.id.TextView_ERSResult4);
		mTextViewERSResult[4]=(TextView)findViewById(R.id.TextView_ERSResult5);
		mTextViewHematResult[0]=(TextView)findViewById(R.id.TextView_HematResult1);
		mTextViewHematResult[1]=(TextView)findViewById(R.id.TextView_HematResult2);
		mTextViewHematResult[2]=(TextView)findViewById(R.id.TextView_HematResult3);
		mTextViewHematResult[3]=(TextView)findViewById(R.id.TextView_HematResult4);
		mTextViewHematResult[4]=(TextView)findViewById(R.id.TextView_HematResult5);
		mTextViewAddress[0]=(TextView)findViewById(R.id.TextView_Address1);
		mTextViewAddress[1]=(TextView)findViewById(R.id.TextView_Address2);
		mTextViewAddress[2]=(TextView)findViewById(R.id.TextView_Address3);
		mTextViewAddress[3]=(TextView)findViewById(R.id.TextView_Address4);
		mTextViewAddress[4]=(TextView)findViewById(R.id.TextView_Address5);
		seltable=(TableLayout)findViewById(R.id.seltable);
	}
	class SelectChecked implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			int position=99;
			if(arg0.getId()==R.id.checkBox1)
			{
				position=0;
			}
			else if(arg0.getId()==R.id.checkBox2)
			{
				position=1;
			}
			else if(arg0.getId()==R.id.checkBox3)
			{
				position=2;
			}
			else if(arg0.getId()==R.id.checkBox4)
			{
				position=3;
			}
			else if(arg0.getId()==R.id.checkBox5)
			{
				position=4;
			}
			if((position+offset)>selCount)
				return;
			CheckBox cb=(CheckBox)arg0;
			boolean arg1=cb.isChecked();
			if((position+offset)>selCount)
			{
				cb.setChecked(false);
				return;
			}
				
			if(position==99)
				return;
			if(radioButton_Selected.isChecked())
			{
				if(offset+position<selCount)
					selChecked[offset+position]=arg1;
			}
			else
			{
				if (arg1)
				{
				    if (firstCheckPos == -1)
				    {
				    	if(position + offset<selCount)
				    	{
				    		firstCheckPos = position + offset;
				    		selChecked[position + offset] = true;
				    	}
					
				    }
				    else
				    {
						if (position + offset >= firstCheckPos)
						{
						    for (int i = firstCheckPos; i <= position + offset; i++)
						    {
						    	//System.out.println(i+"--------111");//+offset
						    	if(i<selCount)
						    	{
						    		selChecked[i] = true;
						    		//System.out.println(i+"--------111---"+Integer.toString(i+offset));
						    	}
						    		
						    }
						    for (int i = 0; i < showcount; i++)
						    {
						    	if(i+offset<selCount)
						    	{
						    		if(selChecked[i+offset])
							    		mCheckBoxSelect[i].setChecked(true);
							    	else
							    		mCheckBoxSelect[i].setChecked(false);
						    	}
						    	else
						    		mCheckBoxSelect[i].setChecked(false);
						    }
						}
						else if (position + offset <= firstCheckPos)
						{
						    for (int i = position + offset; i <= firstCheckPos; i++)
						    {
							selChecked[i] = true;
						    }
						    for (int i = 0; i < showcount; i++)
						    {
						    	if(i+offset<selCount)
						    	{
						    		if(selChecked[i+offset])
							    		mCheckBoxSelect[i].setChecked(true);
							    	else
							    		mCheckBoxSelect[i].setChecked(false);
						    	}
						    	else
						    	{
						    		mCheckBoxSelect[i].setChecked(false);
						    	}
						    }
						}
						firstCheckPos = -1;
				    }
				}
				else
				{
					if(position+offset<selCount)
						selChecked[position + offset] = false;
				}
			}
		}

		/*@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			int position=99;
			if(isLoad)
			{
				isLoad=false;
				return;
			}
			if(arg0.getId()==R.id.checkBox1)
			{
				position=0;
			}
			else if(arg0.getId()==R.id.checkBox2)
			{
				position=1;
			}
			else if(arg0.getId()==R.id.checkBox3)
			{
				position=2;
			}
			else if(arg0.getId()==R.id.checkBox4)
			{
				position=3;
			}
			else if(arg0.getId()==R.id.checkBox5)
			{
				position=4;
			}
			if(position==99)
				return;
			if(radioButton_Selected.isChecked())
			{
				if(offset+position<selCount)
					selChecked[offset+position]=arg1;
			}
			else
			{
				if (arg1)
				{
				    if (firstCheckPos == -1)
				    {
				    	if(position + offset<=selCount)
				    	{
				    		firstCheckPos = position + offset;
				    		selChecked[position + offset] = true;
				    	}
					
				    }
				    else
				    {
						if (position + offset >= firstCheckPos)
						{
						    for (int i = firstCheckPos; i <= position + offset; i++)
						    {
						    	if(i+offset<selCount)
						    		selChecked[i] = true;
						    }
						    for (int i = 0; i < showcount; i++)
						    {
						    	if(i+offset<selCount)
						    	{
						    		if(selChecked[i+offset])
							    		mCheckBoxSelect[i].setChecked(true);
							    	else
							    		mCheckBoxSelect[i].setChecked(false);
						    	}
						    	else
						    		mCheckBoxSelect[i].setChecked(false);
						    }
						}
						else if (position + offset <= firstCheckPos)
						{
						    for (int i = position + offset; i <= firstCheckPos; i++)
						    {
							selChecked[i] = true;
						    }
						    for (int i = 0; i < showcount; i++)
						    {
						    	if(i+offset<selCount)
						    	{
						    		if(selChecked[i+offset])
							    		mCheckBoxSelect[i].setChecked(true);
							    	else
							    		mCheckBoxSelect[i].setChecked(false);
						    	}
						    	else
						    	{
						    		mCheckBoxSelect[i].setChecked(false);
						    	}
						    }
						}
						firstCheckPos = -1;
				    }
				}
				else
				{
					if(position+offset<selCount)
						selChecked[position + offset] = false;
				}
			}
		}*/
		
	}

/*	private void LoadTable(){
		
		for(int i=0;i<5;i++)
		{
			mTableRow[i]=new TableRow(this);
			String s="XXX";
			int fontSize=15;
			mCheckBoxSelect[i]=new CheckBox(this);
			mCheckBoxSelect[i].setWidth(90);
			mCheckBoxSelect[i].setHeight(80);
			
			mCheckBoxSelect[i].setText(s);
			mCheckBoxSelect[i].setTextSize(fontSize);
			mTextViewSampleNum[i]=new TextView(this);
			mTextViewSampleNum[i].setWidth(150);
			mTextViewSampleNum[i].setHeight(80);
			mTextViewSampleNum[i].setLeft(15);
			mTextViewSampleNum[i].setText(s);
			mTextViewSampleNum[i].setTextSize(fontSize);
			mTextViewERSResult[i]=new TextView(this);
			mTextViewERSResult[i].setWidth(120);
			mTextViewERSResult[i].setHeight(80);
			mTextViewERSResult[i].setLeft(15);
			mTextViewERSResult[i].setText(s);
			mTextViewERSResult[i].setTextSize(fontSize);
			mTextViewHematResult[i]=new TextView(this);
			mTextViewHematResult[i].setWidth(120);
			mTextViewHematResult[i].setHeight(80);
			mTextViewHematResult[i].setText(s);
			mTextViewHematResult[i].setLeft(15);
			mTextViewHematResult[i].setTextSize(fontSize);
			mTableRow[i].addView(mCheckBoxSelect[i]);
			mTableRow[i].addView(mTextViewSampleNum[i]);
			mTableRow[i].addView(mTextViewERSResult[i]);
			mTableRow[i].addView(mTextViewHematResult[i]);
			mTableRow[i].setTop(15);
			if(i%2==0)
			{
				mCheckBoxSelect[i].setBackgroundResource(R.drawable.tabletextviewbackwhite);
				mTextViewSampleNum[i].setBackgroundResource(R.drawable.tabletextviewbackwhites);
				mTextViewERSResult[i].setBackgroundResource(R.drawable.tabletextviewbackwhites);
				mTextViewHematResult[i].setBackgroundResource(R.drawable.tabletextviewbackwhites);
			}
			else
			{
				
			//}
			seltable.addView(mTableRow[i]);
		}
	}*/
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {//����������ɷ���
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			if(loadFinish)
			{
				Bundle bundle = data.getExtras();
				String backval = bundle.getString("val");
				int page=Integer.parseInt(backval);
				isLoad=true;
				offset=showcount*(page-1);
				textView_PageNum.setText(offset/showcount+1+"");
				LoadShowData();
			}
			else
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���ڼ������ݣ����Ժ��ת":"Loading data, please skip later", Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}

    private DatePickerDialog.OnDateSetListener mDateStartSetListener = new DatePickerDialog.OnDateSetListener()
    {
	public void onDateSet(DatePicker view, int year, int monthOfYear,
		int dayOfMonth)
	{
	    mSYear = year;
	    mSMonth = monthOfYear;
	    mSDay = dayOfMonth;
	    updateDateDisplay();
	}
    };
    private DatePickerDialog.OnDateSetListener mDateEndSetListener = new DatePickerDialog.OnDateSetListener()
    {
	public void onDateSet(DatePicker view, int year, int monthOfYear,
		int dayOfMonth)
	{
	    mEYear = year;
	    mEMonth = monthOfYear;
	    mEDay = dayOfMonth;
	    updateDateDisplay();
	}
    };

    protected Dialog onCreateDialog(int id)
    {
	switch (id)
	{
	case DATEStart_DIALOG_ID:
	    return new DatePickerDialog(this, mDateStartSetListener, mSYear,
		    mSMonth, mSDay);
	case DATEEnd_DIALOG_ID:
	    return new DatePickerDialog(this, mDateEndSetListener, mEYear,
		    mEMonth, mEDay);

	}
	return null;
    }
	  Handler dateSetHandler = new Handler()
	    {
		@SuppressWarnings("deprecation")
		@Override
		public void handleMessage(Message msg)
		{

		    switch (msg.what)
		    {

		    case DataActivity.SHOW_DATEStartPICK:
			showDialog(DATEStart_DIALOG_ID);
			break;

		    case DataActivity.SHOW_DATEEndPICK:
			showDialog(DATEEnd_DIALOG_ID);
			break;

		    }
		}
	    };

}
