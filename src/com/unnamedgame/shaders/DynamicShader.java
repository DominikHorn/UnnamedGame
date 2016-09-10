package com.unnamedgame.shaders;

import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.openglengine.util.*;
import com.openglengine.util.math.*;
import com.unnamedgame.materials.*;

public class DynamicShader extends Shader {
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

	/** uniform location of light source brightness */
	private int location_transparent;

	/** uniform location of fake lighting property */
	private int location_useFakeLighting;

	/** uniform location of sky color */
	private int location_skyColor;

	/** TODO: refactor */
	private LightSource lightSource;

	private Vector3f skyColor;

	public DynamicShader(LightSource lightSource, Vector3f skyColor) {
		this.lightSource = lightSource;
		this.skyColor = skyColor;
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();

		this.location_lightPosition = super.getUniformLocation("lightPosition");
		this.location_lightColor = super.getUniformLocation("lightColor");
		this.location_shineDamper = super.getUniformLocation("shineDamper");
		this.location_reflectivity = super.getUniformLocation("reflectivity");
		this.location_ambientBrightness = super.getUniformLocation("ambientBrightness");
		this.location_lightBrightness = super.getUniformLocation("lightBrightness");
		this.location_transparent = super.getUniformLocation("transparent");
		this.location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		this.location_skyColor = super.getUniformLocation("skyColor");
	}

	@Override
	public void uploadGlobalUniforms() {
		super.uploadGlobalUniforms();

		// TODO: refactor sky color
		super.loadVector3f(location_skyColor, this.skyColor);

		// TODO: refactor light stuff
		super.loadVector3f(location_lightPosition, this.lightSource.position);
		super.loadVector3f(location_lightColor, this.lightSource.color);
		super.loadFloat(location_lightBrightness, this.lightSource.brightness);
		super.loadFloat(location_ambientBrightness, 0.2f); // TODO: refactor
	}

	@Override
	public void uploadModelUniforms(Model model) {
		super.uploadModelUniforms(model);

		DynamicMaterial material = (DynamicMaterial) model.getMaterial();

		super.loadFloat(location_shineDamper, material.shineDamper);
		super.loadFloat(location_reflectivity, material.reflectivity);
		super.loadFloat(location_transparent, material.transparent == false ? 0 : 1);
		super.loadFloat(location_useFakeLighting, material.useFakeLighting == false ? 0 : 1);
	}
}