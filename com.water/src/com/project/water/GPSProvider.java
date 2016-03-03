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
	// ����
	private static GPSProvider mGPSProvider;
	String provider;
	private static Context context;
	// ����
	private static MyLoactionListener listener;

	Location mainlocation;

	// 1.˽�л����췽��

	private GPSProvider() {
	};

	// 2. �ṩһ����̬�ķ��� ���Է�������һ��ʵ�� ��֤�������ִ����� ���� synchronized ��̬��GPSProvider
	public static synchronized GPSProvider getInstance(Context context) {
		if (mGPSProvider == null) {
			mGPSProvider = new GPSProvider();
			GPSProvider.context = context;
		}
		return mGPSProvider;
	}

	// ��ȡgps ��Ϣ
	public String getLocation() {
		// ��ȡ��λ����صķ��� ������ͨ�������Ļ�ȡ������
		manager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// ��ȡ���еĶ�λ��ʽ
		// manager.getAllProviders(); // gps //wifi //
		// ѡ��һ��Ŀǰ״̬����õĶ�λ��ʽ
		provider = getProvider(manager);
		//provider = manager.GPS_PROVIDER;
		// ע��λ�õļ�����
		/**
		 * provider ��λ��ʽ ��ʲô�豸��λ ��վ ���� GPS AGPS ʱ�� gps �೤ʱ�����»�ȡһ��λ�� ��СΪ1���� λ��
		 * ���λ�� λ�øı���� ���»�ȡһ��λ�� listener λ�÷����ı�ʱ ��Ӧ�Ļص�����
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
	

	// ֹͣgps����
	public void stopGPSListener() {
		// ����ΪLocationListener
		manager.removeUpdates(getListener());
	}

	// ����Listenerʵ��
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
		 * ���ֻ�λ�÷����ı��ʱ�� ���õķ���
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
			editor.commit(); // ���һ�λ�ȡ����λ����Ϣ ��ŵ�sharedpreference����
			//Log.v("TAG", "ʱ�䣺" + location.getTime());
			//Log.v("TAG", "���ȣ�" + location.getLongitude());
			//Log.v("TAG", "γ�ȣ�" + location.getLatitude());
			//Log.v("TAG", "���Σ�" + location.getAltitude());
		}

		/**
		 * ĳһ���豸��״̬�����ı��ʱ�� ���� ����->������ ������->���� GPS�Ƿ����
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		/**
		 * ĳ���豸���� GPS����
		 */
		public void onProviderEnabled(String provider) {
			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("location", " Enabled ");
			editor.commit(); // ���һ�λ�ȡ����λ����Ϣ ��ŵ�sharedpreference����
		}

		/**
		 * ĳ���豸������ GPS������
		 * 
		 */
		public void onProviderDisabled(String provider) {
			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("location", " Disabled ");
			editor.commit(); // ���һ�λ�ȡ����λ����Ϣ ��ŵ�sharedpreference����
		}

	}

	/**
	 * 
	 * @param manager
	 *            λ�ù������
	 * @return ��õ�λ���ṩ�� // gps //wifi //
	 */
	private String getProvider(LocationManager manager) {
		// һ���ѯ����
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // ��ȡ��׼λ�á�
		criteria.setAltitudeRequired(false);// �Ժ��β�����
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);// �ĵ����е�
		criteria.setSpeedRequired(true);// �ٶȱ仯����
		criteria.setCostAllowed(true);// �������� ͨ�ŷ���
		// ������õ�λ���ṩ�� true ��ʾֻ���ص�ǰ�Ѿ��򿪵Ķ�λ�豸
		return manager.getBestProvider(criteria, true);
	}
}
