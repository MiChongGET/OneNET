package cn.buildworld.onenet.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.buildworld.onenet.receiver.DataChangeBroadcast;
import cn.buildworld.onenet.util.HttpUtils;
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
    private String URL;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 1){
//                mQuerySingleDatastreamFunction.apply(saveDeviceNum,"humidity");

                mQueryMultiDataStreamFunction.apply(saveDeviceNum);
                Log.i(TAG, "handleMessage: "+"服务判断数据是否发生了更新");

            }

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
//    //获取指定数据
//    private GetDataService.Function2<String> mQuerySingleDatastreamFunction = new GetDataService.Function2<String>() {
//        @Override
//        public void apply(String deviceId, String dataStreamId) {
//            OneNetApi.querySingleDataStream(deviceId, dataStreamId, new GetDataService.Callback());
//        }
//    };

    //获取所有的数据
    private GetDataService.Function1<String> mQueryMultiDataStreamFunction = new GetDataService.Function1<String>() {
        @Override
        public void apply(String deviceId) {
            OneNetApi.queryMultiDataStreams(deviceId, new GetDataService.Callback());
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
                        //每隔10秒执行一次
                        Thread.sleep(10000);

                        Message message = new Message();
                        message.arg1 = 1;
                        handler.sendMessage(message);

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

//    //获取单个数据流
//    private void getSingleData(String response) throws JSONException{
//        JSONObject jsonObject = new JSONObject(response);
//        JSONObject data = jsonObject.getJSONObject("data");
//
//        getTime = data.getString("update_at");
//
//        Log.i(TAG, "run: "+time_value +"-----"+getTime);
//        //判断时间是否更新了，如果更新了，则向温湿度模块发送广播，传递数据更新的消息
//        if (!(time_value.equals(getTime))){
//            Log.i(TAG, "run: "+"数据已经更新了");
//            preferences.putString(Preferences.UpdateTime,getTime);
//            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//        }
//
//    }

    //获取所有的数据的集合
    private void getAllData(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        JSONArray dataArray = jsonObject.getJSONArray("data");
        JSONObject humidity = dataArray.getJSONObject(9);
        JSONObject temp = dataArray.getJSONObject(4);

        String h_value = humidity.getString("current_value");
        String h_id = humidity.getString("id");
        String h_time = humidity.getString("update_at");
        String h_symbol = humidity.getString("unit_symbol");

        String t_value = temp.getString("current_value");
        String t_id = temp.getString("id");
        String t_time = temp.getString("update_at");
        String t_symbol = temp.getString("unit_symbol");

        Log.i(TAG, "湿度："+h_id+"------"+h_time+"------"+h_value+h_symbol);
        Log.i(TAG, "温度："+t_id+"------"+t_time+"------"+t_value+t_symbol);

        Log.i(TAG, "run: "+time_value +"-----"+h_time);
        //判断时间是否更新了，如果更新了，则向温湿度模块发送广播，传递数据更新的消息
        if (!(time_value.equals(h_time))){
            Log.i(TAG, "run: "+"数据已经更新了");
            //加判断，否则会出现空值导致total界面崩溃
            if (h_value != null && t_value != null) {
                updateData(h_value, t_value, h_time);
            }
            preferences.putString(Preferences.UpdateTime,h_time);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        }

    }


    //向服务器端写入更新的数据，用以将数据记录到自己的服务器中去
    private void updateData(String hum,String temp,String time) {
        //http://www.buildworld.cn/setdata.php?hum=66&temp=33&time=2017-06-03%2010:28:01
         URL = "http://www.buildworld.cn/onenet/setdata.php?hum="+hum+"&temp="+temp+"&time="+time;


        new Thread(){
            @Override
            public void run() {

                try {
                    String result = HttpUtils.doGet(URL);
                    if (result.equals("1")){
                        Log.i(TAG, "run: "+"数据插入到服务器成功！！！");
                    }
                    }catch (Exception e){
                        Log.i(TAG, "run: "+"服务器端网络异常！！！");
                    }
            }
        }.start();
    }

    //获取所有数据的接口
    interface Function1<T> {
        void apply(T t);
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
            getAllData(response);
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
