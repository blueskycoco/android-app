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

import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class AdministratorActivity extends Activity {

	List<Map<String, String>> listmodexuanze = new ArrayList<Map<String, String>>();
	List<Map<String, String>> listceliangfangshi = new ArrayList<Map<String, String>>();
	SharedPreferencesDatabase sharedPreferenceDatabase;

	Context context;
	Spinner spinnermodexuanze;
	Spinner spinnerceliangfangshi;
	Spinner spinnerceliangfangshiunit;
	EditText deviceID;
	EditText imei;
	EditText celiangfangshi;
	Button buttonshanchujiange;
	Button buttontianjiajiange;
	EditText editcanshuname;
	EditText editcanshu1;
	EditText editcanshu2;
	Button buttonshanchumode;
	Button buttontianjiamode;
	Button buttonbaocun;
	Button buttonfanhui;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_administrator);
		// Button Btn1 = (Button) findViewById(R.id.buttoncpashe);

		sharedPreferenceDatabase = new SharedPreferencesDatabase();
		spinnermodexuanze = (Spinner) findViewById(R.id.spinnermode);
		spinnerceliangfangshiunit = (Spinner) findViewById(R.id.spinnerceliangjiangeunit);
		deviceID = (EditText) findViewById(R.id.editdeviceID);
		imei = (EditText) findViewById(R.id.editimei);
		celiangfangshi = (EditText) findViewById(R.id.editceliangjiange);
		spinnerceliangfangshi = (Spinner) findViewById(R.id.spinnerceliangjiange);
		buttonshanchujiange = (Button) findViewById(R.id.buttonshanchujiange);
		buttontianjiajiange = (Button) findViewById(R.id.buttontianjiajiange);
		editcanshuname = (EditText) findViewById(R.id.editcanshuname);
		editcanshu1 = (EditText) findViewById(R.id.editcanshu1);
		editcanshu2 = (EditText) findViewById(R.id.editcanshu2);
		buttonshanchumode = (Button) findViewById(R.id.buttonshanchumode);
		buttontianjiamode = (Button) findViewById(R.id.buttontianjiamode);
		buttonbaocun = (Button) findViewById(R.id.buttonbaocun);
		buttonfanhui = (Button) findViewById(R.id.buttonfanhui);
		context = this;
		try {
			listmodexuanze = sharedPreferenceDatabase
					.GetmodexuanzeArray((Context) this);

			listceliangfangshi = sharedPreferenceDatabase
					.GetjiangeArray((Context) this);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setdeviceID();
		setIMEI();
		setspinnerceliangfangshi();
		setspinnermodexuanze();
		setspinnerceliangfangshiunit();
		setButtonshanchujiange();
		setButtontianjiajiange();
		setButtonshanchumode();
		setButtontianjiamode();
		setButtonbaocun();
		setButtonfanhui();
		SysApplication.getInstance().addActivity(this);
	}
	
	public void backpressed() {
		/*Intent intent = new Intent();
		intent.setClass(AdministratorActivity.this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent);*/
		super.onBackPressed();
	}

	public void setIMEI() {
		String strTmp = null;
		TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE); 
		strTmp = tm.getDeviceId();//String 
		imei.setText(strTmp);
		

	}
	
	public void setdeviceID() {
		String strTmp = null;
		try {
			strTmp = sharedPreferenceDatabase.GetdeviceID(context);
			deviceID.setText(strTmp);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setspinnerceliangfangshiunit() {
		String[] mItems = new String[2];
		mItems[0] = this.getString(R.string.minute);
		mItems[1] = this.getString(R.string.hour);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinner, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定 Adapter到控件
		spinnerceliangfangshiunit.setAdapter(adapter);
		spinnerceliangfangshiunit
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Another interface callback
					}
				});
	}

	public void setspinnerceliangfangshi() {
		// 建立Adapter并且绑定数据源
		int size = listceliangfangshi.size();

		String[] mItems = new String[size];
		Map<String, String> map = new HashMap<String, String>();

		// 添加手动

		for (int i = 0; i < listceliangfangshi.size(); i++) {
			map = listceliangfangshi.get(i);
			mItems[i] = this.getString(R.string.zidong);
			mItems[i] += map.get(SharedPreferencesDatabase.decimal);
			if (map.get(SharedPreferencesDatabase.hour).length() > 0) {
				mItems[i] += this.getString(R.string.hour);
			} else {
				mItems[i] += this.getString(R.string.minute);
			}
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinner, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定 Adapter到控件
		spinnerceliangfangshi.setAdapter(adapter);
		spinnerceliangfangshi
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

						Map<String, String> map = new HashMap<String, String>();
						map = listceliangfangshi.get(pos);
						celiangfangshi.setText(map
								.get(SharedPreferencesDatabase.decimal));
						if (map.get(SharedPreferencesDatabase.hour).length() > 0) {
							spinnerceliangfangshiunit.setSelection(1);
						} else {
							spinnerceliangfangshiunit.setSelection(0);
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Another interface callback
					}
				});
	}

	public void setspinnermodexuanze() {
		// 建立Adapter并且绑定数据源
		String[] mItems = new String[listmodexuanze.size()];
		Map<String, String> map = new HashMap<String, String>();

		for (int i = 0; i < listmodexuanze.size(); i++) {
			map = listmodexuanze.get(i);
			mItems[i] = map.get(SharedPreferencesDatabase.modexuanze);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.spinner, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 绑定 Adapter到控件
		spinnermodexuanze.setAdapter(adapter);
		spinnermodexuanze
				.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						Map<String, String> map = new HashMap<String, String>();
						map = listmodexuanze.get(pos);
						editcanshuname.setText(map
								.get(SharedPreferencesDatabase.modexuanze));
						editcanshu1.setText(map
								.get(SharedPreferencesDatabase.modexuanzecanshu0));
						editcanshu2.setText(map
								.get(SharedPreferencesDatabase.modexuanzecanshu1));

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Another interface callback
					}
				});
	}

	public void setButtonshanchujiange() {
		buttonshanchujiange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int i = spinnerceliangfangshi.getSelectedItemPosition();
				listceliangfangshi.remove(i);
				setspinnerceliangfangshi();
			}

		});
	}

	public void setButtontianjiajiange() {
		buttontianjiajiange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String jiange = celiangfangshi.getText().toString();
				Map<String, String> map = new HashMap<String, String>();
				map.put(SharedPreferencesDatabase.decimal, jiange);
				if (spinnerceliangfangshiunit.getSelectedItemPosition() > 0) {
					map.put(SharedPreferencesDatabase.hour, "1");
					map.put(SharedPreferencesDatabase.minute, "");

				} else {
					map.put(SharedPreferencesDatabase.hour, "");
					map.put(SharedPreferencesDatabase.minute, "1");
				}
				listceliangfangshi.add(map);
				setspinnerceliangfangshi();
			}

		});
	}

	public void setButtonshanchumode() {
		buttonshanchumode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int i = spinnermodexuanze.getSelectedItemPosition();
				listmodexuanze.remove(i);
				setspinnermodexuanze();
			}

		});
	}

	public void setButtontianjiamode() {
		buttontianjiamode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				String canshuname = editcanshuname.getText().toString();
				String canshu1 = editcanshu1.getText().toString();
				String canshu2 = editcanshu2.getText().toString();

				for (int i = 0; i < listmodexuanze.size(); i++) {
					Map<String, String> map1 = new HashMap<String, String>();
					map1 = listmodexuanze.get(i);
					String modexunze = map1
							.get(SharedPreferencesDatabase.modexuanze);
					if (modexunze.equals(canshuname)) {
						listmodexuanze.remove(i);
						break;
					}

				}
				Map<String, String> map = new HashMap<String, String>();
				map.put(SharedPreferencesDatabase.modexuanze, canshuname);
				map.put(SharedPreferencesDatabase.modexuanzecanshu0, canshu1);
				map.put(SharedPreferencesDatabase.modexuanzecanshu1, canshu2);

				listmodexuanze.add(map);
				setspinnermodexuanze();
			}

		});
	}

	public boolean checkdeviceIDvalue() {
		boolean rtn = false;
		if (6 == deviceID.getText().toString().length()) {
			rtn = true;
		}
		return rtn;
	}

	public void setButtonbaocun() {
		buttonbaocun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				try {
					if (checkdeviceIDvalue() == true) {
						sharedPreferenceDatabase.SetdeviceID(context, deviceID
								.getText().toString());
						sharedPreferenceDatabase.SetmodexuanzeArray(context,
								listmodexuanze);
						sharedPreferenceDatabase.SetjiangeArray(context,
								listceliangfangshi);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});
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

}
