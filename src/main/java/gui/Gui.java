package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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

import data.Controller;
import vorlagen.TopicTable;



public class Gui extends Thread {

	/*
	 * 
	 * 
	 * 
	 */


	public JFrame frame;
	public JFrame framelogin;
	public JTextArea txt;

	public JPanel contentPane;
	public JPanel leftPanel;

	public JPanel rightPanel;
	public JPanel rightbottom;

	File file1 = null;
	File file2 = null;
	File file3 = null;

	boolean standardcert = true;

//	String stndserver = "127.0.0.1";
//	String stndserver = "192.168.0.100";
	
	String stndserver = "test.mosquitto.org";
	String stndport = "";


	String capath;
	String clientkeypath;
	String clientpath; 
	
	public Gui() {
		loginscreen();
	}

	private void init() {
		Controller.getInstance();
		capath = Controller.mqttconnection.capath;
		clientkeypath =Controller.mqttconnection.clientkeypath;
		clientpath = Controller.mqttconnection.clientpath;
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Client");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setSize(new Dimension(frame.getWidth(), frame.getHeight()));
		

	


//CONTENTPANEL
		contentPane = new JPanel();

		contentPane.setLayout(new BorderLayout());
		contentPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));
//------------------------------------------------------------------------------------------
//leftPanel - Panel in der Mitte zeigt die Topics in Form von einem Table an 
		leftPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createTitledBorder("Topic"));
		leftPanel.setLayout(new BorderLayout());
		//TABLE - die Klasse TopicTable übernimmt die Topics
		final TopicTable model = new TopicTable();
		
		final JTable table = new JTable(model);
		table.setFont(table.getFont().deriveFont(15f));
		table.setColumnSelectionAllowed(false);
//		table.setSelectionBackground(new Color(0,204,102));
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
		
		leftPanel.add(scrollPane);
//---------------------------------------------------------------------------------------------------------------------
///leftbottomPanel - CenterButtomPanel für den SubscribeButton
		JPanel leftbottomPanel = new JPanel();
		leftbottomPanel.setOpaque(true);
		leftbottomPanel.setBorder(BorderFactory.createTitledBorder(""));
		leftbottomPanel.setLayout(new BorderLayout());
		
		JButton btndisconnect = new JButton("disconnect client!");
		btndisconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Die Methode disconnect() unterbricht die Verbindung zum Broker
				Controller.getInstance().disconnect();
				loginscreen();
				
			}
		});
		leftbottomPanel.add(btndisconnect);
		leftPanel.add(leftbottomPanel, BorderLayout.SOUTH);
//------------------------------------------------------------------------------------------------
// RIGHTPANEL - Panel auf der Rechten Seite
		rightPanel = new JPanel();
//		if(frame.getWidth() < 1000) {
//			rightPanel.setPreferredSize(new Dimension(1000, frame.getHeight()));
//		}
		rightPanel.setLayout(new BorderLayout());
//		rightPanel.setBorder(BorderFactory.createTitledBorder("Data"));
//------------------------------------------------------------------------------------------------

//TEXTAREA - TextArea für die Empfangen Nachrichten
		txt = new JTextArea();
		txt.setPreferredSize(new Dimension(rightPanel.getWidth(), txt.getFont().getSize()*23));
		txt.setEditable(false);
		txt.setLineWrap(false);
		txt.setFont(txt.getFont().deriveFont(20f));
		JScrollPane sp = new JScrollPane(txt);
//------------------------------------------------------------------------------------------------
//NachrichtPanel
		JPanel nachrichtPanel = new JPanel();
		nachrichtPanel.setLayout(new BorderLayout());
		nachrichtPanel.setBorder(BorderFactory.createTitledBorder("Nachrichten"));
		nachrichtPanel.add(sp);
		
//------------------------------------------------------------------------------------------------		
// RIGHTBOTTOM - Panel auf der rechten Seite unten für die Datankurve
		rightbottom = new JPanel();
//		rightbottom.setLayout(new BorderLayout());
//		rightbottom.setBorder(BorderFactory.createTitledBorder("Datenkurve"));
//------------------------------------------------------------------------------------------------

		rightPanel.add(rightbottom);
		rightPanel.add(nachrichtPanel, BorderLayout.NORTH);

		contentPane.add(leftPanel, BorderLayout.WEST);
		contentPane.add(rightPanel, BorderLayout.CENTER);

		
		JMenuBar bar = new JMenuBar();
		
		JMenu menu = new JMenu("TEST");
		menu.add(new JMenuItem("test"));
		
		bar.add(menu);
		
