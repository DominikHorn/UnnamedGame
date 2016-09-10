package com.unnamedgame.materials;

import org.lwjgl.opengl.*;

import com.openglengine.renderer.material.*;

public class DynamicMaterial extends Material {
	public float reflectivity = 0;
	public float shineDamper = 1;
	public boolean transparent = false;
	public boolean useFakeLighting = false;

	public DynamicMaterial(float reflectivity, float shineDamper, boolean transparent, boolean useFakeLighting) {
		this.reflectivity = reflectivity;
		this.shineDamper = shineDamper;
		this.transparent = transparent;
		this.useFakeLighting = useFakeLighting;
	}

	@Override
	public void initRendercode() {
		if (this.transparent)
			GL11.glDisable(GL11.GL_CULL_FACE);
	}

	@Override
	public void deinitRendercode() {
		if (this.transparent)
			GL11.glEnable(GL11.GL_CULL_FACE);
	}
}
