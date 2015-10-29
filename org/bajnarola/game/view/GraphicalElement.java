package org.bajnarola.game.view;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class GraphicalElement extends Image {
	String fname;
	String coords;
	HitBox hitbox;
	
	int globalCenterX, globalCenterY, size;
	int scaledX, scaledY, scaledSize;
	
	int direction;
	
	GameScene scene;
	
	public GraphicalElement(GameScene scene, String fname, String coordinates, int direction, int globalCenterX, int globalCenterY, int size) throws SlickException{
		super(fname);
		this.fname = fname;
		this.size = size;
		this.scene = scene;
		hitbox = new HitBox();
		setCoordinates(coordinates, globalCenterX, globalCenterY);
		this.direction = direction;
		this.rotate(direction*90);
	}
	
	public void rotate(boolean clockwise){
		this.rotate((clockwise ? 90 : 270));
		direction = (direction + (clockwise ? 1 : -1)) % 4;
	}
	
	public String getCoordinates(){
		return coords;
	}
	
	public void displace(int globalOffsetX, int globalOffsetY){
		globalCenterX += globalOffsetX;
		globalCenterY += globalOffsetY;
	}
	
	public void setCoordinates(String coordinates, int globalCenterX, int globalCenterY){
		this.coords = coordinates;
		this.globalCenterX = globalCenterX;
		this.globalCenterY = globalCenterY;
		hitbox.reset(globalCenterX-(size/2), globalCenterY-(size/2), globalCenterX+(size/2), globalCenterY+(size/2));
	}
	
	public void setCoordinates(HitBox newHb){
		hitbox = newHb;
		globalCenterX = hitbox.getCenterX();
		globalCenterY = hitbox.getCenterY();
	}
	
	public String getFileName(){
		return fname;
	}
	
	public boolean isClicked(int x, int y, int viewOffX, int viewOffY){
		return hitbox.hits(x, y, viewOffX, viewOffY);
	}
	
	//TODO: fix all this
	private void setScaledVals(float smallScaleFactor){
		scaledX = globalCenterX - (int)((size * smallScaleFactor) / 2);
		scaledY = globalCenterY - (int)((size * smallScaleFactor) / 2);
		scaledSize = (int)(size * smallScaleFactor);
	}
	
	// XXX: horrible fix
	public void drawAbsolute(){
		int x = hitbox.ulx;
		int y = hitbox.uly;
		switch(direction){
			case 2:
				y -= 12;
			case 1:
				x -= 12;
				break;
			case 3:
				y -= 12;
				break;
		}
		this.draw(x, y, size, size);
	}
	
	public void draw(boolean small, float scaleFactor, Color color){
		if(small){
			setScaledVals(scaleFactor);
			this.draw(scaledX-scene.xOff, scaledY-scene.yOff, scaledSize, scaledSize, color);
		}
		else
			this.draw(hitbox.ulx-scene.xOff, hitbox.uly-scene.yOff, size, size, color);
	}
	
	public void draw(boolean small, float scaleFactor){
		if(small){
			setScaledVals(scaleFactor);
			this.draw(scaledX-scene.xOff, scaledY-scene.yOff, scaledSize, scaledSize);
		}
		else
			this.draw(hitbox.ulx-scene.xOff, hitbox.uly-scene.yOff, size, size);
	}
	
	public void draw(boolean small, float scaleFactor, float animScaleFactor){
		if(small)
			setScaledVals(scaleFactor*animScaleFactor);
		else
			setScaledVals(animScaleFactor);
		this.draw(scaledX-scene.xOff, scaledY-scene.yOff, scaledSize, scaledSize);
	}
	
	public boolean isInView(int offX, int offY, int viewWidth, int viewHeight){
		if(hitbox.lrx > offX && hitbox.ulx < (offX + viewWidth) && hitbox.lry > offY && hitbox.uly < (offY + viewHeight))
			return true;
		return false;
	}
}
