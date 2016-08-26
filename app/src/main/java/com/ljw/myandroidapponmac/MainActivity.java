package com.ljw.myandroidapponmac;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"app is runing normally");
        String[] urlStr={"https://www.baidu.com/img/bd_logo1.png",
                "http://pic56.nipic.com/file/20141227/19674963_215052431000_2.jpg",
                "http://img3.fengniao.com/travel/2_960/1850.jpg",
                "http://uploads.xuexila.com/allimg/1607/676-160G41H453.jpg"};
        String mSDCardPath= Environment.getExternalStorageDirectory().toString();
        String mLocalPhotoCachePath= mSDCardPath+"/myanycam/photoCache/";
        int num=0;
        for(String url:urlStr){
            String localUrlStr=mLocalPhotoCachePath+num+".png";
            //new DownloadAsyncTask(url,localUrlStr);
            new Thread(new DownloadRunnable(url,localUrlStr)).start();
            num++;
        }
    }

    public class DownloadRunnable implements Runnable {
        String serviceUrlStr=null;
        String localUrlStr=null;
        public DownloadRunnable(String serviceUrlStr,String localUrlStr){
            this.serviceUrlStr=serviceUrlStr;
            this.localUrlStr=localUrlStr;
        }

        @Override
        public void run() {
            InputStream is=null;
            FileOutputStream fos=null;

            try {
                //获得url
                URL serviceUrl=new URL(serviceUrlStr);
                //通过url打开连接
                HttpURLConnection conn=(HttpURLConnection)serviceUrl.openConnection();
                //设置获取图片的方式为GET
                conn.setRequestMethod("GET");
                //判断连接是否成功
                int code=conn.getResponseCode();
                if(code==200){
                    //获得文件输出流
                    File file=new File(localUrlStr);
                    if(!file.exists()){
                        file.createNewFile();
                    }

                    fos=new FileOutputStream(file);

                    is=conn.getInputStream();
                    int length=0;
                    byte[] buffer=new byte[1024];
                    while((length=is.read(buffer))!=-1){
                        fos.write(buffer,0,length);
                    }
                    fos.flush();
                    Log.i(TAG,"图片成功存入"+localUrlStr);
                }else{
                    Toast.makeText(MainActivity.this,"连接失败！",Toast.LENGTH_SHORT);
                }
            } catch (Exception e) {
                Log.i(TAG,"图片下载失败");
                e.printStackTrace();
            }finally {
                try{
                    if(is!=null){
                        is.close();
                    }
                    if(fos!=null){
                        fos.close();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }


        }
    }


    public class DownloadAsyncTask extends AsyncTask<String,Integer,Long> {
        String serviceUrlStr=null;
        String localUrlStr=null;

        public DownloadAsyncTask(String serviceUrlStr,String localUrlStr){
            this.serviceUrlStr=serviceUrlStr;
            this.localUrlStr=localUrlStr;
        }
        @Override
        protected Long doInBackground(String... strings) {
            InputStream is=null;
            FileOutputStream fos=null;

            try {
                //获得url
                URL serviceUrl=new URL(serviceUrlStr);
                //通过url打开连接
                HttpURLConnection conn=(HttpURLConnection)serviceUrl.openConnection();
                //判断连接是否成功
                int code=conn.getResponseCode();
                if(code==200){
                    //获得文件输出流
                    File file=new File(localUrlStr);
                    fos=new FileOutputStream(file);

                    is=conn.getInputStream();
                    int length=0;
                    byte[] buffer=new byte[1024];
                    while((length=is.read(buffer))!=-1){
                        fos.write(buffer,0,length);
                    }
                    fos.flush();
                    Log.i(TAG,"图片成功存入"+localUrlStr);
                }else{
                    Toast.makeText(MainActivity.this,"连接失败！",Toast.LENGTH_SHORT);
                }
            } catch (Exception e) {
                Log.i(TAG,"图片下载失败");
                e.printStackTrace();
            }finally {
                try{
                    if(is!=null){
                        is.close();
                    }
                    if(fos!=null){
                        fos.close();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            return null;
        }
    }


}
//public class MainActivity extends AppCompatActivity {
//
//    private Context mContext;
//   // private ImageView image;
//    // 加载成功
//    private static final int LOAD_SUCCESS = 1;
//    // 加载失败
//    private static final int LOAD_ERROR = -1;
//    // 用于异步的显示图片
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//
//            switch (msg.what) {
//                //下载成功
//                case LOAD_SUCCESS:
////                    // 获取图片的文件对象
////                    File file = new File(Environment.getExternalStorageDirectory(), "pic.jpg");
////                    FileInputStream fis = null;
////                    try {
////                        fis = new FileInputStream(file);
////                        Bitmap bitmap = BitmapFactory.decodeStream(fis);
////                        image.setImageBitmap(bitmap);
////
////                    } catch (FileNotFoundException e) {
////                        e.printStackTrace();
////                    }
//                    Toast.makeText(mContext, "下载成功",Toast.LENGTH_SHORT).show();
//                    break;
//                //下载失败
//                case LOAD_ERROR:
//
//                    Toast.makeText(mContext, "加载失败",Toast.LENGTH_SHORT).show();
//
//                    break;
//            }
//
//        };
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mContext = this;
//        setContentView(R.layout.activity_main);
//        //image = (ImageView) findViewById(R.id.image);
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        show();
//    }
//
//    // Button的点击事件
//    public void show() {
//        // 开启新的线程用于下载图片
//        new Thread(new Runnable() {
//            public void run() {
//                getPicture();
//            }
//        }).start();
//
//    }
//
//    //下载图片的主方法
//    private void getPicture() {
//
//        URL url = null;
//        InputStream is = null;
//        FileOutputStream fos = null;
//        try {
//            //构建图片的url地址
//            url = new URL("https://www.baidu.com/img/bd_logo1.png");
//            //开启连接
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            //设置超时的时间，5000毫秒即5秒
//        //    conn.setConnectTimeout(5000);
//            //设置获取图片的方式为GET
//         //   conn.setRequestMethod("GET");
//            //响应码为200，则访问成功
//            if (conn.getResponseCode() == 200) {
//                //获取连接的输入流，这个输入流就是图片的输入流
//                is = conn.getInputStream();
//                //构建一个file对象用于存储图片
//                File file = new File(Environment.getExternalStorageDirectory(), "pic.jpg");
//                fos = new FileOutputStream(file);
//                int len = 0;
//                byte[] buffer = new byte[1024];
//                //将输入流写入到我们定义好的文件中
//                while ((len = is.read(buffer)) != -1) {
//                    fos.write(buffer, 0, len);
//                }
//                //将缓冲刷入文件
//                fos.flush();
//                //告诉handler，图片已经下载成功
//                handler.sendEmptyMessage(LOAD_SUCCESS);
//            }
//        } catch (Exception e) {
//            //告诉handler，图片已经下载失败
//            handler.sendEmptyMessage(LOAD_ERROR);
//            e.printStackTrace();
//        } finally {
//            //在最后，将各种流关闭
//            try {
//                if (is != null) {
//                    is.close();
//                }
//                if (fos != null) {
//                    fos.close();
//                }
//            } catch (Exception e) {
//                handler.sendEmptyMessage(LOAD_ERROR);
//                e.printStackTrace();
//            }
//        }
//    }
//
//}
