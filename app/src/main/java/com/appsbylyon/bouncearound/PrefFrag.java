package com.appsbylyon.bouncearound;

import java.util.HashMap;
import java.util.Map;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Class to control the Preferences
 * 
 * Modified: 7/7/2014
 * 
 * @author Adam Lyon
 *
 */
public class PrefFrag extends PreferenceFragment implements OnPreferenceChangeListener 
{
	private ListPreference opMode;
	private ListPreference bounceBallColor;
	private ListPreference bounceBackgroundColor;
	private ListPreference tiltBallColor;
	private ListPreference tiltBackgroundColor;
	
	private EditTextPreference numOfBalls;
	private EditTextPreference bounceMinBallSize;
	private EditTextPreference bounceBallSizeRange;
	private EditTextPreference bounceMinSpeed;
	private EditTextPreference bounceSpeedRange;
	private EditTextPreference tiltFrictionStrength;
	private EditTextPreference tiltBallSize;
	
	private SharedPreferences sharedPrefs;
	
	private Map<String, String> colors;
	
	/**
	 * Required Empty constructor for fragments.
	 */
	public PrefFrag() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        colors  = new HashMap<String, String>();
        colors.put("-16777216", "Black");
        colors.put("-16776961", "Blue");
        colors.put("-16711681", "Cyan");
        colors.put("-7829368", "Gray");
        colors.put("-16711936", "Green");
        colors.put("-65281", "Magenta");
        colors.put("-65536", "Red");
        colors.put("-1", "White");
        colors.put("-256", "Yellow");
        colors.put("random", "Random");
        colors.put("random_changing", "Random Changing");
        
        addPreferencesFromResource(R.xml.preference_layout);
        
        opMode = (ListPreference) this.findPreference(getString(R.string.pref_mode_key));
        opMode.setOnPreferenceChangeListener(this);
        
        bounceBallColor = (ListPreference) this.findPreference(getString(R.string.pref_bounce_ball_color_key));
        bounceBallColor.setOnPreferenceChangeListener(this);
        
        bounceBackgroundColor = (ListPreference) this.findPreference(getString(R.string.pref_bounce_background_color_key));
        bounceBackgroundColor.setOnPreferenceChangeListener(this);
        
        tiltBallColor = (ListPreference) this.findPreference(getString(R.string.pref_tilt_ball_color_key));
        tiltBallColor.setOnPreferenceChangeListener(this);
        
        tiltBackgroundColor = (ListPreference) this.findPreference(getString(R.string.pref_tilt_background_color_key));
        tiltBackgroundColor.setOnPreferenceChangeListener(this);
        
        tiltBallSize = (EditTextPreference) this.findPreference(getString(R.string.pref_tilt_ball_size_key));
        tiltBallSize.setOnPreferenceChangeListener(this);

        numOfBalls = (EditTextPreference) this.findPreference(getString(R.string.pref_ball_num_key));
        numOfBalls.setOnPreferenceChangeListener(this);
        
        bounceMinBallSize = (EditTextPreference) this.findPreference(getString(R.string.pref_bounce_ball_min_size_key));
        bounceMinBallSize.setOnPreferenceChangeListener(this);
        
        bounceBallSizeRange = (EditTextPreference) this.findPreference(getString(R.string.pref_bounce_size_range_key));
        bounceBallSizeRange.setOnPreferenceChangeListener(this);
        
        bounceMinSpeed = (EditTextPreference) this.findPreference(getString(R.string.pref_bounce_speed_min_key));
        bounceMinSpeed.setOnPreferenceChangeListener(this);
        
        bounceSpeedRange = (EditTextPreference) this.findPreference(getString(R.string.pref_bounce_speed_range_key));
        bounceSpeedRange.setOnPreferenceChangeListener(this);

