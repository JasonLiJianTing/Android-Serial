package com.friendlyarm.Serial;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.friendlyarm.AndroidSDK.HardwareControler;

public class Serial extends Activity
{
	private EditText send_et;
	private TextView rev_tv;
	private Button openSerial;
	private Button closeSerial;
	private Button sendSerial;
	private int fd;
	byte[] buf;
	private Handler revHandler = new Handler()
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg)
		{
			rev_tv.setText(rev_tv.getText() + "  " + Arrays.toString(buf));

			super.handleMessage(msg);
		}

	};

	/**
	 * 发送函数，直接调用友善之臂提供的函数接口 我这里将(EditText)senddata中的内容变字符串再变bytes[] 接收到的结果有点不对
	 */
	public void SendSerial()
	{
		HardwareControler.write(fd, send_et.getText().toString().getBytes());
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rev_tv = (TextView) findViewById(R.id.rev_tv);
		send_et = (EditText) findViewById(R.id.send_et);
		openSerial = (Button) findViewById(R.id.openSerial);
		closeSerial = (Button) findViewById(R.id.closeSerial);
		sendSerial = (Button) findViewById(R.id.sendSerial);
		/**
		 * 软件运行就直接打开串口
		 */
		// TODO Auto-generated method stub
		fd = HardwareControler.openSerialPort("/dev/s3c2410_serial3", 115200,
				8, 1);

		/**
		 * 关闭串口
		 */
		closeSerial.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				HardwareControler.close(fd);
			}
		});

		/**
		 * 发送按钮
		 */
		sendSerial.setOnClickListener(new Button.OnClickListener()
		{
			public void onClick(View v)
			{
				SendSerial();
			}
		});

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					int m = HardwareControler.select(fd, 2, 20);
					if (m == 1)
					{
						buf = new byte[10];
						try
						{
							Thread.sleep(90);
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						int n = HardwareControler.read(fd, buf, buf.length);
						System.out.println(Arrays.toString(buf));
						revHandler.sendEmptyMessage(0x55);
					}
				}
			}
		}).start();
	}
}
