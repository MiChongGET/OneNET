package cn.buildworld.onenet.bean;

/**
 * 作者：MiChong on 2017/5/25 0025 15:27
 * 邮箱：1564666023@qq.com
 */
public class TotalBean {
    private String hum_value;
    private String temp_value;
    private String time;


    public String getHum_value() {
        return hum_value;
    }

    public void setHum_value(String hum_value) {
        this.hum_value = hum_value;
    }

    public String getTemp_value() {
        return temp_value;
    }

    public void setTemp_value(String temp_value) {
        this.temp_value = temp_value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TotalBean{" +
                "hum_value='" + hum_value + '\'' +
                ", temp_value='" + temp_value + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
