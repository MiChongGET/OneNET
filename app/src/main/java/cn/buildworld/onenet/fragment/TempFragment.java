package cn.buildworld.onenet.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.buildworld.onenet.R;

/**
 * 作者：MiChong on 2017/5/22 0022 17:27
 * 邮箱：1564666023@qq.com
 */
public class TempFragment extends Fragment {

    private LayoutInflater layoutInflater;

    public static TempFragment newInstance() {
        return new TempFragment();
    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
//
//        layoutInflater = inflater;
//        View v = inflater.inflate(R.layout.tempfragment,container,false);
//
//        return v;
//    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutInflater = inflater;
        View v = inflater.inflate(R.layout.tempfragment,container,false);

        return v;
    }
}
