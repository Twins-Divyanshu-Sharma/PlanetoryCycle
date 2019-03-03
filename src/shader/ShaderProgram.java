package shader;

import static org.lwjgl.opengl.GL20.*;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import objects.Material;
import objects.PointLight;


public class ShaderProgram {
    private int programID;
    private int vertexID;
    private int fragmentID;
    protected Map<String, Integer> uniforms;
    
    public ShaderProgram() throws Exception{
        uniforms = new HashMap<>();
    	programID = glCreateProgram();
    	if(programID == 0){
    		throw new Exception("unable to create program");
    	}
    }
    
    public void createVertexShader(String sourceCode) throws Exception{
    	vertexID = createShader(sourceCode,GL_VERTEX_SHADER);
    }
    
    public void createFragmentShader(String sourceCode) throws Exception{
    	fragmentID = createShader(sourceCode,GL_FRAGMENT_SHADER);
    }
    
    private int createShader(String sourceCode, int shaderType) throws Exception{
    	int shaderID = glCreateShader(shaderType);
    	if(shaderID == 0){
    		throw new Exception("unable to create shader of type : " + shaderType);
    	}
    	glShaderSource(shaderID,sourceCode);
    	glCompileShader(shaderID);
    	
    	if(glGetShaderi(shaderID,GL_COMPILE_STATUS)==0){
    		throw new Exception("Unable to compile shader : " + shaderID + " : info : " + glGetShaderInfoLog(shaderID,1024));
    	}
    	glAttachShader(programID,shaderID);
    	return shaderID;
    }
    
    public void linkProgram() throws Exception{
    	glLinkProgram(programID);
    	if(glGetProgrami(programID,GL_LINK_STATUS)==0){
    		throw new Exception("Unable to link program : "+glGetProgramInfoLog(programID,1024));
    	}
    	
    	if(vertexID != 0){
    		glDetachShader(programID,vertexID);
    	}
    	if(fragmentID != 0){
    		glDetachShader(programID,fragmentID);
    	}
    	
    	glValidateProgram(programID);
    	if(glGetProgrami(programID,GL_VALIDATE_STATUS)==0){
    		throw new Exception("Unable to validate program : "+glGetProgramInfoLog(programID,1024));
    	}
    	
    }
    
    public void createUniform(String uniformName) throws Exception{
    	int uniformID = glGetUniformLocation(programID,uniformName);
    	if(uniformID < 0){
    		throw new Exception("Unable to create uniform : " + uniformName);
    	}
    	uniforms.put(uniformName, uniformID);
    }

    
    public void createPointLightUniform(String uniformName) throws Exception{
    	createUniform(uniformName+".color");
    	createUniform(uniformName+".position");
    	createUniform(uniformName+".intensity");
    	createUniform(uniformName+".att.constant");
    	createUniform(uniformName+".att.linear");
    	createUniform(uniformName+".att.exponent");
    }
    
    public void createMaterialUniform(String uniformName) throws Exception{
    	createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
        createUniform(uniformName + ".emission");
    }
    
    
    
    
    public void setUniform(String uniformName, Matrix4f mat){
 	   try(MemoryStack s = MemoryStack.stackPush()){
		   FloatBuffer fb = s.mallocFloat(16);
		   mat.get(fb);
		   glUniformMatrix4fv(uniforms.get(uniformName),false,fb);
	   }
    }
    
    public void setUniform(String uniformName, int val){
    	glUniform1i(uniforms.get(uniformName),val);
    }
    
    public void setUniform(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }

    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }
    
    public void setUniform(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }
    
    public void setUniform(String uniformName, PointLight pointLight){
    	setUniform(uniformName+".color", pointLight.getColor());
    	setUniform(uniformName+".position", pointLight.getPosition());
    	setUniform(uniformName+".intensity", pointLight.getIntensity());
    	PointLight.Attenuation att = pointLight.getAttenuation();
    	setUniform(uniformName+".att.constant", att.getConstant());
    	setUniform(uniformName+".att.linear", att.getLinear());
    	setUniform(uniformName+".att.exponent", att.getExponent());
    }
    
    public void setUniform(String uniformName, Material material){
    	setUniform(uniformName + ".ambient", material.getAmbientColor());
        setUniform(uniformName + ".diffuse", material.getDiffuseColor());
        setUniform(uniformName + ".specular", material.getSpecularColor());
        setUniform(uniformName + ".hasTexture", material.hasTextured() ? 1 : 0);
        setUniform(uniformName + ".emission", material.hasEmission() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());
    }
    
    
    
    public void bind(){
    	glUseProgram(programID);
    }
    public void unbind(){
    	glUseProgram(0);
    }
    public void cleanUp(){
    	unbind();
    	if(programID!=0){
    		glDeleteProgram(programID);
    	}
    }
}
