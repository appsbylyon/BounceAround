package com.appsbylyon.bouncearound.Objects;

import android.graphics.Bitmap;

/**
 * Custom class to control the letters on the SplashScreen
 * 
 * Modified: 7/3/2014
 * 
 * @author Adam Lyon
 *
 */
public class Letter 
{
	private Bitmap image;
	private int speed;
	private int direction;
	private int xLoc;
	private int yLoc;
	private int rotationSpeed;
	
	public Letter(Bitmap image, int x, int y) 
	{
		this.image = image;
		this.direction = (int) ((Math.random()*360)+1);
		this.speed = 0;
		this.xLoc = x;
		this.yLoc = y;
		this.rotationSpeed = 0;
	}
	
	public Bitmap getImage() 
	{
		return image;
	}
	
	public void setImage(Bitmap image) 
	{
		this.image = image;
	}
	
	public int getSpeed() 
	{
		return speed;
	}
	
	public void setSpeed(int speed) 
	{
		this.speed = speed;
	}
	
	public int getDirection() 
	{
		return direction;
	}
	
	public void setDirection(int direction) 
	{
		this.direction = direction;
	}
	
	public int getxLoc() 
	{
		return xLoc;
	}
	public void setxLoc(int xLoc)
	{
		this.xLoc = xLoc;
	}
	
	public int getyLoc() 
	{
		return yLoc;
	}
	
	public void setyLoc(int yLoc) 
	{
		this.yLoc = yLoc;
	}

	public int getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(int rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}
}// End of Letter Class
