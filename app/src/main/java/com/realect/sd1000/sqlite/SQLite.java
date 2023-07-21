package com.realect.sd1000.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;
import com.realect.sd1000.parameterStatus.*;

public class SQLite extends SQLiteOpenHelper
{

    public SQLite(Context context, String name, CursorFactory factory,
	    int version)
    {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	// TODO Auto-generated constructor stub
    }

    public final static String DATABASE_NAME = "SD1000DayaBase.db";
    public final static int DATABASE_VERSION = 1;
    public final static String TABLE_NAME1 = "DS1000ResourceTable";
    public final static String DS1000Resource_ID = "id";
    public final static String Date = "date"; // 日期
    public final static String Time = "time"; // 时间
    public final static String XuHao = "xuhao"; // 序号
    public final static String TestXueChen_bit = "testxcbit"; // 血沉测试位
    public final static String TestYaJi_bit = "testyjbit"; // 压积测试位
    public final static String Resource_Data = "resourcedata";// 原始返回数据
    public final static String XueChen_Data = "xuechendata"; // 血沉值
    public final static String YaJi_data = "yajidata"; // 压积
    public final static String Temp = "temp";// 温度
    public final static String XueChenTestTimeType = "xuechentesttimetype";// 血沉测试时间类型60分钟还是30分钟
    public final static String PlusPeriod = "plusperiod";
    public final static String IsDebug = "isdebug";
    public final static String isFinishXueChenTest = "isfinishxuechentest";// 血沉测试是否完成，0：未完成；1：完成
    public final static String isFinishYaJiTest = "isfinishyajitest";// 压积测试是否完成，0：未完成；1：完成
    public final static String Resource_YaJi_Data = "resourceyajidata";// 压积的原始高度
    public final static String TempResource = "tempres";// 温度
    
    public final static String TABLE_NAME2 = "RecompenseHeightTable";

    public final static String RecompenseHeight_ID = "id";
    public final static String RecompenseHeight_0 = "recompenseheight_0";
    public final static String RecompenseHeight_1 = "recompenseheight_1";
    public final static String RecompenseHeight_2 = "recompenseheight_2";
    public final static String RecompenseHeight_3 = "recompenseheight_3";
    public final static String RecompenseHeight_4 = "recompenseheight_4";
    public final static String RecompenseHeight_5 = "recompenseheight_5";
    public final static String RecompenseHeight_6 = "recompenseheight_6";
    public final static String RecompenseHeight_7 = "recompenseheight_7";
    public final static String RecompenseHeight_8 = "recompenseheight_8";
    public final static String RecompenseHeight_9 = "recompenseheight_9";
    public final static String RecompenseHeight_10 = "recompenseheight_10";
    public final static String RecompenseHeight_11 = "recompenseheight_11";
    public final static String RecompenseHeight_12 = "recompenseheight_12";
    public final static String RecompenseHeight_13 = "recompenseheight_13";
    public final static String RecompenseHeight_14 = "recompenseheight_14";
    public final static String RecompenseHeight_15 = "recompenseheight_15";
    public final static String RecompenseHeight_16 = "recompenseheight_16";
    public final static String RecompenseHeight_17 = "recompenseheight_17";
    public final static String RecompenseHeight_18 = "recompenseheight_18";
    public final static String RecompenseHeight_19 = "recompenseheight_19";

    public final static String RecompenseHeight_20 = "recompenseheight_20";
    public final static String RecompenseHeight_21 = "recompenseheight_21";
    public final static String RecompenseHeight_22 = "recompenseheight_22";
    public final static String RecompenseHeight_23 = "recompenseheight_23";
    public final static String RecompenseHeight_24 = "recompenseheight_24";
    public final static String RecompenseHeight_25 = "recompenseheight_25";
    public final static String RecompenseHeight_26 = "recompenseheight_26";
    public final static String RecompenseHeight_27 = "recompenseheight_27";
    public final static String RecompenseHeight_28 = "recompenseheight_28";
    public final static String RecompenseHeight_29 = "recompenseheight_29";

    public final static String RecompenseHeight_30 = "recompenseheight_30";
    public final static String RecompenseHeight_31 = "recompenseheight_31";
    public final static String RecompenseHeight_32 = "recompenseheight_32";
    public final static String RecompenseHeight_33 = "recompenseheight_33";
    public final static String RecompenseHeight_34 = "recompenseheight_34";
    public final static String RecompenseHeight_35 = "recompenseheight_35";
    public final static String RecompenseHeight_36 = "recompenseheight_36";
    public final static String RecompenseHeight_37 = "recompenseheight_37";
    public final static String RecompenseHeight_38 = "recompenseheight_38";
    public final static String RecompenseHeight_39 = "recompenseheight_39";

    public final static String RecompenseHeight_40 = "recompenseheight_40";
    public final static String RecompenseHeight_41 = "recompenseheight_41";
    public final static String RecompenseHeight_42 = "recompenseheight_42";
    public final static String RecompenseHeight_43 = "recompenseheight_43";
    public final static String RecompenseHeight_44 = "recompenseheight_44";
    public final static String RecompenseHeight_45 = "recompenseheight_45";
    public final static String RecompenseHeight_46 = "recompenseheight_46";
    public final static String RecompenseHeight_47 = "recompenseheight_47";
    public final static String RecompenseHeight_48 = "recompenseheight_48";
    public final static String RecompenseHeight_49 = "recompenseheight_49";

    public final static String RecompenseHeight_50 = "recompenseheight_50";
    public final static String RecompenseHeight_51 = "recompenseheight_51";
    public final static String RecompenseHeight_52 = "recompenseheight_52";
    public final static String RecompenseHeight_53 = "recompenseheight_53";
    public final static String RecompenseHeight_54 = "recompenseheight_54";
    public final static String RecompenseHeight_55 = "recompenseheight_55";
    public final static String RecompenseHeight_56 = "recompenseheight_56";
    public final static String RecompenseHeight_57 = "recompenseheight_57";
    public final static String RecompenseHeight_58 = "recompenseheight_58";
    public final static String RecompenseHeight_59 = "recompenseheight_59";

    public final static String RecompenseHeight_60 = "recompenseheight_60";
    public final static String RecompenseHeight_61 = "recompenseheight_61";
    public final static String RecompenseHeight_62 = "recompenseheight_62";
    public final static String RecompenseHeight_63 = "recompenseheight_63";
    public final static String RecompenseHeight_64 = "recompenseheight_64";
    public final static String RecompenseHeight_65 = "recompenseheight_65";
    public final static String RecompenseHeight_66 = "recompenseheight_66";
    public final static String RecompenseHeight_67 = "recompenseheight_67";
    public final static String RecompenseHeight_68 = "recompenseheight_68";
    public final static String RecompenseHeight_69 = "recompenseheight_69";

    public final static String RecompenseHeight_70 = "recompenseheight_70";
    public final static String RecompenseHeight_71 = "recompenseheight_71";
    public final static String RecompenseHeight_72 = "recompenseheight_72";
    public final static String RecompenseHeight_73 = "recompenseheight_73";
    public final static String RecompenseHeight_74 = "recompenseheight_74";
    public final static String RecompenseHeight_75 = "recompenseheight_75";
    public final static String RecompenseHeight_76 = "recompenseheight_76";
    public final static String RecompenseHeight_77 = "recompenseheight_77";
    public final static String RecompenseHeight_78 = "recompenseheight_78";
    public final static String RecompenseHeight_79 = "recompenseheight_79";

    public final static String RecompenseHeight_80 = "recompenseheight_80";
    public final static String RecompenseHeight_81 = "recompenseheight_81";
    public final static String RecompenseHeight_82 = "recompenseheight_82";
    public final static String RecompenseHeight_83 = "recompenseheight_83";
    public final static String RecompenseHeight_84 = "recompenseheight_84";
    public final static String RecompenseHeight_85 = "recompenseheight_85";
    public final static String RecompenseHeight_86 = "recompenseheight_86";
    public final static String RecompenseHeight_87 = "recompenseheight_87";
    public final static String RecompenseHeight_88 = "recompenseheight_88";
    public final static String RecompenseHeight_89 = "recompenseheight_89";

    public final static String RecompenseHeight_90 = "recompenseheight_90";
    public final static String RecompenseHeight_91 = "recompenseheight_91";
    public final static String RecompenseHeight_92 = "recompenseheight_92";
    public final static String RecompenseHeight_93 = "recompenseheight_93";
    public final static String RecompenseHeight_94 = "recompenseheight_94";
    public final static String RecompenseHeight_95 = "recompenseheight_95";
    public final static String RecompenseHeight_96 = "recompenseheight_96";
    public final static String RecompenseHeight_97 = "recompenseheight_97";
    public final static String RecompenseHeight_98 = "recompenseheight_98";
    public final static String RecompenseHeight_99 = "recompenseheight_99";

