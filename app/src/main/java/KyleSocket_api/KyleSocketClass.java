package KyleSocket_api;
public class KyleSocketClass
{
	public static native int GetInfo(int fd);// �����Ƿ����ӵ�����
	public static native int InitPort(String IP, int Port);// ���ӵ�ָ���˿�
	public static native int DeInitPort(int fd);// �����ӵ�ָ���˿�
	public static native int SendData(byte[] buf, int DataLength, int fd);// ��������
																			// ���ط��ͳɹ������ݳ���
	public static native int ReadData(byte[] buf, int fd);// ��ȡ���� ���ط��ͳɹ������ݳ���
	static {
		System.loadLibrary("KyleSocket");// ע������Ϊ�Լ�ָ����.so�ļ�����libǰ׺�����޺�׺
	}
}
