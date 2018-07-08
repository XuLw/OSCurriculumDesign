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
	ArrayList<String> dirs; // ·��

	public Main() {

		try {
			System.out.print("w");
			Thread.sleep(100);
			System.out.print("e");
			Thread.sleep(100);
			System.out.print("l");
			Thread.sleep(100);
			System.out.print("c");
			Thread.sleep(100);
			System.out.print("o");
			Thread.sleep(100);
			System.out.print("m");
			Thread.sleep(100);
			System.out.print("e");
			Thread.sleep(100);
			System.out.println(" !\n");
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		int state = INIT;
		input = ConsoleScanner.getInput();
		dirs = new ArrayList<>();
		FileController.readDataFromFile();
		mLogin = new Login();
		do {
			if ((state = showLogin()) == this.OK)
				state = fileOper();
			if (state == EXIT)
				break;
		} while (true);
		FileController.writeDataToFile();

		System.out.print("bye .");
		try {
			Thread.sleep(300);
			System.out.print(" .");
			Thread.sleep(300);
			System.out.print(" .");
			Thread.sleep(300);
			System.out.print(" .");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
					System.out.println("msg: registered successfully��");
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
					System.out.println("msg: login successfully��");
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
			case Constant.FORMAT:
				// ��ʽ��
				if (FileController.rootFormat() == FileController.OK)
					System.out.println("msg: format successfully !");
				break;
			case Constant.ROOTCONFIG:
				Printer.printSeparator();
				FileController.getSuperBlock().printUsers();
				Printer.printSeparator();
				break;
			default:
				// δʶ������
				System.out.println("err: '" + command + "' not found!");
				break;
			}

			if (state != INIT)
				break;

		} while (true);

		return state;
	}

	public int fileOper() {
		int state = INIT;
		String command = "";

		do {
			command = getCommandP(); // ��ȡ����
			String[] pas = command.split(" ");

			switch (pas[0].toLowerCase()) {
			case Constant.CREATE:
				if (pas.length > 1 && !StringUtils.isNull(pas[1])) {
					// �½��ļ�
					switch (FileManager.createFile(getPath() + pas[1])) {
					case FileManager.NAME_CONFLICT:
						System.out.println("err: file " + pas[1] + "exists!");
						break;
					case FileManager.OK:
						System.out.println("msg: created !");
						break;
					default:
						System.out.println("err: create unknow !!");
						break;
					}
				} else {
					Printer.printSeparator();
					System.out.println("noti: create  <filename>");
					Printer.printSeparator();
				}
				break;
			case Constant.OPEN:
				// ���ļ�
				if (pas.length > 1 && !StringUtils.isNull(pas[1])) {
					switch (FileManager.openFile(getPath() + pas[1])) {
					case FileManager.NOT_FOUND:
						System.out.println("err: file not exists!");
						break;
					case FileManager.OK:
						break;
					default:
						System.out.println("err: open unknow !!");
						break;
					}
				} else {
					Printer.printSeparator();
					System.out.println("noti: open  <filename>");
					Printer.printSeparator();
				}
				break;
			case Constant.LS:
				// �鿴��ǰĿ¼�µ��ļ��ļ���Ϣ
				FileManager.listFile(getPath());
				break;
			case Constant.MAKEDIR:
				// ����Ŀ¼
				if (pas.length > 1 && !StringUtils.isNull(pas[1])) {
					// �½��ļ�
					switch (FileManager.makeDir(getPath() + pas[1] + "/")) {
					case FileManager.NAME_CONFLICT:
						System.out.println("err: dir " + pas[1] + " exists!");
						break;
					case FileManager.OK:
						System.out.println("msg: created !");
						break;
					default:
						System.out.println("err: mkdir unknow !!");
						break;
					}
				} else {
					Printer.printSeparator();
					System.out.println("noti: mkdir  <dirname>");
					Printer.printSeparator();
				}
				break;
			case Constant.CD:
				// ����Ŀ¼
				if (pas.length > 1 && !StringUtils.isNull(pas[1])) {
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
							System.out.println("err: dir not exists!");
							break;
						default:
							System.out.println("msg: cd unknow !!");
							break;
						}
					}
				} else {
					Printer.printSeparator();
					System.out.println("noti: cd  <dirname>");
					Printer.printSeparator();
				}
				break;
			case Constant.RMD:
				// ɾ��Ŀ¼
				if (pas.length > 1 && !StringUtils.isNull(pas[1])) {
					switch (FileManager.removeDir(getPath() + pas[1] + "/")) {
					case FileManager.NOT_FOUND:
						System.out.println("err: dir not exists!");
						break;
					case FileManager.OK:
						System.out.println("msg: deleted!");
						break;
					default:
						System.out.println("err: rmd unknow !!");
						break;
					}
				} else {
					Printer.printSeparator();
					System.out.println("noti: rmd  <dirname>");
					Printer.printSeparator();
				}
				break;
			case Constant.RMF:
				// ɾ���ļ�
				if (pas.length > 1 && !StringUtils.isNull(pas[1])) {
					switch (FileManager.removeFile(getPath() + pas[1])) {
					case FileManager.NOT_FOUND:
						System.out.println("err: file not exists!");
						break;
					case FileManager.OK:
						System.out.println("msg: deleted!");
						break;
					default:
						System.out.println("err: rmf unknow !!");
						break;
					}
				} else {
					Printer.printSeparator();
					System.out.println("noti: rmf  <filename>");
					Printer.printSeparator();
				}
				break;
			case Constant.LOGOUT:
				FileController.writeDataToFile();// �Ƚ��б���
				dirs.clear();// �����ǰջ
				state = OK;
				break;
			case Constant.CONFIG:
				Printer.printSeparator();
				FileController.getSuperBlock().printBitmap();
				Printer.printSeparator();
				break;
			case Constant.HELP:
				Printer.showFileHelp();
				break;

			case Constant.EXIT:
				state = EXIT;
				break;
			case Constant.RESET:
				// ɾ����ǰ�û� ɾ����Ŀ¼
				switch (FileManager.removeDir(dirs.get(0) + "/")) {
				case FileManager.OK:
					// ɾ���û�
					FileController.getSuperBlock().removeUser(currentUser);
					dirs.clear();
					System.out.println("msg: deleted!");
					break;
				}
				state = OK;
				break;
			default:
				// δʶ������
				System.out.println("err: '" + command + "' not found!");
				break;
			}

			if (state != INIT) {
				break;
			}
		} while (true);
		return state;

	}

	public String getCommand() {
		String command = "";
		do {
			System.out.print("enter the commad ('help' for help):   ");
			if (input.hasNext()) {
				command = input.nextLine().trim();
				if (StringUtils.isNull(command))
					System.out.println("err: command can't be empty!");
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
					System.out.println("err: command can't be empty!");
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

}
