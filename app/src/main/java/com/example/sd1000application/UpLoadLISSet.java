package com.example.sd1000application;

import com.example.sd1000application.LanguageActivity.radioButtonChineseCheckedListener;
import com.realect.sd1000.parameterStatus.MidData;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class UpLoadLISSet extends Activity{
	private EditText editText_ip1=null;
	private EditText editText_ip2=null;
	private EditText editText_ip3=null;
	private EditText editText_ip4=null;
	private EditText editText_port=null;
	private EditText editText_brt=null;
	private Button button_submit=null;
	private Button button_cancel=null;
	private TextView textViewTitle=null;
	private TextView textViewIPLab=null;
	private TextView textViewPortLab=null;
	private TextView textViewPortBrtLab=null;
	private TextView textViewUplodLab=null;
	private RadioButton radioButtonUpload=null;
	private RadioButton radioButtonNoUpload=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_uploadlisset);
		LoadXML();
		LoadLastSet();
	}
	private void LoadLastSet()
	{
		String[] ipStr=MidData.IpStr.split("\\.");
		editText_ip1.setText(ipStr[0]);
		editText_ip2.setText(ipStr[1]);
		editText_ip3.setText(ipStr[2]);
		editText_ip4.setText(ipStr[3]);
		editText_port.setText(Integer.toString(MidData.Port));
		editText_brt.setText(Integer.toString(MidData.LisBrt));
		radioButtonUpload.setChecked(MidData.LisUploadLine);
		radioButtonNoUpload.setChecked(!MidData.LisUploadLine);
		
	}
	private void LoadXML(){
		radioButtonNoUpload=(RadioButton)findViewById(R.id.radioButtonNoUpload);
		radioButtonNoUpload.setText(MidData.IsChinese==1?"否":"No");
		radioButtonNoUpload.setOnCheckedChangeListener(new radioButtonNoUploadCheckedListener());
		
		radioButtonUpload=(RadioButton)findViewById(R.id.radioButtonUpload);
		radioButtonUpload.setText(MidData.IsChinese==1?"是":"Yes");
		radioButtonUpload.setOnCheckedChangeListener(new radioButtonUploadCheckedListener());
		
		textViewUplodLab=(TextView)findViewById(R.id.textViewUplodLab);
		textViewUplodLab.setText(MidData.IsChinese==1?"曲线上传:":"U/L curve:");
		
		textViewPortBrtLab=(TextView)findViewById(R.id.textViewPortBrtLab);
		textViewPortBrtLab.setText(MidData.IsChinese==1?"波特率:":"Baud rate:");		
		
		textViewPortLab=(TextView)findViewById(R.id.textViewPortLab);
		textViewPortLab.setText(MidData.IsChinese==1?"端口号:":"Port:");
		
		textViewIPLab=(TextView)findViewById(R.id.textViewIPLab);
		textViewIPLab.setText(MidData.IsChinese==1?"IP 地址:":"IP Address:");
		
		textViewTitle=(TextView)findViewById(R.id.textViewTitle);
		textViewTitle.setText(MidData.IsChinese==1?"LIS传输设置:":"LIS transmission setup");
		
		button_cancel=(Button)findViewById(R.id.button_cancel);
		button_cancel.setOnClickListener(new button_cancelClickListener());
		button_cancel.setText(MidData.IsChinese==1?"取消":"Cancel");
		
		
		button_submit=(Button)findViewById(R.id.button_submit);
		button_submit.setOnClickListener(new button_submitClickListener());
		button_submit.setOnTouchListener(new ButtonTouchListener());
		button_submit.setText(MidData.IsChinese==1?"确定":"Comfirm");
		
		editText_port=(EditText)findViewById(R.id.editText_port);
		
		editText_ip1=(EditText)findViewById(R.id.editText_ip1);
		editText_ip2=(EditText)findViewById(R.id.editText_ip2);
		editText_ip3=(EditText)findViewById(R.id.editText_ip3);
		editText_ip4=(EditText)findViewById(R.id.editText_ip4);
		
		editText_brt=(EditText)findViewById(R.id.editText_brt);
		
	}
	class radioButtonNoUploadCheckedListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub

			radioButtonNoUpload.setChecked(arg1);
			radioButtonUpload.setChecked(!arg1);
		    
		}
		
	}
	class radioButtonUploadCheckedListener implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub

			radioButtonNoUpload.setChecked(!arg1);
			radioButtonUpload.setChecked(arg1);
		    
		}
		
	}
	class button_cancelClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			finish();
		}
		
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
	class button_submitClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String[] ipStr=new String[4];
			int[] ip=new int[4];
			ipStr[0]=editText_ip1.getText().toString().trim();
			ipStr[1]=editText_ip2.getText().toString().trim();
			ipStr[2]=editText_ip3.getText().toString().trim();
			ipStr[3]=editText_ip4.getText().toString().trim();
			if(ipStr[0].equals("")||ipStr[1].equals("")||ipStr[2].equals("")||ipStr[3].equals(""))
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"Ip地址格式不正确，请重新设置！":"Invalid IP format, please reset", Toast.LENGTH_LONG).show();
				return;
			}
			ip[0]=Integer.parseInt(ipStr[0]);
			ip[1]=Integer.parseInt(ipStr[1]);
			ip[2]=Integer.parseInt(ipStr[2]);
			ip[3]=Integer.parseInt(ipStr[3]);
			if(ip[0]<0||ip[0]>255||ip[1]<0||ip[1]>255||ip[2]<0||ip[2]>255||ip[3]<0||ip[3]>255)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"Ip地址格式不正确，请重新设置！":"Invalid IP format, please reset", Toast.LENGTH_LONG).show();
				return;
			}
			String portStr=editText_port.getText().toString().trim();
			if(portStr.equals(""))
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"端口号不能为空，请重新设置！":"Port number is null, please reset", Toast.LENGTH_LONG).show();
				return;
			}
			int port=Integer.parseInt(portStr);
			if(port<0||port>65535)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"端口号无效，请重新设置！":"Invalid port number, please reset", Toast.LENGTH_LONG).show();
				return;
			}
			String brtStr=editText_brt.getText().toString().trim();
			if(brtStr.equals(""))
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"波特率不能为空，请重新设置！":"Baud rate is null, please reset", Toast.LENGTH_LONG).show();
				return;
			}
			String upload ="";
			if(radioButtonUpload.isChecked())
			{
				upload="1";
			}
			else
			{
				upload="0";				
			}
			String[] rowname={"ipstr","port","lisbrt","lisuploadline"};
			String[] value={ipStr[0]+"."+ipStr[1]+"."+ipStr[2]+"."+ipStr[3],portStr,brtStr,upload};
			if(MidData.sqlOp.UpdateSysemSet(1, "SystemSetTable", rowname, value, 4))
			{
				 MidData.IpStr=ipStr[0]+"."+ipStr[1]+"."+ipStr[2]+"."+ipStr[3];
				 MidData.Port=Integer.parseInt(portStr);
				 MidData.LisBrt=Integer.parseInt(brtStr);
				 MidData.LisUploadLine=(upload.equals("1"))?true:false;
				 Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置成功":"Setting success", Toast.LENGTH_LONG).show();
				 finish();
			}
			else
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置失败":"Set failed,please reset", Toast.LENGTH_LONG).show();
				return;
			}
		}		
	}
}