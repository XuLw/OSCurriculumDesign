package bean;

import java.io.Serializable;

import main.Constant;

public class Block implements Serializable {

	private int size; // 块剩余大小
	private String content; // 文件内容

	public Block() {
		size = Constant.BLOCK_SIZE;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getContent() {
		return content.substring(0, Constant.BLOCK_SIZE - size);
	}

	public void setContent(String content) {
		this.content = content;
	}

}
