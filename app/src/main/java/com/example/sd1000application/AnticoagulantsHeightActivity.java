package com.example.sd1000application;

import java.text.DecimalFormat;

import com.realect.sd1000.parameterStatus.MidData;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AnticoagulantsHeightActivity extends Activity{
	private DecimalFormat fnum1 = new DecimalFormat("##0.0");
	
	private Button button_cancle=null;
	private Button button_submit=null;
	private ImageButton imageButton_Next=null;
	private ImageButton ImageButton_Pre=null;
	private EditText editText_value=null;
	private TextView textView_Title=null;
	private float value=0;
	private float valueMin=0;
	private float valueMax=20;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_anticoagulants);
		//getWindow().setTitle(MidData.IsChinese?"抗凝剂高度":"Anticoagulation");
		LoadXML();
		LoadLastSet();
	}
	private void LoadLastSet(){
		value=MidData.mSetParameters.AnticoagulantsHeight;
		editText_value.setText(fnum1.format(value));
		button_cancle.setText(MidData.IsChinese==1?"取消":"Cancel");
		button_submit.setText(MidData.IsChinese==1?"确定":"Comfirm");
		textView_Title.setText(MidData.IsChinese==1?"抗凝剂高度":"Anticoagulant height");
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
	private void LoadXML(){
		button_cancle=(Button)findViewById(R.id.button_cancle);
		button_cancle.setOnClickListener(new button_cancleListener());
		button_cancle.setOnTouchListener(new ButtonTouchListener());
		
		button_submit=(Button)findViewById(R.id.button_submit);
		button_submit.setOnClickListener(new button_submitListener());
		button_submit.setOnTouchListener(new ButtonTouchListener());
		
		imageButton_Next=(ImageButton)findViewById(R.id.ImageButton_Next);
		imageButton_Next.setOnClickListener(new ValueSet());
		
		ImageButton_Pre=(ImageButton)findViewById(R.id.ImageButton_Pre);
		ImageButton_Pre.setOnClickListener(new ValueSet());
		
		editText_value=(EditText)findViewById(R.id.editText_value);
		
		textView_Title=(TextView)findViewById(R.id.textView_Title);
	}
	class button_cancleListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
	class button_submitListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String str=editText_value.getText().toString().trim();
			if(str.equals(""))
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"不能为空":"unblank", Toast.LENGTH_LONG).show();
				return;
			}
			if(str.contains("\\."))
			{
				String[] sp=str.split("\\.");
				if(sp[1].length()>1)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"小数点后1位有效":"effective when one decimal place", Toast.LENGTH_LONG).show();
					return;
				}
			}
			
			value=Float.parseFloat(str);
			if(MidData.sqlOp.update(1, "SystemSetTable", "anticoagulantsheightset", str))
			{
				MidData.mSetParameters.AnticoagulantsHeight=value;
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置成功":"Setting Success", Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			else
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置失败,请重新设置":"Set failed, please reset", Toast.LENGTH_LONG).show();
				return;
			}
		}
		
	}
	class ValueSet implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(arg0.getId()==R.id.ImageButton_Pre)
			{
				if(value<=valueMin)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最小值":"Reached the minimum value", Toast.LENGTH_LONG).show();
					return;
				}
				value-=0.1;
				editText_value.setText(fnum1.format(value));
			}
			else if(arg0.getId()==R.id.ImageButton_Next)
			{
				if(value>=valueMax)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最大值":"Reached the maximum value", Toast.LENGTH_LONG).show();
					return;
				}
				value+=0.1;
				editText_value.setText(fnum1.format(value));
			}				
		}
		
	}
}
