package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import bean.Block;
import bean.SuperBlock;

public class FileController {

	private static SuperBlock mSuperBlock;

	private static File file = new File(Constant.FILE_PATH);;

	public static void readDataFromFile() {

		try {
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			// ��ȡ������
			mSuperBlock = (SuperBlock) ois.readObject();
			ois.close();
			fis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// ��һ�����У�������Ӧ��������Ϣ
			init();
		} catch (ClassNotFoundException e) {
			System.out.println("���ļ�");
			e.printStackTrace();
		}
	}

	// ������д�ش���
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

	// ��һ�����еĳ�ʼ��
	public static void init() {
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

	public static SuperBlock getSuperBlock() {
		return mSuperBlock;
	}



	public static void main(String[] args) {
		new FileController();
	}

}
