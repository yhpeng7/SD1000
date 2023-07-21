package com.example.sd1000application;

import java.text.DecimalFormat;

import com.realect.sd1000.Calculate.CalculateFormula;
import com.realect.sd1000.parameterStatus.*;

import android.app.Activity;
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
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class DataTouchShowActivity extends Activity{
	Intent t;
	Bundle b;
	String num;
	String time;
	String add;
	String ERS;
	String Hemat;
	String tem;
	int ID;
	private ImageView CurveArea;
	private DecimalFormat fnum10 = new DecimalFormat("##0000000000");
	private DecimalFormat fnum1 = new DecimalFormat("##0.0");
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
	private TextView textView_SampleNum;
	private TextView textView_TestTime;
	private TextView textView_TestAdd;
	private TextView textView_RESResult;
	private TextView textView_HematResult;
	private TextView textView_Tem;
	private TextView textView_Title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		
		setContentView(R.layout.activity_datatouchshow);
		//getActionBar().setTitle(MidData.IsChinese?"结果显示":"Result show");
		
		t=getIntent();
		b=t.getExtras();
		num=b.getString("编号");
		time=b.getString("时间");
		add=b.getString("位置");
		ERS=b.getString("血沉结果");
		Hemat=b.getString("压积结果");
		tem=b.getString("温度");
		ID=b.getInt("ID");
		LoadXML();
		LoadData();
		InitialChart();
		DrawChart();
	}
	private void DrawChart(){
		String[] getStr =
		    { "resourcedata", "xuechendata", "yajidata", "xuechentesttimetype",
			    "plusperiod", "isdebug", "temp" };

		    getStr = MidData.sqlOp.selectHistoryById(getStr, ID, 7);
		    String xuechenvalues = getStr[0];
		    if (xuechenvalues.equals(""))
		    {
			return;
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
		    mul = (float) 60 / (float) plusperiod;
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
		    double[] xDataV = new double[(int) ((tempxuechenvalues.length / mul) - 1)];
		    double[] yDataV = new double[(int) ((tempxuechenvalues.length / mul) - 1)];
		    int lenOfData=(int) ((tempxuechenvalues.length / mul) - 1);
		    for (int i = 0; i < lenOfData; i++)
		    {
			xDataV[i] = xData[i];
			yDataV[i] = yData[0] - yData[i + 1];
			if (yDataV[i] < 0)
			    yDataV[i] = 0;
		    }
		    if (MidData.mSetParameters.IsCurveFit == 1)
		    {
				for (int i = 0; i < lenOfData; i++)
				{
				    yDataV[i] = (double) CalculateFormula.CurveFitting("0",
					    (float) yDataV[i], debugflag?1:0, testtime);
				   /* if (yDataV[i] > 160)
					yDataV[i] = 160;*/
				    if (MidData.mSetParameters.IsAutoTemResive==1)
				    {
					yDataV[i] = (double) CalculateFormula.TemReviseByGivenTemp(
						(float) yDataV[i],
						Double.parseDouble(getStr[6]));
					if (yDataV[i] < 1)
					    yDataV[i] = 1;
				    }
				    else
				    {
					if (yDataV[i] < 0)
					    yDataV[i] = 0;
				    }
				    if (yDataV[i] < 1)
					    yDataV[i] = 1;
				    else if (yDataV[i] > 160)
						yDataV[i] = 160;
				}
		    }
		    for (int i = 0; i < lenOfData; i++)
			{
			    xDataV[i] = xDataV[i]*(xSpace/15)+beginX;
			    yDataV[i] =200-yDataV[i]*(ySpace/40);
			}
			Path path=new Path();
			
			path.moveTo((float)xDataV[0], (float)yDataV[0]);
			//str=Double.toString(xData[0])+","+Double.toString(yData[0])+";";
			for (int i = 0; i < lenOfData; i++)
			{
				path.lineTo((float)xDataV[i], (float)yDataV[i]);
				//str+=Double.toString(xData[i]+beginX)+","+Double.toString(yData[i]+beginY)+";";
			}
			canvas.drawPath(path, chartPaint);
			//System.out.println(str+"-----------chartPoint");
			CurveArea.setImageBitmap(baseBitmap);
	}
	private void LoadData(){
		textView_Title.setText(MidData.IsChinese==1?"结果显示":"Result show");
		textView_SampleNum.setText(MidData.IsChinese==1?"编号:"+num:"ID:"+num);
		textView_TestTime.setText(MidData.IsChinese==1?"时间:"+time:"Time:"+time);
		textView_TestAdd.setText(MidData.IsChinese==1?"测试位:"+add:"Position:"+add);
		textView_RESResult.setText(MidData.IsChinese==1?"血沉结果(mm/h):"+ERS:"ESR(mm/h):"+ERS);
		textView_HematResult.setText(MidData.IsChinese==1?"压积结果(%):"+Hemat:"HCT(%):"+Hemat);
		textView_Tem.setText(MidData.IsChinese==1?"温度(℃):"+tem:"Temp(℃)"+tem);
	}
	private void LoadXML(){
		CurveArea=(ImageView)findViewById(R.id.CurveArea);
		textView_SampleNum=(TextView)findViewById(R.id.textView_SampleNum);
		textView_TestTime=(TextView)findViewById(R.id.textView_TestTime);
		textView_TestAdd=(TextView)findViewById(R.id.textView_TestAdd);
		textView_RESResult=(TextView)findViewById(R.id.textView_RESResult);
		textView_HematResult=(TextView)findViewById(R.id.textView_HematResult);
		textView_Tem=(TextView)findViewById(R.id.textView_Tem);
		textView_Title=(TextView)findViewById(R.id.textView_Title);
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
