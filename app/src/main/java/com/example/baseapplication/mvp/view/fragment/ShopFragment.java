package com.example.baseapplication.mvp.view.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.example.baseapplication.R;
import com.example.baseapplication.app.AppConfig;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.model.entity.ALiPayResult;
import com.example.baseapplication.mvp.model.event.MatisseDataEvent;
import com.example.baseapplication.mvp.model.event.WXPayResultEvent;
import com.example.baseapplication.mvp.presenter.ShopFragPresenter;
import com.example.baseapplication.mvp.view.activity.CameraActivity;
import com.example.baseapplication.mvp.view.activity.ScanCodeActivity;
import com.example.baseapplication.mvp.view.activity.StaggerActivity;
import com.example.baseapplication.mvp.view.adapter.ImgAdapter;
import com.example.baseapplication.mvp.view.adapter.ShopAdapter;
import com.example.baseapplication.mvp.view.fragment.base.BaseFragment;
import com.example.baseapplication.mvp.view.iview.IShopFragView;
import com.example.baseapplication.mvp.view.widget.GiftCardComponent;
import com.example.baseapplication.mvp.view.widget.GridSpacingItemDecoration;
import com.example.baseapplication.mvp.view.widget.NoScollFullGridLayoutManager;
import com.example.baseapplication.mvp.view.widget.dialog.WheelBottomDialog;
import com.example.baseapplication.mvp.view.widget.popup.QMUIListPopup;
import com.example.baseapplication.mvp.view.widget.popup.QMUIPopup;
import com.example.baseapplication.permission.PermissionListener;
import com.example.baseapplication.toast.RingToast;
import com.example.baseapplication.util.CommonUtil;
import com.example.baseapplication.util.FileSizeUtil;
import com.example.baseapplication.util.PayUtils;
import com.example.baseapplication.util.QMUIDeviceHelper;
import com.example.baseapplication.util.QMUIDisplayHelper;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.OnUrlClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.iwgang.countdownview.CountdownView;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 19:09
 */
