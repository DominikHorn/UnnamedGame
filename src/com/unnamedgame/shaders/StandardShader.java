package com.unnamedgame.shaders;

import com.openglengine.renderer.*;
import com.openglengine.renderer.material.*;
import com.openglengine.renderer.model.*;
import com.openglengine.renderer.shader.*;
import com.unnamedgame.materials.*;

public class StandardShader extends Shader {
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

	/** TODO: refactor */
	private LightSource lightSource;

	public StandardShader(LightSource lightSource) {
		this.lightSource = lightSource;
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
		location_transparent = super.getUniformLocation("transparent");
	}

	@Override
	public void uploadGlobalUniforms() {
		super.uploadGlobalUniforms();
		super.loadVector3f(location_lightPosition, this.lightSource.position);
		super.loadVector3f(location_lightColor, this.lightSource.color);
		super.loadFloat(location_lightBrightness, this.lightSource.brightness);
		super.loadFloat(location_ambientBrightness, 0.1f); // TODO: refactor
	}

	@Override
	public void uploadModelUniforms(Model model) {
		super.uploadModelUniforms(model);

		Material material = model.getMaterial();

		Float shineDamper = (Float) material.getPropertyValue(DynamicMaterial.PROPERTY_SHINE_DAMPER);
		Float reflectivity = (Float) material.getPropertyValue(DynamicMaterial.PROPERTY_REFLECTIVITY);
		Boolean transparent = (Boolean) material.getPropertyValue(DynamicMaterial.PROPERTY_TRANSPARENCY);

		super.loadFloat(location_shineDamper, shineDamper);
		super.loadFloat(location_reflectivity, reflectivity);
		super.loadFloat(location_transparent, transparent == false ? 0 : 1);
	}
}