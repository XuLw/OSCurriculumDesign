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
	ArrayList<String> dirs; // ·��

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
			command = getCommand(); // ��ȡ����
			if (Constant.REGISTER.equals(command)) {
				// ��ע��
				if (mLogin.register() == Login.OK) {
					// ע��ɹ�
					FileController.getSuperBlock().addUser(mLogin.getUser());// ���û���ӵ��û���
					System.out.println("ע��ɹ���");
				} else {
					// ����ע��

				}

			} else if (Constant.LOGIN.equals(command)) {
				// ��¼
				if (mLogin.login() == Login.OK) {
					// ��¼�ɹ�
					currentUser = mLogin.getUser();
					dirs.add(currentUser.getName());
					System.out.println("��½�ɹ���");
					break;
				} else {
					// ������¼
				}

			} else if (Constant.EXIT.equals(command)) {
				// �˳�����
				FileController.writeDataToFile();
				return;
			} else if (Constant.HELP.equals(command)) {
				// ��������б�
				Help.showLoginHelp();
			} else {
				// δʶ������
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
				// �����ļ�
				if (!StringUtils.isNull(pas[1])) {
					// �½��ļ�
					FileManager.createFile(getPath() + pas[1]);
				}

			} else if (Constant.OPEN.equals(pas[0])) {
				// ���ļ�

			} else if (Constant.LS.equals(pas[0])) {
				// �鿴��ǰĿ¼�µ��ļ��ļ���Ϣ
				FileManager.listFile(getPath());

			} else if (Constant.MAKEDIR.equals(pas[0])) {
				// ����Ŀ¼
				if (!StringUtils.isNull(pas[1])) {
					// �½��ļ�
					FileManager.makeDir(getPath() + pas[1] + "/");
				}

			} else if (Constant.CD.equals(pas[0])) {
				if (!StringUtils.isNull(pas[1])) {
					// ����Ŀ¼

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
