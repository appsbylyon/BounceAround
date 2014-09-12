package com.appsbylyon.bouncearound;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Class for controlling the "About" screen.
 * 
 * Modified: 7/7/2014
 * 
 * @author Adam Lyon 
 *
 */
public class About extends Activity 
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.about_layout);
	}
}