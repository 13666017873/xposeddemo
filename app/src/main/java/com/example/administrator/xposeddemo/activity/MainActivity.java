package com.example.administrator.xposeddemo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.xposeddemo.MyApplication;
import com.example.administrator.xposeddemo.R;
import com.example.administrator.xposeddemo.activity.adapter.MainAdapter;
import com.example.administrator.xposeddemo.bean.TimeBean;
import com.example.administrator.xposeddemo.service.AccessibilityServiceMonitor;
import com.example.administrator.xposeddemo.service.AlipayForestMonitor;
import com.example.administrator.xposeddemo.utils.Config;
import com.example.administrator.xposeddemo.utils.ShareUtil;

import org.litepal.LitePal;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.xml.transform.TransformerException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String date;

    private Handler handler = new Handler();
    private Runnable runnable;


    private static final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ShareUtil shareUtil = new ShareUtil(MyApplication.getContext());
        shareUtil.setShare(Config.APP_ALIPAY_FOREST, true);

        this.initView();
    }

    @SuppressLint("SimpleDateFormat")
    private void initView() {

        TextView start = findViewById(R.id.activity_main_start_listerner);
        TextView end = findViewById(R.id.activity_main_end_listerner);
        start.setOnClickListener(this);
        end.setOnClickListener(this);

        List<TimeBean> timeBeans = LitePal.findAll(TimeBean.class);

        if (timeBeans.size() == 0) {

            TimeBean timeBean = new TimeBean();
            timeBean.setId(0);
            timeBean.setTime("03:00");
            timeBean.save();

            TimeBean timeBean1 = new TimeBean();
            timeBean1.setId(1);
            timeBean1.setTime("07:00");
            timeBean1.save();

            TimeBean timeBean2 = new TimeBean();
            timeBean2.setId(2);
            timeBean2.setTime("23:00");
            timeBean2.save();
        }

        final MainAdapter adapter = new MainAdapter(LitePal.findAll(TimeBean.class));
        RecyclerView recyclerView = findViewById(R.id.activity_main_rv);
        recyclerView.setAdapter(adapter);

        runnable = new Runnable() {
            public void run() {

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minu = calendar.get(Calendar.MINUTE);

                Log.d("Date", "获取时间小时：" + hour + "分钟：" + minu);
                List<TimeBean> timeBeans = LitePal.findAll(TimeBean.class);
                if (timeBeans.size() != 0) {
                    for (TimeBean timeBean : timeBeans) {
                        String honr1 = timeBean.getTime().substring(0, 2);
                        String minu1 = timeBean.getTime().substring(3, 5);

                        if(Integer.parseInt(honr1) <= 9){
                            honr1 = honr1.substring(1,2);
                        }
                        if(Integer.parseInt(minu1) <= 9){
                            minu1 = minu1.substring(1,2);
                        }

                        ShareUtil shareUtil = new ShareUtil(MyApplication.getContext());
                        shareUtil.setShare(Config.KEY_HOUR, honr1);
                        shareUtil.setShare(Config.KEY_MINUTE, minu1);
                        Log.d("TIMEBEAN", "获取时间小时：" + honr1 + "分钟：" + minu1);
                    }
                }
                handler.postDelayed(this, 30000);
            }
        };
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.activity_main_start_listerner) {
            handler.postDelayed(runnable, 1000); // 开始Timer
            Toast.makeText(this, "开始监听",Toast.LENGTH_SHORT).show();
        }
        if (view.getId() == R.id.activity_main_end_listerner) {
            handler.removeCallbacks(runnable); //停止Timer
            Toast.makeText(this, "结束监听",Toast.LENGTH_SHORT).show();
        }
    }
}
