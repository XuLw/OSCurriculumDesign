package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import bean.SuperBlock;

public class FileController {

	public static final int INIT = 11;
	public static final int OK = 22;
	public static final int FAIILED = 33;

	private static SuperBlock mSuperBlock;

	private static File file = new File(Constant.FILE_PATH);;

	public static void readDataFromFile() {

		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			// 读取超级块
			mSuperBlock = (SuperBlock) ois.readObject();
			ois.close();
			fis.close();

		} catch (FileNotFoundException e) {
			try {
				file.createNewFile();
			} catch (IOException ee) {
				ee.printStackTrace();
			}
		} catch (IOException e) {
			// 第一次运行，并无相应的配置信息
			init();
		} catch (ClassNotFoundException e) {
			System.out.println("无文件");
			e.printStackTrace();
		}
	}

	// 将数据写回磁盘
	public static void writeDataToFile() {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mSuperBlock);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 第一次运行的初始化
	private static void init() {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			mSuperBlock = new SuperBlock();
			oos.writeObject(mSuperBlock);
			oos.close();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int rootFormat() {
		int state = INIT;
		if (file.exists() && file.isFile())
			file.delete();
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		init();
		state = OK;
		return state;
	}

	public static SuperBlock getSuperBlock() {
		return mSuperBlock;
	}

	public static void main(String[] args) {
		new FileController();
	}

}
