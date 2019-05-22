package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
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
	private ServerSocket serverSock;

	public Server() {
		initComponents();
	}

	/**
	 * Izgled
	 */
	private void initComponents() {
		panelCenter = new JPanel(new BorderLayout());
		panelRight = new JPanel();
		panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));
		splitHorizontal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCenter, panelRight);
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
		panelRight.add(b_end);
		panelRight.add(b_users);
		panelRight.add(b_clear);

		this.add(splitHorizontal, BorderLayout.CENTER);
		pack();
	}

	/**
	 * Gasenje servera
	 */
	private void endActionPerformed(ActionEvent evt) {
		ta_chat.append("Server stopping");

		GuiUpdater updater = new GuiUpdater(ta_chat, serverSock);
		updater.execute();
	}

	/**
	 * Pokretanje servera
	 */
	private void startActionPerformed(ActionEvent evt) {
		Thread starter = new Thread(new ServerStart());
		starter.start();

		ta_chat.append("Server started...\n");
	}

	/**
	 * Prikaz aktivnih korisnika
	 */
	private void usersActionPerformed(ActionEvent evt) {
		if (users == null || users.isEmpty()) {
			ta_chat.append("No online users!\n");
		} else {
			ta_chat.append("Online users : \n");
			for (String current_user : users) {
				ta_chat.append(current_user);
				ta_chat.append("\n");
			}
		}
	}

	/**
	 * Ciscenje radne povrsine servera
	 */
	private void clearActionPerformed(ActionEvent evt) {
		ta_chat.setText("");
	}

	/**
	 * Dodavanje korisnika u listu
	 * @param data - ime korisnika kojeg dodajemo
	 */
	public void userAdd(String data) {
		String done = "Server: :Done", name = data;
		users.add(name);

		tellEveryone(done);
	}

	/**
	 * Brisanje korisnika u listu
	 * @param data - ime korisnika kojeg brisemo
	 */
	public void userRemove(String data) {
		String done = "Server: :Done", name = data;
		users.remove(name);

		tellEveryone(done);
	}

	/**
	 * Prosledjivanje poruke svim povezanim klijentima
	 * @param message - poruka koju prosledjujemo
	 */
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

	/**
	 * Thread za pokretanje servera, na vezbama obicno pravljeno kao odvojena klasa Server
	 */
	public class ServerStart implements Runnable {
		@Override
		public void run() {
			clientOutputStreams = new ArrayList<PrintWriter>();
			users = new ArrayList<>();

			try {
				serverSock = new ServerSocket(2222);

				while (true) {
					//TODO ne radi iz nepoznatog razloga
					if (serverSock.isClosed()) return;
					
					Socket clientSock = serverSock.accept();
					PrintWriter writer = new PrintWriter(clientSock.getOutputStream());
					clientOutputStreams.add(writer);

					Thread listener = new Thread(new ClientHandler(clientSock, writer));
					listener.start();
					ta_chat.append("Got a connection!\n");
				}
			} catch (Exception ex) {
//				ex.printStackTrace();
				System.err.println("Error making a connection!");
			}
		}
	}

	/**
	 * Thread za komunikaciju sa klijentom, na vezbama obicno pravljeno kao odvojena klasa ServerThread
	 */
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

					if (data[2].equals(connect)) {
						tellEveryone((data[0] + ":" + data[1] + ":" + chat));
						userAdd(data[0]);
					} else if (data[2].equals(disconnect)) {
						tellEveryone((data[0] + ":has disconnected." + ":" + chat));
						userRemove(data[0]);
					} else if (data[2].equals(chat)) {
						tellEveryone(message);
					} else {
						ta_chat.append("No Conditions were met!\n");
					}
				}
			} catch (Exception ex) {
				ta_chat.append("Lost a connection!\n");
//				ex.printStackTrace();
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
