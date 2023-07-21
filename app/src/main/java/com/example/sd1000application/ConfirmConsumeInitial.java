package com.example.sd1000application;

import com.example.sd1000application.PasswordConfirmActivity.ButtonTouchListener;
import com.example.sd1000application.PasswordConfirmActivity.CancelListener;
import com.example.sd1000application.PasswordConfirmActivity.SubmitListener;
import com.realect.sd1000.parameterStatus.MidData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmConsumeInitial extends Activity{
	private EditText editText_password=null;
	private Button button_submit=null;
	private Button button_cancel=null;
	private TextView textView_Title=null;
	Bundle bunde;
	String typeStr;
	boolean result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_confirmconsumeinitial);
		LoadXML();
	}
	private void LoadXML()
	{
		button_cancel=(Button)findViewById(R.id.button_cancel);
		button_cancel.setOnClickListener(new CancelListener());
		button_cancel.setOnTouchListener(new ButtonTouchListener());
		
		button_submit=(Button)findViewById(R.id.button_submit);
		button_submit.setOnClickListener(new SubmitListener());
		button_submit.setOnTouchListener(new ButtonTouchListener());
		
		editText_password=(EditText)findViewById(R.id.editText_password);
		editText_password.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		button_submit.requestFocus();
		button_submit.setText(MidData.IsChinese==1?"确定":"Comfirm");
		button_cancel.setText(MidData.IsChinese==1?"取消":"Cancel");
		textView_Title=(TextView)findViewById(R.id.textView_Title);
		textView_Title.setText(MidData.IsChinese==1?"确认密码":"Confirm Password");
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
	
			
			String numtext = editText_password.getText().toString().trim();
		    if (numtext.equals("1039"))// 4287654321测试态
		    {
		    	MidData.sqlOp
			      .UpdateData(1,
				    "ConsumableInforTable",
				    new String[]{"consumxuechencount","consumyajicount"},
				    new String[]{"20","20"},2);
		    	finish();
		    }
		    else
		    {
		    	editText_password.setText("");
		    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"密码错误，请重新设置！":"Password error, please reset",
					    Toast.LENGTH_LONG).show();
		    }
		}		
	}
}
