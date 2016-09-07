package com.unnamedgame.materials;

import org.lwjgl.opengl.*;

import com.openglengine.renderer.material.*;

public class DynamicMaterial extends Material {
	// TODO: refactor for performance
	public static final String PROPERTY_SHINE_DAMPER = "shine_damper";
	public static final String PROPERTY_REFLECTIVITY = "reflectivity";
	public static final String PROPERTY_TRANSPARENCY = "transparent";
	public static final String PROPERTY_USE_FAKE_LIGHTING = "use_fake_lighting";

	public DynamicMaterial(float reflectivity, float shineDamper, boolean transparent, boolean useFakeLighting) {
		this.putProperty(PROPERTY_REFLECTIVITY, reflectivity);
		this.putProperty(PROPERTY_SHINE_DAMPER, shineDamper);
		this.putProperty(PROPERTY_TRANSPARENCY, transparent);
		this.putProperty(PROPERTY_USE_FAKE_LIGHTING, useFakeLighting);
	}

	@Override
	public void initRendercode() {
		if (((Boolean) this.getPropertyValue(PROPERTY_TRANSPARENCY)) == true) {
			GL11.glDisable(GL11.GL_CULL_FACE);
		}
	}

	@Override
	public void deinitRendercode() {
		if (((Boolean) this.getPropertyValue(PROPERTY_TRANSPARENCY)) == true) {
			GL11.glEnable(GL11.GL_CULL_FACE);
		}
	}
}
