package kr.ac.gwnu.ar.map;

import java.util.HashMap;
import java.util.Iterator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class GoogleMapDrawer {
	private GoogleMap googleMap;
	
	public GoogleMapDrawer(GoogleMap googleMap) {
		this.googleMap = googleMap;
	}

	private HashMap<String, MarkerOptions> markers = new HashMap<String, MarkerOptions>();
	private HashMap<String, PolylineOptions> polyLines = new HashMap<String, PolylineOptions>();
	
	public void addMarker(String name, MarkerOptions markerOptions) {
		markers.put(name, markerOptions);

		googleMapMarkerRedraw();
	}

	public void removeMarker(String name) {
		markers.remove(name);

		googleMapMarkerRedraw();
	}

	public void googleMapMarkerRedraw() {
		googleMap.clear();

		Iterator<String> iter = markers.keySet().iterator();

		while (iter.hasNext()) {
			MarkerOptions mark = markers.get(iter.next());
			
			Marker marker = googleMap.addMarker(mark);
			marker.showInfoWindow();
		}
		
		Iterator<String> iter1 = polyLines.keySet().iterator();
		
		while (iter1.hasNext()) {
			PolylineOptions polyLine = polyLines.get(iter1.next());
			
			googleMap.addPolyline(polyLine);
		}
	}
	
	public void addPolyLine(String name,PolylineOptions polyLineOptions){
		polyLines.put(name, polyLineOptions);
		
		googleMapMarkerRedraw();
	}
	
	public void removePolyLine(String name){
		polyLines.remove(name);
		
		googleMapMarkerRedraw();
	}
	
	public void clearPolyLine(){
		polyLines.clear();
		
		googleMapMarkerRedraw();
	}
}
