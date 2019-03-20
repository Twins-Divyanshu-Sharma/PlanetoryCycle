package base;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.system.MemoryStack.*;


public class Window {
	public static int width = 1366;
	public static int height = 768;
	private static String name = "WateryGame";
	private long win;

	
    public Window(){
    	
    	//GLFWErrorCallback.createPrint(System.err).set();
    	
    	if(!glfwInit()){
    		throw new IllegalStateException("Unable to initialize GLFW");
    	}
    	
    	glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    	glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    	glfwWindowHint(GLFW_VISIBLE,GL_FALSE);
    	glfwWindowHint(GLFW_RESIZABLE,GL_FALSE);
    	
    	win = glfwCreateWindow(width,height,name,glfwGetPrimaryMonitor(),NULL);
    	//win = glfwCreateWindow(300,300,name,NULL,NULL);
    	
    	if(win==NULL){
    		throw new RuntimeException("Failed to create GLFW ");
    	}
    	
 
    
    	try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(win, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				win,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} 
    	
    	
 	
    	glfwMakeContextCurrent(win);
    	

    	
    	glfwSwapInterval(1);  // v-sync
      	glfwShowWindow(win);
    	
        GL.createCapabilities();
        
        glClearColor(0.01f,0.01f,0.01f,1f);
        
  	  glEnable(GL_DEPTH_TEST);
      glEnable(GL_BLEND);
      glBlendFunc(GL_SRC_ALPHA,GL_ONE_MINUS_SRC_ALPHA);
    }
    
    public void swapBuffers(){
 	   glfwSwapBuffers(win);
    }
    
    public void poll(){
 	   glfwPollEvents();
    }
    
    public void setClearColor(float r, float g, float b){
    	glClearColor(r,g,b,1f);
    }
    
    public void clear(){
 	   glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
 	   
    }
    
    public boolean isCloseRequested(){
 	   return glfwWindowShouldClose(win);
    }
    
    public void shutdown(){
   	 // glfwSetErrorCallback(null).free();
 	  // glfwFreeCallbacks(win);
 	   glfwDestroyWindow(win);
 	   
 	   glfwTerminate();

    }
    
    public float getAspectRatio(){
    	return (float)width/height;
    }
    
    public boolean isKeyPressed(int key){
    	int state = glfwGetKey(win, key);
    	return (state != GLFW_RELEASE);
    }
    
    public boolean isKeyTyped(int key){
    	int state = glfwGetKey(win, key);
    	return (state == GLFW_REPEAT);
    }
    
    public long getWindowHandle(){
    	return win;
    }
    
    public void close(){
    	glfwSetWindowShouldClose(win,true);
    }

}
