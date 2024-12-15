package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
	public static List<String[]> readCSV(String filePath) {
	    List<String[]> data = new ArrayList<>();
	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        br.readLine(); // Skip header if exists
	        while ((line = br.readLine()) != null) {
	            String[] values = line.split(","); // Use comma as delimiter
	            data.add(values);
	        }
	    } catch (IOException e) {
	        System.out.println("Error reading file: " + filePath);
	        e.printStackTrace();
	    }
	    return data;
	}
}
