package com.example.sd1000application;


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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ConsumeControlActivity extends Activity{
	private TextView textViewTitle=null;
	private RadioButton radioButtonOn=null;
	private RadioButton radioButtonOff=null;
	private Button buttonSubmit=null;
	private Button buttonCancel=null;
	boolean change=false;
	boolean isCheckChange=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_consumecontrol);
		textViewTitle=(TextView)findViewById(R.id.textViewTitle);
		change = true;
		isCheckChange=true;
		textViewTitle.setText(MidData.IsChinese==1?"耗材封闭":"Consumables decline");
		radioButtonOn=(RadioButton)findViewById(R.id.radioButtonOn);
		radioButtonOn.setText(MidData.IsChinese==1?"是":"Yes");
		radioButtonOn.setOnCheckedChangeListener(new radioButtonOnCheckedListener());
		radioButtonOff=(RadioButton)findViewById(R.id.radioButtonOff);
		radioButtonOff.setText(MidData.IsChinese==1?"否":"No");
		radioButtonOff.setOnCheckedChangeListener(new radioButtonOffCheckedListener());
		if(MidData.IsConsumerReduce)
		{
			radioButtonOn.setChecked(true);
			radioButtonOff.setChecked(false);
		}
		else
		{
			radioButtonOn.setChecked(false);
			radioButtonOff.setChecked(true);
		}
		
		buttonSubmit=(Button)findViewById(R.id.buttonSubmit);
		buttonSubmit.setText(MidData.IsChinese==1?"确定":"Comfirm");
		buttonSubmit.setOnClickListener(new buttonSubmitClickListener());
		buttonSubmit.setOnTouchListener(new ButtonTouchListener());
		
		buttonCancel=(Button)findViewById(R.id.buttonCancels);
		buttonCancel.setText(MidData.IsChinese==1?"取消":"Cancel");
		buttonCancel.setOnClickListener(new buttonCancelClickListener());
		change = false;
		isCheckChange=false;
	}
	class radioButtonOnCheckedListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if(arg1&&!change)
			{
				change = true;
				Intent intent=new Intent();
				intent.setClass(ConsumeControlActivity.this, PasswordConfirmActivity.class);		
				Bundle bundle=new Bundle();
				bundle.putString("type", "On");
	
				bundle.putBoolean("result", arg1);
				intent.putExtras(bundle);
				startActivityForResult(intent,0);
			}
			if(!isCheckChange)
			{
				radioButtonOn.setChecked(!arg1);
				radioButtonOff.setChecked(arg1);
				change=false;
			}
		}
		
	}
	class radioButtonOffCheckedListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub

		    if(arg1&&!change)
			{
		    	change = true;
				Intent intent=new Intent();
				intent.setClass(ConsumeControlActivity.this, PasswordConfirmActivity.class);		
				Bundle bundle=new Bundle();
				bundle.putString("type", "Off");

				bundle.putBoolean("result", arg1);
				intent.putExtras(bundle);
				startActivityForResult(intent,0);
			}
		    if(!isCheckChange)
		    {
				radioButtonOn.setChecked(arg1);
				radioButtonOff.setChecked(!arg1);
				change = false;
		    }
		}
		
	}
	class ButtonTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			Button btn=(Button)arg0;
			if(arg1.getAction()==MotionEvent.ACTION_DOWN)
			{
				btn.setBackgroundResource(R.drawable.buttonbackcancel);
			}
			else if(arg1.getAction()==MotionEvent.ACTION_UP)
			{
				btn.setBackgroundResource(R.drawable.buttonbacksel);
			}
			return false;
		}
		
	}
	class buttonSubmitClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String value;
			if(radioButtonOn.isChecked())
				value="1";
			else
				value="0";
			if(MidData.sqlOp.update(1, "SystemSetTable", "consumerreduce", value))
			{
				
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置成功，重启后生效":"Set success,please reboot", Toast.LENGTH_LONG).show();
				MidData.IsConsumerReduce=value.equals("1")?true:false;
				finish();
			}
			else
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置失败，请重新设置":"Set failed,please reset", Toast.LENGTH_LONG).show();
				
			}
		}
		
	}
	class buttonCancelClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
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
			boolean result=bundle.getBoolean("result");
			    	
			if(type.equals("On"))
			{
				change = true;
				isCheckChange=true;
				radioButtonOn.setChecked(result);
			    radioButtonOff.setChecked(!result);
			    change = false;
			    isCheckChange=false;
			}
			else if (type.equals("Off")) {
				change = true;
				isCheckChange=true;
				radioButtonOff.setChecked(result);
			    radioButtonOn.setChecked(!result);
			    change = false;
			    isCheckChange=false;
			} 
			break;
		default:
			break;
		}
	}
}
