package objects;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;

import base.Window;
import de.matthiasmann.twl.utils.PNGDecoder;

public class MouseInput {
  private Vector2d prevPos;
  private Vector2d currPos;
  private Vector2f dispPos;
  private Vector2f glPos;

  
  private boolean inWindow= false;
  private boolean leftButtonPressed = false;
  private boolean rightButtonPressed = false;
  
  private GLFWCursorPosCallback cursorPosCallback;
  private GLFWCursorEnterCallback cursorEnterCallback;
  private GLFWMouseButtonCallback mouseButtonCallback;
  
  public MouseInput(){
	  prevPos = new Vector2d(-1,-1);
	  currPos = new Vector2d(0,0);
	  dispPos = new Vector2f();
	  glPos = new Vector2f();
  }
  
  private boolean menuMove = false;
  
  public void setMenuMovement(boolean menu){
	  menuMove = menu;
  }
  
  public void init(Window win){
	  glfwSetCursorPosCallback(win.getWindowHandle(),cursorPosCallback = new GLFWCursorPosCallback(){
		 public void invoke(long window, double xPos, double yPos){
			 if(!menuMove){
			 if(currPos.y >= 3*Window.height/4 || currPos.y <= Window.height/4){
			       glfwSetCursorPos(win.getWindowHandle(),1366/2,756/2);
			       prevPos.x = 0; prevPos.y=0;
			  }
			  if(currPos.x >= 3*Window.width/4 || currPos.x <= Window.width/4 ){
				   glfwSetCursorPos(win.getWindowHandle(),1366/2,756/2);
				   prevPos.x = 0; prevPos.y=0;
			  }
			 }
			 currPos.x = xPos;
			 currPos.y = yPos;
			 
		 }
	  });
	  glfwSetCursorEnterCallback(win.getWindowHandle(),cursorEnterCallback = new GLFWCursorEnterCallback(){
		 public void invoke(long window, boolean entered){
			 inWindow = entered;
		 }
	  });
	  glfwSetMouseButtonCallback(win.getWindowHandle(), mouseButtonCallback = new GLFWMouseButtonCallback(){
		 public void invoke(long window, int button, int action, int mods){
			 leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
			 rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
		 }
	  });
  }
  
  public void setCursor(Window w,String s) throws Exception{
		
		FileInputStream i;
		PNGDecoder decoder;

			i = new FileInputStream(new File("resourse/"+s));
			decoder = new PNGDecoder(i);
		
		ByteBuffer buff = ByteBuffer.allocateDirect( 4 * decoder.getHeight() * decoder.getWidth());
		decoder.decode(buff, decoder.getWidth()*4, PNGDecoder.Format.RGBA);
		buff.flip();
		
	    GLFWImage cursorImg= GLFWImage.create();
	    cursorImg.width(decoder.getWidth());     // setup the images' width
	    cursorImg.height(decoder.getHeight());   // setup the images' height
	    cursorImg.pixels(buff);   // pass image data

	    // create custom cursor and store its ID
	    int hotspotX = decoder.getWidth()/2;
	    int hotspotY = decoder.getHeight()/2;
	    long cursorID = glfwCreateCursor(cursorImg, hotspotX , hotspotY);

	    // set current cursor
	    glfwSetCursor(w.getWindowHandle(), cursorID);
	}
  
  
  public Vector2f getDispPos(){
	  return dispPos;
  }
  public Vector2f getGlPos(){
	  glPos.x = (float)((currPos.x*2 / Window.width )-1f);
	  glPos.y = (float)(1f - (currPos.y*2/Window.height));
	  return glPos;
  }
  
  public void input(Window win){
	  dispPos.x = 0; dispPos.y = 0;
	  if(prevPos.x > 0 && prevPos.y > 0 /*&& inWindow*/){
		  double deltaX = currPos.x - prevPos.x;
		  double deltaY = currPos.y - prevPos.y;
		  boolean rotateX = deltaX!=0;
		  boolean rotateY = deltaY!=0;
		  if(rotateX){
			  dispPos.y = (float)deltaX;
		  }
		  if(rotateY){
			  dispPos.x = (float)deltaY;
		  }
	  }

	  prevPos.x = currPos.x;
	  prevPos.y = currPos.y;
	  

  }
  
  public boolean isLeftButtonPressed(){
	  return leftButtonPressed;
  }
  public boolean isRightButtonPressed(){
	  return rightButtonPressed;
  }
  
  
}
