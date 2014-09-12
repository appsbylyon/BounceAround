package com.appsbylyon.bouncearound.Views;

import java.util.ArrayList;

import com.appsbylyon.bouncearound.Objects.Ball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/**
 * Gameboard for the game
 * 
 * Modified: 7/7/2014
 * 
 * @author infinite
 *
 */
public class GameBoard extends View
{
	private static final int NUMBER_OF_BALLS = 300;
	private static final int MAXIMUM_BALL_ANGLE = 360;
	private static final int TILT_MAX_BALL_SPEED = 35;
	private static final int MAX_FRAME_COUNT = 5;
	private static final int MAX_BACKGROUND_FRAME_COUNT = 10;
	private static final int MAX_FRICTION_STRENGTH = 10;
	private static final int BOUNCE_MODE = 0;
	private static final int TILT_MODE = 1;
	
	private Paint paint;
	
	private ArrayList<Ball> allBalls = new ArrayList<Ball>();
	
	private Runnable updateThread;
		
	private float x = 0;
	private float y = 0;
	private float z = 0;
	private float tiltXSum = 0;
	private float tiltYSum = 0;
	private float friction = 0;
	
	private int currMode = TILT_MODE;
	private int frameCount = 0;
	private int backgroundFrameCount = 0;
	private int numOfBalls = NUMBER_OF_BALLS;
	private int bounceMinBallSize = 5;
	private int bounceBallSizeRange = 10;
	private int bounceBallMinSpeed = 5;
	private int bounceSpeedRange = 1;
	private int bounceBallColor = 0;
	private int bounceBackgroundColor = 0;
	private int frictionStrength = 10;
	private int tiltBallSize = 15;
	private int tiltBallColor = -1;
	private int tiltBackgroundColor = -1;
	
	private boolean bounceRandomColors = false;
	private boolean bounceRandomIntialColor = false;
	private boolean bounceBackgroundRandomColor = false;
	private boolean tiltEnforceFriction = false;
	private boolean tiltRandomColors = false;
	private boolean tiltRandomInitialColor = false;
	private boolean tiltBackgroundRandomColor = false;
	
	public GameBoard(Context context) 
	{
		super(context);
		paint = new Paint();
		paint.setAlpha(255);
		paint.setStrokeWidth(1);
		updateThread = new Runnable() 
		{
			@Override
			public void run() 
			{
				frameCount ++;
				GameBoard.this.updateBallsBounce();
				if (frameCount == MAX_FRAME_COUNT) {frameCount = 0;}
			}
		};
	}// end of GameBoard constructor
	
	/**
	 * Method that sets all of the saved options from the sharedPrefs
	 * 
	 * @param mode
	 * @param numOfBalls
	 * @param bounceMinBallSize
	 * @param bounceBallSizeRange
	 * @param bounceMinSpeed
	 * @param bounceSpeedRange
	 * @param bounceBallColor
	 * @param bounceBackgroundColor
	 * @param tiltEnforceFriction
	 * @param frictionStrength
	 * @param tiltBallColor
	 * @param tiltBackgroundColor
	 * @param tiltBallSize
	 */
	public void setPrefs(String mode, String numOfBalls, String bounceMinBallSize, String bounceBallSizeRange,
			String bounceMinSpeed, String bounceSpeedRange, String bounceBallColor, String bounceBackgroundColor,
			boolean tiltEnforceFriction, String frictionStrength, String tiltBallColor, 
			String tiltBackgroundColor, String tiltBallSize) 
	{
		if (mode.equals("BOUNCE_MODE")) 
		{
			currMode = BOUNCE_MODE;
		}
		else if (mode.equals("TILT_MODE")) 
		{
			currMode = TILT_MODE;
		}
		
		this.numOfBalls = Integer.parseInt(numOfBalls);
		this.bounceMinBallSize = Integer.parseInt(bounceMinBallSize);
		this.bounceBallSizeRange = Integer.parseInt(bounceBallSizeRange);
		this.bounceBallMinSpeed = Integer.parseInt(bounceMinSpeed);
		this.bounceSpeedRange = Integer.parseInt(bounceSpeedRange);
		this.tiltEnforceFriction = tiltEnforceFriction;
		this.frictionStrength = Integer.parseInt(frictionStrength);
		this.tiltBallSize = Integer.parseInt(tiltBallSize);
		setBounceBallColor(bounceBallColor);
		setBounceBackgroundColor(bounceBackgroundColor);
		setTiltBallColor(tiltBallColor);
		setTiltBackgroundColor(tiltBackgroundColor);
	}// end of setPrefs method
	
