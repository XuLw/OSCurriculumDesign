package bean;

import java.io.Serializable;
import java.util.ArrayList;

import main.Constant;

public class SuperBlock implements Serializable {

	private int length; // �������С
	private ArrayList<User> users; // �û�
	private int[] bitmap;// λ��ͼ
	private int indexOfEmptyBlock; // ��ǰ���õĿ��п�
	private ArrayList<Record> records;
	private ArrayList<Block> blocks; // ���еĿ�

	public SuperBlock() {
		users = new ArrayList<>();
		bitmap = new int[Constant.SYSTEM_SIZE];
		records = new ArrayList<>();
		blocks = new ArrayList<>(1024);
		indexOfEmptyBlock = 0; // Ĭ�ϴӵ�һ���鿪ʼ��
	}

	// ����û�
	public boolean addUser(User user) {
		this.users.add(user);
		return true;
	}

	public boolean hasUser(String name) {
		for (User u : this.users) {
			if (u.getName().equals(name)) {
				// �Ѵ����û�
				return true;
			}
		}
		// �޸��û�
		return false;
	}

	public boolean checkUser(User u) {
		if (users.indexOf(u) != -1)
			return true;
		else
			return false;
	}

	// ����ļ������Ƿ��ظ�
	public boolean fileExists(String path) {
		for (Record r : records) {
			if (r.getId().equals(path))
				return true;
		}
		return false;
	}

	// ��Ӽ�¼
	public boolean addRecord(Record record) {
		this.records.add(record);
		return true;
	}

	// ��ѯ��¼
	public ArrayList<String> getRecordsByPath(String path) {
		ArrayList<String> files = new ArrayList<>();
		for (Record r : records) {
			if (r.getId().indexOf(path) != -1) {
				// �ҵ�
				files.add(r.getId().substring(path.length()));
			}
		}
		return files;
	}

	// ͨ��Id��ȡ�ļ���¼
	public Record getRecordById(String id) {
		for (Record r : records) {
			if (r.getId().equals(id))
				return r;
		}
		return null;
	}

	// ͨ����id��ȡ������
	public String getContentByBlockIds(int[] ids) {
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] == -1) {
				// �Ѿ�������
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
				// �ǿ�
				indexOfEmptyBlock++;
				return i;
			}
		}
		return -1;
	}

	public static void main(String[] args) {

	}
}
