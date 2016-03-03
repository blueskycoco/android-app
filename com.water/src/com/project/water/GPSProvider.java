package com.project.water;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class GPSProvider {
	LocationManager manager;
	// 单例
	private static GPSProvider mGPSProvider;
	String provider;
	private static Context context;
	// 单例
	private static MyLoactionListener listener;

	Location mainlocation;

	// 1.私有化构造方法

	private GPSProvider() {
	};

	// 2. 提供一个静态的方法 可以返回他的一个实例 保证代码必须执行完成 放在 synchronized 单态的GPSProvider
	public static synchronized GPSProvider getInstance(Context context) {
		if (mGPSProvider == null) {
			mGPSProvider = new GPSProvider();
			GPSProvider.context = context;
		}
		return mGPSProvider;
	}

	// 获取gps 信息
	public String getLocation() {
		// 获取与位置相关的服务 服务都是通过上下文获取出来的
		manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 获取所有的定位方式
		// manager.getAllProviders(); // gps //wifi //
		// 选择一种目前状态下最好的定位方式
		provider = getProvider(manager);
		//provider = manager.GPS_PROVIDER;
		// 注册位置的监听器
		/**
		 * provider 定位方式 用什么设备定位 基站 网络 GPS AGPS 时间 gps 多长时间重新获取一下位置 最小为1分钟 位置
		 * 最短位移 位置改变多少 重新获取一下位置 listener 位置发生改变时 对应的回调方法
		 */
		mainlocation = manager.getLastKnownLocation(manager.GPS_PROVIDER);
		String location = "-";
		if (null != mainlocation) {
			String latitudedata = String.format("%.6f",
					mainlocation.getLatitude());
			String longtitudedata = String.format("%.6f",
					mainlocation.getLongitude());
			String latitude = context.getResources().getString(
					R.string.latitude)
					+ latitudedata; // weidu
			String longtitude = context.getResources().getString(
					R.string.longtitude)
					+ longtitudedata; // jingdu
			location = latitude + "-" + longtitude;
		}
		
		 manager.requestLocationUpdates(provider,60000, 50, getListener());
		 
		 /*SharedPreferences sp = context.getSharedPreferences("config",
		 Context.MODE_PRIVATE); 
		 String location = sp.getString("location",
		 "");*/
		 
		return location;
	}
	public String getlatitude()
	{
		String latitudedata="0";
		mainlocation = manager.getLastKnownLocation(manager.GPS_PROVIDER);
		if(mainlocation!=null)
			latitudedata = String.format("%.6f",mainlocation.getLatitude());
		return latitudedata;
	}
	
	public String getlongtitude()
	{
		String longtitudedata="0";
		mainlocation = manager.getLastKnownLocation(manager.GPS_PROVIDER);
		if(mainlocation!=null)
			longtitudedata = String.format("%.6f",mainlocation.getLongitude());
		return longtitudedata;
	}
	

	// 停止gps监听
	public void stopGPSListener() {
		// 参数为LocationListener
		manager.removeUpdates(getListener());
	}

	// 返回Listener实例
	private synchronized MyLoactionListener getListener() {
		if (listener == null) {
			listener = new MyLoactionListener();
		}
		return listener;
	}

	private class MyLoactionListener implements LocationListener {
		public MyLoactionListener() {
		}

		/**
		 * 当手机位置发生改变的时候 调用的方法
		 */
		public void onLocationChanged(Location location) {
			mainlocation = location;
			String latitudedata = String.format("%.2f", location.getLatitude());
			String longtitudedata = String.format("%.2f",
					location.getLongitude());
			String latitude = context.getResources().getString(
					R.string.latitude)
					+ latitudedata; // weidu
			String longtitude = context.getResources().getString(
					R.string.longtitude)
					+ latitudedata; // jingdu
			// latitude = latitude.substring(0, 6);
			// longtitude = longtitude.substring(0, 6);
			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("location", latitude + " - " + longtitude);
			editor.commit(); // 最后一次获取到的位置信息 存放到sharedpreference里面
			//Log.v("TAG", "时间：" + location.getTime());
			//Log.v("TAG", "经度：" + location.getLongitude());
			//Log.v("TAG", "纬度：" + location.getLatitude());
			//Log.v("TAG", "海拔：" + location.getAltitude());
		}

		/**
		 * 某一个设备的状态发生改变的时候 调用 可用->不可用 不可用->可用 GPS是否可用
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		/**
		 * 某个设备被打开 GPS被打开
		 */
		public void onProviderEnabled(String provider) {
			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("location", " Enabled ");
			editor.commit(); // 最后一次获取到的位置信息 存放到sharedpreference里面
		}

		/**
		 * 某个设备被禁用 GPS被禁用
		 * 
		 */
		public void onProviderDisabled(String provider) {
			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("location", " Disabled ");
			editor.commit(); // 最后一次获取到的位置信息 存放到sharedpreference里面
		}

	}

	/**
	 * 
	 * @param manager
	 *            位置管理服务
	 * @return 最好的位置提供者 // gps //wifi //
	 */
	private String getProvider(LocationManager manager) {
		// 一组查询条件
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 获取精准位置、
		criteria.setAltitudeRequired(false);// 对海拔不敏感
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);// 耗电量中等
		criteria.setSpeedRequired(true);// 速度变化敏感
		criteria.setCostAllowed(true);// 产生开销 通信费用
		// 返回最好的位置提供者 true 表示只返回当前已经打开的定位设备
		return manager.getBestProvider(criteria, true);
	}
}