        tiltFrictionStrength = (EditTextPreference) this.findPreference(getString(R.string.pref_tilt_friction_strength_key));
        tiltFrictionStrength.setOnPreferenceChangeListener(this);
	}//End of onCreate method
	
	@Override
	public void onResume() 
	{
		super.onResume();
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String opModeValue = sharedPrefs.getString(getString(R.string.pref_mode_key), "BOUNCE_MODE");
		setOpModeSummary(opModeValue);
		
		String numOfBallsValue = sharedPrefs.getString(getString(R.string.pref_ball_num_key), "15");
		numOfBalls.setSummary(numOfBallsValue);
		
		String bounceBallMinSize = sharedPrefs.getString(getString(R.string.pref_bounce_ball_min_size_key), "5");
		bounceMinBallSize.setSummary(bounceBallMinSize);
		
		String bounceBallSizeRangeValue = sharedPrefs.getString(getString(R.string.pref_bounce_size_range_key), "10");
		bounceBallSizeRange.setSummary(bounceBallSizeRangeValue);
		
		String bounceSpeedMin = sharedPrefs.getString(getString(R.string.pref_bounce_speed_min_key), "5");
		bounceMinSpeed.setSummary(bounceSpeedMin);
		
		String bounceSpeedRangeValue = sharedPrefs.getString(getString(R.string.pref_bounce_speed_range_key), "1");
		bounceSpeedRange.setSummary(bounceSpeedRangeValue);
		
		String bounceBallColorValue = sharedPrefs.getString(getString(R.string.pref_bounce_ball_color_key), "random");
		bounceBallColor.setSummary(colors.get(bounceBallColorValue));
		
		String bounceBackgroundColorValue = sharedPrefs.getString(getString(R.string.pref_bounce_background_color_key), "random");
		bounceBackgroundColor.setSummary(colors.get(bounceBackgroundColorValue));
		
		String frictionStrength = sharedPrefs.getString(getString(R.string.pref_tilt_friction_strength_key), "10");
		tiltFrictionStrength.setSummary(frictionStrength);
		
		String tiltBallColorValue = sharedPrefs.getString(getString(R.string.pref_tilt_ball_color_key), "random");
		tiltBallColor.setSummary(colors.get(tiltBallColorValue));
		
		String tiltBackgroundColorValue = sharedPrefs.getString(getString(R.string.pref_tilt_background_color_key), "random");
		tiltBackgroundColor.setSummary(colors.get(tiltBackgroundColorValue));
		
		String tiltBallSizeValue = sharedPrefs.getString(getString(R.string.pref_tilt_ball_size_key), "15");
		tiltBallSize.setSummary(tiltBallSizeValue);
	}// End of onResume method
	
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference.equals(opMode)) 
		{
			setOpModeSummary(newValue.toString());
			return true;
		}
		if (preference.equals(numOfBalls)) 
		{
			int intNumOfBalls = Integer.parseInt(newValue.toString());
			if ((intNumOfBalls < 1) || (intNumOfBalls > 500)) 
			{
				Toast.makeText(getActivity(), "Number of balls must be between 1 & 500", Toast.LENGTH_LONG).show();
				return false;
			}
			numOfBalls.setSummary(newValue.toString());
			return true;
		}
		if (preference.equals(bounceMinBallSize)) 
		{
			int ballMinSize = Integer.parseInt(newValue.toString());
			if ((ballMinSize < 5) || (ballMinSize > 25)) 
			{
				Toast.makeText(getActivity(), "Minimum size must be between 5 & 25", Toast.LENGTH_LONG).show();
				return false;
			}
			bounceMinBallSize.setSummary(newValue.toString());
			return true;
		}
		if (preference.equals(bounceBallSizeRange)) 
		{
			int ballSizeRange = Integer.parseInt(newValue.toString());
			if ((ballSizeRange < 0) || (ballSizeRange > 50)) 
			{
				Toast.makeText(getActivity(), "Ball Size Range Must Be Between 0 & 50", Toast.LENGTH_LONG).show();
				return false;
			}
			bounceBallSizeRange.setSummary(newValue.toString());
			return true;
		}
		if (preference.equals(bounceMinSpeed)) 
		{
			int ballMinSpeed = Integer.parseInt(newValue.toString());
			if ((ballMinSpeed < 5) || (ballMinSpeed > 20)) 
			{
				Toast.makeText(getActivity(), "Ball Minimum Speed Must Be Between 5 & 20", Toast.LENGTH_LONG).show();
				return false;
			}
			bounceMinSpeed.setSummary(newValue.toString());
			return true;
		}
		if (preference.equals(bounceSpeedRange)) 
		{
			int ballSpeedRange = Integer.parseInt(newValue.toString());
			if ((ballSpeedRange < 0) || (ballSpeedRange > 30)) 
			{
				Toast.makeText(getActivity(), "Speed Range Must Be Between 0 & 30", Toast.LENGTH_LONG).show();
				return false;
			}
			bounceSpeedRange.setSummary(newValue.toString());
			return true;
		}
		if (preference.equals(bounceBallColor)) 
		{
			bounceBallColor.setSummary(colors.get(newValue.toString()));
			return true;
		}
		if (preference.equals(bounceBackgroundColor)) 
		{
			bounceBackgroundColor.setSummary(colors.get(newValue.toString()));
			return true;
		}
		if (preference.equals(tiltFrictionStrength)) 
		{
			int newFrictionStrength = Integer.parseInt(newValue.toString());
			if (newFrictionStrength < 1 || newFrictionStrength > 10) 
			{
				Toast.makeText(getActivity(), "Friction Strength Must Be Between 1 & 10", Toast.LENGTH_LONG).show();
				return false;
			}
			tiltFrictionStrength.setSummary(newValue.toString());
			return true;
		}
		if (preference.equals(tiltBackgroundColor)) 
		{
			tiltBackgroundColor.setSummary(colors.get(newValue.toString()));
			return true;
		}
		if (preference.equals(tiltBallColor)) 
		{
			tiltBallColor.setSummary(colors.get(newValue.toString()));
			return true;
		}
		if (preference.equals(tiltBallSize)) 
		{
			int newTiltBallSize = Integer.parseInt(newValue.toString());
			if ((newTiltBallSize < 5) || (newTiltBallSize > 50)) 
			{
				Toast.makeText(getActivity(), "Ball Size Must Be Between 5 and 50.", Toast.LENGTH_LONG).show();
				return false;
			}
			tiltBallSize.setSummary(newValue.toString());
			return true;
		}
		return false;
	}// End of onPreferenceChange method
	
	/**
	 * Method to set the summary of the operation Mode.
	 * 
	 * @param keyValue Value saved in the xml file
	 */
	private void setOpModeSummary(String keyValue) 
	{
		if (keyValue.equals("BOUNCE_MODE")) 
		{
			opMode.setSummary("Bounce Mode");
		}
		else if (keyValue.equals("TILT_MODE")) 
		{
			opMode.setSummary("Tilt Mode");
		}
	}// End of setOpModeSummary method
}// End of PrefFrag Class