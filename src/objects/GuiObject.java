package objects;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class GuiObject {
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
   
   public GuiObject(Vector2f position, Vector4f color, float width, float height){  // untextured
	   this.position = position;
	   this.color = color;
	   this.width = width;
	   this.height = height;
	   textured = false;
	   mesh  = new GuiMesh(width, height);
	   fromSpriteSheet = false;
   }
   
   public GuiObject(String texLoc, float width, float height) throws Exception{
	   tex = new Texture(texLoc);
	   this.width = width;
	   this.height = height;
	   mesh = new GuiMesh(width,height);
	   textured = true;
	   fromSpriteSheet = false;
   }
  
   public GuiObject(Vector2f position, SpriteSheet ss, int row, int col, float width, float height) throws Exception{  // untextured
	   this.position = position;
	   this.width = width;
	   this.height = height;
	   textured = true;
	   mesh  = new GuiMesh(ss,row,col,width, height);
	   iCameFrom = ss;
	   textured = true;
	   fromSpriteSheet = false;
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


}
