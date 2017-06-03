package cn.buildworld.onenet.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;

import cn.buildworld.onenet.R;
import cn.buildworld.onenet.adapter.MyFragmentPagerAdapter;


/**
 * 作者：MiChong on 2017/6/3 0003 11:52
 * 邮箱：1564666023@qq.com
 */
public class FirstFragmet extends Fragment {
    private LayoutInflater intentFilter;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private HumFragment humFragment;
    private FirstCharFragment charFragment;


    public static FirstFragmet newInstance(){
        return new FirstFragmet();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        intentFilter = inflater;
        View view = inflater.inflate(R.layout.firstfragment,container,false);
        viewPager = (ViewPager) view.findViewById(R.id.f_viewpager);
        fragments = new ArrayList<Fragment>();
        humFragment = HumFragment.newInstance();
        charFragment = FirstCharFragment.newInstance();

        fragments.add(humFragment);
        fragments.add(charFragment);

        viewPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),fragments));
        viewPager.setCurrentItem(0);

        return view;
    }


}
