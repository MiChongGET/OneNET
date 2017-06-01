package cn.buildworld.onenet.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 作者：MiChong on 2017/6/1 0001 11:56
 * 邮箱：1564666023@qq.com
 */
public class DataChangeBroadcast extends BroadcastReceiver {
    private final String TAG = "广播";
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "onReceive: "+"数据更新广播");
    }
}
