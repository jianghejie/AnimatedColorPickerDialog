package com.jcodecraeer.animatedcolorpickerdialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    private View colorDisplayView;
    public int[] styleColors =new int[]{0xff000000, 0xff4491df, 0xffbe3522, 0xff0a9341, 0xff00BFFF, 0xff0a9341, 0xff27408B, 0xffCD6600, 0xff53868B, 0xffA020F0};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		colorDisplayView = findViewById(R.id.display_color);
	}
	
	public void openColorDialog(View v) {
		new  AnimatedColorPickerDialog.Builder(MainActivity.this)
		.setTitle("选择一种颜色")
		.setColors(styleColors)
		.setOnColorClickListener(new AnimatedColorPickerDialog.ColorClickListener() {
			@Override
			public void onColorClick(int color) { 
				colorDisplayView.setBackgroundColor(color);
			}	
		}).create().show();
	}
}
