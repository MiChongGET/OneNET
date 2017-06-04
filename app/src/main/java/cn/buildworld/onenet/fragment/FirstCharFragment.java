package cn.buildworld.onenet.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.buildworld.onenet.R;
import cn.buildworld.onenet.bean.TotalBean;
import cn.buildworld.onenet.util.HttpUtils;
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
public class FirstCharFragment extends Fragment {
    private LayoutInflater intentFilter;
    private ViewPager viewPager;
    private String TAG = "湿度折线图";

    //折线图
    private LineChartView lineChart;
//    String[] date = {"10-22", "11-22", "12-22", "1-22", "6-22", "5-23", "5-22", "6-22", "5-23", "5-22"};//X轴的标注
    String[] date ;//X轴的标注
//    double[] score = {50.12, 42.5, 110, 33, 10, 74, 22, 18, 79, 20};//图表的数据点
    double[] score ;
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();

    //网络数据请求
//    private final String URL = "http://192.168.7.248/onenet/getdata.php";
    private final String URL = "http://www.buildworld.cn/onenet/getValue.php";
    private List<String> time ;
    private List value ;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 404){
                Toast.makeText(getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                return;
            }
            if (msg.arg1 == 0){
                //折线图功能模块的实现
                getAxisXLables();//获取x轴的标注
                getAxisPoints();//获取坐标点
                initLineChart();//初始化
            }

        }
    };


    public static FirstCharFragment newInstance() {
        return new FirstCharFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        intentFilter = inflater;
        View view = inflater.inflate(R.layout.firstcharfragment, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.f_viewpager);

        lineChart = (LineChartView) view.findViewById(R.id.line_chart);
        doAll();//网络处理


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
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//      line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
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
//        data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        // Y轴是根据数据的大小自动设置Y轴上限(在下面我会给出固定Y轴数据个数的解决方案)
        Axis axisY = new Axis();  //Y轴
        axisY.setName("湿度/℃");//y轴标注
        axisY.setTextSize(10);//设置字体大小
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


    public void doAll() {

        new Thread() {
            @Override
            public void run() {
                String result = HttpUtils.doGet(URL);

                    time = new ArrayList<String>();
                    value = new ArrayList<Double>();

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject object;
                    System.out.println(TAG+result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        object = (JSONObject) jsonArray.get(i);
                        System.out.println(TAG+Double.valueOf(object.getString("hum")));
                        value.add(Double.valueOf(object.getString("hum")));
                        time.add(String.valueOf(object.getString("time")));
                    }
//                    Log.i(TAG, "run: "+time.toString());
                    System.out.println(TAG+value.toString());
                    if (value != null && time != null) {

                        score = new double[value.size()];
                        for (int i = 0; i < value.size(); i++) {
                            score[i] = (double) value.get(i);
                        }

                        date = new String[time.size()];
                        for (int i = 0; i < time.size(); i++) {
                            date[i] = time.get(i);
                        }
                    }else {
                        date = new String[]{"10-22", "11-22", "12-22", "1-22", "6-22", "5-23", "5-22", "6-22", "5-23", "5-22"};
                        score = new double[]{50.12, 42.5, 110, 33, 10, 74, 22, 18, 79, 20};
                    }

                    System.out.println(TAG+score.length);
                    Message message = new Message();
                    message.arg1 = 0;
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