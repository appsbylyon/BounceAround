package com.appsbylyon.bouncearound.Views;

import java.util.ArrayList;

import com.appsbylyon.bouncearound.R;
import com.appsbylyon.bouncearound.Objects.Letter;
import com.appsbylyon.bouncearound.R.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Custom View for the Splash Screen
 * 
 * Modified: 7/7/2014
 * 
 * @author Adam Lyon
 *
 */
public class SplashView extends View 
{
	private static final int LETTER_WIDTH_DIVISOR = 12;
	
	private static final double LETTER_WIDTH_HEIGHT_RATIO = 1.7;
	
	private ArrayList<Letter> letters = new ArrayList<Letter>();
	private ArrayList<Bitmap> rawLetters = new ArrayList<Bitmap>();
	
	private Paint paint;
	
	private int frameCount;
	
	private Runnable task;
	
	/**
	 * Constructor for the SplashView class
	 * @param context
	 */
	public SplashView (Context context) 
	{
		super(context);
		paint = new Paint();
		paint.setStrokeWidth(1);
		rawLetters.add(BitmapFactory.decodeResource(getResources(), R.drawable.l1));
		rawLetters.add(BitmapFactory.decodeResource(getResources(), R.drawable.l2));
		rawLetters.add(BitmapFactory.decodeResource(getResources(), R.drawable.l3));
		rawLetters.add(BitmapFactory.decodeResource(getResources(), R.drawable.l4));
		rawLetters.add(BitmapFactory.decodeResource(getResources(), R.drawable.l5));
		rawLetters.add(BitmapFactory.decodeResource(getResources(), R.drawable.l6));
		rawLetters.add(BitmapFactory.decodeResource(getResources(), R.drawable.l7));
		rawLetters.add(BitmapFactory.decodeResource(getResources(), R.drawable.l8));
		rawLetters.add(BitmapFactory.decodeResource(getResources(), R.drawable.l9));
		rawLetters.add(BitmapFactory.decodeResource(getResources(), R.drawable.l10));
		rawLetters.add(BitmapFactory.decodeResource(getResources(), R.drawable.l11));
		rawLetters.add(BitmapFactory.decodeResource(getResources(), R.drawable.l12));
		task = new Runnable() 
		{
			@Override
			public void run() 
			{
				updatePositions();
			}
		};
	}
	
	/**
	 * Method that scales and positions the letters on the screen
	 * The sizes and positions are based on actual screen dimensions so
	 * the appearance will be consistent from device to device regardless
	 * of screen size and pixel density;
	 * 
	 * @param viewWidth Screen width in pixels
	 * @param viewHeight Screen height in pixels
	 */
	public void loadLetters(int viewWidth, int viewHeight) 
	{
		int bounceLetterWidth = viewWidth/LETTER_WIDTH_DIVISOR;
		int bounceLetterHeight = (int) ((double)bounceLetterWidth*LETTER_WIDTH_HEIGHT_RATIO);
		//The following line calculates the x position of the first letter
		//6 is the number of letters, 25 is the total amount of spacing between letters
		int bounceLetterStartXPos = (viewWidth-(6*bounceLetterWidth+25))/2;
		int bounceLetterYPos = viewHeight / 3;
		int aroundLetterYPos = (bounceLetterYPos * 2)-bounceLetterHeight;
		for (int i = 1; i < 7 ; i++) 
		{
			Bitmap currImage = (Bitmap) rawLetters.get(i-1);
			Bitmap scaledImage = Bitmap.createScaledBitmap(currImage, bounceLetterWidth, bounceLetterHeight, false);
			int xPos = bounceLetterStartXPos + ((i-1)*bounceLetterWidth) + ((i-1)*5);
			letters.add(new Letter(scaledImage, xPos, bounceLetterYPos));
		}
		for (int i = 7; i < 13 ; i++) 
		{
			Bitmap currImage = (Bitmap) rawLetters.get(i-1);
			Bitmap scaledImage = Bitmap.createScaledBitmap(currImage, bounceLetterWidth, bounceLetterHeight, false);
			int xPos = bounceLetterStartXPos + ((i-7)*bounceLetterWidth) + ((i-7)*5);
			letters.add(new Letter(scaledImage, xPos, aroundLetterYPos));
		}
	}// End of loadLetters method
	
	/**
	 * Method that updates the positions of the letters during the time
	 * that the splash screen is being displayed.
	 */
	synchronized public void updatePositions () 
	{
		for (int i = 0; i < letters.size(); i++) 
		{
			Letter tb = (Letter) letters.get(i);
			if (tb.getxLoc() < (1)) 
			{
				if (tb.getDirection() > 180) 
				{
					tb.setDirection((360 - (tb.getDirection()-180)));
				}else if (tb.getDirection() < 181) 
				{
					tb.setDirection(180- tb.getDirection());
				}
			} 
			else if(tb.getxLoc() > (this.getWidth()-tb.getImage().getWidth()-1)) 
			{
				if (tb.getDirection() < 90) 
				{
					tb.setDirection(180-tb.getDirection());
				}else if (tb.getDirection() > 270) 
				{
					tb.setDirection(180 + (360-tb.getDirection()));
				}
			}
			else if ((tb.getyLoc() < (1) && (tb.getDirection()>180) && (tb.getDirection()<360))) 
			{
				if (tb.getDirection() > 270) 
				{
					tb.setDirection(360-tb.getDirection());
				}else if (tb.getDirection() < 271) 
				{
					tb.setDirection(90+ (270-tb.getDirection()));
				}
			}
			else if ((tb.getyLoc() > (this.getHeight()-tb.getImage().getHeight())) && (tb.getDirection()<180) && (tb.getDirection()>0)) 
			{
				if (tb.getDirection() < 91) 
				{
					tb.setDirection(360-tb.getDirection());
				}else if (tb.getDirection() > 90) 
				{
					tb.setDirection(180+ (180-tb.getDirection()));
				}
			}
			int newY = (int) (Math.sin((tb.getDirection()*(Math.PI/180))) * tb.getSpeed());
			int newX = (int) (Math.cos((tb.getDirection()*(Math.PI/180))) * tb.getSpeed());
			tb.setxLoc(tb.getxLoc()+newX);
			tb.setyLoc(tb.getyLoc()+newY);
			letters.set(i, tb);
		}
	}// End of updatePositions method
	
	/**
	 * Method called then the view is invalidated.	
	 */
	@Override
	public void onDraw(Canvas canvas) 
	{
		if (frameCount < 51) 
		{
			paint.setColor(Color.BLACK);
		}
		else 
		{
			if (frameCount % 3 == 0) // The modulus is used here to determine how many frames should pass before the color is changed
			{
				paint.setColor((int)(Math.random()*Color.BLACK-1));
			}
		}
		canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
		frameCount++;
		if (frameCount > 50) 
		{
			for (int i = 0; i < 12; i++) 
			{
				if (frameCount == (51+(i*3))) // This allows the letters to start moving at different times
				{
					Letter currLetter = (Letter) letters.get(i);
					currLetter.setSpeed((int)(Math.random()*10)+10);
				}
			}
		}
		
		for (int i = 0; i < 12; i++) 
		{
			Letter currLetter = (Letter) letters.get(i);
			Bitmap tbmp = currLetter.getImage();
			canvas.drawBitmap(tbmp, currLetter.getxLoc(), currLetter.getyLoc(), paint);
		}
		if (letters.size() > 0) 
		{
			task.run();
		}
	}// end of onDraw method
}// End of SplashView class