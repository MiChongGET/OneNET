package cn.buildworld.onenet.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.buildworld.onenet.R;
import cn.buildworld.onenet.bean.TotalBean;

/**
 * 作者：MiChong on 2017/5/24 0024 15:54
 * 邮箱：1564666023@qq.com
 */
public class TotalAdapter extends BaseAdapter {

    private Context context;
    private List<TotalBean> totalBeen;
    private TotalBean bean;



    public TotalAdapter(Context context, List<TotalBean> totalBeen) {
        this.context = context;
        this.totalBeen = totalBeen;
    }

    @Override
    public int getCount() {
        return totalBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return totalBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        bean = totalBeen.get(position);

        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.total_list_item,null,false);
            viewHolder.temp_value = (TextView)convertView.findViewById(R.id.total_temp);
            viewHolder.hum_value = (TextView) convertView.findViewById(R.id.total_hum);
            viewHolder.time = (TextView) convertView.findViewById(R.id.total_time);
            viewHolder.temp_sign = (ImageView) convertView.findViewById(R.id.temp_sign);
            viewHolder.hum_sign = (ImageView) convertView.findViewById(R.id.hum_sign);


            convertView.setTag(viewHolder);


        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.temp_value.setText("温度:"+bean.getTemp_value()+"℃");
        viewHolder.hum_value.setText("湿度:"+bean.getHum_value()+"%");
        viewHolder.time.setText("更新时间:"+bean.getTime());


        Log.i("单个温湿度数据", "getView: "+bean.getTemp_value()+"  "+bean.getHum_value());

        double temp = Double.parseDouble(bean.getTemp_value());
        double hum = Double.parseDouble(bean.getHum_value());

        if ( temp> 35){
            viewHolder.temp_sign.setImageResource(R.drawable.danger);
        }
        else if (temp<=35 && temp >20){
            viewHolder.temp_sign.setImageResource(R.drawable.attention);
        }
        else viewHolder.temp_sign.setImageResource(R.drawable.safe);

        if (hum>70){
            viewHolder.hum_sign.setImageResource(R.drawable.danger);
        }
        else if (hum<=70 && hum>50){
            viewHolder.hum_sign.setImageResource(R.drawable.attention);
        }else viewHolder.hum_sign.setImageResource(R.drawable.safe);



        return convertView;
    }

    class ViewHolder{
        private TextView temp_value;
        private TextView hum_value;
        private TextView time;
        private ImageView temp_sign;
        private ImageView hum_sign;
    }

}
