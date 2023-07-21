package com.example.sd1000application;

import com.realect.sd1000.parameterStatus.MidData;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class HeightReviseActivity extends Activity{
	private CheckBox checkBox_AutoHeightRevise=null;
	private Button button_Submit=null;
	private Button button_Cancel=null;
	private TextView textView_Title=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.activity_heightfitting);
		LoadXML();
		LoadLastSet();
	}
	private void LoadLastSet()
	{
		button_Cancel.setText(MidData.IsChinese==1?"ȡ��":"Cancel");
		button_Submit.setText(MidData.IsChinese==1?"ȷ��":"Comfirm");
		textView_Title.setText(MidData.IsChinese==1?"�߶��������":"highly fitting");
		checkBox_AutoHeightRevise.setText(MidData.IsChinese==1?"�߶����":"highly fitting");
		checkBox_AutoHeightRevise.setChecked(MidData.mSetParameters.IsCurveFit==1?true:false);
	}
	private void LoadXML(){
		textView_Title=(TextView)findViewById(R.id.textView_Title);
		button_Cancel=(Button)findViewById(R.id.button_Cancel);
		button_Cancel.setOnClickListener(new CancelOnClickListener());
		button_Cancel.setOnTouchListener(new ButtonTouchListener());
		
		button_Submit=(Button)findViewById(R.id.button_runperiodSubmit);
		button_Submit.setOnClickListener(new SubmitOnClickListener());
		button_Submit.setOnTouchListener(new ButtonTouchListener());
		checkBox_AutoHeightRevise=(CheckBox)findViewById(R.id.checkBox_AutoHeightRevise);
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
	class CancelOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
	}
	class SubmitOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(checkBox_AutoHeightRevise.isChecked())
			{
				if(MidData.sqlOp.update(1, "SystemSetTable", "iscurvefit", "1"))
				{
					MidData.mSetParameters.IsCurveFit=1;
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���óɹ�":"Setting Success", Toast.LENGTH_LONG).show();
					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���� ʧ��":"Set failed, please re-set", Toast.LENGTH_LONG).show();
				}
			}
			else
			{
				if(MidData.sqlOp.update(1, "SystemSetTable", "isheightresivse", "0"))
				{
					MidData.mSetParameters.IsCurveFit=0;
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���óɹ�":"Setting Success", Toast.LENGTH_LONG).show();
					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ��":"Set failed, please re-set", Toast.LENGTH_LONG).show();
				}
			}
			
		}
		
	}
}
