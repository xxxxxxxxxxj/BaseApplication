package com.example.baseapplication.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/4/11 11:28
 */
public class MApplication extends Application {
    // IWXAPI 是第三方app和微信通信的openApi接口
    private IWXAPI api;

    @Override
    public void onCreate() {
        super.onCreate();
        //注册微信
        regToWx();
    }

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(AppConfig.WX_ID);
        //建议动态监听微信启动广播进行注册到微信
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 将该app注册到微信
                api.registerApp(AppConfig.WX_ID);
            }
        }, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));
    }
}
