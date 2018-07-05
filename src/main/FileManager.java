package main;

import java.util.ArrayList;

import bean.Record;
import utils.StringUtils;

public class FileManager {

	public static final int OK = 11;
	public static final int FAILE = 22;
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
			new TextEdit(FileController.getSuperBlock().getContentByBlockIds(r.getBlockId()), filePath);
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

	public static void main(String[] args) {
		FileController.readDataFromFile();
		FileController.getSuperBlock().printDetail();
		makeDir("xulw/b");
		FileController.getSuperBlock().printDetail();
		listFile("xulw/");
	}
}
