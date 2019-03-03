package objects;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.system.MemoryUtil;

public class GuiMesh {

    private float[] positions, textures;
    private int[] indices;
    private int vao;
    private int positionVbo, textureVbo, indexVbo;
    private FloatBuffer positionBuffer, textureBuffer;
    private IntBuffer indexBuffer;
	private float width, height;
    
	public GuiMesh(float width, float height){  // untextured
		this.width = width;
		this.height = height;
		setBuffers();
		generate();
	}
	

	
	public GuiMesh(SpriteSheet ss,int row, int col, float width, float height) throws Exception{  // textured spritesheet	
		this.width = width;
	    this.height = height;
	    setBuffers(ss,row, col);
	    generate();
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
    	glVertexAttribPointer(0,2,GL_FLOAT,false,0,0);
    	
    	
    	////////////////////textures////////////////////////
    	//textureBuffer = MemoryUtil.memAllocFloat(textures.length);
    	//textureBuffer.put(textures).flip();
    	textureVbo = glGenBuffers();
    	glBindBuffer(GL_ARRAY_BUFFER,textureVbo);
    	glBufferData(GL_ARRAY_BUFFER,textures,GL_STATIC_DRAW);
    	glVertexAttribPointer(1,2,GL_FLOAT,false,0,0);
    	
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
          if(indexBuffer != null)
              MemoryUtil.memFree(indexBuffer);
      }
    	
    	
    }
    
    
    public void render(double dt, boolean textured, Texture tex){
    	if(textured){
    		glActiveTexture(GL_TEXTURE0);
    		glBindTexture(GL_TEXTURE_2D,tex.getID());
    	}
    	glBindVertexArray(vao);
    	glEnableVertexAttribArray(0);
    	glEnableVertexAttribArray(1);
    	glDrawElements(GL_TRIANGLES,indices.length,GL_UNSIGNED_INT,0);
    	glDisableVertexAttribArray(1);
    	glDisableVertexAttribArray(0);
    	glBindVertexArray(0);
    }
   
	   public void setBuffers(float[] positions, float[] textures, int[] indices){
	    	this.positions = positions;
	    	this.textures = textures;
	    	this.indices = indices;
	    }
    public void setBuffers(){
		   float[] tempPos = {
			 	   0, 0,  // top left
			 	   0+width, 0,  // top right
			 	   0, 0-height, // bottom left
			 	   0+width, 0-height, // bottom right
		   };
		   float[] tempTexCoord = {
				   0, 0,  // top left
				   1, 0,  // top right
				   0, 1,  // bottom left
				   1, 1,  // bottom right
		   };
		   int[] tempIndices = {
				   0, 1, 2, 2, 1, 3,
		   };
		   setBuffers(tempPos, tempTexCoord, tempIndices);
	   }
    
    public void setBuffers(SpriteSheet ss,int row, int col){
		   float[] tempPos = {
			 	   0, 0,  // top left
			 	   0+width, 0,  // top right
			 	   0, 0-height, // bottom left
			 	   0+width, 0-height, // bottom right
		   };
		   float[] tempTexCoord = {
				   0+ss.getDw()*(float)(col), 0 + ss.getDh()*(float)(row),  // top left
				   0+ss.getDw()*(float)(col+1), 0 + ss.getDh()*(float)(row),  // top right
				   0+ss.getDw()*(float)(col), 0 + ss.getDh()*(float)(row +1), // bottom left
				   0+ss.getDw()*(float)(col+1),  0 + ss.getDh()*(float)(row +1),  // bottom right
		   };
		   int[] tempIndices = {
				   0, 1, 2 , 2, 1, 3,
		   };
		   setBuffers(tempPos, tempTexCoord, tempIndices);
	   }
    
    
    public void cleanUp(){
    	glDisableVertexAttribArray(1);
    	glDisableVertexAttribArray(0);
    	
    	glBindBuffer(GL_ARRAY_BUFFER,0);
    	glDeleteBuffers(positionVbo);
    	glDeleteBuffers(textureVbo);
    
    	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,0);
    	
    	glBindVertexArray(0);
    	glDeleteVertexArrays(vao);
    }
  
    
}
