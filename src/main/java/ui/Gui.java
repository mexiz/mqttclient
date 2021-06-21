package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

import data.Controller;
import utils.TopicTable;

public class Gui extends Thread {

	public JFrame frame;
	public JFrame framelogin;
	public JTextArea txt;

	public JPanel contentPane;
	public JPanel leftPanel;

	public JPanel rightPanel;
	public JPanel rightbottom;

	boolean standardcert = true;

//	String stndserver = "127.0.0.1";
//	String stndserver = "192.168.0.100";

	String stndserver = "test.mosquitto.org";
	String stndport = "";

	String capath;
	String clientkeypath;
	String clientpath;

	public Gui() {
		loginscreeninit();
	}

	private void mainscreeninit() {
		Controller.getInstance();

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Client");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(new Dimension(frame.getWidth(), frame.getHeight()));

//ContenPanel
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));

//Menu - change gui theme
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Settings");
		JMenu thememenu = new JMenu("Theme");
		JMenuItem lightitem = new JMenuItem("Light-Theme");
		JMenuItem darkitem = new JMenuItem("Dark-Theme");
		lightitem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(frame,
						"Die Verbindung wird beim Wechsel getrennt.\nWollen sie fortfahren?", "Information",
						JOptionPane.YES_NO_OPTION);
				if (n == 1)
					return;
				try {
					UIManager.setLookAndFeel(new FlatLightLaf());
					Controller.getInstance().disconnect();
					loginscreeninit();
				} catch (Exception e54) {
				}

			}
		});
		darkitem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(frame,
						"Die Verbindung wird beim Wechsel getrennt.\nWollen sie fortfahren?", "Information",
						JOptionPane.YES_NO_OPTION);
				if (n == 1)
					return;
				try {
					UIManager.setLookAndFeel(new FlatDarkLaf());
					Controller.getInstance().disconnect();
					loginscreeninit();
				} catch (Exception e54) {
				}

			}
		});

		thememenu.add(lightitem);
		thememenu.add(darkitem);
		menu.add(thememenu);
		bar.add(menu);

		frame.setJMenuBar(bar);

//LeftPanel
		leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createTitledBorder("Topic"));
		leftPanel.setLayout(new BorderLayout());

		// Table for the topics
		final TopicTable model = new TopicTable();
		final JTable table = new JTable(model);
		table.setFont(table.getFont().deriveFont(15f));
		table.setColumnSelectionAllowed(false);
		table.setRowHeight(30);
		table.setPreferredScrollableViewportSize(new Dimension(200, 200));
		table.setFillsViewportHeight(true);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int i = table.getSelectedRow();
				if (TopicTable.topics[i][0].toString() != Controller.getInstance().currentsubscribedtopic) {
					Controller.getInstance().subscribeto(TopicTable.topics[i][0].toString());
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Topic",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));

		// add to left JPanel
		leftPanel.add(scrollPane);

		///LeftbottomPanel Button for disconnect
		JPanel leftbottomPanel = new JPanel();
		leftbottomPanel.setOpaque(true);
		leftbottomPanel.setBorder(BorderFactory.createTitledBorder(""));
		leftbottomPanel.setLayout(new BorderLayout());

		JButton btndisconnect = new JButton("disconnect client!");
		btndisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				int n = JOptionPane.showConfirmDialog(frame, "Die Verbindung wird getrennt.\nWollen sie fortfahren?",
						"Information", JOptionPane.YES_NO_OPTION);
				if (n == 1)
					return;
				// unsubscribe to current subscribed and disconenct to client
				Controller.getInstance().disconnect();
				// dispose frame and start loginscreenframe
				loginscreeninit();

			}
		});
		leftbottomPanel.add(btndisconnect);

		// add to left JPanel
		leftPanel.add(leftbottomPanel, BorderLayout.SOUTH);

// RightPanel
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		
		// MessagePanel
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BorderLayout());
		messagePanel.setBorder(BorderFactory.createTitledBorder("Nachrichten"));

		//Textarea
		txt = new JTextArea();
		txt.setFont(txt.getFont().deriveFont(20f));
		txt.setRows(Controller.getInstance().maxSizeMessage);
		txt.setEditable(false);
		txt.setLineWrap(false);
		
