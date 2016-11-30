package com.scale.images;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Create2DArrayAuthors {
	public static void main(String args[]) {
		LinkedHashMap <String, LinkedHashMap <String,Integer>> array = new LinkedHashMap <String, LinkedHashMap <String,Integer>>(); 
		try {
			String filePath = "C:\\Users\\ADMIN\\Desktop\\Result.txt";  
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
			String line;
	        while ((line = br.readLine()) != null) {
	        	String[] parseLine = line.split(",");
	        	int length = parseLine.length;
	        	LinkedHashMap <String,Integer> arrayChildren = new LinkedHashMap <String,Integer>();
	        	LinkedHashMap <String, Integer> value = array.get(parseLine[0].toString());
	        	if (value == null) {
	        		array.put(parseLine[0].toString(), arrayChildren);
	        	}
	        	for (int i = 1; i < length ; i++) {
	        		String a = "";
	        		int[] is = {2,1};
	        		HashMap<String, Integer> value1 = array.get(parseLine[i].toString());
		        	if (value == null) {
		        		array.put(parseLine[i].toString(), new LinkedHashMap <String,Integer>());
		        		arrayChildren.put(parseLine[i].toString(), 1);
		        	} else {
		        		Integer value2 = value.get(parseLine[i].toString());
		        		if (value2 == null) {
		        			arrayChildren.put(parseLine[i].toString(), 1);
		        		} else {
		        			arrayChildren.put(parseLine[i].toString(), value2 + 1);
		        		}
		        	}
	        	}
	        	array.put(parseLine[0].toString(), arrayChildren);
	        }
	        br.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println(e);
	    }
		int twoDimdArrayLength = array.size();
		String[][] twoDimdArray = new String[twoDimdArrayLength][twoDimdArrayLength];
		 for (String key: array.keySet()) {
            System.out.println("key : " + key);
            System.out.println("value : " + array.get(key));
        }
	}
}
