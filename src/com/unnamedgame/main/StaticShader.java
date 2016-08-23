package com.unnamedgame.main;

import com.openglengine.renderer.shader.*;

public class StaticShader extends Shader {
	public StaticShader(String vertexShaderPath, String fragmentShaderPath) {
		super(vertexShaderPath, fragmentShaderPath);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
}
