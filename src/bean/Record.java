package bean;

import java.io.Serializable;

import main.Constant;

/*
 * 用户与文件对应关系记录
*/
public class Record implements Serializable {

	private String recordId; // 记录的唯一标识 为该文件的路径
	private int[] blockId;// 对应块Id

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
