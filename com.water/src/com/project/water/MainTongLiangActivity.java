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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainTongLiangActivity extends Activity {

	List<Map<String, String>> listuser = new ArrayList<Map<String, String>>();
	List<Map<String, String>> listmodexuanze = new ArrayList<Map<String, String>>();

	// 初始化控件
	Button buttonCeliang;
	Button buttonpaizhao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintongliang);

		buttonCeliang = (Button) findViewById(R.id.buttonceliang);
		buttonpaizhao = (Button) findViewById(R.id.buttonpaizhao);
		SharedPreferencesDatabase sharedPreferenceDatabase = new SharedPreferencesDatabase();
		try {
			sharedPreferenceDatabase.DefaultSharedPreferences((Context) this);
			listuser = sharedPreferenceDatabase.GetuserArray((Context) this);
			listmodexuanze = sharedPreferenceDatabase
					.GetmodexuanzeArray((Context) this);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setButtonPaizhao();
		setButtonCeliang();
	}
	
	public void setButtonPaizhao()
	{
		buttonpaizhao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainTongLiangActivity.this, CameraActivity.class);// 当前的Activity为Test,目标Activity为Name
				// 从下面这行开始是将数据传给新的Activity,如果不传数据，只是简单的跳转，这几行代码请注释掉
				Bundle bundle = new Bundle();
				bundle.putString("key1", "value1");// key1为名，value1为值
				bundle.putString("key2", "value2");
				intent.putExtras(bundle);
				// 传数据结束
				startActivity(intent);
			}
	});
	}
	
	public void setButtonCeliang()
	{
		buttonCeliang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainTongLiangActivity.this, MainOperationActivity.class);// 当前的Activity为Test,目标Activity为Name
				// 从下面这行开始是将数据传给新的Activity,如果不传数据，只是简单的跳转，这几行代码请注释掉
				Bundle bundle = new Bundle();
				bundle.putString("key1", "value1");// key1为名，value1为值
				bundle.putString("key2", "value2");
				intent.putExtras(bundle);
				// 传数据结束
				startActivity(intent);
			}
	});
	}

}
