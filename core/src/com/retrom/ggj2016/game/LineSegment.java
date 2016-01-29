package com.retrom.ggj2016.game;

/**
 * Created by Asaf on 29/01/2016.
 */
public class LineSegment {
	
	public LineSegment() {};
	
	public LineSegment(float startX, float startY, float endX, float endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}
    public float startX;
    public float startY;
    public float endX;
    public float endY;
}
