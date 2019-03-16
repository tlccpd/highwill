package kr.ac.gwnu.ar;

import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Fullscreen;
import com.googlecode.androidannotations.annotations.NoTitle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import kr.ac.gwnu.MainActivity_;
import kr.ac.gwnu.R;
@NoTitle
@Fullscreen
@EActivity
public class GwnuIntroActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ImageView imageView = new ImageView(this);
		addContentView(imageView, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		imageView.setImageResource(R.drawable.patterns);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
				
				Intent i = new Intent(GwnuIntroActivity.this, MainActivity_.class);
				startActivity(i);
				finish();
			}
		}).start();
	}
}
