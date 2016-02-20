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
	float cod,no3n,nh4n,deep,speed,distance,power;
	byte[] response = new byte[28];
	static boolean header_got=false;
	String jpg2_str=null;
	String jpg1_str=null;
	String jpg3_str=null;
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

		watercap.set_img("3", img_json.toString());
		watercap.set_model("3", "{\"model1\":{\"modeltype\":1,\"waterwidth\":5,\"downarea\":5,\"waterdeep\":3,\"flowspeed\":3,\"fluxcod\":5,\"fluxnirate\":6,\"fluxammonia\":8},\"model2\":{\"modeltype\":2,\"waterwidth\":3,\"downarea\":4,\"waterdeep\":5,\"flowspeed\":8,\"fluxcod\":12,\"fluxnirate\":16,\"fluxammonia\":18},\"model3\":{\"modeltype\":3,\"waterwidth\":5,\"downarea\":6,\"waterdeep\":8,\"flowspeed\":9,\"fluxcod\":22,\"fluxnirate\":26,\"fluxammonia\":28}}");
		
		//watercap.set_model("3", "{\"model1\":{\"modeltype\":1,\"waterwidth\":5,\"downarea\":5,\"waterdeep\":3,\"flowspeed\":3,\"fluxcod\":5,\"fluxnirate\":6,\"fluxammonia\":8},\"model2\":{\"modeltype\":2,\"waterwidth\":3,\"downarea\":4,\"waterdeep\":5,\"flowspeed\":8,\"fluxcod\":12,\"fluxnirate\":16,\"fluxammonia\":18},\"model3\":{\"modeltype\":3,\"waterwidth\":5,\"downarea\":6,\"waterdeep\":8,\"flowspeed\":9,\"fluxcod\":22,\"fluxnirate\":26,\"fluxammonia\":28}}");
		//watercap.sendNet();
		new Thread(runnable).start();  
	}
	void set_param_upload(String cod,String nho3,String nhn4,String avg_cod,String avg_nho3,String avg_nhn4,String speed,String deep,String power,String distance)
	{
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
		JSONObject mode_json1 = new JSONObject();
		JSONObject mode_json = new JSONObject();
		//JSONObject mode_json3 = new JSONObject();
		//JSONObject mode_json = new JSONObject();
		try {
			mode_json1.put("modeltype", "1");
			mode_json1.put("waterwidth", "1");
			mode_json1.put("downarea", "5");
			mode_json1.put("waterdeep", deep);
			mode_json1.put("flowspeed", speed);
			mode_json1.put("fluxcod", avg_cod);
			mode_json1.put("fluxnirate", avg_nho3);
			mode_json1.put("fluxammonia", avg_nhn4);
			mode_json.put("model1",mode_json1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		watercap.set_img("3", img_json.toString());
		//watercap.set_model("3", "{\"model1\":{\"modeltype\":1,\"waterwidth\":5,\"downarea\":5,\"waterdeep\":3,\"flowspeed\":3,\"fluxcod\":5,\"fluxnirate\":6,\"fluxammonia\":8},\"model2\":{\"modeltype\":2,\"waterwidth\":3,\"downarea\":4,\"waterdeep\":5,\"flowspeed\":8,\"fluxcod\":12,\"fluxnirate\":16,\"fluxammonia\":18},\"model3\":{\"modeltype\":3,\"waterwidth\":5,\"downarea\":6,\"waterdeep\":8,\"flowspeed\":9,\"fluxcod\":22,\"fluxnirate\":26,\"fluxammonia\":28}}");
		watercap.set_model("1", mode_json.toString());
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
								cod=byte2float(response,0);
								no3n=byte2float(response,4);
								nh4n=byte2float(response,8);
								deep=byte2float(response,12);
								speed=byte2float(response,16);
								distance=byte2float(response,20);
								power=byte2float(response,24);
								//JuXing
								float r=1;
								float avg_cod=cod*speed*deep*distance;
								Log.i("avg_cod",String.format("%6.3f",avg_cod));
								float avg_no3n=no3n*speed*deep*distance;
								Log.i("avg_no3n",String.format("%6.3f",avg_no3n));
								float avg_nh4n=nh4n*speed*deep*distance;
								Log.i("avg_nh4n",String.format("%6.3f",avg_nh4n));
								double avg2_cod = Math.asin(distance/(2*r))*r*r-0.5f*distance*(r-deep);
								set_param_upload(String.format("%6.3f",cod),String.format("%6.3f",no3n),String.format("%6.3f",nh4n),String.format("%6.3f",avg_cod),String.format("%6.3f",avg_no3n),String.format("%6.3f",avg_nh4n),String.valueOf(speed),String.valueOf(deep),String.format("%6.3f",byte2float(response,24)),String.format("%6.3f",byte2float(response,20)));
								try {
									mOutputStream.write(cmd);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
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
