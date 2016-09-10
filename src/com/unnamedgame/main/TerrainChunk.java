package com.unnamedgame.main;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.imageio.*;

import com.openglengine.core.*;
import com.openglengine.entitity.*;
import com.openglengine.renderer.*;
import com.openglengine.renderer.material.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;
import com.unnamedgame.models.*;

public class TerrainChunk implements RenderDelegate, ResourceManager {

	private Model model;
	private Vector3f pos;

	private float[][] heights;
	private int[][] colors;

	private Random random = new Random();

	// TODO: refactor
	private List<RenderableEntity> terrainDecoration;

	public TerrainChunk(Shader shader, Material material, float x, float z) {
		this.pos = new Vector3f(x, 0, z);
		this.terrainDecoration = new ArrayList<>();
		this.model = this.generateTerrainChunkModel(UnnamedGame.TERRAIN_FOLDER + "blendmap.png",
				UnnamedGame.TERRAIN_FOLDER + "heightmap.png", shader, material);
		this.generateTerrainDecoration();
	}

	public void update() {
		// TODO: implement decoration frustum culling
		for (RenderableEntity deco : this.terrainDecoration) {
			deco.update();
			Engine.getRenderManager().processRenderableEntity(deco);
		}

		Engine.getRenderManager().processRenderObject(this.model, this);
	}

