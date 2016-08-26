package com.unnamedgame.main;

import com.openglengine.renderer.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;

public class StaticShader extends Shader {
	/** uniform location of the lights position shader attrib */
	private int location_lightPosition;

	/** uniform location of the lights color shader attrib */
	private int location_lightColor;

	/** uniform location of the shine dampener shader attrib */
	private int location_shineDamper;

	/** uniform location of the reflectivity shader attrib */
	private int location_reflectivity;

	/** TODO: refactor */
	private LightSource lightSource;

	public StaticShader(LightSource lightSource) {
		this.lightSource = lightSource;
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
	public void uploadGlobalUniforms() {
		super.uploadGlobalUniforms();
		super.loadVector3f(location_lightPosition, this.lightSource.getPosition());
		super.loadVector3f(location_lightColor, this.lightSource.getColor());
	}

	@Override
	public void uploadModelUniforms(TexturedModel model) {
		super.uploadModelUniforms(model);
		super.loadFloat(location_shineDamper, model.getTexture().getShineDamper());
		super.loadFloat(location_reflectivity, model.getTexture().getReflectivity());
	}
}
