package com.example.water;

import com.example.watercap.watercap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("HERE", "we are here");
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
		watercap.set_img("3", "{\"img1\":{\"imgcon\":\"base64_encode(\"imgurl\")\",\"imgext\":\".jpg\"},\"img2\":{\"imgcon\":\"base64_encode(\"imgurl\")\",\"imgext\":\".jpg\"},\"img3\":{\"imgcon\":\"base64_encode(\"imgurl\")\",\"imgext\":\".jpg\"}}");
		watercap.set_model("3", "{\"model1\":{\"modeltype\":1,\"waterwidth\":5,\"downarea\":5,\"waterdeep\":3,\"flowspeed\":3,\"fluxcod\":5,\"fluxnirate\":6,\"fluxammonia\":8},\"model2\":{\"modeltype\":2,\"waterwidth\":3,\"downarea\":4,\"waterdeep\":5,\"flowspeed\":8,\"fluxcod\":12,\"fluxnirate\":16,\"fluxammonia\":18},\"model3\":{\"modeltype\":3,\"waterwidth\":5,\"downarea\":6,\"waterdeep\":8,\"flowspeed\":9,\"fluxcod\":22,\"fluxnirate\":26,\"fluxammonia\":28}}");
		//watercap.sendNet();
		new Thread(runnable).start();  
	}
	Handler handler = new Handler(){  
	    @Override  
	    public void handleMessage(Message msg) {  
	        super.handleMessage(msg);  
	        Bundle data = msg.getData();  
	        String val = data.getString("value");  
	        Log.i("mylog","请求结果-->" + val);  
	    }  
	};  
	  
	Runnable runnable = new Runnable(){  
	    @Override  
	    public void run() {  
	        Message msg = new Message();  
	        Bundle data = new Bundle();  
	        data.putString("value","请求结果");  
	        msg.setData(data);  
	        handler.sendMessage(msg);
	        watercap.sendNet();
	    }
	};
}
