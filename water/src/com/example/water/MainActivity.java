package com.example.water;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.watercap.watercap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
	float[] cod=new float[20],no3n=new float[20],nh4n=new float[20],deep=new float[20],speed=new float[20],distance=new float[20],power=new float[20];
	float[] avg_cod=new float[20];
	float[] avg_no3n=new float[20];
	float[] avg_nh4n=new float[20];
	double[] avg2_cod=new double[20];
	byte[] response = new byte[28];
	float[] wight=new float[10];
	static boolean header_got=false;
	String jpg2_str=null;
	String jpg1_str=null;
	String jpg3_str=null;
	String mode_string=null;
	int cnt=0;
	static int read_len=0,to_read=0;
	byte[] cmd={0x24,0x32,(byte)0xff,0x23,0x0a};
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
/*
		watercap.set_did("1000");		
		watercap.set_address("天津海河口");
		watercap.set_jingdu("120.325259");
		watercap.set_weidu("37.330890");
		watercap.set_batterypower("89");
		watercap.set_watertemper("23");
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
		*/
		/*
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
		*/
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

		//watercap.set_img("3", img_json.toString());
		//watercap.set_model("3", "{\"model1\":{\"modeltype\":1,\"waterwidth\":5,\"downarea\":5,\"waterdeep\":3,\"flowspeed\":3,\"fluxcod\":5,\"fluxnirate\":6,\"fluxammonia\":8},\"model2\":{\"modeltype\":2,\"waterwidth\":3,\"downarea\":4,\"waterdeep\":5,\"flowspeed\":8,\"fluxcod\":12,\"fluxnirate\":16,\"fluxammonia\":18},\"model3\":{\"modeltype\":3,\"waterwidth\":5,\"downarea\":6,\"waterdeep\":8,\"flowspeed\":9,\"fluxcod\":22,\"fluxnirate\":26,\"fluxammonia\":28}}");
		
		//watercap.set_model("3", "{\"model1\":{\"modeltype\":1,\"waterwidth\":5,\"downarea\":5,\"waterdeep\":3,\"flowspeed\":3,\"fluxcod\":5,\"fluxnirate\":6,\"fluxammonia\":8},\"model2\":{\"modeltype\":2,\"waterwidth\":3,\"downarea\":4,\"waterdeep\":5,\"flowspeed\":8,\"fluxcod\":12,\"fluxnirate\":16,\"fluxammonia\":18},\"model3\":{\"modeltype\":3,\"waterwidth\":5,\"downarea\":6,\"waterdeep\":8,\"flowspeed\":9,\"fluxcod\":22,\"fluxnirate\":26,\"fluxammonia\":28}}");
		//watercap.sendNet();
		//new Thread(runnable).start();  
	}
	String set_model_param(String cod,String nho3,String nhn4,String avg_cod1,String avg_nho3,String avg_nhn4,String speed,String deep
						,String modeltype,String waterwidth,String downarea,String cnt,String mode_string)
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
			e.printStackTrace();
		}
		if(mode_string==null)
			mode_string=mode_json.toString();
		else
			mode_string+=","+mode_json.toString();
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
			e.printStackTrace();
		}
		if(img_string==null)
			img_string=img_json.toString();
		else
			img_string+=","+img_json.toString();
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
		String img=set_img_param(jpg1_str,"1",null);
		img=set_img_param(jpg2_str,"2",img);
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
		watercap.set_img("2", img);
		watercap.set_model(String.valueOf(cnt), mode_string);
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
				re+=avg_cod[i];
			else if(type==1)
				re+=avg_no3n[i];
			else
				re+=avg_nh4n[i];
		}
		if(type==0)
			avg_cod[cnt]=re/(times+1);
		else if(type==1)
			avg_no3n[cnt]=re/(times+1);
		else
			avg_nh4n[cnt]=re/(times+1);
		if(type==0)
		Log.i("avg_cod",String.format("%6.3f",avg_cod[cnt]));
		else if(type==1)
		Log.i("avg_no3n",String.format("%6.3f",avg_no3n[cnt]));
		else
		Log.i("avg_nh4n",String.format("%6.3f",avg_nh4n[cnt]));
	}
	void count_tixing_tl(float up,float down,int times,int type)
	{
		float re=0;
		int i=0;
		set_wight(times+1);
		for(i=0;i<times+1;i++)
		{
			if(type==0)
			re+=cod[times]*wight[i];
			else if(type==1)
			re+=no3n[times]*wight[i];
			else
			re+=nh4n[times]*wight[i];
		}
		if(type==0)
			avg_cod[cnt]=(re*speed[times]*deep[times]*(up+down))/(float)2.0;
		else if(type==1)
			avg_no3n[cnt]=(re*speed[times]*deep[times]*(up+down))/(float)2.0;
		else
			avg_nh4n[cnt]=(re*speed[times]*deep[times]*(up+down))/(float)2.0;
		if(type==0)
		Log.i("avg_cod",String.format("%6.3f",avg_cod[cnt]));
		else if(type==1)
		Log.i("avg_no3n",String.format("%6.3f",avg_no3n[cnt]));
		else
		Log.i("avg_nh4n",String.format("%6.3f",avg_nh4n[cnt]));
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
			re+=cod[times]*wight[i];
			else if(type==1)
			re+=no3n[times]*wight[i];
			else
			re+=nh4n[times]*wight[i];
		}
		r = (distance[cnt]*distance[cnt])/(8*deep[cnt])+deep[cnt]/2;
		if(type==0)
			avg_cod[cnt]=(float) (re*Math.asin(distance[cnt]/(2*r))*r*r-0.5f*distance[cnt]*(r-deep[cnt]));
		else if(type==1)
			avg_no3n[cnt]=(float) (re*Math.asin(distance[cnt]/(2*r))*r*r-0.5f*distance[cnt]*(r-deep[cnt]));
		else
			avg_nh4n[cnt]=(float) (re*Math.asin(distance[cnt]/(2*r))*r*r-0.5f*distance[cnt]*(r-deep[cnt]));
		if(type==0)
		Log.i("avg_cod",String.format("%6.3f",avg_cod[cnt]));
		else if(type==1)
		Log.i("avg_no3n",String.format("%6.3f",avg_no3n[cnt]));
		else
		Log.i("avg_nh4n",String.format("%6.3f",avg_nh4n[cnt]));
		return r;
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
	        Log.i("SEND",watercap.sendNet());
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
                            //if (EditTextReception != null) {
                            //	EditTextReception.append(new String(buffer, 0, size));
                            //}
                    		String ret=byte2HexStr(buffer,size);
							Log.i("485",size + "==>"+ret);
							if(header_got && to_read>0)
							{	
								int cur=read_len-to_read;
								//Log.i("Response", String.valueOf(response.length));
								for(int j=0;j<to_read;j++)
								response[j+cur]=(byte) (buffer[j] & 0xFF);
								read_len=0;
								to_read=0;
								header_got=false;
								Log.i("Response1", byte2HexStr(response,28));
								Log.i("COD",String.format("%6.3f",byte2float(response,0)));
								Log.i("NO3-N",String.format("%6.3f",byte2float(response,4)));
								Log.i("NH4-N",String.format("%6.3f",byte2float(response,8)));
								Log.i("Deep",String.format("%6.3f",byte2float(response,12)));
								Log.i("Speed",String.format("%6.3f",byte2float(response,16)));
								Log.i("Distance",String.format("%6.3f",byte2float(response,20)));
								Log.i("Power",String.format("%6.3f",byte2float(response,24)));
								if(cnt<5)
								{
									cod[cnt]=byte2float(response,0);
									no3n[cnt]=byte2float(response,4);
									nh4n[cnt]=byte2float(response,8);
									deep[cnt]=byte2float(response,12);
									speed[cnt]=byte2float(response,16);
									distance[cnt]=byte2float(response,20);
									power[cnt]=byte2float(response,24);
									//JuXing
									/*
									float r=1;
									float tmp_cod=0,tmp_no3n=0,tmp_nh4n=0;
									avg_cod[cnt]=cod[cnt]*speed[cnt]*deep[cnt]*distance[cnt];								
									avg_no3n[cnt]=no3n[cnt]*speed[cnt]*deep[cnt]*distance[cnt];
									avg_nh4n[cnt]=nh4n[cnt]*speed[cnt]*deep[cnt]*distance[cnt];
									for(int i=0;i<cnt+1;i++)
									{
										tmp_cod+=avg_cod[i];
										tmp_no3n+=avg_no3n[i];
										tmp_nh4n+=avg_nh4n[i];
									}
									avg_cod[cnt]=tmp_cod/(cnt+1);
									avg_no3n[cnt]=tmp_no3n/(cnt+1);
									avg_nh4n[cnt]=tmp_nh4n/(cnt+1);
									Log.i("avg_cod",String.format("%6.3f",avg_cod[cnt]));	
									Log.i("avg_no3n",String.format("%6.3f",avg_no3n[cnt]));
									Log.i("avg_nh4n",String.format("%6.3f",avg_nh4n[cnt]));
									avg2_cod[cnt] = Math.asin(distance[cnt]/(2*r))*r*r-0.5f*distance[cnt]*(r-deep[cnt]);
									*/
									//count_juxing_tl(cnt,0);
									//count_juxing_tl(cnt,1);
									//count_juxing_tl(cnt,2);
									count_tixing_tl(distance[cnt],2,cnt,0);
									count_tixing_tl(distance[cnt],2,cnt,1);
									count_tixing_tl(distance[cnt],2,cnt,2);
									//float r = count_yuanxing_tl(cnt,0);
									//count_yuanxing_tl(cnt,1);
									//count_yuanxing_tl(cnt,2);
									//mode_string = set_model_param(String.format("%6.3f",cod[cnt]),String.format("%6.3f",no3n[cnt]),String.format("%6.3f",nh4n[cnt]),String.format("%6.3f",avg_cod[cnt]),String.format("%6.3f",avg_no3n[cnt]),String.format("%6.3f",avg_nh4n[cnt]),String.format("%6.3f",speed[cnt]),String.format("%6.3f",deep[cnt]),
									//								"3",String.format("%6.3f",distance[cnt]),String.format("%6.3f",r),String.valueOf(cnt+1),mode_string);
									mode_string = set_model_param(String.format("%6.3f",cod[cnt]),String.format("%6.3f",no3n[cnt]),String.format("%6.3f",nh4n[cnt]),String.format("%6.3f",avg_cod[cnt]),String.format("%6.3f",avg_no3n[cnt]),String.format("%6.3f",avg_nh4n[cnt]),String.format("%6.3f",speed[cnt]),String.format("%6.3f",deep[cnt]),
																			"2",String.format("%6.3f",distance[cnt]),"2",String.valueOf(cnt+1),mode_string);
									Log.i("CNT",String.valueOf(cnt));
									cnt++;
									try {
										mOutputStream.write(cmd);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								else
								{
									send_net_work(String.format("%6.3f",power[cnt-1]),String.format("%6.3f",distance[cnt-1]),String.format("%6.3f",speed[cnt-1]),String.format("%6.3f",deep[cnt-1]),String.format("%6.3f",cod[cnt-1]),String.format("%6.3f",no3n[cnt-1]),String.format("%6.3f",nh4n[cnt-1]));
									cnt=0;
									mode_string=null;
									//set_param_upload(String.format("%6.3f",cod),String.format("%6.3f",no3n),String.format("%6.3f",nh4n),String.format("%6.3f",avg_cod),String.format("%6.3f",avg_no3n),String.format("%6.3f",avg_nh4n),String.valueOf(speed),String.valueOf(deep),String.format("%6.3f",byte2float(response,24)),String.format("%6.3f",byte2float(response,20)));
									try {
										mOutputStream.write(cmd);
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
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
									}
									else
									{
										for(i=0;i<read_len;i++)
											response[i]=(byte) (buffer[i+index]&0xff);
										read_len=0;
										to_read=0;
										header_got=false;
										Log.i("Response2", byte2HexStr(response,28));
									}
								}
							}
                    }
            });
    }
	
}
