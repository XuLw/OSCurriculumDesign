package main;

import java.util.ArrayList;

import bean.Block;
import bean.Record;
import bean.SuperBlock;
import utils.ConsoleScanner;
import utils.Printer;
import utils.StringUtils;

public class FileManager {

	public static final int OK = 11; // ��ȷ����
	public static final int FAILED = 22; // ����ֵ
	public static final int INIT = 33; // ��־��ʼֵ
	public static final int NAME_CONFLICT = 44; // �ļ�����ͻ
	public static final int NOT_FOUND = 55; // �ļ�������

	// �����ļ�
	public static int createFile(String filePath) {
		int state = INIT;
		if (!FileController.getSuperBlock().fileExists(filePath)) {
			// ��ͬ���ļ�
			Record record = new Record();
			record.setId(filePath);
			FileController.getSuperBlock().addRecord(record);
			state = OK;
		} else {
			// ͬ��
			state = NAME_CONFLICT;
		}
		return state;
	}

	// ���ļ�
	public static int openFile(String filePath) {
		int state = INIT;
		Record r = FileController.getSuperBlock().getRecordById(filePath);
		if (r == null) {
			// �ļ�������
			state = NOT_FOUND;
		} else {
			// ��ԭ�еļ�¼ɾ��
			FileController.getSuperBlock().removeRecord(r);
			new TextEdit(r);
			state = OK;
		}
		return state;
	}

	// ����Ŀ¼
	public static int makeDir(String filePath) {
		int state = INIT;
		if (!FileController.getSuperBlock().fileExists(filePath)) {
			// ��ͬ��Ŀ¼
			Record record = new Record();
			record.setId(filePath);
			FileController.getSuperBlock().addRecord(record);
			state = OK;
		} else {
			state = NAME_CONFLICT;
		}
		return state;
	}

	// ��ӡĿ¼
	public static void listFile(String filePath) {
		ArrayList<String> rs = FileController.getSuperBlock().getRecordsByPath(filePath);
		ArrayList<String> fs = new ArrayList<>();
		ArrayList<String> ds = new ArrayList<>();
		for (String r : rs) {
			// ɸѡ���ļ� ��Ŀ¼
			String name = r.substring(filePath.length());
			if (StringUtils.isNull(name))
				continue;
			if (name.indexOf('/') == -1) {
				fs.add(name);
			} else {
				name = name.split("/")[0];
				if (ds.indexOf(name) == -1) {
					ds.add(name);
				}
			}

		}
		// ���
		Printer.printSeparator();
		if (fs.size() + ds.size() == 0)
			System.out.println("no content here !");
		for (String f : fs) {
			System.out.printf("%-10s<f>\n", f);
		}
		for (String d : ds) {
			System.out.printf("%-10s<d>\n", d);
		}
		Printer.printSeparator();
	}

	// ��������˳�Ŀ¼
	public static int cdDir(String path) {
		int state = INIT;
		// �ж��ǲ���Ŀ¼�ļ�
		if (FileController.getSuperBlock().fileExists(path)) {
			state = OK;
		} else {
			state = NOT_FOUND;
		}
		return state;
	}

	// �����д����
	public static int saveText(String text, String fileId) {
		int state = INIT;
		int blockNum = text.length() / Constant.BLOCK_SIZE;
		int hasMore = text.length() % Constant.BLOCK_SIZE != 0 ? 1 : 0;

		Record r = new Record();
		r.setId(fileId);
		int[] bids = { -1, -1, -1 };

		if (FileController.getSuperBlock().getNumOfEmptyBlock() < (blockNum + hasMore)) {
			// ���п鲻��
			return FAILED;
		}

		int i = 0;
		for (; i < blockNum; i++) {
			Block b = FileController.getSuperBlock().getEmptyBlock();
			bids[i] = b.getSize();
			b.setContent(text.substring(i * Constant.BLOCK_SIZE, Constant.BLOCK_SIZE));
			b.setSize(0);
		}

		if (hasMore == 1) {
			// ����ʣ
			Block b = FileController.getSuperBlock().getEmptyBlock();
			bids[i] = b.getSize();
			b.setSize(Constant.BLOCK_SIZE - (text.length() % Constant.BLOCK_SIZE));
			b.setContent(text.substring(blockNum * Constant.BLOCK_SIZE));
			state = OK;
		}

		r.setBlockId(bids);
		FileController.getSuperBlock().addRecord(r);
		return state;

	}

	// ɾ��Ŀ¼
	public static int removeDir(String dirPath) {
		int state = INIT;
		// ��ȡͬĿ¼�ļ�¼
		SuperBlock sb = FileController.getSuperBlock();
		ArrayList<String> rs = sb.getRecordsByPath(dirPath);
		switch (rs.size()) {
		case 0:
			// �����ڸ�Ŀ¼
			state = NOT_FOUND;
			break;
		case 1:
			// ֻ��Ŀ¼
			sb.removeRecord(sb.getRecordById(dirPath));
			state = OK;
			break;
		default:
			// Ŀ¼�»��ж���
			System.out.println("warnning: delete all files in this directoy(user)? (y to confirm)");
			String command = ConsoleScanner.getInput().nextLine().trim().toLowerCase();
			if (!StringUtils.isNull(command) && command.charAt(0) == 'y') {
				// ȷ������
				for (String record : rs) {
					if (record.lastIndexOf('/') == record.length() - 1)
						// ��Ŀ¼
						sb.removeRecord(sb.getRecordById(record));
					else
						// ���ļ�
						removeFile(record);
				}
			}
			state = OK;
			break;
		}
		return state;
	}

	// ɾ���ļ�
	public static int removeFile(String filePath) {
		int state = INIT;
		Record r = FileController.getSuperBlock().getRecordById(filePath);
		if (r == null) {
			state = NOT_FOUND;
			// �ļ�������
		} else {
			// ���ļ��������
			FileController.getSuperBlock().clearBlock(r.getBlockId());
			// ���ļ���¼ɾ��
			FileController.getSuperBlock().removeRecord(r);
			state = OK;
		}
		return state;
	}

}
