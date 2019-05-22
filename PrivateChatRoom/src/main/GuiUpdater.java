package main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * Pomocna klasa koja animira gasenje servera
 */
public class GuiUpdater extends SwingWorker<String, String> {

	private JTextArea ta_chat;
	private ServerSocket serverSocket;
	
	public GuiUpdater(JTextArea area, ServerSocket serverSocket) {
		this.ta_chat = area;
		this.serverSocket = serverSocket;
	}

	@Override
	protected String doInBackground() throws Exception {
		Thread.sleep(500);
		addDot();
		Thread.sleep(500);
		addDot();
		Thread.sleep(500);
		addDot();
		Thread.sleep(500);
		ta_chat.setText("");
		serverSocket.close();
		
		return null;
	}
	
	private void addDot() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ta_chat.append(".");
				ta_chat.updateUI();
			}
		});
	}
	
}
