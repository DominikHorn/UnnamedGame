package com.unnamedgame.shaders;

import com.openglengine.renderer.shader.*;
import com.unnamedgame.main.*;

public class DynamicShader extends Shader {
	/** uniform location of the lights position shader attrib */
	public int location_lightPosition;

	/** uniform location of the lights color shader attrib */
	public int location_lightColor;

	/** uniform location of the shine dampener shader attrib */
	public int location_shineDamper;

	/** uniform location of the reflectivity shader attrib */
	public int location_reflectivity;

	/** uniform location of ambient brightness attrib */
	public int location_ambientBrightness;

	/** uniform location of light source brightness */
	public int location_lightBrightness;

	/** uniform location of light source brightness */
	public int location_transparent;

	/** uniform location of fake lighting property */
	public int location_useFakeLighting;

	/** uniform location of sky color */
	public int location_skyColor;

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
		// TODO: refactor sky color
		super.loadVector3f(location_skyColor, UnnamedGame.SUN.color);

		// TODO: refactor light stuff
		super.loadVector3f(location_lightPosition, UnnamedGame.SUN.position);
		super.loadVector3f(location_lightColor, UnnamedGame.SUN.color);
		super.loadFloat(location_lightBrightness, UnnamedGame.SUN.brightness);
		super.loadFloat(location_ambientBrightness, 0.0f); // TODO: refactor
	}
}