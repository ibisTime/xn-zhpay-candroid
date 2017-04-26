package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.DetailAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.GoodsModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by dell1 on 2016/12/13.
 */

public class DetailFragment extends Fragment {

    TextView txtDescription;
    @InjectView(R.id.list)
    ListView list;

    private DetailAdapter adapter;

    private View view;
    private View headView;
    private GoodsModel model;


    public static DetailFragment newInstance(GoodsModel model) {
        DetailFragment fragment = new DetailFragment();

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
        view = inflater.inflate(R.layout.fragment_detail, null);
        ButterKnife.inject(this, view);

        Bundle args = getArguments();
        if (args != null) {
//            picture = args.getString("pic");
//            description = args.getString("description");
            model = (GoodsModel) args.getSerializable("model");
        }

        initHeaderView(inflater);
        inits();


        return view;
    }

    private void initHeaderView(LayoutInflater inflater) {
        headView = inflater.inflate(R.layout.header_detail, null);
        txtDescription = (TextView) headView.findViewById(R.id.txt_description);
    }

    private void inits() {
        txtDescription.setText(model.getDescription());

        String[] pic = model.getPic().split("\\|\\|");

        adapter = new DetailAdapter(getActivity(), pic);
        list.setAdapter(adapter);
        list.addHeaderView(headView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
