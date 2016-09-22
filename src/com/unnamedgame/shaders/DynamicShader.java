package com.unnamedgame.shaders;

import com.openglengine.renderer.shader.*;
import com.openglengine.util.math.*;
import com.unnamedgame.main.*;

public class DynamicShader extends Shader {
	/** uniform location of ambient light amount */
	private int location_ambient;
	private int location_density;

	/** uniform location of light stuff */
	private int[] location_lightPositions;
	private int[] location_lightColors;
	private int[] location_lightAttenuation;

	/** uniform locations spotlight stuff */
	private int location_spotLightPosition;
	private int location_spotLightAttenuation;
	private int location_spotLightDirection;
	private int location_spotLightColor;
	private int location_spotCosCutoff;

	/** uniform location of sky color */
	private int location_skyColor;

	/** uniform location of material attribs */
	public int location_shineDamper;
	public int location_reflectivity;
	public int location_transparent;
	public int location_useFakeLighting;

	public DynamicShader() {
		this.compileShaderFromFiles(UnnamedGame.SHADER_FOLDER + "standard_vertex.glsl",
				UnnamedGame.SHADER_FOLDER + "standard_fragment.glsl");
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();

		this.location_ambient = super.getUniformLocation("ambient");
		this.location_density = super.getUniformLocation("density");
		this.location_shineDamper = super.getUniformLocation("shineDamper");
		this.location_reflectivity = super.getUniformLocation("reflectivity");
		this.location_transparent = super.getUniformLocation("transparent");
		this.location_useFakeLighting = super.getUniformLocation("useFakeLighting");
		this.location_skyColor = super.getUniformLocation("skyColor");
		this.location_spotLightPosition = super.getUniformLocation("spotLightPosition");
		this.location_spotLightAttenuation = super.getUniformLocation("spotLightAttenuation");
		this.location_spotLightDirection = super.getUniformLocation("spotLightDirection");
		this.location_spotLightColor = super.getUniformLocation("spotLightColor");
		this.location_spotCosCutoff = super.getUniformLocation("spotCosCutoff");

		// Get array uniforms
		this.location_lightPositions = new int[UnnamedGame.MAX_LIGHTS];
		this.location_lightColors = new int[UnnamedGame.MAX_LIGHTS];
		this.location_lightAttenuation = new int[UnnamedGame.MAX_LIGHTS];

		for (int i = 0; i < UnnamedGame.MAX_LIGHTS; i++) {
			this.location_lightPositions[i] = super.getUniformLocation("lightPositions[" + i + "]");
			this.location_lightColors[i] = super.getUniformLocation("lightColors[" + i + "]");
			this.location_lightAttenuation[i] = super.getUniformLocation("lightAttenuations[" + i + "]");
		}
	}

	@Override
	public void uploadGlobalUniforms() {
		// TODO: refactor sky color
		super.loadVector3f(location_skyColor, UnnamedGame.SKY_COLOR);

		super.loadFloat(this.location_ambient, UnnamedGame.AMBIENCE);
		super.loadFloat(location_density, UnnamedGame.DENSITY);

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
				super.loadVector3f(this.location_lightAttenuation[i], UnnamedGame.POINT_LIGHTS.get(i).attenuation);
			} else {
				super.loadVector3f(this.location_lightPositions[i], new Vector3f());
				super.loadVector3f(this.location_lightColors[i], new Vector3f());
				super.loadVector3f(this.location_lightAttenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
}