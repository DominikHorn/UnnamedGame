package com.unnamedgame.materials;

import com.openglengine.renderer.material.*;

public class StallMaterial extends Material {
	public static final String PROPERTY_SHINE_DAMPER = "shine_damper";
	public static final String PROPERTY_REFLECTIVITY = "reflectivity";

	@Override
	protected void loadMaterialProperties() {
		this.putProperty(PROPERTY_REFLECTIVITY, 1f);
		this.putProperty(PROPERTY_SHINE_DAMPER, 10f);
	}
}
