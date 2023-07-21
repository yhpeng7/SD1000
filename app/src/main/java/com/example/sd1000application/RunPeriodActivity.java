package com.example.sd1000application;

import com.realect.sd1000.Calculate.CalculateFormula;
import com.realect.sd1000.parameterStatus.MidData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class RunPeriodActivity extends Activity{
	private Button button_runperiodCancel=null;
	private Button button_runperiodSubmit=null;
	private EditText textViewTime=null;
	private Button imageButtonAdd=null;
	private Button imageButtonPlus=null;
	private TextView textView_Title=null;
	private int timeSet=0;
	private int min=15;
	private int max=60;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_runperiod);
		LoadXML();
		LoadLastSet();
	}
	private void LoadLastSet()
	{
		timeSet=MidData.mSetParameters.RunPeriodSet;
		textViewTime.setText(Integer.toString(timeSet)+"(s)");
		button_runperiodCancel.setText(MidData.IsChinese==1?"取消":"Cancel");
		button_runperiodSubmit.setText(MidData.IsChinese==1?"确定":"Comfirm");
		textView_Title.setText(MidData.IsChinese==1?"运行周期":"Running cycle");
	}
	
	private void LoadXML()
	{
		textView_Title=(TextView)findViewById(R.id.textView_Title);
		textViewTime=(EditText)findViewById(R.id.textViewLab);
		imageButtonAdd=(Button)findViewById(R.id.imageButtonAdd);
		imageButtonAdd.setOnClickListener(new imageButtonAddListener());
		imageButtonAdd.setOnTouchListener(new SetButtonTouchListener());
		
		imageButtonPlus=(Button)findViewById(R.id.imageButtonPlus);
		imageButtonPlus.setOnClickListener(new imageButtonPlusListener());
		imageButtonPlus.setOnTouchListener(new SetButtonTouchListener());
		
		button_runperiodSubmit=(Button)findViewById(R.id.button_runperiodSubmit);
		button_runperiodSubmit.setOnClickListener(new SubmitListener());
		button_runperiodSubmit.setOnTouchListener(new ButtonTouchListener());
		
		button_runperiodCancel=(Button)findViewById(R.id.button_runperiodCancel);
		button_runperiodCancel.setOnClickListener(new CancelListener());
		button_runperiodCancel.setOnTouchListener(new ButtonTouchListener());
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
	class SetButtonTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			if(arg0.getId()==R.id.imageButtonAdd)
			{
				if(arg1.getAction()==MotionEvent.ACTION_DOWN)
				{
					imageButtonAdd.setBackgroundResource(R.drawable.righthui);
				}
				else  if(arg1.getAction()==MotionEvent.ACTION_UP)
				{
					imageButtonAdd.setBackgroundResource(R.drawable.right);
				}
			}
			else if(arg0.getId()==R.id.imageButtonPlus)
			{
				if(arg1.getAction()==MotionEvent.ACTION_DOWN)
				{
					imageButtonPlus.setBackgroundResource(R.drawable.lefthui);
				}
				else  if(arg1.getAction()==MotionEvent.ACTION_UP)
				{
					imageButtonPlus.setBackgroundResource(R.drawable.left);
				}
			}
			return false;
		}
		
	}
	class CancelListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
	class SubmitListener implements OnClickListener{

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
			if (timeSet > max
				    || timeSet < min)
			    {
				Toast.makeText(getApplicationContext(),
						MidData.IsChinese==1?"运行周期的有效范围是15-60，当前设置超出有效范围":"Effective range is 15-60 operating cycle, the current setting is out of range", Toast.LENGTH_LONG)
					.show();
				return;
			    }
			    
			    /*if (MidData.m_upDown.equals("上"))
			    {
				Toast.makeText(getApplicationContext(), "正在测试，请在下降时设置！",
					Toast.LENGTH_LONG).show();
				return;
			    }*/
			  
			  
			    byte order = 'A';
			    CalculateFormula tc = new CalculateFormula();
			    byte[] PulsPeroid = tc.StringTo2Bytes(timeSet);
			    byte[] PulsPeroidSend = new byte[3];
			    PulsPeroidSend[0] = 'V';
			    PulsPeroidSend[1] = PulsPeroid[0];
			    PulsPeroidSend[2] = PulsPeroid[1];
			    MidData.m_pulsePeriodSetLast = timeSet;
			    // if(MidData.PulsePeriodSet!=PulsePeriodSetNow)
			    // {
			    if (!MidData.sOp.SendDataToSerial(order, PulsPeroidSend,
				    3))
			    {
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置失败":"Set failed, please re-set",
					Toast.LENGTH_LONG).show();
				return;
			    }
			    // }
			    
			    
			if(MidData.sqlOp.update(1, "SystemSetTable", "pulseperiodset", Integer.toString(timeSet)))
			{
				if (MidData.isDebugging==1)
				{
				    MidData.DirveRunTestTime = (double) (5 * 60)
					    / (double) MidData.mSetParameters.RunPeriodSet;
				}
				else if(MidData.isDebugging==0)
				{
				    MidData.DirveRunTestTime = MidData.mSetParameters.ERSTestTime==0 ? ((60 * 60) / MidData.mSetParameters.RunPeriodSet)
					    : ((60 * 30) / MidData.mSetParameters.RunPeriodSet);

				}
				MidData.IsReviseRunPeriod = true;
				MidData.mSetParameters.RunPeriodSet=timeSet;
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置成功":"Setting Success", Toast.LENGTH_LONG).show();
				 Intent intent = new Intent();
				    intent.setClass(RunPeriodActivity.this,
					    MessageShowActivity.class);
				    MidData.isRunPeriodFinish=false;
				    Bundle bundle = new Bundle();
				    bundle.putInt("type", 3);
				   
				    intent.putExtras(bundle);
				    startActivityForResult(intent, 0);
				//finish();
			}
			else
			{
				Toast.makeText(getApplicationContext(),MidData.IsChinese==1? "设置失败":"Set failed, please re-set", Toast.LENGTH_LONG).show();
			}
		}
		
	}
	class imageButtonAddListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(timeSet>=60)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"运行周期最大为60s":"Max running cycle is 60s", Toast.LENGTH_LONG).show();
				return;				
			}
			
			timeSet++;
			textViewTime.setText(Integer.toString(timeSet)+"(s)");
		}
		
	}
	class imageButtonPlusListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(timeSet<=15)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"运行周期最小为15s":"Min running cyle is 15s", Toast.LENGTH_LONG).show();
				return;				
			}
			timeSet--;
			textViewTime.setText(Integer.toString(timeSet)+"(s)");			
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case RESULT_OK:
			Bundle bundle = data.getExtras();
			
			String type = bundle.getString("type");
			String value = bundle.getString("value");
			if (type.equals("3")) {
				if(value.equals("1"))
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"调整运行周期完成":"Adjust running cycle finished", Toast.LENGTH_LONG).show();
					finish();
			} 
			break;
		default:
			break;
		}
	}	
}
