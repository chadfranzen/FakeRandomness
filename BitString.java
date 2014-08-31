/**
 * A class which holds a bit string and has a number of methods for testing the string.
 */
public class BitString{
	/**
	 * Holds the actual bit string
	 */
	public String bitString;
	/**
	 * Holds the string's unique ID
	 */
	public String id;
	/**
	 * Each index i holds the chiSquare value for blocks of size i+1. Defined up to 10.
	 */
	public double[] chiSquare;
	/**
	 * Holds data for the running chi-square test. For use with generateRunningChi(...)
	 */
	public double[] runningChi; 
	/**
	 * Holds data for the recent chi-square test. For use with generateRecentChi(...)
	 */
	public double[] recentChi;
	/**
	 * Holds number of occurrences of different blocks. For use with generateBlockFrequencies(...)
	 */
	public int[] blockFrequencies;
	
	/**
	 * The length of the string
	 */
	public int length;
	/**
	 * The number of runs in the string
	 */
	public int numRuns; 
	/**
	 * The longest continuous run of 0 or 1 in the string
	 */
	public int longestRun;
	/**
	 * The minimum length at which a running chi-square test can be run
	 */
	public static int minRunningValue = 50 ;
	/**
	 * The minimum length at which a recent chi-square test can be run
	 */
	public static int recentValue = 50;
	/**
	 * Used for making unique IDs for each string
	 */
	public static int idGenerator = 10001;
	
	/**
	 * Takes a raw bit string, gives it an ID, and sets up some basic tests.
	 */
	public BitString(String bitString){
		this.bitString = bitString;
		id = Integer.toString(idGenerator++);
		length = this.bitString.length();
		chiSquare = new double[10];
		
		runAnalysis();
		for(int i = 0; i < chiSquare.length; i++){
			chiSquare[i] = blockAnalysis(i + 1);
		}
		
	}
	
	/**
	 * Does a run analysis test and stores results in numRuns and longestRun
	 */
	public void runAnalysis(){
		int runStart = 0;
		numRuns = 1;
		longestRun = 0;
		for (int i = 1; i <= bitString.length(); i++){
			if (i == bitString.length() || (bitString.charAt(i) != bitString.charAt(i - 1))){
				numRuns++;
				if ( (i - runStart) > longestRun) longestRun = (i - runStart);
				runStart = i;
			}
		}
	}
	
	/**
	 * @param blockLength
	 * @return
	 */
	public double blockAnalysis(int blockLength){
		String s = bitString;
		int occurrences[] = new int[(int)Math.pow(2, blockLength)];
		while(blockLength <= s.length()){
			String block = s.substring(0, blockLength);
			occurrences[Integer.parseInt(block, 2)]++;
			if (blockLength <= s.length())
				s = s.substring(blockLength);
			else
				s = "";
		}
		
		int numBlocks = length / blockLength;
		double expected = (double)numBlocks / occurrences.length;
		
		return ChiSquare.getValue(occurrences, expected);
	}

	/**
	 * Finds the number of occurrences of each possible block of the specified length. Stores result in blockFrequencies[]
	 * 
	 * @param blockLength
	 */
	public void generateBlockFrequencies(int blockLength){
		String s = bitString;
		int occurrences[] = new int[(int)Math.pow(2, blockLength)];
		while(blockLength <= s.length()){
			String block = s.substring(0, blockLength);
			occurrences[Integer.parseInt(block, 2)]++;
			if (blockLength <= s.length())
				s = s.substring(blockLength);
			else
				s = "";
		}
		blockFrequencies = occurrences;
	}
	
	
	/**
	 * Does a running chi-square test on blocks of the specified length. Stores result in runningChi[]
	 * 
	 * @param blockLength
	 */
	public void generateRunningChi(int blockLength){
		if (bitString.length() < minRunningValue){
			runningChi = new double[0];
			return;
		}
		runningChi = new double[length - minRunningValue];
		for (int i = 0; i < runningChi.length; i++){
			BitString b = new BitString(bitString.substring(0, minRunningValue + i + 1));
			runningChi[i] = b.getChi(blockLength);
		}
	}
	
	/**
	 * Does a recent chi-square test on blocks of specified length. Stores result in recentChi[]
	 * 
	 * @param blockLength
	 */
	public void generateRecentChi(int blockLength){
		if (bitString.length() < recentValue){
			recentChi = new double[0];
			return;
		}
		recentChi = new double[length - recentValue];
		for (int i = 0; i < recentChi.length; i++){
			BitString b = new BitString(bitString.substring(i, i + 50));
			recentChi[i] = b.getChi(blockLength);
		}
	}
	
	/**
	 * Gets the results of a basic chi-square test on the string with the specified block length
	 * 
	 * @param blockLength
	 * @return The chi-square value
	 */
	public double getChi(int blockLength){
		if (blockLength < 1 || blockLength > chiSquare.length) return -1;
		return chiSquare[blockLength - 1];
	}
	
	public String getBitString(){return bitString;}
	public String getID(){return id;}
	public int getLength(){return length;}
	public int getNumRuns(){return numRuns;}
	public int getLongestRun(){return longestRun;}
	public double[] getRunningChi(int blockLength){
		generateRunningChi(blockLength);
		return runningChi;
	}
	public double[] getRecentChi(int blockLength){
		generateRecentChi(blockLength);
		return recentChi;
	}
	public int[] getBlockFrequencies(int blockLength){
		generateBlockFrequencies(blockLength);
		return blockFrequencies;
	}
	public double getMaxRecent(int blockLength){
		return findMaxValue(getRecentChi(blockLength));
	}
	
	public static double findMaxValue(double[] arr){
		double max = arr[0];
		for (int i = 0; i < arr.length; i++)
		{
			if (arr[i] > max) max = arr[i];
		}
		return max;
	}
	
}