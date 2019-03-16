package kr.ac.gwnu.ar.remote;

import java.util.HashMap;

public interface RemoteLocationCallback {
	void callback(HashMap<String, RemoteLocation> remoteLocations);
}