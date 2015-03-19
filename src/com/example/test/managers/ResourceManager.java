//КЛАСС ОТВЕЧАЮЩИЙ ЗА ЗАГРУЗКУ И ВЫГРУЗКУ РЕСУРСОВ ИГРЫ
package com.example.test.managers;
//Менеджер ресурсов, отсюда загружаются все текстуры, звуки, регионы. 
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;

import com.example.test.GameActivity;

import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXProperties;
import org.andengine.extension.tmx.TMXTile;
import org.andengine.extension.tmx.TMXTileProperty;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.TMXLoader.ITMXTilePropertiesListener;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.debug.Debug;



public class ResourceManager 

{
	//------------------------------------------------------------------
	//ОБЪЕКТЫ
	//------------------------------------------------------------------
	private static final ResourceManager INSTANCE = new ResourceManager();
	
	public Engine engine;
	public GameActivity activity;
	public Camera camera;
	public VertexBufferObjectManager vbo;
	//-----------------------------------------------------------------
	
	//-----------------------------------------------------------------
	//ПЕРЕМЕННЫЕ
	//-----------------------------------------------------------------
	//-----------------------ТЕКСТУРЫ ИНТРО ЭКРАНА-------------------------------
	BitmapTextureAtlas splashTexture;
	public ITextureRegion splashRegion;
	//--------------------------------------------------------------------------
	
	//-----------------------ПЕРЕМЕННЫЕ СЦЕНЫ МЕНЮ-------------------------------
	private BuildableBitmapTextureAtlas menuTextureAtlas; 
	public ITextureRegion menuBackgroundRegion; //фон
	public ITextureRegion menuPlayRegion; // играть
	public ITextureRegion menuOptionsRegion; //опции
	public ITextureRegion menuQuitRegion;//выход
	//---------------------------------------------------------------------------
	
	//---------------------------ТЕКСТУРЫ ЗАГРУЗОЧНОГО ЭКРАНА---------------------
	private BitmapTextureAtlas loadingTexture;
	public ITextureRegion loadingRegion; //загрузка
	//--------------------------ТЕКСТУРЫ Игры---------------------
	private BitmapTextureAtlas mBacgroundMap;
	public TextureRegion bacgroundMap;
	private BitmapTextureAtlas mBacgroundMap2;
	public TextureRegion bacgroundMap2;
	
	private BitmapTextureAtlas mButton;
	public TextureRegion upB;
	public TextureRegion downB;
	public TextureRegion rightB;
	public TextureRegion leftB;
	
	public static TiledTextureRegion uli;
	
	public BuildableBitmapTextureAtlas gameTextureAtlas;
	public ITexture gameFontTexture;
	
	private BitmapTextureAtlas mMonster;
	public TiledTextureRegion enemy_region;
	
	private BitmapTextureAtlas player_texture;
	public TiledTextureRegion player_region;
	
	private BitmapTextureAtlas mHPandE;
	public  TextureRegion font; 
	public  TextureRegion rHP;
	public  TextureRegion rEnergy;
	
	private BitmapTextureAtlas deadBG;
	public 	TextureRegion backGround;
	private BitmapTextureAtlas deadButton;
	public 	TextureRegion buttonMenu;
	public 	TextureRegion buttonReset;
	public 	TextureRegion buttonShop;
	
	private BitmapTextureAtlas points;
	public TextureRegion gEnergyPoint;
	public TextureRegion mEnergyPoint;
	public TextureRegion lEnergyPoint;
	public TextureRegion gHitPoint;
	public TextureRegion mHitPoint;
	public TextureRegion lHitPoint;
	
	private BitmapTextureAtlas mHome;
	public TextureRegion home;
	
	//----------------------------------------------------------------------------
	//---------------------------ОСНОВНОЙ ШРИФТ-----------------------------------
	public Font menuFont;
	public Font gameFont;
	//----------------------------------------------------------------------------
	public TMXTiledMap mTMXTiledMap;
	private HUD gameHUD;
	private PhysicsWorld physicsWorld;
	private boolean stopManagedUpdate = false;
	
	//-----------------------------------------------------------------
	//ЛОГИКА КЛАССА
	//-----------------------------------------------------------------
	
