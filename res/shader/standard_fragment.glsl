#version 400 core

in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in vec2 pass_textureCoords;
in float visibility;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform vec3 skyColor;
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientBrightness;
uniform float transparent;

void main(void) {
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float nDotl = dot(unitNormal, unitLightVector);
	float brightness = max(nDotl, ambientBrightness);
	vec3 diffuse = brightness * lightColor;
	
	vec3 unitVectorToCamera = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
	
	float specularFactor = dot(reflectedLightDirection , unitVectorToCamera);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

	vec4 texColor = texture(textureSampler, pass_textureCoords);
	if (transparent == 1 && texColor.a < 0.5) {
		discard;
	}
	
	out_color = vec4(diffuse + 0.1, 1.0) * texColor + vec4(finalSpecular, 1.0);

	// Enable this line for fog calculation
	out_color = mix(vec4(skyColor, 1.0), out_color, visibility);
}