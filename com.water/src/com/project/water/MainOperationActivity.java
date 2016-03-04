package com.project.water;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
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
import android.text.Editable;
import android.text.TextWatcher;
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
	Boolean auto_mode = false;
	String user;
	int modexuanze;
	int zidongjiange;
	Bundle bundle;
	
	TelephonyManager        Tel;
	MyPhoneStateListener    MyListener;
	
	ImageView signalstrength;
	
	String stringmodexuanze;
	String modexuanzecanshua;
	String modexuanzecanshub;
	String modexuanzecanshuc;
	String modexuanzecanshud;
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
	Button buttonguanji;

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
	
	EditText editCODtongliang;
	EditText editxiaodantongliang;
	EditText editandantongliang;
	
	TextView textcishutime;
	
	String show;
	
	
	Button buttonceliang;
	Button buttonshujucunchu;
	Button buttontongliangjisuan;
	Button buttonshujushangchuan;
	SharedPreferencesDatabase sharedPreferenceDatabase1;
	Button buttonCODLingdian;
	Button buttonxiaodanLingdian;
	SharedPreferencesDatabase sharedPreferenceDatabase;
	Context g_ctx=null;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	byte[] response = new byte[32];
	static boolean header_got=false;
	static int read_len=0,to_read=0;
	byte[] cmd={0x24,0x32,(byte)0xff,0x23,0x0a};
	int cnt=0,bak_cnt=0;
	String mode_string=null;
	String mode_string_bak=null;
	String img_string=null;
	String temparture=null;
	float[] wight=new float[10];
	float[] avg_cod=new float[1024];
	float[] avg_no3n=new float[1024];
	float[] avg_nh4n=new float[1024];
	float cod_wight=0;
	float no3n_wight=0;
	float nh4n_wight=0;
	float cod_zero = 0;
	float no3n_zero =0 ;
	float cod_a=0,cod_b=0;
	float no3n_c=0,no3n_d=0;
	float cur_cod=0,cur_no3n=0,cur_nh4n=0,cur_deep=0,cur_speed=0,cur_distance=0,cur_power=0,cur_shuiwen=0;
	float[] shuiwen=new float[1024],xiadi=new float[1024],cod=new float[1024],no3n=new float[1024],nh4n=new float[1024],deep=new float[1024],speed=new float[1024],distance=new float[1024],power=new float[1024];
	String set_model_param(
			String cod,String nho3,String nhn4,
			String avg_cod1,String avg_nho3,String avg_nhn4,
			String speed,String deep,String modeltype,
			String waterwidth,String downarea,String cnt,String mode_string
			)
	{
		JSONObject mode_json1 = new JSONObject();
		JSONObject mode_json = new JSONObject();
		try {
			mode_json1.put("modeltype", modeltype);
			mode_json1.put("waterwidth", waterwidth);
			mode_json1.put("downarea", downarea);
			mode_json1.put("waterdeep", deep);
			mode_json1.put("flowspeed", speed);
			mode_json1.put("fluxcod", avg_cod1);
			mode_json1.put("fluxnirate", avg_nho3);
			mode_json1.put("fluxammonia", avg_nhn4);
			mode_json.put("model"+cnt,mode_json1);
		} catch (JSONException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
			Log.i("ERROR", "insert model_param failed");
		}
		//String tmp=mode_json.toString().substring(1, mode_json.toString().length()-1);
		if(mode_string==null)
			mode_string=mode_json.toString().substring(1, mode_json.toString().length()-1);
		else
			mode_string+=","+mode_json.toString().substring(1, mode_json.toString().length()-1);
		Log.i("mode_string",mode_string);
		return mode_string;
	}
	String set_img_param(String img,String cnt,String img_string)
	{
		JSONObject img_json = new JSONObject();
		JSONObject img_json1 = new JSONObject();
		try {
			img_json1.put("imgext", ".jpg");
			img_json1.put("imgcon", img);
			img_json.put("img"+cnt, img_json1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			Log.i("ERROR", "insert img_param failed");
		}
		if(img_string==null)
			img_string=img_json.toString().substring(1, img_json.toString().length()-1);
		else
			img_string+=","+img_json.toString().substring(1, img_json.toString().length()-1);
		//Log.i("img_string",img_string);
		return img_string;
	}
	void set_wight(int times)
	{
		switch (times)
		{
			case 1:
				wight[0]=(float) 1.0;
				break;
			case 2:
				wight[0]=(float) 0.5;wight[1]=(float) 0.5;
				break;
			case 3:
				wight[0]=(float) 0.28;wight[1]=(float) 0.44;wight[2]=(float) 0.28;
				break;	
			case 4:
				wight[0]=(float) 0.19;wight[1]=(float) 0.31;wight[2]=(float) 0.31;wight[3]=(float) 0.19;
				break;
			case 5:
				wight[0]=(float) 0.14;wight[1]=(float) 0.24;wight[2]=(float) 0.24;wight[3]=(float) 0.24;wight[4]=(float)0.14;
				break;
			case 6:
				wight[0]=(float) 0.14;wight[1]=(float) 0.18;wight[2]=(float) 0.18;wight[3]=(float) 0.18;wight[4]=(float)0.18;wight[5]=(float)0.14;
				break;
			case 7:
				wight[0]=(float) 0.125;wight[1]=(float) 0.15;wight[2]=(float) 0.15;wight[3]=(float) 0.15;wight[4]=(float)0.15;wight[5]=(float)0.15;wight[6]=(float)0.125;
				break;
			case 8:
				wight[0]=(float) 0.055;wight[1]=(float) 0.145;wight[2]=(float) 0.15;wight[3]=(float) 0.15;wight[4]=(float)0.15;wight[5]=(float)0.15;wight[6]=(float)0.145;
				wight[7]=(float)0x055;
				break;
			case 9:
				wight[0]=(float) 0.09;wight[1]=(float) 0.11;wight[2]=(float) 0.12;wight[3]=(float) 0.12;wight[4]=(float)0.12;wight[5]=(float)0.12;wight[6]=(float)0.12;				
				wight[7]=(float)0.11;wight[8]=(float)0.09;
				break;
			case 10:
				wight[0]=(float) 0.07;wight[1]=(float) 0.10;wight[2]=(float) 0.11;wight[3]=(float) 0.11;wight[4]=(float)0.11;wight[5]=(float)0.11;wight[6]=(float)0.11;				
				wight[7]=(float)0.11;wight[8]=(float)0.01;wight[9]=(float)0.07;
				break;
		}
	}
	void send_net_work(String power,String distance,String speed,String deep,String cod,String nho3,String nhn4)
	{
		//String img=set_img_param(jpg1_str,"1",null);
		//img=set_img_param(jpg2_str,"2",img);
		//img=set_img_param(jpg3_str,"3",img);
		watercap.set_did("1000");		
		watercap.set_address("天津海河口");
		watercap.set_jingdu("120.325259");
		watercap.set_weidu("37.330890");
		watercap.set_batterypower(power);
		watercap.set_watertemper("23");
		watercap.set_widther(distance);
		watercap.set_uptime("1451976178");
		watercap.set_remark("备注");
		watercap.set_cod(cod);
		watercap.set_nitratevalue(nho3);
		watercap.set_ammoniavalue(nhn4);
		watercap.set_flowspeed(speed);
		watercap.set_waterdeep(deep);
		//watercap.set_img("2", img);
		//watercap.set_model(String.valueOf(cnt), mode_string);
		new Thread(runnable).start();  		
	}
	void count_juxing_tl(int times,int type)
	{
		float re=0;
		if(type==0)
		avg_cod[times]=cod[times]*speed[times]*deep[times]*distance[times];
		else if(type==1)
		avg_no3n[times]=no3n[times]*speed[times]*deep[times]*distance[times];
		else
		avg_nh4n[times]=nh4n[times]*speed[times]*deep[times]*distance[times];
		for(int i=0;i<times+1;i++)
		{
			if(type==0)
			{
				re+=avg_cod[i];
				cod_wight+=cod[i];
			}
			else if(type==1)
			{
				re+=avg_no3n[i];
				no3n_wight+=no3n[i];
			}
			else
			{
				re+=avg_nh4n[i];
				nh4n_wight+=nh4n[i];
			}
		}
		
		if(type==0)
		{
			avg_cod[times]=re/(times+1);
			cod_wight=cod_wight/(times+1);
		}
		else if(type==1)
		{
			avg_no3n[times]=re/(times+1);
			no3n_wight=no3n_wight/(times+1);
		}
		else
		{
			avg_nh4n[times]=re/(times+1);
			nh4n_wight=nh4n_wight/(times+1);
		}
		if(type==0)
		Log.i("avg_cod",String.format("%6.3f",avg_cod[times]));
		else if(type==1)
		Log.i("avg_no3n",String.format("%6.3f",avg_no3n[times]));
		else
		Log.i("avg_nh4n",String.format("%6.3f",avg_nh4n[times]));
	}
	void count_tixing_tl(float up,float down,int times,int type)
	{
		float re=0;
		int i=0;
		set_wight(times+1);
		for(i=0;i<times+1;i++)
		{
			if(type==0)
			{
				re+=cod[i]*wight[i];
				Log.i("CALC 平均COD", String.valueOf(wight[i])+"*"+String.valueOf(cod[i])+"="+String.valueOf(re));
			}
			else if(type==1)
			{
				re+=no3n[i]*wight[i];
				Log.i("CALC 平均NO3n", String.valueOf(wight[i])+"*"+String.valueOf(no3n[i])+"="+String.valueOf(re));
			}
			else
			{
				re+=nh4n[i]*wight[i];
				Log.i("CALC 平均COD", String.valueOf(wight[i])+"*"+String.valueOf(nh4n[i])+"="+String.valueOf(re));
			}
		}
		Log.i("CALC 面积", String.valueOf(deep[times]*(up+down)/(float)2.0));
		Log.i("CALC 流速", String.valueOf(speed[times]));
		if(type==0)
			avg_cod[times]=(re*speed[times]*deep[times]*(up+down))/(float)2.0;
		else if(type==1)
			avg_no3n[times]=(re*speed[times]*deep[times]*(up+down))/(float)2.0;
		else
			avg_nh4n[times]=(re*speed[times]*deep[times]*(up+down))/(float)2.0;
		if(type==0)
		Log.i("CALC COD通量",String.format("%6.3f",avg_cod[times]));
		else if(type==1)
		Log.i("CALC NO3n通量",String.format("%6.3f",avg_no3n[times]));
		else
		Log.i("CALC NH4n通量",String.format("%6.3f",avg_nh4n[times]));
	}
	float count_yuanxing_tl(int times,int type)
	{
		//avg2_cod[cnt] = Math.asin(distance[cnt]/(2*r))*r*r-0.5f*distance[cnt]*(r-deep[cnt]);
		float re=0;
		float r=0;
		int i=0;
		set_wight(times+1);
		for(i=0;i<times+1;i++)
		{
			if(type==0)
			{
				re+=cod[i]*wight[i];
				Log.i("CALC 平均COD", String.valueOf(wight[i])+"*"+String.valueOf(cod[i])+"="+String.valueOf(re));
			}
			else if(type==1)
			{
				re+=no3n[i]*wight[i];
				Log.i("CALC 平均NO3n", String.valueOf(wight[i])+"*"+String.valueOf(no3n[i])+"="+String.valueOf(re));
			}
			else
			{
				re+=nh4n[i]*wight[i];
				Log.i("CALC 平均COD", String.valueOf(wight[i])+"*"+String.valueOf(nh4n[i])+"="+String.valueOf(re));
			}
		}
		r = (distance[times]*distance[times])/(8*deep[times])+deep[times]/2;
		Log.i("CALC 半径", String.valueOf(r));
		Log.i("CALC 面积", String.valueOf(Math.asin(distance[times]/(2*r))*r*r-0.5f*distance[times]*(r-deep[times])));
		Log.i("CALC 流速", String.valueOf(speed[times]));
		//Log.i("半径", "("+String.format("%6.3f", distance[times])+"*"+String.format("%6.3f", distance[times])+")/8*"+String.format("%6.3f",deep[times])+")"+"+"+String.format("%6.3f", deep[times])+"/2");
		//Log.i("半径", String.format("%6.3f",r));
		//Log.i("COD通量", String.format("%6.3f", re)+"*sin("+String.format("%6.3f", distance[times])+"/(2*"+String.format("%6.3f", r)+"))*"+String.format("%6.3f", r)+"*"+String.format("%6.3f", r));
		//Log.i("COD通量", "-0.5*"+String.format("%6.3f", distance[times])+"*("+String.format("%6.3f", r)+"-"+String.format("%6.3f", deep[times])+"))");
		if(type==0)
			avg_cod[times]=(float) (re*speed[times]*(Math.asin(distance[times]/(2*r))*r*r-0.5f*distance[times]*(r-deep[times])));
		else if(type==1)
			avg_no3n[times]=(float) (re*speed[times]*(Math.asin(distance[times]/(2*r))*r*r-0.5f*distance[times]*(r-deep[times])));
		else
			avg_nh4n[times]=(float) (re*speed[times]*(Math.asin(distance[times]/(2*r))*r*r-0.5f*distance[times]*(r-deep[times])));
		
		if(type==0)
		Log.i("CALC COD通量",String.format("%6.3f",avg_cod[times]));
		else if(type==1)
		Log.i("CALC NO3n通量",String.format("%6.3f",avg_no3n[times]));
		else
		Log.i("CALC NH4n通量",String.format("%6.3f",avg_nh4n[times]));
		return r;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainoperation);

		//buttonmoxing = (Button) findViewById(R.id.buttonmoxing);
		spinnermoxing = (Spinner) findViewById(R.id.spinnermoxing);
		buttonpaizhao = (Button) findViewById(R.id.buttonpaizhao);
		buttontuichu = (Button) findViewById(R.id.buttontuichu);
		buttonguanji = (Button) findViewById(R.id.buttonguanji);
		
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
		editCODtongliang = (EditText) findViewById(R.id.editcodtongliang);
		editxiaodantongliang = (EditText) findViewById(R.id.editxiaodantongliang);
		editandantongliang = (EditText) findViewById(R.id.editandantongliang);
		
		g_ctx=(Context)this;
		buttonceliang = (Button) findViewById(R.id.buttonshujuceliang);
		buttonshujucunchu = (Button) findViewById(R.id.buttonshujucunchu);
		buttontongliangjisuan = (Button) findViewById(R.id.buttontongliangjisuan);
		buttonshujushangchuan = (Button) findViewById(R.id.buttonshujushangchuan);
		buttonCODLingdian = (Button) findViewById(R.id.buttonCODLingdian);
		buttonxiaodanLingdian = (Button) findViewById(R.id.buttonxiaodanLingdian);
		sharedPreferenceDatabase = new SharedPreferencesDatabase();
		for(int i=0;i<1024;i++)
		{
			cod[i]=0;no3n[i]=0;nh4n[i]=0;speed[i]=0;deep[i]=0;distance[i]=0;
			avg_cod[i]=0;avg_no3n[i]=0;avg_nh4n[i]=0;
		}
		SharedPreferencesDatabase sharedPreferenceDatabase = new SharedPreferencesDatabase();
		try {
			sharedPreferenceDatabase.DefaultSharedPreferences((Context) this);
			listuser = sharedPreferenceDatabase.GetuserArray((Context) this);
			listmodexuanze = sharedPreferenceDatabase
					.GetmodexuanzeArray((Context) this);
			listceliangfangshi = sharedPreferenceDatabase
					.GetjiangeArray((Context) this);
			
			cod_zero = Float.parseFloat(sharedPreferenceDatabase.GetCODLingdian((Context) this));
			no3n_zero = Float.parseFloat(sharedPreferenceDatabase.GetxiaodanLingdian((Context) this));
			Log.i("CREATE cod zero", String.valueOf(cod_zero));
			Log.i("CREATE no3n zero", String.valueOf(no3n_zero));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setButtonPaizhao();
		setButtontongliang();
		setButtontuichu();
		setButtonguanji();
		setButtonshujushangchuan();
		setButtonceliang();
		setbuttonshujucunchu();
		batteryLevel();
		setbuttontongliangjisuan();
		setButtonxiaodanLingdian();
		setButtonCODLingdian();
		SysApplication.getInstance().addActivity(this);

		zidongjiangeminute = 0;
		setspinnermoxing();
		
		getbundledetail();
		try {
			didvalue = sharedPreferenceDatabase.GetdeviceID(g_ctx);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		gpsprovider = GPSProvider.getInstance(MainOperationActivity.this);
		Init();
		getdeviceinfo();
		setID();
		activateLocation();
		initsignalstrength();
		//opmoxing1("3");
		cod_a=Float.parseFloat(modexuanzecanshua);cod_b=Float.parseFloat(modexuanzecanshub);
		no3n_c=Float.parseFloat(modexuanzecanshuc);no3n_d=Float.parseFloat(modexuanzecanshud);
		opmoxing1("3");
		opdizhi("请输入地址！");
	}
	
	private void setinputchange()
	{
		editCOD.addTextChangedListener(new editCODEditChangedListener());
	}
	
    class editCODEditChangedListener implements TextWatcher {  
        private CharSequence temp;//监听前的文本  
        private int editStart;//光标开始位置  
        private int editEnd;//光标结束位置  
        private final int charMaxNum = 10;  
   
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {  
 
        }  
   
        public void onTextChanged(CharSequence s, int start, int before, int count) {  
  
   
        }  
   
        @Override  
        public void afterTextChanged(Editable s) {  

            /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */  
            /*editStart = editCOD.getSelectionStart();  
            editEnd = editCOD.getSelectionEnd();  
            if (temp.length() > charMaxNum) {  
                Toast.makeText(getApplicationContext(), "你输入的字数已经超过了限制！", Toast.LENGTH_LONG).show();  
                s.delete(editStart - 1, editEnd);  
                int tempSelection = editStart;  
                mEditTextMsg.setText(s);  
                mEditTextMsg.setSelection(tempSelection);  
            }  */
   
        }  
    };  
    
	private String opCOD(String input)
	{
		if(input!=null)
		{
			editCOD.setText(input);
			return null;
		}
		else
		{
			Log.i("input", "cod is null");
			if(editCOD.getText().toString().length()>0)
				return editCOD.getText().toString();
			else
				return "1";
		}
	}
	
	private String opliusu(String input)
	{
		if(input!=null)
		{
			editliusu.setText(input);
			return null;
		}
		
		else
		{
			Log.i("input", "liusu is null");
			if(editCOD.getText().toString().length()>0)
				return editliusu.getText().toString();
			else
				return "1";
		}
	}
	
	private String opxiaodan(String input)
	{
		if(input!=null)
		{
			editxiaodan.setText(input);
			return null;
		}

		else
		{
			Log.i("input", "xiaodan is null");
			if(editCOD.getText().toString().length()>0)
				return editxiaodan.getText().toString();
			else
				return "1";
		}
	}
	
	private String opshendu(String input)
	{
		if(input!=null)
		{
			editshendu.setText(input);
			return null;
		}

		else
		{
			Log.i("input", "shendu is null");
			if(editCOD.getText().toString().length()>0)
				return editshendu.getText().toString();
			else
				return "1";
		}
	}
	
	private String opandan(String input)
	{
		if(input!=null)
		{
			editandan.setText(input);
			return null;
		}

		else
		{
			Log.i("input", "andan is null");
			if(editCOD.getText().toString().length()>0)
				return editandan.getText().toString();
			else
				return "1";
		}
	}
	
	private String opkuandu(String input)
	{
		if(input!=null)
		{
			editkuandu.setText(input);
			return null;
		}

		else
		{
			Log.i("input", "kuandu is null");
			if(editCOD.getText().toString().length()>0)
				return editkuandu.getText().toString();
			else
				return "1";
		}
	}
	
	private String opshuiwen(String input)
	{
		if(input!=null)
		{
			editshuiwen.setText(input);
			return null;
		}

		else
		{
			Log.i("input", "shuiwen is null");
			if(editCOD.getText().toString().length()>0)
				return editshuiwen.getText().toString();
			else
				return "1";
		}
	}
	
	private String opdizhi(String input)
	{
		if(input!=null)
		{
			editdizhi.setText(input);
			return null;
		}

		else
		{
			Log.i("input", "dizhi is null");
			if(editCOD.getText().toString().length()>0)
				return editdizhi.getText().toString();
			else
				return "1";
		}
	}
	
	private String opcishutime(String input)
	{
		if(input!=null)
		{
			textcishutime.setText(input);
			return null;
		}

		else
		{
			if(editCOD.getText().toString().length()>0)
				return textcishutime.getText().toString();
			else
				return "0";
		}
	}
	
	private String opCODtongliang(String input)
	{
		if(input!=null)
		{
			editCODtongliang.setText(input);
			return null;
		}

		else
		{
			if(editCOD.getText().toString().length()>0)
				return editCODtongliang.getText().toString();
			else
				return "1";
		}
	}
	
	private String opxiaodantongliang(String input)
	{
		if(input!=null)
		{
			editxiaodantongliang.setText(input);
			return null;
		}

		else
		{
			if(editCOD.getText().toString().length()>0)
				return editxiaodantongliang.getText().toString();
			else
				return "1";
		}
	}
	
	private String opandantongliang(String input)
	{
		if(input!=null)
		{
			editandantongliang.setText(input);
			return null;
		}

		else
		{
			if(editCOD.getText().toString().length()>0)
				return editandantongliang.getText().toString();
			else
				return "1";
		}
	}
	
	private String opmoxing1(String input)
	{
		//Log.i("moxing1", String.valueOf(Float.parseFloat(editmoxing1.getText().toString())));
		if(input!=null)
		{
			editmoxing1.setText(input);
			return null;
		}

		else
		{
			Log.i("USR", String.valueOf(editCOD.getText().toString().length())+"=>>"+editmoxing1.getText().toString());
			if(editCOD.getText().toString().compareTo("")!=0)
				return editmoxing1.getText().toString();
			else
				return "3";
		}
	}
	
	private String opmoxing2(String input)
	{
		if(input!=null)
		{
			editmoxing2.setText(input);
			return null;
		}

		else
		{
			if(editCOD.getText().toString().length()>0)
				return editmoxing2.getText().toString();
			else
				return "1";
		}
	}
	
	private String opmoxing3(String input)
	{
		if(input!=null)
		{
			editmoxing3.setText(input);
			return null;
		}

		else
		{
			if(editCOD.getText().toString().length()>0)
				return editmoxing3.getText().toString();
			else
				return "1";
		}
	}
	
	private int getmoxing()
	{
		int tmp = 0;
		tmp = spinnermoxing.getSelectedItemPosition();
			return tmp;
		
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
	public void setButtonguanji() {
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
				/*try {
					String cmd= new String("reboot -p");
					Process exeEcho = Runtime.getRuntime().exec("su");
		            exeEcho.getOutputStream().write(cmd.getBytes());
		            exeEcho.getOutputStream().flush();
					//Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});
		            //exeEcho.waitFor();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
			}
		});
	}
	
	
	public void setButtonceliang() {
		buttonceliang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(g_ctx, g_ctx.getString(R.string.shujuceliangtext),
						 Toast.LENGTH_LONG).show();
				//encodetupian();
				send_485();
				
				//add dialog display "cap starting"
			}
		});
	}
	
	public void setbuttonshujucunchu() {
		buttonshujucunchu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//encodetupian();
				if(cur_cod!=0||cur_no3n!=0||cur_nh4n!=0||cur_speed!=0||cur_deep!=0||cur_distance!=0)
				{
					cod[cnt]=Float.parseFloat(opCOD(null));
					no3n[cnt]=Float.parseFloat(opxiaodan(null));
					nh4n[cnt]=Float.parseFloat(opandan(null));
					speed[cnt]=Float.parseFloat(opliusu(null));
					deep[cnt]=Float.parseFloat(opshendu(null));
					distance[cnt]=Float.parseFloat(opkuandu(null));
					power[cnt]=cur_power;
					if(getmoxing()==0)
					{
						if(opmoxing1(null)!=null)
						{
							Log.i("XIADI", opmoxing1(null));
								xiadi[cnt]=Float.parseFloat(opmoxing1(null));
						}
						else
							xiadi[cnt]=0;
						Log.i("STORE xiadi", String.valueOf(xiadi[cnt]));
					}
					Log.i("STORE cod", String.valueOf(cod[cnt]));
					Log.i("STORE no3n", String.valueOf(no3n[cnt]));
					Log.i("STORE nh4n", String.valueOf(nh4n[cnt]));
					Log.i("STORE speed", String.valueOf(speed[cnt]));
					Log.i("STORE deep", String.valueOf(deep[cnt]));
					Log.i("STORE distance", String.valueOf(distance[cnt]));
					Log.i("STORE power", String.valueOf(power[cnt]));
					cnt++;
					opcishutime(String.valueOf(cnt));
				}
				else
				{
					Log.i("ERROR","in store,need click cap first");
					 Toast.makeText(g_ctx, "保存失败，请先点采集按键！",
					 Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	public void setbuttontongliangjisuan() {
		buttontongliangjisuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//encodetupian();
				if(Integer.valueOf(opcishutime(null))>0)
				{
					String type="1";
					distance[cnt-1]=Float.parseFloat(opkuandu(null));
					if(opmoxing1(null)!=null)
					xiadi[cnt-1]=Float.parseFloat(opmoxing1(null));
					else
						xiadi[cnt-1]=0;
					cod[cnt-1]=Float.parseFloat(opCOD(null));
					nh4n[cnt-1]=Float.parseFloat(opandan(null));
					no3n[cnt-1]=Float.parseFloat(opxiaodan(null));
					speed[cnt-1]=Float.parseFloat(opliusu(null));
					deep[cnt-1]=Float.parseFloat(opshendu(null));
					Log.i("tl-COD",String.format("%6.3f",cod[cnt-1]));
					Log.i("tl-NO3-N",String.format("%6.3f",no3n[cnt-1]));
					Log.i("tl-NH4-N",String.format("%6.3f",nh4n[cnt-1]));
					Log.i("tl-Deep",String.format("%6.3f",deep[cnt-1]));
					Log.i("tl-Speed",String.format("%6.3f",speed[cnt-1]));
					Log.i("tl-Distance",String.format("%6.3f",distance[cnt-1]));
					Log.i("tl-Power",String.format("%6.3f",power[cnt-1]));
					bak_cnt=cnt;
					int i=cnt-1;
					//for(int i=0;i<bak_cnt;i++)
					{
						if(getmoxing()==0)
						{					
							count_tixing_tl(distance[i],xiadi[i],i,0);
							count_tixing_tl(distance[i],xiadi[i],i,1);
							count_tixing_tl(distance[i],xiadi[i],i,2);
							type = "2";
						}
						else if(getmoxing()==1)
						{
							count_juxing_tl(i,0);
							count_juxing_tl(i,1);
							count_juxing_tl(i,2);
							type = "1";
						}
						else
						{
							count_yuanxing_tl(i,0);
							count_yuanxing_tl(i,1);
							count_yuanxing_tl(i,2);
							type = "3";
						}
						mode_string = set_model_param(
							String.format("%6.6f",cod[i]),String.format("%6.6f",no3n[i]),String.format("%6.6f",nh4n[i]),
							String.format("%6.6f",avg_cod[i]),String.format("%6.6f",avg_no3n[i]),String.format("%6.6f",avg_nh4n[i]),
							String.format("%6.6f",speed[i]),String.format("%6.6f",deep[i]),type,
							String.format("%6.6f",distance[i]),String.format("%6.6f",xiadi[i]),String.valueOf(bak_cnt+1),mode_string);
						watercap.set_model(String.valueOf(++bak_cnt), mode_string);
					}
					for(i=0;i<1024;i++)
					{
						cod[i]=0;no3n[i]=0;nh4n[i]=0;speed[i]=0;deep[i]=0;distance[i]=0;
					}
					handler.post(updatetl);
					Log.i("tl-avg-COD",String.format("%6.3f",avg_cod[cnt-1]));
					Log.i("tl-avg-NO3-N",String.format("%6.3f",avg_no3n[cnt-1]));
					Log.i("tl-avg-NH4-N",String.format("%6.3f",avg_nh4n[cnt-1]));
					mode_string=null;
					cnt=0;
					opcishutime("0");
				}
				else
				{
					Log.i("ERROR","in calc,need click cap ,store first");
					 Toast.makeText(g_ctx, "通量计算失败，请先点采集存储按键！",
					 Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	public void setButtonshujushangchuan() {
		buttonshujushangchuan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//encodetupian();
				if(avg_no3n[0]!=0 || avg_cod[0]!=0 || avg_nh4n[0]!=0)
				{
					upload();
					//SysApplication.getInstance().cameraactivity.deletetupian();
				}

				else
				{
					Log.i("ERROR","in upload,need click cap ,store,calc first");
					 Toast.makeText(g_ctx, "上传失败，请先点采集存储计算按键！",
					 Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	public void setButtonCODLingdian() {
		buttonCODLingdian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				try {
					sharedPreferenceDatabase.SetCODLingdian(g_ctx, editCOD.getText().toString());
					cod_zero = Float.parseFloat(editCOD.getText().toString());
					editCOD.setText("0");
					Log.i("ZERO Cod", String.valueOf(cod_zero));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public void setButtonxiaodanLingdian() {
		buttonxiaodanLingdian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					sharedPreferenceDatabase.SetxiaodanLingdian(g_ctx, editxiaodan.getText().toString());
					no3n_zero = Float.parseFloat(editxiaodan.getText().toString());
					editxiaodan.setText("0");
					Log.i("ZERO no3n", String.valueOf(no3n_zero));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			return true;
			//backpressed();
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
		SysApplication.getInstance().deleteActivity(this);
		this.finish();
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
	public void showtishi(String tmp)
	{
		Toast.makeText(g_ctx, tmp,
				 Toast.LENGTH_LONG).show();
	}
	private final Runnable showmessagetask = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			((MainOperationActivity) g_ctx).showtishi(show);

			}

	};
	
	private final Runnable jiangetask = new Runnable() {

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

				//try {
					send_485();
				//} catch (InterruptedException e) {
					// TODO Auto-generated catch block
				//	e.printStackTrace();
				//}
				int interval = delay*60*zidongjiangeminute;
				handler.postDelayed(this, interval);

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
						R.drawable.xinhao1));
			} else if (5 > signel) {
				signalstrength.setBackground(getResources().getDrawable(
						R.drawable.xinhao2));
				new Thread(runnable_resend).start();
			} else if (8 > signel) {
				signalstrength.setBackground(getResources().getDrawable(
						R.drawable.xinhao3));
				new Thread(runnable_resend).start();
			} else if (12 > signel) {
				signalstrength.setBackground(getResources().getDrawable(
						R.drawable.xinhao4));
				new Thread(runnable_resend).start();
			} else  {
				signalstrength.setBackground(getResources().getDrawable(
						R.drawable.xinhao5));
				new Thread(runnable_resend).start();
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
		modexuanzecanshua = map1.get(SharedPreferencesDatabase.modexuanzecanshu0);
		modexuanzecanshub = map1.get(SharedPreferencesDatabase.modexuanzecanshu1);
		
		modexuanzecanshuc = map1.get(SharedPreferencesDatabase.modexuanzecanshu2);
		modexuanzecanshud = map1.get(SharedPreferencesDatabase.modexuanzecanshu3);
		
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
		autorun();
		}
	}
	private void autorun()
	{
		int interval = delay*60*zidongjiangeminute;
		auto_mode=true;
		handler.postDelayed(jiangetask, interval);
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
							textmoxing1.setText(xiadi);
							textmoxing2.setText(shangdi);
							textmoxing3.setText(shendu);
							textmoxing1.setVisibility(View.VISIBLE);
							editmoxing1.setVisibility(View.VISIBLE);
							textmoxing2.setVisibility(View.INVISIBLE);
							editmoxing2.setVisibility(View.INVISIBLE);
							textmoxing3.setVisibility(View.INVISIBLE);
							editmoxing3.setVisibility(View.INVISIBLE);
						}
						else if(pos==1)
						{
							textmoxing1.setText(changdu);
							textmoxing2.setText(kuandu);
							textmoxing1.setVisibility(View.INVISIBLE);
							editmoxing1.setVisibility(View.INVISIBLE);
						}
						else if(pos==2)
						{
							textmoxing1.setText(zhijing);
							textmoxing2.setText(shendu);
							textmoxing1.setVisibility(View.INVISIBLE);
							editmoxing1.setVisibility(View.INVISIBLE);
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Another interface callback
					}
				});
	}
	public void auto_process() throws InterruptedException
	{
		show="自动测量-采集中...";
    	handler.post(showmessagetask);
    	String type="1";
		//send_485();
    	//new Thread(send_485_thread).start();  
		//Log.i("WAIT", "Begin to wait");
		//Thread.sleep(50000);
		//Log.i("WAIT", "End to wait");
		//cod[0]=cur_cod;no3n[0]=cur_cod;nh4n[0]=cur_nh4n;
    	power[0]=cur_power;
		distance[0]=Float.parseFloat(opkuandu(null));
		if(opmoxing1(null)!=null)
		xiadi[0]=Float.parseFloat(opmoxing1(null));
		else
			xiadi[0]=0;
		cod[0]=Float.parseFloat(opCOD(null));
		nh4n[0]=Float.parseFloat(opandan(null));
		no3n[0]=Float.parseFloat(opxiaodan(null));
		speed[0]=Float.parseFloat(opliusu(null));
		deep[0]=Float.parseFloat(opshendu(null));
		shuiwen[0]=Float.parseFloat(opshuiwen(null));
		
		Log.i("auto tl-COD",String.format("%6.3f",cod[0]));
		Log.i("auto tl-NO3-N",String.format("%6.3f",no3n[0]));
		Log.i("auto tl-NH4-N",String.format("%6.3f",nh4n[0]));
		Log.i("auto tl-Deep",String.format("%6.3f",deep[0]));
		Log.i("auto tl-Speed",String.format("%6.3f",speed[0]));
		Log.i("auto tl-Distance",String.format("%6.3f",distance[0]));
		Log.i("auto tl-Power",String.format("%6.3f",power[0]));
		Log.i("auto tl-shuiwen",String.format("%6.3f",shuiwen[0]));
		show="自动测量-通量计算中...";
    	handler.post(showmessagetask);
    	
    	if(getmoxing()==0)
		{					
			count_tixing_tl(distance[0],xiadi[0],0,0);
			count_tixing_tl(distance[0],xiadi[0],0,1);
			count_tixing_tl(distance[0],xiadi[0],0,2);
			type = "2";
		}
		else if(getmoxing()==1)
		{
			count_juxing_tl(0,0);
			count_juxing_tl(0,1);
			count_juxing_tl(0,2);
			type = "1";
		}
		else
		{
			count_yuanxing_tl(0,0);
			count_yuanxing_tl(0,1);
			count_yuanxing_tl(0,2);
			type = "3";
		}
		mode_string = set_model_param(
			String.format("%6.6f",cod[0]),String.format("%6.6f",no3n[0]),String.format("%6.6f",nh4n[0]),
			String.format("%6.6f",avg_cod[0]),String.format("%6.6f",avg_no3n[0]),String.format("%6.6f",avg_nh4n[0]),
			String.format("%6.6f",speed[0]),String.format("%6.6f",deep[0]),type,
			String.format("%6.6f",distance[0]),String.format("%6.6f",xiadi[0]),"1",null);
		watercap.set_model("1", mode_string);
		Log.i("auto {}mode", mode_string);
		mode_string=null;
		bak_cnt=1;
		handler.post(updatetl);
		show="自动测量-上传中...";
    	handler.post(showmessagetask);
		//upload();
		Log.i("AUTO","auto process done.");
	}
	public void upload()
	{
		distance[bak_cnt-1]=Float.parseFloat(opkuandu(null));
		if(opmoxing1(null)!=null)
		xiadi[bak_cnt-1]=Float.parseFloat(opmoxing1(null));
		else
			xiadi[bak_cnt-1]=0;
		cod[bak_cnt-1]=Float.parseFloat(opCOD(null));
		nh4n[bak_cnt-1]=Float.parseFloat(opandan(null));
		no3n[bak_cnt-1]=Float.parseFloat(opxiaodan(null));
		speed[bak_cnt-1]=Float.parseFloat(opliusu(null));
		deep[bak_cnt-1]=Float.parseFloat(opshendu(null));
		temparture=opshuiwen(null);
		watercap.set_did(didvalue);
		watercap.set_address(opdizhi(null));
		watercap.set_jingdu(gpsprovider.getlongtitude());
		watercap.set_weidu(gpsprovider.getlatitude());
		watercap.set_batterypower(String.format("%6.6f", power[bak_cnt-1]));
		watercap.set_watertemper(temparture);
		watercap.set_cod(String.format("%6.6f", cod[bak_cnt-1]));
		watercap.set_nitratevalue(String.format("%6.6f", no3n[bak_cnt-1]));
		watercap.set_ammoniavalue(String.format("%6.6f", nh4n[bak_cnt-1]));
		watercap.set_flowspeed(String.format("%6.6f", speed[bak_cnt-1]));
		watercap.set_waterdeep(String.format("%6.6f", deep[bak_cnt-1]));
		watercap.set_widther(String.format("%6.6f", distance[bak_cnt-1]));
		//SimpleDateFormat sDateFormat = new SimpleDateFormat("yy:mm:dd hh:mm:ss");
		//String date = sDateFormat.format(new java.util.Date());
		Date d=new Date(System.currentTimeMillis());		
		watercap.set_uptime(String.valueOf(d.getTime()).substring(0, String.valueOf(d.getTime()).length()-3));
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
				String jpg_str=null;
				jpg_str=new String(bufferencodebase);
				img_string = set_img_param(jpg_str,String.valueOf(i+1),img_string);
			}
		
			watercap.set_img(String.valueOf(listtupian.size() ), img_string);
		}
		//Log.i("UPLOAD", String.format("%6.6f", cod[bak_cnt-1]));
		//if(watercap.getPacket()!=null)
		//	Log.i("UPLOAD", watercap.getPacket());
		//else
		//	Log.i("UPLOAD","packet is null");
		img_string=null;
		new Thread(runnable).start();  
	}
	public void reSend()
	{
		int count = sharedPreferenceDatabase.GetshujuCount(g_ctx);
		Log.i("RESEND", "count is"+String.valueOf(count));
		if(count!=0)
		{
			for(int i=0;i<count;i++)
			{
				String packet=sharedPreferenceDatabase.Getshuju(g_ctx, i);
				if(packet.compareTo("{}")!=0)
				{
					Log.i("RESEND", "resend "+String.valueOf(i));
					if(watercap.reSendNet(packet))
						sharedPreferenceDatabase.Deleteshuju(g_ctx, i);
				}
			}
		}
	}
	Runnable send_485_thread = new Runnable(){  
	    @Override  
	    public void run() {
	    	send_485();
	    }
	    };
    Runnable runnable_resend = new Runnable(){  
		    @Override  
		    public void run() {
		    	reSend();
		    }
	    };
	Runnable runnable = new Runnable(){  
	    @Override  
	    public void run() {
	    	String packet=watercap.getPacket();
	    	if(packet!=null)
	    	{
		        if(watercap.sendNet().compareTo("ok")!=0)
		        {
		        	//((MainOperationActivity) g_ctx).showtishi("上传失败！");
		        	show="上传失败！";
		        	handler.post(showmessagetask);
		        	if(sharedPreferenceDatabase!=null)
		        	{
		        		try {
		        			Log.i("SAVE","to save ");
							sharedPreferenceDatabase.Setshuju(g_ctx,packet);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		        	}
		        	else
		        		Log.i("SAVE", "is null");
		        }
		        else
		        {
		        	//((MainOperationActivity) g_ctx).showtishi("上传成功!");
		        	show="上传成功!";
		        	handler.post(showmessagetask);
		        	if(SysApplication.getInstance().cameraactivity!=null)
		        	SysApplication.getInstance().cameraactivity.deletetupian();
		        }
	    	}
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
	public static float byte2float(byte[] b, int index) {    
	    int l;                                             
	    l = b[index + 3];                                  
	    l &= 0xff;                                         
	    l |= ((long) b[index + 2] << 8);                   
	    l &= 0xffff;                                       
	    l |= ((long) b[index + 1] << 16);                  
	    l &= 0xffffff;                                     
	    l |= ((long) b[index + 0] << 24);                  
	    return Float.intBitsToFloat(l);                    
	}
	protected void onDataReceived(final byte[] buffer, final int size) {
		
        runOnUiThread(new Runnable() {
                public void run() {
                		String ret=byte2HexStr(buffer,size);
						Log.i("485",size + "==>"+ret);
						if(header_got && to_read>0)
						{
							int cur=read_len-to_read;
							Log.i("Response1 ", String.valueOf(cur)+"+"+response);
							for(int j=0;j<to_read;j++)
							{
								response[j+cur]=(byte) (buffer[j] & 0xFF);
								//Log.i("DEBUG0", String.valueOf(response[j+cur]));
							}
							Log.i("CCC", String.valueOf(to_read)+" size "+String.valueOf(size));
							if(to_read>size)
							{
								to_read=to_read-size;
								return ;
							}
								
							//for(int j=0;j<read_len;j++)
							//	Log.i("DEBUG", String.valueOf(response[j]));
							read_len=0;
							to_read=0;
							header_got=false;
							Log.i("Response2", byte2HexStr(response,32));
							Log.i("COD",String.format("%6.3f",byte2float(response,0)));
							Log.i("NO3-N",String.format("%6.3f",byte2float(response,4)));
							Log.i("NH4-N",String.format("%6.3f",byte2float(response,8)));
							Log.i("Deep",String.format("%6.3f",byte2float(response,12)));
							Log.i("Speed",String.format("%6.3f",byte2float(response,16)));
							Log.i("Distance",String.format("%6.3f",byte2float(response,20)));
							Log.i("Power",String.format("%6.3f",byte2float(response,24)));
							Log.i("Shuiwen",String.format("%6.3f",byte2float(response,28)));
							Log.i("no3n zero", String.valueOf(no3n_zero));
							Log.i("cod zero", String.valueOf(cod_zero));
							//if(cod_zero>=0)
								cur_cod=(byte2float(response,0)-cod_zero)*cod_a+cod_b;
							//else
							//	cur_cod=(byte2float(response,0)+cod_zero)*cod_a+cod_b;
							//if(no3n_zero>=0)
								cur_no3n=(byte2float(response,4)-no3n_zero)*no3n_c+no3n_d;
							//else
							//	cur_no3n=(byte2float(response,4)+no3n_zero)*no3n_c+no3n_d;
							cur_nh4n=byte2float(response,8);
							cur_deep=byte2float(response,12);
							cur_speed=byte2float(response,16);
							cur_distance=byte2float(response,20);
							cur_power=byte2float(response,24);
							cur_shuiwen=byte2float(response,28);
							Log.i("COD-J",String.format("%6.3f",cur_cod));
							Log.i("NO3-N-J",String.format("%6.3f",cur_no3n));
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
							Log.i("Response", "Size "+String.valueOf(size)+" i "+String.valueOf(i));
							if(i!=size)
							{
								if(read_len>size-3)
								{
									for(i=0;i<size;i++)
										response[i]=(byte) (buffer[i+index]&0xff);
									to_read=read_len-(size-index);
									Log.i("to_read", String.valueOf(to_read));
									Log.i("read_len", String.valueOf(read_len));
									Log.i("Response0", byte2HexStr(response,32));
								}
								else
								{
									for(i=0;i<read_len;i++)
										response[i]=(byte) (buffer[i+index]&0xff);
									read_len=0;
									to_read=0;
									header_got=false;
									Log.i("Response3", byte2HexStr(response,32));
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
			//System.out.println("串口数据发送成功");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private final Runnable updatevalue = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			//add dialog display "cap done"
			opCOD(String.format("%6.6f",cur_cod));
			opxiaodan(String.format("%6.6f",cur_no3n));
			opandan(String.format("%6.6f",cur_nh4n));
			opliusu(String.format("%6.6f",cur_speed));
			opshendu(String.format("%6.6f",cur_deep));
			opkuandu(String.format("%6.6f",cur_distance));
			opshuiwen(String.format("%6.6f", cur_shuiwen));
			Log.i("Cap-cod", String.format("%6.6f",cur_cod));
			Log.i("Cap-no3n", String.format("%6.6f",cur_no3n));
			Log.i("Cap-nh4n", String.format("%6.6f",cur_nh4n));
			Log.i("Cap-speed", String.format("%6.6f",cur_speed));
			Log.i("Cap-deep", String.format("%6.6f",cur_deep));
			Log.i("Cap-distance", String.format("%6.6f",cur_distance));
			Log.i("Cap-shuiwen", String.format("%6.6f",cur_shuiwen));
			show="采集数据刷新完成！";
        	handler.post(showmessagetask);
			if(auto_mode)
				try {
					auto_process();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	};
	private final Runnable updatetl = new Runnable() {

		public void run() {
			// TODO Auto-generated method stub
			//add dialog display "cap done"
			opCODtongliang(String.format("%6.6f",avg_cod[bak_cnt-1]));
			opxiaodantongliang(String.format("%6.6f",avg_no3n[bak_cnt-1]));
			opandantongliang(String.format("%6.6f",avg_nh4n[bak_cnt-1]));
			Log.i("TL-nh4n", String.format("%6.6f",avg_nh4n[bak_cnt-1]));
			Log.i("TL-no3n", String.format("%6.6f",avg_no3n[bak_cnt-1]));
			Log.i("TL-cod", String.format("%6.6f",avg_cod[bak_cnt-1]));
			show="通量计算完成！";
        	handler.post(showmessagetask);
        	if(auto_mode)
					upload();
		}
	};
	

}