//		JScrollPane sp = new JScrollPane(txt);
//		messagePanel.add(sp);
		messagePanel.add(txt);

		rightPanel.add(messagePanel, BorderLayout.NORTH);
//
		
		contentPane.add(leftPanel, BorderLayout.WEST);
		contentPane.add(rightPanel, BorderLayout.CENTER);

		frame.getContentPane().add(contentPane);
		frame.setVisible(true);
	}

	
/*
 * 
 * 
 * frame for login
 * 
 * 
 */
	
	
	public void loginscreeninit() {	
		if (frame != null) {
			frame.dispose();
		}
//FRAMELOGIN
		framelogin = new JFrame();
		framelogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framelogin.setTitle("MQTT: Connect with Server");
		framelogin.setVisible(true);
		framelogin.setExtendedState(JFrame.MAXIMIZED_BOTH);
		framelogin.setSize(500, 300);
		framelogin.setLocationRelativeTo(null);
//LOGINSCREEN
		JPanel loginscreen = new JPanel();
		loginscreen.setLayout(new BorderLayout());
		loginscreen.setPreferredSize(new Dimension(framelogin.getWidth(), framelogin.getHeight()));
//OBENPANEL
		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
//ADDRESSEPANEL		
		JPanel addressePanel = new JPanel();
		addressePanel.setLayout(new FlowLayout());

		JLabel serverlabel = new JLabel("Addresse: ");

		addressePanel.add(serverlabel);

		final JTextField server = new JTextField(21);

		server.setHorizontalAlignment(JTextField.CENTER);
		server.setText(stndserver);
		server.setBorder(BorderFactory.createEtchedBorder(0));
		addressePanel.add(server);

		final JTextField port = new JTextField(10);

		port.setHorizontalAlignment(JTextField.CENTER);
		port.setText(stndserver);
		port.setBorder(BorderFactory.createEtchedBorder(0));
		port.setText(stndport);
		addressePanel.add(port);

//FILESCHOOSEPANEL
		final JPanel fileChoosePanel = new JPanel();
		fileChoosePanel.setLayout(new FlowLayout());

		final JButton btnstnd = new JButton("Standart Zertifikate");
		btnstnd.setForeground(Color.GREEN.darker());

		final JButton btnca = new JButton("ca-cert");
		btnca.setForeground(Color.RED);

		final JButton btnkey = new JButton("key-cert");
		btnkey.setForeground(Color.RED);

		final JButton btnclient = new JButton("client-cert");
		btnclient.setForeground(Color.RED);

		btnstnd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				standardcert = true;

				Controller.getInstance();

				Controller.mqttconnection.capath = Controller.mqttconnection.stndcapath;

				Controller.mqttconnection.clientkeypath = Controller.mqttconnection.stndclientkeypath;

				Controller.mqttconnection.clientpath = Controller.mqttconnection.stndclientpath;

				btnca.setForeground(Color.RED);
				btnkey.setForeground(Color.RED);
				btnclient.setForeground(Color.RED);
				btnstnd.setForeground(Color.GREEN.darker());
			}
		});

		btnca.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String file1 = showOpenDialog("Wähle das Zertifikat -> " + btnca.getName() + " aus!");
				Controller.getInstance();
				Controller.mqttconnection.capath = file1;
				standardcert = false;
				btnca.setForeground(Color.GREEN.darker());
				btnstnd.setForeground(Color.RED);
			}
		});
		fileChoosePanel.add(btnca);

		btnkey.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String file2 = showOpenDialog("Wähle den Schlüssel -> " + btnkey.getName() + " aus !");
				Controller.getInstance();
				Controller.mqttconnection.clientkeypath = file2;
				standardcert = false;
				btnkey.setForeground(Color.GREEN.darker());
				btnstnd.setForeground(Color.RED);
			}
		});
		fileChoosePanel.add(btnkey);

		btnclient.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String file3 = showOpenDialog("Wähle das Zertifikat -> " + btnclient.getName() + " aus!");
				Controller.getInstance();
				Controller.mqttconnection.clientpath = file3;
				standardcert = false;
				btnclient.setForeground(Color.GREEN.darker());
				btnstnd.setForeground(Color.RED);

			}
		});
		fileChoosePanel.add(btnclient);

		fileChoosePanel.add(btnstnd);
		fileChoosePanel.setVisible(false);
