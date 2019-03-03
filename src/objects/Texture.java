package objects;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Texture {
    private PNGDecoder pngDecoder;
    private ByteBuffer buf;
    private int textureID;
    public Texture(String texLoc) throws Exception{
    	FileInputStream i = new FileInputStream(new File("resourse/"+texLoc));
    	pngDecoder = new PNGDecoder(i);
    	
    	buf = ByteBuffer.allocateDirect(4*pngDecoder.getWidth()*pngDecoder.getHeight());
    	pngDecoder.decode(buf, pngDecoder.getWidth()*4, Format.RGBA);
    	buf.flip();
    	
    	//now upload the texture into graphic card
    	textureID = glGenTextures();
    	glBindTexture(GL_TEXTURE_2D,textureID);
    	
    	glPixelStorei(GL_UNPACK_ALIGNMENT,1	);
    	
    	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, pngDecoder.getWidth(),pngDecoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
    	
    	glGenerateMipmap(GL_TEXTURE_2D);
    }
    
    public int getID(){
    	return textureID;
    }
	
	public void cleanUp(){
		glDeleteTextures(textureID);
		buf.clear();
	}
}
