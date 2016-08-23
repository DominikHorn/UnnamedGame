package com.unnamedgame.main;

import com.openglengine.renderer.shader.*;
import com.openglengine.util.math.*;

public class StaticShader extends Shader {
	private int location_lightPosition;
	private int location_lightColor;
	private Light light = new Light(new Vector3f(10, 10, 10), new Vector3f(1, 1, 1));

	public StaticShader(String vertexShaderPath, String fragmentShaderPath) {
		super(vertexShaderPath, fragmentShaderPath);
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();

		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
	}

	@Override
	public void uploadUniforms() {
		super.uploadUniforms();
		
		// Upload light
		super.loadVector3f(location_lightPosition, this.light.getPosition());
		super.loadVector3f(location_lightColor, this.light.getColor());
	}
}
