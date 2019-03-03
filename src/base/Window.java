package base;


import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {
	private static int width = 1366;
	private static int height = 768;
	private static String name = "WateryGame";
	private long win;
	private boolean[] keys = new boolean[65536];

	
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
    	
 	   glfwSetKeyCallback(win, (window,key,scancode,action,mode)->{
		   if(key == GLFW_KEY_ESCAPE && action == GLFW_PRESS)
			   glfwSetWindowShouldClose(win,true);
		   keys[key] = action != GLFW_RELEASE;
	   });
    
    	
    	glfwMakeContextCurrent(win);
    	
    	glfwSwapInterval(1);  // v-sync
    	
    	glfwShowWindow(win);
    	
        GL.createCapabilities();
        
        glClearColor(0.2f,0.2f,0.2f,1f);
        
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
    	return keys[key];
    }
    public long getWindowHandle(){
    	return win;
    }
    

}
