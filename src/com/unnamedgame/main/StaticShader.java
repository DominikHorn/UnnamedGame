package com.unnamedgame.main;

import com.openglengine.core.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;

public class StaticShader extends Shader {
	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();

		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColor = super.getUniformLocation("lightColor");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
	}

	@Override
	public void uploadGlobalUniforms() {
		super.uploadGlobalUniforms();
		super.loadVector3f(location_lightPosition, Engine.getLightSource().getPosition());
		super.loadVector3f(location_lightColor, Engine.getLightSource().getColor());
	}

	@Override
	public void uploadModelUniforms(TexturedModel model) {
		super.uploadModelUniforms(model);
		super.loadFloat(location_shineDamper, model.getTexture().getShineDamper());
		super.loadFloat(location_reflectivity, model.getTexture().getReflectivity());
	}
}
