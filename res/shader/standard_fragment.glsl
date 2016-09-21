#version 400 core

in vec3 surfaceNormal;
in vec3 toLightVectors[4];
in vec3 toCameraVector;
in vec2 pass_textureCoords;
in float visibility;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 lightColors[4];
uniform vec3 skyColor;
uniform float shineDamper;
uniform float reflectivity;
uniform float transparent;

void main(void) {
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);

	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	for (int i = 0; i < 4; i++) {
		vec3 unitLightVector = normalize(toLightVectors[i]);
		float nDotl = dot(unitNormal, unitLightVector);
		float brightness = max(nDotl, 0.0);
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);

		totalDiffuse = totalDiffuse + brightness * lightColors[i];
		totalSpecular = totalSpecular + dampedFactor * reflectivity *  lightColors[i];
	}
	totalDiffuse = max(totalDiffuse, 0.05);

	vec4 texColor = texture(textureSampler, pass_textureCoords);
	if (transparent == 1 && texColor.a < 0.5) {
		discard;
	}
	
	out_color = vec4(totalDiffuse + 0.1, 1.0) * texColor + vec4(totalSpecular, 1.0);

	// Enable this line for fog calculation
	out_color = mix(vec4(skyColor, 1.0), out_color, visibility);
}