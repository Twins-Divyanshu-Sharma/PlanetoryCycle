package objects;

import java.nio.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;


import static org.lwjgl.stb.STBImage.*;

import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;

public class Texture {

    private int textureID;

    private ByteBuffer image;

    int width, height;
    
    public Texture(String texLoc) throws Exception{

        try (MemoryStack stack = MemoryStack.stackPush()) {
            /* Prepare image buffers */
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            /* Load image */
            //stbi_set_flip_vertically_on_load(true);
            image = stbi_load("res/textures/"+texLoc, w, h, comp, 4);
            
            if (image == null) {
                throw new RuntimeException("Failed to load a texture file!"
                                           + System.lineSeparator() + stbi_failure_reason());
            }

            /* Get width and height of image */
            width = w.get();
            height = h.get();
        }
    	
    	//now upload the texture into graphic card
    	textureID = glGenTextures();
    	glBindTexture(GL_TEXTURE_2D,textureID);
    	
    	glPixelStorei(GL_UNPACK_ALIGNMENT,1	);
    	
    	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width,height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
    	
    	glGenerateMipmap(GL_TEXTURE_2D);
    	
    	stbi_image_free(image);
    }
    
    public int getID(){
    	return textureID;
    }
	
	public void cleanUp(){
		glDeleteTextures(textureID);
		image.clear();
	}
}
