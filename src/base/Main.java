package base;

import java.io.File;

public class Main {
     public static void main(String[] args){
    	 //System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
    	 Engine engine = new Engine();
    	 engine.init();
   
     }
}