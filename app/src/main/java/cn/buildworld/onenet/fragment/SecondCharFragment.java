package cn.buildworld.onenet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.buildworld.onenet.R;

/**
 * 作者：MiChong on 2017/6/3 0003 11:52
 * 邮箱：1564666023@qq.com
 */
public class SecondCharFragment extends Fragment {
    private LayoutInflater intentFilter;
    private ViewPager viewPager;


    public static SecondCharFragment newInstance(){
        return new SecondCharFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        intentFilter = inflater;
        View view = inflater.inflate(R.layout.secondcharfragment,container,false);
        viewPager = (ViewPager) view.findViewById(R.id.f_viewpager);

        return view;
    }
}
