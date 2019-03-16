package kr.ac.gwnu.ar.remote;

public class RemoteLocation {
	private double lat;
	private double lng;
	private String title;
	private String webpage;
	private String object_url;
	private int has_detail_page;
	private int elevation;

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWebpage() {
		return webpage;
	}

	public void setWebpage(String webpage) {
		this.webpage = webpage;
	}

	public String getObject_url() {
		return object_url;
	}

	public void setObject_url(String object_url) {
		this.object_url = object_url;
	}

	public int getHas_detail_page() {
		return has_detail_page;
	}

	public void setHas_detail_page(int has_detail_page) {
		this.has_detail_page = has_detail_page;
	}

	public int getElevation() {
		return elevation;
	}

	public void setElevation(int elevation) {
		this.elevation = elevation;
	}

	@Override
	public String toString() {
		return "RemoteLocation [lat=" + lat + ", lng=" + lng + ", title="
				+ title + ", webpage=" + webpage + ", object_url=" + object_url
				+ ", has_detail_page=" + has_detail_page + ", elevation="
				+ elevation + "]";
	}

}
