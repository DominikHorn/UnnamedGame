package com.unnamedgame.main;

import java.io.*;
import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.renderer.model.*;
import com.openglengine.util.math.*;

/**
 * Game entry point
 * 
 * @author Dominik
 *
 */
public class UnnamedGame extends Basic3DGame {
	// TODO: eventually to be moved to a config file
	private static final int SCREEN_WIDTH = 1920;
	private static final int SCREEN_HEIGHT = 1080;
	private static final float FOV = 70f;
	private static final float ASPECT = (float) SCREEN_WIDTH / (float) SCREEN_HEIGHT;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	private static final boolean FULLSCREEN = false;
	private static final String WINDOW_TITLE = "Engine " + Engine.ENGINE_VERSION;

	private List<TexturedModel> loadedModels;
	private List<VisibleEntity> visibleEntities;

	public UnnamedGame() throws IOException {
		super(SCREEN_WIDTH, SCREEN_HEIGHT, FULLSCREEN, WINDOW_TITLE, FOV, ASPECT, NEAR_PLANE, FAR_PLANE);
	}

	@Override
	protected void setup() {
		this.visibleEntities = new ArrayList<>();
		this.loadedModels = new ArrayList<>();

		TexturedModel model1 = Engine.MODEL_MANAGER.getTexturedModel(Engine.MODEL_FOLDER + "dragon.obj");
		TexturedModel model2 = Engine.MODEL_MANAGER.getTexturedModel(Engine.MODEL_FOLDER + "stall.obj");
		try {
			model1.setTexture(Engine.TEXTURE_MANAGER.referenceTexture(Engine.TEX_FOLDER + "dragon.png"));
			model2.setTexture(Engine.TEXTURE_MANAGER.referenceTexture(Engine.TEX_FOLDER + "stall.png"));
		} catch (IOException e) {
			e.printStackTrace();
			Engine.LOGGER.err("could not load textures");
		}
		this.loadedModels.add(model1);
		this.loadedModels.add(model2);

		this.visibleEntities.add(CustomEntityFactory.getStallEntity(new Vector3f(0, -3, -15), 0, 0, 0, 1));
		this.visibleEntities.add(CustomEntityFactory.getDragonEntity(new Vector3f(0, 3, -15), 0, 0, 0, 1));
	}

	@Override
	protected void update() {
		for (VisibleEntity entity : this.visibleEntities) {
			entity.update();
			Engine.RENDERER.processEntity(entity);
		}

	}

	@Override
	protected void cleanup() {
		this.visibleEntities.forEach(e -> e.cleanup());
		this.loadedModels.forEach(m -> m.cleanup());
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
