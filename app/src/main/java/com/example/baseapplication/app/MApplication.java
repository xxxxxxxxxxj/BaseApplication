package com.example.baseapplication.app;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.toast.CustomToastStyle;
import com.example.baseapplication.toast.RingToast;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.converter.SerializableDiskConverter;
import com.zhouyou.http.utils.HttpLog;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

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
        //RingLog
        RingLog.init(AppConfig.isShowLog);
        //RingToast
        RingToast.init(this);
        RingToast.initStyle(new CustomToastStyle(this));
        EasyHttp.init(this);
        EasyHttp.getInstance()
                .debug(AppConfig.KEYWORD, AppConfig.isShowLog)
                .setReadTimeOut(60 * 1000)
                .setWriteTimeOut(60 * 1000)
                .setConnectTimeout(60 * 1000)
                .setRetryCount(3)//默认网络不好自动重试3次
                .setRetryDelay(500)//每次延时500ms重试
                .setRetryIncreaseDelay(500)//每次延时叠加500ms
                .setBaseUrl(UrlConstants.getServiceBaseUrl())
                .setCacheDiskConverter(new SerializableDiskConverter())//默认缓存使用序列化转化
                .setCacheMaxSize(50 * 1024 * 1024)//设置缓存大小为50M
                .setCacheVersion(1)//缓存版本为1
                .setHostnameVerifier(new UnSafeHostnameVerifier(UrlConstants.getServiceBaseUrl()))//全局访问规则
                .setCertificates()//信任所有证书
                //.addConverterFactory(GsonConverterFactory.create(gson))//本框架没有采用Retrofit的Gson转化，所以不用配置
                .addCommonHeaders(UrlConstants.getHeaders(this));//设置全局公共头
        //.addCommonParams(params);//设置全局公共参数
        //.addInterceptor(new HeTInterceptor());//处理自己业务的拦截器
    }

    public class UnSafeHostnameVerifier implements HostnameVerifier {
        private String host;

        public UnSafeHostnameVerifier(String host) {
            this.host = host;
            HttpLog.i("###############　UnSafeHostnameVerifier " + host);
        }

        @Override
        public boolean verify(String hostname, SSLSession session) {
            HttpLog.i("############### verify " + hostname + " " + this.host);
            if (this.host == null || "".equals(this.host) || !this.host.contains(hostname))
                return false;
            return true;
        }
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
