package com.unnamedgame.materials;

import com.openglengine.renderer.material.*;

public class TerrainMaterial extends Material {
	// TODO: refactor for performance
	public static final String PROPERTY_SHINE_DAMPER = "shine_damper";
	public static final String PROPERTY_REFLECTIVITY = "reflectivity";

	public TerrainMaterial(float reflectivity, float shineDamper) {
		this.putProperty(PROPERTY_REFLECTIVITY, reflectivity);
		this.putProperty(PROPERTY_SHINE_DAMPER, shineDamper);
	}

	@Override
	public void initRendercode() {
		// Do nothing for now
	}

	@Override
	public void deinitRendercode() {
		// Do nothing for now
	}
}
