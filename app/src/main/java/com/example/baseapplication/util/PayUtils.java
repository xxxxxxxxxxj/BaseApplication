package com.example.baseapplication.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.example.baseapplication.app.AppConfig;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.view.widget.MProgressDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.Map;

/**
 * <p>
 * Title:PayUtils
 * </p>
 * <p>
 * Description:支付工具类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 *
 * @author 徐俊
 * @date 2017-4-13 下午8:05:23
 */
public class PayUtils {
    // 微信支付
    public static void weChatPayment(Activity activity, String appId,
                                     String partnerId, String prepayId, String packageValue,
                                     String nonceStr, String timeStamp, String sign,
                                     MProgressDialog pDialog) {
        PayReq payReq = new PayReq();
        payReq.appId = appId;
        payReq.partnerId = partnerId;
        payReq.prepayId = prepayId;
        payReq.packageValue = packageValue;
        payReq.nonceStr = nonceStr;
        payReq.timeStamp = timeStamp;
        payReq.sign = sign;
        IWXAPI createWXAPI = WXAPIFactory.createWXAPI(activity, AppConfig.WX_ID);
        createWXAPI.registerApp(appId);
        createWXAPI.sendReq(payReq);
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    // 支付宝支付
    public static void payByAliPay(final Activity activity,
                                   final String payStr, final Handler handler) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                RingLog.d("alipay = " + alipay);
                // 调用支付接口
                Map<String, String> result = alipay.payV2(payStr, true);
                Message msg = new Message();
                msg.what = AppConfig.ALI_SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
