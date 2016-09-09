package com.unnamedgame.main;

import java.util.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.renderer.material.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;
import com.unnamedgame.materials.*;
import com.unnamedgame.shaders.*;

public class Terrain implements ResourceManager {
	public static final float CHUNK_SIZE = 800;
	public static final float MAX_HEIGHT = 80;
	public static final float MIN_HEIGHT = -MAX_HEIGHT;
	public static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	public static final int TERRAIN_WIDTH = 2;
	public static final int TERRAIN_DEPTH = 2;

	// TODO: refactor
	private TerrainShader terrainShader;
	private Material terrainMaterial;

	private TerrainChunk[][] terrainChunks;

	// TODO: refactor
	private List<Entity> terrainDecoration;

	private List<TerrainChunk> visibleChunks;

	public Terrain() {
		this.terrainShader = new TerrainShader(UnnamedGame.LIGHT_SOURCE, UnnamedGame.SKY_COLOR);
		this.terrainShader.compileShaderFromFiles(UnnamedGame.SHADER_FOLDER + "terrain_vertex.glsl",
				UnnamedGame.SHADER_FOLDER + "terrain_fragment.glsl");
		this.terrainMaterial = new TerrainMaterial(0, 1);

		this.terrainChunks = new TerrainChunk[TERRAIN_WIDTH][TERRAIN_DEPTH];
		this.terrainDecoration = new ArrayList<>();
		this.visibleChunks = new ArrayList<>();

		this.generateTerrain();
		this.generateDecoration();
	}

	public void update() {
		for (Entity entity : this.terrainDecoration) {
			entity.update();

			// TODO: implement view frustum culling
			Engine.getRenderManager().processRenderObject(entity.model, entity);
		}

		this.getVisibleChunks().forEach(c -> Engine.getRenderManager().processRenderObject(c.getModel(), c));
	}

	private List<TerrainChunk> getVisibleChunks() {
		this.visibleChunks.clear();

		// TODO: implement propper frustum culling
		for (int x = 0; x < TERRAIN_WIDTH; x++)
			for (int z = 0; z < TERRAIN_DEPTH; z++)
				this.visibleChunks.add(this.terrainChunks[x][z]);

		return this.visibleChunks;
	}

	private void generateTerrain() {
		int terrainPositionOffsetX = TERRAIN_WIDTH / 2;
		int terrainPositionOffsetZ = TERRAIN_DEPTH / 2;

		for (int x = 0; x < TERRAIN_WIDTH; x++)
			for (int z = 0; z < TERRAIN_DEPTH; z++) {
				this.terrainChunks[x][z] = new TerrainChunk(this.terrainShader, this.terrainMaterial,
						(x - terrainPositionOffsetX) * CHUNK_SIZE,
						(z - terrainPositionOffsetZ) * CHUNK_SIZE);
			}
	}

	private void generateDecoration() {
		Random random = new Random(System.currentTimeMillis());

		// Add 200 ferns
		for (int i = 0; i < 200; i++) {
			float posX = (random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE);
			float posY = 0;
			float posZ = random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE;

			Entity e = EntityFactory.getEntityByName(new Vector3f(posX, posY, posZ), new Vector3f(1, 1, 1), "fern");
			this.terrainDecoration.add(e);
		}

		// Add 50 trees
		for (int i = 0; i < 50; i++) {
			float posX = (random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE);
			float posY = 0;
			float posZ = random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE;

			Entity e = EntityFactory.getEntityByName(new Vector3f(posX, posY, posZ), new Vector3f(10, 10, 10), "tree");
			this.terrainDecoration.add(e);
		}

		// Add 100 low poly trees
		for (int i = 0; i < 50; i++) {
			float posX = (random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE);
			float posY = 0;
			float posZ = random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE;

			Entity e = EntityFactory.getEntityByName(new Vector3f(posX, posY, posZ), new Vector3f(2, 2, 2),
					"lowPolyTree");
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
	}

	@Override
	public void cleanup() {
		for (int x = 0; x < TERRAIN_WIDTH; x++)
			for (int z = 0; z < TERRAIN_DEPTH; z++)
				this.terrainChunks[x][z].cleanup();

		this.terrainDecoration.forEach(d -> d.cleanup());
		this.terrainShader.forceDelete();
	}

	public float getHeightAt(float x, float z) {
		return 0;
	}
}
