package com.realect.sd1000.parameterStatus;

public class TestParameters {
public boolean savedToDatabase;//是否存入数据库
public  String aisleNum;//通道编号	
public  long aisleSampleNumTime=0;//用于扫码编号，手动输入编号，检测无样本通道编号是否清除
public long saveDatabaseID;//保存到数据库中的id
public  String aisleNumShow;//通道编号	
public  String sampleAddress;//样本位置
public  int ERSTestCount;//血沉总测试次数
//public  double ERSTestResult;//血沉测试结果
public  int hematTestCount;//压积总测试次数
public  int aisleStatus;//记录通道是否可用，1表示可用，-1表示不可用，0表示未检测（自检）
public  int aisleTestType;//通道测试类型   1血沉测试，2压积测试
public  int testStatus;//通道当前测试状态   0无样本，1发现，2正在测试，3测试完成,4样本超限，5样本不足 6 耗材用完不再测试
public  boolean warningNoConsume; 
public  boolean showEndOfNotSumTime;
public  double[] aisleHeght=new double[200];//测试的高度
public  int  aisleTestTime;//通道当前测试的次数
public  String areaTemp;//测试区温度
public  boolean aisleDataSaved;//通道数据是否保存
public  float aisleRecompenseHeight;//通道高度修正
public  String aislePastTestData;
public  boolean aisleTestFinished;//通道检测完成
public  String ERSResultRes;//血沉测试结果原始差值（用于存入数据库）
public  String ERSResult;//血沉测试结果
public  String hematResult;//压积测试结果
public  boolean IsNewSample;//是否是新样本
public  boolean ReDetect;//是否重检
public  boolean RealReDetect;//是否重检
public  float height_RunAllTime;//拷机原始高度
public  double aisleTestTimeCount;// 通道测试的总次数
public  int hematExcetion;
public  float hematTestHeight;
public  double  averageTem=(double) 0.00;

}
