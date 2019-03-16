package kr.ac.gwnu.ar.mixare;

import org.mixare.LocalMarker;
import org.mixare.lib.gui.PaintScreen;

import android.graphics.Bitmap;
import android.location.Location;

public class GwnuImageMarker extends LocalMarker{
	private Bitmap bitmap = null;
	
	public GwnuImageMarker(String id, String title, double latitude,double longitude, double altitude, String link, int type, int colour) {
		super(id, title, latitude, longitude, altitude, link, type, colour);
	}
	
	public GwnuImageMarker(String id, String title, double latitude,double longitude, double altitude, String link, int type, int colour,Bitmap bitmap) {
		super(id, title, latitude, longitude, altitude, link, type, colour);
		
		Log.e("draw","draw : " + title);
		
		this.bitmap = bitmap;
	}
	@Override
	public int getMaxObjects() {
		return 20;
	}

	@Override
	public void update(Location curGPSFix) {
		super.update(curGPSFix);
	}
	
	@Override
	public void drawCircle(PaintScreen dw) {
		if (isVisible) {
			

			if (distance < 100.0){
				
			}else{
				dw.setStrokeWidth(0);
				dw.setFill(false);
				dw.setColor(getColour());
				
				dw.paintCircle(cMarker.x ,cMarker.y, 0);
				
				int left = (int) (cMarker.x/1.2);
				int top  = (int) (cMarker.y/1.2);
				
				dw.paintBitmap(bitmap, left,top);
			}
		}
	}
	
	@Override
	public void drawTextBlock(PaintScreen dw) {
		float maxHeight = Math.round(dw.getHeight() / 10f) + 1;

		String textStr = "";

		double d = distance;
		
		DecimalFormat df = new DecimalFormat("@#");
		if (d < 1000.0) {
			textStr = title + " (" + df.format(d) + "m)";
		} else {
			d = d / 1000.0;
			textStr = title + " (" + df.format(d) + "km)";
		}

		textBlock = new TextObj(textStr, Math.round(maxHeight / 2f) + 1, 250,dw, underline);
		
		if (isVisible) {
			if (distance < 100.0) {
				textBlock.setBgColor(Color.argb(128, 52, 52, 52));
				textBlock.setBorderColor(Color.rgb(255, 104, 91));
			} else {
				textBlock.setBgColor(Color.argb(128, 0, 0, 0));
				textBlock.setBorderColor(Color.rgb(255, 255, 255));
			}

			float currentAngle = MixUtils.getAngle(cMarker.x, cMarker.y,signMarker.x, signMarker.y);
			txtLab.prepare(textBlock);
			
			dw.setStrokeWidth(1f);
			dw.setFill(true);
			dw.paintObj(txtLab, signMarker.x - txtLab.getWidth() / 2,signMarker.y + maxHeight, currentAngle + 90, 1);
			
		}
	}
	
	@Override
	public String toString() {
		return "title:"+this.getTitle()+",lat:"+this.getLatitude()+",lng:"+this.getLongitude();
	}
}