package com.friendlyarm.Serial;

import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
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
	Handler handler = new Handler();
	Handler sendHandler = new Handler();

	Runnable runnable = new Runnable()
	{
		@Override
		public void run()
		{
			ReadSerial();
			// handler.postDelayed(this, 3000);
			handler.post(this);
		}
	};

	Runnable sendrunnable = new Runnable()
	{
		@Override
		public void run()
		{
			SendSerial();
			// handler.postDelayed(this, 3000);
			handler.post(this);
		}
	};

	/**
	 * 接收函数
	 */
	public void ReadSerial()
	{
		// data1.setText(null);
		// System.out.println(fd);
		int m = HardwareControler.select(fd, 2, 20);
		// System.out.println("a");
		// System.out.println(m);
		if (m == 1)
		{
			byte[] buf = new byte[10];
			try
			{
				Thread.sleep(90);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int n = HardwareControler.read(fd, buf, buf.length);
			// System.out.println("b");
			// System.out.println(n);
			System.out.println(Arrays.toString(buf));
			data1.setText(data1.getText() + "  " + Arrays.toString(buf));
		}
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
		handler.post(runnable);
		sendHandler.post(sendrunnable);
		// System.out.println("启动串口线程");
		fdText.setText(fdText.getText() + "打开线程");

		closeSerial.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				handler.removeCallbacks(runnable);
				HardwareControler.close(fd);
				fdText.setText("关闭串口");
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
	}

	/**
	 * 发送函数，直接调用友善之臂提供的函数接口 我这里将(EditText)senddata中的内容变字符串再变bytes[] 接收到的结果有点不对
	 */
	public void SendSerial()
	{
		HardwareControler.write(fd, senddata.getText().toString().getBytes());
	}
}
