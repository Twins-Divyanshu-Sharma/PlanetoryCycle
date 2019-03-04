#version 400

in vec3 position;
in vec2 textures;
in vec3 normals;


out vec2 texCoord;
out vec3 outWorldNormal;
out vec3 outWorldPosition;


uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 transformationMatrix;

void main()
{
   vec4 worldPos  = transformationMatrix * vec4(position,1.0);  
   vec4 cameraPos = viewMatrix * worldPos;
   vec4 screenPos = projectionMatrix * cameraPos;
   gl_Position = screenPos;
   texCoord = textures;
   
   outWorldPosition = worldPos.xyz;
   outWorldNormal = normalize( ( transformationMatrix * vec4(normals, 0.0)).xyz ); 
}