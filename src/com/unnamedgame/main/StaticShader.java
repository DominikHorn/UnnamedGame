package com.unnamedgame.main;

import com.openglengine.renderer.shader.*;
import com.openglengine.util.math.*;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_SHADER_PATH = "res/shader/vertex.glsl";
	private static final String FRAGMENT_SHADER_PATH = "res/shader/fragment.glsl";

	private int location_transformationMatrix;

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
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(this.location_transformationMatrix, matrix);
	}

}
