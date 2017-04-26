package com.zhenghui.zhqb.zhenghuiqianbaomember.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.zhenghui.zhqb.zhenghuiqianbaomember.Activity.ShopListActivity;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Adapter.GridAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.Model.Model;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;

import java.util.ArrayList;
import java.util.List;


public class GridFragment extends Fragment {

	private View view;
	private GridView gv;
	private int index = -1;
	private FragmentActivity context;
	private List<Model> modelList;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

			userInfoSp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);

		if (view == null) {
			context = getActivity();

			Bundle bundle = getArguments();
			index = bundle.getInt("index");
			modelList = bundle.getParcelableArrayList("model");

			List<Model> newModels;
			int last = 8 * index + 8;
			if (last >= modelList.size()) {
				newModels = modelList.subList((8 * index), (modelList.size()));

			} else {
				newModels = modelList.subList((8 * index), (last));
			}

			view = LayoutInflater.from(context).inflate(R.layout.fragment_grid,
					container, false);
			gv = (GridView) view.findViewById(R.id.gridview);

			// 这里重新开辟一个地址空间，来保存list，否则会报ConcurrentModificationException错误
			final ArrayList<Model> text = new ArrayList<Model>();
			text.addAll(newModels);
			gv.setAdapter(new GridAdapter(getActivity(), text));

			gv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

//					Toast.makeText(context,
//							"点击的item是" + text.get(position).getIndex(),
//							Toast.LENGTH_SHORT).show();

					startActivity(new Intent(getActivity(), ShopListActivity.class)
							.putExtra("title", text.get(position).getTitle())
							.putExtra("type", text.get(position).getType())
							.putExtra("locatedCity", text.get(position).getLocationCity())
							.putExtra("latitude", userInfoSp.getString("latitude",""))
							.putExtra("longitude", userInfoSp.getString("longitude","")));

				}
			});

		} else {
			Log.i("tag", "当前页数是" + index + "view不是null哦");
			ViewGroup root = (ViewGroup) view.getParent();
			if (root != null) {
				root.removeView(view);
			}
		}

		Log.i("tag", "当前页数是" + index);

		return view;
	}

}
