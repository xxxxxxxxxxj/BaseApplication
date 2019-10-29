package com.example.baseapplication.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.baseapplication.mvp.view.activity.WebViewActivity;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-23 11:02
 */
public class JumpToUtil {

    public static void jumpTo(Activity mActivity, int point, String backup) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (point) {
            case 1://跳转到其他页面
                break;
            case 2://跳转到webview
                intent.setClass(mActivity, WebViewActivity.class);
                bundle.putString(WebViewActivity.URL_KEY, backup);
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
                break;
            default:
                break;
        }
    }
}
