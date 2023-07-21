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
	
	//public static  String aisleSampleGetNumTime="0";//����ɨ���ţ��ֶ������ţ����������ͨ������Ƿ����
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
	public static int LaveTestTime;//���Ե���ʱ
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
    public static String[] ErrorInfor = { "103", "104", "105", "107" };// "103-����λ�������","104-����λ�������","105-�������","107-��λ�������"
	
	public static InputStream m_InputStream_LowerMachine = null;//��λ������������
	public static OutputStream m_OutputStream_LowerMachine = null;//��λ�����������
	public static boolean m_isInterrupted_LowerMachine = false;//��λ������״̬
	public static String StatusStrShow = "";//�豸��ǰ״̬   �����Լ���Լ����
	public static String TemperatureErrorShow = "";//�������¶ȹ�����Ϣ
	public static boolean IsESRSelect[] = new boolean[100];
	//public static boolean RunAllTime = false;//�Ƿ񿽻�ģʽ
	public static String  TempRes="";
	
	public static boolean isRecivedIC = false;
	public static boolean isRecivedSA = false;
	
    //public static int m_driRunTime = 0;//����ģʽ��1�Σ�60�Σ�120��
    public static boolean m_UpToSA=false;
    public static boolean m_UpToLIS_ser=false;
    public static boolean m_UpToLIS_net=false;
    //public static boolean m_UpToSAAuto=false;//
  
    public static boolean m_PrintAfterERS;//Ѫ����ɺ��ӡ
    public static boolean m_PrintAfterHemat;//ѹ����ɺ��ӡ
    
    
	
	public static double MaxHeight = 70;// 61
	public static double MinHeight = 20;// 53
	public static double DirveRunTestTime=0;
    public static int isDebugging = 0;//0������ ��1������ 2������
    //public static String[] Difference_First_Last = new String[100];
    public static String XueChenTestRealTime;//Ѫ������ʵ�ʴ���
	//public static int lastTargetNum = 0;
	public static String ErrorStrShow = "��������";//�豸������Ϣ
	public static int MaxRPMOfDrivce = 0;
	public static boolean TestFinished = false;
	public static boolean m_isRecived = false;//��λ������״̬
	public static byte[] m_systemVision = new byte[32];//ϵͳ�汾��
	public static byte[] m_systemError = new byte[16];//ϵͳ����
	public static byte[] m_pulusePeriod = new byte[64];//��������
	public static byte[] m_statusSelfTestAisle = new byte[256];//ͨ���Լ�
	public static byte[] m_temperatureData = new byte[512];//��������¶�
	public static boolean isTop = false;
    public static boolean isBelow = false;

    public static String m_upDown = "";
    public static int m_pulsePeriodSetLast = 0;//������������
    public static int m_voltageCompareSetLast = 800;//��ѹ�Ƚ�ֵ����
    public static int m_minTestSetLast = 800;//��ѹ�Ƚ�ֵ����
    public static int m_voltageReferenceSetLast = 1750;//��ѹ�ο�ֵ����
    public static int m_resultInt=1;
    public static double m_minTest=10.5;
    public static byte[] m_heightAisle = new byte[512];//��ͨ���߶�
    public static byte[] m_statusAisle = new byte[512];//��ͨ��״̬�����Դ���
    public static byte[] m_debug = new byte[512];
    public static String[] m_debugStatus = new String[100];//����״̬
    public static boolean m_isUpdateDebug = false;
    public static byte[] m_recompenseAisle = new byte[512];
    //public static float[] m_recompenseHeight = new float[100];// ����ֵ
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
    public static double[] CurveXP = new double[7];//�������
    public static int[] AddressBitmap = new int[200];
    public static byte[] ICCardSerialPortData = new byte[128];
    //public static boolean m_isInterrupted_ICCreditCard = false;
    public static InputStream m_InputStream_SA = null;
    
    public static OutputStream m_OutputStream_SA = null;
    public static boolean[] IsConnectPort = new boolean[10];
    
    public static InputStream m_InputStream_Scanner = null;
    public static boolean m_isInterrupted_Scanner = false;
    public static OutputStream m_OutputStream_Scanner=null;
    public static byte[] m_scannerData = new byte[512];// ��ʱ���� ɨ�����Ľ��
    public static int ScannerNum = 0;// ��ʱ���� ɨ�����Ľ���ĳ���
    public static boolean ScannerNumFinished = true;
    public static String ScanningNumNowStr = "";// ɨ�����Ľ��
    public static int SelAddress = 101;// ��ǰѡ�е�λ��
    public static TestParameters[] mTestParameters=new TestParameters[100];
    public static SetParameters mSetParameters;
    
    public static int IsNeedReTest;//�Ƿ���Ҫ���¼��  1��Ҫ���¼��
    public static int IsNeedUserConfirm;//�Ƿ�ȷ���Ƿ��ؼ죨������������м�¼��  1��Ҫ�ؼ�
    public static String IsNeedReTestMsg="";//���¼����ʾ��Ϣ
    //public static boolean isReviseRunSpeed;//���������ٶ�
    
    public static boolean AddressSampleNumRefreshed;//�������ˢ������ɨ��ǹ
    public static double ERSRealTestTime=0;//Ѫ�����Դ���
    public static double HematRealTestTime=2;//ѹ�����Դ���
    
    
    public static int DriRunTime;//����ģʽ��1�Σ�60�Σ�120��  5��  ������
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
    public static int IsChinese=0;//0Ӣ��  1���� 
    public static boolean IsConsumerReduce=false;//�Ĳ��Ƿ�ݼ�
    public static boolean NoWarnningChargeForHematConsume=true;
    public static boolean NoWarnningChargeForERSConsume=true;
    public static boolean LisUploadLine=false;//lis�Ƿ��ϴ�����
    public static int LisBrt;//Lis���ڲ�����    
}
