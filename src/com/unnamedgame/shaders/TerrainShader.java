package com.unnamedgame.shaders;

import com.openglengine.renderer.*;
import com.openglengine.renderer.shader.*;

public class TerrainShader extends Shader {
	/** uniform location of the lights position shader attrib */
	private int location_lightPosition;

	/** uniform location of the lights color shader attrib */
	private int location_lightColor;

	/** uniform location of the shine dampener shader attrib */
	private int location_shineDamper;

	/** uniform location of the reflectivity shader attrib */
	private int location_reflectivity;

	/** uniform location of ambient brightness attrib */
	private int location_ambientBrightness;

	/** uniform location of light source brightness */
	private int location_lightBrightness;

	/** TODO: refactor */
	private LightSource lightSource;

	public TerrainShader(LightSource lightSource) {
		this.lightSource = lightSource;
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();

		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_ambientBrightness = super.getUniformLocation("ambientBrightness");
		location_lightBrightness = super.getUniformLocation("lightBrightness");
	}

	@Override
	public void uploadGlobalUniforms() {
		super.uploadGlobalUniforms();

		super.loadVector3f(location_lightPosition, this.lightSource.getPosition());
		super.loadVector3f(location_lightColor, this.lightSource.getColor());
		super.loadFloat(location_lightBrightness, this.lightSource.getBrightness());
		super.loadFloat(location_ambientBrightness, 1f); // TODO: refactor
	}
}
