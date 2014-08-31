
/**
 * A class for doing general Chi-Square test calculations with uniform expected probability
 */
public class ChiSquare{
	
	/**
	 * Runs a chi-square calculation on a set of observed outcomes.
	 * 
	 * @param observed An array containing the number of observed occurrences of each event
	 * @param expected The expected number of occurrences of each event
	 * @return The chi-square value
	 */
	public static double getValue(int[] observed, double expected){
		double value = 0;
		for (int i = 0; i < observed.length; i++){
			value += Math.pow(observed[i] - expected, 2);
		}
		value /= expected;
		return value;
	}
}