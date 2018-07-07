package utils;

public class StringUtils {
	public static boolean isNull(String s) {
		if (s == null || s.equals("") )
			return true;
		else
			return false;
	}
	
}
