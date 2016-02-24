package com.project.water;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity {

	List<Map<String, String>> listuser = new ArrayList<Map<String, String>>();
	List<Map<String, String>> listmodexuanze = new ArrayList<Map<String, String>>();
	List<Map<String, String>> listceliangfangshi = new ArrayList<Map<String, String>>();
	List<Map<String, String>> listremembermiama = new ArrayList<Map<String, String>>();

	// 初始化控件
	Context context;
	SharedPreferencesDatabase sharedPreferenceDatabase;
	Spinner spinnermodexuanze;
	Spinner spinnerceliangfangshi;
	CheckBox remembermima;
	Button buttonlogin;
	Button buttonguanji;
	EditText editUser;
	EditText editMima;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		spinnermodexuanze = (Spinner) findViewById(R.id.spinnermodexuanze);
		spinnerceliangfangshi = (Spinner) findViewById(R.id.spinnerceliangfangshi);
		buttonlogin = (Button) findViewById(R.id.buttonlogin);
		buttonguanji = (Button) findViewById(R.id.buttonguanji);
		editUser = (EditText) findViewById(R.id.edituser);
		editMima = (EditText) findViewById(R.id.editmima);
		remembermima = (CheckBox) findViewById(R.id.checkboxjizhumima);
		editUser.setText("000001");
		editMima.setText("000001");
		sharedPreferenceDatabase = new SharedPreferencesDatabase();
		context = this;
		
		getlatestsettings();
		setspinnermodexuanze();
		setButtonLogin();
		setspinnerceliangfangshi();
		setcheckboxmima();
		SysApplication.getInstance().addActivity(this); 
	}
	
	void getlatestsettings()
	{
		try {
			sharedPreferenceDatabase.DefaultSharedPreferences((Context) this);
			listuser = sharedPreferenceDatabase.GetuserArray((Context) this);
			listmodexuanze = sharedPreferenceDatabase
					.GetmodexuanzeArray((Context) this);
			listceliangfangshi = sharedPreferenceDatabase
					.GetjiangeArray((Context) this);
			listremembermiama = sharedPreferenceDatabase
					.Getremembermima((Context) this);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setButtonLogin() {
		buttonlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Boolean offlineuser = false;
				Boolean adminuser = false;
				String user = editUser.getText().toString();
				String mima = editMima.getText().toString();
				
				//保存用户名密码
				if(remembermima.isChecked()==true)
				{
					try {
						sharedPreferenceDatabase.Setremembermima(context, "1",
								editUser.getText().toString(), editMima.getText().toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					try {
						sharedPreferenceDatabase.Setremembermima(context, "0",
								editUser.getText().toString(), editMima.getText().toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				Intent intent = new Intent();
				Map<String, String> map = new HashMap<String, String>();
				for (int i = 0; i < listuser.size(); i++) {

					map = listuser.get(i);
					String userTmp = map.get(SharedPreferencesDatabase.user);
					String mimaTmp = map.get(SharedPreferencesDatabase.mima);
					if (userTmp.equals(user)
							&& mimaTmp.equals(editMima.getText().toString())) {
						if (i == 1) {
							offlineuser = true;
						}
						if (i == 0) {
							adminuser = true;
						}
					}
				}

				Boolean onlineUser = getonlineUser(user, mima);
				if (adminuser == true || offlineuser == true || onlineUser
						|| true) {
					if (adminuser == true) {
						intent.setClass(MainActivity.this,
								AdministratorActivity.class);// 当前的Activity为Test,目标Activity为Name
					} else {
						intent.setClass(MainActivity.this,
								MainOperationActivity.class);// 当前的Activity为Test,目标Activity为Name
					}

					int modexuanze = spinnermodexuanze.getSelectedItemPosition();
					int zidongjiange = spinnerceliangfangshi.getSelectedItemPosition();
					// 从下面这行开始是将数据传给新的Activity,如果不传数据，只是简单的跳转，这几行代码请注释掉
					Bundle bundle = new Bundle();
					bundle.putString("user", user);// key1为名，value1为值
					bundle.putString("password", mima);
					bundle.putInt("modexuanze", modexuanze);
					bundle.putInt("zidongjiange", zidongjiange);
					intent.putExtras(bundle);
					// 传数据结束
					startActivity(intent);
				}
			}

		});
	}
	
	public void setButtonguanji() {
		buttonguanji.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
			
		}
		);
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

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Another interface callback
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		getlatestsettings();
		setspinnermodexuanze();
		setspinnerceliangfangshi();
		super.onResume();
	}

	public void setspinnerceliangfangshi() {
		// 建立Adapter并且绑定数据源
		int size = listceliangfangshi.size();
		size = size + 1;
		String[] mItems = new String[size];
		Map<String, String> map = new HashMap<String, String>();

		// 添加手动
		mItems[0] = this.getString(R.string.shoudongceliang);
		for (int i = 0; i < listceliangfangshi.size(); i++) {
			map = listceliangfangshi.get(i);
			int index = i + 1;
			mItems[index] = this.getString(R.string.zidong);
			mItems[index] += map.get(SharedPreferencesDatabase.decimal);
			if (map.get(SharedPreferencesDatabase.hour).length() > 0) {
				mItems[index] += this.getString(R.string.hour);
			} else {
				mItems[index] += this.getString(R.string.minute);
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

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// Another interface callback
					}
				});
	}

	public void setcheckboxmima() {

		Map<String, String> map = new HashMap<String, String>();
		if (listremembermiama.size() > 0) {
			map = listremembermiama.get(0);
			if (map.get(SharedPreferencesDatabase.remembermiama).equals("1")) {
				remembermima.setChecked(true);
				editUser.setText(map
						.get(SharedPreferencesDatabase.remembermiamauser));

				editMima.setText(map
						.get(SharedPreferencesDatabase.remembermiamamima));
			} else {
				remembermima.setChecked(false);
			}
		}

remembermima.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	//判断在线用户用户名密码 ，
	public Boolean getonlineUser(String user, String password) {
		return true;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
    	if(keyCode==KeyEvent.KEYCODE_BACK)
    	{
    		return false;
    		//backpressed();
    	}
		return super.onKeyDown(keyCode, event);
	}
}
