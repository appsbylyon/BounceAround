package com.appsbylyon.bouncearound.Objects;

/**
 * Custom Class to control the balls in the main activity.
 * 
 * Modified: 6/26/2014
 * 
 * @author Adam Lyon
 *
 */
public class Ball 
{
	private int ballSize;
	private int ballColor;
	private int ballSpeed;
	private int ballDirection;
	private int xLoc;
	private int yLoc;
	
	public Ball (int ballSize, int ballColor, int direction, int speed, int startX, int startY) 
	{
		this.setBallSize(ballSize);
		this.setBallColor(ballColor);
		this.setBallDirection(direction);
		this.setBallSpeed(speed);
		this.setxLoc(startX);
		this.setyLoc(startY);
	}

	public int getBallSize() {
		return ballSize;
	}

	public void setBallSize(int ballSize) {
		this.ballSize = ballSize;
	}

	public int getBallColor() {
		return ballColor;
	}

	public void setBallColor(int ballColor) {
		this.ballColor = ballColor;
	}

	public int getBallSpeed() {
		return ballSpeed;
	}

	public void setBallSpeed(int ballSpeed) {
		this.ballSpeed = ballSpeed;
	}

	public int getBallDirection() {
		return ballDirection;
	}

	public void setBallDirection(int ballDirection) {
		this.ballDirection = ballDirection;
	}

	public int getxLoc() {
		return xLoc;
	}

	public void setxLoc(int xLoc) {
		this.xLoc = xLoc;
	}

	public int getyLoc() {
		return yLoc;
	}

	public void setyLoc(int yLoc) {
		this.yLoc = yLoc;
	}
}// End of Ball class