    public final static String TABLE_NAME3 = "SystemSetTable";
    public final static String SystemSet_ID = "id";
    public final static String Product_ID = "productid";
    public final static String TestType = "testtype"; // 测试类型 0：血沉测试；1：压积测试
   	public final static String XueChenTestType = "xuechentesttype"; // 0:60分钟;1:30分钟
    //public final static String TestWay = "testway"; // 0:魏氏法；1：温氏法
    public final static String ResetHeighRevise = "resetheighrevise"; // 血沉高度修正
    public final static String XueChenRevise = "xuechenrevise";// 血沉修正
    public final static String YaJiRevise = "yajirevise"; // 压积修正
    //public final static String IsTemRevise = "istemrevise"; // 温度拟合  0不拟合 1拟合
    public final static String TemReviseSet = "temreviseset"; // 温度修正设置
    public final static String IsAutoPrint = "isautoprint"; // 0不自动打印，1:血沉测试完自动打印；2压积测试完自动打印；3血沉、压积测试完均打印
    public final static String IsPrintChart = "isprintchart"; // 0不打印曲线，1:打印曲线
    //public final static String IsHeightRevise="isheightresivse";//高度拟合  0不选；1选中
    public final static String IsAutoUpLoadData = "isautouploaddata"; // 0，不自动上传数据；1:自动上传数据到SA；2自动上传数据的LIS；3自动上传数据到SA、LIS
    public final static String IsAutoTemResive = "isautotemrevise"; // 1:自动温度修正；；0反之
    public final static String MaxRPMOfDrivce = "maxrpmofdrivce";// 最大步数
    public final static String PulsePeriodSet = "pulseperiodset";// 脉冲周期
    public final static String PulusPeriodReal = "pulseperiodreal";// 脉冲周期实际值
    public final static String PlyDistanceSet = "plydistanceset";// 往返最大距离
    public final static String VoltageReferenceSet = "voltagereferenceset";// 电压参考值设置
    public final static String VoltageCompareSet = "voltagecompareset";// 电压比较值设置
    public final static String AnticoagulantsHeightSet="anticoagulantsheightset";//抗凝剂高度
    public final static String InitialNum="initialnum";
    public final static String FanOpenClose="fanopenclose";
    public final static String IpStr="ipstr";
    public final static String Port="port";
    public final static String Language="language";
    //public final static String XueChenMin="xuechenmin";
    //public final static String XueChenMax="xuechenmax";
    //public final static String YaJiMin="yajimin";
    //public final static String YaJiMax="yajimax";
    public final static String ShowInt="showint";
    public final static String MinTest="mintest";
    public final static String QualityDate="qualitydate";
    public final static String QualityNum="qualitynum";
    
    
    public final static String CurveX6 = "curveX6";// 曲线拟合X(6)
    public final static String CurveX5 = "curveX5";// 曲线拟合X(5)
    public final static String CurveX4 = "curveX4";// 曲线拟合X(4)
    public final static String CurveX3 = "curveX3";// 曲线拟合X(3)
    public final static String CurveX2 = "curveX2";// 曲线拟合X(2)
    public final static String CurveX1 = "curveX1";// 曲线拟合X(1)
    public final static String CurveX0 = "curveX0";// 曲线拟合X(0)
    public final static String IsCurveFit = "iscurvefit";// 是否启动曲线拟合：1：开启；0：关闭
    public final static String LastTargetNum = "lasttargetnum";// 最后一个序号//给血沉
    public final static String LastTargetNumYJ = "lasttargetnumyj";// 最后一个序号//给血沉
    public final static String SetNumWay="setnumway";//编号设置方式 0测试前编号 1测试后编号
    //public final static String LastNumSetWay="lastnumsetway";//编号设置方式,0全自动；1扫描枪；2手动；
    public final static String XueChenEffectiveHeightRangeRevise = "xuecheneffectiveheightrangerevise";
    public final static String XueChenPrint="xuechenprint";//血沉完成打印
    public final static String YaJiPrint="yajiprint";//压积完成打印
    public final static String ConsumerReduce="consumerreduce";//耗材递减
    public final static String LisBrt="lisbrt";//串口Lis上传端口波特率
    public final static String LisUplodLine="lisuploadline";//lis上传曲线


    public final static String TABLE_NAME4 = "ConsumableInforTable"; // 耗材统计表
    public final static String ConsumInfor_ID = "id"; // 耗材信息ID
    public final static String Consum_ID = "consumid"; // 耗材ID
    public final static String ConsumXueChen_Name = "consumnamexuechen"; // 耗材血沉管名称
    public final static String ConsumYaJi_Name = "consumnameyaji"; // 耗材压积管名称

    public final static String ConsumXueChen_Count = "consumxuechencount"; // 血沉管剩余量
    public final static String ConsumYaJi_Count = "consumyajicount"; // 压积管剩余量
    public final static String LastCreditCard_Time = "lastcreditcardtime"; // 最后一次刷卡时间

    public boolean tabIsExist(String tabName)
    {
	boolean result = false;
	if (tabName == null)
	{
	    return false;
	}
	SQLiteDatabase db = null;
	Cursor cursor = null;
	try
	{
	    db = this.getReadableDatabase();
	    String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"
		    + tabName.trim() + "' ";
	    cursor = db.rawQuery(sql, null);
	    if (cursor.moveToNext())
	    {
		int count = cursor.getInt(0);
		if (count > 0)
		{
		    result = true;
		}
	    }

	}
	catch (Exception e)
	{
	    if(cursor!=null)
	    	cursor.close();
	    if(db!=null)
	    	db.close();
	}
	finally
	{
		if(cursor!=null)
	    	cursor.close();
	    if(db!=null)
	    	db.close();
	}
	return result;
    }
    public boolean UpdateXueChenByNo(String Num, String address, String res,String result,String temp,String xuechentesttimetype,String plusperiod,String isdebug,String finish,String sourcetemp)
        {
    	Time t = new Time();
    	t.setToNow();
    	String date = Double.toString(t.year * Math.pow(10, 4) + (t.month + 1)
    		* Math.pow(10, 2) + t.monthDay);
    	String time = Double.toString(t.hour * Math.pow(10, 2) + t.minute);
    	ContentValues cv = new ContentValues();
    	/*cv.put("xuechendata", value);
    	cv.put("isfinishyajitest", finishFlag);
    	cv.put("resourceyajidata", res);
    	cv.put("testyjbit", testyjbit);*/
    	cv.put(Date, date);
    	cv.put(Time, time);
    	cv.put(XuHao, Num);
    	cv.put(TestXueChen_bit, address);
    	//cv.put(TestYaJi_bit, s[5]);
    	cv.put(Resource_Data, res);
    	cv.put(XueChen_Data, result);
    	//cv.put(YaJi_data, s[8]);
    	cv.put(Temp, temp);
    	cv.put(XueChenTestTimeType,xuechentesttimetype);
    	cv.put(PlusPeriod, plusperiod);
    	cv.put(IsDebug, isdebug);
    	cv.put(isFinishXueChenTest, finish);
    	//cv.put(isFinishYaJiTest, s[14]);
    /*	
    	ContentValues cv = new ContentValues();
    	cv.put("xuechendata", value);
    	cv.put("isfinishyajitest", finishFlag);
    	cv.put("resourceyajidata", res);
    	cv.put("testyjbit", testyjbit);*/
    	/*Time t = new Time();
    	t.setToNow();
    	String date = Double.toString(t.year * Math.pow(10, 4) + (t.month + 1)
    		* Math.pow(10, 2) + t.monthDay);
    	String time = Double.toString(t.hour * Math.pow(10, 2) + t.minute);*/
    	boolean flag = false;
    	int num=0;
    	while (true)
    	{
    		if(num>100)
    			break;
    	    if (!MidData.isSql)
    	    {
    		MidData.isSql = true;
    		SQLiteDatabase db = this.getWritableDatabase();
    		try {
    			db.update("DS1000ResourceTable", cv, "xuhao = ? and date = ?", new String[]
    					{ Num,date });
    			db.close();	
    			MidData.exSqlOk=true;
    			flag = true;			
    		} catch (Exception e) {
    			// TODO: handle exception
    			if(db!=null)
    				db.close();
    			MidData.isSql = false;
    		}
    		finally{
    			if(db!=null)
    				db.close();
    			MidData.isSql = false;
    		}
    		if(flag)
    	    {
    	    	MidData.isSql = false;
    			break;
    	    }
    	    }
    	    else
    	    {
    			try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    	    }
    	    
    	    num++;
    	}
    	return flag;
        }
    public boolean UpdateYaJiByNo(String Num, String value, String res,
	    String finishFlag,String testyjbit)
    {
	ContentValues cv = new ContentValues();
	cv.put("yajidata", value);
	cv.put("isfinishyajitest", finishFlag);
	cv.put("resourceyajidata", res);
	cv.put("testyjbit", testyjbit);
	Time t = new Time();
	t.setToNow();
	String date = Double.toString(t.year * Math.pow(10, 4) + (t.month + 1)
		* Math.pow(10, 2) + t.monthDay);
	String time = Double.toString(t.hour * Math.pow(10, 2) + t.minute);
	boolean flag = false;
	int num=0;
	while (true)
	{
		if(num>100)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			db.update("DS1000ResourceTable", cv, "xuhao = ? and date = ?", new String[]
					{ Num,date });
			db.close();	
			MidData.exSqlOk=true;
			flag = true;			
		} catch (Exception e) {
			// TODO: handle exception
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		if(flag)
	    {
	    	MidData.isSql = false;
			break;
	    }
	    }
	    else
	    {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    num++;
	}
	return flag;
    }

