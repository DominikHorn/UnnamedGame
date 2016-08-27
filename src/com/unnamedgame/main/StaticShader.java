package com.unnamedgame.main;

import com.openglengine.entitity.*;
import com.openglengine.renderer.*;
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
	public void uploadEntityUniforms(Entity entity) {
		super.uploadEntityUniforms(entity);

		// Float shineDamper = (Float) entity.getValueProperty(CustomEntityProperties.PROPERTY_SHINE_DAMPER);
		// Float reflectivity = (Float) entity.getValueProperty(CustomEntityProperties.PROPERTY_REFLECTIVITY);
		//
		// super.loadFloat(location_shineDamper, shineDamper);
		// super.loadFloat(location_reflectivity, reflectivity);
	}
}
