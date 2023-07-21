package com.sankos.utils;

import java.text.DecimalFormat;

import com.example.sd1000application.R;
import com.realect.sd1000.parameterStatus.MidData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class DrawDebugView extends View{
	Canvas m_canvas;
	float drawGridWidth = 480;//canvas.getWidth();//480;// getWidth() - 30;//缂備焦锚閸╂锟介挊澶婎唺
	float drawGridHeight = 430;//canvas.getHeight();//450;// getHeight() - 40;//缂備焦锚閸╂顨炲Ο鍝勵唺

	float beginX = 0;
	float beginY = 38;
	
	float xSpace = (drawGridWidth)  / 11;
	float ySpace = (drawGridHeight) / 11;
	private DecimalFormat fnum1 = new DecimalFormat("##0.0");
	public DrawDebugView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	 public DrawDebugView(Context context, AttributeSet attrs)
    {
	super(context, attrs);
    }

    public DrawDebugView(Context context, AttributeSet attrs, int defStyle)
    {
	super(context, attrs, defStyle);
    }
    int xxxxxxxxxxx=0;
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
//		if(xxxxxxxxxxx==0)
//		{
//		MidData.m_canvas=canvas;
//		xxxxxxxxxxx=1;
//		}
//		
		

		Paint linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		linePaint.setTextSize(12);
		linePaint.setTextAlign(Align.RIGHT);
		linePaint.setStyle(Style.STROKE);
		for(int i=0;i<13;i++)
		{
			float axisY_h=ySpace*i;
			
			float axisX_v=xSpace*i;
			//  横线
			canvas.drawLine(0, axisY_h, drawGridWidth, axisY_h,
					linePaint);
			//竖线
			
			canvas.drawLine(axisX_v, beginY, axisX_v, drawGridHeight+beginY,
					linePaint);
		}
		
		Paint textPaint = new Paint();
		//textPaint.setAntiAlias(true);
		textPaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		textPaint.setTextSize(25);
		//textPaint.setTextAlign(Align.CENTER);
		//textPaint.setStyle(Style.STROKE);
		for(int i=0;i<10;i++)
		{
			if(i<5)
				canvas.drawText(Integer.toString(i), 230, 70+i*ySpace, textPaint);
			else if(i>=5)
				canvas.drawText(Integer.toString(i), 230, 70+(i+1)*ySpace, textPaint);
		}
		for(int i=0;i<10;i++)
		{
			
			if(i<5)
				canvas.drawText(datatoString(i), 15+i*xSpace, 70+5*ySpace, textPaint);
			else if(i>=5)
				canvas.drawText(datatoString(i), 15+(i+1)*xSpace, 70+5*ySpace, textPaint);
		}
		textPaint.setTextSize(35);
		canvas.drawText("X", 10+5*xSpace, 75+5*ySpace, textPaint);
		//invalidate();
		//textPaint.setTextSize(40);
		//canvas.drawText("1", 230, 110, textPaint);
		//textPaint.setTextSize(80);
		//canvas.drawText("2", 230, 150, textPaint);
		/*Paint newPaint = new Paint();
		newPaint.setAntiAlias(true);
		newPaint.setColor(Color.parseColor("#0000cc"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		newPaint.setTextSize(25);
		MidData.m_canvas.drawRect(50, 50, 100, 200, newPaint);*/
		
	}
	public void ReBackArea(float[] rect)
	{
		m_canvas.save();
		m_canvas.clipRect(rect[0], rect[1], rect[2], rect[3]);
		Paint backPaint = new Paint();
		//textPaint.setAntiAlias(true);
		backPaint.setColor(Color.parseColor("#cccccc"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		backPaint.setTextSize(25);
		m_canvas.drawRect(rect[0], rect[1], rect[2], rect[3], backPaint);
		m_canvas.restore();
	}
	public void NewSelArea(float[] rect)
	{
		//super.onDraw(MidData.m_canvas);
		/*MidData.m_canvas.save();
		MidData.m_canvas.clipRect(rect[0], rect[1], rect[2], rect[3]);
		Paint newPaint = new Paint();
		newPaint.setAntiAlias(true);
		newPaint.setColor(Color.parseColor("#0000cc"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		newPaint.setTextSize(25);
		MidData.m_canvas.drawRect(rect[0], rect[1], rect[2], rect[3], newPaint);
		//MidData.m_canvas.drawRect(50, 50, 100, 200, newPaint);
		
		MidData.m_canvas.restore();*/
		draw(m_canvas);
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
	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		//super.draw(canvas);
		
		if(xxxxxxxxxxx==0)
		{
		//MidData.m_canvas=
			/*LinearLayout mm=(LinearLayout)findViewById(R.id.DebugArea);
			mm.l
		xxxxxxxxxxx=1;*/
		}
		//MidData.m_canvas=canvas;
		
		
		Paint newPaint = new Paint();
		newPaint.setAntiAlias(true);
		newPaint.setColor(Color.parseColor("#0000cc"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		newPaint.setTextSize(25);
		Paint backPaint = new Paint();
		//textPaint.setAntiAlias(true);
		backPaint.setColor(Color.parseColor("#cccccc"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		backPaint.setTextSize(25);
		
		Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.parseColor("#000000"));// 闁告帟顕х�宕囩棯婢跺摜鐟閺夌偛鐡ㄩ弸鍐拷濡ゅ拋鏉归柤鐧告嫹
		textPaint.setTextSize(12);
		textPaint.setTextAlign(Align.CENTER);
		//MidData.m_canvas.drawRect(rect[0], rect[1], rect[2], rect[3], newPaint);
		if(MidData.fgCan==1)
		{
			m_canvas.drawRect(MidData.LastArea[0],MidData.LastArea[1],MidData.LastArea[2],MidData.LastArea[3], newPaint);
		}
			
		else if(MidData.fgCan==2)
		{
			m_canvas.drawRect(MidData.NewArea[0],MidData.NewArea[1],MidData.NewArea[2],MidData.NewArea[3], newPaint);
			//MidData.fgCan=0;
		}
			
		else if(MidData.fgCan==3)
		{
			int len=0;
			for(int i=0;i<11;i++)
			{
				for(int j=0;j<11;j++)
				{
					if((i<5&&j<5)||(i>5&&j<5)||(i<5&&j>5)||(i>5&&j>5))
					{
						//canvas.drawText(datatoString(i), 15+(i+1)*xSpace, 70+5*ySpace, textPaint);
						m_canvas.drawText(fnum1.format(MidData.mTestParameters[len++].aisleRecompenseHeight), j*xSpace+20, i*ySpace+beginY+25, textPaint);
						
					}
					
					
				}
			}
			//MidData.fgCan=0;
		}
		else if(MidData.fgCan==4)
		{
			int len=0;
			for(int i=0;i<11;i++)
			{
				for(int j=0;j<11;j++)
				{
					if((i<5&&j<5)||(i>5&&j<5)||(i<5&&j>5)||(i>5&&j>5))
					{
						//canvas.drawText(datatoString(i), 15+(i+1)*xSpace, 70+5*ySpace, textPaint);
						m_canvas.drawText(MidData.m_debugStatus[len++], j*xSpace+20, i*ySpace+beginY+25, textPaint);
						
					}
					
					
				}
			}
			//MidData.fgCan=0;
		}
	}

}
