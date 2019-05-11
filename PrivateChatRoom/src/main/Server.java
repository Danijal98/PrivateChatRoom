package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

public class Server extends JFrame {

	private JPanel panelCenter;
	private JPanel panelRight;
	private JSplitPane splitHorizontal;
	private JButton b_clear;
	private JButton b_end;
	private JButton b_start;
	private JButton b_users;
	private JScrollPane jScrollPane1;
	private JLabel lb_name;
	private JTextArea ta_chat;

	private ArrayList<PrintWriter> clientOutputStreams;
	private ArrayList<String> users;

	public Server() {
		initComponents();
	}

	private void initComponents() {
		panelCenter = new JPanel(new BorderLayout());
		panelRight = new JPanel();
		panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));
		splitHorizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panelCenter,panelRight);
		jScrollPane1 = new JScrollPane();
		ta_chat = new JTextArea();
		b_start = new JButton();
		b_start.setMaximumSize(new Dimension(150, 25));
		b_start.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		b_end = new JButton();
		b_end.setMaximumSize(new Dimension(150, 25));
		b_end.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		b_users = new JButton();
		b_users.setMaximumSize(new Dimension(150, 25));
		b_users.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		b_clear = new JButton();
		b_clear.setMaximumSize(new Dimension(150, 25));
		b_clear.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		lb_name = new JLabel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Chat - Server's frame");
		setName("server");
		setResizable(false);

		ta_chat.setColumns(20);
		ta_chat.setRows(5);
		jScrollPane1.setViewportView(ta_chat);

		b_start.setText("START");
		b_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				startActionPerformed(evt);
			}
		});

		b_end.setText("END");
		b_end.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				endActionPerformed(evt);
			}
		});

		b_users.setText("Online Users");
		b_users.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				usersActionPerformed(evt);
			}
		});

		b_clear.setText("Clear");
		b_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				clearActionPerformed(evt);
			}
		});
		
		this.setLayout(new BorderLayout());
		ta_chat.setEditable(false);
		jScrollPane1.setPreferredSize(new Dimension(510, 300));
		panelCenter.add(jScrollPane1);
		panelRight.add(b_start);
//		panelRight.add(Box.createGlue());
		panelRight.add(b_end);
//		panelRight.add(Box.createGlue());
		panelRight.add(b_users);
//		panelRight.add(Box.createGlue());
		panelRight.add(b_clear);
		
//		GroupLayout layout = new GroupLayout(getContentPane());
//		getContentPane().setLayout(layout);
//		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
//				.createSequentialGroup().addContainerGap()
//				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jScrollPane1)
//						.addGroup(layout.createSequentialGroup()
//								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
//										.addComponent(b_end, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
//												Short.MAX_VALUE)
//										.addComponent(b_start, javax.swing.GroupLayout.DEFAULT_SIZE, 75,
//												Short.MAX_VALUE))
//								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 291, Short.MAX_VALUE)
//								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
//										.addComponent(b_clear, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
//												Short.MAX_VALUE)
//										.addComponent(b_users, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))))
//				.addContainerGap()).addGroup(GroupLayout.Alignment.TRAILING,
//						layout.createSequentialGroup().addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//								.addComponent(lb_name).addGap(209, 209, 209)));
//		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//				.addGroup(layout.createSequentialGroup().addContainerGap()
//						.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE).addGap(18, 18, 18)
//						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(b_start)
//								.addComponent(b_users))
//						.addGap(18, 18, 18).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//								.addComponent(b_clear).addComponent(b_end))
//						.addGap(4, 4, 4).addComponent(lb_name)));
		this.add(splitHorizontal, BorderLayout.CENTER);
		pack();
	}

	private void endActionPerformed(ActionEvent evt) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}

		tellEveryone("Server:is stopping and all users will be disconnected.\n:Chat");
		ta_chat.append("Server stopping... \n");
		

		ta_chat.setText("");
	}

	private void startActionPerformed(ActionEvent evt) {
		Thread starter = new Thread(new ServerStart());
		starter.start();

		ta_chat.append("Server started...\n");
	}

	private void usersActionPerformed(ActionEvent evt) {
		ta_chat.append("Online users : \n");
		for (String current_user : users) {
			ta_chat.append(current_user);
			ta_chat.append("\n");
		}

	}

	private void clearActionPerformed(ActionEvent evt) {
		ta_chat.setText("");
	}

	public void userAdd(String data) {
		String message, add = ": :Connect", done = "Server: :Done", name = data;
		ta_chat.append("Before " + name + " added. \n");
		users.add(name);
		ta_chat.append("After " + name + " added. \n");
		String[] tempList = new String[(users.size())];
		users.toArray(tempList);

		for (String token : tempList) {
			message = (token + add);
			tellEveryone(message);
		}
		tellEveryone(done);
	}

	public void userRemove(String data) {
		String message, add = ": :Connect", done = "Server: :Done", name = data;
		users.remove(name);
		String[] tempList = new String[(users.size())];
		users.toArray(tempList);

		for (String token : tempList) {
			message = (token + add);
			tellEveryone(message);
		}
		tellEveryone(done);
	}

	public void tellEveryone(String message) {
		Iterator<PrintWriter> it = clientOutputStreams.iterator();

		while (it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();
				writer.println(message);
				ta_chat.append("Sending: " + message + "\n");
				writer.flush();
				ta_chat.setCaretPosition(ta_chat.getDocument().getLength());

			} catch (Exception ex) {
				ta_chat.append("Error telling everyone. \n");
			}
		}
	}

	public class ServerStart implements Runnable {
		@Override
		public void run() {
			clientOutputStreams = new ArrayList<PrintWriter>();
			users = new ArrayList<>();

			try {
				ServerSocket serverSock = new ServerSocket(2222);

				while (true) {
					Socket clientSock = serverSock.accept();
					PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
					clientOutputStreams.add(writer);

					Thread listener = new Thread(new ClientHandler(clientSock, writer));
					listener.start();
					ta_chat.append("Got a connection. \n");
				}
			} catch (Exception ex) {
				ta_chat.append("Error making a connection. \n");
			}
		}
	}

	public class ClientHandler implements Runnable {
		private BufferedReader reader;
		private Socket sock;
		private PrintWriter client;

		public ClientHandler(Socket clientSocket, PrintWriter user) {
			client = user;
			try {
				sock = clientSocket;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);
			} catch (Exception ex) {
				ta_chat.append("Unexpected error... \n");
			}

		}

		@Override
		public void run() {
			String message, connect = "Connect", disconnect = "Disconnect", chat = "Chat";
			String[] data;

			try {
				while ((message = reader.readLine()) != null) {
					ta_chat.append("Received: " + message + "\n");
					data = message.split(":");

					for (String token : data) {
						ta_chat.append(token + "\n");
					}

					if (data[2].equals(connect)) {
						tellEveryone((data[0] + ":" + data[1] + ":" + chat));
						userAdd(data[0]);
					} else if (data[2].equals(disconnect)) {
						tellEveryone((data[0] + ":has disconnected." + ":" + chat));
						userRemove(data[0]);
					} else if (data[2].equals(chat)) {
						tellEveryone(message);
					} else {
						ta_chat.append("No Conditions were met. \n");
					}
				}
			} catch (Exception ex) {
				ta_chat.append("Lost a connection. \n");
				ex.printStackTrace();
				clientOutputStreams.remove(client);
			}
		}
	}
	
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Server().setVisible(true);
			}
		});
	}

}
