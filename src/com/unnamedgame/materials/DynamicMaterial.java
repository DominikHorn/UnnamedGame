package com.unnamedgame.materials;

import org.lwjgl.opengl.*;

import com.openglengine.renderer.material.*;

public class DynamicMaterial extends Material {
	public static final String PROPERTY_SHINE_DAMPER = "shine_damper";
	public static final String PROPERTY_REFLECTIVITY = "reflectivity";
	public static final String PROPERTY_TRANSPARENCY = "transparent";

	public DynamicMaterial(float reflectivity, float shineDamper, boolean transparent) {
		this.putProperty(PROPERTY_REFLECTIVITY, reflectivity);
		this.putProperty(PROPERTY_SHINE_DAMPER, shineDamper);
		this.putProperty(PROPERTY_TRANSPARENCY, transparent);
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
