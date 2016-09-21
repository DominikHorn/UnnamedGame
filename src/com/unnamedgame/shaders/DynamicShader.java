package com.unnamedgame.shaders;

import com.openglengine.renderer.shader.*;
import com.openglengine.util.math.*;
import com.unnamedgame.main.*;

public class DynamicShader extends Shader {
	/** uniform location of the lights position shader attrib */
	public int[] location_lightPositions;

	/** uniform location of the lights color shader attrib */
	public int[] location_lightColors;

	/** uniform location of the lights brightness shader attrib */
	public int[] location_lightBrightness;

	/** uniform location of the shine dampener shader attrib */
	public int location_shineDamper;

	/** uniform location of the reflectivity shader attrib */
	public int location_reflectivity;

	/** uniform location of light source brightness */
	public int location_transparent;

	/** uniform location of fake lighting property */
	public int location_useFakeLighting;

	/** uniform location of sky color */
	public int location_skyColor;

	public DynamicShader() {
		this.compileShaderFromFiles(UnnamedGame.SHADER_FOLDER + "standard_vertex.glsl",
				UnnamedGame.SHADER_FOLDER + "standard_fragment.glsl");
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();

		this.location_shineDamper = super.getUniformLocation("shineDamper");
		this.location_reflectivity = super.getUniformLocation("reflectivity");
		this.location_transparent = super.getUniformLocation("transparent");
		this.location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		this.location_skyColor = super.getUniformLocation("skyColor");

		// Get array uniforms
		this.location_lightPositions = new int[UnnamedGame.MAX_LIGHTS];
		this.location_lightColors = new int[UnnamedGame.MAX_LIGHTS];
		this.location_lightBrightness = new int[UnnamedGame.MAX_LIGHTS];

		for (int i = 0; i < UnnamedGame.MAX_LIGHTS; i++) {
			this.location_lightPositions[i] = super.getUniformLocation("lightPositions[" + i + "]");
			this.location_lightColors[i] = super.getUniformLocation("lightColors[" + i + "]");
			this.location_lightBrightness[i] = super.getUniformLocation("lightBrightness[" + i + "]");
		}
	}

	@Override
	public void uploadGlobalUniforms() {
		// TODO: refactor sky color
		super.loadVector3f(location_skyColor, UnnamedGame.SKY_COLOR);

		// TODO: refactor light stuff
		for (int i = 0; i < UnnamedGame.MAX_LIGHTS; i++) {
			if (i < UnnamedGame.LIGHTS.size()) {
				super.loadVector3f(this.location_lightPositions[i], UnnamedGame.LIGHTS.get(i).position);
				super.loadVector3f(this.location_lightColors[i], UnnamedGame.LIGHTS.get(i).color);
				super.loadFloat(this.location_lightBrightness[i], UnnamedGame.LIGHTS.get(i).brightness);
			} else {
				super.loadVector3f(this.location_lightPositions[i], new Vector3f());
				super.loadVector3f(this.location_lightColors[i], new Vector3f());
				super.loadFloat(this.location_lightBrightness[i], 0f);
			}
		}
	}
}