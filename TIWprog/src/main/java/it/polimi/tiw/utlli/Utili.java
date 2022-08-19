package it.polimi.tiw.utlli;

public class Utili {

	private static String regexCancel="\"£$%&/()=+-*//";
	public static void cleanString(String userInput) {
		for(int i=0;i<regexCancel.length();i++) {
			userInput.replaceAll(regexCancel.charAt(i)+"","");//thanx java very cool
		}
	}
}
