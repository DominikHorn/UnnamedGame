package com.unnamedgame.main;

import java.io.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.util.*;
import com.openglengine.util.Logger.*;
import com.openglengine.util.math.*;
import com.unnamedgame.terrain.*;

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

	public static LightSource SUN;
	public static Vector3f SKY_COLOR;
	public static Terrain TERRAIN;

	private RenderableEntity player;

	// TODO: tmp debug
	private boolean wireframe = false;

	public UnnamedGame() throws IOException {
		super(FOV, NEAR_PLANE, FAR_PLANE);
	}

	@Override
	protected void setup() {
		// Must be setup before entity factory
		SKY_COLOR = new Vector3f(1f, 1f, 1f);

//		 Setup sun
		SUN = new Sun(0.1);

		// Load models
		EntityFactory.load();

		// Setup globals
		TERRAIN = new Terrain();

		// Don't grab the mouse (until we have a fps camera)
		Engine.getInputManager().setMouseGrabbed(false);

		// Create player entity
		this.player = EntityFactory.getPlayerEntity(new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f));
	}

	@Override
	protected void update() {
		// TODO: tmp debug stuff
		if (Engine.getInputManager().wasKeyPressed(InputManager.KEY_F1)) {
			this.wireframe = !this.wireframe;

			if (this.wireframe)
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			else
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}

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
