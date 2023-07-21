package com.example.sd1000application;


import java.text.DecimalFormat;

import com.realect.sd1000.parameterStatus.MidData;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class SetActivity extends Activity{

	private DecimalFormat fnum0 = new DecimalFormat("##0");
	private RadioButton radioButton_ERSTestTime_60min=null;
	private RadioButton radioButton_ERSTestTime_30min=null;
	private RadioButton radioButton_PrintSet_Chart=null;
	private RadioButton radioButton_PrintSet_NoChart=null;
	private RadioButton radioButton_UpSA=null;
	private RadioButton radioButton_UpLIS_net=null;
	private RadioButton radioButton_UpLIS_serial=null;
	//private RadioButton radioButton_UpSAAuto=null;
	private RadioButton radioButton_SetNum_Pre=null;
	private RadioButton radioButton_SetNum_Finish=null;
	private CheckBox checkBox_AutoPrint_AfterERS=null;
	private CheckBox checkBox_AutoPrint_AfterHemat=null;
	private CheckBox checkBox_TemRevise=null;
	private Button button_Anticoa=null;
	private Button button_LISset=null;
	private Button button_FanClose=null;
	private Button button_FanOpen=null;
	private Button button_consume=null;
	private Button button_temperature=null;
	private Button button_version=null;
	private Button button_debug=null;
	private Button button_save=null;
	private Button button_exit=null;
	private TextView textViewTitle=null;
	private TextView textViewERSTestTimeLab=null;
	private TextView textViewTransLab=null;
	private TextView textViewPrintSetLab=null;
	private TextView textViewSetNumLab=null;
	private TextView textViewAutoPrintLab=null;
	private TextView textViewInitialNumLab=null;
	private EditText EditText_InitialNum=null;
	private void SetButtonAble(boolean fg)
	{
		button_Anticoa.setEnabled(fg);
		button_LISset.setEnabled(fg);
		button_FanClose.setEnabled(fg);
		button_FanOpen.setEnabled(fg);
		button_consume.setEnabled(fg);
		button_temperature.setEnabled(fg);
		button_version.setEnabled(fg);
		button_debug.setEnabled(fg);
		button_save.setEnabled(fg);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set);
		LoadXML();
		LoadLastSet();
	}
	private void LoadLastSet(){
		if(MidData.mSetParameters.ERSTestTime==0)
		{
			radioButton_ERSTestTime_60min.setChecked(true);
			radioButton_ERSTestTime_30min.setChecked(false);
		}
		else
		{
			radioButton_ERSTestTime_60min.setChecked(false);
			radioButton_ERSTestTime_30min.setChecked(true);
		}
		radioButton_UpSA.setChecked(MidData.m_UpToSA);
		radioButton_UpLIS_net.setChecked(MidData.m_UpToLIS_net);
		radioButton_UpLIS_serial.setChecked(MidData.m_UpToLIS_ser);
		//radioButton_UpSAAuto.setChecked(MidData.m_UpToSAAuto);
		EditText_InitialNum.setText(fnum0.format(MidData.mSetParameters.InitialNum));
		if(MidData.mSetParameters.PrintSet==1)
		{
			radioButton_PrintSet_Chart.setChecked(true);
			radioButton_PrintSet_NoChart.setChecked(false);
		}
		else
		{
			radioButton_PrintSet_Chart.setChecked(false);
			radioButton_PrintSet_NoChart.setChecked(true);
		}
		if(MidData.isDebugging==0)
			button_debug.setBackgroundResource(R.drawable.buttonbackcancel);
		else
			button_debug.setBackgroundResource(R.drawable.buttonbacksel);
		if(MidData.mSetParameters.SetNumWay==0)
		{
			radioButton_SetNum_Pre.setChecked(true);
			radioButton_SetNum_Finish.setChecked(false);
		}
		else
		{
			radioButton_SetNum_Pre.setChecked(false);
			radioButton_SetNum_Finish.setChecked(true);
		}
		if(MidData.mSetParameters.FanOpenClose==0)
		{
			button_FanOpen.setBackgroundResource(R.drawable.buttonbacksel);
			button_FanClose.setBackgroundResource(R.drawable.buttonbackcancel);
		}
		else
		{
			button_FanClose.setBackgroundResource(R.drawable.buttonbacksel);
			button_FanOpen.setBackgroundResource(R.drawable.buttonbackcancel);
		}
		
		checkBox_AutoPrint_AfterERS.setChecked(MidData.m_PrintAfterERS);
		checkBox_AutoPrint_AfterHemat.setChecked(MidData.m_PrintAfterHemat);
		checkBox_TemRevise.setChecked(MidData.mSetParameters.IsAutoTemResive==1?true:false);
		textViewTitle.setText(MidData.IsChinese==1?"ϵͳ����":"System setup");
		textViewERSTestTimeLab.setText(MidData.IsChinese==1?"Ѫ������ʱ��:":"ESR test time:");
		radioButton_ERSTestTime_60min.setText(MidData.IsChinese==1?"60����":"60 minutes");
		radioButton_ERSTestTime_30min.setText(MidData.IsChinese==1?"30����":"30 minutes");
		textViewTransLab.setText(MidData.IsChinese==1?"���ݴ�������:":"Data transmission:");
		radioButton_UpSA.setText(MidData.IsChinese==1?"SA":"SA");
		//radioButton_UpSAAuto.setText(MidData.IsChinese?"�Զ�����SA":"Auto to SA");
		radioButton_UpLIS_net.setText(MidData.IsChinese==1?"LIS(����)":"LIS(RJ45)");
		radioButton_UpLIS_serial.setText(MidData.IsChinese==1?"LIS(����)":"LIS(RS232)");
		
		textViewPrintSetLab.setText(MidData.IsChinese==1?"��ӡ����:":"Print setup:");
		radioButton_PrintSet_Chart.setText(MidData.IsChinese==1?"�г�������":"with curve");
		radioButton_PrintSet_NoChart.setText(MidData.IsChinese==1?"�޳�������":"without curve");
		textViewSetNumLab.setText(MidData.IsChinese==1?"���ʱ������:":"Time of number setup:");
		radioButton_SetNum_Pre.setText(MidData.IsChinese==1?"����ǰ���":"before test");
		radioButton_SetNum_Finish.setText(MidData.IsChinese==1?"������ɺ���":"after test");
		textViewAutoPrintLab.setText(MidData.IsChinese==1?"�Զ���ӡ����:":"Auto-print setup:");
		checkBox_AutoPrint_AfterERS.setText(MidData.IsChinese==1?"Ѫ����ɺ��ӡ":"ESR finished");
		checkBox_AutoPrint_AfterHemat.setText(MidData.IsChinese==1?"ѹ����ɺ��ӡ":"HCT finished");
		checkBox_TemRevise.setText(MidData.IsChinese==1?"�¶����":"TEMP fit");
		textViewInitialNumLab.setText(MidData.IsChinese==1?"Ĭ����ʼ���:":"Def.start ID:");
	}
	class button_FanControlClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�Լ�δ��ɣ������Լ���ɺ�����":"Self-test is not completed, please set up after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			SetButtonAble(false);
			if(arg0.getId()==R.id.button_FanClose)
			{
				byte or='A';
				byte[] buf={'W',0x31};
				if(!MidData.m_upDown.equals("��"))
				{
					MidData.m_isRecived=false;
					if(MidData.sOp.SendData(or, buf, 2, MidData.m_OutputStream_LowerMachine))
					{
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(MidData.m_isRecived)
						{
							if(MidData.sqlOp.update(1, "SystemSetTable", "fanopenclose", "0"))
							{
								button_FanClose.setBackgroundResource(R.drawable.buttonbackcancel);
								button_FanOpen.setBackgroundResource(R.drawable.buttonbacksel);
								Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���óɹ�":"Setting Success", Toast.LENGTH_LONG).show();
							}
							else
							{
								Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ��":"Set failed, please re-set", Toast.LENGTH_LONG).show();
							}
						}
						else{
							Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ��":"Set failed, please re-set", Toast.LENGTH_LONG).show();
						}
					}
					else
					{
						Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ��":"Set failed, please re-set", Toast.LENGTH_LONG).show();
					}
				}
				else{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�����½��Ĺ�������":"Please set when descending", Toast.LENGTH_LONG).show();
				}
			}
			else if(arg0.getId()==R.id.button_FanOpen)
			{
				byte or='A';
				byte[] buf={'W',0x30};
				if(!MidData.m_upDown.equals("��"))
				{
					MidData.m_isRecived=false;
					if(MidData.sOp.SendData(or, buf, 2, MidData.m_OutputStream_LowerMachine))
					{
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(MidData.m_isRecived)
						{
							if(MidData.sqlOp.update(1, "SystemSetTable", "fanopenclose", "1"))
							{
								button_FanClose.setBackgroundResource(R.drawable.buttonbacksel);
								button_FanOpen.setBackgroundResource(R.drawable.buttonbackcancel);
								Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���óɹ�":"Setting Success", Toast.LENGTH_LONG).show();
							}
							else{
								Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ��":"Set failed, please re-set", Toast.LENGTH_LONG).show();
							}
						}
						else{
							Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ��":"Set failed, please re-set", Toast.LENGTH_LONG).show();
						}
					}
					else
					{
						Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ��":"Set failed, please re-set", Toast.LENGTH_LONG).show();
					}
				}
				else{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�����½��Ĺ�������":"Please set when descending", Toast.LENGTH_LONG).show();
				}
			}
			SetButtonAble(true);
		}
		
	}
    private void LoadXML(){
    	EditText_InitialNum=(EditText)findViewById(R.id.EditText_InitialNum);
    	
    	textViewAutoPrintLab=(TextView)findViewById(R.id.textViewAutoPrintLab);
    	
    	textViewSetNumLab=(TextView)findViewById(R.id.textViewSetNumLab);
    	
    	textViewPrintSetLab=(TextView)findViewById(R.id.textViewPrintSetLab);
    	
    	textViewTransLab=(TextView)findViewById(R.id.textViewTransLab);    	
    	
    	textViewERSTestTimeLab=(TextView)findViewById(R.id.textViewERSTestTimeLab);
    	
    	textViewTitle=(TextView)findViewById(R.id.textViewTitle);
    	
    	textViewInitialNumLab=(TextView)findViewById(R.id.textViewInitialNumLab);
    	
    	radioButton_SetNum_Pre=(RadioButton)findViewById(R.id.radioButton_SetNum_Pre);
    	radioButton_SetNum_Pre.setOnCheckedChangeListener(new SetNumListener());
    	radioButton_SetNum_Finish=(RadioButton)findViewById(R.id.radioButton_SetNum_Finish);
    	radioButton_SetNum_Finish.setOnCheckedChangeListener(new SetNumListener());
    	
    	button_LISset=(Button)findViewById(R.id.button_LISset);
    	button_LISset.setOnClickListener(new button_LISsetClickListener());
    	button_LISset.setOnTouchListener(new ButtonTouchListener());
    	button_LISset.setText(MidData.IsChinese==1?"LIS����":"LIS");
    	
    	
    	button_FanClose=(Button)findViewById(R.id.button_FanClose);
    	button_FanClose.setOnClickListener(new button_FanControlClickListener());
    	button_FanClose.setText(MidData.IsChinese==1?"���ȹ�":"Fan off");
    	//button_FanClose.setOnTouchListener(new ButtonTouchListener());
    	
    	button_FanOpen=(Button)findViewById(R.id.button_FanOpen);
    	button_FanOpen.setOnClickListener(new button_FanControlClickListener());
    	button_FanOpen.setText(MidData.IsChinese==1?"���ȿ�":"Fan on");
    	//button_FanOpen.setOnTouchListener(new ButtonTouchListener());
    	
    	button_Anticoa=(Button)findViewById(R.id.button_Anticoa);
    	button_Anticoa.setOnClickListener(new AnticoaListener());
    	button_Anticoa.setOnTouchListener(new ButtonTouchListener());
    	button_Anticoa.setText(MidData.IsChinese==1?"������":"Anticoag");
    	
    	checkBox_TemRevise=(CheckBox)findViewById(R.id.checkBox_TemRevise);
    	checkBox_AutoPrint_AfterERS=(CheckBox)findViewById(R.id.checkBox_AutoPrint_AfterERS);
    	checkBox_AutoPrint_AfterHemat=(CheckBox)findViewById(R.id.checkBox_AutoPrint_AfterHemat);
    	radioButton_UpSA=(RadioButton)findViewById(R.id.radioButton_UpSA);
    	radioButton_UpSA.setOnCheckedChangeListener(new UpLoadListener());
    	radioButton_UpLIS_net=(RadioButton)findViewById(R.id.radioButton_UpLIS_net);
    	radioButton_UpLIS_net.setOnCheckedChangeListener(new UpLoadListener());
    	
    	radioButton_UpLIS_serial=(RadioButton)findViewById(R.id.radioButton_UpLIS_serial);
    	radioButton_UpLIS_serial.setOnCheckedChangeListener(new UpLoadListener());
    	
    	/*radioButton_UpSAAuto=(RadioButton)findViewById(R.id.radioButton_UpLISNet);
    	radioButton_UpSAAuto.setOnCheckedChangeListener(new UpLoadListener());*/
    	
    	radioButton_PrintSet_Chart=(RadioButton)findViewById(R.id.radioButton_PrintSet_Chart);
    	radioButton_PrintSet_Chart.setOnCheckedChangeListener(new PrintSet());
    	radioButton_PrintSet_NoChart=(RadioButton)findViewById(R.id.radioButton_PrintSet_NoChart);
    	radioButton_PrintSet_NoChart.setOnCheckedChangeListener(new PrintSet());
    	radioButton_ERSTestTime_60min=(RadioButton)findViewById(R.id.radioButton_ERSTestTime_60min);
    	radioButton_ERSTestTime_60min.setOnCheckedChangeListener(new ERSTestTimeSet());
    	radioButton_ERSTestTime_30min=(RadioButton)findViewById(R.id.radioButton_ERSTestTime_30min);
    	radioButton_ERSTestTime_30min.setOnCheckedChangeListener(new ERSTestTimeSet());
    	
    	
    	button_exit=(Button)findViewById(R.id.button_exit);
    	button_exit.setOnClickListener(new ExitOnClick());
    	button_exit.setOnTouchListener(new ButtonTouchListener());
    	button_exit.setText(MidData.IsChinese==1?"����":"Return");
    	
    	button_save=(Button)findViewById(R.id.button_save);
    	button_save.setOnClickListener(new SaveOnClick());
    	button_save.setOnTouchListener(new ButtonTouchListener());
    	button_save.setText(MidData.IsChinese==1?"����":"Save");
    	button_debug=(Button)findViewById(R.id.button_ReTest);
    	button_debug.setOnClickListener(new DebugOnClick());
    	button_debug.setText(MidData.IsChinese==1?"����":"Debug");
    	//button_debug.setOnTouchListener(new ButtonTouchListener());
    	
    	button_version=(Button)findViewById(R.id.button_version);
    	button_version.setOnClickListener(new VersionOnClick());
    	button_version.setOnTouchListener(new ButtonTouchListener());
    	button_version.setText(MidData.IsChinese==1?"�汾":"Version");
    	button_temperature=(Button)findViewById(R.id.button_Data);
    	button_temperature.setOnClickListener(new TemperatureOnClick());
    	button_temperature.setOnTouchListener(new ButtonTouchListener());
    	button_temperature.setText(MidData.IsChinese==1?"�¶�":"Temp");
    	button_consume=(Button)findViewById(R.id.button_runperiodCancel);
    	button_consume.setOnClickListener(new ConsumeOnClick());
    	button_consume.setOnTouchListener(new ButtonTouchListener());
    	button_consume.setText(MidData.IsChinese==1?"�Ĳ�":"Supply");
    }
    class SetNumListener implements CompoundButton.OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if(arg0.getId()==R.id.radioButton_SetNum_Pre)
			{
				radioButton_SetNum_Finish.setChecked(!arg1);
			}
			else if(arg0.getId()==R.id.radioButton_SetNum_Finish)
			{
				radioButton_SetNum_Pre.setChecked(!arg1);
			}
		}
    	
    }
    class button_LISsetClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent t=new Intent();
			t.setClass(SetActivity.this, UpLoadLISSet.class);
			startActivity(t);
		}
    	
    }
    class ButtonTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			Button bt=(Button)arg0;
			if(arg1.getAction()==MotionEvent.ACTION_DOWN)
				bt.setBackgroundResource(R.drawable.buttonbackcancel);
			else if(arg1.getAction()==MotionEvent.ACTION_UP)
				bt.setBackgroundResource(R.drawable.buttonbacksel);
			return false;
		}
    	
    }
    class AnticoaListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent anti=new Intent();
			anti.setClass(SetActivity.this, AnticoagulantsHeightActivity.class);
			startActivity(anti);
		}
    	
    }
    class UpLoadListener implements CompoundButton.OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if(arg1)
			{
				if(arg0.getId()==R.id.radioButton_UpSA)
				{
					radioButton_UpLIS_net.setChecked(!arg1);
					radioButton_UpLIS_serial.setChecked(!arg1);
					//radioButton_UpSAAuto.setChecked(!arg1);
				}
				else if(arg0.getId()==R.id.radioButton_UpLIS_net)
				{
					radioButton_UpSA.setChecked(!arg1);
					radioButton_UpLIS_serial.setChecked(!arg1);
					//radioButton_UpSAAuto.setChecked(!arg1);
				}			
				else if(arg0.getId()==R.id.radioButton_UpLIS_serial)
				{
					radioButton_UpSA.setChecked(!arg1);
					radioButton_UpLIS_net.setChecked(!arg1);
					//radioButton_UpSAAuto.setChecked(!arg1);
				}
				/*else if(arg0.getId()==R.id.radioButton_UpSAAuto)
				{
					radioButton_UpSA.setChecked(!arg1);
					radioButton_UpLIS.setChecked(!arg1);
					radioButton_UpLISAuto.setChecked(!arg1);
				}*/
			}
			
		}
    	
    }
    class PrintSet implements CompoundButton.OnCheckedChangeListener
    {
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if(arg0.getId()==R.id.radioButton_PrintSet_Chart)
			{
				radioButton_PrintSet_NoChart.setChecked(!arg1);
			}
			else if(arg0.getId()==R.id.radioButton_PrintSet_NoChart)
			{
				radioButton_PrintSet_Chart.setChecked(!arg1);
			}
		}
    	
    }
    class ERSTestTimeSet implements CompoundButton.OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			if(arg0.getId()==R.id.radioButton_ERSTestTime_60min)
			{
				radioButton_ERSTestTime_30min.setChecked(!arg1);
			}
			else if(arg0.getId()==R.id.radioButton_ERSTestTime_30min)
			{
				radioButton_ERSTestTime_60min.setChecked(!arg1);
			}
		}
    	
    }
    
    class ExitOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			SetActivity.this.finish();
		}
    	
    }
    class SaveOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			
			// TODO Auto-generated method stub
			for (int k = 0; k < 100; k++)
		    {
			if (MidData.mTestParameters[k].testStatus==1)
			{
			    Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���ڷ������������Ժ����ã�":"Detecting samples, please set later",
				    Toast.LENGTH_LONG).show();
			    return;
			}

		    }
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
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���ڲ���,���ڼ��������ã�":"Testing, please check after setting",
				Toast.LENGTH_LONG).show();
			return;
		    }
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"�Լ�δ��ɣ������Լ���ɺ�����":"Self-test is not completed, please set up after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.IsReviseRunPeriod)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���ڵ����������ڣ����Ժ�����":"Adjusting running cycle, please wait", Toast.LENGTH_LONG).show();
				return;
			}
			/*else if(MidData.m_upDown.equals("��"))
			{
				Toast.makeText(getApplicationContext(), "���ڼ�⣬���Ժ�����", Toast.LENGTH_LONG).show();
				return;
			}*/
			
			else
			{
				SetButtonAble(false);
				String[] value = new String[7]; 
			    
			   
			    if (radioButton_ERSTestTime_60min.isChecked())
			    {
				value[0] = "0";
			    }
			    else
			    {
				value[0] = "1";
			    }
			    int upLoad=0;
			    if (radioButton_UpSA.isChecked())
			    {
			    	upLoad += 1;
			    }
			    if(radioButton_UpLIS_net.isChecked())
			    {
			    	upLoad += 2;
			    }
			    if(radioButton_UpLIS_serial.isChecked())
			    {
			    	upLoad += 4;
			    }
			   /* if(radioButton_UpSAAuto.isChecked())
			    {
			    	upLoad += 8;
			    }*/
			    value[1]=Integer.toString(upLoad);
			    if(radioButton_PrintSet_Chart.isChecked())
			    {
			    	value[2]="1";
			    }
			    else
			    {
			    	value[2]="0";
			    }
			    int autoprint=0;
			    if(checkBox_AutoPrint_AfterERS.isChecked())
			    {
			    	autoprint+=1;
			    }
			    if(checkBox_AutoPrint_AfterHemat.isChecked())
			    {
			    	autoprint+=2;
			    }
			    value[3]=Integer.toString(autoprint);
			    if(checkBox_TemRevise.isChecked())
			    {
			    	value[4]="1";
			    }
			    else
			    {
			    	value[4]="0";
			    }			   
			    if(radioButton_SetNum_Pre.isChecked())
			    {
			    	value[5]="0";
			    }
			    else
			    {
			    	value[5]="1";
			    }
			    int InitialNum=Integer.parseInt(EditText_InitialNum.getText().toString().trim());
			    value[6]=EditText_InitialNum.getText().toString().trim();
				byte order = 'A';
				byte[] buff = new byte[2];
				buff[0] = 'M';
				if(MidData.mSetParameters.TestType==0)
				{
					if (value[0].equals("0"))
					{
					    buff[1] = 0x32;
					    MidData.DriRunTime=2;
					}
					else
					{
					    buff[1] = 0x31;
					    MidData.DriRunTime=1;
					}

					if (!MidData.sOp.SendDataToSerial(order, buff, 2))
					{
					    Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ�ܣ����������ã�":"Set failed, please re-set",
						    Toast.LENGTH_LONG).show();
					    return;
					}
				}
				
				String[] rowname =
			    { "xuechentesttype", "isautouploaddata", "isprintchart", "isautoprint",
				    "isautotemrevise","setnumway","initialnum"};
				if (MidData.sqlOp.UpdateSysemSet(1, "SystemSetTable",rowname, value, 7))
				{
					MidData.mSetParameters.ERSTestTime=Integer.parseInt(value[0]);
					MidData.mSetParameters.InitialNum=Integer.parseInt(value[6]);
					if(MidData.mSetParameters.ERSTestTime==0)
						MidData.ERSRealTestTime=(int) (60 * 60) / MidData.mSetParameters.RunPeriodSet;
					else
						MidData.ERSRealTestTime=(int) (30 * 60) / MidData.mSetParameters.RunPeriodSet;
					MidData.XueChenTestRealTime=Double.toString(MidData.ERSRealTestTime);
					if (MidData.mSetParameters.TestType==0)
					{
					    if (MidData.mSetParameters.ERSTestTime==0)
					    {
						MidData.DriRunTime = 2;
					    }
					    else
					    {
						MidData.DriRunTime = 1;
					    }
					    MidData.DirveRunTestTime=MidData.ERSRealTestTime;
					}
					else
					{
					    MidData.DriRunTime = 0;
					    MidData.DirveRunTestTime=2;
					}					
					MidData.mSetParameters.SetNumWay=Integer.parseInt(value[5]);
					MidData.m_UpToSA=radioButton_UpSA.isChecked()?true:false;
					MidData.m_UpToLIS_net=radioButton_UpLIS_net.isChecked()?true:false;
					MidData.m_UpToLIS_ser=radioButton_UpLIS_serial.isChecked()?true:false;

					//MidData.m_UpToSAAuto=radioButton_UpSAAuto.isChecked()?true:false;
					
					MidData.mSetParameters.DataTransferSet=upLoad;
					MidData.mSetParameters.PrintSet=radioButton_PrintSet_Chart.isChecked()?1:0;
					MidData.mSetParameters.AutoPrintSet=autoprint;
					MidData.m_PrintAfterERS=checkBox_AutoPrint_AfterERS.isChecked();
					MidData.m_PrintAfterHemat=checkBox_AutoPrint_AfterHemat.isChecked();
					MidData.mSetParameters.IsAutoTemResive=checkBox_TemRevise.isChecked()?1:0;
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���óɹ�,������������":"Setting success��Please restart��",Toast.LENGTH_LONG).show();
					SetActivity.this.finish();
				}
				else{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"����ʧ�ܣ����������ã�":"Set failed, please re-set",
							Toast.LENGTH_LONG).show();
				}
				SetButtonAble(true);
			    }			    
			}
		}
 
    class DebugOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			/*Intent debug=new Intent();
			debug.setClass(SetActivity.this, DebugActivity.class);//ComfirmDebugActivity
			startActivity(debug);
			finish();*/
			if(!MidData.TestFinished)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���Լ���ɺ�������":"Debug when Self-check finished", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.isDebugging == 0)
			{
				if(MidData.IsReviseRunPeriod)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"���ڵ����������ڣ����Ժ�������":"Adjusting running cycle,please debug later", Toast.LENGTH_LONG).show();
					return;
				}
				Intent debug=new Intent();
				debug.setClass(SetActivity.this, ComfirmDebugActivity.class);//ComfirmDebugActivity
				//startActivity(debug);
				startActivityForResult(debug,0);
				//finish();
			}
			else
			{
				Intent debug=new Intent();
				debug.setClass(SetActivity.this, DebugActivity.class);//ComfirmDebugActivity
				startActivityForResult(debug,0);
				//finish();
			}
		}
    	
    }
    class VersionOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent ver=new Intent();
			ver.setClass(SetActivity.this, VersionActivity.class);
			startActivity(ver);
		}
    	
    }
    class TemperatureOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent tem=new Intent();
			tem.setClass(SetActivity.this, TemperatureActivity.class);
			startActivity(tem);
		}
    	
    }
    class ConsumeOnClick implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent consume=new Intent();
			consume.setClass(SetActivity.this, ConsumeActivity.class);
			startActivity(consume);
		}
    	
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(MidData.isDebugging==0)
			button_debug.setBackgroundResource(R.drawable.buttonbackcancel);
		else if(MidData.isDebugging==1||MidData.isDebugging==2)
			button_debug.setBackgroundResource(R.drawable.buttonbacksel);
	}
    
}
