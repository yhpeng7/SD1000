package com.example.sd1000application;

import com.example.sd1000application.TestUpActivity.ButtonTouchListener;
import com.realect.sd1000.parameterStatus.MidData;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;

public class ReTestSetActivity extends Activity{
	String rowname[]={"0","1","2","3","4","5","6","7","8","9"};
	String colname[]={"A","B","C","D","E","F","G","H","I","J"};
	private ArrayAdapter<String> rowadapter;
	private ArrayAdapter<String> coladapter;
	Spinner spinnerRowName[]=new Spinner[5];
	Spinner spinnerColName[]=new Spinner[5];
	CheckBox checkboxrow[]=new CheckBox[5];
	EditText editTextNum[]= new EditText[5];
	Button buttonOK;
	Button buttonCancels;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retest);	
		LoadResource();
		InitalStatus();
	}
	private void  InitalStatus()
	{		
		setRowAble(0,false);
		setRowAble(1,false);
		setRowAble(2,false);
		setRowAble(3,false);
		setRowAble(4,false);
	}
	private boolean UpdateNum(int num)
	{
		if(checkboxrow[num].isChecked())
		{
			String numstr=editTextNum[num].getText().toString();
			int nums=spinnerRowName[num].getSelectedItemPosition()*10+spinnerColName[num].getSelectedItemPosition();
			String pass=spinnerColName[num].getSelectedItem().toString()+spinnerRowName[num].getSelectedItem().toString();
			if(MidData.mTestParameters[nums].testStatus!=0)
			{
				new AlertDialog.Builder(ReTestSetActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(MidData.IsChinese==1?"通道"+pass+"正在使用！":"The passage "+pass+" is using")
			    .setPositiveButton(MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{
						//ReTestSetActivity.this.finish();
					}
				    }).show();
				return false;
				
			}
			if(MidData.mTestParameters[nums].aisleStatus != 1)
			{
				new AlertDialog.Builder(ReTestSetActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(MidData.IsChinese==1?"通道"+pass+"不可用！":"The passage "+pass+" can't use")
			    .setPositiveButton(MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{
						//ReTestSetActivity.this.finish();
					}
				    }).show();
				return false;
				
			}
			boolean f=false;
		    for(int i=0;i<100;i++)
		    {
		    	if(!MidData.mTestParameters[i].aisleNum.equals("")&&MidData.mTestParameters[i].aisleNum.equals(numstr))
		    	{
		    		f=true;
		    		break;
		    	}
		    }
		    if(f)
		    {
		    	new AlertDialog.Builder(ReTestSetActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(
			    		MidData.IsChinese==1?"编号："+ numstr+"正在检测":"number "+numstr+" is measuring")
			    .setPositiveButton(
			    		MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{					
					}
				    }).setCancelable(false)
			    .show();
    			return false;
		    }
		    if (!MidData.sqlOp.YaJiTestFinished(numstr)&&!MidData.sqlOp.XueChenTestFinished(numstr))
			{
				new AlertDialog.Builder(ReTestSetActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(
			    		MidData.IsChinese==1?"编号："+ numstr+"未做检测":"number "+numstr+"have not be measured")
			    .setPositiveButton(
			    		MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{					
					}
				    }).setCancelable(false)
			    .show();
    			return false;
			}
			
			
			if(numstr.equals(""))
			{
				new AlertDialog.Builder(ReTestSetActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(MidData.IsChinese==1?spinnerRowName[num].getSelectedItem().toString()+spinnerColName[num].getSelectedItem().toString()+"标本号为空！":spinnerRowName[num].getSelectedItem().toString()+spinnerColName[num].getSelectedItem().toString()+"Num is null")
			    .setPositiveButton(MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{
						//ReTestSetActivity.this.finish();
					}
				    }).show();
				return false;
				
			}			
			if(MidData.mTestParameters[nums].aisleStatus != 1)
			{
				new AlertDialog.Builder(ReTestSetActivity.this)
			    .setTitle(MidData.IsChinese==1?"消息提示":"Notice")
			    .setMessage(MidData.IsChinese==1?spinnerRowName[num].getSelectedItem().toString()+spinnerColName[num].getSelectedItem().toString()+"通道不可用！":spinnerRowName[num].getSelectedItem().toString()+spinnerColName[num].getSelectedItem().toString()+"Error")
			    .setPositiveButton(MidData.IsChinese==1?"确定":"Comfirm",
				    new DialogInterface.OnClickListener()
				    {
					public void onClick(
						DialogInterface dialog,
						int whichButton)
					{
						//ReTestSetActivity.this.finish();
					}
				    }).show();
				return false;				
			}
			MidData.mTestParameters[nums].IsNewSample=false;		    
			MidData.mTestParameters[nums].ReDetect=true;
			MidData.mTestParameters[nums].aisleNumShow=MidData.mTestParameters[nums].aisleNum=editTextNum[num].getText().toString();
			return true;
		}
		return true;
	}
	class buttonOKClick  implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
			if(UpdateNum(0)&&UpdateNum(1)&&UpdateNum(2)&&UpdateNum(3)&&UpdateNum(4))
			{
				finish();				
			}		
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
	class buttonCancelsClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub			
			finish();
		}		
	}
	private void LoadResource()
	{
		buttonCancels=(Button)findViewById(R.id.buttonCancels);
		buttonCancels.setOnClickListener(new buttonCancelsClickListener());
		buttonCancels.setOnTouchListener(new ButtonTouchListener());
		
		buttonOK=(Button)findViewById(R.id.buttonOK);
		buttonOK.setOnClickListener(new buttonOKClick());
		buttonOK.setOnTouchListener(new ButtonTouchListener());
		
		editTextNum[0]=(EditText)findViewById(R.id.editTextNum1);
		editTextNum[1]=(EditText)findViewById(R.id.editTextNum2);
		editTextNum[2]=(EditText)findViewById(R.id.editTextNum3);
		editTextNum[3]=(EditText)findViewById(R.id.editTextNum4);
		editTextNum[4]=(EditText)findViewById(R.id.editTextNum5);
		
		checkboxrow[0]=(CheckBox)findViewById(R.id.checkBox1);
		checkboxrow[0].setOnCheckedChangeListener(new checkboxrowChecked());
		checkboxrow[1]=(CheckBox)findViewById(R.id.checkBox2);
		checkboxrow[1].setOnCheckedChangeListener(new checkboxrowChecked());
		checkboxrow[2]=(CheckBox)findViewById(R.id.checkBox3);
		checkboxrow[2].setOnCheckedChangeListener(new checkboxrowChecked());
		checkboxrow[3]=(CheckBox)findViewById(R.id.checkBox4);
		checkboxrow[3].setOnCheckedChangeListener(new checkboxrowChecked());
		checkboxrow[4]=(CheckBox)findViewById(R.id.checkBox5);
		checkboxrow[4].setOnCheckedChangeListener(new checkboxrowChecked());
		
		spinnerColName[0]=(Spinner)findViewById(R.id.spinnerColName1);
		spinnerColName[1]=(Spinner)findViewById(R.id.spinnerColName2);
		spinnerColName[2]=(Spinner)findViewById(R.id.spinnerColName3);
		spinnerColName[3]=(Spinner)findViewById(R.id.spinnerColName4);		
		spinnerColName[4]=(Spinner)findViewById(R.id.spinnerColName5);
		coladapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,colname);
		coladapter.setDropDownViewResource(R.layout.spinner_dropdown);
		spinnerColName[0].setAdapter(coladapter);
		spinnerColName[1].setAdapter(coladapter);
		spinnerColName[2].setAdapter(coladapter);
		spinnerColName[3].setAdapter(coladapter);
		spinnerColName[4].setAdapter(coladapter);
		
		
		spinnerRowName[0]=(Spinner)findViewById(R.id.spinnerRowName1);
		spinnerRowName[1]=(Spinner)findViewById(R.id.spinnerRowName2);
		spinnerRowName[2]=(Spinner)findViewById(R.id.spinnerRowName3);
		spinnerRowName[3]=(Spinner)findViewById(R.id.spinnerRowName4);
		spinnerRowName[4]=(Spinner)findViewById(R.id.spinnerRowName5);
		rowadapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,rowname);
		rowadapter.setDropDownViewResource(R.layout.spinner_dropdown);
		spinnerRowName[0].setAdapter(rowadapter);
		spinnerRowName[1].setAdapter(rowadapter);
		spinnerRowName[2].setAdapter(rowadapter);
		spinnerRowName[3].setAdapter(rowadapter);
		spinnerRowName[4].setAdapter(rowadapter);
	}
	class checkboxrowChecked implements OnCheckedChangeListener{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			int row=99;
			if(arg0.getId()==R.id.checkBox1)
			{
				row=0;			
			}
			else if(arg0.getId()==R.id.checkBox2)
			{
				row=1;			
			}
			else if(arg0.getId()==R.id.checkBox3)
			{
				row=2;			
			}
			else if(arg0.getId()==R.id.checkBox4)
			{
				row=3;			
			}
			else if(arg0.getId()==R.id.checkBox5)
			{
				row=4;			
			}	
			setRowAble(row,arg1);
		}
		
	}
	private void setRowAble(int row,boolean fg)
	{
		spinnerColName[row].setEnabled(fg);
		spinnerRowName[row].setEnabled(fg);
		editTextNum[row].setEnabled(fg);
	}
}
