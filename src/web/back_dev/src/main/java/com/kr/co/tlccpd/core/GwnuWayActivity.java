package kr.ac.gwnu.ar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.Toast;
import kr.ac.gwnu.ar.application.GwnuApplication;
import kr.ac.gwnu.ar.map.GoogleMapDrawer;
import kr.ac.gwnu.ar.ui.ListDialog;
import kr.ac.gwnu.ar.ui.ListDialogClickListener;
import kr.ac.gwnu.ar.ui.LoadingProgressDialog;
import kr.ac.gwnu.ar.util.MapUtil;
import kr.ac.gwnu.ar.util.NetworkUtil;
import kr.ac.gwnu.ar.util.UiHelper;
import kr.ac.gwnu.ar.way.dijkstra.Node;
import kr.ac.gwnu.ar.way.dijkstra.NodeManager;
import kr.ac.gwnu.R;
@NoTitle
@Fullscreen
@EActivity(R.layout.gwnu_way_acitivity)
public class GwnuWayActivity extends FragmentActivity {
	@App
	GwnuApplication application;
	
	private NodeManager nm = new NodeManager();
	
	protected ProgressDialog dialog;
	protected ListDialog listDialog;
	
	@ViewById
	protected Button start_pos, end_pos;

	@ViewById
	protected Button btn_search,btn_ar,btn_way,btn_map,btn_campus_select;
	
	@FragmentById
	protected SupportMapFragment mapFragment;

	protected GoogleMap googleMap;

	protected GoogleMapDrawer googleMapDrawer;
	
	private String start_loc = "", end_loc = "";
	
	private boolean is_searched = false;
	
	private Location mylocation;
	
	public void initUi() {
		dialog = new LoadingProgressDialog(this);
		dialog.show();
		
		listDialog = new ListDialog(this);
		
		btn_map.getBackground().setAlpha(90);
		btn_ar.getBackground().setAlpha(90);
		btn_campus_select.getBackground().setAlpha(90);
	}

