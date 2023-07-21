package com.example.sd1000application;

import com.example.sd1000application.TestUpActivity.ButtonTouchListener;
import com.example.sd1000application.TestUpActivity.LaveTestTimeThread;
import com.example.sd1000application.TestUpActivity.TestButtonTouchListener;
import com.realect.sd1000.parameterStatus.IsSaveToDatabaseData;
import com.realect.sd1000.parameterStatus.MidData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TestDownActivity extends Activity{
	float drawGridWidth = 650;//canvas.getWidth();//480;// getWidth() - 30;//缂備焦锚閸╂锟介挊澶婎唺
	float drawGridHeight = 435;//canvas.getHeight();//450;// getHeight() - 40;//缂備焦锚閸╂顨炲Ο鍝勵唺

	float beginX = 0;
	float beginY = 0;
	
	float xSpace = (drawGridWidth)  / 11;
	float ySpace = (drawGridHeight) / 6;
	
	private ImageView TestDownArea;
	private Bitmap baseBitmap;
	private static Canvas canvas;
	private Paint backPaint;
	private Paint linePaint;
	private Paint oldAreaPaint;
	private Paint newAreaPaint;
	private Paint textPaint;
	private Paint textLabPaint;
	private Paint labbackPaint;
	private Paint picPaint;
	
	private int[] updatestatus=new int[50];
	private int XshowTime = 0;
	private int LastTouchNum=999;
	
	private Button button_ERSTest;
	private Button button_HematTest;
	private Button button_Exit;
	private Button button_PerPage;
	private Button button_ReTest;
	private Handler mHandler=new Handler(){
	    public void handleMessage(Message msg){
	    	Bundle xx=msg.getData();
			
			int num=xx.getInt("编号");
			if(num==999)
			{
				float[] rect=GetRectOut1(5);
				canvas.drawRect(rect[0], rect[1], rect[2], rect[3], labbackPaint);
				canvas.drawText(Integer.toString(MidData.LaveTestTime), 30+5*xSpace, 50, textLabPaint);
				TestDownArea.setImageBitmap(baseBitmap);
				return;
			}
			num=xx.getInt("编号")-50;
			int addressNum=NumToAddressNum(num);
			
			int status=xx.getInt("状态");
			if((num+50)!=LastTouchNum)
			{
				float[] rect=GetRectOut(addressNum);
				switch (status) {
				case 1:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], oldAreaPaint);
					updatestatus[num]=status;
					break;
				case 2:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], oldAreaPaint);
					picPaint.setStyle(Style.STROKE);
					Path path=new Path();
					path.moveTo(rect[0]+27, rect[1]+20);
					path.lineTo(rect[0]+10, rect[1]+50);
					path.lineTo(rect[0]+45, rect[1]+50);
					path.lineTo(rect[0]+27, rect[1]+20);
					canvas.drawPath(path, picPaint);
					updatestatus[num]=status;
					break;
				case 3:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], oldAreaPaint);
					picPaint.setStyle(Style.STROKE);
					canvas.drawCircle(rect[0]+27, rect[1]+35, 16, picPaint);
					//canvas.drawCircle(rect[0]+27, rect[1]+35, 12, picPaint);
					updatestatus[num]=status;
					break;
				case 4:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], oldAreaPaint);
					picPaint.setStyle(Style.FILL);
					canvas.drawCircle(rect[0]+27, rect[1]+35, 16, picPaint);
					updatestatus[num]=status;
					break;
				case 5:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], oldAreaPaint);
					picPaint.setStyle(Style.STROKE);
					Path pathl=new Path();
					pathl.moveTo(rect[0]+27, rect[1]+20);
					pathl.lineTo(rect[0]+10, rect[1]+50);
					pathl.lineTo(rect[0]+45, rect[1]+50);
					pathl.lineTo(rect[0]+27, rect[1]+20);
					canvas.drawPath(pathl, picPaint);
					updatestatus[num]=status;
					break;
				case 6:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], oldAreaPaint);
					picPaint.setStyle(Style.STROKE);
					canvas.drawRect(rect[0]+11, rect[1]+16, rect[2]-12, rect[3]-17, picPaint);
					updatestatus[num]=status;
					break;
				case 7:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], oldAreaPaint);
					picPaint.setStyle(Style.FILL);
					canvas.drawRect(rect[0]+11, rect[1]+16, rect[2]-12, rect[3]-17, picPaint);
					updatestatus[num]=status;
					break;
				case 8:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], oldAreaPaint);
					picPaint.setStyle(Style.STROKE);
					Path pathlx=new Path();
					pathlx.moveTo(rect[0]+28, rect[1]+18);
					pathlx.lineTo(rect[0]+12, rect[1]+35);
					pathlx.lineTo(rect[0]+28, rect[1]+52);
					pathlx.lineTo(rect[0]+44, rect[1]+35);
					pathlx.lineTo(rect[0]+28, rect[1]+18);
					canvas.drawPath(pathlx, picPaint);
					updatestatus[num]=status;
					break;
				case 9:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], oldAreaPaint);
					picPaint.setStyle(Style.FILL);
					Path pathlxs=new Path();
					pathlxs.moveTo(rect[0]+28, rect[1]+18);
					pathlxs.lineTo(rect[0]+12, rect[1]+35);
					pathlxs.lineTo(rect[0]+28, rect[1]+52);
					pathlxs.lineTo(rect[0]+44, rect[1]+35);
					pathlxs.lineTo(rect[0]+28, rect[1]+18);
					canvas.drawPath(pathlxs, picPaint);
					updatestatus[num]=status;
					break;	
				case 10:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], oldAreaPaint);
					picPaint.setStyle(Style.STROKE);
					Path path1=new Path();
					path1.moveTo(rect[0]+15, rect[1]+20);
					path1.lineTo(rect[2]-15, rect[3]-20);
					Path path2=new Path();
					path2.moveTo(rect[0]+15, rect[3]-20);
					path2.lineTo(rect[2]-15, rect[1]+20);
					canvas.drawPath(path1, picPaint);
					canvas.drawPath(path2, picPaint);	
					updatestatus[num]=status;
					break;
				default:
					break;
				}
			}
			else
			{
				float[] rect=GetRectIn(addressNum);
				switch (status) {
				case 1:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], newAreaPaint);
					updatestatus[num]=status;
					break;
				case 2:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], newAreaPaint);
					picPaint.setStyle(Style.STROKE);
					Path path=new Path();
					path.moveTo(rect[0]+27, rect[1]+20);
					path.lineTo(rect[0]+10, rect[1]+50);
					path.lineTo(rect[0]+45, rect[1]+50);
					path.lineTo(rect[0]+27, rect[1]+20);
					canvas.drawPath(path, picPaint);
					updatestatus[num]=status;
					break;
				case 3:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], newAreaPaint);
					picPaint.setStyle(Style.STROKE);
					canvas.drawCircle(rect[0]+27, rect[1]+35, 16, picPaint);
					//canvas.drawCircle(rect[0]+27, rect[1]+35, 12, picPaint);
					updatestatus[num]=status;
					break;
				case 4:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], newAreaPaint);
					picPaint.setStyle(Style.FILL);
					canvas.drawCircle(rect[0]+27, rect[1]+35, 16, picPaint);
					updatestatus[num]=status;
					break;
				case 5:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], newAreaPaint);
					picPaint.setStyle(Style.STROKE);
					Path pathl=new Path();
					pathl.moveTo(rect[0]+27, rect[1]+20);
					pathl.lineTo(rect[0]+10, rect[1]+50);
					pathl.lineTo(rect[0]+45, rect[1]+50);
					pathl.lineTo(rect[0]+27, rect[1]+20);
					canvas.drawPath(pathl, picPaint);
					updatestatus[num]=status;
					break;
				case 6:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], newAreaPaint);
					picPaint.setStyle(Style.STROKE);
					canvas.drawRect(rect[0]+11, rect[1]+16, rect[2]-12, rect[3]-17, picPaint);
					updatestatus[num]=status;
					break;
				case 7:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], newAreaPaint);
					picPaint.setStyle(Style.FILL);
					canvas.drawRect(rect[0]+11, rect[1]+16, rect[2]-12, rect[3]-17, picPaint);
					updatestatus[num]=status;
					break;
				case 8:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], newAreaPaint);
					picPaint.setStyle(Style.STROKE);
					Path pathlx=new Path();
					pathlx.moveTo(rect[0]+28, rect[1]+18);
					pathlx.lineTo(rect[0]+12, rect[1]+35);
					pathlx.lineTo(rect[0]+28, rect[1]+52);
					pathlx.lineTo(rect[0]+44, rect[1]+35);
					pathlx.lineTo(rect[0]+28, rect[1]+18);
					canvas.drawPath(pathlx, picPaint);
					updatestatus[num]=status;
					break;
				case 9:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], newAreaPaint);
					picPaint.setStyle(Style.FILL);
					Path pathlxs=new Path();
					pathlxs.moveTo(rect[0]+28, rect[1]+18);
					pathlxs.lineTo(rect[0]+12, rect[1]+35);
					pathlxs.lineTo(rect[0]+28, rect[1]+52);
					pathlxs.lineTo(rect[0]+44, rect[1]+35);
					pathlxs.lineTo(rect[0]+28, rect[1]+18);
					canvas.drawPath(pathlxs, picPaint);
					updatestatus[num]=status;
					break;	
				case 10:
					canvas.drawRect(rect[0], rect[1], rect[2], rect[3], newAreaPaint);
					picPaint.setStyle(Style.STROKE);
					Path path1=new Path();
					path1.moveTo(rect[0]+15, rect[1]+20);
					path1.lineTo(rect[2]-15, rect[3]-20);
					Path path2=new Path();
					path2.moveTo(rect[0]+15, rect[3]-20);
					path2.lineTo(rect[2]-15, rect[1]+20);
					canvas.drawPath(path1, picPaint);
					canvas.drawPath(path2, picPaint);	
					updatestatus[num]=status;
					break;
				default:
					break;
				}
			}
			TestDownArea.setImageBitmap(baseBitmap);
	    }
	    };
    private int NumToAddressNum(int ad){
		
		int p=ad/10;
		int m=ad/5;
		
		if(ad<=54)
		{
			return ad+m-p;			
		}
		else
		{
			return ad+m-p+11;	
		}
	}
    private float[] GetRectIn(int num)
	{		
		float[] rect=new float[4];
		int x=num%11;
		int y=num/11;
		rect[0]=(float) (x*xSpace+2);
		rect[1]=(float) ((y+1)*ySpace+2);
		rect[2]=(float) ((x+1)*xSpace-2);
		rect[3]=(float) ((y+2)*ySpace-2);
		return rect;
	}
    private float[] GetRectOut1(int num)
	{		
		float[] rect=new float[4];
		int x=num%11;
		int y=num/11;
		rect[0]=(float) (x*xSpace+1);
		rect[1]=(float) (y*ySpace+1);
		rect[2]=(float) ((x+1)*xSpace-1);
		rect[3]=(float) ((y+1)*ySpace-1);
		return rect;
	}
	private float[] GetRectOut(int num)
		{		
			float[] rect=new float[4];
			int x=num%11;
			int y=num/11;
			rect[0]=(float) (x*xSpace+1);
			rect[1]=(float) ((y+1)*ySpace+1);
			rect[2]=(float) ((x+1)*xSpace-1);
			rect[3]=(float) ((y+2)*ySpace-1);
			return rect;
		}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_testdown);
		LoadXML();
		InitialAreaTest();
		LoadLastSet();
		InitialStatus();
		MidData.isOnTestDown=true;
		Thread updateStatus = new Thread(new Runnable()
		{
		    @Override
		    public void run()
		    {
			// TODO Auto-generated method stub
			while (MidData.m_isInterrupted_LowerMachine&&MidData.isOnTestDown)
			{

			    if (XshowTime > 49)
			    {
				XshowTime = 0;
				runOnUiThread(new Runnable()
				{
				    public void run()
				    {
				    	if(MidData.mSetParameters.SetNumWay==0)
				    	{
				    		for(int i=0;i<100;i++)
					    	{
					    		if(MidData.mTestParameters[i].showEndOfNotSumTime)
					    		{
					    			final int num=i;
					    			new AlertDialog.Builder(TestDownActivity.this)
								    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
								    .setMessage(MidData.IsChinese==1?GetAddress(i)+"标本沉降达到仪器读值下限，测试结果已保存至数据库":GetAddress(i)+"Reach minimum od detection range,result saved!")
								    .setPositiveButton(
								    		MidData.IsChinese==1?"确定":"Comfirm",
									    new DialogInterface.OnClickListener()
									    {
										public void onClick(
											DialogInterface dialog,
											int whichButton)
										{
											
										}
									    }).setCancelable(false).show();
					    			MidData.mTestParameters[num].showEndOfNotSumTime = false;
									
					    		}
					    	}
					    	if(MidData.IsNeedUserConfirm==1)
						    {
						    	//MidData.IsNeedUserConfirm = 5;
						    	for(int i=0;i<100;i++)
						    	{
						    		if(MidData.mTestParameters[i].aisleNumShow.equals("样本重检"))
						    		{
						    			final int num=i;
						    			new AlertDialog.Builder(TestDownActivity.this)
									    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
									    .setMessage(
									    		MidData.IsChinese==1?"通道 "+GetAddress(i)+":" + MidData.mTestParameters[i].aisleNum+"已检测，是否需要重检":"Channel"+GetAddress(i)+":" + MidData.mTestParameters[i].aisleNum+"Detected, detect again")
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
												MidData.mTestParameters[num].aisleNumShow = MidData.mTestParameters[num].aisleNum;
												MidData.IsNeedUserConfirm=0;
											}
										    }).setNegativeButton(MidData.IsChinese==1?"取消":"Cancel", null)
									    .show();
						    			MidData.IsNeedUserConfirm=0;
						    		}
						    	}
						    	
						    }
					    	 else if(MidData.IsNeedUserConfirm==3)
							    {
							    	//MidData.IsNeedUserConfirm = 4;
							    	for(int i=0;i<100;i++)
							    	{
							    		if(MidData.mTestParameters[i].aisleNumShow.equals("此样本正在检测"))
							    		{
							    			final int num=i;
							    			new AlertDialog.Builder(TestDownActivity.this)
										    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
										    .setMessage(
										    		MidData.IsChinese==1?"编号：" + MidData.mTestParameters[i].aisleNum+"正在检测，请对本样本重新编号":"ID：" + MidData.mTestParameters[i].aisleNum+"Measuring, please renumber")
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
													MidData.mTestParameters[num].aisleNumShow =""; 
													MidData.mTestParameters[num].aisleNum="";
												}
											    }).setCancelable(false)
										    .show();
							    			MidData.IsNeedUserConfirm=0;
							    		}
							    	}
							    	
							}
					    	 else if(MidData.IsNeedUserConfirm==4)
							    {
							    	//MidData.IsNeedUserConfirm = 5;
							    	for(int i=0;i<100;i++)
							    	{
							    		if(MidData.mTestParameters[i].aisleNumShow.equals("本编号无效（完成压积测试），请重新编号"))
							    		{
							    			final int num=i;
							    			new AlertDialog.Builder(TestDownActivity.this)
										    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
										    .setMessage(
										    		MidData.IsChinese==1?"通道 "+ GetAddress(i)+":" + MidData.mTestParameters[i].aisleNum+"本编号无效（完成压积测试），请重新编号":"Channel"+ GetAddress(i)+":" + MidData.mTestParameters[i].aisleNum+"Invalid number(HCT finished), please renumber")
										    .setPositiveButton(
											    MidData.IsChinese==1?"确定":"Comfirm",
											    new DialogInterface.OnClickListener()
											    {
												public void onClick(
													DialogInterface dialog,
													int whichButton)
												{
													//MidData.IsNeedUserConfirm=0;
													//MidData.mTestParameters[num].IsNewSample = false;
													//MidData.mTestParameters[num].ReDetect = true;
													//MidData.mTestParameters[num].aisleNumShow = MidData.mTestParameters[num].aisleNum;
												}
											    }).setCancelable(false)
										    .show();
							    			MidData.IsNeedUserConfirm=0;
							    		}
							    	}
							    	//MidData.IsNeedUserConfirm=0;
							    }
							if (MidData.IsNeedReTest == 1)
							{
							    MidData.IsNeedReTest = 2;
							    updateStatusShow();
							    String msg = MidData.IsNeedReTestMsg;
							    MidData.IsNeedReTestMsg = "";
							    new AlertDialog.Builder(TestDownActivity.this)
								    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
								    .setMessage(
								    		MidData.IsChinese==1?"测试值异常 请重新测试 异常通道" + msg:"Abnormal test value, please re-testing, abnormal channels:" + msg)
								    .setPositiveButton(
									    MidData.IsChinese==1?"确定":"Comfirm",
									    new DialogInterface.OnClickListener()
									    {
										public void onClick(
											DialogInterface dialog,
											int whichButton)
										{
										    if (MidData.IsNeedReTest == 2)
										    {
											MidData.IsNeedReTest = 0;
										    }
										}
									    }).setCancelable(false)
								    .show();
	
							}
							else if (MidData.IsNeedReTest == 0)
							{
							    updateStatusShow();
							}					    
				    	}
				    	else
				    	{
				    		/*if(MidData.IsNeedUserConfirm==1)
						    {
						    	MidData.IsNeedUserConfirm = 4;
						    	for(int i=0;i<100;i++)
						    	{
						    		if(MidData.mTestParameters[i].aisleNumShow.equals("样本重检"))
						    		{
						    			final int num=i;
						    			new AlertDialog.Builder(TestDownActivity.this)
									    .setTitle("消息提示")
									    .setMessage(
										   "通道 "+ GetAddress(i)+":" + MidData.mTestParameters[i].aisleNum+"已检测，是否需要覆盖？")
									    .setPositiveButton(
										    "确定",
										    new DialogInterface.OnClickListener()
										    {
											public void onClick(
												DialogInterface dialog,
												int whichButton)
											{
												//MidData.mTestParameters[num].IsNewSample = false;
												//MidData.mTestParameters[num].ReDetect = true;
												//MidData.mTestParameters[num].aisleNumShow = MidData.mTestParameters[num].aisleNum;
												if(IsSaveToDatabaseData.TestType==0)
												{
													MidData.sqlOp
												    .DeleteDataByNum(MidData.mTestParameters[MidData.SelAddress].aisleNum);
											    MidData.mTestParameters[MidData.SelAddress].saveDatabaseID=MidData.sqlOp
												    .InsertData(
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
													    "0", "");
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
												}
												int x=0;
										    	for (int i=1; i < 100; i++)//???????????????????????????????????????
												{
													x = i;
													int address = (MidData.SelAddress+x)%100;
													//MidData.passageStatus[address] = "通道故障";
													if (MidData.mTestParameters[address].aisleStatus==1&&MidData.mTestParameters[address].aisleTestFinished)
													{
													    MidData.SelAddress = address;
													    break;
													}
												}
											}
										    }).setNegativeButton("取消", null)
									    .show();
						    		}
						    	}
						    	MidData.IsNeedUserConfirm=0;
						    }
						    else if(MidData.IsNeedUserConfirm==3)
						    {
						    	MidData.IsNeedUserConfirm = 4;
						    	for(int i=0;i<100;i++)
						    	{
						    		if(MidData.mTestParameters[i].aisleNumShow.equals("此样本正在检测"))
						    		{
						    			final int num=i;
						    			new AlertDialog.Builder(TestDownActivity.this)
									    .setTitle("消息提示")
									    .setMessage(
									    		"通道 "+ GetAddress(i)+":" + MidData.mTestParameters[i].aisleNum+"正在检测，请对本样本重新编号,否则无法录入!")
									    .setPositiveButton(
										    "确定",
										    new DialogInterface.OnClickListener()
										    {
											public void onClick(
												DialogInterface dialog,
												int whichButton)
											{
												//MidData.mTestParameters[num].IsNewSample = false;
												//MidData.mTestParameters[num].ReDetect = true;
												MidData.mTestParameters[num].aisleNumShow = MidData.mTestParameters[num].aisleNum;
											}
										    }).setCancelable(false)
									    .show();
						    		}
						    	}
						    	MidData.IsNeedUserConfirm=0;
						    }*/
							if (MidData.IsNeedReTest == 1)
							{
							    MidData.IsNeedReTest = 2;
							    updateStatusShow();
							    String msg = MidData.IsNeedReTestMsg;
							    MidData.IsNeedReTestMsg = "";
							    new AlertDialog.Builder(TestDownActivity.this)
								    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
								    .setMessage(
									    MidData.IsChinese==1?"测试值异常 请重新测试 异常通道" + msg:"Abnormal test value, please re-testing, abnormal channels:" + msg)
								    .setPositiveButton(
									    MidData.IsChinese==1?"确定":"Comfirm",
									    new DialogInterface.OnClickListener()
									    {
										public void onClick(
											DialogInterface dialog,
											int whichButton)
										{
										    if (MidData.IsNeedReTest == 2)
										    {
											MidData.IsNeedReTest = 0;
										    }
										}
									    }).setCancelable(false)
								    .show();

							}
							else if (MidData.IsNeedReTest == 0)
							{
							    updateStatusShow();
							}
				    	
				    	}
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
	private void InitialStatus(){
		for(int i=0;i<50;i++)
		{
			updatestatus[i]=0;
		}
	}
	private void LoadLastSet(){
		if (MidData.mSetParameters.TestType==1)
		{
		    
		    button_ERSTest.setBackgroundResource(R.drawable.buttonbackupdown);
		    button_HematTest.setBackgroundResource(R.drawable.buttonbackupdownhui);
		}
		else if (MidData.mSetParameters.TestType==0)
		{
			button_ERSTest.setBackgroundResource(R.drawable.buttonbackupdownhui);
		    button_HematTest.setBackgroundResource(R.drawable.buttonbackupdown);
		}
		button_ERSTest.setText(MidData.IsChinese==1?"血沉\n测试":"ESR\nTest");
		button_HematTest.setText(MidData.IsChinese==1?"压积\n测试":"HCT\nTest");
		button_Exit.setText(MidData.IsChinese==1?"返回":"Return");
		button_PerPage.setText(MidData.IsChinese==1?"上页":"Page\nUp");
	}
	private void updateStatusShow(){
		if(MidData.TestFinished)
		{
			if(MidData.SelAddress>=50&&MidData.SelAddress<=99&&MidData.SelAddress!=LastTouchNum)
			{
				int ad=LastTouchNum;
				LastTouchNum=MidData.SelAddress;
				UpdateSelArea(LastTouchNum);
				UpdateSelArea(ad);
			}
			//String ss="";
			//for(int i=50;i<100;i++)
			//	ss+=updatestatus[i-50]+"---"+i+",";
			//System.out.println(ss);
			boolean findHematSample=false;
			boolean findERSSample=false;
			for(int i=0;i<50;i++)
			{
				int ad=i+50;
				int st = 0;
				//System.out.println(Integer.toString(MidData.mTestParameters[i].aisleStatus)+"----"+MidData.IsESRSelect[i]+"---"+MidData.mTestParameters[i].testStatus+"--dffdf-"+i);
				//System.out.println(MidData.IsESRSelect[i]+"----MidData.IsESRSelect[i]---"+i);
				//System.out.println(MidData.mTestParameters[i].testStatus+"----MidData.mTestParameters[i].testStatus---"+i);
				
				if(updatestatus[i]==0)
				{
					if(MidData.mTestParameters[ad].aisleStatus==-1)//通道不可用
					{
						st=10;
					}
					else
					{						
						if(MidData.IsESRSelect[ad])
						{
							if(MidData.mTestParameters[ad].testStatus==0)//无样本
							{
								st=1;
							}
							else if(MidData.mTestParameters[ad].testStatus==1)//发现样本
							{
								findERSSample=true;
								st=2;
							}
							else if(MidData.mTestParameters[ad].testStatus==2)//正在测试
							{
								st=3;
							}
							else if(MidData.mTestParameters[ad].testStatus==3)//测试完成
							{
								st=4;
							}
							else if(MidData.mTestParameters[ad].testStatus==4)//样本超量
							{
								st=8;
							}
							else if(MidData.mTestParameters[ad].testStatus==5)//样本不足
							{
								st=9;
							}
						}
						else
						{
							if(MidData.mTestParameters[ad].testStatus==0)//无样本
							{
								st=1;
							}
							else if(MidData.mTestParameters[ad].testStatus==1)//发现样本
							{
								findHematSample=true;
								st=5;
							}
							else if(MidData.mTestParameters[ad].testStatus==2)//正在测试
							{
								st=6;
							}
							else if(MidData.mTestParameters[ad].testStatus==3)//测试完成
							{
								st=7;
							}
							else if(MidData.mTestParameters[ad].testStatus==4)//样本超量
							{
								st=8;
							}
							else if(MidData.mTestParameters[ad].testStatus==5)//样本不足
							{
								st=9;
							}
						}
					}
					Message msg=new Message();
					Bundle b=new Bundle(); 
					
					b.putInt("状态", st);
					b.putInt("编号", ad);
					msg.setData(b);
					TestDownActivity.this.mHandler.sendMessage(msg);
					
				}
				else
				{
					if(updatestatus[i]!=10)
					{
						if(MidData.IsESRSelect[ad])
						{
							if(MidData.mTestParameters[ad].testStatus==0)//无样本
							{
								st=1;
							}
							else if(MidData.mTestParameters[ad].testStatus==1)//发现样本
							{
								findERSSample=true;
								st=2;
							}
							else if(MidData.mTestParameters[ad].testStatus==2)//正在测试
							{
								st=3;
							}
							else if(MidData.mTestParameters[ad].testStatus==3)//测试完成
							{
								st=4;
							}
							else if(MidData.mTestParameters[ad].testStatus==4)//样本超量
							{
								st=8;
							}
							else if(MidData.mTestParameters[ad].testStatus==5)//样本不足
							{
								st=9;
							}
						}
						else
						{
							if(MidData.mTestParameters[ad].testStatus==0)//无样本
							{
								st=1;
							}
							else if(MidData.mTestParameters[ad].testStatus==1)//发现样本
							{
								findHematSample=true;
								st=5;
							}
							else if(MidData.mTestParameters[ad].testStatus==2)//正在测试
							{
								st=6;
							}
							else if(MidData.mTestParameters[ad].testStatus==3)//测试完成
							{
								st=7;
							}
							else if(MidData.mTestParameters[ad].testStatus==4)//样本超量
							{
								st=8;
							}
							else if(MidData.mTestParameters[ad].testStatus==5)//样本不足
							{
								st=9;
							}
						}
					}
					if(updatestatus[i]!=st&&st!=0)
					{
						Message msg=new Message();
						Bundle b=new Bundle(); 
						
						b.putInt("状态", st);
						b.putInt("编号", ad);
						msg.setData(b);
						TestDownActivity.this.mHandler.sendMessage(msg);
					}
				}
			}
			if(MidData.IsConsumerReduce&&MidData.isDebugging==0)
			{
				if (MidData.mSetParameters.TestType==0&&MidData.mSetParameters.ERSCount<0&&findERSSample&&MidData.NoWarnningChargeForERSConsume)
				{
					MidData.NoWarnningChargeForERSConsume = false;
					new AlertDialog.Builder(TestDownActivity.this)
				    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
				    .setMessage(
				    		MidData.IsChinese==1?"血沉耗材用尽，请充值":"ESR consumables exhausted,please recharge")
				    .setPositiveButton(
				    		MidData.IsChinese==1?"确定":"Comfirm",
					    new DialogInterface.OnClickListener()
					    {
						public void onClick(
							DialogInterface dialog,
							int whichButton)
						{							    
							MidData.NoWarnningChargeForERSConsume = true;
						}
					    }).setCancelable(false)
				    .show();
					//Toast.makeText(getApplicationContext(), MidData.IsChinese?"血沉耗材用尽，请充值":"Please Charge For ERS", Toast.LENGTH_LONG).show();
				}
				else if(MidData.mSetParameters.hematCount<0&&findHematSample&&MidData.NoWarnningChargeForHematConsume)
				{
					 MidData.NoWarnningChargeForHematConsume = false;
					 new AlertDialog.Builder(TestDownActivity.this)
					    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
					    .setMessage(
					    		MidData.IsChinese==1?"压积耗材用尽，请充值":"HCT consumables exhausted,please recharge")
					    .setPositiveButton(
					    		MidData.IsChinese==1?"确定":"Comfirm",
						    new DialogInterface.OnClickListener()
						    {
							public void onClick(
								DialogInterface dialog,
								int whichButton)
							{							    
								MidData.NoWarnningChargeForHematConsume = true;
							}
						    }).setCancelable(false)
					    .show();
					//Toast.makeText(getApplicationContext(), MidData.IsChinese?"压积耗材用尽，请充值":"Please Charge For Hemat", Toast.LENGTH_LONG).show();
	    	    }							
			}
		}
	}
	private void UpdateSelArea(int i){

		//i=GetAddressNum(i);
		if(i<50||i>99)
			return;
		int st=0;
		if(MidData.SelAddress!=LastTouchNum&&(MidData.SelAddress>=0&&MidData.SelAddress<=99))
		{
			LastTouchNum=MidData.SelAddress;
		}
		if(!MidData.TestFinished)
		{
			st=1;
		}
		else if(MidData.mTestParameters[i].aisleStatus==-1)//通道不可用
		{
			st=10;
		}
		else
		{						
			if(MidData.IsESRSelect[i])
			{
				if(MidData.mTestParameters[i].testStatus==0)//无样本
				{
					st=1;
				}
				else if(MidData.mTestParameters[i].testStatus==1)//发现样本
				{
					st=2;
				}
				else if(MidData.mTestParameters[i].testStatus==2)//正在测试
				{
					st=3;
				}
				else if(MidData.mTestParameters[i].testStatus==3)//测试完成
				{
					st=4;
				}
				else if(MidData.mTestParameters[i].testStatus==4)//样本超量
				{
					st=8;
				}
				else if(MidData.mTestParameters[i].testStatus==5)//样本不足
				{
					st=9;
				}
			}
			else
			{
				if(MidData.mTestParameters[i].testStatus==0)//无样本
				{
					st=1;
				}
				else if(MidData.mTestParameters[i].testStatus==1)//发现样本
				{
					st=5;
				}
				else if(MidData.mTestParameters[i].testStatus==2)//正在测试
				{
					st=6;
				}
				else if(MidData.mTestParameters[i].testStatus==3)//测试完成
				{
					st=7;
				}
				else if(MidData.mTestParameters[i].testStatus==4)//样本超量
				{
					st=8;
				}
				else if(MidData.mTestParameters[i].testStatus==5)//样本不足
				{
					st=9;
				}
			}
		}
		Message msg=new Message();
		Bundle b=new Bundle(); 
		
		b.putInt("状态", st);
		b.putInt("编号", i);
		msg.setData(b);
		TestDownActivity.this.mHandler.sendMessage(msg);
		
	
	}
	class button_PerPageClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			MidData.isOnTestUp = true;
			
			MidData.isOnTestDown = false;
			Intent intent = new Intent();
			intent.setClass(TestDownActivity.this, TestUpActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.enter_toptobottom,
				R.anim.exit_toptobottom);
			for(int i=0;i<50;i++)
			{
				updatestatus[i]=0;
			}
			
			finish();
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
	class button_ReTestClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, set after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			Intent intent = new Intent();
			intent.setClass(TestDownActivity.this, ReTestSetActivity.class);
			startActivity(intent);
		}
		
	}
	private void LoadXML(){
		button_ReTest=(Button)findViewById(R.id.button_ReTest);
		button_ReTest.setOnClickListener(new button_ReTestClickListener());
		button_ReTest.setOnTouchListener(new ButtonTouchListener());
		
		button_PerPage=(Button)findViewById(R.id.button_PerPage);
		button_PerPage.setOnClickListener(new button_PerPageClickListener());
		button_PerPage.setOnTouchListener(new ButtonTouchListener());
		
		button_Exit=(Button)findViewById(R.id.button_Exit);
		button_Exit.setOnClickListener(new button_ExitListener());
		button_Exit.setOnTouchListener(new ButtonTouchListener());
		
		button_HematTest=(Button)findViewById(R.id.button_HematTest);
		button_HematTest.setOnClickListener(new button_HematTestListener());
		button_HematTest.setOnTouchListener(new TestButtonTouchListener());
		
		button_ERSTest=(Button)findViewById(R.id.button_ERSTest);
		button_ERSTest.setOnClickListener(new button_ERSTestListener());
		button_ERSTest.setOnTouchListener(new TestButtonTouchListener());
		
		TestDownArea=(ImageView)findViewById(R.id.TestDownArea);
		TestDownArea.setOnTouchListener(new TestDownAreaTouchListener());
	}
	class TestButtonTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			Button bt=(Button)arg0;
			if(arg1.getAction()==MotionEvent.ACTION_DOWN)
			{
				//bt.setBackgroundResource(R.drawable.buttonbackupdownhui);
			}
			else if(arg1.getAction()==MotionEvent.ACTION_UP)
			{
				//bt.setBackgroundResource(R.drawable.buttonbackupdown);
			}
			return false;
		}
		
	}
	private int GetNumByXY(float x,float y)
	{
		
		int add_X=(int) (x/xSpace);
		int add_Y=(int) (y/ySpace);
		return add_Y*11+add_X;
	}
	private int GetAddressNum(int ad){
			
		ad-=11;
		if(ad>=0&&ad<=4)
			return ad;
		else if(ad>=6&&ad<=15)
			return ad-1;
		else if(ad>=17&&ad<=26)
			return ad-2;
		else if(ad>=28&&ad<=37)
			return ad-3;
		else if(ad>=39&&ad<=48)
			return ad-4;
		else if(ad>=49&&ad<=54)
			return ad-5;
		else
			return 0;
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
	class TestDownAreaTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			float x=arg1.getX();
			float y=arg1.getY();
			int num=GetNumByXY(x, y);
			int t=LastTouchNum;
			if(LastTouchNum>=50&&LastTouchNum<=106)
			{
				//MidData.LastArea=GetRect(LastTouchNum);
				
				//canvas.drawRect(MidData.LastArea[0], MidData.LastArea[1], MidData.LastArea[2], MidData.LastArea[3], oldAreaPaint);
				//int areaNum=GetAddressNum(LastTouchNum);
				//canvas.drawRect(MidData.LastArea[0], MidData.LastArea[1], MidData.LastArea[2], MidData.LastArea[3], oldAreaPaint);
				//TestUpArea.setImageBitmap(baseBitmap);
				//canvas.drawText(MidData.m_debugStatus[areaNum], MidData.LastArea[0]+10, MidData.LastArea[1]+25, textPaint);
				UpdateSelArea(LastTouchNum);
				
			}
			
			if(num%11!=5&&num>10)
			{
				//LastTouchNum=num;
			
				//MidData.NewArea=GetRect(num);
				
				//canvas.drawRect(MidData.NewArea[0], MidData.NewArea[1], MidData.NewArea[2], MidData.NewArea[3], newAreaPaint);
				//TestUpArea.setImageBitmap(baseBitmap);
				int newNum=GetAddressNum(num);
				LastTouchNum=newNum+50;
				MidData.SelAddress=newNum+50;
				UpdateSelArea(newNum+50);
				Intent curve=new Intent();
				Bundle b=new Bundle();
				b.putInt("通道号", LastTouchNum);
				b.putString("位置", GetAddress(LastTouchNum));
				curve.putExtras(b);
				curve.setClass(TestDownActivity.this, CurveActivity.class);
				startActivity(curve);
			}
			
			
			//canvas.drawText(fnum1.format(MidData.mTestParameters[newNum].aisleRecompenseHeight),MidData.NewArea[0]+10, MidData.NewArea[1]+25, textPaint);
			
			
			//Toast.makeText(getApplicationContext(), x+":"+y+","+num, Toast.LENGTH_LONG).show();
			
			return false;
		}
		
	}
	class button_ExitListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			MidData.isOnTestDown=false;
			for(int i=0;i<50;i++)
			{
				updatestatus[i]=0;
			}
			finish();
		}
		
	}
	class button_HematTestListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, please set up after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.IsReviseRunPeriod)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在调整运行周期，请稍后设置":"Adjusting running cycle, please wait", Toast.LENGTH_LONG).show();
				return;
			}
			for (int k = 0; k < 100; k++)
		    {
			if (MidData.mTestParameters[k].testStatus==1)
			{
			    Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在发现样本，请稍后设置！":"Detecting samples, please set later",
				    Toast.LENGTH_LONG).show();
			    return;
			}

		    }
			/*if(MidData.m_upDown.equals("上"))
			{
				Toast.makeText(getApplicationContext(), "请在下降过程中设置", Toast.LENGTH_LONG).show();
				return;
			}*/
			new AlertDialog.Builder(arg0.getContext())
		    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
		    .setCancelable(false)
		    .setMessage(MidData.IsChinese==1?"压积模式：后续插入的试管将作为压积测试。":"HCT mode: Subsequent tube will switch to HCT test")
		    .setPositiveButton(MidData.IsChinese==1?"确定":"Comfirm",
			    new DialogInterface.OnClickListener()
			    {

				public void onClick(
					DialogInterface dialoginterface, int i)
				{

				    byte order = 'A';
				    byte[] buff = new byte[2];
				    buff[0] = 'M';
				    buff[1] = 0x30;
				    MidData.DriRunTime = 0;
				    if (MidData.sOp.SendDataToSerial(order,
					    buff, 2))
				    {
					if (MidData.sqlOp.update(1,
						"SystemSetTable", "testtype",
						"1"))
					{
					   
					    MidData.mSetParameters.TestType = 1;
					    MidData.DriRunTime = 0;
					    button_HematTest
						    .setBackgroundResource(R.drawable.buttonbackupdownhui);
					    
					    button_ERSTest.setBackgroundResource(R.drawable.buttonbackupdown);
					    Toast.makeText(
						    getApplicationContext(),
						    MidData.IsChinese==1?"设置成功":"Setting Success", Toast.LENGTH_LONG)
						    .show();
					}
					else
					{
					    Toast.makeText(
						    getApplicationContext(),
						    MidData.IsChinese==1?"设置失败，请重新设置！":"Set failed, please re-set",
						    Toast.LENGTH_LONG).show();
					}
				    }
				    else
				    {
					Toast.makeText(getApplicationContext(),
							MidData.IsChinese==1?"设置失败(Re)，请重新设置！":"Set failed(Re), please re-set",
						Toast.LENGTH_LONG).show();
				    }
				}
			    }).setNegativeButton(MidData.IsChinese==1?"取消":"Cancel", null).show();
		}
		
	}
	class button_ERSTestListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, please set up after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.IsReviseRunPeriod)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在调整运行周期，请稍后设置":"Adjusting running cycle, please wait", Toast.LENGTH_LONG).show();
				return;
			}
			for (int k = 0; k < 100; k++)
		    {
			if (MidData.mTestParameters[k].testStatus==1)
			{
			    Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在发现样本，请稍后设置！":"Detecting samples, please set later",
				    Toast.LENGTH_LONG).show();
			    return;
			}

		    }
			/*if(MidData.m_upDown.equals("上"))
			{
				Toast.makeText(getApplicationContext(), "请在下降过程中设置", Toast.LENGTH_LONG).show();
				return;
			}*/
			new AlertDialog.Builder(arg0.getContext())
		    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
		    .setCancelable(false)
		    .setMessage(MidData.IsChinese==1?"血沉模式：后续插入的试管将作为血沉测试。":"ESR mode: Subsequent tube will switch to ESR test.")
		    .setPositiveButton(MidData.IsChinese==1?"确定":"Comfirm",
			    new DialogInterface.OnClickListener()
			    {
				public void onClick(
					DialogInterface dialoginterface, int i)
				{

				    byte order = 'A';
				    byte[] buff = new byte[2];
				    buff[0] = 'M';

				    if (MidData.isDebugging==1)
				    {
					buff[1] = 0x33;
					MidData.DriRunTime = 3;
				    }
				    else if (MidData.mSetParameters.ERSTestTime==0)
				    {
					buff[1] = 0x32;
					MidData.DriRunTime = 2;
				    }
				    else
				    {
					MidData.DriRunTime = 1;
					buff[1] = 0x31;
				    }
				    if (MidData.sOp.SendDataToSerial(order,
					    buff, 2))
				    {
					if (MidData.sqlOp.update(1,
						"SystemSetTable", "testtype",
						"0"))
					{
					   
					    MidData.mSetParameters.TestType = 0;
					    button_HematTest
						    .setBackgroundResource(R.drawable.buttonbackupdown);
					    
					    button_ERSTest.setBackgroundResource(R.drawable.buttonbackselbthui);
					    Toast.makeText(
						    getApplicationContext(),
						    MidData.IsChinese==1?"设置成功":"Setting Success", Toast.LENGTH_LONG)
						    .show();
					}
					else
					{
					    Toast.makeText(
						    getApplicationContext(),
						    MidData.IsChinese==1?"设置失败，请重新设置！":"Set failed, please re-set",
						    Toast.LENGTH_LONG).show();
					}
				    }
				    else
				    {
					Toast.makeText(getApplicationContext(),
							MidData.IsChinese==1?"设置失败(Re)，请重新设置！":"Set failed, please re-set",
						Toast.LENGTH_LONG).show();
				    }
				}
			    }).setNegativeButton(MidData.IsChinese==1?"取消":"Cancel", null).show();
		}
		
	}
	private void InitialAreaTest(){
		//创建一张空白图片
		baseBitmap=Bitmap.createBitmap((int)drawGridWidth,(int)drawGridHeight,Bitmap.Config.ARGB_8888);
		//创建一张画布
		canvas=new Canvas(baseBitmap);
		//画布背景灰色
		canvas.drawColor(Color.WHITE);
		
		//初始化划线画笔
		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		linePaint.setTextSize(12);
		linePaint.setTextAlign(Align.RIGHT);
		linePaint.setStyle(Style.STROKE);
		//初始化绘图画笔
		picPaint = new Paint();
		picPaint.setAntiAlias(true);
		picPaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		picPaint.setTextSize(12);
		picPaint.setTextAlign(Align.RIGHT);
		picPaint.setStyle(Style.STROKE);
		picPaint.setStrokeWidth(2);
		//初始化标签文本画笔
		textLabPaint = new Paint();
		textLabPaint.setAntiAlias(true);
		textLabPaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		textLabPaint.setTextSize(30);
		textLabPaint.setTextAlign(Align.CENTER);
		
		//初始化文本画笔
		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		textPaint.setTextSize(12);
		//初始化新选中画笔
		newAreaPaint = new Paint();
		newAreaPaint.setAntiAlias(true);
		newAreaPaint.setStyle(Style.FILL);
		newAreaPaint.setColor(Color.BLUE);// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		newAreaPaint.setTextSize(25);
		//初始化上次选中画笔
		oldAreaPaint = new Paint();
		oldAreaPaint.setStyle(Style.FILL);
		//textPaint.setAntiAlias(true);
		oldAreaPaint.setColor(Color.WHITE);// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		oldAreaPaint.setTextSize(25);	
		//初始化标签背景画笔
		labbackPaint = new Paint();
		labbackPaint.setStyle(Style.FILL);
		labbackPaint.setARGB(200, 168, 214, 235);
		//将灰色背景画上
		canvas.drawBitmap(baseBitmap, new Matrix(), backPaint);
		TestDownArea.setImageBitmap(baseBitmap);
		
		//标定坐标
		for(int i=0;i<10;i++)
		{
			int num=5+11*i;
			float[] rect=GetRectOut(num);
			
			canvas.drawRect(rect[0], rect[1], rect[2], rect[3], labbackPaint);
			if(i<5)
				canvas.drawText(Integer.toString(i+5), 30+5*xSpace, 50+(i+1)*ySpace, textLabPaint);
			else if(i>=5)
				canvas.drawText(Integer.toString(i+5), 30+5*xSpace, 50+(i+2)*ySpace, textLabPaint);
		}
		for(int i=0;i<10;i++)
		{
			
			if(i<5)
			{
				float[] rect=GetRectOut1(i);				
				canvas.drawRect(rect[0], rect[1], rect[2], rect[3], labbackPaint);
				canvas.drawText(datatoString(i), 30+i*xSpace, 50, textLabPaint);
			}
				
			else if(i>=5)
			{
				float[] rect=GetRectOut1(i+1);				
				canvas.drawRect(rect[0], rect[1], rect[2], rect[3], labbackPaint);
				canvas.drawText(datatoString(i), 30+(i+1)*xSpace, 50, textLabPaint);
			}
				
		}
		//画背景方格	
		for(int i=0;i<13;i++)
		{
			float axisY_h=ySpace*i;
			
			float axisX_v=xSpace*i;			
			//竖线
			
			canvas.drawLine(axisX_v, beginY, axisX_v, drawGridHeight+beginY,
					linePaint);
		}
		for(int i=0;i<7;i++)
		{
			float axisY_h=ySpace*i;
			//  横线
			canvas.drawLine(0, axisY_h, drawGridWidth, axisY_h,
					linePaint);
			
		}
		textLabPaint.setTextSize(35);
		canvas.drawText("X", 30+5*xSpace, 50, labbackPaint);
		LaveTestTimeThread mLaveTestTimeThread=new LaveTestTimeThread();
		mLaveTestTimeThread.start();
	}
	public class LaveTestTimeThread extends Thread{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			//float[] rect=GetRectIn(5);
			while (MidData.m_isInterrupted_LowerMachine&&MidData.isOnTestDown)
			{
				if(MidData.LaveTestTime>=0)
				{
					//MidData.LaveTestTime--;
					Message msg=new Message();
					Bundle b=new Bundle(); 
					b.putInt("状态", 1);
					b.putInt("编号", 999);
					msg.setData(b);
					TestDownActivity.this.mHandler.sendMessage(msg);
				}
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	private String datatoString(int t)
	{
		switch (t) {
		case 0:
			return "A";
		case 1:
			return "B";
		case 2:
			return "C";
		case 3:
			return "D";
		case 4:
			return "E";
		case 5:
			return "F";
		case 6:
			return "G";
		case 7:
			return "H";
		case 8:
			return "I";
		case 9:
			return "J";
		default:
			return null;

		}
	}
}
