package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.ShopListActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.GridAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.Model;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class GridFragment extends Fragment {

    @InjectView(R.id.gridview)
    GridView gv;

    private View view;
    private int index = -1;
    private FragmentActivity context;
    private List<Model> modelList;
    private List<Model> newModels;
    private TextView no;

    private SharedPreferences userInfoSp;

    public static GridFragment newInstance(int index, ArrayList<Model> modelList) {
        GridFragment gf = new GridFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putParcelableArrayList("model", modelList);
        gf.setArguments(bundle);
        return gf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_grid, null);
        ButterKnife.inject(this, view);

        init();
        initGridView();

        return view;
    }

    private void init() {
        Bundle bundle = getArguments();
        index = bundle.getInt("index");

        newModels = new ArrayList<>();
        modelList = bundle.getParcelableArrayList("model");

        int last = 8 * index + 8;
        if (last >= modelList.size()) {
            newModels = modelList.subList((8 * index), (modelList.size()));

        } else {
            newModels = modelList.subList((8 * index), (last));
        }

        context = getActivity();
        userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
    }

    private void initGridView() {
        // 这里重新开辟一个地址空间，来保存list，否则会报ConcurrentModificationException错误
        final ArrayList<Model> text = new ArrayList<Model>();
        text.addAll(newModels);
        gv.setAdapter(new GridAdapter(getActivity(), text));

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                startActivity(new Intent(getActivity(), ShopListActivity.class)
                        .putExtra("title", text.get(position).getTitle())
                        .putExtra("type", text.get(position).getType())
                        .putExtra("locatedCity", text.get(position).getLocationCity())
                        .putExtra("latitude", userInfoSp.getString("latitude", ""))
                        .putExtra("longitude", userInfoSp.getString("longitude", "")));

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != view) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        ButterKnife.reset(this);
    }

}
