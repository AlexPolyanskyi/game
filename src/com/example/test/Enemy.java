package com.example.test;

import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.test.cons.Action;
import com.example.test.cons.EnemySensors;
import com.example.test.cons.GameConstants;
import com.example.test.managers.ResourceManager;



public abstract class Enemy extends AnimatedSprite {
	private Body body;
	protected boolean enemyIsMoving = false;
	private int enemyHp=5;
	private boolean enemyIsAttacked=false;
	private static int enemyAttackCount=0;
	public boolean aniStart=false;
	public boolean waiting = false;
	protected Action enemyLastDirection;
	
	public Action getEnemyLastDirection() {
		return enemyLastDirection;
	}

	protected boolean enemyIsDie=false;

	public Enemy(float pX, float pY, VertexBufferObjectManager vbo,
			Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourceManager.getInstance().enemy_region, vbo);
		createPhysics(camera, physicsWorld);
	}

	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
		setBody(PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0,
						0, false, GameConstants.CATEGORYBIT_ENEMY,
						GameConstants.MASKBITS_ENEMY, (short) 0)));
		getBody().setFixedRotation(true);
		body.setUserData("Enemy");
		body.getFixtureList().get(0).setUserData("Enemy");

		final PhysicsHandler physicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(physicsHandler);

	//	FixtureDef pFixtureDef = PhysicsFactory.createFixtureDef(0f, 0f, 0f,true, GameConstants.CATEGORYBIT_ENEMYBODY,GameConstants.MASKBITS_ENEMYBODY, (short) 0);
		/*
		PolygonShape mPolyleft = new PolygonShape();
		mPolyleft.setAsBox(.1f, .1f);
		pFixtureDef.shape = mPolyleft;
		Fixture leftSensor = getBody().createFixture(pFixtureDef);
		leftSensor.setUserData(EnemySensors.LEFT);
		mPolyleft.dispose();

		PolygonShape mPolyright = new PolygonShape();
		mPolyright.setAsBox(.1f, .1f);
		pFixtureDef.shape = mPolyright;
		Fixture rightSensor = getBody().createFixture(pFixtureDef);
		rightSensor.setUserData(EnemySensors.RIGHT);
		mPolyright.dispose();

		PolygonShape mPolybottom = new PolygonShape();
		mPolybottom.setAsBox(.1f, .1f);
		pFixtureDef.shape = mPolybottom;
		Fixture bottomSensor = getBody().createFixture(pFixtureDef);
		bottomSensor.setUserData(EnemySensors.BOTTOM);
		mPolybottom.dispose();

		PolygonShape mPolytop = new PolygonShape();
		mPolytop.setAsBox(.1f, .1f);
		pFixtureDef.shape = mPolytop;
		Fixture topSensor = getBody().createFixture(pFixtureDef);
		topSensor.setUserData(EnemySensors.TOP);
		mPolytop.dispose();
		*/
		getBody().setUserData(this);

		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, false));
		
		//if(!this.isMoving)
			//this.move(Action.MOVELEFT);
		

	}
	
	public void move(Action to) {
		this.enemyIsMoving  = true;
		if (to == Action.MOVELEFT)
		{
			if (!aniStart || enemyLastDirection==Action.MOVERIGHT)
			{
				this.animate(new long[] { 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 }, 33, 50, true);
				aniStart=true;
			}
			enemyLastDirection=to;
			this.getBody().setLinearVelocity((float) -1.5, 0);
			
		}else if(to == Action.MOVERIGHT)
		{
			if (!aniStart || enemyLastDirection==Action.MOVELEFT)
			{
				this.animate(new long[] { 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 }, 0, 17, true);
				aniStart=true;
			}
			enemyLastDirection=to;
			this.getBody().setLinearVelocity((float) 1.5, 0);
		}
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	public void waiting(boolean attack){
		aniStart=false;
		if(!attack)
		{
			this.getBody().setLinearVelocity(0,1);
		}
		if(!enemyIsAttacked)
		{
			Random rand = new Random();
			if(enemyLastDirection== Action.MOVERIGHT)
				this.animate(new long[] { rand.nextInt(2000)+3000, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 }, 54, 64, true);
			else 
				this.animate(new long[] {  rand.nextInt(2000)+3000, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 }, 54, 64, true);
		}
	}	
	public int getHP()
	{
		return enemyHp;
		
	}
	public void setHP()
	{
		if(enemyLastDirection==Action.MOVELEFT)
		{
			this.getBody().setLinearVelocity(5,0);
		}else if(enemyLastDirection==Action.MOVERIGHT)
		{
			this.getBody().setLinearVelocity(-5,0);
		}
		enemyHp--;
	}
	
	public void setAtt(boolean e)
	{
		enemyIsAttacked=e;
		if(e)
			enemyAttackCount++;
		else 
			enemyAttackCount--;
			
	}
	
	public void setAtt()
	{
		enemyAttackCount=0;
	}
	
	public boolean getAtt()
	{
		return enemyIsAttacked;
	}
	
	public int getAttCount(){
		return enemyAttackCount;
	}

}