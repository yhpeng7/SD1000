package com.example.sd1000application;

import com.realect.sd1000.parameterStatus.MidData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.TextView;

public class MessageShowActivity extends Activity{
	private TextView textView1;
	private TextView textView_Title;
	Intent intent;
	Bundle b;
	int type=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setFinishOnTouchOutside(false);
		
		setContentView(R.layout.activty_messageshow);
		textView1=(TextView)findViewById(R.id.textViewsoftwarelab);
		textView_Title=(TextView)findViewById(R.id.textView_Title);
		textView_Title.setText(MidData.IsChinese==1?"等待...":"Waiting...");
		intent=getIntent();
		b=intent.getExtras();
		type=b.getInt("type");
		if(type==1)
		{
			textView1.setText(MidData.IsChinese==1?"正在扫描全部，请稍候...":"Scanning all, please wait...");
		}
		if(type==2)
		{
			textView1.setText(MidData.IsChinese==1?"正在获取补偿，请稍候...":"Getting compensation, please wait...");
		}
		if(type==3)
		{
			textView1.setText(MidData.IsChinese==1?"正在调整运行周期，请稍候...":"Adjusting running cycle, please wait...");
		}
		Thread read=new Thread(new Runnable(){//更新界面显示的状态
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(type==1)
				{
					int time=0;
					boolean flag=false;
					while(true){
						if(time>10)
							break;
						time++;
						if(!MidData.isScallingAllFinish)
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						else
						{
							flag=true;
							break;
						}
							
					}
					if(flag)
					{
						intent.putExtra("type", Integer.toString(1));
						intent.putExtra("value", Integer.toString(1));
						MessageShowActivity.this.setResult(RESULT_OK, intent);
						MessageShowActivity.this.finish();
					}
					else
					{
						intent.putExtra("type", Integer.toString(1));
						intent.putExtra("value", Integer.toString(0));
						MessageShowActivity.this.setResult(RESULT_OK, intent);
						MessageShowActivity.this.finish();
					}
					
				}
				else if(type==2)
				{
					int time=0;
					boolean flag=false;
					while(true){
						if(time>(MidData.mSetParameters.RunPeriodSet*2))
							break;
						time++;
						if(!MidData.isGetRecomFinish)
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						else
						{
							flag=true;
							break;
						}							
					}
					if(flag)
					{
						intent.putExtra("type", Integer.toString(2));
						intent.putExtra("value", Integer.toString(1));
						MessageShowActivity.this.setResult(RESULT_OK, intent);
						MessageShowActivity.this.finish();
					}
					else
					{
						intent.putExtra("type", Integer.toString(2));
						intent.putExtra("value", Integer.toString(0));
						MessageShowActivity.this.setResult(RESULT_OK, intent);
						MessageShowActivity.this.finish();
					}
				}
				else if(type==3)
				{
					while(true){
						while(true){
							if(!MidData.isRunPeriodFinish)
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							else
								break;
						}
						intent.putExtra("type", Integer.toString(3));
						intent.putExtra("value", Integer.toString(1));
						MessageShowActivity.this.setResult(RESULT_OK, intent);
						MessageShowActivity.this.finish();
					}
				}				
			}			
		});
		read.start();
	}
	public boolean dispatchKeyEvent(KeyEvent event)
    {
	// TODO Auto-generated method stub
	if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
	{
	    return true;
	}
	else
	    return super.dispatchKeyEvent(event);
    }
}
