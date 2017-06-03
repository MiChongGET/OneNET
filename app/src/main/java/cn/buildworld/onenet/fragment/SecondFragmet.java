package cn.buildworld.onenet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.buildworld.onenet.R;
import cn.buildworld.onenet.adapter.MyFragmentPagerAdapter;

/**
 * 作者：MiChong on 2017/6/3 0003 11:52
 * 邮箱：1564666023@qq.com
 */
public class SecondFragmet extends Fragment {
    private LayoutInflater intentFilter;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private TempFragment tempFragment;
    private SecondCharFragment secondCharFragment;

    public static SecondFragmet newInstance(){
        return new SecondFragmet();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        intentFilter = inflater;
        View view = inflater.inflate(R.layout.secondfragment,container,false);
        viewPager = (ViewPager) view.findViewById(R.id.t_viewpager);
        fragments = new ArrayList<Fragment>();

        tempFragment = TempFragment.newInstance();
        secondCharFragment = SecondCharFragment.newInstance();

        fragments.add(tempFragment);
        fragments.add(secondCharFragment);

        viewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),fragments));
        viewPager.setCurrentItem(0);

        return view;
    }
}
