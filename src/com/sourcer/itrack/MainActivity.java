package com.sourcer.itrack;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.view.Menu;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	// for logging
	private static final String TAG = "iTrack";
	// Local Bluetooth adapter
	private static BluetoothAdapter mBluetoothAdapter = null;
	// data log
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "+ ON CREATE +");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SharedPreferences prefs = this.getSharedPreferences(
      	//      "com.sourcer.itrack", Context.MODE_PRIVATE);
        
        /* info kuva */
        //TextView someText = (TextView)findViewById(R.id.status_text);
        //someText.setText(
        //	prefs.getString("com.sourcer.itrack.locations", "not found..."));
        
        /* Use the LocationManager class to obtain GPS locations */
        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListener();
        //mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mlocListener);
        
        // blue test begin
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
    	
        MyBluetoothDev blue = new MyBluetoothDev();
        blue.isSupported();
        blue.checkStatus();
        if(blue.support){Log.v(TAG, "Blue Supported");}
        
        if(blue.enabled){
        	Log.v(TAG, "Blue Enabled");
        	Log.v(TAG, blue.getDevices());
        	//Toast.makeText(this, blue.getDevices(), Toast.LENGTH_LONG).show();
        }
        else{
        	Log.v(TAG, "Enabling...");
        	blue.enable();
        	Log.v(TAG, "...Enabled");
        	Log.v(TAG, blue.getDevices());
        	//Toast.makeText(this, blue.getDevices(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "+ ON CREATE OPT MENU +");
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "++ ON START ++");
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        Log.v(TAG, "+ ON RESUME +");
    }
    
    /* Class MyNetworkTube */
    public static class MyNetworkTube extends AsyncTask<String, Void, String>{
    	public static String onSendLocation(String provider_url) {
    		URL url;
    		HttpURLConnection urlConnection = null;
    		String response = null;    		
			try {
				url = new URL("http://www.ranno.net/itrack");
				Log.v(TAG, "try url");
	    		urlConnection = (HttpURLConnection) url.openConnection();
				Log.v(TAG, "try http");
	    		InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				Log.v(TAG, "try response:" + response);
    		    response = readStream(in);
			} catch (MalformedURLException e) {
				Log.v(TAG, "MalformedURL");
				e.printStackTrace();
			} catch (IOException e) {
				Log.v(TAG, "IOException");
				e.printStackTrace();
			} finally {
				Log.v(TAG, "finally");
				urlConnection.disconnect();
	    	}
			Log.v(TAG, "return response:" + response);
			return response;
	    }

		private static String readStream(InputStream in) {
			System.out.println("readStream");
			String response = in.toString();
			
			System.out.println("readStream response:" + response);
			return response;
		}

		public static String resh = "init";

		@Override
		protected String doInBackground(String... provider_url) {
			URI ur = null;
			try {
				ur = new URI("http://www.ranno.net/itrack");
			} catch (URISyntaxException e1) {
				Log.v(TAG, "URISyntaxException");
				e1.printStackTrace();
			}
			//HttpURLConnection urlConnection = null;
    		HttpResponse response = null;
    		String page = "";
			try {
				HttpContext httpContext = new BasicHttpContext();
    		    HttpClient httpClient = new DefaultHttpClient();
    		    HttpGet httpGet = new HttpGet(ur);
    		    response = httpClient.execute(httpGet, httpContext);
    		    int statusCode = response.getStatusLine().getStatusCode();
				Log.v(TAG, "statuscode:" + statusCode);
    		    HttpEntity entity = response.getEntity();
    		    page = "Http:" + EntityUtils.toString(entity);
				Log.v(TAG, "page:" + page);
    		    entity.consumeContent();
			} catch (MalformedURLException e) {
				Log.v(TAG, "MalformedURL");
				e.printStackTrace();
			} catch (IOException e) {
				Log.v(TAG, "IOException");
				e.printStackTrace();
			} finally {
				Log.v(TAG, "finally");
				//urlConnection.disconnect();
	    	}
			Log.v(TAG, "return response:" + page);
			return page;
		}

	    @Override
	    protected void onPostExecute(String result) {
			Log.v(TAG, "post exec:" + result);
			resh = result;
	    }
    }/* End of Class MyNetworkTube */
    
    /* Class My Location Listener */
    public class MyLocationListener implements LocationListener {
	    public void onLocationChanged(Location loc) {
		    loc.getLatitude();
		    loc.getLongitude();
		    //String provider_url = "http://www.ranno.net/itrack";
		    String vastus = "";
		    MyNetworkTube task = new MyNetworkTube();
		    task.execute(new String[]{"hppt"});
		    vastus = task.resh;
		    //vastus = MyNetworkTube.onSendLocation(provider_url);
		    String Text = "My current location is: " +
						  "\nLatitud = " + loc.getLatitude() +
						  "\nLongitud = " + loc.getLongitude() +
						  "\n>" + vastus + "<";
		    //Toast.makeText( getApplicationContext(),
			//			    Text,
			//			    Toast.LENGTH_SHORT).show();
		    TextView someText = (TextView)findViewById(R.id.status_text);
	        someText.setText(Text);
		    //prefs.edit().putString("com.sourcer.itrack.locations", Text).commit();
		    
	    }
	
	    public void onProviderDisabled(String provider) {
		    Toast.makeText( getApplicationContext(),
		    "Gps Disabled",
		    Toast.LENGTH_SHORT ).show();
	    }
	
	    public void onProviderEnabled(String provider) {
		    Toast.makeText( getApplicationContext(),
		    "Gps Enabled",
		    Toast.LENGTH_SHORT).show();
	    }
	
	    public void onStatusChanged(String provider, int status, Bundle extras) {
	    	// unimplemented
	    }
    }/* End of Class MyLocationListener */
    
    /* Class MyBluetoothDev */
    public class MyBluetoothDev {
    	private static final int REQUEST_ENABLE_BT = 3;
		public boolean support = false;
		public boolean enabled = false;
    	
	    public void isSupported(){
	    	if (mBluetoothAdapter == null) {
	    	    // Device does not support Bluetooth
	    		support = false;
	    	} else {
	    	    // Device supports Bluetooth
	    		support = true;
	    	}
	    }
	    
	    public void checkStatus(){
	    	if (!mBluetoothAdapter.isEnabled()) {
	    		enabled = false;
	    	} else {
	    		enabled = true;
	    	}
	    }
	    
	    public void enable(){
	    	if (!mBluetoothAdapter.isEnabled()) {
	    	    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	    	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
	    	}
	    }
	    
	    public String getDevices(){
	    	String devices = "";
	    	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
	    	// If there are paired devices
	    	if (pairedDevices.size() > 0) {
	    	    // Loop through paired devices
	    	    for (BluetoothDevice device : pairedDevices) {
	    	        // Add the name and address to an array adapter to show in a ListView
	    	        //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
	    	    	//devices = devices + device.getName() + " - " + device.getAddress() + " UUID:" + device.ACTION_UUID + "\n";
	    	    	if(device.getName().equals("OBDII")){
	    	    		devices = connect(device);
	    	    	}
	    	    }
	    	} else {
	    		devices = "no bonded device";
	    	}
	    	return devices;
	    }
	    
	    public String connect(BluetoothDevice device){
	        MyBluetoothTube cn = new MyBluetoothTube(device);
		    cn.execute(new String[]{"hppt"});
		    Log.v(TAG, "connect:" +  cn.resh2);
		    return cn.resh2;
	    }
    }/* End of Class MyBluetoothDev */
    

    /* Class MyBluetoothTube */
    public static class MyBluetoothTube extends AsyncTask<String, Void, String>{

		public static String resh2 = "initConnect:MyBluetoothTube";
		private final BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;
		private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	    @Override
	    protected void onPreExecute() {
	    	Log.v(TAG, "onPreExecute:MyBluetoothTube");
	    	//Log.v(TAG, "UUID:" + MY_UUID.toString());
	    }
	    
	    public MyBluetoothTube(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;
	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	        	tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
	        } catch (IOException e) { }
	        mmSocket = tmp;
	    }

		@Override
		protected String doInBackground(String... provider_url) {
			// Cancel discovery because it will slow down the connection
			mBluetoothAdapter.cancelDiscovery();
			byte[] buffer = new byte[1024];
			byte[] reset_adapter = new byte[32];
			byte[] echo_off = new byte[32];
			byte[] proto_dect = new byte[32];
			byte[] suported_pids = new byte[3];
			byte[] dect_proto = new byte[32];
			suported_pids[0] = 0x01;
			suported_pids[1] = 0x0C;
			suported_pids[2] = 0x0D; // \r
			reset_adapter = "AT Z\r".getBytes();
			echo_off = "AT E0\r".getBytes();
			proto_dect = "AT SP 00\r".getBytes();
			//suported_pids = "0100\r".getBytes();
			dect_proto = "AT DP\r".getBytes();
		    //Log.v(TAG, "byte biits0:" + String.format("%02X", biits[0]));
		    //Log.v(TAG, "byte biits1:" + String.format("%02X", biits[1]));
		    //Log.v(TAG, "byte biits1:" + String.format("%02X", biits[2]));
			
			//0105 coolant
			//0902 vin
			//biits = "0x0100"; //pid-s supported
			//biits = "0x011C"; //obd standard
			//biits[2] = "\r".getBytes();
	 
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return  "unable to connect: " + mmDevice.getName();
	        }
	 
	        // Do work to manage the connection (in a separate thread)
	        //manageConnectedSocket(mmSocket);
	        MyBluetoothReadWrite rw = new MyBluetoothReadWrite(mmSocket);
	        
	        rw.write(reset_adapter);
	        android.os.SystemClock.sleep(250);
		    //rw.execute(new String[]{"hppt"});
	        rw.write(echo_off);
	        android.os.SystemClock.sleep(250);
		    //rw.execute(new String[]{"hppt"});
	        //rw.write(proto_dect);
	        //android.os.SystemClock.sleep(250);
		    //rw.execute(new String[]{"hppt"});
	        rw.write(suported_pids);
	        android.os.SystemClock.sleep(300);
		    //rw.execute(new String[]{"hppt"});
	        //rw.write(dect_proto);
	        //android.os.SystemClock.sleep(250);
		    rw.execute(new String[]{"hppt"});
	        
		    Log.v(TAG, "read:" +  rw.resh3);
		    resh2 = "connected to:" + mmDevice.getName() + ":" + rw.resh3;
	        return "connected to:" + mmDevice.getName() + ":" + rw.resh3;
		}

	    @Override
	    protected void onPostExecute(String result) {
			Log.v(TAG, "onPostExecute:" + result);
			resh2 = result;
	    }
    }/* End of Class MyBluetoothTube */
    
    /* Class MyBluetoothReadWrite */
    public static class MyBluetoothReadWrite extends AsyncTask<String, Void, String>{
    	public static String resh3 = "resh3";
    	private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

	    @Override
	    protected void onPreExecute() {
	    	Log.v(TAG, "onPreExecute:MyBluetoothReadWrite");
	    }
	    
        public MyBluetoothReadWrite(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
     
            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
     
            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

		@Override
		protected String doInBackground(String... provider_url) {
			byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes = 0; // bytes returned from read()
     
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    Log.v(TAG, "REDOUT:" + String.format("%02X", bytes));
        	        android.os.SystemClock.sleep(300);
                    //return "Read ok:" + bytes;
                } catch (IOException e) {
        			Log.v(TAG, "Read error");
                	//return "Read error";
                    break;
                }
            }
			Log.v(TAG, "cread return"+bytes);
	        return ""+bytes;
		}

	    @Override
	    protected void onPostExecute(String result) {
			Log.v(TAG, "onPostExecute:" + result);
			resh3 = result;
	    }
     
        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
        	try {
                mmOutStream.write(bytes);
                for (byte b : bytes) {
                    sb.append(String.format("%02X ", b));
                }
                Log.v(TAG, "Write bytes:" + sb.toString());
            } catch (IOException e) {
            	Log.v(TAG, "Write error");
            }
        }
     
        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    } /*End of Class MyBluetoothReadWrite*/

    @Override
    public synchronized void onPause() {
        super.onPause();
        Log.v(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth chat services
        //if (mChatService != null) mChatService.stop();
        Log.v(TAG, "--- ON DESTROY ---");
    }
}
