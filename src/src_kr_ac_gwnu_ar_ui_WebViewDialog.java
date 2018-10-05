package kr.ac.gwnu.ar.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import kr.ac.gwnu.R;
public class WebViewDialog extends Dialog {
	private Button webview_dialog_close;
	private WebView webview_dialog_view;

	public WebViewDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.webview);

		webview_dialog_close = (Button) findViewById(R.id.activity_article_close_button);
		webview_dialog_view = (WebView) findViewById(R.id.activity_article_web_view);

		webview_dialog_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public void show(String url) {
		super.show();
		
		webview_dialog_view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webview_dialog_view.getSettings().setJavaScriptEnabled(true);
		webview_dialog_view.getSettings().setAllowFileAccess(true);
		webview_dialog_view.setWebChromeClient(new WebChromeClient());
		webview_dialog_view.loadUrl(url);
	}
}
