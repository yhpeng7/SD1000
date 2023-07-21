package KyleSocket_api;
public class KyleSocketClass
{
	public static native int GetInfo(int fd);// 返回是否连接到网络
	public static native int InitPort(String IP, int Port);// 连接到指定端口
	public static native int DeInitPort(int fd);// 反连接到指定端口
	public static native int SendData(byte[] buf, int DataLength, int fd);// 发送数据
																			// 返回发送成功的数据长度
	public static native int ReadData(byte[] buf, int fd);// 读取数据 返回发送成功的数据长度
	static {
		System.loadLibrary("KyleSocket");// 注意这里为自己指定的.so文件，无lib前缀，亦无后缀
	}
}
