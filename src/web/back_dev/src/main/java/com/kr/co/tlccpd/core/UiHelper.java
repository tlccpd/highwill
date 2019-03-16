package kr.ac.gwnu.ar.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class UiHelper {
	public static void alert(Context context, String message) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("알림");
		dialog.setMessage(message);
		dialog.setNegativeButton("닫기", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dialog.show();
	}
}