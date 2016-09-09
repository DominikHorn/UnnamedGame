package com.unnamedgame.main;

import java.io.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.renderer.*;
import com.openglengine.util.Logger.*;
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
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	private static final boolean FULLSCREEN = false;
	private static final String WINDOW_BASETITLE = "Engine " + Engine.ENGINE_VERSION;

	public static String RES_FOLDER = "res/";
	public static String MODEL_FOLDER = RES_FOLDER + "model/";
	public static String TEX_FOLDER = RES_FOLDER + "tex/";
	public static String SHADER_FOLDER = RES_FOLDER + "shader/";
	public static String TERRAIN_FOLDER = RES_FOLDER + "terrain/";

	public static LightSource LIGHT_SOURCE = new LightSource(new Vector3f(0, 500f, 0), new Vector3f(1.0f, 1.0f, 1.0f),
			0f);
	// public static Vector3f SKY_COLOR = new Vector3f(0.478f, 1f, 0.952f);
	public static Vector3f SKY_COLOR = new Vector3f(1f, 1f, 1f);
	public static Terrain TERRAIN;

	private Entity player;

	public UnnamedGame() throws IOException {
		super(FOV, NEAR_PLANE, FAR_PLANE);
	}

	@Override
	protected void setup() {
		EntityFactory.load();

		Engine.getInputManager().setMouseGrabbed(false);

		TERRAIN = new Terrain();
		this.player = EntityFactory.getPlayerEntity(new Vector3f(0, 0, -20), new Vector3f(0.5f, 0.5f, 0.5f));
	}


	@Override
	protected void update() {
		// Update terrain
		TERRAIN.update();

		// Update player
		this.player.update();

		// Add player to rendering queue
		Engine.getRenderManager().processRenderObject(this.player.model, this.player);

		// Check whether or not we should quit
		if (this.isQuitRequestedByEngine() || Engine.getInputManager().isKeyDown(InputManager.KEY_ESC))
			this.quit();
	}

	@Override
	protected void cleanup() {
		TERRAIN.cleanup();
		EntityFactory.cleanup();
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
