package main;

public class Constant {

	public static final String FILE_PATH = "src/main/DATA.dat";
	public static final int SYSTEM_SIZE = 1024; // 系统块数
	public static final int MAX_USER = 8; // 最大用户
	public static final int FILE_MAX_BLOCK = 3; // 文件最多块
	public static final int BLOCK_SIZE = 256; // 块大小
	public static final int NOMORE_FILE = -1;

	// 命令
	public static final String LOGIN = "login";
	public static final String REGISTER = "register";
	public static final String LOGOUT = "logout";
	public static final String CREATE = "create";
	public static final String OPEN = "open";
	public static final String HELP = "help";
	public static final String CONFIG = "config";
	public static final String LS = "ls";
	public static final String MAKEDIR = "makedir";
	public static final String CD = "cd";
	public static final String EXIT = "exit";
}
