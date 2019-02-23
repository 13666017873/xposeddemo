package com.example.administrator.xposeddemo.activity.adapter;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.xposeddemo.R;
import com.example.administrator.xposeddemo.bean.TimeBean;
import com.example.administrator.xposeddemo.utils.RecycleViewAdapter;
import com.example.administrator.xposeddemo.utils.TextWatchHelper;
import com.example.administrator.xposeddemo.utils.ViewHolder;

import java.util.List;

public class MainAdapter extends RecycleViewAdapter<TimeBean>{

    public MainAdapter(List<TimeBean> dataList) {
        super(dataList);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_my_select_time;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void convert(ViewHolder holder, final TimeBean timeBean, final int position) {

        TextView text = (TextView) holder.getView(R.id.text1);
        final EditText selectTimet = (EditText) holder.getView(R.id.select_time);

        selectTimet.setText(timeBean.getTime());
        text.setText("收取时间：");

        selectTimet.setTag(timeBean.getId());

        selectTimet.addTextChangedListener(new TextWatchHelper() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                timeBean.setTime(charSequence.toString());
                timeBean.update(timeBean.getId());
            }
        });
    }
}
