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

	private User mUser; // 当前用户
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

			// 输入账号
			mUser.setName(temp);
			if (FileController.getSuperBlock().hasUser(mUser.getName())) {
				// 输入密码
				System.out.print("enter your password:   ");
				mUser.setPassword(input.nextLine().trim());
				int restTry = 3; // 可以输错四次
				do {
					if (FileController.getSuperBlock().checkUser(mUser)) {
						// 密码正确
						state = this.OK;
						break;
					} else {
						// 密码错误
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
				// 不存在该用户
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
			// 用户退出
			if (name.equals(Constant.END)) {
				state = this.FAILED;
				break;
			}

			if (!FileController.getSuperBlock().hasUser(name)) {
				// 该用户名未被使用 创建成功 添加根目录
				Record root = new Record();
				root.setId(name + "/");
				FileController.getSuperBlock().addRecord(root);
				state = this.OK;
				break;
			}
			System.out.println("err: the user already exists!");

		} while (true);

		// 用户输入了正确用户名
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
