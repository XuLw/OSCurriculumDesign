package main;

import java.util.ArrayList;

import bean.Block;
import bean.Record;
import utils.StringUtils;

public class FileManager {

	public static final int OK = 11;
	public static final int FAILED = 22;
	public static final int INIT = 33;
	public static final int NAME_CONFLICT = 44; // 文件名冲突

	public static int createFile(String filePath) {
		int state = INIT;
		if (!FileController.getSuperBlock().fileExists(filePath)) {
			// 无同名文件
			Record record = new Record();
			record.setId(filePath);
			FileController.getSuperBlock().addRecord(record);
			state = OK;
		}
		return state;
	}

	public static int openFile(String filePath) {
		int state = INIT;
		Record r = FileController.getSuperBlock().getRecordById(filePath);
		if (r == null) {
			// 文件不存在
			System.out.println("file not exists!");
		} else {
			// 将原有的记录删除
			FileController.getSuperBlock().removeRecord(r);
			new TextEdit(r);
		}
		return state;
	}

	public static int makeDir(String filePath) {
		int state = INIT;
		if (!FileController.getSuperBlock().fileExists(filePath)) {
			// 无同名目录
			Record record = new Record();
			record.setId(filePath);
			FileController.getSuperBlock().addRecord(record);
			state = OK;
		}

		return state;
	}

	public static void listFile(String filePath) {
		ArrayList<String> rs = FileController.getSuperBlock().getRecordsByPath(filePath);
		// 输出
		System.out.println("--------------");
		for (String r : rs) {
			if (StringUtils.isNull(r))
				continue;
			if (r.indexOf('/') == -1) {
				// 只是文件

				System.out.println(r + "   " + "<f>");
			} else {
				System.out.println(r + "   " + "<d>");
			}
		}
	}

	public static int cdDir(String path) {
		System.out.println(path);
		int state = INIT;
		// 判断是不是目录文件
		if (FileController.getSuperBlock().fileExists(path)) {
			state = OK;
		}
		return state;
	}

	// 向块中写内容
	public static int saveText(String text, String fileId) {
		int state = INIT;
		int blockNum = text.length() / Constant.BLOCK_SIZE;
		int hasMore = text.length() % Constant.BLOCK_SIZE != 0 ? 1 : 0;

		Record r = new Record();
		r.setId(fileId);
		int[] bids = { -1, -1, -1 };

		if (FileController.getSuperBlock().getNumOfEmptyBlock() < (blockNum + hasMore)) {
			// 空闲块不够
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
			// 还有剩
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

	public static int removeDir(String dirPath) {
		int state = INIT;
		// 获取同目录的记录
		ArrayList<String> rs = FileController.getSuperBlock().getRecordsByPath(dirPath);
		if (rs.size() > 1) {
			// 目录下还有东西
			System.out.println("目录里面还有");
		} else {
			// 只是目录
		}
		return state;
	}

	public static void main(String[] args) {
		FileController.readDataFromFile();
		FileController.getSuperBlock().printDetail();
		makeDir("xulw/b");
		FileController.getSuperBlock().printDetail();
		listFile("xulw/");
	}
}
