package cn.buildworld.onenet.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.buildworld.onenet.R;
import cn.buildworld.onenet.adapter.TotalAdapter;
import cn.buildworld.onenet.bean.DataPoints;
import cn.buildworld.onenet.bean.TotalBean;
import cn.buildworld.onenet.util.HttpUtils;
import cn.buildworld.onenet.util.Preferences;


/**
 * 作者：MiChong on 2017/5/24 0024 15:40
 * 邮箱：1564666023@qq.com
 */
public class TotalFragment extends Fragment {

    private LayoutInflater layoutInflater;
    private PullRefreshLayout layout;
    private String URL = "http://www.buildworld.xyz/onenet/getdata.php";
    private String TAG = "数据集合";
    private List<TotalBean> listBean;
    private TotalBean bean;
    private TotalAdapter adapter;
    private ListView listView;

    private String saveDeviceNum;


    public static TotalFragment newInstance() {
        return new TotalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layoutInflater = inflater;

        View v = inflater.inflate(R.layout.totalfragment, container, false);

        listView = (ListView) v.findViewById(R.id.list_view);

        saveDeviceNum = Preferences.getInstance(getActivity()).getString(Preferences.Device_Num, null);

        getTotalData(saveDeviceNum);

        //下拉刷新
        layout = (PullRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // 刷新3秒完成
                        layout.setRefreshing(false);
                        getTotalData(saveDeviceNum);

                    }
                }, 2000);
            }
        });

        return v;
    }


    private void getTotalData(String saveDeviceNum) {
        Map<String, String> map = new HashMap<String, String>();
        //设置我们要查询的数据流的名称
        map.put("datastream_id", "humidity,temperature");
        map.put("limit", "30");
        OneNetApi.queryDataPoints(saveDeviceNum, map, new OneNetApiCallback() {

            /**
             * 数据请求成功
             * @param response 返回来的json数据
             */
            @Override
            public void onSuccess(String response) {

                Log.i(TAG, "onSuccess: " + response);

                //使用谷歌的json解析框架来解析数据
                Gson gson = new Gson();
                //自己封装了结果集的实体类
                DataPoints dataPoints = gson.fromJson(response, DataPoints.class);

                System.out.println("获取数据点个数:" + dataPoints.getData().getCount());

                listBean = new ArrayList<TotalBean>();

                //温湿度数据和时间的集合
                List<DataPoints.DataBean.DatastreamsBean.DatapointsBean> tempdatapoints = dataPoints.getData().getDatastreams().get(0).getDatapoints();
                List<DataPoints.DataBean.DatastreamsBean.DatapointsBean> humdatapoints = dataPoints.getData().getDatastreams().get(1).getDatapoints();

                //充值数据,封装为自定义的数据集合
                for (int i = 0; i < 30; i++) {
                    bean = new TotalBean();
                    bean.setHum_value(humdatapoints.get(i).getValue());
                    bean.setTemp_value(tempdatapoints.get(i).getValue());
                    bean.setTime(tempdatapoints.get(i).getAt());
                    listBean.add(bean);
                }

                adapter = new TotalAdapter(getActivity(), listBean);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailed(Exception e) {
                //当获取数据失败的时候
                System.out.println("e = " + e);
            }
        });
    }
}
