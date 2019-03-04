#version 400

in vec2 position;
in vec2 texCoord;

out vec2 outTexCoord;

uniform mat4 transformationMatrix;

void main(){
  outTexCoord = texCoord;
  gl_Position = transformationMatrix *  vec4(position,0,1);
}