package com.unnamedgame.shaders;

import com.openglengine.renderer.shader.*;
import com.openglengine.util.math.*;
import com.unnamedgame.main.*;

public class TerrainShader extends Shader {

	/** uniform location of lights stuff */
	public int[] location_lightPositions;
	public int[] location_lightColors;
	public int[] location_lightAttenuations;

	/** uniform locations spotlight stuff */
	public int location_spotLightPosition;
	public int location_spotLightAttenuation;
	public int location_spotLightDirection;
	public int location_spotLightColor;
	public int location_spotCosCutoff;

	/** uniform location of sky color */
	public int location_skyColor;

	/** uniform location of sky color */
	public int location_backgroundTexture;

	/** uniform location of sky color */
	public int location_rTexture;

	/** uniform location of sky color */
	public int location_gTexture;

	/** uniform location of sky color */
	public int location_bTexture;

	/** uniform location of sky color */
	public int location_blendMap;

	public TerrainShader() {
		this.compileShaderFromFiles(UnnamedGame.SHADER_FOLDER + "terrain_vertex.glsl",
				UnnamedGame.SHADER_FOLDER + "terrain_fragment.glsl");
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();

		this.location_skyColor = super.getUniformLocation("skyColor");
		this.location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		this.location_rTexture = super.getUniformLocation("rTexture");
		this.location_gTexture = super.getUniformLocation("gTexture");
		this.location_bTexture = super.getUniformLocation("bTexture");
		this.location_blendMap = super.getUniformLocation("blendMap");
		this.location_spotLightPosition = super.getUniformLocation("spotLightPosition");
		this.location_spotLightAttenuation = super.getUniformLocation("spotLightAttenuation");
		this.location_spotLightDirection = super.getUniformLocation("spotLightDirection");
		this.location_spotLightColor = super.getUniformLocation("spotLightColor");
		this.location_spotCosCutoff = super.getUniformLocation("spotCosCutoff");

		// Get array uniforms
		this.location_lightPositions = new int[UnnamedGame.MAX_LIGHTS];
		this.location_lightColors = new int[UnnamedGame.MAX_LIGHTS];
		this.location_lightAttenuations = new int[UnnamedGame.MAX_LIGHTS];

		for (int i = 0; i < UnnamedGame.MAX_LIGHTS; i++) {
			this.location_lightPositions[i] = super.getUniformLocation("lightPositions[" + i + "]");
			this.location_lightColors[i] = super.getUniformLocation("lightColors[" + i + "]");
			this.location_lightAttenuations[i] = super.getUniformLocation("lightAttenuations[" + i + "]");
		}
	}

	@Override
	public void uploadGlobalUniforms() {
		// TODO: refactor sky color
		super.loadVector3f(location_skyColor, UnnamedGame.SKY_COLOR);

		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);

		super.loadVector3f(this.location_spotLightPosition, UnnamedGame.SPOTLIGHT.position);
		super.loadVector3f(this.location_spotLightDirection, UnnamedGame.SPOTLIGHT.direction);
		super.loadVector3f(this.location_spotLightColor, UnnamedGame.SPOTLIGHT.color);
		super.loadVector3f(this.location_spotLightAttenuation, UnnamedGame.SPOTLIGHT.attenuation);
		super.loadFloat(this.location_spotCosCutoff, UnnamedGame.SPOTLIGHT.cutoffAngle);

		// TODO: refactor light stuff
		for (int i = 0; i < UnnamedGame.MAX_LIGHTS; i++) {
			if (i < UnnamedGame.POINT_LIGHTS.size()) {
				super.loadVector3f(this.location_lightPositions[i], UnnamedGame.POINT_LIGHTS.get(i).position);
				super.loadVector3f(this.location_lightColors[i], UnnamedGame.POINT_LIGHTS.get(i).color);
				super.loadVector3f(this.location_lightAttenuations[i], UnnamedGame.POINT_LIGHTS.get(i).attenuation);
			} else {
				super.loadVector3f(this.location_lightPositions[i], new Vector3f());
				super.loadVector3f(this.location_lightColors[i], new Vector3f());
				super.loadVector3f(this.location_lightAttenuations[i], new Vector3f(1, 0, 0));
			}
		}
	}
}
