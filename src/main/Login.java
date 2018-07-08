package main;

import java.util.Scanner;

import bean.Record;
import bean.SuperBlock;
import bean.User;
import utils.ConsoleScanner;
import utils.StringUtils;

public class Login {

	public static final int INIT = -2;
	public static final int OK = 0;
	public static final int FAILED = -1;

	private User mUser; // ��ǰ�û�
	Scanner input;

	public Login() {
		input = ConsoleScanner.getInput();
	}

	public int login() {
		int state = this.INIT;
		String temp = "";
		do {
			mUser = new User();
			System.out.print("enter your name('end' to exit):  ");
			temp = input.nextLine().trim();
			if (temp.equals(Constant.END)) {
				state = this.FAILED;
				break;
			}

			// �����˺�
			mUser.setName(temp);
			if (FileController.getSuperBlock().hasUser(mUser.getName())) {
				// ��������
				System.out.print("enter your password:   ");
				mUser.setPassword(input.nextLine().trim());
				int restTry = 3; // ��������Ĵ�
				do {
					if (FileController.getSuperBlock().checkUser(mUser)) {
						// ������ȷ
						state = this.OK;
						break;
					} else {
						// �������
						System.out.println("err: the password is incorrect! (" + restTry-- + " last)");
						System.out.print("enter your password again('end' to exit):   ");
						temp = input.nextLine().trim();
						if (temp.equals(Constant.END)) {
							state = this.FAILED;
							break;
						}
						mUser.setPassword(temp);
					}
				} while (restTry > 0);
			} else {
				// �����ڸ��û�
				System.out.println("err: username not exist!");
			}
			if (state == this.OK || state == this.FAILED)
				break;
		} while (true);
		return state;
	}

	public int register() {
		int state = this.INIT;
		String name = "";
		String password = "";
		do {
			System.out.print("enter your name('end' to exit):   ");
			name = input.nextLine().trim();
			// �û��˳�
			if (name.equals(Constant.END)) {
				state = this.FAILED;
				break;
			}

			if (!FileController.getSuperBlock().hasUser(name)) {
				// ���û���δ��ʹ�� �����ɹ� ��Ӹ�Ŀ¼
				Record root = new Record();
				root.setId(name + "/");
				FileController.getSuperBlock().addRecord(root);
				state = this.OK;
				break;
			}
			System.out.println("err: the user already exists!");

		} while (true);

		// �û���������ȷ�û���
		if (state == this.OK)
			do {
				System.out.print("enter your password('end' to exit):   ");

				password = input.nextLine().trim();
				if (password.equals(Constant.END)) {
					state = this.FAILED;
					break;
				}

				if (StringUtils.isNull(password)) {
					System.out.println("err: password can't be empty!");
				} else {
					mUser = new User();
					mUser.setName(name);
					mUser.setPassword(password);
					break;
				}

			} while (true);

		return state;
	}

	public User getUser() {
		return mUser;
	}

	// public static void main(String[] args) {
	// SuperBlock sb = new SuperBlock();
	// sb.addUser(new User("xulw", "xulw"));
	// new Login().login();
	// }
}
