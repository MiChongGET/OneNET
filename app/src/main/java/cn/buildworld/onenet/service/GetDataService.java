package cn.buildworld.onenet.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.buildworld.onenet.adapter.TotalAdapter;
import cn.buildworld.onenet.bean.TotalBean;
import cn.buildworld.onenet.receiver.DataChangeBroadcast;
import cn.buildworld.onenet.util.Preferences;

/**
 * 作者：MiChong on 2017/6/1 0001 10:33
 * 邮箱：1564666023@qq.com
 * 温湿度数据定时获取服务
 *
 */
public class GetDataService extends Service {
    private final String TAG = "服务：";
    private DataChangeBroadcast dataChangeBroadcast ;
    private Intent intent ;
    private int data = 1;
    private String getTime;
    private Preferences preferences;
    private String time_value;
    private String saveDeviceNum;
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //获取指定数据
    private GetDataService.Function2<String> mQuerySingleDatastreamFunction = new GetDataService.Function2<String>() {
        @Override
        public void apply(String deviceId, String dataStreamId) {
            OneNetApi.querySingleDataStream(deviceId, dataStreamId, new GetDataService.Callback());
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();

        dataChangeBroadcast = new DataChangeBroadcast();
        registerReceiver(dataChangeBroadcast,new IntentFilter("datachange"));
        saveDeviceNum = Preferences.getInstance(this).getString(Preferences.Device_Num,null);
        intent = new Intent("datachange");

        preferences = Preferences.getInstance(this);
        time_value = preferences.getString(Preferences.UpdateTime,null);

        new Thread(){
            @Override
            public void run() {
                while (true) {

                    try {
                        Thread.sleep(5000);
                        //mQuerySingleDatastreamFunction.apply(saveDeviceNum,"humidity");

                        if (data != 1 && data <10) {
                            Log.i(TAG, "run: " + "获取数据");
                            intent.putExtra("datachange",data);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//                            sendBroadcast(intent);
                        }
                        data++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(dataChangeBroadcast);
        dataChangeBroadcast = null;
    }

    //获取单个数据流
    private void getSingleData(String response) throws JSONException{
        JSONObject jsonObject = new JSONObject(response);
        JSONObject data = jsonObject.getJSONObject("data");

        getTime = data.getString("update_at");
        preferences.putString(Preferences.UpdateTime,getTime);



        Log.i(TAG, "时间更新"+getTime);


    }


    //获取制定数据接口
    interface Function2<T>{
        void apply(T t1,T t2);
    }
    private void displayLog(String response) throws JSONException {
        if ((response.startsWith("{") && response.endsWith("}")) || (response.startsWith("[") && response.endsWith("]"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();
            response = gson.toJson(jsonParser.parse(response));
            Log.i(TAG, response);

            //将取得数据进行解析
            getSingleData(response);


        }
    }

    private class Callback implements OneNetApiCallback {
        @Override
        public void onSuccess(String response) {
            try {
                displayLog(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "数据获取成功");
        }

        @Override
        public void onFailed(Exception e) {
            Log.i(TAG, "数据获取失败");
        }
    }


}
