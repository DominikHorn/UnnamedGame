package com.unnamedgame.models;

import java.io.*;
import java.nio.*;
import java.util.*;

import org.lwjgl.*;
import org.lwjgl.opengl.*;

import com.openglengine.core.*;
import com.openglengine.renderer.material.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.renderer.texture.*;

/**
 * Container class for static models that will never change
 * 
 * @author Dominik
 *
 */
public class SimpleTexturedModel extends Model {
	/** Internal list used to keep track of all the vbos that were create for this model */
	private List<Integer> vbos;

	/** Texture used when rendering this model */
	private Texture texture = null;

	public SimpleTexturedModel(String texturePath, Shader shader, Material material, float[] positions,
			float[] texCoords, float[] normals,
			int[] indices) {
		// Initialize to 0
		super(0, indices.length, shader, material);

		// Initialize attributes
		this.vbos = new ArrayList<>();
		this.texture = Engine.getTextureManager().loadTexture(texturePath);

		// Load actual values
		this.loadToVAO(positions, texCoords, normals, indices);
	}

	/**
	 * Set texture for this model
	 * 
	 * @param texPath
	 * @throws IOException
	 */
	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	/**
	 * Retrieve texture from this model
	 * 
	 * @return
	 */
	public Texture getTexture() {
		return this.texture;
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

		// Enable texture 0 TODO: potential overhead
		GL13.glActiveTexture(GL13.GL_TEXTURE0);

		// Bind our texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.texture.getTextureID());
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
		this.texture.cleanup();
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
