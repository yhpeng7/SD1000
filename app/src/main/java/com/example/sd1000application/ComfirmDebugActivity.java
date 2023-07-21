package com.example.sd1000application;

import com.realect.sd1000.parameterStatus.*;

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

public class ComfirmDebugActivity extends Activity{
	private EditText editText_password=null;
	private Button button_submit=null;
	private Button button_cancel=null;
	private TextView textView_Title=null;
	Intent t;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.activity_comfirmdebug);
		//getWindow().setTitle(MidData.IsChinese?"��������":"Debug Password");
		LoadXML();
		t=getIntent();
		
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
		button_submit.setText(MidData.IsChinese==1?"ȷ��":"Comfirm");
		button_cancel.setText(MidData.IsChinese==1?"ȡ��":"Cancel");
		textView_Title=(TextView)findViewById(R.id.textView_Title);
		textView_Title.setText(MidData.IsChinese==1?"��������":"Debug Password");
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
//			if(MidData.m_upDown.equals("��"))
//			{
//				Toast.makeText(getApplicationContext(), "�����½�����������", Toast.LENGTH_LONG).show();
//				return;
//			}
			int run = 0;
		    for (int k = 0; k < 100; k++)
		    {
			if (MidData.TestFinished)
			{
			    if (MidData.mTestParameters[k].aisleStatus == 1)
			    {
				boolean f2 = MidData.mTestParameters[k].testStatus==1||MidData.mTestParameters[k].testStatus==2? true
					: false;

				boolean f3 = !MidData.mTestParameters[k].aisleTestFinished ? true
					: false;
				if (f2 && f3)
				{
				    run = 1;
				    break;
				}
			    }
			}
			else
			{
			    run = 2;
			    break;
			}
		    }
		    if (run == 1)
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���ڲ���,���ڼ��������ã�":"Measuring, please set later",
				Toast.LENGTH_LONG).show();
			return;
		    }
		    else if (run == 2)
		    {
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�Լ�δ��ɣ������Լ���ɺ����ã�":"Please set when selfcheck finished",
				Toast.LENGTH_LONG).show();
			return;
		    }
			for (int k = 0; k < 100; k++)
		    {
			if (MidData.mTestParameters[k].testStatus==1)
			{
			    Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���ڷ������������Ժ����ã�":"Detecting samples, please set later",
				    Toast.LENGTH_LONG).show();
			    return;
			}

		    }
			String numtext = editText_password.getText().toString().trim();
		    if (numtext.equals("4287654321"))// 4287654321����̬
		    {
//			byte order = 'A';
//			byte[] buff = new byte[2];
//			buff[0] = 'M';
//			buff[1] = 0x33;
//			MidData.DriRunTime = 3;
		    	byte order = 'A';
				byte[] data = new byte[10];
				data[0] = 'E';
				data[1] = 0x31;
				MidData.DriRunTime = 3;
			if (!MidData.sOp.SendDataToSerial(order, data, 2))
			{
			    Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ�ܣ����������ã�":"Set failed, please reset",
				    Toast.LENGTH_LONG).show();
			    return;
			}
			else
			{
			    MidData.isDebugging = 1;
			    MidData.DirveRunTestTime =  ((double) 5 * (double) 60) / MidData.mSetParameters.RunPeriodSet;
			    MidData.XueChenTestRealTime=Double.toString(MidData.ERSRealTestTime);
			    //Toast.makeText(getApplicationContext(), "�������ģʽ��",
				 //   Toast.LENGTH_LONG).show();
			    //t.putExtra("val", Integer.toString(MidData.isDebugging));
				//ComfirmDebugActivity.this.setResult(RESULT_OK, t);
			    Intent debug=new Intent();
				debug.setClass(ComfirmDebugActivity.this, DebugActivity.class);
				startActivityForResult(debug, 0);
				
				
				ComfirmDebugActivity.this.finish();
				//finish();
			}
			return;
		    }
		    else
		    {
		    	editText_password.setText("");
		    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����������������ã�":"Password error, please reset",
					    Toast.LENGTH_LONG).show();
		    }
		}		
	}
}
