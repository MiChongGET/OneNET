package cn.buildworld.onenet.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import cn.buildworld.onenet.R;
import cn.buildworld.onenet.util.Preferences;

/**
 * 作者：MiChong on 2017/5/22 0022 17:27
 * 邮箱：1564666023@qq.com
 */
public class TempFragment extends Fragment {

    private LayoutInflater layoutInflater;
    private String TAG = "温度";

    private String t_value ;
    private String t_id ;
    private String t_time ;
    private String t_symbol;
    private TextView t_temp;
    private TextView temp_time;
    private  String saveDeviceNum;

    private TextView temp_state;
    private ImageView img_state;

    //广播接收处理数据
    private LocalBroadcastManager broadcastManager;
    private IntentFilter intentFilter;
    private BroadcastReceiver receiver;

    //获取指定数据
    private TempFragment.Function2<String> mQuerySingleDatastreamFunction = new Function2<String>() {
        @Override
        public void apply(String deviceId, String dataStreamId) {
            OneNetApi.querySingleDataStream(deviceId, dataStreamId, new Callback());
        }
    };

    //获取所有的数据
    private TempFragment.Function1<String> mQueryMultiDataStreamFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            OneNetApi.queryMultiDataStreams(deviceId, new Callback());
        }
    };

    public static TempFragment newInstance() {
        return new TempFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutInflater = inflater;
        View v = inflater.inflate(R.layout.tempfragment,container,false);
         saveDeviceNum = Preferences.getInstance(getActivity()).getString(Preferences.Device_Num,null);

        t_temp = (TextView) v.findViewById(R.id.t_temp);
        temp_time = (TextView) v.findViewById(R.id.t_time);
        temp_state = (TextView) v.findViewById(R.id.temp_state);
        img_state = (ImageView) v.findViewById(R.id.img_state);


        mQuerySingleDatastreamFunction.apply(saveDeviceNum,"temperature");
        return v;
    }


    //获取广播信息
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //处理广播
        broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        intentFilter = new IntentFilter();
        intentFilter.addAction("datachange");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "接收到了广播");
                mQuerySingleDatastreamFunction.apply(saveDeviceNum,"temperature");
            }
        };
        broadcastManager.registerReceiver(receiver,intentFilter);
    }

    //注销广播


    @Override
    public void onDestroy() {
        super.onDestroy();
        broadcastManager.unregisterReceiver(receiver);
    }

    //获取温湿度信息
    //获取单个数据流
    private void getSingleData(String response) throws JSONException{
        JSONObject jsonObject = new JSONObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
        t_value = data.getString("current_value");
        t_id = data.getString("id");
        t_time = data.getString("update_at");
        t_symbol = data.getString("unit_symbol");
        Log.i(TAG, "湿度："+t_id+"------"+t_time+"------"+t_value+t_symbol);

        if (t_value != null){
            double hum = Double.parseDouble(t_value);
            if (hum > 35) {
                img_state.setImageResource(R.drawable.danger);
                temp_state.setText("危险！温度过高");
            } else if (hum <= 35 && hum > 20) {
                img_state.setImageResource(R.drawable.attention);
                temp_state.setText("警告！温度较高");
            } else img_state.setImageResource(R.drawable.safe);
        }

        t_temp.setText(t_value+t_symbol);
        temp_time.setText("更新时间："+t_time);

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
