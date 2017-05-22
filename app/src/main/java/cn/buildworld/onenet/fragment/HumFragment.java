package cn.buildworld.onenet.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
public class HumFragment extends Fragment {

    private LayoutInflater layoutInflater;
    String TAG = "湿度";

    private String h_value ;
    private String h_id ;
    private String h_time ;
    private String h_symbol;
    //获取指定数据
    private Function2<String> mQuerySingleDatastreamFunction = new Function2<String>() {
        @Override
        public void apply(String deviceId, String dataStreamId) {
            OneNetApi.querySingleDataStream(deviceId, dataStreamId, new Callback());
        }
    };

    //获取所有的数据
    private Function1<String> mQueryMultiDataStreamFunction = new Function1<String>() {
        @Override
        public void apply(String deviceId) {
            OneNetApi.queryMultiDataStreams(deviceId, new Callback());
        }
    };

    public static HumFragment newInstance() {
        return new HumFragment();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutInflater = inflater;
        View v = inflater.inflate(R.layout.humfragment,container,false);
        final String saveDeviceNum = Preferences.getInstance(getActivity()).getString(Preferences.Device_Num,null);
//        Button getHum = (Button) v.findViewById(R.id.getHum);
//        getHum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mQuerySingleDatastreamFunction.apply(saveDeviceNum,"humidity");
//            }
//        });

        return v;
    }

    //获取温湿度信息



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

//            showjson.setText(unit+value+time);

        }
        //DisplayApiRespActivity.actionDisplayApiResp(getActivity(), response);
    }

    //获取单个数据流
    private void getSingleData(String response) throws JSONException{
        JSONObject jsonObject = new JSONObject(response);
        JSONObject data = jsonObject.getJSONObject("data");
         h_value = data.getString("current_value");
         h_id = data.getString("id");
         h_time = data.getString("update_at");
         h_symbol = data.getString("unit_symbol");
        Log.i(TAG, "湿度："+h_id+"------"+h_time+"------"+h_value+h_symbol);

    }

//    //获取数据的集合
//    private void getAllData(String response) throws JSONException {
//        JSONObject jsonObject = new JSONObject(response);
//        JSONArray dataArray = jsonObject.getJSONArray("data");
//        JSONObject humidity = dataArray.getJSONObject(9);
//        JSONObject temp = dataArray.getJSONObject(4);
//
//        String h_value = humidity.getString("current_value");
//        String h_id = humidity.getString("id");
//        String h_time = humidity.getString("update_at");
//        String h_symbol = humidity.getString("unit_symbol");
//
//        String t_value = temp.getString("current_value");
//        String t_id = temp.getString("id");
//        String t_time = temp.getString("update_at");
//        String t_symbol = temp.getString("unit_symbol");
//
//        Log.i(TAG, "湿度："+h_id+"------"+h_time+"------"+h_value+h_symbol);
//        Log.i(TAG, "温度："+t_id+"------"+t_time+"------"+t_value+t_symbol);
//    }

    private class Callback implements OneNetApiCallback {
        @Override
        public void onSuccess(String response) {
            try {
                displayLog(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            Toast.makeText(getActivity(), "数据获取成功", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "数据获取成功");
        }

        @Override
        public void onFailed(Exception e) {
//            Toast.makeText(getActivity(), "数据获取失败", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "数据获取失败");
        }
    }



}