	/**
	 * Method the populates the screen with balls
	 * 
	 * @param boardWidth width of the view
	 * @param boardHeight height of the view
	 */
	synchronized public void createBalls(int boardWidth, int boardHeight) 
	{
		int yPos = ((boardHeight / 2));
		int xPos = boardWidth / 2;
		
		if (allBalls.size() > 0) 
		{
			allBalls.clear();
		}
		
		switch (currMode) 
		{
		case BOUNCE_MODE:
			for (int i = 0; i < numOfBalls; i++) 
			{
				int newColor = bounceBallColor;
				if (bounceRandomColors || bounceRandomIntialColor) 
				{
					newColor = (int) ((Math.random()*Color.BLACK)-1);
				}
				int newSize = (int) (((Math.random()*bounceBallSizeRange)+bounceMinBallSize)+1);
				int newSpeed = (int) (((Math.random()*bounceSpeedRange)+bounceBallMinSpeed)+1);
				int newAngle = (int) ((Math.random()*MAXIMUM_BALL_ANGLE)+1);
				allBalls.add(new Ball(newSize, newColor, newAngle, newSpeed, xPos, yPos));
			}
			break;
			
		case TILT_MODE:
			for (int i = 0; i < 1; i++) 
			{
				int newColor = tiltBallColor;
				if (tiltRandomColors || tiltRandomInitialColor) 
				{
					newColor = (int) ((Math.random()*Color.BLACK)-1);
				}
				int newSize = tiltBallSize;
				int newSpeed = 0;
				int newAngle = (int) ((Math.random()*MAXIMUM_BALL_ANGLE)+1);
				allBalls.add(new Ball(newSize, newColor, newAngle, newSpeed, xPos, yPos));
			}
			break;
		}// end of switch (currMode)
	}// End of createBalls method
	
