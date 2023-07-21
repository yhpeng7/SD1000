package com.realect.sd1000.parameterStatus;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import android.R.bool;
import android.graphics.Canvas;
import android.text.format.Time;
import android_serialport_api.SerialPort;

import com.realect.sd1000.devicePortOperation.ICCreditCardPort;
import com.realect.sd1000.devicePortOperation.LowerMachinePort;
import com.realect.sd1000.devicePortOperation.PrintPort;
import com.realect.sd1000.devicePortOperation.SAPort;
import com.realect.sd1000.devicePortOperation.ScanningPort;
import com.realect.sd1000.devicePortOperation.TestDataConvert;
import com.realect.sd1000.sqlite.*;

public class MidData<ReadThread> {
	public static Queue<String> SqlQueue = new LinkedList<String>(); 
	
	//public static  String aisleSampleGetNumTime="0";//用于扫码编号，手动输入编号，检测无样本通道编号是否清除
	public static boolean startTemTestFinish=false;
	public static boolean startTemTestFinish1=false;
	public static boolean startTemTestOK=false;
	public static boolean startTemTestOK1=false;
	public static String LastFindTime="0";
	public static boolean isFindingSample=false;
	public static boolean isScallingAllFinish=false;
	public static boolean isGetRecomFinish=false;
	public static boolean isRunPeriodFinish=false;
	public static DecimalFormat fnum10ESR = new DecimalFormat("##0000000000");
	public static int fnum10LenESR=0;
	public static DecimalFormat fnum10Hemat = new DecimalFormat("##0000000000");
	public static int fnum10LenHemat=0;
	public static int fd=-1;
	public static Time LisNetConnetTime;
	public static String IpStr="192.168.0.54";
	public static int    Port=9977;
	public static int LaveTestTime;//测试倒计时
	public static boolean isScannerUpdated=true;
	public static int fgCan=0;
	public static ICCreditCardPort sOpForICCreditCard = null;
    public static ScanningPort sOpForScanner;
    public static PrintPort sOpForPrint;
    public static SAPort sOpForSA;
    public static LowerMachinePort sOp;
    public static SerialPort portForScanner = null;
    public static SerialPort portForICCreditCard = null;
    public static SerialPort portForPrint = null;
    public static SerialPort portForSA = null;
    public static SerialPort port = null;
    public static TestDataConvert testDataConvert=null;
	public static boolean isOnTestUp=false;
	public static boolean isOnTestDown=false;
    public static String[] ErrorInfor = { "103", "104", "105", "107" };// "103-上限位光耦故障","104-下限位光耦故障","105-光耦故障","107-限位光耦故障"
	
	public static InputStream m_InputStream_LowerMachine = null;//下位机串口输入流
	public static OutputStream m_OutputStream_LowerMachine = null;//下位机串口输出流
	public static boolean m_isInterrupted_LowerMachine = false;//下位机连接状态
	public static String StatusStrShow = "";//设备当前状态   正在自检或自检完成
	public static String TemperatureErrorShow = "";//测试区温度故障信息
	public static boolean IsESRSelect[] = new boolean[100];
	//public static boolean RunAllTime = false;//是否拷机模式
	public static String  TempRes="";
	
	public static boolean isRecivedIC = false;
	public static boolean isRecivedSA = false;
	
    //public static int m_driRunTime = 0;//运行模式，1次，60次，120次
    public static boolean m_UpToSA=false;
    public static boolean m_UpToLIS_ser=false;
    public static boolean m_UpToLIS_net=false;
    //public static boolean m_UpToSAAuto=false;//
  
    public static boolean m_PrintAfterERS;//血沉完成后打印
    public static boolean m_PrintAfterHemat;//压积完成后打印
    
    
	
	public static double MaxHeight = 70;// 61
	public static double MinHeight = 20;// 53
	public static double DirveRunTestTime=0;
    public static int isDebugging = 0;//0：测试 ，1：调试 2：拷机
    //public static String[] Difference_First_Last = new String[100];
    public static String XueChenTestRealTime;//血沉测试实际次数
	//public static int lastTargetNum = 0;
	public static String ErrorStrShow = "仪器正常";//设备故障信息
	public static int MaxRPMOfDrivce = 0;
	public static boolean TestFinished = false;
	public static boolean m_isRecived = false;//下位机接收状态
	public static byte[] m_systemVision = new byte[32];//系统版本号
	public static byte[] m_systemError = new byte[16];//系统故障
	public static byte[] m_pulusePeriod = new byte[64];//运行周期
	public static byte[] m_statusSelfTestAisle = new byte[256];//通道自检
	public static byte[] m_temperatureData = new byte[512];//检测区域温度
	public static boolean isTop = false;
    public static boolean isBelow = false;

