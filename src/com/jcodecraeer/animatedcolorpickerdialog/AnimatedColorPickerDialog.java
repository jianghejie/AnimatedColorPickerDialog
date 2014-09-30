package com.jcodecraeer.animatedcolorpickerdialog;
 
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.OvershootInterpolator;
import android.widget.GridLayout;
import android.widget.GridLayout.Spec;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
public class AnimatedColorPickerDialog extends Dialog  {
    Context context;
    public static final int DIALOG_TYPE_MESSAGE = 0;
    public static final int DIALOG_TYPE_PROGRESS = 2;
    public static final int DIALOG_TYPE_ALERT = 3;
    private ColorClickListener mListener;
    
    private AnimatedColorPickerDialog(Context context) {
        this(context,R.style.CustomTheme);
    }
    private AnimatedColorPickerDialog(Context context, int theme){
        super(context, theme);
        this.context = context; 
    }
    
    public interface ColorClickListener {
        void onColorClick(int color);
    }
    
    public void setOnColorClickListener(ColorClickListener l ){
    	mListener = l;
    }
    
    private int getDarkerColor(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv); // convert to hsv
        // make darker
        hsv[1] = hsv[1] + 0.1f; // more saturation
        hsv[2] = hsv[2] - 0.2f; // less brightness
        int darkerColor = Color.HSVToColor(hsv);
        return  darkerColor ;
    }
    
    private StateListDrawable getStateDrawable(Drawable normal, Drawable pressed, Drawable focus) {
        StateListDrawable sd = new StateListDrawable();
        sd.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
        sd.addState(new int[]{android.R.attr.state_focused}, focus);
        sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
        sd.addState(new int[]{android.R.attr.state_enabled}, normal);
        sd.addState(new int[]{}, normal);
        return sd;
    }      
    @SuppressLint("NewApi")
	public void setnumberOfColums(int columnum, int[] colors) {
    	TableLayout colorTable = (TableLayout)findViewById(R.id.color_group);
        int rows = colors.length % columnum == 0 ? colors.length / columnum : (colors.length / columnum) + 1;
        for(int r=0; r < rows; r++) {
            TableRow tableRow = new TableRow(context);
        	for(int c = 0; c < columnum && (c + r * columnum) < colors.length ; c++) {
            	final View item = new View(context);
            	LayoutParams params = new LayoutParams((int)context.getResources().getDimension(R.dimen.color_circle_size), (int)context.getResources().getDimension(R.dimen.color_circle_size));  
            	item.setLayoutParams(params);        		
            	ShapeDrawable   drawableNormal = new ShapeDrawable (new OvalShape ());
            	drawableNormal.getPaint().setColor(colors[r * columnum + c]);
            	ShapeDrawable   drawablePress = new ShapeDrawable (new OvalShape ());
            	drawablePress.getPaint().setColor(getDarkerColor(colors[r * columnum + c]));
       
            	ShapeDrawable   drawableFocus = new ShapeDrawable (new OvalShape ());
            	drawableFocus.getPaint().setColor(getDarkerColor(colors[r * columnum + c]));
            	
            	item.setBackground(getStateDrawable(drawableNormal, drawablePress, drawableFocus));
            	item.setTag(Integer.valueOf(colors[r * columnum + c]));
            	item.setOnClickListener(new View.OnClickListener(){
    		           @Override
    		           public void onClick(View view){
    		        	   if(mListener != null){
    		        		   Integer colorHexInObject = (Integer)item.getTag();
    		        		   mListener.onColorClick(colorHexInObject.intValue()); 
    		        		   dismiss();
    		        	   }  
    		           }
    	        });    	
                LinearLayout itemContainer = new LinearLayout(context);
                TableRow.LayoutParams pa = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                pa.setMargins(0, 0, 0, 10); 
                itemContainer.setLayoutParams(pa);
                itemContainer.addView(item);
            	tableRow.addView(itemContainer);   
        	}
            colorTable.addView(tableRow);          
         } 	
        //视差效果动画效果
        TextView dialogTitleText =  (TextView)findViewById(R.id.dialog_title);
        dialogTitleText.setTranslationX(-colorTable.getWidth());
		final AnimatorSet localAnimatorSet = new AnimatorSet();
		ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(dialogTitleText, "translationX", -colorTable.getWidth(),0);
		localObjectAnimator1.setDuration(1200);
	    localObjectAnimator1.setInterpolator(new OvershootInterpolator(1.2F));	 
	 
	    localAnimatorSet.play(localObjectAnimator1);
	    colorTable.setTranslationX(-colorTable.getWidth());
	    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(colorTable, "translationX", -colorTable.getWidth(),0);
	    localObjectAnimator2.setDuration(1200);
	    localObjectAnimator2.setInterpolator(new OvershootInterpolator(1.2F));
	    localAnimatorSet.play(localObjectAnimator2).after(100);
	    localAnimatorSet.start();        
        
    }
    public static class Builder {
        private Context context;  
        private ColorClickListener mListener;
        private int[] colors;
        private String title;
        public Builder(Context context) {  
            this.context = context;  
        }  
        
        public Builder setOnColorClickListener(ColorClickListener l ){
        	mListener = l;
        	return this;
        }
  
        public Builder setColors(int[] c) {
        	colors = c;
        	return this;
        }
        public Builder setTitle(String t) {
        	title = t;
        	return this;
        }
        
    	@SuppressLint("NewApi")
		public AnimatedColorPickerDialog create() {   
            // instantiate the dialog with the custom Theme  
            final AnimatedColorPickerDialog dialog = new AnimatedColorPickerDialog(context,R.style.CustomTheme);  
            dialog.setContentView(R.layout.color_picker_dialog_layout);  
            final TableLayout colorTable= (TableLayout)dialog.findViewById(R.id.color_group); 
            colorTable.getViewTreeObserver().addOnGlobalLayoutListener(
                    new OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                        	colorTable.getViewTreeObserver()
                                    .removeGlobalOnLayoutListener(this); 
                        	int leftWidth = (colorTable.getWidth() - colorTable.getPaddingLeft() * 2) % (int)context.getResources().getDimension(R.dimen.color_circle_size);
                        	int tempColums = (colorTable.getWidth() - colorTable.getPaddingLeft() * 2) / (int)context.getResources().getDimension(R.dimen.color_circle_size);
                        	if(leftWidth < (tempColums - 1) * 1) {
                        		tempColums = tempColums -1;
                        	}
                        	dialog.setnumberOfColums(tempColums, colors);
                        }
            });
            
            dialog.setOnColorClickListener(mListener);
            TextView titleView = (TextView)dialog.findViewById(R.id.dialog_title);
            titleView.setText(title);
            View closebutton  = dialog.findViewById(R.id.close_button);
            closebutton.setOnClickListener(new View.OnClickListener(){
    			@Override
    			public void onClick(View view){
    				dialog.dismiss();
    			}
    		});
            
            dialog.setCanceledOnTouchOutside(true);
            return dialog;  
       }
    }
}