package utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	

	   
	   public static float[] listToArray(List<Float> list) {
	        int size = list != null ? list.size() : 0;
	        float[] floatArr = new float[size];
	        for (int i = 0; i < size; i++) {
	            floatArr[i] = list.get(i);
	        }
	        return floatArr;
	    }
}
