package com.unnamedgame.main;

import java.io.*;
import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
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

	private List<Entity> terrainDecoration;
	private List<Entity> terrainChunks;

	private Entity camera;
	private Entity player;

	public UnnamedGame() throws IOException {
		super(FOV, NEAR_PLANE, FAR_PLANE);
	}

	@Override
	protected void setup() {
		Engine.getInputManager().setMouseGrabbed(true);

		EntityFactory.load();
		this.terrainDecoration = new ArrayList<>();
		this.terrainChunks = new ArrayList<>();
		this.camera = EntityFactory.getCamera(new Vector3f(0, 3, 0));

		this.setupTerrain();
		Random random = new Random(System.currentTimeMillis());

		// Add 200 ferns
		for (int i = 0; i < 200; i++) {
			float posX = (random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE);
			float posY = 0;
			float posZ = random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE;

			Entity e = EntityFactory.getEntityByName(new Vector3f(posX, posY, posZ), new Vector3f(1, 1, 1), "fern");
			this.terrainDecoration.add(e);
		}

		// Add 300 trees
		for (int i = 0; i < 30; i++) {
			float posX = (random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE);
			float posY = 0;
			float posZ = random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE;

			Entity e = EntityFactory.getEntityByName(new Vector3f(posX, posY, posZ), new Vector3f(10, 10, 10),
					"tree");
			this.terrainDecoration.add(e);
		}

		// Add 400 grass
		for (int i = 0; i < 400; i++) {
			float posX = random.nextFloat() * Terrain.CHUNK_SIZE * 2 - Terrain.CHUNK_SIZE;
			float posY = 0;
			float posZ = random.nextFloat() * Terrain.CHUNK_SIZE * 2 - Terrain.CHUNK_SIZE;

			Entity e = EntityFactory.getEntityByName(new Vector3f(posX, posY, posZ), new Vector3f(2, 2, 2), "grass");
			this.terrainDecoration.add(e);
		}

		// Add player
		this.player = EntityFactory.getPlayerEntity(new Vector3f(0, 0, -20), new Vector3f(1, 1, 1));
	}


	@Override
	protected void update() {
		this.camera.update();
		this.player.update();

		// Add entities
		for (Entity entity : this.terrainDecoration) {
			entity.update();
			Engine.getRenderManager().processEntity(entity);
		}

		// Add terrain
		this.terrainChunks.forEach(e -> Engine.getRenderManager().processEntity(e));

		// Add player
		Engine.getRenderManager().processEntity(this.player);

		// Check whether or not we should quit
		if (this.isQuitRequestedByEngine() || Engine.getInputManager().isKeyDown(InputManager.KEY_ESC))
			this.quit();
	}

	@Override
	protected void cleanup() {
		this.terrainDecoration.forEach(e -> e.cleanup());
		this.terrainChunks.forEach(t -> t.cleanup());
		EntityFactory.cleanup();
	}

	@Override
	protected Display setupDisplay() {
		return new Display(SCREEN_WIDTH, SCREEN_HEIGHT, FULLSCREEN, WINDOW_BASETITLE);
	}

	private void setupTerrain() {
		this.terrainChunks.add(EntityFactory.getTerrainChunk(-1, -1));
		this.terrainChunks.add(EntityFactory.getTerrainChunk(-1, 0));
		this.terrainChunks.add(EntityFactory.getTerrainChunk(0, -1));
		this.terrainChunks.add(EntityFactory.getTerrainChunk(0, 0));
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
