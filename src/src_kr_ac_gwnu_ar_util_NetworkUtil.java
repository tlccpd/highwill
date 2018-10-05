package kr.ac.gwnu.ar.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
	public static boolean isOnline(Context context) { // network 연결 상태 확인
		ConnectivityManager cManager; 
		NetworkInfo mobile; 
		NetworkInfo wifi; 
		 
		cManager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
		wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
		 
		if(mobile != null){
			if(mobile.isConnected()){
				return true;
			}
		}
		
		if(wifi != null){
			if(wifi.isConnected()) {
				return true;
			}
		}
		
		return false;
	}
}
