package com.example.water;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.watercap.watercap;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
public class MainActivity extends Activity {
	//@Override
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;
	byte[] cmd={0x24,0x32,(byte) 0xff,0x23,0x0a};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.i("HERE", "we are here");
		try {
			watercap.init(this);
			mInputStream = watercap.getInputStream();
			mOutputStream = watercap.getOutputStream();
			/* Create a receiving thread */
			mReadThread = new ReadThread();
			mReadThread.start();
			mOutputStream.write(cmd);
			System.out.println("串口数据发送成功");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		AssetManager mAssetManager = this.getAssets();
		InputStream is_jpg1=null;
		InputStream is_jpg2=null;
		InputStream is_jpg3=null;
		String jpg2_str=null;
		String jpg1_str=null;
		String jpg3_str=null;
		try {
			is_jpg1=mAssetManager.open("20160110031630new.jpg.base");
			is_jpg2=mAssetManager.open("20160110031636new.jpg.base");
			is_jpg3=mAssetManager.open("20160110031644new.jpg.base");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(is_jpg1!=null)
		{
			ByteArrayOutputStream bos1=null;
			byte[] buf1=new byte[1024];
			int readLength=0;
			try {
				bos1=new ByteArrayOutputStream();
				while((readLength=is_jpg1.read(buf1))>0)
					bos1.write(buf1,0,readLength);
				bos1.flush();
				jpg1_str=new String(bos1.toByteArray());
				Log.i("jpg1", jpg1_str);
				byte[] out1=Base64.decode(jpg1_str, Base64.DEFAULT);
		        FileOutputStream outf1 = new FileOutputStream(new File(this.getFilesDir()+"/jpg1.jpg"));
		        outf1.write(out1);
		        outf1.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}if(is_jpg2!=null)
		{
			ByteArrayOutputStream bos2=null;
			byte[] buf2=new byte[1024];
			int readLength=0;
			try {
				bos2=new ByteArrayOutputStream();
				while((readLength=is_jpg2.read(buf2))>0)
					bos2.write(buf2,0,readLength);
				bos2.flush();
				jpg2_str=new String(bos2.toByteArray());
				Log.i("jpg2", jpg2_str);
				byte[] out2=Base64.decode(jpg2_str, Base64.DEFAULT);
		        FileOutputStream outf2 = new FileOutputStream(new File(this.getFilesDir()+"/jpg2.jpg"));
		        outf2.write(out2);
		        outf2.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
		if(is_jpg3!=null)
		{
			ByteArrayOutputStream bos3=null;
			byte[] buf3=new byte[1024];
			int readLength=0;
			try {
				bos3=new ByteArrayOutputStream();
				while((readLength=is_jpg3.read(buf3))>0)
					bos3.write(buf3,0,readLength);
				bos3.flush();
				jpg3_str=new String(bos3.toByteArray());
				Log.i("jpg3", jpg3_str);
				byte[] out3=Base64.decode(jpg3_str, Base64.DEFAULT);
		        FileOutputStream outf3 = new FileOutputStream(new File(this.getFilesDir()+"/jpg3.jpg"));
		        outf3.write(out3);
		        outf3.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
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
		
		JSONObject img_json1 = new JSONObject();
		JSONObject img_json2 = new JSONObject();
		JSONObject img_json3 = new JSONObject();
		JSONObject img_json = new JSONObject();
		try {
			img_json1.put("imgext", ".jpg");
			img_json1.put("imgcon", jpg1_str);
			img_json.put("img1", img_json1);
			img_json2.put("imgcon", jpg2_str);
			img_json2.put("imgext", ".jpg");
			img_json.put("img2", img_json2);
			img_json3.put("imgcon", jpg3_str);
			img_json3.put("imgext", ".jpg");
			img_json.put("img3", img_json3);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		watercap.set_img("3", img_json.toString());
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
	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[64];
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
	protected void onDataReceived(final byte[] buffer, final int size) {
            runOnUiThread(new Runnable() {
                    public void run() {
                            //if (EditTextReception != null) {
                            //	EditTextReception.append(new String(buffer, 0, size));
                            //}
                    	Log.i("485",buffer.toString());
                    }
            });
    }
}
