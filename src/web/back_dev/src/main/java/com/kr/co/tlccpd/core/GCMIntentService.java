package kr.ac.gwnu;

import java.net.URLDecoder;

import com.google.android.gcm.GCMBaseIntentService;
import kr.ac.gwnu.R;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import kr.ac.gwnu.ar.GwnuIntroActivity_;
import kr.ac.gwnu.constants.Constants;
import kr.co.sa.core.android.helper.gcm.SAGcm;
import kr.co.sa.core.android.helper.util.SAAppUtil;

public class GCMIntentService extends GCMBaseIntentService {
	private SAGcm saGcm = new SAGcm(this);
	
	@Override
	protected void onError(Context context, String arg1) {
		SAAppUtil.alertMessage(this,"에러가 발생했습니다.");
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		try{
			String message = "";
			String msg = intent.getStringExtra("msg");
			String msg_multi = intent.getStringExtra("msg_multi"); 
			
			if(msg_multi != null){
				message = URLDecoder.decode(msg_multi,"EUC-KR");
			}
			
			if(msg != null){
				message = msg;
			}
			
			saGcm.notifyGCM(R.drawable.ic_launcher,"강릉원주대학교",message, GwnuIntroActivity_.class);
		}catch(Exception e){
		}
	}
	
	@Override
	protected void onRegistered(Context context, String reg_id) {
		Log.e("키를 등록합니다.(GCM INTENTSERVICE)", reg_id);
		
		try{
			saGcm.registerGCMtoServer(Constants.APIKEY,Constants.URL_PUSH_REGISTER);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.e("키를 제거합니다.(GCM INTENTSERVICE)", "제거되었습니다.");
	}
}
