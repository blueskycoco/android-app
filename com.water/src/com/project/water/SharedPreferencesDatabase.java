package com.project.water;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

class SharedPreferencesDatabase {
	public static final String DATABASE = "Database";
	public static final String Default = "Default";
	public static final String JsonMain = "JsonMain";
	public static final String usershuzu = "usershuzu";
	public static final String mima = "mima";
	public static final String user = "user";
	public static final String modexuanzeshuzu = "modexuanzeshuzu";
	public static final String modexuanze = "modexuanze";
	public static final String jiange = "jiange";
	public static final String decimal = "decimal";
	public static final String minute = "minute";
	public static final String hour = "hour";
	public static final String modexuanzecanshu0 = "modexuanzecanshu0";
	public static final String modexuanzecanshu1 = "modexuanzecanshu1";
	public static final String setcheckboxmima = "setcheckboxmima";
	public static final String remembermiama = "remembermiama";
	public static final String remembermiamauser = "remembermiamauser";
	public static final String remembermiamamima = "remembermiamamima";
	public static final String deviceID = "deviceID";
	public static final String deviceIDstr = "deviceIDstr";
	// public static final String celiangjiangeshuzu = "celiangjiangeshuzu";

	Context ctx = null;

	public void SharedPreferencesDatabase() {
	}

	public void DefaultSharedPreferences(Context context) throws JSONException {
		SharedPreferences sp = (SharedPreferences) context
				.getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);

		Integer result = sp.getInt(Default, 0);

