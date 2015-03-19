//СЦЕНА МЕНЮ
package com.example.test.scenes;

import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;

import android.opengl.GLES20;

import com.example.test.cons.GameConstants;
import com.example.test.managers.SceneManager;
import com.example.test.managers.SceneManager.SceneType;
import com.example.test.scenes.BaseScene;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener, GameConstants
{
	//---------------------------------------------
	// ПЕРЕМЕННЫЕ
	//---------------------------------------------
	
	private MenuScene menuChildScene;
	
	private final int MENU_PLAY = 0;
	private final int MENU_OPTIONS = 1;
	private final int MENU_RESET = 2;
	private final int MENU_QUIT = 3;
	private Sprite bg_sprite;
	//---------------------------------------------
	// МЕТОДЫ
	//---------------------------------------------

	@Override
	public void createScene()
	{	
		createBackground();
		createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed()
	{
		System.exit(0);
	}

	@Override
	public SceneType getSceneType()
	{
		return SceneType.SCENE_MENU;
	}
	

	@Override
	public void disposeScene()
	{
		bg_sprite.detachSelf();
		bg_sprite.dispose();
		menuChildScene.detachSelf();
		menuChildScene.dispose();
		
	}
	
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
		switch(pMenuItem.getID())
		{
			case MENU_PLAY:
				SceneManager.getInstance().loadGameScene(engine,false);
				return true;
			case MENU_OPTIONS:
				System.exit(0);
				return true;
			case MENU_RESET:
				System.exit(0);
				return true;
				
			case MENU_QUIT:
				System.exit(0);
				return true;
			default:
				return false;
		}
	}
	
	//---------------------------------------------
	// ЛОГИКА КЛАССА
	//---------------------------------------------
	
	//создание сцены бэкграунда
	private void createBackground()
	{
		bg_sprite = new Sprite(0, 0, resourceManager.menuBackgroundRegion, vbo);
		attachChild(bg_sprite);
		
	}
	
	//функция создания подсцены элементов меню
	private void createMenuChildScene()
	{
		menuChildScene = new MenuScene(camera);
		menuChildScene.setPosition(0,0);
		
		final SpriteMenuItem resetMenuItem = new SpriteMenuItem(MENU_RESET, resourceManager.menuPlayRegion, vbo);
		resetMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		final SpriteMenuItem playMenuItem = new SpriteMenuItem(MENU_PLAY, resourceManager.menuPlayRegion, vbo);
		playMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		final SpriteMenuItem optionsMenuItem = new SpriteMenuItem(MENU_OPTIONS, resourceManager.menuOptionsRegion, vbo);
		optionsMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		final SpriteMenuItem quitMenuItem = new SpriteMenuItem(MENU_QUIT, resourceManager.menuQuitRegion, vbo);
		quitMenuItem.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		menuChildScene.addMenuItem(playMenuItem);
		menuChildScene.addMenuItem(optionsMenuItem);
		menuChildScene.addMenuItem(resetMenuItem);
		menuChildScene.addMenuItem(quitMenuItem);
		
		menuChildScene.buildAnimations();
		menuChildScene.setBackgroundEnabled(false);
		
	/*	playMenuItem.setPosition((D_CAMERA_WIDTH/2)-(playMenuItem.getWidth()/2),D_CAMERA_HEIGHT/8);
		optionsMenuItem.setPosition((D_CAMERA_WIDTH/2)-(optionsMenuItem.getWidth()/2),(D_CAMERA_HEIGHT/8)+70);
		quitMenuItem.setPosition((D_CAMERA_WIDTH/2)-(quitMenuItem.getWidth()/2),300);
		resetMenuItem.setPosition((D_CAMERA_WIDTH/2)-(resetMenuItem.getWidth()/2),400);
		*/
		menuChildScene.setOnMenuItemClickListener(this);
		
		setChildScene(menuChildScene);
	}
}