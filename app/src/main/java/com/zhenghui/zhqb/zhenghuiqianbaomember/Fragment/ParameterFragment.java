package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.ParameterAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.GoodsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by LeiQ on 2017/4/6.
 */

public class ParameterFragment extends Fragment {

    @InjectView(R.id.list)
    ListView listView;

    private View view;
    private GoodsModel model;

    private ParameterAdapter adapter;
    private List<GoodsModel.ProductSpecsBean> list;

    public static ParameterFragment newInstance(GoodsModel model) {
        ParameterFragment fragment = new ParameterFragment();

        Bundle bundle = new Bundle();
//        bundle.putString("description", description);
//        bundle.putString("pic", pic);
        bundle.putSerializable("model", model);
        fragment.setArguments(bundle);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_parameter, null);
        ButterKnife.inject(this, view);

        Bundle args = getArguments();
        if (args != null) {
            model = (GoodsModel) args.getSerializable("model");
        }

        inits();
        initListView();

        return view;
    }

    private void inits() {
        list = new ArrayList<>();

        if(model.getProductSpecs() != null){
            list.addAll(model.getProductSpecs());
        }

        adapter = new ParameterAdapter(getActivity(), list);
    }

    private void initListView() {
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
