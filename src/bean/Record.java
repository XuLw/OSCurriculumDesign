package bean;

import java.io.Serializable;

import main.Constant;

/*
 * �û����ļ���Ӧ��ϵ��¼
*/
public class Record implements Serializable {

	private String recordId; // ��¼��Ψһ��ʶ Ϊ���ļ���·��
	private int[] blockId;// ��Ӧ��Id

	public Record() {
		blockId = new int[Constant.FILE_MAX_BLOCK];
		for (int i = 0; i < blockId.length; i++) {
			blockId[i] = -1;
		}
	}

	public String getId() {
		return recordId;
	}

	public void setId(String id) {
		this.recordId = id;
	}

	public int[] getBlockId() {
		return blockId;
	}

	public void setBlockId(int[] blockId) {
		this.blockId = blockId;
	}

	@Override
	public String toString() {
		return "[" + recordId + "_ blockId _" + blockId + "]";
	}

}
