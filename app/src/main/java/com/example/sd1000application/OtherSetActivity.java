package com.example.sd1000application;

import com.realect.sd1000.Calculate.CalculateFormula;
import com.realect.sd1000.parameterStatus.*;

import java.text.DecimalFormat;




import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class OtherSetActivity extends Activity{
	private DecimalFormat fnum0 = new DecimalFormat("##0");
	private DecimalFormat fnum1 = new DecimalFormat("##0.0");
	private DecimalFormat fnum2 = new DecimalFormat("##0.00");
	
	
	private EditText EditText_minTest=null;
	private Button ImageButton_minTestPlus=null;
	private Button ImageButton_minTestAdd=null;
	private float minTest=0;
	private float minTestMin=(float) 0.0;
	private float minTestMax=(float) 20.0;//12.0
	
	
	private EditText EditText_ResetHeight=null;
	private Button ImageButton_ResetHeightPlus=null;
	private Button ImageButton_ResetHeightAdd=null;
	private float ResetHeight=0;
	private float ResetHeightMin=0;
	private float ResetHeightMax=15;
	
	
	private EditText EditText_ERSInit=null;
	private Button ImageButton_ERSInitPlus=null;
	private Button ImageButton_ERSInitAdd=null;
	private float ERSInit=0;
	private float ERSInitMin=0;
	private float ERSInitMax=12;
	
	
	private EditText EditText_VoltageReference=null;
	private Button ImageButton_VoltageReferencePlus=null;
	private Button ImageButton_VoltageReferenceAdd=null;
	private int VoltageReference=0;
	private int VoltageReferenceMin=500;
	private int VoltageReferenceMax=1800;
	
	
	private EditText EditText_VoltageCompare=null;
	private Button ImageButton_VoltageComparePlus=null;
	private Button ImageButton_VoltageCompareAdd=null;
	private int VoltageCompare=0;
	private int VoltageCompareMin=0;
	private int VoltageCompareMax=99999;
	
	
	private EditText EditText_ERSResultRevise=null;
	private Button ImageButton_ERSResultRevisePlus=null;
	private Button ImageButton_ERSResultReviseAdd=null;
	private float ERSResultRevise=0;
	private float ERSResultReviseMin=-50;
	private float ERSResultReviseMax=50;
	
	
	private EditText EditText_HematResultRevise=null;
	private Button ImageButton_HematResultRevisePlus=null;
	private Button ImageButton_HematResultReviseAdd=null;
	private float HematResultRevise=0;
	private float HematResultReviseMin=-20;
	private float HematResultReviseMax=20;
	
	
	private EditText EditText_TemRevise=null;
	private Button ImageButton_TemRevisePlus=null;
	private Button ImageButton_TemReviseAdd=null;
	private float TemRevise=0;
	private float TemReviseMin=-5;
	private float TemReviseMax=5;
	
	private EditText EditText_InitialNum=null;
	private Button ImageButton_InitialNumPlus=null;
	private Button ImageButton_InitialNumAdd=null;
	private int InitialNum=0;
	private int InitialNumMin=0;
	private int InitialNumMax=9999;
	
	
	private Button button_submit=null;
	private Button button_cancel=null;
	private TextView textView_Title=null;
	private TextView TextViewResetHeightLab=null;
	private TextView TextViewERSInitLab=null;
	private TextView TextViewVoltageReferenceLab=null;
	private TextView TextViewVoltageCompareLab=null;
	private TextView TextViewERSResultReviseLab=null;
	private TextView textViewHematResultReviseLab=null;
	private TextView textViewTemReviseLab=null;
	private TextView textViewResultShowLab=null;
	private TextView textViewMinTestLab=null;
	private TextView textViewInitialNumLab=null;
	private RadioButton isIntradioButton=null;
	private RadioButton isFloatradioButton=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_otherset);
		LoadXML();
		LoadLastSet();
	}
	private void LoadLastSet()
	{
		isFloatradioButton.setChecked(MidData.m_resultInt==0?true:false);
		isIntradioButton.setChecked(MidData.m_resultInt==1?true:false);
		
		textView_Title.setText(MidData.IsChinese==1?"其他设置":"Other Setting");
		button_cancel.setText(MidData.IsChinese==1?"取消":"Cancel");
		button_submit.setText(MidData.IsChinese==1?"确定":"Comfirm");
		
		textViewResultShowLab.setText(MidData.IsChinese==1?"结果显示精度:":"Result precision:");
		isIntradioButton.setText(MidData.IsChinese==1?"整数显示":"Integer");
		isFloatradioButton.setText(MidData.IsChinese==1?"小数显示":"Decimal");
		textViewMinTestLab.setText(MidData.IsChinese==1?"测试下限(mm):":"Minimum(mm):");
		textViewTemReviseLab.setText(MidData.IsChinese==1?"温度修正(℃):":"Tem correction(℃)");
		
		textViewHematResultReviseLab.setText(MidData.IsChinese==1?"压积测试结果修正(%):":"HCT correction()%");
		
		TextViewERSResultReviseLab.setText(MidData.IsChinese==1?"血沉测试结果修正(mm):":"ESR correction(mm)");
		
		TextViewVoltageCompareLab.setText(MidData.IsChinese==1?"电压比较值设置(mV):":"Volt fiducial(mV):");
		
		TextViewVoltageReferenceLab.setText(MidData.IsChinese==1?"电压参考值设置(mV):":"Volt Ref(mV):");
		
		TextViewERSInitLab.setText(MidData.IsChinese==1?"血沉测试初始值57±(mm):":"ESR initial 57±(mm):");
		
		TextViewResetHeightLab.setText(MidData.IsChinese==1?"复位高度修正(mm):":"Height correction(mm):");
		
		textViewInitialNumLab.setText(MidData.IsChinese==1?"默认初始编号:":"Start Num:");
		
		TemRevise=MidData.mSetParameters.TemperatureRevise;		
		EditText_TemRevise.setText(fnum1.format(TemRevise));
		
		HematResultRevise=MidData.mSetParameters.HematResultRevise;
		EditText_HematResultRevise.setText(fnum1.format(HematResultRevise));
		
		ERSResultRevise=MidData.mSetParameters.ERSTestResultRevise;
		EditText_ERSResultRevise.setText(fnum1.format(ERSResultRevise));
		
		VoltageCompare=MidData.mSetParameters.VolageCompareSet;
		EditText_VoltageCompare.setText(Integer.toString(VoltageCompare));
		
		VoltageReference=MidData.mSetParameters.VoltageReferenceSet;
		EditText_VoltageReference.setText(Integer.toString(VoltageReference));
		
		ERSInit=MidData.mSetParameters.ERSTestIniValue;
		EditText_ERSInit.setText(fnum1.format(ERSInit));
		
		ResetHeight=(float) MidData.mSetParameters.ResetHeightRevise;
		EditText_ResetHeight.setText(fnum1.format(ResetHeight));
		
		EditText_minTest.setText(fnum1.format(MidData.m_minTest));
		minTest=(float) MidData.m_minTest;
		
		EditText_InitialNum.setText(fnum0.format(MidData.mSetParameters.InitialNum));
		InitialNum= MidData.mSetParameters.InitialNum;
		
	}
	
	private void LoadXML()
	{
		textViewInitialNumLab=(TextView)findViewById(R.id.textViewInitialNumLab);
		EditText_InitialNum=(EditText)findViewById(R.id.EditText_InitialNum);
		ImageButton_InitialNumPlus=(Button)findViewById(R.id.ImageButton_InitialNumPlus);
		ImageButton_InitialNumPlus.setOnClickListener(new InitialNumSet());
		ImageButton_InitialNumPlus.setOnTouchListener(new SetPlusTouchListener());
		ImageButton_InitialNumAdd=(Button)findViewById(R.id.ImageButton_InitialNumAdd);
		ImageButton_InitialNumAdd.setOnClickListener(new InitialNumSet());
		ImageButton_InitialNumAdd.setOnTouchListener(new SetAddTouchListener());
		
		textViewMinTestLab=(TextView)findViewById(R.id.textViewMinTestLab);
		textViewResultShowLab=(TextView)findViewById(R.id.textViewresultshowLab);
		EditText_minTest=(EditText)findViewById(R.id.EditText_minTest);
		ImageButton_minTestPlus=(Button)findViewById(R.id.ImageButton_minTestPlus);
		ImageButton_minTestPlus.setOnClickListener(new MinTestSet());
		ImageButton_minTestPlus.setOnTouchListener(new SetPlusTouchListener());
		ImageButton_minTestAdd=(Button)findViewById(R.id.ImageButton_minTestAdd);
		ImageButton_minTestAdd.setOnClickListener(new MinTestSet());
		ImageButton_minTestAdd.setOnTouchListener(new SetAddTouchListener());
		
		isFloatradioButton=(RadioButton)findViewById(R.id.isFloatradioButton);
		isFloatradioButton.setOnCheckedChangeListener(new isFloatradioButtonCheckChange());
		isIntradioButton=(RadioButton)findViewById(R.id.isIntradioButton);
		isIntradioButton.setOnCheckedChangeListener(new isIntradioButtonCheckChange());
		
		textView_Title=(TextView)findViewById(R.id.textView_Title);
		textViewTemReviseLab=(TextView)findViewById(R.id.textViewTemReviseLab);
		textViewHematResultReviseLab=(TextView)findViewById(R.id.textViewHematResultReviseLab);
		TextViewERSResultReviseLab=(TextView)findViewById(R.id.TextViewERSResultReviseLab);
		TextViewVoltageCompareLab=(TextView)findViewById(R.id.TextViewVoltageCompareLab);
		TextViewVoltageReferenceLab=(TextView)findViewById(R.id.TextViewVoltageReferenceLab);
		TextViewERSInitLab=(TextView)findViewById(R.id.TextViewERSInitLab);
		TextViewResetHeightLab=(TextView)findViewById(R.id.TextViewResetHeightLab);
		button_cancel=(Button)findViewById(R.id.button_cancel);
		button_cancel.setOnClickListener(new CancelListener());
		button_cancel.setOnTouchListener(new ButtonTouchListener());
		button_submit=(Button)findViewById(R.id.button_submit);
		button_submit.setOnClickListener(new SubmitListener());
		button_submit.setOnTouchListener(new ButtonTouchListener());
		ImageButton_TemReviseAdd=(Button)findViewById(R.id.ImageButton_TemReviseAdd);
		ImageButton_TemReviseAdd.setOnClickListener(new TemReviseSet());
		ImageButton_TemReviseAdd.setOnTouchListener(new SetAddTouchListener());
		
		ImageButton_TemRevisePlus=(Button)findViewById(R.id.ImageButton_TemRevisePlus);
		ImageButton_TemRevisePlus.setOnClickListener(new TemReviseSet());
		ImageButton_TemRevisePlus.setOnTouchListener(new SetPlusTouchListener());
		EditText_TemRevise=(EditText)findViewById(R.id.EditText_TemRevise);
		
		
		ImageButton_HematResultReviseAdd=(Button)findViewById(R.id.ImageButton_HematResultReviseAdd);
		ImageButton_HematResultReviseAdd.setOnClickListener(new HematResultReviseSet());
		ImageButton_HematResultReviseAdd.setOnTouchListener(new SetAddTouchListener());
		
		ImageButton_HematResultRevisePlus=(Button)findViewById(R.id.ImageButton_HematResultRevisePlus);
		ImageButton_HematResultRevisePlus.setOnClickListener(new HematResultReviseSet());
		ImageButton_HematResultRevisePlus.setOnTouchListener(new SetPlusTouchListener());
		EditText_HematResultRevise=(EditText)findViewById(R.id.EditText_HematResultRevise);
		
		ImageButton_ERSResultReviseAdd=(Button)findViewById(R.id.ImageButton_ERSResultReviseAdd);
		ImageButton_ERSResultReviseAdd.setOnClickListener(new ERSResultReviseSet());
		ImageButton_ERSResultReviseAdd.setOnTouchListener(new SetAddTouchListener());
		
		ImageButton_ERSResultRevisePlus=(Button)findViewById(R.id.ImageButton_ERSResultRevisePlus);
		ImageButton_ERSResultRevisePlus.setOnClickListener(new ERSResultReviseSet());
		ImageButton_ERSResultRevisePlus.setOnTouchListener(new SetPlusTouchListener());
		
		EditText_ERSResultRevise=(EditText)findViewById(R.id.EditText_ERSResultRevise);
				
		ImageButton_VoltageCompareAdd=(Button)findViewById(R.id.ImageButton_VoltageCompareAdd);
		ImageButton_VoltageCompareAdd.setOnClickListener(new VoltageCompareSet());
		ImageButton_VoltageCompareAdd.setOnTouchListener(new SetAddTouchListener());
		
		ImageButton_VoltageComparePlus=(Button)findViewById(R.id.ImageButton_VoltageComparePlus);
		ImageButton_VoltageComparePlus.setOnClickListener(new VoltageCompareSet());
		ImageButton_VoltageComparePlus.setOnTouchListener(new SetPlusTouchListener());
		
		EditText_VoltageCompare=(EditText)findViewById(R.id.EditText_VoltageCompare);
				
		ImageButton_VoltageReferenceAdd=(Button)findViewById(R.id.ImageButton_VoltageReferenceAdd);
		ImageButton_VoltageReferenceAdd.setOnClickListener(new VoltageReferenceSet());
		ImageButton_VoltageReferenceAdd.setOnTouchListener(new SetAddTouchListener());
		
		ImageButton_VoltageReferencePlus=(Button)findViewById(R.id.ImageButton_VoltageReferencePlus);
		ImageButton_VoltageReferencePlus.setOnClickListener(new VoltageReferenceSet());
		ImageButton_VoltageReferencePlus.setOnTouchListener(new SetPlusTouchListener());
		
		EditText_VoltageReference=(EditText)findViewById(R.id.EditText_VoltageReference);
				
		ImageButton_ERSInitAdd=(Button)findViewById(R.id.ImageButton_ERSInitAdd);
		ImageButton_ERSInitAdd.setOnClickListener(new ERSInitSet());
		ImageButton_ERSInitAdd.setOnTouchListener(new SetAddTouchListener());
		
		ImageButton_ERSInitPlus=(Button)findViewById(R.id.ImageButton_ERSInitPlus);
		ImageButton_ERSInitPlus.setOnClickListener(new ERSInitSet());
		ImageButton_ERSInitPlus.setOnTouchListener(new SetPlusTouchListener());
		
		EditText_ERSInit=(EditText)findViewById(R.id.EditText_ERSInit);
		
		ImageButton_ResetHeightAdd=(Button)findViewById(R.id.ImageButton_ResetHeightAdd);
		ImageButton_ResetHeightAdd.setOnClickListener(new ResetHeightReviseSet());
		ImageButton_ResetHeightAdd.setOnTouchListener(new SetAddTouchListener());
		
		ImageButton_ResetHeightPlus=(Button)findViewById(R.id.ImageButton_ResetHeightPlus);
		ImageButton_ResetHeightPlus.setOnClickListener(new ResetHeightReviseSet());
		ImageButton_ResetHeightPlus.setOnTouchListener(new SetPlusTouchListener());
		
		EditText_ResetHeight=(EditText)findViewById(R.id.EditText_ResetHeight);
	}
	class InitialNumSet implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			InitialNum=Integer.parseInt(EditText_InitialNum.getText().toString().trim());
			
			if(arg0.getId()==R.id.ImageButton_InitialNumPlus)
			{
				if(InitialNum<=InitialNumMin)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最小值":"Reached the minimum value", Toast.LENGTH_LONG).show();
					return;
				}
				InitialNum-=1;
				EditText_InitialNum.setText(fnum1.format(InitialNum));
			}
			else if(arg0.getId()==R.id.ImageButton_InitialNumAdd)
			{
				if(InitialNum>=InitialNumMax)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最大值":"Reached the maximum value", Toast.LENGTH_LONG).show();
					return;
				}
				InitialNum+=1;
				EditText_InitialNum.setText(fnum1.format(InitialNum));
			}
			
			
		}
		
	}
	class MinTestSet implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			minTest=Float.parseFloat(EditText_minTest.getText().toString().trim());
			
			if(arg0.getId()==R.id.ImageButton_minTestPlus)
			{
				if(minTest<=minTestMin)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最小值":"Reached the minimum value", Toast.LENGTH_LONG).show();
					return;
				}
				minTest-=0.1;
				EditText_minTest.setText(fnum1.format(minTest));
			}
			else if(arg0.getId()==R.id.ImageButton_minTestAdd)
			{
				if(minTest>=minTestMax)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最大值":"Reached the maximum value", Toast.LENGTH_LONG).show();
					return;
				}
				minTest+=0.1;
				EditText_minTest.setText(fnum1.format(minTest));
			}
			
			
		}
		
	}
	class isFloatradioButtonCheckChange implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			isFloatradioButton.setChecked(arg1);
			isIntradioButton.setChecked(!arg1);
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
	class isIntradioButtonCheckChange implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			isIntradioButton.setChecked(arg1);
			isFloatradioButton.setChecked(!arg1);
			
		}
		
	}
	class SetPlusTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			Button bt=(Button)arg0;
			if(arg1.getAction()==MotionEvent.ACTION_DOWN)
			{
				bt.setBackgroundResource(R.drawable.lefthui);
			}
			else if(arg1.getAction()==MotionEvent.ACTION_UP)
			{
				bt.setBackgroundResource(R.drawable.left);
			}
			return false;
		}
		
	}
	class SetAddTouchListener implements OnTouchListener{

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			Button bt=(Button)arg0;
			if(arg1.getAction()==MotionEvent.ACTION_DOWN)
			{
				bt.setBackgroundResource(R.drawable.righthui);
			}
			else if(arg1.getAction()==MotionEvent.ACTION_UP)
			{
				bt.setBackgroundResource(R.drawable.right);
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
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请自检完成后设置":"Self-test is not completed, set after the completion of the self-test", Toast.LENGTH_LONG).show();
				return;
			}
			if(MidData.IsReviseRunPeriod)
			{
				Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"正在调整运行周期，请稍后设置":"Adjusting running cycle, please set later", Toast.LENGTH_LONG).show();
				return;
			}
			/*if(MidData.m_upDown.equals("上"))
			{
				Toast.makeText(getApplicationContext(), "请在下降过程设置", Toast.LENGTH_LONG).show();
				return;
			}*/
			if(ConfirmSetDataFormat())
			{
				String[] rownames={"resetheighrevise","xuecheneffectiveheightrangerevise","voltagereferenceset","voltagecompareset","xuechenrevise","yajirevise","temreviseset","showint","mintest","initialnum"};
				String[] value={fnum1.format(ResetHeight),fnum1.format(ERSInit),Integer.toString(VoltageReference),Integer.toString(VoltageCompare),fnum1.format(ERSResultRevise),fnum1.format(HematResultRevise),fnum1.format(TemRevise),isIntradioButton.isChecked()?"1":"0",fnum1.format(minTest),fnum0.format(InitialNum)};
				MidData.mSetParameters.InitialNum=InitialNum;
				byte order = 'A';
			    CalculateFormula tc = new CalculateFormula();
			    byte[] vf = tc.StringTo4Bytes(VoltageReference);
			    byte[] buff = new byte[5];
			    buff[0] = 'S';
			    for (int i = 0; i < 4; i++)
			    {
			    	buff[i + 1] = vf[i];
			    }
			    MidData.m_voltageReferenceSetLast = VoltageReference;
			    
			    if (!MidData.sOp.SendDataToSerial(order, buff, 5))
			    {
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"串口无应答，设置失败，请重新设置":"Serial No Answer，set failed, please re-set",Toast.LENGTH_LONG).show();
					return;
			    }
			   			    
			    byte[] vc = tc.StringTo4Bytes(VoltageCompare);
			    buff[0] = 'O';
			    for (int i = 0; i < 4; i++)
			    {
			    	buff[i + 1] = vc[i];
			    }
			    MidData.m_voltageCompareSetLast = VoltageCompare;
			    
			    if (!MidData.sOp.SendDataToSerial(order, buff, 5))
			    {
			    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"串口无应答，设置失败，请重新设置":"Serial No Answer，set failed, please re-set",Toast.LENGTH_LONG).show();
			    	return;
			    }
			    
			    
			    double tmintest=(((minTest-MidData.mSetParameters.ResetHeightRevise)*1200)-5)/10;
			    byte[] mt = tc.StringTo4Bytes((int)tmintest);
			    buff[0] = 'N';
			    for (int i = 0; i < 4; i++)
			    {
			    	buff[i + 1] = mt[i];
			    }
			    //MidData.m_minTestSetLast = minTest;
			    
			    if (!MidData.sOp.SendDataToSerial(order, buff, 5))
			    {
			    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"串口无应答，设置失败，请重新设置":"Serial No Answer，set failed, please re-set",Toast.LENGTH_LONG).show();
			    	return;
			    }
			    
			    
			    
			    
			    
			    String[] rownameRe=new String[100];
			    String[] valueRe=new String[100];
			    for(int i=0;i<100;i++)
			    {
			    	rownameRe[i]="recompenseheight_"+i;
			    	valueRe[i]=value[0];
			    }
			    if(!MidData.sqlOp.UpdateData(1, "SystemSetTable", rownames, value, 10))
			    {
			    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置失败，请重新设置":"Set failed, please re-set",Toast.LENGTH_LONG).show();
			    	return;
			    }
			    else
			    {
			    	if(!MidData.sqlOp.UpdateData(1, "RecompenseHeightTable", rownameRe, valueRe, 100))
			    	{
			    		Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置失败，请重新设置":"Set failed, please re-set",Toast.LENGTH_LONG).show();
			    		return;
			    	}
			    	else
			    	{
			    		for(int i=0;i<100;i++)
					    {
					    	MidData.mTestParameters[i].aisleRecompenseHeight=ResetHeight;
					    }
			    		MidData.m_minTest=minTest;
			    		MidData.m_resultInt=isIntradioButton.isChecked()?1:0;
			    		MidData.mSetParameters.ResetHeightRevise=ResetHeight;
				    	MidData.mSetParameters.ERSTestIniValue=ERSInit;
				    	MidData.mSetParameters.VoltageReferenceSet=VoltageReference;
				    	MidData.mSetParameters.VolageCompareSet=VoltageCompare;
				    	MidData.mSetParameters.ERSTestResultRevise=ERSResultRevise;
				    	MidData.mSetParameters.HematResultRevise=HematResultRevise;
				    	MidData.mSetParameters.TemperatureRevise=TemRevise;
				    	Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"设置成功,请重新启动设备":"Setting Success，please restart",Toast.LENGTH_LONG).show();
				    	finish();
			    	}
			    	
			    }
			}
			
		}
		
	}
	private boolean ConfirmSetDataFormat(){
		String EditText_InitialNumStr=EditText_InitialNum.getText().toString().trim();
		if(EditText_InitialNumStr.equals(""))
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请设置默认初始编号！":"Please set start number", Toast.LENGTH_LONG).show();
			return false;
		}
		InitialNum=Integer.parseInt(EditText_InitialNumStr);
		
		
		String EditText_ResetHeightStr=EditText_ResetHeight.getText().toString().trim();
		if(EditText_ResetHeightStr.equals(""))
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请设置复位高度修正！":"Please set reset height correction", Toast.LENGTH_LONG).show();
			return false;
		}
		ResetHeight=Float.parseFloat(fnum2.format(Float.parseFloat(EditText_ResetHeightStr)));
		
		if(ResetHeight<ResetHeightMin||ResetHeight>ResetHeightMax)
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"复位高度修正的有效范围是"+fnum1.format(ResetHeightMin)+"-"+fnum1.format(ResetHeightMax)+",请重新设置！":"Reset height correction range is "+fnum1.format(ResetHeightMin)+"-"+fnum1.format(ResetHeightMax)+",please reset！", Toast.LENGTH_LONG).show();
			//EditText_ResetHeight.setText("");
			return false;
		}
		int temp=0;
		if(EditText_ResetHeightStr.contains("."))
		{
			temp=EditText_ResetHeightStr.split("\\.")[1].length();
		}
		if(temp>1)
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"复位高度修正的小数点后1位有效，请重新设置！":"Reset height correction effective when one decimal place, please reset", Toast.LENGTH_LONG).show();
			//EditText_ResetHeight.setText("");
			return false;
		}
		
		
		String EditText_ERSInitStr=EditText_ERSInit.getText().toString().trim();
		
		if(EditText_ERSInitStr.equals(""))
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请设置血沉测试初始值！":"Please set ESR initial value", Toast.LENGTH_LONG).show();
			return false;
		}
		ERSInit=Float.parseFloat(EditText_ERSInitStr);
		if(ERSInit<ERSInitMin||ERSInit>ERSInitMax)
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"血沉测试初始值的有效范围是"+fnum1.format(ERSInitMin)+"-"+fnum1.format(ERSInitMax)+",请重新设置！":"ESR effective initial value range is "+fnum1.format(ERSInitMin)+"-"+fnum1.format(ERSInitMax)+",please reset！", Toast.LENGTH_LONG).show();
			//EditText_ERSInit.setText("");
			return false;
		}
		temp=0;
		if(EditText_ERSInitStr.contains("."))
		{
			temp=EditText_ERSInitStr.split("\\.")[1].length();
		}
		if(temp>1)
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"血沉测试初始值的小数点后1位有效，请重新设置！":"ESR initial value effective when one decimal place, please reset", Toast.LENGTH_LONG).show();
			//EditText_ERSInit.setText("");
			return false;
		}
		
		String EditText_VoltageReferenceStr=EditText_VoltageReference.getText().toString().trim();
		if(EditText_VoltageReferenceStr.equals(""))
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请设置电压参考值！":"Please set the voltage reference value", Toast.LENGTH_LONG).show();
			return false;
		}
		VoltageReference=Integer.parseInt(EditText_VoltageReferenceStr);
		if(VoltageReference<VoltageReferenceMin||VoltageReference>VoltageReferenceMax)
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"电压参考值的有效范围是"+fnum1.format(VoltageReferenceMin)+"-"+fnum1.format(VoltageReferenceMax)+",请重新设置！":"Voltage reference value effective range is "+fnum1.format(VoltageReferenceMin)+"-"+fnum1.format(VoltageReferenceMax)+",please reset！", Toast.LENGTH_LONG).show();
			//EditText_VoltageReference.setText("");
			return false;
		}
		VoltageCompare=Integer.parseInt(EditText_VoltageCompare.getText().toString().trim());
		if(VoltageCompare<VoltageCompareMin||VoltageCompare>VoltageCompareMax)
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"电压比较值的有效范围是"+fnum1.format(VoltageCompareMin)+"-"+fnum1.format(VoltageCompareMax)+",请重新设置！":"Voltage comparator value effective range is "+fnum1.format(VoltageCompareMin)+"-"+fnum1.format(VoltageCompareMax)+",please reset！", Toast.LENGTH_LONG).show();
			//EditText_VoltageReference.setText("");
			return false;
		}
		
		
		String EditText_ERSResultReviseStr=EditText_ERSResultRevise.getText().toString().trim();
		if(EditText_ERSResultReviseStr.equals(""))
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请设置血沉测试结果修正！":"Please set ESR result correction", Toast.LENGTH_LONG).show();
			return false;
		}
		ERSResultRevise=Float.parseFloat(EditText_ERSResultReviseStr);
		if(ERSResultRevise<ERSResultReviseMin||ERSResultRevise>ERSResultReviseMax)
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"血沉测试结果修正的有效范围是"+fnum1.format(ERSResultReviseMin)+"-"+fnum1.format(ERSResultReviseMax)+",请重新设置！":"ESR result correction effective range is "+fnum1.format(ERSResultReviseMin)+"-"+fnum1.format(ERSResultReviseMax)+",please reset！", Toast.LENGTH_LONG).show();
			//EditText_ERSResultRevise.setText("");
			return false;
		}
		temp=0;
		if(EditText_ERSResultReviseStr.contains("."))
		{
			temp=EditText_ERSResultReviseStr.split("\\.")[1].length();
		}
		if(temp>1)
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"血沉测试结果修正的小数点后1位有效，请重新设置！":"ESR result correction effective when one decimal place, please reset", Toast.LENGTH_LONG).show();
			
			//EditText_ERSResultRevise.setText("");
			return false;
		}
		
		
		
		String EditText_HematResultReviseStr=EditText_HematResultRevise.getText().toString().trim();
		if(EditText_HematResultReviseStr.equals(""))
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请设置压积测试结果修正！":"Please set HCT result correction", Toast.LENGTH_LONG).show();
			return false;
		}
		HematResultRevise=Float.parseFloat(EditText_HematResultReviseStr);
		if(HematResultRevise<HematResultReviseMin||HematResultRevise>HematResultReviseMax)
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"压积测试结果修正的有效范围是"+fnum1.format(HematResultReviseMin)+"-"+fnum1.format(HematResultReviseMax)+",请重新设置！":"HCT result correction effective range is "+fnum1.format(HematResultReviseMin)+"-"+fnum1.format(HematResultReviseMax)+",please reset！", Toast.LENGTH_LONG).show();
			//EditText_HematResultRevise.setText("");
			return false;
		}
		temp=0;
		if(EditText_HematResultReviseStr.contains("."))
		{
			temp=EditText_HematResultReviseStr.split("\\.")[1].length();
		}
		if(temp>1)
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"压积测试结果修正的小数点后1位有效，请重新设置！":"HCT result correction effective when one decimal place, please reset", Toast.LENGTH_LONG).show();
			//EditText_HematResultRevise.setText("");
			return false;
		}
		
		String EditText_TemReviseStr=EditText_TemRevise.getText().toString().trim();
		if(EditText_TemReviseStr.equals(""))
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请设置温度修正！":"Please set temperature correction", Toast.LENGTH_LONG).show();
			return false;
		}
		TemRevise=Float.parseFloat(EditText_TemReviseStr);
		if(TemRevise<TemReviseMin||TemRevise>TemReviseMax)
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"温度修正的有效范围是"+fnum1.format(TemReviseMin)+"-"+fnum1.format(TemReviseMax)+",请重新设置！":"Temperature correction range is "+fnum1.format(TemReviseMin)+"-"+fnum1.format(TemReviseMax)+"please reset", Toast.LENGTH_LONG).show();
			//EditText_TemRevise.setText("");
			return false;
		}
		temp=0;
		if(EditText_TemReviseStr.contains("."))
		{
			temp=EditText_TemReviseStr.split("\\.")[1].length();
		}
		if(temp>1)
		{
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"温度修正的小数点后1位有效，请重新设置！":"Temperature correction effective when one decimal place, please reset", Toast.LENGTH_LONG).show();
			//EditText_TemRevise.setText("");
			return false;
		}
		
		//翻译补充
		String EditText_minTestStr=EditText_minTest.getText().toString().trim();
		if(EditText_minTest.equals(""))
		{
			//Toast.makeText(getApplicationContext(), MidData.IsChinese?"请设置测试下限！":"Please set reset height correction", Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"请设置测试下限":"Please set minimum of detection！", Toast.LENGTH_LONG).show();
			
			return false;
		}
		minTest=Float.parseFloat(fnum2.format(Float.parseFloat(EditText_minTestStr)));
		
		if(minTest<minTestMin||minTest>minTestMax)
		{
			//Toast.makeText(getApplicationContext(), MidData.IsChinese?"测试下限的有效范围是"+fnum1.format(minTestMin)+"-"+fnum1.format(minTestMax)+",请重新设置！":"Reset height correction range is "+fnum1.format(ResetHeightMin)+"-"+fnum1.format(ResetHeightMax)+",please reset！", Toast.LENGTH_LONG).show();
			//EditText_ResetHeight.setText("");
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"测试下限的有效范围是:"+fnum1.format(minTestMin)+"-"+fnum1.format(minTestMax)+",请重新设置！":"Minimum valid range:"+fnum1.format(minTestMin)+"-"+fnum1.format(minTestMax)+",please reset！", Toast.LENGTH_LONG).show();
			
			return false;
		}
		temp=0;
		if(EditText_minTestStr.contains("."))
		{
			temp=EditText_minTestStr.split("\\.")[1].length();
		}
		if(temp>1)
		{
			//Toast.makeText(getApplicationContext(), MidData.IsChinese?"测试下限的小数点后1位有效，请重新设置！":"Reset height correction effective when one decimal place, please reset", Toast.LENGTH_LONG).show();
			//EditText_ResetHeight.setText("");
			Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"检测下限的小数点后1位有效，请重新设置！":"Valid when one decimal place of minimum-detection,please reset", Toast.LENGTH_LONG).show();
			
			return false;
		}
		
		
		
		
		
		
		
		return true;
	}
	class TemReviseSet implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			TemRevise=Float.parseFloat(EditText_TemRevise.getText().toString().trim());
			
			if(arg0.getId()==R.id.ImageButton_TemRevisePlus)
			{
				if(TemRevise<=TemReviseMin)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最小值":"Reached the minimum value", Toast.LENGTH_LONG).show();
					return;
				}
				TemRevise-=0.1;
				EditText_TemRevise.setText(fnum1.format(TemRevise));
			}
			else if(arg0.getId()==R.id.ImageButton_TemReviseAdd)
			{
				if(TemRevise>=TemReviseMax)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最大值":"Reached the maximum value", Toast.LENGTH_LONG).show();
					return;
				}
				TemRevise+=0.1;
				EditText_TemRevise.setText(fnum1.format(TemRevise));
			}
		}		
	}
	class HematResultReviseSet implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			HematResultRevise=Float.parseFloat(EditText_HematResultRevise.getText().toString().trim());
			
			if(arg0.getId()==R.id.ImageButton_HematResultRevisePlus)
			{
				if(HematResultRevise<=HematResultReviseMin)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最小值":"Reached the minimum value", Toast.LENGTH_LONG).show();
					return;
				}
				HematResultRevise-=0.1;
				EditText_HematResultRevise.setText(fnum1.format(HematResultRevise));
			}
			else if(arg0.getId()==R.id.ImageButton_HematResultReviseAdd)
			{
				if(HematResultRevise>=HematResultReviseMax)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最大值":"Reached the maximum value", Toast.LENGTH_LONG).show();
					return;
				}
				HematResultRevise+=0.1;
				EditText_HematResultRevise.setText(fnum1.format(HematResultRevise));
			}
		}
		
	}
	class ERSResultReviseSet implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			ERSResultRevise=Float.parseFloat(EditText_ERSResultRevise.getText().toString().trim());
			
			if(arg0.getId()==R.id.ImageButton_ERSResultRevisePlus)
			{
				if(ERSResultRevise<=ERSResultReviseMin)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最小值":"Reached the minimum value", Toast.LENGTH_LONG).show();
					return;
				}
				ERSResultRevise-=0.1;
				EditText_ERSResultRevise.setText(fnum1.format(ERSResultRevise));
			}
			else if(arg0.getId()==R.id.ImageButton_ERSResultReviseAdd)
			{
				if(ERSResultRevise>=ERSResultReviseMax)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最大值":"Reached the maximum value", Toast.LENGTH_LONG).show();
					return;
				}
				ERSResultRevise+=0.1;
				EditText_ERSResultRevise.setText(fnum1.format(ERSResultRevise));
			}
				
		}
		
	}
	class VoltageCompareSet implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			VoltageCompare=Integer.parseInt(EditText_VoltageCompare.getText().toString().trim());
			
			if(arg0.getId()==R.id.ImageButton_VoltageComparePlus)
			{
				if(VoltageCompare<=VoltageCompareMin)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最小值":"Reached the minimum value", Toast.LENGTH_LONG).show();
					return;
				}
				VoltageCompare--;
				EditText_VoltageCompare.setText(Integer.toString(VoltageCompare));
				
			}
			else if(arg0.getId()==R.id.ImageButton_VoltageCompareAdd)
			{
				if(VoltageCompare>=VoltageCompareMax)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最大值":"Reached the maximum value", Toast.LENGTH_LONG).show();
					return;
				}
				VoltageCompare++;
				EditText_VoltageCompare.setText(Integer.toString(VoltageCompare));
			}
		}
		
	}
	class VoltageReferenceSet implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			VoltageReference=Integer.parseInt(EditText_VoltageReference.getText().toString().trim());
			
			if(arg0.getId()==R.id.ImageButton_VoltageReferencePlus)
			{
				if(VoltageReference<=VoltageReferenceMin)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最小值":"Reached the minimum value", Toast.LENGTH_LONG).show();
					return;
				}
				VoltageReference--;
				EditText_VoltageReference.setText(Integer.toString(VoltageReference));
			}
			else if(arg0.getId()==R.id.ImageButton_VoltageReferenceAdd)
			{
				if(VoltageReference>=VoltageReferenceMax)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最大值":"Reached the maximum value", Toast.LENGTH_LONG).show();
					return;
				}
				VoltageReference++;
				EditText_VoltageReference.setText(Integer.toString(VoltageReference));
			}
				
		}
		
	}
	class ERSInitSet implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			ERSInit=Float.parseFloat(EditText_ERSInit.getText().toString().trim());
			
			if(arg0.getId()==R.id.ImageButton_ERSInitPlus)
			{
				if(ERSInit<=ERSInitMin)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最小值":"Reached the minimum value", Toast.LENGTH_LONG).show();
					return;
				}
				ERSInit-=0.1;
				EditText_ERSInit.setText(fnum1.format(ERSInit));
				
			}
			else if(arg0.getId()==R.id.ImageButton_ERSInitAdd)
			{
				if(ERSInit>=ERSInitMax)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最大值":"Reached the maximum value", Toast.LENGTH_LONG).show();
					return;
				}
				ERSInit+=0.1;
				EditText_ERSInit.setText(fnum1.format(ERSInit));
			}
		}
		
	}
	class ResetHeightReviseSet implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			ResetHeight=Float.parseFloat(EditText_ResetHeight.getText().toString().trim());
			
			if(arg0.getId()==R.id.ImageButton_ResetHeightPlus)
			{
				if(ResetHeight<=ResetHeightMin)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最小值":"Reached the minimum value", Toast.LENGTH_LONG).show();
					return;
				}
				ResetHeight-=0.1;
				EditText_ResetHeight.setText(fnum1.format(ResetHeight));
			}
			else if(arg0.getId()==R.id.ImageButton_ResetHeightAdd)
			{
				if(ResetHeight>=ResetHeightMax)
				{
					Toast.makeText(getApplicationContext(), MidData.IsChinese==1?"已到达最大值":"Reached the maximum value", Toast.LENGTH_LONG).show();
					return;
				}
				ResetHeight+=0.1;
				EditText_ResetHeight.setText(fnum1.format(ResetHeight));
			}
		}		
	}
}