    public void DeleteById(String TableName, String[] id, int len)
    {
	boolean flag = false;
	int time = 0;
	System.out.println("-----Deletedata-----DeleteById----");
	while (true)
	{
		if (time > 5)
			break;
	    if (!MidData.isSql)
	    {
			MidData.isSql = true;
			SQLiteDatabase dbd = this.getWritableDatabase();
			try {
				for (int i = 0; i < len; i++)
				{
				    String sql = "delete from " + TableName + " where id ="
					    + id[i] + " ";
				    dbd.execSQL(sql);
				}
				dbd.close();
				flag = true;
			MidData.exSqlOk=true;
			} catch (Exception e) {
				// TODO: handle exception
				if(dbd!=null)
					dbd.close();
				MidData.isSql = false;
			}
			finally
			{
				if(dbd!=null)
					dbd.close();
				MidData.isSql = false;
			}		
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	    }
	    if(flag)
	    {
	    	MidData.isSql = false;			
			break;
	    }
	    time++;
	    
	}
    }

    public void DeleteDataByNum(String num)
    {
	boolean flag = false;
	int time = 0;
	String sql = "delete from " + TABLE_NAME1 + " where xuhao ='"
			+ num+"'";

	System.out.println("-----Deletedata-----DeleteDataByNum----");
	while (true)
	{
		if (time > 50)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		SQLiteDatabase dbd = this.getWritableDatabase();
		try {
			dbd.execSQL(sql);
			dbd.close();
			flag = true;
			MidData.exSqlOk=true;
		} catch (Exception e) {
			// TODO: handle exception
			if(dbd!=null)
				dbd.close();
			MidData.isSql = false;
		}
		finally{
			if(dbd!=null)
				dbd.close();
			MidData.isSql = false;
		}
		if(flag)
	    {
			MidData.isSql = false;			
			break;
	    }
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    time++;
	}
    }

