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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import cn.buildworld.onenet.R;
import cn.buildworld.onenet.adapter.TotalAdapter;
import cn.buildworld.onenet.bean.TotalBean;
import cn.buildworld.onenet.util.HttpUtils;


/**
 * 作者：MiChong on 2017/5/24 0024 15:40
 * 邮箱：1564666023@qq.com
 */
public class TotalFragment extends Fragment{

    private LayoutInflater layoutInflater;
    private PullRefreshLayout layout;
    private String URL = "http://www.buildworld.cn/onenet/getdata.php";
    private String TAG = "数据集合";
    private List<TotalBean> listBean;
    private TotalBean bean;
    private TotalAdapter adapter;
    private ListView listView;
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 404){
                Toast.makeText(getActivity(), "网络超时", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = (Bundle) msg.obj;
            listBean = (List<TotalBean>) bundle.get("list");
            adapter = new TotalAdapter(getActivity(),listBean);
            listView.setAdapter(adapter);



        }
    };



    public static TotalFragment newInstance(){
        return new TotalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layoutInflater = inflater;

        View v = inflater.inflate(R.layout.totalfragment,container,false);

        listView = (ListView) v.findViewById(R.id.list_view);

        doAll();
        //下拉刷新
        layout= (PullRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // 刷新3秒完成
                        layout.setRefreshing(false);
                        doAll();
                    }
                }, 2000);
            }
        });

        return v;
    }

 public void doAll(){
     new Thread(){
         @Override
         public void run() {
             String result = HttpUtils.doGet(URL);

             try {
                 listBean = new ArrayList<TotalBean>();
                 JSONArray jsonArray = new JSONArray(result);
                 JSONObject object;

                 for (int i =0;i<jsonArray.length();i++){
                     object = (JSONObject) jsonArray.get(i);
                     bean = new TotalBean();
                     bean.setHum_value(object.getString("hum"));
                     bean.setTemp_value(object.getString("temp"));
                     bean.setTime(object.getString("time"));

                     listBean.add(bean);
                 }


                 Message message = new Message();
                 Bundle bundle = new Bundle();
                 bundle.putSerializable("list", (Serializable) listBean);
                 message = handler.obtainMessage(1,bundle);
                 handler.sendMessage(message);


             } catch (Exception e) {
                 Message message = handler.obtainMessage();
                 message.what = 404;
                 handler.sendMessage(message);
             }
         }
     }.start();




 }

}