	@AfterViews
	public void myinit() {
		try{
			initUi();
			
			this.googleMap = mapFragment.getMap();
			
			googleMapDrawer = new GoogleMapDrawer(googleMap);
			
			if(!NetworkUtil.isOnline(this))
				loadLocations();
			
			googleMap.setMyLocationEnabled(true);
			googleMap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
				@Override
				public void onMyLocationChange(Location arg0) {
					mylocation = arg0;
					
					if(!is_searched){
						loadLocations();
						is_searched = true;
					}
				}
			});
			
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(application.getConfig().getCenterLoc()));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
			
			gpsCheck();
			
			//loadLocations();
		}catch(Exception e){
			e.printStackTrace();
			btn_campus_select();
		}
	}
	
	public void gpsCheck(){
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			UiHelper.alert(this, "GPS를 사용할수 없습니다. 네트워크를 이용하여 위치를 검색합니다. 오차범위가 늘어날수 있습니다.");
        }
	}
	
	public void loadLocations() {
		initLocations();
	}
	
	@Background
	public void initLocations() {
		try {
			if (NetworkUtil.isOnline(this)){
				nm.getRemoteNodes(this,application.getConfig().getRemoteNodeUrl());
				nm.getRemoteEdges(this,application.getConfig().getRemoteEdgeUrl());
			}else{
				nm.getLocalNodes(this);
				nm.getLocalEdges(this);
			}
			
			complateLoadLocations();
		} catch (Exception e) {
			
			loadLocationError();
			e.printStackTrace();
		}
	}
	
	@UiThread
	public void loadLocationError(){
		dialog.dismiss();
		
		UiHelper.alert(this, "데이터를 가지고오지 못했습니다.\r\n잠시후에 다시 시도하세요.");
	}

	@UiThread
	public void complateLoadLocations() {
		dialog.dismiss();

		listDialog.setListItem(getLocationNameList(false));
		
		String webpage_m = getIntent().getStringExtra("end_loc");
		
		if(webpage_m != null){
			start_loc = getStartPos("현재위치");
			end_loc = webpage_m;
			
			drawStart();
			drawEnd();
			
			btn_search();
		}
	}
	
	@Click
	void start_pos() {
		listDialog.setListDialogClickListener(new ListDialogClickListener() {
			@Override
			public void onClick(String item, int position) {
				start_loc = getStartPos(item);
				
				drawStart();
				
				listDialog.cancel();
			}
		}).show();
	}
	
	public void drawStart(){
		googleMapDrawer.clearPolyLine();
		googleMapDrawer.removeMarker("출발");
		
		Node node = nm.getNode(start_loc);
		
		LatLng latLng = new LatLng(node.getPosition_x(), node.getPosition_y());
		
		googleMapDrawer.addMarker("출발",new MarkerOptions().title(start_loc).position(latLng));
		
		Toast.makeText(GwnuWayActivity.this, "출발 : "+start_loc, Toast.LENGTH_SHORT).show();
		
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
	}
	
	public void drawEnd(){
		googleMapDrawer.clearPolyLine();
		googleMapDrawer.removeMarker("도착");

		Node node = nm.getNode(end_loc);
		
		LatLng latLng = new LatLng(node.getPosition_x(), node.getPosition_y());
		
		googleMapDrawer.addMarker("도착",new MarkerOptions().title(end_loc).position(latLng));

		Toast.makeText(GwnuWayActivity.this, "도착 : "+end_loc, Toast.LENGTH_SHORT).show();
		
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
	}
	
	public String getStartPos(String item){
		if("현재위치".equals(item)){
			Location loc = mylocation;
			
			if(loc == null){
				UiHelper.alert(GwnuWayActivity.this, "현재위치를 가저올수 없습니다. 잠시후 다시 시도하세요.");
				return "";
			}
			
			return getMinLocFromTarget(loc.getLatitude(), loc.getLongitude()).getName();
		}else{
			return item;
		}
	}

	@Click
	void end_pos() {
		listDialog.setListDialogClickListener(new ListDialogClickListener() {
			@Override
			@Override
			public void onClick(String item, int position) {
				end_loc = getStartPos(item);
				
				drawEnd();
				
				listDialog.cancel();
			}
		}).show();
	}

	@Click
	void btn_search() {
		if (start_loc.length() == 0) {
			UiHelper.alert(this, "출발지를 선택하세요.");
			return;
		}

		if (end_loc.length() == 0) {
			UiHelper.alert(this, "도착지를 선택하세요.");
			return;
		}

		LinkedList<Node> path = nm.getPath(start_loc, end_loc);

		PolylineOptions rectOptions = new PolylineOptions();

		rectOptions.width(6.5f);
		rectOptions.color(getResources().getColor(R.color.Blue));

		for (Node node : path) {
			Log.e("node draw","node : " + node);
			rectOptions.add(new LatLng(node.getPosition_x(), node.getPosition_y()));
		}
		
		googleMapDrawer.addPolyLine("myPolyLine", rectOptions);
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
	}
	
	@Click
	void btn_map(){
		if(!NetworkUtil.isOnline(this)){
			UiHelper.alert(this, "인터넷에 연결되어있지 않으면 사용할수 없습니다.");
			return;
		}
		
		Intent i = new Intent(this,GwnuMapActivity_.class);
		startActivity(i);
	}
	
	@Click
	void btn_campus_select(){
		Intent i = new Intent(this,GwnuMenuActivity_.class);
		startActivity(i);
		finish();
	}
	
	/**
	 * 현재 위치에서 도착지까지의 가장 가까운 장소를 리턴한다.
	 * @param lat
	 * @param lng
	 * @return
	 */
	private Node getMinLocFromTarget(double lat,double lng) {
		int minNode = 0;
		float min = 0;
		
		List<Node> nodes = nm.getNodes();
		
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);

			if (i == 0) {
				min = MapUtil.getDistanceBeetWeen(lat,lng, node.getPosition_x(),node.getPosition_y())[0];
			}

			float[] a = MapUtil.getDistanceBeetWeen(lat,lng, node.getPosition_x(),node.getPosition_y());

			if (a[0] < min) {
				min = a[0];
				minNode = i;
			}
		}

		return nodes.get(minNode);
	}
	
	private List<String> getLocationNameList(boolean includeWay) {
		List<String> result = new ArrayList<String>();

		for (Node node : nm.getNodes()) {
			String name = node.getName();

			if (includeWay) {
				result.add(name);
			} else {
				if (name.indexOf("길") == -1) {
					result.add(name);
				}
			}
		}
		
		Collections.sort(result,new Comparator<String>() {
			@Override
			public int compare(String name1, String name2) {
				return name1.compareTo(name2);
			}
		});
		
		if(NetworkUtil.isOnline(this))
			result.add("현재위치");

		return result;
	}
}