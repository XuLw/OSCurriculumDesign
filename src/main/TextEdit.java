package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import bean.Record;

public class TextEdit extends JFrame implements ActionListener {

	JTextArea textArea;
	JPanel bp;
	JButton save;
	JButton exit;

	private Record r;

	public TextEdit(Record record) {
		Container cp = this.getContentPane();
		textArea = new JTextArea(15, 30);
		textArea.setLineWrap(true);
		textArea.setAutoscrolls(true);
		r = record;
		textArea.setLayout(new BorderLayout());
		textArea.setText(FileController.getSuperBlock().getContentByBlockIds(r.getBlockId()));
		JPanel panel = new JPanel();
		panel.add(new JScrollPane(textArea));
		cp.add(panel, BorderLayout.CENTER);
		save = new JButton("SAVE");
		exit = new JButton("EXIT");
		save.addActionListener(this);
		exit.addActionListener(this);
		bp = new JPanel();
		bp.add(save);
		bp.add(exit);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setTitle(record.getId());
		cp.add(bp, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == save) {
			if (textArea.getText().length() > Constant.BLOCK_SIZE * Constant.FILE_MAX_BLOCK) {
				// 文本过长
				JOptionPane.showMessageDialog(null, "超出字数 (" + textArea.getText().length() + "/"
						+ Constant.BLOCK_SIZE * Constant.FILE_MAX_BLOCK + ")！");
			} else {
				if (FileManager.saveText(textArea.getText(), r.getId()) == FileManager.OK) {
					// 保存成功
					System.out.println();
					System.out.println("--------------");
					System.out.println(r.getId() + " save successfully!");
					System.out.println("--------------");
					this.dispose();
				} else {
					System.out.println("--------------");
					System.out.println("out of free space!");
					System.out.println("--------------");
					this.dispose();
				}

			}
		} else if (arg0.getSource() == exit) {
			// 退出
			FileController.getSuperBlock().addRecord(r);
			this.dispose();
		}
	}

}
