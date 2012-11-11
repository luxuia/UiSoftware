package com.example.uisoftware;

import java.util.Set;

import com.example.uisoftware.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ConnectView  extends Activity {
    private final static String TAG = "luxuia";
    private final static boolean D = false  ;
    
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    
    // Name of the connected device
    private static String mConnectedDeviceName = null;
    
    // Array adapter for conversation thread
    //private static ArrayAdapter<String> mConversationArrayAdapter = null;
    
    // Loacal Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter;
    // Chat services
    private BluetoothChatService mChatService = null;
    
    private Button	mSearchButton;
    private Button mConnectButton;
    
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
   // private ArrayAdapter<String> mNewDevicesArrayAdapter;

    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_view);
        
        Log.e(TAG, "Connect onCreate");
        
       // listView  = (ListView)findViewById(R.id.paired_devices);
        //listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData()));
        
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
        	Toast.makeText(getApplicationContext(), "Bluetooth Can't find", Toast.LENGTH_SHORT).show();
        	finish();
        	return;
        }
    
    }
    


	@Override
	protected void onDestroy() {
		
		// TODO Auto-generated method stub
		super.onDestroy(); 
		Log.e(TAG, "Connect onDestroy");
		
		if (mChatService != null) mChatService.stop();
	}

	@Override
	protected void onPause() {
		
		// TODO Auto-generated method stub
		super.onPause(); 
		Log.e(TAG, "Connect onPause");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		getParent().getWindow()
			.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		Log.e(TAG, "Connect onResume");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.e(TAG, "Connect onStart");
		
		if (!mBluetoothAdapter.isEnabled()) {
			((ToggleButton)findViewById(R.id.bluetooth_state)).setChecked(false);
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			
		} else {
			((ToggleButton)findViewById(R.id.bluetooth_state)).setChecked(true);
			if (mChatService == null) {
				setupChat();
			}
		}
		
		((ToggleButton)findViewById(R.id.bluetooth_state)).setOnClickListener( new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (!mBluetoothAdapter.isEnabled()) {
					mBluetoothAdapter.enable();
				} else {
					mBluetoothAdapter.disable();
				}
			}
		});
		
		mSearchButton = (Button)findViewById(R.id.bluetooth_searchbutton);
		
		mSearchButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				doSearchBluetooth();
				//arg0.setVisibility(View.GONE);
			}
		});
		
		mConnectButton = (Button)findViewById(R.id.bluetooth_connectbutton);
		
		mConnectButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String address = ((TextView)findViewById(R.id.bluetooth_selected)).getText().toString().trim();
				//int split = address.indexOf("\n");
				//address = address.substring(split+1);
				
				try {
					BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
					
					if (device.getBondState() == BluetoothDevice.BOND_NONE) {
						Log.e(TAG, "还没有配对呢");
						throw new IllegalArgumentException();
					}
					
					mChatService.connect(device);
					Toast.makeText(getApplicationContext(), "正在连接" + address, Toast.LENGTH_SHORT).show();
					
				} catch (IllegalArgumentException ex) {
					Toast.makeText(getApplicationContext(), "设备没准备好哦", Toast.LENGTH_SHORT).show();
				}
				
				
				/*
			
				ArrayList<byte[]> datas = new ArrayList<byte[]>();
				datas.add(address.getBytes());
				SaveData(getApplicationContext(), "hello",datas);
				*/
			
				//System.console().sleep(1000);
			
				
				//((TextView)findViewById(R.id.bluetooth_selected)).setText(string);
			}

			
		});
	}
	
	
	
	
	private void doSearchBluetooth() {
		// TODO Auto-generated method stub
		
		mPairedDevicesArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.device_name);
	//	mNewDevicesArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.device_name);
		
		 ListView pairedListView = (ListView) findViewById(R.id.paired_devices);
	     pairedListView.setAdapter(mPairedDevicesArrayAdapter);
	     pairedListView.setOnItemClickListener(mDeviceClickListener);

	     /*
	        // Find and set up the ListView for newly discovered devices
	     ListView newDevicesListView = (ListView) findViewById(R.id.new_devices);
	     newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
	     newDevicesListView.setOnItemClickListener(mDeviceClickListener);
	     */
	     IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
	     this.registerReceiver(mReceiver, filter);
	     
	     filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
	     this.registerReceiver(mReceiver, filter);
	     
	     
	     Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
	     
	     
	     if (pairedDevices.size() > 0) {
	    	 findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
	    	 for(BluetoothDevice device:pairedDevices) {
	    		 mPairedDevicesArrayAdapter.add(device.getName()+"\n" + device.getAddress());
	    	 }
	     }
	     
	     doDiscovery();

	}
	
	
	private void doDiscovery() {
		// TODO Auto-generated method stub
		
		//findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
		
	}

	private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBluetoothAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);

            TextView textView = (TextView) findViewById(R.id.bluetooth_selected);
            
            textView.setText(address);
        }
    };
	
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            
            // 两个ListView 不好控制
            // When discovery finds a device
          //  findViewById(R.id.title_new_devices).setVisibility(View.VISIBLE);
            
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
               // if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                	//mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
              //  }
            // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
              //  setProgressBarIndeterminateVisibility(false);
               // setTitle(R.string.select_device);
            	
            	
                if (mPairedDevicesArrayAdapter.getCount() == 0) {
                  //  String noDevices = getResources().getText(R.string.none_found).toString();
                    mPairedDevicesArrayAdapter.add("再等会吧~");
                    //findViewById(R.id.title_new_devices).setVisibility(View.GONE);
                    //mNewDevicesArrayAdapter.add("No News find");
                }
            }
        }
    };
    
    
    
	private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
       // mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
      //  mConversationView = (ListView) findViewById(R.id.in);
      //  mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
       // mOutEditText = (EditText) findViewById(R.id.edit_text_out);
       // mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
      /*  mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                TextView view = (TextView) findViewById(R.id.edit_text_out);
                String message = view.getText().toString();
                sendMessage(message);
            }
        });
       */
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(this, mHandler);

        new StringBuffer("");
    }
  
	
	
	@SuppressWarnings("unused")
	private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (mBluetoothAdapter.getScanMode() !=
            BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }
	
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BluetoothChatService.STATE_CONNECTED:
                  //  mTitle.setText(R.string.title_connected_to);
                  //  mTitle.append(mConnectedDeviceName);
                    //mConversationArrayAdapter.clear();
                    break;
                case BluetoothChatService.STATE_CONNECTING:
                 //   mTitle.setText(R.string.title_connecting);
                    break;
                case BluetoothChatService.STATE_LISTEN:
                case BluetoothChatService.STATE_NONE:
                  //  mTitle.setText(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                //mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                //mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                // Get the device MAC address
            	
                // Get the BLuetoothDevice object
              //  BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                // Attempt to connect to the device
             //   mChatService.connect(device);
            }
            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
            	((ToggleButton)findViewById(R.id.bluetooth_state)).setChecked(true);
            	
                // Bluetooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable Bluetooth or an error occured
                
            	((ToggleButton)findViewById(R.id.bluetooth_state)).setChecked(false);
            	
            	Log.d(TAG, "BT not enabled");
                Toast.makeText(this, "蓝牙可能不可用哦", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    
    
}