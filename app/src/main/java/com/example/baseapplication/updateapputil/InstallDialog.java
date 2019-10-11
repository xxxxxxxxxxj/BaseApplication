package com.example.baseapplication.updateapputil;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.baseapplication.R;

/**
 * <p>
 * Title:InstallDialog
 * </p>
 * <p>
 * Description:升级提示dialog
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-1-22 上午11:50:58
 */
public class InstallDialog extends Dialog {
	public static int DIALOGTYPE_ALERT = 1;
	public static int DIALOGTYPE_CONFIRM = 2;

	private int nDialogType = DIALOGTYPE_ALERT;
	private Context mContext;
	private Button btOK;
	private TextView tvCancel;
	private TextView tvTitle;
	private TextView tvMessage;
	private LinearLayout llCancel;
	private LinearLayout llOk;
	private String strTitle;
	private String strMessage;
	private String strOk;
	private String strCancel;
	private int okColorId;
	private int cancelColorId;
	private View vw_install_line;
	private View.OnClickListener positive_listener;
	private View.OnClickListener negative_listener;
	private View.OnClickListener default_positive_listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (nDialogType != DIALOGTYPE_ALERT){
				InstallDialog.this.dismiss();
			}
			if (null != positive_listener)
				positive_listener.onClick(v);
		}
	};
	private View.OnClickListener default_negative_listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			InstallDialog.this.dismiss();
			if (null != negative_listener)
				negative_listener.onClick(v);

		}
	};

	public InstallDialog(Context context) {
		super(context);
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.install_dialog);

		initEnvironment();
		initControls();
	}

	private void initEnvironment() {
		// TODO Auto-generated method stub
		this.getWindow().setBackgroundDrawable(
				new ColorDrawable(Color.TRANSPARENT));
	}

	private void initControls() {
		vw_install_line = (View) findViewById(R.id.vw_install_line);
		vw_install_line.bringToFront();
		tvTitle = (TextView) findViewById(R.id.tv_install_dialog_title);
		tvMessage = (TextView) findViewById(R.id.tv_install_dialog_msg);
		btOK = (Button) findViewById(R.id.btn_install_ok);
		tvCancel = (TextView) findViewById(R.id.tv_install_cancel);
		llCancel = (LinearLayout) findViewById(R.id.ll_install_cancel);
		llOk = (LinearLayout) findViewById(R.id.ll_install_ok);
		if (null != strTitle)
			tvTitle.setText(strTitle);
		if (null != strMessage)
			tvMessage.setText(strMessage);
		if (0 != okColorId)
			btOK.setTextColor(okColorId);
		if (0 != cancelColorId)
			tvCancel.setTextColor(cancelColorId);
		btOK.setOnClickListener(default_positive_listener);
		tvCancel.setOnClickListener(default_negative_listener);
		llOk.setOnClickListener(default_positive_listener);
		llCancel.setOnClickListener(default_negative_listener);
		if (null != strOk)
			btOK.setText(strOk);
		if (null != strCancel)
			tvCancel.setText(strCancel);
		if (nDialogType == DIALOGTYPE_ALERT) {
			llCancel.setVisibility(View.GONE);
			vw_install_line.setVisibility(View.GONE);
		}
	}

	public void setDialogType(int nDialogType) {
		this.nDialogType = nDialogType;
	}

	public void setTitle(String strtitle) {
		this.strTitle = strtitle;
	}

	public void setMessage(String strmessage) {
		this.strMessage = strmessage;
	}

	public void setOkStr(String strok) {
		this.strOk = strok;
	}

	public void setOkTextColor(int colorid) {
		this.okColorId = colorid;
	}

	public void setCancelTextColor(int colorid) {
		this.cancelColorId = colorid;
	}

	public void setCancelStr(String strcancel) {
		this.strCancel = strcancel;
	}

	public void setPositiveListener(View.OnClickListener positive_listener) {
		this.positive_listener = positive_listener;
	}

	public void setNegativeListener(View.OnClickListener negative_listener) {
		this.negative_listener = negative_listener;
	}

	public static class Builder {
		private Context mContext;
		private String strTitle, strMessage, strOK, strCancel;
		private int okColorId, cancelColorId;
		private int nDialogType = DIALOGTYPE_ALERT;
		private boolean cancelable = true;
		private View.OnClickListener positive_listener, negative_listener;

		public Builder(Context context) {
			this.mContext = context;
		}

		public Builder setTitle(String title) {
			this.strTitle = title;
			return this;
		}

		public Builder setMessage(String msg) {
			this.strMessage = msg;
			return this;
		}

		public Builder setType(int nType) {
			this.nDialogType = nType;
			return this;
		}

		public Builder setCancelable(boolean cancelable) {
			this.cancelable = cancelable;
			return this;
		}

		public Builder setOKStr(String strok) {
			this.strOK = strok;
			return this;
		}

		public Builder setOKTextColor(int colorid) {
			this.okColorId = colorid;
			return this;
		}

		public Builder setCancelTextColor(int colorid) {
			this.cancelColorId = colorid;
			return this;
		}

		public Builder setCancelStr(String strcancel) {
			this.strCancel = strcancel;
			return this;
		}

		public Builder positiveListener(View.OnClickListener positiveListener) {
			this.positive_listener = positiveListener;
			return this;
		}

		public Builder negativeListener(View.OnClickListener negativeListener) {
			this.negative_listener = negativeListener;
			return this;
		}

		public InstallDialog build() {
			if (null == mContext)
				return null;
			InstallDialog md = new InstallDialog(mContext);
			md.setDialogType(nDialogType);
			md.setTitle(strTitle);
			md.setMessage(strMessage);
			md.setOkStr(strOK);
			md.setCancelStr(strCancel);
			md.setOkTextColor(okColorId);
			md.setCancelTextColor(cancelColorId);
			md.setCancelable(cancelable);
			md.setPositiveListener(positive_listener);
			md.setNegativeListener(negative_listener);
			return md;
		}

	}

}
