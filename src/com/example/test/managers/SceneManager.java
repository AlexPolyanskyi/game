//--------------------------------------------------------------------------
//�������� ����, ����� ���� ����������� �����
//--------------------------------------------------------------------------
package com.example.test.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.example.test.scenes.BaseScene;
import com.example.test.scenes.GameScene;
import com.example.test.scenes.LoadingScene;
import com.example.test.scenes.MainMenuScene;
import com.example.test.scenes.SplashScene;

public class SceneManager 
{
	//-------------------------------------------------------
	//����� ����� ����� � ����
	//-------------------------------------------------------
	private BaseScene splashScene; //�����
	private BaseScene menuScene; //����
	private BaseScene loadingScene;//��������
	public BaseScene gameScene;//����
	//-------------------------------------------------------
	
	//-------------------------------------------------------
	//���������� � �������
	//-------------------------------------------------------
	private static final SceneManager INSTANCE = new SceneManager();
	private SceneType currentSceneType = SceneType.SCENE_SPLASH; //������ ��������� ����� �� �����
	private BaseScene currentScene; 
	private Engine engine = ResourceManager.getInstance().engine;
	
	//��� �����
	public enum SceneType
	{
		SCENE_SPLASH,
		SCENE_MENU,
		SCENE_LOADING,
		SCENE_GAME,
	}
	
	//---------------------------------------------------------
	//������ ������
	//---------------------------------------------------------
	public void setScene (BaseScene scene)//������� �������� � ��������� �������� ����� ������� ����� ����������
	{
		engine.setScene(scene); //����� ������ ������ �����
		currentScene = scene; //������ ������� ����� �� �� ������� ��������
		currentSceneType = scene.getSceneType(); //������ ��� ������� ����� �� ��� ������� �������� 
	}
	
	//������ �����, �� ������ ��� ������� ��� �����, � �� �������� ��� � ������� ����
	public void setScene (SceneType sceneType)
	{
		switch (sceneType)
		{
		case SCENE_MENU:
			setScene(menuScene); 
			break;
		case SCENE_GAME:
			setScene(gameScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		default:
			break;
		}
	}
	
	//---------------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------------
	//����������� � ����������� :))))
	//---------------------------------------------------------------------------------
	public static SceneManager getInstance()
	{
		return INSTANCE;
	}
	
	public SceneType getCurrentSceneType()
	{
		return currentSceneType;
	}
	
	public BaseScene getCurrentScene()
	{
		return currentScene;
	}
	//---------------------------------------------------------------------------------
	
	//---------------------------------------------------------------------------------
	//�����
	//---------------------------------------------------------------------------------
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback)
	{
		ResourceManager.getInstance().loadSplashScreen();//��������� �������� ���. ������
		splashScene = new SplashScene(); //��������������
		currentScene = splashScene; //������ ������� �����
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}
	
	private void disposeSplashScene()
	{
		ResourceManager.getInstance().unloadSplashScreen(); //�������� ��������
		splashScene.disposeScene(); //�����������
		splashScene = null;
	}
	
	private void disposeMenuScene()
	{
		ResourceManager.getInstance().unloadMenuTextures(); //�������� ��������
		menuScene.disposeScene(); //�����������
	}
	//-------------------------------------------------------------------------------------
	
	//-------------------------------------------------------------------------------------
	//����� ����
	//-------------------------------------------------------------------------------------
	public void createMenuScene()
	{
	    ResourceManager.getInstance().loadMenuResources(); //�������� ��������, ����� ��������� � ResourceManager
	    menuScene = new MainMenuScene(); 
	    loadingScene = new LoadingScene();
	    SceneManager.getInstance().setScene(menuScene);
	    disposeSplashScene(); 
	}
	//----------------������� �����
	public void loadGameScene(final Engine mEngine,boolean reload)
	{
		setScene(loadingScene); // �� ����� �������� ������� �����, ������ ����������� �����
		if(reload)
		{
			ResourceManager.getInstance().unloadGameTextures();
			gameScene.disposeScene();
		} else 
		{
			ResourceManager.getInstance().unloadMenuTextures();
			disposeMenuScene();
		}
		mEngine.registerUpdateHandler(new TimerHandler(0.1f,new ITimerCallback()
		{
			public void onTimePassed (final TimerHandler pTimerHandler)
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadGameResources();
				gameScene = new GameScene();
				setScene(gameScene);
			}
		}));
	}
	
	//�������� ����� ���� ��� ��������
	public void loadMenuScene(final Engine mEngine)
	{
		setScene(loadingScene);
		ResourceManager.getInstance().unloadGameTextures();
		gameScene.disposeScene();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f,new ITimerCallback()
		{
			public void onTimePassed (final TimerHandler pTimerHandler)
			{
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadMenuTextures();
				menuScene.createScene();
				setScene(menuScene);
			}
		}));
	}
	
}
