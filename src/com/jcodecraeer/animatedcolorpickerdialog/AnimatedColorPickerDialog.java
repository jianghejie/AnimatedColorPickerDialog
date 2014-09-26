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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
public class AnimatedColorPickerDialog extends Dialog  {
    Context context;
    public static final int DIALOG_TYPE_MESSAGE = 0;
    public static final int DIALOG_TYPE_PROGRESS = 2;
    public static final int DIALOG_TYPE_ALERT = 3;

    public AnimatedColorPickerDialog(Context context) {
        this(context,R.style.CustomTheme);
    }
    public AnimatedColorPickerDialog(Context context, int theme){
        super(context, theme);
        this.context = context; 
    }
    
    public interface ColorClickListener {
        void onThemeClick(int color);
    }
    public static class Builder {
        private Context context;  
        private ColorClickListener mListener;
        private int[] colors;
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
        /** 
         * 为数字textview添加点击事件
         * @param view 
         * @return 
         */  
        private void setOnclickForViews(ViewGroup view, final AnimatedColorPickerDialog dialog){
            for(int i = 0; i <view.getChildCount(); i++) {
                final View v = view.getChildAt(i);
                if(v instanceof View ) {
                       ((View)v).setOnClickListener(new View.OnClickListener(){
    			           @Override
    			           public void onClick(View view){
    			        	   if(mListener != null){
    			        		   Integer colorHexInObject = (Integer)v.getTag();
    			        		   mListener.onThemeClick(colorHexInObject.intValue()); 
    			        		   dialog.dismiss();
    			        	   }  
    			           }
    		    });
                }else if(v instanceof ViewGroup) {
                	setOnclickForViews((ViewGroup)v, dialog);
                }
            }       	
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
		public AnimatedColorPickerDialog create() {   
            // instantiate the dialog with the custom Theme  
            final AnimatedColorPickerDialog dialog = new AnimatedColorPickerDialog(context,R.style.CustomTheme);  
            dialog.setContentView(R.layout.color_picker_dialog_layout);  
            ViewGroup colorContainer= (ViewGroup)dialog.findViewById(R.id.color_group);

            for(int i=0; i<colors.length; i++) {
            	View item = new View(context);
    	        LayoutParams params = new LayoutParams((int)context.getResources().getDimension(R.dimen.color_circle_size), (int)context.getResources().getDimension(R.dimen.color_circle_size));  
            	item.setLayoutParams(params);
            	
            	ShapeDrawable   drawableNormal = new ShapeDrawable (new OvalShape ());
            	drawableNormal.getPaint().setColor(colors[i]);
            	ShapeDrawable   drawablePress = new ShapeDrawable (new OvalShape ());
            	drawablePress.getPaint().setColor(getDarkerColor(colors[i]));
       
            	ShapeDrawable   drawableFocus = new ShapeDrawable (new OvalShape ());
            	drawableFocus.getPaint().setColor(getDarkerColor(colors[i]));
            	
            	item.setBackground(getStateDrawable(drawableNormal, drawablePress, drawableFocus));
            	item.setTag(Integer.valueOf(colors[i]));
            	colorContainer.addView(item);
            }
            setOnclickForViews(colorContainer, dialog);
            View closebutton  = dialog.findViewById(R.id.close_button);
            closebutton.setOnClickListener(new View.OnClickListener(){
    			@Override
    			public void onClick(View view){
    				dialog.dismiss();
    			}
    		});
            
            //视差效果动画效果
            TextView dialogTitleText =  (TextView)dialog.findViewById(R.id.dialog_title);
            dialogTitleText.setTranslationX(-600);
    		final AnimatorSet localAnimatorSet = new AnimatorSet();
    		ObjectAnimator localObjectAnimator1 = ObjectAnimator.ofFloat(dialogTitleText, "translationX", -600,0);
    		localObjectAnimator1.setDuration(800);
    	    localObjectAnimator1.setInterpolator(new OvershootInterpolator(1.2F));	 
    	 
    	    localAnimatorSet.play(localObjectAnimator1);
    	    colorContainer.setTranslationX(600);
    	    ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(colorContainer, "translationX", 600,0);
    	    localObjectAnimator2.setDuration(800);
    	    localObjectAnimator2.setInterpolator(new OvershootInterpolator(1.2F));
    	    localAnimatorSet.play(localObjectAnimator2).after(100);
    	    localAnimatorSet.start();
            
            dialog.setCanceledOnTouchOutside(true);
            return dialog;  
       }
    }
}