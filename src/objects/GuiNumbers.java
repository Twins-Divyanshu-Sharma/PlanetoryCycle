package objects;

import org.joml.Vector2f;

public class GuiNumbers {
   private int length = 5;
   private GuiObject[] letters;
   private SpriteSheet spriteSheet;
   
   private float letterHeight = 0.125f * 0.6f;
   private float letterWidth = 0.0625f * 0.6f;
   
   private int[] row;
   private int[] col;
   
   private Vector2f position;
   
   public GuiNumbers(SpriteSheet spriteSheet, Vector2f position) throws Exception{
	   letters = new GuiObject[length];
	   this.spriteSheet = spriteSheet;
	   row =  new int[length];
	   col = new int[length];
	   this.position = position;
	   prepareAll();
   }
   public GuiNumbers(SpriteSheet spriteSheet, Vector2f position, int length) throws Exception{
	   this.length = length;
	   this.spriteSheet = spriteSheet;
	   row =  new int[length];
	   col = new int[length];
	   this.position = position;
	   prepareAll();
   }
   
   
   private void prepareAll() throws Exception{
	   for(int i=0; i<length; i++){
		   Vector2f pos = new Vector2f();
		   pos.x = position.x + letterWidth*i;
		   pos.y = position.y;
		   letters[i] = new GuiObject(pos,spriteSheet,0,0,letterWidth,letterHeight);
	   }
   }
   
   public void change(int digitPos){
	   float[] tempTexCoord = {
			   0+spriteSheet.getDw()*(float)(col[digitPos]), 0 + spriteSheet.getDh()*(float)(row[digitPos]),  // top left
			   0+spriteSheet.getDw()*(float)(col[digitPos]+1), 0 + spriteSheet.getDh()*(float)(row[digitPos]),  // top right
			   0+spriteSheet.getDw()*(float)(col[digitPos]), 0 + spriteSheet.getDh()*(float)(row[digitPos] +1), // bottom left
			   0+spriteSheet.getDw()*(float)(col[digitPos]+1),  0 + spriteSheet.getDh()*(float)(row[digitPos] +1),  // bottom right
	   };
	   letters[digitPos].getMesh().changeTextures(tempTexCoord);
   }
   
   public void setAndChange(int digit, int digitPos){
	   setDigit(digit,digitPos);
	   change(digitPos);
   }
   
   public GuiObject[] getLetters(){
	   return letters;
   }

   
   //////////////////////////// HARD CODED MAPPING , BECAUSE WHY NOT /////////////////////////
    public void setDigit(int digit, int digitPos){
    	switch(digit){
    	case 0:
    		col[digitPos] = 0;
    		row[digitPos] = 0;
    		break;
    	case 1:
    		col[digitPos] = 1;
    		row[digitPos] = 0;
    		break;
    	case 2:
    		col[digitPos] = 2;
    		row[digitPos] = 0;
    		break;
    	case 3:
    		col[digitPos] = 3;
    		row[digitPos] = 0;
    		break;
    	case 4:
    		col[digitPos] = 0;
    		row[digitPos] = 1;
    		break;
    	case 5:
    		col[digitPos] = 1;
    		row[digitPos] = 1;
    		break;
    	case 6:
    		col[digitPos] = 2;
    		row[digitPos] = 1;
    		break;
    	case 7:
    		col[digitPos] = 3;
    		row[digitPos] = 1;
    		break;
    	case 8:
    		col[digitPos] = 0;
    		row[digitPos] = 2;
    		break;
    	case 9:
    		col[digitPos] = 1;
    		row[digitPos] = 2;
    		break;
    	}
    }
    
    
   
   public int getLength(){
	   return length;
   }
   
   
   
   
}
