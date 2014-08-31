
/**
 * Simple class for generating random bit strings
 */
public class StringGenerator {
	/**
	 * Generates a random bit string
	 * 
	 * @param length The length of the string (not including ID)
	 * @return The generated string
	 */
	public static String generateString(int length){
		String bitString = "";
		while (bitString.length() < length) bitString += (int)(Math.random() * 2);
		return bitString;
	}

}
