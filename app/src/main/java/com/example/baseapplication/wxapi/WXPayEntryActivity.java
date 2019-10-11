package com.example.baseapplication.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.baseapplication.app.AppConfig;
import com.example.baseapplication.mvp.model.event.WXPayResultEvent;
import com.example.baseapplication.util.Global;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, AppConfig.WX_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("TAG", "resp = " + resp.toString());
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX && resp != null) {
            Global.WXPAYCODE = resp.errCode;
            EventBus.getDefault().post(new WXPayResultEvent(resp));
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}