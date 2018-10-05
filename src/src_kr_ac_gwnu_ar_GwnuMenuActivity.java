package kr.ac.gwnu.ar;

import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;

import kr.ac.gwnu.ar.application.GwnuApplication;
import kr.ac.gwnu.ar.config.GwnuCampus;

@NoTitle
@Fullscreen
@EActivity(R.layout.gwnu_menu_acitivity)
public class GwnuMenuActivity extends Activity{
	@App
	GwnuApplication application;
	
	@Click
	void button1(){
		application.setCampus(GwnuCampus.GANGNEUNG);
		
		startWayActivity();
	}
	
	@Click
	void button2(){
		application.setCampus(GwnuCampus.WONJU);
		
		startWayActivity();
	}
	
	private void startWayActivity(){
		Intent i = new Intent(this,GwnuWayActivity_.class);
		startActivity(i);
	}
}
