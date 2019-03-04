package base;


import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {
	public static int width = 1366;
	public static int height = 768;
	private static String name = "WateryGame";
	private long win;

	
    public Window(){
    	if(!glfwInit()){
    		System.out.println("unable to initialize glfw");
    	}
    	glfwDefaultWindowHints();
    	
    	glfwWindowHint(GLFW_VISIBLE,GL_TRUE);
    	glfwWindowHint(GLFW_RESIZABLE,GL_FALSE);
    	
    	win = glfwCreateWindow(width,height,name,glfwGetPrimaryMonitor(),NULL);
    	
    	if(win==NULL){
    		System.out.println("unable to create window");
    	}
    
    	
    	glfwMakeContextCurrent(win);
    	
    	glfwSwapInterval(1);  // v-sync
    	
    	glfwShowWindow(win);
    	
        GL.createCapabilities();
        
        glClearColor(0.01f,0.01f,0.01f,1f);
        
        glEnable(GL_DEPTH_TEST);
        
        glEnable(GL_ALPHA_TEST);
        glAlphaFunc(GL_GREATER,0.1f);
        
        System.out.println(glGetString(GL_VERSION));
    	
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
 	   glfwFreeCallbacks(win);
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
