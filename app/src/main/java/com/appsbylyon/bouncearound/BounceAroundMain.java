package com.appsbylyon.bouncearound;



import com.appsbylyon.bouncearound.Views.GameBoard;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Main Activity for the Bounce Around app
 * 
 * Modified: 7/7/2014
 * 
 * @author Adam Lyon
 *
 */
public class BounceAroundMain extends Activity implements SensorEventListener, OnGlobalLayoutListener
{
	private static final int FRAME_RATE = 20;
	
	private SharedPreferences sharedPrefs;
	
	private SensorManager sensorManager;
	
	private Sensor accSensor;
	
	private Handler frame = new Handler();
	
	private GameBoard game;
	
	private float x;
	private float y;
	private float z;
	
	private boolean initialized = false;
	
	private int boardWidth;
	private int boardHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		game = new GameBoard(this);
		setContentView(game);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
				
		ViewTreeObserver vto = game.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(this);
	}// End of onCreate method
	
	
	@Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, accSensor);
    }
	
	@Override
	public void onResume() 
	{
		super.onResume();
		sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String mode = sharedPrefs.getString(getString(R.string.pref_mode_key), "BOUNCE_MODE");
		String numOfBalls = sharedPrefs.getString(getString(R.string.pref_ball_num_key), "15");
		String bounceMinBallSize = sharedPrefs.getString(getString(R.string.pref_bounce_ball_min_size_key), "5");
		String bounceBallSizeRange = sharedPrefs.getString(getString(R.string.pref_bounce_size_range_key), "10");
		String bounceMinSpeed = sharedPrefs.getString(getString(R.string.pref_bounce_speed_min_key), "5");
		String bounceSpeedRange = sharedPrefs.getString(getString(R.string.pref_bounce_speed_range_key), "1");
		String bounceBallColor = sharedPrefs.getString(getString(R.string.pref_bounce_ball_color_key), "random");
		String bounceBackgroundColor = sharedPrefs.getString(getString(R.string.pref_bounce_background_color_key), "random");
		boolean tiltEnforceFriction = sharedPrefs.getBoolean(getString(R.string.pref_tilt_friction_key), false);
		String frictionStrength = sharedPrefs.getString(getString(R.string.pref_tilt_friction_strength_key), "10");
		String tiltBallColor = sharedPrefs.getString(getString(R.string.pref_tilt_ball_color_key), "random");
		String tiltBackgroundColor = sharedPrefs.getString(getString(R.string.pref_tilt_background_color_key), "random");
		String tiltBallSize = sharedPrefs.getString(getString(R.string.pref_tilt_ball_size_key), "15");
		game.setPrefs(mode, numOfBalls, bounceMinBallSize, bounceBallSizeRange, 
				bounceMinSpeed, bounceSpeedRange, bounceBallColor, bounceBackgroundColor,
				tiltEnforceFriction, frictionStrength, tiltBallColor, tiltBackgroundColor, tiltBallSize);
		
		if (initialized) 
		{
			initGfx();
		}
	}// End of onResume method
	
	/**
	 * Method to create the balls and set the handler up for frame updates
	 */
	synchronized public void initGfx() 
	{
		frame.removeCallbacks(frameUpdate);
		frame.postDelayed(frameUpdate, FRAME_RATE);
		boardWidth = game.getWidth();
		boardHeight = game.getHeight();
		game.createBalls(boardWidth, boardHeight);
	}// end of initGfx method
	
	/**
	 * Anonymous inner class to update the screen every 20ms (50fps)
	 */
	private Runnable frameUpdate = new Runnable() 
	{
		synchronized public void run() 
		{
			game.invalidate();
			frame.postDelayed(frameUpdate, FRAME_RATE);
		}
	};// End of frameUpdate Class
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.bounce_around_main, menu);
		return true;
	}// end of onCreateOptionsMenu method

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent prefIntent = new Intent(getApplicationContext(), PrefActivity.class);
			startActivity(prefIntent);
			return true;
		} 
		else if (id == R.id.about_screen) 
		{
			Intent aboutIntent = new Intent(getApplicationContext(), About.class);
			startActivity(aboutIntent);
		}
		return super.onOptionsItemSelected(item);
	}// end of onOptionsItemSelected method
	
	/**
	 * Method called when the device tilt position has changed
	 */
	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		float N[] = new float[9];
        float orientation[] = new float[3];
        SensorManager.getOrientation(N, orientation);
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];
        game.updateOrientation(x, y, z);
    }// end of onSensorChanged
	
	// Unused method that was inherited from SensorEventListener interface
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {}

	/**
	 * Method called when view has dimensions and is ready to be drawn
	 */
	@Override
	public void onGlobalLayout() {
		initialized = true;
		initGfx();
	}
}// End of BounceAroundMain class