package com.project.water;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.example.watercap.watercap;

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CameraActivity extends Activity {
	public String workpath = "/mnt/sdcard";
	
	public int delay = 1000;
	GPSProvider gpsprovider;
	private final Handler handler = new Handler();
	Boolean auto = true;
	
	Button buttonshanchutupian;
	Button buttonpai;
	Button buttonfanhui;
	Button buttontuichu;
	Button buttonguanji;
	Spinner spinnertupian;
	
	TextView textdianchi;
	ImageView imageviewdianchi;
	TextView textGPSLocate;
	TextView textGSMdetails;
	TextView texttimedetail;
	TextView textID;
	
	String user;
	Bundle bundle;
	
	TelephonyManager        Tel;
	MyPhoneStateListener    MyListener;
	
	ImageView signalstrength;
	Context g_ctx=null;
	List listtupian;
	private ImageView showImage;
	private Uri imageUri; // 图片路径
	private Uri imageUritmp; // 图片路径
	private String filename; // 图片名称
	File outputImage;
	File outputImagetmp;
	SharedPreferencesDatabase sharedPreferenceDatabase;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paizhao);
		buttonpai = (Button) findViewById(R.id.buttonpaishe);
		showImage = (ImageView) findViewById(R.id.paizhao);
		buttonshanchutupian = (Button) findViewById(R.id.buttonshanchutupian);
		buttonfanhui = (Button) findViewById(R.id.buttonfanhui);
		buttontuichu = (Button) findViewById(R.id.buttontuichu);
		buttonguanji = (Button) findViewById(R.id.buttonguanji);
		spinnertupian = (Spinner) findViewById(R.id.spinnertupian);
		
		imageviewdianchi = (ImageView) findViewById(R.id.imageviewdianchi);
		//textdianchi = (TextView) findViewById(R.id.textdianchi);
		textGPSLocate = (TextView) findViewById(R.id.textGPSLocate);
		textGSMdetails = (TextView) findViewById(R.id.textGSMdetails);
		signalstrength = (ImageView) findViewById(R.id.imageviewsignalstrength);
		texttimedetail = (TextView) findViewById(R.id.texttimedetail);
		textID = (TextView) findViewById(R.id.textID);
		
		listtupian = new ArrayList();
		listtupian = GetVideoFileName(workpath);
		setspinnershanchu();
		setbuttonshanchu();
		setbuttonpai();
		setButtonfanhui();
		setbuttontuichu();
		setbuttonguanji();
		getbundledetail();
		g_ctx=(Context)this;
		sharedPreferenceDatabase = new SharedPreferencesDatabase();
		batteryLevel();
		SysApplication.getInstance().addActivity(this); 
		gpsprovider = GPSProvider.getInstance(CameraActivity.this);
		getdeviceinfo();
		setID();
		getsignalstrength();
		
		SysApplication.getInstance().cameraactivity = this;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	    switch(requestCode) {
	    case 1:
	    	if(resultCode==-1)
	    	{
	    		startPhotoZoom(imageUri, imageUritmp);
	    	}
	    	else
	    	{
	    		deletefile(imageUri.toString().replaceAll("file://", ""));
	    		deletefile(imageUritmp.toString().replaceAll("file://", ""));
	    	}
	        break;
	    case 2:
	        try {    
	            //图片解析成Bitmap对象
	            Bitmap bitmap = BitmapFactory.decodeStream(
	                    getContentResolver().openInputStream(imageUritmp));
	            //Toast.makeText(CameraActivity.this, imageUritmp.toString(), Toast.LENGTH_SHORT).show();
	            deletefile(outputImage.toString());
	            listtupian.add(imageUritmp.toString().replaceAll("file://", ""));
	            setspinnershanchu();
	            spinnertupian.setSelection(spinnertupian.getCount()-1);
	            showImage.setImageBitmap(bitmap); //将剪裁后照片显示出来
	        } catch(FileNotFoundException e) {
	            e.printStackTrace();
	        }
	        break;
	    default:
	        break;
	    }
	}
	
	/**
	    * 裁剪图片
	    *
	    * @param uri
	    */
	    public void startPhotoZoom(Uri uri, Uri uritmp) {
	        Intent intent = new Intent("com.android.camera.action.CROP");
	        intent.setDataAndType(uri, "image/*");
	        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
	        /*intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
	        intent.putExtra("aspectY", 1);// x:y=1:1
	        intent.putExtra("outputX", 200);//图片输出大小
	        intent.putExtra("outputY", 200);*/
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritmp);
	        startActivityForResult(intent, 2);
	    }
	    
	    public void deletefile(String filestr)
	    {
	    	File file = new File(filestr);
	    	if (file.exists()) {
	    		file.delete();
			}
	    }
	    public void setspinnershanchu() {
			// 建立Adapter并且绑定数据源
			String[] mItems = new String[listtupian.size()];
			Map<String, String> map = new HashMap<String, String>();

			for (int i = 0; i < listtupian.size(); i++) {
				mItems[i] = (String)listtupian.get(i);
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					R.layout.spinner, mItems);
			adapter.setDropDownViewResource(R.layout.spinner);
			// 绑定 Adapter到控件
			spinnertupian.setAdapter(adapter);
			spinnertupian
					.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int pos, long id) {
							outputImagetmp = new File((String)listtupian.get(pos));
							imageUritmp = Uri.fromFile(outputImagetmp);
							try {
								Bitmap bitmap = BitmapFactory.decodeStream(
								        getContentResolver().openInputStream(imageUritmp));
								showImage.setImageBitmap(bitmap); //将剪裁后照片显示出来
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {
							// Another interface callback
						}
					});
		}
	    public void setbuttonpai()
	    {
	    	buttonpai.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// 图片名称 时间命名
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
					Date date = new Date(System.currentTimeMillis());
					filename = format.format(date);
					// 创建File对象用于存储拍照的图片 SD卡根目录
					// File outputImage = new
					// File(Environment.getExternalStorageDirectory(),test.jpg);
					// 存储至DCIM文件夹
					File path = Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
					String strpath = workpath;
					outputImage = new File(strpath, filename + ".jpg");
					outputImagetmp = new File(strpath, filename + "new.jpg");
					try {
						if (outputImage.exists()) {
							outputImage.delete();
						}
						if (outputImagetmp.exists()) {
							outputImagetmp.delete();
						}
						outputImage.createNewFile();
						outputImagetmp.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
					imageUri = Uri.fromFile(outputImage);
					imageUritmp = Uri.fromFile(outputImagetmp);
					// 将File对象转换为Uri并启动照相程序
					Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); // 照相
					intent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(outputImage)); // 指定图片输出地址
					startActivityForResult(intent, 1); // 启动照相
					// 拍完照startActivityForResult() 结果返回onActivityResult()函数
				}
			});
	    }
	    public void setbuttonshanchu() {
	    	buttonshanchutupian.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int i = spinnertupian.getSelectedItemPosition();
					if(i<0)
						return;
					
					String tmp = (String)listtupian.get(i);
					if(tmp==null)
						return;
					
					deletefile(tmp);
					listtupian.remove(i);
					setspinnershanchu();
				}

			});
		}
	    
	    public void deletetupian()
	    {
	    	for(int i = spinnertupian.getCount()-1;i>=0;i--)
	    	{
		
			String tmp = (String)listtupian.get(i);
			if(tmp==null)
				return;
			
			deletefile(tmp);
			listtupian.remove(i);
	    	}
	    }
	    public void setButtonfanhui() {
	    	buttonfanhui.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					backpressed();
				}

			});
		}
	    public void setbuttontuichu() {
	    	buttontuichu.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SysApplication.getInstance().exit();
				}

			});
		}
	    public void setbuttonguanji() {
	    	buttonguanji.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Log.i("Power Off", "to close system");
					if(watercap.getPacket()!=null)
						try {
							sharedPreferenceDatabase.Setshuju(g_ctx,watercap.getPacket());
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					Toast.makeText(g_ctx, "请关闭电源",
							 Toast.LENGTH_LONG).show();
				}

			});
		}
	    @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
	    	if(keyCode==KeyEvent.KEYCODE_BACK)
	    	{
	    		backpressed();
	    	}
			return super.onKeyDown(keyCode, event);
		}
	    public void backpressed()
	    {
	    	Intent intent = new Intent();
    		intent.setClass(CameraActivity.this, MainOperationActivity.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    		SysApplication.getInstance().listtupian = listtupian;
    		startActivity(intent);
	    }
	    private void setID() {
			
			textID.setText(user);
		}
		public List GetVideoFileName(String fileAbsolutePath) {
	        List listfile = new ArrayList();
	        File file = new File(fileAbsolutePath);
	        File[] subFile = file.listFiles();
	 
	        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
	            // 判断是否为文件夹
	            if (!subFile[iFileLength].isDirectory()) {
	                String filename = subFile[iFileLength].getName();
	                // 判断是否为MP4结尾
	                if (filename.trim().toLowerCase().endsWith(".jpg")) {
	                	String tmp = fileAbsolutePath+"/"+filename;
	                	listfile.add(tmp);
	                }
	            }
	        }
	        return listfile;
	    }
		private void getbundledetail()
		{
			bundle = this.getIntent().getExtras();
			user = (String) bundle.get("user");
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

			if (gprs == State.CONNECTED || gprs == State.CONNECTING) {
				// Toast.makeText(this, "wifi is open! gprs",
				// Toast.LENGTH_SHORT).show();
				return true;
			}

			return false;

		}
		
		private void getsignalstrength()
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
							R.drawable.xinhao1));
				} else if (5 > signel) {
					signalstrength.setBackground(getResources().getDrawable(
							R.drawable.xinhao2));
				} else if (8 > signel) {
					signalstrength.setBackground(getResources().getDrawable(
							R.drawable.xinhao3));
				} else if (12 > signel) {
					signalstrength.setBackground(getResources().getDrawable(
							R.drawable.xinhao4));
				} else  {
					signalstrength.setBackground(getResources().getDrawable(
							R.drawable.xinhao5));
				}

				
	      }

	    };

}
