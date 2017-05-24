package cn.buildworld.onenet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;

import cn.buildworld.onenet.R;

/**
 * 作者：MiChong on 2017/5/24 0024 15:40
 * 邮箱：1564666023@qq.com
 */
public class TotalFragment extends Fragment{

    private LayoutInflater layoutInflater;
    private PullRefreshLayout layout;

    public static TotalFragment newInstance(){
        return new TotalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layoutInflater = inflater;
        View v = inflater.inflate(R.layout.totalfragment,container,false);
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
                    }
                }, 2000);
            }
        });

        return v;
    }
}
