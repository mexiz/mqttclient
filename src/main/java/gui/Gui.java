package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
		framelogin.setSize(500, 500);
		framelogin.setBackground(Color.BLUE);
		JPanel test = new JPanel();
		test.setPreferredSize(new Dimension(500, 500));
		JButton btn = new JButton("login");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(true);
				framelogin.dispose();
				
			}
		});
		framelogin.getContentPane().add(btn);
		

		
	}

}
