package main;

import java.util.Scanner;

import bean.Record;
import bean.SuperBlock;
import bean.User;
import utils.StringUtils;

public class Login {

	public static final int INIT = -2;
	public static final int OK = 0;
	public static final int FAILED = -1;

	private User mUser; // ��ǰ�û�
	Scanner input;

	public Login() {
		input = new Scanner(System.in);
		mUser = new User();
	}

	public int login() {
		int state = this.INIT;
		do {
			mUser = new User();
			System.out.print("enter your name(Ctrl + Z to exit):  ");
			if (!input.hasNext()) {
				state = this.FAILED;
				break;
			}

			// �����˺�
			mUser.setName(input.nextLine().trim());
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
						System.out.println("the password is incorrect! (" + restTry + " last)");
						System.out.print("enter your password again (Ctrl + Z to exit):   ");
						if (!input.hasNext()) {
							state = this.FAILED;
							break;
						}
						mUser.setPassword(input.nextLine().trim());
					}
				} while (restTry > 0);
			} else {
				// �����ڸ��û�
				System.out.println("username not exist!");
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
			System.out.print("enter your name (Ctrl + Z to exit):   ");

			// �û��˳�
			if (!input.hasNext()) {
				state = this.FAILED;
				break;
			}

			name = input.nextLine().trim();
			if (!FileController.getSuperBlock().hasUser(name)) {
				// ���û���δ��ʹ�� �����ɹ� ��Ӹ�Ŀ¼
				Record root = new Record();
				root.setId(name + "/");
				FileController.getSuperBlock().addRecord(root);
				state = this.OK;
				
				break;
			}
			System.out.println("the user already exists!");

		} while (true);

		// �û��������û���
		if (state == this.OK)
			do {
				System.out.print("enter your password (Ctrl + Z to exit):   ");

				if (!input.hasNext()) {
					state = this.FAILED;
					break;
				}

				password = input.nextLine().trim();

				if (StringUtils.isNull(password)) {
					System.out.println("password can't be empty!");
				} else {
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

	public static void main(String[] args) {
		SuperBlock sb = new SuperBlock();
		sb.addUser(new User("xulw", "xulw"));
		new Login().login();
	}
}
