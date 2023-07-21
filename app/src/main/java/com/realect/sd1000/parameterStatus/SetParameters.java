package com.realect.sd1000.parameterStatus;

public class SetParameters {
public static int TestType;//测试类型  0血沉  1压积
public static int ERSTestTime;//血沉测试时间 0:60分钟  1:30分钟
public static int DataTransferSet;//数据传输设置   1:上传至SA  2:上传至LIS 

public static int PrintSet;//打印设置  1:有沉降曲线   0: 无沉降曲线
public static int AutoPrintSet;//1血沉完成打印  2压积完成打印


public static int IsAutoTemResive;//温度修正  1修正  0不修正
public static float TemperatureRevise;//温度修正

public static int InitialNum;//默认起始编号

public static int IsCurveFit = 0;//高度修正 0不选   1选中

public static float ResetHeightRevise;//复位高度修正
public static float ERSTestIniValue;//血沉测试初始值  57±
public static int  VoltageReferenceSet;//电压参考值设置
public static int  VolageCompareSet;//电压比较值设置

public static float ERSTestResultRevise;//血沉测试结果修正
public static float HematResultRevise;//压积测试结果修正

public static float AnticoagulantsHeight;

public static int FanOpenClose;//风扇开关状态
public static int  RunPeriodSet;//运行周期设置
public static long lastTargetNumERS;//血沉自动编号最大号
public static long lastTargetNumHemat;//压积自动编号最大号
public static int lastNumSetWay;//编号设置方法
public static int SetNumWay;//编号设置时间  0测试前编号  1测试完成后编号



public static int ERSCount;//血沉试管剩余量
public static int hematCount;//压积试管剩余量
public static String LastCreditCard_Time;//上次充值时间
}
