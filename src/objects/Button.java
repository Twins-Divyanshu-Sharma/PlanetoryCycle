package objects;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Button extends GuiObject {
	   public Button(Vector2f position, Vector4f color, float width, float height){  // untextured
	       super(position,color,width,height);
	   }
	   
	   public Button(Vector2f position, String texLoc, float width, float height) throws Exception{
		   super(position,texLoc,width,height);
	   }
	   
	   public Button(Vector2f position, SpriteSheet ss, int row, int col, float width, float height) throws Exception{  // untextured
		   super(position,ss,row,col,width,height);
	   }
}
