package com.unnamedgame.models;

import java.nio.*;
import java.util.*;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.renderer.material.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.texture.*;
import com.unnamedgame.main.*;
import com.unnamedgame.shaders.*;

public class TerrainChunkModel extends Model<TerrainShader> {
	/** Internal list used to keep track of all the vbos that were create for this model */
	private List<Integer> vbos;

	private Texture backgroundTexture = null;
	private Texture rTexture = null;
	private Texture gTexture = null;
	private Texture bTexture = null;
	private Texture blendMap = null;

	public TerrainChunkModel(Texture blendMap, TerrainShader shader, Material<TerrainShader> material,
			float[] positions, float[] texCoords, float[] normals, int[] indices) {
		// Initialize to 0
		super(indices.length, shader, material);

		// Initialize attributes
		this.vbos = new ArrayList<>();

		this.backgroundTexture = Engine.getTextureManager().loadTexture(UnnamedGame.TEX_FOLDER + "terrain.png");
		this.rTexture = Engine.getTextureManager().loadTexture(UnnamedGame.TEX_FOLDER + "snow.png");
		this.gTexture = Engine.getTextureManager().loadTexture(UnnamedGame.TEX_FOLDER + "flowers.png");
		this.bTexture = Engine.getTextureManager().loadTexture(UnnamedGame.TEX_FOLDER + "path.png");
		this.blendMap = blendMap;

		// Load actual values
		this.loadToVAO(positions, texCoords, normals, indices);
	}

	@Override
	public void initRendercode() {
		// Bind vao for use
		GL30.glBindVertexArray(this.getVaoID());

		// Enable first vertex attrib array (Vertex data)
		GL20.glEnableVertexAttribArray(0);

		// Enable vertex attrib array 1 (Texture data)
		GL20.glEnableVertexAttribArray(1);

		// Enable vertex attrib array 2 (Normal data)
		GL20.glEnableVertexAttribArray(2);

		// Bind all textures
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.backgroundTexture.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.rTexture.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.gTexture.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.bTexture.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.blendMap.getTextureID());
	}

	@Override
	public void deinitRendercode() {
		// Disable first vertex attrib array (Vertex data)
		GL20.glDisableVertexAttribArray(0);

		// Disable vertex attrib array 1 (Texture data)
		GL20.glDisableVertexAttribArray(1);

		// Disable vertex attrib array 2 (Normal data)
		GL20.glDisableVertexAttribArray(2);

		// Unbind vao
		GL30.glBindVertexArray(0);
	}

	@Override
	protected void forceDelete() {
		super.forceDelete();

		// We own this -> force delete
		this.vbos.forEach(vbo -> GL15.glDeleteBuffers(vbo));

		// We don't own this, hence only remove reference
		this.backgroundTexture.cleanup();
		this.rTexture.cleanup();
		this.gTexture.cleanup();
		this.bTexture.cleanup();
		this.blendMap.cleanup();
	}

	/**
	 * Load data to vao
	 * 
	 * @param vertices
	 * @param texCoords
	 * @param indices
	 */
	private void loadToVAO(float[] vertices, float[] texCoords, float[] normals, int[] indices) {
		// Create new VAO
		this.setVaoID(GL30.glGenVertexArrays());

		// Bind that VAO for modification
		GL30.glBindVertexArray(this.getVaoID());

		// Create indices VBO
		int indicesID = GL15.glGenBuffers();

		// Add to list for destruction upon force delete
		this.vbos.add(indicesID);

		// Bind Indices buffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesID);

		// Convert indices data to appropriate format
		IntBuffer buffer = convertToIntBuffer(indices);

		// Upload buffer data
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

		// Store vertex data in attribute list slot 0
		this.storeDataInAttributeList(0, 3, vertices);

		// Store tex data in attribute list slot 1
		this.storeDataInAttributeList(1, 2, texCoords);

		// Store normal data in attribute list slot 2
		this.storeDataInAttributeList(2, 3, normals);

		// Unbind VAO
		GL30.glBindVertexArray(0);
	}

	/**
	 * Convert int array to IntBuffer
	 * 
	 * @param data
	 * @return
	 */
	private IntBuffer convertToIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}

	/**
	 * Convert float array to FloatBuffer
	 * 
	 * @param data
	 * @return
	 */
	private FloatBuffer convertToFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		return buffer;
	}

	/**
	 * Upload float data to a certain attributeList slot
	 * 
	 * @param attributeNumber
	 * @param coordinateSize
	 * @param data
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		// Gen vbo for data
		int vboID = GL15.glGenBuffers();

		// Add to autodestruct list
		this.vbos.add(vboID);

		// Bind for use
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);

		// Convert to correct data format
		FloatBuffer buffer = convertToFloatBuffer(data);

		// Upload to vbo
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

		// Tell opengl how to interpret this attribute
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);

		// Unbind vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
}
