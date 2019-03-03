package objects;

import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

public class Mesh {
    private float[] positions, textures, normals;
    private int[] indices;
    private int vao;
    private int positionVbo, textureVbo, normalVbo, indexVbo;
    private FloatBuffer positionBuffer, textureBuffer, normalBuffer;
    private IntBuffer indexBuffer;
    private Texture tex;
    private Material material;
    
    public Mesh(){};
    
    public Mesh(Mesh mesh){
    	positions = mesh.positions;
    	textures = mesh.textures;
    	normals = mesh.normals;
    	indices = mesh.indices;
    	generate();
    }
    
    
    public void setTexture(String texLoc)throws Exception{
    	tex = new Texture(texLoc);
    }
    
    public void generate(){
    	vao = glGenVertexArrays();
    	glBindVertexArray(vao);
    	try{
    	////////////////////position///////////////////////
    	//positionBuffer = MemoryUtil.memAllocFloat(positions.length);
    	//positionBuffer.put(positions).flip();
    	positionVbo = glGenBuffers();
    	glBindBuffer(GL_ARRAY_BUFFER,positionVbo);
    	glBufferData(GL_ARRAY_BUFFER,positions,GL_STATIC_DRAW);
    	glVertexAttribPointer(0,3,GL_FLOAT,false,0,0);
    	
    	
    	////////////////////textures////////////////////////
    	//textureBuffer = MemoryUtil.memAllocFloat(textures.length);
    	//textureBuffer.put(textures).flip();
    	textureVbo = glGenBuffers();
    	glBindBuffer(GL_ARRAY_BUFFER,textureVbo);
    	glBufferData(GL_ARRAY_BUFFER,textures,GL_STATIC_DRAW);
    	glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);
    	
    	///////////////////normals//////////////////////////
    	//normalBuffer = MemoryUtil.memAllocFloat(normals.length);
    	//normalBuffer.put(normals).flip();
    	normalVbo = glGenBuffers();
    	glBindBuffer(GL_ARRAY_BUFFER,normalVbo);
    	glBufferData(GL_ARRAY_BUFFER,normals,GL_STATIC_DRAW);
    	glVertexAttribPointer(2,3,GL_FLOAT,false,0,0);
    	
    	////////////////////index////////////////////////////
    	//indexBuffer = MemoryUtil.memAllocInt(indices.length);
    	//indexBuffer.put(indices).flip();
    	indexVbo = glGenBuffers();
    	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,indexVbo);
    	glBufferData(GL_ELEMENT_ARRAY_BUFFER,indices,GL_STATIC_DRAW);
    	
	  }catch(Exception e){
		  e.printStackTrace();
	   }finally{
          if (positionBuffer != null)
              MemoryUtil.memFree(positionBuffer);
          if(textureBuffer != null)
        	  MemoryUtil.memFree(textureBuffer);
          if(normalBuffer != null)
        	  MemoryUtil.memFree(normalBuffer);
          if(indexBuffer != null)
              MemoryUtil.memFree(indexBuffer);
      }
    	
    	
    }
    
    
    public void render(double dt){
    	glActiveTexture(GL_TEXTURE0);
    	glBindTexture(GL_TEXTURE_2D,tex.getID());
    	
    	glBindVertexArray(vao);
    	glEnableVertexAttribArray(0);
    	glEnableVertexAttribArray(1);
    	glEnableVertexAttribArray(2);
    	glDrawElements(GL_TRIANGLES,indices.length,GL_UNSIGNED_INT,0);
    	glDisableVertexAttribArray(2);
    	glDisableVertexAttribArray(1);
    	glDisableVertexAttribArray(0);
    	glBindVertexArray(0);
    }
    
    public void setBuffers(float[] positions, float[] textures, float[] normals, int[] indices){
    	this.positions = positions;
    	this.textures = textures;
    	this.normals = normals;
    	this.indices = indices;
    }
    
    public void cleanUp(){
    	tex.cleanUp();
    	glDisableVertexAttribArray(2);
    	glDisableVertexAttribArray(1);
    	glDisableVertexAttribArray(0);
    	
    	glBindBuffer(GL_ARRAY_BUFFER,0);
    	glDeleteBuffers(positionVbo);
    	glDeleteBuffers(textureVbo);
    	glDeleteBuffers(normalVbo);
    
    	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
    	
    	glBindVertexArray(0);
    	glDeleteVertexArrays(vao);
    }
    
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
        this.tex = material.getTexture();
    }
    
}
