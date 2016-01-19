package realarm.hardware;

import java.io.FileDescriptor;

public class HardwareControl {
	
	public native int LedSetState(int ledNum);
	public native static FileDescriptor OpenSerialPort(String path, int baudrate,
			int flags);
	public native static void CloseSerialPort();
	
	public native static  void InitCan(int baudrate);
	public native static  int OpenCan();
	public native static  int CanWrite(int canId,String data);
	public native static  CanFrame CanRead(CanFrame mcanFrame, int time);
	public native static   void CloseCan();
	static {
		System.loadLibrary("RealarmHardwareJni");
	}

}

