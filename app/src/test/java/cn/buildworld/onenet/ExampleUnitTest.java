package cn.buildworld.onenet;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cn.buildworld.onenet.util.HttpUtils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void nettest() throws JSONException {
         List<String> time ;
         List<Double> value ;

        String result = HttpUtils.doGet("http://www.buildworld.cn/onenet/getdata.php");
        time = new ArrayList<String>();
        value = new ArrayList<Double>();

        JSONArray jsonArray = new JSONArray(result);
        JSONObject object;

        for (int i =0;i<jsonArray.length();i++){
            object = (JSONObject) jsonArray.get(i);
            time.add(object.getString("hum"));
            System.out.println(object.getString("hum"));
            value.add(Double.valueOf(object.getString("time")));
        }

//        String[] s = (String[]) time.toArray();
//        Double[] d = (Double[]) value.toArray();
        System.out.println(time.toString());
        System.out.println(value.toString());

    }

}