//		frame.setResizable(false);
		
		frame.setJMenuBar(bar);
		frame.getContentPane().add(contentPane);
		
		
		frame.setVisible(true);
	}

	
	
	
	
	
	
	//Screen für den Loginwindow
	public void loginscreen() {
		//Wenn ein Frame existiert wird er zerstört
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
//------------------------------------------------------------------------------------------------
//LOGINSCREEN
		JPanel loginscreen = new JPanel();
		loginscreen.setLayout(new BorderLayout());
		loginscreen.setPreferredSize(new Dimension(framelogin.getWidth(), framelogin.getHeight()));
//------------------------------------------------------------------------------------------------
//OBENPANEL
		JPanel obenpanel = new JPanel();
		obenpanel.setLayout(new BoxLayout(obenpanel, BoxLayout.Y_AXIS));
		obenpanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
//------------------------------------------------------------------------------------------------
//ADDRESSEPANEL		
		JPanel addressePanel = new JPanel();
		addressePanel.setLayout(new FlowLayout());
//		addressePanel.setBackground(blackpanel);
		

		
		JLabel serverlabel = new JLabel("Addresse: ");
//		serverlabel.setForeground(Color.WHITE);
//		serverlabel.setBackground(blackpanel);
		

		addressePanel.add(serverlabel);
		
		final JTextField server = new JTextField(21);
//		server.setForeground(Color.white);
//		server.setBackground(blacktextarea);
		
		server.setHorizontalAlignment(JTextField.CENTER);
		server.setText(stndserver);
		server.setBorder(BorderFactory.createEtchedBorder(0));
		addressePanel.add(server);
		
		final JTextField port = new JTextField(10);
//		port.setForeground(Color.white);
//		port.setBackground(blacktextarea);
		
		port.setHorizontalAlignment(JTextField.CENTER);
		port.setText(stndserver);
		port.setBorder(BorderFactory.createEtchedBorder(0));
		port.setText(stndport);
		addressePanel.add(port);
		
//------------------------------------------------------------------------------------------------
//FILESCHOOSEPANEL
		final JPanel fileChoosePanel = new JPanel();
		fileChoosePanel.setLayout(new FlowLayout());
//		fileChoosePanel.setBackground(blackpanel);
		
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
				try {
					file1 = new File(capath);
					file2 = new File(clientkeypath);
					file3 = new File(clientpath);
				}catch(Exception e0) {
//					JOptionPane.showMessageDialog(null, " Keine Standardzertifikate!" ,
//							"ERROR" , JOptionPane.ERROR_MESSAGE);
				}

				Controller.getInstance();
				Controller.mqttconnection.cacertfile = file1;
				Controller.mqttconnection.clientkeyfile = file2;
				Controller.mqttconnection.clientpemfile = file3;
				btnca.setForeground(Color.RED);
				btnkey.setForeground(Color.RED);
				btnclient.setForeground(Color.RED);
				btnstnd.setForeground(Color.GREEN.darker());				
			}
		});
		
		

		btnca.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent e) {
				file1 = showOpenDialog("CHOOSE FILE: " + btnca.getName());
				Controller.getInstance();
				Controller.mqttconnection.cacertfile = file1;
				standardcert = false;
				btnca.setForeground(Color.GREEN.darker());
				btnstnd.setForeground(Color.RED);
			}
		});
		fileChoosePanel.add(btnca);
		
		btnkey.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent e) {
				file2 = showOpenDialog("CHOOSE FILE: " + btnkey.getName());
				Controller.getInstance();
				Controller.mqttconnection.clientkeyfile = file2;
				standardcert = false;
				btnkey.setForeground(Color.GREEN.darker());
				btnstnd.setForeground(Color.RED);
			}
		});
		fileChoosePanel.add(btnkey);
		
		btnclient.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent e) {
				file3 = showOpenDialog("CHOOSE FILE: " + btnclient.getName());
				Controller.getInstance();
				Controller.mqttconnection.clientpemfile = file3;
				standardcert = false;
				btnclient.setForeground(Color.GREEN.darker());
				btnstnd.setForeground(Color.RED);

			}
		});
		fileChoosePanel.add(btnclient);
		

		fileChoosePanel.add(btnstnd);
		fileChoosePanel.setVisible(false);
//------------------------------------------------------------------------------------------------
//USERPANEL
		JPanel userPanel = new JPanel();
		userPanel.setLayout(new FlowLayout());
//		userPanel.setBackground(blackpanel);
		
		
		JLabel usernamelabel = new JLabel("Username");
//		usernamelabel.setForeground(Color.WHITE);
		
		userPanel.add(usernamelabel);
		
		final JTextField username = new JTextField(20);
