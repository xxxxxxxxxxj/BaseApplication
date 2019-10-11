
package com.example.baseapplication.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.baseapplication.app.AppConfig;
import com.example.baseapplication.toast.RingToast;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_ID, false);
        api.handleIntent(getIntent(), this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        String result = "";
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "分享成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "分享取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "分享被拒绝";
                break;
            default:
                result = "分享返回";
                break;
        }
        RingToast.show(result);
        finish();
    }

    @Override
    public void onReq(BaseReq arg0) {

    }
}
