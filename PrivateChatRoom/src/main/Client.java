package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Client extends JFrame {
	
	//View vars
	private JPanel panelCenter;
	private JPanel panelRight;
	private JSplitPane splitHorizontal;
	private JButton b_anonymous;
	private JButton b_connect;
	private JButton b_disconnect;
	private JButton b_send;
	private JScrollPane jScrollPane1;
	private JLabel lb_address;
	private JLabel lb_name;
	private JLabel lb_key;
	private JLabel lb_port;
	private JLabel lb_username;
	private JTextArea ta_chat;
	private JTextField tf_address;
	private JTextField tf_chat;
	private JTextField tf_key;
	private JTextField tf_port;
	private JTextField tf_username;
	
	private String username, address = "localhost";
	private ArrayList<String> users = new ArrayList<>();
	private int port = 2222;
	private Boolean isConnected = false;

	private Socket sock;
	private BufferedReader reader;
	private PrintWriter writer;

	public Client() {
		initComponents();
	}
	
	private void initComponents() {
		panelCenter = new JPanel();
		panelRight = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		splitHorizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panelCenter,panelRight);
		lb_address = new JLabel();
		tf_address = new JTextField();
		tf_address.setPreferredSize(new Dimension(150, 25));
		lb_port = new JLabel();
		tf_port = new JTextField();
		tf_port.setPreferredSize(new Dimension(150, 25));
		lb_username = new JLabel();
		tf_username = new JTextField();
		tf_username.setPreferredSize(new Dimension(150, 25));
		lb_key = new JLabel();
		tf_key = new JTextField();
		tf_key.setPreferredSize(new Dimension(150, 25));
		b_connect = new JButton();
		b_disconnect = new JButton();
		b_anonymous = new JButton();
		jScrollPane1 = new JScrollPane();
		ta_chat = new JTextArea();
		tf_chat = new JTextField();
		b_send = new JButton();
		lb_name = new JLabel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Chat - Client's frame");
		setName("client");
		setResizable(false);

		lb_address.setText("Address : ");

		tf_address.setText("localhost");
		tf_address.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//tf_addressActionPerformed(evt);
			}
		});

		lb_port.setText("Port :");

		tf_port.setText("2222");
		tf_port.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//tf_portActionPerformed(evt);
			}
		});

		lb_username.setText("Username :");

		tf_username.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//tf_usernameActionPerformed(evt);
			}
		});

		lb_key.setText("Encrypt Key : ");

		b_connect.setText("Connect");
		b_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				connectActionPerformed(evt);
			}
		});

		b_disconnect.setText("Disconnect");
		b_disconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				disconnectActionPerformed(evt);
			}
		});

		b_anonymous.setText("Anonymous Login");
		b_anonymous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				anonymousActionPerformed(evt);
			}
		});

		ta_chat.setColumns(20);
		ta_chat.setRows(5);
		ta_chat.setEditable(false);
		ta_chat.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
		jScrollPane1.setViewportView(ta_chat);

		b_send.setText("SEND");
		b_send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sendActionPerformed(evt);
			}
		});
		
		this.setLayout(new BorderLayout());
		jScrollPane1.setPreferredSize(new Dimension(510, 300));
		tf_chat.setPreferredSize(new Dimension(400, 30));
		gbc.insets = new Insets(5, 5, 5, 5);
		panelCenter.setLayout(new GridBagLayout());
		b_send.setPreferredSize(new Dimension(100, 30));
		JPanel textPanel = new JPanel(new BorderLayout());
		textPanel.add(jScrollPane1, BorderLayout.CENTER);
		
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panelCenter.add(textPanel,gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		panelCenter.add(tf_chat,gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		panelCenter.add(b_send,gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panelRight.add(lb_username,gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		panelRight.add(tf_username,gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		panelRight.add(lb_address,gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		panelRight.add(tf_address,gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		panelRight.add(lb_port,gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 2;
		panelRight.add(tf_port,gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		panelRight.add(lb_key,gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 3;
		panelRight.add(tf_key,gbc);
		
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 4;
		panelRight.add(b_connect,gbc);
		
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 5;
		panelRight.add(b_disconnect,gbc);
		
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 6;
		panelRight.add(b_anonymous,gbc);
		
//		GroupLayout layout = new GroupLayout(getContentPane());
//		getContentPane().setLayout(layout);
//		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout
//				.createSequentialGroup().addContainerGap()
//				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//						.addGroup(layout.createSequentialGroup()
//								.addComponent(tf_chat, GroupLayout.PREFERRED_SIZE, 352,
//										GroupLayout.PREFERRED_SIZE)
//								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//								.addComponent(b_send, GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE))
//						.addComponent(jScrollPane1)
//						.addGroup(layout.createSequentialGroup()
//								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
//										.addComponent(lb_username, GroupLayout.DEFAULT_SIZE, 62,
//												Short.MAX_VALUE)
//										.addComponent(lb_address, GroupLayout.DEFAULT_SIZE,
//												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//								.addGap(18, 18, 18)
//								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
//										.addComponent(tf_address, GroupLayout.DEFAULT_SIZE, 89,
//												Short.MAX_VALUE)
//										.addComponent(tf_username))
//								.addGap(18, 18, 18)
//								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
//										.addComponent(lb_key, GroupLayout.DEFAULT_SIZE,
//												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//										.addComponent(lb_port, GroupLayout.DEFAULT_SIZE,
//												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
//										.addComponent(tf_key).addComponent(tf_port,
//												GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE))
//								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//										.addGroup(layout.createSequentialGroup().addComponent(b_connect).addGap(2, 2, 2)
//												.addComponent(b_disconnect).addGap(0, 0, Short.MAX_VALUE))
//										.addComponent(b_anonymous, GroupLayout.DEFAULT_SIZE,
//												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
//				.addContainerGap())
//				.addGroup(GroupLayout.Alignment.TRAILING,
//						layout.createSequentialGroup()
//								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//								.addComponent(lb_name).addGap(201, 201, 201)));
//		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//				.addGroup(layout.createSequentialGroup().addContainerGap()
//						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//								.addComponent(lb_address)
//								.addComponent(tf_address, GroupLayout.PREFERRED_SIZE,
//										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//								.addComponent(lb_port)
//								.addComponent(tf_port, GroupLayout.PREFERRED_SIZE,
//										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//								.addComponent(b_anonymous))
//						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
//						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
//								.addComponent(tf_username).addComponent(tf_key)
//								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//										.addComponent(lb_username).addComponent(lb_key).addComponent(b_connect)
//										.addComponent(b_disconnect)))
//						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
//						.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 310,
//								GroupLayout.PREFERRED_SIZE)
//						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
//						.addGroup(layout
//								.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(tf_chat)
//								.addComponent(b_send, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE))
//						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(lb_name)));
		this.add(splitHorizontal, BorderLayout.CENTER);
		pack();
		this.setLocationRelativeTo(null);
	}

	public void ListenThread() {
		Thread IncomingReader = new Thread(new IncomingReader());
		IncomingReader.start();
	}


	public void userAdd(String data) {
		users.add(data);
	}


	public void userRemove(String data) {
		ta_chat.append(data + " is now offline.\n");
	}


	public void writeUsers() {
		String[] tempList = new String[(users.size())];
		users.toArray(tempList);
		for (String token : tempList) {
			// users.append(token + "\n");
		}
	}


	public void sendDisconnect() {
		String bye = (username + ":has Disconected." + ":Disconnect");
		try {
			writer.println(bye);
			writer.flush();
		} catch (Exception e) {
			ta_chat.append("Could not send Disconnect message.\n");
		}
	}


	public void disconnect() {
		try {
			ta_chat.append("Disconnected.\n");
			sock.close();
		} catch (Exception ex) {
			ta_chat.append("Failed to disconnect.\n");
		}
		isConnected = false;
		tf_username.setEditable(true);
	}

	/*
	private void tf_addressActionPerformed(ActionEvent evt) {

	}

	private void tf_portActionPerformed(ActionEvent evt) {

	}

	private void tf_usernameActionPerformed(ActionEvent evt) {

	}
	*/

	private void connectActionPerformed(ActionEvent evt) {
		if (isConnected == false) {
			username = tf_username.getText();
			tf_username.setEditable(false);

			try {
				sock = new Socket(address, port);
				InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(streamreader);
				writer = new PrintWriter(sock.getOutputStream());
				writer.println(username + ":has connected.:Connect");
				writer.flush();
				isConnected = true;
			} catch (Exception ex) {
				ta_chat.append("Cannot Connect! Try Again. \n");
				tf_username.setEditable(true);
			}

			ListenThread();

		} else if (isConnected == true) {
			ta_chat.append("You are already connected. \n");
		}
	}

	private void disconnectActionPerformed(ActionEvent evt) {
		sendDisconnect();
		disconnect();
	}

	private void anonymousActionPerformed(ActionEvent evt) {
		tf_username.setText("");
		if (isConnected == false) {
			String anon = "anon";
			Random generator = new Random();
			int i = generator.nextInt(999) + 1;
			String is = String.valueOf(i);
			anon = anon.concat(is);
			username = anon;

			tf_username.setText(anon);
			tf_username.setEditable(false);

			try {
				sock = new Socket(address, port);
				InputStreamReader streamreader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(streamreader);
				writer = new PrintWriter(sock.getOutputStream());
				writer.println(anon + ":has connected.:Connect");
				writer.flush();
				isConnected = true;
			} catch (Exception ex) {
				ta_chat.append("Cannot Connect! Try Again. \n");
				tf_username.setEditable(true);
			}

			ListenThread();

		} else if (isConnected == true) {
			ta_chat.append("You are already connected. \n");
		}
	}

	private void sendActionPerformed(ActionEvent evt) {
		String nothing = "";
		if ((tf_chat.getText()).equals(nothing)) {
			tf_chat.setText("");
			tf_chat.requestFocus();
		} else {
			try {
				writer.println(username + ":" + tf_chat.getText() + ":" + "Chat");
				writer.flush();
			} catch (Exception ex) {
				ta_chat.append("Message was not sent. \n");
			}
			tf_chat.setText("");
			tf_chat.requestFocus();
		}

		tf_chat.setText("");
		tf_chat.requestFocus();
	}
	
	public class IncomingReader implements Runnable {
		@Override
		public void run() {
			String[] data;
			String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";

			try {
				while ((stream = reader.readLine()) != null) {
					data = stream.split(":");

					if (data[2].equals(chat)) {
						ta_chat.append(data[0] + ": " + data[1] + "\n");
						ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
					} else if (data[2].equals(connect)) {
						ta_chat.removeAll();
						userAdd(data[0]);
					} else if (data[2].equals(disconnect)) {
						userRemove(data[0]);
					} else if (data[2].equals(done)) {
						// users.setText("");
						writeUsers();
						users.clear();
					}
				}
			} catch (Exception ex) {
			}
		}
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Client().setVisible(true);
			}
		});
	}

}
