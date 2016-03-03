package com.example.watercap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import realarm.hardware.HardwareControl;

public class watercap {
	//static 
	//{
	//	System.loadLibrary("RealarmHardwareJni");
	//}
	//public native static FileDescriptor OpenSerialPort(String path, int baudrate,int flags);
	//public native static void CloseSerialPort();
	private static final String TAG = "SerialPort";
	private static FileDescriptor mFd;
	private static FileInputStream mFileInputStream;
	private static FileOutputStream mFileOutputStream;
	private static final String BASIC_URL="http://123.56.69.228/api/devdatas";
	private static String did;
	private static String address;
	private static String jingdu;
	private static String weidu;
	private static String batterypower;
	private static String watertemper;
	private static String cod;
	private static String imgnums;
	private static String modelnums;
	private static String remark;
	private static String uptime;
	private static String widther;
	private static String waterdeep;
	private static String flowspeed;
	private static String ammoniavalue;
	private static String nitratevalue;
	private static String imgdata;
	private static String modedata;
	public static String getData()
	{
		return "123";
	}
	public static String setData(String data)
	{
		return "123";
	}
	public static void set_did(String value)
	{
		//Log.i("CAP", value);
		did=value;
	}
	public static void set_address(String value)
	{
		address=value;
	}
	public static void set_jingdu(String value)
	{
		jingdu=value;
	}
	public static void set_weidu(String value)
	{
		weidu=value;
	}
	public static void set_batterypower(String value)
	{
		batterypower=value;
	}
	public static void set_watertemper(String value)
	{
		watertemper=value;
	}
	public static void set_cod(String value)
	{
		cod=value;
	}
	public static void set_nitratevalue(String value)
	{
		nitratevalue=value;
	}
	public static void set_ammoniavalue(String value)
	{
		ammoniavalue=value;
	}
	public static void set_flowspeed(String value)
	{
		flowspeed=value;
	}
	public static void set_waterdeep(String value)
	{
		waterdeep=value;
	}
	public static void set_widther(String value)
	{
		widther=value;
	}
	public static void set_uptime(String value)
	{
		uptime=value;
	}
	public static void set_remark(String value)
	{
		remark=value;
	}
	public static void set_img(String num,String json)
	{
		imgnums=num;
		if(json!=null)
			imgdata="{"+json+"}";
	}
	public static void set_model(String num,String json)
	{
		modelnums=num;
		modedata="{"+json+"}";
	}
	public static String getPacket()
	{
		//if(did==null||address==null||jingdu==null||weidu==null||batterypower==null||watertemper==null||cod==null||nitratevalue==null||ammoniavalue==null||flowspeed==null||
		//		waterdeep==null||widther==null||uptime==null||remark==null||modelnums==null||modedata==null)
		//	return null;
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("did",did));
        nameValuePair.add(new BasicNameValuePair("address",address));
        nameValuePair.add(new BasicNameValuePair("jingdu",jingdu));
        nameValuePair.add(new BasicNameValuePair("weidu",weidu));
        nameValuePair.add(new BasicNameValuePair("batterypower",batterypower));
        nameValuePair.add(new BasicNameValuePair("watertemper",watertemper));
        nameValuePair.add(new BasicNameValuePair("cod",cod));
        nameValuePair.add(new BasicNameValuePair("nitratevalue",nitratevalue));
        nameValuePair.add(new BasicNameValuePair("ammoniavalue",ammoniavalue));
        nameValuePair.add(new BasicNameValuePair("flowspeed",flowspeed));
        nameValuePair.add(new BasicNameValuePair("waterdeep",waterdeep));
        nameValuePair.add(new BasicNameValuePair("widther",widther));
        nameValuePair.add(new BasicNameValuePair("uptime",uptime));
        nameValuePair.add(new BasicNameValuePair("remark",remark));
        nameValuePair.add(new BasicNameValuePair("modelnums",modelnums));
        nameValuePair.add(new BasicNameValuePair("modedatas",modedata));
        if(imgdata!=null)
        {
            nameValuePair.add(new BasicNameValuePair("imgnums",imgnums));
        	nameValuePair.add(new BasicNameValuePair("imgdatas",imgdata));
        }
        else
            nameValuePair.add(new BasicNameValuePair("imgnums","0"));
        //Log.i("getPacket==>", nameValuePair.toString());
        return nameValuePair.toString();
	}
	public static Boolean reSendNet(String packet)
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(BASIC_URL);
		List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair(packet,null));
		//Log.i("reSendNet==>", packet);
        try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int httpCode = httpResponse.getStatusLine().getStatusCode();
            if (httpCode == HttpURLConnection.HTTP_OK&&httpResponse!=null) {
                HttpEntity entity = httpResponse.getEntity();
                InputStream inputStream = entity.getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String s;
                StringBuilder result = new StringBuilder();
                while (((s = reader.readLine()) != null)) {
                    result.append(s);
                }
                reader.close();
                JSONObject jsonObject = new JSONObject(result.toString());
                String status = jsonObject.getString("status");
                String data = jsonObject.getString("data");
                JSONObject jsonObject2 = new JSONObject(data);
                String error = jsonObject2.getString("error");
                Log.v("<=="," status " +status+" data "+data+" error "+error);
                if(status.compareTo("ERR")==0)
                	return false;
            } else {
                Log.e("CAP","Error Response" + httpResponse.getStatusLine().toString());
                return false;
            }
        } catch (UnsupportedEncodingException e) {
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        } 
		finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();// 最后关掉链接。
                httpClient = null;
            }
        }
		return true;
	}
	public static String sendNet()
	{
		
		HttpClient httpClient = new DefaultHttpClient();
		//if(did==null||address==null||jingdu==null||weidu==null||batterypower==null||watertemper==null||cod==null||nitratevalue==null||ammoniavalue==null||flowspeed==null||
		//		waterdeep==null||widther==null||uptime==null||remark==null||modelnums==null||modedata==null)
		//	return "wrong param";
        HttpPost httpPost = new HttpPost(BASIC_URL);
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("did",did));
        nameValuePair.add(new BasicNameValuePair("address",address));
        nameValuePair.add(new BasicNameValuePair("jingdu",jingdu));
        nameValuePair.add(new BasicNameValuePair("weidu",weidu));
        nameValuePair.add(new BasicNameValuePair("batterypower",batterypower));
        nameValuePair.add(new BasicNameValuePair("watertemper",watertemper));
        nameValuePair.add(new BasicNameValuePair("cod",cod));
        nameValuePair.add(new BasicNameValuePair("nitratevalue",nitratevalue));
        nameValuePair.add(new BasicNameValuePair("ammoniavalue",ammoniavalue));
        nameValuePair.add(new BasicNameValuePair("flowspeed",flowspeed));
        nameValuePair.add(new BasicNameValuePair("waterdeep",waterdeep));
        nameValuePair.add(new BasicNameValuePair("widther",widther));
        nameValuePair.add(new BasicNameValuePair("uptime",uptime));
        nameValuePair.add(new BasicNameValuePair("remark",remark));
        nameValuePair.add(new BasicNameValuePair("modelnums",modelnums));
        nameValuePair.add(new BasicNameValuePair("modedatas",modedata));
        if(imgdata!=null)
        {
            nameValuePair.add(new BasicNameValuePair("imgnums",imgnums));
        	nameValuePair.add(new BasicNameValuePair("imgdatas",imgdata));
        }
        else
            nameValuePair.add(new BasicNameValuePair("imgnums","0"));
        //JSONObject jsonObject = new JSONObject();
        //JSONObject jsonObject2 = new JSONObject();
        //jsonObject.put("uemail", userbean.getEmail());
        //jsonObject.put("password", userbean.getPassword());
        //jsonObject2.put("userbean", jsonObject);
        //nameValuePair.add(new BasicNameValuePair("jsonString", jsonObject
        //       .toString()));
        did=null;address=null;jingdu=null;weidu=null;batterypower=null;watertemper=null;cod=null;nitratevalue=null;ammoniavalue=null;flowspeed=null;
				waterdeep=null;widther=null;uptime=null;remark=null;imgnums=null;modelnums=null;modedata=null;imgdata=null;
        Log.i("==>", nameValuePair.toString());
        try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));
			Log.i("==>","here1");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            Log.i("==>","here2");
            int httpCode = httpResponse.getStatusLine().getStatusCode();
            Log.i("==>","here3");
            if (httpCode == HttpURLConnection.HTTP_OK&&httpResponse!=null) {
               // Header[] headers = httpResponse.getAllHeaders();
                HttpEntity entity = httpResponse.getEntity();
                Log.i("==>","here4");
                //Header header = httpResponse.getFirstHeader("content-type");
                //读取服务器返回的json数据（接受json服务器数据）
                InputStream inputStream = entity.getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);// 读字符串用的。
                String s;
                StringBuilder result = new StringBuilder();
                while (((s = reader.readLine()) != null)) {
                    result.append(s);
                }
                reader.close();// 关闭输入流
                Log.i("<==", result.toString());
                //在这里把result这个字符串个给JSONObject。解读里面的内容。
                JSONObject jsonObject = new JSONObject(result.toString());
                String status = jsonObject.getString("status");
                String data = jsonObject.getString("data");
                JSONObject jsonObject2 = new JSONObject(data);
                String error = jsonObject2.getString("error");
                //String info = jsonObject.getString("info");
                Log.v("<=="," status " +status+" data "+data+" error "+error);
                if(status.compareTo("ERR")==0)
                	return "failed";
            } else {
                Log.e("CAP","Error Response" + httpResponse.getStatusLine().toString());
                return "false";
            }
        } catch (UnsupportedEncodingException e) {
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } catch (JSONException e) {
            e.printStackTrace();
            return "failed";
        } 
		finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();// 最后关掉链接。
                httpClient = null;
            }
        }
		return "ok";
	}
	public static boolean init(Context ctx) throws IOException
	{
		File device=null;
		/** Read serial port parameters */
        SharedPreferences sp = ctx.getSharedPreferences("android_serialport_api.sample_preferences", Context.MODE_PRIVATE);
        String path = sp.getString("DEVICE", "/dev/ttyAMA4");
        int baudrate = Integer.decode(sp.getString("BAUDRATE", "9600"));

        /* Check parameters */
        if ( (path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
        }
        device=new File(path);
        /* Open the serial port */
		/* Check access permission */
		if (!device.canRead() || !device.canWrite()) {
			try {
				/* Missing read/write permission, trying to chmod the file */
				Process su;
				su = Runtime.getRuntime().exec("/system/xbin/su");
				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
						+ "exit\n";
				su.getOutputStream().write(cmd.getBytes());
				if ((su.waitFor() != 0) || !device.canRead()
						|| !device.canWrite()) {
					throw new SecurityException();
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new SecurityException();
			}
		}

		mFd = HardwareControl.OpenSerialPort(device.getAbsolutePath(), baudrate, 0);
		if (mFd == null) {
			Log.e(TAG, "native open returns null");
			return false;
		}
		mFileInputStream = new FileInputStream(mFd);
		mFileOutputStream = new FileOutputStream(mFd);
		//new ReadThread().start();
		return true;
	}
	public static void deinit()
	{
		HardwareControl.CloseSerialPort();
	}
	public static InputStream getInputStream() {
		return mFileInputStream;
	}

	public static OutputStream getOutputStream() {
		return mFileOutputStream;
	}
/*
	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[64];
					if (mFileInputStream == null)
						return;

					
					size = mFileInputStream.read(buffer);
					if (size > 0) {
						onDataReceived(buffer, size);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	protected void onDataReceived(final byte[] buffer, final int size) {
            runOnUiThread(new Runnable() {
                    public void run() {
                            //if (EditTextReception != null) {
                            //	EditTextReception.append(new String(buffer, 0, size));
                            //}
                    }
            });
    }
*/
}