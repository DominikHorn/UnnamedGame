package com.unnamedgame.main;

import com.openglengine.renderer.shader.*;
import com.openglengine.util.math.*;

public class StaticShader extends Shader {
	// TODO: tmp
	private Light light = new Light(new Vector3f(10, 10, 10), new Vector3f(1, 1, 1));

	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;

	private float shineDamper;
	private float reflectivity;

	public StaticShader(String vertexShaderPath, String fragmentShaderPath, float shineDamper, float reflectivity) {
		super(vertexShaderPath, fragmentShaderPath);

		this.shineDamper = shineDamper;
		this.reflectivity = reflectivity;
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();

		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
	}

	@Override
	public void uploadUniforms() {
		super.uploadUniforms();

		// Upload diffuse light data
		super.loadVector3f(location_lightPosition, this.light.getPosition());
		super.loadVector3f(location_lightColor, this.light.getColor());

		// Upload specular data
		super.loadFloat(location_shineDamper, this.shineDamper);
		super.loadFloat(location_reflectivity, this.reflectivity);
	}
}
