#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform vec3 lightColor = vec(1,1,1);
uniform float shineDamper = 10f;
uniform float reflectivity = 1f;
uniform float ambientBrightness = 0.05f;
uniform float lightBrightness = 100f;

void main(void) {
	float brightnessFactor = (lightBrightness / length(toLightVector));
	
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDot = dot(unitNormal, unitLightVector);
	float brightness = max(nDot, ambientBrightness);
	vec3 diffuse = brightness * lightColor * brightnessFactor;

	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	
	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor,0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColor * brightnessFactor;

	out_Color = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords) + vec4(finalSpecular, 1.0);
}