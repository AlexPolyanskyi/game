package com.example.test;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;


public abstract class Points extends Sprite {
	private int pointsCount;
	public Points(float pX, float pY, VertexBufferObjectManager vbo,String pointsType, ITextureRegion image,int count) {
		super(pX, pY, image, vbo);
		pointsCount=count;
	}
		
		
		
	public int getPointsCount() {
		return pointsCount;
	}
}
