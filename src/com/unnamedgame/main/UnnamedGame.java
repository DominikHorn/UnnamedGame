package com.unnamedgame.main;

import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
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

	private List<Entity> entities;

	public UnnamedGame() {
		super(SCREEN_WIDTH, SCREEN_HEIGHT, FULLSCREEN, WINDOW_TITLE, FOV, ASPECT, NEAR_PLANE, FAR_PLANE);
	}

	@Override
	protected void setup() {
		this.entities = new ArrayList<>();
		this.entities.add(CustomEntityFactory.getStallEntity(new Vector3f(0, -3, -15), 0, 0, 0, 1));
		this.entities.add(CustomEntityFactory.getDragonEntity(new Vector3f(0, 3, -15), 0, 0, 0, 1));
	}

	@Override
	protected void update() {
		for (Entity entity : this.entities) {
			entity.update();
		}
	}

	public static void main(String argv[]) {
		UnnamedGame game = new UnnamedGame();
		game.cleanup();
		Engine.cleanup();
	}

	@Override
	protected void cleanup() {
		this.entities.forEach(e -> e.cleanup());
	}

}
