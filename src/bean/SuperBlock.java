package bean;

import java.io.Serializable;
import java.util.ArrayList;

import main.Constant;

public class SuperBlock implements Serializable {

	private int length; // 超级快大小
	private ArrayList<User> users; // 用户
	private int[] bitmap;// 位视图
	private int indexOfEmptyBlock; // 当前可用的空闲块
	private int numOfEmptyBlock; // 当前剩余空闲快
	private ArrayList<Record> records;
	private ArrayList<Block> blocks; // 所有的块

	public SuperBlock() {
		users = new ArrayList<>();
		bitmap = new int[Constant.SYSTEM_SIZE];
		records = new ArrayList<>();
		blocks = new ArrayList<>();
		// 新建块
		numOfEmptyBlock = Constant.SYSTEM_SIZE;
		for (int i = 0; i < Constant.SYSTEM_SIZE; i++)
			blocks.add(new Block());
		indexOfEmptyBlock = 0; // 默认从第一个块开始放
	}

	public int getNumOfEmptyBlock() {
		return this.numOfEmptyBlock;
	}

	// 添加用户
	public boolean addUser(User user) {
		this.users.add(user);
		return true;
	}

	// 删除用户
	public boolean removeUser(User user) {
		if (this.users.indexOf(user) != -1)
			this.users.remove(user);
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

	// 删除记录
	public void removeRecord(Record r) {
		this.records.remove(r);
	}

	// 查询记录
	public ArrayList<String> getRecordsByPath(String path) {
		ArrayList<String> files = new ArrayList<>();
		for (Record r : records) {
			if (r.getId().indexOf(path) != -1) {
				// 找到
				files.add(r.getId());
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
				break;
			} else {
				// 将块置空
				bitmap[ids[i]] = 0;
				content.append(blocks.get(ids[i]).getContent());
			}
		}
		return content.toString();
	}

	// 获取空闲快
	public Block getEmptyBlock() {
		if (numOfEmptyBlock == 0)
			return null;
		int index = getEmptyId();
		Block b = blocks.get(index);
		b.setSize(index);
		if (index == -1)
			return null;
		else
			return b;
	}

	private int getEmptyId() {
		if (indexOfEmptyBlock > 1023)
			indexOfEmptyBlock = 0;
		for (int i = indexOfEmptyBlock; i < 1023; i++) {
			if (bitmap[i] == 0) {
				// 是空
				indexOfEmptyBlock++;
				// 设置块已被占用标志
				bitmap[i] = 1;
				return i;
			}
		}
		return -1;
	}

	// 将空闲块置为空闲状态
	public boolean clearBlock(int[] bids) {
		for (int i = 0; i < bids.length; i++) {
			if (bids[i] == -1)
				break;
			bitmap[bids[i]] = 0;
		}
		return true;
	}

	// 状态输出
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
		for (int i = 1; i <= bitmap.length; i++) {
			System.out.print(bitmap[i - 1] + " ");
			if (i % 64 == 0)
				System.out.println();
		}
		System.out.println("record: " + records.size());
		System.out.println(allRecords);
		System.out.println("--------------------");
	}

	public void printBitmap() {
		System.out.println("bitmap");
		for (int i = 1; i <= bitmap.length; i++) {
			System.out.print(bitmap[i - 1] + " ");
			if (i % 64 == 0)
				System.out.println();
		}
	}

	public void printUsers() {
		if (users.size() > 0) {
			for (User u : users) {
				System.out.println(u.getName() + "   " + u.getPassword());
			}
		} else {
			System.out.println("no user yet !");
		}
	}

	// public static void main(String[] args) {
	//
	// }
}
