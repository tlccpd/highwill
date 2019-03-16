package kr.ac.gwnu.ar;

import java.util.HashMap;
import java.util.Iterator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import kr.ac.gwnu.R;
import kr.ac.gwnu.ar.application.GwnuApplication;
import kr.ac.gwnu.ar.remote.RemoteLocation;
import kr.ac.gwnu.ar.remote.RemoteLocationCallback;
import kr.ac.gwnu.ar.remote.RemoteLocationManager;
import kr.ac.gwnu.ar.ui.LoadingProgressDialog;
import kr.ac.gwnu.ar.ui.WebViewDialog;
import kr.ac.gwnu.ar.util.NetworkUtil;
import kr.ac.gwnu.ar.util.UiHelper;
@NoTitle
@Fullscreen
@EActivity(R.layout.gwnu_map_acitivity)
public class GwnuMapActivity extends FragmentActivity{
	@App
	GwnuApplication application;
	
	private RemoteLocationManager remoteLocationManager = new RemoteLocationManager();
	
	protected ProgressDialog dialog;
	
	@ViewById
	protected Button btn_ar,btn_way,btn_map,btn_campus_select;
	
	@FragmentById
	protected SupportMapFragment mapFragment;

	protected GoogleMap googleMap;
	
	public void initUi() {
		dialog = new LoadingProgressDialog(this);
		
		btn_way.getBackground().setAlpha(90);
		btn_ar.getBackground().setAlpha(90);
		btn_campus_select.getBackground().setAlpha(90);
	}
	
	@AfterViews
	public void myinit() {
		try{
			initUi();
			
			this.googleMap = mapFragment.getMap();
			
			googleMap.setOnMarkerClickListener(markClickListener);
			googleMap.setMyLocationEnabled(true);
			
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(application.getConfig().getCenterLoc()));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
			
			loadLocations();	
		}catch(Exception e){
			btn_campus_select();
			e.printStackTrace();
		}
	}
	
	public void loadLocations() {
		dialog.show();
		initLocations();
	}
	
	@Background
	public void initLocations() {
		try{
			remoteLocationManager.getRemoteLocations(application.getConfig().getRemoteLocationUrl(), new RemoteLocationCallback() {
				@Override
				public void callback(HashMap<String,RemoteLocation> remoteLocations) {
					complateLoadLocations(remoteLocations);
				}
			});			
		}catch(Exception e){
			e.printStackTrace();
			
			loadLocationError();
		}
	}
	
	@UiThread
	public void loadLocationError(){
		dialog.dismiss();
		
		UiHelper.alert(this, "데이터를 가지고오지 못했습니다.\r\n잠시후에 다시 시도하세요.");
	}
	
	@UiThread
	public void complateLoadLocations(HashMap<String,RemoteLocation> remoteLocations){
		dialog.dismiss();
		
		Iterator<String> iter = remoteLocations.keySet().iterator();
		
		while(iter.hasNext()){
			RemoteLocation loc = remoteLocations.get(iter.next());
			
			MarkerOptions mark = new MarkerOptions();
			mark.title(loc.getTitle());
			mark.position(new LatLng(loc.getLat(), loc.getLng()));
			
			googleMap.addMarker(mark);
		}
	}
	
	private OnMarkerClickListener markClickListener = new OnMarkerClickListener() {
		@Override
		public boolean onMarkerClick(Marker mark) {
			try {
				RemoteLocation location = remoteLocationManager.getRemoteLocation(mark.getTitle());
				new WebViewDialog(GwnuMapActivity.this).show(location.getWebpage()+"?loc="+location.getTitle());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return false;
		}
	};
	
	@Click
	void btn_way(){
		Intent i = new Intent(this,GwnuWayActivity_.class);
		startActivity(i);
		finish();
	}
	
	@Click
	void btn_ar(){
		if(!NetworkUtil.isOnline(this)){
			UiHelper.alert(this, "인터넷에 연결되어있지 않으면 사용할수 없습니다.");
			return;
		}
		
		Intent i = new Intent();
		i.setAction(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse(application.getConfig().getRemoteLocationUrl()),"application/mixare-json");
		startActivity(i);
		
		finish();
	}
	
	@Click
	void btn_campus_select(){
		Intent i = new Intent(this,GwnuMenuActivity_.class);
		startActivity(i);
		finish();
	}
	
}