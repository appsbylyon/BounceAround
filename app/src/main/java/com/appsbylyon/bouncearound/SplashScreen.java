package com.appsbylyon.bouncearound;


import com.appsbylyon.bouncearound.Views.SplashView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;

/**
 * Class to control the Splash Screen
 * 
 * Modified: 7/7/2014
 * 
 * @author Adam Lyon
 *
 */
public class SplashScreen extends Activity implements OnGlobalLayoutListener
{
	private static final int FRAME_RATE = 20;
	
	private static final int SHOW_SPLASH_TIME = 4000;
	
	private SplashView view;
	
	Handler frame = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		view = new SplashView(this);
		this.setContentView(view);
		
		ViewTreeObserver vto = view.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(this);
		
		//This will show the splash screen for a duration, start the main activity and finish
		new Handler().postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				Intent mainActivity = new Intent(SplashScreen.this, BounceAroundMain.class);
				startActivity(mainActivity);
				finish();
			}
			
		}, SHOW_SPLASH_TIME);
	}// End of onCreate method
	
	/**
	 * This method is called when the view is about to be drawn and has a height
	 * and width value that can be used to correctly position the letters
	 */
	@Override
	public void onGlobalLayout() 
	{
		view.loadLetters(view.getWidth(), view.getHeight());
		frame.removeCallbacks(frameUpdate);
		frame.postDelayed(frameUpdate, FRAME_RATE);
	}// End of onGlobalLayout method
	
	/**
	 * Anonymous inner class to control update the view every 20ms (50fps)
	 */
	private Runnable frameUpdate = new Runnable() 
	{
		synchronized public void run() 
		{
			view.invalidate();
			frame.postDelayed(frameUpdate, FRAME_RATE);
		}
	};// End of frameUpdate class
}// End of SplashScreen class