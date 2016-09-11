package com.unnamedgame.terrain;

import java.util.*;

import com.openglengine.renderer.material.*;
import com.openglengine.util.*;
import com.unnamedgame.main.*;
import com.unnamedgame.materials.*;
import com.unnamedgame.shaders.*;

public class Terrain implements ResourceManager {
	public static final float CHUNK_SIZE = 2000;
	public static final float NO_TERRAIN_HEIGHT = -2000;
	protected static final float MAX_HEIGHT = 300;
	protected static final float MIN_HEIGHT = -MAX_HEIGHT;
	protected static final float MAX_PIXEL_COLOR = 256 * 256 * 256;
	private static final int TERRAIN_WIDTH = 2;
	private static final int TERRAIN_DEPTH = 2;

	// TODO: refactor
	private TerrainShader terrainShader;
	private Material<TerrainShader> terrainMaterial;

	private TerrainChunk[][] terrainChunks;

	private List<TerrainChunk> visibleChunks;

	public Terrain() {
		this.terrainShader = new TerrainShader(UnnamedGame.SKY_COLOR);
		this.terrainShader.compileShaderFromFiles(UnnamedGame.SHADER_FOLDER + "terrain_vertex.glsl",
				UnnamedGame.SHADER_FOLDER + "terrain_fragment.glsl");
		this.terrainMaterial = new TerrainMaterial();

		this.terrainChunks = new TerrainChunk[TERRAIN_WIDTH][TERRAIN_DEPTH];
		this.visibleChunks = new ArrayList<>();

		this.generateTerrain();
	}

	public void update() {
		this.getVisibleChunks().forEach(c -> c.update());

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

	@Override
	public void cleanup() {
		for (int x = 0; x < TERRAIN_WIDTH; x++)
			for (int z = 0; z < TERRAIN_DEPTH; z++)
				this.terrainChunks[x][z].cleanup();

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

		if (chunkX < 0 || chunkX >= TERRAIN_WIDTH || chunkZ < 0 || chunkZ >= TERRAIN_DEPTH)
			return NO_TERRAIN_HEIGHT;

		return this.terrainChunks[chunkX][chunkZ].getHeightAt(x, z);
	}
}
