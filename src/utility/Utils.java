package utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	
	   public static List<String> readAllLines(String fileName) throws Exception {
	       List<String> list = new ArrayList<>();
	       try (BufferedReader br = new BufferedReader(new FileReader("resourse/"+fileName))) {
	           String line;
	           while ((line = br.readLine()) != null) {
	               list.add(line);
	           }
	       }
	       return list;
	   }
	   
	   public static float[] listToArray(List<Float> list) {
	        int size = list != null ? list.size() : 0;
	        float[] floatArr = new float[size];
	        for (int i = 0; i < size; i++) {
	            floatArr[i] = list.get(i);
	        }
	        return floatArr;
	    }
}
