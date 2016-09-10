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
	public static final float NO_TERRAIN_HEIGHT = -2000;
	protected static final float MAX_HEIGHT = 50;
	protected static final float MIN_HEIGHT = -MAX_HEIGHT;
	protected static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	private static final int TERRAIN_WIDTH = 2;
	private static final int TERRAIN_DEPTH = 2;

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
		for (int x = 0; x < TERRAIN_WIDTH; x++)
			for (int z = 0; z < TERRAIN_DEPTH; z++) {
				this.terrainChunks[x][z] = new TerrainChunk(this.terrainShader, this.terrainMaterial,
						(x - getTerrainOffsetX()) * CHUNK_SIZE, (z - getTerrainOffsetZ()) * CHUNK_SIZE);
			}
	}

	private int getTerrainOffsetX() {
		return TERRAIN_WIDTH / 2;
	}

	private int getTerrainOffsetZ() {
		return TERRAIN_DEPTH / 2;
	}

	private void generateDecoration() {
		Random random = new Random(System.currentTimeMillis());

		// Add 200 ferns
		for (int i = 0; i < 200; i++) {
			float posX = (random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE);
			float posZ = random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE;
			float posY = this.getHeightAt(posX, posZ);

			Entity e = EntityFactory.getEntityByName(new Vector3f(posX, posY, posZ), new Vector3f(1, 1, 1), "fern");
			this.terrainDecoration.add(e);
		}


		// Add 200 low poly trees
		for (int i = 0; i < 100; i++) {
			float posX = (random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE);
			float posZ = random.nextInt((int) (Terrain.CHUNK_SIZE * 2)) - Terrain.CHUNK_SIZE;
			float posY = this.getHeightAt(posX, posZ) - 3f;

			Entity e = EntityFactory.getEntityByName(new Vector3f(posX, posY, posZ), new Vector3f(2, 2, 2),
					"lowPolyTree");
			this.terrainDecoration.add(e);
		}

		// // Add 400 grass
		// for (int i = 0; i < 400; i++) {
		// float posX = random.nextFloat() * Terrain.CHUNK_SIZE * 2 - Terrain.CHUNK_SIZE;
		// float posZ = random.nextFloat() * Terrain.CHUNK_SIZE * 2 - Terrain.CHUNK_SIZE;
		// float posY = this.getHeightAt(posX, posZ) - 1;
		//
		// Entity e = EntityFactory.getEntityByName(new Vector3f(posX, posY, posZ), new Vector3f(2, 2, 2), "grass");
		// this.terrainDecoration.add(e);
		// }
	}

	@Override
	public void cleanup() {
		for (int x = 0; x < TERRAIN_WIDTH; x++)
			for (int z = 0; z < TERRAIN_DEPTH; z++)
				this.terrainChunks[x][z].cleanup();

		this.terrainDecoration.forEach(d -> d.cleanup());
		this.terrainShader.forceDelete();
	}

	/**
	 * Get the height of the terrain at a specific coordinate x, z
	 * 
	 * @param x
	 * @param z
	 * @return Height of Terrain or NO_TERRAIN_HEIGHT if out of bounds
	 */
	public float getHeightAt(float x, float z) {
		int chunkX = (int) ((x - CHUNK_SIZE) / CHUNK_SIZE) + getTerrainOffsetX();
		int chunkZ = (int) ((z - CHUNK_SIZE) / CHUNK_SIZE) + getTerrainOffsetZ();

		if (chunkX < 0 || chunkX > TERRAIN_WIDTH || chunkZ < 0 || chunkZ > TERRAIN_DEPTH)
			return NO_TERRAIN_HEIGHT;

		// float posInChunkX = x % CHUNK_SIZE;
		// float posInChunkZ = z % CHUNK_SIZE;
		// if (posInChunkX < 0)
		// posInChunkX = CHUNK_SIZE + posInChunkX;
		// if (posInChunkZ < 0)
		// posInChunkZ = CHUNK_SIZE + posInChunkZ;

		return this.terrainChunks[chunkX][chunkZ].getHeightAt(x, z);
	}
}
