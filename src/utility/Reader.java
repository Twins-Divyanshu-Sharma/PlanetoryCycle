package utility;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

import javax.rmi.CORBA.Util;

public class Reader {
/*	public static String getCode(String codeLocation)throws Exception{
	  String codeAsString = new Scanner(new File("src/shader/"+codeLocation)).useDelimiter("\\A").next();
 	  return codeAsString;
   }*/
	
	public String getCodeString(String codeLoc)throws Exception{

		//InputStream input = getClass().getResourceAsStream("..");
		 String codeAsString = new Scanner(new File("res/shaders/"+codeLoc)).useDelimiter("\\A").next();
		
	 	  return codeAsString;
	}
   

}
