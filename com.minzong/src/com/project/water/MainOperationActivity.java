package com.project.water;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.watercap.watercap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.media.audiofx.BassBoost.Settings;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainOperationActivity extends Activity {
	String didvalue = null;		
	String addressvalue = null;
	String jingduvalue = null;
	String weiduvalue = null;
	String batterypowervalue = null;
	String watertempervalue = null;
	String codvalue = null;
	String nitratevalue = null;
	String ammoniavalue = null;
	String flowspeedvalue = null;
	String waterdeepvalue = null;
	String widthervalue = null;
	String uptimevalue = null;
	String remarkvalue = null;
	
	public int delay = 1000;
	GPSProvider gpsprovider;
	private final Handler handler = new Handler();
	Boolean auto = true;

	String user;
	int modexuanze;
	int zidongjiange;
	Bundle bundle;
	
	TelephonyManager        Tel;
	MyPhoneStateListener    MyListener;
	
	ImageView signalstrength;
	
	String stringmodexuanze;
	String modexuanzecanshu0;
	String modexuanzecanshu1;
	int zidongjiangeminute;

	List<Map<String, String>> listuser = new ArrayList<Map<String, String>>();
	List<Map<String, String>> listmodexuanze = new ArrayList<Map<String, String>>();
	List<Map<String, String>> listceliangfangshi = new ArrayList<Map<String, String>>();
	
	List listtupian;
	List listtupianContent = new ArrayList();

	// 初始化控件
	Spinner spinnermoxing;
	Button buttonpaizhao;
	Button buttontuichu;

	TextView textdianchi;
	ImageView imageviewdianchi;
	TextView textGPSLocate;
	TextView textGSMdetails;
	TextView texttimedetail;
	TextView textID;
	
	TextView textmoxing1;
	TextView textmoxing2;
	TextView textmoxing3;
	EditText editmoxing1;
	EditText editmoxing2;
	EditText editmoxing3;
	
	EditText editCOD;
	EditText editliusu;
	EditText editxiaodan;
	EditText editshendu;
	EditText editandan;
	EditText editkuandu;
	EditText editshuiwen;
	EditText editdizhi;
	
	TextView textcishutime;
	
	Button buttonshujushangchuan;
	Button buttonceliang;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	byte[] response = new byte[28];
	static boolean header_got=false;
	static int read_len=0,to_read=0;
	byte[] cmd={0x24,0x32,(byte)0xff,0x23,0x0a};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainoperation);

		//buttonmoxing = (Button) findViewById(R.id.buttonmoxing);
		spinnermoxing = (Spinner) findViewById(R.id.spinnermoxing);
		buttonpaizhao = (Button) findViewById(R.id.buttonpaizhao);
		buttontuichu = (Button) findViewById(R.id.buttontuichu);

		imageviewdianchi = (ImageView) findViewById(R.id.imageviewdianchi);
		//textdianchi = (TextView) findViewById(R.id.textdianchi);
		textGPSLocate = (TextView) findViewById(R.id.textGPSLocate);
		textGSMdetails = (TextView) findViewById(R.id.textGSMdetails);
		signalstrength = (ImageView) findViewById(R.id.imageviewsignalstrength);
		texttimedetail = (TextView) findViewById(R.id.texttimedetail);
		textID = (TextView) findViewById(R.id.textID);

		textmoxing1 = (TextView) findViewById(R.id.textshangdi);
		textmoxing2 = (TextView) findViewById(R.id.textxiadi);
		textmoxing3 = (TextView) findViewById(R.id.textshui);
		editmoxing1 = (EditText) findViewById(R.id.editshangdi);
		editmoxing2 = (EditText) findViewById(R.id.editxiadi);
		editmoxing3 = (EditText) findViewById(R.id.editshui);
		
		textcishutime = (TextView) findViewById(R.id.textcishutime);
		
		editCOD = (EditText) findViewById(R.id.editCOD);
		editliusu = (EditText) findViewById(R.id.editliusu);
		editxiaodan = (EditText) findViewById(R.id.editxiaodan);
		editshendu = (EditText) findViewById(R.id.editshendu);
		editandan = (EditText) findViewById(R.id.editandan);
		editkuandu = (EditText) findViewById(R.id.editkuandu);
		editshuiwen = (EditText) findViewById(R.id.editshuiwen);
		editdizhi = (EditText) findViewById(R.id.editdizhi);
		
		buttonshujushangchuan = (Button) findViewById(R.id.buttonshujushangchuan);
		buttonceliang = (Button) findViewById(R.id.buttonshujuceliang);
		
		SharedPreferencesDatabase sharedPreferenceDatabase = new SharedPreferencesDatabase();
		try {
			sharedPreferenceDatabase.DefaultSharedPreferences((Context) this);
			listuser = sharedPreferenceDatabase.GetuserArray((Context) this);
			listmodexuanze = sharedPreferenceDatabase
					.GetmodexuanzeArray((Context) this);
			listceliangfangshi = sharedPreferenceDatabase
					.GetjiangeArray((Context) this);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setButtonPaizhao();
		setButtontongliang();
		setButtontuichu();
		setButtonshujushangchuan();
		setButtonceliang();

		batteryLevel();
		SysApplication.getInstance().addActivity(this);

		zidongjiangeminute = 0;
		setspinnermoxing();
		
		getbundledetail();

		gpsprovider = GPSProvider.getInstance(MainOperationActivity.this);
		Init();
		getdeviceinfo();
		setID();
		activateLocation();
		initsignalstrength();
	}

	public void setButtonPaizhao() {
		buttonpaizhao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainOperationActivity.this,
						CameraActivity.class);// 当前的Activity为Test,目标Activity为Name
				// 从下面这行开始是将数据传给新的Activity,如果不传数据，只是简单的跳转，这几行代码请注释掉
				Bundle bundle = new Bundle();
				bundle.putString("user", user);// key1为名，value1为值
				bundle.putString("key2", "value2");
				intent.putExtras(bundle);
				// 传数据结束
				startActivity(intent);
			}
		});
	}

	public void setButtontuichu() {
		buttontuichu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				closeactivities();
			}
		});
	}
	
	public void setButtonshujushangchuan() {
		buttonshujushangchuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//encodetupian();
				upload();
			}
		});
	}
	
	public void setButtonceliang() {
		buttonceliang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//encodetupian();
				send_485();
			}
		});
	}

	public void closeactivities() {
		SysApplication.getInstance().exit();
	}

	public void setButtontongliang() {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backpressed();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void batteryLevel() {
		BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				context.unregisterReceiver(this);
				int rawlevel = intent.getIntExtra("level", -1);// 获得当前电量
				int scale = intent.getIntExtra("scale", -1);
				// 获得总电量
				int level = -1;
				if (rawlevel >= 0 && scale > 0) {
					level = (rawlevel * 100) / scale;
				}

				if (20 > level) {
					imageviewdianchi.setBackground(getResources().getDrawable(
							R.drawable.batt0));
				} else if (40 > level) {
					imageviewdianchi.setBackground(getResources().getDrawable(
							R.drawable.batt1));
				} else if (60 > level) {
					imageviewdianchi.setBackground(getResources().getDrawable(
							R.drawable.batt2));
				} else if (80 > level) {
					imageviewdianchi.setBackground(getResources().getDrawable(
							R.drawable.batt3));
				} else if (100 > level) {
					imageviewdianchi.setBackground(getResources().getDrawable(
							R.drawable.batt4));
				}

				if (100 == level) {
					imageviewdianchi.setBackground(getResources().getDrawable(
							R.drawable.batt5));
				}

			}
		};
		IntentFilter batteryLevelFilter = new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryLevelReceiver, batteryLevelFilter);
	}

	public void backpressed() {
		Intent intent = new Intent();
		intent.setClass(MainOperationActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);
	}

	public void getdeviceinfo() {
		getGPSLocation();
	}

	public void getGPSLocation() {
		handler.postDelayed(task, delay);

	}

	private final Runnable task = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			if (auto) { // change to refresh if it is auto mod
				/*boolean connect = getconnect();
				if (connect == false) {
					textGSMdetails.setText(getResources().getString(
							R.string.connecting));
				} else {
					textGSMdetails.setText(getResources().getString(
							R.string.connected));
				}*/

				String strLocation = gpsprovider.getLocation();
				textGPSLocate.setText(strLocation);

				SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss");
				String date = sDateFormat.format(new java.util.Date());
				texttimedetail.setText(date);
				handler.postDelayed(this, delay);

				/*
				 * 这里写你的功能模块代码
				 */

			}

		}
	};

	private boolean getconnect() {
		// 得到网络连接信息
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();

		if (gprs == State.CONNECTED) {
			// Toast.makeText(this, "wifi is open! gprs",
			// Toast.LENGTH_SHORT).show();
			return true;
		}

		return false;

	}
	
	private void initsignalstrength()
	{
		MyListener   = new MyPhoneStateListener();
        Tel       = ( TelephonyManager )getSystemService(Context.TELEPHONY_SERVICE);
      Tel.listen(MyListener ,PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}
	
	private class MyPhoneStateListener extends PhoneStateListener
    {
     
      @Override
      public void onSignalStrengthsChanged(SignalStrength signalStrength)
      {
         super.onSignalStrengthsChanged(signalStrength);
         /*Toast.makeText(getApplicationContext(), "Go to Firstdroid!!! GSM Cinr = "
            + String.valueOf(signalStrength.getGsmSignalStrength()), Toast.LENGTH_SHORT).show();*/
         
         int signel = signalStrength.getGsmSignalStrength();
         int level; 
         /*if (signel <= 2 || signel == 99) level = SIGNAL_STRENGTH_NONE_OR_UNKNOWN; 
         else if (signel >= 12) level = SIGNAL_STRENGTH_GREAT;
         else if (signel >= 8)  level = SIGNAL_STRENGTH_GOOD; 
         else if (signel >= 5)  level = SIGNAL_STRENGTH_MODERATE;  
                 else level = SIGNAL_STRENGTH_POOR;*/
         
         if (signel <= 2 || signel == 99) {
        	 signalstrength.setBackground(getResources().getDrawable(
						R.drawable.batt0));
			} else if (5 > signel) {
				signalstrength.setBackground(getResources().getDrawable(
						R.drawable.batt1));
			} else if (8 > signel) {
				signalstrength.setBackground(getResources().getDrawable(
						R.drawable.batt2));
			} else if (12 > signel) {
				signalstrength.setBackground(getResources().getDrawable(
						R.drawable.batt3));
			} else  {
				signalstrength.setBackground(getResources().getDrawable(
						R.drawable.batt4));
			}

			
      }

    };

	private void getbundledetail()
	{
		bundle = this.getIntent().getExtras();
		user = (String) bundle.get("user");
		modexuanze = bundle.getInt("modexuanze");
		zidongjiange = bundle.getInt("zidongjiange");
		
		//mode
		Map<String, String> map1 = new HashMap<String, String>();
		map1 = listmodexuanze.get(modexuanze);
		stringmodexuanze = map1.get(SharedPreferencesDatabase.modexuanze);
		modexuanzecanshu0 = map1.get(SharedPreferencesDatabase.modexuanzecanshu0);
		modexuanzecanshu1 = map1.get(SharedPreferencesDatabase.modexuanzecanshu1);
		
		//zidongjiange
		Map<String, String> map2 = new HashMap<String, String>();
		
		if(zidongjiange>0)
		{
		map2 = listceliangfangshi.get(zidongjiange-1);
		String jiange = map2.get(SharedPreferencesDatabase.decimal);
		zidongjiangeminute = Integer.parseInt(jiange);
		if (map2.get(SharedPreferencesDatabase.hour).length() > 0)
		{
			zidongjiangeminute = 60 * Integer.parseInt(jiange);
		}
		
		}
	}
	private void setID() {
		
		textID.setText(user);
	}
	
	private void activateLocation()
	{
		LocationManager lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
        //判断GPS是否正常启动
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){

            //返回开启GPS导航设置界面
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent,0);
            return;
        }
	}
	
	private byte[] readFile(String tupianPath) throws IOException
	{
		File file = new File(tupianPath);  
        FileInputStream in = new FileInputStream(file);  
        byte[] buffer = new byte[(int) file.length()];  
        int length = in.read(buffer);
        return buffer;
	}
	
	private void encodetupian() throws IOException
	{
		listtupian = SysApplication.getInstance().listtupian;
		if(null!=listtupian)
		{
		for(int i=0;i<listtupian.size();i++)
		{
			String tupianPath = (String) listtupian.get(i);
			byte[] buffer = readFile(tupianPath);
			byte[] bufferencodebase = Base64.encode(buffer, Base64.DEFAULT);
			listtupianContent.add(bufferencodebase);
			writeFile(tupianPath+".base", bufferencodebase);
		}
		}
	}
	
	private void writeFile(String filePath, byte[] filecontent) throws IOException
	{
		File file = new File(filePath);    
		  
        FileOutputStream fos = new FileOutputStream(file);    
  
        //byte [] bytes = write_str.getBytes();   
  
        fos.write(filecontent);   
  
        fos.close(); 
	}
	
	private String Encode(byte[] buffer)
	{
		String str = "Hello!";
		// 在这里使用的是encode方式，返回的是byte类型加密数据，可使用new String转为String类型
		String strBase64 = new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
		return strBase64;
	}
	
	public void setspinnermoxing() {
		// 建立Adapter并且绑定数据源
		String[] mItems = new String[3];
		//Map<String, String> map = new HashMap<String, String>();

		//for (int i = 0; i < 3; i++) {
			//map = listmodexuanze.get(i);
			//mItems[i] = map.get(SharedPreferencesDatabase.modexuanze);
		//}
		
		mItems[0] = this.getString(R.string.tixing);
		mItems[1] = this.getString(R.string.juxing);
		mItems[2] = this.getString(R.string.yuanxing);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinner, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定 Adapter到控件
		spinnermoxing.setAdapter(adapter);
		
		final String shangdi = this.getString(R.string.shangdi);
		final String xiadi = this.getString(R.string.xiadi);
		final String shendu = this.getString(R.string.shendu);
		final String changdu = this.getString(R.string.changdu);
		final String zhijing = this.getString(R.string.zhijing);
		final String kuandu = this.getString(R.string.kuandu);
		spinnermoxing
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						if(pos==0)
						{
							textmoxing1.setText(shangdi);
							textmoxing2.setText(xiadi);
							textmoxing3.setText(shendu);
							textmoxing3.setVisibility(View.VISIBLE);
							editmoxing3.setVisibility(View.VISIBLE);
						}
						else if(pos==1)
						{
							textmoxing1.setText(changdu);
							textmoxing2.setText(kuandu);
							textmoxing3.setVisibility(View.INVISIBLE);
							editmoxing3.setVisibility(View.INVISIBLE);
						}
						else if(pos==2)
						{
							textmoxing1.setText(zhijing);
							textmoxing2.setText(shendu);
							textmoxing3.setVisibility(View.INVISIBLE);
							editmoxing3.setVisibility(View.INVISIBLE);
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Another interface callback
					}
				});
	}
	
	public void upload()
	{
		watercap.set_did("1304");		
		watercap.set_address("天津海河口");
		watercap.set_jingdu("120.325259");
		watercap.set_weidu("37.330890");
		watercap.set_batterypower("89");
		watercap.set_watertemper("50");
		watercap.set_cod("34");
		watercap.set_nitratevalue("45");
		watercap.set_ammoniavalue("60");
		watercap.set_flowspeed("60");
		watercap.set_waterdeep("10");
		watercap.set_widther("8");
		watercap.set_uptime("1451976178");
		watercap.set_remark("备注");
		
		JSONObject img_json = new JSONObject();
		listtupian = SysApplication.getInstance().listtupian;
		if(null!=listtupian)
		{
		for(int i=0;i<listtupian.size();i++)
		{
			String tupianPath = (String) listtupian.get(i);
			byte[] buffer = {0};
			try {
				buffer = readFile(tupianPath);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			byte[] bufferencodebase = Base64.encode(buffer, Base64.DEFAULT);
			//listtupianContent.add(bufferencodebase);
			//writeFile(tupianPath+".base", bufferencodebase);
			
			JSONObject img_jsontmp = new JSONObject();
			try {
				img_jsontmp.put("imgext", ".jpg");
			
			String jpg_str=null;
			jpg_str=new String(bufferencodebase);
			img_jsontmp.put("imgcon", jpg_str);
			img_json.put("img"+String.valueOf(i+1), img_jsontmp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		watercap.set_img(String.valueOf(listtupian.size() ), img_json.toString());
		}

		
        /*
		String img=new String("{\"img1\":{\"imgcon\":\"");
		String img1=img+jpg1_str;
		img=new String("{\",\"imgext\":\".jpg\"},\"img2\":{\"imgcon\":\"");
		String img2=img1+img+jpg2_str;
		img=new String("{\",\"imgext\":\".jpg\"},\"img3\":{\"imgcon\":\"");
		String img3=img2+img+jpg3_str;
		img=new String("{\",\"imgext\":\".jpg\"}}");
		String img4=img3+img;
		Log.i("img4", img4);
		watercap.set_img("3", img4);
		*/
		//watercap.set_img("3", "{\"img1\":{\"imgcon\":\"base164_encode(\"imgurl\")\",\"imgext\":\".jpg\"},\"img2\":{\"imgcon\":\"base164_encode(\"imgurl\")\",\"imgext\":\".jpg\"},\"img3\":{\"imgcon\":\"base164_encode(\"imgurl\")\",\"imgext\":\".jpg\"}}");
		//watercap.set_img("3","{\"img1\":{\"imgcon\":\"123\",\"imgext\":\".jpg\"},\"img2\":{\"imgcon\":\"456\",\"imgext\":\".jpg\"},\"img3\":{\"imgcon\":\"789\",\"imgext\":\".jpg\"}}");

		watercap.set_model("3", "{\"model1\":{\"modeltype\":1,\"waterwidth\":5,\"downarea\":5,\"waterdeep\":3,\"flowspeed\":3,\"fluxcod\":5,\"fluxnirate\":6,\"fluxammonia\":8},\"model2\":{\"modeltype\":2,\"waterwidth\":3,\"downarea\":4,\"waterdeep\":5,\"flowspeed\":8,\"fluxcod\":12,\"fluxnirate\":16,\"fluxammonia\":18},\"model3\":{\"modeltype\":3,\"waterwidth\":5,\"downarea\":6,\"waterdeep\":8,\"flowspeed\":9,\"fluxcod\":22,\"fluxnirate\":26,\"fluxammonia\":28}}");
		//watercap.sendNet();
		new Thread(runnable).start();  
	}
	
	Runnable runnable = new Runnable(){  
	    @Override  
	    public void run() {  
	        //Message msg = new Message();  
	        //Bundle data = new Bundle();  
	        //data.putString("value","请求结果");  
	        //msg.setData(data);  
	        //handler1.sendMessage(msg);
	        watercap.sendNet();
	    }
	};
	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[128];
					if (mInputStream == null)
						return;

					
					size = mInputStream.read(buffer);
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
	public static String byte2HexStr(byte[] b,int len)    
	{    
	    String stmp="";    
	    StringBuilder sb = new StringBuilder("");    
	    for (int n=0;n<len;n++)    
	    {    
	        stmp = Integer.toHexString(b[n] & 0xFF);    
	        sb.append((stmp.length()==1)? "0"+stmp : stmp);    
	        sb.append(" ");    
	    }    
	    return sb.toString().toUpperCase().trim();    
	}
	protected void onDataReceived(final byte[] buffer, final int size) {
            runOnUiThread(new Runnable() {
                    public void run() {
                            //if (EditTextReception != null) {
                            //	EditTextReception.append(new String(buffer, 0, size));
                            //}
                    		String ret=byte2HexStr(buffer,size);
							//Log.i("485",size + "==>"+ret);
							if(header_got && to_read>0)
							{	
								int cur=read_len-to_read;
								//Log.i("Response", String.valueOf(response.length));
								for(int j=0;j<to_read;j++)
								response[j+cur]=(byte) (buffer[j] & 0xFF);
								read_len=0;
								to_read=0;
								header_got=false;
								byte[] by={0,0,0,0};
								for(int i=0;i<4;i++)
									by[i]=response[12+i];
								Log.i("Response", byte2HexStr(response,28));
								codvalue=byte2HexStr(by,4);
								handler.post(updatevalue);
							}
							else
							{
								int index=0,i=0;
								for(i=0;i<size;i++)
								{
									//Log.i("Response==>",Integer.toHexString(buffer[i]));
									if((buffer[i]&0xff)==0xF5 && (buffer[i+1]&0xff)==0x31)
									{								
										header_got=true;
										read_len=Integer.valueOf(Integer.toHexString(buffer[i+2]&0xff)).intValue();									
										index=i+3;
										break;
									}
								}
								//Log.i("Response", "Size "+String.valueOf(size)+" i "+String.valueOf(i));
								if(i!=size)
								{
									if(read_len>size-3)
									{
										for(i=0;i<size;i++)
											response[i]=(byte) (buffer[i+index]&0xff);
										to_read=read_len-(size-index);
										//Log.i("to_read", String.valueOf(to_read));
										//Log.i("read_len", String.valueOf(read_len));
									}
									else
									{
										for(i=0;i<read_len;i++)
											response[i]=(byte) (buffer[i+index]&0xff);
										read_len=0;
										to_read=0;
										header_got=false;
										//Log.i("Response2", byte2HexStr(response,28));
									}
								}
							}
                    }
            });
    }
	public void send_485()
	{
		try {
			mOutputStream.write(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void Init()
	{
		try {
			watercap.init(this);
			mInputStream = watercap.getInputStream();
			mOutputStream = watercap.getOutputStream();
			/* Create a receiving thread */
			mReadThread = new ReadThread();
			mReadThread.start();
			//mOutputStream.write(cmd);
			System.out.println("串口数据发送成功");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private final Runnable updatevalue = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			editCOD.setText(codvalue);

		}
	};

}
