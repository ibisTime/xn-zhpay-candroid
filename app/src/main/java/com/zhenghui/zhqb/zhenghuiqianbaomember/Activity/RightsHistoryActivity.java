package com.zhenghui.zhqb.zhenghuiqianbaomember.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhenghui.zhqb.zhenghuiqianbaomember.adapter.RightsListAdapter;
import com.zhenghui.zhqb.zhenghuiqianbaomember.model.RightsListModel;
import com.zhenghui.zhqb.zhenghuiqianbaomember.R;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.RefreshLayout;
import com.zhenghui.zhqb.zhenghuiqianbaomember.util.Xutil;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RightsHistoryActivity extends MyBaseActivity implements SwipeRefreshLayout.OnRefreshListener, RefreshLayout.OnLoadListener {

    @InjectView(R.id.layout_back)
    LinearLayout layoutBack;
    @InjectView(R.id.txt_confirm)
    TextView txtConfirm;
    @InjectView(R.id.txt_start)
    TextView txtStart;
    @InjectView(R.id.txt_end)
    TextView txtEnd;
    @InjectView(R.id.list_rights)
    ListView listRights;
    @InjectView(R.id.swipe_container)
    RefreshLayout swipeContainer;

    List<RightsListModel> list;
    RightsListAdapter adapter;

    private String code;

    private int page = 1;
    private int pageSize = 10;

    private Calendar calendar;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rights_history);
        ButterKnife.inject(this);
        inits();
        initDate();
        initsListView();
        initRefreshLayout();

    }

    private void inits() {
        list = new ArrayList<>();
        adapter = new RightsListAdapter(this, list);

        calendar = Calendar.getInstance();
        calendar.roll(Calendar.DATE, -1);//日期回滚1天
        date = calendar.getTime();

        code = getIntent().getStringExtra("code");

    }

    private void initDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.roll(Calendar.DATE, -7);
        txtStart.setText(sdf.format(startCalendar.getTime()));

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.roll(Calendar.DATE, -1);
        txtEnd.setText(sdf.format(endCalendar.getTime()));

        getData();
    }

    private void initsListView() {
        listRights.setAdapter(adapter);
    }

    private void initRefreshLayout() {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnLoadListener(this);
        swipeContainer.setOnRefreshListener(this);
    }

    @OnClick({R.id.layout_back, R.id.txt_start, R.id.txt_end, R.id.txt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;

            case R.id.txt_start:
                DatePickerDialog dialog = new DatePickerDialog(RightsHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        if ((month + 1) < 10) {
                            if (day < 10) {
                                txtStart.setText(year + "-0" + (month + 1) + "-0" + day);
                            } else {
                                txtStart.setText(year + "-0" + (month + 1) + "-" + day);
                            }
                        } else {
                            if (day < 10) {
                                txtStart.setText(year + "-" + (month + 1) + "-0" + day);
                            } else {
                                txtStart.setText(year + "-" + (month + 1) + "-" + day);
                            }
                        }
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        Date date = null;
//                        try{
//                            date = sdf.parse(txtStart.getText().toString());//初始日期
//                            endStart.setTime(date);
//                            endStart.set(Calendar.DATE, endStart.get(Calendar.DATE) - 6);
//                            endDate = endStart.getTime();
//                            txtEnd.setText(new SimpleDateFormat("yyyy-MM-dd").format(endDate));
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                DatePicker datePicker = dialog.getDatePicker();
                datePicker.setMaxDate(date.getTime());
                dialog.show();

                break;

            case R.id.txt_end:
                DatePickerDialog endDialog = new DatePickerDialog(RightsHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        if ((month + 1) < 10) {
                            if (day < 10) {
                                txtEnd.setText(year + "-0" + (month + 1) + "-0" + day);
                            } else {
                                txtEnd.setText(year + "-0" + (month + 1) + "-" + day);
                            }
                        } else {
                            if (day < 10) {
                                txtEnd.setText(year + "-" + (month + 1) + "-0" + day);
                            } else {
                                txtEnd.setText(year + "-" + (month + 1) + "-" + day);
                            }
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                DatePicker endPicker = endDialog.getDatePicker();
                endPicker.setMaxDate(date.getTime());
                endDialog.show();

                break;

            case R.id.txt_confirm:
                if (check()) {
                    getData();
                }
                break;
        }
    }

    private boolean check() {
        if (txtStart.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请选择起始时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (txtEnd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!checkDate()) {
            Toast.makeText(this, "起始时间不能大于结束时间", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean checkDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date endDate;
        Date startDate;

        try {
            endDate = sdf.parse(txtEnd.getText().toString());
            startDate = sdf.parse(txtStart.getText().toString());
            if (startDate.after(endDate)) {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void getData() {
        Calendar lastDate = Calendar.getInstance();
        lastDate.roll(Calendar.DATE, -7);//日期回滚7天

        JSONObject object = new JSONObject();
        try {
            object.put("token", userInfoSp.getString("token", null));
            object.put("fundCode", "");
            object.put("stockCode", code);
            object.put("toUser", userInfoSp.getString("userId", ""));
            object.put("dateStart", txtStart.getText().toString());
            object.put("dateEnd", txtEnd.getText().toString());
            object.put("start", page);
            object.put("limit", pageSize);
            object.put("orderColumn", "");
            object.put("orderDir", "");
            object.put("companyCode", appConfigSp.getString("systemCode", null));
            object.put("systemCode", appConfigSp.getString("systemCode", null));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Xutil().post("808428", object.toString(), new Xutil.XUtils3CallBackPost() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);

                    Gson gson = new Gson();
                    ArrayList<RightsListModel> lists = gson.fromJson(jsonObject.getJSONArray("list").toString(), new TypeToken<ArrayList<RightsListModel>>() {
                    }.getType());

                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(lists);

                    if (list.size() == 0) {
                        Toast.makeText(RightsHistoryActivity.this, "暂无历史记录", Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onTip(String tip) {
                Toast.makeText(RightsHistoryActivity.this, tip, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error, boolean isOnCallback) {
                Toast.makeText(RightsHistoryActivity.this, "无法连接服务器，请检查网络", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
                page = 1;
                getData();
            }
        }, 1500);
    }

    @Override
    public void onLoad() {
        swipeContainer.postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeContainer.setLoading(false);
                page = page + 1;
                getData();


            }
        }, 1500);
    }
}
