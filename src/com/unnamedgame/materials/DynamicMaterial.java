package com.unnamedgame.materials;

import org.lwjgl.opengl.*;

import com.openglengine.renderer.material.*;
import com.unnamedgame.shaders.*;

public class DynamicMaterial extends Material<DynamicShader> {
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
	public void initRendercode(DynamicShader shader) {
		if (this.transparent)
			GL11.glDisable(GL11.GL_CULL_FACE);

		shader.loadFloat(shader.location_shineDamper, this.shineDamper);
		shader.loadFloat(shader.location_reflectivity, this.reflectivity);
		shader.loadFloat(shader.location_transparent, this.transparent == false ? 0 : 1);
		shader.loadFloat(shader.location_useFakeLighting, this.useFakeLighting == false ? 0 : 1);
	}

	@Override
	public void deinitRendercode() {
		if (this.transparent)
			GL11.glEnable(GL11.GL_CULL_FACE);
	}
}
