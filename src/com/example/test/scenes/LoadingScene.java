//СЦЕНА ЗАГРУЗКИ
package com.example.test.scenes;

import org.andengine.entity.sprite.Sprite;

import com.example.test.managers.SceneManager.SceneType;

public class LoadingScene extends BaseScene {
	
private Sprite loading;

	@Override
	public void createScene()
	
	{
		// TODO Auto-generated method stub
	 loading = new Sprite (0,0,resourceManager.loadingRegion,vbo); // тупо ставим спрайт с картинкой
		attachChild(loading);
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
	return;	
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene() {
		
		// TODO Auto-generated method stub
		
	}

}
