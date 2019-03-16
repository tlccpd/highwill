/*
 * Copyright (C) 2012- Peer internet solutions 
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
package org.mixare.mgr.webcontent;

import org.mixare.MixContext;

import kr.ac.gwnu.ar.GwnuWayActivity_;


class WebPageMgrImpl implements WebContentManager {

	protected MixContext mixContext;
	
	/**
	 * Shows a webpage with the given url when clicked on a marker.
	 */
	public void loadMixViewWebPage(String url) throws Exception {
		loadWebPage(url, mixContext.getActualMixView());
	}

	public WebPageMgrImpl(MixContext mixContext) {
       this.mixContext=mixContext;
	}
	
	@Override
	@SuppressLint("SetJavaScriptEnabled")
	public void loadWebPage(final String url, final Context context) throws Exception {
		WebView webview = new WebView(context);
		webview.getSettings().setJavaScriptEnabled(true);

		final Dialog d = new Dialog(context) {
			@Override
			public boolean onKeyDown(int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK)
					this.dismiss();
				return true;
			}
		};

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (url.endsWith("return")) {
					d.dismiss();
					mixContext.getActualMixView().repaint();
				} else {
					super.onPageFinished(view, url);
				}
			}

		});

		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.getWindow().setGravity(Gravity.BOTTOM);
		d.addContentView(webview, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,Gravity.BOTTOM));
		
		Button btn = new Button(context);
		btn.setText("길찾기");
		
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//intent
				
				try{
					String s = url.split("\\?")[1].split("=")[1];
					
					Intent intent = new Intent(context,GwnuWayActivity_.class);
					intent.putExtra("end_loc", s);
					context.startActivity(intent);
				}catch(Exception e){
					Toast.makeText(context, "장애가 발생하였습니다.", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});
		
		d.addContentView(btn, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,Gravity.TOP));
		
		if (!processUrl(url, mixContext.getActualMixView())) {
			Log.e("open","open webview");
			d.show();
			webview.loadUrl(url);
		}
	}

	@Override
	public boolean processUrl(String url, Context ctx) {
		List<ResolveInfo> resolveInfos = getAvailablePackagesForUrl(url, ctx);
		List<ResolveInfo> webBrowsers  = getAvailablePackagesForUrl("http://www.google.com", ctx);
		
		for (ResolveInfo resolveInfo : resolveInfos) {
			for (ResolveInfo webBrowser : webBrowsers) { 
				if (!resolveInfo.activityInfo.packageName.equals(webBrowser.activityInfo.packageName)) {
					
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(url));
					intent.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
					ctx.startActivity(intent);
					
					return true;
				}
			}
		}
		
		return false;
	}

	private List<ResolveInfo> getAvailablePackagesForUrl(String url, Context ctx) {
		PackageManager packageManager = ctx.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		
		return packageManager.queryIntentActivities(intent,PackageManager.GET_RESOLVED_FILTER);
	}
}
