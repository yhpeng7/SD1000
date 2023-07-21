package com.example.sd1000application;

import com.realect.sd1000.parameterStatus.MidData;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ParaSetActivity extends Activity{
	private TextView titleTextView;
	private Button PlusButton;
	private Button AddButton;
	private EditText ValueEditText;
	private Button SubmitButton;
	private Button CancelButton;
	Intent intent;
	Bundle b;
	int min=0;
	int max=10;
	int value=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_paraset);
		LoadXML();
		LoadLast();
	}
	private void LoadLast(){
		intent=getIntent();
		b=intent.getExtras();
		min=b.getInt("min");
		max=b.getInt("max");
		value=b.getInt("page");
		String title=b.getString("title");
		titleTextView.setText(title);
		ValueEditText.setText(Integer.toString(value));
		CancelButton.setText(MidData.IsChinese==1?"取消":"Cancel");
		SubmitButton.setText(MidData.IsChinese==1?"确定":"Comfirm");
	}
	private void LoadXML(){
		CancelButton=(Button)findViewById(R.id.CancelButton);
		CancelButton.setOnClickListener(new CancelButtonClickListener());
		SubmitButton=(Button)findViewById(R.id.SubmitButton);
		SubmitButton.setOnClickListener(new SubmitButtonClickListener());
		AddButton=(Button)findViewById(R.id.AddButton);
		AddButton.setOnClickListener(new ValueSetClickListener());
		ValueEditText=(EditText)findViewById(R.id.ValueEditText);
		PlusButton=(Button)findViewById(R.id.PlusButton);
		PlusButton.setOnClickListener(new ValueSetClickListener());
		titleTextView=(TextView)findViewById(R.id.titleTextView);
	}
	class CancelButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
	class SubmitButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			value=Integer.parseInt(ValueEditText.getText().toString().trim());
			if(value<min||value>max)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"可设置的有效范围是："+min+"-"+max:"Configurable effective range is："+min+"-"+max, Toast.LENGTH_LONG).show();
				return;
			}
			intent.putExtra("val", Integer.toString(value));
			ParaSetActivity.this.setResult(RESULT_OK, intent);
			ParaSetActivity.this.finish();	
		}
		
	}
	class ValueSetClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			value=Integer.parseInt(ValueEditText.getText().toString().trim());
			if(arg0.getId()==R.id.PlusButton){
				if(value<=min)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最小值":"Reached the minimum value", Toast.LENGTH_LONG).show();
					return;
				}
				value--;
				ValueEditText.setText(Integer.toString(value));
			}
			else if(arg0.getId()==R.id.AddButton){
				if(value>=max)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最大值":"Reached the maximum value", Toast.LENGTH_LONG).show();
					return;
				}
				value++;
				ValueEditText.setText(Integer.toString(value));
			}
				
			
		}
		
	}
}
