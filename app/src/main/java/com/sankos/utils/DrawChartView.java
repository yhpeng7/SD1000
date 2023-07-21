package com.sankos.utils;

import com.realect.sd1000.parameterStatus.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.View;

public class DrawChartView extends View
{
    private String[] xGraduation;
    private String[] yGraduation;
    private double[] dataPointsX;
    private double[] dataPointsY;
    private double minXData = 0.0;
    private double maxXData = 0.0;
    private double minYData = 0.0;
    private double maxYData = 0.0;
    private int dataLen = 0;
    @SuppressWarnings("unused")
    private PathEffect dashedEffect = new DashPathEffect(new float[]
    { 6, 6, 6, 6, 6 }, 2);

    public DrawChartView(Context context)
    {
	super(context);
    }

    public DrawChartView(Context context, AttributeSet attrs)
    {
	super(context, attrs);
    }

    public DrawChartView(Context context, AttributeSet attrs, int defStyle)
    {
	super(context, attrs, defStyle);
    }

    public void setData(String[] xData, String[] yData, double[] drawDataX,
	    double[] drawDataY, int len)
    {
	xGraduation = xData;
	yGraduation = yData;
	dataPointsX = drawDataX;
	dataPointsY = drawDataY;
	dataLen = len;
	minXData = Double.parseDouble(xData[0]);
	minYData = Double.parseDouble(yData[0]);
	for (String temp : yData)
	{
	    if (Double.parseDouble(temp) <= minYData)
	    {
		minYData = Double.parseDouble(temp);
	    }
	    // System.out.println(temp+"  __Ydata");
	}

	for (String temp : xData)
	{
	    if (Double.parseDouble(temp) <= minXData)
	    {
		minXData = Double.parseDouble(temp);
	    }
	    // System.out.println(temp+"  __Xdata");
	}
	maxXData = Double.parseDouble(xData[xData.length - 1]);
	maxYData = Double.parseDouble(yData[yData.length - 1]);

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas)
    {
	super.onDraw(canvas);

	float axisX = 30;
	float axisY = 225;// getHeight() - 40;

	float drawGridWidth = 275;// getWidth() - 30;//缂備焦锚閸╂锟介挊澶婎唺
	float drawGridHeight = 170;// getHeight() - 40;//缂備焦锚閸╂顨炲Ο鍝勵唺

	float beginX = axisX + 41;

	float xSpace = (drawGridWidth - beginX) / 5;
	float ySpace = drawGridHeight / 5;

	Paint linePaint = new Paint();
	linePaint.setAntiAlias(true);
	linePaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
	linePaint.setTextSize(12);
	linePaint.setTextAlign(Align.RIGHT);

	// 缂備焦锚閸╂閺夌儑鎷�
	canvas.drawLine(axisX, axisY, axisX, axisY - drawGridHeight + 30,
		linePaint);// Y杞�

	// 缂備焦锚閸╂螣椤忓嫭鍊婚柛鎺戞濞堁呯棯閹稿孩钂�
	float startDrawx = axisX;
	float endDrawx = drawGridWidth - 80;
	float xAxis;
	float yAxis;
	float minDataXpoint = 0;
	float maxDataXpoint = 0;
	float minDataYpoint = 0;
	float maxDataYpoint = 0;
	for (int n = 0; n < yGraduation.length; n++)
	{
	    yAxis = axisY - n * ySpace;
	    if (n == 0)
	    {
		maxDataYpoint = yAxis;
	    }
	    if (n == yGraduation.length - 1)
	    {
		minDataYpoint = yAxis;
	    }
	    xAxis = axisX - n * xSpace;
	    if (n == 0)
	    {
		maxDataXpoint = xAxis;
	    }
	    if (n == xGraduation.length - 1)
	    {
		minDataXpoint = xAxis;
	    }
	    canvas.drawLine(startDrawx, yAxis, endDrawx, yAxis, linePaint);
	    canvas.drawText(yGraduation[n], startDrawx - 5, yAxis, linePaint);
	}
	/*int add = ((99 - MidData.SelNumNow) % 10) * 10
		+ (99 - MidData.SelNumNow) / 10;*/

	/*if (MidData.isOnMain 
		&& MidData.SelAddress >= 0
		&& MidData.SelAddress < 100
		&& MidData.passageStatus[MidData.SelAddress].equals("妫�祴瀹屾垚"))
	{
	    if (MidData.IsESRSelect[MidData.SelAddress])
	    {
		 canvas.drawText("琛�矇娴嬭瘯(mm/h)  "
			    + MidData.Difference_First_Last[MidData.SelAddress],
			    startDrawx + 125, 70, linePaint);
	    }else
	    {
		canvas.drawText("琛�矇娴嬭瘯(mm/h)", startDrawx + 93, 70, linePaint);
	    }
	   
	}
	else
	{
	    canvas.drawText("琛�矇娴嬭瘯(mm/h)", startDrawx + 93, 70, linePaint);
	}*/

	// canvas.drawText("鐞涳拷鐭囧ù瀣槸(mm/h)", startDrawx+93, 70, linePaint);
	// 缂備焦锚閸╂螣椤忓嫭缍忛柡宥忔嫹
	linePaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
	linePaint.setTextAlign(Align.CENTER);
	float xTextPoint;
	yAxis = axisY + 20;
	for (int n = 0; n < xGraduation.length; n++)
	{
	    xTextPoint = beginX + n * xSpace;
	    canvas.drawText(xGraduation[n], xTextPoint, yAxis, linePaint);
	    canvas.drawCircle(xTextPoint, yAxis - 20, 2, linePaint);
	}

	// 鐎殿噯鎷烽‖濠勭磼濡搫鐓戦柟鑸殿焽閸わ拷
	linePaint.setStyle(Style.FILL);// 閻庡湱鍋涚缓楣冨捶閸℃劧鎷�
	linePaint.setStrokeWidth(1);// 缂佹儳鐏濋锟�
	linePaint.setColor(Color.parseColor("#0033CC"));// 缂佹儳娼￠·渚�嚌鐏炲偊鎷�

	float currentPointX;
	float currentPointY;
	float lastPointX = 0;
	float lastPointY = 0;

	// canvas.drawLine(axisX, axisY+0, axisX+15,axisY-40, linePaint);
	// canvas.drawLine(axisX+30, axisY-30, axisX+60,axisY-80, linePaint);

	// dataLen=4;
	// dataPointsX=new double [4];
	// dataPointsY=new double [4];
	//
	// dataPointsX[0]=0;dataPointsY[0]=0;
	// dataPointsX[1]=15;dataPointsY[1]=40;
	// dataPointsX[2]=30;dataPointsY[2]=40;
	// dataPointsX[3]=30;dataPointsY[3]=120;
	for (int n = 0; n < dataLen; n++) // 鐢荤偣
	{

	    currentPointX = (float) (beginX + (((dataPointsX[n] - minXData) / (maxXData - minXData)) * (maxDataXpoint - minDataXpoint)));
	    currentPointY = (float) (maxDataYpoint - (((dataPointsY[n] - minYData) / (maxYData - minYData)) * (maxDataYpoint - minDataYpoint)));
	    // System.out.println(String.valueOf(dataPointsY[n])+"  __ydata");
	    if (n == 0)
	    {
		lastPointX = currentPointX;
		lastPointY = currentPointY;
	    }
	    canvas.drawLine(lastPointX, lastPointY, currentPointX,
		    currentPointY, linePaint);

	    lastPointX = currentPointX;
	    lastPointY = currentPointY;
	}
    }
}
