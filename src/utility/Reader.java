package utility;

import java.io.File;

import java.util.Scanner;

public class Reader {
   public static String getCode(String codeLocation)throws Exception{
 	  String codeAsString = new Scanner(new File("src/shader/"+codeLocation)).useDelimiter("\\A").next();
 	  return codeAsString;
   }
   

}
