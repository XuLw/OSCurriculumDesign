package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import bean.User;
import utils.ConsoleScanner;
import utils.Printer;
import utils.StringUtils;

public class Main {

	public static final int OK = 12;
	public static final int FAILED = 13;
	public static final int INIT = 10;
	public static final int EXIT = -99;

	Scanner input;
	Login mLogin;
	User currentUser;
	int currentLevel;
	ArrayList<String> dirs; // ·��

	public Main() {
		input = ConsoleScanner.getInput();
		dirs = new ArrayList<>();
		currentLevel = 0;
		FileController.readDataFromFile();

		mLogin = new Login();
		if (showLogin() == this.OK)
			fileOper();
		FileController.writeDataToFile();
	}

	public int showLogin() {
		int state = INIT;
		String command = "";
		do {
			command = getCommand(); // ��ȡ����

			switch (command.toLowerCase()) {
			case Constant.REGISTER:
				// ��ע��
				if (mLogin.register() == Login.OK) {
					// ע��ɹ�
					FileController.getSuperBlock().addUser(mLogin.getUser());// ���û���ӵ��û���
					System.out.println("registered successfully��");
				} else {
					// ����ע��

				}
				break;
			case Constant.LOGIN:
				// ��¼
				if (mLogin.login() == Login.OK) {
					// ��¼�ɹ�
					currentUser = mLogin.getUser();
					dirs.add(currentUser.getName());
					System.out.println("login successfully��");
					state = this.OK;
				} else {
					// ������¼
				}
				break;
			case Constant.HELP:
				// ��������б�
				Printer.showLoginHelp();
				break;
			case Constant.EXIT:
				// �˳�����

				state = this.EXIT;
				break;

			default:
				// δʶ������
				System.out.println("'" + command + "' not found!");
				break;
			}

			if (state != INIT)
				break;

		} while (true);

		return state;
	}

	public void fileOper() {
		int state = INIT;
		String command = "";
		do {
			command = getCommandP(); // ��ȡ����
			String[] pas = command.split(" ");

			switch (pas[0].toLowerCase()) {
			case Constant.CREATE:
				if (!StringUtils.isNull(pas[1])) {
					// �½��ļ�
					switch (FileManager.createFile(getPath() + pas[1])) {
					case FileManager.NAME_CONFLICT:
						System.out.println("file " + pas[1] + "exists!");
						break;
					case FileManager.OK:
						System.out.println("created !");
						break;
					default:
						System.out.println("create unknow !!");
						break;
					}
				}
				break;
			case Constant.OPEN:
				// ���ļ�
				if (!StringUtils.isNull(pas[1])) {
					switch (FileManager.openFile(getPath() + pas[1])) {
					case FileManager.NOT_FOUND:
						System.out.println("file not exists!");
						break;
					case FileManager.OK:
						break;
					default:
						System.out.println("open unknow !!");
						break;
					}
				}
				break;
			case Constant.LS:
				// �鿴��ǰĿ¼�µ��ļ��ļ���Ϣ
				FileManager.listFile(getPath());
				break;
			case Constant.MAKEDIR:
				// ����Ŀ¼
				if (!StringUtils.isNull(pas[1])) {
					// �½��ļ�
					switch (FileManager.makeDir(getPath() + pas[1] + "/")) {
					case FileManager.NAME_CONFLICT:
						System.out.println("dir " + pas[1] + "exists!");
						break;
					case FileManager.OK:
						System.out.println("created !");
						break;
					default:
						System.out.println("mkdir unknow !!");
						break;
					}
				}
				break;
			case Constant.CD:
				// ����Ŀ¼
				if (!StringUtils.isNull(pas[1])) {
					if ("..".equals(pas[1])) {
						// ������һ��
						if (dirs.size() > 1)
							dirs.remove(dirs.size() - 1);
					} else {
						switch (FileManager.cdDir(getPath() + pas[1] + "/")) {
						case FileManager.OK:
							// ����ǰ��Ŀ¼���뵽·��ջ��
							dirs.addAll(Arrays.asList(pas[1].split("/")));
							break;
						case FileManager.NOT_FOUND:
							System.out.println("dir not exists!");
							break;
						default:
							System.out.println("cd unknow !!");
							break;
						}
					}
				}
				break;
			case Constant.RMD:
				// ɾ��Ŀ¼
				if (!StringUtils.isNull(pas[1])) {
					switch (FileManager.removeDir(getPath() + pas[1] + "/")) {
					case FileManager.NOT_FOUND:
						System.out.println("dir not exists!");
						break;
					case FileManager.OK:
						System.out.println("deleted!");
						break;
					default:
						System.out.println("rmd unknow !!");
						break;
					}
				}
				break;
			case Constant.RMF:
				// ɾ���ļ�
				if (!StringUtils.isNull(pas[1])) {
					switch (FileManager.removeFile(getPath() + pas[1])) {
					case FileManager.NOT_FOUND:
						System.out.println("file not exists!");
						break;
					case FileManager.OK:
						System.out.println("deleted!");
						break;
					default:
						System.out.println("rmf unknow !!");
						break;
					}
				}
				break;
			case Constant.HELP:
				Printer.showFileHelp();
				break;
			case Constant.EXIT:
				state = EXIT;
				break;
			default:
				break;
			}

			if (state != INIT) {
				break;
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
