package main;

import java.util.ArrayList;
import java.util.Scanner;

import bean.Block;
import bean.SuperBlock;
import bean.User;
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
		input = new Scanner(System.in);
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
		Help.showLoginHelp();
		String command = "";
		do {
			command = getCommand(); // ��ȡ����

			switch (command.toLowerCase()) {
			case Constant.REGISTER:
				// ��ע��
				if (mLogin.register() == Login.OK) {
					// ע��ɹ�
					FileController.getSuperBlock().addUser(mLogin.getUser());// ���û���ӵ��û���
					System.out.println("ע��ɹ���");
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
					System.out.println("��½�ɹ���");
					state = this.OK;
				} else {
					// ������¼
				}
				break;
			case Constant.HELP:
				// ��������б�
				Help.showLoginHelp();
				break;
			case Constant.EXIT:
				// �˳�����

				state = this.EXIT;
				break;

			default:
				// δʶ������
				System.out.println(command + " not found!");
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
		Help.showFileHelp();
		do {

			FileController.getSuperBlock().printDetail();

			command = getCommandP();
			String[] pas = command.split(" ");
			switch (pas[0].toLowerCase()) {
			case Constant.CREATE:
				if (!StringUtils.isNull(pas[1])) {
					// �½��ļ�
					FileManager.createFile(getPath() + pas[1]);
				}
				break;
			case Constant.OPEN:
				// ���ļ�
				if (!StringUtils.isNull(pas[1])) {
					FileManager.openFile(getPath() + pas[1]);
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
					FileManager.makeDir(getPath() + pas[1] + "/");
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
						if (FileManager.cdDir(getPath() + pas[1] + "/") == FileManager.OK) {
							// ����ǰ��Ŀ¼���뵽·��ջ��
							dirs.add(pas[1]);
						} else {
							// Ŀ¼������
						}
					}

				}
				break;
			case Constant.RMD:
				// ɾ��Ŀ¼
				if (!StringUtils.isNull(pas[1])) {
					// �½��ļ�
					FileManager.removeDir(getPath() + pas[1] + "/");
				}
				break;
			case Constant.RMF:
				// ɾ���ļ�
				break;
			case Constant.HELP:
				Help.showFileHelp();
				break;
			case Constant.EXIT:
				state = FAILED;
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
