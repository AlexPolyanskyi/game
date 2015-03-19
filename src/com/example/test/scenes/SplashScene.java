package com.example.test.scenes;

import org.andengine.entity.sprite.Sprite;

import com.example.test.cons.GameConstants;
import com.example.test.managers.SceneManager.SceneType;

//--------------------------------------------------------------------------
//ИНТРО
//--------------------------------------------------------------------------

public class SplashScene extends BaseScene  implements GameConstants
{
	//---------------------------------------------
	private Sprite splash;
	//----------------------------------------------

	@Override
	public void createScene() 
	{
		//splash = new Sprite((D_CAMERA_WIDTH/2)-sprite_w,(D_CAMERA_HEIGHT/2)-sprite_h,resourceManager.splashRegion,vbo); 
		//splash.setScale(1f);
		splash = new Sprite(0,0,resourceManager.splashRegion,vbo);
		attachChild(splash);
	}

	@Override
	public void onBackKeyPressed()
	{
		
	}

	@Override
	public SceneType getSceneType()
	{
		//установка типа сцены 
		return SceneType.SCENE_SPLASH;
	}

	@Override
	public void disposeScene()
	{	
		//уничтожение интро
		splash.detachSelf();
	    splash.dispose();
	    this.detachSelf();
	    this.dispose();
	}

}
