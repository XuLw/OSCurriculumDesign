package main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TextEdit extends JFrame implements ActionListener {

	JTextArea textArea;
	JPanel bp;
	JButton save;
	JButton exit;

	private String fileId;

	public TextEdit(String text, String path) {
		Container cp = this.getContentPane();
		textArea = new JTextArea(30, 40);
		textArea.setLineWrap(true);
		textArea.setAutoscrolls(true);
		fileId = path;
		textArea.setLayout(new BorderLayout());
		textArea.setText(text);
		cp.add(textArea, BorderLayout.CENTER);
		save = new JButton("SAVE");
		exit = new JButton("EXIT");
		bp = new JPanel();
		bp.add(save);
		bp.add(exit);
		this.setTitle(path);
		cp.add(bp, BorderLayout.SOUTH);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == save) {
			
		} else if (arg0.getSource() == exit) {
			// ÍË³ö
		}
	}

	public static void main(String[] args) {
		new TextEdit("fshdjfsd", "text");
	}
}
