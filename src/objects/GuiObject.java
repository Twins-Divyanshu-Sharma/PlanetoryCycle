package objects;

import org.joml.Vector2f;
import org.joml.Vector4f;

import base.CursorOn;

public class GuiObject {
  public static float buttonWIDTH = 0.5f;	
  public static float buttonHEIGHT = 0.125f;	
	
   private Vector2f position;
   private float height;
   private float width;
   private boolean textured = false;
   private String texLoc;
   
   private Vector4f color = new Vector4f(0,0,0,1);  // if not textured
   private SpriteSheet iCameFrom; // always a pointer
   private Texture tex; // either texture or spritesheet
   
   private boolean fromSpriteSheet = false;
   
   private GuiMesh mesh;
   
   
   public boolean isButton = false;
   public CursorOn id = CursorOn.NOWHERE;
   
   public GuiObject(Vector2f position, Vector4f color, float width, float height){  // untextured
	   this.position = position;
	   this.color = color;
	   this.width = width;
	   this.height = height;
	   textured = false;
	   mesh  = new GuiMesh(width, height);
	   fromSpriteSheet = false;
   }
   
   public GuiObject(Vector2f position, String texLoc, float width, float height) throws Exception{
	   tex = new Texture(texLoc);
	   this.position = position;
	   this.width = width;
	   this.height = height;
	   mesh = new GuiMesh(width,height);
	   textured = true;
	   fromSpriteSheet = false;
   }
  
   public GuiObject(Vector2f position, SpriteSheet ss, int row, int col, float width, float height) throws Exception{  // textured
	   this.position = position;
	   this.width = width;
	   this.height = height;
	   textured = true;
	   mesh  = new GuiMesh(ss,row,col,width, height);
	   iCameFrom = ss;
	   textured = true;
	   fromSpriteSheet = true;
   }
   
   
public boolean isTextured() {
	return textured;
}
public void setTextured(boolean textured) {
	this.textured = textured;
}
public Vector4f getColor() {
	return color;
}
public void setColor(Vector4f color) {
	this.color = color;
}
public Vector2f getPosition() {
	return position;
}
public void setPosition(Vector2f position) {
	this.position = position;
}
public float getHeight() {
	return height;
}
public void setHeight(float height) {
	this.height = height;
}
public float getWidth() {
	return width;
}
public void setWidth(float width) {
	this.width = width;
}

public Texture getTexture(){
   if(!textured){
	   return null;
   }else{
	   if(!fromSpriteSheet){
		   return tex;
	   }else{
		   return iCameFrom.getTex();
	   }
   }
}

public GuiMesh getMesh(){
	return mesh;
}

public void cleanUp(){
	mesh.cleanUp();
}

public void setButton(CursorOn buttonId){
	this.id = buttonId;
	this.isButton = true;
}


}
