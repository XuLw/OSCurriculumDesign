package main;

import java.util.ArrayList;
import java.util.Scanner;

import bean.Block;
import bean.SuperBlock;
import bean.User;
import utils.StringUtils;

public class Main {
	Scanner input;

	Login mLogin;
	User currentUser;
	int currentLevel;
	ArrayList<String> dirs; // 路径

	public Main() {
		input = new Scanner(System.in);
		dirs = new ArrayList<>();
		currentLevel = 0;
		FileController.readDataFromFile();

		mLogin = new Login();
		showLogin();
		fileOper();
		// System.out.println("register");
		// System.out.println("login");
		// System.out.println("exit");

	}

	public void showLogin() {
		Help.showLoginHelp();
		String command = "";
		do {
			command = getCommand(); // 获取命令
			if (Constant.REGISTER.equals(command)) {
				// 新注册
				if (mLogin.register() == Login.OK) {
					// 注册成功
					FileController.getSuperBlock().addUser(mLogin.getUser());// 将用户添加到用户表
					System.out.println("注册成功！");
				} else {
					// 放弃注册

				}

			} else if (Constant.LOGIN.equals(command)) {
				// 登录
				if (mLogin.login() == Login.OK) {
					// 登录成功
					currentUser = mLogin.getUser();
					dirs.add(currentUser.getName());
					System.out.println("登陆成功！");
					break;
				} else {
					// 放弃登录
				}

			} else if (Constant.EXIT.equals(command)) {
				// 退出命令
				FileController.writeDataToFile();
				return;
			} else if (Constant.HELP.equals(command)) {
				// 输出命令列表
				Help.showLoginHelp();
			} else {
				// 未识别命令
				System.out.println(command + " not found!");
			}
		} while (true);
	}

	public void fileOper() {
		String command = "";
		Help.showFileHelp();
		do {

			FileController.getSuperBlock().printDetail();

			command = getCommandP();
			String[] pas = command.split(" ");
			if (Constant.CREATE.equals(pas[0])) {
				// 创建文件
				if (!StringUtils.isNull(pas[1])) {
					// 新建文件
					FileManager.createFile(getPath() + pas[1]);
				}

			} else if (Constant.OPEN.equals(pas[0])) {
				// 打开文件

			} else if (Constant.LS.equals(pas[0])) {
				// 查看当前目录下的文件文件信息
				FileManager.listFile(getPath());

			} else if (Constant.MAKEDIR.equals(pas[0])) {
				// 创建目录
				if (!StringUtils.isNull(pas[1])) {
					// 新建文件
					FileManager.makeDir(getPath() + pas[1] + "/");
				}

			} else if (Constant.CD.equals(pas[0])) {
				if (!StringUtils.isNull(pas[1])) {
					// 进入目录

				}
			} else if (Constant.HELP.equals(pas[0])) {
				Help.showFileHelp();
			}

		} while (true);
	}

	public String getCommand() {
		String command = "";
		do {
			System.out.print("enter the commad ('help' for help):   ");
			if (input.hasNext()) {
				command = input.nextLine().trim();
				if (StringUtils.isNull(command))
					System.out.println("command can't be empty!");
				else
					break;
			}
		} while (true);
		return command;
	}

	public String getCommandP() {
		String command = "";
		do {
			System.out.print(getPath() + ">:   ");
			if (input.hasNext()) {
				command = input.nextLine().trim();
				if (StringUtils.isNull(command))
					System.out.println("command can't be empty!");
				else
					break;
			}
		} while (true);
		return command;
	}

	public String getPath() {
		String path = "";
		for (String s : dirs) {
			path += s + "/";
		}
		return path;
	}

	public static void main(String[] args) {
		new Main();
	}

}
