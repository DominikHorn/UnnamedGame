package com.unnamedgame.main;

import java.io.*;
import java.util.*;

import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.util.Logger.*;
import com.openglengine.util.light.*;
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

	/** maximum lights for this shader */
	public static final int MAX_LIGHTS = 4;

	public static List<LightSource> POINT_LIGHTS;
	public static SpotLightSource SPOTLIGHT;
	public static Vector3f SKY_COLOR;
	public static Terrain TERRAIN;

	/** TODO: tmp */
	public static float AMBIENT = 0.05f;
	public static float DENSITY = 0.005f;

	private RenderableEntity<?> player;

	// TODO: tmp debug
	private boolean ambientChangeable = true;
	private boolean wireframe = false;

	public UnnamedGame() throws IOException {
		super(FOV, NEAR_PLANE, FAR_PLANE);
	}

	@Override
	protected void setup() {
		// Must be setup before entity factory
		SKY_COLOR = new Vector3f(0f, 0f, 0f);
		POINT_LIGHTS = new ArrayList<>();

		// Load models
		EntityFactory.load();

		// Setup globals
		TERRAIN = new Terrain();
		// POINT_LIGHTS
		// .add(new LightSource(new Vector3f(0, 20, 0), new Vector3f(1, 1, 1),
		// new Vector3f(1f, 0f, 0f)));
		// POINT_LIGHTS.add(
		// new LightSource(new Vector3f(-100, 20, 0), new Vector3f(1, 1, 1), new Vector3f(0.5f, 0.01f, 0.0002f)));
		// POINT_LIGHTS.add(
		// new LightSource(new Vector3f(0, 20, 100), new Vector3f(1, 1, 1), new Vector3f(0.5f, 0.01f, 0.0002f)));
		// POINT_LIGHTS.add(
		// new LightSource(new Vector3f(0, 20, -100), new Vector3f(1, 1, 1), new Vector3f(0.5f, 0.01f, 0.0002f)));
		SPOTLIGHT = new SpotLightSource(new Vector3f(0, 5f, 0), new Vector3f(1, 0.9f, 0.7f),
				new Vector3f(1f, 0.01f, 0.0002f), new Vector3f(0f, -1f, 0f), (float) Math.cos(Math.toRadians(180)));

		// Don't grab the mouse (until we have a fps camera)
		Engine.getInputManager().setMouseGrabbed(false);

		// Create player entity
		this.player = EntityFactory.getPlayerEntity(new Vector3f(0, 0, 0), new Vector3f(1f, 1f, 1f));

		// Setup gui
		// Engine.getGuiManager().addGuiElement(
		// new GuiElement(Engine.getTextureManager().loadTexture(TEX_FOLDER + "tmp.png"), new Vector2f(0.5f, 0.5f),
		// new Vector2f(0.25f, 0.25f)));
	}

	@Override
	protected void update(double deltatime) {
		/** TODO tmp debug stuff */
		if (Engine.getInputManager().wasKeyPressed(InputManager.KEY_F1)) {
			this.wireframe = !this.wireframe;

			if (this.wireframe)
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			else
				GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
		if (Engine.getInputManager().isKeyDown(InputManager.KEY_F2)) {
			if (Engine.getInputManager().wasKeyPressed(InputManager.KEY_PLUS) && this.ambientChangeable) {
				AMBIENT += 0.1f;
				this.ambientChangeable = false;
			} else if (Engine.getInputManager().wasKeyPressed(InputManager.KEY_MINUS) && this.ambientChangeable) {
				AMBIENT -= 0.1f;
				this.ambientChangeable = false;
			} else {
				this.ambientChangeable = true;
			}

			AMBIENT = MathUtils.clamp(AMBIENT, 0.0f, 1.0f);
		}
		if (Engine.getInputManager().isKeyDown(InputManager.KEY_F3)) {
			if (Engine.getInputManager().isKeyDown(InputManager.KEY_PLUS)) {
				DENSITY += 0.0001f;
			}
			if (Engine.getInputManager().isKeyDown(InputManager.KEY_MINUS)) {
				DENSITY -= 0.0001f;
			}
		}
		// TODO: use delta time

		// Update terrain
		TERRAIN.update();

		// Update player
		this.player.update();

		// Add player to rendering queue
		Engine.getRenderManager().processRenderObject(this.player.getModel(), this.player);

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
