AnimatedColorPickerDialog
=========================

![](https://raw.githubusercontent.com/jianghejie/AnimatedColorPickerDialog/master/screenshots/device-2014-09-30-133000.png)  
a simple dialog used to pick a color ,much like the colorpickdialog in the calendar of android 4.4.
 to use it you just just need to call it's static class Builder like this:
 
 		new  AnimatedColorPickerDialog.Builder(MainActivity.this)
		.setTitle("choose a color")
		.setColors(styleColors)
		.setOnColorClickListener(new AnimatedColorPickerDialog.ColorClickListener() {
			@Override
			public void onColorClick(int color) { 
			  ...
			}	
		}).create().show();
