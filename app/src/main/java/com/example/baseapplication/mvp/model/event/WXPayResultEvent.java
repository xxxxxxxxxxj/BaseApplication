package com.example.baseapplication.mvp.model.event;

import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019/3/31 15:41
 */
public class WXPayResultEvent {
    private BaseResp resp;

    public BaseResp getResp() {
        return resp;
    }

    public void setResp(BaseResp resp) {
        this.resp = resp;
    }

    public WXPayResultEvent(BaseResp resp) {
        this.resp = resp;
    }
}
