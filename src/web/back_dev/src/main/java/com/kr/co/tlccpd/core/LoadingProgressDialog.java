package kr.ac.gwnu.ar.ui;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingProgressDialog extends ProgressDialog{
	public LoadingProgressDialog(Context context) {
		super(context);
		
		setTitle("Loading");
		setMessage("Loading...");
		setCancelable(false);
	}
}