	/**
	 * Method that is called by main activity to update the device position values
	 * @param x
	 * @param y
	 * @param z
	 */
	synchronized public void updateOrientation(float x, float y, float z) 
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}//end of updateOrientation method
	
	/**
	 * Method to update the ball positions in BOUNCE mode
	 */
	synchronized public void updateBallsBounce() 
	{
		for (int i = 0; i < allBalls.size(); i++) 
		{
			Ball tb = (Ball) allBalls.get(i);
			if (tb.getxLoc() < (tb.getBallSize()+1)) 
			{
				if (tb.getBallDirection() > 180) 
				{
					tb.setBallDirection(360 - (tb.getBallDirection()-180));
				}else if (tb.getBallDirection() < 181) 
				{
					tb.setBallDirection(180- tb.getBallDirection());
				}
			} 
			else if(tb.getxLoc() > (this.getWidth()-tb.getBallSize()-1)) 
			{
				if (tb.getBallDirection() < 90) 
				{
					tb.setBallDirection(180-tb.getBallDirection());
				}else if (tb.getBallDirection() > 270) 
				{
					tb.setBallDirection(180 + (360-tb.getBallDirection()));
				}
			}
			else if ((tb.getyLoc() < (tb.getBallSize()) && (tb.getBallDirection()>180) && (tb.getBallDirection()<360))) 
			{
				if (tb.getBallDirection() > 270) 
				{
					tb.setBallDirection(360-tb.getBallDirection());
				}else if (tb.getBallDirection() < 271) 
				{
					tb.setBallDirection(90+ (270-tb.getBallDirection()));
				}
			}
			else if ((tb.getyLoc() > (this.getHeight()-tb.getBallSize())) && (tb.getBallDirection()<180) && (tb.getBallDirection()>0)) 
			{
				if (tb.getBallDirection() < 91) 
				{
					tb.setBallDirection(360-tb.getBallDirection());
				}else if (tb.getBallDirection() > 90) 
				{
					tb.setBallDirection(180+ (180-tb.getBallDirection()));
				}
			}
			
			int newY = (int) (Math.sin((tb.getBallDirection()*(Math.PI/180))) * tb.getBallSpeed());
			int newX = (int) (Math.cos((tb.getBallDirection()*(Math.PI/180))) * tb.getBallSpeed());
			tb.setxLoc(tb.getxLoc()+newX);
			tb.setyLoc(tb.getyLoc()+newY);
			
			if (bounceRandomColors) 
			{
				if (frameCount == MAX_FRAME_COUNT) 
				{
					tb.setBallColor((int)(Math.random()*Color.BLACK-1));
				}
			}
			allBalls.set(i, tb);
		}
	}//End of updateBallsBounce method
	
	/**
	 * Method to update the ball position in TILT mode
	 */
	synchronized public void updateBallsTilt() 
	{
		for (int i = 0; i < allBalls.size(); i++) 
		{
			Ball tb = allBalls.get(i);
			
			tiltXSum += (0-(x/10));
			tiltYSum += (y/10);
			
			if (tiltXSum > TILT_MAX_BALL_SPEED) {tiltXSum = TILT_MAX_BALL_SPEED;}
			if (tiltXSum < -TILT_MAX_BALL_SPEED) {tiltXSum = -TILT_MAX_BALL_SPEED;}
			if (tiltYSum > TILT_MAX_BALL_SPEED) {tiltYSum = TILT_MAX_BALL_SPEED;}
			if (tiltYSum < -TILT_MAX_BALL_SPEED) {tiltYSum = -TILT_MAX_BALL_SPEED;}
			
			if (tb.getxLoc() < (tb.getBallSize()+1)||(tb.getxLoc() > (this.getWidth()-tb.getBallSize()-1))) 
			{
				tiltXSum = -1*tiltXSum;
				
			} 
			else if (tb.getyLoc() < (tb.getBallSize()+1)||(tb.getyLoc() > (this.getHeight()-tb.getBallSize()-1))) 
			{
				tiltYSum = -1*tiltYSum;
			}
			
			if (tiltEnforceFriction) 
			{
				friction = (float)(.94 + (.05-(((float)frictionStrength/(float)MAX_FRICTION_STRENGTH)/20)));
				tiltXSum = friction*tiltXSum;
				tiltYSum = friction*tiltYSum;
			}
			
			tb.setxLoc(tb.getxLoc()+(int)tiltXSum);
			tb.setyLoc(tb.getyLoc()+(int)tiltYSum);
			
			if (tiltRandomColors) 
			{
				if (frameCount == MAX_FRAME_COUNT) 
				{
					tb.setBallColor((int)(Math.random()*Color.BLACK-1));
					
				}
			}
		}
	}// End of updateBallsTilt method
	
	/**
	 * Method called when view is invalidated
	 */
	synchronized public void onDraw(Canvas canvas) 
	{
		if (allBalls.size()>0) 
		{
			switch (currMode) 
			{
			case BOUNCE_MODE:
				if (bounceBackgroundRandomColor) 
				{
					backgroundFrameCount++;
					if (backgroundFrameCount == MAX_BACKGROUND_FRAME_COUNT) 
					{
						bounceBackgroundColor =(int) (Math.random() * Color.BLACK - 1);
						backgroundFrameCount = 0;
					}
				}
				paint.setColor(bounceBackgroundColor);
				canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
				for (int i = 0; i < allBalls.size(); i++) 
				{
					Ball tb = (Ball) allBalls.get(i);
					paint.setColor(tb.getBallColor());
					canvas.drawCircle(tb.getxLoc(), tb.getyLoc(), tb.getBallSize(), paint);
				}
				updateThread.run();
				break;
				
			case TILT_MODE:
				if (tiltBackgroundRandomColor) 
				{
					backgroundFrameCount++;
					if (backgroundFrameCount == MAX_BACKGROUND_FRAME_COUNT) 
					{
						tiltBackgroundColor =(int) (Math.random() * Color.BLACK - 1);
						backgroundFrameCount = 0;
					}
				}
				paint.setColor(tiltBackgroundColor);
				canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);
				frameCount++;
				updateBallsTilt();
				if (frameCount == MAX_FRAME_COUNT) {frameCount = 0;}
				for (int i = 0; i < allBalls.size(); i++) 
				{
					Ball tb = (Ball) allBalls.get(i);
					paint.setColor(tb.getBallColor());
					canvas.drawCircle(tb.getxLoc(), tb.getyLoc(), tb.getBallSize(), paint);
				}
				break;
			}// end of switch (currMode)
		}// end of if (allBalls.size()>0)
	}// End of onDraw method
	
	/**
	 * Method to set options for the ball color in BOUNCE mode.
	 * 
	 * @param value Value obtained from sharedPrefs
	 */
	private void setBounceBallColor(String value) 
	{
		if (value.equals("random")) 
		{
			bounceRandomIntialColor = true;
			bounceRandomColors = false;
		}
		else if(value.equals("random_changing")) 
		{
			bounceRandomColors = true;
			bounceRandomIntialColor = false;
		}
		else 
		{
			this.bounceBallColor = Integer.parseInt(value);
			bounceRandomColors = false;
			bounceRandomIntialColor = false;
		}
	}// end of setBounceBallColor method
	
	/**
	 * Method to set options for the background color in BOUNCE mode.
	 * 
	 * @param value Value obtained from sharedPrefs
	 */
	private void setBounceBackgroundColor(String value) 
	{
		if (value.equals("random")) 
		{
			bounceBackgroundRandomColor = false;
			this.bounceBackgroundColor = (int)( Math.random() * Color.BLACK -1);
		}
		else if(value.equals("random_changing")) 
		{
			bounceBackgroundRandomColor = true;
			this.bounceBackgroundColor = (int)( Math.random() * Color.BLACK -1);
		}
		else 
		{
			this.bounceBackgroundColor = Integer.parseInt(value);
			bounceBackgroundRandomColor = false;
		}
	}// end of setBounceBackgroundColor method
	
	/**
	 * Method to set options for the ball color in TILT mode.
	 * 
	 * @param value Value obtained from sharedPrefs
	 */
	private void setTiltBallColor(String value) 
	{
		if (value.equals("random")) 
		{
			tiltRandomInitialColor = true;
			tiltRandomColors = false;
		}
		else if(value.equals("random_changing")) 
		{
			tiltRandomColors = true;
			tiltRandomInitialColor = false;
		}
		else 
		{
			this.tiltBallColor = Integer.parseInt(value);
			tiltRandomColors = false;
			tiltRandomInitialColor = false;
		}
	}// end of setTiltBallColor method
	
	/**
	 * Method to set options for the background color in TILT mode
	 * 
	 * @param value Value obtained from sharedPrefs
	 */
	private void setTiltBackgroundColor(String value) 
	{
		if (value.equals("random")) 
		{
			tiltBackgroundRandomColor = false;
			this.tiltBackgroundColor = (int)( Math.random() * Color.BLACK -1);
		}
		else if(value.equals("random_changing")) 
		{
			tiltBackgroundRandomColor = true;
			this.tiltBackgroundColor = (int)( Math.random() * Color.BLACK -1);
		}
		else 
		{
			this.tiltBackgroundColor = Integer.parseInt(value);
			tiltBackgroundRandomColor = false;
		}
	}// end of setTiltBackgroundColor method
} // End of GameBoard Class