	//-------------------------------
	//ЗАГРУЗКА РЕСУРСОВ МЕНЮ

	public void loadMenuResources()
	{
		loadMenuGraphics();
		loadMenuSounds();
	}

	
	//загрузка графики
	private void loadMenuGraphics()
	{

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
        menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 1024, TextureOptions.DEFAULT);
        menuBackgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "bg.png");
        menuPlayRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
        menuOptionsRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "options.png");
        menuQuitRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "quit.png");
    	try 
    	{
			this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.menuTextureAtlas.load();
		} 
    	catch (final TextureAtlasBuilderException e)
    	{
			Debug.e(e);
		}
    	
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		loadingTexture = new BitmapTextureAtlas(activity.getTextureManager(),2048,1024,TextureOptions.BILINEAR);
		loadingRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(loadingTexture, activity, "loading.png",0,0);
		loadingTexture.load();
	}
	
	
	//загрузка звуков
	private void loadMenuSounds()
	{
	
	}
	//---------------------------------
	
	
	//---------------------------------
	//ЗАГРУЗКА ИГРОВЫХ РЕСУРСОВ
	public void loadGameResources()
	{
		loadGameGraphics();
		loadGameSounds();
		loadGameFonts();
	}
	
	//загрузка графики
	private void loadGameGraphics()
	{
		
		try {
			final TMXLoader tmxLoader = new TMXLoader(activity.getAssets(), engine.getTextureManager(), TextureOptions.BILINEAR_PREMULTIPLYALPHA, vbo, new ITMXTilePropertiesListener() {
				@Override
				public void onTMXTileWithPropertiesCreated(final TMXTiledMap pTMXTiledMap, final TMXLayer pTMXLayer, final TMXTile pTMXTile, final TMXProperties<TMXTileProperty> pTMXTileProperties) {

				}
			});
			//mTMXTiledMap = tmxLoader.loadFromAsset("tmx/test.tmx");
			mTMXTiledMap = tmxLoader.loadFromAsset("tmx/t2.tmx");

		} catch (final Exception e) {
			Debug.e(e);
		}
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		mButton = new BitmapTextureAtlas(activity.getTextureManager(),264,65,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		upB=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mButton, activity, "btnUp.png", 0, 0);
		downB=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mButton, activity, "btnDown.png", 66, 0);
		rightB=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mButton, activity, "btnRight.png", 122, 0);
		leftB=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mButton, activity, "btnLeft.png", 188, 0);
		mButton.load();
		

		/*mBacgroundMap = new BitmapTextureAtlas(activity.getTextureManager(),1280,480,TextureOptions.BILINEAR);

		mBacgroundMap.load();*/
		
		mBacgroundMap = new BitmapTextureAtlas(activity.getTextureManager(),3840,480,TextureOptions.BILINEAR);
		bacgroundMap = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBacgroundMap, activity, "mapP1.png", 0, 0);
		mBacgroundMap.load();
		
		mBacgroundMap2 = new BitmapTextureAtlas(activity.getTextureManager(),1280,480,TextureOptions.BILINEAR);
		bacgroundMap2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBacgroundMap2, activity, "mapP2.png", 0, 0);
		mBacgroundMap2.load();
		
		mHPandE = new BitmapTextureAtlas(activity.getTextureManager(),303,5,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHPandE, activity, "font.png", 0, 0);
		rHP=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHPandE, activity, "hp2.png", 101, 0);
		rEnergy=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHPandE, activity, "energy.png", 202, 0);
		mHPandE.load();
		
		
		player_texture = new BitmapTextureAtlas(activity.getTextureManager(),722,154,TextureOptions.BILINEAR);
		player_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(player_texture, activity,  "uli.png", 0, 0, 16, 2);
		player_texture.load();
		
		mHome = new BitmapTextureAtlas(activity.getTextureManager(), 180, 310, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		home =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(mHome, activity,  "home.png", 0, 0);
		mHome.load();
		
		mMonster = new BitmapTextureAtlas(activity.getTextureManager(), 2100, 90, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		enemy_region = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mMonster, activity,  "wolf.png", 0, 0, 33, 2);
		mMonster.load();
		
		FontFactory.setAssetBasePath("gfx/font/");
		gameFontTexture = new BitmapTextureAtlas(activity.getTextureManager(),512,512,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		gameFont = FontFactory.createStrokeFromAsset(activity.getFontManager(), gameFontTexture, activity.getAssets(), "MicraBold.ttf", 35, true, Color.WHITE, 2, Color.BLACK);
		gameFont.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/deadMenu/");
		
		deadBG = new BitmapTextureAtlas(activity.getTextureManager(),800,480,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			backGround = BitmapTextureAtlasTextureRegionFactory.createFromAsset(deadBG, activity, "Dead.png", 0, 0);
		deadBG.load();
		
		deadButton = new BitmapTextureAtlas(activity.getTextureManager(),382,126,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			buttonMenu = BitmapTextureAtlasTextureRegionFactory.createFromAsset(deadButton, activity, "menu.png", 0, 0);
			buttonReset = BitmapTextureAtlasTextureRegionFactory.createFromAsset(deadButton, activity, "replay.png", 128, 0);
			buttonShop = BitmapTextureAtlasTextureRegionFactory.createFromAsset(deadButton, activity, "Shop.png", 254, 0);
		deadButton.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/points/");
		points = new BitmapTextureAtlas(activity.getTextureManager(),306,51,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			gEnergyPoint = BitmapTextureAtlasTextureRegionFactory.createFromAsset(points, activity, "gEnergy.png", 0, 0);
			mEnergyPoint = BitmapTextureAtlasTextureRegionFactory.createFromAsset(points, activity, "mEnergy.png", 51, 0);
			lEnergyPoint = BitmapTextureAtlasTextureRegionFactory.createFromAsset(points, activity, "lEnergy.png", 102, 0);
			gHitPoint = BitmapTextureAtlasTextureRegionFactory.createFromAsset(points, activity, "gHP.png", 153, 0);
			mHitPoint = BitmapTextureAtlasTextureRegionFactory.createFromAsset(points, activity, "mHP.png", 204, 0);
			lHitPoint = BitmapTextureAtlasTextureRegionFactory.createFromAsset(points, activity, "lHP.png", 255, 0);
		points.load();
		
	}
	
	//загрузка звуков
	private void loadGameSounds()
	{

    }

	
	
	//загрузка шрифтов
	private void loadGameFonts()
	{	

	}
	
	//-----------------------------------

	//-----------------------------------
	//ЗАГРУЗКА И ВЫГРУЗКА СТАРТОВОГО ЭКРАНА ИГРЫ
	public void loadSplashScreen()
	{	
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.splashTexture = new BitmapTextureAtlas(activity.getTextureManager(),800,480,TextureOptions.BILINEAR);
		this.splashRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTexture, activity,"logo.png",0,0);
		splashTexture.load();
		
	}
	
	public void unloadSplashScreen()
	{
		splashTexture.unload();
		splashRegion = null;
	}
	
	public void unloadMenuTextures()
	{
		menuTextureAtlas.unload();
	}
	
	public void loadMenuTextures()
	{
		menuTextureAtlas.load();
		
	}
	
	public void unloadGameTextures()
	{
		/*gameTextureAtlas.unload();
		mButton.unload();
		gameTextureAtlas.unload();
		mMonster.unload();
		player_texture.unload();
		mHP.unload();
		deadBG.unload();
		deadButton.unload();*/
		
	}
	
	//-------------------------------------
	
	//------------------------------------------
	//помоему, это что то типа подготовки менеджера ресурсов
	//----------------------------------------------------
	public static void prepareManager (Engine engine, GameActivity activity,Camera camera,VertexBufferObjectManager vbo)
	{
		getInstance().engine = engine; //скорее всего, мы этими кодами проверяем статус движка, актвити,камеры и ВБО,
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbo = vbo;
	}
	//не знаю пока, надо будет разобраться в дальнейшем и не забыть написать комментарии по этому поводу
	//UPD 1. Возможно с помощью этих действий мы принимаем положения и параметры каждого этого элемента, например камеры, чтобы не инициализировать её снова или получать её координаты, статус и прочую инфу сюда, чтобы дальше работать с этой инфой
	public static ResourceManager getInstance ()
	{
		return INSTANCE;
	}
}
