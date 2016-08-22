package com.unnamedgame.main;

import com.openglengine.renderer.shader.*;
import com.openglengine.util.math.*;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_SHADER_PATH = "res/shader/vertex.glsl";
	private static final String FRAGMENT_SHADER_PATH = "res/shader/fragment.glsl";

	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;

	public StaticShader() {
		super(VERTEX_SHADER_PATH, FRAGMENT_SHADER_PATH);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		this.location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		this.location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		this.location_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(this.location_transformationMatrix, matrix);
	}

	@Override
	public void loadViewMatrix(Matrix4f matrix) {
		super.loadMatrix(this.location_viewMatrix, matrix);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(this.location_projectionMatrix, matrix);
	}
}
