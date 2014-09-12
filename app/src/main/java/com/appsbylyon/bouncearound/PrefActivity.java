package com.appsbylyon.bouncearound;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

/**
 * Class to control the PrefFrag activity.
 * 
 * Modified: 7/7/2014
 * 
 * @author Adam Lyon
 *
 */
public class PrefActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		PrefFrag prefFragment = new PrefFrag();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(android.R.id.content, prefFragment);
		fragmentTransaction.commit();
	}
}// End of PrefActivity class