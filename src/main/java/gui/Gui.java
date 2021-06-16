package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextField;
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
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.eclipse.paho.client.mqttv3.MqttException;

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

	public JPanel centerPanel;

	public JPanel rightPanel;
	public JPanel rightbottom;

	File file1 = null;
	File file2 = null;
	File file3 = null;

	boolean standardcert = true;

	public Gui() {
		init();
		loginscreen();
	}

	private void init() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("MQTT: Client");
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

//CONTENTPANEL
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.setBackground(Color.black.brighter());
		contentPane.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight()));

//LEFTPANEL 
//		leftPanel = new JPanel();
//		leftPanel.setPreferredSize(new Dimension(100, frame.getHeight()));
//		leftPanel.setBorder(BorderFactory.createTitledBorder("optionen"));

//CENTERPANEL - Panel in der Mitte zeigt die Topics in Form von einem Table an 
		centerPanel = new JPanel();
		centerPanel.setBorder(BorderFactory.createTitledBorder("Topic"));
		centerPanel.setLayout(new BorderLayout());
//TABLE - die Klasse TopicTable übernimmt die Topics
		final TopicTable model = new TopicTable();
		final JTable table = new JTable(model);
		table.setFont(table.getFont().deriveFont(15f));
		table.setColumnSelectionAllowed(false);
		table.setSelectionBackground(Color.green.brighter());
		table.setRowHeight(30);
		table.setPreferredScrollableViewportSize(new Dimension(200, 200));
		table.setFillsViewportHeight(true);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int i = table.getSelectedRow();
				if (model.topics[i][0].toString() != Controller.getInstance().currentsubscribedtopic) {
					Controller.getInstance().subscribeto(model.topics[i][0].toString());
				}
			}
		});
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Topic",
				TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
		centerPanel.add(scrollPane);

////CENTERBOTTOMPANEL - CenterButtomPanel für den SubscribeButton
//		JPanel centerbottomPanel = new JPanel();
//		centerbottomPanel.setOpaque(true);
//		centerbottomPanel.setBorder(BorderFactory.createTitledBorder(""));
//		centerbottomPanel.setLayout(new BorderLayout());

// RIGHTPANEL - Panel auf der Rechten Seite
		rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(1000, frame.getHeight()));
		rightPanel.setLayout(new BorderLayout());
		rightPanel.setBorder(BorderFactory.createTitledBorder("Topic Nachrichten"));

//TEXTAREA - TextArea für die Empfangen Nachrichten
		txt = new JTextArea();
		txt.setPreferredSize(new Dimension(rightPanel.getWidth(), 265));
		txt.setEditable(false);
		txt.setLineWrap(false);

		txt.setFont(txt.getFont().deriveFont(20f));
		JScrollPane sp = new JScrollPane(txt);

// RIGHTBOTTOM - Panel auf der rechten Seite unten für die Datankurve
		rightbottom = new JPanel();
		rightbottom.setLayout(new BorderLayout());
		rightbottom.setBorder(BorderFactory.createTitledBorder("Datenkurve"));

		rightPanel.add(rightbottom);
		rightPanel.add(sp, BorderLayout.NORTH);

		contentPane.add(centerPanel, BorderLayout.CENTER);
		contentPane.add(rightPanel, BorderLayout.EAST);
