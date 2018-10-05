package kr.ac.gwnu;

import java.util.HashMap;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.ViewById;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import kr.ac.gwnu.constants.Constants;
import kr.co.sa.core.android.auth.SAAuth;
import kr.co.sa.core.android.helper.util.SAAppUtil;
import kr.co.sa.core.util.string.SAStringUtil;

@NoTitle
@EActivity
public class SettingActivity extends Activity {
	private SAAuth saAuth = SAAuth.getInstance(this);
	
	@ViewById
	protected CheckBox setting_check_login, setting_check_push;
	
	@ViewById
	protected EditText setting_et_loginId, setting_et_loginPw;

	@ViewById
	protected Button btn_setting_login;
	
	@ViewById
	protected ImageButton btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		boolean isLoginRequired = getIntent().getBooleanExtra("isLoginRequired", false);
		
		if(isLoginRequired){
			
		}
		
		setting_et_loginId.setText(saAuth.getMemberId());
		setting_et_loginPw.setText(saAuth.getMemberPw());		

		if (saAuth.isAutoLogin()) {
			setting_check_login.setChecked(true);
		} else {
			setting_check_login.setChecked(false);
		}
		
		if (saAuth.isPushReceive()) {
			setting_check_push.setChecked(true);
		} else {
			setting_check_push.setChecked(false);
		}

		setting_check_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				saAuth.setAutoLogin(SAStringUtil.booleanToString(isChecked));
			}
		});
		
		setting_check_push.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				saAuth.setPushReceive(SAStringUtil.booleanToString(isChecked));
			}
		});
	}
	
	@Click
	public void btn_setting_login(){
		String id = setting_et_loginId.getText().toString();
		String pw = setting_et_loginPw.getText().toString();
		
		if(!id.equals("") || !pw.equals("")){
			AQuery aq = new AQuery(SettingActivity.this);
			
			final ProgressDialog dialog = SAAppUtil.getProgressDialog(this,"알림", "로그인중입니다.",false);
			
			HashMap<String, String> param = new HashMap<String, String>();
			param.put("km_bm_id", id);
			param.put("km_bm_pwd", pw);
			
			aq.progress(dialog).ajax(Constants.URL_LOGIN_CHECK, param, String.class,
					new AjaxCallback<String>() {
						@Override
						public void callback(String url, String object,AjaxStatus status) {
							try {
								JSONObject resultJson = new JSONObject(object);
								String result = (String) resultJson.get("result");
								
								if(result.equals("true")){//로그인 성공
									saAuth.setIdAndPw(setting_et_loginId.getText().toString(), setting_et_loginPw.getText().toString());
									
									/* 자동 로그인 체크 - 필수적으로 들어가야 함(작업자 : 함민석) */
									if(setting_check_login.isChecked() == true) {
										saAuth.setAutoLogin("true");
									} else {
										saAuth.setAutoLogin("false");							
									}
									
									/* 푸시알림 체크 - 필수적으로 들어가야 함(작업자 : 함민석) */
									if(setting_check_push.isChecked() == true) {
										saAuth.setPushReceive("true");
									} else {
										saAuth.setPushReceive("false");
									}
									
									Intent intent = new Intent(SettingActivity.this, MainActivity_.class);
									//intent.putExtra("isSettingExtra", true);
									intent.putExtra("isSettingExtra", true);
									startActivity(intent);
									
									finish();
								}else{
									SAAppUtil.alertMessage(SettingActivity.this,"로그인에 실패했습니다.");
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}
						};
					});
		}else{
			SAAppUtil.alertMessage(SettingActivity.this,"모두 입력하세요");
		}
	}
		
	@Click
	public void btn_back(){
		Intent intent = new Intent(this, MainActivity_.class);
		startActivity(intent);
		finish();
	}
}