		if (result == 0) {
			Editor editor = sp.edit();
			// ����ȡ�����е���Ϣ
			editor.putInt(Default, 1);

			JSONObject jsonMainValue = new JSONObject();
			// �û�����
			JSONArray jsonuserArray = new JSONArray();

			JSONObject jsonusermima0 = new JSONObject();

			jsonusermima0.put(user, "000000");
			jsonusermima0.put(mima, "000000");
			jsonuserArray.put(jsonusermima0);

			JSONObject jsonusermima1 = new JSONObject();
			jsonusermima1.put(user, "000001");
			jsonusermima1.put(mima, "000001");
			jsonuserArray.put(jsonusermima1);

			jsonMainValue.put(usershuzu, jsonuserArray);

			// ģʽѡ��
			JSONArray jsonmodexuanzeArray = new JSONArray();
			JSONObject jsonmodexuanze0 = new JSONObject();
			jsonmodexuanze0.put(modexuanze, "��׼��Һ");
			jsonmodexuanze0.put(modexuanzecanshu0, "1.00");
			jsonmodexuanze0.put(modexuanzecanshu1, "1.00");
			jsonmodexuanzeArray.put(jsonmodexuanze0);

			JSONObject jsonmodexuanze1 = new JSONObject();
			jsonmodexuanze1.put(modexuanze, "��ҵ���ۿ�");
			jsonmodexuanze1.put(modexuanzecanshu0, "2.00");
			jsonmodexuanze1.put(modexuanzecanshu1, "2.00");
			jsonmodexuanzeArray.put(jsonmodexuanze1);

			JSONObject jsonmodexuanze2 = new JSONObject();
			jsonmodexuanze2.put(modexuanze, "�������ۿ�");
			jsonmodexuanze2.put(modexuanzecanshu0, "3.00");
			jsonmodexuanze2.put(modexuanzecanshu1, "3.00");
			jsonmodexuanzeArray.put(jsonmodexuanze2);

			JSONObject jsonmodexuanze3 = new JSONObject();
			jsonmodexuanze3.put(modexuanze, "�ر�ˮ");
			jsonmodexuanze3.put(modexuanzecanshu0, "4.00");
			jsonmodexuanze3.put(modexuanzecanshu1, "4.00");
			jsonmodexuanzeArray.put(jsonmodexuanze3);

			JSONObject jsonmodexuanze4 = new JSONObject();
			jsonmodexuanze4.put(modexuanze, "����ˮ");
			jsonmodexuanze4.put(modexuanzecanshu0, "5.00");
			jsonmodexuanze4.put(modexuanzecanshu1, "5.00");
			jsonmodexuanzeArray.put(jsonmodexuanze4);

			jsonMainValue.put(modexuanzeshuzu, jsonmodexuanzeArray);

			// ����ʱ����
			JSONArray jsonceliangjiangeArray = new JSONArray();
			JSONObject jsonjiange0 = new JSONObject();
			jsonjiange0.put(decimal, "10");
			jsonjiange0.put(hour, "");
			jsonjiange0.put(minute, "1");

			jsonceliangjiangeArray.put(jsonjiange0);

			JSONObject jsonjiange1 = new JSONObject();
			jsonjiange1.put(decimal, "3");
			jsonjiange1.put(hour, "1");
			jsonjiange1.put(minute, "");

			jsonceliangjiangeArray.put(jsonjiange1);

			jsonMainValue.put(jiange, jsonceliangjiangeArray);

			// �豸ID
			JSONObject jsondeviceID = new JSONObject();
			jsondeviceID.put(deviceIDstr, "000000");

			jsonMainValue.put(deviceID, jsondeviceID);

			editor.putString(JsonMain, jsonMainValue.toString());

			editor.commit();
		}

	}

	public List GetuserArray(Context context) throws JSONException {

		SharedPreferences sp = (SharedPreferences) context
				.getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		String strJson = sp.getString(JsonMain, "");
		JSONObject jsonMainValue = new JSONObject(strJson);

		// �û�����
		JSONArray jsonuserArray = new JSONArray();

		JSONObject jsonusermima0 = new JSONObject();
		jsonuserArray = (JSONArray) jsonMainValue.get(usershuzu);
		for (int i = 0; i < jsonuserArray.length(); i++) {
			jsonusermima0 = jsonuserArray.getJSONObject(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put(user, jsonusermima0.getString(user));
			map.put(mima, jsonusermima0.getString(mima));
			list.add(map);
		}

		return list;
	}

	public List GetmodexuanzeArray(Context context) throws JSONException {

		SharedPreferences sp = (SharedPreferences) context
				.getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		String strJson = sp.getString(JsonMain, "");
		JSONObject jsonMainValue = new JSONObject(strJson);

		// ģʽѡ��
		JSONArray jsonmodexuanzeArray = new JSONArray();
		JSONObject jsonmodexuanze0 = new JSONObject();
		jsonmodexuanzeArray = (JSONArray) jsonMainValue.get(modexuanzeshuzu);
		for (int i = 0; i < jsonmodexuanzeArray.length(); i++) {
			jsonmodexuanze0 = jsonmodexuanzeArray.getJSONObject(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put(modexuanze, jsonmodexuanze0.getString(modexuanze));
			map.put(modexuanzecanshu0,
					jsonmodexuanze0.getString(modexuanzecanshu0));
			map.put(modexuanzecanshu1,
					jsonmodexuanze0.getString(modexuanzecanshu1));
			list.add(map);
		}

		return list;
	}

	public List GetjiangeArray(Context context) throws JSONException {
		SharedPreferences sp = (SharedPreferences) context
				.getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		String strJson = sp.getString(JsonMain, "");
		JSONObject jsonMainValue = new JSONObject(strJson);
		JSONArray jsonjiangeArray = new JSONArray();
		JSONObject jsonjiange0 = new JSONObject();
		jsonjiangeArray = (JSONArray) jsonMainValue.get(jiange);
		for (int i = 0; i < jsonjiangeArray.length(); i++) {
			jsonjiange0 = jsonjiangeArray.getJSONObject(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put(decimal, jsonjiange0.getString(decimal));
			map.put(hour, jsonjiange0.getString(hour));
			map.put(minute, jsonjiange0.getString(minute));
			list.add(map);
		}

		return list;

	}

	public String GetdeviceID(Context context) throws JSONException {
		SharedPreferences sp = (SharedPreferences) context
				.getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);

		String strJson = sp.getString(JsonMain, "");
		JSONObject jsonMainValue = new JSONObject(strJson);
		JSONObject jsondeviceID = new JSONObject();

		jsondeviceID = (JSONObject) jsonMainValue.get(deviceID);

		return jsondeviceID.getString(deviceIDstr);

	}

	public List Getremembermima(Context context) throws JSONException {
		SharedPreferences sp = (SharedPreferences) context
				.getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		String strJson = sp.getString(JsonMain, "");
		JSONObject jsonMainValue = new JSONObject(strJson);
		JSONArray jsonremembercheckboxArray = new JSONArray();
		JSONObject jsonremembercheckbox0 = new JSONObject();
		jsonremembercheckboxArray = (JSONArray) jsonMainValue
				.get(setcheckboxmima);
		for (int i = 0; i < jsonremembercheckboxArray.length(); i++) {
			jsonremembercheckbox0 = jsonremembercheckboxArray.getJSONObject(i);
			Map<String, String> map = new HashMap<String, String>();
			map.put(remembermiama,
					jsonremembercheckbox0.getString(remembermiama));
			map.put(remembermiamauser,
					jsonremembercheckbox0.getString(remembermiamauser));
			map.put(remembermiamamima,
					jsonremembercheckbox0.getString(remembermiamamima));
			list.add(map);
		}

		return list;
	}

	public Boolean Setremembermima(Context context, String strChecked,
			String user, String mima) throws JSONException {
		Boolean rtn;
		SharedPreferences sp = (SharedPreferences) context
				.getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		String strJson = sp.getString(JsonMain, "");
		JSONObject jsonMainValue = new JSONObject(strJson);
		JSONArray jsonremembercheckboxArray = new JSONArray();
		JSONObject jsonremembercheckbox0 = new JSONObject();

		jsonremembercheckbox0.put(remembermiama, strChecked);
		jsonremembercheckbox0.put(remembermiamauser, user);
		jsonremembercheckbox0.put(remembermiamamima, mima);

		jsonremembercheckboxArray.put(jsonremembercheckbox0);
		jsonMainValue.put(setcheckboxmima, jsonremembercheckboxArray);

		editor.putString(JsonMain, jsonMainValue.toString());

		rtn = editor.commit();

		return rtn;
	}

	public Boolean SetmodexuanzeArray(Context context,
			List<Map<String, String>> listmodexuanze) throws JSONException {
		Boolean rtn;
		SharedPreferences sp = (SharedPreferences) context
				.getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		String strJson = sp.getString(JsonMain, "");
		JSONObject jsonMainValue = new JSONObject(strJson);
		JSONArray jsonmodexuanzeArray = new JSONArray();

		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < listmodexuanze.size(); i++) {
			map = listmodexuanze.get(i);
			JSONObject jsonmodexuanze0 = new JSONObject();
			jsonmodexuanze0.put(modexuanze, map.get(modexuanze));
			jsonmodexuanze0.put(modexuanzecanshu0, map.get(modexuanzecanshu0));
			jsonmodexuanze0.put(modexuanzecanshu1, map.get(modexuanzecanshu1));
			jsonmodexuanzeArray.put(jsonmodexuanze0);
		}

		jsonMainValue.put(modexuanzeshuzu, jsonmodexuanzeArray);

		editor.putString(JsonMain, jsonMainValue.toString());

		rtn = editor.commit();

		return rtn;
	}

	public Boolean SetjiangeArray(Context context,
			List<Map<String, String>> listceliangfangshi) throws JSONException {
		Boolean rtn;
		SharedPreferences sp = (SharedPreferences) context
				.getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		String strJson = sp.getString(JsonMain, "");
		JSONObject jsonMainValue = new JSONObject(strJson);
		JSONArray jsonmodexuanzeArray = new JSONArray();

		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < listceliangfangshi.size(); i++) {
			map = listceliangfangshi.get(i);
			JSONObject jsonmodexuanze0 = new JSONObject();
			jsonmodexuanze0.put(decimal, map.get(decimal));
			jsonmodexuanze0.put(hour, map.get(hour));
			jsonmodexuanze0.put(minute, map.get(minute));
			jsonmodexuanzeArray.put(jsonmodexuanze0);
		}

		jsonMainValue.put(jiange, jsonmodexuanzeArray);

		editor.putString(JsonMain, jsonMainValue.toString());

		rtn = editor.commit();

		return rtn;
	}

	public Boolean SetdeviceID(Context context, String strdeviceID)
			throws JSONException {
		Boolean rtn;
		SharedPreferences sp = (SharedPreferences) context
				.getSharedPreferences(DATABASE, Activity.MODE_PRIVATE);
		Editor editor = sp.edit();
		String strJson = sp.getString(JsonMain, "");
		JSONObject jsonMainValue = new JSONObject(strJson);
		JSONObject jsondeviceID = new JSONObject();
		jsondeviceID.put(deviceIDstr, strdeviceID);

		jsonMainValue.put(deviceID, jsondeviceID);

		editor.putString(JsonMain, jsonMainValue.toString());

		rtn = editor.commit();

		return rtn;
	}
}