//		username.setForeground(Color.white);
//		username.setBackground(blacktextarea);
		username.setHorizontalAlignment(JTextField.CENTER);
		username.setText(stndserver);
		username.setBorder(BorderFactory.createEtchedBorder(0));
		username.setText("");
		
		userPanel.add(username);
//------------------------------------------------------------------------------------------------
//PASSWORDPANEL
		JPanel passwordPanel = new JPanel();
//		passwordPanel.setBackground(blackpanel);
//		passwordlabel.setForeground(Color.WHITE);
		
		passwordPanel.setLayout(new FlowLayout());

		JLabel passwordlabel = new JLabel("Passwort");
//		passwordlabel.setForeground(Color.WHITE);
		passwordPanel.add(passwordlabel);
		
		final JPasswordField password = new JPasswordField(20);

//		password.setForeground(Color.white);
//		password.setBackground(blacktextarea);
		password.setHorizontalAlignment(JTextField.CENTER);
		password.setText(stndserver);
		password.setBorder(BorderFactory.createEtchedBorder(0));
		password.setText("");
		passwordPanel.add(password);
//-------------------------------------------------------------------------------------------------
//RADIOBTNPANEL
		JPanel RadiobtnPanel = new JPanel();
		RadiobtnPanel.setLayout(new FlowLayout());
//		RadiobtnPanel.setBackground(blackpanel);
		
		final JRadioButton runencrypted = new JRadioButton("unverschlüsselt");
//		runencrypted.setBackground(blackpanel);
//		runencrypted.setForeground(Color.WHITE);
		runencrypted.setSelected(true);
		runencrypted.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fileChoosePanel.setVisible(false);
				
				Controller.getInstance();
				Controller.mqttconnection.cacertfile = file1;
				Controller.mqttconnection.clientkeyfile = file2;
				Controller.mqttconnection.clientpemfile = file3;
			}
		});
		
		final JRadioButton rencrypted = new JRadioButton("verschlüsselt");
//		rencrypted.setBackground(blackpanel);
//		rencrypted.setForeground(Color.WHITE);
		rencrypted.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fileChoosePanel.setVisible(true);

			}
		});
		
		ButtonGroup group = new ButtonGroup();
		group.add(runencrypted);
		group.add(rencrypted);

		RadiobtnPanel.add(runencrypted);
		RadiobtnPanel.add(rencrypted);
//------------------------------------------------------------------------------------------------
		//alles wird zum obenpanel hinzugefügt
		obenpanel.add(addressePanel);
		obenpanel.add(userPanel);
		obenpanel.add(passwordPanel);
		obenpanel.add(RadiobtnPanel);
		obenpanel.add(fileChoosePanel);
//------------------------------------------------------------------------------------------------
//UNTENPANEL
		JPanel untenpanel = new JPanel();
		
		JButton btnlogin = new JButton("login");
		btnlogin.setPreferredSize(new Dimension(framelogin.getWidth() - 50, 30));
		btnlogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean a = false;
				if (runencrypted.isSelected()) {
					
					if(port.getText().matches("") && username.getText().matches("") && password.getText().matches("")) {
						
						port.setText("1883");
					}else if(port.getText().matches("")){
						port.setText("1884");
					}
						
					Controller.getInstance();
					a = Controller.mqttconnection.connectionunverschluesselt(server.getText(),
							port.getText(), username.getText(), password.getText());
					stndserver = server.getText();
				}
				if (rencrypted.isSelected()) {
					if(port.getText().matches("")) {
						port.setText("8883");
					}
					
					if (standardcert) {
					} else if (!standardcert && file1 != null && file2 != null && file3 != null) {
					} else {
						JOptionPane.showMessageDialog(null, "Es wurden keine bzw. nicht alle Zertifikate ausgewählt!\nBitte wählen sie alle Zertifikate!" ,
								"ERROR" , JOptionPane.ERROR_MESSAGE);
						return;
					}
					Controller.getInstance();
					a = Controller.mqttconnection.connectionverschluesselt(server.getText(),
							port.getText(), username.getText(), password.getText());
				}
				

				
				if (a) {
					init();
					framelogin.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Keine Verbindungsaufbau möglich!\nBitte vesuchen sie es erneut!" ,
							"Connection" , JOptionPane.ERROR_MESSAGE);
					return;
				}
				

			}
		});
		
		untenpanel.add(btnlogin);
		
		loginscreen.add(untenpanel, BorderLayout.SOUTH);
		loginscreen.add(obenpanel, BorderLayout.CENTER);
		framelogin.getContentPane().add(loginscreen);
		framelogin.pack();
	}

	private File showOpenDialog(String filename) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(filename);
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.updateUI();
		int choice = fileChooser.showOpenDialog(null);
		if (choice == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

}
