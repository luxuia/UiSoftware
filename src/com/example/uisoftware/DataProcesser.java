package com.example.uisoftware;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class DataProcesser extends Thread{

	private final String TAG = "Processer";
	
	
	
	private final Handler mHandler;
	private InputStream mInputStream;
	private OutputStream mOutputStream;
	private ArrayList<byte[]> mDataBytes;
	private Context mContext;
	
	
	public DataProcesser(Context context, BluetoothSocket socket, Handler handler) {
		// TODO Auto-generated constructor stub
		mHandler = handler;
		mContext = context;
		try {
			mInputStream = new BufferedInputStream( socket.getInputStream() );
			mOutputStream = socket.getOutputStream();
		} 
		catch (IOException ex) {
			Log.e(TAG, "Stream get error");
		}
	}

	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		//super.run();
		
		byte[] TransHead = "pclo_".getBytes();
		byte[] buffer = new byte[20];
		
		byte[] e1 = {(byte) 0xe1};
			
			while (true) {
				
				try {
					sendMessage(e1);
				} catch (IOException e) {
					Log.e(TAG, "send err");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			Log.e(TAG, "Success to Send pclo_");
				
				/*
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					Log.e(TAG, "sleep error");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				Log.e(TAG, "Reading msg1");
				try {
					mInputStream.read(buffer);
				} catch (IOException e) {
					Log.e(TAG, "read err");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String outputString = Integer.toBinaryString(buffer[0]&0xFF);
	
				
				if (0xE0 == buffer[0]) {
					Message msg = mHandler.obtainMessage(ConnectView.MESSAGE_TOAST);
					Bundle bundle = new Bundle();
					bundle.putString(ConnectView.TOAST, "UserData length err");
					msg.setData(bundle);
					mHandler.sendMessage(msg);
				}
				
				
			
				Log.e(TAG, "read " + outputString);
				if (0x03 == buffer[0]) {
					
					Message msg = mHandler.obtainMessage(ConnectView.MESSAGE_TOAST);
					Bundle bundle = new Bundle();
					bundle.putString(ConnectView.TOAST, "没有数据");
					msg.setData(bundle);
					mHandler.sendMessage(msg);
					
					break;
				}
			}
			// 48 + 33 + 1
			
			Project project = new Project();
			
			while (true) {
				try {
					if (mInputStream.read(buffer) <=0 ) {
						SaveData(project);
						break;
					}
				} catch (IOException e) {
					Log.e(TAG, "read err");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				translateData(project, buffer);
				mDataBytes.add(buffer);
			}
			
			//Toast.makeText(mContext, "成功导入数据", Toast.LENGTH_SHORT);
			
		
		Message msg = mHandler.obtainMessage(ConnectView.MESSAGE_TOAST);
		Bundle bundle = new Bundle();
		bundle.putString(ConnectView.TOAST, "文件传输完成");
		msg.setData(bundle);
		mHandler.sendMessage(msg);
			
	}

	
	public void translateData(Project project, byte[] data) {
		//10|11|7|4|5|5|6
		//21|7|4|5|5|6 year
		//28|4|5 month
		//32|5|5|6 day
		//37|5|6 hour
		//42|6 minute
		
		
		
		int year = (data[3]&0x07)<<4 + (data[2]&0xf0)>>4;
		int month = data[2]&0x0f;
		int day = (data[5]&0xf8)>>3;
		int hour = (data[5]&0x07)<<2 + (data[4]&0xc0)>>6;
		int minute = data[4]&0x3f;
		
		/*
		int year = data[3]>>5 + (data[4]&0x0f) << 3;
		int month = (data[4]&0xf0) >> 4;
		int day = data[5]&0x1f;
		int hour = (data[5]&0xe0)>>5 + (data[6]&0x03) << 3;
		int minute = (data[6]&0xfc)>>2;
		*/
		project.time = new Timestamp(year-1900, month-1, day, hour, minute, 0, 0).toString();
		
		//10|11| effective_type
		/*
		int available = (data[2]&0xfc)>>2 + (data[3]&0x1f)<<6;
		for (int i = 0; i < 11; i++) {
			if (((available>>i)&1) == 1) {
				available = i;
				break;
			}
		}
		*/
		
		int available = (data[0]&0x3f)<<5 + (data[3]&0xf8)>>3;
		for (int i = 0; i < 11; i++) {
			if (((available>>i)&1) == 1) {
				available = i;
				break;
			}
		}
		/*
		if (available == 0) {
			project.values[available] = data[7]&0x07;    // 48|3|
		} else if (available == 1) {
			project.values[available] = (data[7]&0x38)>>3; // 51|3
		} else if (available == 2) {
			project.values[available] = (data[7]&0xc0)>>6 + (data[8]&0x01)<<2; //54|3
		} else if (available == 3) {
			project.values[available] = (data[8]&0x0e)>>1; //57|3
		} else if (available == 4) {
			project.values[available] = (data[8]&0x70)>>4; //60|3 
		} else if (available == 5) {
			project.values[available] = (data[8]&0x80)>>7 + (data[9]&0x03)<<1; //63|3
		} else if (available == 6) {
			project.values[available] = (data[9]&0x1c)>>2; //66|3
		} else if (available == 7) {
			project.values[available] = (data[9]&0xe0)>>5; //69|3
		} else if (available == 8) {
			project.values[available] = (data[10]&0x07); //72|3
		} else if (available == 9) {
			project.values[available] = (data[10]&0x38)>>3; //75|3
		} else if (available == 10) {
			project.values[available] = (data[10]&0xc0)>>6 + (data[11]&0x01)<<2; //78|3
		} else if (available == 11) {
			project.values[available] = (data[11]&0x0e)>>1;
		} 
		*/
		
		if (available == 0) {
			project.values[available] = (data[7]&0xe)>>5;    // 48|3|
		} else if (available == 1) {
			project.values[available] = (data[7]&0x1c)>>2; // 51|3
		} else if (available == 2) {
			project.values[available] = (data[7]&0x03)<<1 + (data[6]&0x80)>>7; //54|3
		} else if (available == 3) {
			project.values[available] = (data[6]&0x70)>>4; //57|3
		} else if (available == 4) {
			project.values[available] = (data[6]&0x0e)>>1; //60|3 
		} else if (available == 5) {
			project.values[available] = (data[6]&0x01)<<2 + (data[9]&0xc0)>>6; //63|3
		} else if (available == 6) {
			project.values[available] = (data[9]&0x38)>>3; //66|3
		} else if (available == 7) {
			project.values[available] = (data[9]&0x07); //69|3
		} else if (available == 8) {
			project.values[available] = (data[8]&0xe0)>>5; //72|3
		} else if (available == 9) {
			project.values[available] = (data[8]&0x1c)>>2; //75|3
		} else if (available == 10) {
			project.values[available] = (data[8]&0x03)<<1 + (data[11]&0x80)>>7; //78|3
		} else if (available == 11) {
			project.values[available] = (data[11]&0x0e)>>1;
		} 
		
		
	}
	

	public void sendMessage(byte[] message) throws IOException {
		mOutputStream.flush();
		mOutputStream.write(message);
		mOutputStream.flush();
	}
	
	public void SaveData(Project project) {
		SQLiteManager sqLiteManager = new SQLiteManager(mContext);
		
		ArrayList<Project> ps = new ArrayList<Project>();
		ps.add(project);
		
		sqLiteManager.add(ps);
		
		//Toast.makeText(mContext, "Success to Save Data", Toast.LENGTH_SHORT).show();
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
