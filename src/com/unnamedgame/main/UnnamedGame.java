package com.unnamedgame.main;

import java.io.*;
import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.renderer.*;
import com.openglengine.renderer.model.*;
import com.openglengine.util.Logger.*;
import com.openglengine.util.math.*;
import com.unnamedgame.shaders.*;

/**
 * Game entry point
 * 
 * @author Dominik
 *
 */
public class UnnamedGame extends Basic3DGame {
	public static String RES_FOLDER = "res/";
	public static String MODEL_FOLDER = RES_FOLDER + "model/";
	public static String TEX_FOLDER = RES_FOLDER + "tex/";
	public static String SHADER_FOLDER = RES_FOLDER + "shader/";

	// TODO: eventually to be moved to a config file
	private static final int SCREEN_WIDTH = 1920;
	private static final int SCREEN_HEIGHT = 1080;
	private static final float FOV = 70f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	private static final boolean FULLSCREEN = false;
	private static final String WINDOW_BASETITLE = "Engine " + Engine.ENGINE_VERSION;

	private List<TexturedModel> loadedModels;
	private List<Entity> visibleEntities;

	private Entity camera;
	private StandardShader shader;

	public UnnamedGame() throws IOException {
		super(FOV, NEAR_PLANE, FAR_PLANE);
	}

	@Override
	protected void setup() {
		CustomEntityFactory.load();
		this.visibleEntities = new ArrayList<>();
		this.loadedModels = new ArrayList<>();

		this.camera = CustomEntityFactory.getCamera(new Vector3f());

		this.shader = new StandardShader(new LightSource(new Vector3f(), new Vector3f(1.0f, 1.0f, 1.0f), 100f));
		this.shader.compileShaderFromFiles(SHADER_FOLDER + "standard_vertex.glsl",
				SHADER_FOLDER + "standard_fragment.glsl");

		TexturedModel model1 = Engine.getModelManager().loadTexturedModel(MODEL_FOLDER + "dragon.obj");
		TexturedModel model2 = Engine.getModelManager().loadTexturedModel(MODEL_FOLDER + "stall.obj");
		try {
			// TODO: refactor
			model1.setTexture(Engine.getTextureManager().loadTexture(TEX_FOLDER + "dragon.png"));
			model2.setTexture(Engine.getTextureManager().loadTexture(TEX_FOLDER + "stall.png"));
			model1.setShader(shader);
			model2.setShader(shader);
		} catch (IOException e) {
			e.printStackTrace();
			Engine.getLogger().err("could not load textures");
		}
		this.loadedModels.add(model1);
		this.loadedModels.add(model2);

		Random random = new Random();
		for (int i = 0; i < 40; i++) {
			int posX = random.nextInt(200) - 100;
			int posY = random.nextInt(200) - 100;
			int posZ = random.nextInt(150) - 170;

			// Entity e = CustomEntityFactory.getStallEntity(new Vector3f(posX, posY, posZ));
			Entity e = CustomEntityFactory.getDragonEntity(new Vector3f(posX, posY, posZ));
			this.visibleEntities.add(e);
		}
	}

	@Override
	protected void update() {
		this.camera.update();

		for (Entity entity : this.visibleEntities) {
			entity.update();
			Engine.getRenderManager().processEntity(entity);
		}

		// Check whether or not we should quit
		if (this.isQuitRequestedByEngine() || Engine.getInputManager().isKeyDown(InputManager.KEY_ESC))
			this.quit();
	}

	@Override
	protected void cleanup() {
		this.visibleEntities.forEach(e -> e.cleanup());
		this.loadedModels.forEach(m -> m.cleanup());
		this.shader.forceDelete();
		CustomEntityFactory.cleanup();
	}

	@Override
	protected Display setupDisplay() {
		return new Display(SCREEN_WIDTH, SCREEN_HEIGHT, FULLSCREEN, WINDOW_BASETITLE);
	}

	public static void main(String argv[]) {
		Engine.getLogger().setLogLevel(LogLevel.LOG_DEBUG);
		try {
			new UnnamedGame();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
