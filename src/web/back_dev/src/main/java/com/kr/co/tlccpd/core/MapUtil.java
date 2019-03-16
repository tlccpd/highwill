package kr.ac.gwnu.ar.util;

import android.location.Location;

public class MapUtil {
	public static float[] getDistanceBeetWeen(double lat,double lng,double lat1,double lng1){
		float[] dist=new float[3];
		
		Location.distanceBetween( lat, lng ,lat1, lng1 , dist);
		
		return dist;
	}
}
