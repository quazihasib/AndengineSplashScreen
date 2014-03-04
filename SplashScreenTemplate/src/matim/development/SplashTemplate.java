package matim.development;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;


public class SplashTemplate extends BaseGameActivity
{
	private final int CAMERA_WIDTH = 720;
	private final int CAMERA_HEIGHT = 480;
	
	private Camera camera;
	private Scene splashScene;
	private Scene mainScene;
	
    private BitmapTextureAtlas splashTextureAtlas;
    private ITextureRegion splashTextureRegion;
    private Sprite splash;
    
    public BitmapTextureAtlas mBitmapTextureAtlasBackGround;
    public ITextureRegion mbackGroundTextureRegion;
    
    private BuildableBitmapTextureAtlas mAnimatedBitmapTextureAtlas, mAnimatedMonkeyBitmapTextureAtlas;
	public static TiledTextureRegion mFishTextureRegion,mMonkeyTextureRegion;
	
	public static AnimatedSprite cursor, monkeyTutorial;
	public static Sprite backGround;
	
	
	@Override
	public EngineOptions onCreateEngineOptions()
	{
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
				new FillResolutionPolicy(), camera);
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception
	{
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        splashTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.DEFAULT);
        splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas,
        		this, "splash.png", 0, 0);
        splashTextureAtlas.load();
       
        pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception
	{
		initSplashScene();
        pOnCreateSceneCallback.onCreateSceneFinished(this.splashScene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception
	{
		mEngine.registerUpdateHandler(new TimerHandler(1f, new ITimerCallback() 
		{
            public void onTimePassed(final TimerHandler pTimerHandler) 
            {
                mEngine.unregisterUpdateHandler(pTimerHandler);
                loadResources();
                loadScenes();         
                splash.detachSelf();
                mEngine.setScene(mainScene);
            }
		}));
  
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	public void loadResources() 
	{
		// Load your game resources here!
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		mBitmapTextureAtlasBackGround = new BitmapTextureAtlas(this.getTextureManager(), 1600, 864, TextureOptions.BILINEAR);
		mbackGroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(mBitmapTextureAtlasBackGround, this,
						"JungleBG.png", 0, 0, 1, 1);
		mBitmapTextureAtlasBackGround.load();
		
		
		mAnimatedBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 614, 104, TextureOptions.NEAREST);
		mFishTextureRegion = BitmapTextureAtlasTextureRegionFactory.
				createTiledFromAsset(this.mAnimatedBitmapTextureAtlas, this, "fish.png", 6, 1);
		try 
		{
			this.mAnimatedBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			this.mAnimatedBitmapTextureAtlas.load();
		} 
		catch (TextureAtlasBuilderException e) 
		{
			Debug.e(e);
		}
		
		
		
		mAnimatedMonkeyBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 2000, 267, TextureOptions.NEAREST);
		mMonkeyTextureRegion = BitmapTextureAtlasTextureRegionFactory.
				createTiledFromAsset(this.mAnimatedMonkeyBitmapTextureAtlas, this, "monkeyTutorial.png", 10, 1);
		try 
		{
			this.mAnimatedMonkeyBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			this.mAnimatedMonkeyBitmapTextureAtlas.load();
		} 
		catch (TextureAtlasBuilderException e) 
		{
			Debug.e(e);
		}
		
	}
	
	private void loadScenes()
	{
		// load your game here, you scenes
		mainScene = new Scene();
		
		backGround = new Sprite(0, 0, mbackGroundTextureRegion,
				getVertexBufferObjectManager());
		backGround.setHeight(CAMERA_HEIGHT);
		backGround.setWidth(CAMERA_WIDTH);
		mainScene.attachChild(backGround);
		
		//creating the fish cursor
		cursor = new AnimatedSprite(10, 10, 
				mFishTextureRegion, getVertexBufferObjectManager());
		cursor.animate(new long[]{100, 100, 100, 100, 100, 100}, 0, 5, true);
		mainScene.attachChild(cursor);
		
		monkeyTutorial = new AnimatedSprite(100, 100, mMonkeyTextureRegion,
				getVertexBufferObjectManager()) 
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, 
					final float pTouchAreaLocalY) 
			{
				switch (pSceneTouchEvent.getAction())
				{
				case TouchEvent.ACTION_DOWN: 
				{

				}
					break;

				case TouchEvent.ACTION_UP:

					break;

				}

				return true;
			}
		};
		
		//animating the monkey tutorial
		monkeyTutorial.animate(new long[] { 1000, 1000, 1000, 100, 100, 4000,
				1000, 1000, 100, 100 }, 0, 9, true);
		mainScene.registerTouchArea(monkeyTutorial);
		mainScene.attachChild(monkeyTutorial);
		
	}
	
	// ===========================================================
	// INITIALIZIE  
	// ===========================================================
	
	private void initSplashScene()
	{
    	splashScene = new Scene();
    	splash = new Sprite(0, 0, splashTextureRegion, mEngine.getVertexBufferObjectManager())
    	{
    		@Override
            protected void preDraw(GLState pGLState, Camera pCamera) 
    		{
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
    	};
    	
    	splash.setScale(1.5f);
    	splash.setPosition((CAMERA_WIDTH - splash.getWidth()) * 0.5f, (CAMERA_HEIGHT - splash.getHeight()) * 0.5f);
    	splashScene.attachChild(splash);
	}
}
