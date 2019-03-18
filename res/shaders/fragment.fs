#version 330

in vec2 texCoord;
in vec3 outWorldNormal;
in vec3 outWorldPosition;


out vec4 fragColor;

struct Attenuation
{
	float constant;
	float linear;
	float exponent;
};

struct PointLight
{
	vec3 color;
	vec3 position;
	float intensity;
	Attenuation att;
};

struct Material
{
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	int hasTexture;
	int emission;
	float reflectance;
};


uniform sampler2D sampler;
uniform vec3 ambientLight;
uniform float specularPower;
uniform Material material;
uniform PointLight pointLight;
uniform vec3 camPos;

vec4 calcPointLight(PointLight light, vec3 position, vec3 normal)
{
	vec4 diffuseColor = vec4(0,0,0,0);
	vec4 specColor = vec4(0,0,0,0);
	
	//Diffuse Light
	vec3 lightVector = light.position - position;
	vec3 toLightSource = normalize(lightVector);
	float diffuseFactor = max(dot(normal,toLightSource), 0.0);
	diffuseColor = vec4(light.color, 1.0) * light.intensity * diffuseFactor;
	
	//Specular Light
	vec3 camDirection = normalize(-position);
	vec3 fromLightSource = - toLightSource;
	vec3 reflectedLight = normalize(reflect(fromLightSource, normal));
	float specularFactor = max(dot(camDirection, reflectedLight), 0.0);
	specularFactor = pow(specularFactor, specularPower);
	specColor = specularFactor * material.reflectance * vec4(light.color, 1.0);
	
	
		
	//Attenuation
	float distance = length(lightVector);
	float attInv = light.att.constant + light.att.linear*distance + light.att.exponent*distance*distance;
	
	return (diffuseColor + specColor) / attInv;
}


void main()
{
	vec4 baseColor;
	if(material.hasTexture == 0)
	{
		baseColor = material.ambient +  material.diffuse + material.specular;
	}
	else
	{
		baseColor = texture(sampler, texCoord);
	}
	
	vec4 lightColor = calcPointLight(pointLight, outWorldPosition, outWorldNormal);
	vec4 totalLight;
	
	if(material.emission == 0)
	{
	   totalLight = vec4(ambientLight, 1.0);
	   totalLight += lightColor;
	}
	else
	{
	   totalLight = material.ambient;
	}
	
	fragColor = baseColor * totalLight;	
}