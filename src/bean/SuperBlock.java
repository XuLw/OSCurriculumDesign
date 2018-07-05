package bean;

import java.io.Serializable;
import java.util.ArrayList;

import main.Constant;

public class SuperBlock implements Serializable {

	private int length; // 超级快大小
	private ArrayList<User> users; // 用户
	private int[] bitmap;// 位视图
	private int indexOfEmptyBlock; // 当前可用的空闲块
	private ArrayList<Record> records;
	private ArrayList<Block> blocks; // 所有的块

	public SuperBlock() {
		users = new ArrayList<>();
		bitmap = new int[Constant.SYSTEM_SIZE];
		records = new ArrayList<>();
		blocks = new ArrayList<>(1024);
		indexOfEmptyBlock = 0; // 默认从第一个块开始放
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

	// 通过Id获取文件记录
	public Record getRecordById(String id) {
		for (Record r : records) {
			if (r.getId().equals(id))
				return r;
		}
		return null;
	}

	// 通过块id获取快内容
	public String getContentByBlockIds(int[] ids) {
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] == -1) {
				// 已经结束了
			} else {
				content.append(blocks.get(ids[i]).getContent());
			}
		}
		return content.toString();
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
		System.out.println(bitmap.toString());
		System.out.println("record: " + records.size());
		System.out.println(allRecords);
		System.out.println("--------------------");
	}

	public Block getEmptyBlock() {
		Block b = null;
		
			return b;
	}

	private int getEmptyId() {
		if (indexOfEmptyBlock > 1023)
			indexOfEmptyBlock = 0;
		for (int i = indexOfEmptyBlock; i < 1023; i++) {
			if (bitmap[i] == 0) {
				// 是空
				indexOfEmptyBlock++;
				return i;
			}
		}
		return -1;
	}

	public static void main(String[] args) {

	}
}
