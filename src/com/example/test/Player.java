package com.example.test;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.test.cons.Action;
import com.example.test.cons.GameConstants;
import com.example.test.managers.ResourceManager;


public abstract class Player extends AnimatedSprite {
	private static Body body;
	
	protected static int footContacts =  0;
	private static boolean playerIsJump = false;
	private static boolean playerIsJumping = true;
	private static boolean playerIsMoving =false;
	private static boolean playerIsAttack =false;
	protected static Action playerLastDirection = Action.MOVERIGHT;
	private boolean canRun = false;
	private int playerMaxHp = 100;
	private int playerCurrentHp = 100;
	private int playerMaxEnergy = 100;
	private int playerCurrentEnergy = 100;
	private double barCount = 0;
	
///////////////////////////////////////////
	public int getPlayerMaxHp() {
		return playerMaxHp;
	}
	
	public void setPlayerMaxHp(int playerMaxHp) {
		this.playerMaxHp = playerMaxHp;
	}
	
	public int getMaxPlayerEnergy() {
		return playerMaxEnergy;
	}
	
	public void setMaxPlayerEnergy(int maxPlayerEnergy) {
		this.playerMaxEnergy = maxPlayerEnergy;
	}
	
	public int getPlayerEnergy() {
		return playerCurrentEnergy;
	}
	
	public void setPlayerEnergy(int playerEnergy) {
		this.playerCurrentEnergy = playerEnergy;
	}

	public Player(float pX, float pY, VertexBufferObjectManager vbo,
			Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, ResourceManager.getInstance().player_region, vbo);
		createPhysics(camera, physicsWorld);
	
	}

	private void createPhysics(final Camera camera, PhysicsWorld physicsWorld) {
		final FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0);
		body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, objectFixtureDef);
		body.setFixedRotation(false);
		body.setUserData("Player");
		body.getFixtureList().get(0).setUserData("Player");
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));

		final PhysicsHandler physicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(physicsHandler);
		
	}
	public Body move(Action to)
	{
		if (to == Action.MOVERIGHT) {
			playerIsMoving = true;
			playerLastDirection = to;
			if(!playerIsJump){
				body.setLinearVelocity((float) 2.3,0);
				if(playerIsJumping)
				{
					playerIsJump=true;
				}
			}
				this.animate(new long[] {100, 100, 100, 100, 100, 100, 100, 100}, 0,7, true);
		} else if (to == Action.MOVELEFT) {
			playerIsMoving = true;
			playerLastDirection = to;
			if(!playerIsJump){
				body.setLinearVelocity((float) -2.3,0);
				if(playerIsJumping)
				{
					playerIsJump=true;
				}
			}
				this.animate(new long[] {100, 100, 100, 100, 100, 100, 100, 100}, 16,23, true);
		} else if (to == Action.STOP) {
			playerIsMoving = false;
			if(!playerIsJumping)
			{
				body.setLinearVelocity(0, 0);
				animationRecreation();
			}
		}
		return body;
	}
	
	private void animationRecreation(){
		if (playerLastDirection == Action.MOVELEFT) {
			this.animate(new long[] {350, 350}, 24,25, true);
		} else if (playerLastDirection == Action.MOVERIGHT) {
			this.animate(new long[] {350, 350}, 8,9, true);
		}
	}
	public void jump() {
		if (footContacts>0) {
			playerIsJumping = true;
			if (playerLastDirection == Action.MOVELEFT) {
				stopAnimation(31);
			} else if (playerLastDirection == Action.MOVERIGHT) {
				stopAnimation(15);
			}
			final Vector2 velocity = Vector2Pool.obtain(body.getLinearVelocity().x, -11);
			body.setLinearVelocity(velocity);
			Vector2Pool.recycle(velocity);
		}

	}
	public void attack(){
		if (playerLastDirection==Action.MOVELEFT){
			body.setLinearVelocity(-11, 0);
			this.animate(new long[] {10, 20, 30, 50, 250}, 26,30, false);
		}else if(playerLastDirection==Action.MOVERIGHT){
			body.setLinearVelocity(11, 0);
			this.animate(new long[] {10, 20, 30, 50, 250}, 10,14, false);
		}
		body.setLinearDamping(3);
		playerIsAttack=true;
		playerCurrentEnergy-=5;
	}
	public void attackStop(){
		body.setLinearVelocity(0, 0);
		body.setLinearDamping(0);
		playerIsAttack=false;
		animationRecreation();
	}
	public void decreaseFootContacts() {
		footContacts--;
		
	}
	public boolean getJumping(){
		return playerIsJumping;
		
	}
	
	public boolean getAttack(){
		return playerIsAttack;
		
	}

	public void increaseFootContacts() {
		footContacts++;
		if (footContacts>0 && playerIsJumping) {
			playerIsJumping = false;
			playerIsJump=false;
		}
	}

	public void stop(){
		if(!playerIsMoving){
		body.setLinearVelocity(0, 0);
		if(!playerIsAttack)
		animationRecreation();
	}
	}

	public Action getLastdirection()
	{
		return playerLastDirection;
	}

	public void setHP(int hitPoint)
	{
		playerCurrentHp=hitPoint;
	}
	
	public int getHP()
	{
		return playerCurrentHp;
	}
	
	public int getBarCount(int max, int current){
		barCount = ((double)current/(double)max)*100;
		return (int)barCount;		
	}
	}