//USERPANEL
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new FlowLayout());

		JLabel usernamelabel = new JLabel("Username");

		userPanel.add(usernamelabel);

		final JTextField username = new JTextField(20);
		username.setHorizontalAlignment(JTextField.CENTER);
		username.setText(stndserver);
		username.setBorder(BorderFactory.createEtchedBorder(0));
		username.setText("");

		userPanel.add(username);
//------------------------------------------------------------------------------------------------
//PASSWORDPANEL
		JPanel passwordPanel = new JPanel();
		passwordPanel.setLayout(new FlowLayout());

		JLabel passwordlabel = new JLabel("Passwort");
		passwordPanel.add(passwordlabel);

		final JPasswordField password = new JPasswordField(20);

		password.setHorizontalAlignment(JTextField.CENTER);
		password.setText(stndserver);
		password.setBorder(BorderFactory.createEtchedBorder(0));
		password.setText("");
		passwordPanel.add(password);

//RADIOBTNPANEL
		JPanel radiobtnPanel = new JPanel();
		radiobtnPanel.setLayout(new FlowLayout());

		final JRadioButton runencrypted = new JRadioButton("unverschlüsselt");

		runencrypted.setSelected(true);
		runencrypted.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChoosePanel.setVisible(false);
			}
		});

		final JRadioButton rencrypted = new JRadioButton("verschlüsselt");
		rencrypted.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileChoosePanel.setVisible(true);

			}
		});

		ButtonGroup group = new ButtonGroup();
		group.add(runencrypted);
		group.add(rencrypted);

		radiobtnPanel.add(runencrypted);
		radiobtnPanel.add(rencrypted);
//------------------------------------------------------------------------------------------------
		northPanel.add(addressePanel);
		northPanel.add(userPanel);
		northPanel.add(passwordPanel);
		northPanel.add(radiobtnPanel);
		northPanel.add(fileChoosePanel);
//------------------------------------------------------------------------------------------------
//UNTENPANEL
		JPanel southPanel = new JPanel();

		JButton btnlogin = new JButton("login");
		btnlogin.setPreferredSize(new Dimension(framelogin.getWidth() - 50, 30));
		
		//connect
		btnlogin.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {

				boolean clientconnected = false;
				if (runencrypted.isSelected()) {

					if (port.getText().matches("") && username.getText().matches("")
							&& password.getText().matches("")) {
						port.setText("1883");
					} else if (port.getText().matches("")) {
						port.setText("1884");
					}

					Controller.getInstance();
					clientconnected = Controller.mqttconnection.connectionunverschluesselt(server.getText(),
							port.getText(), username.getText(), password.getText());
					stndserver = server.getText();
				}
				if (rencrypted.isSelected()) {
					port.setText("8883");

					if (standardcert) {
					} else if (!standardcert && btnca.getForeground() != Color.GREEN.darker()
							&& btnclient.getForeground() != Color.GREEN.darker()
							&& btnkey.getForeground() != Color.GREEN.darker()) {
					} else {
						JOptionPane.showMessageDialog(null,
								"Es wurden keine bzw. nicht alle Zertifikate ausgewählt!\nBitte wählen sie alle Zertifikate!",
								"ERROR", JOptionPane.ERROR_MESSAGE);
						return;
					}
					Controller.getInstance();
					clientconnected = Controller.mqttconnection.connectionverschluesselt(server.getText(),
							port.getText(), username.getText(), password.getText());
				}

				if (clientconnected) {
					mainscreeninit();
					framelogin.dispose();
				} else {
					return;
				}

			}
		});

		southPanel.add(btnlogin);

		loginscreen.add(southPanel, BorderLayout.SOUTH);
		loginscreen.add(northPanel, BorderLayout.CENTER);
		
		
		framelogin.getContentPane().add(loginscreen);
		framelogin.pack();
	}

	private String showOpenDialog(String filename) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(filename);
		int choice = fileChooser.showOpenDialog(null);
		if (choice == JFileChooser.APPROVE_OPTION) 
			return fileChooser.getSelectedFile().getAbsolutePath();
		
		return null;
	}

}
