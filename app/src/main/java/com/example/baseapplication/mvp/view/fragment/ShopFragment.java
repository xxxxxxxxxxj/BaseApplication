package com.example.baseapplication.mvp.view.fragment;

import android.Manifest;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.baseapplication.R;
import com.example.baseapplication.app.AppConfig;
import com.example.baseapplication.log.RingLog;
import com.example.baseapplication.mvp.model.entity.ALiPayResult;
import com.example.baseapplication.mvp.model.event.MatisseDataEvent;
import com.example.baseapplication.mvp.model.event.WXPayResultEvent;
import com.example.baseapplication.mvp.presenter.ShopFragPresenter;
import com.example.baseapplication.mvp.view.activity.ScanCodeActivity;
import com.example.baseapplication.mvp.view.adapter.ImgAdapter;
import com.example.baseapplication.mvp.view.adapter.ShopAdapter;
import com.example.baseapplication.mvp.view.fragment.base.BaseFragment;
import com.example.baseapplication.mvp.view.iview.IShopFragView;
import com.example.baseapplication.mvp.view.widget.GridSpacingItemDecoration;
import com.example.baseapplication.mvp.view.widget.NoScollFullGridLayoutManager;
import com.example.baseapplication.permission.PermissionListener;
import com.example.baseapplication.toast.RingToast;
import com.example.baseapplication.util.FileSizeUtil;
import com.example.baseapplication.util.PayUtils;
import com.example.baseapplication.util.SystemUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * <p>Title:${type_name}</p>
 * <p>Description:</p>
 * <p>Company:北京昊唐科技有限公司</p>
 *
 * @author 徐俊
 * @date zhoujunxia on 2019-10-14 19:09
 */
public class ShopFragment extends BaseFragment<ShopFragPresenter> implements IShopFragView {
    @BindView(R.id.rv_shopfrag_item)
    RecyclerView rvShopfragItem;
    @BindView(R.id.rv_shopfrag_img)
    RecyclerView rvShopfragImg;
    private final String[] mTitles = {"Matisse", "zxing", "微信支付", "支付宝支付"};
    private ShopAdapter shopAdapter;
    private List<String> imgList = new ArrayList<String>();
    private ImgAdapter imgAdapter;

    @Override
    protected ShopFragPresenter createPresenter() {
        return new ShopFragPresenter(getContext(), this);
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
        setOnLuBanSuccessListener(new OnLuBanSuccessListener() {
            @Override
            public void OnLuBanSuccess(List<File> list) {
                for (int i = 0; i < list.size(); i++) {
                    String formatFileSize = FileSizeUtil
                            .formatFileSize(list.get(i).length(), false);
                    RingLog.e("压缩后 = " + formatFileSize + "---路径 = " + list.get(i).getAbsolutePath());
                }
                imgList.clear();
                imgList.addAll(SystemUtil.fileToPath(list));
                imgAdapter.setImgData(imgList);
            }
        });
        shopAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
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
                                showToast("请打开相机权限");
                            }
                        }, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
                        break;
                    case 2:
                        PayUtils.weChatPayment(mActivity, "", "", "", "", "", "", "", mProgressDialog);
                        break;
                    case 3:
                        PayUtils.payByAliPay(mActivity, "", mHandler);
                        break;
                    default:
                        break;
                }
            }
        });
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
                withRx(SystemUtil.pathToFile(strings));
            }
        }
    }
}
