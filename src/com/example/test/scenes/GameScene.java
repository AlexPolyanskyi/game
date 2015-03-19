package com.example.test.scenes;

import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.controller.MultiTouchController;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.example.test.Enemy;
import com.example.test.Player;
import com.example.test.Points;
import com.example.test.cons.Action;
import com.example.test.cons.GameConstants;
import com.example.test.managers.ResourceManager;
import com.example.test.managers.SceneManager;
import com.example.test.managers.SceneManager.SceneType;
 
public class GameScene extends BaseScene implements IOnSceneTouchListener, GameConstants {

	private Player player;
	private Enemy enemy;
	private PhysicsWorld physicsWorld;
	private HUD gameHUD;
	private boolean stopManagedUpdate = false;
	//private int hp=100;
	private Sprite home;
	private Sprite hp; 
	private Sprite energy; 
	private Rectangle rect;
	int i=0;
	private boolean isenemyAtt=false;
	@Override
	public void createScene() {
	createHUD();
	createPhysics();
	loadlevel();
	statsDecrease(engine);
	}
		
	private void loadlevel(){
		this.setTouchAreaBindingOnActionDownEnabled(true);
		  this.setOnAreaTouchTraversalFrontToBack();
		
		for(TMXLayer tmxLayer : resourceManager.mTMXTiledMap.getTMXLayers()) {

		attachChild(tmxLayer);
			}
		
		Sprite backgroundMap = new Sprite(0,0,resourceManager.bacgroundMap,vbo);
		Sprite backgroundMap2 = new Sprite(3840,0,resourceManager.bacgroundMap2,vbo);
		
		this.attachChild(backgroundMap);
		this.attachChild(backgroundMap2);
		
		for (final TMXObjectGroup group : resourceManager.mTMXTiledMap
				.getTMXObjectGroups()) {
			for (final TMXObject object : group.getTMXObjects()) {
				if (group.getName().equals("gro")) {
					Rectangle rect = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight(), vbo);

					FixtureDef boxFixtureDef = PhysicsFactory.createFixtureDef(
							0.0f, 0.0f, 1f, false,
							GameConstants.CATEGORYBIT_WALL,
							GameConstants.MASKBITS_WALL, (short) 0);

					PhysicsFactory.createBoxBody(physicsWorld, rect,
							BodyType.StaticBody, boxFixtureDef);

					rect.setVisible(false);

					final PhysicsHandler physicsHandler = new PhysicsHandler(
							rect);
					rect.registerUpdateHandler(physicsHandler);
					
		attachChild(rect);

				}
				 else if (group.getName().equals("hpPoints")) {
					 ITextureRegion image = null;
					 Random rand = new Random();
					 int i = rand.nextInt(2);
					 int count=0;
					 switch(i)
					 {
					 	case 0: image = ResourceManager.getInstance().gHitPoint; 
					 			count=100;
					 			break;
					 	case 1: image = ResourceManager.getInstance().mHitPoint; 
					 			count=40;
					 			break;
					 	case 2: image = ResourceManager.getInstance().lHitPoint; 
					 			count=20;
					 			break;
					 }
					 Points hpPoint = new Points(object.getX(),object.getY(),vbo,"HitPoint",image,count)
					 {
						 protected void onManagedUpdate(float pSecondsElapsed)
					      {
							  if (player.collidesWith(this))
					          {                                                              
					             player.setHP(player.getHP() + this.getPointsCount()); 
					             if (player.getHP() > player.getPlayerMaxHp()){
					            	 player.setHP(player.getPlayerMaxHp());
					             }
					             this.setVisible(false);
		                         this.setIgnoreUpdate(true);
					          }
					      } 
					 };
		attachChild(hpPoint);

				}
				 else if(group.getName().equals("abyss"))
				 {
					 rect = new Rectangle(object.getX(),
								object.getY(), object.getWidth(),
								object.getHeight(), vbo);
					 rect.setVisible(false);
		attachChild(rect);
				
				 }
				 else if (group.getName().equals("energyPoints")) {
					 ITextureRegion image = null;
					 Random rand = new Random();
					 int i = rand.nextInt(3);
					 int count=0;
					 switch(i)
					 {
					 	case 0: image = ResourceManager.getInstance().gEnergyPoint; 
					 			count=100;
					 			break;
					 	case 1: image = ResourceManager.getInstance().mEnergyPoint; 
					 			count=40;
					 			break;
					 	case 2: image = ResourceManager.getInstance().lEnergyPoint; 
					 			count=20;
					 			break;
					 }
					 Points EnergyPoint = new Points(object.getX(),object.getY(),vbo,"Energy",image,count)
					 {
						  protected void onManagedUpdate(float pSecondsElapsed)
					      {
							  if (player.collidesWith(this))
					          {                                                              
					             player.setPlayerEnergy(player.getPlayerEnergy() + this.getPointsCount()); 
					             if (player.getPlayerEnergy() > player.getMaxPlayerEnergy()){
					            	 player.setPlayerEnergy(player.getMaxPlayerEnergy());
					             }
					             this.setVisible(false);
		                         this.setIgnoreUpdate(true);
					          }
					      }
					 };
			attachChild(EnergyPoint); 
					
				}
				 else if (group.getName().equals("Enemies")) {

						 enemy = new Enemy(object.getX(), object.getY(), vbo,
								camera, physicsWorld) {
							      protected void onManagedUpdate(float pSecondsElapsed)
							      {
							    	  super.onManagedUpdate(pSecondsElapsed);
							          if (player.collidesWith(this))
							          {                                                              
							              if(player.getAttack())
							              {
							            	 this.setHP();	
							              }
								          if(this.getHP()<0)
								          {
								        	 dieEnemy(this);
								        	 this.enemyIsDie=true;
								          }
							          }else if(rect.collidesWith(this))
							          {
								        	 dieEnemy(this);
								        	 this.enemyIsDie=true; 
							          }
							       if(!this.enemyIsDie)
							       {
							    	  
							    	  if((((player.getX()-this.getX())<70)&&((player.getX()-this.getX())>-(player.getWidth()+5)))
							        		  &&
							        		  ((player.getY()+player.getHeight()-10>this.getY())&&(player.getY()-70<this.getY())))
							          {
							    		  this.waiting=false;
							        	  waiting(player.getAttack());
							        	  if(!this.getAtt())
							        	  {
							        		this.setAtt(true);
							        		if(this.enemyLastDirection==Action.MOVERIGHT)
							        			this.animate(new long[] { 800,100,100 }, 18,20, true);
							        		else
							        			this.animate(new long[] { 800,100,100 }, 51,53, true);	
							        	  }
							        	  isenemyAtt=this.getAtt();
							          }
							          else if((((Math.abs(player.getX()-this.getX()))<300)) 
							        		  &&
							        		  ((player.getY()+player.getHeight()+1>this.getY())&&(player.getY()-300<this.getY())) )
							          {
							        	  this.waiting=false;
							        	  	  if(this.getAtt())
							        	  	  {
							        		  this.setAtt(false);
							        	  	  }
							        	  	  isenemyAtt=this.getAtt();
							        	  	  /*
							        	  	  if((Math.abs(player.getX()-this.getX()))<60)
							        	  	  {							      	  
							        	  		this.move(Action.MOVERIGHT);
							        	  	  }else */
							        	  	  if(player.getX()<this.getX())
							        	  	  {
							        	  		  this.move(Action.MOVELEFT);
							        	  	  }
							        	  	  else if(player.getX()>this.getX())
							        	  	  {
							        	  		  this.move(Action.MOVERIGHT);
							        	  	  }
							          }else 
							          {
							        	  if(!this.waiting){
							        	  waiting(player.getAttack());
							        	  this.waiting=true;
							        	  }
							          }
							       }

							      }; 
						};
						 
					
		attachChild(enemy); 
					} 
				}
			}
			
