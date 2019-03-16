package kr.ac.gwnu;

import java.io.InputStreamReader;
import java.security.spec.EncodedKeySpec;

import com.google.android.gcm.GCMRegistrar;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaCodecInfo.EncoderCapabilities;
import android.os.Bundle;
import android.util.Xml.Encoding;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import kr.ac.gwnu.ar.GwnuMenuActivity_;
import kr.ac.gwnu.constants.Constants;
import kr.co.sa.core.android.auth.SAAuth;
import kr.co.sa.core.android.helper.gcm.SAGcm;
import kr.co.sa.core.android.helper.util.SAAppUtil;
import kr.co.sa.core.android.ui.webview.SAWebChromeClientWrapper;
import kr.co.sa.core.android.ui.webview.SAWebViewClientWrapper;
import kr.co.sa.core.android.ui.webview.SAWebViewHelper;

@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
@NoTitle
@EActivity
public class MainActivity extends Activity {
	private SAGcm saGcm = new SAGcm(this);
	private SAAuth saAuth = SAAuth.getInstance(this);
	
	public static String currentUrl = "";
	
	@ViewById
	protected WebView main_webview;

	@ViewById
	protected ProgressBar webview_progressBar;

	@ViewById
	protected ImageButton btn1, btn2, btn3, btn5;

	private boolean settingFlag;
	
	public void setWebviewClient() {
		SAWebViewHelper.setDefaultWebView(main_webview);
		
		main_webview.getSettings().setBuiltInZoomControls(true);
		main_webview.getSettings().setJavaScriptEnabled(true);
		main_webview.setVerticalScrollbarOverlay(true);
		main_webview.setHorizontalScrollBarEnabled(false);
		main_webview.setVerticalScrollBarEnabled(false);
		
		new SAWebChromeClientWrapper(main_webview, "강릉원주대학교", webview_progressBar);
		
		new SAWebViewClientWrapper(this, main_webview){
			@Override
			public boolean sa_new_shouldOverrideUrlLoading(WebView view, final String url) {				
				addBasicBrowserOpenUrl("downLoad.mbs");
				addBasicBrowserOpenUrl("mail.gwnu.ac.kr");
				addBasicBrowserOpenUrl("fileDownServlet");
				
				if (url.contains("/auth/logout.do")) {
					saAuth.setIdAndPw("", "");
					saAuth.setAutoLogin("false");
					saAuth.setPushReceive("false");
					saAuth.resetIdAndPw();
					
					return true;
				}
				
				if (url.contains("/auth/login.do")) {					
					Intent intent = new Intent(MainActivity.this, SettingActivity_.class);
					intent.putExtra("startFromLogin", true);
					
					startActivity(intent);
					return true;
				}
				
				if (url.contains("/intro/kangnung.do") || url.contains("/intro/wonju.do")) {
					main_webview.getSettings().setSupportZoom(true);
					main_webview.getSettings().setBuiltInZoomControls(true);
					main_webview.getSettings().setDisplayZoomControls(false);
				}
				
				view.loadUrl(url);
				
				return true;
			}
		};
	}

	public void init() {
		settingFlag = getIntent().getBooleanExtra("isSettingExtra", false);
		
		saGcm.registerGCMtoServer(Constants.APIKEY,Constants.URL_PUSH_REGISTER);
		
		setWebviewClient();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
		
		//설정화면에서 넘어온경우
		if (settingFlag) {
			login();
		} 
		
		//그냥 시작한경우
		if(!settingFlag){
			if (saAuth.isAutoLogin()) {
				login();
			} else {
				main_webview.loadUrl(Constants.URL_INDEX);
			}
		}
	}
	
	public void login() {
		StringBuffer param = new StringBuffer();
		param.append("km_bm_id=");
		param.append(saAuth.getMemberId());
		param.append("&km_bm_pwd=");
		param.append(saAuth.getMemberPw());
		param.append("&deviceToken=");
		param.append(GCMRegistrar.getRegistrationId(this));		
		
		main_webview.postUrl(Constants.URL_LOGIN, Encoding.UTF_16);
	}

	@Click
	public void btn1() {
		main_webview.loadUrl(Constants.URL_INDEX);
	}

	@Click
	public void btn2() {
		Intent intent = new Intent(this,GwnuMenuActivity_.class);
		SAAppUtil.startActivity(this, intent, false);
	}

	@Click
	public void btn3() {
		main_webview.loadUrl(Constants.URL_FACEBOOK);
	}

	@Click
	public void btn5() {
		Intent intent = new Intent(this, SettingActivity_.class);
		SAAppUtil.startActivity(this, intent, false);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (main_webview != null) {
			if ((String.valueOf(main_webview.getUrl()).indexOf("http://m.gwnu.ac.kr/index.do") > -1) || main_webview.getUrl().equals("http://m.gwnu.ac.kr/") || main_webview.getUrl().equals("http://m.gwnu.ac.kr") || main_webview.getUrl().equals("https://m.gwnu.ac.kr") || main_webview.getUrl().equals("https://m.gwnu.ac.kr/") || currentUrl.equals("https://m.gwnu.ac.kr/index.do")) {
				SAAppUtil.appFinsh(this);
				return true;
			}
		}

		if (keyCode == KeyEvent.KEYCODE_BACK && main_webview.canGoBack()) {
			main_webview.goBack();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK && !main_webview.canGoBack()) {
			SAAppUtil.appFinsh(this);
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_1){
			SAAppUtil.appFinsh(this);
		}
		return super.onOptionsItemSelected(item);
	}
}