    public boolean UpdateData(int id, String Table_name, String[] rowname,
	    String[] value, int num)
    {
	ContentValues cv = new ContentValues();
	for (int i = 0; i < num; i++)
	{
	    cv.put(rowname[i], value[i]);
	}
	long row = 0;
	int time=0;
	while (true)
	{
		if(time>100)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			row = db.update(Table_name, cv, "id = ?", new String[]
					{ Double.toString(id) });
			db.close();
			MidData.isSql = false;
			break;
		} catch (Exception e) {
			// TODO: handle exception
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}		
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    time++;
	}
	if (row == id)
	    return true;
	else
	    return false;
    }
    public boolean UpdateQualityNum(int num)
        {
    	boolean fg=false;
    	ContentValues cv = new ContentValues();
    	Time t = new Time();
    	t.setToNow();
    	double Nowdate=t.year * Math.pow(10, 4) + (t.month + 1)* Math.pow(10, 2) + t.monthDay;
    	
    	cv.put(QualityDate, Double.toString(Nowdate));
    	
    	cv.put(QualityNum, Integer.toString(num));
    	long row = 0;
    	int time=0;
    	while (true)
    	{
    		if(time>100)
    			break;
    	    if (!MidData.isSql)
    	    {
    		MidData.isSql = true;
    		SQLiteDatabase db = this.getWritableDatabase();
    		try {
    			row = db.update("SystemSetTable", cv, "id = ?", new String[]
    					{ Double.toString(1) });
    			fg=true;
    			db.close();
    			MidData.isSql = false;
    			break;
    		} catch (Exception e) {
    			// TODO: handle exception
    			if(db!=null)
    				db.close();
    			MidData.isSql = false;
    		}
    		finally{
    			if(db!=null)
    				db.close();
    			MidData.isSql = false;
    		}		
    	    }
    	    else
    	    {
    	    	try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    	    }
    	    
    	    time++;
    	}
    	return fg;
        }
    public boolean UpdateSysemSet(int id, String Table_name, String[] rowname,
	    String[] value, int num)
    {
	ContentValues cv = new ContentValues();
	for (int i = 0; i < num; i++)
	{
	    cv.put(rowname[i], value[i]);
	}
	long row = 0;
	int time=0;
	while (true)
	{
		if(time>5)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			row = db.update(Table_name, cv, "id = ?", new String[]
					{ Integer.toString(id) });
			db.close();
			MidData.isSql = false;
			break;
		} catch (Exception e) {
			// TODO: handle exception
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		
		
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    time++;
	}
	if (row == id)
	    return true;
	else
	    return false;
    }

    public void InsertData(String num ,String xh, String testxcbit, String testyjbit, String resource,
	    String xuechen, String yaji, String tem,
	    String xuechentesttimetype, String plusperiod, String isdebug,
	    String isfinishXC, String isfinishYJ, String Resource_YaJi,String Resource_Temp)
    {
	Time t = new Time();
	t.setToNow();
	String date = Double.toString(t.year * Math.pow(10, 4) + (t.month + 1)
		* Math.pow(10, 2) + t.monthDay);
	String time = Double.toString(t.hour * Math.pow(10, 2) + t.minute);
	//System.out.println(time+"IIIIIIIIIIInnnnnnnTime");
	String sql=num+"#"+date+"#"+time+"#"+xh+"#"+testxcbit+"#"+testyjbit+"#"+resource+"#"+xuechen+"#"+yaji+"#"+tem
			+"#"+xuechentesttimetype+"#"+plusperiod+"#"+isdebug+"#"+isfinishXC+"#"+isfinishYJ+"#"+Resource_YaJi+"#"+Resource_Temp;
	//System.out.println(sql+"------QueueInsert---");
	MidData.SqlQueue.add(sql);
	/*ContentValues cv = new ContentValues();
	cv.put(Date, date);
	cv.put(Time, time);
	cv.put(XuHao, xh);
	cv.put(TestXueChen_bit, testxcbit);
	cv.put(TestYaJi_bit, testyjbit);
	cv.put(Resource_Data, resource);
	cv.put(XueChen_Data, xuechen);
	cv.put(YaJi_data, yaji);
	cv.put(Temp, tem);
	cv.put(XueChenTestTimeType, xuechentesttimetype);
	cv.put(PlusPeriod, plusperiod);
	cv.put(IsDebug, isdebug);
	cv.put(isFinishXueChenTest, isfinishXC);
	cv.put(isFinishYaJiTest, isfinishYJ);
	cv.put(Resource_YaJi_Data,Resource_YaJi);
	//long row = db.insert(TABLE_NAME1, null, cv);
	String sql = "INSERT INTO DS1000ResourceTable"
		+ " (date,time,xuhao,testxcbit,testyjbit,resourcedata,xuechendata,yajidata,temp,xuechentesttimetype,plusperiod,isdebug,isfinishxuechentest,isfinishyajitest,resourceyajidata) "
		+ "VALUES ('"
		+ date
		+ "','"
		+ time
		+ "','"
		+ xh
		+ "','"
		+ testxcbit
		+ "','"
		+ testyjbit
		+ "','"
		+ resource
		+ "','"
		+ xuechen
		+ "','"
		+ yaji
		+ "','"
		+ tem
		+ "','"
		+ xuechentesttimetype
		+ "','"
		+ plusperiod
		+ "','"
		+ isdebug
		+ "','"
		+ isfinishXC
		+ "','"
		+ isfinishYJ
		+ "','"
		+ Resource_YaJi + "')";
	//System.out.println("datedatedatedate="+date);
	long row = 0;
	int times=0;
	
	while (true)
	{
		if(times>50)
			break;
		times++;
		//System.out.println(MidData.isSql+"--------exSQLFlag");
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		SQLiteDatabase db = this.getWritableDatabase();
		try {			
			row = db.insert(TABLE_NAME1, null, cv);
			db.close();			
			MidData.isSql = false;
			break;
		} catch (Exception e) {
			// TODO: handle exception
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		
	    }
	    else
	    {
		//Sleep sleep = new Sleep();
				//sleep.start();
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}*/
	//return row;
    }
    public boolean ExInsertTestResult(String sql)
    {
    	String[] s=sql.split("#");
    	//long row = 0;
    	//int times=0;
    	//System.out.println(sql+"-----------exSQL----"+s.length);
    	ContentValues cv = new ContentValues();
    	cv.put(Date, s[1]);
    	cv.put(Time, s[2]);
    	cv.put(XuHao, s[3]);
    	cv.put(TestXueChen_bit, s[4]);
    	cv.put(TestYaJi_bit, s[5]);
    	cv.put(Resource_Data, s[6]);
    	cv.put(XueChen_Data, s[7]);
    	cv.put(YaJi_data, s[8]);
    	cv.put(Temp, s[9]);
    	cv.put(XueChenTestTimeType, s[10]);
    	cv.put(PlusPeriod, s[11]);
    	cv.put(IsDebug, s[12]);
    	cv.put(isFinishXueChenTest, s[13]);
    	cv.put(isFinishYaJiTest, s[14]);
    	if(s.length<=15)
    	{
    		cv.put(Resource_YaJi_Data,"");
    		cv.put(TempResource, s[15]);
    	}    		
    	else
    	{
    		cv.put(Resource_YaJi_Data,s[15]);
    		cv.put(TempResource, s[16]);
    	}
    		
    	
    	
    		//System.out.println(MidData.isSql+"--------exSQLFlag");
    	    
    		
    		//Sleep sleep = new Sleep();
    				//sleep.start();
    		SQLiteDatabase db = this.getWritableDatabase();
    		try {			
    			MidData.mTestParameters[Integer.parseInt(s[0])].saveDatabaseID = db.insert(TABLE_NAME1, null, cv);
    			db.close();			
    			
    		} catch (Exception e) {
    			// TODO: handle exception
    			if(db!=null)
    				db.close();
    			
    		}
    		finally{
    			if(db!=null)
    				db.close();
    		}
    		
    	   
    	    
    	//System.out.println(MidData.mTestParameters[Integer.parseInt(s[0])].saveDatabaseID+"----save");
    	//if(row==0)
    	//	return false;
    	//else
    	     return true;
    }
    
    public void onUpgrade()
    {
	// TODO Auto-generated method stub
    	
    	if(!checkColumnExists2(TABLE_NAME3,ConsumerReduce))
    	{
    		SQLiteDatabase db=this.getWritableDatabase();
    		String cmd="ALTER TABLE " + TABLE_NAME3 + " ADD COLUMN " + ConsumerReduce +" text ";
    		db.execSQL(cmd);
    		
    		db.close();
    		update(1,"SystemSetTable", "consumerreduce","0");
    	}
    	if(!checkColumnExists2(TABLE_NAME3,LisBrt))
    	{
    		SQLiteDatabase db=this.getWritableDatabase();
    		String cmd="ALTER TABLE " + TABLE_NAME3 + " ADD COLUMN " + LisBrt +" text ";
    		db.execSQL(cmd);
    		
    		db.close();
    		update(1,"SystemSetTable", "lisbrt","1200");
    	}
    	if(!checkColumnExists2(TABLE_NAME3,LisUplodLine))
    	{
    		SQLiteDatabase db=this.getWritableDatabase();
    		String cmd="ALTER TABLE " + TABLE_NAME3 + " ADD COLUMN " + LisUplodLine +" text ";
    		db.execSQL(cmd);
    		
    		db.close();
    		update(1,"SystemSetTable", "lisuploadline","0");
    	}
	    //db.execSQL("ALTER TABLE MainTable ADD COLUMN thistimestartdivceontime double");
    }
    public boolean checkColumnExists2(String tableName, String columnName) 
    {
    	    boolean result = false ;
    	    SQLiteDatabase db= this.getReadableDatabase();
    	    Cursor cursor = null ;

    	    try{
    	    	String str="select "+columnName+" from " + tableName;
    	        cursor = db.rawQuery(str,null);
    	        result = null != cursor && cursor.moveToFirst() ;
    	    }catch (Exception e){
    	        //Log.e(TAG,"checkColumnExists2..." + e.getMessage()) ;
    	    }finally{
    	        if(null != cursor && !cursor.isClosed()){
    	            cursor.close() ;
    	        }
    	        db.close();
    	    }

    	    return result ;
    }
    public void CreateSDSystemSetTable(SQLiteDatabase db)
    {

	String sql;
	if (tabIsExist(TABLE_NAME3))
	{
	    db.execSQL(" DROP TABLE " + TABLE_NAME3);
	}
	sql = "CREATE TABLE " + TABLE_NAME3 + " (" + SystemSet_ID

	+ " INTEGER primary key autoincrement, " + TestType + " text,"
		+ XueChenTestType + " text,"
		+ ResetHeighRevise + " text," + XueChenRevise + " text,"
		+ YaJiRevise + " text," + TemReviseSet + " text," + IsPrintChart + " text," + IsAutoPrint
		+ " text," + IsAutoUpLoadData + " text," + IsAutoTemResive
		+ " text," + MaxRPMOfDrivce + " text," + PulsePeriodSet
		+ " text," + PulusPeriodReal + " text," + PlyDistanceSet
		+ " text," + VoltageReferenceSet + " text," + AnticoagulantsHeightSet
		+ " text," + FanOpenClose
		+ " text," + IpStr + " text," + Port + " text," + VoltageCompareSet
		+ " text," + CurveX6 + " text," + CurveX5 + " text," + CurveX4
		+ " text," + CurveX3 + " text," + CurveX2 + " text," + CurveX1
		+ " text," + CurveX0 + " text," + Product_ID + " text,"
		+ IsCurveFit + " text," + LastTargetNum + " text,"
		+ XueChenEffectiveHeightRangeRevise + " text,"
		+ XueChenPrint + " text,"
		+ YaJiPrint + " text,"+ LastTargetNumYJ + " text,"
		+ SetNumWay + " text,"+ Language + " text,"
		+ ShowInt + " text,"
		+ MinTest + " text,"
		+InitialNum+" text,"
		+QualityDate+" text,"
		+QualityNum+" text)";

	db.execSQL(sql);
	ContentValues cv = new ContentValues();

	cv.put(TestType, "0");
	
	cv.put(XueChenTestType, "1");
	cv.put(ResetHeighRevise, "9.0");
	cv.put(XueChenRevise, "0.0");
	cv.put(YaJiRevise, "0.0");
	cv.put(TemReviseSet, "0.0");
	cv.put(IsPrintChart, "1");
	cv.put(IsAutoPrint, "0");
	cv.put(IsAutoUpLoadData, "1");
	cv.put(IsAutoTemResive, "0");
	cv.put(MaxRPMOfDrivce, "75495");
	cv.put(PulsePeriodSet, "30");// 795
	cv.put(PulusPeriodReal, "4238");
	cv.put(PlyDistanceSet, "70");
	cv.put(VoltageReferenceSet, "1750");
	cv.put(AnticoagulantsHeightSet, "0.0");
	cv.put(FanOpenClose, "1");
	cv.put(IpStr, "192.168.0.54");
	cv.put(Port, "9977");
	cv.put(VoltageCompareSet, "1000");
	cv.put(CurveX6, "0.0000000000");
	cv.put(CurveX5, "0.0000000000");
	cv.put(CurveX4, "0.0000210000");
	cv.put(CurveX3, "0.0005850000");
	cv.put(CurveX2, "0.0072440000");
	cv.put(CurveX1, "0.6653850000");
	cv.put(CurveX0, "0.0914410000");
	cv.put(Product_ID, "SD-00001");
	cv.put(IsCurveFit, "1");
	cv.put(LastTargetNum, "0");
	cv.put(XueChenEffectiveHeightRangeRevise, "8.0");
	cv.put(XueChenPrint, "0");
	cv.put(YaJiPrint, "0");
	cv.put(LastTargetNumYJ, "0");
	cv.put(SetNumWay, "0");
	cv.put(Language, "1");
	cv.put(ShowInt, "1");
	cv.put(MinTest, "12.0");
	cv.put(InitialNum, "1");
	cv.put(QualityDate, "160101");
	cv.put(QualityNum, "9991");
	db.insert(TABLE_NAME3, null, cv);

    }

    public void CreateSDSystemSetTableDB(SQLiteDatabase db)
    {

	String sql;
	if (tabIsExist(TABLE_NAME3))
	{
	    return;
	}
	sql = "CREATE TABLE " + TABLE_NAME3 + " (" + SystemSet_ID

	+ " INTEGER primary key autoincrement, " + TestType + " text,"
		
		+ XueChenTestType + " text,"
		+ ResetHeighRevise + " text," + XueChenRevise + " text,"
		+ YaJiRevise + " text," + TemReviseSet + " text," + IsPrintChart + " text," + IsAutoPrint
		+ " text," + IsAutoUpLoadData + " text," + IsAutoTemResive
		+ " text," + MaxRPMOfDrivce + " text," + PulsePeriodSet
		+ " text," + PulusPeriodReal + " text," + PlyDistanceSet
		+ " text," + VoltageReferenceSet + " text," + AnticoagulantsHeightSet
		+ " text," + FanOpenClose
		+ " text," + IpStr + " text," + Port + " text," + VoltageCompareSet
		+ " text," + CurveX6 + " text," + CurveX5 + " text," + CurveX4
		+ " text," + CurveX3 + " text," + CurveX2 + " text," + CurveX1
		+ " text," + CurveX0 + " text," + Product_ID + " text,"
		+ IsCurveFit + " text," + LastTargetNum + " text,"
		+ XueChenEffectiveHeightRangeRevise + " text,"
		+ XueChenPrint + " text,"+ YaJiPrint + " text,"+ LastTargetNumYJ + " text,"
		+ SetNumWay + " text,"+ Language + " text,"
		+ ShowInt + " text,"
		+ MinTest + " text,"
		+InitialNum+" text,"
		+QualityDate+" text,"
		+QualityNum+" text)";

	db.execSQL(sql);
	ContentValues cv = new ContentValues();

	cv.put(TestType, "0");
	
	cv.put(XueChenTestType, "1");
	cv.put(ResetHeighRevise, "9.0");
	cv.put(XueChenRevise, "0.0");
	cv.put(YaJiRevise, "0.0");
	cv.put(TemReviseSet, "0.0");
	cv.put(IsPrintChart, "1");

	cv.put(IsAutoPrint, "0");
	cv.put(IsAutoUpLoadData, "1");
	cv.put(IsAutoTemResive, "0");
	cv.put(MaxRPMOfDrivce, "75495");
	cv.put(PulsePeriodSet, "30");// 795
	cv.put(PulusPeriodReal, "4238");
	cv.put(PlyDistanceSet, "70");
	cv.put(VoltageReferenceSet, "1750");
	cv.put(AnticoagulantsHeightSet, "0.0");
	cv.put(FanOpenClose, "1");
	cv.put(IpStr, "192.168.0.54");
	cv.put(Port, "9977");
	cv.put(VoltageCompareSet, "1000");
	cv.put(CurveX6, "0.0000000000");
	cv.put(CurveX5, "0.0000000000");
	cv.put(CurveX4, "0.0000210000");
	cv.put(CurveX3, "0.0005850000");
	cv.put(CurveX2, "0.0072440000");
	cv.put(CurveX1, "0.6653850000");
	cv.put(CurveX0, "0.0914410000");
	cv.put(Product_ID, "SD-00001");
	cv.put(IsCurveFit, "1");
	cv.put(LastTargetNum, "0");
	cv.put(XueChenEffectiveHeightRangeRevise, "8.0");
	cv.put(XueChenPrint, "0");
	cv.put(YaJiPrint, "0");
	cv.put(LastTargetNumYJ, "0");
	cv.put(SetNumWay, "0");
	cv.put(Language, "1");
	cv.put(ShowInt, "1");
	cv.put(MinTest, "12.0");
	cv.put(InitialNum, "1");
	cv.put(QualityDate, "160101");
	cv.put(QualityNum, "9991");
	db.insert(TABLE_NAME3, null, cv);
    }

    public void CreateSDConsumInfoTable(SQLiteDatabase db)
    {

	String sql;
	if (tabIsExist(TABLE_NAME4))
	{
	    db.execSQL(" DROP TABLE " + TABLE_NAME4);
	}
	sql = "CREATE TABLE " + TABLE_NAME4 + " (" + ConsumInfor_ID

	+ " INTEGER primary key autoincrement, " + Consum_ID + " text,"
		+ ConsumXueChen_Name + " text," + ConsumYaJi_Name + " text, "
		+ ConsumXueChen_Count + " int, " + ConsumYaJi_Count + " int, "
		+ LastCreditCard_Time + " text);";

	db.execSQL(sql);
	ContentValues cv1 = new ContentValues();

	cv1.put(Consum_ID, "0");
	cv1.put(ConsumXueChen_Name, "血沉试管");
	cv1.put(ConsumYaJi_Name, "压积试管");
	cv1.put(ConsumXueChen_Count, "500");
	cv1.put(ConsumYaJi_Count, "500");
	cv1.put(LastCreditCard_Time, "2014-01-01 12:12");
	long row = db.insert(TABLE_NAME4, null, cv1);

    }

    public void CreateSDConsumInfoTableDB(SQLiteDatabase db)
    {

	String sql;
	if (tabIsExist(TABLE_NAME4))
	{
	    return;
	}
	sql = "CREATE TABLE " + TABLE_NAME4 + " (" + ConsumInfor_ID

	+ " INTEGER primary key autoincrement, " + Consum_ID + " text,"
		+ ConsumXueChen_Name + " text," + ConsumYaJi_Name + " text, "
		+ ConsumXueChen_Count + " int, " + ConsumYaJi_Count + " int, "
		+ LastCreditCard_Time + " text);";

	db.execSQL(sql);
	ContentValues cv1 = new ContentValues();

	cv1.put(Consum_ID, "0");
	cv1.put(ConsumXueChen_Name, "血沉试管");
	cv1.put(ConsumYaJi_Name, "压积试管");
	cv1.put(ConsumXueChen_Count, "500");
	cv1.put(ConsumYaJi_Count, "500");
	cv1.put(LastCreditCard_Time, "2014-01-01 12:12");
	long row = db.insert(TABLE_NAME4, null, cv1);

    }

    public boolean UpdateConsumInfo(int[] num)
    {
	boolean flag = false;
	int times = 0;

	while (true)
	{
		if (times >= 5)
	    {
		break;
	    }
	    
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		SQLiteDatabase db = this.getWritableDatabase();
		String where = " consumid = ?";
		String[] whereValue =
		{ Integer.toString(0) };
		ContentValues cv1 = new ContentValues();
		cv1.put("consumxuechencount", Integer.toString(num[0]));
		cv1.put("consumyajicount", Integer.toString(num[1]));
		try {
			db.update(TABLE_NAME4, cv1, where, whereValue);
			db.close();
			flag = true;
			MidData.isSql = false;
			break;
		} catch (Exception e) {
			// TODO: handle exception
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    times++;
	}

	return flag;
    }

    public void CreateSDResourceTable(SQLiteDatabase db)
    {

	String sql;
	if (tabIsExist(TABLE_NAME1))
	{
	    db.execSQL(" DROP TABLE " + TABLE_NAME1);
	    // return;
	}
	sql = "CREATE TABLE " + TABLE_NAME1 + " (" + DS1000Resource_ID

	+ " INTEGER primary key autoincrement, " + Date + " double," + Time
		+ " double, " + XuHao + " text," + TestXueChen_bit + " text," + TestYaJi_bit + " text,"
		+ Resource_Data + " text," + XueChen_Data + " text,"
		+ YaJi_data + " text," + Temp + " text," + XueChenTestTimeType
		+ " text," + PlusPeriod + " text," + IsDebug + " text,"
		+ isFinishXueChenTest + " text," + isFinishYaJiTest + " text,"
		+ Resource_YaJi_Data + " text,"
		+ TempResource + " text);";

	db.execSQL(sql);
	/*ContentValues cv = new ContentValues();
	String s = "";
	for (int i = 0; i < 30; i++)
	{
	    s += Integer.toString(i + 1) + "," + Integer.toString(i + 1);
	    if (i < 29)
	    {
		s += ";";
	    }
	}
	cv.put(Date, "20140620");
	cv.put(Time, "1009");
	cv.put(XuHao, "1406200001");
	cv.put(TestXueChen_bit, "0");
	cv.put(TestYaJi_bit, "0");
	cv.put(Resource_Data, s);
	cv.put(XueChen_Data, "2.0");
	cv.put(YaJi_data, "1.0");
	cv.put(Temp, "15");
	cv.put(XueChenTestTimeType, "1");
	cv.put(PlusPeriod, "30");
	cv.put(IsDebug, "0");
	cv.put(isFinishXueChenTest, "1");
	cv.put(isFinishYaJiTest, "1");
	cv.put(Resource_YaJi_Data, "1,10;2,20");
	long row = db.insert(TABLE_NAME1, null, cv);
	cv.put(XuHao, "1406200002");
	row = db.insert(TABLE_NAME1, null, cv);
	cv.put(XuHao, "1406200003");
	row = db.insert(TABLE_NAME1, null, cv);
	cv.put(XuHao, "1406200004");
	row = db.insert(TABLE_NAME1, null, cv);
	cv.put(XuHao, "1406200005");
	row = db.insert(TABLE_NAME1, null, cv);
	cv.put(XuHao, "1406200006");
	row = db.insert(TABLE_NAME1, null, cv);*/
    }

    public void CreateSDResourceTableDB(SQLiteDatabase db)
    {
	String sql;
	if (tabIsExist(TABLE_NAME1))
	{
	    // db.execSQL(" DROP TABLE "+TABLE_NAME1);
	    return;
	}
	sql = "CREATE TABLE " + TABLE_NAME1 + " (" + DS1000Resource_ID

	+ " INTEGER primary key autoincrement, " + Date + " double," + Time
		+ " double, " + XuHao + " text," + TestXueChen_bit + " text," + TestYaJi_bit + " text,"
		+ Resource_Data + " text," + XueChen_Data + " text,"
		+ YaJi_data + " text," + Temp + " text," + XueChenTestTimeType
		+ " text," + PlusPeriod + " text," + IsDebug + " text,"
		+ isFinishXueChenTest + " text," + isFinishYaJiTest + " text,"
		+ Resource_YaJi_Data + " text,"
		+ TempResource + " text);";

	db.execSQL(sql);
	/*ContentValues cv = new ContentValues();
	String s = "";
	for (int i = 0; i < 30; i++)
	{
	    s += Integer.toString(i + 1) + "," + Integer.toString(i + 1);
	    if (i < 29)
	    {
		s += ";";
	    }
	}
	cv.put(Date, "20140620");
	cv.put(Time, "1009");
	cv.put(XuHao, "1406200001");
	cv.put(TestXueChen_bit, "0");
	cv.put(TestYaJi_bit, "0");
	cv.put(Resource_Data, s);
	cv.put(XueChen_Data, "2.0");
	cv.put(YaJi_data, "1.0");
	cv.put(Temp, "15");
	cv.put(XueChenTestTimeType, "1");
	cv.put(PlusPeriod, "30");
	cv.put(IsDebug, "0");
	cv.put(isFinishXueChenTest, "1");
	cv.put(isFinishYaJiTest, "1");
	cv.put(Resource_YaJi_Data, "1,10;2,20");
	long row = db.insert(TABLE_NAME1, null, cv);
	cv.put(XuHao, "1406200002");
	row = db.insert(TABLE_NAME1, null, cv);
	cv.put(XuHao, "1406200003");
	row = db.insert(TABLE_NAME1, null, cv);
	cv.put(XuHao, "1406200004");
	row = db.insert(TABLE_NAME1, null, cv);
	cv.put(XuHao, "1406200005");
	row = db.insert(TABLE_NAME1, null, cv);
	cv.put(XuHao, "1406200006");
	row = db.insert(TABLE_NAME1, null, cv);*/
    }

    public void CreateSDRecompenseHeightTable(SQLiteDatabase db)
    {

	String sql;
	if (tabIsExist(TABLE_NAME2))
	{
	    db.execSQL(" DROP TABLE " + TABLE_NAME2);
	}
	sql = "CREATE TABLE " + TABLE_NAME2 + " (" + RecompenseHeight_ID

	+ " INTEGER primary key autoincrement";// lastelecthottime
	for (int i = 0; i < 100; i++)
	{
	    sql += ", recompenseheight_" + Integer.toString(i) + " text";
	}
	sql += ");";
	sql = sql;
	db.execSQL(sql);
	ContentValues cv = new ContentValues();
	// cv.put(LastTargetNum, "0");
	for (int i = 0; i < 100; i++)
	{
	    String rowname = "recompenseheight_" + Integer.toString(i);
	    cv.put(rowname, "9.0");
	}
	long row = db.insert(TABLE_NAME2, null, cv);
	int a = 0;
	// row = db.insert(TABLE_NAME2, null, cv);
    }

    public void CreateSDRecompenseHeightTableDB(SQLiteDatabase db)
    {

	String sql;
	if (tabIsExist(TABLE_NAME2))
	{
	    return;
	}
	sql = "CREATE TABLE " + TABLE_NAME2 + " (" + RecompenseHeight_ID

	+ " INTEGER primary key autoincrement";// lastelecthottime
	for (int i = 0; i < 100; i++)
	{
	    sql += ", recompenseheight_" + Integer.toString(i) + " text";
	}
	sql += ");";
	sql = sql;
	db.execSQL(sql);
	ContentValues cv = new ContentValues();
	// cv.put(LastTargetNum, "0");
	for (int i = 0; i < 100; i++)
	{
	    String rowname = "recompenseheight_" + Integer.toString(i);
	    cv.put(rowname, "9.0");
	}
	long row = db.insert(TABLE_NAME2, null, cv);
    }
    // 创建table
    public void onCreate(SQLiteDatabase db)
    {
	CreateSDResourceTable(db);
	CreateSDRecompenseHeightTable(db);
	CreateSDSystemSetTable(db);
	CreateSDConsumInfoTable(db);
    }

    public void onCreateDataBase(SQLiteDatabase db)
    {
	CreateSDResourceTableDB(db);
	CreateSDRecompenseHeightTableDB(db);
	CreateSDSystemSetTableDB(db);
	CreateSDConsumInfoTableDB(db);
	onUpgrade();	
    }

    public String selectById(String TABLE_NAME, String[] getStr, int id)
    {

	String value = null;
	int time=0;
	while (true)
	{
		if(time>5)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		Cursor cursor=null;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			cursor = db.query(TABLE_NAME, getStr, "id=?",
					new String[]
					{ Integer.toString(id) }, null, null, null);

				while (cursor.moveToNext())
				{
				    // for(int i=0;i<len;i++)
				    // {
				    value = cursor.getString(cursor.getColumnIndex(getStr[0]));
				    // }
				}
				db.close();
				MidData.isSql = false;
				break;
		} catch (Exception e) {
			// TODO: handle exception
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}		
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    time++;
	}

	return value;
    }

    public String[] select(String TABLE_NAME, String[] getStr, int len)
    {

	String[] value = new String[len];
	for (int i = 0; i < len; i++)
	{
	    value[i] = "";
	}
	int time=0;
	while (true)
	{
		if(time>10)
			break;
		
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor =null;
		try {
			cursor = db.query(TABLE_NAME, getStr, "id=?",
					new String[]
					{ "1" }, null, null, null);

				while (cursor.moveToNext())
				{
				    for (int i = 0; i < len; i++)
				    {
					value[i] = cursor.getString(cursor
						.getColumnIndex(getStr[i]));
				    }
				}
				cursor.close();
				db.close();
				MidData.isSql = false;
				MidData.exSqlOk=true;
				break;
		} catch (Exception e) {
			// TODO: handle exception
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally
		{
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		
	    }
	    else
	    {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    time++;
	}

	return value;
    }

    public String[] selectConsumData(String[] getStr, int len)
    {
	
	String[] value = new String[len];
	int time=0;
	while (true)
	{
		if(time>5)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		Cursor cursor=null;
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select * from ConsumableInforTable";
		try {
			cursor = db.rawQuery(sql, null);// db.query(TABLE_NAME, getStr,
			// "type=?,time>=?,time<=?", new
			// String[]{con,s,e}, null,
			// null,null);
			while (cursor.moveToNext())
			{
			for (int i = 0; i < len; i++)
			{
			value[i] = cursor.getString(cursor
				.getColumnIndex(getStr[i]));
			}
			}
			cursor.close();
			db.close();
			MidData.exSqlOk=true;
			MidData.isSql = false;
			break;
		} catch (Exception e) {
			// TODO: handle exception
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    time++;
	}
	return value;
    }

    public float[] selectRecompenseData()
    { // 获取补偿数据
	
	float[] value = new float[100];
	int time=0;
	while (true)
	{
		if(time>5)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		Cursor cursor = null;
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select * from RecompenseHeightTable";
		try {
			cursor = db.rawQuery(sql, null);// db.query(TABLE_NAME, getStr,
			// "type=?,time>=?,time<=?", new
			// String[]{con,s,e}, null,
			// null,null);
		while (cursor.moveToNext())
		{
		for (int i = 0; i < 100; i++)
		{
		value[i] = Float.parseFloat(cursor.getString(cursor
			.getColumnIndex("recompenseheight_"
				+ Integer.toString(i))));
		}
		}
		cursor.close();
		db.close();
		MidData.exSqlOk=true;
		MidData.isSql = false;
		break;
		} catch (Exception e) {
			// TODO: handle exception
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
	    }
	    else
	    {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    time++;
	}
	return value;
    }

    public Cursor selectServerUpData()
    {
	Cursor cursor;
	while (true)
	{
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select * from UpdateSetedDataToServerCache";
		cursor = db.rawQuery(sql, null);
		
		MidData.isSql = false;
		break;
	    }
	    else
	    {
		//Sleep sleep = new Sleep();
				//sleep.start();
	    }
	}
	return cursor;
    }
    public int GetQualityNum()
    {
    	int num=99999;
    	Cursor cursor = null;
    	Time t = new Time();
    	t.setToNow();
    	double Nowdate=t.year * Math.pow(10, 4) + (t.month + 1)* Math.pow(10, 2) + t.monthDay;
    	boolean flag=false;
    	boolean exFlag=false;
    	//double n = Double.parseDouble(num);
    	int time=0;
    	while (true)
    	{
    		if(time>20)
    			break;
    	    if (!MidData.isSql)
    	    {
    		MidData.isSql = true;
    		
    		SQLiteDatabase db = this.getReadableDatabase();
    		String sql = "select * from SystemSetTable where  qualitydate='"
    			+ Double.toString(Nowdate) + "'";
    		try {
    			cursor = db.rawQuery(sql, null);
    			while(cursor.moveToNext())
    			{	    
    			    if(Nowdate==cursor.getDouble(cursor.getColumnIndex("qualitydate")))
    			    {
    			    	num=cursor.getInt(cursor.getColumnIndex("qualitynum"))+1;
    			    	break;
    			    }
    			}
    			exFlag=true;
    			cursor.close();
    			db.close();
    			MidData.isSql = false;
    			break;
    		} catch (Exception e) {
    			// TODO: handle exception
    			if(cursor!=null)
    				cursor.close();
    			if(db!=null)
    				db.close();
    			MidData.isSql = false;
    		}
    		finally{
    			if(cursor!=null)
    				cursor.close();
    			if(db!=null)
    				db.close();
    			MidData.isSql = false;
    		}
    		
    	    }
    	    else
    	    {
    			try {
    				Thread.sleep(100);
    			} catch (InterruptedException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    	    }
    	    time++;
    	}
    	return num==99999?9991:num;
    }
    public boolean XueChenTestFinished(String num)
    {
	Cursor cursor = null;
	Time t = new Time();
	t.setToNow();
	double Nowdate=t.year * Math.pow(10, 4) + (t.month + 1)* Math.pow(10, 2) + t.monthDay;
	boolean flag=false;
	boolean exFlag=false;
	//double n = Double.parseDouble(num);
	int time=0;
	while (true)
	{
		if(time>20)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select * from DS1000ResourceTable where  xuhao='"
			+ num + "' and isfinishxuechentest=1 ";
		try {
			cursor = db.rawQuery(sql, null);
			while(cursor.moveToNext())
			{	    
			    if(Nowdate==cursor.getDouble(cursor.getColumnIndex("date")))
			    {
			    	flag=true;
			    	break;
			    }
			}
			exFlag=true;
			cursor.close();
			db.close();
			MidData.isSql = false;
			break;
		} catch (Exception e) {
			// TODO: handle exception
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		
	    }
	    else
	    {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    time++;
	}
	
	    return flag;
    }

    public boolean YaJiTestFinished(String num)
    {
	Cursor cursor = null;
	Time t = new Time();
	t.setToNow();
	Double Nowdate = t.year * Math.pow(10, 4) + (t.month + 1)* Math.pow(10, 2) + t.monthDay;
	boolean flag=false;
	boolean exFlag=false;
	int time=0;
	while (true)
	{
		if(time>100)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select * from DS1000ResourceTable where  xuhao='"
			+ num + "' and isfinishyajitest=1 ";
		try {
			cursor = db.rawQuery(sql, null);
			while(cursor.moveToNext())
			{
			    if(Nowdate==cursor.getDouble(cursor.getColumnIndex("date")))
			    {
				flag=true;
				break;
			    }
			}
			exFlag=true;
			cursor.close();
			db.close();
			MidData.isSql = false;
			break;
		} catch (Exception e) {
			// TODO: handle exception
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    time++;
	}
	    return flag;

    }
    public boolean TodayTestedXueChen()
    {
	Cursor cursor = null;
	Time t = new Time();
	t.setToNow();
	Double Nowdate = t.year * Math.pow(10, 4) + (t.month + 1)* Math.pow(10, 2) + t.monthDay;
	boolean flag=false;
	boolean exFlag=false;
	int time=0;
	//System.out.println(MidData.isSql+"---sql1111111111");
	while (true)
	{
		if(time>10)
			break;
		//System.out.println(MidData.isSql+"---sql11112222");
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		t.setToNow();
		String date = Double.toString(t.year * Math.pow(10, 4) + (t.month + 1)
			* Math.pow(10, 2) + t.monthDay);
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select * from DS1000ResourceTable where  date="
			+ date+ " and isfinishxuechentest=1 ";
		try {
			cursor = db.rawQuery(sql, null);
			while(cursor.moveToNext())
			{
			    if(Nowdate==cursor.getDouble(cursor.getColumnIndex("date")))
			    {
				flag= true;
				break;
			    }
			    
			}	
			exFlag=true;
			cursor.close();
			db.close();
			MidData.exSqlOk=true;
			MidData.isSql = false;
			break;
		} catch (Exception e) {
			// TODO: handle exception
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
	    }
	    else
	    {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    time++;
	}
	return flag;

    }
    public boolean TodayTestedYaJi()
    {
	Cursor cursor = null;
	Time t = new Time();
	t.setToNow();
	Double Nowdate = t.year * Math.pow(10, 4) + (t.month + 1)* Math.pow(10, 2) + t.monthDay;
	boolean flag=false;
	boolean exFlag=false;
	int time=0;
	while (true)
	{
		if(time>10)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		t.setToNow();
		String date = Double.toString(t.year * Math.pow(10, 4) + (t.month + 1)
			* Math.pow(10, 2) + t.monthDay);
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select * from DS1000ResourceTable where  date="
			+ date+ " and isfinishyajitest=1 ";
		try {
			cursor = db.rawQuery(sql, null);
			while(cursor.moveToNext())
			{
			    if(Nowdate==cursor.getDouble(cursor.getColumnIndex("date")))
			    {
				flag= true;
				break;
			    }
			}
			exFlag=true;
			cursor.close();
			db.close();
			MidData.exSqlOk=true;
			MidData.isSql = false;
			break;
		} catch (Exception e) {
			// TODO: handle exception
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		
	    }
	    else
	    {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    time++;
	}
	
	return flag;

    }
    public Cursor selectResourceDataByID(Cursor cursor,String id)
    {
	//Cursor cursor;
    	int time=0;
	while (true)
	{
		if(time>20)
			break;
		System.out.println(MidData.isSql+"----ssssqqqqselect");
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;

		SQLiteDatabase db = this.getReadableDatabase();
		try {
			cursor = db.query("DS1000ResourceTable", new String[]
					{ "date", "xuhao", "resourcedata", "xuechendata", "yajidata",
						"xuechentesttimetype", "plusperiod", "isdebug","temp" },
						"id=?", new String[]
						{ id }, null, null, null);

					MidData.isSql = false;
					break;
		} catch (Exception e) {
			// TODO: handle exception
			/*if(db!=null)
				db.close();*/
			MidData.isSql = false;
		}
		finally{
			/*if(db!=null)
				db.close();*/
			MidData.isSql = false;
		}
	    }
	    else
	    {
		try
		{
		    Thread.sleep(1000);
		}
		catch (Exception e)
		{
		    // TODO: handle exception
		}
	    }
	    time++;
	}
	return cursor;
    }

    public Cursor selectResourceDataByIDs(Cursor cursor,String[] ids)
    {
	//Cursor cursor;
    	int time=0;
	while (true)
	{
		if(time>10)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;

		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select * from DS1000ResourceTable where id in(";
		int len = ids.length;
		for (int i = 0; i < len; i++)
		{
		    if (i < len - 1)
			sql += ids[i] + ",";
		    else
			sql += ids[i] + ")";
		}
		try {
			cursor = db.rawQuery(sql, null);
			MidData.isSql = false;
			break;
		} catch (Exception e) {
			// TODO: handle exception
			/*if(db!=null)
				db.close();*/
			MidData.isSql = false;
		}
		finally{
			/*if(db!=null)
				db.close();*/
			MidData.isSql = false;
		}
	    }
	    else
	    {
		try
		{
		    Thread.sleep(100);
		}
		catch (Exception e)
		{
		    // TODO: handle exception
		}
	    }
	    time++;
	}
	return cursor;
    }

    public Cursor selectHistoryByDate(Cursor cursor,String[] getStr, double s, double e)
    {
	//Cursor cursor;
    	int time=0;
	while (true)
	{
		if(time>10)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select * from DS1000ResourceTable where  date>="
			+ s + " and date<=" + e + " order by id asc";
		try {
			cursor = db.rawQuery(sql, null);
			
			//db.close();
			MidData.exSqlOk=true;
			MidData.isSql = false;
			break;
		} catch (Exception e2) {
			// TODO: handle exception
			//if(db!=null)
			//	db.close();
			MidData.isSql = false;
		}
		finally{
			//if(db!=null)
			//	db.close();
			MidData.isSql = false;
		}
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    }
	    time++;
	}
	return cursor;
    }

    public Cursor selectHistoryByNum(Cursor cursor,String[] getStr, String s, String e)
    {
	//Cursor cursor;
    	int num=0;
	while (true)
	{
		if(num>5)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select * from DS1000ResourceTable where xuhao>="
			+ s + " and xuhao<=" + e + " ";
		try {
			cursor = db.rawQuery(sql, null);
			//db.close();
			MidData.isSql = false;
			break;
		} catch (Exception e2) {
			// TODO: handle exception
			MidData.isSql = false;
		}
		finally{
			MidData.isSql = false;
		}
	    }
	    else
	    {
		//Sleep sleep = new Sleep();
				//sleep.start();
	    }
	    num++;
	}
	int m = cursor.getCount();
	return cursor;
    }

    /*public Cursor selectTestResult()
    {
	Cursor cursor;
	while (true)
	{
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "select xuhao,xuechendata,yajidata from DS1000ResourceTable where isfinishxuechentest>="
			+ 1 + " and isfinishyajitest<=" + 1 + " ";
		cursor = db.rawQuery(sql, null);
		MidData.isSql = false;
		break;
	    }
	    else
	    {
		//Sleep sleep = new Sleep();
				//sleep.start();
	    }
	}
	return cursor;
    }*/
    public String[] selectSingleHistoryByNumByK(String num)
    {
    	Time t = new Time();
    	t.setToNow();
    	String date = Double.toString(t.year * Math.pow(10, 4) + (t.month + 1)
    		* Math.pow(10, 2) + t.monthDay);
    	String time = Double.toString(t.hour * Math.pow(10, 2) + t.minute);
	String str []=new String[3];
	int times=0;
	//num="1201020002";
	while (true)
	{
		if(times>100)
			break;
		//System.out.println(MidData.isSql+"--------exSQLFlag--------selectSingleHistoryByNumByK");
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		String sql = "select * from DS1000ResourceTable where xuhao='"
				+ num + "' and date= "+date;
		/*try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		SQLiteDatabase db = this.getReadableDatabase();	
		Cursor cursor =null;
		//System.out.println(sql+"---------sql语句---"+times);
		try {
			
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext())
			{
			    str[0] = cursor.getString(6);
			    str[1] = cursor.getString(7);
			    str[2] = cursor.getString(9);
			}
			//System.out.println(cursor.getString(6)+"---"+cursor.getString(7)+"---222222");
			MidData.isSql = false;
			cursor.close();
			db.close();
			//System.out.println("EXOK----");
			
			break;
		} catch (Exception e) {
			// TODO: handle exception
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
			
			//System.out.println("EXFail----"+times);
		}
		finally{
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
	    }
	    else
	    {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    times++;
	}
	return str;
    }

    public String[] selectHistoryById(String[] getStr, int id, int len)
    {
	String[] value = new String[len];
	for (int i = 0; i < len; i++)
	{
	    value[i] = "";
	}
	int time=0;
	while (true)
	{
		if(time>5)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		Sleep sleep = new Sleep();
		sleep.start();
		Cursor cursor =null;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			cursor = db.query("DS1000ResourceTable", getStr, "id=?",
					new String[]
					{ Integer.toString(id) }, null, null, null);

				while (cursor.moveToNext())
				{
				    for (int i = 0; i < len; i++)
				    {
					value[i] = cursor.getString(cursor
						.getColumnIndex(getStr[i]));
				    }
				}
				cursor.close();
				db.close();
				MidData.isSql = false;
				break;
		} catch (Exception e) {
			// TODO: handle exception
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}		
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    time++;
	}

	return value;
    }
    public String[] selectHistoryByNumAndTime(String[] getStr, int num, int len)
    {
    	Time t = new Time();
    	t.setToNow();
    	String date = Double.toString(t.year * Math.pow(10, 4) + (t.month + 1)
    		* Math.pow(10, 2) + t.monthDay);
	String[] value = new String[len];
	for (int i = 0; i < len; i++)
	{
	    value[i] = "";
	}
	int time=0;
	while (true)
	{
		if(time>5)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		Sleep sleep = new Sleep();
		sleep.start();
		Cursor cursor =null;
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			cursor = db.query("DS1000ResourceTable", getStr, "xuhao = ? and date = ?",
					new String[]
					{ Integer.toString(num),date }, null, null, null);

				while (cursor.moveToNext())
				{
				    for (int i = 0; i < len; i++)
				    {
					value[i] = cursor.getString(cursor
						.getColumnIndex(getStr[i]));
				    }
				}
				cursor.close();
				db.close();
				MidData.isSql = false;
				break;
		} catch (Exception e) {
			// TODO: handle exception
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(cursor!=null)
				cursor.close();
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}		
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    time++;
	}

	return value;
    }
    public boolean DeleteSelectedHistory(String type, double start, double end)
    {
	boolean flag = false;
	int time = 0;
	System.out.println("-----Deletedata-----DeleteSelectedHistory----");
	while (true)
	{
		if (time > 5)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "delete from DS1000ResourceTable where type ='"
			+ type + "' and time >=" + start + " and time<=" + end
			+ " ";
		SQLiteDatabase dbd = this.getWritableDatabase();
		dbd.execSQL(sql);
		dbd.close();
		MidData.isSql = false;
		flag = true;
		break;
	    }
	    else
	    {
		//Sleep sleep = new Sleep();
				//sleep.start();
		
	    }
	    time++;
	    
	}
	return flag;
    }

    public boolean DeleteServerCacheData(int id)
    {
	boolean flag = false;
	int time = 0;
	while (true)
	{
		if (time > 5)
			break;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		String sql = "delete from UpdateSetedDataToServerCache where id ="
			+ id + "  ";
		SQLiteDatabase dbd = this.getWritableDatabase();
		try {
			dbd.execSQL(sql);
			dbd.close();
			flag = true;
		} catch (Exception e) {
			// TODO: handle exception
			if(dbd!=null)
				dbd.close();
		}
		finally{
			if(dbd!=null)
				dbd.close();
		}
		if(flag)
		{
			MidData.isSql = false;
		
			break;
		}
		
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	    }
	    time++;
	    
	}
	return flag;
    }

    // 删除操作
    public boolean deleteSourceByDate(String date)
    {
	int time = 0;
	boolean flag = false;
	while (true)
	{
		if (time >5)
	    {
		break;
	    }
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		// Sleep sleep=new Sleep();
		// sleep.start();
		SQLiteDatabase db = this.getWritableDatabase();
		String where = "date < ?";
		String[] whereValue =
		{ date };
		try {
			db.delete("DS1000ResourceTable", where, whereValue);
			db.close();
			flag = true;
			MidData.isSql = false;
			break;
		} catch (Exception e) {
			// TODO: handle exception
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	    }
	    time++;
	    
	}
	return flag;
    }
    public boolean delete(int id)
    {
	int time = 0;
	boolean flag = false;
	while (true)
	{
		if (time >5)
	    {
		break;
	    }
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		// Sleep sleep=new Sleep();
		// sleep.start();
		SQLiteDatabase db = this.getWritableDatabase();
		String where = id + " = ?";
		System.out.println("-----Deletedata-----delete");
		String[] whereValue =
		{ Integer.toString(id) };
		try {
			db.delete("HistoryTable", where, whereValue);
			db.close();
			flag = true;
			MidData.isSql = false;
			break;
		} catch (Exception e) {
			// TODO: handle exception
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		finally{
			if(db!=null)
				db.close();
			MidData.isSql = false;
		}
		
	    }
	    else
	    {
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	    }
	    time++;
	    
	}
	return flag;
    }

    // 修改操作
    public boolean update(int id, String TABLE_NAME, String updataRow,
	    String condtion)
    {
	boolean flag = false;
	int times = 0;

	while (true)
	{
	    times++;
	    if (!MidData.isSql)
	    {
		MidData.isSql = true;
		//Sleep sleep = new Sleep();
				//sleep.start();
		SQLiteDatabase db = this.getWritableDatabase();
		String where = " id = ?";
		String[] whereValue =
		{ Integer.toString(id) };
		ContentValues cv = new ContentValues();
		cv.put(updataRow, condtion);
		try {
			db.update(TABLE_NAME, cv, where, whereValue);
		db.close();
		flag = true;
		MidData.isSql = false;
		break;
		} catch (Exception e) {
			// TODO: handle exception
			MidData.isSql = false;
		}
		finally{
			MidData.isSql = false;
		}
	    }
	    else
	    {
		//Sleep sleep = new Sleep();
				//sleep.start();
	    }
	    if (times >= 3)
	    {
		flag = false;
		break;
	    }
	}

	return flag;
    }

    // 修改操作
    class Sleep extends Thread
    {
	@Override
	public void run()
	{
	    // TODO Auto-generated method stub
	    super.run();
	    try
	    {
		sleep(1000);
	    }
	    catch (InterruptedException e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

    
}
