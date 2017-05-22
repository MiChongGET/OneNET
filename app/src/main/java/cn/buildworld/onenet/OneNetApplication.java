package cn.buildworld.onenet;

import android.app.Application;
import android.content.Intent;
import android.preference.Preference;

import com.chinamobile.iot.onenet.OneNetApi;

import cn.buildworld.onenet.util.Preferences;


/**
 * 作者：MiChong on 2017/5/22 0022 18:23
 * 邮箱：1564666023@qq.com
 */
public class OneNetApplication extends Application {

    @Override
    public void onCreate() {

        //初始化APP,获取并设置API_KEY
        super.onCreate();
        OneNetApi.init(this,true);

        String savedApiKey = Preferences.getInstance(this).getString(Preferences.API_KEY,null);
        String saveDeviceNum = Preferences.getInstance(this).getString(Preferences.Device_Num,null);

        if(savedApiKey != null){
            OneNetApi.setAppKey(savedApiKey);
        }else startActivity(new Intent(OneNetApplication.this,EditApikeyActivity.class));
    }
}
