package com.example.baseapplication.mvp.view.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.example.baseapplication.R;
import com.example.baseapplication.toast.RingToast;
import com.example.baseapplication.util.CommonUtil;

import java.util.Arrays;
import java.util.Calendar;

import me.shaohui.bottomdialog.BottomDialog;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-11-02 16:47
 */
public class WheelBottomDialog extends BottomDialog implements View.OnClickListener {
    protected final static String TAG = WheelBottomDialog.class.getSimpleName();
    private Context context;
    private WheelView wv_birthday_month;
    private WheelView wv_birthday_year;
    private WheelView wv_birthday_day;
    private static final String[] months =
            {"1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    private String[] years = new String[20];
    private String[] days = null;
    private int selectedYearIndex;
    private int selectedMonthIndex;
    private int selectedDayIndex;
    private String selectYear;
    private String selectMonth;
    private String selectDay;

    public WheelBottomDialog() {
        context = getActivity();
    }

    public WheelBottomDialog(Context context) {
        this.context = context;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.layout_bottom_wheel;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        RingToast.show("选中的日期为：" + selectYear + selectMonth + selectDay);
    }

    @Override
    public void bindView(final View v) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        selectedYearIndex = years.length - 1;
        selectedMonthIndex = month;
        selectedDayIndex = date - 1;

        calendar.setTimeInMillis(System.currentTimeMillis());
        for (int i = (calendar.get(Calendar.YEAR) - 19); i <= calendar.get(Calendar.YEAR); i++) {
            years[i - (calendar.get(Calendar.YEAR) - 19)] = i + "年";
        }

        int countDay = CommonUtil.getMonthLastDay(Integer.parseInt(years[selectedYearIndex].replace("年", "")),
                Integer.parseInt(months[selectedMonthIndex].replace("月", "")));
        days = new String[countDay];
        for (int i = 0; i < days.length; i++) {
            days[i] = (i + 1) + "日";
        }

        wv_birthday_year = (WheelView) v.findViewById(R.id.wv_birthday_year);
        wv_birthday_month = (WheelView) v.findViewById(R.id.wv_birthday_month);
        wv_birthday_day = (WheelView) v.findViewById(R.id.wv_birthday_day);
        v.findViewById(R.id.btn_ageortt_bottom).setOnClickListener(this);
        v.findViewById(R.id.iv_ageortt_bottom_close).setOnClickListener(this);
        wv_birthday_year.setLineSpacingMultiplier(1.5f);
        wv_birthday_year.setTextSize(23 * getResources().getDisplayMetrics().density / 3);
        wv_birthday_year.setTextColorCenter(getResources().getColor(R.color.colorPrimaryDark));
        wv_birthday_year.setTextColorOut(getResources().getColor(R.color.colorPrimary));
        wv_birthday_year.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(years)));
        wv_birthday_year.setCyclic(false);//循环滚动
        wv_birthday_year.setDividerColor(getResources().getColor(R.color.colorPrimary));
        wv_birthday_year.smoothScroll(WheelView.ACTION.FLING);
        wv_birthday_month.setLineSpacingMultiplier(1.5f);
        wv_birthday_month.setTextSize(23 * getResources().getDisplayMetrics().density / 3);
        wv_birthday_month.setTextColorCenter(getResources().getColor(R.color.colorPrimaryDark));
        wv_birthday_month.setTextColorOut(getResources().getColor(R.color.colorPrimary));
        wv_birthday_month.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(months)));
        wv_birthday_month.setCyclic(false);//循环滚动
        wv_birthday_month.setDividerColor(getResources().getColor(R.color.colorPrimary));
        wv_birthday_month.smoothScroll(WheelView.ACTION.FLING);
        wv_birthday_day.setLineSpacingMultiplier(1.5f);
        wv_birthday_day.setTextSize(23 * getResources().getDisplayMetrics().density / 3);
        wv_birthday_day.setTextColorCenter(getResources().getColor(R.color.colorPrimaryDark));
        wv_birthday_day.setTextColorOut(getResources().getColor(R.color.colorPrimary));
        wv_birthday_day.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(days)));
        wv_birthday_day.setCyclic(false);//循环滚动
        wv_birthday_day.setDividerColor(getResources().getColor(R.color.colorPrimary));
        wv_birthday_day.smoothScroll(WheelView.ACTION.FLING);
        wv_birthday_year.setCurrentItem(selectedYearIndex);
        wv_birthday_month.setCurrentItem(selectedMonthIndex);
        wv_birthday_day.setCurrentItem(selectedDayIndex);
        wv_birthday_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                selectYear = years[index];
                int countDay = CommonUtil.getMonthLastDay(Integer.parseInt(years[index].replace("年", "")),
                        Integer.parseInt(months[wv_birthday_month.getCurrentItem()].replace("月", "")));
                days = new String[countDay];
                for (int i = 0; i < days.length; i++) {
                    days[i] = (i + 1) + "日";
                }
                wv_birthday_day.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(days)));
                wv_birthday_day.setCurrentItem(0);
            }
        });
        wv_birthday_month.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                selectMonth = months[index];
                int countDay = CommonUtil.getMonthLastDay(Integer.parseInt(years[wv_birthday_year.getCurrentItem()].replace("年", "")),
                        Integer.parseInt(months[index].replace("月", "")));
                days = new String[countDay];
                for (int i = 0; i < days.length; i++) {
                    days[i] = (i + 1) + "日";
                }
                wv_birthday_day.setAdapter(new ArrayWheelAdapter<String>(Arrays.asList(days)));
                wv_birthday_day.setCurrentItem(0);
            }
        });
        wv_birthday_day.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                selectDay = days[index];
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_ageortt_bottom_close:
                break;
            case R.id.btn_ageortt_bottom:
                break;
        }
        dismiss();
    }
}