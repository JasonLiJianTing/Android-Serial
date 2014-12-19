package com.friendlyarm.AndroidSDK;

import android.util.Log;

public class HardwareControler
{
	/* Serial Port */
	static public native int openSerialPort(String devName, long baud,
			int dataBits, int stopBits);

	/* I/O */
	static public native int write(int fd, byte[] data);

	static public native int read(int fd, byte[] buf, int len);

	static public native int select(int fd, int sec, int usec);

	static public native void close(int fd);

	static
	{
		try
		{
			System.loadLibrary("friendlyarm-hardware");
		} catch (UnsatisfiedLinkError e)
		{
			Log.d("HardwareControler",
					"libfriendlyarm-hardware library not found!");
		}
	}
}