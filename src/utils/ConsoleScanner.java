package utils;

import java.util.Scanner;

public class ConsoleScanner {
	static Scanner input;

	public static Scanner getInput() {
		if (input == null)
			input = new Scanner(System.in);
		return input;
	}
}