    public static String m_upDown = "";
    public static int m_pulsePeriodSetLast = 0;//脉冲周期设置
    public static int m_voltageCompareSetLast = 800;//电压比较值设置
    public static int m_minTestSetLast = 800;//电压比较值设置
    public static int m_voltageReferenceSetLast = 1750;//电压参考值设置
    public static int m_resultInt=1;
    public static double m_minTest=10.5;
    public static byte[] m_heightAisle = new byte[512];//各通道高度
    public static byte[] m_statusAisle = new byte[512];//各通道状态及测试次数
    public static byte[] m_debug = new byte[512];
    public static String[] m_debugStatus = new String[100];//调试状态
    public static boolean m_isUpdateDebug = false;
    public static byte[] m_recompenseAisle = new byte[512];
    //public static float[] m_recompenseHeight = new float[100];// 修正值
    public static boolean m_isUpdateRecompense = false;
    public static boolean isSql = false;
    public static SQLite sqlOp;
    public static boolean exSqlOk=false;
    
    public static String LastCreditCard_Time;
    public static boolean m_isInterrupted_ICCreditCard = false;
    public static InputStream m_InputStream_ICCreditCard = null;
    public static OutputStream m_OutputStream_ICCreditCard = null;
    //public static byte[] ICCardSerialPort = new byte[128];
    //public static int IsCurveFit = 0;
    //public static String IsAutoTemResive = null;
    public static double[] Temperature = new double[5];
    public static double TemRevise = 0;
    public static OutputStream m_OutputStream_Print = null;
    public static InputStream m_InputStream_Print=null;
    public static boolean m_isInterrupted_Print=false;
    public static double[] CurveXP = new double[7];//曲线拟合
    public static int[] AddressBitmap = new int[200];
    public static byte[] ICCardSerialPortData = new byte[128];
    //public static boolean m_isInterrupted_ICCreditCard = false;
    public static InputStream m_InputStream_SA = null;
    
    public static OutputStream m_OutputStream_SA = null;
    public static boolean[] IsConnectPort = new boolean[10];
    
    public static InputStream m_InputStream_Scanner = null;
    public static boolean m_isInterrupted_Scanner = false;
    public static OutputStream m_OutputStream_Scanner=null;
    public static byte[] m_scannerData = new byte[512];// 暂时保存 扫码器的结果
    public static int ScannerNum = 0;// 暂时保存 扫码器的结果的长度
    public static boolean ScannerNumFinished = true;
    public static String ScanningNumNowStr = "";// 扫码器的结果
    public static int SelAddress = 101;// 当前选中的位置
    public static TestParameters[] mTestParameters=new TestParameters[100];
    public static SetParameters mSetParameters;
    
    public static int IsNeedReTest;//是否需要重新检测  1需要重新检测
    public static int IsNeedUserConfirm;//是否确认是否重检（此样本编号已有记录）  1需要重检
    public static String IsNeedReTestMsg="";//重新检测提示消息
    //public static boolean isReviseRunSpeed;//修正运行速度
    
    public static boolean AddressSampleNumRefreshed;//样本编号刷新来自扫描枪
    public static double ERSRealTestTime=0;//血沉测试次数
    public static double HematRealTestTime=2;//压积测试次数
    
    
    public static int DriRunTime;//运行模式，1次，60次，120次  5次  无数次
    public static boolean isOnMain;
    public static String isPrintDateStr;
    public static String isPrintTimeStr;
    public static String isPrintNumStr;
    public static String isPrintXueChenValueStr;
    public static String isPrintYaJiValueStr;
    public static float[] LastArea=new float[4];
    public static float[] NewArea=new float[4];
    
    public static String[] Recompense=new String[100];
    public static String Product_ID = "";
    public static boolean IsReviseRunPeriod=false;
    public static int IsChinese=0;//0英语  1中文 
    public static boolean IsConsumerReduce=false;//耗材是否递减
    public static boolean NoWarnningChargeForHematConsume=true;
    public static boolean NoWarnningChargeForERSConsume=true;
    public static boolean LisUploadLine=false;//lis是否上传曲线
    public static int LisBrt;//Lis串口波特率    
}