		player = new Player(70, 240, vbo, camera, physicsWorld)
		{		
			  protected void onManagedUpdate(float pSecondsElapsed)
		      {
		      	  super.onManagedUpdate(pSecondsElapsed);
		          hp.setSize(player.getBarCount(getPlayerMaxHp(),getHP()), 5);
		          energy.setSize(player.getBarCount(getMaxPlayerEnergy(),getPlayerEnergy()), 5);
				  if (rect.collidesWith(this))
		          {                                                              
					  player.setHP(0);
		          }
		      }
		};
	attachChild(player);
		
		
		home = new Sprite(4570,92,resourceManager.home,vbo)
		{		
			  protected void onManagedUpdate(float pSecondsElapsed)
		      {
				  if (player.collidesWith(this))
		          {   
					  gameOver();
		          }
		      }
		};
		attachChild(home);
	}
	public void statsDecrease(final Engine engine)
	{
		 engine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback()
		    {                                    
		        public void onTimePassed(final TimerHandler pTimerHandler)
		        {
		            pTimerHandler.setAutoReset(true);
		            
		        	if (player.getPlayerEnergy()<100)
		        	{
		        		player.setPlayerEnergy(player.getPlayerEnergy()+1);
		        	}
		        	
		            if(isenemyAtt)
		            {
		    			player.setHP(player.getHP()-enemy.getAttCount());
		            }
	    			if(player.getHP()<=0)
	    			{
	    				gameOver();
	    			}
		        }
		    }));
	}
	public void gameOver(){
		Sprite bg = new Sprite(0,0,resourceManager.backGround,vbo);
		Sprite bMenu = new Sprite(GameConstants.D_CAMERA_WIDTH - 500 - resourceManager.buttonMenu.getWidth(),
									GameConstants.D_CAMERA_HEIGHT - resourceManager.buttonMenu.getHeight() - 110,
										resourceManager.buttonMenu,vbo){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionUp()) {
					SceneManager.getInstance().loadMenuScene(engine);
				} 
				return true;
			};
		};
		Sprite bReset = new Sprite(GameConstants.D_CAMERA_WIDTH - 350 - resourceManager.buttonReset.getWidth(),
				GameConstants.D_CAMERA_HEIGHT - resourceManager.buttonReset.getHeight() - 110,
					resourceManager.buttonReset,vbo){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionUp()) {
					SceneManager.getInstance().loadGameScene(engine,true);
				} 
				return true;
			};
		};
		Sprite bShop = new Sprite(GameConstants.D_CAMERA_WIDTH - 200 - resourceManager.buttonShop.getWidth(),
				GameConstants.D_CAMERA_HEIGHT - resourceManager.buttonShop.getHeight() - 110,
					resourceManager.buttonShop,vbo){
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionUp()) {

				} 
				return true;
			};
		};
		gameHUD.attachChild(bg);
		gameHUD.attachChild(bMenu);
		gameHUD.attachChild(bReset);
		gameHUD.attachChild(bShop);
		gameHUD.registerTouchArea(bMenu);
		gameHUD.registerTouchArea(bReset);
		gameHUD.registerTouchArea(bShop);
	}
	public void dieEnemy(final Enemy e)
	{
		if(e.getEnemyLastDirection() == Action.MOVELEFT)
		{
			e.stopAnimation(65);
		}
		else
		{
			e.stopAnimation(32);
		}
		
		 enemy.setAtt();

		engine.registerUpdateHandler(new TimerHandler(0.8f, new ITimerCallback()
	    {                                    
	        public void onTimePassed(final TimerHandler pTimerHandler)
	        {
	        	pTimerHandler.setAutoReset(false);
	        	((BaseGameActivity) activity)
	        	.runOnUpdateThread(new Runnable() {

	        		@Override
	        		public void run() {
	        			physicsWorld
							.unregisterPhysicsConnector(physicsWorld
									.getPhysicsConnectorManager()
									.findPhysicsConnectorByShape(e
											));
	        			e.getBody().setActive(false);
	        			physicsWorld.destroyBody(e.getBody()); 
	        			detachChild(e);
	        		}
			});
	        }
	    }
	 ));}
	
	private void createHUD() {
		
		gameHUD = new HUD();
		gameHUD.setTouchAreaBindingOnActionDownEnabled(true);
		gameHUD.setTouchAreaBindingOnActionMoveEnabled(true);
		final Sprite attButton = new Sprite(GameConstants.D_CAMERA_WIDTH - 120
				- resourceManager.upB.getWidth(),
				GameConstants.D_CAMERA_HEIGHT
						- resourceManager.upB
								.getHeight() - 100,
								resourceManager.upB, vbo) 														{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
					if (pSceneTouchEvent.isActionDown() && !player.getJumping() && player.getPlayerEnergy()>=5) 
					{
						player.attack();
					} else if (pSceneTouchEvent.isActionUp()&& !player.getJumping()) 
					{
						player.attackStop();
					}
				return true;
			};
		};
		final Sprite jumpButton = new Sprite(GameConstants.D_CAMERA_WIDTH - 30
				- resourceManager.upB.getWidth(),
				GameConstants.D_CAMERA_HEIGHT
						- resourceManager.upB
								.getHeight() - 100,
								resourceManager.upB, vbo) 														{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionUp()) {

					player.jump();

				} 
				return true;
			};
		};
		final Sprite leftArrowButton = new Sprite(15,
				GameConstants.D_CAMERA_HEIGHT
						- resourceManager.leftB
								.getHeight() - 100,
								resourceManager.leftB, vbo) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionDown()) {

					player.move(Action.MOVELEFT);

				} else if (pSceneTouchEvent.isActionUp()) {

					player.move(Action.STOP);

				}

				return true;
			};
		};

		final Sprite rightArrowButton = new Sprite(
				resourceManager.rightB.getWidth()
						+ (60 + 15),
				GameConstants.D_CAMERA_HEIGHT
						- resourceManager.rightB
								.getHeight() - 100,
								resourceManager.rightB, vbo) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {

				if (pSceneTouchEvent.isActionDown()) {

					player.move(Action.MOVERIGHT);

				} else if (pSceneTouchEvent.isActionUp()) {

					player.move(Action.STOP);

				}

				return true;
			};
		};
		
		Sprite bgHP = new Sprite(20,70,resourceManager.font,vbo);
		Sprite bgEnergy = new Sprite(20,90,resourceManager.font,vbo);
		hp = new Sprite(20,70,resourceManager.rHP,vbo);
		energy = new Sprite(20,90,resourceManager.rEnergy,vbo);
		
		gameHUD.attachChild(bgHP);
		gameHUD.attachChild(hp);
		
		gameHUD.attachChild(bgEnergy);
		gameHUD.attachChild(energy);
	
		gameHUD.attachChild(jumpButton);
		gameHUD.attachChild(attButton);
		gameHUD.attachChild(leftArrowButton);
		gameHUD.attachChild(rightArrowButton);
		gameHUD.registerTouchArea(jumpButton);
		gameHUD.registerTouchArea(attButton);
		gameHUD.registerTouchArea(leftArrowButton);
		gameHUD.registerTouchArea(rightArrowButton);
		camera.setHUD(gameHUD);

	}
	
	private void createPhysics() {
		physicsWorld = new PhysicsWorld(new Vector2(0f,SensorManager.GRAVITY_EARTH
				), false);
		physicsWorld.setContactListener(contactListener());
		registerUpdateHandler(physicsWorld);
	}
	
	private ContactListener contactListener() {
		ContactListener contactListener = new ContactListener() {
			public void beginContact(Contact contact) {
				final Fixture x1 = contact.getFixtureA();
				final Fixture x2 = contact.getFixtureB();

				  if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("Player")){
						player.increaseFootContacts();
						player.stop();	
				  }
			}
			@Override
			public void endContact(Contact contact) {
				  if(contact.getFixtureB().getUserData() != null && contact.getFixtureB().getUserData().equals("Player")){
				player.decreaseFootContacts();	
				  }
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub
				
			}
		};
		return contactListener;
		

			}
	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		this.stopManagedUpdate = true;
		camera.setHUD(null);
		camera.setChaseEntity(null);
		camera.setCenter(GameConstants.D_CAMERA_WIDTH / 2,
				GameConstants.D_CAMERA_HEIGHT / 2);
		this.detachSelf();
		this.detachChildren();
		this.dispose();		
	}
	public void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		if (this.stopManagedUpdate)
			return;
		physicsWorld.onUpdate(pSecondsElapsed);
		Entity e = new Entity();
		int tilemapheight = resourceManager.mTMXTiledMap.getTileHeight()
				* resourceManager.mTMXTiledMap.getTileRows(), mapW = resourceManager.mTMXTiledMap
				.getTileWidth() * resourceManager.mTMXTiledMap.getTileColumns();

		float y = (tilemapheight - (GameConstants.D_CAMERA_HEIGHT / 2));
		
		if (player.getX() > (GameConstants.D_CAMERA_WIDTH / 2)
				&& player.getX() < (mapW - GameConstants.D_CAMERA_WIDTH / 2))
			e.setPosition(player.getX(), y);
		else if (player.getX() < (GameConstants.D_CAMERA_WIDTH / 2))
			e.setPosition((GameConstants.D_CAMERA_WIDTH / 2), y);
		else if (player.getX() > (mapW - GameConstants.D_CAMERA_WIDTH / 2))
			e.setPosition((mapW - GameConstants.D_CAMERA_WIDTH / 2), y);
		else 
			e.setPosition(player.getX(),y);

		camera.setChaseEntity(e);

		final MoveModifier modifier = new MoveModifier(30, e.getX(),
				player.getX(), e.getY(), y) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				camera.setChaseEntity(null);
			}
		};
		e.registerEntityModifier(modifier);
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}
}
