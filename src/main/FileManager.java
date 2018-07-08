package main;

import java.util.ArrayList;

import bean.Block;
import bean.Record;
import bean.SuperBlock;
import utils.ConsoleScanner;
import utils.Printer;
import utils.StringUtils;

public class FileManager {

	public static final int OK = 11; // 正确处理
	public static final int FAILED = 22; // 错误值
	public static final int INIT = 33; // 标志初始值
	public static final int NAME_CONFLICT = 44; // 文件名冲突
	public static final int NOT_FOUND = 55; // 文件不存在

	// 创建文件
	public static int createFile(String filePath) {
		int state = INIT;
		if (!FileController.getSuperBlock().fileExists(filePath)) {
			// 无同名文件
			Record record = new Record();
			record.setId(filePath);
			FileController.getSuperBlock().addRecord(record);
			state = OK;
		} else {
			// 同名
			state = NAME_CONFLICT;
		}
		return state;
	}

	// 打开文件
	public static int openFile(String filePath) {
		int state = INIT;
		Record r = FileController.getSuperBlock().getRecordById(filePath);
		if (r == null) {
			// 文件不存在
			state = NOT_FOUND;
		} else {
			// 将原有的记录删除
			FileController.getSuperBlock().removeRecord(r);
			new TextEdit(r);
			state = OK;
		}
		return state;
	}

	// 创建目录
	public static int makeDir(String filePath) {
		int state = INIT;
		if (!FileController.getSuperBlock().fileExists(filePath)) {
			// 无同名目录
			Record record = new Record();
			record.setId(filePath);
			FileController.getSuperBlock().addRecord(record);
			state = OK;
		} else {
			state = NAME_CONFLICT;
		}
		return state;
	}

	// 打印目录
	public static void listFile(String filePath) {
		ArrayList<String> rs = FileController.getSuperBlock().getRecordsByPath(filePath);
		ArrayList<String> fs = new ArrayList<>();
		ArrayList<String> ds = new ArrayList<>();
		for (String r : rs) {
			// 筛选出文件 、目录
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
		// 输出
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

	// 进入或者退出目录
	public static int cdDir(String path) {
		int state = INIT;
		// 判断是不是目录文件
		if (FileController.getSuperBlock().fileExists(path)) {
			state = OK;
		} else {
			state = NOT_FOUND;
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

	// 删除目录
	public static int removeDir(String dirPath) {
		int state = INIT;
		// 获取同目录的记录
		SuperBlock sb = FileController.getSuperBlock();
		ArrayList<String> rs = sb.getRecordsByPath(dirPath);
		switch (rs.size()) {
		case 0:
			// 不存在该目录
			state = NOT_FOUND;
			break;
		case 1:
			// 只是目录
			sb.removeRecord(sb.getRecordById(dirPath));
			state = OK;
			break;
		default:
			// 目录下还有东西
			System.out.println("warnning: delete all files in this directoy(user)? (y to confirm)");
			String command = ConsoleScanner.getInput().nextLine().trim().toLowerCase();
			if (!StringUtils.isNull(command) && command.charAt(0) == 'y') {
				// 确认命令
				for (String record : rs) {
					if (record.lastIndexOf('/') == record.length() - 1)
						// 是目录
						sb.removeRecord(sb.getRecordById(record));
					else
						// 是文件
						removeFile(record);
				}
			}
			state = OK;
			break;
		}
		return state;
	}

	// 删除文件
	public static int removeFile(String filePath) {
		int state = INIT;
		Record r = FileController.getSuperBlock().getRecordById(filePath);
		if (r == null) {
			state = NOT_FOUND;
			// 文件不存在
		} else {
			// 将文件内容清除
			FileController.getSuperBlock().clearBlock(r.getBlockId());
			// 将文件记录删除
			FileController.getSuperBlock().removeRecord(r);
			state = OK;
		}
		return state;
	}

}
