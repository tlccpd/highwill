/*
 * Copyright (C) 2010- Peer internet solutions
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

package org.mixare;

/**
 * The SocialMarker class represents a marker, which contains data from sources
 * like twitter etc. Social markers appear at the top of the screen and show a
 * small logo of the source.
 * 
 * @author hannes
 * 
 */
public class SocialMarker extends LocalMarker {

	public static final int MAX_OBJECTS = 15;

	public SocialMarker(String id, String title, double latitude,
			double longitude, double altitude, String URL, int type, int color) {
		super(id, title, latitude, longitude, altitude, URL, type, color);
	}

	@Override
	public void update(Location curGPSFix) {
		double altitude = curGPSFix.getAltitude()
				+ Math.sin(0.35)
				* distance
				+ Math.sin(0.4)
				* (distance / (MixView.getDataView().getRadius() * 1000f / distance));
		mGeoLoc.setAltitude(altitude);
		super.update(curGPSFix);

	}

	@Override
	public void draw(PaintScreen dw) {

		drawTextBlock(dw);

		if (isVisible) {
			float maxHeight = Math.round(dw.getHeight() / 10f) + 1;
			dw.setStrokeWidth(maxHeight / 10f);
			dw.setFill(false);
			dw.paintCircle(cMarker.x, cMarker.y, maxHeight / 1.5f);
		}
	}

	@Override
	@Override
	public int getMaxObjects() {
		return MAX_OBJECTS;
	}

}
