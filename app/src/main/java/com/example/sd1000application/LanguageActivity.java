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

public class LanguageActivity extends Activity{
	private TextView textViewTitle=null;
	private RadioButton radioButtonChinese=null;
	private RadioButton radioButtonEnglish=null;
	private Button buttonSubmit=null;
	private Button buttonCancel=null;
	boolean change=false;
	boolean isCheckChange=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_language);
		textViewTitle=(TextView)findViewById(R.id.textViewTitle);
		change = true;
		textViewTitle.setText(MidData.IsChinese==1?"语言设置":"Language setting");
		radioButtonChinese=(RadioButton)findViewById(R.id.radioButtonChinese);
		radioButtonChinese.setText(MidData.IsChinese==1?"中文":"Chinese");
		radioButtonChinese.setOnCheckedChangeListener(new radioButtonChineseCheckedListener());
		radioButtonEnglish=(RadioButton)findViewById(R.id.radioButtonEnglish);
		radioButtonEnglish.setText(MidData.IsChinese==1?"英文":"English");
		radioButtonEnglish.setOnCheckedChangeListener(new radioButtonEnglishCheckedListener());
		if(MidData.IsChinese==1)
		{
			radioButtonChinese.setChecked(true);
			radioButtonEnglish.setChecked(false);
		}
		else
		{
			radioButtonChinese.setChecked(false);
			radioButtonEnglish.setChecked(true);
		}
		
		buttonSubmit=(Button)findViewById(R.id.buttonSubmit);
		buttonSubmit.setText(MidData.IsChinese==1?"确定":"Comfirm");
		buttonSubmit.setOnClickListener(new buttonSubmitClickListener());
		buttonSubmit.setOnTouchListener(new ButtonTouchListener());
		
		buttonCancel=(Button)findViewById(R.id.buttonCancels);
		buttonCancel.setText(MidData.IsChinese==1?"取消":"Cancel");
		buttonCancel.setOnClickListener(new buttonCancelClickListener());
		change = false;
	}
	class radioButtonEnglishCheckedListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			radioButtonChinese.setChecked(!arg1);
			radioButtonEnglish.setChecked(arg1);
			/*if(arg1&&!change)
			{
				change = true;
				Intent intent=new Intent();
				intent.setClass(LanguageActivity.this, LanguageChangeConfirmActivity.class);		
				Bundle bundle=new Bundle();
				bundle.putString("type", "English");
	
				bundle.putBoolean("result", arg1);
				intent.putExtras(bundle);
				startActivityForResult(intent,0);
			}
			if(!isCheckChange)
			{
				radioButtonChinese.setChecked(arg1);
				radioButtonEnglish.setChecked(!arg1);
				change=false;
			}*/
		}
		
	}
	class radioButtonChineseCheckedListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub

            radioButtonChinese.setChecked(arg1);
            radioButtonEnglish.setChecked(!arg1);
		    /*if(arg1&&!change)
			{
		    	change = true;
				Intent intent=new Intent();
				intent.setClass(LanguageActivity.this, LanguageChangeConfirmActivity.class);		
				Bundle bundle=new Bundle();
				bundle.putString("type", "Chinese");

				bundle.putBoolean("result", arg1);
				intent.putExtras(bundle);
				startActivityForResult(intent,0);
			}
		    if(!isCheckChange)
		    {
				radioButtonChinese.setChecked(!arg1);
				radioButtonEnglish.setChecked(arg1);
				change = false;
		    }*/
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
			if(radioButtonChinese.isChecked())
				value="1";
			else
				value="0";
			if(MidData.sqlOp.update(1, "SystemSetTable", "language", value))
			{				
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置成功，重启后生效":"Set success,please reboot", Toast.LENGTH_LONG).show();
				MidData.IsChinese=Integer.parseInt(value); //value.equals("1")?true:false;
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
			    	
			if(type.equals("English"))
			{
				change = true;
				isCheckChange=true;
				radioButtonEnglish.setChecked(result);
			    radioButtonChinese.setChecked(!result);
			    change = false;
			    isCheckChange=false;
			}
			else if (type.equals("Chinese")) {
				change = true;
				isCheckChange=true;
				radioButtonChinese.setChecked(result);
			    radioButtonEnglish.setChecked(!result);
			    change = false;
			    isCheckChange=false;
			} 
			break;
		default:
			break;
		}
	}
}
