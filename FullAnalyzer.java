import java.util.*;

import java.io.*;
import jxl.*;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

/**
 *	A class which collects and tests bitStrings and prints the data in a variety of ways.
 */
public class FullAnalyzer{
		
	public static void main(String[] args){
		TextIO.readFile("processed.txt");
		TextIO.writeFile("results.txt");
		
		//Use for getting pre-collected strings
		ArrayList<BitString> bitStrings = new ArrayList<BitString>();
		while (!TextIO.eof()){
			bitStrings.add(new BitString(TextIO.getln()));
		}
		
//		Use for randomly-generated strings
//		for (int i = 0; i < 119; i++){
//			bitStrings.add(new BitString(StringGenerator.generateString(200)));
//		}

		sort(bitStrings, 0, bitStrings.size() - 1);
		
		writeToExcel(bitStrings);
		
		//printing results to console as well
		TextIO.putln("ID\t\t\t\tChi-Square (3)\t\t\tChi-Square (4)\tNumber of Runs\tLongest Run");
		for (int i = 0; i < bitStrings.size(); i++){
			BitString b = bitStrings.get(i);
			TextIO.putln(b.getID() + "\t\t\t" + b.getChi(3) + "\t\t\t" + b.getChi(4) + "\t\t\t" + b.getNumRuns() + "\t\t\t" + b.getLongestRun());
		}
		TextIO.putln();
		for (int i = 0; i < bitStrings.size(); i++){
			BitString b = bitStrings.get(i);
			System.out.println(b.getBitString());
			double[] runningChi = b.getRunningChi(3);
			TextIO.putln(b.getID());
			for (int j = 0; j < runningChi.length; j++){
				TextIO.putln(runningChi[j]);
			}
			TextIO.putln();
		}
	}
	
	
	/**
	 * Takes BitString objects, runs some tests, and puts the results in an Excel file named output.xls
	 * 
	 * @param bitStrings 
	 */
	public static void writeToExcel(ArrayList<BitString> bitStrings){
		try{
			WritableWorkbook workbook = Workbook.createWorkbook(new File("output.xls"));
			WritableSheet sheet1 = workbook.createSheet("Summary", 0);
			
			//making headers
			Label label1 = new Label(0, 0, "ID");
			sheet1.addCell(label1);
			Label label2 = new Label(1, 0, "Chi-Square (3)");
			sheet1.addCell(label2);
			Label label3 = new Label(2, 0, "Chi-Square (4)");
			sheet1.addCell(label3);
			Label label4 = new Label(3, 0, "Num Runs");
			sheet1.addCell(label4);
			Label label5 = new Label(4, 0, "Longest Run");
			sheet1.addCell(label5);
			Label label6 = new Label(5, 0, "Max Recent Chi (3)");
			sheet1.addCell(label6);
			
			//making first sheet, a summary of data
			for (int i = 0; i < bitStrings.size(); i++){
				BitString b = bitStrings.get(i);
				Number id = new Number(0, i + 1, Integer.parseInt(b.getID()));
				Number chiThree = new Number(1, i + 1, b.getChi(3));
				Number chiFour = new Number(2, i + 1, b.getChi(4));
				Number numRuns = new Number(3, i + 1, b.getNumRuns());
				Number longest = new Number(4, i + 1, b.getLongestRun());
				Number maxRecent = new Number(5, i + 1, b.getMaxRecent(3));
				sheet1.addCell(id);
				sheet1.addCell(chiThree);
				sheet1.addCell(chiFour);
				sheet1.addCell(numRuns);
				sheet1.addCell(longest);
				sheet1.addCell(maxRecent);
			}
			
			//making sheet 2, a detailed Chi-3 breakdown
			WritableSheet sheet2 = workbook.createSheet("Chi-3", 1);
			
			for (int i = 0; i < bitStrings.size(); i++){
				BitString b = bitStrings.get(i);
				double[] runningChi = b.getRunningChi(3);
				Label label = new Label(i, 0, b.getID());
				sheet2.addCell(label);
				for (int j = 0; j < runningChi.length; j++){
					Number num = new Number(i, j + 1, runningChi[j]);
					sheet2.addCell(num);
				}
			}
			
			//making sheet 3, a detailed Chi-4 breakdown
			WritableSheet sheet3 = workbook.createSheet("Chi-4", 2);
			
			for (int i = 0; i < bitStrings.size(); i++){
				BitString b = bitStrings.get(i);
				double[] runningChi = b.getRunningChi(4);
				Label label = new Label(i, 0, b.getID());
				sheet3.addCell(label);
				for (int j = 0; j < runningChi.length; j++){
					Number num = new Number(i, j + 1, runningChi[j]);
					sheet3.addCell(num);
				}
			}
			
			//making sheet 4, which just prints the strings
			WritableSheet sheet4 = workbook.createSheet("Strings", 3);
			for (int i = 0; i < bitStrings.size(); i++){
				BitString b = bitStrings.get(i);
				Label label = new Label(0, i, b.getID());
				sheet4.addCell(label);
				label = new Label(1, i, b.getBitString());
				sheet4.addCell(label);
			}
			
			//making sheet 5, a detailed recent chi-square breakdown
			WritableSheet sheet5 = workbook.createSheet("Recent Chi-3", 4);
			for (int i = 0; i < bitStrings.size(); i++){
				BitString b = bitStrings.get(i);
				double[] recentChi = b.getRecentChi(3);
				Label label = new Label(i, 0, b.getID());
				System.out.println(b.getID());
				sheet5.addCell(label);
				for (int j = 0; j < recentChi.length; j++){
					Number num = new Number(i, j + 1, recentChi[j]);
					sheet5.addCell(num);
					System.out.println(recentChi[j]);
				}
			}
			
			
			workbook.write();
			workbook.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Simple recursive selection sort
	 */
	public static void sort(ArrayList<BitString> bitStrings, int lo, int hi){
		if (lo == hi) return;
		swap(bitStrings, lo, findMin(bitStrings, lo, hi));
		sort(bitStrings, lo + 1, hi);
	}
	
	//part of sort(...)
	public static void swap(ArrayList<BitString> bitStrings, int a, int b){
		if (b > a) {
			BitString temp = bitStrings.get(a);
			bitStrings.set(a, bitStrings.get(b));
			bitStrings.set(b, temp);
		}
	}
	
	//part of sort(...)
	public static int findMin(ArrayList<BitString> bitStrings, int lo, int hi){
		int min = lo;
		for (int i = lo; i <= hi; i++){
			if (bitStrings.get(i).getChi(3) < bitStrings.get(min).getChi(3)) min = i;
		}
		return min;
	}
	
	//part of sort(...)
	public static double findMaxValue(double[] arr){
		double max = arr[0];
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i] > max) max = arr[i];
		}
		return max;
	}
}