public class ShopFragment extends BaseFragment<ShopFragPresenter> implements IShopFragView {
    public static final int REQUESTCODE_VIDEO = 100;
    @BindView(R.id.rv_shopfrag_item)
    RecyclerView rvShopfragItem;
    @BindView(R.id.rv_shopfrag_img)
    RecyclerView rvShopfragImg;
    private final String[] mTitles = {"Matisse", "zxing", "微信支付", "支付宝支付", "拍摄视频", "RichText", "普通浮层",
            "列表浮层", "加载框", "提示框", "自定义提示框", "亮色ios对话框", "暗色ios对话框", "亮色md对话框", "暗色md对话框",
            "新手引导", "倒计时", "滚轮", "瀑布流", "购物车动画", "StickLayout", "当页浮窗", "系统浮窗", "红包动画", "Flipper", "通知"};
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.tv_upgrade_bottomdia_time)
    CountdownView tvUpgradeBottomdiaTime;
    @BindView(R.id.rl_appointment_root)
    RelativeLayout rl_appointment_root;
    @BindView(R.id.iv_appointment_cart)
    ImageView iv_appointment_cart;
    private ImageView beisaierImageView;
    private float[] mCurrentPosition = new float[2];
    private ShopAdapter shopAdapter;
    private List<String> imgList = new ArrayList<String>();
    private ImgAdapter imgAdapter;
    private static final String html = "<article class=\"markdown-body entry-content\" itemprop=\"text\"><h1><a href=\"#richtext\" aria-hidden=\"true\" class=\"anchor\" id=\"user-content-richtext\"><svg aria-hidden=\"true\" class=\"octicon octicon-link\" height=\"16\" version=\"1.1\" viewBox=\"0 0 16 16\" width=\"16\"><path fill-rule=\"evenodd\" d=\"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z\"></path></svg></a>RichText</h1>\n" +
            "<blockquote>\n" +
            "<p style=\"background-color:rgba(255,0,0,1);\">Android平台下的富文本解析器</p>\n" +
            "</blockquote>\n" +
            "<ul>\n" +
            "<li>流式操作</li>\n" +
            "<li>低侵入性</li>\n" +
            "<li>依赖少，只依赖了<code>disklrucache</code>和<code>support v4</code></li>\n" +
            "<li>支持Html和Markdown格式文本</li>\n" +
            "<li>支持图片点击和长按事件</li>\n" +
            "<li>链接点击事件和长按事件</li>\n" +
            "<li>支持设置加载中和加载错误时的图片</li>\n" +
            "<li>支持自定义超链接的点击回调</li>\n" +
            "<li>支持修正图片宽高</li>\n" +
            "<li>支持GIF图片</li>\n" +
            "<li>支持Base64编码、本地图片和Assets目录图片</li>\n" +
            "<li>自持自定义图片加载器、图片加载器</li>\n" +
            "<li>支持内存和磁盘双缓存</li>\n" +
            "</ul>\n" +
            "<h3><a href=\"#效果\" aria-hidden=\"true\" class=\"anchor\" id=\"user-content-效果\"><svg aria-hidden=\"true\" class=\"octicon octicon-link\" height=\"16\" version=\"1.1\" viewBox=\"0 0 16 16\" width=\"16\"><path fill-rule=\"evenodd\" d=\"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z\"></path></svg></a>效果</h3>\n" +
            "<p><a href=\"/zzhoujay/RichText/blob/master/image/image.jpg\" target=\"_blank\"><img src=\"/zzhoujay/RichText/raw/master/image/image.jpg\" alt=\"演示\" title=\"演示\" style=\"max-width:100%;\"></a></p>\n" +
            "<h3><a href=\"#gradle中引用的方法\" aria-hidden=\"true\" class=\"anchor\" id=\"user-content-gradle中引用的方法\"><svg aria-hidden=\"true\" class=\"octicon octicon-link\" height=\"16\" version=\"1.1\" viewBox=\"0 0 16 16\" width=\"16\"><path fill-rule=\"evenodd\" d=\"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z\"></path></svg></a>gradle中引用的方法</h3>\n" +
            "<pre><code>compile 'com.zzhoujay.richtext:richtext:3.0.5'\n" +
            "</code></pre>\n" +
            "<h3><a href=\"#关于issue\" aria-hidden=\"true\" class=\"anchor\" id=\"user-content-关于issue\"><svg aria-hidden=\"true\" class=\"octicon octicon-link\" height=\"16\" version=\"1.1\" viewBox=\"0 0 16 16\" width=\"16\"><path fill-rule=\"evenodd\" d=\"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z\"></path></svg></a>关于issue</h3>\n" +
            "<p style=\"text-indent:50px;\">最近一段时间会比较忙，issue不能及时处理，一般会定时抽空集中解决issue，但时间有限解决速度上不敢保证。</p>\n" +
            "<p>欢迎提交pull request帮助完善这个项目</p>\n" +
            "<h3><a href=\"#注意\" aria-hidden=\"true\" class=\"anchor\" id=\"user-content-注意\"><svg aria-hidden=\"true\" class=\"octicon octicon-link\" height=\"16\" version=\"1.1\" viewBox=\"0 0 16 16\" width=\"16\"><path fill-rule=\"evenodd\" d=\"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z\"></path></svg></a>注意</h3>\n" +
            "<p>在第一次调用RichText之前先调用<code>RichText.initCacheDir()</code>方法设置缓存目录</p>\n" +
            "<p>ImageFixCallback的回调方法不一定是在主线程回调，注意不要进行UI操作</p>\n" +
            "<p>本地图片由根路径<code>\\</code>开头，Assets目录图片由<code>file:///android_asset/</code>开头</p>\n" +
            "<p>Gif图片播放不支持硬件加速，若要使用Gif图片请先关闭TextView的硬件加速</p>\n" +
            "<pre><code>textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);\n" +
            "</code></pre>\n" +
            "<h3><a href=\"#使用方式\" aria-hidden=\"true\" class=\"anchor\" id=\"user-content-使用方式\"><svg aria-hidden=\"true\" class=\"octicon octicon-link\" height=\"16\" version=\"1.1\" viewBox=\"0 0 16 16\" width=\"16\"><path fill-rule=\"evenodd\" d=\"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z\"></path></svg></a>使用方式</h3>\n" +
            "<p><a href=\"https://github.com/zzhoujay/RichText/wiki\">多看wiki</a>、<a href=\"https://github.com/zzhoujay/RichText/wiki\">多看wiki</a>、<a href=\"https://github.com/zzhoujay/RichText/wiki\">多看wiki</a>，重要的事情说三遍</p>\n" +
            "<h3><a href=\"#后续计划\" aria-hidden=\"true\" class=\"anchor\" id=\"user-content-后续计划\"><svg aria-hidden=\"true\" class=\"octicon octicon-link\" height=\"16\" version=\"1.1\" viewBox=\"0 0 16 16\" width=\"16\"><path fill-rule=\"evenodd\" d=\"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z\"></path></svg></a>后续计划</h3>\n" +
            "<ul>\n" +
            "<li><del>添加自定义标签的支持</del> (已添加对少部分自定义标签的支持)</li>\n" +
            "</ul>\n" +
            "<h3><a href=\"#关于markdown\" aria-hidden=\"true\" class=\"anchor\" id=\"user-content-关于markdown\"><svg aria-hidden=\"true\" class=\"octicon octicon-link\" height=\"16\" version=\"1.1\" viewBox=\"0 0 16 16\" width=\"16\"><path fill-rule=\"evenodd\" d=\"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z\"></path></svg></a>关于Markdown</h3>\n" +
            "<p>Markdown源于子项目：<a href=\"https://github.com/zzhoujay/Markdown\">Markdown</a></p>\n" +
            "<p>若在markdown解析过程中发现什么问题可以在该项目中反馈</p>\n" +
            "<h3><a href=\"#富文本编辑器\" aria-hidden=\"true\" class=\"anchor\" id=\"user-content-富文本编辑器\"><svg aria-hidden=\"true\" class=\"octicon octicon-link\" height=\"16\" version=\"1.1\" viewBox=\"0 0 16 16\" width=\"16\"><path fill-rule=\"evenodd\" d=\"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z\"></path></svg></a>富文本编辑器</h3>\n" +
            "<p>编辑功能目前正在开发中，<a href=\"https://github.com/zzhoujay/RichEditor\">RichEditor</a></p>\n" +
            "<h3><a href=\"#具体使用请查看demo\" aria-hidden=\"true\" class=\"anchor\" id=\"user-content-具体使用请查看demo\"><svg aria-hidden=\"true\" class=\"octicon octicon-link\" height=\"16\" version=\"1.1\" viewBox=\"0 0 16 16\" width=\"16\"><path fill-rule=\"evenodd\" d=\"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z\"></path></svg></a>具体使用请查看demo</h3>\n" +
            "<p><a href=\"https://github.com/zzhoujay/RichText/blob/master/app/src/main/java/zhou/demo/ListViewActivity.java\">ListView Demo</a>、\n" +
            "<a href=\"https://github.com/zzhoujay/RichText/blob/master/app/src/main/java/zhou/demo/RecyclerViewActivity.java\">RecyclerView Demo</a>、\n" +
            "<a href=\"https://github.com/zzhoujay/RichText/blob/master/app/src/main/java/zhou/demo/GifActivity.java\">Gif Demo</a></p>\n" +
            "<h3><a href=\"#license\" aria-hidden=\"true\" class=\"anchor\" id=\"user-content-license\"><svg aria-hidden=\"true\" class=\"octicon octicon-link\" height=\"16\" version=\"1.1\" viewBox=\"0 0 16 16\" width=\"16\"><path fill-rule=\"evenodd\" d=\"M4 9h1v1H4c-1.5 0-3-1.69-3-3.5S2.55 3 4 3h4c1.45 0 3 1.69 3 3.5 0 1.41-.91 2.72-2 3.25V8.59c.58-.45 1-1.27 1-2.09C10 5.22 8.98 4 8 4H4c-.98 0-2 1.22-2 2.5S3 9 4 9zm9-3h-1v1h1c1 0 2 1.22 2 2.5S13.98 12 13 12H9c-.98 0-2-1.22-2-2.5 0-.83.42-1.64 1-2.09V6.25c-1.09.53-2 1.84-2 3.25C6 11.31 7.55 13 9 13h4c1.45 0 3-1.69 3-3.5S14.5 6 13 6z\"></path></svg></a>License</h3>\n" +
            "<pre><code>The MIT License (MIT)\n" +
            "\n" +
            "Copyright (c) 2016 zzhoujay\n" +
            "\n" +
            "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
            "of this software and associated documentation files (the \"Software\"), to deal\n" +
            "in the Software without restriction, including without limitation the rights\n" +
            "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
            "copies of the Software, and to permit persons to whom the Software is\n" +
            "furnished to do so, subject to the following conditions:\n" +
            "\n" +
            "The above copyright notice and this permission notice shall be included in all\n" +
            "copies or substantial portions of the Software.\n" +
            "\n" +
            "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
            "\n" +
            "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
            "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
            "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
            "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
            "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE\n" +
            "SOFTWARE.\n" +
            "</code></pre>\n" +
            "<p><em>by zzhoujay</em></p>\n" +
            "</article>";
    private QMUIPopup mNormalPopup;
    private QMUIListPopup mListPopup;
    private Guide guide;

    @Override
    protected ShopFragPresenter createPresenter() {
        return new ShopFragPresenter(mActivity, this);
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Override
    protected boolean isLazyLoad() {
        return false;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_shop;
    }

    @Override
    protected void initView() {
        RichText.initCacheDir(mActivity);
        RichText.debugMode = true;

        rvShopfragItem.setHasFixedSize(true);
        rvShopfragItem.setNestedScrollingEnabled(false);
        NoScollFullGridLayoutManager noScollFullGridLayoutManager = new
                NoScollFullGridLayoutManager(rvShopfragItem, mActivity, 4, GridLayoutManager.VERTICAL, false);
        noScollFullGridLayoutManager.setScrollEnabled(false);
        rvShopfragItem.setLayoutManager(noScollFullGridLayoutManager);
        shopAdapter = new ShopAdapter(mActivity, R.layout.item_shopfrag_item, new ArrayList<String>(Arrays.asList(mTitles)));
        rvShopfragItem.setAdapter(shopAdapter);
        rvShopfragItem.addItemDecoration(new GridSpacingItemDecoration(4,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                true));

        rvShopfragImg.setHasFixedSize(true);
        rvShopfragImg.setNestedScrollingEnabled(false);
        NoScollFullGridLayoutManager noScollFullGridLayoutManager1 = new
                NoScollFullGridLayoutManager(rvShopfragItem, mActivity, 3, GridLayoutManager.VERTICAL, false);
        noScollFullGridLayoutManager1.setScrollEnabled(false);
        rvShopfragImg.setLayoutManager(noScollFullGridLayoutManager1);
        imgAdapter = new ImgAdapter(mActivity, R.layout.item_img, imgList, 40);
        rvShopfragImg.setAdapter(imgAdapter);
        rvShopfragImg.addItemDecoration(new GridSpacingItemDecoration(3,
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                getResources().getDimensionPixelSize(R.dimen.horizontalSpacing10),
                true));
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initEvent() {
        tvUpgradeBottomdiaTime.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                RingToast.show("倒计时结束");
            }
        });
        setOnLuBanSuccessListener(new OnLuBanSuccessListener() {
            @Override
            public void OnLuBanSuccess(List<File> list) {
                for (int i = 0; i < list.size(); i++) {
                    String formatFileSize = FileSizeUtil
                            .formatFileSize(list.get(i).length(), false);
                    RingLog.e("压缩后 = " + formatFileSize + "---路径 = " + list.get(i).getAbsolutePath());
                }
                imgList.clear();
                imgList.addAll(CommonUtil.fileToPath(list));
                imgAdapter.setImgData(imgList);
            }
        });
        shopAdapter.setOnItemAddListener(new ShopAdapter.OnItemAddListener() {
            @Override
            public void OnItemAdd(int position, ImageView imageView) {
                switch (position) {
                    case 0:
                        goPhoto(9);
                        break;
                    case 1:
                        requestEachCombined(new PermissionListener() {
                            @Override
                            public void onGranted(String permissionName) {
                                startActivity(ScanCodeActivity.class);
                            }

                            @Override
                            public void onDenied(String permissionName) {
                                showToast("请打开相机权限");
                            }

                            @Override
                            public void onDeniedWithNeverAsk(String permissionName) {
                                MessageDialog.show(mActivity, "请打开相机权限", "确定要打开相机权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog baseDialog, View v) {
                                        QMUIDeviceHelper.goToPermissionManager(mActivity);
                                        return false;
                                    }
                                });
                            }
                        }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
                        break;
                    case 2:
                        PayUtils.weChatPayment(mActivity, "", "", "", "", "", "", "", tipDialog);
                        break;
                    case 3:
                        PayUtils.payByAliPay(mActivity, "", mHandler);
                        break;
                    case 4:
                        requestEachCombined(new PermissionListener() {
                            @Override
                            public void onGranted(String permissionName) {
                                startActivityForResult(CameraActivity.class, null, REQUESTCODE_VIDEO);
                            }

                            @Override
                            public void onDenied(String permissionName) {
                                showToast("请打开相机权限");
                            }

                            @Override
                            public void onDeniedWithNeverAsk(String permissionName) {
                                MessageDialog.show(mActivity, "请打开相机权限", "确定要打开相机权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                                    @Override
                                    public boolean onClick(BaseDialog baseDialog, View v) {
                                        QMUIDeviceHelper.goToPermissionManager(mActivity);
                                        return false;
                                    }
                                });
                            }
                        }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.CAMERA});
                        break;
                    case 5:
                        RichText.from(html)
                                .urlClick(new OnUrlClickListener() {
                                    @Override
                                    public boolean urlClicked(String url) {
                                        if (url.startsWith("code://")) {
                                            RingToast.show(url.replaceFirst("code://", ""));
                                            return true;
                                        }
                                        return false;
                                    }
                                })
                                .into(text);
                        break;
                    case 6:
                        initNormalPopupIfNeed();
                        mNormalPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
                        mNormalPopup.setPreferredDirection(QMUIPopup.DIRECTION_BOTTOM);
                        mNormalPopup.show(rvShopfragItem.getChildAt(6));
                        break;
                    case 7:
                        initListPopupIfNeed();
                        mListPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
                        mListPopup.setPreferredDirection(QMUIPopup.DIRECTION_TOP);
                        mListPopup.show(rvShopfragItem.getChildAt(7));
                        break;
                    case 8:
                        showLoadDialog();
                        rvShopfragItem.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideLoadDialog();
                            }
                        }, 1500);
                        break;
                    case 9:
                        showLoadDialog("正在压缩...");
                        rvShopfragItem.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideLoadDialog();
                            }
                        }, 1500);
                        break;
                    case 10:
                        showLoadDialog("正在上传...");
                        rvShopfragItem.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                hideLoadDialog();
                            }
                        }, 1500);
                        break;
                    case 11:
                        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
                        DialogSettings.theme = DialogSettings.THEME.LIGHT;
                        DialogSettings.tipTheme = DialogSettings.THEME.DARK;
                        MessageDialog.show(mActivity, "请打开相机权限", "确定要打开相机权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                QMUIDeviceHelper.goToPermissionManager(mActivity);
                                return false;
                            }
                        });
                        break;
                    case 12:
                        DialogSettings.style = DialogSettings.STYLE.STYLE_IOS;
                        DialogSettings.theme = DialogSettings.THEME.DARK;
                        DialogSettings.tipTheme = DialogSettings.THEME.LIGHT;
                        MessageDialog.show(mActivity, "请打开相机权限", "确定要打开相机权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                QMUIDeviceHelper.goToPermissionManager(mActivity);
                                return false;
                            }
                        });
                        break;
                    case 13:
                        DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
                        DialogSettings.theme = DialogSettings.THEME.LIGHT;
                        DialogSettings.tipTheme = DialogSettings.THEME.DARK;
                        MessageDialog.show(mActivity, "请打开相机权限", "确定要打开相机权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                QMUIDeviceHelper.goToPermissionManager(mActivity);
                                return false;
                            }
                        });
                        break;
                    case 14:
                        DialogSettings.style = DialogSettings.STYLE.STYLE_MATERIAL;
                        DialogSettings.theme = DialogSettings.THEME.DARK;
                        DialogSettings.tipTheme = DialogSettings.THEME.LIGHT;
                        MessageDialog.show(mActivity, "请打开相机权限", "确定要打开相机权限吗？", "确定", "取消").setOnOkButtonClickListener(new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                QMUIDeviceHelper.goToPermissionManager(mActivity);
                                return false;
                            }
                        });
                        break;
                    case 15:
                        showGuideView(rvShopfragItem.getChildAt(15));
                        break;
                    case 16:
                        tvUpgradeBottomdiaTime.updateShow(10 * 3600 * 1000 * 1000);
                        tvUpgradeBottomdiaTime.start(10 * 3600 * 1000 * 1000);
                        break;
                    case 17:
                        WheelBottomDialog dialog = new WheelBottomDialog(mActivity);
                        dialog.show(getFragmentManager());
                        break;
                    case 18:
                        startActivity(StaggerActivity.class);
                        break;
                    case 19:
                        beisaierImageView = imageView;
                        addGoodToCar();
                        break;
                    case 20://StickLayout
                        break;
                    case 21://当页浮窗
                        break;
                    case 22://系统浮窗
                        break;
                    case 23://红包动画
                        break;
                    case 24://Flipper
                        break;
                    case 25://通知
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void addGoodToCar() {
        final ImageView view = new ImageView(mActivity);
        view.setImageResource(R.mipmap.icon_beisaier);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(56, 56);
        rl_appointment_root.addView(view, layoutParams);

        //二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLoc = new int[2];
        rl_appointment_root.getLocationInWindow(parentLoc);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        beisaierImageView.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        iv_appointment_cart.getLocationInWindow(endLoc);

        float startX = startLoc[0] - parentLoc[0] + beisaierImageView.getWidth() / 2;
        float startY = startLoc[1] - parentLoc[1] + beisaierImageView.getHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLoc[0] + iv_appointment_cart.getWidth() / 5;
        float toY = endLoc[1] - parentLoc[1];

        //开始绘制贝塞尔曲线
        Path path = new Path();
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        PathMeasure mPathMeasure = new PathMeasure(path, false);

        //属性动画
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                mPathMeasure.getPosTan(value, mCurrentPosition, null);
                view.setTranslationX(mCurrentPosition[0]);
                view.setTranslationY(mCurrentPosition[1]);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 把移动的图片imageview从父布局里移除
                rl_appointment_root.removeView(view);
                //shopImg 开始一个放大动画
                Animation scaleAnim = AnimationUtils.loadAnimation(mActivity, R.anim.shop_car_scale);
                iv_appointment_cart.startAnimation(scaleAnim);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();
    }

    public void showGuideView(View view) {
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(view).setAlpha(200)
                .setHighTargetCorner(getResources().getDimensionPixelSize(R.dimen.dp_10))
                .setExitAnimationId(android.R.anim.fade_out)
                .setAutoDismiss(false)
                .setOverlayTarget(false).setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
            }

            @Override
            public void onDismiss() {
                spUtil.saveBoolean("GUIDE_GIFTCARD", true);
            }
        });
        builder.addComponent(new GiftCardComponent(mActivity, clickListener));
        guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(true);
        guide.show(mActivity);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_guide_eattime2:
                    //执行业务操作
                    guide.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private void initNormalPopupIfNeed() {
        if (mNormalPopup == null) {
            mNormalPopup = new QMUIPopup(mActivity, QMUIPopup.DIRECTION_NONE);
            TextView textView = new TextView(mActivity);
            textView.setLayoutParams(mNormalPopup.generateLayoutParam(
                    QMUIDisplayHelper.dp2px(mActivity, 250),
                    WRAP_CONTENT
            ));
            textView.setLineSpacing(QMUIDisplayHelper.dp2px(mActivity, 4), 1.0f);
            int padding = QMUIDisplayHelper.dp2px(mActivity, 20);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText("Popup 可以设置其位置以及显示和隐藏的动画");
            textView.setTextColor(ContextCompat.getColor(mActivity, R.color.a666666));
            mNormalPopup.setContentView(textView);
            mNormalPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    RingToast.show("普通浮层消失啦");
                }
            });
        }
    }

    private void initListPopupIfNeed() {
        if (mListPopup == null) {
            String[] listItems = new String[]{
                    "Item 1",
                    "Item 2",
                    "Item 3",
                    "Item 4",
                    "Item 5",
                    "Item 5",
                    "Item 5",
                    "Item 5",
                    "Item 5",
                    "Item 5",
                    "Item 5",
                    "Item 5",
                    "Item 5",
                    "Item 5",
            };
            List<String> data = new ArrayList<>();
            Collections.addAll(data, listItems);
            ArrayAdapter adapter = new ArrayAdapter<>(mActivity, R.layout.simple_list_item, data);
            mListPopup = new QMUIListPopup(mActivity, QMUIPopup.DIRECTION_NONE, adapter);
            mListPopup.create(QMUIDisplayHelper.dp2px(mActivity, 250), QMUIDisplayHelper.dp2px(mActivity, 200), new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(mActivity, "Item " + (i + 1), Toast.LENGTH_SHORT).show();
                    mListPopup.dismiss();
                }
            });
            mListPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    RingToast.show("列表浮层消失啦");
                }
            });
        }
    }

    @Override
    protected void loadData() {

    }

    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppConfig.ALI_SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    ALiPayResult payResult = new ALiPayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        RingLog.e("支付成功");
                        RingToast.show("支付成功");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        RingLog.e("支付失败");
                        RingToast.show("支付失败");
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @Subscribe
    public void onWXPayResult(WXPayResultEvent baseResp) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateAppState(MatisseDataEvent event) {
        if (event != null) {
            List<String> strings = event.getStrings();
            List<Uri> uris = event.getUris();
            RingLog.e("uris = " + uris);
            RingLog.e("strings = " + strings);
            if (strings != null && strings.size() > 0) {
                for (int i = 0; i < strings.size(); i++) {
                    String formatFileSize = FileSizeUtil
                            .formatFileSize(
                                    new File(strings.get(i))
                                            .length(), false);
                    RingLog.e("压缩前 = " + formatFileSize + "---路径 = " + strings.get(i));
                }
                //开启鲁班压缩
                withRx(CommonUtil.pathToFile(strings));
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RichText.recycle();
    }
}
