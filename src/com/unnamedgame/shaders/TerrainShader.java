package com.unnamedgame.shaders;

import com.openglengine.renderer.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.math.*;
import com.unnamedgame.materials.*;

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

	/** uniform location of sky color */
	private int location_skyColor;

	/** uniform location of sky color */
	private int location_backgroundTexture;

	/** uniform location of sky color */
	private int location_rTexture;

	/** uniform location of sky color */
	private int location_gTexture;

	/** uniform location of sky color */
	private int location_bTexture;

	/** uniform location of sky color */
	private int location_blendMap;

	/** TODO: refactor */
	private LightSource lightSource;

	private Vector3f skyColor;

	public TerrainShader(LightSource lightSource, Vector3f skyColor) {
		this.lightSource = lightSource;
		this.skyColor = skyColor;
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
		location_skyColor = super.getUniformLocation("skyColor");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
	}

	@Override
	public void uploadGlobalUniforms() {
		super.uploadGlobalUniforms();

		// TODO: refactor sky color
		super.loadVector3f(location_skyColor, this.skyColor);

		// TODO: refactor lighting
		super.loadVector3f(location_lightPosition, this.lightSource.position);
		super.loadVector3f(location_lightColor, this.lightSource.color);
		super.loadFloat(location_lightBrightness, this.lightSource.brightness);
		super.loadFloat(location_ambientBrightness, 0.0f); // TODO: refactor
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}

	@Override
	public void uploadModelUniforms(Model model) {
		super.uploadModelUniforms(model);
		TerrainMaterial material = (TerrainMaterial) model.getMaterial();

		Float shineDamper = (Float) material.getPropertyValue(TerrainMaterial.PROPERTY_SHINE_DAMPER);
		Float reflectivity = (Float) material.getPropertyValue(TerrainMaterial.PROPERTY_REFLECTIVITY);

		super.loadFloat(location_shineDamper, shineDamper);
		super.loadFloat(location_reflectivity, reflectivity);
	}
}
