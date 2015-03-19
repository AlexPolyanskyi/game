//ОСНОВНОЙ КЛАСС ИГРЫ. Активити. С него запускается игра и движок.
package com.example.test;
import java.io.IOException;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.ui.activity.BaseGameActivity;

import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.test.cons.GameConstants;
import com.example.test.managers.ResourceManager;
import com.example.test.managers.SceneManager;

public class GameActivity extends BaseGameActivity implements GameConstants {

	private EngineOptions engineOptions;
	private BoundCamera mCamera;

	public static float CAMERA_WIDTH = 800;
	public static float CAMERA_HEIGHT = 480;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
// TODO Auto-generated method stub
			final DisplayMetrics displayMetrics = new DisplayMetrics();
			WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(displayMetrics);
			wm.getDefaultDisplay().getRotation();
			CAMERA_WIDTH = displayMetrics.widthPixels;
			CAMERA_HEIGHT = displayMetrics.heightPixels;
				
			this.mCamera = new BoundCamera(0, 0, D_CAMERA_WIDTH, D_CAMERA_HEIGHT);
			mCamera.setResizeOnSurfaceSizeChanged(true); 
	
			engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
		
			engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true); //установка звуковых опции
			engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON); //хрен значает что это устанавливает, что то связанное с блокировкой экрана
			engineOptions.getTouchOptions().setNeedsMultiTouch(true); //включение мультитач
			engineOptions.getTouchOptions().setTouchEventIntervalMilliseconds(10);
	/*		
			if(MultiTouch.isSupported(this)) {
				if(MultiTouch.isSupportedDistinct(this)) {
					Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
			}
			*/
			return engineOptions; //возвращаем опции
		}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException 
	{
		ResourceManager.prepareManager(mEngine, this, mCamera, getVertexBufferObjectManager()); 
		pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException 
	
	{
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback); 
	}
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException 
	{
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() 
	    {
	        public void onTimePassed(final TimerHandler pTimerHandler) 
	        {
	            mEngine.unregisterUpdateHandler(pTimerHandler);
	            SceneManager.getInstance().createMenuScene();
	        }
	    }));
	    pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		System.exit(0);	
	}
}
