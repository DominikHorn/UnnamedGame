#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform float ambientBrightness = 0.05f;

void main(void) {
	out_Color = ambientBrightness * texture(textureSampler, pass_textureCoords);
}