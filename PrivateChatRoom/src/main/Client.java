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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

	// View vars
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

	// Key shift
	private final int MAX_SHIFT = 126;
	private final int MIN_SHIFT = 32;
	private final int MAX_KEY = MAX_SHIFT - MIN_SHIFT;

	public Client() {
		initComponents();
	}

	/**
	 * Izgled
	 */
	private void initComponents() {
		panelCenter = new JPanel();
		panelRight = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		splitHorizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCenter, panelRight);
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

		lb_port.setText("Port :");

		tf_port.setText("2222");

		lb_username.setText("Username :");

		lb_key.setText("Encrypt Key : ");

		tf_key.setText("0");
		
		tf_key.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {}
			@Override
			public void keyReleased(KeyEvent arg0) {
				Integer key = getKey();
				if (key > MAX_KEY) {
					tf_key.setText(String.valueOf(MAX_KEY));
				} else if (key < 0) {
					tf_key.setText(String.valueOf(0));
				}
			}
			@Override
			public void keyPressed(KeyEvent arg0) {}
		});

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

		tf_chat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sendActionPerformed(evt);
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
		panelCenter.add(textPanel, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		panelCenter.add(tf_chat, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		panelCenter.add(b_send, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		panelRight.add(lb_username, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 0;
		panelRight.add(tf_username, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		panelRight.add(lb_address, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 1;
		panelRight.add(tf_address, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 2;
		panelRight.add(lb_port, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 2;
		panelRight.add(tf_port, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 3;
		panelRight.add(lb_key, gbc);

		gbc.gridwidth = 1;
		gbc.gridx = 1;
		gbc.gridy = 3;
		panelRight.add(tf_key, gbc);

		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 4;
		panelRight.add(b_connect, gbc);

		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 5;
		panelRight.add(b_disconnect, gbc);

		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 6;
		panelRight.add(b_anonymous, gbc);

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

	/**
	 * Slanje poruke o diskonektovanju na server
	 */
	public void sendDisconnect() {
		String bye = (username + ":has Disconected." + ":Disconnect");
		try {
			writer.println(bye);
			writer.flush();
		} catch (Exception e) {
			ta_chat.append("Could not send Disconnect message.\n");
		}
	}

	/**
	 * Gasenje soketa klijenta
	 */
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

	/**
	 * Konektovanje klijenta na server
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

	/**
	 * Proces diskonektovanja
	 */
	private void disconnectActionPerformed(ActionEvent evt) {
		sendDisconnect();
		disconnect();
	}

	/**
	 * Pravljenje anonimnog klijenta
	 */
	private void anonymousActionPerformed(ActionEvent evt) {
		tf_username.setText("");
		if (isConnected == false) {
			String anon = "user";
			Random generator = new Random();
			int i = generator.nextInt(999) + 1;
			anon += i;
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

	/**
	 * Slanje poruke drugim klijentima
	 * Desava se na klik dugmeta send i klikom dugmeta enter na tastaturi
	 */
	private void sendActionPerformed(ActionEvent evt) {
		if ((tf_chat.getText()).isEmpty()) {
			tf_chat.setText("");
			tf_chat.requestFocus();
		} else {
			try {
				String str = username + ":" + encrypt(tf_chat.getText().trim()) + ":" + "Chat";
				writer.println(str);
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

	/**
	 * Enkripcija texta
	 * @param string - Text za enkripciju
	 */
	private String encrypt(String string) {
		String res = "";
		char[] text = string.toCharArray();
		for (int i = 0; i < text.length; i++) {
			int tmp = (int) text[i];
			if (tmp + getKey() > MAX_SHIFT) {
				int difference = tmp + getKey() - MAX_SHIFT;
				tmp = MIN_SHIFT + difference - 1;
			} else {
				tmp += getKey();
			}
			res += (char) tmp;
		}
		return res;
	}

	/**
	 * Dekripcija texta
	 * @param string - Text za dekripciju
	 */
	private String decrypt(String string) {
		String res = "";
		char[] text = string.toCharArray();
		for (int i = 0; i < text.length; i++) {
			int tmp = (int) text[i];
			if (tmp - getKey() < MIN_SHIFT) {
				int difference = MIN_SHIFT - Math.abs(tmp - getKey());
				tmp = MAX_SHIFT - difference + 1;
			} else {
				tmp = tmp - getKey();
			}
			res += (char) tmp;
		}
		return res;
	}

	/**
	 * Uzima kljuc iz tf_key
	 * @return 0 - ako je unesen text nije broj, u suprotnom vraca broj
	 */
	private int getKey() {
		int key;
		try {
			key = Integer.parseInt(tf_key.getText().trim());
		} catch (Exception e) {
			key = 0;
		}
		return key;
	}

	/**
	 * Proces citanja dolazecih poruka
	 */
	public class IncomingReader implements Runnable {
		@Override
		public void run() {
			String[] data;
			String stream, done = "Done", connect = "Connect", disconnect = "Disconnect", chat = "Chat";

			try {
				while ((stream = reader.readLine()) != null) {
					data = stream.split(":");

					if (data[2].equals(chat)) {
						if (data[1].equals("has connected.") || data[1].equals("has disconnected.")) {
							ta_chat.append(data[0] + ": " + data[1] + " \n");
						} else {
							ta_chat.append(data[0] + ": " + decrypt(data[1]) + " \n");
						}
						ta_chat.setCaretPosition(ta_chat.getDocument().getLength());
					} else if (data[2].equals(connect)) {
						ta_chat.removeAll();
						userAdd(data[0]);
					} else if (data[2].equals(disconnect)) {
						userRemove(data[0]);
					} else if (data[2].equals(done)) {
						users.clear();
					}
				}
			} catch (Exception ex) {
				System.err.println("Something went wrong!");
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
