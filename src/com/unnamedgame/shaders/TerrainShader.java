package com.unnamedgame.shaders;

import com.openglengine.renderer.shader.*;
import com.unnamedgame.main.*;

public class TerrainShader extends Shader {
	/** uniform location of the lights position shader attrib */
	public int location_sunPosition;

	/** uniform location of the lights color shader attrib */
	public int location_sunColor;

	/** uniform location of the shine dampener shader attrib */
	public int location_shineDamper;

	/** uniform location of the reflectivity shader attrib */
	public int location_reflectivity;

	/** uniform location of ambient brightness attrib */
	public int location_ambientBrightness;

	/** uniform location of light source brightness */
	public int location_sunBrightness;

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

		location_sunPosition = super.getUniformLocation("sunPosition");
		location_sunColor = super.getUniformLocation("sunColor");
		location_sunBrightness = super.getUniformLocation("sunBrightness");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_ambientBrightness = super.getUniformLocation("ambientBrightness");
		location_skyColor = super.getUniformLocation("skyColor");
		location_backgroundTexture = super.getUniformLocation("backgroundTexture");
		location_rTexture = super.getUniformLocation("rTexture");
		location_gTexture = super.getUniformLocation("gTexture");
		location_bTexture = super.getUniformLocation("bTexture");
		location_blendMap = super.getUniformLocation("blendMap");
	}

	@Override
	public void uploadGlobalUniforms() {
		// TODO: refactor sky color
		super.loadVector3f(location_skyColor, UnnamedGame.SUN.color);

		// TODO: refactor lighting
		super.loadVector3f(location_sunPosition, UnnamedGame.SUN.position);
		super.loadVector3f(location_sunColor, UnnamedGame.SUN.color);
		super.loadFloat(location_sunBrightness, UnnamedGame.SUN.brightness);
		super.loadFloat(location_ambientBrightness, 0.0f); // TODO: refactor
		super.loadInt(location_backgroundTexture, 0);
		super.loadInt(location_rTexture, 1);
		super.loadInt(location_gTexture, 2);
		super.loadInt(location_bTexture, 3);
		super.loadInt(location_blendMap, 4);
	}
}
