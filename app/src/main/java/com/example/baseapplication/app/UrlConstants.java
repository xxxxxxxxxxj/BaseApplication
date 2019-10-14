package com.example.baseapplication.app;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date XJ on 2018/4/11 11:30
 */
public class UrlConstants {
    /**
     * 1.下发验证码
     */
    public static final String SENDVERIFYCODE = "user/info/sendVerifyCode";
    /**
     * 2.登陆
     */
    public static final String LOGIN = "user/info/login";
    /**
     * 3.用户主页信息
     */
    public static final String HOME = "user/info/home";
    /**
     * 4.首页
     */
    public static final String HOMEINDEX = "home/index";
    /**
     * 5.附近充电桩
     */
    public static final String NEARBY = "charging/info/nearby";
    /**
     * 6.充电桩详情
     */
    public static final String CHARGEDETAIL = "charging/info/detail";
    /**
     * 7.上传充电桩
     */
    public static final String SAVECHARGE = "charging/info/save";
    /**
     * 8.充电桩评论列表
     */
    public static final String COMMENT_LIST = "charging/comment/list";
    /**
     * 9.充电桩评论
     */
    public static final String COMMENT_SAVE = "charging/comment/save";
    /**
     * 10.管家留言列表
     */
    public static final String HISTORYMSG = "user/message/history";
    /**
     * 11.发布留言
     */
    public static final String SAVEMSG = "user/message/save";
    /**
     * 12.收藏的充电桩列表
     */
    public static final String COLLECT_CHARGE = "user/charging/list";
    /**
     * 13.收藏充电桩
     */
    public static final String FOLLOW_CHARGE = "user/charging/follow";
    /**
     * 14.取消收藏充电桩
     */
    public static final String CANCEL_FOLLOW_CHARGE = "user/charging/cancel";
    /**
     * 15.关注的人列表
     */
    public static final String FOLLOW_LIST = "user/idol/list";
    /**
     * 16.用户帖子列表
     */
    public static final String USERINFO_POST = "article/info/list";
    /**
     * 17.用户信息
     */
    public static final String USERINFO_UUID = "user/info";
    /**
     * 18.关注用户
     */
    public static final String FOLLOW_USER = "user/idol/follow";
    /**
     * 19.取消关注用户
     */
    public static final String CANCEL_FOLLOW_USER = "user/idol/cancel";
    /**
     * 20.广告
     */
    public static final String ADVERTISEMENT = "util/ad/list";
    /**
     * 21.热门品牌
     */
    public static final String HOT_CAR_BRAND = "brand/info/hot";
    /**
     * 22.所有品牌
     */
    public static final String ALL_CAR_BRAND = "brand/info/list";
    /**
     * 23.最新帖子列表
     */
    public static final String NEWEST_POINT = "article/info/new";
    /**
     * 24.热门帖子列表
     */
    public static final String HOT_POINT = "article/info/hot";
    /**
     * 25.问题车帖子列表
     */
    public static final String PROBLEM_CAR_POINT = "article/info/problem";
    /**
     * 26.品牌热帖
     */
    public static final String BRAND_HOT_POINT = "brand/info/article";
    /**
     * 27.发帖
     */
    public static final String SENDPOST = "article/info/save";
    /**
     * 28.热门车型
     */
    public static final String HOT_SPECIAL_CAR = "brand/car/special";
    /**
     * 29.评价用户
     */
    public static final String EVAL_USER = "user/eval";
    /**
     * 30.点赞
     */
    public static final String PRAISE_USER = "article/praise/save";
    /**
     * 31.用户车辆信息
     */
    public static final String MY_CAR = "user/car/my";
    /**
     * 32.保存或修改用户车辆信息
     */
    public static final String SAVE_ORUPDATE_USERCAR = "user/car/save";
    /**
     * 33.编辑充电桩
     */
    public static final String UPDATECHARGE = "charging/info/update";
    /**
     * 34.评论标签
     */
    public static final String COMMENT_TAGS = "charging/comment/tags";
    /**
     * 35.评价星级
     */
    public static final String STARS = "user/eval/stars";
    /**
     * 36.删除帖子
     */
    public static final String DELETE_POST = "article/info/delete";
    /**
     * 37.分享帖子成功回调
     */
    public static final String SHARE_POST = "article/info/share/callback";
    /**
     * 38.编辑用户信息
     */
    public static final String UPDATE_USER_INFO = "user/info/edit";
    /**
     * 39.获取微信WxOpenId
     */
    public static final String GET_WXOPENID = "wx/app/user/login";
    /**
     * 40.获取微信用户信息
     */
    public static final String GET_WX_USERINFO = "wx/app/user/info";
    /**
     * 41.品牌车型
     */
    public static final String BRAND_CAR = "brand/car/list";
    /**
     * 42.品牌和车型
     */
    public static final String BRAND_AND_CAR = "brand/car";
    /**
     * 43.导航成功回调
     */
    public static final String NAV_CALLBACK = "charging/info/navi/callback";
    /**
     * 44.可充值模板列表
     */
    public static final String RECHARGE_TEMP = "recharge/template/list";
    /**
     * 45.发起充值请求
     */
    public static final String RECHARGE_BUILD = "recharge/request/build";
    /**
     * 45.发起充电
     */
    public static final String START_CHARGEING = "order/charge/start";
    /**
     * 46.获取进行中的订单
     */
    public static final String CHARGEING_ORDER = "order/charge/ing";
    /**
     * 47.查询充电状态
     */
    public static final String CHARGEING_STATE = "order/charge/state";
    /**
     * 48.结束充电
     */
    public static final String CHARGEING_STOP = "order/charge/stop";
    /**
     * 49.获取账单
     */
    public static final String CHARGEING_BILL = "order/charge/bill";
    /**
     * 50.支付账单
     */
    public static final String CHARGEING_BILL_PAY = "order/charge/pay";
    /**
     * 51.车型检索条件
     */
    public static final String SCREENCAR_ITEM = "brand/car/items";
    /**
     * 52.车型检索
     */
    public static final String SCREENCAR_QUERY = "brand/car/query";
    /**
     * 53.标签列表
     */
    public static final String REFUND_TAG = "util/label/list";
    /**
     * 54.退款说明
     */
    public static final String REFUND_EXPLAN = "recharge/request/explain";
    /**
     * 55.发起充值退款
     */
    public static final String REFUND = "recharge/request/refund";
    /**
     * 56.订单列表
     */
    public static final String RECHARGE_LIST = "order/charge/list";
    /**
     * 57.交易记录列表
     */
    public static final String HISTORY_LIST = "trade/history/list";
    /**
     * 58.我的优惠券列表
     */
    public static final String MYCOUPON = "coupon/list";
    /**
     * 59.车辆详情
     */
    public static final String CARDETAIL = "brand/car/detail";
    /**
     * 60.车型预定
     */
    public static final String CAR_PERSON_SAVE = "brand/appoint/save";
    /**
     * 61.匹配可用优惠券列表
     */
    public static final String MATCH_COUPON = "coupon/match";
    /**
     * 62.取消订单
     */
    public static final String CANCEL_ORDER = "order/charge/timeout";
    /**
     * 63.故障保修
     */
    public static final String CHARGE_REPORT = "charging/report/save";
    /**
     * 64.分享站点详情成功回调
     */
    public static final String SHARE_CHARGEING = "charging/info/share/callback";
    /**
     * 65.分享车辆详情成功回调
     */
    public static final String SHARE_CAR = "brand/car/share/callback";
    /**
     * 66.打开app回调
     */
    public static final String OPENAPP_CALLBACK = "user/info/open/callback";
    /**
     * 66.兑换码
     */
    public static final String REDEEMCODE = "coupon/code/use";
    /**
     * 67.检查升级
     */
    public static final String CHECK_VERSION = "util/version/check";
    /**
     * 68.选车首页
     */
    public static final String CARTYPE = "brand/car/special";
    /**
     * 69.文章热门搜索关键字
     */
    public static final String SERCH_POST_KEYS = "article/info/keys";
    /**
     * 70.文章热门搜索关键字
     */
    public static final String SERCH_POST = "article/info/list";
    /**
     * 71.相关文案配置
     */
    public static final String USER_CONFIG = "user/info/config";

    private static int getEnvironmental() {
        return AppConfig.environmental;//1.test环境---2.demo环境---3.线上环境
    }

    /**
     * 获取带端口的IP地址
     *
     * @return
     */
    public static String getServiceBaseUrl() {
        String url = "";
        switch (getEnvironmental()) {
            case 2://demo环境
                url = "https://demo.dzztrip.cn/api/";
                break;
            case 3://线上环境
                url = "https://service.dzztrip.cn/api/";
                break;
            default:
                break;
        }
        return url;
    }
}
