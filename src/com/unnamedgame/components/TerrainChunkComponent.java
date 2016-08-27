package com.unnamedgame.components;

import com.openglengine.entitity.*;
import com.openglengine.entitity.component.*;
import com.openglengine.eventsystem.defaultevents.*;
import com.openglengine.renderer.model.*;
import com.unnamedgame.main.*;

public class TerrainChunkComponent extends Component {
	private int gridX;
	private int gridZ;

	public TerrainChunkComponent(int gridX, int gridZ) {
		this.gridX = gridX;
		this.gridZ = gridZ;
	}


	@Override
	public void init(Entity entity) {
		entity.putProperty(DefaultEntityProperties.PROPERTY_MODEL, generateTerrain());
	}

	@Override
	public void update(Entity entity) {
		// Do nothing
	}

	@Override
	public void receiveEvent(BaseEvent event) {
		// TODO: receive event
	}

	@Override
	public void cleanup(Entity entity) {
		((Model) entity.getValueProperty(DefaultEntityProperties.PROPERTY_MODEL)).cleanup();
	}

	private TexturedModel generateTerrain() {
		int count = Terrain.VERTEX_COUNT * Terrain.VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (Terrain.VERTEX_COUNT - 1) * (Terrain.VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < Terrain.VERTEX_COUNT; i++) {
			for (int j = 0; j < Terrain.VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) Terrain.VERTEX_COUNT - 1) * Terrain.CHUNK_SIZE;
				vertices[vertexPointer * 3 + 1] = 0;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) Terrain.VERTEX_COUNT - 1) * Terrain.CHUNK_SIZE;
				normals[vertexPointer * 3] = 0;
				normals[vertexPointer * 3 + 1] = 1;
				normals[vertexPointer * 3 + 2] = 0;
				textureCoords[vertexPointer * 2] = (float) j / ((float) Terrain.VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) Terrain.VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < Terrain.VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < Terrain.VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * Terrain.VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * Terrain.VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return new TexturedModel(vertices, textureCoords, normals, indices);
	}
}
