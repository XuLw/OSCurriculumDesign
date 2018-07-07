package bean;

import java.io.Serializable;
import java.util.ArrayList;

import main.Constant;

public class SuperBlock implements Serializable {

	private int length; // �������С
	private ArrayList<User> users; // �û�
	private int[] bitmap;// λ��ͼ
	private int indexOfEmptyBlock; // ��ǰ���õĿ��п�
	private int numOfEmptyBlock; // ��ǰʣ����п�
	private ArrayList<Record> records;
	private ArrayList<Block> blocks; // ���еĿ�

	public SuperBlock() {
		users = new ArrayList<>();
		bitmap = new int[Constant.SYSTEM_SIZE];
		records = new ArrayList<>();
		blocks = new ArrayList<>();
		// �½���
		numOfEmptyBlock = Constant.SYSTEM_SIZE;
		for (int i = 0; i < Constant.SYSTEM_SIZE; i++)
			blocks.add(new Block());
		indexOfEmptyBlock = 0; // Ĭ�ϴӵ�һ���鿪ʼ��
	}

	public int getNumOfEmptyBlock() {
		return this.numOfEmptyBlock;
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

	// ɾ����¼
	public void removeRecord(Record r) {
		this.records.remove(r);
	}

	// ��ѯ��¼
	public ArrayList<String> getRecordsByPath(String path) {
		ArrayList<String> files = new ArrayList<>();
		for (Record r : records) {
			if (r.getId().indexOf(path) != -1) {
				// �ҵ�
				files.add(r.getId());
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
				break;
			} else {
				// �����ÿ�
				bitmap[ids[i]] = 0;
				content.append(blocks.get(ids[i]).getContent());
			}
		}
		return content.toString();
	}

	// ��ȡ���п�
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
				// �ǿ�
				indexOfEmptyBlock++;
				// ���ÿ��ѱ�ռ�ñ�־
				bitmap[i] = 1;
				return i;
			}
		}
		return -1;
	}

	// �����п���Ϊ����״̬
	public boolean clearBlock(int[] bids) {
		for (int i = 0; i < bids.length; i++) {
			if (bids[i] == -1)
				break;
			bitmap[bids[i]] = 0;
		}
		return true;
	}

	// ״̬���
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

	public static void main(String[] args) {

	}
}
