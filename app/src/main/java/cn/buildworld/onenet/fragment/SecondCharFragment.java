package cn.buildworld.onenet.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chinamobile.iot.onenet.OneNetApi;
import com.chinamobile.iot.onenet.OneNetApiCallback;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.buildworld.onenet.R;
import cn.buildworld.onenet.bean.DataPoints;
import cn.buildworld.onenet.util.HttpUtils;
import cn.buildworld.onenet.util.Preferences;
import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 作者：MiChong on 2017/6/3 0003 11:52
 * 邮箱：1564666023@qq.com
 */
public class SecondCharFragment extends Fragment {
    private LayoutInflater intentFilter;
    private ViewPager viewPager;
    private String TAG = "温度折线图";

    private String saveDeviceNum;

    //折线图
    private LineChartView lineChart;
    //    String[] date = {"10-22", "11-22", "12-22", "1-22", "6-22", "5-23", "5-22", "6-22", "5-23", "5-22"};//X轴的标注
    String[] date ;//X轴的标注
    //    double[] score = {50.12, 42.5, 110, 33, 10, 74, 22, 18, 79, 20};//图表的数据点
    double[] score ;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();



    public SecondCharFragment() {
    }

    public static SecondCharFragment newInstance(){
        return new SecondCharFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        intentFilter = inflater;
        View view = inflater.inflate(R.layout.secondcharfragment,container,false);
        viewPager = (ViewPager) view.findViewById(R.id.f_viewpager);

        lineChart = (LineChartView) view.findViewById(R.id.line_chart);

        saveDeviceNum = Preferences.getInstance(getActivity()).getString(Preferences.Device_Num,null);

        //获取数据点的数据
        String savedApiKey = Preferences.getInstance(getActivity()).getString(Preferences.API_KEY,null);
        OneNetApi.setAppKey(savedApiKey);
        Map<String,String> map = new HashMap<String, String>();
        //设置我们要查询的数据流的名称
        map.put("datastream_id","temperature");
        map.put("limit","20");
        OneNetApi.queryDataPoints(saveDeviceNum, map, new OneNetApiCallback() {

            /**
             * 数据请求成功
             * @param response 返回来的json数据
             */
            @Override
            public void onSuccess(String response) {

                //使用谷歌的json解析框架来解析数据
                Gson gson = new Gson();
                //自己封装了结果集的实体类
                DataPoints dataPoints = gson.fromJson(response, DataPoints.class);

                System.out.println("获取数据点个数:"+dataPoints.getData().getCount());

                //结果的个数
                int count = dataPoints.getData().getCount();

                //初始化数组
                date = new String[count];
                score = new double[count];

                //温湿度数据和时间的集合
                List<DataPoints.DataBean.DatastreamsBean.DatapointsBean> datapoints = dataPoints.getData().getDatastreams().get(0).getDatapoints();
                for (int i = 0;i<datapoints.size();i++){
                    date[i] = datapoints.get(i).getAt();
                    score[i] = Double.parseDouble(datapoints.get(i).getValue());
                }

                getAxisXLables();//获取x轴的标注
                getAxisPoints();//获取坐标点
                initLineChart();//初始化
            }

            @Override
            public void onFailed(Exception e) {

                //当获取数据失败的时候
                System.out.println("e = " + e);
                date = new String[]{"10-22", "11-22", "12-22", "1-22", "6-22", "5-23", "5-22", "6-22", "5-23", "5-22"};
                score = new double[]{50.12, 42.5, 110, 33, 10, 74, 22, 18, 79, 20};
                getAxisXLables();//获取x轴的标注
                getAxisPoints();//获取坐标点
                initLineChart();//初始化
            }
        });

        return view;
    }
    /**
     * 设置X 轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < date.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, (float) score[i]));
        }
    }

    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(true);//曲线是否平滑，即是曲线还是折线
        line.setFilled(true);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        LineChartValueFormatter chartValueFormatter = new SimpleLineChartValueFormatter(2);
        line.setFormatter(chartValueFormatter);//显示小数点

        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("日期/t");  //表格名称
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(8); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("温度/℃");//y轴标注
        axisY.setTextSize(10);//设置字体大小
        axisY.setMaxLabelChars(8);
        axisY.setTextColor(Color.BLACK);
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom((float) 2);//最大方法比例
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注：下面的7，10只是代表一个数字去类比而已
         * 当时是为了解决X轴固定数据个数。见（http://forum.xda-developers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }


}
