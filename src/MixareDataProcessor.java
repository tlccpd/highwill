/*
 * Copyright (C) 2012- Peer internet solutions & Finalist IT Group
 * 
 * This file is part of mixare.
 * 
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details. 
 * 
 * You should have received a copy of the GNU General Public License along with 
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package org.mixare.data.convert;

import org.mixare.data.DataHandler;

import kr.ac.gwnu.ar.mixare.GwnuImageMarker;

/**
 * A data processor for custom urls or data, Responsible for converting raw data (to json and then) to marker data.
 * @author A. Egal
 */
public class MixareDataProcessor extends DataHandler implements DataProcessor{
	public static final int MAX_JSON_OBJECTS = 1000;
	
	private static final String TAG = MixareDataProcessor.class.getName();
	
	@Override
	@Override
	public String[] getUrlMatch() {
		String[] str = new String[0]; //only use this data source if all the others don't match
		return str;
	}

	@Override
	@Override
	public String[] getDataMatch() {
		String[] str = new String[0]; //only use this data source if all the others don't match
		return str;
	}
	
	@Override
	@Override
	public boolean matchesRequiredType(String type) {
		return true; //this datasources has no required type, it will always match.
	}

	@Override
	@Override
	public List<Marker> load(String rawData, int taskId, int colour) throws JSONException {
		List<Marker> markers = new ArrayList<Marker>();
		JSONObject root = convertToJSON(rawData);
		JSONArray dataArray = root.getJSONArray("results");
		int top = Math.min(MAX_JSON_OBJECTS, dataArray.length());

			for (int i = 0; i < top; i++) {
				JSONObject jo = dataArray.getJSONObject(i);
				
				Marker ma = null;
				
				if (jo.has("title") && jo.has("lat") && jo.has("lng") && jo.has("elevation")) {
					Bitmap image = getBitmapFromURL(jo.getString("object_url"));
					
					String link=null;
					String title = HtmlUnescape.unescapeHTML(jo.getString("title"), 0);
					
					if(jo.has("has_detail_page") && jo.getInt("has_detail_page")!=0 && jo.has("webpage"))
						link=jo.getString("webpage");
					
					ma = new GwnuImageMarker(
							"id_"+i,
							HtmlUnescape.unescapeHTML(jo.getString("title"), 0), 
							jo.getDouble("lat"), 
							jo.getDouble("lng"), 
							jo.getDouble("elevation"), 
							link+"?loc="+title, 
							taskId, colour,image);

					/*
					ma = new POIMarker(
							"id_"+i,
							HtmlUnescape.unescapeHTML(jo.getString("title"), 0), 
							jo.getDouble("lat"), 
							jo.getDouble("lng"), 
							jo.getDouble("elevation"), 
							link +"?loc="+title, 
							taskId, colour);
					*/
					
					markers.add(ma);
				}
			}
			
			return markers;
	}
	
	private JSONObject convertToJSON(String rawData){
		try {
			return new JSONObject(rawData);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			return BitmapFactory.decodeStream(input);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}		
	}
}
