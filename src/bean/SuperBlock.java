package bean;

import java.io.Serializable;
import java.util.ArrayList;

import main.Constant;

public class SuperBlock implements Serializable {

	private int length; // 超级快大小
	private ArrayList<User> users; // 用户
	private int[] bitmap;// 位视图
	private ArrayList<Record> records;

	public SuperBlock() {
		users = new ArrayList<>();
		bitmap = new int[Constant.SYSTEM_SIZE];
		records = new ArrayList<>();
	}

	// 添加用户
	public boolean addUser(User user) {
		this.users.add(user);
		return true;
	}

	public boolean hasUser(String name) {
		for (User u : this.users) {
			if (u.getName().equals(name)) {
				// 已存在用户
				return true;
			}
		}
		// 无该用户
		return false;
	}

	public boolean checkUser(User u) {
		if (users.indexOf(u) != -1)
			return true;
		else
			return false;
	}

	// 检查文件名字是否重复
	public boolean fileExists(String path) {
		for (Record r : records) {
			if (r.getId().equals(path))
				return true;
		}
		return false;
	}

	// 添加记录
	public boolean addRecord(Record record) {
		this.records.add(record);
		return true;
	}

	// 查询记录
	public ArrayList<String> getRecordsByPath(String path) {
		ArrayList<String> files = new ArrayList<>();
		for (Record r : records) {
			if (r.getId().indexOf(path) != -1) {
				// 找到
				files.add(r.getId().substring(path.length()));
			}
		}
		return files;
	}

	public void printDetail() {
		String allUser = "";
		for (User u : users) {
			allUser += u + "  ||  ";
		}
		String allRecords = "";
		for (Record r : records) {
			allRecords += r.toString() + "\n";
		}
		System.out.println("--------------------");
		System.out.println("user");
		System.out.println(allUser);
		System.out.println("bitmap");
		System.out.println(bitmap);
		System.out.println("record");
		System.out.println(allRecords);
		System.out.println("--------------------");
	}

	public static void main(String[] args) {

	}
}