//		contentPane.setVisible(false);

		frame.getContentPane().add(contentPane);
		frame.pack();
		frame.setVisible(false);

	}

	public void loginscreen() {
		frame.setVisible(false);
		framelogin = new JFrame();
		framelogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		framelogin.setTitle("MQTT: Connect with Server");
		framelogin.setVisible(true);
		framelogin.setExtendedState(JFrame.MAXIMIZED_BOTH);
		framelogin.setSize(500, 300);
		framelogin.setLocationRelativeTo(null);

		JPanel loginscreen = new JPanel();
		loginscreen.setLayout(new BorderLayout());
		loginscreen.setPreferredSize(new Dimension(framelogin.getWidth(), framelogin.getHeight()));

		JPanel obenpanel = new JPanel();
		obenpanel.setLayout(new BoxLayout(obenpanel, BoxLayout.Y_AXIS));
		obenpanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout());
		JLabel serverlabel = new JLabel("Server-ip: ");
		final JTextField server = new JTextField(21);
		server.setText("test.mosquitto.org");
		final JTextField port = new JTextField(10);
		port.setText("1883");
		p1.add(serverlabel);
		p1.add(server);
		p1.add(port);

		JPanel p5 = new JPanel();
		p5.setLayout(new FlowLayout());
		final JLabel error = new JLabel();
		error.setForeground(Color.RED);
		p5.add(error);

		final JPanel p6 = new JPanel();
		p6.setLayout(new FlowLayout());
		final JButton btnca = new JButton("ca-cert");
		btnca.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				file1 = showOpenDialog("CHOOSE FILE: " + btnca.getName());
				Controller.getInstance().mqttconnection.cacertfile = file1;
				standardcert = false;
				error.setText("ca-cert wurde ausgewählt!");
			}
		});
		final JButton btnkey = new JButton("key-cert");
		btnkey.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				file2 = showOpenDialog("CHOOSE FILE: " + btnkey.getName());
				Controller.getInstance().mqttconnection.clientkeyfile = file2;
				standardcert = false;
				error.setText("key-cert wurde ausgewählt!");
			}
		});
		final JButton btnclient = new JButton("client-cert");
		btnclient.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				file3 = showOpenDialog("CHOOSE FILE: " + btnclient.getName());
				Controller.getInstance().mqttconnection.clientpemfile = file3;
				error.setText("client-cert wurde ausgewählt!");
				standardcert = false;
			}
		});
		final JButton btnstnd = new JButton("Standart Zertifikate");
		btnstnd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				standardcert = true;
				file1 = new File("C:\\Users\\markus\\Documents\\GitHub\\mqttclient\\mosquitto_certificate\\ca.pem");
				file2 = new File(
						"C:\\Users\\markus\\Documents\\GitHub\\mqttclient\\mosquitto_certificate\\clientkey.pem");
				file3 = new File("C:\\Users\\markus\\Documents\\GitHub\\mqttclient\\mosquitto_certificate\\client.pem");
				Controller.getInstance().mqttconnection.cacertfile = file1;
				Controller.getInstance().mqttconnection.clientkeyfile = file2;
				Controller.getInstance().mqttconnection.clientpemfile = file3;
			}
		});
		p6.add(btnca);
		p6.add(btnkey);
		p6.add(btnclient);
		p6.add(btnstnd);

		p6.setVisible(false);

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout());
		ButtonGroup group = new ButtonGroup();
		final JRadioButton r1 = new JRadioButton("unverschlüsselt");
		r1.setSelected(true);
		r1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p6.setVisible(false);
				file1 = new File("C:\\Users\\markus\\Documents\\GitHub\\mqttclient\\mosquitto_certificate\\ca.pem");
				file2 = new File(
						"C:\\Users\\markus\\Documents\\GitHub\\mqttclient\\mosquitto_certificate\\clientkey.pem");
				file3 = new File("C:\\Users\\markus\\Documents\\GitHub\\mqttclient\\mosquitto_certificate\\client.pem");
				Controller.getInstance().mqttconnection.cacertfile = file1;
				Controller.getInstance().mqttconnection.clientkeyfile = file2;
				Controller.getInstance().mqttconnection.clientpemfile = file3;
			}
		});
//		r1.setSelected(true);
		final JRadioButton r2 = new JRadioButton("verschlüsselt");
		r2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				p6.setVisible(true);

			}
		});
		group.add(r1);
		group.add(r2);

		p2.add(r1);
		p2.add(r2);

		JPanel p3 = new JPanel();
		p3.setLayout(new FlowLayout());
		JLabel usernamelabel = new JLabel("Username");
		final JTextField username = new JTextField(20);
		username.setText("");
		p3.add(usernamelabel);
		p3.add(username);

		JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout());

		JLabel passwordlabel = new JLabel("Passwort");
		final JTextField password = new JTextField(20);
		password.setText("");
		p4.add(passwordlabel);
		p4.add(password);

		obenpanel.add(p1);
		obenpanel.add(p3);
		obenpanel.add(p4);
		obenpanel.add(p2);
		obenpanel.add(p5);
		obenpanel.add(p6);

		JPanel untenpanel = new JPanel();
		JButton btnlogin = new JButton("login");
		btnlogin.setPreferredSize(new Dimension(framelogin.getWidth() - 50, 30));
		btnlogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean a = false;
				error.setText("");
				if (r1.isSelected()) {
						a = Controller.getInstance().mqttconnection.connectionunverschluesselt(server.getText(),
								port.getText(), username.getText(), password.getText());
				}
				if (r2.isSelected()) {
					if (standardcert) {
						error.setText("Es wurden die Standard Zertifikate ausgewählt!");
					}else if (!standardcert && file1 != null &&file2 != null && file3 != null ) {
						error.setText("Es wurden die Zertifikate ausgewählt!");
					}else {
						error.setText("Es wurden keine Zertifikate ausgewählt!");
						return;
					}
					a = Controller.getInstance().mqttconnection.connectionverschluesselt(server.getText(),
								port.getText(), username.getText(), password.getText());
				}
				if (a) {
					frame.setVisible(true);
					framelogin.dispose();
				} else {
					error.setText("Keine Verbindungsaufbau möglich!");
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
		int choice = fileChooser.showOpenDialog(null);
		boolean f = false;
		if (choice == JFileChooser.APPROVE_OPTION) {
			return fileChooser.getSelectedFile();
		}
		return null;
	}

}
