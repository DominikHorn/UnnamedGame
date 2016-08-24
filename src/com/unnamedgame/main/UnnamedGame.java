package com.unnamedgame.main;

import java.io.*;
import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.renderer.model.*;
import com.openglengine.util.Logger.*;
import com.openglengine.util.math.*;

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
	private List<VisibleEntity> visibleEntities;

	private Display gameDisplay;

	private StaticShader shader;

	public UnnamedGame() throws IOException {
		super(FOV, NEAR_PLANE, FAR_PLANE);

		this.gameDisplay = new Display(SCREEN_WIDTH, SCREEN_HEIGHT, FULLSCREEN, WINDOW_BASETITLE);
		this.gameDisplay.create();
	}

	@Override
	protected void setup() {
		Engine.getLogger().setLogLevel(LogLevel.LOG_DEBUG);
		this.visibleEntities = new ArrayList<>();
		this.loadedModels = new ArrayList<>();

		this.shader = new StaticShader();
		this.shader.compileShaderFromFiles(SHADER_FOLDER + "vertex.glsl", SHADER_FOLDER + "fragment.glsl");

		TexturedModel model1 = Engine.getModelManager().getTexturedModel(MODEL_FOLDER + "dragon.obj");
		TexturedModel model2 = Engine.getModelManager().getTexturedModel(MODEL_FOLDER + "stall.obj");
		try {
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
		long numIndices = 0;
		for (int i = 0; i < 40; i++) {
			int posX = random.nextInt(400) - 200;
			int posY = random.nextInt(400) - 200;
			int posZ = random.nextInt(300) - 320;
			// VisibleEntity e = CustomEntityFactory.getStallEntity(new Vector3f(posX, posY, posZ), 0, 0, 0, 1);
			VisibleEntity e = CustomEntityFactory.getDragonEntity(new Vector3f(posX, posY, posZ), 0, 0, 0, 1);
			this.visibleEntities.add(e);
			numIndices += e.getModel().getIndicesCount();
		}

		Engine.getLogger().info(numIndices + " indicies are rendered each frame");
	}

	@Override
	protected void update() {
		for (VisibleEntity entity : this.visibleEntities) {
			entity.update();
			Engine.getRenderManager().processEntity(entity);
		}

	}

	@Override
	protected void cleanup() {
		this.visibleEntities.forEach(e -> e.cleanup());
		this.loadedModels.forEach(m -> m.cleanup());
		this.shader.forceDelete();
	}

	@Override
	protected Display getGameDisplay() {
		return this.gameDisplay;
	}

	public static void main(String argv[]) {
		UnnamedGame game = null;
		try {
			game = new UnnamedGame();
		} catch (IOException e) {
			e.printStackTrace();
		}
		game.cleanup();
		Engine.cleanup();
	}
}