	/**
	 * Get height of these coordinates in chunk
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public float getHeightAt(float worldX, float worldZ) {
		float terrainX = worldX - this.pos.x;
		float terrainZ = worldZ - this.pos.z;
		float gridSquareSize = Terrain.CHUNK_SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX >= heights.length - 1)
			gridX = heights.length - 2;
		if (gridZ >= heights.length - 1)
			gridZ = heights.length - 2;
		if (gridX < 0)
			gridX = 0;
		if (gridZ < 0)
			gridZ = 0;

		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1 - zCoord)) {
			answer = MathUtils.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			answer = MathUtils.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0),
					new Vector3f(1, heights[gridX + 1][gridZ + 1], 1), new Vector3f(0, heights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		}

		return answer;
	}

	/**
	 * Get color of these coordinates in chunk
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	public Color getColorAt(float worldX, float worldZ) {
		float terrainX = worldX - this.pos.x;
		float terrainZ = worldZ - this.pos.z;
		float gridSquareSize = Terrain.CHUNK_SIZE / ((float) colors.length - 1);
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

		if (gridX >= colors.length - 1)
			gridX = colors.length - 2;
		if (gridZ >= colors.length - 1)
			gridZ = colors.length - 2;
		if (gridX < 0)
			gridX = 0;
		if (gridZ < 0)
			gridZ = 0;

		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		int answer;
		if (xCoord <= (1 - zCoord)) {
			answer = (int) MathUtils.barryCentric(new Vector3f(0, colors[gridX][gridZ], 0),
					new Vector3f(1, colors[gridX + 1][gridZ], 0), new Vector3f(0, colors[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			answer = (int) MathUtils.barryCentric(new Vector3f(1, colors[gridX + 1][gridZ], 0),
					new Vector3f(1, colors[gridX + 1][gridZ + 1], 1), new Vector3f(0, colors[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		}

		return new Color(answer, true);
	}

	private void generateTerrainDecoration() {
		// Place fern using terrain height as indicator
		for (int i = 0; i < 20; i++) {
			Vector3f pos = new Vector3f();
			Color color = null;
			do {
				pos.x = random.nextFloat() * Terrain.CHUNK_SIZE + this.pos.x;
				pos.z = random.nextFloat() * Terrain.CHUNK_SIZE + this.pos.z;
				pos.y = this.getHeightAt(pos.x, pos.z);
				color = this.getColorAt(pos.x, pos.z);
			} while (pos.y > -5f || color.getBlue() > 0 || color.getRed() > 0);
			this.terrainDecoration.add(EntityFactory.getEntityByName(pos, new Vector3f(2, 2, 2), "fern"));
		}

		// Place trees
		for (int i = 0; i < 100; i++) {
			Vector3f pos = new Vector3f();
			Color color = null;
			do {
				pos.x = random.nextFloat() * Terrain.CHUNK_SIZE + this.pos.x;
				pos.z = random.nextFloat() * Terrain.CHUNK_SIZE + this.pos.z;
				pos.y = this.getHeightAt(pos.x, pos.z) - 3f;
				color = this.getColorAt(pos.x, pos.z);
			} while (pos.y > 5f || color.getBlue() > 0 || color.getRed() > 0 || color.getGreen() > 0);
			this.terrainDecoration.add(EntityFactory.getEntityByName(pos, new Vector3f(2, 2, 2), "tree"));
		}

	}

	private Model generateTerrainChunkModel(String blendMapPath, String heightMapPath, Shader shader,
			Material material) {
		BufferedImage heightMap = null;
		try {
			heightMap = ImageIO.read(new File(heightMapPath));
		} catch (IOException e) {
			e.printStackTrace();
			Engine.getLogger().err("Could not load terrain heightmap! " + e);
		}
		BufferedImage blendMap = null;
		try {
			blendMap = ImageIO.read(new File(blendMapPath));
		} catch (IOException e) {
			e.printStackTrace();
			Engine.getLogger().err("Could not load terrain heightmap! " + e);
		}

		assert blendMap.getHeight() == heightMap.getHeight() : "Blendmap and Heightmap must be the same size!";

		int vertexcount = heightMap.getHeight();
		this.colors = new int[vertexcount][vertexcount];
		this.heights = new float[vertexcount][vertexcount];

		int count = vertexcount * vertexcount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (vertexcount - 1) * (vertexcount - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < vertexcount; i++) {
			for (int j = 0; j < vertexcount; j++) {
				// Calculate vertex position
				vertices[vertexPointer * 3] = (float) j / ((float) vertexcount - 1) * Terrain.CHUNK_SIZE;
				float height = getHeight(j, i, heightMap);

				// Parse heights
				this.heights[j][i] = height;

				// Parse colors
				this.colors[j][i] = blendMap.getRGB(j, i);

				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) vertexcount - 1) * Terrain.CHUNK_SIZE;

				// Calculate normals
				Vector3f normal = calculateNormal(j, i, heightMap);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;

				// Calculate tex coord position
				textureCoords[vertexPointer * 2] = (float) j / ((float) vertexcount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) vertexcount - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < vertexcount - 1; gz++) {
			for (int gx = 0; gx < vertexcount - 1; gx++) {
				int topLeft = (gz * vertexcount) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * vertexcount) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}

		return new TerrainChunkModel(Engine.getTextureManager().loadTexture(blendMapPath), shader, material, vertices,
				textureCoords, normals, indices);
	}

	private Vector3f calculateNormal(int x, int z, BufferedImage heightMap) {
		float heightL = getHeight(x - 1, z, heightMap);
		float heightR = getHeight(x + 1, z, heightMap);
		float heightD = getHeight(x, z - 1, heightMap);
		float heightU = getHeight(x, z + 1, heightMap);

		Vector3f normal = new Vector3f(heightL - heightR, 2f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	private float getHeight(int x, int z, BufferedImage heightMap) {
		if (x < 0 || x >= heightMap.getHeight() || z < 0 || z >= heightMap.getHeight())
			return 0;

		float height = heightMap.getRGB(x, z);
		height += Terrain.MAX_PIXEL_COLOR / 2f;
		height /= Terrain.MAX_PIXEL_COLOR / 2f;
		height *= Terrain.MAX_HEIGHT;
		return height;
	}

	@Override
	public void initRendercode() {
		// Set model transform
		TransformationMatrixStack tms = Engine.getModelMatrixStack();
		tms.push();
		tms.translate(new Vector3f(this.pos.x, this.pos.y, this.pos.z));
	}

	@Override
	public void deinitRendercode() {
		Engine.getModelMatrixStack().pop();
	}

	@Override
	public void cleanup() {
		this.model.cleanup();
		this.terrainDecoration.forEach(d -> d.cleanup());
	}
}
